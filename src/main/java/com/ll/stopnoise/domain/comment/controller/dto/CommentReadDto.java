package com.ll.stopnoise.domain.comment.controller.dto;

import com.ll.stopnoise.domain.comment.entity.Comment;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommentReadDto {

    private int id;
    private int writerId;
    private int postId;

    public static CommentReadDto from(Comment comment) {
        return CommentReadDto.builder()
                .id(comment.getId())
                .writerId(comment.getCustomer().getId())
                .postId(comment.getPost().getId())
                .build();
    }
}
