package com.ll.stopnoise.domain.noiseReport.repository;

import com.ll.stopnoise.domain.customer.entity.Customer;
import com.ll.stopnoise.domain.noiseReport.entity.NoiseReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoiseReportRepository extends JpaRepository<NoiseReport, Integer> {

    List<NoiseReport> findByCustomer(Customer customer);
}
