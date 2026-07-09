/*
 * Copyright (c) 2024-2026 CatcSoft. Author: Carlos Torres Email: torrescamargo@gmail.com
 */
package com.catcsoft.simappe.shared.tenant.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import com.catcsoft.simappe.shared.notification.client.EmailNotificationWebClient;
import com.catcsoft.simappe.shared.registration.client.RegistrationWebClient;
import com.catcsoft.simappe.shared.tenant.client.TenantCustomerRoleWebClient;
import com.catcsoft.simappe.shared.tenant.client.TenantUserIdentityWebClient;

import lombok.extern.slf4j.Slf4j;

/**
 * Autoconfiguración de los WebClients tenant de SimappeShared, calcada del
 * patrón de los WebClients v2 de SimappeCommons ({@code @HttpExchange} +
 * {@code HttpServiceProxyFactory} sobre {@code WebClient}).
 *
 * <p>Propiedades (config-server):</p>
 * <pre>
 *   simappe.tenant.clients.enabled=true   (activa los beans; default false)
 *   simappe.admin.url=http://simappe-admin (base de SimappeAdmin, sin sufijo)
 * </pre>
 *
 * @author Carlos Torres
 * @version 1.0
 * @since 1.1.0
 * @see TenantUserIdentityWebClient
 * @see TenantCustomerRoleWebClient
 */
@Slf4j
@AutoConfiguration
@ConditionalOnClass(WebClient.class)
@ConditionalOnProperty(prefix = "simappe.tenant.clients", name = "enabled", havingValue = "true", matchIfMissing = false)
public class TenantClientsAutoConfiguration {

    /**
     * Crea el WebClient tenant de identidades de usuario.
     *
     * @param webClientBuilder builder de WebClient del contexto
     * @param baseUrl          URL base de SimappeAdmin (sin sufijo)
     * @return el proxy del cliente de identidades
     */
    @Bean
    @ConditionalOnMissingBean
    TenantUserIdentityWebClient tenantUserIdentityWebClient(
            WebClient.Builder webClientBuilder,
            @Value("${simappe.admin.url:http://simappe-admin}") String baseUrl) {
        log.info("[SimappeShared] Creando TenantUserIdentityWebClient con baseUrl: {}", baseUrl);
        return proxy(webClientBuilder, baseUrl, TenantUserIdentityWebClient.class);
    }

    /**
     * Crea el WebClient tenant de roles del cliente.
     *
     * @param webClientBuilder builder de WebClient del contexto
     * @param baseUrl          URL base de SimappeAdmin (sin sufijo)
     * @return el proxy del cliente de roles
     */
    @Bean
    @ConditionalOnMissingBean
    TenantCustomerRoleWebClient tenantCustomerRoleWebClient(
            WebClient.Builder webClientBuilder,
            @Value("${simappe.admin.url:http://simappe-admin}") String baseUrl) {
        log.info("[SimappeShared] Creando TenantCustomerRoleWebClient con baseUrl: {}", baseUrl);
        return proxy(webClientBuilder, baseUrl, TenantCustomerRoleWebClient.class);
    }

    /**
     * Crea el WebClient de notificaciones por correo de SimappeAdmin. Comparte
     * la misma base ({@code simappe.admin.url}) y flag de activación que los
     * clientes tenant, ya que todos apuntan a SimappeAdmin.
     *
     * @param webClientBuilder builder de WebClient del contexto
     * @param baseUrl          URL base de SimappeAdmin (sin sufijo)
     * @return el proxy del cliente de correo
     */
    @Bean
    @ConditionalOnMissingBean
    EmailNotificationWebClient emailNotificationWebClient(
            WebClient.Builder webClientBuilder,
            @Value("${simappe.admin.url:http://simappe-admin}") String baseUrl) {
        log.info("[SimappeShared] Creando EmailNotificationWebClient con baseUrl: {}", baseUrl);
        return proxy(webClientBuilder, baseUrl, EmailNotificationWebClient.class);
    }

    /**
     * Crea el WebClient del enlace de activación contra el servidor oauth2. Base
     * distinta a los clientes de admin ({@code simappe.oauth2.url}) porque los
     * endpoints seal/open viven en el servidor de autenticación.
     *
     * @param webClientBuilder builder de WebClient del contexto
     * @param baseUrl          URL base de oauth2 (sin sufijo)
     * @return el proxy del cliente de activación
     */
    @Bean
    @ConditionalOnMissingBean
    RegistrationWebClient registrationWebClient(
            WebClient.Builder webClientBuilder,
            @Value("${simappe.oauth2.url:http://simappe-oauth2-server}") String baseUrl) {
        log.info("[SimappeShared] Creando RegistrationWebClient con baseUrl: {}", baseUrl);
        return proxy(webClientBuilder, baseUrl, RegistrationWebClient.class);
    }

    /**
     * Construye un proxy {@code @HttpExchange} sobre WebClient.
     *
     * @param <T>              tipo de la interfaz del cliente
     * @param webClientBuilder builder de WebClient del contexto
     * @param baseUrl          URL base del servicio destino
     * @param clientType       interfaz del cliente
     * @return el proxy creado
     */
    private <T> T proxy(WebClient.Builder webClientBuilder, String baseUrl, Class<T> clientType) {
        WebClient webClient = webClientBuilder.baseUrl(baseUrl).build();
        return HttpServiceProxyFactory
                .builderFor(WebClientAdapter.create(webClient))
                .build()
                .createClient(clientType);
    }
}
