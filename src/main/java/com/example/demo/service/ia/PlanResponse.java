package com.example.demo.service.ia;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PlanResponse(
        String plan
) {}

