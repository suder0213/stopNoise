package com.ll.stopnoise.domain.gemini.controller.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class GeminiRequest {
    private List<Map<String, Object>> contents;

    public GeminiRequest(String userMessage) {
        this.contents = List.of(
                Map.of(
                        "role", "user",
                        "parts", List.of(Map.of("text", userMessage))
                )
        );
    }

    public List<Map<String, Object>> getContents() {
        return contents;
    }

    public void setContents(List<Map<String, Object>> contents) {
        this.contents = contents;
    }
}
