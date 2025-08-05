package com.ll.stopnoise.domain.comment.service;

import com.ll.stopnoise.domain.comment.controller.dto.CommentCreateDto;
import com.ll.stopnoise.domain.comment.entity.Comment;
import com.ll.stopnoise.domain.comment.repository.CommentRepository;
import com.ll.stopnoise.domain.customer.service.CustomerService;
import com.ll.stopnoise.domain.post.repository.PostRepository;
import com.ll.stopnoise.domain.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final CustomerService customerService;
    private final PostService postService;

    @Transactional
    public Comment create(CommentCreateDto commentCreateDto) {
        return commentRepository.save(Comment.builder()
                        .content(commentCreateDto.getContent())
                        .customer(customerService.getCustomer(commentCreateDto.getAuthorId()))
                        .post(postService.getPost(commentCreateDto.getPostId()))
                        .build());
    }
}
