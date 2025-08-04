package com.ll.stopnoise.domain.post.controller;

import com.ll.stopnoise.domain.post.controller.dto.PostCreateDto;
import com.ll.stopnoise.domain.post.entity.Post;
import com.ll.stopnoise.domain.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/post")
public class PostController {
    private final PostService postService;

    @PostMapping
    public Post create(@RequestBody PostCreateDto postCreateDto) {
        return postService.create(postCreateDto);
    }
    @GetMapping("/{id}")
    public Post get(@PathVariable int id) {
        return postService.getPost(id);
    }
    @GetMapping
    public List<Post> getAll() {
        return postService.getAllPost();
    }

    @PutMapping
    public Post update(@RequestBody Post post){
        return postService.updatePost(post);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        postService.deletePost(id);
    }
}
