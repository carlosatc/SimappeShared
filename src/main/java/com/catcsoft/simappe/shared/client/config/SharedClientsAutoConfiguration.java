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

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestClient;
import org.springframework.web.reactive.function.client.WebClient;

import com.catcsoft.simappe.shared.client.admin.AnalyticsBlockingClient;
import com.catcsoft.simappe.shared.client.admin.AnalyticsReactiveClient;
import com.catcsoft.simappe.shared.client.admin.AuthBlockingClient;
import com.catcsoft.simappe.shared.client.admin.AuthReactiveClient;
import com.catcsoft.simappe.shared.client.admin.BillingBlockingClient;
import com.catcsoft.simappe.shared.client.admin.BillingReactiveClient;
import com.catcsoft.simappe.shared.client.admin.BrandingBlockingClient;
import com.catcsoft.simappe.shared.client.admin.BrandingReactiveClient;
import com.catcsoft.simappe.shared.client.admin.ComplianceBlockingClient;
import com.catcsoft.simappe.shared.client.admin.ComplianceReactiveClient;
import com.catcsoft.simappe.shared.client.admin.ConfigurationBlockingClient;
import com.catcsoft.simappe.shared.client.admin.ConfigurationReactiveClient;
import com.catcsoft.simappe.shared.client.admin.CurrencyBlockingClient;
import com.catcsoft.simappe.shared.client.admin.CurrencyReactiveClient;
import com.catcsoft.simappe.shared.client.admin.DatabaseBlockingClient;
import com.catcsoft.simappe.shared.client.admin.DatabaseReactiveClient;
import com.catcsoft.simappe.shared.client.admin.ExchangeRateBlockingClient;
import com.catcsoft.simappe.shared.client.admin.ExchangeRateReactiveClient;
import com.catcsoft.simappe.shared.client.admin.FeatureBlockingClient;
import com.catcsoft.simappe.shared.client.admin.FeatureReactiveClient;
import com.catcsoft.simappe.shared.client.admin.GeographyBlockingClient;
import com.catcsoft.simappe.shared.client.admin.GeographyReactiveClient;
import com.catcsoft.simappe.shared.client.admin.I18nBlockingClient;
import com.catcsoft.simappe.shared.client.admin.I18nReactiveClient;
import com.catcsoft.simappe.shared.client.admin.IntegrationBlockingClient;
import com.catcsoft.simappe.shared.client.admin.IntegrationReactiveClient;
import com.catcsoft.simappe.shared.client.admin.LicensingBlockingClient;
import com.catcsoft.simappe.shared.client.admin.LicensingReactiveClient;
import com.catcsoft.simappe.shared.client.admin.MaintenanceBlockingClient;
import com.catcsoft.simappe.shared.client.admin.MaintenanceReactiveClient;
import com.catcsoft.simappe.shared.client.admin.MiscBlockingClient;
import com.catcsoft.simappe.shared.client.admin.MiscReactiveClient;
import com.catcsoft.simappe.shared.client.admin.MonitoringBlockingClient;
import com.catcsoft.simappe.shared.client.admin.MonitoringReactiveClient;
import com.catcsoft.simappe.shared.client.admin.OAuthBlockingClient;
import com.catcsoft.simappe.shared.client.admin.OAuthReactiveClient;
import com.catcsoft.simappe.shared.client.admin.OrganizationBlockingClient;
import com.catcsoft.simappe.shared.client.admin.OrganizationReactiveClient;
import com.catcsoft.simappe.shared.client.admin.RolClienteBlockingClient;
import com.catcsoft.simappe.shared.client.admin.RolClienteReactiveClient;
import com.catcsoft.simappe.shared.client.admin.SecurityBlockingClient;
import com.catcsoft.simappe.shared.client.admin.SecurityReactiveClient;
import com.catcsoft.simappe.shared.client.admin.SimappeAdminBlockingClient;
import com.catcsoft.simappe.shared.client.admin.SimappeAdminReactiveClient;
import com.catcsoft.simappe.shared.client.admin.SystemBlockingClient;
import com.catcsoft.simappe.shared.client.admin.SystemReactiveClient;
import com.catcsoft.simappe.shared.client.admin.WebhookBlockingClient;
import com.catcsoft.simappe.shared.client.admin.WebhookReactiveClient;
import com.catcsoft.simappe.shared.client.client.SimappeClientBlockingClient;
import com.catcsoft.simappe.shared.client.client.SimappeClientReactiveClient;
import com.catcsoft.simappe.shared.client.configurations.TipoMaestroBlockingClient;
import com.catcsoft.simappe.shared.client.configurations.TipoMaestroReactiveClient;
import com.catcsoft.simappe.shared.client.configurations.UsuarioSimappeBlockingClient;
import com.catcsoft.simappe.shared.client.configurations.UsuarioSimappeReactiveClient;
import com.catcsoft.simappe.shared.client.oauth2.ExecutionTokenBlockingClient;
import com.catcsoft.simappe.shared.client.oauth2.ExecutionTokenReactiveClient;
import com.catcsoft.simappe.shared.client.tareas.ControlTareasBlockingClient;
import com.catcsoft.simappe.shared.client.tareas.ControlTareasReactiveClient;
import com.catcsoft.simappe.shared.identity.client.UserIdentityBlockingClient;
import com.catcsoft.simappe.shared.identity.client.UserIdentityReactiveClient;
import com.catcsoft.simappe.shared.notification.client.EmailNotificationBlockingClient;
import com.catcsoft.simappe.shared.notification.client.EmailNotificationReactiveClient;
import com.catcsoft.simappe.shared.registration.client.RegistrationBlockingClient;
import com.catcsoft.simappe.shared.registration.client.RegistrationReactiveClient;
import com.catcsoft.simappe.shared.tenant.client.TenantBlockingClient;
import com.catcsoft.simappe.shared.tenant.client.TenantCustomerRoleBlockingClient;
import com.catcsoft.simappe.shared.tenant.client.TenantCustomerRoleReactiveClient;
import com.catcsoft.simappe.shared.tenant.client.TenantReactiveClient;
import com.catcsoft.simappe.shared.tenant.client.TenantUserIdentityBlockingClient;
import com.catcsoft.simappe.shared.tenant.client.TenantUserIdentityReactiveClient;

