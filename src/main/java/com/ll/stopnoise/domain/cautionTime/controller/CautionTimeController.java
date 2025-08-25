package com.ll.stopnoise.domain.cautionTime.controller;

import com.ll.stopnoise.domain.cautionTime.controller.dto.CautionTimeCreateDto;
import com.ll.stopnoise.domain.cautionTime.controller.dto.CautionTimeReadDto;
import com.ll.stopnoise.domain.cautionTime.entity.CautionTime;
import com.ll.stopnoise.domain.cautionTime.service.CautionTimeService;
import com.ll.stopnoise.domain.post.controller.dto.PostReadDto;
import com.ll.stopnoise.domain.post.controller.dto.PostUpdateDto;
import com.ll.stopnoise.global.RsData;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class CautionTimeController {

    private final CautionTimeService cautionTimeService;

    // POST: 게시글 생성
    @PostMapping
    public ResponseEntity<RsData<CautionTimeReadDto>> create(CautionTimeCreateDto createDto) {

        try {
            CautionTimeReadDto dto = CautionTimeReadDto.from(cautionTimeService.create(createDto));
            RsData<CautionTimeReadDto> response = RsData.of("S-1", "게시글이 성공적으로 생성되었습니다.", dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }catch (IllegalArgumentException e) {
            RsData<CautionTimeReadDto> response = RsData.of("F-1", "유효하지 않은 게시물 양식입니다.", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    // GET: 모든 게시글 조회
    @GetMapping
    public ResponseEntity<RsData<List<CautionTimeReadDto>>> getAll() {
        List<CautionTimeReadDto> dtoList = cautionTimeService.getAllCautionTime().stream()
                .map(CautionTimeReadDto::from)
                .collect(Collectors.toList());
        RsData<List<CautionTimeReadDto>> response = RsData.of("S-1", "모든 게시글이 성공적으로 조회되었습니다.", dtoList);
        return ResponseEntity.ok(response);
    }

    // GET: 특정 일자에 해당하는 주의 시간 조회
    @GetMapping("/date")
    public ResponseEntity<RsData<List<CautionTimeReadDto>>> getAllByCategory(@RequestBody String startDateTime,
                                                                             @RequestBody String endDateTime) {
        try {
            List<CautionTimeReadDto> dtoList = cautionTimeService.getAllByTime(startDateTime, endDateTime).stream()
                    .map(CautionTimeReadDto::from)
                    .collect(Collectors.toList());
            RsData<List<CautionTimeReadDto>> response = RsData.of("S-1", "모든 게시글이 성공적으로 조회되었습니다.", dtoList);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            RsData<List<CautionTimeReadDto>> response = RsData.of("F-1", "유효하지 않은 게시물 양식입니다.", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
    // DELETE: 게시글 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<RsData<String>> delete(@PathVariable int id) {
        try {
            cautionTimeService.delete(id);
            RsData<String> response = RsData.of("S-1", "게시글이 성공적으로 삭제되었습니다.", null);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            RsData<String> response = RsData.of("F-1", "해당 ID의 게시글을 찾을 수 없습니다.", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }


}
