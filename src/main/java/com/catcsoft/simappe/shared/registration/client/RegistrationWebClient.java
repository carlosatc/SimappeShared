/*
 * Copyright (c) 2024-2026 CatcSoft. Author: Carlos Torres Email: torrescamargo@gmail.com
 */
package com.catcsoft.simappe.shared.registration.client;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

import com.catcsoft.simappe.commons.api.v1.core.response.SuccessResponse;
import com.catcsoft.simappe.shared.registration.dto.RegistrationOpenRequest;
import com.catcsoft.simappe.shared.registration.dto.RegistrationOpenResponse;
import com.catcsoft.simappe.shared.registration.dto.RegistrationSealRequest;
import com.catcsoft.simappe.shared.registration.dto.RegistrationSealResponse;

import reactor.core.publisher.Mono;

/**
 * WebClient del enlace de activación de usuario contra el servidor oauth2
 * ({@code /api/v1/registration}). Consumido servicio-a-servicio por
 * nebula-masters; se autentica con la clave de servicio compartida
 * ({@code X-Nebula-Service-Key}).
 *
 * <ul>
 * <li>{@code seal} — cifra el contexto de activación (alta de usuario).</li>
 * <li>{@code open} — descifra el blob y devuelve el JWT de registro
 * (activación pública).</li>
 * </ul>
 *
 * @author Carlos Torres
 * @version 1.0
 * @since 1.4.0
 */
@HttpExchange("simappe-oauth2-server/api/v1/registration")
public interface RegistrationWebClient {

    /**
     * Cifra el contexto de activación en un blob para la URL del correo.
     *
     * @param request    contexto de activación a cifrar
     * @param serviceKey clave de servicio compartida
     * @return el blob cifrado
     */
    @PostExchange("/seal")
    Mono<SuccessResponse<RegistrationSealResponse>> seal(
            @RequestBody RegistrationSealRequest request,
            @RequestHeader("X-Nebula-Service-Key") String serviceKey);

    /**
     * Descifra el blob del enlace y devuelve el JWT de registro con el contexto
     * de tenant.
     *
     * @param request    blob cifrado del enlace
     * @param serviceKey clave de servicio compartida
     * @return el JWT de registro más los datos del payload
     */
    @PostExchange("/open")
    Mono<SuccessResponse<RegistrationOpenResponse>> open(
            @RequestBody RegistrationOpenRequest request,
            @RequestHeader("X-Nebula-Service-Key") String serviceKey);
}
