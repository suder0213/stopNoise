package com.ll.stopnoise.domain.noiseData.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.ll.stopnoise.domain.noiseData.controller.dto.NoiseDataCreateDto;
import com.ll.stopnoise.domain.noiseData.controller.dto.NoiseDataDateAndCustomerRequestDto;
import com.ll.stopnoise.domain.noiseData.controller.dto.NoiseDataReadDto;
import com.ll.stopnoise.domain.noiseData.controller.dto.NoiseDataUpdateDto;
import com.ll.stopnoise.domain.noiseData.entity.NoiseData;
import com.ll.stopnoise.domain.noiseData.service.NoiseDataService;
import com.ll.stopnoise.domain.s3.service.S3Service;
import com.ll.stopnoise.global.RsData;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
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
    private final ObjectMapper objectMapper;
    private final S3Service s3Service;

    // POST: 파일과 데이터를 함께 업로드하여 NoiseData 생성
    @PostMapping("/upload")
    public ResponseEntity<RsData<NoiseDataReadDto>> uploadNoiseData(
            @RequestParam("file") MultipartFile file,
            @RequestParam("data") String data
    ) {
        try {
            String fileUrl = s3Service.uploadFile(file);
            NoiseDataCreateDto noiseDataCreateDto = objectMapper.readValue(data, NoiseDataCreateDto.class);
            NoiseData uploadedNoiseData = noiseDataService.createWithFile(noiseDataCreateDto, fileUrl, file);
            NoiseDataReadDto dto = NoiseDataReadDto.from(uploadedNoiseData);

            RsData<NoiseDataReadDto> response = RsData.of("S-1", "파일 업로드 및 NoiseData 생성이 성공했습니다.", dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            // 구체적인 예외 메시지를 포함하도록 수정
            RsData<NoiseDataReadDto> response = RsData.of("F-1", "파일 업로드 또는 데이터 처리 중 오류가 발생했습니다: " + e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    // GET: 모든 NoiseData 조회
    @GetMapping
    public ResponseEntity<RsData<List<NoiseDataReadDto>>> getAllNoiseData() {
        List<NoiseDataReadDto> dtoList = noiseDataService.getAll().stream()
                .map(NoiseDataReadDto::from)
                .collect(Collectors.toList());

        RsData<List<NoiseDataReadDto>> response = RsData.of("S-1", "성공적으로 조회됨", dtoList);
        return ResponseEntity.ok(response);
    }
    // GET: ID로 특정 NoiseData 조회
    @GetMapping("/{id}")
    public ResponseEntity<RsData<NoiseDataReadDto>> getNoiseDataById(@PathVariable int id) {
        try {
            NoiseData noiseData = noiseDataService.getById(id);
            NoiseDataReadDto dto = NoiseDataReadDto.from(noiseData);
            RsData<NoiseDataReadDto> response = RsData.of("S-1", "성공적으로 조회됨", dto);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            RsData<NoiseDataReadDto> response = RsData.of("F-1", "해당 ID의 NoiseData를 찾을 수 없음", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
    // GET: 고객 ID로 모든 NoiseData 조회
    @GetMapping("/customer/{customer_id}")
    public ResponseEntity<RsData<List<NoiseDataReadDto>>> getNoiseDataByCustomerId(@PathVariable int customer_id) {
        List<NoiseDataReadDto> dtoList = noiseDataService.getByCustomerId(customer_id).stream()
                .map(NoiseDataReadDto::from)
                .collect(Collectors.toList());

        RsData<List<NoiseDataReadDto>> response = RsData.of("S-1", "성공적으로 조회됨", dtoList);
        return ResponseEntity.ok(response);
    }
    // GET: 고객 ID와 특정 기간으로 NoiseData 조회
    @GetMapping("/date")
    public ResponseEntity<RsData<List<NoiseDataReadDto>>> getNoiseDataByDateAndCustomerId(
            @RequestBody NoiseDataDateAndCustomerRequestDto dto){
        List<NoiseDataReadDto> dtoList = noiseDataService.getByCustomerAndUploadTimeBetween(dto)
                .stream().map(NoiseDataReadDto::from)
                .collect(Collectors.toList());

        RsData<List<NoiseDataReadDto>> response = RsData.of("S-1", "성공적으로 조회됨", dtoList);
        return ResponseEntity.ok(response);
    }

    // PUT: NoiseData 수정
    @PutMapping
    public ResponseEntity<RsData<NoiseDataReadDto>> updateNoiseData(@RequestBody NoiseDataUpdateDto noiseDataUpdateDto) {
        try {
            NoiseData updatedNoiseData = noiseDataService.update(noiseDataUpdateDto);
            NoiseDataReadDto dto = NoiseDataReadDto.from(updatedNoiseData);
            RsData<NoiseDataReadDto> response = RsData.of("S-1", "성공적으로 수정됨", dto);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            RsData<NoiseDataReadDto> response = RsData.of("F-1", "해당 ID의 NoiseData를 찾을 수 없음", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    // DELETE: ID로 파일과 함께 NoiseData 삭제
    @DeleteMapping("/file/{id}")
    public ResponseEntity<RsData<String>> deleteNoiseDataWithFile(@PathVariable int id) {
        try {
            noiseDataService.deleteWithFile(id);
            RsData<String> response = RsData.of("S-1", "성공적으로 삭제됨", null);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            RsData<String> response = RsData.of("F-1", "해당 ID의 NoiseData를 찾을 수 없음", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }



}
