package com.ll.stopnoise.domain.post.entity;

import com.ll.stopnoise.domain.comment.entity.Comment;
import com.ll.stopnoise.domain.customer.entity.Customer;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "author", nullable = false)
    private Customer author;

    private String title;

    @Column(columnDefinition = "TEXT",nullable = false)
    private String content;

    @Column(nullable = false)
    private String category;

    private Integer viewCount;

    @Column(nullable = true)
    private String imageURL;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "post")
    private List<Comment> comments;

}
