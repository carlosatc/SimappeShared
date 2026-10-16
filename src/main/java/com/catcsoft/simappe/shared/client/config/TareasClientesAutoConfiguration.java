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

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

import com.catcsoft.simappe.commons.api.v1.tareas.estado.AvisoControlTareas;
import com.catcsoft.simappe.commons.api.v1.tareas.estado.NombreUsuarioTareas;
import com.catcsoft.simappe.commons.api.v1.tareas.tenant.EmisorTokenEjecucion;
import com.catcsoft.simappe.shared.client.configurations.NombreUsuarioSimappe;
import com.catcsoft.simappe.shared.client.configurations.UsuarioSimappeBlockingClient;
import com.catcsoft.simappe.shared.client.oauth2.EmisorTokenEjecucionCliente;
import com.catcsoft.simappe.shared.client.oauth2.ExecutionTokenBlockingClient;
import com.catcsoft.simappe.shared.client.tareas.AvisoControlTareasCliente;
import com.catcsoft.simappe.shared.client.tareas.ControlTareasBlockingClient;

/**
 * Las piezas del marco de tareas de SimappeCommons que se apoyan en los clientes de SimappeShared
 * (HT_ARQ_013): el emisor del token de ejecucion, el aviso al control central y el nombre de quien
 * lanza la tarea. Corre despues de {@link SharedClientsAutoConfiguration} y antes de
 * {@code TareasAutoConfiguration}; cada pieza existe solo si su cliente esta registrado, y sin ella el
 * marco arranca igual (sin aviso el relé cubre; sin nombre la tarea muestra el usuario).
 *
 * <p>Activación: {@code simappe.tareas.enabled=true}, igual que el marco.</p>
 *
 * @author Carlos Torres
 * @since 2.5.0
 */
@AutoConfiguration(after = SharedClientsAutoConfiguration.class,
        beforeName = "com.catcsoft.simappe.commons.api.v1.tareas.TareasAutoConfiguration")
@ConditionalOnClass(name = "com.catcsoft.simappe.commons.api.v1.tareas.TareasAutoConfiguration")
@ConditionalOnProperty(prefix = "simappe.tareas", name = "enabled", havingValue = "true")
public class TareasClientesAutoConfiguration {

    /**
     * El emisor del token de ejecucion, si el cliente del servidor de autorizacion esta registrado.
     *
     * @param cliente cliente del token de ejecucion
     * @return el emisor
     */
    @Bean
    @ConditionalOnMissingBean(EmisorTokenEjecucion.class)
    @ConditionalOnBean(ExecutionTokenBlockingClient.class)
    EmisorTokenEjecucion emisorTokenEjecucion(ExecutionTokenBlockingClient cliente) {
        return new EmisorTokenEjecucionCliente(cliente);
    }

    /**
     * El aviso al control central, si el cliente esta registrado.
     *
     * @param cliente cliente del control central
     * @return el aviso
     */
    @Bean
    @ConditionalOnMissingBean(AvisoControlTareas.class)
    @ConditionalOnBean(ControlTareasBlockingClient.class)
    AvisoControlTareas avisoControlTareas(ControlTareasBlockingClient cliente) {
        return new AvisoControlTareasCliente(cliente);
    }

    /**
     * El resolutor del nombre de quien lanza la tarea, si el cliente del usuario esta registrado.
     *
     * @param cliente cliente del usuario de configuraciones
     * @return el resolutor
     */
    @Bean
    @ConditionalOnMissingBean(NombreUsuarioTareas.class)
    @ConditionalOnBean(UsuarioSimappeBlockingClient.class)
    NombreUsuarioTareas nombreUsuarioTareas(UsuarioSimappeBlockingClient cliente) {
        return new NombreUsuarioSimappe(cliente);
    }
}
