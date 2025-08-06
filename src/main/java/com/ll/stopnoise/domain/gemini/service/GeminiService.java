package com.ll.stopnoise.domain.gemini.service;

import com.ll.stopnoise.domain.gemini.controller.dto.GeminiRequest;
import com.ll.stopnoise.domain.gemini.controller.dto.GeminiResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GeminiService {

    @Value("${gemini.api-key}")
    private String apiKey;

    private final String API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=";

    public String askGemini(String prompt) {
        RestTemplate restTemplate = new RestTemplate();

        GeminiRequest request = new GeminiRequest(prompt);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<GeminiRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<GeminiResponse> response = restTemplate.exchange(
                API_URL + apiKey,
                HttpMethod.POST,
                entity,
                GeminiResponse.class
        );

        if (response.getBody() != null &&
                !response.getBody().getCandidates().isEmpty()) {
            return response.getBody().getCandidates()
                    .get(0)
                    .getContent()
                    .getParts()
                    .get(0)
                    .getText();
        }

        return "응답 없음";
    }

    public String apikey() {
        return apiKey;
    }
}