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
 * HTTP client for the AI Gateway with fallback to SimappeAdmin.
 *
 * Primary:  SimappeClient  /api/v1/ai-gateway/*  (new multi-provider gateway)
 * Fallback: SimappeAdmin   /api/v1/ai/*          (legacy Gemini-only endpoints)
 *
 * The fallback is used during the migration period. Once SimappeClient is stable
 * in production, disable it with: simappe.ai.fallback-enabled=false
 */
@Slf4j
public class AiClient {

    private final RestTemplate restTemplate;
    private final String primaryBaseUrl;   // SimappeClient /api/v1/ai-gateway
    private final String fallbackBaseUrl;  // SimappeAdmin  /api/v1/ai (null = disabled)

    public AiClient(RestTemplate restTemplate, String gatewayUrl, String fallbackAdminUrl) {
        this.restTemplate = restTemplate;
        this.primaryBaseUrl = gatewayUrl + "/api/v1/ai-gateway";
        this.fallbackBaseUrl = fallbackAdminUrl != null ? fallbackAdminUrl + "/api/v1/ai" : null;
        log.info("[AiClient] primary: {}, fallback: {}", this.primaryBaseUrl,
                this.fallbackBaseUrl != null ? this.fallbackBaseUrl : "DISABLED");
    }

    // ── Simple generation ───────────────────────────────

    public AiResponseDto generate(AiRequestDto request) {
        return executeWithFallback(
                () -> postForAiResponse(primaryBaseUrl + "/generate", request, null),
                () -> postForAiResponse(fallbackBaseUrl + "/generate", request, null),
                "generate"
        );
    }

    public AiResponseDto generate(AiRequestDto request, String bearerToken) {
        return executeWithFallback(
                () -> postForAiResponse(primaryBaseUrl + "/generate", request, bearerToken),
                () -> postForAiResponse(fallbackBaseUrl + "/generate", request, bearerToken),
                "generate"
        );
    }

    // ── Chat with tool use ──────────────────────────────

    @SuppressWarnings("unchecked")
    public Map<String, Object> chat(Map<String, Object> chatRequest) {
        // Chat with tools is a NEW feature — no fallback to SimappeAdmin (doesn't support it)
        String url = primaryBaseUrl + "/chat";
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(chatRequest, jsonHeaders());
        ResponseEntity<Map> response = restTemplate.postForEntity(url, entity, Map.class);
        return response.getBody();
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> chat(Map<String, Object> chatRequest, String bearerToken) {
        String url = primaryBaseUrl + "/chat";
        HttpHeaders headers = jsonHeaders();
        headers.setBearerAuth(bearerToken);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(chatRequest, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(url, entity, Map.class);
        return response.getBody();
    }

    public AiResponseDto sendFunctionResult(Map<String, Object> chatRequest) {
        String url = primaryBaseUrl + "/chat/function-result";
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(chatRequest, jsonHeaders());
        ResponseEntity<AiResponseDto> response = restTemplate.postForEntity(url, entity, AiResponseDto.class);
        return response.getBody();
    }

    public AiResponseDto sendFunctionResult(Map<String, Object> chatRequest, String bearerToken) {
        String url = primaryBaseUrl + "/chat/function-result";
        HttpHeaders headers = jsonHeaders();
        headers.setBearerAuth(bearerToken);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(chatRequest, headers);
        ResponseEntity<AiResponseDto> response = restTemplate.postForEntity(url, entity, AiResponseDto.class);
        return response.getBody();
    }

    // ── Configuration ───────────────────────────────────

    @SuppressWarnings("unchecked")
    public boolean testConnection() {
        return executeWithFallback(
                () -> {
                    ResponseEntity<Map> r = restTemplate.getForEntity(primaryBaseUrl + "/test-connection", Map.class);
                    Map<String, Object> body = r.getBody();
                    return body != null && Boolean.TRUE.equals(body.get("success"));
                },
                () -> {
                    ResponseEntity<Map> r = restTemplate.getForEntity(fallbackBaseUrl + "/test-connection", Map.class);
                    Map<String, Object> body = r.getBody();
                    return body != null && Boolean.TRUE.equals(body.get("success"));
                },
                "testConnection"
        );
    }

    public AiProviderConfigDto getConfig() {
        return executeWithFallback(
                () -> restTemplate.getForEntity(primaryBaseUrl + "/config", AiProviderConfigDto.class).getBody(),
                () -> restTemplate.getForEntity(fallbackBaseUrl + "/config", AiProviderConfigDto.class).getBody(),
                "getConfig"
        );
    }

    // ── Backwards compatibility ─────────────────────────

    @Deprecated(since = "2.10.0")
    public AiResponseDto generate(Long companyId, AiRequestDto request) {
        return generate(request);
    }

    @Deprecated(since = "2.10.0")
    public AiResponseDto generate(Long companyId, AiRequestDto request, String bearerToken) {
        return generate(request, bearerToken);
    }

    @Deprecated(since = "2.10.0")
    public boolean testConnection(Long companyId) {
        return testConnection();
    }

    @Deprecated(since = "2.10.0")
    public AiProviderConfigDto getConfig(Long companyId) {
        return getConfig();
    }

    // ── Fallback mechanism ──────────────────────────────

    @FunctionalInterface
    private interface AiCall<T> {
        T execute();
    }

    /**
     * Intenta ejecutar en SimappeClient (primary). Si falla y hay fallback habilitado,
     * reintenta en SimappeAdmin (legacy). Logea el fallback para monitoring.
     */
    private <T> T executeWithFallback(AiCall<T> primary, AiCall<T> fallback, String operation) {
        try {
            return primary.execute();
        } catch (Exception primaryEx) {
            if (fallbackBaseUrl == null) {
                throw primaryEx; // No fallback configured
            }

            log.warn("[AiClient] Primary ({}) failed for '{}': {}. Falling back to SimappeAdmin ({})",
                    primaryBaseUrl, operation, primaryEx.getMessage(), fallbackBaseUrl);

            try {
                T result = fallback.execute();
                log.info("[AiClient] Fallback succeeded for '{}'", operation);
                return result;
            } catch (Exception fallbackEx) {
                log.error("[AiClient] Fallback also failed for '{}': {}", operation, fallbackEx.getMessage());
                throw primaryEx; // Throw the original exception
            }
        }
    }

    // ── Helpers ─────────────────────────────────────────

    private AiResponseDto postForAiResponse(String url, AiRequestDto request, String bearerToken) {
        HttpHeaders headers = jsonHeaders();
        if (bearerToken != null) headers.setBearerAuth(bearerToken);
        HttpEntity<AiRequestDto> entity = new HttpEntity<>(request, headers);
        ResponseEntity<AiResponseDto> response = restTemplate.postForEntity(url, entity, AiResponseDto.class);
        return response.getBody();
    }

    private HttpHeaders jsonHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }
}
