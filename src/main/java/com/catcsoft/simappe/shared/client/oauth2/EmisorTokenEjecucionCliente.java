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
package com.catcsoft.simappe.shared.client.oauth2;

import com.catcsoft.simappe.commons.api.v1.tareas.tenant.EmisorTokenEjecucion;
import com.catcsoft.simappe.model.admin.dto.security.SimappeToken;
import com.catcsoft.simappe.model.admin.dto.security.UserSession;

/**
 * El emisor del token de ejecucion del marco de tareas sobre el cliente del servidor de
 * autorizacion (HT_ARQ_013, RN-TAR-061): pide el token y devuelve el contenido del envelope.
 *
 * @author Carlos Torres
 * @since 2.5.0
 */
public class EmisorTokenEjecucionCliente implements EmisorTokenEjecucion {

    /** Cliente del servidor de autorizacion. */
    private final ExecutionTokenBlockingClient cliente;

    /**
     * Crea el emisor.
     *
     * @param cliente cliente del token de ejecucion
     */
    public EmisorTokenEjecucionCliente(ExecutionTokenBlockingClient cliente) {
        this.cliente = cliente;
    }

    @Override
    public SimappeToken emitir(String serviceKey, UserSession sesion) {
        var respuesta = cliente.generate(serviceKey, sesion);
        return respuesta == null || respuesta.getResponse() == null ? null : respuesta.getResponse().getContent();
    }
}
