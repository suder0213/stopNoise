package com.ll.stopnoise.domain.post.controller.dto;

import com.ll.stopnoise.domain.post.entity.Post;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class PostReadDto {
    private int id;
    private int authorId;
    private String title;
    private String content;
    private String category;
    private Integer viewCount;
    private String imageUrl;
    private List<Integer> commentIds;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static PostReadDto from(Post post) {
        List<Integer> commentIds = new ArrayList<>();
        if (post.getComments() != null) {
            post.getComments().forEach(comment -> commentIds.add(comment.getId()));
        }
        PostReadDto dto = PostReadDto.builder()
                .id(post.getId())
                .authorId(post.getAuthor().getId())
                .title(post.getTitle())
                .content(post.getContent())
                .category(post.getCategory())
                .viewCount(post.getViewCount())
                .imageUrl(post.getImageURL())
                .commentIds(commentIds)
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();
        return dto;
    }
}
