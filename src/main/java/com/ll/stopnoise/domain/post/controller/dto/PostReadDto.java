package com.ll.stopnoise.domain.post.controller.dto;

import com.ll.stopnoise.domain.post.entity.Post;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class PostReadDto {
    private int id;
    private String title;
    private String content;
    private int authorId;
    private List<Integer> commentIds;

    public static PostReadDto from(Post post) {
        List<Integer> commentIds = new ArrayList<>();
        if (post.getComments() != null) {
            post.getComments().forEach(comment -> commentIds.add(comment.getId()));
        }
        PostReadDto dto = PostReadDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .authorId(post.getAuthor().getId())
                .commentIds(commentIds)
                .build();
        return dto;
    }
}
