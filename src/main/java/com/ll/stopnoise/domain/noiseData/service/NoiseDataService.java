package com.ll.stopnoise.domain.noiseData.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ll.stopnoise.domain.customer.entity.Customer;
import com.ll.stopnoise.domain.customer.service.CustomerService;
import com.ll.stopnoise.domain.noiseData.controller.dto.NoiseDataCreateDto;
import com.ll.stopnoise.domain.noiseData.controller.dto.NoiseDataDateAndCustomerRequestDto;
import com.ll.stopnoise.domain.noiseData.controller.dto.NoiseDataUpdateDto;
import com.ll.stopnoise.domain.noiseData.entity.NoiseData;
import com.ll.stopnoise.domain.noiseData.repository.NoiseDataRepository;
import com.ll.stopnoise.domain.s3.service.S3Service;
import com.ll.stopnoise.domain.yamNet.service.YAMNetService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NoiseDataService {
    private final NoiseDataRepository noiseDataRepository;
    private final CustomerService customerService;
    private final S3Service s3Service;
    private final YAMNetService yamNetService;
    private final ObjectMapper objectMapper;
    private static final List<String> ALLOWED_AUDIO_TYPES = Arrays.asList(
//            "audio/mpeg",  // .mp3
            "audio/wave"   // .wav
//            "audio/ogg",   // .ogg
//            "audio/webm"   // .webm
    );

    public List<NoiseData> getAll() {
        return noiseDataRepository.findAll();
    }

    public NoiseData getById(int id) {
        Optional<NoiseData> noiseData = noiseDataRepository.findById(id);
        if (noiseData.isEmpty()) {
            throw new IllegalArgumentException("No noise data found with id " + id);
        }
        return noiseData.get();
    }

    public List<NoiseData> getByUploadTimeBetween(LocalDateTime start, LocalDateTime end){
        return noiseDataRepository.findByUploadTimeBetween(start, end);
    }

    @Transactional
    public NoiseData create(NoiseDataCreateDto noiseDataCreateDto) {
        return NoiseData.builder()
                .customer(customerService.getCustomer(noiseDataCreateDto.getCustomerId()))
                .decibelLevel(noiseDataCreateDto.getDecibelLevel())
                .noiseType(noiseDataCreateDto.getNoiseType())
                .memo(noiseDataCreateDto.getMemo())
                .build();
    }
    
    // memo 정도만 수정 가능
    @Transactional
    public NoiseData update(NoiseDataUpdateDto noiseDataUpdateDto) {
        Optional<NoiseData> noiseData = noiseDataRepository.findById(noiseDataUpdateDto.getId());
        if (noiseData.isEmpty()) {
            throw new IllegalArgumentException("No noise data found with id " + noiseDataUpdateDto.getId());
        }
        noiseData.get().setMemo(noiseDataUpdateDto.getMemo());
        return noiseDataRepository.save(noiseData.get());
    }


    @Transactional
    public NoiseData createWithFile(MultipartFile file, String data) {
        String fileUrl;
        NoiseDataCreateDto noiseDataCreateDto;
        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_AUDIO_TYPES.contains(contentType)) {
            throw new IllegalArgumentException("Unsupported content type " + contentType);
        }

        try {
            fileUrl = s3Service.uploadFile(file);
            noiseDataCreateDto = objectMapper.readValue(data, NoiseDataCreateDto.class);
        } catch (Exception e) {
            // 구체적인 예외 메시지를 포함하도록 수정
            throw new IllegalArgumentException("Error occurred while parsing file or uploading" + file.getOriginalFilename(), e);
        }
        // AI가 소리를 분석하여 타입을 결정하는 로직
        String noiseType = yamNetService.analyzeAudio(fileUrl);

        return noiseDataRepository.save(NoiseData.builder()
                .customer(customerService.getCustomer(noiseDataCreateDto.getCustomerId()))
                .decibelLevel(noiseDataCreateDto.getDecibelLevel())
                .noiseType(noiseType)
                .dataFileUrl(fileUrl)
                .memo(noiseDataCreateDto.getMemo())
                .uploadTime(LocalDateTime.now())
                .build());
    }

    @Transactional
    public void deleteWithFile(int id) {
        // 1. DB에서 NoiseData 엔티티 조회
        NoiseData noiseData = noiseDataRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("NoiseData not found with id " + id));

        // 2. S3Service를 통해 파일 삭제
        String fileUrl = noiseData.getDataFileUrl();
        s3Service.deleteFile(fileUrl);

        // 3. DB에서 NoiseData 엔티티 삭제
        noiseDataRepository.delete(noiseData);
    }

    public List<NoiseData> getByCustomerId(int customerId) {
        List<NoiseData> noiseDataList = noiseDataRepository.findByCustomer(
                customerService.getCustomer(customerId)
        );
        return noiseDataList;
    }

    public List<NoiseData> getByCustomerAndUploadTimeBetween(Customer customer, LocalDateTime start, LocalDateTime end) {
        return noiseDataRepository.findByCustomerAndUploadTimeBetween(customer, start, end);
    }

    public List<NoiseData> getByCustomerAndUploadTimeBetween(
            NoiseDataDateAndCustomerRequestDto dto) {
        Customer customer = customerService.getCustomer(dto.getCustomerId());
        LocalDate startDate = LocalDate.parse(dto.getStartDate());
        LocalDate endDate = LocalDate.parse(dto.getEndDate());

        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);
        return noiseDataRepository.findByCustomerAndUploadTimeBetween(customer, startDateTime, endDateTime);
    }
}
