package com.ll.stopnoise.domain.customer.controller.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CustomerUpdateDto {
    private int id;
    private String name;
}
