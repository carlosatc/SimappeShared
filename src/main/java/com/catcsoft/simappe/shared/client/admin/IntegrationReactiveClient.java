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
import com.catcsoft.simappe.model.admin.dto.integration.IntegrationConfigDto;
import com.catcsoft.simappe.model.admin.dto.integration.IntegrationExecutionDto;
import com.catcsoft.simappe.model.admin.dto.integration.IntegrationLogDto;
import com.catcsoft.simappe.model.admin.dto.integration.IntegrationProviderDto;
import com.catcsoft.simappe.model.admin.dto.integration.IntegrationScheduleDto;

import jakarta.validation.Valid;
import reactor.core.publisher.Mono;

/**
 * Cliente WebClient modular para operaciones de integración en Simappe.
 * Proporciona métodos para la gestión de configuraciones, ejecuciones, logs,
 * proveedores y horarios de integración.
 * 
 * <p>
 * Este cliente forma parte de la arquitectura modular v2 que reemplaza
 * gradualmente
 * al SimappeAdminWebClient monolítico, ofreciendo una interfaz especializada
 * para integrations.
 * 
 * @author torrescamargo@gmail.com
 * @since 2026-01-18
 * @version 2.6.0
 */
@HttpExchange("simappe-admin")
public interface IntegrationReactiveClient {

    // ========== GESTIÓN DE CONFIGURACIÓN DE INTEGRACIÓN (INTEGRATION CONFIG
    // MANAGEMENT) ==========

