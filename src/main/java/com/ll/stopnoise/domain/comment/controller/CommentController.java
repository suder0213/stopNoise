package com.ll.stopnoise.domain.comment.controller;

import com.ll.stopnoise.domain.comment.controller.dto.CommentCreateDto;
import com.ll.stopnoise.domain.comment.controller.dto.CommentReadDto;
import com.ll.stopnoise.domain.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comment")
public class CommentController {
    private final CommentService commentService;

    @PostMapping
    public CommentReadDto createComment(@RequestBody CommentCreateDto commentCreateDto) {
        return CommentReadDto.fromDto(commentService.create(commentCreateDto));
    }
}
