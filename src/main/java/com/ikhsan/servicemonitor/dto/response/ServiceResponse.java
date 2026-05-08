package com.ikhsan.servicemonitor.dto.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ServiceResponse {

    private Long id;
    private String name;
    private String url;
    private String category;
    private String status;
    private Long lastLatency;
    private LocalDateTime lastCheckedAt;
}
