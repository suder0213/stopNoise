package com.ll.stopnoise.domain.noiseReport.controller;

import com.ll.stopnoise.domain.noiseReport.controller.dto.NoiseReportCreateDto;
import com.ll.stopnoise.domain.noiseReport.controller.dto.NoiseReportReadDto;
import com.ll.stopnoise.domain.noiseReport.service.NoiseReportService;
import com.ll.stopnoise.global.RsData;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/noiseReport")
public class NoiseReportController {
    private final NoiseReportService noiseReportService;

    // POST: NoiseReport 생성
    @PostMapping
    public ResponseEntity<RsData<NoiseReportReadDto>> create(@RequestBody NoiseReportCreateDto noiseReportCreateDto) {
        try {
            NoiseReportReadDto dto = NoiseReportReadDto.from(noiseReportService.create(noiseReportCreateDto));
            RsData<NoiseReportReadDto> response = RsData.of("S-1", "리포트가 성공적으로 생성되었습니다.", dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            RsData<NoiseReportReadDto> response = RsData.of("F-1", "리포트 생성 오류", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // GET: 모든 NoiseReport 조회
    @GetMapping
    public ResponseEntity<RsData<List<NoiseReportReadDto>>> getAll() {
        List<NoiseReportReadDto> dtoList = noiseReportService.getAll().stream()
                .map(NoiseReportReadDto::from)
                .collect(Collectors.toList());
        RsData<List<NoiseReportReadDto>> response = RsData.of("S-1", "모든 리포트가 성공적으로 조회되었습니다.", dtoList);
        return ResponseEntity.ok(response);
    }

    // GET: ID로 특정 NoiseReport 조회
    @GetMapping("/{id}")
    public ResponseEntity<RsData<NoiseReportReadDto>> getOne(@PathVariable Integer id) {
        try {
            NoiseReportReadDto dto = NoiseReportReadDto.from(noiseReportService.getById(id));
            RsData<NoiseReportReadDto> response = RsData.of("S-1", "리포트가 성공적으로 조회되었습니다.", dto);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            RsData<NoiseReportReadDto> response = RsData.of("F-1", "해당 ID의 리포트를 찾을 수 없습니다.", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping("/customer/{customer_id}")
    public ResponseEntity<RsData<List<NoiseReportReadDto>>> getByCustomerId(@PathVariable Integer customer_id) {
        // 1. 서비스 레이어에서 고객 존재 여부를 먼저 확인
        //    이 로직은 서비스 단에서 고객이 없으면 예외(e.g., IllegalArgumentException)를 던진다고 가정합니다.
        try {
            List<NoiseReportReadDto> dtoList = noiseReportService.getByCustomerId(customer_id).stream()
                    .map(NoiseReportReadDto::from)
                    .collect(Collectors.toList());

            RsData<List<NoiseReportReadDto>> response = RsData.of("S-1", "해당 고객의 리포트가 성공적으로 조회되었습니다.", dtoList);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            // 2. 고객 ID가 존재하지 않을 경우 404 Not Found 반환
            RsData<List<NoiseReportReadDto>> response = RsData.of("F-1", "해당 ID의 고객을 찾을 수 없습니다.", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    // DELETE: ID로 NoiseReport 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<RsData<String>> delete(@PathVariable Integer id) {
        try {
            noiseReportService.delete(id);
            RsData<String> response = RsData.of("S-1", "리포트가 성공적으로 삭제되었습니다.", null);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            RsData<String> response = RsData.of("F-1", "해당 ID의 리포트를 찾을 수 없습니다.", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}
