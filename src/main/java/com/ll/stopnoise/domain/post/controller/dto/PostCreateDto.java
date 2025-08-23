package com.ll.stopnoise.domain.post.controller.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostCreateDto {

    private int customerId;

    private String title;
    private String content;

    private String category;
}
