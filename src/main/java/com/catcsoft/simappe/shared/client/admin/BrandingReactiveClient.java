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
import com.catcsoft.simappe.model.admin.dto.branding.BrandAssetDto;
import com.catcsoft.simappe.model.admin.dto.branding.BrandConfigurationDto;
import com.catcsoft.simappe.model.admin.dto.branding.BrandThemeDto;

import jakarta.validation.Valid;
import reactor.core.publisher.Mono;

/**
 * Cliente WebClient modular para operaciones de branding en Simappe.
 * Proporciona métodos para brand assets, brand configuration y brand themes.
 * 
 * @author torrescamargo@gmail.com
 * @since 2026-01-19
 * @version 2.6.0
 */
@HttpExchange("simappe-admin")
public interface BrandingReactiveClient {

    // ========== GESTIÓN DE RECURSOS DE MARCA (BRAND ASSETS MANAGEMENT) ==========

    @GetExchange(value = "/api/v1/brand-assets" + SimappeConstant.PATH_READ, accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<List<BrandAssetDto>>> readBrandAssets();

    @GetExchange(value = "/api/v1/brand-assets" + SimappeConstant.PATH_GET, accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<BrandAssetDto>> getBrandAsset(@RequestParam Long id);

    @PostExchange(value = "/api/v1/brand-assets"
            + SimappeConstant.PATH_CREATE, accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<BrandAssetDto>> createBrandAsset(@Valid @RequestBody BrandAssetDto dto)
            throws SimappeException;

    @PutExchange(value = "/api/v1/brand-assets"
            + SimappeConstant.PATH_UPDATE, accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<BrandAssetDto>> updateBrandAsset(@Valid @RequestBody BrandAssetDto dto, @RequestParam Long id);

    @DeleteExchange(value = "/api/v1/brand-assets"
            + SimappeConstant.PATH_DELETE, accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<Void>> deleteBrandAsset(@RequestParam Long id);

    @PostExchange(value = "/api/v1/brand-assets" + SimappeConstant.PATH_PAGE, accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<PageDto<BrandAssetDto>>> getBrandAssetPage(@RequestBody SimappeRequestQuery requestQuery)
            throws SimappeException;

    // ========== GESTIÓN DE CONFIGURACIÓN DE MARCA (BRAND CONFIGURATION MANAGEMENT)
    // ==========

    @GetExchange(value = "/api/v1/brand-configuration"
            + SimappeConstant.PATH_READ, accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<List<BrandConfigurationDto>>> readBrandConfigurations();

    @GetExchange(value = "/api/v1/brand-configuration/by-customer", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<List<BrandConfigurationDto>>> readBrandConfigurationsByCustomer(@RequestParam Long customerId);

    @GetExchange(value = "/api/v1/brand-configuration/active-by-customer", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<BrandConfigurationDto>> getActiveBrandConfigurationByCustomer(@RequestParam Long customerId);

    @GetExchange(value = "/api/v1/brand-configuration"
            + SimappeConstant.PATH_GET, accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<BrandConfigurationDto>> getBrandConfiguration(@RequestParam Long id);

    @PostExchange(value = "/api/v1/brand-configuration"
            + SimappeConstant.PATH_CREATE, accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<BrandConfigurationDto>> createBrandConfiguration(@Valid @RequestBody BrandConfigurationDto dto)
            throws SimappeException;

    @PutExchange(value = "/api/v1/brand-configuration"
            + SimappeConstant.PATH_UPDATE, accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<BrandConfigurationDto>> updateBrandConfiguration(@Valid @RequestBody BrandConfigurationDto dto,
            @RequestParam Long id);

    @DeleteExchange(value = "/api/v1/brand-configuration"
            + SimappeConstant.PATH_DELETE, accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<Void>> deleteBrandConfiguration(@RequestParam Long id);

    @PostExchange(value = "/api/v1/brand-configuration"
            + SimappeConstant.PATH_PAGE, accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<PageDto<BrandConfigurationDto>>> getBrandConfigurationPage(
            @RequestBody SimappeRequestQuery requestQuery) throws SimappeException;

    // ========== GESTIÓN DE TEMAS DE MARCA (BRAND THEME MANAGEMENT) ==========

    @GetExchange(value = "/api/v1/brand-theme" + SimappeConstant.PATH_READ, accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<List<BrandThemeDto>>> readBrandThemes();

    @GetExchange(value = "/api/v1/brand-theme/public-active", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<List<BrandThemeDto>>> readPublicActiveBrandThemes();

    @GetExchange(value = "/api/v1/brand-theme" + SimappeConstant.PATH_GET, accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<BrandThemeDto>> getBrandTheme(@RequestParam Long id);

    @PostExchange(value = "/api/v1/brand-theme"
            + SimappeConstant.PATH_CREATE, accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<BrandThemeDto>> createBrandTheme(@Valid @RequestBody BrandThemeDto dto)
            throws SimappeException;

    @PutExchange(value = "/api/v1/brand-theme" + SimappeConstant.PATH_UPDATE, accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<BrandThemeDto>> updateBrandTheme(@Valid @RequestBody BrandThemeDto dto, @RequestParam Long id);

    @DeleteExchange(value = "/api/v1/brand-theme"
            + SimappeConstant.PATH_DELETE, accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<Void>> deleteBrandTheme(@RequestParam Long id);

    @PostExchange(value = "/api/v1/brand-theme" + SimappeConstant.PATH_PAGE, accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<PageDto<BrandThemeDto>>> getBrandThemePage(@RequestBody SimappeRequestQuery requestQuery)
            throws SimappeException;
}
