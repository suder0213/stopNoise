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
        String prompt = "아래 JSON 형식의 파일들은 특정 기간 동안 발생한 소음 목록이야.\n"
                + "다른 아무 말도 하지 말고, 해당 파일들을 보고 다음 속성을 포함한 JSON 형식으로 답변을 해줘.\n"
                + "[속성]\n"
                + "averageNoiseDecibel (int) : 해당 기간 동안 소음들의 평균 크기\n"
                + "maxNoiseDecibel (int) : 해당 기간 동안 발생한 소음의 최대 크기\n"
                + "assumedStress (String) : 해당 소음으로 인해 다른 집이 느낄 것으로 추정되는 스트레스 수준(하~상)\n"
                + "AIAdvise (String) : 현재 소음 상태에 대한 너의 조언 ( 구어체로 친숙하게 )\n"
                + "\n[소음 데이터 목록]\n" // 줄바꿈을 추가하여 프롬프트와 데이터 구분
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