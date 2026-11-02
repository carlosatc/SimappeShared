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
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import com.catcsoft.simappe.shared.client.admin.RolClienteReactiveClient;
import com.catcsoft.simappe.shared.client.configurations.TipoMaestroBlockingClient;
import com.catcsoft.simappe.shared.client.configurations.UsuarioSimappeBlockingClient;
import com.catcsoft.simappe.shared.client.oauth2.ExecutionTokenBlockingClient;
import com.catcsoft.simappe.shared.client.tareas.ControlTareasBlockingClient;
import com.catcsoft.simappe.shared.client.tareas.ControlTareasReactiveClient;

/**
 * Los clientes compartidos solo existen si el servicio los activa.
 */
class SharedClientsAutoConfigurationTest {

    private final ApplicationContextRunner contexto = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(SharedClientsAutoConfiguration.class));

    @Test
    void sinPropiedadesNoSeRegistraNada() {
        contexto.run(ctx -> {
            assertThat(ctx).doesNotHaveBean(ControlTareasBlockingClient.class);
        });
    }

    @Test
    void conClientesActivosSeRegistranAmbosEstilos() {
        contexto.withPropertyValues("simappe.shared.clients.enabled=true",
                "simappe.shared.clients.base-url=http://gateway:8090").run(ctx -> {
                    assertThat(ctx).hasSingleBean(ControlTareasBlockingClient.class);
                    assertThat(ctx).hasSingleBean(ControlTareasReactiveClient.class);
                    assertThat(ctx).hasSingleBean(ExecutionTokenBlockingClient.class);
                    assertThat(ctx).hasSingleBean(UsuarioSimappeBlockingClient.class);
                    assertThat(ctx).hasSingleBean(TipoMaestroBlockingClient.class);
                    assertThat(ctx).hasSingleBean(RolClienteReactiveClient.class);
                        });
    }

    @Test
    void elEstiloReactivoSePuedeApagar() {
        contexto.withPropertyValues("simappe.shared.clients.enabled=true",
                "simappe.shared.clients.reactive.enabled=false",
                "simappe.shared.clients.base-url=http://gateway:8090").run(ctx -> {
                    assertThat(ctx).hasSingleBean(ControlTareasBlockingClient.class);
                    assertThat(ctx).doesNotHaveBean(ControlTareasReactiveClient.class);
                });
    }
}
