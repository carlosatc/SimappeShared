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
import com.catcsoft.simappe.shared.security.ServiceKeyHeader;

/**
 * Cliente HTTP <b>bloqueante</b> del token de ejecución
 * (simappe-oauth2-server).
 *
 * <h2>Para qué sirve</h2>
 * <p>
 * Lo usa el proceso que retoma un trabajo lanzado antes. Un proceso diferido no
 * puede arrastrar el token de quien lo lanzó: cuando el mensaje se procesa con
 * retraso o tras un reintento ese token ya venció, y transportarlo deja la
 * credencial escrita en el canal de mensajería y en cuanto la persista por el
 * camino. En su lugar viaja la {@link UserSession} que salió de aquel token
 * —un dato ya respaldado, porque la invocación que originó el proceso llegó con
 * token vigente y ahí se validaron firma, vigencia y roles— y con ella este
 * cliente obtiene un token nuevo, con la misma firma y con la vigencia que la
 * ejecución necesita.
 * </p>
 *
 * <h2>Autenticación</h2>
 * <p>
 * A diferencia del resto de los clientes de Simappe, este <b>no</b> lleva
 * {@code Authorization: Bearer}: se pide precisamente cuando no hay token
 * vigente que ofrecer. Se autentica con el secreto de servicio compartido en la
 * cabecera de clave de servicio, el mismo control con que el ecosistema
 * protege sus llamadas servicio-a-servicio. Sin ese secreto el emisor responde
 * 401 y no emite nada.
 * </p>
 *
 * <h2>Cómo se usa el resultado</h2>
 * <p>
 * El envelope es {@link SuccessResponse}; el contenido útil está en
 * {@code resp.getResponse().getContent()}. Conviene <b>guardar el token por
 * tenant y renovarlo antes de que venza</b> en vez de pedir uno por cada
 * llamada saliente: la vigencia la define el emisor y cubre la ejecución
 * completa del proceso.
 * </p>
 *
 * <p>
 * El bean lo provee {@code SharedClientsAutoConfiguration} cuando
 * {@code simappe.shared.clients.enabled=true}.
 * </p>
 *
 * @see ExecutionTokenReactiveClient
 */
@HttpExchange(url = "/simappe-oauth2-server/api/v1/execution-token", accept = "application/json",
        contentType = "application/json")
public interface ExecutionTokenBlockingClient {

    /**
     * Invoca {@code POST /generate}; emite el token de ejecución con la identidad
     * y el tenant de la sesión recibida.
     * <p>
     * La sesión debe traer al menos usuario, cliente y ambiente: sin usuario no
     * hay a quién atribuir lo que el proceso escriba, y sin cliente ni ambiente
     * no hay tenant al que dirigir la ejecución. El emisor responde 400 si falta
     * alguno y 401 si el secreto de servicio no coincide.
     * </p>
     *
     * @param serviceKey  secreto de servicio (cabecera de clave de servicio)
     * @param userSession sesión tal como salió del token que invocó el proceso
     * @return envelope con el token de ejecución y su vigencia
     */
    @PostExchange("/generate")
    SuccessResponse<SimappeToken> generate(
            @RequestHeader(ServiceKeyHeader.DEFAULT_NAME) String serviceKey,
            @RequestBody UserSession userSession);
}
