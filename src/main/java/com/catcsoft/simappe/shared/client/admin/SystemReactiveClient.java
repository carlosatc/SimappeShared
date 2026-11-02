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
import com.catcsoft.simappe.model.admin.dto.featureflag.EnvironmentConfigDto;
import com.catcsoft.simappe.model.admin.dto.parameter.SystemParametersDto;
import com.catcsoft.simappe.model.admin.dto.parameter.TemplateParametersDto;

import jakarta.validation.Valid;
import reactor.core.publisher.Mono;

/**
 * Cliente WebClient modular para operaciones de configuración de sistema en
 * Simappe.
 * Proporciona métodos para environment config, system parameters y template
 * parameters.
 * 
 * <p>
 * Este cliente forma parte de la arquitectura modular v2 que reemplaza
 * gradualmente
 * al SimappeAdminWebClient monolítico, ofreciendo una interfaz especializada
 * para system configuration.
 * 
 * @author torrescamargo@gmail.com
 * @since 2026-01-19
 * @version 2.6.0
 */
@HttpExchange("simappe-admin")
public interface SystemReactiveClient {

    // ========== GESTIÓN DE CONFIGURACIÓN DE ENTORNO (ENVIRONMENT CONFIG
    // MANAGEMENT) ==========

    @GetExchange(value = "/api/v1/environment-config"
            + SimappeConstant.PATH_READ, accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<List<EnvironmentConfigDto>>> readEnvironmentConfigs();

    @GetExchange(value = "/api/v1/environment-config"
            + SimappeConstant.PATH_GET, accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<EnvironmentConfigDto>> getEnvironmentConfig(@RequestParam Long id);

    @PostExchange(value = "/api/v1/environment-config"
            + SimappeConstant.PATH_CREATE, accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<EnvironmentConfigDto>> createEnvironmentConfig(@Valid @RequestBody EnvironmentConfigDto dto)
            throws SimappeException;

    @PutExchange(value = "/api/v1/environment-config"
            + SimappeConstant.PATH_UPDATE, accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<EnvironmentConfigDto>> updateEnvironmentConfig(@Valid @RequestBody EnvironmentConfigDto dto,
            @RequestParam Long id);

    @DeleteExchange(value = "/api/v1/environment-config"
            + SimappeConstant.PATH_DELETE, accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<Void>> deleteEnvironmentConfig(@RequestParam Long id);

    @PostExchange(value = "/api/v1/environment-config"
            + SimappeConstant.PATH_PAGE, accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<PageDto<EnvironmentConfigDto>>> getEnvironmentConfigPage(
            @RequestBody SimappeRequestQuery requestQuery) throws SimappeException;

    // ========== GESTIÓN DE PARÁMETROS DEL SISTEMA (SYSTEM PARAMETERS MANAGEMENT)
    // ==========

    @GetExchange(value = "/api/v1/system-parameters/read", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<List<SystemParametersDto>>> readSystemParameters();

    @GetExchange(value = "/api/v1/system-parameters/read-active", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<List<SystemParametersDto>>> readActiveSystemParameters();

    @GetExchange(value = "/api/v1/system-parameters/get", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<SystemParametersDto>> getSystemParameter(@RequestParam Long id);

    @PostExchange(value = "/api/v1/system-parameters/create", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<SystemParametersDto>> createSystemParameter(@Valid @RequestBody SystemParametersDto dto)
            throws SimappeException;

    @PostExchange(value = "/api/v1/system-parameters/init-parameters", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<SystemParametersDto>> initializeSystemParameters(@RequestParam Long customerId)
            throws SimappeException;

    @PutExchange(value = "/api/v1/system-parameters/update", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<SystemParametersDto>> updateSystemParameter(@Valid @RequestBody SystemParametersDto dto,
            @RequestParam Long id);

    @DeleteExchange(value = "/api/v1/system-parameters/delete", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<Void>> deleteSystemParameter(@RequestParam Long id);

    @PostExchange(value = "/api/v1/system-parameters/page", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<PageDto<SystemParametersDto>>> getSystemParameterPage(
            @RequestBody SimappeRequestQuery requestQuery) throws SimappeException;

    // ========== GESTIÓN DE PARÁMETROS DE PLANTILLA (TEMPLATE PARAMETERS
    // MANAGEMENT) ==========

    @GetExchange(value = "/api/v1/template-parameters/read-active", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<List<TemplateParametersDto>>> readActiveTemplateParameters();

    @GetExchange(value = "/api/v1/template-parameters/read-parameter-type", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<List<TemplateParametersDto>>> readTemplateParametersByType(@RequestParam String parameterType);

    @GetExchange(value = "/api/v1/template-parameters/get", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<TemplateParametersDto>> getTemplateParameter(@RequestParam Long id);

    @GetExchange(value = "/api/v1/template-parameters/get-name", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<TemplateParametersDto>> getTemplateParameterByName(@RequestParam String name);

    @PostExchange(value = "/api/v1/template-parameters/create", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<TemplateParametersDto>> createTemplateParameter(@Valid @RequestBody TemplateParametersDto dto)
            throws SimappeException;

    @PutExchange(value = "/api/v1/template-parameters/update", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<TemplateParametersDto>> updateTemplateParameter(@Valid @RequestBody TemplateParametersDto dto,
            @RequestParam Long id);

    @DeleteExchange(value = "/api/v1/template-parameters/delete", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<Void>> deleteTemplateParameter(@RequestParam Long id);

    @PostExchange(value = "/api/v1/template-parameters/page", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<PageDto<TemplateParametersDto>>> getTemplateParameterPage(
            @RequestBody SimappeRequestQuery requestQuery) throws SimappeException;
}
