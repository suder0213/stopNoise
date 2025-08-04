package com.ll.stopnoise.domain.post.entity;

import com.ll.stopnoise.domain.comment.entity.Comment;
import com.ll.stopnoise.domain.customer.entity.Customer;
import jakarta.persistence.*;
import lombok.*;

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
    private String title;
    private String content;

    @ManyToOne
    @NonNull
    @JoinColumn(name = "author")
    private Customer author;

    @OneToMany(mappedBy = "post")
    private List<Comment> comments;
}
