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
package com.catcsoft.simappe.shared.client.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import com.catcsoft.simappe.shared.client.admin.RolClienteBlockingClient;
import com.catcsoft.simappe.shared.client.admin.RolClienteReactiveClient;
import com.catcsoft.simappe.shared.client.configurations.UsuarioSimappeBlockingClient;
import com.catcsoft.simappe.shared.client.configurations.UsuarioSimappeReactiveClient;
import com.catcsoft.simappe.shared.client.oauth2.ExecutionTokenBlockingClient;
import com.catcsoft.simappe.shared.client.oauth2.ExecutionTokenReactiveClient;
import com.catcsoft.simappe.shared.client.tareas.ControlTareasBlockingClient;
import com.catcsoft.simappe.shared.client.tareas.ControlTareasReactiveClient;

import com.catcsoft.simappe.shared.security.ServiceKeyHeader;

import lombok.extern.slf4j.Slf4j;

/**
 * Autoconfiguración de los clientes HTTP compartidos de configuraciones y tareas (interfaz
 * {@code @HttpExchange} + {@code HttpServiceProxyFactory}): los servicios consumidores solo inyectan
 * el bean, sin definirlo.
 *
 * <p>Activación opt-in: {@code simappe.shared.clients.enabled=true}; sin la propiedad no se registra
 * nada y los servicios que ya usan SimappeShared no cambian. Expone dos estilos (ambos activos por
 * defecto cuando la autoconfiguración está habilitada, opt-out individual):</p>
 * <ul>
 *   <li>{@code simappe.shared.clients.blocking.enabled} (default true) → RestClient</li>
 *   <li>{@code simappe.shared.clients.reactive.enabled} (default true) → WebClient</li>
 * </ul>
 *
 * <p>Config (config-server): {@code simappe.shared.clients.base-url} (gateway, ej.
 * {@code http://simappe-gateway-server:8090}) y {@code simappe.shared.clients.environment} (header
 * {@code x-simappe-environment}, fijo por despliegue). La cabecera de la clave de servicio toma su nombre
 * de {@code simappe.security.service-key-header} (default {@code X-Simappe-Service-Key}). El token de auth NO se configura aquí: se pasa
 * explícito por llamada ({@code Authorization: Bearer}).</p>
 *
 * @author Carlos Torres
 * @since 2.5.0
 */
@AutoConfiguration
@ConditionalOnProperty(prefix = "simappe.shared.clients", name = "enabled", havingValue = "true")
@Slf4j
public class SharedClientsAutoConfiguration {

    /** Cabecera del ambiente del despliegue. */
    private static final String ENV_HEADER = "x-simappe-environment";

    /** Base de los clientes (gateway). */
    @Value("${simappe.shared.clients.base-url}")
    private String baseUrl;

    /** Ambiente del despliegue. */
    @Value("${simappe.shared.clients.environment:dev}")
    private String environment;

    /** Nombre de la cabecera de la clave de servicio, por contexto (config-server). */
    @Value(ServiceKeyHeader.VALUE_EXPRESSION)
    private String serviceKeyHeader;

    /**
     * Crea un proxy {@code @HttpExchange} bloqueante (RestClient) sobre el gateway.
     *
     * @param <T>        tipo del cliente
     * @param clientType interfaz del cliente
     * @return el proxy
     */
    private <T> T blockingProxy(Class<T> clientType) {
        log.info("SimappeShared: creando {} (RestClient) baseUrl={} env={}",
                clientType.getSimpleName(), baseUrl, environment);
        RestClient restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(ENV_HEADER, environment)
                .requestInterceptor(ServiceKeyHeader.restClientInterceptor(serviceKeyHeader))
                .build();
        return HttpServiceProxyFactory
                .builderFor(RestClientAdapter.create(restClient))
                .build()
                .createClient(clientType);
    }

