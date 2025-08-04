package com.ll.stopnoise.domain.post.service;

import com.ll.stopnoise.domain.customer.Repository.CustomerRepository;
import com.ll.stopnoise.domain.customer.entity.Customer;
import com.ll.stopnoise.domain.post.controller.dto.PostCreateDto;
import com.ll.stopnoise.domain.post.entity.Post;
import com.ll.stopnoise.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {
    private final CustomerRepository customerRepository;
    private final PostRepository postRepository;

    @Transactional
    public Post create(PostCreateDto postCreateDto) {

        Optional<Customer> customer = customerRepository.findById(postCreateDto.getAuthorId());
        if (customer.isEmpty()) {
            throw new IllegalArgumentException("Customer not found");
        }

        Post post = Post.builder()
                .title(postCreateDto.getTitle())
                .content(postCreateDto.getContent())
                .author(customerRepository.findById(postCreateDto.getAuthorId()).orElse(null))
                .build();
        return postRepository.save(post);
    }

    public Post getPost(int id) {
        Optional<Post> post = postRepository.findById(id);
        if (post.isEmpty()) {
            throw new IllegalArgumentException("No post found with id " + id);
        }
        return post.get();
    }

    public List<Post> getAllPost() {
        return postRepository.findAll();
    }

    @Transactional
    public Post updatePost(Post post) {
        Optional<Post> postOptional = postRepository.findById(post.getId());
        if (postOptional.isEmpty()) {
            throw new IllegalArgumentException("No post found with id " + post.getId());
        }
        Post postToUpdate = postOptional.get();
        postToUpdate.setTitle(post.getTitle());
        postToUpdate.setContent(post.getContent());
        return postRepository.save(postToUpdate);
    }


    public void deletePost(int id) {
        Optional<Post> postOptional = postRepository.findById(id);
        if (postOptional.isEmpty()) {
            throw new IllegalArgumentException("No post found with id " + id);
        }
        postRepository.deleteById(id);
    }
}
