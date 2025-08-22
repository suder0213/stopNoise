package com.ll.stopnoise.domain.noiseReport.service;

import com.ll.stopnoise.domain.customer.entity.Customer;
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

        Customer customer = customerService.getCustomer(noiseReportCreateDto.getCustomerId());

        LocalDate startDate = LocalDate.parse(noiseReportCreateDto.getStartDate());
        LocalDate endDate = LocalDate.parse(noiseReportCreateDto.getEndDate());

        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);

        List<NoiseData> noiseDataList = noiseDataService.getByCustomerAndUploadTimeBetween(customer, startDateTime, endDateTime);
        String analysisSummary = geminiService.noiseReportAnalysis(noiseDataList);
        // 💡 Gemini 응답을 슬래시 기준으로 분리
        String[] summaryParts = analysisSummary.split("/");
        if (summaryParts[2].equals("데이터 없음")) {
            throw new IllegalArgumentException("데이터 없음");
        }

        NoiseReport noiseReport = NoiseReport.builder()
                .customer(customerService.getCustomer(noiseReportCreateDto.getCustomerId()))
                .startDate(startDateTime)
                .endDate(endDateTime)
                .averageNoiseDecibel(Integer.parseInt(summaryParts[0]))
                .maxNoiseDecibel(Integer.parseInt(summaryParts[1]))
                .maxNoiseType(summaryParts[2])
                .assumedStress(Integer.parseInt(summaryParts[3]))
                .AIAdvise(summaryParts[4])
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

    public List<NoiseReport> getByCustomerId(Integer customerId) {
        Customer customer = customerService.getCustomer(customerId);
        return noiseReportRepository.findByCustomer(customer);
    }
}
