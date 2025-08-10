package com.ll.stopnoise.domain.noiseData.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.ll.stopnoise.domain.noiseData.controller.dto.NoiseDataCreateDto;
import com.ll.stopnoise.domain.noiseData.controller.dto.NoiseDataReadDto;
import com.ll.stopnoise.domain.noiseData.controller.dto.NoiseDataUpdateDto;
import com.ll.stopnoise.domain.noiseData.entity.NoiseData;
import com.ll.stopnoise.domain.noiseData.service.NoiseDataService;
import com.ll.stopnoise.domain.s3.service.S3Service;
import lombok.RequiredArgsConstructor;
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

    @PostMapping
    public NoiseDataReadDto createNoiseData(@RequestBody NoiseDataCreateDto noiseDataCreateDto) {
        return NoiseDataReadDto.from(noiseDataService.create(noiseDataCreateDto));
    }
    @PostMapping("/upload")
    public NoiseDataReadDto uploadNoiseData(
            @RequestParam("file") MultipartFile file,
            @RequestParam("data") String data
    ) {
        try {
            String fileUrl = s3Service.uploadFile(file);

            // TODO: fileUrl과 다른 데이터를 조합하여 NoiseData 엔티티 저장
            // 예: noiseDataService.save(customer, fileUrl);
            NoiseDataCreateDto noiseDataCreateDto = objectMapper.readValue(data, NoiseDataCreateDto.class);
            NoiseData uploadedNoiseData = noiseDataService.createWithFile(noiseDataCreateDto, fileUrl);
            return NoiseDataReadDto.from(uploadedNoiseData);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @GetMapping
    public List<NoiseDataReadDto> getAllNoiseData() {
        return noiseDataService.getAll().stream().map(NoiseDataReadDto::from).collect(Collectors.toList());
    }
    @GetMapping("/{id}")
    public NoiseDataReadDto getNoiseDataById(@PathVariable int id) {
        return NoiseDataReadDto.from(noiseDataService.getById(id));
    }

    @PutMapping
    public NoiseDataReadDto updateNoiseData(@RequestBody NoiseDataUpdateDto noiseDataUpdateDto) {
        return NoiseDataReadDto.from(noiseDataService.update(noiseDataUpdateDto));
    }
    @DeleteMapping("/{id}")
    public String deleteNoiseDataById(@RequestParam int id) {
        noiseDataService.delete(id);
        return "NoiseData %d deleted".formatted(id);
    }

    @DeleteMapping("/file/{id}")
    public String deleteNoiseDataByFile(@PathVariable int id) {
        noiseDataService.deleteWithFile(id);
        return "NoiseData %d deleted".formatted(id);
    }



}
