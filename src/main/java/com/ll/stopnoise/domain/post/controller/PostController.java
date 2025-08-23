package com.ll.stopnoise.domain.post.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ll.stopnoise.domain.post.controller.dto.PostCreateDto;
import com.ll.stopnoise.domain.post.controller.dto.PostReadDto;
import com.ll.stopnoise.domain.post.controller.dto.PostUpdateDto;
import com.ll.stopnoise.domain.post.service.PostService;
import com.ll.stopnoise.global.RsData;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/post")
public class PostController {
    private final PostService postService;
    private final ObjectMapper objectMapper;

    // POST: 게시글 생성
    @PostMapping
    public ResponseEntity<RsData<PostReadDto>> create(@RequestParam(value = "image", required = false) MultipartFile image,
                                                      @RequestParam int customerId,
                                                      @RequestParam(required = false) String title,
                                                      @RequestParam String content,
                                                      @RequestParam String category) {

        PostCreateDto postCreateDto = PostCreateDto.builder()
                .customerId(customerId)
                .title(title)
                .content(content)
                .category(category)
                .build();
        try {
            PostReadDto dto = PostReadDto.from(postService.create(image, postCreateDto));
            RsData<PostReadDto> response = RsData.of("S-1", "게시글이 성공적으로 생성되었습니다.", dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }catch (IllegalArgumentException e) {
            RsData<PostReadDto> response = RsData.of("F-1", "유효하지 않은 게시물 양식입니다.", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    // GET: 모든 게시글 조회
    @GetMapping
    public ResponseEntity<RsData<List<PostReadDto>>> getAll() {
        List<PostReadDto> dtoList = postService.getAllPost().stream()
                .map(PostReadDto::from)
                .collect(Collectors.toList());
        RsData<List<PostReadDto>> response = RsData.of("S-1", "모든 게시글이 성공적으로 조회되었습니다.", dtoList);
        return ResponseEntity.ok(response);
    }

    // GET: 특정 카테고리에 해당하는 글 조회
    @GetMapping("/category/{category}")
    public ResponseEntity<RsData<List<PostReadDto>>> getAllByCategory(@PathVariable String category) {
        try {
            List<PostReadDto> dtoList = postService.getAllByCategory(category).stream()
                    .map(PostReadDto::from)
                    .collect(Collectors.toList());
            RsData<List<PostReadDto>> response = RsData.of("S-1", "모든 게시글이 성공적으로 조회되었습니다.", dtoList);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            RsData<List<PostReadDto>> response = RsData.of("F-1", "유효하지 않은 게시물 양식입니다.", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    // GET: 특정 게시글 조회
    @GetMapping("/{id}")
    public ResponseEntity<RsData<PostReadDto>> get(@PathVariable int id) {
        try {
            PostReadDto dto = PostReadDto.from(postService.getPost(id));
            RsData<PostReadDto> response = RsData.of("S-1", "게시글이 성공적으로 조회되었습니다.", dto);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            RsData<PostReadDto> response = RsData.of("F-1", "해당 ID의 게시글을 찾을 수 없습니다.", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    // PUT: 게시글 수정
    @PutMapping
    public ResponseEntity<RsData<PostReadDto>> update(@RequestBody PostUpdateDto postUpdateDto) {
        try {
            PostReadDto dto = PostReadDto.from(postService.updatePost(postUpdateDto));
            RsData<PostReadDto> response = RsData.of("S-1", "게시글이 성공적으로 수정되었습니다.", dto);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            RsData<PostReadDto> response = RsData.of("F-1", "해당 ID의 게시글을 찾을 수 없습니다.", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    // DELETE: 게시글 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<RsData<String>> delete(@PathVariable int id) {
        try {
            postService.deletePost(id);
            RsData<String> response = RsData.of("S-1", "게시글이 성공적으로 삭제되었습니다.", null);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            RsData<String> response = RsData.of("F-1", "해당 ID의 게시글을 찾을 수 없습니다.", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}
