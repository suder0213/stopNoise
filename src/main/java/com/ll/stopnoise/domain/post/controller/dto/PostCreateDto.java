package com.ll.stopnoise.domain.post.controller.dto;

import lombok.Getter;

@Getter
public class PostCreateDto {
    private String title;
    private String content;
    private int authorId;
}
