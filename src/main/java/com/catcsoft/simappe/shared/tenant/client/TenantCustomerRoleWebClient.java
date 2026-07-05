/*
 * Copyright (c) 2024-2026 CatcSoft. Author: Carlos Torres Email: torrescamargo@gmail.com
 */
package com.catcsoft.simappe.shared.tenant.client;

import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;
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
import com.catcsoft.simappe.model.admin.dto.tenant.TenantCustomerRoleDto;
import com.catcsoft.simappe.model.admin.dto.tenant.TenantRoleActionDto;
import com.catcsoft.simappe.model.admin.record.tenant.TenantRoleOptionResponse;
import com.catcsoft.simappe.model.admin.record.tenant.TenantRoleTypeResponse;
import com.catcsoft.simappe.model.admin.record.tenant.TenantCustomerRoleResponse;

import reactor.core.publisher.Mono;

/**
 * WebClient del contexto tenant de roles del cliente
 * ({@code /api/v1/tenant/customer-role} en SimappeAdmin), estilo canónico de
 * los WebClients v2 ({@code @HttpExchange} + {@link SuccessResponse}): roles
 * con etiqueta propia sobre tipos base, recursos dentro del alcance comprado
 * y personalización de acciones por recurso.
 *
 * <p>
 * Todas las operaciones reciben el header {@code Authorization} con el JWT
 * del administrador del tenant (pass-through).
 * </p>
 *
 * @author Carlos Torres
 * @version 1.0
 * @since 1.1.0
 * @see TenantUserIdentityWebClient
 */
@HttpExchange("simappe-admin/api/v1/tenant/customer-role")
public interface TenantCustomerRoleWebClient {

    /**
     * Consulta paginada estándar (DTO) de los roles del cliente.
     *
     * @param query         criterios de paginación, filtro y ordenamiento
     * @param authorization header {@code Bearer <jwt>} del admin del tenant
     * @return página de roles
     */
    @PostExchange("/page")
    Mono<SuccessResponse<PageDto<TenantCustomerRoleDto>>> page(
            @RequestBody SimappeRequestQuery query,
            @RequestHeader("Authorization") String authorization);

    /**
     * Consulta paginada inmutable (page-response) de los roles del cliente.
     *
     * @param query         criterios de paginación, filtro y ordenamiento
     * @param authorization header {@code Bearer <jwt>} del admin del tenant
     * @return página de records con conteos de usuarios y opciones
     */
    @PostExchange("/page-response")
    Mono<SuccessResponse<PageResponse<TenantCustomerRoleResponse>>> pageResponse(
            @RequestBody SimappeRequestQuery query,
            @RequestHeader("Authorization") String authorization);

    /**
     * Obtiene el record inmutable de un rol del cliente (get-record).
     *
     * @param id            identificador del rol
     * @param authorization header {@code Bearer <jwt>} del admin del tenant
     * @return record inmutable del rol
     */
    @GetExchange("/get-record")
    Mono<SuccessResponse<TenantCustomerRoleResponse>> getRecord(
            @RequestParam("id") Long id,
            @RequestHeader("Authorization") String authorization);

    /**
     * Página los tipos base del catálogo (BUSINESS activos) para el
     * formulario de creación de roles.
     *
     * @param query         criterios de paginación
     * @param authorization header {@code Bearer <jwt>} del admin del tenant
     * @return página de tipos base
     */
    @PostExchange("/role-types/page-response")
    Mono<SuccessResponse<PageResponse<TenantRoleTypeResponse>>> roleTypesPage(
            @RequestBody SimappeRequestQuery query,
            @RequestHeader("Authorization") String authorization);

    /**
     * Crea un rol del cliente (etiqueta + tipo base + opciones iniciales
     * dentro del alcance comprado).
     *
     * @param dto           rol a crear
     * @param authorization header {@code Bearer <jwt>} del admin del tenant
     * @return el rol creado
     */
    @PostExchange("/create")
    Mono<SuccessResponse<TenantCustomerRoleDto>> create(
            @RequestBody TenantCustomerRoleDto dto,
            @RequestHeader("Authorization") String authorization);

