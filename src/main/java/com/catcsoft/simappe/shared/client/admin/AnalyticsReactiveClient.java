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

import java.time.LocalDate;
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
import com.catcsoft.simappe.model.admin.dto.analytics.ApiUsageLogDto;
import com.catcsoft.simappe.model.admin.dto.analytics.CustomerActivityLogDto;
import com.catcsoft.simappe.model.admin.dto.analytics.FeatureUsageMetricDto;
import com.catcsoft.simappe.model.admin.dto.analytics.SystemHealthMetricDto;

import jakarta.validation.Valid;
import reactor.core.publisher.Mono;

/**
 * Cliente WebClient modular para operaciones de analytics en Simappe.
 * Proporciona métodos para logs de uso, actividad, métricas y facturación por
 * consumo.
 * 
 * @author torrescamargo@gmail.com
 * @since 2026-01-19
 * @version 2.6.0
 */
@HttpExchange("simappe-admin")
public interface AnalyticsReactiveClient {

    // ========== API USAGE LOGS ==========

    @GetExchange(value = "/api/v1/analytics/api-usage-logs"
            + SimappeConstant.PATH_READ, accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<List<ApiUsageLogDto>>> readApiUsageLogs();

    @GetExchange(value = "/api/v1/analytics/api-usage-logs/by-customer", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<List<ApiUsageLogDto>>> readApiUsageLogsByCustomer(@RequestParam Long customerId);

    @GetExchange(value = "/api/v1/analytics/api-usage-logs/by-user", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<List<ApiUsageLogDto>>> readApiUsageLogsByUser(@RequestParam Long userId);

    @GetExchange(value = "/api/v1/analytics/api-usage-logs/by-date-range", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<List<ApiUsageLogDto>>> readApiUsageLogsByDateRange(@RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate);

    @GetExchange(value = "/api/v1/analytics/api-usage-logs"
            + SimappeConstant.PATH_GET, accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<ApiUsageLogDto>> getApiUsageLog(@RequestParam Long id);

    @PostExchange(value = "/api/v1/analytics/api-usage-logs"
            + SimappeConstant.PATH_CREATE, accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<ApiUsageLogDto>> createApiUsageLog(@Valid @RequestBody ApiUsageLogDto dto)
            throws SimappeException;

    @PutExchange(value = "/api/v1/analytics/api-usage-logs"
            + SimappeConstant.PATH_UPDATE, accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<ApiUsageLogDto>> updateApiUsageLog(@Valid @RequestBody ApiUsageLogDto dto,
            @RequestParam Long id);

    @DeleteExchange(value = "/api/v1/analytics/api-usage-logs"
            + SimappeConstant.PATH_DELETE, accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<Void>> deleteApiUsageLog(@RequestParam Long id);

    @PostExchange(value = "/api/v1/analytics/api-usage-logs"
            + SimappeConstant.PATH_PAGE, accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<PageDto<ApiUsageLogDto>>> getApiUsageLogPage(@RequestBody SimappeRequestQuery requestQuery)
            throws SimappeException;

    // ========== CUSTOMER ACTIVITY LOGS ==========

    @PostExchange(value = "/api/v1/analytics/customer-activity-logs"
            + SimappeConstant.PATH_CREATE, accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<CustomerActivityLogDto>> createCustomerActivityLog(
            @Valid @RequestBody CustomerActivityLogDto dto) throws SimappeException;

    @GetExchange(value = "/api/v1/analytics/customer-activity-logs/by-date-range", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<List<CustomerActivityLogDto>>> readCustomerActivityLogsByDateRange(
            @RequestParam Long customerId,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate);

    @GetExchange(value = "/api/v1/analytics/customer-activity-logs/count", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<Long>> getCustomerActivityLogsCount(@RequestParam Long customerId);

    @DeleteExchange(value = "/api/v1/analytics/customer-activity-logs/delete-old", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<Void>> deleteOldCustomerActivityLogs(@RequestParam LocalDate beforeDate);

    // ========== FEATURE USAGE METRICS ==========

    @GetExchange(value = "/api/v1/analytics/feature-usage-metrics"
            + SimappeConstant.PATH_READ, accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<List<FeatureUsageMetricDto>>> readFeatureUsageMetrics();

    @GetExchange(value = "/api/v1/analytics/feature-usage-metrics/by-customer", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<List<FeatureUsageMetricDto>>> readFeatureUsageMetricsByCustomer(@RequestParam Long customerId);

    @GetExchange(value = "/api/v1/analytics/feature-usage-metrics/by-feature", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<List<FeatureUsageMetricDto>>> readFeatureUsageMetricsByFeature(@RequestParam Long featureId);

    @GetExchange(value = "/api/v1/analytics/feature-usage-metrics/by-period", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<List<FeatureUsageMetricDto>>> readFeatureUsageMetricsByPeriod(
            @RequestParam LocalDate startDate, @RequestParam LocalDate endDate);

    @GetExchange(value = "/api/v1/analytics/feature-usage-metrics"
            + SimappeConstant.PATH_GET, accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<FeatureUsageMetricDto>> getFeatureUsageMetric(@RequestParam Long id);

    @PostExchange(value = "/api/v1/analytics/feature-usage-metrics"
            + SimappeConstant.PATH_CREATE, accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<FeatureUsageMetricDto>> createFeatureUsageMetric(@Valid @RequestBody FeatureUsageMetricDto dto)
            throws SimappeException;

    @PutExchange(value = "/api/v1/analytics/feature-usage-metrics"
            + SimappeConstant.PATH_UPDATE, accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<FeatureUsageMetricDto>> updateFeatureUsageMetric(@Valid @RequestBody FeatureUsageMetricDto dto,
            @RequestParam Long id);

    @DeleteExchange(value = "/api/v1/analytics/feature-usage-metrics"
            + SimappeConstant.PATH_DELETE, accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<Void>> deleteFeatureUsageMetric(@RequestParam Long id);

    @PostExchange(value = "/api/v1/analytics/feature-usage-metrics"
            + SimappeConstant.PATH_PAGE, accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<PageDto<FeatureUsageMetricDto>>> getFeatureUsageMetricPage(
            @RequestBody SimappeRequestQuery requestQuery) throws SimappeException;

    // ========== SYSTEM HEALTH METRICS ==========

    @GetExchange(value = "/api/v1/analytics/system-health-metrics"
            + SimappeConstant.PATH_READ, accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<List<SystemHealthMetricDto>>> readSystemHealthMetrics();

    @GetExchange(value = "/api/v1/analytics/system-health-metrics"
            + SimappeConstant.PATH_GET, accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<SystemHealthMetricDto>> getSystemHealthMetric(@RequestParam Long id);

    @PostExchange(value = "/api/v1/analytics/system-health-metrics"
            + SimappeConstant.PATH_CREATE, accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<SystemHealthMetricDto>> createSystemHealthMetric(@Valid @RequestBody SystemHealthMetricDto dto)
            throws SimappeException;

    @PutExchange(value = "/api/v1/analytics/system-health-metrics"
            + SimappeConstant.PATH_UPDATE, accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<SystemHealthMetricDto>> updateSystemHealthMetric(@Valid @RequestBody SystemHealthMetricDto dto,
            @RequestParam Long id);

    @DeleteExchange(value = "/api/v1/analytics/system-health-metrics"
            + SimappeConstant.PATH_DELETE, accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<Void>> deleteSystemHealthMetric(@RequestParam Long id);

    @PostExchange(value = "/api/v1/analytics/system-health-metrics"
            + SimappeConstant.PATH_PAGE, accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<PageDto<SystemHealthMetricDto>>> getSystemHealthMetricPage(
            @RequestBody SimappeRequestQuery requestQuery) throws SimappeException;
}
