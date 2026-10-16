/*
 * Copyright (c) 2026 CatcSoft
 *
 * This software is the confidential and proprietary information of CatcSoft
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of a
 * written license agreement entered into with CatcSoft.
 */
package com.catcsoft.simappe.shared.client.tareas;

import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

import com.catcsoft.simappe.commons.api.v1.core.response.SuccessResponse;
import com.catcsoft.simappe.model.tasks.tarea.record.ReconciliacionTareasResponse;
import com.catcsoft.simappe.model.tasks.tarea.record.SaludTareasResponse;

/**
 * Cliente bloqueante del control central del proceso de tareas de simappe-tasks (HT_ARQ_013, orden de
 * Carlos del 7-oct-2026): el mecanismo de respaldo que cualquier servicio consume para empujar lo que
 * quedo registrado en TASKS sin publicar o sin ejecutar, y para medir la salud del proceso.
 *
 * @author Carlos Torres
 * @since 0.17.0
 */
@HttpExchange(url = "/simappe-tasks/api/v1/tareas/control", accept = "application/json",
        contentType = "application/json")
public interface ControlTareasBlockingClient {

    /**
     * Publica lo pendiente y redespacha lo varado ya, sin esperar al relé.
     *
     * @param servicio      servicio; nulo para todos
     * @param tareaId       tarea; nula para todas
     * @param authorization token del servicio o del usuario ({@code Bearer ...})
     * @return lo que hizo la reconciliacion
     */
    @PostExchange("/reconciliar")
    SuccessResponse<ReconciliacionTareasResponse> reconciliar(@RequestParam(required = false) String servicio,
            @RequestParam(required = false) String tareaId, @RequestHeader("Authorization") String authorization);

    /**
     * La salud del proceso de tareas.
     *
     * @param servicio      servicio; nulo para todos
     * @param authorization token del servicio o del usuario ({@code Bearer ...})
     * @return la salud
     */
    @GetExchange("/salud")
    SuccessResponse<SaludTareasResponse> salud(@RequestParam(required = false) String servicio,
            @RequestHeader("Authorization") String authorization);
}