/**
 * Fábrica única de los clientes de SimappeShared: crea el proxy {@code @HttpExchange} de cada
 * {@code XxxBlockingClient} (RestClient) y de cada {@code XxxReactiveClient} (WebClient). Los
 * servicios consumidores solo inyectan el bean, sin definirlo.
 *
 * <p>Activación opt-in: {@code simappe.shared.clients.enabled=true}. Cada estilo existe si su
 * cliente HTTP está en el classpath y no se apaga por propiedad:</p>
 * <ul>
 *   <li>{@code simappe.shared.clients.blocking.enabled}: RestClient. Sin la propiedad, existe en una aplicación
 *   servlet o sin web y no existe en una aplicación web reactiva ({@link BlockingClientsCondition})</li>
 *   <li>{@code simappe.shared.clients.reactive.enabled} (por defecto true): WebClient</li>
 * </ul>
 *
 * <p>Base (config-server): {@code simappe.shared.clients.base-url} (el gateway, por ejemplo
 * {@code http://simappe-gateway-server:8090}). Un servicio destino puede ir por otra base con
 * {@code simappe.shared.clients.urls.<destino>}; los destinos son {@code admin}, {@code client},
 * {@code configurations}, {@code oauth2} y {@code tasks}. {@code simappe.shared.clients.environment}
 * va en la cabecera {@code x-simappe-environment}; la cabecera de la clave de servicio toma su nombre de
 * {@code simappe.security.service-key-header}. El token de la sesión no se configura aquí: se pasa
 * explícito por llamada ({@code Authorization: Bearer}).</p>
 *
 * @author Carlos Torres
 * @since 2.5.0
 */
@AutoConfiguration
@ConditionalOnProperty(prefix = "simappe.shared.clients", name = "enabled", havingValue = "true")
public class SharedClientsAutoConfiguration {

    /**
     * La fábrica de proxies sobre la configuración del servicio.
     *
     * @param environment propiedades del servicio
     * @return la fábrica
     */
    @Bean
    @ConditionalOnMissingBean
    ClientFactory simappeSharedClientFactory(Environment environment) {
        return new ClientFactory(environment);
    }

