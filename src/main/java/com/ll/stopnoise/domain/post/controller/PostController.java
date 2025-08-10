package com.ll.stopnoise.domain.post.controller;

import com.ll.stopnoise.domain.post.controller.dto.PostCreateDto;
import com.ll.stopnoise.domain.post.controller.dto.PostReadDto;
import com.ll.stopnoise.domain.post.entity.Post;
import com.ll.stopnoise.domain.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/post")
public class PostController {
    private final PostService postService;

    @PostMapping
    public PostReadDto create(@RequestBody PostCreateDto postCreateDto) {
        return PostReadDto.from(postService.create(postCreateDto));
    }
    @GetMapping
    public List<PostReadDto> getAll() {
        return postService.getAllPost().stream().map(PostReadDto::from).collect(Collectors.toList());
    }
    @GetMapping("/{id}")
    public PostReadDto get(@PathVariable int id) {
        return PostReadDto.from(postService.getPost(id));
    }

    @PutMapping
    public PostReadDto update(@RequestBody Post post){
        return PostReadDto.from(postService.updatePost(post));
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable int id) {
        postService.deletePost(id);
        return "Post %d deleted".formatted(id);
    }
}
