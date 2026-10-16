/*
 * Copyright (c) 2026 CatcSoft
 *
 * This software is the confidential and proprietary information of CatcSoft
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of a
 * written license agreement entered into with CatcSoft.
 */
package com.catcsoft.simappe.shared.client.oauth2;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

import com.catcsoft.simappe.commons.api.v1.core.response.SuccessResponse;
import com.catcsoft.simappe.model.admin.dto.security.SimappeToken;
import com.catcsoft.simappe.model.admin.dto.security.UserSession;

import reactor.core.publisher.Mono;

/**
 * Cliente HTTP <b>reactivo</b> del token de ejecución
 * (simappe-oauth2-server). Variante reactiva de
 * {@link ExecutionTokenBlockingClient}: mismo contrato y misma autenticación por
 * secreto de servicio (cabecera de clave de servicio, sin
 * {@code Authorization: Bearer}, porque se pide justamente cuando no hay token
 * vigente que ofrecer), pero retorno {@link Mono}.
 * <p>
 * El bean lo provee {@code SharedClientsAutoConfiguration}.
 * </p>
 *
 * @see ExecutionTokenBlockingClient
 */
@HttpExchange(url = "/simappe-oauth2-server/api/v1/execution-token", accept = "application/json",
        contentType = "application/json")
public interface ExecutionTokenReactiveClient {

    /**
     * Variante reactiva de {@code generate}: mismo contrato, retorno
     * {@link Mono}. Invoca {@code POST /generate}; emite el token de ejecución
     * con la identidad y el tenant de la sesión recibida.
     *
     * @param serviceKey  secreto de servicio (cabecera de clave de servicio)
     * @param userSession sesión tal como salió del token que invocó el proceso
     * @return envelope con el token de ejecución y su vigencia
     */
    @PostExchange("/generate")
    Mono<SuccessResponse<SimappeToken>> generate(
            @RequestHeader("X-Nebula-Service-Key") String serviceKey,
            @RequestBody UserSession userSession);
}
