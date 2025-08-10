package com.ll.stopnoise.domain.noiseReport.controller.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NoiseReportCreateDto {

    private Integer customerId;

    // YYYY-MM-DD 형식으로 입력 받음
    private String startDate;
    private String endDate;

}
