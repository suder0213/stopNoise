package com.ll.stopnoise.domain.noiseReport.controller;

import com.ll.stopnoise.domain.noiseReport.controller.dto.NoiseReportCreateDto;
import com.ll.stopnoise.domain.noiseReport.controller.dto.NoiseReportReadDto;
import com.ll.stopnoise.domain.noiseReport.service.NoiseReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/noiseReport")
public class NoiseReportController {
    private final NoiseReportService noiseReportService;

    @PostMapping
    public NoiseReportReadDto create(@RequestBody NoiseReportCreateDto noiseReportCreateDto) {
        return NoiseReportReadDto.from(noiseReportService.create(noiseReportCreateDto));
    }

    @GetMapping
    public List<NoiseReportReadDto> getAll() {
        return noiseReportService.getAll().stream().map(NoiseReportReadDto::from).collect(Collectors.toList());
    }
    @GetMapping("/{id}")
    public NoiseReportReadDto getOne(@PathVariable Integer id) {
        return NoiseReportReadDto.from(noiseReportService.getById(id));
    }
    @GetMapping("/customer/{customer_id}")
    public List<NoiseReportReadDto> getByCustomerId(@PathVariable Integer customer_id) {
        return noiseReportService.getByCustomerId(customer_id).stream().map(NoiseReportReadDto::from).collect(Collectors.toList());
    }
//    @PutMapping
//    public NoiseReportReadDto update(){
//
//    }
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Integer id) {
        noiseReportService.delete(id);
        return "NoiseReport %d deleted".formatted(id);
    }
}
