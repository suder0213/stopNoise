package com.ll.stopnoise.domain.cautionTime.entity;

import com.ll.stopnoise.domain.customer.entity.Customer;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CautionTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "customer", nullable = false)
    private Customer customer;


    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
}
