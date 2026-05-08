package com.ikhsan.servicemonitor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ikhsan.servicemonitor.dto.request.CreateServiceRequest;
import com.ikhsan.servicemonitor.dto.request.UpdateServiceRequest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ServiceMonitorApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("create service integration test")
    void createServiceIntegrationTest() throws Exception {

        CreateServiceRequest request
                = new CreateServiceRequest();

        request.setName("Google");
        request.setUrl("https://google.com");
        request.setCategory("Search Engine");

        mockMvc.perform(
                post("/api/services/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                objectMapper.writeValueAsString(request)
                        )
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").exists())
                .andExpect(jsonPath("$.data.name")
                        .value("Google"))
                .andExpect(jsonPath("$.data.url")
                        .value("https://google.com"))
                .andExpect(jsonPath("$.data.category")
                        .value("Search Engine"));
    }

    @Test
    @DisplayName("get all services integration test")
    void getAllServicesIntegrationTest() throws Exception {

        mockMvc.perform(
                get("/api/services/")
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    @DisplayName("get service by id integration test")
    void getServiceByIdIntegrationTest() throws Exception {

        CreateServiceRequest request
                = new CreateServiceRequest();

        request.setName("Github");
        request.setUrl("https://github.com");
        request.setCategory("Repository");

        String response = mockMvc.perform(
                post("/api/services/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                objectMapper.writeValueAsString(request)
                        )
        )
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long id = objectMapper.readTree(response)
                .path("data")
                .path("id")
                .asLong();

        mockMvc.perform(
                get("/api/services/" + id)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id")
                        .value(id))
                .andExpect(jsonPath("$.data.name")
                        .value("Github"));
    }

    @Test
    @DisplayName("update service integration test")
    void updateServiceIntegrationTest() throws Exception {

        CreateServiceRequest createRequest
                = new CreateServiceRequest();

        createRequest.setName("Old Name");
        createRequest.setUrl("https://test.com");
        createRequest.setCategory("Testing");

        String createResponse = mockMvc.perform(
                post("/api/services/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                objectMapper.writeValueAsString(createRequest)
                        )
        )
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long id = objectMapper.readTree(createResponse)
                .path("data")
                .path("id")
                .asLong();

        UpdateServiceRequest updateRequest
                = new UpdateServiceRequest();

        updateRequest.setName("Updated Name");

        mockMvc.perform(
                patch("/api/services/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                objectMapper.writeValueAsString(updateRequest)
                        )
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id")
                        .value(id))
                .andExpect(jsonPath("$.data.name")
                        .value("Updated Name"));
    }

    @Test
    @DisplayName("delete service integration test")
    void deleteServiceIntegrationTest() throws Exception {

        CreateServiceRequest request
                = new CreateServiceRequest();

        request.setName("Delete Test");
        request.setUrl("https://delete.com");
        request.setCategory("Testing");

        String createResponse = mockMvc.perform(
                post("/api/services/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                objectMapper.writeValueAsString(request)
                        )
        )
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long id = objectMapper.readTree(createResponse)
                .path("data")
                .path("id")
                .asLong();

        mockMvc.perform(
                delete("/api/services/" + id)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data")
                        .value("Ok"));
    }

    @Test
    @DisplayName("trigger health check integration test")
    void forceCheckIntegrationTest() throws Exception {

        CreateServiceRequest request
                = new CreateServiceRequest();

        request.setName("Google");
        request.setUrl("https://google.com");
        request.setCategory("Search Engine");

        String createResponse = mockMvc.perform(
                post("/api/services/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                objectMapper.writeValueAsString(request)
                        )
        )
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long id = objectMapper.readTree(createResponse)
                .path("data")
                .path("id")
                .asLong();

        mockMvc.perform(
                post("/api/services/" + id + "/check")
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data")
                        .value("Ok"));
    }
}
