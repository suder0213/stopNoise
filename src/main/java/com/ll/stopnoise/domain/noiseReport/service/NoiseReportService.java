package com.ll.stopnoise.domain.noiseReport.service;

import com.ll.stopnoise.domain.noiseReport.controller.dto.NoiseReportCreateDto;
import com.ll.stopnoise.domain.noiseReport.entity.NoiseReport;
import com.ll.stopnoise.domain.noiseReport.repository.NoiseReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class NoiseReportService {
    private final NoiseReportRepository noiseReportRepository;

    // TODO
    @Transactional
    public NoiseReport create(NoiseReportCreateDto noiseReportCreateDto) {
        NoiseReport noiseReport = NoiseReport.builder()
                .build();
        return noiseReport;
    }

    public List<NoiseReport> getAll() {
        return noiseReportRepository.findAll();
    }
    public NoiseReport getById(Long id) {
        Optional<NoiseReport> noiseReport = noiseReportRepository.findById(id);
        if (noiseReport.isEmpty()) {
            throw new IllegalArgumentException("No NoiseReport with id " + id + " found");
        }
        return noiseReport.get();
    }
    @Transactional
    public void delete(Long id) {
        Optional<NoiseReport> noiseReport = noiseReportRepository.findById(id);
        if (noiseReport.isEmpty()) {
            throw new IllegalArgumentException("No NoiseReport with id " + id + " found");
        }
        noiseReportRepository.delete(noiseReport.get());
    }
}