    /**
     * Los clientes bloqueantes ({@code XxxBlockingClient} sobre RestClient).
     */
    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass(RestClient.class)
    @Conditional(BlockingClientsCondition.class)
    static class BlockingClients {

        /** Fábrica de proxies. */
        private final ClientFactory factory;

        /**
         * @param factory         fábrica de proxies
         * @param restClientBuilder builder de RestClient del contexto, si existe
         */
        BlockingClients(ClientFactory factory, ObjectProvider<RestClient.Builder> restClientBuilder) {
            this.factory = factory.withRestClient(restClientBuilder.getIfAvailable(RestClient::builder));
        }

        /**
         * Proxy bloqueante de {@link AnalyticsBlockingClient} (destino {@code admin}).
         *
         * @return el proxy
         */
        @Bean
        @ConditionalOnMissingBean
        AnalyticsBlockingClient analyticsBlockingClient() {
            return factory.blocking(AnalyticsBlockingClient.class, "admin");
        }

        /**
         * Proxy bloqueante de {@link AuthBlockingClient} (destino {@code admin}).
         *
         * @return el proxy
         */
        @Bean
        @ConditionalOnMissingBean
        AuthBlockingClient authBlockingClient() {
            return factory.blocking(AuthBlockingClient.class, "admin");
        }

        /**
         * Proxy bloqueante de {@link BillingBlockingClient} (destino {@code admin}).
         *
         * @return el proxy
         */
        @Bean
        @ConditionalOnMissingBean
        BillingBlockingClient billingBlockingClient() {
            return factory.blocking(BillingBlockingClient.class, "admin");
        }

        /**
         * Proxy bloqueante de {@link BrandingBlockingClient} (destino {@code admin}).
         *
         * @return el proxy
         */
        @Bean
        @ConditionalOnMissingBean
        BrandingBlockingClient brandingBlockingClient() {
            return factory.blocking(BrandingBlockingClient.class, "admin");
        }

        /**
         * Proxy bloqueante de {@link ComplianceBlockingClient} (destino {@code admin}).
         *
         * @return el proxy
         */
        @Bean
        @ConditionalOnMissingBean
        ComplianceBlockingClient complianceBlockingClient() {
            return factory.blocking(ComplianceBlockingClient.class, "admin");
        }

        /**
         * Proxy bloqueante de {@link ConfigurationBlockingClient} (destino {@code admin}).
         *
         * @return el proxy
         */
        @Bean
        @ConditionalOnMissingBean
        ConfigurationBlockingClient configurationBlockingClient() {
            return factory.blocking(ConfigurationBlockingClient.class, "admin");
        }

        /**
         * Proxy bloqueante de {@link CurrencyBlockingClient} (destino {@code admin}).
         *
         * @return el proxy
         */
        @Bean
        @ConditionalOnMissingBean
        CurrencyBlockingClient currencyBlockingClient() {
            return factory.blocking(CurrencyBlockingClient.class, "admin");
        }

        /**
         * Proxy bloqueante de {@link DatabaseBlockingClient} (destino {@code admin}).
         *
         * @return el proxy
         */
        @Bean
        @ConditionalOnMissingBean
        DatabaseBlockingClient databaseBlockingClient() {
            return factory.blocking(DatabaseBlockingClient.class, "admin");
        }

        /**
         * Proxy bloqueante de {@link EmailNotificationBlockingClient} (destino {@code admin}).
         *
         * @return el proxy
         */
        @Bean
        @ConditionalOnMissingBean
        EmailNotificationBlockingClient emailNotificationBlockingClient() {
            return factory.blocking(EmailNotificationBlockingClient.class, "admin");
        }

        /**
         * Proxy bloqueante de {@link ExchangeRateBlockingClient} (destino {@code admin}).
         *
         * @return el proxy
         */
        @Bean
        @ConditionalOnMissingBean
        ExchangeRateBlockingClient exchangeRateBlockingClient() {
            return factory.blocking(ExchangeRateBlockingClient.class, "admin");
        }

        /**
         * Proxy bloqueante de {@link FeatureBlockingClient} (destino {@code admin}).
         *
         * @return el proxy
         */
        @Bean
        @ConditionalOnMissingBean
        FeatureBlockingClient featureBlockingClient() {
            return factory.blocking(FeatureBlockingClient.class, "admin");
        }

