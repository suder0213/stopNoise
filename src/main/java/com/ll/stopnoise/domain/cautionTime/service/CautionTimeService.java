package com.ll.stopnoise.domain.cautionTime.service;

import com.ll.stopnoise.domain.cautionTime.controller.dto.CautionTimeCreateDto;
import com.ll.stopnoise.domain.cautionTime.entity.CautionTime;
import com.ll.stopnoise.domain.cautionTime.repository.CautionTimeRepository;
import com.ll.stopnoise.domain.customer.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CautionTimeService {
    private final CautionTimeRepository cautionTimeRepository;
    private final CustomerService customerService;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH:mm:ss");

    @Transactional
    public CautionTime create(CautionTimeCreateDto dto) {

        LocalDateTime startDateTime = LocalDateTime.parse(dto.getStartDateTime(), FORMATTER);
        LocalDateTime endDateTime = LocalDateTime.parse(dto.getEndDateTime(), FORMATTER);
        CautionTime cautionTime = CautionTime.builder()
                .customer(customerService.getCustomer(dto.getCustomerId()))
                .startDateTime(startDateTime)
                .endDateTime(endDateTime)
                .build();
        return cautionTimeRepository.save(cautionTime);
    }

    public List<CautionTime> getAllCautionTime() {
        return cautionTimeRepository.findAll();
    }

    public List<CautionTime> getAllByTime(String startDateTime, String endDateTime) {
        LocalDateTime sdt = LocalDateTime.parse(startDateTime, FORMATTER);
        LocalDateTime edt = LocalDateTime.parse(endDateTime, FORMATTER);
        return cautionTimeRepository.findByStartDateTimeAfterAndEndDateTimeBefore(sdt, edt);
    }

    public void delete(int id) {
        cautionTimeRepository.deleteById(id);
    }
}
