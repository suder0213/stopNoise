package com.ll.stopnoise.domain.noiseReport.controller.dto;

import com.ll.stopnoise.domain.noiseReport.entity.NoiseReport;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class NoiseReportReadDto {

    private int id;

    private int customerId;

    private LocalDate startDate;
    private LocalDate endDate;

    private String analysisSummary;

    private LocalDateTime createAt;

    private List<Integer> reportNoiseDataIds;

    public static NoiseReportReadDto from(NoiseReport noiseReport) {
        List<Integer> reportNoiseDataIds = new ArrayList<>();

        if (noiseReport.getReportNoiseData() != null) {
            noiseReport.getReportNoiseData().forEach(noiseData -> {
                reportNoiseDataIds.add(noiseData.getId());
            });
        }

        NoiseReportReadDto noiseReportReadDto = NoiseReportReadDto.builder()
                .id(noiseReport.getId())
                .customerId(noiseReport.getCustomer().getId())
                .startDate(noiseReport.getStartDate())
                .endDate(noiseReport.getEndDate())
                .analysisSummary(noiseReport.getAnalysisSummary())
                .createAt(noiseReport.getCreateAt())
                .reportNoiseDataIds(reportNoiseDataIds)
                .build();
        return noiseReportReadDto;
    }
}
