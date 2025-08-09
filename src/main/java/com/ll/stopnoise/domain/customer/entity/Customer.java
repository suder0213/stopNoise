package com.ll.stopnoise.domain.customer.entity;


import com.ll.stopnoise.domain.post.entity.Post;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Getter
@Setter
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false)
    private String name;

    private String dong;
    private String ho;

    @OneToMany(mappedBy = "author")
    private List<Post> posts;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
