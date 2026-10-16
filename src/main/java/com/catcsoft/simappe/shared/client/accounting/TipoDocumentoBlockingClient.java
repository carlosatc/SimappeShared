/*
 * Copyright (c) 2026 CatcSoft
 *
 * This software is the confidential and proprietary information of CatcSoft
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of a
 * written license agreement entered into with CatcSoft.
 */
package com.catcsoft.simappe.shared.client.accounting;

import java.util.List;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.DeleteExchange;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;
import org.springframework.web.service.annotation.PutExchange;

import com.catcsoft.simappe.commons.api.v1.core.response.SuccessResponse;
import com.catcsoft.simappe.model.configurations.tipodocumento.dto.TipoDocumentoDto;
import com.catcsoft.simappe.model.configurations.tipodocumento.record.TipoDocumentoResponse;

/**
 * Cliente HTTP <b>bloqueante</b> del maestro Tipo de Documento (simappe-accounting-core,
 * transversal al ERP). Espeja {@code TipoDocumentoController}.
 *
 * <p>El token de autenticacion se pasa <b>explicitamente</b> en cada llamada
 * (header {@code Authorization: Bearer <jwt>}); el que invoca decide que token usar (el JWT del
 * request entrante, o un token de servicio para jobs). No hay propagacion implicita ni filtro de
 * auth en el cliente.</p>
 *
 * <p>Las respuestas conservan el envelope estandar: se devuelve {@link SuccessResponse}; el
 * contenido util esta en {@code resp.getResponse().getContent()}.</p>
 *
 * <p>El bean lo provee {@code SharedClientsAutoConfiguration} cuando
 * {@code simappe.shared.clients.enabled=true} (sin necesidad de definir beans).</p>
 */
@HttpExchange(url = "/simappe-accounting-core/api/v1/tipo-documento", accept = "application/json",
        contentType = "application/json")
public interface TipoDocumentoBlockingClient {

    /**
     * Invoca {@code GET /read}; lista los tipos de documento ACTIVOS del tenant. Envelope
     * {@link SuccessResponse}; token explicito ({@code Authorization: Bearer}).
     *
     * @param authorization cabecera Authorization (Bearer JWT).
     * @return los tipos de documento activos del tenant.
     */
    @GetExchange("/read")
    SuccessResponse<List<TipoDocumentoDto>> read(@RequestHeader("Authorization") String authorization);

    /**
     * Invoca {@code GET /get}; obtiene un tipo de documento por id. Envelope {@link SuccessResponse};
     * token explicito ({@code Authorization: Bearer}).
     *
     * @param id            identificador del tipo de documento.
     * @param authorization cabecera Authorization (Bearer JWT).
     * @return el tipo de documento (DTO).
     */
    @GetExchange("/get")
    SuccessResponse<TipoDocumentoDto> get(@RequestParam Long id,
            @RequestHeader("Authorization") String authorization);

    /**
     * Invoca {@code GET /get-record}; obtiene un tipo de documento por id como record plano. Envelope
     * {@link SuccessResponse}; token explicito ({@code Authorization: Bearer}).
     *
     * @param id            identificador del tipo de documento.
     * @param authorization cabecera Authorization (Bearer JWT).
     * @return el record plano del tipo de documento.
     */
    @GetExchange("/get-record")
    SuccessResponse<TipoDocumentoResponse> getRecord(@RequestParam Long id,
            @RequestHeader("Authorization") String authorization);

    /**
     * Invoca {@code POST /create}; crea un tipo de documento y devuelve el persistido. Envelope
     * {@link SuccessResponse}; token explicito ({@code Authorization: Bearer}).
     *
     * @param dto           datos del tipo de documento a crear.
     * @param authorization cabecera Authorization (Bearer JWT).
     * @return el tipo de documento creado (DTO).
     */
    @PostExchange("/create")
    SuccessResponse<TipoDocumentoDto> create(@RequestBody TipoDocumentoDto dto,
            @RequestHeader("Authorization") String authorization);

    /**
     * Invoca {@code PUT /update}; actualiza el tipo de documento identificado por {@code id} y devuelve
     * el persistido. Envelope {@link SuccessResponse}; token explicito ({@code Authorization: Bearer}).
     *
     * @param dto           datos actualizados.
     * @param id            identificador del tipo de documento.
     * @param authorization cabecera Authorization (Bearer JWT).
     * @return el tipo de documento actualizado (DTO).
     */
    @PutExchange("/update")
    SuccessResponse<TipoDocumentoDto> update(@RequestBody TipoDocumentoDto dto, @RequestParam Long id,
            @RequestHeader("Authorization") String authorization);

    /**
     * Invoca {@code DELETE /delete}; da de baja (soft-delete) el tipo de documento identificado por
     * {@code id}. Sin cuerpo de respuesta. Token explicito ({@code Authorization: Bearer}).
     *
     * @param id            identificador del tipo de documento.
     * @param authorization cabecera Authorization (Bearer JWT).
     */
    @DeleteExchange("/delete")
    void delete(@RequestParam Long id, @RequestHeader("Authorization") String authorization);
}
