/*
 * Copyright (c) 2025 CatcSoft
 *
 * This software is the confidential and proprietary information of CatcSoft
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of a
 * written license agreement entered into with CatcSoft.
 *
 * Author: torrescamargo@gmail.com
 */
package com.catcsoft.simappe.shared.client.admin;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;
import org.springframework.web.service.annotation.PutExchange;

import com.catcsoft.simappe.commons.api.v1.util.SimappeConstant;
import com.catcsoft.simappe.model.admin.dto.oauth.OAuthClientDto;
import com.catcsoft.simappe.model.admin.dto.oauth.PerpetualTokenDto;
import com.catcsoft.simappe.model.core.response.SuccessResponse;

import jakarta.validation.Valid;

/**
 * Cliente WebClient modular para operaciones OAuth2 y tokens perpetuos.
 * <p>
 * Este cliente agrupa los endpoints relacionados con:
 * </p>
 * <ul>
 * <li>Credenciales de clientes OAuth2</li>
 * <li>Creación, actualización y consulta de tokens perpetuos</li>
 * </ul>
 * 
 * <p>
 * Reemplaza parcialmente a {@code SimappeAdminWebClient} para reducir el tamaño
 * de las interfaces y mejorar el rendimiento del IDE.
 * </p>
 * 
 * @author Carlos Torres (torrescamargo@gmail.com)
 * @since 2.5.0
 * @see com.catcsoft.simappe.commons.api.v1.core.webclient.SimappeAdminWebClient
 */
@HttpExchange("simappe-admin")
public interface OAuthBlockingClient {

        // ========== OAUTH CLIENT CREDENTIALS ==========

        /**
         * Recupera las credenciales de usuario por su ID de cliente.
         *
         * @param clientId el ID del cliente (clientId)
         * @return las credenciales del cliente OAuth2
         */
        @GetExchange(value = "/api/v1/oauth/client/get", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<OAuthClientDto> getUserCredentials(@RequestParam String clientId);

        // ========== PERPETUAL TOKEN MANAGEMENT ==========

        /**
         * Crea un nuevo token perpetuo.
         *
         * @param dto el DTO del token perpetuo a crear
         * @return el token perpetuo creado
         */
        @PostExchange(value = "/api/v1/oauth/perpetual-token"
                        + SimappeConstant.PATH_CREATE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<PerpetualTokenDto> createPerpetualToken(@Valid @RequestBody PerpetualTokenDto dto);

        /**
         * Actualiza un token perpetuo existente.
         *
         * @param dto el DTO del token perpetuo con los cambios
         * @param id  el ID del token a actualizar
         * @return el token perpetuo actualizado
         */
        @PutExchange(value = "/api/v1/oauth/perpetual-token"
                        + SimappeConstant.PATH_UPDATE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<PerpetualTokenDto> updatePerpetualToken(@Valid @RequestBody PerpetualTokenDto dto,
                        @RequestParam String id);

        /**
         * Recupera un token perpetuo por el hash del token.
         *
         * @param tokenHash el hash del token
         * @return los detalles del token perpetuo encontrado
         */
        @GetExchange(value = "/api/v1/oauth/perpetual-token/by-hash", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<PerpetualTokenDto> getPerpetualTokenByHash(@RequestParam String tokenHash);

        /**
         * Recupera los tokens perpetuos asociados a un nombre de usuario.
         *
         * @param username el nombre de usuario
         * @return la lista de tokens perpetuos para el usuario dado
         */
        @GetExchange(value = "/api/v1/oauth/perpetual-token/by-username", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<PerpetualTokenDto>> getPerpetualTokensByUsername(@RequestParam String username);

        /**
         * Recupera un token perpetuo por su ID.
         *
         * @param id el ID del token perpetuo
         * @return los detalles del token perpetuo encontrado
         */
        @GetExchange(value = "/api/v1/oauth/perpetual-token"
                        + SimappeConstant.PATH_GET, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<PerpetualTokenDto> getPerpetualToken(@RequestParam String id);

        /**
         * Recupera los tokens perpetuos activos asociados a un ID de cliente.
         *
         * @param clientId el ID del cliente
         * @return la lista de tokens perpetuos activos para el cliente dado
         */
        @GetExchange(value = "/api/v1/oauth/perpetual-token/by-client", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<PerpetualTokenDto>> getPerpetualTokensByClientId(@RequestParam Long clientId);
}
