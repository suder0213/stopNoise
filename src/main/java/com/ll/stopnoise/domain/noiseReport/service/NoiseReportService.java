package com.ll.stopnoise.domain.noiseReport.service;

import com.ll.stopnoise.domain.customer.entity.Customer;
import com.ll.stopnoise.domain.customer.service.CustomerService;
import com.ll.stopnoise.domain.gemini.service.GeminiService;
import com.ll.stopnoise.domain.noiseData.entity.NoiseData;
import com.ll.stopnoise.domain.noiseData.service.NoiseDataService;
import com.ll.stopnoise.domain.noiseReport.controller.dto.NoiseReportCreateDto;
import com.ll.stopnoise.domain.noiseReport.entity.NoiseReport;
import com.ll.stopnoise.domain.noiseReport.repository.NoiseReportRepository;
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

        NoiseReport noiseReport = NoiseReport.builder()
                .customer(customerService.getCustomer(noiseReportCreateDto.getCustomerId()))
                .startDate(startDateTime)
                .endDate(endDateTime)
                .averageNoiseDecibel(Integer.parseInt(summaryParts[0]))
                .maxNoiseDecibel(Integer.parseInt(summaryParts[1]))
                .maxNoiseType(summaryParts[2])
                .assumedStress(Integer.parseInt(summaryParts[3]))
                .staticalAnalyze(summaryParts[4].trim())
                .caution(summaryParts[5].trim())
                .noiseFeature(summaryParts[6].trim())
                .recommendedAction(summaryParts[7].trim())
                .hashtag(summaryParts[8].trim())
                .createAt(LocalDateTime.now())
                .build();
        NoiseReport savedReport = noiseReportRepository.save(noiseReport);



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

    public void deleteAll() {
        if(noiseReportRepository.findAll().isEmpty()) {
            throw new IllegalArgumentException("No NoiseReport found");
        }
        noiseReportRepository.deleteAll();
    }
}
