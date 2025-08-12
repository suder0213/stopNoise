package com.ll.stopnoise.domain.noiseData.controller.dto;

import lombok.Getter;

@Getter
public class NoiseDataDateAndCustomerRequestDto {
    private Integer customerId;
    private String startDate;
    private String endDate;
}
