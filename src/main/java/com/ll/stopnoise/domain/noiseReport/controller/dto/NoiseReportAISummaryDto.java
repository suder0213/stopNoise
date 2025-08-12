package com.ll.stopnoise.domain.noiseReport.controller.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NoiseReportAISummaryDto {
    private int average_noise_decibel;

    private int max_noise_decibel;

    private String max_noise_type;

    private int assumedStress;

    private String AIAdvise;
}
