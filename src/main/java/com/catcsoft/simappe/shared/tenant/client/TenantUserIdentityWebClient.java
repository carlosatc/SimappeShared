/*
 * Copyright (c) 2024-2026 CatcSoft. Author: Carlos Torres Email: torrescamargo@gmail.com
 */
package com.catcsoft.simappe.shared.tenant.client;


import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.DeleteExchange;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;
import org.springframework.web.service.annotation.PutExchange;

import com.catcsoft.simappe.commons.api.v1.core.query.PageDto;
import com.catcsoft.simappe.commons.api.v1.core.query.PageResponse;
import com.catcsoft.simappe.commons.api.v1.core.query.SimappeRequestQuery;
import com.catcsoft.simappe.commons.api.v1.core.response.SuccessResponse;
import com.catcsoft.simappe.model.admin.dto.tenant.TenantUserIdentityDto;
import com.catcsoft.simappe.shared.tenant.dto.ServiceActivationRequest;
import com.catcsoft.simappe.model.admin.record.tenant.TenantUserIdentityResponse;

import reactor.core.publisher.Mono;

/**
 * WebClient del contexto tenant de identidades de usuario
 * ({@code /api/v1/tenant/user-identity} en SimappeAdmin), estilo canónico de
 * los WebClients v2 ({@code @HttpExchange} + {@link SuccessResponse}).
 *
 * <p>
 * Todas las operaciones reciben el header {@code Authorization} con el JWT
 * del administrador del tenant (pass-through): el customer se deriva SIEMPRE
 * del token en el servidor.
 * </p>
 *
 * @author Carlos Torres
 * @version 1.0
 * @since 1.1.0
 * @see TenantCustomerRoleWebClient
 */
@HttpExchange("simappe-admin/api/v1/tenant/user-identity")
public interface TenantUserIdentityWebClient {

    /**
     * Consulta paginada estándar (DTO) de las identidades del customer.
     *
     * @param query         criterios de paginación, filtro y ordenamiento
     * @param authorization header {@code Bearer <jwt>} del admin del tenant
     * @return página de identidades
     */
    @PostExchange("/page")
    Mono<SuccessResponse<PageDto<TenantUserIdentityDto>>> page(
            @RequestBody SimappeRequestQuery query,
            @RequestHeader("Authorization") String authorization);

    /**
     * Consulta paginada inmutable (page-response) de las identidades.
     *
     * @param query         criterios de paginación, filtro y ordenamiento
     * @param authorization header {@code Bearer <jwt>} del admin del tenant
     * @return página de records inmutables (identidad + roles agregados)
     */
    @PostExchange("/page-response")
    Mono<SuccessResponse<PageResponse<TenantUserIdentityResponse>>> pageResponse(
            @RequestBody SimappeRequestQuery query,
            @RequestHeader("Authorization") String authorization);

    /**
     * Obtiene el record inmutable de una identidad (get-record).
     *
     * @param id            identificador de la identidad
     * @param authorization header {@code Bearer <jwt>} del admin del tenant
     * @return record inmutable de la identidad
     */
    @GetExchange("/get-record")
    Mono<SuccessResponse<TenantUserIdentityResponse>> getRecord(
            @RequestParam("id") Long id,
            @RequestHeader("Authorization") String authorization);

    /**
     * Crea una identidad para el customer del token (username, email,
     * contraseña validada y roles preasignados; sin company).
     *
     * @param dto           identidad a crear
     * @param authorization header {@code Bearer <jwt>} del admin del tenant
     * @return la identidad creada
     */
    @PostExchange("/create")
    Mono<SuccessResponse<TenantUserIdentityDto>> create(
            @RequestBody TenantUserIdentityDto dto,
            @RequestHeader("Authorization") String authorization);

    /**
     * Crea una identidad para el customer del token en estado {@code INACTIVE} y
     * SIN contraseña, para el flujo de alta con activación posterior. La
     * contraseña la define el propio usuario en la activación vía
     * {@code define-password}. El campo {@code password} del dto se ignora.
     *
     * @param dto           identidad a crear (username, email, roles)
     * @param authorization header {@code Bearer <jwt>} del admin del tenant
     * @return la identidad creada en estado INACTIVE
     */
    @PostExchange("/create-inactive")
    Mono<SuccessResponse<TenantUserIdentityDto>> createInactive(
            @RequestBody TenantUserIdentityDto dto,
            @RequestHeader("Authorization") String authorization);

    /**
     * Activación por SERVICIO (flujo público de activación, SIN JWT de usuario). Autenticada por un
     * secreto de servicio compartido en la cabecera {@code X-Nebula-Service-Key}: fija la contraseña y
     * activa la identidad INACTIVE. La invoca un servicio de confianza (nebula-masters) tras validar su
     * token de un solo uso.
     *
     * @param request    datos de activación ({@code username}, {@code customerId}, {@code password});
     *                   DTO propio para que el {@code password} SÍ se serialice (el de
     *                   {@code TenantUserIdentityDto} es WRITE_ONLY y no viajaría)
     * @param serviceKey secreto de servicio ({@code X-Nebula-Service-Key})
     * @return señal de completitud
     */
    @PutExchange("/activate-by-service")
    Mono<Void> activateByService(
            @RequestBody ServiceActivationRequest request,
            @RequestHeader("X-Nebula-Service-Key") String serviceKey);

    /**
     * Actualiza una identidad del customer del token.
     *
     * @param dto           datos a actualizar
     * @param id            identificador de la identidad
     * @param authorization header {@code Bearer <jwt>} del admin del tenant
     * @return la identidad actualizada
     */
    @PutExchange("/update")
    Mono<SuccessResponse<TenantUserIdentityDto>> update(
            @RequestBody TenantUserIdentityDto dto,
            @RequestParam("id") Long id,
            @RequestHeader("Authorization") String authorization);

