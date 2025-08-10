package com.ll.stopnoise.domain.noiseReport.service;

import com.ll.stopnoise.domain.customer.service.CustomerService;
import com.ll.stopnoise.domain.gemini.service.GeminiService;
import com.ll.stopnoise.domain.noiseData.entity.NoiseData;
import com.ll.stopnoise.domain.noiseData.service.NoiseDataService;
import com.ll.stopnoise.domain.noiseReport.controller.dto.NoiseReportCreateDto;
import com.ll.stopnoise.domain.noiseReport.entity.NoiseReport;
import com.ll.stopnoise.domain.noiseReport.repository.NoiseReportRepository;
import com.ll.stopnoise.domain.reportNoiseData.entity.ReportNoiseData;
import com.ll.stopnoise.domain.reportNoiseData.repository.ReportNoiseDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class NoiseReportService {
    private final NoiseReportRepository noiseReportRepository;
    private final CustomerService customerService;
    private final NoiseDataService noiseDataService;
    private final GeminiService geminiService;
    private final ReportNoiseDataRepository reportNoiseDataRepository;

    // TODO
    @Transactional
    public NoiseReport create(NoiseReportCreateDto noiseReportCreateDto) {

        LocalDate startDate = LocalDate.parse(noiseReportCreateDto.getStartDate());
        LocalDate endDate = LocalDate.parse(noiseReportCreateDto.getEndDate());

        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);

        List<NoiseData> noiseDataList = noiseDataService.getByUploadTimeBetween(startDateTime, endDateTime);;
        String analysisSummary = geminiService.noiseReportAnalysis(noiseDataList);
        NoiseReport noiseReport = NoiseReport.builder()
                .customer(customerService.getCustomer(noiseReportCreateDto.getCustomerId()))
                .startDate(startDateTime)
                .endDate(endDateTime)
                .analysisSummary(analysisSummary)
                .createAt(LocalDateTime.now())
                .build();
        NoiseReport savedReport = noiseReportRepository.save(noiseReport);
        // --- 이 부분이 핵심! ---
        // 4. report_noise_data 관계 저장
        // 조회된 각 NoiseData에 대해 중간 테이블 엔티티를 생성하고 저장
        for (NoiseData noiseData : noiseDataList) {
            ReportNoiseData reportNoiseData = new ReportNoiseData();
            reportNoiseData.setNoiseReport(savedReport); // 방금 저장된 리포트와 연결
            reportNoiseData.setNoiseData(noiseData); // 소음 데이터와 연결
            reportNoiseDataRepository.save(reportNoiseData);
        }
        return savedReport;

    }

    public List<NoiseReport> getAll() {
        return noiseReportRepository.findAll();
    }
    public NoiseReport getById(int id) {
        Optional<NoiseReport> noiseReport = noiseReportRepository.findById(id);
        if (noiseReport.isEmpty()) {
            throw new IllegalArgumentException("No NoiseReport with id " + id + " found");
        }
        return noiseReport.get();
    }
    @Transactional
    public void delete(Integer id) {
        Optional<NoiseReport> noiseReport = noiseReportRepository.findById(id);
        if (noiseReport.isEmpty()) {
            throw new IllegalArgumentException("No NoiseReport with id " + id + " found");
        }
        noiseReportRepository.delete(noiseReport.get());
    }
}
