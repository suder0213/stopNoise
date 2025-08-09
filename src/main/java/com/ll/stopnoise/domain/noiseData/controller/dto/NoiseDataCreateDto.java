package com.ll.stopnoise.domain.noiseData.controller.dto;

import com.ll.stopnoise.domain.customer.entity.Customer;
import lombok.Getter;

@Getter
public class NoiseDataCreateDto {
    private Customer customer;

    private Integer decibelLevel;
    private String noiseType;

    private String dataFileUrl;

    private String memo;
}
