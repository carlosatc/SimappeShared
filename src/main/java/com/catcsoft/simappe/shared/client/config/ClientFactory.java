/*
 * Copyright (c) 2026 CatcSoft.
 *
 * This software is the confidential and proprietary information of CatcSoft
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of a
 * written license agreement entered into with CatcSoft.
 *
 * Author: Carlos Torres
 */
package com.catcsoft.simappe.shared.client.config;

import org.springframework.core.env.Environment;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import com.catcsoft.simappe.shared.security.ServiceKeyHeader;

import lombok.extern.slf4j.Slf4j;

/**
 * Arma los proxies {@code @HttpExchange} de SimappeShared sobre el servicio destino.
 *
 * <p>La base de cada destino es {@code simappe.shared.clients.urls.<destino>} si está definida, y si no
 * {@code simappe.shared.clients.base-url}. Todo proxy lleva la cabecera {@code x-simappe-environment}
 * ({@code simappe.shared.clients.environment}, por defecto {@code dev}) y la clave de servicio cuando el
 * llamador la pide ({@link ServiceKeyHeader}).</p>
 *
 * <p>Es inmutable: {@link #withRestClient} y {@link #withWebClient} devuelven una copia con el builder de
 * cada estilo. Los builders se clonan por proxy para no dejarle la base ni los filtros a otro cliente.</p>
 *
 * @author Carlos Torres
 * @since 4.0.0
 */
@Slf4j
public final class ClientFactory {

    /** Cabecera del ambiente del despliegue. */
    static final String ENVIRONMENT_HEADER = "x-simappe-environment";

    /** Prefijo de las bases por servicio destino. */
    static final String URLS_PREFIX = "simappe.shared.clients.urls.";

    /** Propiedades del servicio. */
    private final Environment environment;

    /** Builder de RestClient (variante bloqueante); null hasta {@link #withRestClient}. */
    private final RestClient.Builder restClient;

    /** Builder de WebClient (variante reactiva); null hasta {@link #withWebClient}. */
    private final WebClient.Builder webClient;

    /**
     * @param environment propiedades del servicio
     */
    public ClientFactory(Environment environment) {
        this(environment, null, null);
    }

    private ClientFactory(Environment environment, RestClient.Builder restClient, WebClient.Builder webClient) {
        this.environment = environment;
        this.restClient = restClient;
        this.webClient = webClient;
    }

    /**
     * Copia de la fábrica que arma proxies bloqueantes sobre el builder dado.
     *
     * @param builder builder de RestClient
     * @return la copia
     */
    public ClientFactory withRestClient(RestClient.Builder builder) {
        return new ClientFactory(environment, builder, webClient);
    }

    /**
     * Copia de la fábrica que arma proxies reactivos sobre el builder dado.
     *
     * @param builder builder de WebClient
     * @return la copia
     */
    public ClientFactory withWebClient(WebClient.Builder builder) {
        return new ClientFactory(environment, restClient, builder);
    }

    /**
     * Base del servicio destino: {@code simappe.shared.clients.urls.<destino>} o, si falta,
     * {@code simappe.shared.clients.base-url}.
     *
     * @param target servicio destino ({@code admin}, {@code client}, {@code configurations},
     *                {@code oauth2} o {@code tasks})
     * @return la base
     * @throws IllegalStateException si no hay base para el destino
     */
    public String base(String target) {
        String base = environment.getProperty(URLS_PREFIX + target,
                environment.getProperty("simappe.shared.clients.base-url"));
        if (base == null || base.isBlank()) {
            throw new IllegalStateException("SimappeShared: falta simappe.shared.clients.base-url (o "
                    + URLS_PREFIX + target + ")");
        }
        return base;
    }

    /**
     * Proxy bloqueante (RestClient) del cliente sobre su destino.
     *
     * @param <T>     tipo del cliente
     * @param type    interfaz del cliente
     * @param target servicio destino
     * @return el proxy
     */
    public <T> T blocking(Class<T> type, String target) {
        if (restClient == null) {
            throw new IllegalStateException("SimappeShared: fábrica sin RestClient");
        }
        String base = base(target);
        log.debug("SimappeShared: {} (RestClient) base={}", type.getSimpleName(), base);
        RestClient client = restClient.clone()
                .baseUrl(base)
                .defaultHeader(ENVIRONMENT_HEADER, environmentName())
                .requestInterceptor(ServiceKeyHeader.restClientInterceptor(serviceKeyHeaderName()))
                .build();
        return HttpServiceProxyFactory.builderFor(RestClientAdapter.create(client)).build().createClient(type);
    }

    /**
     * Proxy reactivo (WebClient) del cliente sobre su destino.
     *
     * @param <T>     tipo del cliente
     * @param type    interfaz del cliente
     * @param target servicio destino
     * @return el proxy
     */
    public <T> T reactive(Class<T> type, String target) {
        if (webClient == null) {
            throw new IllegalStateException("SimappeShared: fábrica sin WebClient");
        }
        String base = base(target);
        log.debug("SimappeShared: {} (WebClient) base={}", type.getSimpleName(), base);
        WebClient client = webClient.clone()
                .baseUrl(base)
                .defaultHeader(ENVIRONMENT_HEADER, environmentName())
                .filter(ServiceKeyHeader.webClientFilter(serviceKeyHeaderName()))
                .build();
        return HttpServiceProxyFactory.builderFor(WebClientAdapter.create(client)).build().createClient(type);
    }

    private String environmentName() {
        return environment.getProperty("simappe.shared.clients.environment", "dev");
    }

    private String serviceKeyHeaderName() {
        return environment.resolvePlaceholders(ServiceKeyHeader.VALUE_EXPRESSION);
    }
}
