package com.ll.stopnoise.domain.post.service;

import com.ll.stopnoise.domain.customer.Repository.CustomerRepository;
import com.ll.stopnoise.domain.customer.entity.Customer;
import com.ll.stopnoise.domain.post.controller.dto.PostCreateDto;
import com.ll.stopnoise.domain.post.controller.dto.PostUpdateDto;
import com.ll.stopnoise.domain.post.entity.Post;
import com.ll.stopnoise.domain.post.repository.PostRepository;
import com.ll.stopnoise.domain.s3.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {
    private final CustomerRepository customerRepository;
    private final PostRepository postRepository;
    private final S3Service s3Service;



    @Transactional
    public Post create(MultipartFile image, PostCreateDto postCreateDto) {
        if (!postCreateDto.getCategory().equals("notice") && !postCreateDto.getCategory().equals("community")) {
            throw new IllegalArgumentException("Invalid category");
        }

        Optional<Customer> customer = customerRepository.findById(postCreateDto.getAuthorId());
        if (customer.isEmpty()) {
            throw new IllegalArgumentException("Customer not found");
        }
        String imageURL = null;
        if (postCreateDto.getCategory().equals("community") && !image.isEmpty()) {
            try {
                imageURL = s3Service.uploadFile(image);
            }catch (Exception e){
                throw new IllegalArgumentException("image upload failed");
            }
        }

        Post post = Post.builder()
                .author(customer.get())
                .title(postCreateDto.getTitle())
                .content(postCreateDto.getContent())
                .category(postCreateDto.getCategory())
                .viewCount(0)
                .imageURL(imageURL)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
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
    public Post updatePost(PostUpdateDto dto) {
        Optional<Post> postOptional = postRepository.findById(dto.getId());
        if (postOptional.isEmpty()) {
            throw new IllegalArgumentException("No post found with id " + dto.getId());
        }
        Post postToUpdate = postOptional.get();
        postToUpdate.setTitle(dto.getTitle());
        postToUpdate.setContent(dto.getContent());
        return postRepository.save(postToUpdate);
    }


    @Transactional
    public void deletePost(int id) {
        Optional<Post> postOptional = postRepository.findById(id);
        if (postOptional.isEmpty()) {
            throw new IllegalArgumentException("No post found with id " + id);
        }
        postRepository.deleteById(id);
    }

    public List<Post> getAllByCategory(String category) {
        if (!category.equals("notice") && !category.equals("community")) {
            throw new IllegalArgumentException("Invalid category");
        }
        return postRepository.findByCategory(category);
    }
}
