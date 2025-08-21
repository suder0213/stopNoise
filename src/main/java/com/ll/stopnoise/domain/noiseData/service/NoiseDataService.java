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

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.File;
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
            // 스테레오를 모노로 바꾸는 로직
            // 1. MultipartFile을 File 객체로 변환
            File tempFile = File.createTempFile("audio", ".wav");
            file.transferTo(tempFile);

            // 2. 오디오 파일 포맷 정보 확인
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(tempFile);
            AudioFormat format = audioInputStream.getFormat();

            // 파일의 원래 채널 수 출력
            int originalChannels = format.getChannels();
            System.out.println("원본 오디오 파일의 채널 수: " + originalChannels);

            File fileToUpload = tempFile; // 기본값은 원본 파일

            // 3. 채널 수가 2 (스테레오)인 경우 모노로 변환
            if (format.getChannels() != 1) {
                System.out.println("스테레오 파일입니다. 모노로 변환을 시작합니다.");

                AudioFormat monoFormat = new AudioFormat(
                        format.getEncoding(),
                        format.getSampleRate(),
                        format.getSampleSizeInBits(),
                        1, // 채널을 1로 설정
                        format.getFrameSize() / 2,
                        format.getFrameRate(),
                        format.isBigEndian()
                );

                AudioInputStream monoStream = AudioSystem.getAudioInputStream(monoFormat, audioInputStream);
                File monoFile = File.createTempFile("mono_audio", ".wav");
                AudioSystem.write(monoStream, AudioFileFormat.Type.WAVE, monoFile);

                fileToUpload = monoFile; // 업로드할 파일을 변환된 모노 파일로 지정

                monoStream.close();
                monoFile.deleteOnExit(); // 애플리케이션 종료 시 임시 파일 삭제
            } else {
                System.out.println("모노 파일이거나 지원되지 않는 채널 수입니다.");
            }

            // 4. 변환된(혹은 원본) 파일을 S3에 업로드
            fileUrl = s3Service.uploadFile(fileToUpload);
            noiseDataCreateDto = objectMapper.readValue(data, NoiseDataCreateDto.class);

            // 5. 임시 파일 정리
            audioInputStream.close();
            tempFile.delete(); // 원본 임시 파일 삭제
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
