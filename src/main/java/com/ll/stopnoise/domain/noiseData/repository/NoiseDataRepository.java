package com.ll.stopnoise.domain.noiseData.repository;

import com.ll.stopnoise.domain.noiseData.entity.NoiseData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoiseDataRepository extends JpaRepository<NoiseData, Long> {
}
