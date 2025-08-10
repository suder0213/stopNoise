package com.ll.stopnoise.domain.reportNoiseData.entity;

import com.ll.stopnoise.domain.noiseData.entity.NoiseData;
import com.ll.stopnoise.domain.noiseReport.entity.NoiseReport;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "report_noise_data")
public class ReportNoiseData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "noise_data", nullable = false)
    private NoiseData noiseData;

    @ManyToOne
    @JoinColumn(name = "noise_report", nullable = false)
    private NoiseReport noiseReport;
}
