package com.ll.stopnoise.domain.gemini.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ll.stopnoise.domain.gemini.controller.dto.GeminiRequest;
import com.ll.stopnoise.domain.gemini.controller.dto.GeminiResponse;
import com.ll.stopnoise.domain.noiseData.controller.dto.NoiseDataReadDto;
import com.ll.stopnoise.domain.noiseData.entity.NoiseData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GeminiService {

    @Value("${gemini.api-key}")
    private String apiKey;

    private final String API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=";


    // Gemini API가 작동하는지 확인하기 위한 메소드
//    public String askGemini(String prompt) {
//        RestTemplate restTemplate = new RestTemplate();
//        GeminiRequest request = new GeminiRequest(prompt);
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        HttpEntity<GeminiRequest> entity = new HttpEntity<>(request, headers);
//        ResponseEntity<GeminiResponse> response = restTemplate.exchange(
//                API_URL + apiKey,
//                HttpMethod.POST,
//                entity,
//                GeminiResponse.class
//        );
//        if (response.getBody() != null &&
//                !response.getBody().getCandidates().isEmpty()) {
//            return response.getBody().getCandidates()
//                    .get(0)
//                    .getContent()
//                    .getParts()
//                    .get(0)
//                    .getText();
//        }
//        return "응답 없음";
//    }

    public String noiseReportAnalysis(List<NoiseData> noiseDataList){
        // 1. 엔티티 리스트를 DTO 리스트로 변환
        List<NoiseDataReadDto> noiseDataDtoList = noiseDataList.stream()
                .map(NoiseDataReadDto::from)
                .collect(Collectors.toList());

        ObjectMapper objectMapper = new ObjectMapper();
        // 💡 JavaTimeModule을 ObjectMapper에 수동으로 등록
        objectMapper.registerModule(new JavaTimeModule());
        String noiseDataJson;
        try {
            noiseDataJson = objectMapper.writeValueAsString(noiseDataDtoList);
        } catch (JsonProcessingException e) {
            // 이 부분에 오류 메시지를 출력
            e.printStackTrace(); // 콘솔에 상세한 스택 트레이스를 출력
            return "JSON 변환 실패";
        }
        String prompt = "아래 JSON 형식의 파일들은 특정 기간 동안 한 세대에서 발생시킨 소음 목록이야.\n"
                + "다른 아무 말도 하지 말고, 다음 속성들을 슬래시(/)로 구분된 하나의 문자열로 답변을 해줘.\n"
                + "응답을 감싸는 마크다운(markdown) 문법(```)은 절대 사용하지 마.\n"
                + "만약 아무 데이터도 넘겨 받지 못했다면, int 값은 모두 0, max_noise_type은 \"데이터 없음\", AIAdvise는 \"데이터 없음\"으로 보내.\n"
                + "[속성]\n"
                + "average_noise_decibel (int)\n"
                + "max_noise_decibel (int)\n"
                + "max_noise_type (String)\n"
                + "assumedStress (int)\n"
                + "AIAdvise (String) (이 속성은 슬래시를 포함하지 않도록 주의해줘)\n"
                + "\n[소음 데이터 목록]\n"
                + noiseDataJson;

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
}