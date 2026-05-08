package com.ikhsan.servicemonitor.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClient;
import org.springframework.web.server.ResponseStatusException;

import com.ikhsan.servicemonitor.entity.MonitoredService;
import com.ikhsan.servicemonitor.enums.ServiceStatus;
import com.ikhsan.servicemonitor.repository.MonitoredServiceRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HealthCheckService {

    @Autowired
    private RestClient restClient;

    @Autowired
    private MonitoredServiceRepository monitoredServiceRepository;

    @Transactional
    public void check(MonitoredService monitoredService) {

        long start = System.currentTimeMillis();

        try {

            ResponseEntity<String> response
                    = restClient.get()
                            .uri(monitoredService.getUrl())
                            .retrieve()
                            .toEntity(String.class);

            long latency = System.currentTimeMillis() - start;

            monitoredService.setStatus(ServiceStatus.UP);
            monitoredService.setResponseCode(response.getStatusCode().toString());
            monitoredService.setMessageStatus("Ok");
            monitoredService.setLastLatency(latency);
            monitoredService.setLastCheckedAt(LocalDateTime.now());

        } catch (HttpStatusCodeException e) {

            monitoredService.setStatus(ServiceStatus.DOWN);
            monitoredService.setResponseCode(
                    e.getStatusCode().toString()
            );
            monitoredService.setMessageStatus(
                    e.getMessage()
            );
            monitoredService.setLastCheckedAt(LocalDateTime.now());

        } catch (Exception e) {

            monitoredService.setStatus(ServiceStatus.DOWN);
            monitoredService.setResponseCode(null);
            monitoredService.setMessageStatus(
                    e.getMessage()
            );
            monitoredService.setLastCheckedAt(LocalDateTime.now());

        }

        monitoredServiceRepository.save(monitoredService);
    }

    @Scheduled(fixedDelay = 30000)
    public void checkAllServices() {

        List<MonitoredService> services
                = monitoredServiceRepository.findAll();

        for (MonitoredService service : services) {

            check(service);
        }
    }

    public void checkService(Long id) {
        MonitoredService service = monitoredServiceRepository.findById(id).
                orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Service not found")
                );

        check(service);
    }
}