    /**
     * Actualiza la etiqueta y la precedencia de un rol del cliente.
     *
     * @param dto           datos a actualizar
     * @param id            identificador del rol
     * @param authorization header {@code Bearer <jwt>} del admin del tenant
     * @return el rol actualizado
     */
    @PutExchange("/update")
    Mono<SuccessResponse<TenantCustomerRoleDto>> update(
            @RequestBody TenantCustomerRoleDto dto,
            @RequestParam("id") Long id,
            @RequestHeader("Authorization") String authorization);

    /**
     * Activa un rol del cliente (fast).
     *
     * @param id            identificador del rol
     * @param authorization header {@code Bearer <jwt>} del admin del tenant
     * @return señal de completitud
     */
    @PutExchange("/activate")
    Mono<SuccessResponse<Void>> activate(
            @RequestParam("id") Long id,
            @RequestHeader("Authorization") String authorization);

    /**
     * Inactiva un rol del cliente (fast).
     *
     * @param id            identificador del rol
     * @param authorization header {@code Bearer <jwt>} del admin del tenant
     * @return señal de completitud
     */
    @PutExchange("/deactivate")
    Mono<SuccessResponse<Void>> deactivate(
            @RequestParam("id") Long id,
            @RequestHeader("Authorization") String authorization);

    /**
     * Elimina lógicamente un rol del cliente (soft-delete a INACTIVE).
     *
     * @param id            identificador del rol
     * @param authorization header {@code Bearer <jwt>} del admin del tenant
     * @return señal de completitud
     */
    @DeleteExchange("/delete")
    Mono<SuccessResponse<Void>> delete(
            @RequestParam("id") Long id,
            @RequestHeader("Authorization") String authorization);

    /**
     * Página las opciones (recursos) asignadas al rol, acotadas al contexto
     * de aplicación dado (opcional: vacío = sin acotar).
     *
     * @param id              identificador del rol
     * @param applicationCode código de la aplicación del contexto (opcional)
     * @param query           criterios de paginación
     * @param authorization   header {@code Bearer <jwt>} del admin del tenant
     * @return página de opciones asignadas con estado de relación
     */
    @PostExchange("/{id}/options/page-response")
    Mono<SuccessResponse<PageResponse<TenantRoleOptionResponse>>> optionsPage(
            @PathVariable("id") Long id,
            @RequestParam(value = "applicationCode", required = false) String applicationCode,
            @RequestBody SimappeRequestQuery query,
            @RequestHeader("Authorization") String authorization);

    /**
     * Página las opciones compradas aún no asignadas al rol, acotadas al
     * contexto de aplicación dado (opcional: vacío = sin acotar).
     *
     * @param id              identificador del rol
     * @param applicationCode código de la aplicación del contexto (opcional)
     * @param query           criterios de paginación
     * @param authorization   header {@code Bearer <jwt>} del admin del tenant
     * @return página de opciones disponibles
     */
    @PostExchange("/{id}/available-options/page-response")
    Mono<SuccessResponse<PageResponse<TenantRoleOptionResponse>>> availableOptionsPage(
            @PathVariable("id") Long id,
            @RequestParam(value = "applicationCode", required = false) String applicationCode,
            @RequestBody SimappeRequestQuery query,
            @RequestHeader("Authorization") String authorization);

    /**
     * Reemplaza por delta el set de opciones del rol DENTRO del contexto de
     * aplicación dado: las relaciones de otras aplicaciones no participan
     * del delta (opcional: vacío = alcance total).
     *
     * @param id              identificador del rol
     * @param applicationCode código de la aplicación del contexto (opcional)
     * @param optionIds       set objetivo de opciones del contexto
     * @param authorization   header {@code Bearer <jwt>} del admin del tenant
     * @return señal de completitud
     */
    @PutExchange("/{id}/options")
    Mono<SuccessResponse<Void>> replaceOptions(
            @PathVariable("id") Long id,
            @RequestParam(value = "applicationCode", required = false) String applicationCode,
            @RequestBody List<Long> optionIds,
            @RequestHeader("Authorization") String authorization);

    /**
     * Asigna una opción comprada al rol (idempotente).
     *
     * @param id            identificador del rol
     * @param optionId      identificador de la opción
     * @param authorization header {@code Bearer <jwt>} del admin del tenant
     * @return señal de completitud
     */
    @PostExchange("/{id}/options/{optionId}")
    Mono<SuccessResponse<Void>> addOption(
            @PathVariable("id") Long id,
            @PathVariable("optionId") Long optionId,
            @RequestHeader("Authorization") String authorization);

