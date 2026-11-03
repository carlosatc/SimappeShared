/*
 * Copyright (c) 2026 CatcSoft.
 *
 * This software is the confidential and proprietary information of CatcSoft
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of a
 * written license agreement entered into with CatcSoft.
 *
 * Author: Carlos Torres
 * Date: 2026-11-02
 */
package com.catcsoft.simappe.shared.client.config;

import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import com.catcsoft.simappe.shared.security.ServiceKeyHeader;

import lombok.extern.slf4j.Slf4j;

/**
 * Arma los proxies reactivos ({@code XxxReactiveClient}, WebClient) de SimappeShared.
 *
 * <p>Toma de {@link ClientFactory} la base del destino, el ambiente y la cabecera de la clave de servicio, para que
 * los dos flujos resuelvan lo mismo. Vive aparte para que el flujo servlet no cargue WebFlux: solo la usa la
 * configuración de los clientes reactivos, que exige WebClient en el classpath.</p>
 *
 * @author Carlos Torres
 * @since 4.0.0
 */
@Slf4j
public final class ReactiveClientFactory {

    /** Fábrica común: base, ambiente y clave de servicio. */
    private final ClientFactory factory;

    /** Builder de WebClient del que se clona cada proxy. */
    private final WebClient.Builder webClient;

    /**
     * @param factory   fábrica común
     * @param webClient builder de WebClient
     */
    public ReactiveClientFactory(ClientFactory factory, WebClient.Builder webClient) {
        this.factory = factory;
        this.webClient = webClient;
    }

    /**
     * Proxy reactivo (WebClient) del cliente sobre su destino.
     *
     * @param <T>    tipo del cliente
     * @param type   interfaz del cliente
     * @param target servicio destino
     * @return el proxy
     * @throws IllegalStateException si no hay base para el destino
     */
    public <T> T reactive(Class<T> type, String target) {
        String base = factory.base(target);
        log.debug("SimappeShared: {} (WebClient) base={}", type.getSimpleName(), base);
        WebClient client = webClient.clone()
                .baseUrl(base)
                .defaultHeader(ClientFactory.ENVIRONMENT_HEADER, factory.environmentName())
                .filter(ServiceKeyHeader.webClientFilter(factory.serviceKeyHeaderName()))
                .build();
        return HttpServiceProxyFactory.builderFor(WebClientAdapter.create(client)).build().createClient(type);
    }
}
