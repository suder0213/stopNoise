package com.ll.stopnoise.domain.noiseReport.controller.dto;

import com.ll.stopnoise.domain.customer.entity.Customer;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NoiseReportCreateDto {

    private Customer customer;

    private String analysisSummary;

}