        /**
         * Proxy bloqueante de {@link GeographyBlockingClient} (destino {@code admin}).
         *
         * @return el proxy
         */
        @Bean
        @ConditionalOnMissingBean
        GeographyBlockingClient geographyBlockingClient() {
            return factory.blocking(GeographyBlockingClient.class, "admin");
        }

        /**
         * Proxy bloqueante de {@link I18nBlockingClient} (destino {@code admin}).
         *
         * @return el proxy
         */
        @Bean
        @ConditionalOnMissingBean
        I18nBlockingClient i18nBlockingClient() {
            return factory.blocking(I18nBlockingClient.class, "admin");
        }

        /**
         * Proxy bloqueante de {@link IntegrationBlockingClient} (destino {@code admin}).
         *
         * @return el proxy
         */
        @Bean
        @ConditionalOnMissingBean
        IntegrationBlockingClient integrationBlockingClient() {
            return factory.blocking(IntegrationBlockingClient.class, "admin");
        }

        /**
         * Proxy bloqueante de {@link LicensingBlockingClient} (destino {@code admin}).
         *
         * @return el proxy
         */
        @Bean
        @ConditionalOnMissingBean
        LicensingBlockingClient licensingBlockingClient() {
            return factory.blocking(LicensingBlockingClient.class, "admin");
        }

        /**
         * Proxy bloqueante de {@link MaintenanceBlockingClient} (destino {@code admin}).
         *
         * @return el proxy
         */
        @Bean
        @ConditionalOnMissingBean
        MaintenanceBlockingClient maintenanceBlockingClient() {
            return factory.blocking(MaintenanceBlockingClient.class, "admin");
        }

        /**
         * Proxy bloqueante de {@link MiscBlockingClient} (destino {@code admin}).
         *
         * @return el proxy
         */
        @Bean
        @ConditionalOnMissingBean
        MiscBlockingClient miscBlockingClient() {
            return factory.blocking(MiscBlockingClient.class, "admin");
        }

        /**
         * Proxy bloqueante de {@link MonitoringBlockingClient} (destino {@code admin}).
         *
         * @return el proxy
         */
        @Bean
        @ConditionalOnMissingBean
        MonitoringBlockingClient monitoringBlockingClient() {
            return factory.blocking(MonitoringBlockingClient.class, "admin");
        }

        /**
         * Proxy bloqueante de {@link OAuthBlockingClient} (destino {@code admin}).
         *
         * @return el proxy
         */
        @Bean
        @ConditionalOnMissingBean
        OAuthBlockingClient oAuthBlockingClient() {
            return factory.blocking(OAuthBlockingClient.class, "admin");
        }

        /**
         * Proxy bloqueante de {@link OrganizationBlockingClient} (destino {@code admin}).
         *
         * @return el proxy
         */
        @Bean
        @ConditionalOnMissingBean
        OrganizationBlockingClient organizationBlockingClient() {
            return factory.blocking(OrganizationBlockingClient.class, "admin");
        }

        /**
         * Proxy bloqueante de {@link RolClienteBlockingClient} (destino {@code admin}).
         *
         * @return el proxy
         */
        @Bean
        @ConditionalOnMissingBean
        RolClienteBlockingClient rolClienteBlockingClient() {
            return factory.blocking(RolClienteBlockingClient.class, "admin");
        }

        /**
         * Proxy bloqueante de {@link SecurityBlockingClient} (destino {@code admin}).
         *
         * @return el proxy
         */
        @Bean
        @ConditionalOnMissingBean
        SecurityBlockingClient securityBlockingClient() {
            return factory.blocking(SecurityBlockingClient.class, "admin");
        }

        /**
         * Proxy bloqueante de {@link SimappeAdminBlockingClient} (destino {@code admin}).
         *
         * @return el proxy
         */
        @Bean
        @ConditionalOnMissingBean
        SimappeAdminBlockingClient simappeAdminBlockingClient() {
            return factory.blocking(SimappeAdminBlockingClient.class, "admin");
        }

