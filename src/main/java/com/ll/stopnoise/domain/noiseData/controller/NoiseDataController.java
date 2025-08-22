package com.ll.stopnoise.domain.noiseData.controller;


import com.ll.stopnoise.domain.noiseData.controller.dto.NoiseDataDateAndCustomerRequestDto;
import com.ll.stopnoise.domain.noiseData.controller.dto.NoiseDataReadDto;
import com.ll.stopnoise.domain.noiseData.controller.dto.NoiseDataUpdateDto;
import com.ll.stopnoise.domain.noiseData.entity.NoiseData;
import com.ll.stopnoise.domain.noiseData.service.NoiseDataService;
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
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/noiseData")
@RequiredArgsConstructor
public class NoiseDataController {
    private final NoiseDataService noiseDataService;
    // 허용할 오디오 파일 형식 리스트

    // POST: 파일과 데이터를 함께 업로드하여 NoiseData 생성
    @PostMapping("/upload")
    public ResponseEntity<RsData<NoiseDataReadDto>> uploadNoiseData(
            @RequestParam("file") MultipartFile file,
            @RequestParam("data") String data
    ) {
        try {
            NoiseDataReadDto dto = NoiseDataReadDto.from(noiseDataService.createWithFile(file, data));
            RsData<NoiseDataReadDto> response = RsData.of("S-1", "성공적으로 생성됨", dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            RsData<NoiseDataReadDto> response = RsData.of("F-1", e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
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
            noiseDataService.delete(id);
            RsData<String> response = RsData.of("S-1", "성공적으로 삭제됨", null);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            RsData<String> response = RsData.of("F-1", e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }



}
