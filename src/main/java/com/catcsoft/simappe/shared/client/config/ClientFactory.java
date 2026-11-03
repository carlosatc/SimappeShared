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
 * <p>Es inmutable: {@link #withRestClient} devuelve una copia con el builder de RestClient. El builder se clona
 * por proxy para no dejarle la base ni los filtros a otro cliente.</p>
 *
 * <p>Solo depende de {@code spring-web}, que está en los dos flujos: una aplicación servlet la usa sin WebFlux en
 * el classpath. Los proxies reactivos los arma {@link ReactiveClientFactory}, que se carga solo si hay WebClient.</p>
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

    /**
     * @param environment propiedades del servicio
     */
    public ClientFactory(Environment environment) {
        this(environment, null);
    }

    private ClientFactory(Environment environment, RestClient.Builder restClient) {
        this.environment = environment;
        this.restClient = restClient;
    }

    /**
     * Copia de la fábrica que arma proxies bloqueantes sobre el builder dado.
     *
     * @param builder builder de RestClient
     * @return la copia
     */
    public ClientFactory withRestClient(RestClient.Builder builder) {
        return new ClientFactory(environment, builder);
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
     * Nombre del ambiente que va en {@code x-simappe-environment}.
     *
     * @return {@code simappe.shared.clients.environment}, por defecto {@code dev}
     */
    String environmentName() {
        return environment.getProperty("simappe.shared.clients.environment", "dev");
    }

    /**
     * Nombre de la cabecera de la clave de servicio.
     *
     * @return el nombre resuelto de {@code simappe.security.service-key-header}
     */
    String serviceKeyHeaderName() {
        return environment.resolvePlaceholders(ServiceKeyHeader.VALUE_EXPRESSION);
    }
}
