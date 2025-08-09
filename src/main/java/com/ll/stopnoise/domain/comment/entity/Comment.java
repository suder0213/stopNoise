package com.ll.stopnoise.domain.comment.entity;

import com.ll.stopnoise.domain.customer.entity.Customer;
import com.ll.stopnoise.domain.post.entity.Post;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(columnDefinition = "TEXT",nullable = false)
    private String content;

    private LocalDateTime createdAt;
}
