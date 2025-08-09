package com.ll.stopnoise.domain.noiseData.controller;


import com.ll.stopnoise.domain.noiseData.controller.dto.NoiseDataCreateDto;
import com.ll.stopnoise.domain.noiseData.controller.dto.NoiseDataReadDto;
import com.ll.stopnoise.domain.noiseData.controller.dto.NoiseDataUpdateDto;
import com.ll.stopnoise.domain.noiseData.service.NoiseDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RestController
@RequestMapping("/api/noiseData")
@RequiredArgsConstructor
public class NoiseDataController {
    private final NoiseDataService noiseDataService;

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

}