    /**
     * Retira una opción del rol (con salvaguarda anti-lockout).
     *
     * @param id            identificador del rol
     * @param optionId      identificador de la opción
     * @param authorization header {@code Bearer <jwt>} del admin del tenant
     * @return señal de completitud
     */
    @DeleteExchange("/{id}/options/{optionId}")
    Mono<SuccessResponse<Void>> removeOption(
            @PathVariable("id") Long id,
            @PathVariable("optionId") Long optionId,
            @RequestHeader("Authorization") String authorization);

    /**
     * Bloquea una opción del rol sin perder la asignación.
     *
     * @param id            identificador del rol
     * @param optionId      identificador de la opción
     * @param authorization header {@code Bearer <jwt>} del admin del tenant
     * @return señal de completitud
     */
    @PutExchange("/{id}/options/{optionId}/block")
    Mono<SuccessResponse<Void>> blockOption(
            @PathVariable("id") Long id,
            @PathVariable("optionId") Long optionId,
            @RequestHeader("Authorization") String authorization);

    /**
     * Reactiva una opción bloqueada del rol.
     *
     * @param id            identificador del rol
     * @param optionId      identificador de la opción
     * @param authorization header {@code Bearer <jwt>} del admin del tenant
     * @return señal de completitud
     */
    @PutExchange("/{id}/options/{optionId}/activate")
    Mono<SuccessResponse<Void>> activateOption(
            @PathVariable("id") Long id,
            @PathVariable("optionId") Long optionId,
            @RequestHeader("Authorization") String authorization);

    /**
     * Página las acciones efectivas del rol para un recurso, con el origen de
     * cada estado (propio del recurso, propio del rol o template).
     *
     * @param id            identificador del rol
     * @param optionId      identificador de la opción (recurso)
     * @param query         criterios de paginación
     * @param authorization header {@code Bearer <jwt>} del admin del tenant
     * @return página de acciones efectivas
     */
    @PostExchange("/{id}/options/{optionId}/actions/page")
    Mono<SuccessResponse<PageResponse<TenantRoleActionDto>>> optionActionsPage(
            @PathVariable("id") Long id,
            @PathVariable("optionId") Long optionId,
            @RequestBody SimappeRequestQuery query,
            @RequestHeader("Authorization") String authorization);

    /**
     * Guarda el set deseado de acciones del rol para un recurso (solo se
     * persisten las diferencias contra el contexto general del rol).
     *
     * @param id            identificador del rol
     * @param optionId      identificador de la opción (recurso)
     * @param actions       set deseado (actionId + granted)
     * @param authorization header {@code Bearer <jwt>} del admin del tenant
     * @return señal de completitud
     */
    @PutExchange("/{id}/options/{optionId}/actions")
    Mono<SuccessResponse<Void>> replaceOptionActions(
            @PathVariable("id") Long id,
            @PathVariable("optionId") Long optionId,
            @RequestBody List<TenantRoleActionDto> actions,
            @RequestHeader("Authorization") String authorization);

    /**
     * Elimina las personalizaciones de acciones del recurso (vuelve al
     * contexto general del rol).
     *
     * @param id            identificador del rol
     * @param optionId      identificador de la opción (recurso)
     * @param authorization header {@code Bearer <jwt>} del admin del tenant
     * @return señal de completitud
     */
    @DeleteExchange("/{id}/options/{optionId}/actions")
    Mono<SuccessResponse<Void>> clearOptionActions(
            @PathVariable("id") Long id,
            @PathVariable("optionId") Long optionId,
            @RequestHeader("Authorization") String authorization);

    /**
     * Actualiza el contexto general de acciones del rol (materializándolo
     * desde el template si el rol es previo a la capa tenant).
     *
     * @param id            identificador del rol
     * @param actions       set deseado (actionId + granted)
     * @param authorization header {@code Bearer <jwt>} del admin del tenant
     * @return señal de completitud
     */
    @PutExchange("/{id}/actions")
    Mono<SuccessResponse<Void>> replaceGeneralActions(
            @PathVariable("id") Long id,
            @RequestBody List<TenantRoleActionDto> actions,
            @RequestHeader("Authorization") String authorization);
}
