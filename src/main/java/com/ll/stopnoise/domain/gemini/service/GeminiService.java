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
                + "또 가능하면 한국어만을 사용해\n"
                + "소음에 대한 기준은 「공동주택 층간소음의 범위와 기준에 관한 규칙」 법령을 참고해.\n"
                + "사소한 소음에 대한 내용을 계속 언급하면 스트레스 받으니 34dB 이하의 소음에 대해선 언급하지 마.\n"
                + "AIAdvise에 포함되어야 할 내용은 \'내가 발생시키는 소음\'에 대한 내용이야.\n"
                + "작은 소음에 대해 계속 주의를 주면 스트레스를 받을 수 있으니, 가능하면 친근하게 얘기해\n"
                + "만약 소음 데이터 목록을 넘겨 받지 못 했다면, int 값은 모두 0, max_noise_type은 \"데이터 없음\", AIAdvise는 \"발생한 소음이 없습니다!\"으로 보내.\n"
                + "[속성]\n"
                + "average_noise_decibel (int)\n"
                + "max_noise_decibel (int)\n"
                + "max_noise_type (String) (한국어로 번역해서 전달)\n"
                + "assumedStress (int)\n"
                + "StaticalAnalyze (String) (해당 세대의 소음에 대한 수치를 포함한 객관적 분석 3문장)\n"
                + "Caution (String) (소음 지속 시간, 취약 시간, 주의 시간에 대한 조언 3문장)\n"
                + "NoiseFeatured (String) (최빈 소음 유형, 소음 평균, 최대 소음, 소음 빈도에 대한 분석 3문장)\n"
                + "RecommendedAction (String) (소음을 줄이기 위해 할만한 노력 3문장. 가능하면 구체적 숫자 포함)\n"
                + "Hashtag (String) (분석에 포함된 소음들을 요약 할만한 단어 3개를 , (반점)으로 구분하여 한국어로 제시)"
                + "\n\n[소음 데이터 목록]\n"
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