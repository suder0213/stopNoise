package com.ll.stopnoise.domain.noiseData.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
public class NoiseData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Id;
    
    // 업로드된 음성 파일을 찾기 위한 필드
    private String fileName;
    private String filePath;


    private LocalDateTime uploadTime;
}
