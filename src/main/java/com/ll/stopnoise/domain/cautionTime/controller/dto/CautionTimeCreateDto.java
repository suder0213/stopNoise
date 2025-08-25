package com.ll.stopnoise.domain.cautionTime.controller.dto;

import lombok.Getter;

@Getter
public class CautionTimeCreateDto {
    private int customerId;
    private String memo;
    private String startDateTime;
    private String endDateTime;
}
