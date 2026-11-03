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

import com.catcsoft.simappe.commons.api.v1.util.SimappeConstant;
import com.catcsoft.simappe.model.admin.dto.security.policy.LoginAttemptDto;
import com.catcsoft.simappe.model.core.response.SuccessResponse;

import jakarta.validation.Valid;
import reactor.core.publisher.Mono;

/**
 * Cliente WebClient modular para operaciones de seguridad y auditoría de login.
 * <p>
 * Este cliente agrupa los endpoints relacionados con:
 * </p>
 * <ul>
 * <li>Registro de intentos de login</li>
 * <li>Consulta de intentos de login por usuario</li>
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
public interface SecurityReactiveClient {

        // ========== LOGIN ATTEMPT MANAGEMENT ==========

        /**
         * Retrieves login attempt records by user ID.
         *
         * @param userId the user ID
         * @return the list of login attempts for the user
         */
        @GetExchange(value = "/api/v1/login-attempt/by-user", accept = MediaType.APPLICATION_JSON_VALUE)
        Mono<SuccessResponse<List<LoginAttemptDto>>> readLoginAttemptsByUser(
                        @RequestParam("userId") Long userId);

        /**
         * Creates a new login attempt record.
         *
         * @param dto the login attempt DTO
         * @return the created login attempt
         */
        @PostExchange(value = "/api/v1/login-attempt"
                        + SimappeConstant.PATH_CREATE, accept = MediaType.APPLICATION_JSON_VALUE)
        Mono<SuccessResponse<LoginAttemptDto>> recordLoginAttempt(
                        @Valid @RequestBody LoginAttemptDto dto);
}
