/*
 * Copyright (c) 2024-2026 CatcSoft. Author: Carlos Torres Email: torrescamargo@gmail.com
 */
package com.catcsoft.simappe.shared.identity.client;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import com.catcsoft.simappe.commons.api.v1.core.response.SuccessResponse;
import com.catcsoft.simappe.model.admin.dto.security.UserIdentityDto;

import reactor.core.publisher.Mono;

/**
 * WebClient del contexto GLOBAL de identidades de usuario ({@code /api/v1/user-identity} en
 * SimappeAdmin). A diferencia de {@code TenantUserIdentityWebClient} (acotado al tenant del JWT), este
 * resuelve una identidad de forma GLOBAL por username o email (ambos únicos globales) y devuelve su
 * cliente y su empresa efectiva (la de la identidad, o la empresa por defecto del cliente). Se usa en
 * flujos SIN sesión —p.ej. el reset público de "olvidé mi contraseña"— donde hay que resolver el
 * tenant a partir del identificador. Los endpoints son accesibles sin JWT de usuario.
 *
 * @author Carlos Torres
 * @version 1.0
 * @since 1.3.12
 */
@HttpExchange("simappe-admin/api/v1/user-identity")
public interface UserIdentityWebClient {

    /**
     * Resuelve la identidad global por username.
     *
     * @param username username a resolver.
     * @return la identidad (con su cliente y empresa efectiva).
     */
    @GetExchange("/get-username")
    Mono<SuccessResponse<UserIdentityDto>> getUsername(@RequestParam("username") String username);

    /**
     * Resuelve la identidad global por email.
     *
     * @param useremail email a resolver.
     * @return la identidad (con su cliente y empresa efectiva).
     */
    @GetExchange("/get-useremail")
    Mono<SuccessResponse<UserIdentityDto>> getUseremail(@RequestParam("useremail") String useremail);
}
