package com.ll.stopnoise.domain.noiseData.controller.dto;

import lombok.Getter;

@Getter
public class NoiseDataCreateDto {
    private Integer customerId;

    private Integer decibelLevel;
    private String noiseType;

    private String memo;
}
