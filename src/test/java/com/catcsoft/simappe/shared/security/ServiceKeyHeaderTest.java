/*
 * Copyright (c) 2026 CatcSoft.
 *
 * This software is the confidential and proprietary information of CatcSoft
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of a
 * written license agreement entered into with CatcSoft.
 */
package com.catcsoft.simappe.shared.security;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRequest;
import org.springframework.mock.http.client.MockClientHttpRequest;
import org.springframework.mock.http.client.MockClientHttpResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;

import reactor.core.publisher.Mono;

/**
 * La cabecera de la clave de servicio toma el nombre configurado por contexto.
 */
class ServiceKeyHeaderTest {

    @Test
    @DisplayName("WebClient: renombra la cabecera declarada al nombre configurado")
    void webClientRenombra() {
        AtomicReference<ClientRequest> enviada = new AtomicReference<>();
        ClientRequest pedida = ClientRequest.create(HttpMethod.POST, URI.create("http://gw/x"))
                .header(ServiceKeyHeader.DEFAULT_NAME, "secreto").build();
        ServiceKeyHeader.webClientFilter("X-Otra-Clave")
                .filter(pedida, r -> {
                    enviada.set(r);
                    return Mono.just(ClientResponse.create(HttpStatus.OK).build());
                }).block();
        assertThat(enviada.get().headers().getFirst("X-Otra-Clave")).isEqualTo("secreto");
        assertThat(enviada.get().headers().containsHeader(ServiceKeyHeader.DEFAULT_NAME)).isFalse();
    }

    @Test
    @DisplayName("WebClient: sin propiedad viaja con el nombre por defecto")
    void webClientPorDefecto() {
        AtomicReference<ClientRequest> enviada = new AtomicReference<>();
        ClientRequest pedida = ClientRequest.create(HttpMethod.POST, URI.create("http://gw/x"))
                .header(ServiceKeyHeader.DEFAULT_NAME, "secreto").build();
        ServiceKeyHeader.webClientFilter(" ")
                .filter(pedida, r -> {
                    enviada.set(r);
                    return Mono.just(ClientResponse.create(HttpStatus.OK).build());
                }).block();
        assertThat(enviada.get().headers().getFirst(ServiceKeyHeader.DEFAULT_NAME)).isEqualTo("secreto");
    }

    @Test
    @DisplayName("RestClient: renombra la cabecera declarada al nombre configurado")
    void restClientRenombra() throws Exception {
        MockClientHttpRequest pedida = new MockClientHttpRequest(HttpMethod.POST, URI.create("http://gw/x"));
        pedida.getHeaders().add(ServiceKeyHeader.DEFAULT_NAME, "secreto");
        AtomicReference<HttpRequest> enviada = new AtomicReference<>();
        ServiceKeyHeader.restClientInterceptor("X-Otra-Clave").intercept(pedida, new byte[0], (r, b) -> {
            enviada.set(r);
            return new MockClientHttpResponse(new byte[0], HttpStatus.OK);
        });
        assertThat(enviada.get().getHeaders().getFirst("X-Otra-Clave")).isEqualTo("secreto");
        assertThat(enviada.get().getHeaders().containsHeader(ServiceKeyHeader.DEFAULT_NAME)).isFalse();
    }
}
