package com.ll.stopnoise.domain.post.controller.dto;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class PostCreateDto {

    private int authorId;

    private String title;
    private String content;

    private String category;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private List<Integer> commentsId;
}
