package com.ll.stopnoise.domain.noiseData.service;

import com.ll.stopnoise.domain.noiseData.controller.dto.NoiseDataCreateDto;
import com.ll.stopnoise.domain.noiseData.controller.dto.NoiseDataUpdateDto;
import com.ll.stopnoise.domain.noiseData.entity.NoiseData;
import com.ll.stopnoise.domain.noiseData.repository.NoiseDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NoiseDataService {
    private final NoiseDataRepository noiseDataRepository;

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
                .customer(noiseDataCreateDto.getCustomer())
                .decibelLevel(noiseDataCreateDto.getDecibelLevel())
                .noiseType(noiseDataCreateDto.getNoiseType())
                .dataFileUrl(noiseDataCreateDto.getDataFileUrl())
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
}
