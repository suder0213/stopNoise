package com.ll.stopnoise.domain.comment.controller.dto;


import lombok.Getter;

@Getter
public class CommentCreateDto {
    private int authorId;
    private int postId;
    private String content;
}
