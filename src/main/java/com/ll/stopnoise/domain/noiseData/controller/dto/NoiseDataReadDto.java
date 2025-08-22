package com.ll.stopnoise.domain.noiseData.controller.dto;

import com.ll.stopnoise.domain.noiseData.entity.NoiseData;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class NoiseDataReadDto {

    private int id;

    private Integer customerId;

    private Integer decibelLevel;
    private String noiseType;

    private String memo;

    private LocalDateTime uploadTime;

    private List<Integer> reportNoiseDataIds;

    public static NoiseDataReadDto from(NoiseData noiseData) {
        List<Integer> reportNoiseDataIds_ = new ArrayList<>();
        if (noiseData.getReportNoiseDatas() != null) {
            noiseData.getReportNoiseDatas().forEach(reportNoiseData -> {
                reportNoiseDataIds_.add(reportNoiseData.getId());
            });
        }
        NoiseDataReadDto noiseDataReadDto = NoiseDataReadDto.builder()
                .id(noiseData.getId())
                .customerId(noiseData.getCustomer().getId())
                .decibelLevel(noiseData.getDecibelLevel())
                .noiseType(noiseData.getNoiseType())
                .memo(noiseData.getMemo())
                .uploadTime(noiseData.getUploadTime())
                .reportNoiseDataIds(reportNoiseDataIds_)
                .build();
        return noiseDataReadDto;
    }
}