    /**
     * Crea un proxy {@code @HttpExchange} reactivo (WebClient) sobre el gateway.
     *
     * @param <T>        tipo del cliente
     * @param clientType interfaz del cliente
     * @return el proxy
     */
    private <T> T reactiveProxy(Class<T> clientType) {
        log.info("SimappeShared: creando {} (WebClient) baseUrl={} env={}",
                clientType.getSimpleName(), baseUrl, environment);
        WebClient webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(ENV_HEADER, environment)
                .filter(ServiceKeyHeader.webClientFilter(serviceKeyHeader))
                .build();
        return HttpServiceProxyFactory
                .builderFor(WebClientAdapter.create(webClient))
                .build()
                .createClient(clientType);
    }

    /**
     * Bean del {@link ControlTareasBlockingClient} (RestClient). Control central del proceso de tareas de simappe-tasks (HT_ARQ_013).
     *
     * @return el proxy bloqueante
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "simappe.shared.clients.blocking", name = "enabled",
            havingValue = "true", matchIfMissing = true)
    ControlTareasBlockingClient controlTareasBlockingClient() {
        return blockingProxy(ControlTareasBlockingClient.class);
    }

    /**
     * Bean del {@link ControlTareasReactiveClient} (WebClient).
     *
     * @return el proxy reactivo
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "simappe.shared.clients.reactive", name = "enabled",
            havingValue = "true", matchIfMissing = true)
    ControlTareasReactiveClient controlTareasReactiveClient() {
        return reactiveProxy(ControlTareasReactiveClient.class);
    }

    /**
     * Bean del {@link ExecutionTokenBlockingClient} (RestClient). Token de ejecucion del servidor de autorizacion; no lleva {@code Authorization}, se autentica con la clave de servicio.
     *
     * @return el proxy bloqueante
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "simappe.shared.clients.blocking", name = "enabled",
            havingValue = "true", matchIfMissing = true)
    ExecutionTokenBlockingClient executionTokenBlockingClient() {
        return blockingProxy(ExecutionTokenBlockingClient.class);
    }

    /**
     * Bean del {@link ExecutionTokenReactiveClient} (WebClient).
     *
     * @return el proxy reactivo
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "simappe.shared.clients.reactive", name = "enabled",
            havingValue = "true", matchIfMissing = true)
    ExecutionTokenReactiveClient executionTokenReactiveClient() {
        return reactiveProxy(ExecutionTokenReactiveClient.class);
    }

    /**
     * Bean del {@link UsuarioSimappeBlockingClient} (RestClient). Maestro de usuarios de simappe-configurations: resuelve el nombre y el correo de una persona a partir de su username.
     *
     * @return el proxy bloqueante
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "simappe.shared.clients.blocking", name = "enabled",
            havingValue = "true", matchIfMissing = true)
    UsuarioSimappeBlockingClient usuarioSimappeBlockingClient() {
        return blockingProxy(UsuarioSimappeBlockingClient.class);
    }

    /**
     * Bean del {@link UsuarioSimappeReactiveClient} (WebClient).
     *
     * @return el proxy reactivo
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "simappe.shared.clients.reactive", name = "enabled",
            havingValue = "true", matchIfMissing = true)
    UsuarioSimappeReactiveClient usuarioSimappeReactiveClient() {
        return reactiveProxy(UsuarioSimappeReactiveClient.class);
    }

    /**
     * Bean del {@link RolClienteBlockingClient} (RestClient). Roles del cliente con su precedencia, plataforma simappe-admin (HT_ARQ_012).
     *
     * @return el proxy bloqueante
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "simappe.shared.clients.blocking", name = "enabled",
            havingValue = "true", matchIfMissing = true)
    RolClienteBlockingClient rolClienteBlockingClient() {
        return blockingProxy(RolClienteBlockingClient.class);
    }

    /**
     * Bean del {@link RolClienteReactiveClient} (WebClient).
     *
     * @return el proxy reactivo
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "simappe.shared.clients.reactive", name = "enabled",
            havingValue = "true", matchIfMissing = true)
    RolClienteReactiveClient rolClienteReactiveClient() {
        return reactiveProxy(RolClienteReactiveClient.class);
    }
}
