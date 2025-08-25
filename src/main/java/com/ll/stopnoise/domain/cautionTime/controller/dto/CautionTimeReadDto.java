package com.ll.stopnoise.domain.cautionTime.controller.dto;

import com.ll.stopnoise.domain.cautionTime.entity.CautionTime;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class CautionTimeReadDto {
    private int customerId;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;

    public static CautionTimeReadDto from(CautionTime cautionTime) {
        CautionTimeReadDto dto = CautionTimeReadDto.builder()
                .customerId(cautionTime.getId())
                .startDateTime(cautionTime.getStartDateTime())
                .endDateTime(cautionTime.getEndDateTime())
                .build();
        return dto;
    }
}
