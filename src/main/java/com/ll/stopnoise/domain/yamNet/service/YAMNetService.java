package com.ll.stopnoise.domain.yamNet.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class YAMNetService {

    private final RestClient restClient;
    private final String yamnetApiUrl;

    public YAMNetService(@Value("${yamnet.api.url}") String yamnetApiUrl) {
        this.yamnetApiUrl = yamnetApiUrl;
        this.restClient = RestClient.create(yamnetApiUrl);
    }

    /**
     * YAMNet 서버의 analyze_audio API를 호출하여 소음 유형을 분석합니다.
     * @param audioUrl 분석할 오디오 파일의 URL
     * @return 소음 유형 (예: "Speech", "Music", "Vehicle")
     */
    // API 응답을 매핑할 데이터 모델 클래스 (NoiseAnalysisResponse.java)
    public record NoiseAnalysisResponse(String noiseType) {}

    // API 요청 바디를 위한 데이터 모델 클래스 (AnalyzeAudioRequest.java)
    public record AnalyzeAudioRequest(String url) {}


    public String analyzeAudio(String audioUrl) {
        try {
            // 요청 바디 생성
            AnalyzeAudioRequest requestBody = new AnalyzeAudioRequest(audioUrl);

            // POST 요청 실행 및 응답 받기
            NoiseAnalysisResponse response = restClient.post()
                    .uri("/analyze_audio")
                    .body(requestBody)
                    .retrieve()
                    .body(NoiseAnalysisResponse.class);

            // 응답에서 noiseType 반환
            return response != null ? response.noiseType() : "Unknown";

        } catch (Exception e) {
            // 예외 발생 시 에러 메시지 반환
            System.err.println("API 호출 중 오류 발생: " + e.getMessage());
            return "Error: " + e.getMessage();
        }
    }
}
