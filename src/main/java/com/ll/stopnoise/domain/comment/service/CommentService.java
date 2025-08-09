package com.ll.stopnoise.domain.comment.service;

import com.ll.stopnoise.domain.comment.controller.dto.CommentCreateDto;
import com.ll.stopnoise.domain.comment.entity.Comment;
import com.ll.stopnoise.domain.comment.repository.CommentRepository;
import com.ll.stopnoise.domain.customer.service.CustomerService;
import com.ll.stopnoise.domain.post.entity.Post;
import com.ll.stopnoise.domain.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final CustomerService customerService;
    private final PostService postService;

    @Transactional
    public Comment create(CommentCreateDto commentCreateDto) {
        return commentRepository.save(Comment.builder()
                        .customer(customerService.getCustomer(commentCreateDto.getAuthorId()))
                        .post(postService.getPost(commentCreateDto.getPostId()))
                        .content(commentCreateDto.getContent())
                        .createdAt(LocalDateTime.now())
                        .build());
    }

    public Comment getById(int id) {
        Optional<Comment> comment = commentRepository.findById(id);
        if (comment.isEmpty()) {
            throw new IllegalArgumentException("comment not found");
        }
        return comment.get();
    }

    public List<Comment> getAll(){
        return commentRepository.findAll();
    }

    @Transactional
    public void delete(int id) {
        Optional<Comment> comment = commentRepository.findById(id);
        if (comment.isEmpty()) {
            throw new IllegalArgumentException("comment not found");
        }
        commentRepository.deleteById(id);
    }

    public List<Comment> getByPostId(int id) {
        Post targetPost = postService.getPost(id);
        return targetPost.getComments();
    }
}
