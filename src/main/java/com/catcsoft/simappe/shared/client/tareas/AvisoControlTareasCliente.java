/*
 * Copyright (c) 2026 CatcSoft.
 *
 * This software is the confidential and proprietary information of CatcSoft
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of a
 * written license agreement entered into with CatcSoft.
 *
 * Author: Carlos Torres
 * Date: 2026-10-07
 */
package com.catcsoft.simappe.shared.client.tareas;

import com.catcsoft.simappe.commons.api.v1.util.SimappeConstant;
import com.catcsoft.simappe.commons.api.v1.tareas.estado.AvisoControlTareas;
import com.catcsoft.simappe.commons.api.v1.tareas.sesion.SesionVigente;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

/**
 * El aviso al control central por su cliente de SimappeShared, con el token de la peticion del hilo:
 * el del usuario en la peticion HTTP o el de ejecucion en el trabajador (HT_ARQ_013).
 *
 * @author Carlos Torres
 * @since 0.12.0
 */
@Slf4j
public class AvisoControlTareasCliente implements AvisoControlTareas {

    private final ControlTareasBlockingClient cliente;

    /**
     * Crea el aviso.
     *
     * @param cliente cliente del control central
     */
    public AvisoControlTareasCliente(ControlTareasBlockingClient cliente) {
        this.cliente = cliente;
    }

    @Override
    public void pedirReconciliacion(String servicio, String tareaId) {
        HttpServletRequest peticion = SesionVigente.peticion();
        String token = peticion == null ? null : peticion.getHeader(SimappeConstant.AUTHORIZATION);
        if (token == null || token.isBlank()) {
            log.debug("[TAREAS] Sin token para avisar al control central; la tarea {} queda para el relé", tareaId);
            return;
        }
        try {
            cliente.reconciliar(servicio, tareaId, token);
        } catch (RuntimeException e) {
            log.warn("[TAREAS] El control central no recibio el aviso de la tarea {}; queda para el relé: {}",
                    tareaId, e.getMessage());
        }
    }
}
