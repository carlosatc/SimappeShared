/*
 * Copyright (c) 2026 CatcSoft
 *
 * Author: torrescamargo@gmail.com
 */
package com.catcsoft.simappe.shared.unattended.client;

import java.util.List;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import com.catcsoft.simappe.commons.api.v1.core.unattended.UnattendedTaskConfig;

import reactor.core.publisher.Mono;

/**
 * Cliente de SimappeAdmin para las filas activas de configuración de tareas
 * desatendidas. La respuesta no lleva credenciales: solo usuario, tenant, rol y
 * vigencia.
 *
 * @author torrescamargo@gmail.com
 * @since 1.4.0
 */
@HttpExchange("simappe-admin/api/v1/internal/unattended-task-configs")
public interface UnattendedTaskConfigWebClient {

    /**
     * Filas activas de un tipo de tarea en un ambiente: compañías activas de
     * clientes activos.
     *
     * @param taskType    tipo de tarea
     * @param environment ambiente
     * @return filas
     */
    @GetExchange
    Mono<List<UnattendedTaskConfig>> listActive(
            @RequestParam("taskType") String taskType,
            @RequestParam("environment") String environment);
}