        /**
         * Proxy bloqueante de {@link SystemBlockingClient} (destino {@code admin}).
         *
         * @return el proxy
         */
        @Bean
        @ConditionalOnMissingBean
        SystemBlockingClient systemBlockingClient() {
            return factory.blocking(SystemBlockingClient.class, "admin");
        }

        /**
         * Proxy bloqueante de {@link TenantBlockingClient} (destino {@code admin}).
         *
         * @return el proxy
         */
        @Bean
        @ConditionalOnMissingBean
        TenantBlockingClient tenantBlockingClient() {
            return factory.blocking(TenantBlockingClient.class, "admin");
        }

        /**
         * Proxy bloqueante de {@link TenantCustomerRoleBlockingClient} (destino {@code admin}).
         *
         * @return el proxy
         */
        @Bean
        @ConditionalOnMissingBean
        TenantCustomerRoleBlockingClient tenantCustomerRoleBlockingClient() {
            return factory.blocking(TenantCustomerRoleBlockingClient.class, "admin");
        }

        /**
         * Proxy bloqueante de {@link TenantUserIdentityBlockingClient} (destino {@code admin}).
         *
         * @return el proxy
         */
        @Bean
        @ConditionalOnMissingBean
        TenantUserIdentityBlockingClient tenantUserIdentityBlockingClient() {
            return factory.blocking(TenantUserIdentityBlockingClient.class, "admin");
        }

        /**
         * Proxy bloqueante de {@link UserIdentityBlockingClient} (destino {@code admin}).
         *
         * @return el proxy
         */
        @Bean
        @ConditionalOnMissingBean
        UserIdentityBlockingClient userIdentityBlockingClient() {
            return factory.blocking(UserIdentityBlockingClient.class, "admin");
        }

        /**
         * Proxy bloqueante de {@link WebhookBlockingClient} (destino {@code admin}).
         *
         * @return el proxy
         */
        @Bean
        @ConditionalOnMissingBean
        WebhookBlockingClient webhookBlockingClient() {
            return factory.blocking(WebhookBlockingClient.class, "admin");
        }

        /**
         * Proxy bloqueante de {@link SimappeClientBlockingClient} (destino {@code client}).
         *
         * @return el proxy
         */
        @Bean
        @ConditionalOnMissingBean
        SimappeClientBlockingClient simappeClientBlockingClient() {
            return factory.blocking(SimappeClientBlockingClient.class, "client");
        }

        /**
         * Proxy bloqueante de {@link TipoMaestroBlockingClient} (destino {@code configurations}).
         *
         * @return el proxy
         */
        @Bean
        @ConditionalOnMissingBean
        TipoMaestroBlockingClient tipoMaestroBlockingClient() {
            return factory.blocking(TipoMaestroBlockingClient.class, "configurations");
        }

        /**
         * Proxy bloqueante de {@link UsuarioSimappeBlockingClient} (destino {@code configurations}).
         *
         * @return el proxy
         */
        @Bean
        @ConditionalOnMissingBean
        UsuarioSimappeBlockingClient usuarioSimappeBlockingClient() {
            return factory.blocking(UsuarioSimappeBlockingClient.class, "configurations");
        }

        /**
         * Proxy bloqueante de {@link ExecutionTokenBlockingClient} (destino {@code oauth2}).
         *
         * @return el proxy
         */
        @Bean
        @ConditionalOnMissingBean
        ExecutionTokenBlockingClient executionTokenBlockingClient() {
            return factory.blocking(ExecutionTokenBlockingClient.class, "oauth2");
        }

        /**
         * Proxy bloqueante de {@link RegistrationBlockingClient} (destino {@code oauth2}).
         *
         * @return el proxy
         */
        @Bean
        @ConditionalOnMissingBean
        RegistrationBlockingClient registrationBlockingClient() {
            return factory.blocking(RegistrationBlockingClient.class, "oauth2");
        }

        /**
         * Proxy bloqueante de {@link ControlTareasBlockingClient} (destino {@code tasks}).
         *
         * @return el proxy
         */
        @Bean
        @ConditionalOnMissingBean
        ControlTareasBlockingClient controlTareasBlockingClient() {
            return factory.blocking(ControlTareasBlockingClient.class, "tasks");
        }
    }

