/*
 * Copyright (c) 2026 CatcSoft.
 *
 * This software is the confidential and proprietary information of CatcSoft
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of a
 * written license agreement entered into with CatcSoft.
 */
package com.catcsoft.simappe.shared.client.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.FilteredClassLoader;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.boot.test.context.runner.ReactiveWebApplicationContextRunner;
import org.springframework.mock.env.MockEnvironment;

import com.catcsoft.simappe.shared.client.admin.RolClienteReactiveClient;
import com.catcsoft.simappe.shared.client.admin.SimappeAdminBlockingClient;
import com.catcsoft.simappe.shared.client.configurations.TipoMaestroBlockingClient;
import com.catcsoft.simappe.shared.client.configurations.UsuarioSimappeBlockingClient;
import com.catcsoft.simappe.shared.client.oauth2.ExecutionTokenBlockingClient;
import com.catcsoft.simappe.shared.client.tareas.ControlTareasBlockingClient;
import com.catcsoft.simappe.shared.client.tareas.ControlTareasReactiveClient;
import com.catcsoft.simappe.shared.notification.client.EmailNotificationReactiveClient;
import com.catcsoft.simappe.shared.registration.client.RegistrationBlockingClient;
import com.catcsoft.simappe.shared.tenant.client.TenantBlockingClient;

/**
 * Los clientes compartidos solo existen si el servicio los activa, y cada uno va a la base de su
 * destino.
 */
class SharedClientsAutoConfigurationTest {

    /** Pares de cliente de SimappeShared (ver {@code ClientParityTest}). */
    private static final int PARES = 35;

    private final ApplicationContextRunner contexto = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(SharedClientsAutoConfiguration.class));

    @Test
    void noPropertiesRegistersNothing() {
        contexto.run(ctx -> assertThat(ctx).doesNotHaveBean(ControlTareasBlockingClient.class));
    }

    @Test
    void enabledClientsRegisterBothStyles() {
        contexto.withPropertyValues("simappe.shared.clients.enabled=true",
                "simappe.shared.clients.base-url=http://gateway:8090").run(ctx -> {
                    assertThat(ctx).hasSingleBean(ControlTareasBlockingClient.class);
                    assertThat(ctx).hasSingleBean(ControlTareasReactiveClient.class);
                    assertThat(ctx).hasSingleBean(ExecutionTokenBlockingClient.class);
                    assertThat(ctx).hasSingleBean(UsuarioSimappeBlockingClient.class);
                    assertThat(ctx).hasSingleBean(TipoMaestroBlockingClient.class);
                    assertThat(ctx).hasSingleBean(RolClienteReactiveClient.class);
                    assertThat(ctx).hasSingleBean(SimappeAdminBlockingClient.class);
                    assertThat(ctx).hasSingleBean(EmailNotificationReactiveClient.class);
                    assertThat(ctx).hasSingleBean(RegistrationBlockingClient.class);
                    assertThat(ctx).hasSingleBean(TenantBlockingClient.class);
                    long clientes = java.util.Arrays.stream(ctx.getBeanDefinitionNames())
                            .filter(n -> n.endsWith("BlockingClient") || n.endsWith("ReactiveClient")).count();
                    assertThat(clientes).isEqualTo(2L * PARES);
                });
    }

    @Test
    void reactiveStyleCanBeDisabled() {
        contexto.withPropertyValues("simappe.shared.clients.enabled=true",
                "simappe.shared.clients.reactive.enabled=false",
                "simappe.shared.clients.base-url=http://gateway:8090").run(ctx -> {
                    assertThat(ctx).hasSingleBean(ControlTareasBlockingClient.class);
                    assertThat(ctx).doesNotHaveBean(ControlTareasReactiveClient.class);
                });
    }

    @Test
    void reactiveWebApplicationHasNoBlockingClientsByDefault() {
        new ReactiveWebApplicationContextRunner()
                .withConfiguration(AutoConfigurations.of(SharedClientsAutoConfiguration.class))
                .withPropertyValues("simappe.shared.clients.enabled=true",
                        "simappe.shared.clients.base-url=http://gateway:8090")
                .run(ctx -> {
                    assertThat(ctx).doesNotHaveBean(ControlTareasBlockingClient.class);
                    assertThat(ctx).hasSingleBean(ControlTareasReactiveClient.class);
                });
    }

    @Test
    void reactiveWebApplicationCanAskForBlockingClients() {
        new ReactiveWebApplicationContextRunner()
                .withConfiguration(AutoConfigurations.of(SharedClientsAutoConfiguration.class))
                .withPropertyValues("simappe.shared.clients.enabled=true",
                        "simappe.shared.clients.blocking.enabled=true",
                        "simappe.shared.clients.base-url=http://gateway:8090")
                .run(ctx -> assertThat(ctx).hasSingleBean(ControlTareasBlockingClient.class));
    }

    @Test
    void blockingStyleCanBeDisabled() {
        contexto.withPropertyValues("simappe.shared.clients.enabled=true",
                "simappe.shared.clients.blocking.enabled=false",
                "simappe.shared.clients.base-url=http://gateway:8090").run(ctx -> {
                    assertThat(ctx).doesNotHaveBean(ControlTareasBlockingClient.class);
                    assertThat(ctx).hasSingleBean(ControlTareasReactiveClient.class);
                });
    }

    /**
     * Flujo servlet sin WebFlux ni Reactor: los clientes bloqueantes se registran y los reactivos no, sin que la
     * fábrica exija clases de WebFlux.
     */
    @Test
    void servletFlowWithoutWebFluxRegistersOnlyBlockingClients() {
        contexto.withClassLoader(new FilteredClassLoader("org.springframework.web.reactive", "reactor."))
                .withPropertyValues("simappe.shared.clients.enabled=true",
                        "simappe.shared.clients.base-url=http://gateway:8090")
                .run(ctx -> {
                    assertThat(ctx).hasNotFailed();
                    assertThat(ctx).hasSingleBean(ControlTareasBlockingClient.class);
                    assertThat(ctx).hasSingleBean(SimappeAdminBlockingClient.class);
                    assertThat(ctx).doesNotHaveBean(ReactiveClientFactory.class);
                    assertThat(ctx.getBeanNamesForType(ControlTareasReactiveClient.class)).isEmpty();
                });
    }

    @Test
    void eachTargetCanHaveItsOwnBase() {
        MockEnvironment environment = new MockEnvironment()
                .withProperty("simappe.shared.clients.base-url", "http://gateway:8090")
                .withProperty("simappe.shared.clients.urls.oauth2", "http://simappe-oauth2-server:8787");
        ClientFactory factory = new ClientFactory(environment);
        assertThat(factory.base("oauth2")).isEqualTo("http://simappe-oauth2-server:8787");
        assertThat(factory.base("admin")).isEqualTo("http://gateway:8090");
    }
}
