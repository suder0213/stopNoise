package com.ll.stopnoise.domain.noiseReport.controller.dto;

import com.ll.stopnoise.domain.noiseReport.entity.NoiseReport;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class NoiseReportReadDto {

    private int id;

    private int customerId;

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    private Integer averageNoiseDecibel;
    private Integer maxNoiseDecibel;
    private Integer assumedStress;
    private String maxNoiseType;


    private String staticalAnalyze;
    private String caution;
    private String noiseFeature;
    private String recommendedAction;
    private String hashtag;

    private LocalDateTime createAt;

    public static NoiseReportReadDto from(NoiseReport noiseReport) {

        NoiseReportReadDto noiseReportReadDto = NoiseReportReadDto.builder()
                .id(noiseReport.getId())
                .customerId(noiseReport.getCustomer().getId())
                .startDate(noiseReport.getStartDate())
                .endDate(noiseReport.getEndDate())
                .averageNoiseDecibel(noiseReport.getAverageNoiseDecibel())
                .maxNoiseDecibel(noiseReport.getMaxNoiseDecibel())
                .maxNoiseType(noiseReport.getMaxNoiseType())
                .assumedStress(noiseReport.getAssumedStress())
                .staticalAnalyze(noiseReport.getStaticalAnalyze())
                .caution(noiseReport.getCaution())
                .noiseFeature(noiseReport.getNoiseFeature())
                .recommendedAction(noiseReport.getRecommendedAction())
                .hashtag(noiseReport.getHashtag())
                .createAt(noiseReport.getCreateAt())
                .build();
        return noiseReportReadDto;
    }
}
