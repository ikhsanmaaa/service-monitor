package com.ikhsan.servicemonitor.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ikhsan.servicemonitor.entity.MonitoredService;

public interface MonitoredServiceRepository extends JpaRepository<MonitoredService, Long> {

}
