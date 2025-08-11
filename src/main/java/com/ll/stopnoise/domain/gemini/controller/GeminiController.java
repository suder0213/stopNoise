package com.ll.stopnoise.domain.gemini.controller;

import com.ll.stopnoise.domain.gemini.service.GeminiService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController

@RequestMapping("/api/gemini")
public class GeminiController {

    private final GeminiService geminiService;

    public GeminiController(GeminiService geminiService) {
        this.geminiService = geminiService;
    }

    // Gemini API 연동 테스트용
//    @GetMapping("/ask")
//    public String ask(@RequestParam String prompt) {
//        return geminiService.askGemini(prompt);
//    }

}
