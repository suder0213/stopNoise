package com.ll.stopnoise.domain.comment.controller;

import com.ll.stopnoise.domain.comment.controller.dto.CommentCreateDto;
import com.ll.stopnoise.domain.comment.controller.dto.CommentReadDto;
import com.ll.stopnoise.domain.comment.service.CommentService;
import com.ll.stopnoise.global.RsData;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE})
@RequestMapping("/api/comment")
public class CommentController {
    private final CommentService commentService;

    // POST: 댓글 생성
    @PostMapping
    public ResponseEntity<RsData<CommentReadDto>> create(@RequestBody CommentCreateDto commentCreateDto) {
        try {
            CommentReadDto dto = CommentReadDto.from(commentService.create(commentCreateDto));
            RsData<CommentReadDto> response = RsData.of("S-1", "댓글이 성공적으로 생성되었습니다.", dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            // 존재하지 않는 게시글 ID 또는 고객 ID로 댓글을 생성하려 할 때 404 Not Found 반환
            RsData<CommentReadDto> response = RsData.of("F-1", "댓글을 작성할 게시글 또는 고객을 찾을 수 없습니다.", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    // GET: 모든 댓글 조회
    @GetMapping
    public ResponseEntity<RsData<List<CommentReadDto>>> getAll() {
        List<CommentReadDto> dtoList = commentService.getAll().stream()
                .map(CommentReadDto::from)
                .collect(Collectors.toList());
        RsData<List<CommentReadDto>> response = RsData.of("S-1", "모든 댓글이 성공적으로 조회되었습니다.", dtoList);
        return ResponseEntity.ok(response);
    }

    // GET: 특정 댓글 조회
    @GetMapping("/{id}")
    public ResponseEntity<RsData<CommentReadDto>> get(@PathVariable int id) {
        try {
            CommentReadDto dto = CommentReadDto.from(commentService.getById(id));
            RsData<CommentReadDto> response = RsData.of("S-1", "댓글이 성공적으로 조회되었습니다.", dto);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            RsData<CommentReadDto> response = RsData.of("F-1", "해당 ID의 댓글을 찾을 수 없습니다.", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping("/post/{id}")
    public ResponseEntity<RsData<List<CommentReadDto>>> getByPostId(@PathVariable int id) {
        try {
            List<CommentReadDto> dtoList = commentService.getByPostId(id).stream()
                    .map(CommentReadDto::from)
                    .collect(Collectors.toList());

            // 게시글이 존재하지만 댓글이 없는 경우, 빈 리스트와 함께 200 OK 반환
            RsData<List<CommentReadDto>> response = RsData.of("S-1", "해당 게시글의 댓글이 성공적으로 조회되었습니다.", dtoList);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            // 게시글 자체가 존재하지 않는 경우, 404 Not Found 반환
            RsData<List<CommentReadDto>> response = RsData.of("F-1", "해당 ID의 게시글을 찾을 수 없습니다.", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }


    // DELETE: 댓글 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<RsData<String>> delete(@PathVariable int id) {
        try {
            commentService.delete(id);
            RsData<String> response = RsData.of("S-1", "댓글이 성공적으로 삭제되었습니다.", null);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            RsData<String> response = RsData.of("F-1", "해당 ID의 댓글을 찾을 수 없습니다.", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}
