package com.ll.stopnoise.domain.customer.controller.dto;

import jakarta.persistence.Column;
import lombok.Getter;

@Getter
public class CustomerCreateDto {

    @Column(nullable = false)
    private String name;
    private String dong;
    private String ho;
}
