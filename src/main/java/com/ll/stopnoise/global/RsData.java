package com.ll.stopnoise.global;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RsData<T> {
    private String resultCode; // 결과 코드 (예: "S-1", "F-1")
    private String msg;        // 메시지 (예: "성공", "실패")
    private T data;            // 실제 데이터

    // 성공 응답을 위한 정적 메서드
    public static <T> RsData<T> of(String resultCode, String msg, T data) {
        return new RsData<>(resultCode, msg, data);
    }

    // 실패 여부를 확인하는 편의 메서드
    public boolean isSuccess() {
        return resultCode.startsWith("S-");
    }
}