    /**
     * Los clientes reactivos ({@code XxxReactiveClient} sobre WebClient).
     */
    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass(WebClient.class)
    @ConditionalOnProperty(prefix = "simappe.shared.clients.reactive", name = "enabled",
            havingValue = "true", matchIfMissing = true)
    static class ReactiveClients {

        /** Fábrica de proxies reactivos. */
        private final ReactiveClientFactory factory;

        /**
         * @param factory          fábrica de proxies
         * @param webClientBuilder builder de WebClient del contexto, si existe
         */
        ReactiveClients(ClientFactory factory, ObjectProvider<WebClient.Builder> webClientBuilder) {
            this.factory = new ReactiveClientFactory(factory, webClientBuilder.getIfAvailable(WebClient::builder));
        }

        /**
         * Proxy reactivo de {@link AnalyticsReactiveClient} (destino {@code admin}).
         *
         * @return el proxy
         */
        @Bean
        @ConditionalOnMissingBean
        AnalyticsReactiveClient analyticsReactiveClient() {
            return factory.reactive(AnalyticsReactiveClient.class, "admin");
        }

        /**
         * Proxy reactivo de {@link AuthReactiveClient} (destino {@code admin}).
         *
         * @return el proxy
         */
        @Bean
        @ConditionalOnMissingBean
        AuthReactiveClient authReactiveClient() {
            return factory.reactive(AuthReactiveClient.class, "admin");
        }

        /**
         * Proxy reactivo de {@link BillingReactiveClient} (destino {@code admin}).
         *
         * @return el proxy
         */
        @Bean
        @ConditionalOnMissingBean
        BillingReactiveClient billingReactiveClient() {
            return factory.reactive(BillingReactiveClient.class, "admin");
        }

        /**
         * Proxy reactivo de {@link BrandingReactiveClient} (destino {@code admin}).
         *
         * @return el proxy
         */
        @Bean
        @ConditionalOnMissingBean
        BrandingReactiveClient brandingReactiveClient() {
            return factory.reactive(BrandingReactiveClient.class, "admin");
        }

        /**
         * Proxy reactivo de {@link ComplianceReactiveClient} (destino {@code admin}).
         *
         * @return el proxy
         */
        @Bean
        @ConditionalOnMissingBean
        ComplianceReactiveClient complianceReactiveClient() {
            return factory.reactive(ComplianceReactiveClient.class, "admin");
        }

        /**
         * Proxy reactivo de {@link ConfigurationReactiveClient} (destino {@code admin}).
         *
         * @return el proxy
         */
        @Bean
        @ConditionalOnMissingBean
        ConfigurationReactiveClient configurationReactiveClient() {
            return factory.reactive(ConfigurationReactiveClient.class, "admin");
        }

        /**
         * Proxy reactivo de {@link CurrencyReactiveClient} (destino {@code admin}).
         *
         * @return el proxy
         */
        @Bean
        @ConditionalOnMissingBean
        CurrencyReactiveClient currencyReactiveClient() {
            return factory.reactive(CurrencyReactiveClient.class, "admin");
        }

        /**
         * Proxy reactivo de {@link DatabaseReactiveClient} (destino {@code admin}).
         *
         * @return el proxy
         */
        @Bean
        @ConditionalOnMissingBean
        DatabaseReactiveClient databaseReactiveClient() {
            return factory.reactive(DatabaseReactiveClient.class, "admin");
        }

        /**
         * Proxy reactivo de {@link EmailNotificationReactiveClient} (destino {@code admin}).
         *
         * @return el proxy
         */
        @Bean
        @ConditionalOnMissingBean
        EmailNotificationReactiveClient emailNotificationReactiveClient() {
            return factory.reactive(EmailNotificationReactiveClient.class, "admin");
        }

        /**
         * Proxy reactivo de {@link ExchangeRateReactiveClient} (destino {@code admin}).
         *
         * @return el proxy
         */
        @Bean
        @ConditionalOnMissingBean
        ExchangeRateReactiveClient exchangeRateReactiveClient() {
            return factory.reactive(ExchangeRateReactiveClient.class, "admin");
        }

