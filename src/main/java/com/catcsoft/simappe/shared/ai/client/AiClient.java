/*
 * Copyright (c) 2024-2026 CatcSoft. Author: Carlos Torres Email: torrescamargo@gmail.com
 */
package com.catcsoft.simappe.shared.ai.client;

import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.catcsoft.simappe.model.admin.dto.ai.AiRequestDto;
import com.catcsoft.simappe.model.admin.dto.ai.AiResponseDto;
import com.catcsoft.simappe.model.admin.dto.ai.AiProviderConfigDto;

import lombok.extern.slf4j.Slf4j;

/**
 * HTTP client for SimappeAdmin AI endpoints.
 * Auto-configured via AiClientAutoConfiguration.
 *
 * Usage in any microservice:
 * <pre>
 *   &#64;Autowired AiClient aiClient;
 *   AiResponseDto response = aiClient.generate(6L, AiRequestDto.builder()
 *       .prompt("Genera un mensaje de bienvenida")
 *       .useCase("message_generation")
 *       .build());
 * </pre>
 */
@Slf4j
public class AiClient {

    private final RestTemplate restTemplate;
    private final String baseUrl;

    public AiClient(RestTemplate restTemplate, String adminUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = adminUrl + "/api/v1/ai";
        log.info("[AiClient] Initialized with baseUrl: {}", this.baseUrl);
    }

    /**
     * Generate content using AI
     * @param companyId the company ID
     * @param request the AI request (prompt, context, useCase, temperature, maxTokens)
     * @return AI response with generated text and usage stats
     */
    public AiResponseDto generate(Long companyId, AiRequestDto request) {
        String url = baseUrl + "/generate?companyId=" + companyId;
        HttpEntity<AiRequestDto> entity = new HttpEntity<>(request, jsonHeaders());
        ResponseEntity<AiResponseDto> response = restTemplate.postForEntity(url, entity, AiResponseDto.class);
        return response.getBody();
    }

    /**
     * Generate content with JWT token forwarding (for authenticated requests)
     */
    public AiResponseDto generate(Long companyId, AiRequestDto request, String bearerToken) {
        String url = baseUrl + "/generate?companyId=" + companyId;
        HttpHeaders headers = jsonHeaders();
        headers.setBearerAuth(bearerToken);
        HttpEntity<AiRequestDto> entity = new HttpEntity<>(request, headers);
        ResponseEntity<AiResponseDto> response = restTemplate.postForEntity(url, entity, AiResponseDto.class);
        return response.getBody();
    }

    /**
     * Test AI connection for a company
     */
    @SuppressWarnings("unchecked")
    public boolean testConnection(Long companyId) {
        try {
            String url = baseUrl + "/test-connection?companyId=" + companyId;
            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
            Map<String, Object> body = response.getBody();
            return body != null && Boolean.TRUE.equals(body.get("success"));
        } catch (Exception e) {
            log.warn("[AiClient] Test connection failed: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Get AI config for a company
     */
    public AiProviderConfigDto getConfig(Long companyId) {
        String url = baseUrl + "/config?companyId=" + companyId;
        ResponseEntity<AiProviderConfigDto> response = restTemplate.getForEntity(url, AiProviderConfigDto.class);
        return response.getBody();
    }

    private HttpHeaders jsonHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }
}
