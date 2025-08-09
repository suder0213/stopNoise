package com.ll.stopnoise.domain.reportNoiseData.repository;

import com.ll.stopnoise.domain.reportNoiseData.entity.ReportNoiseData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportNoiseDataRepository extends JpaRepository<ReportNoiseData, Long> {
}
