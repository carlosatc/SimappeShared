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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.DeleteExchange;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;
import org.springframework.web.service.annotation.PutExchange;

import com.catcsoft.simappe.commons.api.v1.core.error.SimappeException;
import com.catcsoft.simappe.model.core.query.PageDto;
import com.catcsoft.simappe.model.core.query.SimappeRequestQuery;
import com.catcsoft.simappe.model.core.response.SuccessResponse;
import com.catcsoft.simappe.commons.api.v1.util.SimappeConstant;
import com.catcsoft.simappe.model.admin.dto.ApiRouteDto;

import jakarta.validation.Valid;
import reactor.core.publisher.Mono;

/**
 * Cliente WebClient modular para operaciones de monitoreo de rutas API en
 * Simappe.
 * Proporciona métodos para la gestión de rutas API y su configuración.
 * 
 * <p>
 * Este cliente forma parte de la arquitectura modular v2 que reemplaza
 * gradualmente
 * al SimappeAdminWebClient monolítico, ofreciendo una interfaz especializada
 * para monitoring.
 * 
 * @author torrescamargo@gmail.com
 * @since 2026-01-19
 * @version 2.6.0
 */
@HttpExchange("simappe-admin")
public interface MonitoringReactiveClient {

    // ========== GESTIÓN DE RUTAS API (API ROUTE MANAGEMENT) ==========

    @GetExchange(value = "/api/v1/api-route" + SimappeConstant.PATH_READ, accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<List<ApiRouteDto>>> readApiRoutes();

    @GetExchange(value = "/api/v1/api-route/read-status", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<List<ApiRouteDto>>> readApiRoutesByStatus(@RequestParam String status);

    @GetExchange(value = "/api/v1/api-route/read-method", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<List<ApiRouteDto>>> readApiRoutesByMethod(@RequestParam String method);

    @GetExchange(value = "/api/v1/api-route" + SimappeConstant.PATH_GET, accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<ApiRouteDto>> getApiRoute(@RequestParam Long id);

    @GetExchange(value = "/api/v1/api-route/get-route-identifier", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<ApiRouteDto>> getApiRouteByIdentifier(@RequestParam String routeIdentifier);

    @PostExchange(value = "/api/v1/api-route" + SimappeConstant.PATH_CREATE, accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<ApiRouteDto>> createApiRoute(@Valid @RequestBody ApiRouteDto dto) throws SimappeException;

    @PutExchange(value = "/api/v1/api-route" + SimappeConstant.PATH_UPDATE, accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<ApiRouteDto>> updateApiRoute(@Valid @RequestBody ApiRouteDto dto, @RequestParam Long id);

    @DeleteExchange(value = "/api/v1/api-route"
            + SimappeConstant.PATH_DELETE, accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<Void>> deleteApiRoute(@RequestParam Long id);

    @PostExchange(value = "/api/v1/api-route" + SimappeConstant.PATH_PAGE, accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<PageDto<ApiRouteDto>>> getApiRoutePage(@RequestBody SimappeRequestQuery requestQuery)
            throws SimappeException;
}
