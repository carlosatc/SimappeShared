/*
 * Copyright (c) 2025 CatcSoft
 *
 * This software is the confidential and proprietary information of CatcSoft
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with CatcSoft.
 *
 * @author torrescamargo@gmail.com
 */
package com.catcsoft.simappe.shared.client.admin;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import com.catcsoft.simappe.commons.api.v1.core.error.SimappeException;
import com.catcsoft.simappe.model.core.response.SuccessResponse;
import com.catcsoft.simappe.commons.api.v1.util.SimappeConstant;
import com.catcsoft.simappe.model.core.database.dto.DatabaseConfigV2Dto;

import reactor.core.publisher.Mono;

/**
 * Cliente WebClient modular para operaciones de configuración de base de datos
 * en Simappe.
 * Proporciona métodos para obtener conexiones y listar configuraciones de base
 * de datos.
 * 
 * <p>
 * Este cliente forma parte de la arquitectura modular v2 que reemplaza
 * gradualmente
 * al SimappeAdminWebClient monolítico.
 * 
 * @author torrescamargo@gmail.com
 * @since 2026-02-01
 * @version 2.8.1
 */
@HttpExchange("simappe-admin")
public interface DatabaseReactiveClient {

        /**
         * Recupera todas las configuraciones de base de datos V2.
         *
         * @return la lista de configuraciones de base de datos V2
         */
        @GetExchange(value = "/api/v1/database-config-v2"
                        + SimappeConstant.PATH_READ, accept = MediaType.APPLICATION_JSON_VALUE)
        Mono<SuccessResponse<List<DatabaseConfigV2Dto>>> read();

        /**
         * Recupera los detalles de conexión a la base de datos utilizando un token de
         * autorización.
         *
         * @param token el token de autorización
         * @return los detalles de conexión encontrados
         * @throws SimappeException si ocurre un error en el microservicio
         */
        @GetExchange(value = "/api/v2/database-config/connection", accept = MediaType.APPLICATION_JSON_VALUE)
        Mono<SuccessResponse<DatabaseConfigV2Dto>> getConnection(
                        @RequestHeader(SimappeConstant.AUTHORIZATION) String token)
                        throws SimappeException;

        /**
         * Variante que además envía el header opcional
         * {@code X-Simappe-Connection-Schema} (cifrado) para resolver una
         * conexión por esquema de trabajo. Si {@code connectionSchema} es
         * {@code null}, el header se omite y la resolución es legacy
         * (idéntica a {@link #getConnection(String)}).
         *
         * @param token            token de autorización
         * @param connectionSchema valor cifrado del esquema solicitado, o
         *                         {@code null} para omitir el header
         * @return los detalles de conexión efectivos
         * @throws SimappeException si ocurre un error en el microservicio
         */
        @GetExchange(value = "/api/v2/database-config/connection", accept = MediaType.APPLICATION_JSON_VALUE)
        Mono<SuccessResponse<DatabaseConfigV2Dto>> getConnection(
                        @RequestHeader(SimappeConstant.AUTHORIZATION) String token,
                        @RequestHeader(name = SimappeConstant.CONNECTION_SCHEMA_HEADER, required = false) String connectionSchema)
                        throws SimappeException;
}
