/*
 * Copyright (c) 2026 CatcSoft
 *
 * Author: torrescamargo@gmail.com
 */
package com.catcsoft.simappe.shared.unattended.client;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

import com.catcsoft.simappe.model.admin.dto.security.UserSession;
import com.fasterxml.jackson.databind.JsonNode;

import reactor.core.publisher.Mono;

/**
 * Cliente del servidor OAuth2 que emite el token de ejecución de una sesión de
 * proceso.
 *
 * @author torrescamargo@gmail.com
 * @since 1.4.0
 */
@HttpExchange("simappe-oauth2-server/api/v1/execution-token")
public interface ExecutionTokenWebClient {

    /** Cabecera con la clave de servicio. */
    String SERVICE_KEY_HEADER = "X-Nebula-Service-Key";

    /**
     * Emite el token.
     *
     * @param serviceKey   clave de servicio
     * @param expirationMs vigencia pedida
     * @param session      sesión del proceso
     * @return respuesta con {@code accessToken} en la raíz o en {@code response}
     */
    @PostExchange("/generate")
    Mono<JsonNode> generate(
            @RequestHeader(SERVICE_KEY_HEADER) String serviceKey,
            @RequestParam("expirationMs") Long expirationMs,
            @RequestBody UserSession session);
}
