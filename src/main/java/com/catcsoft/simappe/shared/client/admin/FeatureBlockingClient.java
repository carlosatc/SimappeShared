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
import com.catcsoft.simappe.model.admin.dto.featureflag.FeatureFlagDto;
import com.catcsoft.simappe.model.admin.dto.featureflag.FeatureFlagOverrideDto;
import com.catcsoft.simappe.model.admin.dto.subscription.FeatureDto;

import jakarta.validation.Valid;

/**
 * Cliente WebClient modular para operaciones de feature flags en Simappe.
 * Proporciona métodos para la gestión de características, banderas de
 * características
 * y sobrescrituras de feature flags.
 * 
 * <p>
 * Este cliente forma parte de la arquitectura modular v2 que reemplaza
 * gradualmente
 * al SimappeAdminWebClient monolítico, ofreciendo una interfaz especializada
 * para feature toggles.
 * 
 * @author torrescamargo@gmail.com
 * @since 2026-01-19
 * @version 2.6.0
 */
@HttpExchange("simappe-admin")
public interface FeatureBlockingClient {

    // ========== GESTIÓN DE CARACTERÍSTICAS (FEATURE MANAGEMENT) ==========

    @GetExchange(value = "/api/v1/feature/read", accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<List<FeatureDto>> readFeatures();

    @GetExchange(value = "/api/v1/feature/read-active", accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<List<FeatureDto>> readActiveFeatures();

    @GetExchange(value = "/api/v1/feature/get", accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<FeatureDto> getFeature(@RequestParam Long id);

    @GetExchange(value = "/api/v1/feature/get-by-code", accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<FeatureDto> getFeatureByCode(@RequestParam String code);

    @PostExchange(value = "/api/v1/feature/create", accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<FeatureDto> createFeature(@Valid @RequestBody FeatureDto dto) throws SimappeException;

    @PutExchange(value = "/api/v1/feature/update", accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<FeatureDto> updateFeature(@Valid @RequestBody FeatureDto dto, @RequestParam Long id);

    @DeleteExchange(value = "/api/v1/feature/delete", accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<Void> deleteFeature(@RequestParam Long id);

    @PostExchange(value = "/api/v1/feature/page", accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<PageDto<FeatureDto>> getFeaturePage(@RequestBody SimappeRequestQuery requestQuery)
            throws SimappeException;

    // ========== GESTIÓN DE BANDERAS DE CARACTERÍSTICAS (FEATURE FLAG MANAGEMENT)
    // ==========

    @GetExchange(value = "/api/v1/feature-flag" + SimappeConstant.PATH_READ, accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<List<FeatureFlagDto>> readFeatureFlags();

    @GetExchange(value = "/api/v1/feature-flag/read-active", accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<List<FeatureFlagDto>> readActiveFeatureFlags();

    @GetExchange(value = "/api/v1/feature-flag" + SimappeConstant.PATH_GET, accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<FeatureFlagDto> getFeatureFlag(@RequestParam Long id);

    @GetExchange(value = "/api/v1/feature-flag/get-code", accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<FeatureFlagDto> getFeatureFlagByCode(@RequestParam String code);

    @PostExchange(value = "/api/v1/feature-flag"
            + SimappeConstant.PATH_CREATE, accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<FeatureFlagDto> createFeatureFlag(@Valid @RequestBody FeatureFlagDto dto)
            throws SimappeException;

    @PutExchange(value = "/api/v1/feature-flag"
            + SimappeConstant.PATH_UPDATE, accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<FeatureFlagDto> updateFeatureFlag(@Valid @RequestBody FeatureFlagDto dto,
            @RequestParam Long id);

    @DeleteExchange(value = "/api/v1/feature-flag"
            + SimappeConstant.PATH_DELETE, accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<Void> deleteFeatureFlag(@RequestParam Long id);

    @PostExchange(value = "/api/v1/feature-flag" + SimappeConstant.PATH_PAGE, accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<PageDto<FeatureFlagDto>> getFeatureFlagPage(@RequestBody SimappeRequestQuery requestQuery)
            throws SimappeException;

    // ========== GESTIÓN DE SOBRESCRITURAS DE FEATURE FLAGS (FEATURE FLAG OVERRIDE
    // MANAGEMENT) ==========

    @GetExchange(value = "/api/v1/feature-flag-override"
            + SimappeConstant.PATH_READ, accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<List<FeatureFlagOverrideDto>> readFeatureFlagOverrides();

    @GetExchange(value = "/api/v1/feature-flag-override/read-active", accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<List<FeatureFlagOverrideDto>> readActiveFeatureFlagOverrides();

    @GetExchange(value = "/api/v1/feature-flag-override"
            + SimappeConstant.PATH_GET, accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<FeatureFlagOverrideDto> getFeatureFlagOverride(@RequestParam Long id);

    @PostExchange(value = "/api/v1/feature-flag-override"
            + SimappeConstant.PATH_CREATE, accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<FeatureFlagOverrideDto> createFeatureFlagOverride(
            @Valid @RequestBody FeatureFlagOverrideDto dto) throws SimappeException;

    @PutExchange(value = "/api/v1/feature-flag-override"
            + SimappeConstant.PATH_UPDATE, accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<FeatureFlagOverrideDto> updateFeatureFlagOverride(
            @Valid @RequestBody FeatureFlagOverrideDto dto, @RequestParam Long id);

    @DeleteExchange(value = "/api/v1/feature-flag-override"
            + SimappeConstant.PATH_DELETE, accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<Void> deleteFeatureFlagOverride(@RequestParam Long id);

    @PostExchange(value = "/api/v1/feature-flag-override"
            + SimappeConstant.PATH_PAGE, accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<PageDto<FeatureFlagOverrideDto>> getFeatureFlagOverridePage(
            @RequestBody SimappeRequestQuery requestQuery) throws SimappeException;
}
