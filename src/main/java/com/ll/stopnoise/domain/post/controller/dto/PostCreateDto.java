package com.ll.stopnoise.domain.post.controller.dto;

import lombok.Getter;

@Getter
public class PostCreateDto {

    private int authorId;

    private String title;
    private String content;

    private String category;
}
