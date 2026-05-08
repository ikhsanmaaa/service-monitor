package com.ikhsan.servicemonitor.dto.request;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UpdateServiceRequest {

    @JsonIgnore
    @NotBlank
    @Size(max = 100)
    private Long id;

    @Size(max = 100)
    private String name;

    @Size(max = 100)
    private String url;

    @Size(max = 100)
    private String category;

    @Size(max = 100)
    private Long lastLatency;

    @Size(max = 100)
    private LocalDateTime lastCheckedAt;
}
