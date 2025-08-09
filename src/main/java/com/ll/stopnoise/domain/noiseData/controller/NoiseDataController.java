package com.ll.stopnoise.domain.noiseData.controller;


import com.ll.stopnoise.domain.noiseData.controller.dto.NoiseDataCreateDto;
import com.ll.stopnoise.domain.noiseData.controller.dto.NoiseDataReadDto;
import com.ll.stopnoise.domain.noiseData.controller.dto.NoiseDataUpdateDto;
import com.ll.stopnoise.domain.noiseData.service.NoiseDataService;
import com.ll.stopnoise.domain.s3.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RestController
@RequestMapping("/api/noiseData")
@RequiredArgsConstructor
public class NoiseDataController {
    private final NoiseDataService noiseDataService;
    private final S3Service s3Service;

    @GetMapping
    public List<NoiseDataReadDto> getAllNoiseData() {
        return noiseDataService.getAll().stream().map(NoiseDataReadDto::from).collect(Collectors.toList());
    }
    @GetMapping("/{id}")
    public NoiseDataReadDto getNoiseDataById(@PathVariable int id) {
        return NoiseDataReadDto.from(noiseDataService.getById(id));
    }
    @PostMapping
    public NoiseDataReadDto createNoiseData(@RequestBody NoiseDataCreateDto noiseDataCreateDto) {
        return NoiseDataReadDto.from(noiseDataService.create(noiseDataCreateDto));
    }
    @PutMapping
    public NoiseDataReadDto updateNoiseData(@RequestBody NoiseDataUpdateDto noiseDataUpdateDto) {
        return NoiseDataReadDto.from(noiseDataService.update(noiseDataUpdateDto));
    }
    @DeleteMapping
    public String deleteNoiseDataById(@RequestParam int id) {
        noiseDataService.delete(id);
        return "NoiseData %d deleted".formatted(id);
    }
    @Transactional
    @PostMapping("/upload")
    public ResponseEntity<String> uploadNoiseData(@RequestParam("file") MultipartFile file) {
        try {
            String fileUrl = s3Service.uploadFile(file);

            // TODO: fileUrl과 다른 데이터를 조합하여 NoiseData 엔티티 저장
            // 예: noiseDataService.save(customer, fileUrl);

            return ResponseEntity.ok("파일 업로드 성공: " + fileUrl);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("파일 업로드 실패: " + e.getMessage());
        }
    }

}
