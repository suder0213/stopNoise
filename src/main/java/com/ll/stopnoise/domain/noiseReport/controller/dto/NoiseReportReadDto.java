package com.ll.stopnoise.domain.noiseReport.controller.dto;

import com.ll.stopnoise.domain.noiseReport.entity.NoiseReport;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class NoiseReportReadDto {

    private int id;

    private int customerId;

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    private String analysisSummary;

    private LocalDateTime createAt;

    private List<Integer> reportNoiseDataIds;

    public static NoiseReportReadDto from(NoiseReport noiseReport) {
        // 수정된 부분: Stream을 사용하여 간결하게 ID 목록을 생성
        List<Integer> reportNoiseDataIds = noiseReport.getReportNoiseData() != null ?
                noiseReport.getReportNoiseData().stream()
                        .map(reportNoiseData -> reportNoiseData.getId()) // 💡 reportNoiseData.getId()로 수정
                        .collect(Collectors.toList()) : new ArrayList<>();

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
