package com.ll.stopnoise.domain.noiseReport.repository;

import com.ll.stopnoise.domain.noiseReport.entity.NoiseReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoiseReportRepository extends JpaRepository<NoiseReport, Integer> {

}
