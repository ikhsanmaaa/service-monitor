package com.ikhsan.servicemonitor.controller;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ikhsan.servicemonitor.dto.request.CreateServiceRequest;
import com.ikhsan.servicemonitor.dto.request.UpdateServiceRequest;
import com.ikhsan.servicemonitor.dto.response.ServiceResponse;
import com.ikhsan.servicemonitor.dto.response.WebResponse;
import com.ikhsan.servicemonitor.service.MonitoredServiceService;

@RestController
public class MonitoredServiceController {

    private MonitoredServiceService monitoredServiceService;

    @PostMapping(path = "/api/services/create",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<ServiceResponse> create(@RequestBody CreateServiceRequest request) {
        ServiceResponse serviceResponse = monitoredServiceService.create(request);

        return WebResponse.<ServiceResponse>builder().data(serviceResponse).build();
    }

    @GetMapping(path = "/api/services/",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<List<ServiceResponse>> getAll() {

        List<ServiceResponse> serviceResponse = monitoredServiceService.getAll();

        return WebResponse.<List<ServiceResponse>>builder().data(serviceResponse).build();
    }

    @GetMapping(path = "/api/services/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<ServiceResponse> get(@PathVariable Long id) {

        ServiceResponse serviceResponse = monitoredServiceService.get(id);

        return WebResponse.<ServiceResponse>builder().data(serviceResponse).build();
    }

    @PatchMapping(path = "/api/services/{id}/update",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<ServiceResponse> update(Long id, @RequestBody UpdateServiceRequest request) {

        request.setId(id);

        ServiceResponse serviceResponse = monitoredServiceService.update(id, request);

        return WebResponse.<ServiceResponse>builder().data(serviceResponse).build();
    }

    @DeleteMapping(path = "/api/services/{id}/delete",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<String> delete(Long id) {

        monitoredServiceService.delete(id);

        return WebResponse.<String>builder().data("Ok").build();
    }
}
