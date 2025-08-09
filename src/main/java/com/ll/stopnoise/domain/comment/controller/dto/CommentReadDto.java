package com.ll.stopnoise.domain.comment.controller.dto;

import com.ll.stopnoise.domain.comment.entity.Comment;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class CommentReadDto {

    private int id;
    private int authorId;
    private int postId;
    private String content;
    private LocalDateTime createTime;

    public static CommentReadDto from(Comment comment) {
        return CommentReadDto.builder()
                .id(comment.getId())
                .authorId(comment.getCustomer().getId())
                .postId(comment.getPost().getId())
                .content(comment.getContent())
                .createTime(comment.getCreatedAt())
                .build();
    }
}
