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
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.DeleteExchange;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;
import org.springframework.web.service.annotation.PutExchange;

import com.catcsoft.simappe.model.core.query.PageDto;
import com.catcsoft.simappe.model.core.query.SimappeRequestQuery;
import com.catcsoft.simappe.model.core.response.SuccessResponse;
import com.catcsoft.simappe.commons.api.v1.util.SimappeConstant;

import jakarta.validation.Valid;
import reactor.core.publisher.Mono;

/**
 * Cliente WebClient modular para operaciones de configuración en Simappe.
 * Proporciona métodos para la gestión de módulos, opciones y grupos de
 * opciones.
 * 
 * <p>
 * Este cliente forma parte de la arquitectura modular v2 que reemplaza
 * gradualmente
 * al SimappeAdminWebClient monolítico, ofreciendo una interfaz especializada
 * para configuration.
 * 
 * @author torrescamargo@gmail.com
 * @since 2026-01-18
 * @version 2.6.0
 */
@HttpExchange("simappe-admin")
public interface ConfigurationReactiveClient {

    // ========== GESTIÓN DE MÓDULOS (MODULE MANAGEMENT) ==========

    @GetExchange(value = "/api/v1/module" + SimappeConstant.PATH_READ, accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<List<?>>> readModules();

    @GetExchange(value = "/api/v1/module/read-status", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<List<?>>> readModulesByStatus(@RequestParam String status);

    @GetExchange(value = "/api/v1/module" + SimappeConstant.PATH_GET, accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<?>> getModule(@RequestParam Long id);

    @GetExchange(value = "/api/v1/module/get-name", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<?>> getModuleByName(@RequestParam String name);

    @GetExchange(value = "/api/v1/module/get-code", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<?>> getModuleByCode(@RequestParam String code);

    @PostExchange(value = "/api/v1/module" + SimappeConstant.PATH_CREATE, accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<?>> createModule(@Valid @RequestBody Object dto);

    @PutExchange(value = "/api/v1/module" + SimappeConstant.PATH_UPDATE, accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<?>> updateModule(@Valid @RequestBody Object dto, @RequestParam Long id);

    @PutExchange(value = "/api/v1/module/update-path", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<?>> updateModulePath(@RequestBody Map<String, Object> config);

    @PutExchange(value = "/api/v1/module/update-config", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<?>> updateModuleConfig(@RequestBody Map<String, Object> config);

    @PutExchange(value = "/api/v1/module/add-option-group", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<?>> addOptionGroupToModule(@RequestBody Map<String, Object> data);

    @DeleteExchange(value = "/api/v1/module/delete-option-group", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<Void>> deleteOptionGroupFromModule(@RequestBody Map<String, Object> data);

    @PutExchange(value = "/api/v1/module/add-module-package", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<?>> addModulePackageToModule(@RequestBody Map<String, Object> data);

    @DeleteExchange(value = "/api/v1/module/delete-module-package", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<Void>> deleteModulePackageFromModule(@RequestBody Map<String, Object> data);

    @DeleteExchange(value = "/api/v1/module" + SimappeConstant.PATH_DELETE, accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<Void>> deleteModule(@RequestParam Long id);

    @PostExchange(value = "/api/v1/module" + SimappeConstant.PATH_PAGE, accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<PageDto<?>>> getModulePage(@RequestBody SimappeRequestQuery query);

    // ========== OPTION MANAGEMENT ==========

    @GetExchange(value = "/api/v1/option" + SimappeConstant.PATH_READ, accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<List<?>>> readOptions();

    @GetExchange(value = "/api/v1/option/read-status", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<List<?>>> readOptionsByStatus(@RequestParam String status);

    @GetExchange(value = "/api/v1/option/read-parent-option", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<List<?>>> readParentOptions(@RequestParam Long parentId);

    @GetExchange(value = "/api/v1/option" + SimappeConstant.PATH_GET, accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<?>> getOption(@RequestParam Long id);

    @GetExchange(value = "/api/v1/option/get-name", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<?>> getOptionByName(@RequestParam String name);

    @GetExchange(value = "/api/v1/option/get-code", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<?>> getOptionByCode(@RequestParam String code);

    @PostExchange(value = "/api/v1/option" + SimappeConstant.PATH_CREATE, accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<?>> createOption(@Valid @RequestBody Object dto);

    @PutExchange(value = "/api/v1/option" + SimappeConstant.PATH_UPDATE, accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<?>> updateOption(@Valid @RequestBody Object dto, @RequestParam Long id);

    @PutExchange(value = "/api/v1/option/update-path", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<?>> updateOptionPath(@RequestBody Map<String, Object> config);

    @PutExchange(value = "/api/v1/option/update-config", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<?>> updateOptionConfig(@RequestBody Map<String, Object> config);

    @DeleteExchange(value = "/api/v1/option" + SimappeConstant.PATH_DELETE, accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<Void>> deleteOption(@RequestParam Long id);

    @PostExchange(value = "/api/v1/option" + SimappeConstant.PATH_PAGE, accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<PageDto<?>>> getOptionPage(@RequestBody SimappeRequestQuery query);

    // ========== GESTIÓN DE GRUPOS DE OPCIONES (OPTION GROUP MANAGEMENT) ==========

    @GetExchange(value = "/api/v1/option-group" + SimappeConstant.PATH_READ, accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<List<?>>> readOptionGroups();

    @GetExchange(value = "/api/v1/option-group/read-status", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<List<?>>> readOptionGroupsByStatus(@RequestParam String status);

    @GetExchange(value = "/api/v1/option-group/read-parent", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<List<?>>> readParentOptionGroups(@RequestParam Long parentId);

    @GetExchange(value = "/api/v1/option-group" + SimappeConstant.PATH_GET, accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<?>> getOptionGroup(@RequestParam Long id);

    @GetExchange(value = "/api/v1/option-group/get-name", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<?>> getOptionGroupByName(@RequestParam String name);

    @GetExchange(value = "/api/v1/option-group/get-code", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<?>> getOptionGroupByCode(@RequestParam String code);

    @PostExchange(value = "/api/v1/option-group"
            + SimappeConstant.PATH_CREATE, accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<?>> createOptionGroup(@Valid @RequestBody Object dto);

    @PutExchange(value = "/api/v1/option-group"
            + SimappeConstant.PATH_UPDATE, accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<?>> updateOptionGroup(@Valid @RequestBody Object dto, @RequestParam Long id);

    @PutExchange(value = "/api/v1/option-group/update-path", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<?>> updateOptionGroupPath(@RequestBody Map<String, Object> config);

    @PutExchange(value = "/api/v1/option-group/update-config", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<?>> updateOptionGroupConfig(@RequestBody Map<String, Object> config);

    @PutExchange(value = "/api/v1/option-group/add-option", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<?>> addOptionToGroup(@RequestBody Map<String, Object> data);

    @DeleteExchange(value = "/api/v1/option-group/delete-option", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<Void>> deleteOptionFromGroup(@RequestBody Map<String, Object> data);

    @DeleteExchange(value = "/api/v1/option-group"
            + SimappeConstant.PATH_DELETE, accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<Void>> deleteOptionGroup(@RequestParam Long id);

    @PostExchange(value = "/api/v1/option-group" + SimappeConstant.PATH_PAGE, accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<PageDto<?>>> getOptionGroupPage(@RequestBody SimappeRequestQuery query);
}
