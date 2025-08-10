package com.ll.stopnoise.domain.noiseData.service;

import com.ll.stopnoise.domain.customer.service.CustomerService;
import com.ll.stopnoise.domain.noiseData.controller.dto.NoiseDataCreateDto;
import com.ll.stopnoise.domain.noiseData.controller.dto.NoiseDataUpdateDto;
import com.ll.stopnoise.domain.noiseData.entity.NoiseData;
import com.ll.stopnoise.domain.noiseData.repository.NoiseDataRepository;
import com.ll.stopnoise.domain.s3.service.S3Service;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NoiseDataService {
    private final NoiseDataRepository noiseDataRepository;
    private final CustomerService customerService;
    private final S3Service s3Service;

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
        return noiseData.get();
    }
    @Transactional
    public void delete(int id) {
        Optional<NoiseData> noiseData = noiseDataRepository.findById(id);
        if(noiseData.isEmpty()) {
            throw new IllegalArgumentException("No noise data found with id " + id);
        }
        noiseDataRepository.deleteById(id);
    }

    @Transactional
    public NoiseData createWithFile(NoiseDataCreateDto noiseDataCreateDto, String fileUrl) {
        return noiseDataRepository.save(NoiseData.builder()
                .customer(customerService.getCustomer(noiseDataCreateDto.getCustomerId()))
                .decibelLevel(noiseDataCreateDto.getDecibelLevel())
                .noiseType(noiseDataCreateDto.getNoiseType())
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
}
