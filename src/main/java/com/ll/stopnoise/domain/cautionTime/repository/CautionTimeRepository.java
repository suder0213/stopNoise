package com.ll.stopnoise.domain.cautionTime.repository;

import com.ll.stopnoise.domain.cautionTime.entity.CautionTime;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface CautionTimeRepository extends JpaRepository<CautionTime, Integer> {
    List<CautionTime> findByStartDateTimeAfterAndEndDateTimeBefore(
            LocalDateTime startDateTime, LocalDateTime endDateTime
    );
}