        /**
         * Proxy reactivo de {@link FeatureReactiveClient} (destino {@code admin}).
         *
         * @return el proxy
         */
        @Bean
        @ConditionalOnMissingBean
        FeatureReactiveClient featureReactiveClient() {
            return factory.reactive(FeatureReactiveClient.class, "admin");
        }

        /**
         * Proxy reactivo de {@link GeographyReactiveClient} (destino {@code admin}).
         *
         * @return el proxy
         */
        @Bean
        @ConditionalOnMissingBean
        GeographyReactiveClient geographyReactiveClient() {
            return factory.reactive(GeographyReactiveClient.class, "admin");
        }

        /**
         * Proxy reactivo de {@link I18nReactiveClient} (destino {@code admin}).
         *
         * @return el proxy
         */
        @Bean
        @ConditionalOnMissingBean
        I18nReactiveClient i18nReactiveClient() {
            return factory.reactive(I18nReactiveClient.class, "admin");
        }

        /**
         * Proxy reactivo de {@link IntegrationReactiveClient} (destino {@code admin}).
         *
         * @return el proxy
         */
        @Bean
        @ConditionalOnMissingBean
        IntegrationReactiveClient integrationReactiveClient() {
            return factory.reactive(IntegrationReactiveClient.class, "admin");
        }

        /**
         * Proxy reactivo de {@link LicensingReactiveClient} (destino {@code admin}).
         *
         * @return el proxy
         */
        @Bean
        @ConditionalOnMissingBean
        LicensingReactiveClient licensingReactiveClient() {
            return factory.reactive(LicensingReactiveClient.class, "admin");
        }

        /**
         * Proxy reactivo de {@link MaintenanceReactiveClient} (destino {@code admin}).
         *
         * @return el proxy
         */
        @Bean
        @ConditionalOnMissingBean
        MaintenanceReactiveClient maintenanceReactiveClient() {
            return factory.reactive(MaintenanceReactiveClient.class, "admin");
        }

        /**
         * Proxy reactivo de {@link MiscReactiveClient} (destino {@code admin}).
         *
         * @return el proxy
         */
        @Bean
        @ConditionalOnMissingBean
        MiscReactiveClient miscReactiveClient() {
            return factory.reactive(MiscReactiveClient.class, "admin");
        }

        /**
         * Proxy reactivo de {@link MonitoringReactiveClient} (destino {@code admin}).
         *
         * @return el proxy
         */
        @Bean
        @ConditionalOnMissingBean
        MonitoringReactiveClient monitoringReactiveClient() {
            return factory.reactive(MonitoringReactiveClient.class, "admin");
        }

        /**
         * Proxy reactivo de {@link OAuthReactiveClient} (destino {@code admin}).
         *
         * @return el proxy
         */
        @Bean
        @ConditionalOnMissingBean
        OAuthReactiveClient oAuthReactiveClient() {
            return factory.reactive(OAuthReactiveClient.class, "admin");
        }

        /**
         * Proxy reactivo de {@link OrganizationReactiveClient} (destino {@code admin}).
         *
         * @return el proxy
         */
        @Bean
        @ConditionalOnMissingBean
        OrganizationReactiveClient organizationReactiveClient() {
            return factory.reactive(OrganizationReactiveClient.class, "admin");
        }

        /**
         * Proxy reactivo de {@link RolClienteReactiveClient} (destino {@code admin}).
         *
         * @return el proxy
         */
        @Bean
        @ConditionalOnMissingBean
        RolClienteReactiveClient rolClienteReactiveClient() {
            return factory.reactive(RolClienteReactiveClient.class, "admin");
        }

        /**
         * Proxy reactivo de {@link SecurityReactiveClient} (destino {@code admin}).
         *
         * @return el proxy
         */
        @Bean
        @ConditionalOnMissingBean
        SecurityReactiveClient securityReactiveClient() {
            return factory.reactive(SecurityReactiveClient.class, "admin");
        }

        /**
         * Proxy reactivo de {@link SimappeAdminReactiveClient} (destino {@code admin}).
         *
         * @return el proxy
         */
        @Bean
        @ConditionalOnMissingBean
        SimappeAdminReactiveClient simappeAdminReactiveClient() {
            return factory.reactive(SimappeAdminReactiveClient.class, "admin");
        }

