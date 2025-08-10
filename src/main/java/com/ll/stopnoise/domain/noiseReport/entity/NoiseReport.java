package com.ll.stopnoise.domain.noiseReport.entity;

import com.ll.stopnoise.domain.customer.entity.Customer;
import com.ll.stopnoise.domain.reportNoiseData.entity.ReportNoiseData;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "noise_reports")
public class NoiseReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    @Column(columnDefinition = "TEXT")
    private String analysisSummary;

    private LocalDateTime createAt;

    @OneToMany(mappedBy = "noiseReport", cascade = CascadeType.ALL)
    @Builder.Default
    private List<ReportNoiseData> reportNoiseData = new ArrayList<>();
}
