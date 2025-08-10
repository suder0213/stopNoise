package com.ll.stopnoise.domain.noiseData.repository;

import com.ll.stopnoise.domain.noiseData.entity.NoiseData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NoiseDataRepository extends JpaRepository<NoiseData, Integer> {
    List<NoiseData> findByUploadTimeBetween(LocalDateTime start, LocalDateTime end);
}
