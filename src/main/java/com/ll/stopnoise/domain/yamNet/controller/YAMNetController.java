package com.ll.stopnoise.domain.yamNet.controller;

import com.ll.stopnoise.domain.yamNet.controller.dto.YAMNetRequestDto;
import com.ll.stopnoise.domain.yamNet.service.YAMNetService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class YAMNetController {
    private final YAMNetService yamNetService;
    @PostMapping("/analyze_audio")
    public String analyzeAudio(@RequestBody YAMNetRequestDto yamNetRequestDto) {
        return yamNetService.analyzeAudio(yamNetRequestDto.getUrl());
    }
}