        /**
         * Proxy reactivo de {@link SystemReactiveClient} (destino {@code admin}).
         *
         * @return el proxy
         */
        @Bean
        @ConditionalOnMissingBean
        SystemReactiveClient systemReactiveClient() {
            return factory.reactive(SystemReactiveClient.class, "admin");
        }

        /**
         * Proxy reactivo de {@link TenantReactiveClient} (destino {@code admin}).
         *
         * @return el proxy
         */
        @Bean
        @ConditionalOnMissingBean
        TenantReactiveClient tenantReactiveClient() {
            return factory.reactive(TenantReactiveClient.class, "admin");
        }

        /**
         * Proxy reactivo de {@link TenantCustomerRoleReactiveClient} (destino {@code admin}).
         *
         * @return el proxy
         */
        @Bean
        @ConditionalOnMissingBean
        TenantCustomerRoleReactiveClient tenantCustomerRoleReactiveClient() {
            return factory.reactive(TenantCustomerRoleReactiveClient.class, "admin");
        }

        /**
         * Proxy reactivo de {@link TenantUserIdentityReactiveClient} (destino {@code admin}).
         *
         * @return el proxy
         */
        @Bean
        @ConditionalOnMissingBean
        TenantUserIdentityReactiveClient tenantUserIdentityReactiveClient() {
            return factory.reactive(TenantUserIdentityReactiveClient.class, "admin");
        }

        /**
         * Proxy reactivo de {@link UserIdentityReactiveClient} (destino {@code admin}).
         *
         * @return el proxy
         */
        @Bean
        @ConditionalOnMissingBean
        UserIdentityReactiveClient userIdentityReactiveClient() {
            return factory.reactive(UserIdentityReactiveClient.class, "admin");
        }

        /**
         * Proxy reactivo de {@link WebhookReactiveClient} (destino {@code admin}).
         *
         * @return el proxy
         */
        @Bean
        @ConditionalOnMissingBean
        WebhookReactiveClient webhookReactiveClient() {
            return factory.reactive(WebhookReactiveClient.class, "admin");
        }

        /**
         * Proxy reactivo de {@link SimappeClientReactiveClient} (destino {@code client}).
         *
         * @return el proxy
         */
        @Bean
        @ConditionalOnMissingBean
        SimappeClientReactiveClient simappeClientReactiveClient() {
            return factory.reactive(SimappeClientReactiveClient.class, "client");
        }

        /**
         * Proxy reactivo de {@link TipoMaestroReactiveClient} (destino {@code configurations}).
         *
         * @return el proxy
         */
        @Bean
        @ConditionalOnMissingBean
        TipoMaestroReactiveClient tipoMaestroReactiveClient() {
            return factory.reactive(TipoMaestroReactiveClient.class, "configurations");
        }

        /**
         * Proxy reactivo de {@link UsuarioSimappeReactiveClient} (destino {@code configurations}).
         *
         * @return el proxy
         */
        @Bean
        @ConditionalOnMissingBean
        UsuarioSimappeReactiveClient usuarioSimappeReactiveClient() {
            return factory.reactive(UsuarioSimappeReactiveClient.class, "configurations");
        }

        /**
         * Proxy reactivo de {@link ExecutionTokenReactiveClient} (destino {@code oauth2}).
         *
         * @return el proxy
         */
        @Bean
        @ConditionalOnMissingBean
        ExecutionTokenReactiveClient executionTokenReactiveClient() {
            return factory.reactive(ExecutionTokenReactiveClient.class, "oauth2");
        }

        /**
         * Proxy reactivo de {@link RegistrationReactiveClient} (destino {@code oauth2}).
         *
         * @return el proxy
         */
        @Bean
        @ConditionalOnMissingBean
        RegistrationReactiveClient registrationReactiveClient() {
            return factory.reactive(RegistrationReactiveClient.class, "oauth2");
        }

        /**
         * Proxy reactivo de {@link ControlTareasReactiveClient} (destino {@code tasks}).
         *
         * @return el proxy
         */
        @Bean
        @ConditionalOnMissingBean
        ControlTareasReactiveClient controlTareasReactiveClient() {
            return factory.reactive(ControlTareasReactiveClient.class, "tasks");
        }
    }
}
