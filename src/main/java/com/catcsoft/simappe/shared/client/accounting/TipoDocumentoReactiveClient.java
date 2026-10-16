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

import reactor.core.publisher.Mono;

/**
 * Cliente HTTP <b>reactivo</b> (WebClient) del maestro Tipo de Documento (simappe-accounting-core).
 * Espeja {@code TipoDocumentoController}. Variante reactiva del
 * {@link TipoDocumentoBlockingClient} (token explicito, envelope {@link SuccessResponse}).
 *
 * <p>El bean lo provee {@code SharedClientsAutoConfiguration} cuando {@code simappe.shared.clients.enabled}
 * y {@code simappe.shared.clients.reactive.enabled} (default true).</p>
 */
@HttpExchange(url = "/simappe-accounting-core/api/v1/tipo-documento", accept = "application/json",
        contentType = "application/json")
public interface TipoDocumentoReactiveClient {

    /**
     * Invoca {@code GET /read}; lista los tipos de documento ACTIVOS del tenant.
     *
     * @param authorization cabecera Authorization (Bearer JWT).
     * @return los tipos de documento activos del tenant (reactivo).
     */
    @GetExchange("/read")
    Mono<SuccessResponse<List<TipoDocumentoDto>>> read(@RequestHeader("Authorization") String authorization);

    /**
     * Invoca {@code GET /get}; obtiene un tipo de documento por id.
     *
     * @param id            identificador del tipo de documento.
     * @param authorization cabecera Authorization (Bearer JWT).
     * @return el tipo de documento (DTO, reactivo).
     */
    @GetExchange("/get")
    Mono<SuccessResponse<TipoDocumentoDto>> get(@RequestParam Long id,
            @RequestHeader("Authorization") String authorization);

    /**
     * Invoca {@code GET /get-record}; obtiene un tipo de documento por id como record plano.
     *
     * @param id            identificador del tipo de documento.
     * @param authorization cabecera Authorization (Bearer JWT).
     * @return el record plano del tipo de documento (reactivo).
     */
    @GetExchange("/get-record")
    Mono<SuccessResponse<TipoDocumentoResponse>> getRecord(@RequestParam Long id,
            @RequestHeader("Authorization") String authorization);

    /**
     * Invoca {@code POST /create}; crea un tipo de documento y devuelve el persistido.
     *
     * @param dto           datos del tipo de documento a crear.
     * @param authorization cabecera Authorization (Bearer JWT).
     * @return el tipo de documento creado (DTO, reactivo).
     */
    @PostExchange("/create")
    Mono<SuccessResponse<TipoDocumentoDto>> create(@RequestBody TipoDocumentoDto dto,
            @RequestHeader("Authorization") String authorization);

    /**
     * Invoca {@code PUT /update}; actualiza el tipo de documento identificado por {@code id}.
     *
     * @param dto           datos actualizados.
     * @param id            identificador del tipo de documento.
     * @param authorization cabecera Authorization (Bearer JWT).
     * @return el tipo de documento actualizado (DTO, reactivo).
     */
    @PutExchange("/update")
    Mono<SuccessResponse<TipoDocumentoDto>> update(@RequestBody TipoDocumentoDto dto, @RequestParam Long id,
            @RequestHeader("Authorization") String authorization);

    /**
     * Invoca {@code DELETE /delete}; da de baja (soft-delete) el tipo de documento identificado por
     * {@code id}. Sin cuerpo de respuesta.
     *
     * @param id            identificador del tipo de documento.
     * @param authorization cabecera Authorization (Bearer JWT).
     * @return senal de completado (reactivo).
     */
    @DeleteExchange("/delete")
    Mono<Void> delete(@RequestParam Long id, @RequestHeader("Authorization") String authorization);
}
