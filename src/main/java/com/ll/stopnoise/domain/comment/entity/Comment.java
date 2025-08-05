package com.ll.stopnoise.domain.comment.entity;

import com.ll.stopnoise.domain.customer.entity.Customer;
import com.ll.stopnoise.domain.post.entity.Post;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String content;

    @NonNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer")
    private Customer customer;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "post")
    private Post post;
}
