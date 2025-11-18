package com.example.demo.domain.model.dto.gemini;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PlanRequest(
        String userContext,
        String sustainabilityGoal
) {}
