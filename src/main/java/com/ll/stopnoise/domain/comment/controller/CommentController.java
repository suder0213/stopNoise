package com.ll.stopnoise.domain.comment.controller;

import com.ll.stopnoise.domain.comment.controller.dto.CommentCreateDto;
import com.ll.stopnoise.domain.comment.controller.dto.CommentReadDto;
import com.ll.stopnoise.domain.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comment")
public class CommentController {
    private final CommentService commentService;

    @PostMapping
    public CommentReadDto create(@RequestBody CommentCreateDto commentCreateDto) {
        return CommentReadDto.from(commentService.create(commentCreateDto));
    }

    @GetMapping
    public List<CommentReadDto> getAll() {
        return commentService.getAll().stream().map(CommentReadDto::from).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public CommentReadDto get(@PathVariable int id) {
        return CommentReadDto.from(commentService.getById(id));
    }


    @GetMapping("/post/{id}")
    public List<CommentReadDto> getByPostId(@PathVariable int id) {
        return commentService.getByPostId(id).stream().map(CommentReadDto::from).collect(Collectors.toList());
    }

    // 수정 기능 X
//    @PutMapping
//    public CommentReadDto updateComment(@RequestBody CustomerUpdateDto customerUpdateDto) {
//        return CommentReadDto.from(commentService.update(customerUpdateDto));
//    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable int id) {
        commentService.delete(id);
        return "Comment %d deleted".formatted(id);
    }
}
