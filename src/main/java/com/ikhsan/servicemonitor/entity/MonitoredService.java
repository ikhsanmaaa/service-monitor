package com.ikhsan.servicemonitor.entity;

import java.time.LocalDateTime;

import com.ikhsan.servicemonitor.enums.ServiceStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "monitored_service")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MonitoredService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100)
    private String name;

    @Column(length = 100)
    private String url;

    @Column(length = 100)
    private String category;

    @Column(length = 100)
    @Enumerated(EnumType.STRING)
    private ServiceStatus status;

    @Column(length = 100)
    private String responseCode;

    @Column(length = 100)
    private String messageStatus;

    @Column
    private Long lastLatency;

    @Column(length = 100)
    private LocalDateTime lastCheckedAt;

}
