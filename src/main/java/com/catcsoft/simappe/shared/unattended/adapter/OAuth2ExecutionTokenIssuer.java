/*
 * Copyright (c) 2026 CatcSoft
 *
 * Author: torrescamargo@gmail.com
 */
package com.catcsoft.simappe.shared.unattended.adapter;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import com.catcsoft.simappe.commons.api.v1.core.unattended.ExecutionTokenIssuer;
import com.catcsoft.simappe.commons.api.v1.core.unattended.UnattendedTaskConfig;
import com.catcsoft.simappe.commons.api.v1.core.unattended.UnattendedTaskToken;
import com.catcsoft.simappe.model.admin.dto.security.RoleDto;
import com.catcsoft.simappe.model.admin.dto.security.UserSession;
import com.catcsoft.simappe.model.admin.enums.RoleType;
import com.catcsoft.simappe.model.core.database.enums.ConnectionContext;
import com.catcsoft.simappe.shared.unattended.client.ExecutionTokenWebClient;
import com.fasterxml.jackson.databind.JsonNode;

import lombok.extern.slf4j.Slf4j;

/**
 * Emisor de tokens de ejecución sobre el servidor OAuth2: arma la sesión del
 * proceso con el usuario, el tenant, el rol y la vigencia de la fila, y pide el
 * token con la clave de servicio. No guarda tokens: cada llamada emite uno.
 *
 * @author torrescamargo@gmail.com
 * @since 1.5.0
 */
@Slf4j
public class OAuth2ExecutionTokenIssuer implements ExecutionTokenIssuer {

    private final ExecutionTokenWebClient client;
    private final String serviceKey;
    private final Duration timeout;
    private final Clock clock;

    /**
     * @param client     cliente del servidor OAuth2
     * @param serviceKey clave de servicio
     * @param timeout    espera máxima de la respuesta
     * @param clock      reloj
     */
    public OAuth2ExecutionTokenIssuer(ExecutionTokenWebClient client, String serviceKey, Duration timeout,
            Clock clock) {
        this.client = Objects.requireNonNull(client, "client no puede ser null");
        if (serviceKey == null || serviceKey.isBlank()) {
            throw new IllegalArgumentException("La clave de servicio es obligatoria");
        }
        this.serviceKey = serviceKey;
        this.timeout = Objects.requireNonNull(timeout, "timeout no puede ser null");
        this.clock = Objects.requireNonNull(clock, "clock no puede ser null");
    }

    /** {@inheritDoc} */
    @Override
    public UnattendedTaskToken issue(UnattendedTaskConfig config) {
        Objects.requireNonNull(config, "config no puede ser null");
        config.requireExecutable();
        Instant emitido = clock.instant();
        JsonNode body = client.generate(serviceKey, config.expirationMs(), session(config)).block(timeout);
        String token = readToken(body);
        if (token == null || token.isBlank()) {
            throw new IllegalStateException("El servidor de autorización respondió sin accessToken para la compañía "
                    + config.clientId() + "/" + config.companyId());
        }
        log.debug("Token de ejecución emitido para {}/{}/{} ({}), vigencia {} ms", config.clientId(),
                config.companyId(), config.subsidiaryId(), config.environment(), config.expirationMs());
        return new UnattendedTaskToken(token, emitido, emitido.plusMillis(config.expirationMs()));
    }

    /**
     * Lee el token de la respuesta: en la raíz, envuelto en {@code response} o en el sobre estándar de éxito
     * ({@code response.content}), que es como lo entrega el servidor OAuth2 a través de {@code CustomResponseAdvise}.
     *
     * @param body respuesta
     * @return token o {@code null}
     */
    static String readToken(JsonNode body) {
        if (body == null || body.isNull()) {
            return null;
        }
        for (JsonNode nodo : List.of(body, body.path("response"), body.path("response").path("content"))) {
            JsonNode token = nodo.get("accessToken");
            if (token != null && token.isTextual()) {
                return token.asText();
            }
        }
        return null;
    }

    /**
     * Sesión del proceso: contexto de negocio, compañía fija y un solo rol.
     *
     * @param c fila
     * @return sesión
     */
    static UserSession session(UnattendedTaskConfig c) {
        RoleDto rol = RoleDto.builder()
                .id(c.roleId())
                .name(c.roleName())
                .authority(c.roleAuthority())
                .roleType(roleType(c.roleType()))
                .build();
        return UserSession.builder()
                .username(c.username())
                .customerId(c.clientId())
                .companyId(c.companyId())
                .subsidiaryId(c.subsidiaryId())
                .environment(c.environment())
                .connectionContext(ConnectionContext.BUSINESS)
                .companyLocked(Boolean.TRUE)
                .roles(List.of(rol))
                .build();
    }

    private static RoleType roleType(String valor) {
        if (valor == null || valor.isBlank()) {
            return null;
        }
        try {
            return RoleType.valueOf(valor.trim().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
