package com.ikhsan.servicemonitor.service;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.ikhsan.servicemonitor.dto.request.CreateServiceRequest;
import com.ikhsan.servicemonitor.dto.request.UpdateServiceRequest;
import com.ikhsan.servicemonitor.dto.response.ServiceResponse;
import com.ikhsan.servicemonitor.entity.MonitoredService;
import com.ikhsan.servicemonitor.enums.ServiceStatus;
import com.ikhsan.servicemonitor.repository.MonitoredServiceRepository;

@Service
public class MonitoredServiceService {

    @Autowired
    private MonitoredServiceRepository monitoredServiceRepository;

    @Autowired
    private ValidateService validateService;

    private ServiceResponse toServiceResponse(MonitoredService monitoredService) {
        return ServiceResponse.builder().
                id(monitoredService.getId()).
                name(monitoredService.getName()).
                url(monitoredService.getUrl()).
                category(monitoredService.getCategory()).
                status(monitoredService.getStatus().toString()).
                lastLatency(monitoredService.getLastLatency()).
                lastCheckedAt(monitoredService.getLastCheckedAt()).
                build();
    }

    @Transactional
    public ServiceResponse create(CreateServiceRequest request) {
        validateService.validate(request);

        var monitoredService = new MonitoredService();

        monitoredService.setName(request.getName());
        monitoredService.setUrl(request.getUrl());
        monitoredService.setCategory(request.getCategory());
        monitoredService.setStatus(ServiceStatus.DOWN);
        monitoredService.setLastLatency(null);
        monitoredService.setLastCheckedAt(null);

        monitoredServiceRepository.save(monitoredService);

        return toServiceResponse(monitoredService);
    }

    @Transactional(readOnly = true)
    public List<ServiceResponse> getAll() {

        List<MonitoredService> services
                = monitoredServiceRepository.findAll();

        return services.stream()
                .map(this::toServiceResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ServiceResponse get(Long id) {

        MonitoredService monitoredService = monitoredServiceRepository.findById(id).
                orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Service not Found")
                );

        return toServiceResponse(monitoredService);
    }

    @Transactional
    public ServiceResponse update(Long id, UpdateServiceRequest request) {
        validateService.validate(request);

        MonitoredService monitoredService = monitoredServiceRepository.findById(id).
                orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Service not Found")
                );

        if (Objects.nonNull(request.getName())) {
            monitoredService.setName(request.getName());
        }
        if (Objects.nonNull(request.getUrl())) {
            monitoredService.setUrl(request.getUrl());
        }
        if (Objects.nonNull(request.getCategory())) {
            monitoredService.setCategory(request.getCategory());
        }
        if (Objects.nonNull(request.getLastLatency())) {
            monitoredService.setLastLatency(request.getLastLatency());
        }
        if (Objects.nonNull(request.getLastCheckedAt())) {
            monitoredService.setLastCheckedAt(request.getLastCheckedAt());
        }

        monitoredServiceRepository.save(monitoredService);

        return toServiceResponse(monitoredService);
    }

    @Transactional
    public void delete(Long id) {

        MonitoredService monitoredService = monitoredServiceRepository.findById(id)
                .orElseThrow(()
                        -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Service not found"
                )
                );

        monitoredServiceRepository.delete(monitoredService);
    }
}
