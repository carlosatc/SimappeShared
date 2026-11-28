/*
 * Copyright (c) 2026 CatcSoft
 *
 * Author: torrescamargo@gmail.com
 */
package com.catcsoft.simappe.shared.unattended.config;

import java.time.Clock;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.convert.DurationStyle;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import com.catcsoft.simappe.commons.api.v1.core.unattended.ExecutionTokenIssuer;
import com.catcsoft.simappe.commons.api.v1.core.unattended.UnattendedTaskConfigSource;
import com.catcsoft.simappe.shared.unattended.adapter.AdminUnattendedTaskConfigSource;
import com.catcsoft.simappe.shared.unattended.adapter.OAuth2ExecutionTokenIssuer;
import com.catcsoft.simappe.shared.unattended.client.ExecutionTokenWebClient;
import com.catcsoft.simappe.shared.unattended.client.UnattendedTaskConfigWebClient;

import lombok.extern.slf4j.Slf4j;

/**
 * Lo que SimappeShared aporta a las tareas desatendidas de SimappeCommons: la
 * fuente de configuración (SimappeAdmin) y el emisor de tokens (OAuth2).
 *
 * <p>
 * No es restrictiva: se activa sola cuando el servicio tiene clave de servicio
 * ({@code simappe.unattended.service-key} o, por compatibilidad,
 * {@code nebula.execution-token.service-key}). Sin clave no crea nada y el
 * servicio arranca igual; si además tiene tareas desatendidas, SimappeCommons
 * detiene el arranque diciendo que falta el emisor.
 * </p>
 *
 * <pre>
 *   simappe.unattended.service-key   (o nebula.execution-token.service-key)
 *   simappe.unattended.client-timeout=30s
 *   simappe.admin.url=http://simappe-admin
 *   simappe.oauth2.url=http://simappe-oauth2-server
 * </pre>
 *
 * @author torrescamargo@gmail.com
 * @since 1.5.0
 */
@Slf4j
@AutoConfiguration
@ConditionalOnClass({ WebClient.class, UnattendedTaskConfigSource.class })
@ConditionalOnExpression("'${simappe.unattended.service-key:${nebula.execution-token.service-key:}}' != ''")
public class UnattendedClientsAutoConfiguration {

    /** Clave de servicio resuelta. */
    static final String SERVICE_KEY = "${simappe.unattended.service-key:${nebula.execution-token.service-key:}}";

    /** Espera máxima de las llamadas (formato de duración de Spring Boot). */
    static final String TIMEOUT = "${simappe.unattended.client-timeout:30s}";

    /**
     * @param builder builder de WebClient
     * @param baseUrl URL base de SimappeAdmin
     * @return cliente
     */
    @Bean
    @ConditionalOnMissingBean
    UnattendedTaskConfigWebClient unattendedTaskConfigWebClient(WebClient.Builder builder,
            @Value("${simappe.admin.url:http://simappe-admin}") String baseUrl) {
        return proxy(builder, baseUrl, UnattendedTaskConfigWebClient.class);
    }

    /**
     * @param builder builder de WebClient
     * @param baseUrl URL base del servidor OAuth2
     * @return cliente
     */
    @Bean
    @ConditionalOnMissingBean
    ExecutionTokenWebClient executionTokenWebClient(WebClient.Builder builder,
            @Value("${simappe.oauth2.url:http://simappe-oauth2-server}") String baseUrl) {
        return proxy(builder, baseUrl, ExecutionTokenWebClient.class);
    }

    /**
     * @param client  cliente de SimappeAdmin
     * @param timeout espera máxima
     * @return fuente de configuración
     */
    @Bean
    @ConditionalOnMissingBean
    UnattendedTaskConfigSource unattendedTaskConfigSource(UnattendedTaskConfigWebClient client,
            @Value(TIMEOUT) String timeout) {
        return new AdminUnattendedTaskConfigSource(client, DurationStyle.detectAndParse(timeout));
    }

    /**
     * @param client     cliente del servidor OAuth2
     * @param serviceKey clave de servicio
     * @param timeout    espera máxima
     * @return emisor
     */
    @Bean
    @ConditionalOnMissingBean
    ExecutionTokenIssuer executionTokenIssuer(ExecutionTokenWebClient client,
            @Value(SERVICE_KEY) String serviceKey,
            @Value(TIMEOUT) String timeout) {
        log.info("[SimappeShared] Emisor de tokens de ejecución listo");
        return new OAuth2ExecutionTokenIssuer(client, serviceKey, DurationStyle.detectAndParse(timeout),
                Clock.systemUTC());
    }

    private static <T> T proxy(WebClient.Builder builder, String baseUrl, Class<T> type) {
        WebClient webClient = builder.clone().baseUrl(baseUrl).build();
        return HttpServiceProxyFactory.builderFor(WebClientAdapter.create(webClient)).build().createClient(type);
    }
}
