/*
 * Copyright (c) 2026 CatcSoft.
 *
 * This software is the confidential and proprietary information of CatcSoft
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of a
 * written license agreement entered into with CatcSoft.
 *
 * Author: Carlos Torres
 * Date: 2026-10-15
 */
package com.catcsoft.simappe.shared.security;

import java.util.List;

import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;

/**
 * Nombre de la cabecera con que un servicio de confianza presenta la clave de servicio compartida.
 *
 * <p>El nombre es parametrizable por contexto con {@value #PROPERTY} (config-server). Las interfaces
 * {@code @HttpExchange} no resuelven placeholders en {@code @RequestHeader}, así que declaran la
 * cabecera con {@link #DEFAULT_NAME} y el filtro (WebClient) o el interceptor (RestClient) de esta
 * clase la renombra al valor configurado antes de enviar la petición. Sin la propiedad viaja con
 * {@link #DEFAULT_NAME}.</p>
 *
 * <p>La misma propiedad la leen SimappeAdmin y SimappeOauth2Server para recibirla: los tres deben
 * coincidir en cada ambiente.</p>
 *
 * @author Carlos Torres
 * @since 2.5.0
 */
public final class ServiceKeyHeader {

    /** Propiedad del config-server con el nombre de la cabecera. */
    public static final String PROPERTY = "simappe.security.service-key-header";

    /** Nombre por defecto y el que declaran las interfaces de los clientes. */
    public static final String DEFAULT_NAME = "X-Simappe-Service-Key";

    /** Placeholder de {@code @Value} con el valor por defecto. */
    public static final String VALUE_EXPRESSION = "${" + PROPERTY + ":" + DEFAULT_NAME + "}";

    private ServiceKeyHeader() {
    }

    /**
     * Filtro de WebClient que renombra la cabecera declarada al nombre configurado.
     *
     * @param headerName nombre configurado; vacío o nulo deja {@link #DEFAULT_NAME}
     * @return el filtro
     */
    public static ExchangeFilterFunction webClientFilter(String headerName) {
        String destino = normalizar(headerName);
        return (request, next) -> {
            if (destino.equalsIgnoreCase(DEFAULT_NAME) || !request.headers().containsHeader(DEFAULT_NAME)) {
                return next.exchange(request);
            }
            List<String> valores = request.headers().get(DEFAULT_NAME);
            ClientRequest renombrada = ClientRequest.from(request).headers(h -> {
                h.remove(DEFAULT_NAME);
                h.put(destino, valores);
            }).build();
            return next.exchange(renombrada);
        };
    }

    /**
     * Interceptor de RestClient que renombra la cabecera declarada al nombre configurado.
     *
     * @param headerName nombre configurado; vacío o nulo deja {@link #DEFAULT_NAME}
     * @return el interceptor
     */
    public static ClientHttpRequestInterceptor restClientInterceptor(String headerName) {
        String destino = normalizar(headerName);
        return (request, body, execution) -> {
            if (!destino.equalsIgnoreCase(DEFAULT_NAME) && request.getHeaders().containsHeader(DEFAULT_NAME)) {
                List<String> valores = request.getHeaders().get(DEFAULT_NAME);
                request.getHeaders().remove(DEFAULT_NAME);
                request.getHeaders().put(destino, valores);
            }
            return execution.execute(request, body);
        };
    }

    /**
     * Nombre efectivo de la cabecera.
     *
     * @param headerName nombre configurado
     * @return el configurado, o {@link #DEFAULT_NAME} si viene vacío
     */
    public static String normalizar(String headerName) {
        return headerName == null || headerName.isBlank() ? DEFAULT_NAME : headerName.trim();
    }
}
