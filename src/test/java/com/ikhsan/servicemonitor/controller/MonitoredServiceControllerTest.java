package com.ikhsan.servicemonitor.controller;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ikhsan.servicemonitor.dto.request.CreateServiceRequest;
import com.ikhsan.servicemonitor.dto.request.UpdateServiceRequest;
import com.ikhsan.servicemonitor.dto.response.ServiceResponse;
import com.ikhsan.servicemonitor.service.HealthCheckService;
import com.ikhsan.servicemonitor.service.MonitoredServiceService;

@WebMvcTest(MonitoredServiceController.class)
public class MonitoredServiceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private MonitoredServiceService monitoredServiceService;

    @MockitoBean
    private HealthCheckService healthCheckService;

    @Test
    @DisplayName("create monitored service")
    void createServiceSuccess() throws Exception {

        CreateServiceRequest request = new CreateServiceRequest();
        request.setName("Google");
        request.setUrl("https://google.com");
        request.setCategory("Search Engine");

        ServiceResponse response = new ServiceResponse();
        response.setId(1L);
        response.setName("Google");
        response.setUrl("https://google.com");
        response.setCategory("Search Engine");

        Mockito.when(monitoredServiceService.create(any(CreateServiceRequest.class)))
                .thenReturn(response);

        mockMvc.perform(
                post("/api/services/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.name").value("Google"))
                .andExpect(jsonPath("$.data.url").value("https://google.com"));
    }

    @Test
    @DisplayName("get all monitored services")
    void getAllServicesSuccess() throws Exception {

        ServiceResponse response = new ServiceResponse();
        response.setId(1L);
        response.setName("Google");

        Mockito.when(monitoredServiceService.getAll())
                .thenReturn(List.of(response));

        mockMvc.perform(
                get("/api/services/")
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[0].name").value("Google"));
    }

    @Test
    @DisplayName("get monitored service by id")
    void getServiceByIdSuccess() throws Exception {

        ServiceResponse response = new ServiceResponse();
        response.setId(1L);
        response.setName("Google");

        Mockito.when(monitoredServiceService.get(1L))
                .thenReturn(response);

        mockMvc.perform(
                get("/api/services/1")
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.name").value("Google"));
    }

    @Test
    @DisplayName("update monitored service")
    void updateServiceSuccess() throws Exception {

        UpdateServiceRequest request = new UpdateServiceRequest();
        request.setName("Updated Google");

        ServiceResponse response = new ServiceResponse();
        response.setId(1L);
        response.setName("Updated Google");

        Mockito.when(
                monitoredServiceService.update(
                        eq(1L),
                        any(UpdateServiceRequest.class)
                )
        )
                .thenReturn(response);

        mockMvc.perform(
                patch("/api/services/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.name").value("Updated Google"));
    }

    @Test
    @DisplayName("delete monitored service")
    void deleteServiceSuccess() throws Exception {

        mockMvc.perform(
                delete("/api/services/1")
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value("Ok"));

        Mockito.verify(monitoredServiceService)
                .delete(1L);
    }

    @Test
    @DisplayName("trigger force health check")
    void forceCheckSuccess() throws Exception {

        mockMvc.perform(
                post("/api/services/1/check")
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value("Ok"));

        Mockito.verify(healthCheckService)
                .checkService(1L);
    }
}