    @GetExchange(value = "/api/v1/integration-config"
            + SimappeConstant.PATH_READ, accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<List<IntegrationConfigDto>>> readIntegrationConfigs();

    @GetExchange(value = "/api/v1/integration-config"
            + SimappeConstant.PATH_GET, accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<IntegrationConfigDto>> getIntegrationConfigId(@RequestParam Long id);

    @PostExchange(value = "/api/v1/integration-config"
            + SimappeConstant.PATH_CREATE, accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<IntegrationConfigDto>> createIntegrationConfig(@Valid @RequestBody IntegrationConfigDto dto)
            throws SimappeException;

    @PutExchange(value = "/api/v1/integration-config"
            + SimappeConstant.PATH_UPDATE, accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<IntegrationConfigDto>> updateIntegrationConfig(@Valid @RequestBody IntegrationConfigDto dto,
            @RequestParam Long id);

    @DeleteExchange(value = "/api/v1/integration-config"
            + SimappeConstant.PATH_DELETE, accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<Void>> deleteIntegrationConfig(@RequestParam Long id);

    @PostExchange(value = "/api/v1/integration-config"
            + SimappeConstant.PATH_PAGE, accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<PageDto<IntegrationConfigDto>>> getIntegrationConfigPage(
            @RequestBody SimappeRequestQuery requestQuery) throws SimappeException;

    // ========== GESTIÓN DE EJECUCIÓN DE INTEGRACIÓN (INTEGRATION EXECUTION
    // MANAGEMENT) ==========

    @GetExchange(value = "/api/v1/integration-execution"
            + SimappeConstant.PATH_READ, accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<List<IntegrationExecutionDto>>> readIntegrationExecutions();

    @GetExchange(value = "/api/v1/integration-execution"
            + SimappeConstant.PATH_GET, accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<IntegrationExecutionDto>> getIntegrationExecutionId(@RequestParam Long id);

    @PostExchange(value = "/api/v1/integration-execution"
            + SimappeConstant.PATH_CREATE, accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<IntegrationExecutionDto>> createIntegrationExecution(
            @Valid @RequestBody IntegrationExecutionDto dto) throws SimappeException;

    @PutExchange(value = "/api/v1/integration-execution"
            + SimappeConstant.PATH_UPDATE, accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<IntegrationExecutionDto>> updateIntegrationExecution(
            @Valid @RequestBody IntegrationExecutionDto dto, @RequestParam Long id);

    @DeleteExchange(value = "/api/v1/integration-execution"
            + SimappeConstant.PATH_DELETE, accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<Void>> deleteIntegrationExecution(@RequestParam Long id);

    @PostExchange(value = "/api/v1/integration-execution"
            + SimappeConstant.PATH_PAGE, accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<PageDto<IntegrationExecutionDto>>> getIntegrationExecutionPage(
            @RequestBody SimappeRequestQuery requestQuery) throws SimappeException;

    // ========== GESTIÓN DE REGISTROS DE INTEGRACIÓN (INTEGRATION LOG MANAGEMENT)
    // ==========

    @GetExchange(value = "/api/v1/integration-log"
            + SimappeConstant.PATH_READ, accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<List<IntegrationLogDto>>> readIntegrationLogs();

    @GetExchange(value = "/api/v1/integration-log"
            + SimappeConstant.PATH_GET, accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<IntegrationLogDto>> getIntegrationLogId(@RequestParam Long id);

    @PostExchange(value = "/api/v1/integration-log"
            + SimappeConstant.PATH_CREATE, accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<IntegrationLogDto>> createIntegrationLog(@Valid @RequestBody IntegrationLogDto dto)
            throws SimappeException;

    @PutExchange(value = "/api/v1/integration-log"
            + SimappeConstant.PATH_UPDATE, accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<IntegrationLogDto>> updateIntegrationLog(@Valid @RequestBody IntegrationLogDto dto,
            @RequestParam Long id);

    @DeleteExchange(value = "/api/v1/integration-log"
            + SimappeConstant.PATH_DELETE, accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<Void>> deleteIntegrationLog(@RequestParam Long id);

    @PostExchange(value = "/api/v1/integration-log"
            + SimappeConstant.PATH_PAGE, accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<PageDto<IntegrationLogDto>>> getIntegrationLogPage(
            @RequestBody SimappeRequestQuery requestQuery) throws SimappeException;

    // ========== GESTIÓN DE PROVEEDORES DE INTEGRACIÓN (INTEGRATION PROVIDER
    // MANAGEMENT) ==========

    @GetExchange(value = "/api/v1/integration-provider"
            + SimappeConstant.PATH_READ, accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<List<IntegrationProviderDto>>> readIntegrationProviders();

    @GetExchange(value = "/api/v1/integration-provider"
            + SimappeConstant.PATH_GET, accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<IntegrationProviderDto>> getIntegrationProviderId(@RequestParam Long id);

    @PostExchange(value = "/api/v1/integration-provider"
            + SimappeConstant.PATH_CREATE, accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<IntegrationProviderDto>> createIntegrationProvider(
            @Valid @RequestBody IntegrationProviderDto dto) throws SimappeException;

    @PutExchange(value = "/api/v1/integration-provider"
            + SimappeConstant.PATH_UPDATE, accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<IntegrationProviderDto>> updateIntegrationProvider(
            @Valid @RequestBody IntegrationProviderDto dto, @RequestParam Long id);

    @DeleteExchange(value = "/api/v1/integration-provider"
            + SimappeConstant.PATH_DELETE, accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<Void>> deleteIntegrationProvider(@RequestParam Long id);

    @PostExchange(value = "/api/v1/integration-provider"
            + SimappeConstant.PATH_PAGE, accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<PageDto<IntegrationProviderDto>>> getIntegrationProviderPage(
            @RequestBody SimappeRequestQuery requestQuery) throws SimappeException;

    // ========== GESTIÓN DE HORARIOS DE INTEGRACIÓN (INTEGRATION SCHEDULE
    // MANAGEMENT) ==========

    @GetExchange(value = "/api/v1/integration-schedule"
            + SimappeConstant.PATH_READ, accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<List<IntegrationScheduleDto>>> readIntegrationSchedules();

    @GetExchange(value = "/api/v1/integration-schedule"
            + SimappeConstant.PATH_GET, accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<IntegrationScheduleDto>> getIntegrationScheduleId(@RequestParam Long id);

    @PostExchange(value = "/api/v1/integration-schedule"
            + SimappeConstant.PATH_CREATE, accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<IntegrationScheduleDto>> createIntegrationSchedule(
            @Valid @RequestBody IntegrationScheduleDto dto) throws SimappeException;

    @PutExchange(value = "/api/v1/integration-schedule"
            + SimappeConstant.PATH_UPDATE, accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<IntegrationScheduleDto>> updateIntegrationSchedule(
            @Valid @RequestBody IntegrationScheduleDto dto, @RequestParam Long id);

    @DeleteExchange(value = "/api/v1/integration-schedule"
            + SimappeConstant.PATH_DELETE, accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<Void>> deleteIntegrationSchedule(@RequestParam Long id);

    @PostExchange(value = "/api/v1/integration-schedule"
            + SimappeConstant.PATH_PAGE, accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<PageDto<IntegrationScheduleDto>>> getIntegrationSchedulePage(
            @RequestBody SimappeRequestQuery requestQuery) throws SimappeException;
}