    /**
     * Define la contraseña de una identidad (validada contra la política).
     *
     * @param id            identificador de la identidad
     * @param dto           DTO con la contraseña a definir
     * @param authorization header {@code Bearer <jwt>} del admin del tenant
     * @return señal de completitud
     */
    @PutExchange("/define-password")
    Mono<SuccessResponse<Void>> definePassword(
            @RequestParam("id") Long id,
            @RequestBody TenantUserIdentityDto dto,
            @RequestHeader("Authorization") String authorization);

    /**
     * Reinicia la contraseña de una identidad (política + historial).
     *
     * @param id            identificador de la identidad
     * @param dto           DTO con la nueva contraseña
     * @param authorization header {@code Bearer <jwt>} del admin del tenant
     * @return señal de completitud
     */
    @PutExchange("/reset-password")
    Mono<SuccessResponse<Void>> resetPassword(
            @RequestParam("id") Long id,
            @RequestBody TenantUserIdentityDto dto,
            @RequestHeader("Authorization") String authorization);

    /**
     * Activa una identidad (fast).
     *
     * @param id            identificador de la identidad
     * @param authorization header {@code Bearer <jwt>} del admin del tenant
     * @return señal de completitud
     */
    @PutExchange("/activate")
    Mono<SuccessResponse<Void>> activate(
            @RequestParam("id") Long id,
            @RequestHeader("Authorization") String authorization);

    /**
     * Inactiva una identidad (fast); sus sesiones activas se invalidan.
     *
     * @param id            identificador de la identidad
     * @param authorization header {@code Bearer <jwt>} del admin del tenant
     * @return señal de completitud
     */
    @PutExchange("/deactivate")
    Mono<SuccessResponse<Void>> deactivate(
            @RequestParam("id") Long id,
            @RequestHeader("Authorization") String authorization);

    /**
     * Bloquea una identidad (fast); sus sesiones activas se invalidan.
     *
     * @param id            identificador de la identidad
     * @param authorization header {@code Bearer <jwt>} del admin del tenant
     * @return señal de completitud
     */
    @PutExchange("/block")
    Mono<SuccessResponse<Void>> block(
            @RequestParam("id") Long id,
            @RequestHeader("Authorization") String authorization);

    /**
     * Desbloquea una identidad (fast).
     *
     * @param id            identificador de la identidad
     * @param authorization header {@code Bearer <jwt>} del admin del tenant
     * @return señal de completitud
     */
    @PutExchange("/unlock")
    Mono<SuccessResponse<Void>> unlock(
            @RequestParam("id") Long id,
            @RequestHeader("Authorization") String authorization);

    /**
     * Habilita o deshabilita el MFA de una identidad (fast).
     *
     * @param id            identificador de la identidad
     * @param enabled       valor a establecer
     * @param authorization header {@code Bearer <jwt>} del admin del tenant
     * @return señal de completitud
     */
    @PutExchange("/mfa")
    Mono<SuccessResponse<Void>> mfa(
            @RequestParam("id") Long id,
            @RequestParam("enabled") boolean enabled,
            @RequestHeader("Authorization") String authorization);

    /**
     * Asigna un rol del customer a la identidad (idempotente).
     *
     * @param id             identificador de la identidad
     * @param customerRoleId identificador del rol del customer
     * @param authorization  header {@code Bearer <jwt>} del admin del tenant
     * @return señal de completitud
     */
    @PostExchange("/add-role")
    Mono<SuccessResponse<Void>> addRole(
            @RequestParam("id") Long id,
            @RequestParam("customerRoleId") Long customerRoleId,
            @RequestHeader("Authorization") String authorization);

    /**
     * Retira un rol del customer de la identidad.
     *
     * @param id             identificador de la identidad
     * @param customerRoleId identificador del rol del customer
     * @param authorization  header {@code Bearer <jwt>} del admin del tenant
     * @return señal de completitud
     */
    @DeleteExchange("/remove-role")
    Mono<SuccessResponse<Void>> removeRole(
            @RequestParam("id") Long id,
            @RequestParam("customerRoleId") Long customerRoleId,
            @RequestHeader("Authorization") String authorization);

    /**
     * Verifica si un username ya está en uso (único global).
     *
     * @param username      nombre de usuario a verificar
     * @param authorization header {@code Bearer <jwt>} del admin del tenant
     * @return {@code true} si ya existe
     */
    @GetExchange("/exists-username")
    Mono<SuccessResponse<Boolean>> existsUsername(
            @RequestParam("username") String username,
            @RequestHeader("Authorization") String authorization);

    /**
     * Verifica si un email ya está en uso (único global).
     *
     * @param email         correo electrónico a verificar
     * @param authorization header {@code Bearer <jwt>} del admin del tenant
     * @return {@code true} si ya existe
     */
    @GetExchange("/exists-email")
    Mono<SuccessResponse<Boolean>> existsEmail(
            @RequestParam("email") String email,
            @RequestHeader("Authorization") String authorization);

    /**
     * Elimina lógicamente una identidad (soft-delete a INACTIVE).
     *
     * @param id            identificador de la identidad
     * @param authorization header {@code Bearer <jwt>} del admin del tenant
     * @return señal de completitud
     */
    @DeleteExchange("/delete")
    Mono<SuccessResponse<Void>> delete(
            @RequestParam("id") Long id,
            @RequestHeader("Authorization") String authorization);
}
