package com.ll.stopnoise.domain.noiseData.entity;

import com.ll.stopnoise.domain.customer.entity.Customer;
import com.ll.stopnoise.domain.reportNoiseData.entity.ReportNoiseData;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NoiseData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Id;

    @ManyToOne
    @JoinColumn(name = "customer", nullable = false)
    private Customer customer;

    private Integer decibelLevel;
    private String noiseType;

    @Column(nullable = false)
    private String dataFileUrl;

    @Column(columnDefinition = "TEXT")
    private String memo;

    private LocalDateTime uploadTime;

    @OneToMany(mappedBy = "noiseData")
    private List<ReportNoiseData> reportNoiseDatas;
}
