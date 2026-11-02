/*
 * Copyright (c) 2025 CatcSoft
 *
 * This software is the confidential and proprietary information of CatcSoft
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of a
 * written license agreement entered into with CatcSoft.
 *
 * Author: torrescamargo@gmail.com
 */
package com.catcsoft.simappe.shared.client.admin;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
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
import com.catcsoft.simappe.model.admin.dto.CompanyDto;
import com.catcsoft.simappe.model.admin.dto.CustomerDto;
import com.catcsoft.simappe.model.admin.dto.analytics.ApiUsageLogDto;
import com.catcsoft.simappe.model.admin.dto.analytics.CustomerActivityLogDto;
import com.catcsoft.simappe.model.admin.dto.analytics.FeatureUsageMetricDto;
import com.catcsoft.simappe.model.admin.dto.analytics.SystemHealthMetricDto;
import com.catcsoft.simappe.model.admin.dto.analytics.UsageBillingRecordDto;
import com.catcsoft.simappe.model.admin.dto.apiversioning.ApiEndpointDto;
import com.catcsoft.simappe.model.admin.dto.apiversioning.ClientApiUsageDto;
import com.catcsoft.simappe.model.admin.dto.appconfig.ApplicationDto;
import com.catcsoft.simappe.model.admin.dto.appconfig.VMenuDto;
import com.catcsoft.simappe.model.admin.dto.baloto.BalotoDTO;
import com.catcsoft.simappe.model.admin.dto.baloto.BalotoType;
import com.catcsoft.simappe.model.admin.dto.baloto.PredictionResult;
import com.catcsoft.simappe.model.admin.dto.billing.InvoiceDto;
import com.catcsoft.simappe.model.admin.dto.billing.InvoiceItemDto;
import com.catcsoft.simappe.model.admin.dto.billing.PaymentDto;
import com.catcsoft.simappe.model.admin.dto.billing.PaymentMethodDto;
import com.catcsoft.simappe.model.admin.dto.billing.stripe.StripeCustomerDto;
import com.catcsoft.simappe.model.admin.dto.billing.stripe.StripePaymentIntentDto;
import com.catcsoft.simappe.model.admin.dto.billing.stripe.StripePaymentMethodDto;
import com.catcsoft.simappe.model.admin.dto.billing.stripe.StripeRefundDto;
import com.catcsoft.simappe.model.admin.dto.branding.BrandAssetDto;
import com.catcsoft.simappe.model.admin.dto.branding.BrandConfigurationDto;
import com.catcsoft.simappe.model.admin.dto.branding.BrandThemeDto;
import com.catcsoft.simappe.model.admin.dto.config.ConsecutiveDto;
import com.catcsoft.simappe.model.admin.dto.contract.ApplicationContractDto;
import com.catcsoft.simappe.model.admin.dto.featureflag.EnvironmentConfigDto;
import com.catcsoft.simappe.model.admin.dto.featureflag.FeatureFlagDto;
import com.catcsoft.simappe.model.admin.dto.featureflag.FeatureFlagOverrideDto;
import com.catcsoft.simappe.model.admin.dto.gdpr.DataRetentionPolicyDto;
import com.catcsoft.simappe.model.admin.dto.gdpr.GdprConsentRecordDto;
import com.catcsoft.simappe.model.admin.dto.gdpr.GdprDataBreachDto;
import com.catcsoft.simappe.model.admin.dto.gdpr.GdprDataRequestDto;
import com.catcsoft.simappe.model.admin.dto.gdpr.GdprProcessingActivityDto;
import com.catcsoft.simappe.model.admin.dto.i18n.LanguageDto;
import com.catcsoft.simappe.model.admin.dto.i18n.LocaleDto;
import com.catcsoft.simappe.model.admin.dto.i18n.TranslationBundleDto;
import com.catcsoft.simappe.model.admin.dto.i18n.TranslationDto;
import com.catcsoft.simappe.model.admin.dto.i18n.TranslationKeyDto;
import com.catcsoft.simappe.model.admin.dto.integration.IntegrationConfigDto;
import com.catcsoft.simappe.model.admin.dto.integration.IntegrationExecutionDto;
import com.catcsoft.simappe.model.admin.dto.integration.IntegrationLogDto;
import com.catcsoft.simappe.model.admin.dto.integration.IntegrationProviderDto;
import com.catcsoft.simappe.model.admin.dto.integration.IntegrationScheduleDto;
import com.catcsoft.simappe.model.admin.dto.maintenance.MaintenanceWindowDto;
import com.catcsoft.simappe.model.admin.dto.oauth.OAuthClientDto;
import com.catcsoft.simappe.model.admin.dto.oauth.PerpetualTokenDto;
import com.catcsoft.simappe.model.admin.dto.parameter.CompanyParametersDto;
import com.catcsoft.simappe.model.admin.dto.parameter.CustomerParametersDto;
import com.catcsoft.simappe.model.admin.dto.parameter.SystemParametersDto;
import com.catcsoft.simappe.model.admin.dto.parameter.TemplateParametersDto;
import com.catcsoft.simappe.model.admin.dto.security.RoleDto;
import com.catcsoft.simappe.model.admin.dto.customerrole.CustomerRoleDto;
import com.catcsoft.simappe.model.admin.dto.security.UserDto;
import com.catcsoft.simappe.model.admin.dto.security.UserIdentityDto;
import com.catcsoft.simappe.model.admin.dto.subscription.CustomerSubscriptionDto;
import com.catcsoft.simappe.model.admin.dto.subscription.FeatureDto;
import com.catcsoft.simappe.model.admin.dto.subscription.LicenseDto;
import com.catcsoft.simappe.model.admin.dto.subscription.SubscriptionPlanDto;
import com.catcsoft.simappe.model.admin.dto.subscription.UsageTrackingDto;
import com.catcsoft.simappe.model.admin.dto.territory.CityDto;
import com.catcsoft.simappe.model.admin.dto.territory.CountryDto;
import com.catcsoft.simappe.model.admin.dto.territory.StateDto;
import com.catcsoft.simappe.model.admin.dto.todo.Priority;
import com.catcsoft.simappe.model.admin.dto.todo.TodoDTO;
import com.catcsoft.simappe.model.admin.dto.util.TransferDto2;
import com.catcsoft.simappe.model.admin.dto.webhook.WebhookDeliveryDto;
import com.catcsoft.simappe.model.admin.dto.webhook.WebhookEndpointDto;
import com.catcsoft.simappe.model.admin.dto.webhook.WebhookEventDto;
import com.catcsoft.simappe.model.admin.dto.workflow.ApprovalRequestDto;
import com.catcsoft.simappe.model.admin.dto.workflow.WorkflowDefinitionDto;
import com.catcsoft.simappe.model.admin.dto.workflow.WorkflowInstanceDto;
import com.catcsoft.simappe.model.admin.dto.workflow.WorkflowStepExecutionDto;
import com.catcsoft.simappe.model.admin.enums.ApplicationType;
import com.catcsoft.simappe.model.admin.enums.ConsecutiveScope;
import com.catcsoft.simappe.model.admin.enums.ContractStatus;
import com.catcsoft.simappe.model.admin.enums.CustomerType;
import com.catcsoft.simappe.model.admin.enums.RegistrationStatus;
import com.catcsoft.simappe.model.admin.enums.analytics.BillingStatus;
import com.catcsoft.simappe.model.admin.enums.analytics.PeriodType;
import com.catcsoft.simappe.model.core.database.dto.DatabaseConfigV2Dto;
import com.catcsoft.simappe.model.core.util.dto.ServiceResponseAvailable;
import com.catcsoft.simappe.model.admin.enums.CurrencyCode;

import jakarta.validation.Valid;

/**
 * Interfaz WebClient para interactuar con los servicios administrativos de
 * Simappe.
 * Proporciona métodos para la gestión de logs de uso de API, actividad de
 * clientes,
 * parámetros del sistema, módulos, opciones y otros recursos de administración.
 *
 * @author torrescamargo@gmail.com
 * @since 2025-01-18
 */
@HttpExchange("simappe-admin")
public interface SimappeAdminBlockingClient {

        // ========== GESTIÓN DE LOGS DE USO DE API (API USAGE LOG MANAGEMENT)
        // ==========

        /**
         * Verifica la disponibilidad del servicio de logs de uso de API.
         *
         * @return la respuesta con el estado de disponibilidad del servicio
         */
        @GetExchange(value = "/api/v1/analytics/api-usage-logs"
                        + SimappeConstant.PATH_INDEX, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<ServiceResponseAvailable> apiUsageLogIndex();

        /**
         * Recupera todos los registros de uso de API.
         *
         * @return la lista de registros de uso de API
         */
        @GetExchange(value = "/api/v1/analytics/api-usage-logs"
                        + SimappeConstant.PATH_READ, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<ApiUsageLogDto>> readApiUsageLogs();

        /**
         * Recupera los registros de uso de API por ID de cliente.
         *
         * @param customerId el ID del cliente
         * @return la lista de registros de uso de API para el cliente
         */
        @GetExchange(value = "/api/v1/analytics/api-usage-logs/by-customer", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<ApiUsageLogDto>> getApiUsageLogsByCustomerId(@RequestParam Long customerId);

        /**
         * Recupera los registros de uso de API por ID de usuario.
         *
         * @param userId el ID del usuario
         * @return la lista de registros de uso de API para el usuario
         */
        @GetExchange(value = "/api/v1/analytics/api-usage-logs/by-user", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<ApiUsageLogDto>> getApiUsageLogsByUserId(@RequestParam Long userId);

        /**
         * Recupera los registros de uso de API por rango de fechas.
         *
         * @param startDate la fecha de inicio
         * @param endDate   la fecha de fin
         * @return la lista de registros de uso de API dentro del rango de fechas
         */
        @GetExchange(value = "/api/v1/analytics/api-usage-logs/by-date-range", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<ApiUsageLogDto>> getApiUsageLogsByDateRange(
                        @RequestParam Date startDate,
                        @RequestParam Date endDate);

        /**
         * Recupera los detalles de un registro de uso de API por su ID.
         *
         * @param id el ID del registro de uso de API
         * @return los detalles del registro de uso de API
         */
        @GetExchange(value = "/api/v1/analytics/api-usage-logs"
                        + SimappeConstant.PATH_GET, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<ApiUsageLogDto> getApiUsageLogId(@RequestParam Long id);

        /**
         * Crea un nuevo registro de uso de API.
         *
         * @param dto el DTO del registro de uso de API
         * @return el registro de uso de API creado
         * @throws SimappeException si ocurre un error durante la creación
         */
        @PostExchange(value = "/api/v1/analytics/api-usage-logs"
                        + SimappeConstant.PATH_CREATE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<ApiUsageLogDto> createApiUsageLog(@Valid @RequestBody ApiUsageLogDto dto)
                        throws SimappeException;

        /**
         * Actualiza un registro de uso de API existente.
         *
         * @param dto el DTO del registro de uso de API con los cambios
         * @param id  el ID del registro de uso de API a actualizar
         * @return el registro de uso de API actualizado
         */
        @PutExchange(value = "/api/v1/analytics/api-usage-logs"
                        + SimappeConstant.PATH_UPDATE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<ApiUsageLogDto> updateApiUsageLog(@Valid @RequestBody ApiUsageLogDto dto,
                        @RequestParam Long id);

        /**
         * Elimina un registro de uso de API por su ID.
         *
         * @param id el ID del registro de uso de API a eliminar
         * @return una respuesta vacía indicando el resultado de la operación
         */
        @DeleteExchange(value = "/api/v1/analytics/api-usage-logs"
                        + SimappeConstant.PATH_DELETE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<Void> deleteApiUsageLog(@RequestParam Long id);

        /**
         * Recupera una lista paginada de registros de uso de API.
         *
         * @param requestQuery los parámetros de consulta y paginación
         * @return una página de registros de uso de API
         * @throws SimappeException si ocurre un error durante la consulta
         */
        @PostExchange(value = "/api/v1/analytics/api-usage-logs"
                        + SimappeConstant.PATH_PAGE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<PageDto<ApiUsageLogDto>> getApiUsageLogPage(@RequestBody SimappeRequestQuery requestQuery)
                        throws SimappeException;

        // ========== GESTIÓN DE LOGS DE ACTIVIDAD DE CLIENTES (CUSTOMER ACTIVITY LOG
        // MANAGEMENT) ==========

        /**
         * Verifica la disponibilidad del servicio de logs de actividad de clientes.
         *
         * @return la respuesta con el estado de disponibilidad del servicio
         */
        @GetExchange(value = "/api/v1/analytics/customer-activity-logs"
                        + SimappeConstant.PATH_INDEX, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<ServiceResponseAvailable> customerActivityLogIndex();

        /**
         * Crea un nuevo registro de actividad de cliente.
         *
         * @param dto el DTO del registro de actividad de cliente
         * @return el registro de actividad de cliente creado
         */
        @PostExchange(value = "/api/v1/analytics/customer-activity-logs"
                        + SimappeConstant.PATH_CREATE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<CustomerActivityLogDto> createCustomerActivityLog(
                        @Valid @RequestBody CustomerActivityLogDto dto);

        /**
         * Recupera los registros de actividad de clientes por ID de cliente (paginado).
         *
         * @param customerId el ID del cliente
         * @param page       el número de página
         * @param size       el tamaño de la página
         * @return una página de registros de actividad de clientes
         */
        /*
         * @GetExchange(value = "/api/v1/analytics/customer-activity-logs/by-customer",
         * accept = MediaType.APPLICATION_JSON_VALUE)
         * SuccessResponse<Page<CustomerActivityLogDto>>
         * getCustomerActivityLogsByCustomerId(
         * 
         * @RequestParam Long customerId,
         * 
         * @RequestParam(defaultValue = "0") int page,
         * 
         * @RequestParam(defaultValue = "20") int size);
         */

        /**
         * Recupera los registros de actividad de clientes por ID de usuario (paginado).
         *
         * @param userId el ID del usuario
         * @param page   el número de página
         * @param size   el tamaño de la página
         * @return una página de registros de actividad de clientes
         */
        /*
         * @GetExchange(value = "/api/v1/analytics/customer-activity-logs/by-user",
         * accept = MediaType.APPLICATION_JSON_VALUE)
         * SuccessResponse<Page<CustomerActivityLogDto>>
         * getCustomerActivityLogsByUserId(
         * 
         * @RequestParam Long userId,
         * 
         * @RequestParam(defaultValue = "0") int page,
         * 
         * @RequestParam(defaultValue = "20") int size);
         */

        /**
         * Recupera los registros de actividad de clientes por rango de fechas.
         *
         * @param customerId el ID del cliente
         * @param startDate  la fecha de inicio
         * @param endDate    la fecha de fin
         * @return la lista de registros de actividad de clientes dentro del rango de
         *         fechas
         */
        @GetExchange(value = "/api/v1/analytics/customer-activity-logs/by-date-range", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<CustomerActivityLogDto>> getCustomerActivityLogsByDateRange(
                        @RequestParam Long customerId,
                        @RequestParam Date startDate,
                        @RequestParam Date endDate);

        /**
         * Cuenta los registros de actividad de clientes por tipo de actividad.
         *
         * @param customerId   el ID del cliente
         * @param activityType el tipo de actividad
         * @return el conteo de registros encontrados
         */
        @GetExchange(value = "/api/v1/analytics/customer-activity-logs/count", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<Long> countCustomerActivityLogs(
                        @RequestParam Long customerId,
                        @RequestParam String activityType);

        /**
         * Elimina los registros antiguos de actividad de clientes anteriores a la fecha
         * especificada.
         *
         * @param beforeDate la fecha límite para la eliminación de registros
         * @return el número de registros eliminados
         */
        @DeleteExchange(value = "/api/v1/analytics/customer-activity-logs/delete-old", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<Long> deleteOldCustomerActivityLogs(@RequestParam Date beforeDate);

        // ========== GESTIÓN DE MÉTRICAS DE USO DE FUNCIONALIDADES (FEATURE USAGE
        // METRIC MANAGEMENT) ==========

        /**
         * Verifica la disponibilidad del servicio de métricas de uso de
         * funcionalidades.
         *
         * @return la respuesta con el estado de disponibilidad del servicio
         */
        @GetExchange(value = "/api/v1/analytics/feature-usage-metrics"
                        + SimappeConstant.PATH_INDEX, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<ServiceResponseAvailable> featureUsageMetricIndex();

        /**
         * Recupera todas las métricas de uso de funcionalidades.
         *
         * @return la lista de métricas de uso de funcionalidades
         */
        @GetExchange(value = "/api/v1/analytics/feature-usage-metrics"
                        + SimappeConstant.PATH_READ, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<FeatureUsageMetricDto>> readFeatureUsageMetrics();

        /**
         * Recupera las métricas de uso de funcionalidades por ID de cliente.
         *
         * @param customerId el ID del cliente
         * @return la lista de métricas de uso de funcionalidades para el cliente
         */
        @GetExchange(value = "/api/v1/analytics/feature-usage-metrics/by-customer", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<FeatureUsageMetricDto>> getFeatureUsageMetricsByCustomerId(
                        @RequestParam Long customerId);

        /**
         * Recupera las métricas de uso de funcionalidades por clave de funcionalidad.
         *
         * @param featureKey la clave de la funcionalidad
         * @return la lista de métricas de uso de funcionalidades para la funcionalidad
         */
        @GetExchange(value = "/api/v1/analytics/feature-usage-metrics/by-feature", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<FeatureUsageMetricDto>> getFeatureUsageMetricsByFeatureKey(
                        @RequestParam String featureKey);

        /**
         * Recupera las métricas de uso de funcionalidades por tipo de periodo.
         *
         * @param periodType el tipo de periodo
         * @return la lista de métricas de uso de funcionalidades para el periodo
         */
        @GetExchange(value = "/api/v1/analytics/feature-usage-metrics/by-period", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<FeatureUsageMetricDto>> getFeatureUsageMetricsByPeriodType(
                        @RequestParam PeriodType periodType);

        /**
         * Recupera los detalles de una métrica de uso de funcionalidad por su ID.
         *
         * @param id el ID de la métrica de uso de funcionalidad
         * @return los detalles de la métrica de uso de funcionalidad
         */
        @GetExchange(value = "/api/v1/analytics/feature-usage-metrics"
                        + SimappeConstant.PATH_GET, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<FeatureUsageMetricDto> getFeatureUsageMetricId(@RequestParam Long id);

        /**
         * Crea una nueva métrica de uso de funcionalidad.
         *
         * @param dto el DTO de la métrica de uso de funcionalidad
         * @return la métrica de uso de funcionalidad creada
         * @throws SimappeException si ocurre un error durante la creación
         */
        @PostExchange(value = "/api/v1/analytics/feature-usage-metrics"
                        + SimappeConstant.PATH_CREATE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<FeatureUsageMetricDto> createFeatureUsageMetric(
                        @Valid @RequestBody FeatureUsageMetricDto dto)
                        throws SimappeException;

        /**
         * Actualiza una métrica de uso de funcionalidad existente.
         *
         * @param dto el DTO de la métrica de uso de funcionalidad con los cambios
         * @param id  el ID de la métrica de uso de funcionalidad a actualizar
         * @return la métrica de uso de funcionalidad actualizada
         */
        @PutExchange(value = "/api/v1/analytics/feature-usage-metrics"
                        + SimappeConstant.PATH_UPDATE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<FeatureUsageMetricDto> updateFeatureUsageMetric(
                        @Valid @RequestBody FeatureUsageMetricDto dto,
                        @RequestParam Long id);

        /**
         * Elimina una métrica de uso de funcionalidad por su ID.
         *
         * @param id el ID de la métrica de uso de funcionalidad a eliminar
         * @return una respuesta vacía indicando el resultado de la operación
         */
        @DeleteExchange(value = "/api/v1/analytics/feature-usage-metrics"
                        + SimappeConstant.PATH_DELETE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<Void> deleteFeatureUsageMetric(@RequestParam Long id);

        /**
         * Recupera una lista paginada de métricas de uso de funcionalidades.
         *
         * @param requestQuery los parámetros de consulta y paginación
         * @return una página de métricas de uso de funcionalidades
         * @throws SimappeException si ocurre un error durante la consulta
         */
        @PostExchange(value = "/api/v1/analytics/feature-usage-metrics"
                        + SimappeConstant.PATH_PAGE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<PageDto<FeatureUsageMetricDto>> getFeatureUsageMetricPage(
                        @RequestBody SimappeRequestQuery requestQuery) throws SimappeException;

        // ========== GESTIÓN DE MÉTRICAS DE SALUD DEL SISTEMA (SYSTEM HEALTH METRIC
        // MANAGEMENT) ==========

        /**
         * Verifica la disponibilidad del servicio de métricas de salud del sistema.
         *
         * @return la respuesta con el estado de disponibilidad del servicio
         */
        @GetExchange(value = "/api/v1/analytics/system-health-metrics"
                        + SimappeConstant.PATH_INDEX, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<ServiceResponseAvailable> systemHealthMetricIndex();

        /**
         * Recupera todas las métricas de salud del sistema.
         *
         * @return la lista de métricas de salud del sistema
         */
        @GetExchange(value = "/api/v1/analytics/system-health-metrics"
                        + SimappeConstant.PATH_READ, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<SystemHealthMetricDto>> readSystemHealthMetrics();

        /**
         * Recupera los detalles de una métrica de salud del sistema por su ID.
         *
         * @param id el ID de la métrica de salud del sistema
         * @return los detalles de la métrica de salud del sistema
         */
        @GetExchange(value = "/api/v1/analytics/system-health-metrics"
                        + SimappeConstant.PATH_GET, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<SystemHealthMetricDto> getSystemHealthMetricId(@RequestParam Long id);

        /**
         * Crea una nueva métrica de salud del sistema.
         *
         * @param dto el DTO de la métrica de salud del sistema
         * @return la métrica de salud del sistema creada
         * @throws SimappeException si ocurre un error durante la creación
         */
        @PostExchange(value = "/api/v1/analytics/system-health-metrics"
                        + SimappeConstant.PATH_CREATE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<SystemHealthMetricDto> createSystemHealthMetric(
                        @Valid @RequestBody SystemHealthMetricDto dto)
                        throws SimappeException;

        /**
         * Actualiza una métrica de salud del sistema existente.
         *
         * @param dto el DTO de la métrica de salud del sistema con los cambios
         * @param id  el ID de la métrica de salud del sistema a actualizar
         * @return la métrica de salud del sistema actualizada
         */
        @PutExchange(value = "/api/v1/analytics/system-health-metrics"
                        + SimappeConstant.PATH_UPDATE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<SystemHealthMetricDto> updateSystemHealthMetric(
                        @Valid @RequestBody SystemHealthMetricDto dto,
                        @RequestParam Long id);

        /**
         * Elimina una métrica de salud del sistema por su ID.
         *
         * @param id el ID de la métrica de salud del sistema a eliminar
         * @return una respuesta vacía indicando el resultado de la operación
         */
        @DeleteExchange(value = "/api/v1/analytics/system-health-metrics"
                        + SimappeConstant.PATH_DELETE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<Void> deleteSystemHealthMetric(@RequestParam Long id);

        /**
         * Recupera una lista paginada de métricas de salud del sistema.
         *
         * @param requestQuery los parámetros de consulta y paginación
         * @return una página de métricas de salud del sistema
         * @throws SimappeException si ocurre un error durante la consulta
         */
        @PostExchange(value = "/api/v1/analytics/system-health-metrics"
                        + SimappeConstant.PATH_PAGE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<PageDto<SystemHealthMetricDto>> getSystemHealthMetricPage(
                        @RequestBody SimappeRequestQuery requestQuery) throws SimappeException;

        // ========== GESTIÓN DE REGISTROS DE FACTURACIÓN POR USO (USAGE BILLING RECORD
        // MANAGEMENT) ==========

        /**
         * Verifica la disponibilidad del servicio de registros de facturación por uso.
         *
         * @return la respuesta con el estado de disponibilidad del servicio
         */
        @GetExchange(value = "/api/v1/analytics/usage-billing-records"
                        + SimappeConstant.PATH_INDEX, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<ServiceResponseAvailable> usageBillingRecordIndex();

        /**
         * Recupera todos los registros de facturación por uso.
         *
         * @return la lista de registros de facturación por uso
         */
        @GetExchange(value = "/api/v1/analytics/usage-billing-records"
                        + SimappeConstant.PATH_READ, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<UsageBillingRecordDto>> readUsageBillingRecords();

        /**
         * Recupera los registros de facturación por uso por ID de cliente.
         *
         * @param customerId el ID del cliente
         * @return la lista de registros de facturación por uso para el cliente
         */
        @GetExchange(value = "/api/v1/analytics/usage-billing-records/by-customer", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<UsageBillingRecordDto>> getUsageBillingRecordsByCustomerId(
                        @RequestParam Long customerId);

        /**
         * Recupera los registros de facturación por uso por estado.
         *
         * @param status el estado de facturación
         * @return la lista de registros de facturación por uso con el estado
         *         especificado
         */
        @GetExchange(value = "/api/v1/analytics/usage-billing-records/by-status", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<UsageBillingRecordDto>> getUsageBillingRecordsByStatus(
                        @RequestParam BillingStatus status);

        /**
         * Recupera los registros de facturación por uso por rango de fechas.
         *
         * @param startDate la fecha de inicio
         * @param endDate   la fecha de fin
         * @return la lista de registros de facturación por uso dentro del rango de
         *         fechas
         */
        @GetExchange(value = "/api/v1/analytics/usage-billing-records/by-date-range", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<UsageBillingRecordDto>> getUsageBillingRecordsByDateRange(
                        @RequestParam Date startDate,
                        @RequestParam Date endDate);

        /**
         * Recupera los detalles de un registro de facturación por uso por su ID.
         *
         * @param id el ID del registro de facturación por uso
         * @return los detalles del registro de facturación por uso
         */
        @GetExchange(value = "/api/v1/analytics/usage-billing-records"
                        + SimappeConstant.PATH_GET, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<UsageBillingRecordDto> getUsageBillingRecordId(@RequestParam Long id);

        /**
         * Crea un nuevo registro de facturación por uso.
         *
         * @param dto el DTO del registro de facturación por uso
         * @return el registro de facturación por uso creado
         * @throws SimappeException si ocurre un error durante la creación
         */
        @PostExchange(value = "/api/v1/analytics/usage-billing-records"
                        + SimappeConstant.PATH_CREATE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<UsageBillingRecordDto> createUsageBillingRecord(
                        @Valid @RequestBody UsageBillingRecordDto dto)
                        throws SimappeException;

        /**
         * Actualiza un registro de facturación por uso existente.
         *
         * @param dto el DTO del registro de facturación por uso con los cambios
         * @param id  el ID del registro de facturación por uso a actualizar
         * @return el registro de facturación por uso actualizado
         */
        @PutExchange(value = "/api/v1/analytics/usage-billing-records"
                        + SimappeConstant.PATH_UPDATE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<UsageBillingRecordDto> updateUsageBillingRecord(
                        @Valid @RequestBody UsageBillingRecordDto dto,
                        @RequestParam Long id);

        /**
         * Elimina un registro de facturación por uso por su ID.
         *
         * @param id el ID del registro de facturación por uso a eliminar
         * @return una respuesta vacía indicando el resultado de la operación
         */
        @DeleteExchange(value = "/api/v1/analytics/usage-billing-records"
                        + SimappeConstant.PATH_DELETE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<Void> deleteUsageBillingRecord(@RequestParam Long id);

        /**
         * Recupera una lista paginada de registros de facturación por uso.
         *
         * @param requestQuery los parámetros de consulta y paginación
         * @return una página de registros de facturación por uso
         * @throws SimappeException si ocurre un error durante la consulta
         */
        @PostExchange(value = "/api/v1/analytics/usage-billing-records"
                        + SimappeConstant.PATH_PAGE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<PageDto<UsageBillingRecordDto>> getUsageBillingRecordPage(
                        @RequestBody SimappeRequestQuery requestQuery) throws SimappeException;

        // ========== GESTIÓN DE USO DE API POR CLIENTE (CLIENT API USAGE MANAGEMENT)
        // ==========
        // WebClient Pattern

        /**
         * Verifica la disponibilidad del servicio de uso de API por cliente.
         *
         * @return la respuesta con el estado de disponibilidad del servicio
         */
        @GetExchange(value = "/api/v1/api-versioning/client-usage"
                        + SimappeConstant.PATH_INDEX, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<ServiceResponseAvailable> clientUsageIndex();

        /**
         * Recupera todos los registros de uso de API por cliente.
         *
         * @return la lista de registros de uso de API por cliente
         */
        @GetExchange(value = "/api/v1/api-versioning/client-usage"
                        + SimappeConstant.PATH_READ, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<ClientApiUsageDto>> readClientUsages();

        /**
         * Recupera los registros de uso de API por cliente para un cliente específico.
         *
         * @param customerId el ID del cliente
         * @return la lista de registros de uso de API por cliente
         */
        @GetExchange(value = "/api/v1/api-versioning/client-usage/read-by-customer", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<ClientApiUsageDto>> readClientUsagesByCustomer(@RequestParam Long customerId);

        /**
         * Recupera los detalles de un registro de uso de API por cliente por su ID.
         *
         * @param id el ID del registro de uso de API por cliente
         * @return los detalles del registro de uso de API por cliente
         */
        @GetExchange(value = "/api/v1/api-versioning/client-usage"
                        + SimappeConstant.PATH_GET, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<ClientApiUsageDto> getClientUsage(@RequestParam Long id);

        /**
         * Crea un nuevo registro de uso de API por cliente.
         *
         * @param dto el DTO del registro de uso de API por cliente
         * @return el registro de uso de API por cliente creado
         * @throws SimappeException si ocurre un error durante la creación
         */
        @PostExchange(value = "/api/v1/api-versioning/client-usage"
                        + SimappeConstant.PATH_CREATE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<ClientApiUsageDto> createClientUsage(@Valid @RequestBody ClientApiUsageDto dto)
                        throws SimappeException;

        /**
         * Actualiza un registro de uso de API por cliente existente.
         *
         * @param dto el DTO del registro de uso de API por cliente con los cambios
         * @param id  el ID del registro de uso de API por cliente a actualizar
         * @return el registro de uso de API por cliente actualizado
         */
        @PutExchange(value = "/api/v1/api-versioning/client-usage"
                        + SimappeConstant.PATH_UPDATE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<ClientApiUsageDto> updateClientUsage(@Valid @RequestBody ClientApiUsageDto dto,
                        @RequestParam Long id);

        /**
         * Elimina un registro de uso de API por cliente por su ID.
         *
         * @param id el ID del registro de uso de API por cliente a eliminar
         * @return una respuesta vacía indicando el resultado de la operación
         */
        @DeleteExchange(value = "/api/v1/api-versioning/client-usage"
                        + SimappeConstant.PATH_DELETE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<Void> deleteClientUsage(@RequestParam Long id);

        /**
         * Recupera una lista paginada de registros de uso de API por cliente.
         *
         * @param requestQuery los parámetros de consulta y paginación
         * @return una página de registros de uso de API por cliente
         * @throws SimappeException si ocurre un error durante la consulta
         */
        @PostExchange(value = "/api/v1/api-versioning/client-usage"
                        + SimappeConstant.PATH_PAGE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<PageDto<ClientApiUsageDto>> getClientUsagePage(
                        @RequestBody SimappeRequestQuery requestQuery)
                        throws SimappeException;

        /**
         * Rastrea el uso de una API para un cliente específico.
         *
         * @param customerId    el ID del cliente
         * @param apiEndpointId el ID del endpoint de la API
         * @return el registro de uso de API por cliente resultante
         */
        @PostExchange(value = "/api/v1/api-versioning/client-usage/track", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<ClientApiUsageDto> trackUsage(@RequestParam Long customerId,
                        @RequestParam Long apiEndpointId);

        /**
         * Recupera el uso de API agrupado por versión para un cliente.
         *
         * @param customerId el ID del cliente
         * @return un mapa con el uso agrupado por versión
         */
        @GetExchange(value = "/api/v1/api-versioning/client-usage/by-version", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<Map<String, List<ClientApiUsageDto>>> getUsageByVersion(@RequestParam Long customerId);

        /**
         * Encuentra clientes que están utilizando APIs obsoletas.
         *
         * @return un mapa con las APIs obsoletas y la lista de IDs de clientes que las
         *         usan
         */
        @GetExchange(value = "/api/v1/api-versioning/client-usage/deprecated-usage", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<Map<String, List<Long>>> findClientsUsingDeprecatedApis();

        /**
         * Marca un endpoint como no utilizado por un cliente específico.
         *
         * @param customerId    el ID del cliente
         * @param apiEndpointId el ID del endpoint de la API
         * @return el registro de uso de API por cliente modificado
         * @throws SimappeException si ocurre un error durante la operación
         */
        @PostExchange(value = "/api/v1/api-versioning/client-usage/mark-unused", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<ClientApiUsageDto> markAsNotUsed(@RequestParam Long customerId,
                        @RequestParam Long apiEndpointId) throws SimappeException;

        // ========== GESTIÓN DE BALOTO (BALOTO MANAGEMENT) ==========
        // WebClient Pattern

        /**
         * Verifica la disponibilidad del servicio de Baloto.
         *
         * @return la respuesta con el estado de disponibilidad del servicio
         */
        @GetExchange(value = "/api/v1/baloto" + SimappeConstant.PATH_INDEX, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<ServiceResponseAvailable> balotoIndex();

        /**
         * Recupera todos los sorteos de Baloto.
         *
         * @return la lista de sorteos de Baloto
         */
        @GetExchange(value = "/api/v1/baloto" + SimappeConstant.PATH_READ, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<BalotoDTO>> readBalotoDraws();

        /**
         * Recupera los sorteos de Baloto validados.
         *
         * @return la lista de sorteos validados
         */
        @GetExchange(value = "/api/v1/baloto/read-validated", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<BalotoDTO>> readValidatedDraws();

        /**
         * Recupera los sorteos de Baloto por rango de fechas.
         *
         * @param start la fecha de inicio
         * @param end   la fecha de fin
         * @return la lista de sorteos encontrados
         */
        @GetExchange(value = "/api/v1/baloto/read-by-date-range", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<BalotoDTO>> readDrawsByDateRange(@RequestParam LocalDateTime start,
                        @RequestParam LocalDateTime end);

        /**
         * Predice el próximo sorteo de Baloto basándose en estadísticas.
         *
         * @return el sorteo de Baloto predicho
         */
        @GetExchange(value = "/api/v1/baloto/predict-next-draw", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<BalotoDTO> predictNextDraw();

        /**
         * Recupera los detalles de un sorteo de Baloto por su ID.
         *
         * @param id el ID del sorteo
         * @return los detalles del sorteo
         */
        @GetExchange(value = "/api/v1/baloto" + SimappeConstant.PATH_GET, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<BalotoDTO> getBalotoDraw(@RequestParam String id);

        /**
         * Recupera un sorteo de Baloto por su número de sorteo y tipo.
         *
         * @param drawNumber el número de sorteo
         * @param type       el tipo de Baloto
         * @return los detalles del sorteo encontrado
         */
        @GetExchange(value = "/api/v1/baloto/get-by-draw-number", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<BalotoDTO> getBalotoByNumber(@RequestParam Long drawNumber,
                        @RequestParam BalotoType type);

        /**
         * Crea un nuevo registro de sorteo de Baloto.
         *
         * @param dto el DTO del sorteo de Baloto
         * @return el sorteo de Baloto creado
         */
        @PostExchange(value = "/api/v1/baloto" + SimappeConstant.PATH_CREATE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<BalotoDTO> createBaloto(@Valid @RequestBody BalotoDTO dto);

        /**
         * Actualiza un sorteo de Baloto existente por su ID.
         *
         * @param dto el DTO del sorteo con los cambios
         * @param id  el ID del sorteo a actualizar
         * @return el sorteo de Baloto actualizado
         * @throws SimappeException si ocurre un error durante la actualización
         */
        @PutExchange(value = "/api/v1/baloto" + SimappeConstant.PATH_UPDATE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<BalotoDTO> updateBaloto(@Valid @RequestBody BalotoDTO dto, @RequestParam String id)
                        throws SimappeException;

        /**
         * Elimina un sorteo de Baloto por su ID.
         *
         * @param id el ID del sorteo a eliminar
         * @return una respuesta vacía indicando el resultado de la operación
         * @throws SimappeException si ocurre un error durante la eliminación
         */
        @DeleteExchange(value = "/api/v1/baloto"
                        + SimappeConstant.PATH_DELETE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<Void> deleteBaloto(@RequestParam String id) throws SimappeException;

        /**
         * Recupera una lista paginada de sorteos de Baloto.
         *
         * @param requestQuery los parámetros de consulta y paginación
         * @return una página de sorteos de Baloto
         * @throws SimappeException si ocurre un error durante la consulta
         */
        @PostExchange(value = "/api/v1/baloto" + SimappeConstant.PATH_PAGE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<PageDto<BalotoDTO>> getBalotoPage(@RequestBody SimappeRequestQuery requestQuery)
                        throws SimappeException;

        /**
         * Carga masivamente sorteos de Baloto.
         *
         * @param draws la lista de sorteos a cargar
         * @return una respuesta vacía indicando el resultado de la operación
         */
        @PostExchange(value = "/api/v1/baloto/bulk-load", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<Void> bulkLoadDraws(@Valid @RequestBody List<BalotoDTO> draws);

        /**
         * Obtiene un análisis estadístico para la predicción del próximo sorteo.
         *
         * @return el resultado del análisis de predicción
         */
        @GetExchange(value = "/api/v1/baloto/prediction-analysis", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<PredictionResult> getPredictionAnalysis();

        /**
         * Analiza la frecuencia de aparición de los números en los sorteos.
         *
         * @return un mapa con la frecuencia de cada número
         */
        @GetExchange(value = "/api/v1/baloto/number-frequency", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<Map<Integer, Integer>> analyzeNumberFrequency();

        /**
         * Obtiene la lista de los números más frecuentes aparecidos.
         *
         * @param topCount la cantidad de números a retornar (por defecto 6)
         * @return la lista de los números más frecuentes
         */
        @GetExchange(value = "/api/v1/baloto/most-frequent-numbers", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<Integer>> getMostFrequentNumbers(@RequestParam(defaultValue = "6") int topCount);

        /**
         * Calcula las estadísticas de distribución de los números.
         *
         * @return el objeto con las estadísticas de distribución calculadas
         */
        @GetExchange(value = "/api/v1/baloto/number-distribution", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<Object> calculateNumberDistribution();

        /**
         * Genera una predicción detallada basada en múltiples algoritmos.
         *
         * @return el resultado detallado de la predicción generada
         */
        @GetExchange(value = "/api/v1/baloto/detailed-prediction", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<PredictionResult> generateDetailedPrediction();

        // ========== GESTIÓN DE FACTURAS (INVOICE MANAGEMENT) ==========
        // WebClient Pattern

        /**
         * Verifica la disponibilidad del servicio de facturas.
         *
         * @return la respuesta con el estado de disponibilidad del servicio
         */
        @GetExchange(value = "/api/v1/invoice" + SimappeConstant.PATH_INDEX, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<ServiceResponseAvailable> invoiceIndex();

        /**
         * Recupera todas las facturas.
         *
         * @return la lista de facturas
         */
        @GetExchange(value = "/api/v1/invoice" + SimappeConstant.PATH_READ, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<InvoiceDto>> readInvoices();

        /**
         * Recupera las facturas de un cliente específico.
         *
         * @param customerId el ID del cliente
         * @return la lista de facturas para el cliente
         */
        @GetExchange(value = "/api/v1/invoice/read-by-customer", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<InvoiceDto>> readInvoicesByCustomer(@RequestParam Long customerId);

        /**
         * Recupera las facturas por su estado.
         *
         * @param status el estado de la factura
         * @return la lista de facturas con el estado especificado
         */
        @GetExchange(value = "/api/v1/invoice/read-by-status", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<InvoiceDto>> readInvoicesByStatus(@RequestParam String status);

        /**
         * Encuentra facturas vencidas.
         *
         * @return la lista de facturas vencidas
         */
        @GetExchange(value = "/api/v1/invoice/find-overdue", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<InvoiceDto>> findOverdueInvoices();

        /**
         * Recupera los detalles de una factura por su ID.
         *
         * @param id el ID de la factura
         * @return los detalles de la factura
         */
        @GetExchange(value = "/api/v1/invoice" + SimappeConstant.PATH_GET, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<InvoiceDto> getInvoice(@RequestParam Long id);

        /**
         * Genera una factura a partir de una suscripción.
         *
         * @param subscriptionId el ID de la suscripción
         * @return la factura generada
         * @throws SimappeException si ocurre un error durante la generación
         */
        @PostExchange(value = "/api/v1/invoice/generate-from-subscription", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<InvoiceDto> generateInvoiceFromSubscription(@RequestParam Long subscriptionId)
                        throws SimappeException;

        /**
         * Marca una factura como pagada.
         *
         * @param invoiceId el ID de la factura
         * @param paymentId el ID del pago (opcional)
         * @return la factura actualizada
         * @throws SimappeException si ocurre un error durante la operación
         */
        @PutExchange(value = "/api/v1/invoice/mark-as-paid", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<InvoiceDto> markInvoiceAsPaid(@RequestParam Long invoiceId,
                        @RequestParam(required = false) Long paymentId) throws SimappeException;

        /**
         * Crea una nueva factura.
         *
         * @param dto el DTO de la factura
         * @return la factura creada
         * @throws SimappeException si ocurre un error durante la creación
         */
        @PostExchange(value = "/api/v1/invoice"
                        + SimappeConstant.PATH_CREATE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<InvoiceDto> createInvoice(@Valid @RequestBody InvoiceDto dto) throws SimappeException;

        /**
         * Actualiza una factura existente.
         *
         * @param dto el DTO de la factura con los cambios
         * @param id  el ID de la factura a actualizar
         * @return la factura actualizada
         */
        @PutExchange(value = "/api/v1/invoice" + SimappeConstant.PATH_UPDATE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<InvoiceDto> updateInvoice(@Valid @RequestBody InvoiceDto dto, @RequestParam Long id);

        /**
         * Elimina una factura por su ID.
         *
         * @param id el ID de la factura a eliminar
         * @return una respuesta vacía indicando el resultado de la operación
         */
        @DeleteExchange(value = "/api/v1/invoice"
                        + SimappeConstant.PATH_DELETE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<Void> deleteInvoice(@RequestParam Long id);

        /**
         * Recupera una lista paginada de facturas.
         *
         * @param requestQuery los parámetros de consulta y paginación
         * @return una página de facturas
         * @throws SimappeException si ocurre un error durante la consulta
         */
        @PostExchange(value = "/api/v1/invoice" + SimappeConstant.PATH_PAGE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<PageDto<InvoiceDto>> getInvoicePage(@RequestBody SimappeRequestQuery requestQuery)
                        throws SimappeException;

        // ========== GESTIÓN DE ÍTEMS DE FACTURA (INVOICE ITEM MANAGEMENT) ==========
        // WebClient Pattern

        /**
         * Verifica la disponibilidad del servicio de ítems de factura.
         *
         * @return la respuesta con el estado de disponibilidad del servicio
         */
        @GetExchange(value = "/api/v1/invoice-item"
                        + SimappeConstant.PATH_INDEX, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<ServiceResponseAvailable> invoiceItemIndex();

        /**
         * Recupera todos los ítems de factura.
         *
         * @return la lista de ítems de factura
         */
        @GetExchange(value = "/api/v1/invoice-item"
                        + SimappeConstant.PATH_READ, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<InvoiceItemDto>> readInvoiceItems();

        /**
         * Recupera los ítems de una factura específica.
         *
         * @param invoiceId el ID de la factura
         * @return la lista de ítems para la factura
         */
        @GetExchange(value = "/api/v1/invoice-item/read-by-invoice", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<InvoiceItemDto>> readInvoiceItemsByInvoice(@RequestParam Long invoiceId);

        /**
         * Recupera los detalles de un ítem de factura por su ID.
         *
         * @param id el ID del ítem de factura
         * @return los detalles del ítem de factura
         */
        @GetExchange(value = "/api/v1/invoice-item"
                        + SimappeConstant.PATH_GET, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<InvoiceItemDto> getInvoiceItem(@RequestParam Long id);

        /**
         * Crea un nuevo ítem de factura.
         *
         * @param dto el DTO del ítem de factura
         * @return el ítem de factura creado
         * @throws SimappeException si ocurre un error durante la creación
         */
        @PostExchange(value = "/api/v1/invoice-item"
                        + SimappeConstant.PATH_CREATE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<InvoiceItemDto> createInvoiceItem(@Valid @RequestBody InvoiceItemDto dto)
                        throws SimappeException;

        /**
         * Actualiza un ítem de factura existente.
         *
         * @param dto el DTO del ítem de factura con los cambios
         * @param id  el ID del ítem de factura a actualizar
         * @return el ítem de factura actualizado
         */
        @PutExchange(value = "/api/v1/invoice-item"
                        + SimappeConstant.PATH_UPDATE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<InvoiceItemDto> updateInvoiceItem(@Valid @RequestBody InvoiceItemDto dto,
                        @RequestParam Long id);

        /**
         * Elimina un ítem de factura por su ID.
         *
         * @param id el ID del ítem de factura a eliminar
         * @return una respuesta vacía indicando el resultado de la operación
         */
        @DeleteExchange(value = "/api/v1/invoice-item"
                        + SimappeConstant.PATH_DELETE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<Void> deleteInvoiceItem(@RequestParam Long id);

        /**
         * Recupera una lista paginada de ítems de factura.
         *
         * @param requestQuery los parámetros de consulta y paginación
         * @return una página de ítems de factura
         * @throws SimappeException si ocurre un error durante la consulta
         */
        @PostExchange(value = "/api/v1/invoice-item"
                        + SimappeConstant.PATH_PAGE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<PageDto<InvoiceItemDto>> getInvoiceItemPage(@RequestBody SimappeRequestQuery requestQuery)
                        throws SimappeException;

        // ========== GESTIÓN DE PAGOS (PAYMENT MANAGEMENT) ==========
        // WebClient Pattern

        /**
         * Verifica la disponibilidad del servicio de pagos.
         *
         * @return la respuesta con el estado de disponibilidad del servicio
         */
        @GetExchange(value = "/api/v1/payment" + SimappeConstant.PATH_INDEX, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<ServiceResponseAvailable> paymentIndex();

        /**
         * Recupera todos los registros de pagos.
         *
         * @return la lista de pagos
         */
        @GetExchange(value = "/api/v1/payment" + SimappeConstant.PATH_READ, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<PaymentDto>> readPayments();

        /**
         * Recupera los pagos de un cliente específico.
         *
         * @param customerId el ID del cliente
         * @return la lista de pagos para el cliente
         */
        @GetExchange(value = "/api/v1/payment/read-by-customer", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<PaymentDto>> readPaymentsByCustomer(@RequestParam Long customerId);

        /**
         * Recupera los pagos asociados a una factura específica.
         *
         * @param invoiceId el ID de la factura
         * @return la lista de pagos para la factura
         */
        @GetExchange(value = "/api/v1/payment/read-by-invoice", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<PaymentDto>> readPaymentsByInvoice(@RequestParam Long invoiceId);

        /**
         * Recupera los pagos exitosos de un cliente específico.
         *
         * @param customerId el ID del cliente
         * @return la lista de pagos exitosos para el cliente
         */
        @GetExchange(value = "/api/v1/payment/read-successful-by-customer", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<PaymentDto>> readSuccessfulPaymentsByCustomer(@RequestParam Long customerId);

        /**
         * Recupera los detalles de un pago por su ID.
         *
         * @param id el ID del pago
         * @return los detalles del pago
         */
        @GetExchange(value = "/api/v1/payment" + SimappeConstant.PATH_GET, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<PaymentDto> getPayment(@RequestParam Long id);

        /**
         * Procesa un pago para una factura específica.
         *
         * @param invoiceId       el ID de la factura
         * @param paymentMethodId el ID del método de pago
         * @param amount          el monto a pagar
         * @return el pago procesado
         * @throws SimappeException si ocurre un error durante el procesamiento
         */
        @PostExchange(value = "/api/v1/payment/process-payment", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<PaymentDto> processPayment(@RequestParam Long invoiceId,
                        @RequestParam Long paymentMethodId,
                        @RequestParam BigDecimal amount) throws SimappeException;

        /**
         * Registra un pago realizado manualmente para una factura.
         *
         * @param invoiceId       el ID de la factura
         * @param amount          el monto pagado
         * @param paymentMethodId el ID del método de pago (opcional)
         * @param reference       la referencia del pago (opcional)
         * @return el pago registrado
         * @throws SimappeException si ocurre un error durante el registro
         */
        @PostExchange(value = "/api/v1/payment/register-payment", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<PaymentDto> registerPayment(@RequestParam Long invoiceId, @RequestParam BigDecimal amount,
                        @RequestParam(required = false) Long paymentMethodId,
                        @RequestParam(required = false) String reference)
                        throws SimappeException;

        /**
         * Realiza un reembolso de un pago específico.
         *
         * @param paymentId el ID del pago a reembolsar
         * @param amount    el monto a reembolsar
         * @param reason    el motivo del reembolso (opcional)
         * @return el pago de reembolso creado
         * @throws SimappeException si ocurre un error durante el reembolso
         */
        @PostExchange(value = "/api/v1/payment/refund", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<PaymentDto> refundPayment(@RequestParam Long paymentId, @RequestParam BigDecimal amount,
                        @RequestParam(required = false) String reason) throws SimappeException;

        /**
         * Crea un nuevo registro de pago directamente.
         *
         * @param dto el DTO del pago
         * @return el pago creado
         * @throws SimappeException si ocurre un error durante la creación
         */
        @PostExchange(value = "/api/v1/payment"
                        + SimappeConstant.PATH_CREATE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<PaymentDto> createPayment(@Valid @RequestBody PaymentDto dto) throws SimappeException;

        /**
         * Actualiza un registro de pago existente.
         *
         * @param dto el DTO del pago con los cambios
         * @param id  el ID del pago a actualizar
         * @return el pago actualizado
         */
        @PutExchange(value = "/api/v1/payment" + SimappeConstant.PATH_UPDATE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<PaymentDto> updatePayment(@Valid @RequestBody PaymentDto dto, @RequestParam Long id);

        /**
         * Elimina un registro de pago por su ID.
         *
         * @param id el ID del pago a eliminar
         * @return una respuesta vacía indicando el resultado de la operación
         */
        @DeleteExchange(value = "/api/v1/payment"
                        + SimappeConstant.PATH_DELETE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<Void> deletePayment(@RequestParam Long id);

        /**
         * Recupera una lista paginada de pagos.
         *
         * @param requestQuery los parámetros de consulta y paginación
         * @return una página de pagos
         * @throws SimappeException si ocurre un error durante la consulta
         */
        @PostExchange(value = "/api/v1/payment" + SimappeConstant.PATH_PAGE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<PageDto<PaymentDto>> getPaymentPage(@RequestBody SimappeRequestQuery requestQuery)
                        throws SimappeException;

        // ========== GESTIÓN DE MÉTODOS DE PAGO (PAYMENT METHOD MANAGEMENT) ==========
        // WebClient Pattern

        /**
         * Verifica la disponibilidad del servicio de métodos de pago.
         *
         * @return la respuesta con el estado de disponibilidad del servicio
         */
        @GetExchange(value = "/api/v1/payment-method"
                        + SimappeConstant.PATH_INDEX, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<ServiceResponseAvailable> paymentMethodIndex();

        /**
         * Recupera todos los métodos de pago.
         *
         * @return la lista de métodos de pago
         */
        @GetExchange(value = "/api/v1/payment-method"
                        + SimappeConstant.PATH_READ, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<PaymentMethodDto>> readPaymentMethods();

        /**
         * Recupera los métodos de pago de un cliente específico.
         *
         * @param customerId el ID del cliente
         * @return la lista de métodos de pago para el cliente
         */
        @GetExchange(value = "/api/v1/payment-method/read-by-customer", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<PaymentMethodDto>> readPaymentMethodsByCustomer(@RequestParam Long customerId);

        /**
         * Recupera los métodos de pago activos de un cliente específico.
         *
         * @param customerId el ID del cliente
         * @return la lista de métodos de pago activos para el cliente
         */
        @GetExchange(value = "/api/v1/payment-method/read-active-by-customer", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<PaymentMethodDto>> readActivePaymentMethodsByCustomer(@RequestParam Long customerId);

        /**
         * Recupera el método de pago por defecto de un cliente específico.
         *
         * @param customerId el ID del cliente
         * @return los detalles del método de pago por defecto
         */
        @GetExchange(value = "/api/v1/payment-method/get-default-by-customer", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<PaymentMethodDto> getDefaultPaymentMethodByCustomer(@RequestParam Long customerId);

        /**
         * Recupera los detalles de un método de pago por su ID.
         *
         * @param id el ID del método de pago
         * @return los detalles del método de pago
         */
        @GetExchange(value = "/api/v1/payment-method"
                        + SimappeConstant.PATH_GET, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<PaymentMethodDto> getPaymentMethod(@RequestParam Long id);

        /**
         * Establece un método de pago como el predeterminado.
         *
         * @param paymentMethodId el ID del método de pago
         * @return el método de pago actualizado
         * @throws SimappeException si ocurre un error durante la operación
         */
        @PutExchange(value = "/api/v1/payment-method/set-as-default", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<PaymentMethodDto> setPaymentMethodAsDefault(@RequestParam Long paymentMethodId)
                        throws SimappeException;

        /**
         * Crea un nuevo método de pago.
         *
         * @param dto el DTO del método de pago
         * @return el método de pago creado
         * @throws SimappeException si ocurre un error durante la creación
         */
        @PostExchange(value = "/api/v1/payment-method"
                        + SimappeConstant.PATH_CREATE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<PaymentMethodDto> createPaymentMethod(@Valid @RequestBody PaymentMethodDto dto)
                        throws SimappeException;

        /**
         * Actualiza un método de pago existente.
         *
         * @param dto el DTO del método de pago con los cambios
         * @param id  el ID del método de pago a actualizar
         * @return el método de pago actualizado
         */
        @PutExchange(value = "/api/v1/payment-method"
                        + SimappeConstant.PATH_UPDATE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<PaymentMethodDto> updatePaymentMethod(@Valid @RequestBody PaymentMethodDto dto,
                        @RequestParam Long id);

        /**
         * Elimina un método de pago por su ID.
         *
         * @param id el ID del método de pago a eliminar
         * @return una respuesta vacía indicando el resultado de la operación
         */
        @DeleteExchange(value = "/api/v1/payment-method"
                        + SimappeConstant.PATH_DELETE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<Void> deletePaymentMethod(@RequestParam Long id);

        /**
         * Recupera una lista paginada de métodos de pago.
         *
         * @param requestQuery los parámetros de consulta y paginación
         * @return una página de métodos de pago
         * @throws SimappeException si ocurre un error durante la consulta
         */
        @PostExchange(value = "/api/v1/payment-method"
                        + SimappeConstant.PATH_PAGE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<PageDto<PaymentMethodDto>> getPaymentMethodPage(
                        @RequestBody SimappeRequestQuery requestQuery)
                        throws SimappeException;

        // ========== GESTIÓN DE WEBHOOKS DE STRIPE (STRIPE WEBHOOK MANAGEMENT)
        // ==========
        // WebClient Pattern

        /**
         * Verifica la disponibilidad del servicio de Stripe.
         *
         * @return la respuesta con el estado de disponibilidad del servicio
         */
        @GetExchange(value = "/api/v1/stripe" + SimappeConstant.PATH_INDEX, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<ServiceResponseAvailable> stripeIndex();

        /**
         * Procesa las notificaciones de webhooks recibidas de Stripe.
         *
         * @param payload   el cuerpo del webhook de Stripe
         * @param signature la firma de Stripe para validación (opcional)
         * @return un mapa con el resultado del procesamiento
         */
        @PostExchange(value = "/api/v1/stripe/webhook", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<Map<String, Object>> handleStripeWebhook(@RequestBody String payload,
                        @RequestHeader(value = "Stripe-Signature", required = false) String signature);

        /**
         * Crea un Payment Intent de prueba en Stripe.
         *
         * @param amount          el monto del intento de pago
         * @param currency        la moneda del pago
         * @param customerId      el ID del cliente de Stripe (opcional)
         * @param paymentMethodId el ID del método de pago (opcional)
         * @return los detalles del Payment Intent creado
         */
        @PostExchange(value = "/api/v1/stripe/test/payment-intent", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<StripePaymentIntentDto> createTestPaymentIntent(@RequestParam Long amount,
                        @RequestParam CurrencyCode currency, @RequestParam(required = false) String customerId,
                        @RequestParam(required = false) String paymentMethodId);

        /**
         * Confirma un Payment Intent de prueba en Stripe.
         *
         * @param paymentIntentId el ID del Payment Intent a confirmar
         * @return los detalles del Payment Intent confirmado
         */
        @PostExchange(value = "/api/v1/stripe/test/confirm-payment-intent", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<StripePaymentIntentDto> confirmTestPaymentIntent(@RequestParam String paymentIntentId);

        /**
         * Crea un cliente de prueba en Stripe.
         *
         * @param email el correo electrónico del cliente
         * @param name  el nombre del cliente
         * @return los detalles del cliente de Stripe creado
         */
        @PostExchange(value = "/api/v1/stripe/test/customer", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<StripeCustomerDto> createTestCustomer(@RequestParam String email,
                        @RequestParam String name);

        /**
         * Asocia un método de pago a un cliente de prueba en Stripe.
         *
         * @param paymentMethodId el ID del método de pago
         * @param customerId      el ID del cliente de Stripe
         * @return los detalles del método de pago asociado
         */
        @PostExchange(value = "/api/v1/stripe/test/attach-payment-method", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<StripePaymentMethodDto> attachTestPaymentMethod(@RequestParam String paymentMethodId,
                        @RequestParam String customerId);

        /**
         * Crea un reembolso de prueba en Stripe.
         *
         * @param paymentIntentId el ID del Payment Intent a reembolsar
         * @param amount          el monto a reembolsar
         * @param reason          el motivo del reembolso (opcional)
         * @return los detalles del reembolso creado
         */
        @PostExchange(value = "/api/v1/stripe/test/refund", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<StripeRefundDto> createTestRefund(@RequestParam String paymentIntentId,
                        @RequestParam Long amount, @RequestParam(required = false) String reason);

        // ========== GESTIÓN DE RECURSOS DE MARCA (BRAND ASSET MANAGEMENT) ==========
        // WebClient Pattern

        /**
         * Verifica la disponibilidad del servicio de recursos de marca.
         *
         * @return la respuesta con el estado de disponibilidad del servicio
         */
        @GetExchange(value = "/api/v1/brand-assets"
                        + SimappeConstant.PATH_INDEX, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<ServiceResponseAvailable> brandAssetIndex();

        /**
         * Recupera todos los recursos de marca.
         *
         * @return la lista de recursos de marca
         */
        @GetExchange(value = "/api/v1/brand-assets"
                        + SimappeConstant.PATH_READ, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<BrandAssetDto>> readBrandAssets();

        /**
         * Recupera los detalles de un recurso de marca por su ID.
         *
         * @param id el ID del recurso de marca
         * @return los detalles del recurso de marca
         */
        @GetExchange(value = "/api/v1/brand-assets"
                        + SimappeConstant.PATH_GET, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<BrandAssetDto> getBrandAsset(@RequestParam Long id);

        /**
         * Crea un nuevo recurso de marca.
         *
         * @param dto el DTO del recurso de marca
         * @return el recurso de marca creado
         * @throws SimappeException si ocurre un error durante la creación
         */
        @PostExchange(value = "/api/v1/brand-assets"
                        + SimappeConstant.PATH_CREATE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<BrandAssetDto> createBrandAsset(@Valid @RequestBody BrandAssetDto dto)
                        throws SimappeException;

        /**
         * Actualiza un recurso de marca existente.
         *
         * @param dto el DTO del recurso de marca con los cambios
         * @param id  el ID del recurso de marca a actualizar
         * @return el recurso de marca actualizado
         * @throws SimappeException si ocurre un error durante la actualización
         */
        @PutExchange(value = "/api/v1/brand-assets"
                        + SimappeConstant.PATH_UPDATE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<BrandAssetDto> updateBrandAsset(@Valid @RequestBody BrandAssetDto dto,
                        @RequestParam Long id) throws SimappeException;

        /**
         * Elimina un recurso de marca por su ID.
         *
         * @param id el ID del recurso de marca a eliminar
         * @return una respuesta vacía indicando el resultado de la operación
         * @throws SimappeException si ocurre un error durante la eliminación
         */
        @DeleteExchange(value = "/api/v1/brand-assets"
                        + SimappeConstant.PATH_DELETE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<Void> deleteBrandAsset(@RequestParam Long id) throws SimappeException;

        /**
         * Recupera una lista paginada de recursos de marca.
         *
         * @param requestQuery los parámetros de consulta y paginación
         * @return una página de recursos de marca
         * @throws SimappeException si ocurre un error durante la consulta
         */
        @PostExchange(value = "/api/v1/brand-assets"
                        + SimappeConstant.PATH_PAGE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<PageDto<BrandAssetDto>> getBrandAssetPage(@RequestBody SimappeRequestQuery requestQuery)
                        throws SimappeException;

        // ========== GESTIÓN DE CONFIGURACIÓN DE MARCA (BRAND CONFIGURATION MANAGEMENT)
        // ==========
        // WebClient Pattern

        /**
         * Verifica la disponibilidad del servicio de configuración de marca.
         *
         * @return la respuesta con el estado de disponibilidad del servicio
         */
        @GetExchange(value = "/api/v1/brand-configuration"
                        + SimappeConstant.PATH_INDEX, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<ServiceResponseAvailable> brandConfigIndex();

        /**
         * Recupera todas las configuraciones de marca.
         *
         * @return la lista de configuraciones de marca
         */
        @GetExchange(value = "/api/v1/brand-configuration"
                        + SimappeConstant.PATH_READ, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<BrandConfigurationDto>> readBrandConfigurations();

        /**
         * Recupera las configuraciones de marca para un cliente específico.
         *
         * @param customerId el ID del cliente
         * @return la lista de configuraciones de marca para el cliente
         */
        @GetExchange(value = "/api/v1/brand-configuration/by-customer", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<BrandConfigurationDto>> readBrandConfigurationsByCustomer(
                        @RequestParam Long customerId);

        /**
         * Recupera la configuración de marca activa para un cliente específico.
         *
         * @param customerId el ID del cliente
         * @return los detalles de la configuración de marca activa
         */
        @GetExchange(value = "/api/v1/brand-configuration/active-by-customer", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<BrandConfigurationDto> getActiveBrandConfigurationByCustomer(
                        @RequestParam Long customerId);

        /**
         * Recupera los detalles de una configuración de marca por su ID.
         *
         * @param id el ID de la configuración de marca
         * @return los detalles de la configuración de marca
         */
        @GetExchange(value = "/api/v1/brand-configuration"
                        + SimappeConstant.PATH_GET, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<BrandConfigurationDto> getBrandConfiguration(@RequestParam Long id);

        /**
         * Crea una nueva configuración de marca.
         *
         * @param dto el DTO de la configuración de marca
         * @return la configuración de marca creada
         * @throws SimappeException si ocurre un error durante la creación
         */
        @PostExchange(value = "/api/v1/brand-configuration"
                        + SimappeConstant.PATH_CREATE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<BrandConfigurationDto> createBrandConfiguration(
                        @Valid @RequestBody BrandConfigurationDto dto)
                        throws SimappeException;

        /**
         * Actualiza una configuración de marca existente.
         *
         * @param dto el DTO de la configuración de marca con los cambios
         * @param id  el ID de la configuración de marca a actualizar
         * @return la configuración de marca actualizada
         * @throws SimappeException si ocurre un error durante la actualización
         */
        @PutExchange(value = "/api/v1/brand-configuration"
                        + SimappeConstant.PATH_UPDATE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<BrandConfigurationDto> updateBrandConfiguration(
                        @Valid @RequestBody BrandConfigurationDto dto,
                        @RequestParam Long id) throws SimappeException;

        /**
         * Elimina una configuración de marca por su ID.
         *
         * @param id el ID de la configuración de marca a eliminar
         * @return una respuesta vacía indicando el resultado de la operación
         * @throws SimappeException si ocurre un error durante la eliminación
         */
        @DeleteExchange(value = "/api/v1/brand-configuration"
                        + SimappeConstant.PATH_DELETE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<Void> deleteBrandConfiguration(@RequestParam Long id) throws SimappeException;

        /**
         * Recupera una lista paginada de configuraciones de marca.
         *
         * @param requestQuery los parámetros de consulta y paginación
         * @return una página de configuraciones de marca
         * @throws SimappeException si ocurre un error durante la consulta
         */
        @PostExchange(value = "/api/v1/brand-configuration"
                        + SimappeConstant.PATH_PAGE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<PageDto<BrandConfigurationDto>> getBrandConfigurationPage(
                        @RequestBody SimappeRequestQuery requestQuery) throws SimappeException;

        // ========== GESTIÓN DE TEMAS DE MARCA (BRAND THEME MANAGEMENT) ==========
        // WebClient Pattern

        /**
         * Verifica la disponibilidad del servicio de temas de marca.
         *
         * @return la respuesta con el estado de disponibilidad del servicio
         */
        @GetExchange(value = "/api/v1/brand-theme"
                        + SimappeConstant.PATH_INDEX, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<ServiceResponseAvailable> brandThemeIndex();

        /**
         * Recupera todos los temas de marca.
         *
         * @return la lista de temas de marca
         */
        @GetExchange(value = "/api/v1/brand-theme"
                        + SimappeConstant.PATH_READ, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<BrandThemeDto>> readBrandThemes();

        /**
         * Recupera los temas de marca públicos y activos.
         *
         * @return la lista de temas de marca públicos activos
         */
        @GetExchange(value = "/api/v1/brand-theme/public-active", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<BrandThemeDto>> readPublicActiveBrandThemes();

        /**
         * Recupera los detalles de un tema de marca por su ID.
         *
         * @param id el ID del tema de marca
         * @return los detalles del tema de marca
         */
        @GetExchange(value = "/api/v1/brand-theme"
                        + SimappeConstant.PATH_GET, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<BrandThemeDto> getBrandTheme(@RequestParam Long id);

        /**
         * Crea un nuevo tema de marca.
         *
         * @param dto el DTO del tema de marca
         * @return el tema de marca creado
         * @throws SimappeException si ocurre un error durante la creación
         */
        @PostExchange(value = "/api/v1/brand-theme"
                        + SimappeConstant.PATH_CREATE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<BrandThemeDto> createBrandTheme(@Valid @RequestBody BrandThemeDto dto)
                        throws SimappeException;

        /**
         * Actualiza un tema de marca existente.
         *
         * @param dto el DTO del tema de marca con los cambios
         * @param id  el ID del tema de marca a actualizar
         * @return el tema de marca actualizado
         * @throws SimappeException si ocurre un error durante la actualización
         */
        @PutExchange(value = "/api/v1/brand-theme"
                        + SimappeConstant.PATH_UPDATE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<BrandThemeDto> updateBrandTheme(@Valid @RequestBody BrandThemeDto dto,
                        @RequestParam Long id) throws SimappeException;

        /**
         * Elimina un tema de marca por su ID.
         *
         * @param id el ID del tema de marca a eliminar
         * @return una respuesta vacía indicando el resultado de la operación
         * @throws SimappeException si ocurre un error durante la eliminación
         */
        @DeleteExchange(value = "/api/v1/brand-theme"
                        + SimappeConstant.PATH_DELETE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<Void> deleteBrandTheme(@RequestParam Long id) throws SimappeException;

        /**
         * Recupera una lista paginada de temas de marca.
         *
         * @param requestQuery los parámetros de consulta y paginación
         * @return una página de temas de marca
         * @throws SimappeException si ocurre un error durante la consulta
         */
        @PostExchange(value = "/api/v1/brand-theme"
                        + SimappeConstant.PATH_PAGE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<PageDto<BrandThemeDto>> getBrandThemePage(@RequestBody SimappeRequestQuery requestQuery)
                        throws SimappeException;

        // ========== GESTIÓN DE EMPRESAS (COMPANY MANAGEMENT) ==========

        /**
         * Recupera los detalles de una empresa por su ID.
         *
         * @param id el ID de la empresa
         * @return los detalles de la empresa
         */
        @GetExchange(value = "/api/v1/company" + SimappeConstant.PATH_GET, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<CompanyDto> getCompanyId(@RequestParam Long id);

        // ========== PARÁMETROS DE EMPRESA (COMPANY PARAMETERS) ==========

        /**
         * Recupera todos los parámetros de empresa activos.
         *
         * @return la lista de parámetros de empresa activos
         */
        @GetExchange(value = "/api/v1/company-parameters/read-active", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<CompanyParametersDto>> readActiveCompanyParameters();

        /**
         * Recupera los parámetros de empresa definidos como plantillas.
         *
         * @return la lista de parámetros de empresa plantilla
         */
        @GetExchange(value = "/api/v1/company-parameters/read-template-parameters", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<CompanyParametersDto>> readTemplateCompanyParameters();

        /**
         * Recupera los parámetros específicos de una empresa.
         *
         * @param companyId el ID de la empresa
         * @return la lista de parámetros para la empresa
         */
        @GetExchange(value = "/api/v1/company-parameters/read-company", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<CompanyParametersDto>> readCompanyParametersByCompany(@RequestParam Long companyId);

        /**
         * Recupera los detalles de un parámetro de empresa por su ID.
         *
         * @param id el ID del parámetro de empresa
         * @return los detalles del parámetro de empresa
         */
        @GetExchange(value = "/api/v1/company-parameters"
                        + SimappeConstant.PATH_GET, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<CompanyParametersDto> getCompanyParametersId(@RequestParam Long id);

        /**
         * Inicializa los parámetros para una empresa a partir de una plantilla.
         *
         * @param dto el DTO con la información para la inicialización
         * @return el parámetro de empresa inicializado
         * @throws SimappeException si ocurre un error durante la inicialización
         */
        @PostExchange(value = "/api/v1/company-parameters/init-parameters", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<CompanyParametersDto> initializeCompanyParameters(
                        @Valid @RequestBody CompanyParametersDto dto) throws SimappeException;

        // ========== GESTIÓN DE CONSECUTIVOS (CONSECUTIVE MANAGEMENT) ==========

        /**
         * Recupera los consecutivos filtrados por su estado.
         *
         * @param status el estado de los consecutivos
         * @return la lista de consecutivos con el estado especificado
         */
        @GetExchange(value = "/api/v1/consecutive/read-status", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<ConsecutiveDto>> readConsecutivesByStatus(@RequestParam String status);

        /**
         * Recupera los consecutivos asociados a un rol específico.
         *
         * @param role el rol para filtrar los consecutivos
         * @return la lista de consecutivos para el rol
         */
        @GetExchange(value = "/api/v1/consecutive/read-by-role", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<ConsecutiveDto>> readConsecutivesByRole(@RequestParam String role);

        /**
         * Recupera los consecutivos dentro de un ámbito (scope) específico.
         *
         * @param scope el ámbito para filtrar los consecutivos
         * @return la lista de consecutivos para el ámbito
         */
        @GetExchange(value = "/api/v1/consecutive/read-by-scope", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<ConsecutiveDto>> readConsecutivesByScope(@RequestParam String scope);

        /**
         * Recupera los detalles de un consecutivo por su ID.
         *
         * @param id el ID del consecutivo
         * @return los detalles del consecutivo
         */
        @GetExchange(value = "/api/v1/consecutive"
                        + SimappeConstant.PATH_GET, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<ConsecutiveDto> getConsecutiveId(@RequestParam Long id);

        /**
         * Genera y recupera el siguiente valor para un consecutivo específico.
         *
         * @param consecutiveId el ID del consecutivo
         * @return el siguiente valor generado como una cadena
         */
        @GetExchange(value = "/api/v1/consecutive/next-value", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<String> getNextConsecutiveValue(@RequestParam Long consecutiveId);

        /**
         * Agrega una asociación de rol a un consecutivo.
         *
         * @param consecutiveId el ID del consecutivo
         * @param role          el rol a agregar
         * @return el consecutivo actualizado
         */
        @PutExchange(value = "/api/v1/consecutive/add-role", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<ConsecutiveDto> addRoleToConsecutive(@RequestParam Long consecutiveId,
                        @RequestParam String role);

        /**
         * Elimina una asociación de rol de un consecutivo.
         *
         * @param consecutiveId el ID del consecutivo
         * @param role          el rol a eliminar
         * @return el consecutivo actualizado
         */
        @DeleteExchange(value = "/api/v1/consecutive/remove-role", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<ConsecutiveDto> removeRoleFromConsecutive(@RequestParam Long consecutiveId,
                        @RequestParam String role);

        // ========== GESTIÓN DE PAÍSES (COUNTRY MANAGEMENT) ==========

        /**
         * Recupera los detalles de un país por su ID.
         *
         * @param id el ID del país
         * @return los detalles del país
         */
        @GetExchange(value = "/api/v1/country" + SimappeConstant.PATH_GET, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<CountryDto> getCountryId(@RequestParam Long id);

        // ========== GESTIÓN DE CLIENTES (CUSTOMER MANAGEMENT) ==========

        /**
         * Recupera los clientes filtrados por su tipo.
         *
         * @param customerType el tipo de cliente para filtrar
         * @return la lista de clientes del tipo especificado
         */
        @GetExchange(value = "/api/v1/customer/read-customer-type", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<CustomerDto>> readCustomersByType(@RequestParam String customerType);

        // ========== PARÁMETROS DE CLIENTE (CUSTOMER PARAMETERS) ==========

        /**
         * Recupera todos los parámetros de cliente activos.
         *
         * @return la lista de parámetros de cliente activos
         */
        @GetExchange(value = "/api/v1/customer-parameters/read-active", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<CustomerParametersDto>> readActiveCustomerParameters();

        /**
         * Recupera los parámetros de cliente definidos como plantillas.
         *
         * @return la lista de parámetros de cliente plantilla
         */
        @GetExchange(value = "/api/v1/customer-parameters/read-template-parameters", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<CustomerParametersDto>> readTemplateCustomerParameters();

        /**
         * Recupera los parámetros específicos de un cliente.
         *
         * @param customerId el ID del cliente
         * @return la lista de parámetros para el cliente
         */
        @GetExchange(value = "/api/v1/customer-parameters/read-customer", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<CustomerParametersDto>> readCustomerParametersByCustomer(
                        @RequestParam Long customerId);

        /**
         * Recupera los detalles de un parámetro de cliente por su ID.
         *
         * @param id el ID del parámetro de cliente
         * @return los detalles del parámetro de cliente
         */
        @GetExchange(value = "/api/v1/customer-parameters"
                        + SimappeConstant.PATH_GET, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<CustomerParametersDto> getCustomerParametersId(@RequestParam Long id);

        /**
         * Inicializa los parámetros para un cliente a partir de una plantilla.
         *
         * @param dto el DTO con la información para la inicialización
         * @return el parámetro de cliente inicializado
         * @throws SimappeException si ocurre un error durante la inicialización
         */
        @PostExchange(value = "/api/v1/customer-parameters/init-parameters", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<CustomerParametersDto> initializeCustomerParameters(
                        @Valid @RequestBody CustomerParametersDto dto) throws SimappeException;

        // ========== GESTIÓN DE POLÍTICAS DE RETENCIÓN DE DATOS (DATA RETENTION POLICY
        // MANAGEMENT) ==========

        /**
         * Verifica la disponibilidad del servicio de políticas de retención de datos.
         *
         * @return la respuesta con el estado de disponibilidad del servicio
         */
        @GetExchange(value = "/api/v1/data-retention-policy"
                        + SimappeConstant.PATH_INDEX, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<ServiceResponseAvailable> dataRetentionPolicyIndex();

        /**
         * Recupera todas las políticas de retención de datos.
         *
         * @return la lista de políticas de retención de datos
         */
        @GetExchange(value = "/api/v1/data-retention-policy"
                        + SimappeConstant.PATH_READ, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<DataRetentionPolicyDto>> readDataRetentionPolicies();

        /**
         * Recupera los detalles de una política de retención de datos por su ID.
         *
         * @param id el ID de la política de retención de datos
         * @return los detalles de la política de retención de datos
         */
        @GetExchange(value = "/api/v1/data-retention-policy"
                        + SimappeConstant.PATH_GET, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<DataRetentionPolicyDto> getDataRetentionPolicyId(@RequestParam Long id);

        /**
         * Crea una nueva política de retención de datos.
         *
         * @param dto el DTO de la política de retención de datos
         * @return la política de retención de datos creada
         * @throws SimappeException si ocurre un error durante la creación
         */
        @PostExchange(value = "/api/v1/data-retention-policy"
                        + SimappeConstant.PATH_CREATE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<DataRetentionPolicyDto> createDataRetentionPolicy(
                        @Valid @RequestBody DataRetentionPolicyDto dto) throws SimappeException;

        /**
         * Actualiza una política de retención de datos existente.
         *
         * @param dto el DTO de la política de retención de datos con los cambios
         * @param id  el ID de la política de retención de datos a actualizar
         * @return la política de retención de datos actualizada
         */
        @PutExchange(value = "/api/v1/data-retention-policy"
                        + SimappeConstant.PATH_UPDATE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<DataRetentionPolicyDto> updateDataRetentionPolicy(
                        @Valid @RequestBody DataRetentionPolicyDto dto, @RequestParam Long id);

        /**
         * Elimina una política de retención de datos por su ID.
         *
         * @param id el ID de la política de retención de datos a eliminar
         * @return una respuesta vacía indicando el resultado de la operación
         */
        @DeleteExchange(value = "/api/v1/data-retention-policy"
                        + SimappeConstant.PATH_DELETE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<Void> deleteDataRetentionPolicy(@RequestParam Long id);

        /**
         * Recupera una lista paginada de políticas de retención de datos.
         *
         * @param requestQuery los parámetros de consulta y paginación
         * @return una página de políticas de retención de datos
         * @throws SimappeException si ocurre un error durante la consulta
         */
        @PostExchange(value = "/api/v1/data-retention-policy"
                        + SimappeConstant.PATH_PAGE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<PageDto<DataRetentionPolicyDto>> getDataRetentionPolicyPage(
                        @RequestBody SimappeRequestQuery requestQuery) throws SimappeException;

        // ========== GESTIÓN DE CONFIGURACIÓN DE BASE DE DATOS V2 (DATABASE CONFIG V2
        // MANAGEMENT) ==========

        /**
         * Verifica la disponibilidad del servicio de configuración de base de datos V2.
         *
         * @return la respuesta con el estado de disponibilidad del servicio
         */
        @GetExchange(value = "/api/v1/database-config-v2"
                        + SimappeConstant.PATH_INDEX, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<ServiceResponseAvailable> databaseConfigV2Index();

        /**
         * Recupera todas las configuraciones de base de datos V2.
         *
         * @return la lista de configuraciones de base de datos V2
         */
        @GetExchange(value = "/api/v1/database-config-v2"
                        + SimappeConstant.PATH_READ, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<DatabaseConfigV2Dto>> readDatabaseConfigsV2();

        /**
         * Recupera los detalles de una configuración de base de datos V2 por su ID.
         *
         * @param id el ID de la configuración
         * @return los detalles de la configuración
         */
        @GetExchange(value = "/api/v1/database-config-v2"
                        + SimappeConstant.PATH_GET, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<DatabaseConfigV2Dto> getDatabaseConfigV2Id(@RequestParam Long id);

        /**
         * Crea una nueva configuración de base de datos V2.
         *
         * @param dto el DTO de la configuración
         * @return la configuración creada
         * @throws SimappeException si ocurre un error durante la creación
         */
        @PostExchange(value = "/api/v1/database-config-v2"
                        + SimappeConstant.PATH_CREATE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<DatabaseConfigV2Dto> createDatabaseConfigV2(@Valid @RequestBody DatabaseConfigV2Dto dto)
                        throws SimappeException;

        /**
         * Actualiza una configuración de base de datos V2 existente.
         *
         * @param dto el DTO de la configuración con los cambios
         * @param id  el ID de la configuración a actualizar
         * @return la configuración actualizada
         */
        @PutExchange(value = "/api/v1/database-config-v2"
                        + SimappeConstant.PATH_UPDATE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<DatabaseConfigV2Dto> updateDatabaseConfigV2(@Valid @RequestBody DatabaseConfigV2Dto dto,
                        @RequestParam Long id);

        /**
         * Elimina una configuración de base de datos V2 por su ID.
         *
         * @param id el ID de la configuración a eliminar
         * @return una respuesta vacía indicando el resultado de la operación
         */
        @DeleteExchange(value = "/api/v1/database-config-v2"
                        + SimappeConstant.PATH_DELETE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<Void> deleteDatabaseConfigV2(@RequestParam Long id);

        /**
         * Recupera una lista paginada de configuraciones de base de datos V2.
         *
         * @param requestQuery los parámetros de consulta y paginación
         * @return una página de configuraciones de base de datos V2
         * @throws SimappeException si ocurre un error durante la consulta
         */
        @PostExchange(value = "/api/v1/database-config-v2"
                        + SimappeConstant.PATH_PAGE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<PageDto<DatabaseConfigV2Dto>> getDatabaseConfigV2Page(
                        @RequestBody SimappeRequestQuery requestQuery) throws SimappeException;

        // ========== GESTIÓN DE CONFIGURACIÓN DE ENTORNO (ENVIRONMENT CONFIG
        // MANAGEMENT) ==========

        /**
         * Verifica la disponibilidad del servicio de configuración de entorno.
         *
         * @return la respuesta con el estado de disponibilidad del servicio
         */
        @GetExchange(value = "/api/v1/environment-config"
                        + SimappeConstant.PATH_INDEX, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<ServiceResponseAvailable> environmentConfigIndex();

        /**
         * Recupera todas las configuraciones de entorno.
         *
         * @return la lista de configuraciones de entorno
         */
        @GetExchange(value = "/api/v1/environment-config"
                        + SimappeConstant.PATH_READ, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<EnvironmentConfigDto>> readEnvironmentConfigs();

        /**
         * Recupera los detalles de una configuración de entorno por su ID.
         *
         * @param id el ID de la configuración
         * @return los detalles de la configuración
         */
        @GetExchange(value = "/api/v1/environment-config"
                        + SimappeConstant.PATH_GET, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<EnvironmentConfigDto> getEnvironmentConfigId(@RequestParam Long id);

        /**
         * Crea una nueva configuración de entorno.
         *
         * @param dto el DTO de la configuración
         * @return la configuración creada
         * @throws SimappeException si ocurre un error durante la creación
         */
        @PostExchange(value = "/api/v1/environment-config"
                        + SimappeConstant.PATH_CREATE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<EnvironmentConfigDto> createEnvironmentConfig(
                        @Valid @RequestBody EnvironmentConfigDto dto)
                        throws SimappeException;

        /**
         * Actualiza una configuración de entorno existente.
         *
         * @param dto el DTO de la configuración con los cambios
         * @param id  el ID de la configuración a actualizar
         * @return la configuración actualizada
         */
        @PutExchange(value = "/api/v1/environment-config"
                        + SimappeConstant.PATH_UPDATE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<EnvironmentConfigDto> updateEnvironmentConfig(
                        @Valid @RequestBody EnvironmentConfigDto dto,
                        @RequestParam Long id);

        /**
         * Elimina una configuración de entorno por su ID.
         *
         * @param id el ID de la configuración a eliminar
         * @return una respuesta vacía indicando el resultado de la operación
         */
        @DeleteExchange(value = "/api/v1/environment-config"
                        + SimappeConstant.PATH_DELETE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<Void> deleteEnvironmentConfig(@RequestParam Long id);

        /**
         * Recupera una lista paginada de configuraciones de entorno.
         *
         * @param requestQuery los parámetros de consulta y paginación
         * @return una página de configuraciones de entorno
         * @throws SimappeException si ocurre un error durante la consulta
         */
        @PostExchange(value = "/api/v1/environment-config"
                        + SimappeConstant.PATH_PAGE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<PageDto<EnvironmentConfigDto>> getEnvironmentConfigPage(
                        @RequestBody SimappeRequestQuery requestQuery) throws SimappeException;

        // ========== GESTIÓN DE BANDERAS DE CARACTERÍSTICAS (FEATURE FLAG MANAGEMENT)
        // ==========

        /**
         * Verifica la disponibilidad del servicio de banderas de características.
         *
         * @return la respuesta con el estado de disponibilidad del servicio
         */
        @GetExchange(value = "/api/v1/feature-flag"
                        + SimappeConstant.PATH_INDEX, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<ServiceResponseAvailable> featureFlagIndex();

        /**
         * Recupera todas las banderas de características.
         *
         * @return la lista de banderas de características
         */
        @GetExchange(value = "/api/v1/feature-flag"
                        + SimappeConstant.PATH_READ, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<FeatureFlagDto>> readFeatureFlags();

        /**
         * Recupera todas las banderas de características activas.
         *
         * @return la lista de banderas de características activas
         */
        @GetExchange(value = "/api/v1/feature-flag/read-active", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<FeatureFlagDto>> readActiveFeatureFlags();

        /**
         * Recupera los detalles de una bandera de característica por su ID.
         *
         * @param id el ID de la bandera
         * @return los detalles de la bandera
         */
        @GetExchange(value = "/api/v1/feature-flag"
                        + SimappeConstant.PATH_GET, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<FeatureFlagDto> getFeatureFlagId(@RequestParam Long id);

        /**
         * Recupera los detalles de una bandera de característica por su código.
         *
         * @param code el código único de la bandera
         * @return los detalles de la bandera
         */
        @GetExchange(value = "/api/v1/feature-flag/get-code", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<FeatureFlagDto> getFeatureFlagByCode(@RequestParam String code);

        /**
         * Crea una nueva bandera de característica.
         *
         * @param dto el DTO de la bandera
         * @return la bandera creada
         * @throws SimappeException si ocurre un error durante la creación
         */
        @PostExchange(value = "/api/v1/feature-flag"
                        + SimappeConstant.PATH_CREATE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<FeatureFlagDto> createFeatureFlag(@Valid @RequestBody FeatureFlagDto dto)
                        throws SimappeException;

        /**
         * Actualiza una bandera de característica existente.
         *
         * @param dto el DTO de la bandera con los cambios
         * @param id  el ID de la bandera a actualizar
         * @return la bandera actualizada
         */
        @PutExchange(value = "/api/v1/feature-flag"
                        + SimappeConstant.PATH_UPDATE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<FeatureFlagDto> updateFeatureFlag(@Valid @RequestBody FeatureFlagDto dto,
                        @RequestParam Long id);

        /**
         * Elimina una bandera de característica por su ID.
         *
         * @param id el ID de la bandera a eliminar
         * @return una respuesta vacía indicando el resultado de la operación
         */
        @DeleteExchange(value = "/api/v1/feature-flag"
                        + SimappeConstant.PATH_DELETE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<Void> deleteFeatureFlag(@RequestParam Long id);

        /**
         * Recupera una lista paginada de banderas de características.
         *
         * @param requestQuery los parámetros de consulta y paginación
         * @return una página de banderas de características
         * @throws SimappeException si ocurre un error durante la consulta
         */
        @PostExchange(value = "/api/v1/feature-flag"
                        + SimappeConstant.PATH_PAGE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<PageDto<FeatureFlagDto>> getFeatureFlagPage(@RequestBody SimappeRequestQuery requestQuery)
                        throws SimappeException;

        // ========== GESTIÓN DE SOBREESCRITURAS DE BANDERAS DE CARACTERÍSTICAS (FEATURE
        // FLAG OVERRIDE MANAGEMENT) ==========

        /**
         * Verifica la disponibilidad del servicio de sobreescrituras de banderas de
         * características.
         *
         * @return la respuesta con el estado de disponibilidad del servicio
         */
        @GetExchange(value = "/api/v1/feature-flag-override"
                        + SimappeConstant.PATH_INDEX, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<ServiceResponseAvailable> featureFlagOverrideIndex();

        /**
         * Recupera todas las sobreescrituras de banderas de características.
         *
         * @return la lista de sobreescrituras de banderas
         */
        @GetExchange(value = "/api/v1/feature-flag-override"
                        + SimappeConstant.PATH_READ, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<FeatureFlagOverrideDto>> readFeatureFlagOverrides();

        /**
         * Recupera todas las sobreescrituras de banderas de características activas.
         *
         * @return la lista de sobreescrituras de banderas activas
         */
        @GetExchange(value = "/api/v1/feature-flag-override/read-active", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<FeatureFlagOverrideDto>> readActiveFeatureFlagOverrides();

        /**
         * Recupera los detalles de una sobreescritura de bandera de característica por
         * su ID.
         *
         * @param id el ID de la sobreescritura
         * @return los detalles de la sobreescritura
         */
        @GetExchange(value = "/api/v1/feature-flag-override"
                        + SimappeConstant.PATH_GET, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<FeatureFlagOverrideDto> getFeatureFlagOverrideId(@RequestParam Long id);

        /**
         * Crea una nueva sobreescritura de bandera de característica.
         *
         * @param dto el DTO de la sobreescritura
         * @return la sobreescritura creada
         * @throws SimappeException si ocurre un error durante la creación
         */
        @PostExchange(value = "/api/v1/feature-flag-override"
                        + SimappeConstant.PATH_CREATE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<FeatureFlagOverrideDto> createFeatureFlagOverride(
                        @Valid @RequestBody FeatureFlagOverrideDto dto) throws SimappeException;

        /**
         * Actualiza una sobreescritura de bandera de característica existente.
         *
         * @param dto el DTO de la sobreescritura con los cambios
         * @param id  el ID de la sobreescritura a actualizar
         * @return la sobreescritura actualizada
         */
        @PutExchange(value = "/api/v1/feature-flag-override"
                        + SimappeConstant.PATH_UPDATE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<FeatureFlagOverrideDto> updateFeatureFlagOverride(
                        @Valid @RequestBody FeatureFlagOverrideDto dto, @RequestParam Long id);

        /**
         * Elimina una sobreescritura de bandera de característica por su ID.
         *
         * @param id el ID de la sobreescritura a eliminar
         * @return una respuesta vacía indicando el resultado de la operación
         */
        @DeleteExchange(value = "/api/v1/feature-flag-override"
                        + SimappeConstant.PATH_DELETE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<Void> deleteFeatureFlagOverride(@RequestParam Long id);

        /**
         * Recupera una lista paginada de sobreescrituras de banderas de
         * características.
         *
         * @param requestQuery los parámetros de consulta y paginación
         * @return una página de sobreescrituras de banderas
         * @throws SimappeException si ocurre un error durante la consulta
         */
        @PostExchange(value = "/api/v1/feature-flag-override"
                        + SimappeConstant.PATH_PAGE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<PageDto<FeatureFlagOverrideDto>> getFeatureFlagOverridePage(
                        @RequestBody SimappeRequestQuery requestQuery) throws SimappeException;

        // ========== GESTIÓN DE REGISTROS DE CONSENTIMIENTO GDPR (GDPR CONSENT RECORD
        // MANAGEMENT) ==========

        /**
         * Verifica la disponibilidad del servicio de registros de consentimiento GDPR.
         *
         * @return la respuesta con el estado de disponibilidad del servicio
         */
        @GetExchange(value = "/api/v1/gdpr-consent-record"
                        + SimappeConstant.PATH_INDEX, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<ServiceResponseAvailable> gdprConsentRecordIndex();

        /**
         * Recupera todos los registros de consentimiento GDPR.
         *
         * @return la lista de registros de consentimiento
         */
        @GetExchange(value = "/api/v1/gdpr-consent-record"
                        + SimappeConstant.PATH_READ, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<GdprConsentRecordDto>> readGdprConsentRecords();

        /**
         * Recupera los detalles de un registro de consentimiento GDPR por su ID.
         *
         * @param id el ID del registro de consentimiento
         * @return los detalles del registro de consentimiento
         */
        @GetExchange(value = "/api/v1/gdpr-consent-record"
                        + SimappeConstant.PATH_GET, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<GdprConsentRecordDto> getGdprConsentRecordId(@RequestParam Long id);

        /**
         * Crea un nuevo registro de consentimiento GDPR.
         *
         * @param dto el DTO del registro de consentimiento
         * @return el registro de consentimiento creado
         * @throws SimappeException si ocurre un error durante la creación
         */
        @PostExchange(value = "/api/v1/gdpr-consent-record"
                        + SimappeConstant.PATH_CREATE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<GdprConsentRecordDto> createGdprConsentRecord(
                        @Valid @RequestBody GdprConsentRecordDto dto)
                        throws SimappeException;

        /**
         * Actualiza un registro de consentimiento GDPR existente.
         *
         * @param dto el DTO del registro de consentimiento con los cambios
         * @param id  el ID del registro a actualizar
         * @return el registro de consentimiento actualizado
         */
        @PutExchange(value = "/api/v1/gdpr-consent-record"
                        + SimappeConstant.PATH_UPDATE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<GdprConsentRecordDto> updateGdprConsentRecord(
                        @Valid @RequestBody GdprConsentRecordDto dto,
                        @RequestParam Long id);

        /**
         * Elimina un registro de consentimiento GDPR por su ID.
         *
         * @param id el ID del registro a eliminar
         * @return una respuesta vacía indicando el resultado de la operación
         */
        @DeleteExchange(value = "/api/v1/gdpr-consent-record"
                        + SimappeConstant.PATH_DELETE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<Void> deleteGdprConsentRecord(@RequestParam Long id);

        /**
         * Recupera una lista paginada de registros de consentimiento GDPR.
         *
         * @param requestQuery los parámetros de consulta y paginación
         * @return una página de registros de consentimiento
         * @throws SimappeException si ocurre un error durante la consulta
         */
        @PostExchange(value = "/api/v1/gdpr-consent-record"
                        + SimappeConstant.PATH_PAGE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<PageDto<GdprConsentRecordDto>> getGdprConsentRecordPage(
                        @RequestBody SimappeRequestQuery requestQuery) throws SimappeException;

        // ========== GESTIÓN DE BRECHAS DE DATOS GDPR (GDPR DATA BREACH MANAGEMENT)
        // ==========

        /**
         * Verifica la disponibilidad del servicio de brechas de datos GDPR.
         *
         * @return la respuesta con el estado de disponibilidad del servicio
         */
        @GetExchange(value = "/api/v1/gdpr-data-breach"
                        + SimappeConstant.PATH_INDEX, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<ServiceResponseAvailable> gdprDataBreachIndex();

        /**
         * Recupera todas las brechas de datos GDPR registradas.
         *
         * @return la lista de brechas de datos
         */
        @GetExchange(value = "/api/v1/gdpr-data-breach"
                        + SimappeConstant.PATH_READ, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<GdprDataBreachDto>> readGdprDataBreaches();

        /**
         * Recupera los detalles de una brecha de datos GDPR por su ID.
         *
         * @param id el ID de la brecha de datos
         * @return los detalles de la brecha de datos
         */
        @GetExchange(value = "/api/v1/gdpr-data-breach"
                        + SimappeConstant.PATH_GET, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<GdprDataBreachDto> getGdprDataBreachId(@RequestParam Long id);

        /**
         * Crea un nuevo registro de brecha de datos GDPR.
         *
         * @param dto el DTO de la brecha de datos
         * @return la brecha de datos registrada
         * @throws SimappeException si ocurre un error durante la creación
         */
        @PostExchange(value = "/api/v1/gdpr-data-breach"
                        + SimappeConstant.PATH_CREATE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<GdprDataBreachDto> createGdprDataBreach(@Valid @RequestBody GdprDataBreachDto dto)
                        throws SimappeException;

        /**
         * Actualiza un registro de brecha de datos GDPR existente.
         *
         * @param dto el DTO de la brecha de datos con los cambios
         * @param id  el ID de la brecha de datos a actualizar
         * @return la brecha de datos actualizada
         */
        @PutExchange(value = "/api/v1/gdpr-data-breach"
                        + SimappeConstant.PATH_UPDATE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<GdprDataBreachDto> updateGdprDataBreach(@Valid @RequestBody GdprDataBreachDto dto,
                        @RequestParam Long id);

        /**
         * Elimina un registro de brecha de datos GDPR por su ID.
         *
         * @param id el ID de la brecha de datos a eliminar
         * @return una respuesta vacía indicando el resultado de la operación
         */
        @DeleteExchange(value = "/api/v1/gdpr-data-breach"
                        + SimappeConstant.PATH_DELETE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<Void> deleteGdprDataBreach(@RequestParam Long id);

        /**
         * Recupera una lista paginada de brechas de datos GDPR.
         *
         * @param requestQuery los parámetros de consulta y paginación
         * @return una página de brechas de datos
         * @throws SimappeException si ocurre un error durante la consulta
         */
        @PostExchange(value = "/api/v1/gdpr-data-breach"
                        + SimappeConstant.PATH_PAGE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<PageDto<GdprDataBreachDto>> getGdprDataBreachPage(
                        @RequestBody SimappeRequestQuery requestQuery) throws SimappeException;

        // ========== GESTIÓN DE SOLICITUDES DE DATOS GDPR (GDPR DATA REQUEST
        // MANAGEMENT) ==========

        /**
         * Verifica la disponibilidad del servicio de solicitudes de datos GDPR.
         *
         * @return la respuesta con el estado de disponibilidad del servicio
         */
        @GetExchange(value = "/api/v1/gdpr-data-request"
                        + SimappeConstant.PATH_INDEX, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<ServiceResponseAvailable> gdprDataRequestIndex();

        /**
         * Recupera todas las solicitudes de datos GDPR registradas.
         *
         * @return la lista de solicitudes de datos
         */
        @GetExchange(value = "/api/v1/gdpr-data-request"
                        + SimappeConstant.PATH_READ, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<GdprDataRequestDto>> readGdprDataRequests();

        /**
         * Recupera los detalles de una solicitud de datos GDPR por su ID.
         *
         * @param id el ID de la solicitud
         * @return los detalles de la solicitud
         */
        @GetExchange(value = "/api/v1/gdpr-data-request"
                        + SimappeConstant.PATH_GET, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<GdprDataRequestDto> getGdprDataRequestId(@RequestParam Long id);

        /**
         * Crea una nueva solicitud de datos GDPR.
         *
         * @param dto el DTO de la solicitud de datos
         * @return la solicitud de datos registrada
         * @throws SimappeException si ocurre un error durante la creación
         */
        @PostExchange(value = "/api/v1/gdpr-data-request"
                        + SimappeConstant.PATH_CREATE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<GdprDataRequestDto> createGdprDataRequest(@Valid @RequestBody GdprDataRequestDto dto)
                        throws SimappeException;

        /**
         * Actualiza una solicitud de datos GDPR existente.
         *
         * @param dto el DTO de la solicitud con los cambios
         * @param id  el ID de la solicitud a actualizar
         * @return la solicitud actualizada
         */
        @PutExchange(value = "/api/v1/gdpr-data-request"
                        + SimappeConstant.PATH_UPDATE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<GdprDataRequestDto> updateGdprDataRequest(@Valid @RequestBody GdprDataRequestDto dto,
                        @RequestParam Long id);

        /**
         * Elimina una solicitud de datos GDPR por su ID.
         *
         * @param id el ID de la solicitud a eliminar
         * @return una respuesta vacía indicando el resultado de la operación
         */
        @DeleteExchange(value = "/api/v1/gdpr-data-request"
                        + SimappeConstant.PATH_DELETE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<Void> deleteGdprDataRequest(@RequestParam Long id);

        /**
         * Recupera una lista paginada de solicitudes de datos GDPR.
         *
         * @param requestQuery los parámetros de consulta y paginación
         * @return una página de solicitudes de datos
         * @throws SimappeException si ocurre un error durante la consulta
         */
        @PostExchange(value = "/api/v1/gdpr-data-request"
                        + SimappeConstant.PATH_PAGE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<PageDto<GdprDataRequestDto>> getGdprDataRequestPage(
                        @RequestBody SimappeRequestQuery requestQuery) throws SimappeException;

        // ========== GESTIÓN DE ACTIVIDADES DE PROCESAMIENTO GDPR (GDPR PROCESSING
        // ACTIVITY MANAGEMENT) ==========

        /**
         * Verifica la disponibilidad del servicio de actividades de procesamiento GDPR.
         *
         * @return la respuesta con el estado de disponibilidad del servicio
         */
        @GetExchange(value = "/api/v1/gdpr-processing-activity"
                        + SimappeConstant.PATH_INDEX, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<ServiceResponseAvailable> gdprProcessingActivityIndex();

        /**
         * Recupera todas las actividades de procesamiento GDPR registradas.
         *
         * @return la lista de actividades de procesamiento
         */
        @GetExchange(value = "/api/v1/gdpr-processing-activity"
                        + SimappeConstant.PATH_READ, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<GdprProcessingActivityDto>> readGdprProcessingActivities();

        /**
         * Recupera los detalles de una actividad de procesamiento GDPR por su ID.
         *
         * @param id el ID de la actividad
         * @return los detalles de la actividad
         */
        @GetExchange(value = "/api/v1/gdpr-processing-activity"
                        + SimappeConstant.PATH_GET, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<GdprProcessingActivityDto> getGdprProcessingActivityId(@RequestParam Long id);

        /**
         * Crea una nueva actividad de procesamiento GDPR.
         *
         * @param dto el DTO de la actividad de procesamiento
         * @return la actividad de procesamiento registrada
         * @throws SimappeException si ocurre un error durante la creación
         */
        @PostExchange(value = "/api/v1/gdpr-processing-activity"
                        + SimappeConstant.PATH_CREATE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<GdprProcessingActivityDto> createGdprProcessingActivity(
                        @Valid @RequestBody GdprProcessingActivityDto dto) throws SimappeException;

        /**
         * Actualiza una actividad de procesamiento GDPR existente.
         *
         * @param dto el DTO de la actividad con los cambios
         * @param id  el ID de la actividad a actualizar
         * @return la actividad actualizada
         */
        @PutExchange(value = "/api/v1/gdpr-processing-activity"
                        + SimappeConstant.PATH_UPDATE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<GdprProcessingActivityDto> updateGdprProcessingActivity(
                        @Valid @RequestBody GdprProcessingActivityDto dto, @RequestParam Long id);

        /**
         * Elimina una actividad de procesamiento GDPR por su ID.
         *
         * @param id el ID de la actividad a eliminar
         * @return una respuesta vacía indicando el resultado de la operación
         */
        @DeleteExchange(value = "/api/v1/gdpr-processing-activity"
                        + SimappeConstant.PATH_DELETE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<Void> deleteGdprProcessingActivity(@RequestParam Long id);

        /**
         * Recupera una lista paginada de actividades de procesamiento GDPR.
         *
         * @param requestQuery los parámetros de consulta y paginación
         * @return una página de actividades de procesamiento
         * @throws SimappeException si ocurre un error durante la consulta
         */
        @PostExchange(value = "/api/v1/gdpr-processing-activity"
                        + SimappeConstant.PATH_PAGE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<PageDto<GdprProcessingActivityDto>> getGdprProcessingActivityPage(
                        @RequestBody SimappeRequestQuery requestQuery) throws SimappeException;

        // ========== GESTIÓN DE CONFIGURACIÓN DE INTEGRACIÓN (INTEGRATION CONFIG
        // MANAGEMENT) ==========

        /**
         * Verifica la disponibilidad del servicio de configuración de integración.
         *
         * @return la respuesta con el estado de disponibilidad del servicio
         */
        @GetExchange(value = "/api/v1/integration-config"
                        + SimappeConstant.PATH_INDEX, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<ServiceResponseAvailable> integrationConfigIndex();

        /**
         * Recupera todas las configuraciones de integración registradas.
         *
         * @return la lista de configuraciones de integración
         */
        @GetExchange(value = "/api/v1/integration-config"
                        + SimappeConstant.PATH_READ, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<IntegrationConfigDto>> readIntegrationConfigs();

        /**
         * Recupera los detalles de una configuración de integración por su ID.
         *
         * @param id el ID de la configuración
         * @return los detalles de la configuración
         */
        @GetExchange(value = "/api/v1/integration-config"
                        + SimappeConstant.PATH_GET, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<IntegrationConfigDto> getIntegrationConfigId(@RequestParam Long id);

        /**
         * Crea una nueva configuración de integración.
         *
         * @param dto el DTO de la configuración de integración
         * @return la configuración creada
         * @throws SimappeException si ocurre un error durante la creación
         */
        @PostExchange(value = "/api/v1/integration-config"
                        + SimappeConstant.PATH_CREATE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<IntegrationConfigDto> createIntegrationConfig(
                        @Valid @RequestBody IntegrationConfigDto dto)
                        throws SimappeException;

        /**
         * Actualiza una configuración de integración existente.
         *
         * @param dto el DTO de la configuración con los cambios
         * @param id  el ID de la configuración a actualizar
         * @return la configuración actualizada
         */
        @PutExchange(value = "/api/v1/integration-config"
                        + SimappeConstant.PATH_UPDATE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<IntegrationConfigDto> updateIntegrationConfig(
                        @Valid @RequestBody IntegrationConfigDto dto,
                        @RequestParam Long id);

        /**
         * Elimina una configuración de integración por su ID.
         *
         * @param id el ID de la configuración a eliminar
         * @return una respuesta vacía indicando el resultado de la operación
         */
        @DeleteExchange(value = "/api/v1/integration-config"
                        + SimappeConstant.PATH_DELETE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<Void> deleteIntegrationConfig(@RequestParam Long id);

        /**
         * Recupera una lista paginada de configuraciones de integración.
         *
         * @param requestQuery los parámetros de consulta y paginación
         * @return una página de configuraciones de integración
         * @throws SimappeException si ocurre un error durante la consulta
         */
        @PostExchange(value = "/api/v1/integration-config"
                        + SimappeConstant.PATH_PAGE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<PageDto<IntegrationConfigDto>> getIntegrationConfigPage(
                        @RequestBody SimappeRequestQuery requestQuery) throws SimappeException;

        // ========== GESTIÓN DE EJECUCIÓN DE INTEGRACIÓN (INTEGRATION EXECUTION
        // MANAGEMENT) ==========

        /**
         * Verifica la disponibilidad del servicio de ejecución de integración.
         *
         * @return la respuesta con el estado de disponibilidad del servicio
         */
        @GetExchange(value = "/api/v1/integration-execution"
                        + SimappeConstant.PATH_INDEX, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<ServiceResponseAvailable> integrationExecutionIndex();

        /**
         * Recupera todas las ejecuciones de integración registradas.
         *
         * @return la lista de ejecuciones de integración
         */
        @GetExchange(value = "/api/v1/integration-execution"
                        + SimappeConstant.PATH_READ, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<IntegrationExecutionDto>> readIntegrationExecutions();

        /**
         * Recupera los detalles de una ejecución de integración por su ID.
         *
         * @param id el ID de la ejecución
         * @return los detalles de la ejecución
         */
        @GetExchange(value = "/api/v1/integration-execution"
                        + SimappeConstant.PATH_GET, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<IntegrationExecutionDto> getIntegrationExecutionId(@RequestParam Long id);

        /**
         * Crea una nueva ejecución de integración.
         *
         * @param dto el DTO de la ejecución de integración
         * @return la ejecución creada
         * @throws SimappeException si ocurre un error durante la creación
         */
        @PostExchange(value = "/api/v1/integration-execution"
                        + SimappeConstant.PATH_CREATE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<IntegrationExecutionDto> createIntegrationExecution(
                        @Valid @RequestBody IntegrationExecutionDto dto) throws SimappeException;

        /**
         * Actualiza una ejecución de integración existente.
         *
         * @param dto el DTO de la ejecución con los cambios
         * @param id  el ID de la ejecución a actualizar
         * @return la ejecución actualizada
         */
        @PutExchange(value = "/api/v1/integration-execution"
                        + SimappeConstant.PATH_UPDATE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<IntegrationExecutionDto> updateIntegrationExecution(
                        @Valid @RequestBody IntegrationExecutionDto dto, @RequestParam Long id);

        /**
         * Elimina una ejecución de integración por su ID.
         *
         * @param id el ID de la ejecución a eliminar
         * @return una respuesta vacía indicando el resultado de la operación
         */
        @DeleteExchange(value = "/api/v1/integration-execution"
                        + SimappeConstant.PATH_DELETE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<Void> deleteIntegrationExecution(@RequestParam Long id);

        /**
         * Recupera una lista paginada de ejecuciones de integración.
         *
         * @param requestQuery los parámetros de consulta y paginación
         * @return una página de ejecuciones de integración
         * @throws SimappeException si ocurre un error durante la consulta
         */
        @PostExchange(value = "/api/v1/integration-execution"
                        + SimappeConstant.PATH_PAGE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<PageDto<IntegrationExecutionDto>> getIntegrationExecutionPage(
                        @RequestBody SimappeRequestQuery requestQuery) throws SimappeException;

        // ========== GESTIÓN DE REGISTROS DE INTEGRACIÓN (INTEGRATION LOG MANAGEMENT)
        // ==========

        /**
         * Verifica la disponibilidad del servicio de registros de integración.
         *
         * @return la respuesta con el estado de disponibilidad del servicio
         */
        @GetExchange(value = "/api/v1/integration-log"
                        + SimappeConstant.PATH_INDEX, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<ServiceResponseAvailable> integrationLogIndex();

        /**
         * Recupera todos los registros de integración.
         *
         * @return la lista de registros de integración
         */
        @GetExchange(value = "/api/v1/integration-log"
                        + SimappeConstant.PATH_READ, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<IntegrationLogDto>> readIntegrationLogs();

        /**
         * Recupera los detalles de un registro de integración por su ID.
         *
         * @param id el ID del registro
         * @return los detalles del registro
         */
        @GetExchange(value = "/api/v1/integration-log"
                        + SimappeConstant.PATH_GET, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<IntegrationLogDto> getIntegrationLogId(@RequestParam Long id);

        /**
         * Crea un nuevo registro de integración.
         *
         * @param dto el DTO del registro de integración
         * @return el registro de integración creado
         * @throws SimappeException si ocurre un error durante la creación
         */
        @PostExchange(value = "/api/v1/integration-log"
                        + SimappeConstant.PATH_CREATE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<IntegrationLogDto> createIntegrationLog(@Valid @RequestBody IntegrationLogDto dto)
                        throws SimappeException;

        /**
         * Actualiza un registro de integración existente.
         *
         * @param dto el DTO del registro con los cambios
         * @param id  el ID del registro a actualizar
         * @return el registro de integración actualizado
         */
        @PutExchange(value = "/api/v1/integration-log"
                        + SimappeConstant.PATH_UPDATE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<IntegrationLogDto> updateIntegrationLog(@Valid @RequestBody IntegrationLogDto dto,
                        @RequestParam Long id);

        /**
         * Elimina un registro de integración por su ID.
         *
         * @param id el ID del registro a eliminar
         * @return una respuesta vacía indicando el resultado de la operación
         */
        @DeleteExchange(value = "/api/v1/integration-log"
                        + SimappeConstant.PATH_DELETE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<Void> deleteIntegrationLog(@RequestParam Long id);

        /**
         * Recupera una lista paginada de registros de integración.
         *
         * @param requestQuery los parámetros de consulta y paginación
         * @return una página de registros de integración
         * @throws SimappeException si ocurre un error durante la consulta
         */
        @PostExchange(value = "/api/v1/integration-log"
                        + SimappeConstant.PATH_PAGE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<PageDto<IntegrationLogDto>> getIntegrationLogPage(
                        @RequestBody SimappeRequestQuery requestQuery) throws SimappeException;

        // ========== GESTIÓN DE PROVEEDORES DE INTEGRACIÓN (INTEGRATION PROVIDER
        // MANAGEMENT) ==========

        /**
         * Verifica la disponibilidad del servicio de proveedores de integración.
         *
         * @return la respuesta con el estado de disponibilidad del servicio
         */
        @GetExchange(value = "/api/v1/integration-provider"
                        + SimappeConstant.PATH_INDEX, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<ServiceResponseAvailable> integrationProviderIndex();

        /**
         * Recupera todos los proveedores de integración registrados.
         *
         * @return la lista de proveedores de integración
         */
        @GetExchange(value = "/api/v1/integration-provider"
                        + SimappeConstant.PATH_READ, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<IntegrationProviderDto>> readIntegrationProviders();

        /**
         * Recupera los detalles de un proveedor de integración por su ID.
         *
         * @param id el ID del proveedor
         * @return los detalles del proveedor
         */
        @GetExchange(value = "/api/v1/integration-provider"
                        + SimappeConstant.PATH_GET, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<IntegrationProviderDto> getIntegrationProviderId(@RequestParam Long id);

        /**
         * Crea un nuevo proveedor de integración.
         *
         * @param dto el DTO del proveedor de integración
         * @return el proveedor creado
         * @throws SimappeException si ocurre un error durante la creación
         */
        @PostExchange(value = "/api/v1/integration-provider"
                        + SimappeConstant.PATH_CREATE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<IntegrationProviderDto> createIntegrationProvider(
                        @Valid @RequestBody IntegrationProviderDto dto) throws SimappeException;

        /**
         * Actualiza un proveedor de integración existente.
         *
         * @param dto el DTO del proveedor con los cambios
         * @param id  el ID del proveedor a actualizar
         * @return el proveedor actualizado
         */
        @PutExchange(value = "/api/v1/integration-provider"
                        + SimappeConstant.PATH_UPDATE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<IntegrationProviderDto> updateIntegrationProvider(
                        @Valid @RequestBody IntegrationProviderDto dto, @RequestParam Long id);

        /**
         * Elimina un proveedor de integración por su ID.
         *
         * @param id el ID del proveedor a eliminar
         * @return una respuesta vacía indicando el resultado de la operación
         */
        @DeleteExchange(value = "/api/v1/integration-provider"
                        + SimappeConstant.PATH_DELETE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<Void> deleteIntegrationProvider(@RequestParam Long id);

        /**
         * Recupera una lista paginada de proveedores de integración.
         *
         * @param requestQuery los parámetros de consulta y paginación
         * @return una página de proveedores de integración
         * @throws SimappeException si ocurre un error durante la consulta
         */
        @PostExchange(value = "/api/v1/integration-provider"
                        + SimappeConstant.PATH_PAGE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<PageDto<IntegrationProviderDto>> getIntegrationProviderPage(
                        @RequestBody SimappeRequestQuery requestQuery) throws SimappeException;

        // ========== GESTIÓN DE HORARIOS DE INTEGRACIÓN (INTEGRATION SCHEDULE
        // MANAGEMENT) ==========

        /**
         * Verifica la disponibilidad del servicio de horarios de integración.
         *
         * @return la respuesta con el estado de disponibilidad del servicio
         */
        @GetExchange(value = "/api/v1/integration-schedule"
                        + SimappeConstant.PATH_INDEX, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<ServiceResponseAvailable> integrationScheduleIndex();

        /**
         * Recupera todos los horarios de integración registrados.
         *
         * @return la lista de horarios de integración
         */
        @GetExchange(value = "/api/v1/integration-schedule"
                        + SimappeConstant.PATH_READ, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<IntegrationScheduleDto>> readIntegrationSchedules();

        /**
         * Recupera los detalles de un horario de integración por su ID.
         *
         * @param id el ID del horario
         * @return los detalles del horario
         */
        @GetExchange(value = "/api/v1/integration-schedule"
                        + SimappeConstant.PATH_GET, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<IntegrationScheduleDto> getIntegrationScheduleId(@RequestParam Long id);

        /**
         * Crea un nuevo horario de integración.
         *
         * @param dto el DTO del horario de integración
         * @return el horario creado
         * @throws SimappeException si ocurre un error durante la creación
         */
        @PostExchange(value = "/api/v1/integration-schedule"
                        + SimappeConstant.PATH_CREATE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<IntegrationScheduleDto> createIntegrationSchedule(
                        @Valid @RequestBody IntegrationScheduleDto dto) throws SimappeException;

        /**
         * Actualiza un horario de integración existente.
         *
         * @param dto el DTO del horario con los cambios
         * @param id  el ID del horario a actualizar
         * @return el horario actualizado
         */
        @PutExchange(value = "/api/v1/integration-schedule"
                        + SimappeConstant.PATH_UPDATE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<IntegrationScheduleDto> updateIntegrationSchedule(
                        @Valid @RequestBody IntegrationScheduleDto dto, @RequestParam Long id);

        /**
         * Elimina un horario de integración por su ID.
         *
         * @param id el ID del horario a eliminar
         * @return una respuesta vacía indicando el resultado de la operación
         */
        @DeleteExchange(value = "/api/v1/integration-schedule"
                        + SimappeConstant.PATH_DELETE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<Void> deleteIntegrationSchedule(@RequestParam Long id);

        /**
         * Recupera una lista paginada de horarios de integración.
         *
         * @param requestQuery los parámetros de consulta y paginación
         * @return una página de horarios de integración
         * @throws SimappeException si ocurre un error durante la consulta
         */
        @PostExchange(value = "/api/v1/integration-schedule"
                        + SimappeConstant.PATH_PAGE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<PageDto<IntegrationScheduleDto>> getIntegrationSchedulePage(
                        @RequestBody SimappeRequestQuery requestQuery) throws SimappeException;

        // ========== GESTIÓN DE IDIOMAS (LANGUAGE MANAGEMENT) ==========

        /**
         * Verifica la disponibilidad del servicio de idiomas.
         *
         * @return la respuesta con el estado de disponibilidad del servicio
         */
        @GetExchange(value = "/api/v1/language" + SimappeConstant.PATH_INDEX, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<ServiceResponseAvailable> languageIndex();

        /**
         * Recupera todos los idiomas registrados.
         *
         * @return la lista de idiomas
         */
        @GetExchange(value = "/api/v1/language" + SimappeConstant.PATH_READ, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<LanguageDto>> readLanguages();

        /**
         * Recupera los detalles de un idioma por su ID.
         *
         * @param id el ID del idioma
         * @return los detalles del idioma
         */
        @GetExchange(value = "/api/v1/language" + SimappeConstant.PATH_GET, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<LanguageDto> getLanguageId(@RequestParam Long id);

        /**
         * Crea un nuevo idioma.
         *
         * @param dto el DTO del idioma
         * @return el idioma creado
         * @throws SimappeException si ocurre un error durante la creación
         */
        @PostExchange(value = "/api/v1/language"
                        + SimappeConstant.PATH_CREATE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<LanguageDto> createLanguage(@Valid @RequestBody LanguageDto dto) throws SimappeException;

        /**
         * Actualiza un idioma existente.
         *
         * @param dto el DTO del idioma con los cambios
         * @param id  el ID del idioma a actualizar
         * @return el idioma actualizado
         */
        @PutExchange(value = "/api/v1/language"
                        + SimappeConstant.PATH_UPDATE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<LanguageDto> updateLanguage(@Valid @RequestBody LanguageDto dto, @RequestParam Long id);

        /**
         * Elimina un idioma por su ID.
         *
         * @param id el ID del idioma a eliminar
         * @return una respuesta vacía indicando el resultado de la operación
         */
        @DeleteExchange(value = "/api/v1/language"
                        + SimappeConstant.PATH_DELETE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<Void> deleteLanguage(@RequestParam Long id);

        /**
         * Recupera una lista paginada de idiomas.
         *
         * @param requestQuery los parámetros de consulta y paginación
         * @return una página de idiomas
         * @throws SimappeException si ocurre un error durante la consulta
         */
        @PostExchange(value = "/api/v1/language" + SimappeConstant.PATH_PAGE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<PageDto<LanguageDto>> getLanguagePage(@RequestBody SimappeRequestQuery requestQuery)
                        throws SimappeException;

        // ========== GESTIÓN DE LOCALIZACIÓN (LOCALE MANAGEMENT) ==========

        /**
         * Verifica la disponibilidad del servicio de localización.
         *
         * @return la respuesta con el estado de disponibilidad del servicio
         */
        @GetExchange(value = "/api/v1/locale" + SimappeConstant.PATH_INDEX, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<ServiceResponseAvailable> localeIndex();

        /**
         * Recupera todas las localizaciones registradas.
         *
         * @return la lista de localizaciones
         */
        @GetExchange(value = "/api/v1/locale" + SimappeConstant.PATH_READ, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<LocaleDto>> readLocales();

        /**
         * Recupera los detalles de una localización por su ID.
         *
         * @param id el ID de la localización
         * @return los detalles de la localización
         */
        @GetExchange(value = "/api/v1/locale" + SimappeConstant.PATH_GET, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<LocaleDto> getLocaleId(@RequestParam Long id);

        /**
         * Crea una nueva localización.
         *
         * @param dto el DTO de la localización
         * @return la localización creada
         * @throws SimappeException si ocurre un error durante la creación
         */
        @PostExchange(value = "/api/v1/locale" + SimappeConstant.PATH_CREATE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<LocaleDto> createLocale(@Valid @RequestBody LocaleDto dto) throws SimappeException;

        /**
         * Actualiza una localización existente.
         *
         * @param dto el DTO de la localización con los cambios
         * @param id  el ID de la localización a actualizar
         * @return la localización actualizada
         */
        @PutExchange(value = "/api/v1/locale" + SimappeConstant.PATH_UPDATE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<LocaleDto> updateLocale(@Valid @RequestBody LocaleDto dto, @RequestParam Long id);

        /**
         * Elimina una localización por su ID.
         *
         * @param id el ID de la localización a eliminar
         * @return una respuesta vacía indicando el resultado de la operación
         */
        @DeleteExchange(value = "/api/v1/locale"
                        + SimappeConstant.PATH_DELETE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<Void> deleteLocale(@RequestParam Long id);

        /**
         * Recupera una lista paginada de localizaciones.
         *
         * @param requestQuery los parámetros de consulta y paginación
         * @return una página de localizaciones
         * @throws SimappeException si ocurre un error durante la consulta
         */
        @PostExchange(value = "/api/v1/locale" + SimappeConstant.PATH_PAGE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<PageDto<LocaleDto>> getLocalePage(@RequestBody SimappeRequestQuery requestQuery)
                        throws SimappeException;

        // ========== GESTIÓN DE TRADUCCIONES (TRANSLATION MANAGEMENT) ==========

        /**
         * Verifica la disponibilidad del servicio de traducciones.
         *
         * @return la respuesta con el estado de disponibilidad del servicio
         */
        @GetExchange(value = "/api/v1/translation"
                        + SimappeConstant.PATH_INDEX, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<ServiceResponseAvailable> translationIndex();

        /**
         * Recupera todas las traducciones registradas.
         *
         * @return la lista de traducciones
         */
        @GetExchange(value = "/api/v1/translation"
                        + SimappeConstant.PATH_READ, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<TranslationDto>> readTranslations();

        /**
         * Recupera los detalles de una traducción por su ID.
         *
         * @param id el ID de la traducción
         * @return los detalles de la traducción
         */
        @GetExchange(value = "/api/v1/translation"
                        + SimappeConstant.PATH_GET, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<TranslationDto> getTranslationId(@RequestParam Long id);

        /**
         * Crea una nueva traducción.
         *
         * @param dto el DTO de la traducción
         * @return la traducción creada
         * @throws SimappeException si ocurre un error durante la creación
         */
        @PostExchange(value = "/api/v1/translation"
                        + SimappeConstant.PATH_CREATE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<TranslationDto> createTranslation(@Valid @RequestBody TranslationDto dto)
                        throws SimappeException;

        /**
         * Actualiza una traducción existente.
         *
         * @param dto el DTO de la traducción con los cambios
         * @param id  el ID de la traducción a actualizar
         * @return la traducción actualizada
         */
        @PutExchange(value = "/api/v1/translation"
                        + SimappeConstant.PATH_UPDATE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<TranslationDto> updateTranslation(@Valid @RequestBody TranslationDto dto,
                        @RequestParam Long id);

        /**
         * Elimina una traducción por su ID.
         *
         * @param id el ID de la traducción a eliminar
         * @return una respuesta vacía indicando el resultado de la operación
         */
        @DeleteExchange(value = "/api/v1/translation"
                        + SimappeConstant.PATH_DELETE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<Void> deleteTranslation(@RequestParam Long id);

        /**
         * Recupera una lista paginada de traducciones.
         *
         * @param requestQuery los parámetros de consulta y paginación
         * @return una página de traducciones
         * @throws SimappeException si ocurre un error durante la consulta
         */
        @PostExchange(value = "/api/v1/translation"
                        + SimappeConstant.PATH_PAGE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<PageDto<TranslationDto>> getTranslationPage(@RequestBody SimappeRequestQuery requestQuery)
                        throws SimappeException;

        // ========== GESTIÓN DE PAQUETES DE TRADUCCIÓN (TRANSLATION BUNDLE MANAGEMENT)
        // ==========
        // DEPRECATED: el CRUD translation-bundle es legacy; las traducciones en runtime se sirven por
        // GET /api/v1/translation/bundle/{langCode}. Se elimina en el próximo MAJOR.

        /**
         * Verifica la disponibilidad del servicio de paquetes de traducción.
         *
         * @return la respuesta con el estado de disponibilidad del servicio
         * @deprecated CRUD legacy de {@code translation_bundles}; se elimina en el próximo MAJOR.
         */
        @Deprecated(since = "5.10.0", forRemoval = true)
        @SuppressWarnings("java:S1133")
        @GetExchange(value = "/api/v1/translation-bundle"
                        + SimappeConstant.PATH_INDEX, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<ServiceResponseAvailable> translationBundleIndex();

        /**
         * Recupera todos los paquetes de traducción registrados.
         *
         * @return la lista de paquetes de traducción
         * @deprecated CRUD legacy de {@code translation_bundles}; se elimina en el próximo MAJOR.
         */
        @Deprecated(since = "5.10.0", forRemoval = true)
        @SuppressWarnings("java:S1133")
        @GetExchange(value = "/api/v1/translation-bundle"
                        + SimappeConstant.PATH_READ, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<TranslationBundleDto>> readTranslationBundles();

        /**
         * Recupera los detalles de un paquete de traducción por su ID.
         *
         * @param id el ID del paquete
         * @return los detalles del paquete
         * @deprecated CRUD legacy de {@code translation_bundles}; se elimina en el próximo MAJOR.
         */
        @Deprecated(since = "5.10.0", forRemoval = true)
        @SuppressWarnings("java:S1133")
        @GetExchange(value = "/api/v1/translation-bundle"
                        + SimappeConstant.PATH_GET, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<TranslationBundleDto> getTranslationBundleId(@RequestParam Long id);

        /**
         * Crea un nuevo paquete de traducción.
         *
         * @param dto el DTO del paquete de traducción
         * @return el paquete de traducción creado
         * @throws SimappeException si ocurre un error durante la creación
         * @deprecated CRUD legacy de {@code translation_bundles}; se elimina en el próximo MAJOR.
         */
        @Deprecated(since = "5.10.0", forRemoval = true)
        @SuppressWarnings("java:S1133")
        @PostExchange(value = "/api/v1/translation-bundle"
                        + SimappeConstant.PATH_CREATE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<TranslationBundleDto> createTranslationBundle(
                        @Valid @RequestBody TranslationBundleDto dto)
                        throws SimappeException;

        /**
         * Actualiza un paquete de traducción existente.
         *
         * @param dto el DTO del paquete con los cambios
         * @param id  el ID del paquete a actualizar
         * @return el paquete de traducción actualizado
         * @deprecated CRUD legacy de {@code translation_bundles}; se elimina en el próximo MAJOR.
         */
        @Deprecated(since = "5.10.0", forRemoval = true)
        @SuppressWarnings("java:S1133")
        @PutExchange(value = "/api/v1/translation-bundle"
                        + SimappeConstant.PATH_UPDATE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<TranslationBundleDto> updateTranslationBundle(
                        @Valid @RequestBody TranslationBundleDto dto,
                        @RequestParam Long id);

        /**
         * Elimina un paquete de traducción por su ID.
         *
         * @param id el ID del paquete a eliminar
         * @return una respuesta vacía indicando el resultado de la operación
         * @deprecated CRUD legacy de {@code translation_bundles}; se elimina en el próximo MAJOR.
         */
        @Deprecated(since = "5.10.0", forRemoval = true)
        @SuppressWarnings("java:S1133")
        @DeleteExchange(value = "/api/v1/translation-bundle"
                        + SimappeConstant.PATH_DELETE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<Void> deleteTranslationBundle(@RequestParam Long id);

        /**
         * Recupera una lista paginada de paquetes de traducción.
         *
         * @param requestQuery los parámetros de consulta y paginación
         * @return una página de paquetes de traducción
         * @throws SimappeException si ocurre un error durante la consulta
         * @deprecated CRUD legacy de {@code translation_bundles}; se elimina en el próximo MAJOR.
         */
        @Deprecated(since = "5.10.0", forRemoval = true)
        @SuppressWarnings("java:S1133")
        @PostExchange(value = "/api/v1/translation-bundle"
                        + SimappeConstant.PATH_PAGE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<PageDto<TranslationBundleDto>> getTranslationBundlePage(
                        @RequestBody SimappeRequestQuery requestQuery) throws SimappeException;

        // ========== GESTIÓN DE CLAVES DE TRADUCCIÓN (TRANSLATION KEY MANAGEMENT)
        // ==========

        /**
         * Verifica la disponibilidad del servicio de claves de traducción.
         *
         * @return la respuesta con el estado de disponibilidad del servicio
         */
        @GetExchange(value = "/api/v1/translation-key"
                        + SimappeConstant.PATH_INDEX, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<ServiceResponseAvailable> translationKeyIndex();

        /**
         * Recupera todas las claves de traducción registradas.
         *
         * @return la lista de claves de traducción
         */
        @GetExchange(value = "/api/v1/translation-key"
                        + SimappeConstant.PATH_READ, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<TranslationKeyDto>> readTranslationKeys();

        /**
         * Recupera los detalles de una clave de traducción por su ID.
         *
         * @param id el ID de la clave
         * @return los detalles de la clave
         */
        @GetExchange(value = "/api/v1/translation-key"
                        + SimappeConstant.PATH_GET, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<TranslationKeyDto> getTranslationKeyId(@RequestParam Long id);

        /**
         * Crea una nueva clave de traducción.
         *
         * @param dto el DTO de la clave de traducción
         * @return la clave de traducción creada
         * @throws SimappeException si ocurre un error durante la creación
         */
        @PostExchange(value = "/api/v1/translation-key"
                        + SimappeConstant.PATH_CREATE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<TranslationKeyDto> createTranslationKey(@Valid @RequestBody TranslationKeyDto dto)
                        throws SimappeException;

        /**
         * Actualiza una clave de traducción existente.
         *
         * @param dto el DTO de la clave con los cambios
         * @param id  el ID de la clave a actualizar
         * @return la clave de traducción actualizada
         */
        @PutExchange(value = "/api/v1/translation-key"
                        + SimappeConstant.PATH_UPDATE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<TranslationKeyDto> updateTranslationKey(@Valid @RequestBody TranslationKeyDto dto,
                        @RequestParam Long id);

        /**
         * Elimina una clave de traducción por su ID.
         *
         * @param id el ID de la clave a eliminar
         * @return una respuesta vacía indicando el resultado de la operación
         */
        @DeleteExchange(value = "/api/v1/translation-key"
                        + SimappeConstant.PATH_DELETE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<Void> deleteTranslationKey(@RequestParam Long id);

        /**
         * Recupera una lista paginada de claves de traducción.
         *
         * @param requestQuery los parámetros de consulta y paginación
         * @return una página de claves de traducción
         * @throws SimappeException si ocurre un error durante la consulta
         */
        @PostExchange(value = "/api/v1/translation-key"
                        + SimappeConstant.PATH_PAGE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<PageDto<TranslationKeyDto>> getTranslationKeyPage(
                        @RequestBody SimappeRequestQuery requestQuery) throws SimappeException;

        // ========== GESTIÓN DE CONTROL DE ACCESO (ACCESS CONTROL MANAGEMENT)
        // ==========

        /**
         * Verifica la disponibilidad del servicio de control de acceso.
         *
         * @return la respuesta con el estado de disponibilidad del servicio
         */
        @GetExchange(value = "/api/v1/access-control"
                        + SimappeConstant.PATH_INDEX, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<ServiceResponseAvailable> accessControlIndex();

        /**
         * Recupera todos los registros de control de acceso.
         *
         * @return la lista de registros de control de acceso
         */
        @GetExchange(value = "/api/v1/access-control"
                        + SimappeConstant.PATH_READ, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<?>> readAccessControls();

        /**
         * Recupera los registros de control de acceso por ID de usuario.
         *
         * @param userId el ID del usuario
         * @return la lista de registros de control de acceso para el usuario
         */
        @GetExchange(value = "/api/v1/access-control/by-user", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<?>> readAccessControlsByUser(@RequestParam Long userId);

        /**
         * Recupera los registros de control de acceso por ID de rol.
         *
         * @param roleId el ID del rol
         * @return la lista de registros de control de acceso para el rol
         */
        @GetExchange(value = "/api/v1/access-control/by-role", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<?>> readAccessControlsByRole(@RequestParam Long roleId);

        /**
         * Evalúa el acceso basado en el ID de usuario y los datos de evaluación.
         *
         * @param userId         el ID del usuario
         * @param evaluationData los datos de evaluación
         * @return el resultado de la evaluación
         */
        @PostExchange(value = "/api/v1/access-control/evaluate", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<Boolean> evaluateAccess(@RequestParam Long userId,
                        @RequestBody Map<String, String> evaluationData);

        /**
         * Recupera los detalles de un registro de control de acceso por su ID.
         *
         * @param id el ID del registro
         * @return los detalles del registro de control de acceso
         */
        @GetExchange(value = "/api/v1/access-control"
                        + SimappeConstant.PATH_GET, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<?> getAccessControl(@RequestParam Long id);

        /**
         * Crea un nuevo registro de control de acceso.
         *
         * @param dto el DTO del registro de control de acceso (Object genérico)
         * @return el registro de control de acceso creado
         */
        @PostExchange(value = "/api/v1/access-control"
                        + SimappeConstant.PATH_CREATE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<?> createAccessControl(@Valid @RequestBody Object dto);

        /**
         * Actualiza un registro de control de acceso existente.
         *
         * @param dto el DTO del registro con los cambios
         * @param id  el ID del registro a actualizar
         * @return el registro de control de acceso actualizado
         */
        @PutExchange(value = "/api/v1/access-control"
                        + SimappeConstant.PATH_UPDATE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<?> updateAccessControl(@Valid @RequestBody Object dto, @RequestParam Long id);

        /**
         * Elimina un registro de control de acceso por su ID.
         *
         * @param id el ID del registro a eliminar
         * @return una respuesta vacía indicando el resultado de la operación
         */
        @DeleteExchange(value = "/api/v1/access-control"
                        + SimappeConstant.PATH_DELETE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<Void> deleteAccessControl(@RequestParam Long id);

        /**
         * SuccessResponse<PageDto<?>> queryAccessControls(@RequestBody
         * SimappeRequestQuery query);
         * 
         * // ========== LOGIN ATTEMPT MANAGEMENT ==========
         * 
         * /**
         * Retrieves login attempt records by user ID.
         *
         * @param userId the user ID
         * @return the list of login attempts for the user
         */
        @GetExchange(value = "/api/v1/login-attempt/by-user", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<com.catcsoft.simappe.model.admin.dto.security.policy.LoginAttemptDto>> readLoginAttemptsByUser(
                        @RequestParam("userId") Long userId);

        /**
         * Creates a new login attempt record.
         *
         * @param dto the login attempt DTO
         * @return the created login attempt
         */
        @PostExchange(value = "/api/v1/login-attempt"
                        + SimappeConstant.PATH_CREATE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<com.catcsoft.simappe.model.admin.dto.security.policy.LoginAttemptDto> recordLoginAttempt(
                        @Valid @RequestBody com.catcsoft.simappe.model.admin.dto.security.policy.LoginAttemptDto dto);

        // ========== MAINTENANCE WINDOW MANAGEMENT ==========

        /**
         * Checks the availability of the maintenance window service.
         *
         * @return the service availability status
         */
        @GetExchange(value = "/api/v1/maintenance-windows"
                        + SimappeConstant.PATH_INDEX, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<ServiceResponseAvailable> maintenanceWindowIndex();

        /**
         * Retrieves all maintenance window records.
         *
         * @return the list of maintenance window records
         */
        @GetExchange(value = "/api/v1/maintenance-windows"
                        + SimappeConstant.PATH_READ, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<?>> readMaintenanceWindows();

        /**
         * Retrieves upcoming maintenance windows.
         *
         * @return the list of upcoming maintenance windows
         */
        @GetExchange(value = "/api/v1/maintenance-windows/read-upcoming", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<?>> readUpcomingMaintenanceWindows();

        /**
         * Retrieves active maintenance windows.
         *
         * @return the list of active maintenance windows
         */
        @GetExchange(value = "/api/v1/maintenance-windows/read-active", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<?>> readActiveMaintenanceWindows();

        /**
         * Retrieves maintenance windows by service name.
         *
         * @param serviceName the service name
         * @return the list of maintenance windows for the service
         */
        @GetExchange(value = "/api/v1/maintenance-windows/read-by-service", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<?>> readMaintenanceWindowsByService(@RequestParam String serviceName);

        /**
         * Retrieves maintenance windows by customer ID.
         *
         * @param customerId the customer ID
         * @return the list of maintenance windows for the customer
         */
        @GetExchange(value = "/api/v1/maintenance-windows/read-by-customer", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<?>> readMaintenanceWindowsByCustomer(@RequestParam Long customerId);

        /**
         * Retrieves maintenance window details by ID.
         *
         * @param id the maintenance window ID
         * @return the maintenance window details
         */
        @GetExchange(value = "/api/v1/maintenance-windows"
                        + SimappeConstant.PATH_GET, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<?> getMaintenanceWindow(@RequestParam Long id);

        /**
         * Creates a new maintenance window record.
         *
         * @param dto the maintenance window DTO
         * @return the created maintenance window
         */
        @PostExchange(value = "/api/v1/maintenance-windows"
                        + SimappeConstant.PATH_CREATE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<?> createMaintenanceWindow(@Valid @RequestBody Object dto);

        /**
         * Starts a maintenance window.
         *
         * @param id the maintenance window ID
         * @return the updated maintenance window
         */
        @PutExchange(value = "/api/v1/maintenance-windows/start", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<?> startMaintenanceWindow(@RequestParam Long id);

        /**
         * Completes a maintenance window.
         *
         * @param id the maintenance window ID
         * @return the updated maintenance window
         */
        @PutExchange(value = "/api/v1/maintenance-windows/complete", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<?> completeMaintenanceWindow(@RequestParam Long id);

        /**
         * Cancels a maintenance window.
         *
         * @param id the maintenance window ID
         * @return the updated maintenance window
         */
        @PutExchange(value = "/api/v1/maintenance-windows/cancel", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<?> cancelMaintenanceWindow(@RequestParam Long id);

        /**
         * Updates an existing maintenance window record.
         *
         * @param dto the maintenance window DTO
         * @param id  the maintenance window ID
         * @return the updated maintenance window
         */
        @PutExchange(value = "/api/v1/maintenance-windows"
                        + SimappeConstant.PATH_UPDATE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<?> updateMaintenanceWindow(@Valid @RequestBody Object dto, @RequestParam Long id);

        /**
         * Deletes a maintenance window record by ID.
         *
         * @param id the maintenance window ID
         * @return void response
         */
        @DeleteExchange(value = "/api/v1/maintenance-windows"
                        + SimappeConstant.PATH_DELETE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<Void> deleteMaintenanceWindow(@RequestParam Long id);

        /**
         * Retrieves a paginated list of maintenance windows.
         *
         * @param query the query parameters
         * @return a page of maintenance windows
         */
        @PostExchange(value = "/api/v1/maintenance-windows"
                        + SimappeConstant.PATH_PAGE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<PageDto<?>> getMaintenanceWindowPage(@RequestBody SimappeRequestQuery query);

        // ========== GESTIÓN DE MÓDULOS (MODULE MANAGEMENT) ==========

        /**
         * Verifica la disponibilidad del servicio de módulos.
         *
         * @return la respuesta con el estado de disponibilidad del servicio
         */
        @GetExchange(value = "/api/v1/module" + SimappeConstant.PATH_INDEX, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<ServiceResponseAvailable> moduleIndex();

        /**
         * Recupera todos los registros de módulos.
         *
         * @return la lista de registros de módulos
         */
        @GetExchange(value = "/api/v1/module" + SimappeConstant.PATH_READ, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<?>> readModules();

        /**
         * Recupera los módulos por estado.
         *
         * @param status el estado de registro
         * @return la lista de módulos para el estado
         */
        @GetExchange(value = "/api/v1/module/read-status", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<?>> readModulesByStatus(@RequestParam String status);

        /**
         * Recupera los detalles de un módulo por su ID.
         *
         * @param id el ID del módulo
         * @return los detalles del módulo
         */
        @GetExchange(value = "/api/v1/module" + SimappeConstant.PATH_GET, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<?> getModule(@RequestParam Long id);

        /**
         * Recupera los detalles de un módulo por su nombre.
         *
         * @param name el nombre del módulo
         * @return los detalles del módulo
         */
        @GetExchange(value = "/api/v1/module/get-name", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<?> getModuleByName(@RequestParam String name);

        /**
         * Recupera los detalles de un módulo por su código.
         *
         * @param code el código del módulo
         * @return los detalles del módulo
         */
        @GetExchange(value = "/api/v1/module/get-code", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<?> getModuleByCode(@RequestParam String code);

        /**
         * Crea un nuevo registro de módulo.
         *
         * @param dto el DTO del módulo (Object genérico)
         * @return el módulo creado
         */
        @PostExchange(value = "/api/v1/module" + SimappeConstant.PATH_CREATE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<?> createModule(@Valid @RequestBody Object dto);

        /**
         * Actualiza un registro de módulo existente.
         *
         * @param dto el DTO del módulo con los cambios
         * @param id  el ID del módulo a actualizar
         * @return el módulo actualizado
         */
        @PutExchange(value = "/api/v1/module" + SimappeConstant.PATH_UPDATE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<?> updateModule(@Valid @RequestBody Object dto, @RequestParam Long id);

        /**
         * Actualiza la configuración de ruta del módulo.
         *
         * @param config la configuración de ruta
         * @return el módulo actualizado
         */
        @PutExchange(value = "/api/v1/module/update-path", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<?> updateModulePath(@RequestBody Map<String, Object> config);

        /**
         * Actualiza la configuración del módulo.
         *
         * @param config los datos de configuración
         * @return el módulo actualizado
         */
        @PutExchange(value = "/api/v1/module/update-config", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<?> updateModuleConfig(@RequestBody Map<String, Object> config);

        /**
         * Agrega un grupo de opciones al módulo.
         *
         * @param data los datos de mapeo del grupo de opciones
         * @return el módulo actualizado
         */
        @PutExchange(value = "/api/v1/module/add-option-group", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<?> addOptionGroupToModule(@RequestBody Map<String, Object> data);

        /**
         * Elimina un grupo de opciones del módulo.
         *
         * @param data los datos de mapeo del grupo de opciones
         * @return respuesta vacía
         */
        @DeleteExchange(value = "/api/v1/module/delete-option-group", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<Void> deleteOptionGroupFromModule(@RequestBody Map<String, Object> data);

        /**
         * Agrega un paquete de módulo al módulo.
         *
         * @param data los datos de mapeo del paquete de módulo
         * @return el módulo actualizado
         */
        @PutExchange(value = "/api/v1/module/add-module-package", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<?> addModulePackageToModule(@RequestBody Map<String, Object> data);

        /**
         * Elimina un paquete de módulo del módulo.
         *
         * @param data los datos de mapeo del paquete de módulo
         * @return respuesta vacía
         */
        @DeleteExchange(value = "/api/v1/module/delete-module-package", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<Void> deleteModulePackageFromModule(@RequestBody Map<String, Object> data);

        /**
         * Elimina un registro de módulo por su ID.
         *
         * @param id el ID del módulo a eliminar
         * @return una respuesta vacía indicando el resultado de la operación
         */
        @DeleteExchange(value = "/api/v1/module"
                        + SimappeConstant.PATH_DELETE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<Void> deleteModule(@RequestParam Long id);

        /**
         * Recupera una lista paginada de módulos.
         *
         * @param query los parámetros de consulta
         * @return una página de módulos
         */
        @PostExchange(value = "/api/v1/module" + SimappeConstant.PATH_PAGE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<PageDto<?>> getModulePage(@RequestBody SimappeRequestQuery query);

        // ========== GESTIÓN DE PAQUETES DE MÓDULOS (MODULE PACKAGE MANAGEMENT)
        // ==========

        /**
         * Verifica la disponibilidad del servicio de paquetes de módulos.
         *
         * @return la respuesta con el estado de disponibilidad del servicio
         */
        @GetExchange(value = "/api/v1/module-package"
                        + SimappeConstant.PATH_INDEX, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<ServiceResponseAvailable> modulePackageIndex();

        /**
         * Recupera todos los registros de paquetes de módulos.
         *
         * @return la lista de registros de paquetes de módulos
         */
        @GetExchange(value = "/api/v1/module-package"
                        + SimappeConstant.PATH_READ, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<?>> readModulePackages();

        /**
         * Recupera los paquetes de módulos por estado.
         *
         * @param status el estado de registro
         * @return la lista de paquetes de módulos para el estado
         */
        @GetExchange(value = "/api/v1/module-package/read-status", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<?>> readModulePackagesByStatus(@RequestParam String status);

        /**
         * Recupera los detalles de un paquete de módulo por su ID.
         *
         * @param id el ID del paquete de módulo
         * @return los detalles del paquete de módulo
         */
        @GetExchange(value = "/api/v1/module-package"
                        + SimappeConstant.PATH_GET, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<?> getModulePackage(@RequestParam Long id);

        /**
         * Recupera los detalles de un paquete de módulo por su nombre.
         *
         * @param name el nombre del paquete de módulo
         * @return los detalles del paquete de módulo
         */
        @GetExchange(value = "/api/v1/module-package/get-name", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<?> getModulePackageByName(@RequestParam String name);

        /**
         * Recupera los detalles de un paquete de módulo por su código.
         *
         * @param code el código del paquete de módulo
         * @return los detalles del paquete de módulo
         */
        @GetExchange(value = "/api/v1/module-package/get-code", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<?> getModulePackageByCode(@RequestParam String code);

        /**
         * Crea un nuevo registro de paquete de módulo.
         *
         * @param dto el DTO del paquete de módulo (Object genérico)
         * @return el paquete de módulo creado
         */
        @PostExchange(value = "/api/v1/module-package"
                        + SimappeConstant.PATH_CREATE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<?> createModulePackage(@Valid @RequestBody Object dto);

        /**
         * Actualiza un registro de paquete de módulo existente.
         *
         * @param dto el DTO del paquete de módulo con los cambios
         * @param id  el ID del paquete de módulo a actualizar
         * @return el paquete de módulo actualizado
         */
        @PutExchange(value = "/api/v1/module-package"
                        + SimappeConstant.PATH_UPDATE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<?> updateModulePackage(@Valid @RequestBody Object dto, @RequestParam Long id);

        /**
         * Actualiza la configuración del paquete de módulo.
         *
         * @param config los datos de configuración
         * @return el paquete de módulo actualizado
         */
        @PutExchange(value = "/api/v1/module-package/update-config", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<?> updateModulePackageConfig(@RequestBody Map<String, Object> config);

        /**
         * Elimina un registro de paquete de módulo por su ID.
         *
         * @param id el ID del paquete de módulo a eliminar
         * @return una respuesta vacía indicando el resultado de la operación
         */
        @DeleteExchange(value = "/api/v1/module-package"
                        + SimappeConstant.PATH_DELETE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<Void> deleteModulePackage(@RequestParam Long id);

        /**
         * Recupera una lista paginada de paquetes de módulos.
         *
         * @param query los parámetros de consulta
         * @return una página de paquetes de módulos
         */
        @PostExchange(value = "/api/v1/module-package"
                        + SimappeConstant.PATH_PAGE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<PageDto<?>> getModulePackagePage(@RequestBody SimappeRequestQuery query);

        // ========== GESTIÓN DE CLIENTES OAUTH (OAUTH CLIENT MANAGEMENT) ==========

        /**
         * Verifica la disponibilidad del servicio de clientes OAuth.
         *
         * @return la respuesta con el estado de disponibilidad del servicio
         */
        @GetExchange(value = "/api/v1/oauth/client"
                        + SimappeConstant.PATH_INDEX, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<ServiceResponseAvailable> oauthClientIndex();

        /**
         * Recupera todos los registros de clientes OAuth.
         *
         * @return la lista de registros de clientes OAuth
         */
        @GetExchange(value = "/api/v1/oauth/client"
                        + SimappeConstant.PATH_READ, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<?>> readOAuthClients();

        /**
         * Recupera los detalles de un cliente OAuth por su ID.
         *
         * @param id el ID del cliente OAuth
         * @return los detalles del cliente OAuth
         */
        @GetExchange(value = "/api/v1/oauth/client"
                        + SimappeConstant.PATH_GET, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<?> getOAuthClient(@RequestParam Long id);

        /**
         * Crea un nuevo registro de cliente OAuth.
         *
         * @param dto el DTO del cliente OAuth (Object genérico)
         * @return el cliente OAuth creado
         */
        @PostExchange(value = "/api/v1/oauth/client"
                        + SimappeConstant.PATH_CREATE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<?> createOAuthClient(@Valid @RequestBody Object dto);

        /**
         * Actualiza un registro de cliente OAuth existente.
         *
         * @param dto el DTO del cliente OAuth con los cambios
         * @param id  el ID del cliente OAuth a actualizar
         * @return el cliente OAuth actualizado
         */
        @PutExchange(value = "/api/v1/oauth/client"
                        + SimappeConstant.PATH_UPDATE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<?> updateOAuthClient(@Valid @RequestBody Object dto, @RequestParam Long id);

        /**
         * Elimina un registro de cliente OAuth por su ID.
         *
         * @param id el ID del cliente OAuth a eliminar
         * @return una respuesta vacía indicando el resultado de la operación
         */
        @DeleteExchange(value = "/api/v1/oauth/client"
                        + SimappeConstant.PATH_DELETE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<Void> deleteOAuthClient(@RequestParam Long id);

        /**
         * Recupera una lista paginada de clientes OAuth.
         *
         * @param query los parámetros de consulta
         * @return una página de clientes OAuth
         */
        @PostExchange(value = "/api/v1/oauth/client"
                        + SimappeConstant.PATH_PAGE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<PageDto<?>> getOAuthClientPage(@RequestBody SimappeRequestQuery query);

        // ========== OPTION MANAGEMENT ==========

        /**
         * Checks the availability of the option service.
         *
         * @return the service availability status
         */
        @GetExchange(value = "/api/v1/option" + SimappeConstant.PATH_INDEX, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<ServiceResponseAvailable> optionIndex();

        /**
         * Retrieves all option records.
         *
         * @return the list of option records
         */
        @GetExchange(value = "/api/v1/option" + SimappeConstant.PATH_READ, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<?>> readOptions();

        /**
         * Retrieves options by status.
         *
         * @param status the registration status
         * @return the list of options for the status
         */
        @GetExchange(value = "/api/v1/option/read-status", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<?>> readOptionsByStatus(@RequestParam String status);

        /**
         * Retrieves parent options.
         *
         * @param parentId the parent option ID
         * @return the list of parent options
         */
        @GetExchange(value = "/api/v1/option/read-parent-option", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<?>> readParentOptions(@RequestParam Long parentId);

        /**
         * Retrieves option details by ID.
         *
         * @param id the option ID
         * @return the option details
         */
        @GetExchange(value = "/api/v1/option" + SimappeConstant.PATH_GET, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<?> getOption(@RequestParam Long id);

        /**
         * Retrieves option details by name.
         *
         * @param name the option name
         * @return the option details
         */
        @GetExchange(value = "/api/v1/option/get-name", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<?> getOptionByName(@RequestParam String name);

        /**
         * Retrieves option details by code.
         *
         * @param code the option code
         * @return the option details
         */
        @GetExchange(value = "/api/v1/option/get-code", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<?> getOptionByCode(@RequestParam String code);

        /**
         * Creates a new option record.
         *
         * @param dto the option DTO
         * @return the created option
         */
        @PostExchange(value = "/api/v1/option" + SimappeConstant.PATH_CREATE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<?> createOption(@Valid @RequestBody Object dto);

        /**
         * Updates an existing option record.
         *
         * @param dto the option DTO
         * @param id  the option ID
         * @return the updated option
         */
        @PutExchange(value = "/api/v1/option" + SimappeConstant.PATH_UPDATE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<?> updateOption(@Valid @RequestBody Object dto, @RequestParam Long id);

        /**
         * Updates the option path configuration.
         *
         * @param config the path configuration
         * @return the updated option
         */
        @PutExchange(value = "/api/v1/option/update-path", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<?> updateOptionPath(@RequestBody Map<String, Object> config);

        /**
         * Updates the option configuration.
         *
         * @param config the configuration data
         * @return the updated option
         */
        @PutExchange(value = "/api/v1/option/update-config", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<?> updateOptionConfig(@RequestBody Map<String, Object> config);

        /**
         * Deletes an option record by ID.
         *
         * @param id the option ID
         * @return void response
         */
        @DeleteExchange(value = "/api/v1/option"
                        + SimappeConstant.PATH_DELETE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<Void> deleteOption(@RequestParam Long id);

        /**
         * Retrieves a paginated list of options.
         *
         * @param query the query parameters
         * @return a page of options
         */
        @PostExchange(value = "/api/v1/option" + SimappeConstant.PATH_PAGE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<PageDto<?>> getOptionPage(@RequestBody SimappeRequestQuery query);

        // ========== GESTIÓN DE GRUPOS DE OPCIONES (OPTION GROUP MANAGEMENT) ==========

        /**
         * Verifica la disponibilidad del servicio de grupos de opciones.
         *
         * @return la respuesta con el estado de disponibilidad del servicio
         */
        @GetExchange(value = "/api/v1/option-group"
                        + SimappeConstant.PATH_INDEX, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<ServiceResponseAvailable> optionGroupIndex();

        /**
         * Recupera todos los registros de grupos de opciones.
         *
         * @return la lista de registros de grupos de opciones
         */
        @GetExchange(value = "/api/v1/option-group"
                        + SimappeConstant.PATH_READ, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<?>> readOptionGroups();

        /**
         * Recupera los grupos de opciones por estado.
         *
         * @param status el estado de registro
         * @return la lista de grupos de opciones para el estado
         */
        @GetExchange(value = "/api/v1/option-group/read-status", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<?>> readOptionGroupsByStatus(@RequestParam String status);

        /**
         * Recupera los grupos de opciones padre.
         *
         * @param parentId el ID del grupo de opciones padre
         * @return la lista de grupos de opciones padre
         */
        @GetExchange(value = "/api/v1/option-group/read-parent", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<?>> readParentOptionGroups(@RequestParam Long parentId);

        /**
         * Recupera los detalles de un grupo de opciones por su ID.
         *
         * @param id el ID del grupo de opciones
         * @return los detalles del grupo de opciones
         */
        @GetExchange(value = "/api/v1/option-group"
                        + SimappeConstant.PATH_GET, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<?> getOptionGroup(@RequestParam Long id);

        /**
         * Recupera los detalles de un grupo de opciones por su nombre.
         *
         * @param name el nombre del grupo de opciones
         * @return los detalles del grupo de opciones
         */
        @GetExchange(value = "/api/v1/option-group/get-name", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<?> getOptionGroupByName(@RequestParam String name);

        /**
         * Recupera los detalles de un grupo de opciones por su código.
         *
         * @param code el código del grupo de opciones
         * @return los detalles del grupo de opciones
         */
        @GetExchange(value = "/api/v1/option-group/get-code", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<?> getOptionGroupByCode(@RequestParam String code);

        /**
         * Crea un nuevo registro de grupo de opciones.
         *
         * @param dto el DTO del grupo de opciones (Object genérico)
         * @return el grupo de opciones creado
         */
        @PostExchange(value = "/api/v1/option-group"
                        + SimappeConstant.PATH_CREATE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<?> createOptionGroup(@Valid @RequestBody Object dto);

        /**
         * Actualiza un registro de grupo de opciones existente.
         *
         * @param dto el DTO del grupo de opciones con los cambios
         * @param id  el ID del grupo de opciones a actualizar
         * @return el grupo de opciones actualizado
         */
        @PutExchange(value = "/api/v1/option-group"
                        + SimappeConstant.PATH_UPDATE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<?> updateOptionGroup(@Valid @RequestBody Object dto, @RequestParam Long id);

        /**
         * Actualiza la configuración de ruta del grupo de opciones.
         *
         * @param config la configuración de ruta
         * @return el grupo de opciones actualizado
         */
        @PutExchange(value = "/api/v1/option-group/update-path", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<?> updateOptionGroupPath(@RequestBody Map<String, Object> config);

        /**
         * Actualiza la configuración del grupo de opciones.
         *
         * @param config los datos de configuración
         * @return el grupo de opciones actualizado
         */
        @PutExchange(value = "/api/v1/option-group/update-config", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<?> updateOptionGroupConfig(@RequestBody Map<String, Object> config);

        /**
         * Agrega una opción al grupo de opciones.
         *
         * @param data los datos de mapeo de la opción
         * @return el grupo de opciones actualizado
         */
        @PutExchange(value = "/api/v1/option-group/add-option", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<?> addOptionToGroup(@RequestBody Map<String, Object> data);

        /**
         * Elimina una opción del grupo de opciones.
         *
         * @param data los datos de mapeo de la opción
         * @return respuesta vacía
         */
        @DeleteExchange(value = "/api/v1/option-group/delete-option", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<Void> deleteOptionFromGroup(@RequestBody Map<String, Object> data);

        /**
         * Elimina un registro de grupo de opciones por su ID.
         *
         * @param id el ID del grupo de opciones a eliminar
         * @return una respuesta vacía indicando el resultado de la operación
         */
        @DeleteExchange(value = "/api/v1/option-group"
                        + SimappeConstant.PATH_DELETE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<Void> deleteOptionGroup(@RequestParam Long id);

        /**
         * Recupera una lista paginada de grupos de opciones.
         *
         * @param query los parámetros de consulta
         * @return una página de grupos de opciones
         */
        @PostExchange(value = "/api/v1/option-group"
                        + SimappeConstant.PATH_PAGE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<PageDto<?>> getOptionGroupPage(@RequestBody SimappeRequestQuery query);

        // ========== GESTIÓN DE HISTORIAL DE CONTRASEÑAS (PASSWORD HISTORY MANAGEMENT)
        // ==========

        /**
         * Verifica la disponibilidad del servicio de historial de contraseñas.
         *
         * @return la respuesta con el estado de disponibilidad del servicio
         */
        @GetExchange(value = "/api/v1/password-history"
                        + SimappeConstant.PATH_INDEX, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<ServiceResponseAvailable> passwordHistoryIndex();

        /**
         * Recupera todos los registros de historial de contraseñas.
         *
         * @return la lista de registros de historial de contraseñas
         */
        @GetExchange(value = "/api/v1/password-history"
                        + SimappeConstant.PATH_READ, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<?>> readPasswordHistories();

        /**
         * Recupera los historiales de contraseñas por ID de usuario.
         *
         * @param userId el ID del usuario
         * @return la lista de historiales de contraseñas para el usuario
         */
        @GetExchange(value = "/api/v1/password-history/by-user", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<?>> readPasswordHistoriesByUser(@RequestParam Long userId);

        /**
         * Recupera los detalles de un historial de contraseña por su ID.
         *
         * @param id el ID del historial de contraseña
         * @return los detalles del historial de contraseña
         */
        @GetExchange(value = "/api/v1/password-history"
                        + SimappeConstant.PATH_GET, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<?> getPasswordHistory(@RequestParam Long id);

        /**
         * Crea un nuevo registro de historial de contraseña.
         *
         * @param dto el DTO del historial de contraseña (Object genérico)
         * @return el historial de contraseña creado
         */
        @PostExchange(value = "/api/v1/password-history"
                        + SimappeConstant.PATH_CREATE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<?> createPasswordHistory(@Valid @RequestBody Object dto);

        /**
         * Actualiza un registro de historial de contraseña existente.
         *
         * @param dto el DTO del historial de contraseña con los cambios
         * @param id  el ID del historial de contraseña a actualizar
         * @return el historial de contraseña actualizado
         */
        @PutExchange(value = "/api/v1/password-history"
                        + SimappeConstant.PATH_UPDATE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<?> updatePasswordHistory(@Valid @RequestBody Object dto, @RequestParam Long id);

        /**
         * Elimina un registro de historial de contraseña por su ID.
         *
         * @param id el ID del historial de contraseña a eliminar
         * @return una respuesta vacía indicando el resultado de la operación
         */
        @DeleteExchange(value = "/api/v1/password-history"
                        + SimappeConstant.PATH_DELETE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<Void> deletePasswordHistory(@RequestParam Long id);

        /**
         * Verifica si una contraseña ha sido utilizada anteriormente.
         *
         * @param userId   el ID del usuario
         * @param password la contraseña a verificar
         * @return true si la contraseña fue utilizada anteriormente
         */
        @PostExchange(value = "/api/v1/password-history/check-reuse", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<Boolean> checkPasswordReuse(@RequestParam Long userId, @RequestBody String password);

        /**
         * Recupera una lista paginada de historiales de contraseñas.
         *
         * @param query los parámetros de consulta
         * @return una página de historiales de contraseñas
         */
        @PostExchange(value = "/api/v1/password-history/page", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<PageDto<?>> getPasswordHistoryPage(@RequestBody SimappeRequestQuery query);

        // ========== GESTIÓN DE TOKENS PERPETUOS (PERPETUAL TOKEN MANAGEMENT)
        // ==========

        /**
         * Recupera los detalles de un token perpetuo por su ID.
         *
         * @param id el ID del token perpetuo
         * @return los detalles del token perpetuo
         */
        @GetExchange(value = "/api/v1/perpetual-token"
                        + SimappeConstant.PATH_GET, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<?> getPerpetualToken(@RequestParam Long id);

        /**
         * Crea un nuevo registro de token perpetuo.
         *
         * @param dto el DTO del token perpetuo (Object genérico)
         * @return el token perpetuo creado
         */
        @PostExchange(value = "/api/v1/perpetual-token"
                        + SimappeConstant.PATH_CREATE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<?> createPerpetualToken(@Valid @RequestBody Object dto);

        /**
         * Actualiza un registro de token perpetuo existente.
         *
         * @param dto el DTO del token con los cambios
         * @param id  el ID del token perpetuo a actualizar
         * @return el token perpetuo actualizado
         */
        @PutExchange(value = "/api/v1/perpetual-token"
                        + SimappeConstant.PATH_UPDATE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<?> updatePerpetualToken(@Valid @RequestBody Object dto, @RequestParam Long id);

        /**
         * Elimina un registro de token perpetuo por su ID.
         *
         * @param id el ID del token perpetuo a eliminar
         * @return una respuesta vacía indicando el resultado de la operación
         */
        @DeleteExchange(value = "/api/v1/perpetual-token"
                        + SimappeConstant.PATH_DELETE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<Void> deletePerpetualToken(@RequestParam Long id);

        /**
         * Revoca un token perpetuo.
         *
         * @param id el ID del token perpetuo
         * @return el token perpetuo revocado
         */
        @PostExchange(value = "/api/v1/perpetual-token/revoke", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<?> revokePerpetualToken(@RequestParam Long id);

        /**
         * Recupera los tokens perpetuos por ID de cliente.
         *
         * @param clientId el ID del cliente
         * @return la lista de tokens perpetuos para el cliente
         */
        @GetExchange(value = "/api/v1/perpetual-token/by-client", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<?>> getPerpetualTokensByClient(@RequestParam String clientId);

        /**
         * Recupera los tokens perpetuos por contexto.
         *
         * @param context el contexto
         * @return la lista de tokens perpetuos para el contexto
         */
        @GetExchange(value = "/api/v1/perpetual-token/by-context", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<?>> getPerpetualTokensByContext(@RequestParam String context);

        // ========== GESTIÓN DE REGIONES (REGION MANAGEMENT) ==========

        /**
         * Verifica la disponibilidad del servicio de regiones.
         *
         * @return la respuesta con el estado de disponibilidad del servicio
         */
        @GetExchange(value = "/api/v1/region" + SimappeConstant.PATH_INDEX, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<ServiceResponseAvailable> regionIndex();

        /**
         * Recupera todos los registros de regiones.
         *
         * @return la lista de registros de regiones
         */
        @GetExchange(value = "/api/v1/region" + SimappeConstant.PATH_READ, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<?>> readRegions();

        /**
         * Recupera las regiones por ID de país.
         *
         * @param countryId el ID del país
         * @return la lista de regiones para el país
         */
        @GetExchange(value = "/api/v1/region/read-country", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<?>> readRegionsByCountry(@RequestParam Long countryId);

        /**
         * Recupera los detalles de una región por su ID.
         *
         * @param id el ID de la región
         * @return los detalles de la región
         */
        @GetExchange(value = "/api/v1/region" + SimappeConstant.PATH_GET, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<?> getRegion(@RequestParam Long id);

        /**
         * Recupera los detalles de una región por su nombre.
         *
         * @param name el nombre de la región
         * @return los detalles de la región
         */
        @GetExchange(value = "/api/v1/region/get-name", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<?> getRegionByName(@RequestParam String name);

        /**
         * Recupera los detalles de una región por su código de región.
         *
         * @param regionCode el código de la región
         * @return los detalles de la región
         */
        @GetExchange(value = "/api/v1/region/get-region-code", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<?> getRegionByCode(@RequestParam String regionCode);

        /**
         * Crea un nuevo registro de región.
         *
         * @param dto el DTO de la región (Object genérico)
         * @return la región creada
         */
        @PostExchange(value = "/api/v1/region" + SimappeConstant.PATH_CREATE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<?> createRegion(@Valid @RequestBody Object dto);

        /**
         * Actualiza un registro de región existente.
         *
         * @param dto el DTO de la región con los cambios
         * @param id  el ID de la región a actualizar
         * @return la región actualizada
         */
        @PutExchange(value = "/api/v1/region" + SimappeConstant.PATH_UPDATE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<?> updateRegion(@Valid @RequestBody Object dto, @RequestParam Long id);

        /**
         * Elimina un registro de región por su ID.
         *
         * @param id el ID de la región a eliminar
         * @return una respuesta vacía indicando el resultado de la operación
         */
        @DeleteExchange(value = "/api/v1/region"
                        + SimappeConstant.PATH_DELETE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<Void> deleteRegion(@RequestParam Long id);

        /**
         * Recupera una lista paginada de regiones.
         *
         * @param query los parámetros de consulta
         * @return una página de regiones
         */
        @PostExchange(value = "/api/v1/region" + SimappeConstant.PATH_PAGE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<PageDto<?>> getRegionPage(@RequestBody SimappeRequestQuery query);

        // ========== GESTIÓN DE ROLES (ROLE MANAGEMENT) ==========

        /**
         * Verifica la disponibilidad del servicio de roles.
         *
         * @return la respuesta con el estado de disponibilidad del servicio
         */
        @GetExchange(value = "/api/v1/role" + SimappeConstant.PATH_INDEX, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<ServiceResponseAvailable> roleIndex();

        /**
         * Recupera todos los registros de roles.
         *
         * @return la lista de registros de roles
         */
        @GetExchange(value = "/api/v1/role" + SimappeConstant.PATH_READ, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<?>> readRoles();

        /**
         * Recupera los detalles de un rol por su ID.
         *
         * @param id el ID del rol
         * @return los detalles del rol
         */
        @GetExchange(value = "/api/v1/role" + SimappeConstant.PATH_GET, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<?> getRole(@RequestParam Long id);

        /**
         * Recupera los detalles de un rol por su nombre.
         *
         * @param name el nombre del rol
         * @return los detalles del rol
         */
        @GetExchange(value = "/api/v1/role/get-name", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<?> getRoleByName(@RequestParam String name);

        /**
         * Crea un nuevo registro de rol.
         *
         * @param dto el DTO del rol (Object genérico)
         * @return el rol creado
         */
        @PostExchange(value = "/api/v1/role" + SimappeConstant.PATH_CREATE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<?> createRole(@Valid @RequestBody Object dto);

        /**
         * Actualiza un registro de rol existente.
         *
         * @param dto el DTO del rol con los cambios
         * @param id  el ID del rol a actualizar
         * @return el rol actualizado
         */
        @PutExchange(value = "/api/v1/role" + SimappeConstant.PATH_UPDATE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<?> updateRole(@Valid @RequestBody Object dto, @RequestParam Long id);

        /**
         * Agrega una opción al rol.
         *
         * @param data los datos de mapeo de la opción
         * @return el rol actualizado
         */
        @PutExchange(value = "/api/v1/role/add-option", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<?> addOptionToRole(@RequestBody Map<String, Object> data);

        /**
         * Elimina una opción del rol.
         *
         * @param data los datos de mapeo de la opción
         * @return respuesta vacía
         */
        @DeleteExchange(value = "/api/v1/role/delete-option", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<Void> deleteOptionFromRole(@RequestBody Map<String, Object> data);

        /**
         * Elimina un registro de rol por su ID.
         *
         * @param id el ID del rol a eliminar
         * @return una respuesta vacía indicando el resultado de la operación
         */
        @DeleteExchange(value = "/api/v1/role" + SimappeConstant.PATH_DELETE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<Void> deleteRole(@RequestParam Long id);

        /**
         * Recupera una lista paginada de roles.
         *
         * @param query los parámetros de consulta
         * @return una página de roles
         */
        @PostExchange(value = "/api/v1/role" + SimappeConstant.PATH_PAGE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<PageDto<?>> getRolePage(@RequestBody SimappeRequestQuery query);

        // ========== GESTIÓN DE PERMISOS POR ROL (ROLE PERMISSION MANAGEMENT)
        // ==========

        /**
         * Verifica la disponibilidad del servicio de permisos por rol.
         *
         * @return la respuesta con el estado de disponibilidad del servicio
         */
        @GetExchange(value = "/api/v1/role-permission"
                        + SimappeConstant.PATH_INDEX, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<ServiceResponseAvailable> rolePermissionIndex();

        /**
         * Recupera todos los registros de permisos por rol.
         *
         * @return la lista de registros de permisos por rol
         */
        @GetExchange(value = "/api/v1/role-permission"
                        + SimappeConstant.PATH_READ, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<?>> readRolePermissions();

        /**
         * Recupera los permisos por ID de rol.
         *
         * @param roleId el ID del rol
         * @return la lista de permisos para el rol
         */
        @GetExchange(value = "/api/v1/role-permission/by-role", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<?>> readRolePermissionsByRole(@RequestParam Long roleId);

        /**
         * Recupera los detalles de un permiso por rol por su ID.
         *
         * @param id el ID del permiso por rol
         * @return los detalles del permiso por rol
         */
        @GetExchange(value = "/api/v1/role-permission"
                        + SimappeConstant.PATH_GET, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<?> getRolePermission(@RequestParam Long id);

        /**
         * Crea un nuevo registro de permiso por rol.
         *
         * @param dto el DTO del permiso por rol (Object genérico)
         * @return el permiso por rol creado
         */
        @PostExchange(value = "/api/v1/role-permission"
                        + SimappeConstant.PATH_CREATE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<?> createRolePermission(@Valid @RequestBody Object dto);

        /**
         * Actualiza un registro de permiso por rol existente.
         *
         * @param dto el DTO del permiso por rol con los cambios
         * @param id  el ID del permiso por rol a actualizar
         * @return el permiso por rol actualizado
         */
        @PutExchange(value = "/api/v1/role-permission"
                        + SimappeConstant.PATH_UPDATE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<?> updateRolePermission(@Valid @RequestBody Object dto, @RequestParam Long id);

        /**
         * Elimina un registro de permiso por rol por su ID.
         *
         * @param id el ID del permiso por rol a eliminar
         * @return una respuesta vacía indicando el resultado de la operación
         */
        @DeleteExchange(value = "/api/v1/role-permission"
                        + SimappeConstant.PATH_DELETE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<Void> deleteRolePermission(@RequestParam Long id);

        /**
         * Verifica si un rol tiene un permiso específico.
         *
         * @param roleId     el ID del rol
         * @param permission el permiso a verificar
         * @return true si el rol tiene el permiso
         */
        @PostExchange(value = "/api/v1/role-permission/check", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<Boolean> checkPermission(@RequestParam Long roleId, @RequestParam String permission);

        /**
         * Asigna permisos de forma masiva a un rol.
         *
         * @param data los datos de asignación de permisos
         * @return los permisos por rol actualizados
         */
        @PostExchange(value = "/api/v1/role-permission/bulk-assign", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<?> bulkAssignPermissions(@RequestBody Map<String, Object> data);

        /**
         * Recupera una lista paginada de permisos por rol.
         *
         * @param query los parámetros de consulta
         * @return una página de permisos por rol
         */
        @PostExchange(value = "/api/v1/role-permission/page", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<PageDto<?>> getRolePermissionPage(@RequestBody SimappeRequestQuery query);

        // ========== GESTIÓN DE TAREAS PROGRAMADAS (SCHEDULED JOB MANAGEMENT)
        // ==========

        /**
         * Verifica la disponibilidad del servicio de tareas programadas.
         *
         * @return la respuesta con el estado de disponibilidad del servicio
         */
        @GetExchange(value = "/api/v1/scheduled-job"
                        + SimappeConstant.PATH_INDEX, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<ServiceResponseAvailable> scheduledJobIndex();

        /**
         * Recupera todos los registros de tareas programadas.
         *
         * @return la lista de registros de tareas programadas
         */
        @GetExchange(value = "/api/v1/scheduled-job"
                        + SimappeConstant.PATH_READ, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<?>> readScheduledJobs();

        /**
         * Recupera las tareas programadas habilitadas.
         *
         * @return la lista de tareas programadas habilitadas
         */
        @GetExchange(value = "/api/v1/scheduled-job/read-enabled", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<?>> readEnabledScheduledJobs();

        /**
         * Recupera las tareas programadas por estado.
         *
         * @param status el estado de la tarea
         * @return la lista de tareas programadas para el estado
         */
        @GetExchange(value = "/api/v1/scheduled-job/read-by-status", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<?>> readScheduledJobsByStatus(@RequestParam String status);

        /**
         * Recupera las tareas programadas pendientes de ejecución.
         *
         * @return la lista de tareas programadas pendientes
         */
        @GetExchange(value = "/api/v1/scheduled-job/read-due", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<?>> readDueScheduledJobs();

        /**
         * Recupera los detalles de una tarea programada por su ID.
         *
         * @param id el ID de la tarea programada
         * @return los detalles de la tarea programada
         */
        @GetExchange(value = "/api/v1/scheduled-job"
                        + SimappeConstant.PATH_GET, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<?> getScheduledJob(@RequestParam Long id);

        /**
         * Recupera los detalles de una tarea programada por su nombre.
         *
         * @param name el nombre de la tarea programada
         * @return los detalles de la tarea programada
         */
        @GetExchange(value = "/api/v1/scheduled-job/get-by-name", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<?> getScheduledJobByName(@RequestParam String name);

        /**
         * Crea un nuevo registro de tarea programada.
         *
         * @param dto el DTO de la tarea programada (Object genérico)
         * @return la tarea programada creada
         */
        @PostExchange(value = "/api/v1/scheduled-job"
                        + SimappeConstant.PATH_CREATE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<?> createScheduledJob(@Valid @RequestBody Object dto);

        /**
         * Habilita una tarea programada.
         *
         * @param id el ID de la tarea programada
         * @return la tarea programada actualizada
         */
        @PutExchange(value = "/api/v1/scheduled-job/enable", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<?> enableScheduledJob(@RequestParam Long id);

        /**
         * Deshabilita una tarea programada.
         *
         * @param id el ID de la tarea programada
         * @return la tarea programada actualizada
         */
        @PutExchange(value = "/api/v1/scheduled-job/disable", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<?> disableScheduledJob(@RequestParam Long id);

        /**
         * Actualiza un registro de tarea programada existente.
         *
         * @param dto el DTO de la tarea programada con los cambios
         * @param id  el ID de la tarea programada a actualizar
         * @return la tarea programada actualizada
         */
        @PutExchange(value = "/api/v1/scheduled-job"
                        + SimappeConstant.PATH_UPDATE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<?> updateScheduledJob(@Valid @RequestBody Object dto, @RequestParam Long id);

        /**
         * Elimina un registro de tarea programada por su ID.
         *
         * @param id el ID de la tarea programada a eliminar
         * @return una respuesta vacía indicando el resultado de la operación
         */
        @DeleteExchange(value = "/api/v1/scheduled-job"
                        + SimappeConstant.PATH_DELETE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<Void> deleteScheduledJob(@RequestParam Long id);

        /**
         * Recupera una lista paginada de tareas programadas.
         *
         * @param query los parámetros de consulta
         * @return una página de tareas programadas
         */
        @PostExchange(value = "/api/v1/scheduled-job"
                        + SimappeConstant.PATH_PAGE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<PageDto<?>> getScheduledJobPage(@RequestBody SimappeRequestQuery query);

        // ========== GESTIÓN DE AUDITORÍA DE SEGURIDAD (SECURITY AUDIT MANAGEMENT)
        // ==========

        /**
         * Verifica la disponibilidad del servicio de auditoría de seguridad.
         *
         * @return la respuesta con el estado de disponibilidad del servicio
         */
        @GetExchange(value = "/api/v1/security-audit"
                        + SimappeConstant.PATH_INDEX, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<ServiceResponseAvailable> securityAuditIndex();

        /**
         * Recupera todos los registros de auditoría de seguridad.
         *
         * @return la lista de registros de auditoría de seguridad
         */
        @GetExchange(value = "/api/v1/security-audit"
                        + SimappeConstant.PATH_READ, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<?>> readSecurityAudits();

        /**
         * Recupera los registros de auditoría de seguridad por ID de usuario.
         *
         * @param userId el ID del usuario
         * @return la lista de auditorías de seguridad para el usuario
         */
        @GetExchange(value = "/api/v1/security-audit/by-user", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<?>> readSecurityAuditsByUser(@RequestParam Long userId);

        /**
         * Recupera los registros de auditoría de seguridad por tipo de evento.
         *
         * @param eventType el tipo de evento
         * @return la lista de auditorías de seguridad para el tipo de evento
         */
        @GetExchange(value = "/api/v1/security-audit/by-event-type", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<?>> readSecurityAuditsByEventType(@RequestParam String eventType);

        /**
         * Recupera eventos críticos desde una fecha especificada.
         *
         * @param since la fecha desde (formato ISO)
         * @return la lista de eventos críticos
         */
        @GetExchange(value = "/api/v1/security-audit/critical-events", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<?>> readCriticalEvents(@RequestParam String since);

        /**
         * Recupera registros de auditoría de seguridad por severidad.
         *
         * @param severity el nivel de severidad
         * @return la lista de auditorías de seguridad para la severidad
         */
        @GetExchange(value = "/api/v1/security-audit/by-severity", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<?>> readSecurityAuditsBySeverity(@RequestParam String severity);

        /**
         * Recupera los detalles de una auditoría de seguridad por su ID.
         *
         * @param id el ID de la auditoría de seguridad
         * @return los detalles de la auditoría de seguridad
         */
        @GetExchange(value = "/api/v1/security-audit"
                        + SimappeConstant.PATH_GET, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<?> getSecurityAudit(@RequestParam Long id);

        /**
         * Crea un nuevo registro de auditoría de seguridad.
         *
         * @param dto el DTO de la auditoría de seguridad (Object genérico)
         * @return la auditoría de seguridad creada
         */
        @PostExchange(value = "/api/v1/security-audit"
                        + SimappeConstant.PATH_CREATE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<?> createSecurityAudit(@Valid @RequestBody Object dto);

        /**
         * Recupera una lista paginada de auditorías de seguridad.
         *
         * @param query los parámetros de consulta
         * @return una página de auditorías de seguridad
         */
        @PostExchange(value = "/api/v1/security-audit/page", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<PageDto<?>> getSecurityAuditPage(@RequestBody SimappeRequestQuery query);

        // ========== GESTIÓN DE POLÍTICAS DE SEGURIDAD (SECURITY POLICY MANAGEMENT)
        // ==========

        /**
         * Verifica la disponibilidad del servicio de políticas de seguridad.
         *
         * @return la respuesta con el estado de disponibilidad del servicio
         */
        @GetExchange(value = "/api/v1/security-policy"
                        + SimappeConstant.PATH_INDEX, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<ServiceResponseAvailable> securityPolicyIndex();

        /**
         * Recupera todos los registros de políticas de seguridad.
         *
         * @return la lista de registros de políticas de seguridad
         */
        @GetExchange(value = "/api/v1/security-policy"
                        + SimappeConstant.PATH_READ, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<?>> readSecurityPolicies();

        /**
         * Recupera la política de seguridad por ID de empresa.
         *
         * @param companyId el ID de la empresa
         * @return la política de seguridad para la empresa
         */
        @GetExchange(value = "/api/v1/security-policy/by-company", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<?> getSecurityPolicyByCompany(@RequestParam Long companyId);

        /**
         * Recupera la política de seguridad global activa.
         *
         * @return la política de seguridad global activa
         */
        @GetExchange(value = "/api/v1/security-policy/global-active", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<?> getGlobalActiveSecurityPolicy();

        /**
         * Recupera los detalles de una política de seguridad por su ID.
         *
         * @param id el ID de la política de seguridad
         * @return los detalles de la política de seguridad
         */
        @GetExchange(value = "/api/v1/security-policy"
                        + SimappeConstant.PATH_GET, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<?> getSecurityPolicy(@RequestParam Long id);

        /**
         * Crea un nuevo registro de política de seguridad.
         *
         * @param dto el DTO de la política de seguridad (Object genérico)
         * @return la política de seguridad creada
         */
        @PostExchange(value = "/api/v1/security-policy"
                        + SimappeConstant.PATH_CREATE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<?> createSecurityPolicy(@Valid @RequestBody Object dto);

        /**
         * Actualiza un registro de política de seguridad existente.
         *
         * @param dto el DTO de la política de seguridad con los cambios
         * @param id  el ID de la política de seguridad a actualizar
         * @return la política de seguridad actualizada
         */
        @PutExchange(value = "/api/v1/security-policy"
                        + SimappeConstant.PATH_UPDATE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<?> updateSecurityPolicy(@Valid @RequestBody Object dto, @RequestParam Long id);

        /**
         * Elimina un registro de política de seguridad por su ID.
         *
         * @param id el ID de la política de seguridad a eliminar
         * @return una respuesta vacía indicando el resultado de la operación
         */
        @DeleteExchange(value = "/api/v1/security-policy"
                        + SimappeConstant.PATH_DELETE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<Void> deleteSecurityPolicy(@RequestParam Long id);

        /**
         * Recupera una lista paginada de políticas de seguridad.
         *
         * @param query los parámetros de consulta
         * @return una página de políticas de seguridad
         */
        @PostExchange(value = "/api/v1/security-policy/page", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<PageDto<?>> getSecurityPolicyPage(@RequestBody SimappeRequestQuery query);

        // ========== GESTIÓN DE DISPONIBILIDAD DE SERVICIOS (SERVICE AVAILABILITY
        // MANAGEMENT) ==========

        /**
         * Verifica la disponibilidad del servicio de disponibilidad de servicios.
         *
         * @return la respuesta con el estado de disponibilidad del servicio
         */
        @GetExchange(value = "/api/v1/service-availability"
                        + SimappeConstant.PATH_INDEX, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<ServiceResponseAvailable> serviceAvailabilityIndex();

        /**
         * Recupera todos los registros de disponibilidad de servicios.
         *
         * @return la lista de registros de disponibilidad de servicios
         */
        @GetExchange(value = "/api/v1/service-availability"
                        + SimappeConstant.PATH_READ, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<?>> readServiceAvailabilities();

        /**
         * Recupera las disponibilidades de servicios activas.
         *
         * @return la lista de disponibilidades de servicios activas
         */
        @GetExchange(value = "/api/v1/service-availability/read-active", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<?>> readActiveServiceAvailabilities();

        /**
         * Recupera la disponibilidad de un servicio por su nombre.
         *
         * @param serviceName el nombre del servicio
         * @return la disponibilidad del servicio
         */
        @GetExchange(value = "/api/v1/service-availability/read-by-service", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<?> getServiceAvailabilityByService(@RequestParam String serviceName);

        /**
         * Recupera las disponibilidades de servicios por ID de cliente.
         *
         * @param customerId el ID del cliente
         * @return la lista de disponibilidades de servicios para el cliente
         */
        @GetExchange(value = "/api/v1/service-availability/read-by-customer", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<?>> readServiceAvailabilitiesByCustomer(@RequestParam Long customerId);

        /**
         * Recupera los detalles de una disponibilidad de servicio por su ID.
         *
         * @param id el ID de la disponibilidad de servicio
         * @return los detalles de la disponibilidad de servicio
         */
        @GetExchange(value = "/api/v1/service-availability"
                        + SimappeConstant.PATH_GET, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<?> getServiceAvailability(@RequestParam Long id);

        /**
         * Crea un nuevo registro de disponibilidad de servicio.
         *
         * @param dto el DTO de la disponibilidad de servicio (Object genérico)
         * @return la disponibilidad de servicio creada
         */
        @PostExchange(value = "/api/v1/service-availability"
                        + SimappeConstant.PATH_CREATE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<?> createServiceAvailability(@Valid @RequestBody Object dto);

        /**
         * Actualiza un registro de disponibilidad de servicio existente.
         *
         * @param dto el DTO de la disponibilidad de servicio con los cambios
         * @param id  el ID de la disponibilidad de servicio a actualizar
         * @return la disponibilidad de servicio actualizada
         */
        @PutExchange(value = "/api/v1/service-availability"
                        + SimappeConstant.PATH_UPDATE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<?> updateServiceAvailability(@Valid @RequestBody Object dto, @RequestParam Long id);

        /**
         * Elimina un registro de disponibilidad de servicio por su ID.
         *
         * @param id el ID de la disponibilidad de servicio a eliminar
         * @return una respuesta vacía indicando el resultado de la operación
         */
        @DeleteExchange(value = "/api/v1/service-availability"
                        + SimappeConstant.PATH_DELETE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<Void> deleteServiceAvailability(@RequestParam Long id);

        /**
         * Recupera una lista paginada de disponibilidades de servicios.
         *
         * @param query los parámetros de consulta
         * @return una página de disponibilidades de servicios
         */
        @PostExchange(value = "/api/v1/service-availability"
                        + SimappeConstant.PATH_PAGE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<PageDto<?>> getServiceAvailabilityPage(@RequestBody SimappeRequestQuery query);

        // ========== GESTIÓN DE SESIONES DE USUARIO (USER SESSION MANAGEMENT)
        // ==========

        /**
         * Verifica la disponibilidad del servicio de sesiones de usuario.
         *
         * @return la respuesta con el estado de disponibilidad del servicio
         */
        @GetExchange(value = "/api/v1/user-session"
                        + SimappeConstant.PATH_INDEX, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<ServiceResponseAvailable> userSessionIndex();

        /**
         * Recupera todos los registros de sesiones de usuario.
         *
         * @return la lista de registros de sesiones de usuario
         */
        @GetExchange(value = "/api/v1/user-session"
                        + SimappeConstant.PATH_READ, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<?>> readUserSessions();

        /**
         * Recupera las sesiones de usuario activas por ID de usuario.
         *
         * @param userId el ID del usuario
         * @return la lista de sesiones activas para el usuario
         */
        @GetExchange(value = "/api/v1/user-session/active-by-user", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<?>> readActiveUserSessionsByUser(@RequestParam Long userId);

        /**
         * Recupera el historial de sesiones de usuario por ID de usuario.
         *
         * @param userId el ID del usuario
         * @return la lista del historial de sesiones para el usuario
         */
        @GetExchange(value = "/api/v1/user-session/history-by-user", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<?>> readUserSessionHistoryByUser(@RequestParam Long userId);

        /**
         * Recupera los detalles de una sesión de usuario por su ID.
         *
         * @param id el ID de la sesión de usuario
         * @return los detalles de la sesión de usuario
         */
        @GetExchange(value = "/api/v1/user-session"
                        + SimappeConstant.PATH_GET, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<?> getUserSession(@RequestParam Long id);

        /**
         * Crea un nuevo registro de sesión de usuario.
         *
         * @param dto el DTO de la sesión de usuario (Object genérico)
         * @return la sesión de usuario creada
         */
        @PostExchange(value = "/api/v1/user-session"
                        + SimappeConstant.PATH_CREATE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<?> createUserSession(@Valid @RequestBody Object dto);

        /**
         * Actualiza un registro de sesión de usuario existente.
         *
         * @param dto el DTO de la sesión de usuario con los cambios
         * @param id  el ID de la sesión de usuario a actualizar
         * @return la sesión de usuario actualizada
         */
        @PutExchange(value = "/api/v1/user-session"
                        + SimappeConstant.PATH_UPDATE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<?> updateUserSession(@Valid @RequestBody Object dto, @RequestParam Long id);

        /**
         * Elimina un registro de sesión de usuario por su ID.
         *
         * @param id el ID de la sesión de usuario a eliminar
         * @return una respuesta vacía indicando el resultado de la operación
         */
        @DeleteExchange(value = "/api/v1/user-session"
                        + SimappeConstant.PATH_DELETE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<Void> deleteUserSession(@RequestParam Long id);

        /**
         * Invalida una sesión de usuario.
         *
         * @param id el ID de la sesión de usuario
         * @return la sesión de usuario invalidada
         */
        @PutExchange(value = "/api/v1/user-session/invalidate", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<?> invalidateUserSession(@RequestParam Long id);

        /**
         * Invalida todas las sesiones de un usuario.
         *
         * @param userId el ID del usuario
         * @return el resultado de la invalidación
         */
        @PutExchange(value = "/api/v1/user-session/invalidate-all", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<?> invalidateAllUserSessions(@RequestParam Long userId);

        /**
         * Recupera una lista paginada de sesiones de usuario.
         *
         * @param query los parámetros de consulta
         * @return una página de sesiones de usuario
         */
        @PostExchange(value = "/api/v1/user-session/page", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<PageDto<?>> getUserSessionPage(@RequestBody SimappeRequestQuery query);

        // ========== GESTIÓN DE WABOXAPP (WABOXAPP MANAGEMENT) ==========

        /**
         * Verifica la disponibilidad del servicio waboxapp.
         *
         * @return la respuesta con el estado de disponibilidad del servicio
         */
        @GetExchange(value = "/api/v1/waboxapp" + SimappeConstant.PATH_INDEX, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<ServiceResponseAvailable> waboxappIndex();

        /**
         * Recupera todos los registros de waboxapp.
         *
         * @return la lista de registros de waboxapp
         */
        @GetExchange(value = "/api/v1/waboxapp" + SimappeConstant.PATH_READ, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<?>> readWaboxapps();

        /**
         * Recupera los registros de waboxapp por ID de cliente.
         *
         * @param clientId el ID del cliente
         * @return la lista de registros de waboxapp para el cliente
         */
        @GetExchange(value = "/api/v1/waboxapp/read-by-client", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<?>> readWaboxappsByClient(@RequestParam String clientId);

        /**
         * Recupera los registros de waboxapp activos por ID de cliente.
         *
         * @param clientId el ID del cliente
         * @return la lista de registros de waboxapp activos para el cliente
         */
        @GetExchange(value = "/api/v1/waboxapp/read-active-by-client", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<?>> readActiveWaboxappsByClient(@RequestParam String clientId);

        /**
         * Recupera los detalles de un registro waboxapp por su ID.
         *
         * @param id el ID del registro waboxapp
         * @return los detalles del registro waboxapp
         */
        @GetExchange(value = "/api/v1/waboxapp" + SimappeConstant.PATH_GET, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<?> getWaboxapp(@RequestParam Long id);

        /**
         * Crea un nuevo registro waboxapp.
         *
         * @param dto el DTO de waboxapp (Object genérico)
         * @return el registro waboxapp creado
         */
        @PostExchange(value = "/api/v1/waboxapp"
                        + SimappeConstant.PATH_CREATE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<?> createWaboxapp(@Valid @RequestBody Object dto);

        /**
         * Actualiza un registro waboxapp existente.
         *
         * @param dto el DTO de waboxapp con los cambios
         * @param id  el ID del registro waboxapp a actualizar
         * @return el registro waboxapp actualizado
         */
        @PutExchange(value = "/api/v1/waboxapp"
                        + SimappeConstant.PATH_UPDATE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<?> updateWaboxapp(@Valid @RequestBody Object dto, @RequestParam Long id);

        /**
         * Elimina un registro waboxapp por su ID.
         *
         * @param id el ID del registro waboxapp a eliminar
         * @return una respuesta vacía indicando el resultado de la operación
         */
        @DeleteExchange(value = "/api/v1/waboxapp"
                        + SimappeConstant.PATH_DELETE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<Void> deleteWaboxapp(@RequestParam Long id);

        /**
         * Envía un mensaje de chat a través de waboxapp.
         *
         * @param data los datos del mensaje
         * @return el resultado del envío
         */
        @PostExchange(value = "/api/v1/waboxapp/send-chat", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<?> sendWaboxappChat(@RequestBody Map<String, Object> data);

        /**
         * Envía una imagen a través de waboxapp.
         *
         * @param data los datos de la imagen
         * @return el resultado del envío
         */
        @PostExchange(value = "/api/v1/waboxapp/send-image", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<?> sendWaboxappImage(@RequestBody Map<String, Object> data);

        /**
         * Envía un enlace a través de waboxapp.
         *
         * @param data los datos del enlace
         * @return el resultado del envío
         */
        @PostExchange(value = "/api/v1/waboxapp/send-link", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<?> sendWaboxappLink(@RequestBody Map<String, Object> data);

        /**
         * Envía un archivo multimedia a través de waboxapp.
         *
         * @param data los datos multimedia
         * @return el resultado del envío
         */
        @PostExchange(value = "/api/v1/waboxapp/send-media", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<?> sendWaboxappMedia(@RequestBody Map<String, Object> data);

        /**
         * Verifica el estado de un cliente waboxapp.
         *
         * @param clientId el ID del cliente
         * @return el estado del cliente
         */
        @GetExchange(value = "/api/v1/waboxapp/check-status", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<?> checkWaboxappStatus(@RequestParam String clientId);

        /**
         * Maneja las devoluciones de llamada del webhook de waboxapp.
         *
         * @param payload el cuerpo del webhook
         * @return el resultado del procesamiento del webhook
         */
        @PostExchange(value = "/api/v1/waboxapp/webhook", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<?> handleWaboxappWebhook(@RequestBody Map<String, Object> payload);

        // ======================================================================
        // GESTIÓN DE USUARIOS (USER MANAGEMENT)
        // ======================================================================

        /**
         * Recupera un usuario por su ID.
         *
         * @param id el ID del usuario
         * @return el usuario encontrado
         */
        @Deprecated(since = "2025-12-25", forRemoval = true)
        @GetExchange(value = "/api/v1/user/get", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<UserDto> getUserById(@RequestParam Long id);

        /**
         * Recupera un usuario por su nombre de usuario.
         *
         * @param username el nombre de usuario
         * @return el usuario encontrado
         */
        @Deprecated(since = "2025-12-25", forRemoval = true)
        @GetExchange(value = "/api/v1/user/get-username", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<UserDto> getUserByUsername(@RequestParam String username);

        /**
         * Recupera un usuario por su correo electrónico.
         *
         * @param email el correo electrónico del usuario
         * @return el usuario encontrado
         */
        @Deprecated(since = "2025-12-25", forRemoval = true)
        @GetExchange(value = "/api/v1/user/get-useremail", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<UserDto> getUserByEmail(@RequestParam String email);

        /**
         * Agrega un rol a un usuario.
         *
         * @param roleData DTO con los IDs de usuario y rol
         * @return el rol agregado
         */
        @Deprecated(since = "2025-12-25", forRemoval = true)
        @PutExchange(value = "/api/v1/user/add-role", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<RoleDto> addRoleToUser(@RequestBody TransferDto2<Long, Long> roleData);

        /**
         * Elimina un rol de un usuario.
         *
         * @param roleData DTO con los IDs de usuario y rol
         * @return respuesta vacía indicando el éxito de la operación
         */
        @Deprecated(since = "2025-12-25", forRemoval = true)
        @DeleteExchange(value = "/api/v1/user/delete-role", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<Void> deleteRoleFromUser(@RequestBody TransferDto2<Long, Long> roleData);

        /**
         * Recupera una lista paginada de usuarios.
         *
         * @param requestQuery los parámetros de consulta
         * @return una página de usuarios
         * @throws SimappeException si ocurre un error en el microservicio
         */
        @Deprecated(since = "2025-12-25", forRemoval = true)
        @PostExchange(value = "/api/v1/user/page", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<PageDto<UserDto>> getUsersPage(@RequestBody SimappeRequestQuery requestQuery)
                        throws SimappeException;

        // ======================================================================
        // GESTIÓN DE DEPARTAMENTOS/ESTADOS (STATE MANAGEMENT)
        // ======================================================================

        /**
         * Verifica la disponibilidad del servicio de departamentos/estados.
         *
         * @return la respuesta con el estado de disponibilidad del servicio
         */
        @GetExchange(value = "/api/v1/state", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<ServiceResponseAvailable> stateIndex();

        /**
         * Recupera todos los departamentos/estados.
         *
         * @return la lista de todos los departamentos
         */
        @GetExchange(value = "/api/v1/state/read", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<StateDto>> readStates();

        /**
         * Recupera los departamentos por ID de país.
         *
         * @param countryId el ID del país
         * @return la lista de departamentos para el país
         */
        @GetExchange(value = "/api/v1/state/read-country", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<StateDto>> readStatesByCountry(@RequestParam Long countryId);

        /**
         * Recupera los departamentos por ID de región.
         *
         * @param regionId el ID de la región
         * @return la lista de departamentos para la región
         */
        @GetExchange(value = "/api/v1/state/read-region", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<StateDto>> readStatesByRegion(@RequestParam Long regionId);

        /**
         * Recupera un departamento por su ID.
         *
         * @param id el ID del departamento
         * @return el departamento encontrado
         */
        @GetExchange(value = "/api/v1/state/get", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<StateDto> getStateById(@RequestParam Long id);

        /**
         * Recupera un departamento por su nombre.
         *
         * @param name el nombre del departamento
         * @return el departamento encontrado
         */
        @GetExchange(value = "/api/v1/state/get-name", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<StateDto> getStateByName(@RequestParam String name);

        /**
         * Recupera un departamento por su código.
         *
         * @param stateCode el código del departamento
         * @return el departamento encontrado
         */
        @GetExchange(value = "/api/v1/state/get-state-code", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<StateDto> getStateByCode(@RequestParam String stateCode);

        /**
         * Crea un nuevo departamento.
         *
         * @param dto el DTO del departamento
         * @return el departamento creado
         * @throws SimappeException si ocurre un error de validación o duplicidad
         */
        @PostExchange(value = "/api/v1/state/create", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<StateDto> createState(@Valid @RequestBody StateDto dto) throws SimappeException;

        /**
         * Actualiza un departamento existente.
         *
         * @param dto el DTO del departamento con los cambios
         * @param id  el ID del departamento a actualizar
         * @return el departamento actualizado
         */
        @PutExchange(value = "/api/v1/state/update", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<StateDto> updateState(@Valid @RequestBody StateDto dto, @RequestParam Long id);

        /**
         * Elimina un departamento por su ID.
         *
         * @param id el ID del departamento a eliminar
         * @return respuesta vacía indicando el éxito de la operación
         */
        @DeleteExchange(value = "/api/v1/state/delete", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<Void> deleteState(@RequestParam Long id);

        /**
         * Recupera una lista paginada de departamentos.
         *
         * @param requestQuery los parámetros de consulta
         * @return una página de departamentos
         * @throws SimappeException si ocurre un error en el microservicio
         */
        @PostExchange(value = "/api/v1/state/page", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<PageDto<StateDto>> getStatesPage(@RequestBody SimappeRequestQuery requestQuery)
                        throws SimappeException;

        // ======================================================================
        // GESTIÓN DE TAREAS (TODO MANAGEMENT)
        // ======================================================================

        /**
         * Verifica la disponibilidad del servicio de tareas pendientes.
         *
         * @return la respuesta con el estado de disponibilidad del servicio
         */
        @GetExchange(value = "/api/v1/todo", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<ServiceResponseAvailable> todoIndex();

        /**
         * Recupera todas las tareas pendientes.
         *
         * @return la lista de todas las tareas
         */
        @GetExchange(value = "/api/v1/todo/read", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<TodoDTO>> readTodos();

        /**
         * Recupera todas las tareas completadas.
         *
         * @return la lista de tareas completadas
         */
        @GetExchange(value = "/api/v1/todo/read-completed", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<TodoDTO>> readCompletedTodos();

        /**
         * Recupera todas las tareas eliminadas.
         *
         * @return la lista de tareas eliminadas
         */
        @GetExchange(value = "/api/v1/todo/read-deleted", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<TodoDTO>> readDeletedTodos();

        /**
         * Recupera las tareas pendientes de un usuario por su nombre de usuario.
         *
         * @param username el nombre de usuario
         * @return la lista de tareas del usuario
         */
        @GetExchange(value = "/api/v1/todo/read-by-username", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<TodoDTO>> readTodosByUsername(@RequestParam String username);

        /**
         * Recupera las tareas pendientes por nivel de prioridad.
         *
         * @param priority el nivel de prioridad
         * @return la lista de tareas filtradas por prioridad
         */
        @GetExchange(value = "/api/v1/todo/read-by-priority", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<TodoDTO>> readTodosByPriority(@RequestParam Priority priority);

        /**
         * Recupera los detalles de una tarea por su ID.
         *
         * @param id el ID de la tarea
         * @return la tarea encontrada
         */
        @GetExchange(value = "/api/v1/todo/get", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<TodoDTO> getTodoById(@RequestParam String id);

        /**
         * Crea una nueva tarea pendiente.
         *
         * @param todoDTO el DTO de la tarea
         * @return la tarea creada
         * @throws SimappeException si ocurre un error de validación
         */
        @PostExchange(value = "/api/v1/todo/create", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<TodoDTO> createTodo(@Valid @RequestBody TodoDTO todoDTO) throws SimappeException;

        /**
         * Actualiza una tarea pendiente existente.
         *
         * @param todoDTO el DTO de la tarea con los cambios
         * @param id      el ID de la tarea a actualizar
         * @return la tarea actualizada
         */
        @PutExchange(value = "/api/v1/todo/update", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<TodoDTO> updateTodo(@Valid @RequestBody TodoDTO todoDTO, @RequestParam String id);

        /**
         * Elimina una tarea pendiente por su ID.
         *
         * @param id el ID de la tarea a eliminar
         * @return respuesta vacía indicando el éxito de la operación
         */
        @DeleteExchange(value = "/api/v1/todo/delete", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<Void> deleteTodo(@RequestParam String id);

        // ======================================================================
        // PARÁMETROS DEL SISTEMA (SYSTEM PARAMETERS)
        // ======================================================================

        /**
         * Verifica la disponibilidad del servicio de parámetros del sistema.
         *
         * @return la respuesta con el estado de disponibilidad del servicio
         */
        @GetExchange(value = "/api/v1/system-parameters", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<ServiceResponseAvailable> systemParametersIndex();

        /**
         * Recupera todos los parámetros del sistema.
         *
         * @return la lista de todos los parámetros del sistema
         */
        @GetExchange(value = "/api/v1/system-parameters/read", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<SystemParametersDto>> readSystemParameters();

        /**
         * Recupera los parámetros del sistema activos.
         *
         * @return la lista de parámetros del sistema activos
         */
        @GetExchange(value = "/api/v1/system-parameters/read-active", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<SystemParametersDto>> readActiveSystemParameters();

        /**
         * Recupera un parámetro del sistema por su ID.
         *
         * @param id el ID del parámetro del sistema
         * @return el parámetro del sistema encontrado
         */
        @GetExchange(value = "/api/v1/system-parameters/get", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<SystemParametersDto> getSystemParametersById(@RequestParam Long id);

        /**
         * Crea un nuevo parámetro del sistema.
         *
         * @param dto el DTO del parámetro del sistema
         * @return el parámetro del sistema creado
         * @throws SimappeException si ocurre un error en el microservicio
         */
        @PostExchange(value = "/api/v1/system-parameters/create", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<SystemParametersDto> createSystemParameters(@Valid @RequestBody SystemParametersDto dto)
                        throws SimappeException;

        /**
         * Inicializa los parámetros del sistema con valores predeterminados.
         *
         * @return respuesta vacía indicando el éxito de la inicialización
         * @throws SimappeException si ocurre un error durante la inicialización
         */
        @PostExchange(value = "/api/v1/system-parameters/init-parameters", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<Void> initSystemParameters() throws SimappeException;

        /**
         * Actualiza un parámetro del sistema existente.
         *
         * @param dto el DTO del parámetro con los cambios
         * @param id  el ID del parámetro a actualizar
         * @return el parámetro del sistema actualizado
         */
        @PutExchange(value = "/api/v1/system-parameters/update", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<SystemParametersDto> updateSystemParameters(@Valid @RequestBody SystemParametersDto dto,
                        @RequestParam Long id);

        /**
         * Elimina un parámetro del sistema por su ID.
         *
         * @param id el ID del parámetro del sistema a eliminar
         * @return respuesta vacía indicando el éxito de la operación
         */
        @DeleteExchange(value = "/api/v1/system-parameters/delete", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<Void> deleteSystemParameters(@RequestParam Long id);

        /**
         * Recupera una lista paginada de parámetros del sistema.
         *
         * @param requestQuery los parámetros de consulta
         * @return una página de parámetros del sistema
         * @throws SimappeException si ocurre un error en el microservicio
         */
        @PostExchange(value = "/api/v1/system-parameters/page", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<PageDto<SystemParametersDto>> getSystemParametersPage(
                        @RequestBody SimappeRequestQuery requestQuery)
                        throws SimappeException;

        // ======================================================================
        // PARÁMETROS DE PLANTILLA (TEMPLATE PARAMETERS)
        // ======================================================================

        /**
         * Verifica la disponibilidad del servicio de parámetros de plantilla.
         *
         * @return la respuesta con el estado de disponibilidad del servicio
         */
        @GetExchange(value = "/api/v1/template-parameters", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<ServiceResponseAvailable> templateParametersIndex();

        /**
         * Recupera los parámetros de plantilla activos.
         *
         * @return la lista de parámetros de plantilla activos
         */
        @GetExchange(value = "/api/v1/template-parameters/read-active", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<TemplateParametersDto>> readActiveTemplateParameters();

        /**
         * Recupera los parámetros de plantilla por tipo de parámetro.
         *
         * @param parameterType el tipo de parámetro a filtrar
         * @return la lista de parámetros de plantilla para el tipo especificado
         */
        @GetExchange(value = "/api/v1/template-parameters/read-parameter-type", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<TemplateParametersDto>> readTemplateParametersByType(
                        @RequestParam String parameterType);

        /**
         * Recupera un parámetro de plantilla por su ID.
         *
         * @param id el ID del parámetro de plantilla
         * @return el parámetro de plantilla encontrado
         */
        @GetExchange(value = "/api/v1/template-parameters/get", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<TemplateParametersDto> getTemplateParametersById(@RequestParam Long id);

        /**
         * Recupera un parámetro de plantilla por su nombre.
         *
         * @param name el nombre del parámetro de plantilla
         * @return el parámetro de plantilla encontrado
         */
        @GetExchange(value = "/api/v1/template-parameters/get-name", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<TemplateParametersDto> getTemplateParametersByName(@RequestParam String name);

        /**
         * Crea un nuevo parámetro de plantilla.
         *
         * @param dto el DTO del parámetro de plantilla
         * @return el parámetro de plantilla creado
         * @throws SimappeException si ocurre un error en el microservicio
         */
        @PostExchange(value = "/api/v1/template-parameters/create", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<TemplateParametersDto> createTemplateParameters(
                        @Valid @RequestBody TemplateParametersDto dto)
                        throws SimappeException;

        /**
         * Actualiza un parámetro de plantilla existente.
         *
         * @param dto el DTO del parámetro con los cambios
         * @param id  el ID del parámetro a actualizar
         * @return el parámetro de plantilla actualizado
         */
        @PutExchange(value = "/api/v1/template-parameters/update", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<TemplateParametersDto> updateTemplateParameters(
                        @Valid @RequestBody TemplateParametersDto dto,
                        @RequestParam Long id);

        /**
         * Elimina un parámetro de plantilla por su ID.
         *
         * @param id el ID del parámetro de plantilla a eliminar
         * @return respuesta vacía indicando el éxito de la operación
         */
        @DeleteExchange(value = "/api/v1/template-parameters/delete", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<Void> deleteTemplateParameters(@RequestParam Long id);

        /**
         * Recupera una lista paginada de parámetros de plantilla.
         *
         * @param requestQuery los parámetros de consulta
         * @return una página de parámetros de plantilla
         * @throws SimappeException si ocurre un error en el microservicio
         */
        @PostExchange(value = "/api/v1/template-parameters/page", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<PageDto<TemplateParametersDto>> getTemplateParametersPage(
                        @RequestBody SimappeRequestQuery requestQuery)
                        throws SimappeException;

        // ======================================================================
        // GESTIÓN DE MENÚ (MENU MANAGEMENT)
        // ======================================================================

        /**
         * Recupera todos los elementos del menú.
         *
         * @return la lista de todos los elementos del menú
         */
        @GetExchange(value = "/api/v1/menu/read", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<VMenuDto>> readMenuItems();

        /**
         * Recupera un elemento del menú por su ID.
         *
         * @param id el ID del elemento del menú
         * @return el elemento del menú encontrado
         */
        @GetExchange(value = "/api/v1/menu/get", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<VMenuDto> getMenuItemById(@RequestParam Long id);

        /**
         * Recupera los elementos del menú por ID de rol.
         *
         * @param roleId el ID del rol
         * @return la lista de elementos del menú para el rol
         */
        @GetExchange(value = "/api/v1/menu/read-by-role", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<VMenuDto>> getMenuItemsByRole(@RequestParam Long roleId);

        /**
         * Recupera los elementos del menú para múltiples roles.
         *
         * @param roleIds la lista de IDs de roles
         * @return la lista de elementos del menú para los roles especificados
         */
        @PostExchange(value = "/api/v1/menu/read-by-roles", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<VMenuDto>> getMenuItemsByRoles(@RequestBody List<Long> roleIds);

        /**
         * Recupera los elementos del menú por ID de aplicación.
         *
         * @param applicationId el ID de la aplicación
         * @return la lista de elementos del menú para la aplicación
         */
        @GetExchange(value = "/api/v1/menu/read-by-application", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<VMenuDto>> getMenuItemsByApplication(@RequestParam Long applicationId);

        /**
         * Recupera los elementos del menú por ID de módulo.
         *
         * @param moduleId el ID del módulo
         * @return la lista de elementos del menú para el módulo
         */
        @GetExchange(value = "/api/v1/menu/read-by-module", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<VMenuDto>> getMenuItemsByModule(@RequestParam Long moduleId);

        /**
         * Recupera los elementos del menú por ID de grupo de opciones.
         *
         * @param optionGroupId el ID del grupo de opciones
         * @return la lista de elementos del menú para el grupo de opciones
         */
        @GetExchange(value = "/api/v1/menu/read-by-option-group", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<VMenuDto>> getMenuItemsByOptionGroup(@RequestParam Long optionGroupId);

        /**
         * Recupera los elementos del menú por ID de padre.
         *
         * @param parentId el ID del elemento padre
         * @return la lista de elementos del menú hijos
         */
        @GetExchange(value = "/api/v1/menu/read-by-parent", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<VMenuDto>> getMenuItemsByParent(@RequestParam Long parentId);

        /**
         * Busca elementos del menú por un término de búsqueda.
         *
         * @param searchTerm el término de búsqueda
         * @return la lista de elementos del menú que coinciden con la búsqueda
         */
        @GetExchange(value = "/api/v1/menu/search", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<VMenuDto>> searchMenuItems(@RequestParam String searchTerm);

        /**
         * Busca elementos del menú por término de búsqueda y rol.
         *
         * @param searchTerm el término de búsqueda
         * @param roleId     el ID del rol
         * @return la lista de elementos del menú encontrados
         */
        @GetExchange(value = "/api/v1/menu/search-by-roles", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<VMenuDto>> searchMenuItemsByRoles(@RequestParam String searchTerm,
                        @RequestParam Long roleId);

        /**
         * Busca elementos del menú por término de búsqueda y módulo.
         *
         * @param searchTerm el término de búsqueda
         * @param moduleId   el ID del módulo
         * @return la lista de elementos del menú encontrados
         */
        @GetExchange(value = "/api/v1/menu/search-by-module", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<VMenuDto>> searchMenuItemsByModule(@RequestParam String searchTerm,
                        @RequestParam Long moduleId);

        /**
         * Recupera una lista paginada de elementos del menú.
         *
         * @param requestQuery los parámetros de consulta
         * @return una página de elementos del menú
         * @throws SimappeException si ocurre un error en el microservicio
         */
        @PostExchange(value = "/api/v1/menu/page", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<PageDto<VMenuDto>> getMenuItemsPage(@RequestBody SimappeRequestQuery requestQuery)
                        throws SimappeException;

        // ======================================================================
        // GESTIÓN DE SUSCRIPCIONES DE CLIENTES (CUSTOMER SUBSCRIPTION MANAGEMENT)
        // ======================================================================

        /**
         * Verifica la disponibilidad del servicio de suscripciones de clientes.
         *
         * @return la respuesta con el estado de disponibilidad del servicio
         */
        @GetExchange(value = "/api/v1/customer-subscription", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<ServiceResponseAvailable> customerSubscriptionIndex();

        /**
         * Recupera todas las suscripciones de clientes.
         *
         * @return la lista de todas las suscripciones
         */
        @GetExchange(value = "/api/v1/customer-subscription/read", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<CustomerSubscriptionDto>> readSubscriptions();

        /**
         * Recupera las suscripciones por ID de cliente.
         *
         * @param customerId el ID del cliente
         * @return la lista de suscripciones para el cliente
         */
        @GetExchange(value = "/api/v1/customer-subscription/read-by-customer", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<CustomerSubscriptionDto>> readSubscriptionsByCustomer(@RequestParam Long customerId);

        /**
         * Recupera las suscripciones activas por ID de cliente.
         *
         * @param customerId el ID del cliente
         * @return la lista de suscripciones activas para el cliente
         */
        @GetExchange(value = "/api/v1/customer-subscription/read-active-by-customer", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<CustomerSubscriptionDto>> readActiveSubscriptionsByCustomer(
                        @RequestParam Long customerId);

        /**
         * Recupera una suscripción por su ID.
         *
         * @param id el ID de la suscripción
         * @return la suscripción encontrada
         */
        @GetExchange(value = "/api/v1/customer-subscription/get", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<CustomerSubscriptionDto> getSubscriptionById(@RequestParam Long id);

        /**
         * Suscribe a un cliente a un plan.
         *
         * @param dto el DTO con los detalles de la suscripción
         * @return la suscripción creada
         * @throws SimappeException si ocurre un error en el microservicio
         */
        @PostExchange(value = "/api/v1/customer-subscription/subscribe", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<CustomerSubscriptionDto> subscribeCustomer(@RequestBody CustomerSubscriptionDto dto)
                        throws SimappeException;

        /**
         * Cancela una suscripción activa.
         *
         * @param subscriptionId el ID de la suscripción a cancelar
         * @return la suscripción cancelada
         * @throws SimappeException si ocurre un error en el microservicio
         */
        @PutExchange(value = "/api/v1/customer-subscription/cancel", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<CustomerSubscriptionDto> cancelSubscription(@RequestParam Long subscriptionId)
                        throws SimappeException;

        /**
         * Crea un nuevo registro de suscripción.
         *
         * @param dto el DTO de la suscripción
         * @return la suscripción creada
         * @throws SimappeException si ocurre un error en el microservicio
         */
        @PostExchange(value = "/api/v1/customer-subscription/create", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<CustomerSubscriptionDto> createSubscription(
                        @Valid @RequestBody CustomerSubscriptionDto dto)
                        throws SimappeException;

        /**
         * Actualiza un registro de suscripción existente.
         *
         * @param dto el DTO de la suscripción con los cambios
         * @param id  el ID de la suscripción a actualizar
         * @return la suscripción actualizada
         */
        @PutExchange(value = "/api/v1/customer-subscription/update", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<CustomerSubscriptionDto> updateSubscription(
                        @Valid @RequestBody CustomerSubscriptionDto dto, @RequestParam Long id);

        /**
         * Elimina un registro de suscripción por su ID.
         *
         * @param id el ID de la suscripción a eliminar
         * @return respuesta vacía indicando el éxito de la operación
         */
        @DeleteExchange(value = "/api/v1/customer-subscription/delete", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<Void> deleteSubscription(@RequestParam Long id);

        /**
         * Recupera una lista paginada de suscripciones.
         *
         * @param requestQuery los parámetros de consulta
         * @return una página de suscripciones
         * @throws SimappeException si ocurre un error en el microservicio
         */
        @PostExchange(value = "/api/v1/customer-subscription/page", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<PageDto<CustomerSubscriptionDto>> getSubscriptionsPage(SimappeRequestQuery requestQuery)
                        throws SimappeException;

        // ======================================================================
        // GESTIÓN DE PLANES DE SUSCRIPCIÓN (SUBSCRIPTION PLAN MANAGEMENT)
        // ======================================================================

        /**
         * Verifica la disponibilidad del servicio de planes de suscripción.
         *
         * @return la respuesta con el estado de disponibilidad del servicio
         */
        @GetExchange(value = "/api/v1/subscription-plan", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<ServiceResponseAvailable> subscriptionPlanIndex();

        /**
         * Recupera todos los planes de suscripción.
         *
         * @return la lista de todos los planes de suscripción
         */
        @GetExchange(value = "/api/v1/subscription-plan/read", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<SubscriptionPlanDto>> readSubscriptionPlans();

        /**
         * Recupera los planes de suscripción activos.
         *
         * @return la lista de planes de suscripción activos
         */
        @GetExchange(value = "/api/v1/subscription-plan/read-active", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<SubscriptionPlanDto>> readActiveSubscriptionPlans();

        /**
         * Recupera los planes de suscripción disponibles para contratación.
         *
         * @return la lista de planes de suscripción disponibles
         */
        @GetExchange(value = "/api/v1/subscription-plan/available", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<SubscriptionPlanDto>> getAvailableSubscriptionPlans();

        /**
         * Recupera un plan de suscripción por su ID.
         *
         * @param id el ID del plan de suscripción
         * @return el plan de suscripción encontrado
         */
        @GetExchange(value = "/api/v1/subscription-plan/get", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<SubscriptionPlanDto> getSubscriptionPlanById(Long id);

        /**
         * Recupera un plan de suscripción por su código.
         *
         * @param code el código del plan de suscripción
         * @return el plan de suscripción encontrado
         */
        @GetExchange(value = "/api/v1/subscription-plan/get-by-code", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<SubscriptionPlanDto> getSubscriptionPlanByCode(String code);

        /**
         * Crea un nuevo plan de suscripción.
         *
         * @param dto el DTO del plan de suscripción
         * @return el plan de suscripción creado
         * @throws SimappeException si ocurre un error en el microservicio
         */
        @PostExchange(value = "/api/v1/subscription-plan/create", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<SubscriptionPlanDto> createSubscriptionPlan(@Valid SubscriptionPlanDto dto)
                        throws SimappeException;

        /**
         * Actualiza un plan de suscripción existente.
         *
         * @param dto el DTO del plan de suscripción con los cambios
         * @param id  el ID del plan de suscripción a actualizar
         * @return el plan de suscripción actualizado
         */
        @PutExchange(value = "/api/v1/subscription-plan/update", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<SubscriptionPlanDto> updateSubscriptionPlan(@Valid SubscriptionPlanDto dto, Long id);

        /**
         * Elimina un plan de suscripción por su ID.
         *
         * @param id el ID del plan de suscripción a eliminar
         * @return respuesta vacía indicando el éxito de la operación
         */
        @DeleteExchange(value = "/api/v1/subscription-plan/delete", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<Void> deleteSubscriptionPlan(Long id);

        /**
         * Recupera una lista paginada de planes de suscripción.
         *
         * @param requestQuery los parámetros de consulta
         * @return una página de planes de suscripción
         * @throws SimappeException si ocurre un error en el microservicio
         */
        @PostExchange(value = "/api/v1/subscription-plan/page", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<PageDto<SubscriptionPlanDto>> getSubscriptionPlansPage(SimappeRequestQuery requestQuery)
                        throws SimappeException;

        // ======================================================================
        // FEATURE MANAGEMENT
        // ======================================================================

        // ======================================================================
        // GESTIÓN DE FUNCIONALIDADES (FEATURE MANAGEMENT)
        // ======================================================================

        /**
         * Verifica la disponibilidad del servicio de funcionalidades.
         *
         * @return la respuesta con el estado de disponibilidad del servicio
         */
        @GetExchange(value = "/api/v1/feature", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<ServiceResponseAvailable> featureIndex();

        /**
         * Recupera todas las funcionalidades.
         *
         * @return la lista de todas las funcionalidades
         */
        @GetExchange(value = "/api/v1/feature/read", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<FeatureDto>> readFeatures();

        /**
         * Recupera las funcionalidades activas.
         *
         * @return la lista de funcionalidades activas
         */
        @GetExchange(value = "/api/v1/feature/read-active", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<FeatureDto>> readActiveFeatures();

        /**
         * Recupera una funcionalidad por su ID.
         *
         * @param id el ID de la funcionalidad
         * @return la funcionalidad encontrada
         */
        @GetExchange(value = "/api/v1/feature/get", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<FeatureDto> getFeatureById(Long id);

        /**
         * Recupera una funcionalidad por su código.
         *
         * @param code el código de la funcionalidad
         * @return la funcionalidad encontrada
         */
        @GetExchange(value = "/api/v1/feature/get-by-code", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<FeatureDto> getFeatureByCode(String code);

        /**
         * Crea una nueva funcionalidad.
         *
         * @param dto el DTO de la funcionalidad
         * @return la funcionalidad creada
         * @throws SimappeException si ocurre un error en el microservicio
         */
        @PostExchange(value = "/api/v1/feature/create", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<FeatureDto> createFeature(@Valid FeatureDto dto) throws SimappeException;

        /**
         * Actualiza una funcionalidad existente.
         *
         * @param dto el DTO de la funcionalidad con los cambios
         * @param id  el ID de la funcionalidad a actualizar
         * @return la funcionalidad actualizada
         */
        @PutExchange(value = "/api/v1/feature/update", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<FeatureDto> updateFeature(@Valid FeatureDto dto, Long id);

        /**
         * Elimina una funcionalidad por su ID.
         *
         * @param id el ID de la funcionalidad a eliminar
         * @return respuesta vacía indicando el éxito de la operación
         */
        @DeleteExchange(value = "/api/v1/feature/delete", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<Void> deleteFeature(Long id);

        /**
         * Recupera una lista paginada de funcionalidades.
         *
         * @param requestQuery los parámetros de consulta
         * @return una página de funcionalidades
         * @throws SimappeException si ocurre un error en el microservicio
         */
        @PostExchange(value = "/api/v1/feature/page", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<PageDto<FeatureDto>> getFeaturesPage(SimappeRequestQuery requestQuery)
                        throws SimappeException;

        // ======================================================================
        // GESTIÓN DE LICENCIAS (LICENSE MANAGEMENT)
        // ======================================================================

        /**
         * Verifica la disponibilidad del servicio de licencias.
         *
         * @return la respuesta con el estado de disponibilidad del servicio
         */
        @GetExchange(value = "/api/v1/license", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<ServiceResponseAvailable> licenseIndex();

        /**
         * Recupera todas las licencias.
         *
         * @return la lista de todas las licencias
         */
        @GetExchange(value = "/api/v1/license/read", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<LicenseDto>> readLicenses();

        /**
         * Recupera las licencias por ID de cliente.
         *
         * @param customerId el ID del cliente
         * @return la lista de licencias para el cliente
         */
        @GetExchange(value = "/api/v1/license/read-by-customer", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<LicenseDto>> readLicensesByCustomer(Long customerId);

        /**
         * Recupera las licencias por ID de empresa.
         *
         * @param companyId el ID de la empresa
         * @return la lista de licencias para la empresa
         */
        @GetExchange(value = "/api/v1/license/read-by-company", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<LicenseDto>> readLicensesByCompany(Long companyId);

        /**
         * Recupera una licencia por su ID.
         *
         * @param id el ID de la licencia
         * @return la licencia encontrada
         */
        @GetExchange(value = "/api/v1/license/get", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<LicenseDto> getLicenseById(Long id);

        /**
         * Recupera una licencia por su clave de licencia.
         *
         * @param licenseKey la clave de la licencia
         * @return la licencia encontrada
         */
        @GetExchange(value = "/api/v1/license/get-by-key", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<LicenseDto> getLicenseByKey(String licenseKey);

        /**
         * Valida una licencia por su clave.
         *
         * @param licenseKey la clave de la licencia a validar
         * @return verdadero si la licencia es válida, falso en caso contrario
         */
        @GetExchange(value = "/api/v1/license/validate", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<Boolean> validateLicense(String licenseKey);

        /**
         * Genera una nueva licencia.
         *
         * @param dto el DTO con los detalles para la generación
         * @return la licencia generada
         * @throws SimappeException si ocurre un error en el microservicio
         */
        @PostExchange(value = "/api/v1/license/generate", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<LicenseDto> generateLicense(LicenseDto dto) throws SimappeException;

        /**
         * Revoca una licencia activa.
         *
         * @param licenseId el ID de la licencia a revocar
         * @return la licencia revocada
         * @throws SimappeException si ocurre un error en el microservicio
         */
        @PutExchange(value = "/api/v1/license/revoke", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<LicenseDto> revokeLicense(Long licenseId) throws SimappeException;

        /**
         * Crea un nuevo registro de licencia.
         *
         * @param dto el DTO de la licencia
         * @return la licencia creada
         * @throws SimappeException si ocurre un error en el microservicio
         */
        @PostExchange(value = "/api/v1/license/create", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<LicenseDto> createLicense(@Valid LicenseDto dto) throws SimappeException;

        /**
         * Actualiza un registro de licencia existente.
         *
         * @param dto el DTO de la licencia con los cambios
         * @param id  el ID de la licencia a actualizar
         * @return la licencia actualizada
         */
        @PutExchange(value = "/api/v1/license/update", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<LicenseDto> updateLicense(@Valid @RequestBody LicenseDto dto, @RequestParam Long id);

        /**
         * Elimina un registro de licencia por su ID.
         *
         * @param id el ID de la licencia a eliminar
         * @return respuesta vacía indicando el éxito de la operación
         */
        @DeleteExchange(value = "/api/v1/license/delete", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<Void> deleteLicense(@RequestParam Long id);

        /**
         * Recupera una lista paginada de licencias.
         *
         * @param requestQuery los parámetros de consulta
         * @return una página de licencias
         * @throws SimappeException si ocurre un error en el microservicio
         */
        @PostExchange(value = "/api/v1/license/page", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<PageDto<LicenseDto>> getLicensesPage(@RequestBody SimappeRequestQuery requestQuery)
                        throws SimappeException;

        // ======================================================================
        // GESTIÓN DE SEGUIMIENTO DE USO (USAGE TRACKING MANAGEMENT)
        // ======================================================================

        /**
         * Verifica la disponibilidad del servicio de seguimiento de uso.
         *
         * @return la respuesta con el estado de disponibilidad del servicio
         */
        @GetExchange(value = "/api/v1/usage-tracking", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<ServiceResponseAvailable> usageTrackingIndex();

        /**
         * Recupera todos los registros de seguimiento de uso.
         *
         * @return la lista de todos los registros de seguimiento de uso
         */
        @GetExchange(value = "/api/v1/usage-tracking/read", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<UsageTrackingDto>> readUsageTracking();

        /**
         * Recupera los registros de seguimiento de uso por ID de cliente.
         *
         * @param customerId el ID del cliente
         * @return la lista de registros de seguimiento de uso para el cliente
         */
        @GetExchange(value = "/api/v1/usage-tracking/read-by-customer", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<UsageTrackingDto>> readUsageTrackingByCustomer(@RequestParam Long customerId);

        /**
         * Recupera los registros de seguimiento de uso por ID de suscripción.
         *
         * @param subscriptionId el ID de la suscripción
         * @return la lista de registros de seguimiento de uso para la suscripción
         */
        @GetExchange(value = "/api/v1/usage-tracking/read-by-subscription", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<UsageTrackingDto>> readUsageTrackingBySubscription(
                        @RequestParam Long subscriptionId);

        /**
         * Recupera el registro de seguimiento de uso actual para una suscripción.
         *
         * @param subscriptionId el ID de la suscripción
         * @return el registro de seguimiento de uso actual
         */
        @GetExchange(value = "/api/v1/usage-tracking/current", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<UsageTrackingDto> getCurrentUsageTracking(@RequestParam Long subscriptionId);

        /**
         * Recupera un registro de seguimiento de uso por su ID.
         *
         * @param id el ID del registro de seguimiento de uso
         * @return el registro de seguimiento de uso encontrado
         */
        @GetExchange(value = "/api/v1/usage-tracking/get", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<UsageTrackingDto> getUsageTrackingById(@RequestParam Long id);

        /**
         * Incrementa el uso registrado para una suscripción.
         *
         * @param subscriptionId el ID de la suscripción
         * @param amount         la cantidad a incrementar
         * @return el registro de seguimiento de uso actualizado
         */
        @PutExchange(value = "/api/v1/usage-tracking/increment", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<UsageTrackingDto> incrementUsage(@RequestParam Long subscriptionId,
                        @RequestParam Long amount);

        /**
         * Reinicia el uso registrado para una suscripción.
         *
         * @param subscriptionId el ID de la suscripción
         * @return el registro de seguimiento de uso reiniciado
         */
        @PutExchange(value = "/api/v1/usage-tracking/reset", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<UsageTrackingDto> resetUsage(@RequestParam Long subscriptionId);

        /**
         * Crea un nuevo registro de seguimiento de uso.
         *
         * @param dto el DTO del seguimiento de uso
         * @return el registro de seguimiento de uso creado
         * @throws SimappeException si ocurre un error en el microservicio
         */
        @PostExchange(value = "/api/v1/usage-tracking/create", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<UsageTrackingDto> createUsageTracking(@Valid @RequestBody UsageTrackingDto dto)
                        throws SimappeException;

        /**
         * Actualiza un registro de seguimiento de uso existente.
         *
         * @param dto el DTO del seguimiento de uso con los cambios
         * @param id  el ID del registro a actualizar
         * @return el registro de seguimiento de uso actualizado
         */
        @PutExchange(value = "/api/v1/usage-tracking/update", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<UsageTrackingDto> updateUsageTracking(@Valid @RequestBody UsageTrackingDto dto,
                        @RequestParam Long id);

        /**
         * Elimina un registro de seguimiento de uso por su ID.
         *
         * @param id el ID del registro a eliminar
         * @return respuesta vacía indicando el éxito de la operación
         */
        @DeleteExchange(value = "/api/v1/usage-tracking/delete", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<Void> deleteUsageTracking(@RequestParam Long id);

        /**
         * Recupera una lista paginada de registros de seguimiento de uso.
         *
         * @param requestQuery los parámetros de consulta
         * @return una página de registros de seguimiento de uso
         * @throws SimappeException si ocurre un error en el microservicio
         */
        @PostExchange(value = "/api/v1/usage-tracking/page", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<PageDto<UsageTrackingDto>> getUsageTrackingPage(
                        @RequestBody SimappeRequestQuery requestQuery)
                        throws SimappeException;

        // ======================================================================
        // GESTIÓN DE ENDPOINTS DE WEBHOOK (WEBHOOK ENDPOINT MANAGEMENT)
        // ======================================================================

        /**
         * Verifica la disponibilidad del servicio de endpoints de webhook.
         *
         * @return la respuesta con el estado de disponibilidad del servicio
         */
        @GetExchange(value = "/api/v1/webhook-endpoint", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<ServiceResponseAvailable> webhookEndpointIndex();

        /**
         * Recupera todos los endpoints de webhook.
         *
         * @return la lista de todos los endpoints de webhook
         */
        @GetExchange(value = "/api/v1/webhook-endpoint/read", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<WebhookEndpointDto>> readWebhookEndpoints();

        /**
         * Recupera un endpoint de webhook por su ID.
         *
         * @param id el ID del endpoint de webhook
         * @return el endpoint de webhook encontrado
         */
        @GetExchange(value = "/api/v1/webhook-endpoint/get", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<WebhookEndpointDto> getWebhookEndpointById(@RequestParam Long id);

        /**
         * Crea un nuevo endpoint de webhook.
         *
         * @param dto el DTO del endpoint de webhook
         * @return el endpoint de webhook creado
         * @throws SimappeException si ocurre un error en el microservicio
         */
        @PostExchange(value = "/api/v1/webhook-endpoint/create", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<WebhookEndpointDto> createWebhookEndpoint(@Valid @RequestBody WebhookEndpointDto dto)
                        throws SimappeException;

        /**
         * Actualiza un endpoint de webhook existente.
         *
         * @param dto el DTO del endpoint con los cambios
         * @param id  el ID del endpoint a actualizar
         * @return el endpoint de webhook actualizado
         */
        @PutExchange(value = "/api/v1/webhook-endpoint/update", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<WebhookEndpointDto> updateWebhookEndpoint(@Valid @RequestBody WebhookEndpointDto dto,
                        @RequestParam Long id);

        /**
         * Elimina un endpoint de webhook por su ID.
         *
         * @param id el ID del endpoint de webhook a eliminar
         * @return respuesta vacía indicando el éxito de la operación
         */
        @DeleteExchange(value = "/api/v1/webhook-endpoint/delete", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<Void> deleteWebhookEndpoint(@RequestParam Long id);

        /**
         * Recupera una lista paginada de endpoints de webhook.
         *
         * @param requestQuery los parámetros de consulta
         * @return una página de endpoints de webhook
         * @throws SimappeException si ocurre un error en el microservicio
         */
        @PostExchange(value = "/api/v1/webhook-endpoint/page", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<PageDto<WebhookEndpointDto>> getWebhookEndpointsPage(
                        @RequestBody SimappeRequestQuery requestQuery)
                        throws SimappeException;

        /**
         * Recupera los endpoints de webhook por ID de cliente.
         *
         * @param customerId el ID del cliente
         * @return la lista de endpoints de webhook para el cliente
         */
        @GetExchange(value = "/api/v1/webhook-endpoint/by-customer", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<WebhookEndpointDto>> getWebhookEndpointsByCustomer(@RequestParam Long customerId);

        /**
         * Habilita un endpoint de webhook desactivado.
         *
         * @param id el ID del endpoint a habilitar
         * @return el endpoint de webhook habilitado
         */
        @PostExchange(value = "/api/v1/webhook-endpoint/{id}/enable", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<WebhookEndpointDto> enableWebhookEndpoint(@PathVariable Long id);

        /**
         * Deshabilita un endpoint de webhook activo.
         *
         * @param id el ID del endpoint a deshabilitar
         * @return el endpoint de webhook deshabilitado
         */
        @PostExchange(value = "/api/v1/webhook-endpoint/{id}/disable", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<WebhookEndpointDto> disableWebhookEndpoint(@PathVariable Long id);

        // ======================================================================
        // GESTIÓN DE EVENTOS DE WEBHOOK (WEBHOOK EVENT MANAGEMENT)
        // ======================================================================

        /**
         * Verifica la disponibilidad del servicio de eventos de webhook.
         *
         * @return la respuesta con el estado de disponibilidad del servicio
         */
        @GetExchange(value = "/api/v1/webhook-event", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<ServiceResponseAvailable> webhookEventIndex();

        /**
         * Recupera todos los eventos de webhook registrados.
         *
         * @return la lista de todos los eventos de webhook
         */
        @GetExchange(value = "/api/v1/webhook-event/read", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<WebhookEventDto>> readWebhookEvents();

        /**
         * Recupera un evento de webhook por su ID.
         *
         * @param id el ID del evento de webhook
         * @return el evento de webhook encontrado
         */
        @GetExchange(value = "/api/v1/webhook-event/get", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<WebhookEventDto> getWebhookEventById(@RequestParam Long id);

        /**
         * Crea un nuevo tipo de evento de webhook.
         *
         * @param dto el DTO del evento de webhook
         * @return el evento de webhook creado
         * @throws SimappeException si ocurre un error en el microservicio
         */
        @PostExchange(value = "/api/v1/webhook-event/create", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<WebhookEventDto> createWebhookEvent(@Valid @RequestBody WebhookEventDto dto)
                        throws SimappeException;

        /**
         * Actualiza un tipo de evento de webhook existente.
         *
         * @param dto el DTO del evento con los cambios
         * @param id  el ID del evento a actualizar
         * @return el evento de webhook actualizado
         */
        @PutExchange(value = "/api/v1/webhook-event/update", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<WebhookEventDto> updateWebhookEvent(@Valid @RequestBody WebhookEventDto dto,
                        @RequestParam Long id);

        /**
         * Elimina un tipo de evento de webhook por su ID.
         *
         * @param id el ID del evento a eliminar
         * @return respuesta vacía indicando el éxito de la operación
         */
        @DeleteExchange(value = "/api/v1/webhook-event/delete", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<Void> deleteWebhookEvent(@RequestParam Long id);

        /**
         * Recupera una lista paginada de eventos de webhook.
         *
         * @param requestQuery los parámetros de consulta
         * @return una página de eventos de webhook
         * @throws SimappeException si ocurre un error en el microservicio
         */
        @PostExchange(value = "/api/v1/webhook-event/page", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<PageDto<WebhookEventDto>> getWebhookEventsPage(
                        @RequestBody SimappeRequestQuery requestQuery)
                        throws SimappeException;

        /**
         * Recupera los eventos de webhook por categoría.
         *
         * @param category la categoría de los eventos
         * @return la lista de eventos de webhook para la categoría
         */
        @GetExchange(value = "/api/v1/webhook-event/by-category", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<WebhookEventDto>> getWebhookEventsByCategory(@RequestParam String category);

        /**
         * Recupera los eventos de webhook activos.
         *
         * @return la lista de eventos de webhook activos
         */
        @GetExchange(value = "/api/v1/webhook-event/active", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<WebhookEventDto>> getActiveWebhookEvents();

        // ======================================================================
        // GESTIÓN DE ENTREGAS DE WEBHOOK (WEBHOOK DELIVERY MANAGEMENT)
        // ======================================================================

        /**
         * Verifica la disponibilidad del servicio de entregas de webhook.
         *
         * @return la respuesta con el estado de disponibilidad del servicio
         */
        @GetExchange(value = "/api/v1/webhook-delivery", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<ServiceResponseAvailable> webhookDeliveryIndex();

        /**
         * Recupera todas las entregas de webhook.
         *
         * @return la lista de todas las entregas de webhook
         */
        @GetExchange(value = "/api/v1/webhook-delivery/read", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<WebhookDeliveryDto>> readWebhookDeliveries();

        /**
         * Recupera una entrega de webhook por su ID.
         *
         * @param id el ID de la entrega de webhook
         * @return la entrega de webhook encontrada
         */
        @GetExchange(value = "/api/v1/webhook-delivery/get", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<WebhookDeliveryDto> getWebhookDeliveryById(@RequestParam Long id);

        /**
         * Crea un nuevo registro de entrega de webhook.
         *
         * @param dto el DTO de la entrega de webhook
         * @return la entrega de webhook creada
         * @throws SimappeException si ocurre un error en el microservicio
         */
        @PostExchange(value = "/api/v1/webhook-delivery/create", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<WebhookDeliveryDto> createWebhookDelivery(@Valid @RequestBody WebhookDeliveryDto dto)
                        throws SimappeException;

        /**
         * Actualiza un registro de entrega de webhook existente.
         *
         * @param dto el DTO de la entrega con los cambios
         * @param id  el ID de la entrega a actualizar
         * @return la entrega de webhook actualizada
         */
        @PutExchange(value = "/api/v1/webhook-delivery/update", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<WebhookDeliveryDto> updateWebhookDelivery(@Valid @RequestBody WebhookDeliveryDto dto,
                        @RequestParam Long id);

        /**
         * Elimina un registro de entrega de webhook por su ID.
         *
         * @param id el ID de la entrega de webhook a eliminar
         * @return respuesta vacía indicando el éxito de la operación
         */
        @DeleteExchange(value = "/api/v1/webhook-delivery/delete", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<Void> deleteWebhookDelivery(@RequestParam Long id);

        /**
         * Recupera una lista paginada de entregas de webhook.
         *
         * @param requestQuery los parámetros de consulta
         * @return una página de entregas de webhook
         * @throws SimappeException si ocurre un error en el microservicio
         */
        @PostExchange(value = "/api/v1/webhook-delivery/page", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<PageDto<WebhookDeliveryDto>> getWebhookDeliveriesPage(
                        @RequestBody SimappeRequestQuery requestQuery)
                        throws SimappeException;

        /**
         * Recupera las entregas de webhook por ID de endpoint.
         *
         * @param endpointId el ID del endpoint
         * @return la lista de entregas de webhook para el endpoint
         */
        @GetExchange(value = "/api/v1/webhook-delivery/by-endpoint", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<WebhookDeliveryDto>> getWebhookDeliveriesByEndpoint(@RequestParam Long endpointId);

        /**
         * Recupera las entregas de webhook por ID de evento.
         *
         * @param eventId el ID del evento
         * @return la lista de entregas de webhook para el evento
         */
        @GetExchange(value = "/api/v1/webhook-delivery/by-event", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<WebhookDeliveryDto>> getWebhookDeliveriesByEvent(@RequestParam Long eventId);

        /**
         * Reintenta una entrega de webhook fallida.
         *
         * @param id el ID de la entrega a reintentar
         * @return el registro de entrega de webhook después del reintento
         */
        @PostExchange(value = "/api/v1/webhook-delivery/{id}/retry", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<WebhookDeliveryDto> retryWebhookDelivery(@PathVariable Long id);

        // ======================================================================
        // GESTIÓN DE SOLICITUDES DE APROBACIÓN (APPROVAL REQUEST MANAGEMENT)
        // ======================================================================

        /**
         * Verifica la disponibilidad del servicio de solicitudes de aprobación.
         *
         * @return la respuesta con el estado de disponibilidad del servicio
         */
        @GetExchange(value = "/api/v1/workflow/approval-request", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<ServiceResponseAvailable> approvalRequestIndex();

        /**
         * Recupera todas las solicitudes de aprobación.
         *
         * @return la lista de todas las solicitudes de aprobación
         */
        @GetExchange(value = "/api/v1/workflow/approval-request/read", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<ApprovalRequestDto>> readApprovalRequests();

        /**
         * Recupera las solicitudes de aprobación por ID de instancia de flujo de
         * trabajo.
         *
         * @param instanceId el ID de la instancia de flujo de trabajo
         * @return la lista de solicitudes de aprobación para la instancia
         */
        @GetExchange(value = "/api/v1/workflow/approval-request/read-by-instance", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<ApprovalRequestDto>> readApprovalRequestsByInstance(@RequestParam Long instanceId);

        /**
         * Recupera las solicitudes de aprobación por ID de paso de flujo de trabajo.
         *
         * @param stepId el ID del paso de flujo de trabajo
         * @return la lista de solicitudes de aprobación para el paso
         */
        @GetExchange(value = "/api/v1/workflow/approval-request/read-by-step", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<ApprovalRequestDto>> readApprovalRequestsByStep(@RequestParam Long stepId);

        /**
         * Recupera las solicitudes de aprobación enviadas por un usuario.
         *
         * @param userId el ID del usuario
         * @return la lista de solicitudes de aprobación para el usuario
         */
        @GetExchange(value = "/api/v1/workflow/approval-request/read-by-user", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<ApprovalRequestDto>> readApprovalRequestsByUser(@RequestParam Long userId);

        /**
         * Recupera las solicitudes de aprobación destinadas a un rol.
         *
         * @param roleId el ID del rol
         * @return la lista de solicitudes de aprobación para el rol
         */
        @GetExchange(value = "/api/v1/workflow/approval-request/read-by-role", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<ApprovalRequestDto>> readApprovalRequestsByRole(@RequestParam Long roleId);

        /**
         * Recupera las solicitudes de aprobación por su estado.
         *
         * @param status el estado de la solicitud
         * @return la lista de solicitudes de aprobación con el estado dado
         */
        @GetExchange(value = "/api/v1/workflow/approval-request/read-by-status", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<ApprovalRequestDto>> readApprovalRequestsByStatus(@RequestParam String status);

        /**
         * Recupera una solicitud de aprobación por su ID.
         *
         * @param id el ID de la solicitud de aprobación
         * @return la solicitud de aprobación encontrada
         */
        @GetExchange(value = "/api/v1/workflow/approval-request/get", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<ApprovalRequestDto> getApprovalRequestById(@RequestParam Long id);

        /**
         * Crea una nueva solicitud de aprobación.
         *
         * @param dto el DTO de la solicitud de aprobación
         * @return la solicitud de aprobación creada
         * @throws SimappeException si ocurre un error en el microservicio
         */
        @PostExchange(value = "/api/v1/workflow/approval-request/create", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<ApprovalRequestDto> createApprovalRequest(@Valid @RequestBody ApprovalRequestDto dto)
                        throws SimappeException;

        /**
         * Actualiza una solicitud de aprobación existente.
         *
         * @param dto el DTO de la solicitud con los cambios
         * @param id  el ID de la solicitud a actualizar
         * @return la solicitud de aprobación actualizada
         */
        @PutExchange(value = "/api/v1/workflow/approval-request/update", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<ApprovalRequestDto> updateApprovalRequest(@Valid @RequestBody ApprovalRequestDto dto,
                        @RequestParam Long id);

        /**
         * Elimina una solicitud de aprobación por su ID.
         *
         * @param id el ID de la solicitud de aprobación a eliminar
         * @return respuesta vacía indicando el éxito de la operación
         */
        @DeleteExchange(value = "/api/v1/workflow/approval-request/delete", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<Void> deleteApprovalRequest(@RequestParam Long id);

        /**
         * Recupera una lista paginada de solicitudes de aprobación.
         *
         * @param requestQuery los parámetros de consulta
         * @return una página de solicitudes de aprobación
         * @throws SimappeException si ocurre un error en el microservicio
         */
        @PostExchange(value = "/api/v1/workflow/approval-request/page", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<PageDto<ApprovalRequestDto>> getApprovalRequestsPage(
                        @RequestBody SimappeRequestQuery requestQuery)
                        throws SimappeException;

        /**
         * Aprueba una solicitud de aprobación pendiente.
         *
         * @param approvalId el ID de la solicitud de aprobación
         * @param approverId el ID del usuario que aprueba
         * @return la solicitud de aprobación actualizada con estado aprobado
         * @throws SimappeException si ocurre un error en el microservicio
         */
        @PostExchange(value = "/api/v1/workflow/approval-request/approve", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<ApprovalRequestDto> approveApprovalRequest(@RequestParam Long approvalId,
                        @RequestParam Long approverId)
                        throws SimappeException;

        /**
         * Rechaza una solicitud de aprobación pendiente.
         *
         * @param approvalId      el ID de la solicitud de aprobación
         * @param approverId      el ID del usuario que rechaza
         * @param rejectionReason el motivo del rechazo
         * @return la solicitud de aprobación actualizada con estado rechazado
         * @throws SimappeException si ocurre un error en el microservicio
         */
        @PostExchange(value = "/api/v1/workflow/approval-request/reject", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<ApprovalRequestDto> rejectApprovalRequest(@RequestParam Long approvalId,
                        @RequestParam Long approverId,
                        @RequestParam String rejectionReason) throws SimappeException;

        /**
         * Recupera las solicitudes de aprobación pendientes para un usuario específico.
         *
         * @param userId el ID del usuario
         * @return la lista de solicitudes de aprobación pendientes
         */
        @GetExchange(value = "/api/v1/workflow/approval-request/pending-approvals", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<ApprovalRequestDto>> getPendingApprovalsForUser(@RequestParam Long userId);

        // ======================================================================
        // GESTIÓN DE INSTANCIAS DE FLUJO DE TRABAJO (WORKFLOW INSTANCE MANAGEMENT)
        // ======================================================================

        /**
         * Verifica la disponibilidad del servicio de instancias de flujo de trabajo.
         *
         * @return la respuesta con el estado de disponibilidad del servicio
         */
        @GetExchange(value = "/api/v1/workflow/instance", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<ServiceResponseAvailable> workflowInstanceIndex();

        /**
         * Recupera todas las instancias de flujo de trabajo.
         *
         * @return la lista de todas las instancias de flujo de trabajo
         */
        @GetExchange(value = "/api/v1/workflow/instance/read", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<WorkflowInstanceDto>> readWorkflowInstances();

        /**
         * Recupera las instancias de flujo de trabajo por ID de definición.
         *
         * @param definitionId el ID de la definición del flujo de trabajo
         * @return la lista de instancias de flujo de trabajo para la definición
         */
        @GetExchange(value = "/api/v1/workflow/instance/read-by-definition", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<WorkflowInstanceDto>> readWorkflowInstancesByDefinition(
                        @RequestParam Long definitionId);

        /**
         * Recupera las instancias de flujo de trabajo por ID de cliente.
         *
         * @param customerId el ID del cliente
         * @return la lista de instancias de flujo de trabajo para el cliente
         */
        @GetExchange(value = "/api/v1/workflow/instance/read-by-customer", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<WorkflowInstanceDto>> readWorkflowInstancesByCustomer(@RequestParam Long customerId);

        /**
         * Recupera las instancias de flujo de trabajo por su estado.
         *
         * @param status el estado de la instancia
         * @return la lista de instancias de flujo de trabajo con el estado dado
         */
        @GetExchange(value = "/api/v1/workflow/instance/read-by-status", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<WorkflowInstanceDto>> readWorkflowInstancesByStatus(@RequestParam String status);

        /**
         * Recupera una instancia de flujo de trabajo por su ID.
         *
         * @param id el ID de la instancia de flujo de trabajo
         * @return la instancia de flujo de trabajo encontrada
         */
        @GetExchange(value = "/api/v1/workflow/instance/get", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<WorkflowInstanceDto> getWorkflowInstanceById(@RequestParam Long id);

        /**
         * Recupera una instancia de flujo de trabajo por su ID de instancia (UUID).
         *
         * @param instanceId el UUID de la instancia
         * @return la instancia de flujo de trabajo encontrada
         */
        @GetExchange(value = "/api/v1/workflow/instance/get-by-instance-id", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<WorkflowInstanceDto> getWorkflowInstanceByInstanceId(@RequestParam UUID instanceId);

        /**
         * Crea una nueva instancia de flujo de trabajo.
         *
         * @param dto el DTO de la instancia de flujo de trabajo
         * @return la instancia de flujo de trabajo creada
         * @throws SimappeException si ocurre un error en el microservicio
         */
        @PostExchange(value = "/api/v1/workflow/instance/create", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<WorkflowInstanceDto> createWorkflowInstance(@Valid @RequestBody WorkflowInstanceDto dto)
                        throws SimappeException;

        /**
         * Actualiza una instancia de flujo de trabajo existente.
         *
         * @param dto el DTO de la instancia con los cambios
         * @param id  el ID de la instancia a actualizar
         * @return la instancia de flujo de trabajo actualizada
         */
        @PutExchange(value = "/api/v1/workflow/instance/update", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<WorkflowInstanceDto> updateWorkflowInstance(@Valid @RequestBody WorkflowInstanceDto dto,
                        @RequestParam Long id);

        /**
         * Elimina una instancia de flujo de trabajo por su ID.
         *
         * @param id el ID de la instancia a eliminar
         * @return respuesta vacía indicando el éxito de la operación
         */
        @DeleteExchange(value = "/api/v1/workflow/instance/delete", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<Void> deleteWorkflowInstance(@RequestParam Long id);

        /**
         * Recupera una lista paginada de instancias de flujo de trabajo.
         *
         * @param requestQuery los parámetros de consulta
         * @return una página de instancias de flujo de trabajo
         * @throws SimappeException si ocurre un error en el microservicio
         */
        @PostExchange(value = "/api/v1/workflow/instance/page", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<PageDto<WorkflowInstanceDto>> getWorkflowInstancesPage(
                        @RequestBody SimappeRequestQuery requestQuery)
                        throws SimappeException;

        /**
         * Dispara la ejecución de un flujo de trabajo.
         *
         * @param workflowData los datos necesarios para iniciar el flujo
         * @return la instancia de flujo de trabajo iniciada
         * @throws SimappeException si ocurre un error en el microservicio
         */
        @PostExchange(value = "/api/v1/workflow/instance/trigger", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<WorkflowInstanceDto> triggerWorkflow(@RequestBody Map<String, Object> workflowData)
                        throws SimappeException;

        /**
         * Recupera el estado actual de un flujo de trabajo por su UUID.
         *
         * @param instanceId el UUID de la instancia
         * @return el estado actual del flujo de trabajo
         */
        @GetExchange(value = "/api/v1/workflow/instance/status", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<String> getWorkflowStatus(@RequestParam UUID instanceId);

        // ======================================================================
        // GESTIÓN DE EJECUCIÓN DE PASOS DE FLUJO DE TRABAJO (WORKFLOW STEP EXECUTION
        // MANAGEMENT)
        // ======================================================================

        /**
         * Verifica la disponibilidad del servicio de ejecución de pasos de flujo de
         * trabajo.
         *
         * @return la respuesta con el estado de disponibilidad del servicio
         */
        @GetExchange(value = "/api/v1/workflow/step-execution", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<ServiceResponseAvailable> workflowStepExecutionIndex();

        /**
         * Recupera todas las ejecuciones de pasos de flujo de trabajo.
         *
         * @return la lista de todas las ejecuciones de pasos
         */
        @GetExchange(value = "/api/v1/workflow/step-execution/read", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<WorkflowStepExecutionDto>> readWorkflowStepExecutions();

        /**
         * Recupera las ejecuciones de pasos por ID de instancia de flujo de trabajo.
         *
         * @param instanceId el ID de la instancia de flujo de trabajo
         * @return la lista de ejecuciones de pasos para la instancia
         */
        @GetExchange(value = "/api/v1/workflow/step-execution/read-by-instance", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<WorkflowStepExecutionDto>> readWorkflowStepExecutionsByInstance(
                        @RequestParam Long instanceId);

        /**
         * Recupera las ejecuciones de pasos por ID de paso.
         *
         * @param stepId el ID del paso del flujo de trabajo
         * @return la lista de ejecuciones de pasos para el ID de paso dado
         */
        @GetExchange(value = "/api/v1/workflow/step-execution/read-by-step-id", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<WorkflowStepExecutionDto>> readWorkflowStepExecutionsByStepId(
                        @RequestParam Long stepId);

        /**
         * Recupera las ejecuciones de pasos por su estado.
         *
         * @param status el estado de la ejecución
         * @return la lista de ejecuciones de pasos con el estado dado
         */
        @GetExchange(value = "/api/v1/workflow/step-execution/read-by-status", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<WorkflowStepExecutionDto>> readWorkflowStepExecutionsByStatus(
                        @RequestParam String status);

        /**
         * Recupera las ejecuciones de pasos asignadas a un usuario específico.
         *
         * @param userId el ID del usuario asignado
         * @return la lista de ejecuciones de pasos para el usuario
         */
        @GetExchange(value = "/api/v1/workflow/step-execution/read-by-assigned", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<WorkflowStepExecutionDto>> readWorkflowStepExecutionsByAssigned(
                        @RequestParam Long userId);

        /**
         * Recupera una ejecución de paso por su ID.
         *
         * @param id el ID de la ejecución de paso
         * @return la ejecución de paso encontrada
         */
        @GetExchange(value = "/api/v1/workflow/step-execution/get", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<WorkflowStepExecutionDto> getWorkflowStepExecutionById(@RequestParam Long id);

        /**
         * Crea una nueva ejecución de paso de flujo de trabajo.
         *
         * @param dto el DTO de la ejecución de paso
         * @return la ejecución de paso creada
         * @throws SimappeException si ocurre un error en el microservicio
         */
        @PostExchange(value = "/api/v1/workflow/step-execution/create", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<WorkflowStepExecutionDto> createWorkflowStepExecution(
                        @Valid @RequestBody WorkflowStepExecutionDto dto)
                        throws SimappeException;

        /**
         * Actualiza una ejecución de paso de flujo de trabajo existente.
         *
         * @param dto el DTO de la ejecución de paso con los cambios
         * @param id  el ID de la ejecución a actualizar
         * @return la ejecución de paso actualizada
         */
        @PutExchange(value = "/api/v1/workflow/step-execution/update", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<WorkflowStepExecutionDto> updateWorkflowStepExecution(
                        @Valid @RequestBody WorkflowStepExecutionDto dto,
                        @RequestParam Long id);

        /**
         * Elimina una ejecución de paso de flujo de trabajo por su ID.
         *
         * @param id el ID de la ejecución a eliminar
         * @return respuesta vacía indicando el éxito de la operación
         */
        @DeleteExchange(value = "/api/v1/workflow/step-execution/delete", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<Void> deleteWorkflowStepExecution(@RequestParam Long id);

        /**
         * Recupera una lista paginada de ejecuciones de pasos de flujo de trabajo.
         *
         * @param requestQuery los parámetros de consulta
         * @return una página de ejecuciones de pasos
         * @throws SimappeException si ocurre un error en el microservicio
         */
        @PostExchange(value = "/api/v1/workflow/step-execution/page", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<PageDto<WorkflowStepExecutionDto>> getWorkflowStepExecutionsPage(
                        @RequestBody SimappeRequestQuery requestQuery) throws SimappeException;

        // ======================================================================
        // GESTIÓN DE DEFINICIÓN DE FLUJO DE TRABAJO (WORKFLOW DEFINITION MANAGEMENT)
        // ======================================================================

        /**
         * Verifica la disponibilidad del servicio de definición de flujo de trabajo.
         *
         * @return la respuesta con el estado de disponibilidad del servicio
         */
        @GetExchange(value = "/api/v1/workflow/definition", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<ServiceResponseAvailable> workflowDefinitionIndex();

        /**
         * Recupera todas las definiciones de flujo de trabajo.
         *
         * @return la lista de todas las definiciones de flujo de trabajo
         */
        @GetExchange(value = "/api/v1/workflow/definition/read", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<WorkflowDefinitionDto>> readWorkflowDefinitions();

        /**
         * Recupera una definición de flujo de trabajo por su ID.
         *
         * @param id el ID de la definición de flujo de trabajo
         * @return la definición de flujo de trabajo encontrada
         */
        @GetExchange(value = "/api/v1/workflow/definition/get", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<WorkflowDefinitionDto> getWorkflowDefinitionById(Long id);

        /**
         * Crea una nueva definición de flujo de trabajo.
         *
         * @param dto el DTO de la definición de flujo de trabajo
         * @return la definición de flujo de trabajo creada
         * @throws SimappeException si ocurre un error en el microservicio
         */
        @PostExchange(value = "/api/v1/workflow/definition/create", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<WorkflowDefinitionDto> createWorkflowDefinition(
                        @Valid @RequestBody WorkflowDefinitionDto dto)
                        throws SimappeException;

        /**
         * Actualiza una definición de flujo de trabajo existente.
         *
         * @param dto el DTO de la definición con los cambios
         * @param id  el ID de la definición a actualizar
         * @return la definición de flujo de trabajo actualizada
         */
        @PutExchange(value = "/api/v1/workflow/definition/update", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<WorkflowDefinitionDto> updateWorkflowDefinition(
                        @Valid @RequestBody WorkflowDefinitionDto dto,
                        @RequestParam Long id);

        /**
         * Elimina una definición de flujo de trabajo por su ID.
         *
         * @param id el ID de la definición a eliminar
         * @return respuesta vacía indicando el éxito de la operación
         */
        @DeleteExchange(value = "/api/v1/workflow/definition/delete", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<Void> deleteWorkflowDefinition(@RequestParam Long id);

        /**
         * Recupera una lista paginada de definiciones de flujo de trabajo.
         *
         * @param requestQuery los parámetros de consulta
         * @return una página de definiciones de flujo de trabajo
         * @throws SimappeException si ocurre un error en el microservicio
         */
        @PostExchange(value = "/api/v1/workflow/definition/page", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<PageDto<WorkflowDefinitionDto>> getWorkflowDefinitionsPage(
                        @RequestBody SimappeRequestQuery requestQuery)
                        throws SimappeException;

        // ======================================================================
        // EMPRESAS DE CLIENTES ACTIVAS (ACTIVE CUSTOMER COMPANY)
        // ======================================================================

        /**
         * Recupera todas las empresas de clientes activas.
         *
         * @return la lista de empresas de clientes
         */
        @GetExchange(value = "/api/v1/customers-with-companies")
        SuccessResponse<List<CustomerDto>> getAllActiveCustomerCompanies();

        ////////////////////// ********************************* *

        /**
         * Recupera los detalles de un usuario por su nombre de usuario.
         *
         * @param username el nombre de usuario
         * @return los detalles del usuario encontrado
         */
        @GetExchange(value = "/api/v1/user/get-username", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<UserDto> getUsername(@RequestParam("username") String username);

        /**
         * Recupera los detalles de un usuario por su correo electrónico.
         *
         * @param useremail el correo electrónico del usuario
         * @return los detalles del usuario encontrado
         */
        @GetExchange(value = "/api/v1/user/get-useremail", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<UserDto> getUseremail(@RequestParam String useremail);

        /**
         * Recupera un usuario por su ID.
         *
         * @param id el ID del usuario
         * @return los detalles del usuario encontrado
         */
        @GetExchange(value = "/api/v1/user" + SimappeConstant.PATH_GET, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<UserDto> getUser(@RequestParam Long id);

        /**
         * Crea un nuevo usuario.
         *
         * @param dto el DTO del usuario a crear
         * @return el usuario creado
         * @throws SimappeException si ocurre un error en el microservicio
         */
        @PostExchange(value = "/api/v1/user" + SimappeConstant.PATH_CREATE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<UserDto> createUser(@Valid @RequestBody UserDto dto) throws SimappeException;

        /**
         * Actualiza un usuario existente.
         *
         * @param dto el DTO del usuario con los cambios
         * @param id  el ID del usuario a actualizar
         * @return el usuario actualizado
         */
        @PutExchange(value = "/api/v1/user" + SimappeConstant.PATH_UPDATE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<UserDto> updateUser(@Valid @RequestBody UserDto dto, @RequestParam Long id);

        /**
         * Actualiza el cliente asociado a un usuario.
         *
         * @param userId     el ID del usuario
         * @param customerId el ID del cliente
         * @return el usuario actualizado
         */
        @PutExchange(value = "/api/v1/user/update-customer", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<UserDto> updateUserCustomer(@RequestParam Long userId, @RequestParam Long customerId);

        /**
         * Actualiza la contraseña de un usuario.
         *
         * @param userId   el ID del usuario
         * @param password el objeto de transferencia con la nueva contraseña
         * @return respuesta vacía indicando el éxito de la operación
         * @throws SimappeException si ocurre un error en el microservicio
         */
        @PutExchange(value = "/api/v1/user/update-password", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<Void> updateUserPassword(@RequestParam Long userId,
                        @RequestBody TransferDto2<String, String> password) throws SimappeException;

        /**
         * Asigna un rol a un usuario.
         *
         * @param role el objeto de transferencia con los IDs de rol y usuario
         * @return los detalles del rol asignado
         */
        @PutExchange(value = "/api/v1/user/add-role", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<RoleDto> addUserRole(@RequestBody TransferDto2<Long, Long> role);

        /**
         * Elimina un rol de un usuario.
         *
         * @param role el objeto de transferencia con los IDs de rol y usuario
         * @return respuesta vacía indicando el éxito de la operación
         */
        @DeleteExchange(value = "/api/v1/user/delete-role", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<Void> deleteUserRole(@RequestBody TransferDto2<Long, Long> role);

        /**
         * Elimina un usuario por su ID.
         *
         * @param id el ID del usuario a eliminar
         * @return respuesta vacía indicando el éxito de la operación
         */
        @DeleteExchange(value = "/api/v1/user" + SimappeConstant.PATH_DELETE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<Void> deleteUser(@RequestParam Long id);

        /**
         * Recupera una lista paginada de usuarios.
         *
         * @param requestQuery los parámetros de consulta
         * @return una página de usuarios
         * @throws SimappeException si ocurre un error en el microservicio
         */
        @PostExchange(value = "/api/v1/user" + SimappeConstant.PATH_PAGE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<PageDto<UserDto>> getUserPage(@RequestBody SimappeRequestQuery requestQuery)
                        throws SimappeException;

        // ========== GESTIÓN DE IDENTIDAD DE USUARIO (AUTENTICACIÓN) (USER IDENTITY
        // MANAGEMENT) ==========

        /**
         * Recupera los detalles de identidad de un usuario por su nombre de usuario
         * (para autenticación OAuth2).
         *
         * @param username el nombre de usuario
         * @return los detalles de identidad del usuario
         */
        @GetExchange(value = "/api/v1/user-identity/get-username", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<UserIdentityDto> getUserIdentityByUsername(@RequestParam("username") String username);

        /**
         * Recupera los roles de cliente para una identidad de usuario.
         *
         * @param userIdentityId el ID de la identidad de usuario
         * @return la lista de roles de cliente
         */
        @GetExchange(value = "/api/v1/user-identity-customer-role/user/{userIdentityId}", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<CustomerRoleDto>> getUserCustomerRoles(
                        @PathVariable("userIdentityId") Long userIdentityId);

        /**
         * Recupera los detalles de identidad de un usuario por su correo electrónico.
         *
         * @param useremail el correo electrónico
         * @return los detalles de identidad del usuario
         */
        @GetExchange(value = "/api/v1/user-identity/get-useremail", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<UserIdentityDto> getUserIdentityByEmail(@RequestParam String useremail);

        /**
         * Recupera una identidad de usuario por su ID.
         *
         * @param id el ID de la identidad de usuario
         * @return los detalles de identidad del usuario
         */
        @GetExchange(value = "/api/v1/user-identity"
                        + SimappeConstant.PATH_GET, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<UserIdentityDto> getUserIdentity(@RequestParam Long id);

        /**
         * Crea una nueva identidad de usuario.
         *
         * @param dto el DTO de la identidad de usuario
         * @return la identidad de usuario creada
         * @throws SimappeException si ocurre un error en el microservicio
         */
        @PostExchange(value = "/api/v1/user-identity"
                        + SimappeConstant.PATH_CREATE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<UserIdentityDto> createUserIdentity(@Valid @RequestBody UserIdentityDto dto)
                        throws SimappeException;

        /**
         * Actualiza una identidad de usuario existente.
         *
         * @param dto el DTO de la identidad de usuario con los cambios
         * @param id  el ID de la identidad a actualizar
         * @return la identidad de usuario actualizada
         */
        @PutExchange(value = "/api/v1/user-identity"
                        + SimappeConstant.PATH_UPDATE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<UserIdentityDto> updateUserIdentity(@Valid @RequestBody UserIdentityDto dto,
                        @RequestParam Long id);

        /**
         * Actualiza la contraseña de una identidad de usuario.
         *
         * @param userId   el ID de la identidad de usuario
         * @param password el objeto de transferencia con la nueva contraseña
         * @return respuesta vacía indicando el éxito de la operación
         * @throws SimappeException si ocurre un error en el microservicio
         */
        @PutExchange(value = "/api/v1/user-identity/update-password", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<Void> updateUserIdentityPassword(@RequestParam Long userId,
                        @RequestBody TransferDto2<String, String> password) throws SimappeException;

        /**
         * Asigna un rol a una identidad de usuario.
         *
         * @param role el objeto de transferencia con los IDs de rol e identidad
         * @return los detalles del rol asignado
         */
        @PutExchange(value = "/api/v1/user-identity/add-role", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<RoleDto> addUserIdentityRole(@RequestBody TransferDto2<Long, Long> role);

        /**
         * Elimina un rol de una identidad de usuario.
         *
         * @param role el objeto de transferencia con los IDs de rol e identidad
         * @return respuesta vacía indicando el éxito de la operación
         */
        @DeleteExchange(value = "/api/v1/user-identity/delete-role", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<Void> deleteUserIdentityRole(@RequestBody TransferDto2<Long, Long> role);

        /**
         * Elimina una identidad de usuario por su ID.
         *
         * @param id el ID de la identidad de usuario a eliminar
         * @return respuesta vacía indicando el éxito de la operación
         */
        @DeleteExchange(value = "/api/v1/user-identity"
                        + SimappeConstant.PATH_DELETE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<Void> deleteUserIdentity(@RequestParam Long id);

        /**
         * Recupera una lista paginada de identidades de usuario.
         *
         * @param requestQuery los parámetros de consulta
         * @return una página de identidades de usuario
         * @throws SimappeException si ocurre un error en el microservicio
         */
        @PostExchange(value = "/api/v1/user-identity"
                        + SimappeConstant.PATH_PAGE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<PageDto<UserIdentityDto>> getUserIdentityPage(
                        @RequestBody SimappeRequestQuery requestQuery)
                        throws SimappeException;

        // ========== GESTIÓN DE CIUDADES (CITY MANAGEMENT) ==========

        /**
         * Verifica la disponibilidad del servicio de ciudades.
         *
         * @return la respuesta con el estado de disponibilidad del servicio
         */
        @GetExchange(value = "/api/v1/city" + SimappeConstant.PATH_INDEX, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<ServiceResponseAvailable> cityIndex();

        /**
         * Recupera todas las ciudades.
         *
         * @return la lista de ciudades
         */
        @GetExchange(value = "/api/v1/city" + SimappeConstant.PATH_READ, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<CityDto>> readCities();

        /**
         * Recupera las ciudades asociadas a un ID de estado.
         *
         * @param stateId el ID del estado
         * @return la lista de ciudades para el estado dado
         */
        @GetExchange(value = "/api/v1/city/read-state", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<CityDto>> readCitiesByState(@RequestParam Long stateId);

        /**
         * Recupera los detalles de una ciudad por su ID.
         *
         * @param id el ID de la ciudad
         * @return los detalles de la ciudad encontrada
         */
        @GetExchange(value = "/api/v1/city" + SimappeConstant.PATH_GET, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<CityDto> getCityId(@RequestParam Long id);

        /**
         * Recupera los detalles de una ciudad por su nombre.
         *
         * @param name el nombre de la ciudad
         * @return los detalles de la ciudad encontrada
         */
        @GetExchange(value = "/api/v1/city/get-name", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<CityDto> getCityByName(@RequestParam String name);

        /**
         * Recupera los detalles de una ciudad por su código de ciudad.
         *
         * @param cityCode el código de la ciudad
         * @return los detalles de la ciudad encontrada
         */
        @GetExchange(value = "/api/v1/city/get-city-code", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<CityDto> getCityByCode(@RequestParam String cityCode);

        /**
         * Crea una nueva ciudad.
         *
         * @param dto el DTO de la ciudad a crear
         * @return la ciudad creada
         * @throws SimappeException si ocurre un error en el microservicio
         */
        @PostExchange(value = "/api/v1/city" + SimappeConstant.PATH_CREATE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<CityDto> createCity(@Valid @RequestBody CityDto dto) throws SimappeException;

        /**
         * Actualiza una ciudad existente.
         *
         * @param dto el DTO de la ciudad con los cambios
         * @param id  el ID de la ciudad a actualizar
         * @return la ciudad actualizada
         */
        @PutExchange(value = "/api/v1/city" + SimappeConstant.PATH_UPDATE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<CityDto> updateCity(@Valid @RequestBody CityDto dto, @RequestParam Long id);

        /**
         * Elimina una ciudad por su ID.
         *
         * @param id el ID de la ciudad a eliminar
         * @return respuesta vacía indicando el éxito de la operación
         */
        @DeleteExchange(value = "/api/v1/city" + SimappeConstant.PATH_DELETE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<Void> deleteCity(@RequestParam Long id);

        /**
         * Recupera una lista paginada de ciudades.
         *
         * @param requestQuery los parámetros de consulta
         * @return una página de ciudades
         * @throws SimappeException si ocurre un error en el microservicio
         */
        @PostExchange(value = "/api/v1/city" + SimappeConstant.PATH_PAGE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<PageDto<CityDto>> getCityPage(@RequestBody SimappeRequestQuery requestQuery)
                        throws SimappeException;

        // ========== GESTIÓN DE CLIENTES (ESTÁNDAR) (CUSTOMER MANAGEMENT) ==========

        /**
         * Verifica la disponibilidad del servicio de clientes.
         *
         * @return la respuesta con el estado de disponibilidad del servicio
         */
        @GetExchange(value = "/api/v1/customer" + SimappeConstant.PATH_INDEX, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<ServiceResponseAvailable> customerIndex();

        /**
         * Recupera todos los clientes.
         *
         * @return la lista de clientes
         */
        @GetExchange(value = "/api/v1/customer" + SimappeConstant.PATH_READ, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<CustomerDto>> readCustomers();

        /**
         * Recupera los clientes por su tipo.
         *
         * @param customerType el tipo de cliente
         * @return la lista de clientes para el tipo dado
         */
        @GetExchange(value = "/api/v1/customer/read-customer-type", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<CustomerDto>> readCustomersByType(@RequestParam CustomerType customerType);

        /**
         * Recupera los clientes por ID de ciudad.
         *
         * @param cityId el ID de la ciudad
         * @return la lista de clientes en la ciudad dada
         */
        @GetExchange(value = "/api/v1/customer/read-city", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<CustomerDto>> readCustomersByCity(@RequestParam Long cityId);

        /**
         * Recupera los detalles de un cliente por su ID.
         *
         * @param id el ID del cliente
         * @return los detalles del cliente encontrado
         */
        @GetExchange(value = "/api/v1/customer" + SimappeConstant.PATH_GET, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<CustomerDto> getCustomerId(@RequestParam Long id);

        /**
         * Recupera los detalles de un cliente por su nombre.
         *
         * @param customerName el nombre del cliente
         * @return los detalles del cliente encontrado
         */
        @GetExchange(value = "/api/v1/customer/get-customer-name", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<CustomerDto> getCustomerByName(@RequestParam String customerName);

        /**
         * Recupera los detalles de un cliente por su código postal.
         *
         * @param postalCode el código postal
         * @return los detalles del cliente encontrado
         */
        @GetExchange(value = "/api/v1/customer/get-postal-code", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<CustomerDto> getCustomerByPostalCode(@RequestParam String postalCode);

        /**
         * Crea un nuevo cliente.
         *
         * @param dto el DTO del cliente a crear
         * @return el cliente creado
         * @throws SimappeException si ocurre un error en el microservicio
         */
        @PostExchange(value = "/api/v1/customer"
                        + SimappeConstant.PATH_CREATE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<CustomerDto> createCustomer(@Valid @RequestBody CustomerDto dto) throws SimappeException;

        /**
         * Actualiza un cliente existente.
         *
         * @param dto el DTO del cliente con los cambios
         * @param id  el ID del cliente a actualizar
         * @return el cliente actualizado
         */
        @PutExchange(value = "/api/v1/customer"
                        + SimappeConstant.PATH_UPDATE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<CustomerDto> updateCustomer(@Valid @RequestBody CustomerDto dto, @RequestParam Long id);

        /**
         * Elimina un cliente por su ID.
         *
         * @param id el ID del cliente a eliminar
         * @return respuesta vacía indicando el éxito de la operación
         * @throws SimappeException si ocurre un error en el microservicio
         */
        @DeleteExchange(value = "/api/v1/customer"
                        + SimappeConstant.PATH_DELETE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<Void> deleteCustomer(@RequestParam Long id) throws SimappeException;

        /**
         * Recupera una lista paginada de clientes.
         *
         * @param requestQuery los parámetros de consulta
         * @return una página de clientes
         * @throws SimappeException si ocurre un error en el microservicio
         */
        @PostExchange(value = "/api/v1/customer" + SimappeConstant.PATH_PAGE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<PageDto<CustomerDto>> getCustomerPage(@RequestBody SimappeRequestQuery requestQuery)
                        throws SimappeException;

        // ========== GESTIÓN DE CONFIGURACIÓN DE BASE DE DATOS (ESTÁNDAR) (DATABASE
        // CONFIG MANAGEMENT) ==========

        /**
         * Verifica la disponibilidad del servicio.
         *
         * @return la respuesta con el estado de disponibilidad del servicio
         */
        @GetExchange(value = "/api/v2/database-config"
                        + SimappeConstant.PATH_INDEX, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<ServiceResponseAvailable> index();

        /**
         * Recupera todas las configuraciones de base de datos.
         *
         * @return la lista de configuraciones de base de datos
         */
        @GetExchange(value = "/api/v2/database-config"
                        + SimappeConstant.PATH_READ, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<DatabaseConfigV2Dto>> read();

        /**
         * Recupera una configuración de base de datos por su ID.
         *
         * @param id el ID de la configuración de base de datos
         * @return los detalles de la configuración encontrada
         */
        @GetExchange(value = "/api/v2/database-config"
                        + SimappeConstant.PATH_GET, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<DatabaseConfigV2Dto> get(@RequestParam Long id);

        /**
         * Recupera una configuración de base de datos por su código.
         *
         * @param code el código de la configuración
         * @return los detalles de la configuración encontrada
         */
        @GetExchange(value = "/api/v2/database-config/get-code", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<DatabaseConfigV2Dto> getCode(@RequestParam String code);

        /**
         * Crea una nueva configuración de base de datos.
         *
         * @param dto el DTO de la configuración a crear
         * @return la configuración creada
         */
        @PostExchange(value = "/api/v2/database-config"
                        + SimappeConstant.PATH_CREATE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<DatabaseConfigV2Dto> create(@Valid @RequestBody DatabaseConfigV2Dto dto);

        /**
         * Actualiza una configuración de base de datos existente.
         *
         * @param dto el DTO de la configuración con los cambios
         * @param id  el ID de la configuración a actualizar
         * @return la configuración actualizada
         */
        @PutExchange(value = "/api/v2/database-config"
                        + SimappeConstant.PATH_UPDATE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<DatabaseConfigV2Dto> update(@Valid @RequestBody DatabaseConfigV2Dto dto,
                        @RequestParam Long id);

        /**
         * Elimina una configuración de base de datos por su ID.
         *
         * @param id el ID de la configuración a eliminar
         * @return respuesta vacía indicando el éxito de la operación
         */
        @DeleteExchange(value = "/api/v2/database-config"
                        + SimappeConstant.PATH_DELETE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<Void> delete(@RequestParam Long id);

        /**
         * Prueba la conexión para una configuración de base de datos específica.
         *
         * @param id el ID de la configuración de base de datos
         * @return los detalles de la configuración con el resultado de la prueba de
         *         conexión
         */
        @GetExchange(value = "/api/v2/database-config/test-connection", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<DatabaseConfigV2Dto> testConnection(@RequestParam Long id);

        /**
         * Recupera los detalles de conexión a la base de datos utilizando un token de
         * autorización.
         *
         * @param token el token de autorización
         * @return los detalles de conexión encontrados
         * @throws SimappeException si ocurre un error en el microservicio
         */
        @GetExchange(value = "/api/v2/database-config/connection", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<DatabaseConfigV2Dto> getConnection(
                        @RequestHeader(SimappeConstant.AUTHORIZATION) String token)
                        throws SimappeException;

        /**
         * Recupera los detalles de conexión a la base de datos utilizando un token.
         *
         * @param token el token
         * @return los detalles de conexión encontrados
         * @throws SimappeException si ocurre un error en el microservicio
         */
        @GetExchange(value = "/api/v2/database-config/connection-token", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<DatabaseConfigV2Dto> getConnectionByToken(@RequestHeader String token)
                        throws SimappeException;

        /**
         * Recupera los detalles de conexión a la base de datos utilizando un token de
         * ruta.
         *
         * @param token el token de la ruta
         * @return los detalles de conexión encontrados
         * @throws SimappeException si ocurre un error en el microservicio
         */
        @GetExchange(value = "/api/v2/database-config/connection-path-token/{token}", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<DatabaseConfigV2Dto> getConnectionToken(@PathVariable String token)
                        throws SimappeException;

        /**
         * Recupera una lista paginada de configuraciones de base de datos.
         *
         * @param requestQuery los parámetros de consulta
         * @return una página de configuraciones de base de datos
         * @throws SimappeException si ocurre un error en el microservicio
         */
        @PostExchange(value = "/api/v2/database-config"
                        + SimappeConstant.PATH_PAGE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<PageDto<DatabaseConfigV2Dto>> getPage(@RequestBody SimappeRequestQuery requestQuery)
                        throws SimappeException;

        // ========== GESTIÓN DE MENÚS (ESTÁNDAR) (MENU MANAGEMENT) ==========

        /**
         * Recupera todos los elementos del menú.
         *
         * @return la lista de elementos del menú
         */
        @GetExchange(value = "/api/v1/menu" + SimappeConstant.PATH_READ, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<VMenuDto>> readMenu();

        /**
         * Recupera un elemento del menú por su ID.
         *
         * @param id el ID del elemento del menú
         * @return los detalles del elemento encontrado
         */
        @GetExchange(value = "/api/v1/menu" + SimappeConstant.PATH_GET, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<VMenuDto> getMenu(@RequestParam Long id);

        /**
         * Recupera los elementos del menú asociados a un ID de rol.
         *
         * @param roleId el ID del rol
         * @return la lista de elementos del menú para el rol dado
         */
        @GetExchange(value = "/api/v1/menu/read-by-role", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<VMenuDto>> readMenuByRole(@RequestParam Long roleId);

        /**
         * Recupera los elementos del menú asociados a múltiples IDs de roles.
         *
         * @param roleIds la lista de IDs de roles
         * @return la lista de elementos del menú para los roles dados
         */
        @PostExchange(value = "/api/v1/menu/read-by-roles", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<VMenuDto>> readMenuByRoles(@RequestBody List<Long> roleIds);

        /**
         * Recupera los elementos del menú asociados a un ID de aplicación.
         *
         * @param applicationId el ID de la aplicación
         * @return la lista de elementos del menú para la aplicación dada
         */
        @GetExchange(value = "/api/v1/menu/read-by-application", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<VMenuDto>> readMenuByApplication(@RequestParam Long applicationId);

        /**
         * Recupera los elementos del menú asociados a un ID de módulo.
         *
         * @param moduleId el ID del módulo
         * @return la lista de elementos del menú para el módulo dado
         */
        @GetExchange(value = "/api/v1/menu/read-by-module", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<VMenuDto>> readMenuByModule(@RequestParam Long moduleId);

        /**
         * Recupera los elementos del menú asociados a un ID de grupo de opciones.
         *
         * @param optionGroupId el ID del grupo de opciones
         * @return la lista de elementos del menú para el grupo de opciones dado
         */
        @GetExchange(value = "/api/v1/menu/read-by-option-group", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<VMenuDto>> readMenuByOptionGroup(@RequestParam Long optionGroupId);

        /**
         * Recupera los elementos del menú asociados a un ID de opción padre.
         *
         * @param parentOptionId el ID de la opción padre
         * @return la lista de elementos del menú para la opción padre dada
         */
        @GetExchange(value = "/api/v1/menu/read-by-parent", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<VMenuDto>> readMenuByParent(@RequestParam Long parentOptionId);

        /**
         * Busca elementos del menú.
         *
         * @param term el término de búsqueda
         * @return los resultados de la búsqueda
         */
        @GetExchange(value = "/api/v1/menu/search", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<VMenuDto>> searchMenu(@RequestParam String term);

        /**
         * Busca elementos del menú filtrados por roles.
         *
         * @param term    el término de búsqueda
         * @param roleIds la lista de IDs de roles
         * @return los resultados de la búsqueda
         */
        @GetExchange(value = "/api/v1/menu/search-by-roles", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<VMenuDto>> searchMenuByRoles(@RequestParam String term,
                        @RequestParam List<Long> roleIds);

        /**
         * Busca elementos del menú filtrados por módulo.
         *
         * @param term     el término de búsqueda
         * @param moduleId el ID del módulo
         * @return los resultados de la búsqueda
         */
        @GetExchange(value = "/api/v1/menu/search-by-module", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<VMenuDto>> searchMenuByModule(@RequestParam String term,
                        @RequestParam Long moduleId);

        /**
         * Recupera una lista paginada de elementos del menú.
         *
         * @param requestQuery los parámetros de consulta
         * @return una página de elementos del menú
         * @throws SimappeException si ocurre un error en el microservicio
         */
        @PostExchange(value = "/api/v1/menu" + SimappeConstant.PATH_PAGE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<PageDto<VMenuDto>> getMenuPage(@RequestBody SimappeRequestQuery requestQuery)
                        throws SimappeException;

        // ========== OTROS ENDPOINTS MISCELÁNEOS (OTHER MISCELLANEOUS ENDPOINTS)
        // ==========

        /**
         * Recupera los clientes activos con sus empresas activas.
         *
         * @return la lista de clientes activos y sus empresas activas
         */
        @GetExchange(value = "/api/v1/customers-with-companies", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<CustomerDto>> getActiveCustomersWithCompanies();

        /**
         * Recupera las credenciales de usuario por su ID de cliente.
         *
         * @param clientId el ID del cliente (clientId)
         * @return las credenciales del cliente OAuth2
         */
        @GetExchange(value = "/api/v1/oauth/client/get", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<OAuthClientDto> getUserCredentials(@RequestParam String clientId);

        /**
         * Recupera un parámetro del sistema por su ID.
         *
         * @param id el ID del parámetro del sistema
         * @return los detalles del parámetro del sistema encontrado
         */
        @GetExchange(value = "/api/v1/system-parameters/get", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<SystemParametersDto> getSystemParameter(@RequestParam Long id);

        // ========== GESTIÓN DE TOKENS PERPETUOS (ESTÁNDAR) (PERPETUAL TOKEN
        // MANAGEMENT) ==========

        /**
         * Verifica la disponibilidad del servicio de tokens perpetuos.
         *
         * @return la respuesta con el estado de disponibilidad del servicio
         */
        @GetExchange(value = "/api/v1/oauth/perpetual-token"
                        + SimappeConstant.PATH_INDEX, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<ServiceResponseAvailable> perpetualTokenIndex();

        /**
         * Recupera todos los tokens perpetuos.
         *
         * @return la lista de tokens perpetuos
         */
        @GetExchange(value = "/api/v1/oauth/perpetual-token"
                        + SimappeConstant.PATH_READ, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<PerpetualTokenDto>> readPerpetualTokens();

        /**
         * Recupera un token perpetuo por su ID.
         *
         * @param id el ID del token perpetuo
         * @return los detalles del token perpetuo encontrado
         */
        @GetExchange(value = "/api/v1/oauth/perpetual-token"
                        + SimappeConstant.PATH_GET, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<PerpetualTokenDto> getPerpetualToken(@RequestParam String id);

        /**
         * Crea un nuevo token perpetuo.
         *
         * @param dto el DTO del token perpetuo a crear
         * @return el token perpetuo creado
         */
        @PostExchange(value = "/api/v1/oauth/perpetual-token"
                        + SimappeConstant.PATH_CREATE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<PerpetualTokenDto> createPerpetualToken(@Valid @RequestBody PerpetualTokenDto dto);

        /**
         * Actualiza un token perpetuo existente.
         *
         * @param dto el DTO del token perpetuo con los cambios
         * @param id  el ID del token a actualizar
         * @return el token perpetuo actualizado
         */
        @PutExchange(value = "/api/v1/oauth/perpetual-token"
                        + SimappeConstant.PATH_UPDATE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<PerpetualTokenDto> updatePerpetualToken(@Valid @RequestBody PerpetualTokenDto dto,
                        @RequestParam String id);

        /**
         * Elimina un token perpetuo por su ID.
         *
         * @param id el ID del token perpetuo a eliminar
         * @return respuesta vacía indicando el éxito de la operación
         */
        @DeleteExchange(value = "/api/v1/oauth/perpetual-token"
                        + SimappeConstant.PATH_DELETE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<Void> deletePerpetualToken(@RequestParam String id);

        /**
         * Revoca un token perpetuo por su ID.
         *
         * @param id el ID del token perpetuo a revocar
         * @return los detalles del token perpetuo revocado
         */
        @PostExchange(value = "/api/v1/oauth/perpetual-token/revoke", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<PerpetualTokenDto> revokePerpetualToken(@RequestParam String id);

        /**
         * Recupera una lista paginada de tokens perpetuos.
         *
         * @param requestQuery los parámetros de consulta
         * @return una página de tokens perpetuos
         * @throws SimappeException si ocurre un error en el microservicio
         */
        @PostExchange(value = "/api/v1/oauth/perpetual-token"
                        + SimappeConstant.PATH_PAGE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<PageDto<PerpetualTokenDto>> getPerpetualTokenPage(
                        @RequestBody SimappeRequestQuery requestQuery)
                        throws SimappeException;

        /**
         * Recupera los tokens perpetuos asociados a un nombre de usuario.
         *
         * @param username el nombre de usuario
         * @return la lista de tokens perpetuos para el usuario dado
         */
        @GetExchange(value = "/api/v1/oauth/perpetual-token/by-username", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<PerpetualTokenDto>> getPerpetualTokensByUsername(@RequestParam String username);

        /**
         * Recupera los tokens perpetuos activos asociados a un ID de cliente.
         *
         * @param clientId el ID del cliente
         * @return la lista de tokens perpetuos activos para el cliente dado
         */
        @GetExchange(value = "/api/v1/oauth/perpetual-token/by-client", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<PerpetualTokenDto>> getPerpetualTokensByClientId(@RequestParam Long clientId);

        /**
         * Recupera un token perpetuo por su contexto (nombre de usuario, ID de cliente,
         * ID de empresa).
         *
         * @param username  el nombre de usuario
         * @param clientId  el ID del cliente
         * @param companyId el ID de la empresa (opcional)
         * @return el token perpetuo para el contexto específico
         */
        @GetExchange(value = "/api/v1/oauth/perpetual-token/by-context", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<PerpetualTokenDto> getPerpetualTokenByContext(
                        @RequestParam String username,
                        @RequestParam Long clientId,
                        @RequestParam(required = false) Long companyId);

        /**
         * Recupera un token perpetuo por el hash del token.
         *
         * @param tokenHash el hash del token
         * @return los detalles del token perpetuo encontrado
         */
        @GetExchange(value = "/api/v1/oauth/perpetual-token/by-hash", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<PerpetualTokenDto> getPerpetualTokenByHash(@RequestParam String tokenHash);

        // ========== GESTIÓN DE APLICACIONES (ESTÁNDAR) (APPLICATION MANAGEMENT)
        // ==========

        /**
         * Verifica la disponibilidad del servicio de aplicaciones.
         *
         * @return la respuesta con el estado de disponibilidad del servicio
         */
        @GetExchange(value = "/api/v1/application"
                        + SimappeConstant.PATH_INDEX, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<ServiceResponseAvailable> applicationIndex();

        /**
         * Recupera todas las aplicaciones.
         *
         * @return la lista de aplicaciones
         */
        @GetExchange(value = "/api/v1/application"
                        + SimappeConstant.PATH_READ, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<ApplicationDto>> readApplications();

        /**
         * Recupera las aplicaciones asociadas a un tipo de aplicación.
         *
         * @param type el tipo de aplicación
         * @return la lista de aplicaciones para el tipo dado
         */
        @GetExchange(value = "/api/v1/application/read-type", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<ApplicationDto>> readApplicationsByType(@RequestParam ApplicationType type);

        /**
         * Recupera las aplicaciones asociadas a un estado de registro.
         *
         * @param status el estado de registro
         * @return la lista de aplicaciones para el estado dado
         */
        @GetExchange(value = "/api/v1/application/read-status", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<ApplicationDto>> readApplicationsByStatus(@RequestParam RegistrationStatus status);

        /**
         * Recupera los detalles de una aplicación por su ID.
         *
         * @param id el ID de la aplicación
         * @return los detalles de la aplicación encontrada
         */
        @GetExchange(value = "/api/v1/application"
                        + SimappeConstant.PATH_GET, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<ApplicationDto> getApplication(@RequestParam Long id);

        /**
         * Recupera los detalles de una aplicación por su nombre.
         *
         * @param name el nombre de la aplicación
         * @return los detalles de la aplicación encontrada
         */
        @GetExchange(value = "/api/v1/application/get-name", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<ApplicationDto> getApplicationByName(@RequestParam String name);

        /**
         * Recupera los detalles de una aplicación por su código.
         *
         * @param code el código de la aplicación
         * @return los detalles de la aplicación encontrada
         */
        @GetExchange(value = "/api/v1/application/get-code", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<ApplicationDto> getApplicationByCode(@RequestParam String code);

        /**
         * Crea una nueva aplicación.
         *
         * @param dto el DTO de la aplicación a crear
         * @return la aplicación creada
         */
        @PostExchange(value = "/api/v1/application"
                        + SimappeConstant.PATH_CREATE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<ApplicationDto> createApplication(@Valid @RequestBody ApplicationDto dto);

        /**
         * Actualiza una aplicación existente.
         *
         * @param dto el DTO de la aplicación con los cambios
         * @param id  el ID de la aplicación a actualizar
         * @return la aplicación actualizada
         */
        @PutExchange(value = "/api/v1/application"
                        + SimappeConstant.PATH_UPDATE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<ApplicationDto> updateApplication(@Valid @RequestBody ApplicationDto dto,
                        @RequestParam Long id);

        /**
         * Actualiza la configuración de la ruta de la aplicación.
         *
         * @param config el objeto de transferencia con el ID de la aplicación y la ruta
         * @return la aplicación actualizada
         */
        @PutExchange(value = "/api/v1/application/update-path", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<ApplicationDto> updateApplicationPath(
                        @Valid @RequestBody TransferDto2<Long, String> config);

        /**
         * Actualiza la configuración de la aplicación.
         *
         * @param config el objeto de transferencia con el ID de la aplicación y el mapa
         *               de configuración
         * @return la aplicación actualizada
         */
        @PutExchange(value = "/api/v1/application/update-config", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<ApplicationDto> updateApplicationConfig(
                        @RequestBody TransferDto2<Long, Map<String, Object>> config);

        /**
         * Agrega un paquete de módulos a una aplicación.
         *
         * @param config el objeto de transferencia con el ID de la aplicación y el ID
         *               del paquete de módulos
         * @return la aplicación actualizada
         */
        @PutExchange(value = "/api/v1/application/add-module-package", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<ApplicationDto> addApplicationModulePackage(@RequestBody TransferDto2<Long, Long> config);

        /**
         * Elimina un paquete de módulos de una aplicación.
         *
         * @param config el objeto de transferencia con el ID de la aplicación y el ID
         *               del paquete de módulos
         * @return respuesta vacía indicando el éxito de la operación
         */
        @DeleteExchange(value = "/api/v1/application/delete-module-package", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<Void> deleteApplicationModulePackage(@RequestBody TransferDto2<Long, Long> config);

        /**
         * Elimina una aplicación por su ID.
         *
         * @param id el ID de la aplicación a eliminar
         * @return respuesta vacía indicando el éxito de la operación
         */
        @DeleteExchange(value = "/api/v1/application"
                        + SimappeConstant.PATH_DELETE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<Void> deleteApplication(@RequestParam Long id);

        /**
         * Recupera una lista paginada de aplicaciones.
         *
         * @param requestQuery los parámetros de consulta
         * @return una página de aplicaciones
         * @throws SimappeException si ocurre un error en el microservicio
         */
        @PostExchange(value = "/api/v1/application"
                        + SimappeConstant.PATH_PAGE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<PageDto<ApplicationDto>> getApplicationPage(@RequestBody SimappeRequestQuery requestQuery)
                        throws SimappeException;

        // ========== GESTIÓN DE CONTRATOS DE APLICACIÓN (ESTÁNDAR) (APPLICATION
        // CONTRACT MANAGEMENT) ==========

        /**
         * Verifica la disponibilidad del servicio de contratos de aplicación.
         *
         * @return la respuesta con el estado de disponibilidad del servicio
         */
        @GetExchange(value = "/api/v1/application-contract"
                        + SimappeConstant.PATH_INDEX, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<ServiceResponseAvailable> applicationContractIndex();

        /**
         * Recupera todos los contratos de aplicación.
         *
         * @return la lista de contratos de aplicación
         */
        @GetExchange(value = "/api/v1/application-contract"
                        + SimappeConstant.PATH_READ, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<ApplicationContractDto>> readApplicationContracts();

        /**
         * Busca un contrato por su número de contrato.
         *
         * @param contractNumber el número de contrato
         * @return los detalles del contrato de aplicación encontrado
         */
        @GetExchange(value = "/api/v1/application-contract/find-by-number", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<ApplicationContractDto> findContractByNumber(@RequestParam String contractNumber);

        /**
         * Busca contratos asociados a un ID de cliente.
         *
         * @param customerId el ID del cliente
         * @return la lista de contratos para el cliente dado
         */
        @GetExchange(value = "/api/v1/application-contract/find-by-customer", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<ApplicationContractDto>> findContractsByCustomer(@RequestParam Long customerId);

        /**
         * Busca contratos asociados a un ID de aplicación.
         *
         * @param applicationId el ID de la aplicación
         * @return la lista de contratos para la aplicación dada
         */
        @GetExchange(value = "/api/v1/application-contract/find-by-application", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<ApplicationContractDto>> findContractsByApplication(
                        @RequestParam Long applicationId);

        /**
         * Busca contratos filtrados por estado de registro.
         *
         * @param status el estado de registro
         * @return la lista de contratos encontrados
         */
        @GetExchange(value = "/api/v1/application-contract/find-by-status", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<ApplicationContractDto>> findContractsByStatus(
                        @RequestParam RegistrationStatus status);

        /**
         * Busca contratos filtrados por estado del contrato.
         *
         * @param contractStatus el estado del contrato
         * @return la lista de contratos encontrados
         */
        @GetExchange(value = "/api/v1/application-contract/find-by-contract-status", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<ApplicationContractDto>> findContractsByContractStatus(
                        @RequestParam ContractStatus contractStatus);

        /**
         * Busca contratos que han expirado antes de una fecha específica.
         *
         * @param date la fecha de expiración
         * @return la lista de contratos expirados
         */
        @GetExchange(value = "/api/v1/application-contract/find-expired", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<ApplicationContractDto>> findExpiredContracts(@RequestParam Date date);

        /**
         * Busca contratos dentro de un rango de fechas.
         *
         * @param startDate la fecha de inicio
         * @param endDate   la fecha de fin
         * @return la lista de contratos en el rango de fechas dado
         */
        @GetExchange(value = "/api/v1/application-contract/find-by-date-range", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<ApplicationContractDto>> findContractsByDateRange(@RequestParam Date startDate,
                        @RequestParam Date endDate);

        /**
         * Recupera los detalles de un contrato de aplicación por su ID.
         *
         * @param id el ID del contrato
         * @return los detalles del contrato de aplicación encontrado
         */
        @GetExchange(value = "/api/v1/application-contract"
                        + SimappeConstant.PATH_GET, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<ApplicationContractDto> getApplicationContract(@RequestParam Long id);

        /**
         * Crea un nuevo contrato de aplicación.
         *
         * @param dto el DTO del contrato de aplicación a crear
         * @return el contrato de aplicación creado
         */
        @PostExchange(value = "/api/v1/application-contract"
                        + SimappeConstant.PATH_CREATE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<ApplicationContractDto> createApplicationContract(
                        @Valid @RequestBody ApplicationContractDto dto);

        /**
         * Actualiza un contrato de aplicación existente.
         *
         * @param dto el DTO del contrato de aplicación con los cambios
         * @param id  el ID del contrato a actualizar
         * @return el contrato de aplicación actualizado
         */
        @PutExchange(value = "/api/v1/application-contract"
                        + SimappeConstant.PATH_UPDATE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<ApplicationContractDto> updateApplicationContract(
                        @Valid @RequestBody ApplicationContractDto dto, @RequestParam Long id);

        /**
         * Elimina un contrato de aplicación por su ID.
         *
         * @param id el ID del contrato a eliminar
         * @return respuesta vacía indicando el éxito de la operación
         */
        @DeleteExchange(value = "/api/v1/application-contract"
                        + SimappeConstant.PATH_DELETE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<Void> deleteApplicationContract(@RequestParam Long id);

        /**
         * Recupera una lista paginada de contratos de aplicación.
         *
         * @param requestQuery los parámetros de consulta
         * @return una página de contratos de aplicación
         * @throws SimappeException si ocurre un error en el microservicio
         */
        @PostExchange(value = "/api/v1/application-contract"
                        + SimappeConstant.PATH_PAGE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<PageDto<ApplicationContractDto>> getApplicationContractPage(
                        @RequestBody SimappeRequestQuery requestQuery) throws SimappeException;

        // ========== GESTIÓN DE EMPRESAS (ESTÁNDAR) (COMPANY MANAGEMENT) ==========

        /**
         * Verifica la disponibilidad del servicio de empresas.
         *
         * @return la respuesta con el estado de disponibilidad del servicio
         */
        @GetExchange(value = "/api/v1/company" + SimappeConstant.PATH_INDEX, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<ServiceResponseAvailable> companyIndex();

        /**
         * Recupera todas las empresas.
         *
         * @return la lista de empresas
         */
        @GetExchange(value = "/api/v1/company" + SimappeConstant.PATH_READ, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<CompanyDto>> readCompanies();

        /**
         * Recupera las empresas asociadas a un ID de cliente.
         *
         * @param customerId el ID del cliente
         * @return la lista de empresas para el cliente dado
         */
        @GetExchange(value = "/api/v1/company/read-customer", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<CompanyDto>> readCompaniesByCustomer(@RequestParam Long customerId);

        /**
         * Recupera las empresas asociadas a un ID de ciudad.
         *
         * @param cityId el ID de la ciudad
         * @return la lista de empresas en la ciudad dada
         */
        @GetExchange(value = "/api/v1/company/read-city", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<CompanyDto>> readCompaniesByCity(@RequestParam Long cityId);

        /**
         * Recupera los detalles de una empresa por su ID.
         *
         * @param id el ID de la empresa
         * @return los detalles de la empresa encontrada
         */
        @GetExchange(value = "/api/v1/company" + SimappeConstant.PATH_GET, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<CompanyDto> getCompany(@RequestParam Long id);

        /**
         * Recupera los detalles de una empresa por su nombre.
         *
         * @param name el nombre de la empresa
         * @return los detalles de la empresa encontrada
         */
        @GetExchange(value = "/api/v1/company/get-name", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<CompanyDto> getCompanyByName(@RequestParam String name);

        /**
         * Recupera los detalles de una empresa por su NIT.
         *
         * @param nit el NIT de la empresa
         * @return los detalles de la empresa encontrada
         */
        @GetExchange(value = "/api/v1/company/get-nit", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<CompanyDto> getCompanyByNit(@RequestParam String nit);

        /**
         * Recupera los detalles de una empresa por su correo electrónico.
         *
         * @param email el correo electrónico de la empresa
         * @return los detalles de la empresa encontrada
         */
        @GetExchange(value = "/api/v1/company/get-email", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<CompanyDto> getCompanyByEmail(@RequestParam String email);

        /**
         * Recupera los detalles de una empresa por su código postal.
         *
         * @param postalCode el código postal
         * @return los detalles de la empresa encontrada
         */
        @GetExchange(value = "/api/v1/company/get-postal-code", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<CompanyDto> getCompanyByPostalCode(@RequestParam String postalCode);

        /**
         * Crea una nueva empresa.
         *
         * @param dto el DTO de la empresa a crear
         * @return la empresa creada
         * @throws SimappeException si ocurre un error en el microservicio
         */
        @PostExchange(value = "/api/v1/company"
                        + SimappeConstant.PATH_CREATE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<CompanyDto> createCompany(@Valid @RequestBody CompanyDto dto) throws SimappeException;

        /**
         * Actualiza una empresa existente.
         *
         * @param dto el DTO de la empresa con los cambios
         * @param id  el ID de la empresa a actualizar
         * @return la empresa actualizada
         */
        @PutExchange(value = "/api/v1/company" + SimappeConstant.PATH_UPDATE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<CompanyDto> updateCompany(@Valid @RequestBody CompanyDto dto, @RequestParam Long id);

        /**
         * Actualiza el cliente asociado a una empresa.
         *
         * @param companyId  el ID de la empresa
         * @param customerId el ID del cliente
         * @return la empresa actualizada
         */
        @PutExchange(value = "/api/v1/company/update-customer", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<CompanyDto> updateCompanyCustomer(@RequestParam Long companyId,
                        @RequestParam Long customerId);

        /**
         * Elimina una empresa por su ID.
         *
         * @param id el ID de la empresa a eliminar
         * @return respuesta vacía indicando el éxito de la operación
         */
        @DeleteExchange(value = "/api/v1/company"
                        + SimappeConstant.PATH_DELETE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<Void> deleteCompany(@RequestParam Long id);

        /**
         * Recupera una lista paginada de empresas.
         *
         * @param requestQuery los parámetros de consulta
         * @return una página de empresas
         * @throws SimappeException si ocurre un error en el microservicio
         */
        @PostExchange(value = "/api/v1/company" + SimappeConstant.PATH_PAGE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<PageDto<CompanyDto>> getCompanyPage(@RequestBody SimappeRequestQuery requestQuery)
                        throws SimappeException;

        // ========== COMPANY PARAMETERS MANAGEMENT ==========

        // ========== GESTIÓN DE PARÁMETROS DE EMPRESA (ESTÁNDAR) (COMPANY PARAMETERS
        // MANAGEMENT) ==========

        /**
         * Verifica la disponibilidad del servicio de parámetros de empresa.
         *
         * @return la respuesta con el estado de disponibilidad del servicio
         */
        @GetExchange(value = "/api/v1/company-parameters"
                        + SimappeConstant.PATH_INDEX, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<ServiceResponseAvailable> companyParametersIndex();

        /**
         * Recupera todos los parámetros de empresa.
         *
         * @return la lista de parámetros de empresa
         */
        @GetExchange(value = "/api/v1/company-parameters"
                        + SimappeConstant.PATH_READ, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<CompanyParametersDto>> readCompanyParameters();

        /**
         * Recupera los parámetros de empresa por su estado de activación.
         *
         * @param active el flag de activación
         * @return la lista de parámetros de empresa
         */
        @GetExchange(value = "/api/v1/company-parameters/read-active", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<CompanyParametersDto>> readCompanyParametersByActive(@RequestParam Boolean active);

        /**
         * Recupera los parámetros de empresa por ID de parámetros de plantilla.
         *
         * @param templateParametersId el ID de los parámetros de plantilla
         * @return la lista de parámetros de empresa
         */
        @GetExchange(value = "/api/v1/company-parameters/read-template-parameters", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<CompanyParametersDto>> readCompanyParametersByTemplateId(
                        @RequestParam Long templateParametersId);

        /**
         * Recupera los parámetros de empresa por ID de empresa.
         *
         * @param companyId el ID de la empresa
         * @return la lista de parámetros de empresa
         */
        @GetExchange(value = "/api/v1/company-parameters/read-company", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<CompanyParametersDto>> readCompanyParametersByCompanyId(
                        @RequestParam Long companyId);

        /**
         * Recupera los parámetros de empresa por su ID.
         *
         * @param id el ID de los parámetros de empresa
         * @return los detalles de los parámetros de empresa encontrados
         */
        @GetExchange(value = "/api/v1/company-parameters"
                        + SimappeConstant.PATH_GET, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<CompanyParametersDto> getCompanyParameters(@RequestParam Long id);

        /**
         * Crea nuevos parámetros de empresa.
         *
         * @param dto el DTO de los parámetros de empresa a crear
         * @return los parámetros de empresa creados
         * @throws SimappeException si ocurre un error en el microservicio
         */
        @PostExchange(value = "/api/v1/company-parameters"
                        + SimappeConstant.PATH_CREATE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<CompanyParametersDto> createCompanyParameters(
                        @Valid @RequestBody CompanyParametersDto dto)
                        throws SimappeException;

        /**
         * Inicializa los parámetros de empresa desde una plantilla.
         *
         * @param companyId el ID de la empresa
         * @return respuesta vacía indicando el éxito de la operación
         * @throws SimappeException si ocurre un error en el microservicio
         */
        @PostExchange(value = "/api/v1/company-parameters/init-parameters", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<Void> initCompanyParameters(@RequestParam Long companyId) throws SimappeException;

        /**
         * Actualiza parámetros de empresa existentes.
         *
         * @param dto el DTO de los parámetros de empresa con los cambios
         * @param id  el ID de los parámetros de empresa a actualizar
         * @return los parámetros de empresa actualizados
         */
        @PutExchange(value = "/api/v1/company-parameters"
                        + SimappeConstant.PATH_UPDATE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<CompanyParametersDto> updateCompanyParameters(
                        @Valid @RequestBody CompanyParametersDto dto,
                        @RequestParam Long id);

        /**
         * Elimina parámetros de empresa por su ID.
         *
         * @param id el ID de los parámetros de empresa a eliminar
         * @return respuesta vacía indicando el éxito de la operación
         */
        @DeleteExchange(value = "/api/v1/company-parameters"
                        + SimappeConstant.PATH_DELETE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<Void> deleteCompanyParameters(@RequestParam Long id);

        /**
         * Recupera una lista paginada de parámetros de empresa.
         *
         * @param requestQuery los parámetros de consulta
         * @return una página de parámetros de empresa
         * @throws SimappeException si ocurre un error en el microservicio
         */
        @PostExchange(value = "/api/v1/company-parameters"
                        + SimappeConstant.PATH_PAGE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<PageDto<CompanyParametersDto>> getCompanyParametersPage(
                        @RequestBody SimappeRequestQuery requestQuery) throws SimappeException;

        // ========== GESTIÓN DE CONSECUTIVOS (ESTÁNDAR) (CONSECUTIVE MANAGEMENT)
        // ==========

        /**
         * Verifica la disponibilidad del servicio de consecutivos.
         *
         * @return la respuesta con el estado de disponibilidad del servicio
         */
        @GetExchange(value = "/api/v1/consecutive"
                        + SimappeConstant.PATH_INDEX, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<ServiceResponseAvailable> consecutiveIndex();

        /**
         * Recupera todos los consecutivos.
         *
         * @return la lista de consecutivos
         */
        @GetExchange(value = "/api/v1/consecutive"
                        + SimappeConstant.PATH_READ, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<ConsecutiveDto>> readConsecutives();

        /**
         * Recupera los consecutivos por su estado de registro.
         *
         * @param status el estado de registro
         * @return la lista de consecutivos
         */
        @GetExchange(value = "/api/v1/consecutive/read-status", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<ConsecutiveDto>> readConsecutivesByStatus(@RequestParam RegistrationStatus status);

        /**
         * Recupera todos los consecutivos activos.
         *
         * @return la lista de consecutivos activos
         */
        @GetExchange(value = "/api/v1/consecutive/read-active", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<ConsecutiveDto>> readActiveConsecutives();

        /**
         * Recupera los consecutivos asociados a un ID de rol.
         *
         * @param roleId el ID del rol
         * @return la lista de consecutivos para el rol dado
         */
        @GetExchange(value = "/api/v1/consecutive/read-by-role", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<ConsecutiveDto>> readConsecutivesByRole(@RequestParam Long roleId);

        /**
         * Recupera los consecutivos asociados a un ámbito (scope).
         *
         * @param scope el ámbito del consecutivo
         * @return la lista de consecutivos para el ámbito dado
         */
        @GetExchange(value = "/api/v1/consecutive/read-by-scope", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<ConsecutiveDto>> readConsecutivesByScope(@RequestParam ConsecutiveScope scope);

        /**
         * Recupera los detalles de un consecutivo por su ID.
         *
         * @param id el ID del consecutivo
         * @return los detalles del consecutivo encontrado
         */
        @GetExchange(value = "/api/v1/consecutive"
                        + SimappeConstant.PATH_GET, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<ConsecutiveDto> getConsecutive(@RequestParam Long id);

        /**
         * Recupera los detalles de un consecutivo por su código.
         *
         * @param code el código del consecutivo
         * @return los detalles del consecutivo encontrado
         */
        @GetExchange(value = "/api/v1/consecutive/get-code", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<ConsecutiveDto> getConsecutiveByCode(@RequestParam String code);

        /**
         * Crea un nuevo consecutivo.
         *
         * @param dto el DTO del consecutivo a crear
         * @return el consecutivo creado
         */
        @PostExchange(value = "/api/v1/consecutive"
                        + SimappeConstant.PATH_CREATE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<ConsecutiveDto> createConsecutive(@Valid @RequestBody ConsecutiveDto dto);

        /**
         * Actualiza un consecutivo existente.
         *
         * @param dto el DTO del consecutivo con los cambios
         * @param id  el ID del consecutivo a actualizar
         * @return el consecutivo actualizado
         */
        @PutExchange(value = "/api/v1/consecutive"
                        + SimappeConstant.PATH_UPDATE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<ConsecutiveDto> updateConsecutive(@Valid @RequestBody ConsecutiveDto dto,
                        @RequestParam Long id);

        /**
         * Elimina un consecutivo por su ID.
         *
         * @param id el ID del consecutivo a eliminar
         * @return respuesta vacía indicando el éxito de la operación
         */
        @DeleteExchange(value = "/api/v1/consecutive"
                        + SimappeConstant.PATH_DELETE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<Void> deleteConsecutive(@RequestParam Long id);

        /**
         * Recupera el siguiente valor para un consecutivo dado su código.
         *
         * @param code el código del consecutivo
         * @return el siguiente valor formateado
         */
        @GetExchange(value = "/api/v1/consecutive/next-value", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<String> getConsecutiveNextValue(@RequestParam String code);

        /**
         * Valida un consecutivo.
         *
         * @param dto el DTO del consecutivo a validar
         * @return el resultado de la validación
         */
        @PostExchange(value = "/api/v1/consecutive/validate", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<Boolean> validateConsecutive(@Valid @RequestBody ConsecutiveDto dto);

        /**
         * Agrega un rol a un consecutivo.
         *
         * @param config el objeto de transferencia con el ID del consecutivo y el ID
         *               del rol
         * @return el consecutivo actualizado
         */
        @PutExchange(value = "/api/v1/consecutive/add-role", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<ConsecutiveDto> addConsecutiveRole(@RequestBody TransferDto2<Long, Long> config);

        /**
         * Elimina un rol de un consecutivo.
         *
         * @param config el objeto de transferencia con el ID del consecutivo y el ID
         *               del rol
         * @return respuesta vacía indicando el éxito de la operación
         */
        @DeleteExchange(value = "/api/v1/consecutive/remove-role", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<Void> removeConsecutiveRole(@RequestBody TransferDto2<Long, Long> config);

        /**
         * Agrega un cliente a un consecutivo.
         *
         * @param config el objeto de transferencia con el ID del consecutivo y el ID
         *               del cliente
         * @return el consecutivo actualizado
         */
        @PutExchange(value = "/api/v1/consecutive/add-customer", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<ConsecutiveDto> addConsecutiveCustomer(@RequestBody TransferDto2<Long, Long> config);

        /**
         * Elimina un cliente de un consecutivo.
         *
         * @param config el objeto de transferencia con el ID del consecutivo y el ID
         *               del cliente
         * @return respuesta vacía indicando el éxito de la operación
         */
        @DeleteExchange(value = "/api/v1/consecutive/remove-customer", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<Void> removeConsecutiveCustomer(@RequestBody TransferDto2<Long, Long> config);

        /**
         * Recupera una lista paginada de consecutivos.
         *
         * @param requestQuery los parámetros de consulta
         * @return una página de consecutivos
         * @throws SimappeException si ocurre un error en el microservicio
         */
        @PostExchange(value = "/api/v1/consecutive"
                        + SimappeConstant.PATH_PAGE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<PageDto<ConsecutiveDto>> getConsecutivePage(@RequestBody SimappeRequestQuery requestQuery)
                        throws SimappeException;

        // ========== GESTIÓN DE PAÍSES (ESTÁNDAR) (COUNTRY MANAGEMENT) ==========

        /**
         * Verifica la disponibilidad del servicio de países.
         *
         * @return la respuesta con el estado de disponibilidad del servicio
         */
        @GetExchange(value = "/api/v1/country" + SimappeConstant.PATH_INDEX, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<ServiceResponseAvailable> countryIndex();

        /**
         * Recupera todos los países.
         *
         * @return la lista de países
         */
        @GetExchange(value = "/api/v1/country" + SimappeConstant.PATH_READ, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<CountryDto>> readCountries();

        /**
         * Recupera los detalles de un país por su ID.
         *
         * @param id el ID del país
         * @return los detalles del país encontrado
         */
        @GetExchange(value = "/api/v1/country" + SimappeConstant.PATH_GET, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<CountryDto> getCountry(@RequestParam Long id);

        /**
         * Recupera los detalles de un país por su nombre.
         *
         * @param name el nombre del país
         * @return los detalles del país encontrado
         */
        @GetExchange(value = "/api/v1/country/get-name", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<CountryDto> getCountryByName(@RequestParam String name);

        /**
         * Recupera los detalles de un país por su código de país.
         *
         * @param countryCode el código del país
         * @return los detalles del país encontrado
         */
        @GetExchange(value = "/api/v1/country/get-country-code", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<CountryDto> getCountryByCode(@RequestParam String countryCode);

        /**
         * Crea un nuevo país.
         *
         * @param dto el DTO del país a crear
         * @return el país creado
         * @throws SimappeException si ocurre un error en el microservicio
         */
        @PostExchange(value = "/api/v1/country"
                        + SimappeConstant.PATH_CREATE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<CountryDto> createCountry(@Valid @RequestBody CountryDto dto) throws SimappeException;

        /**
         * Actualiza un país existente.
         *
         * @param dto el DTO del país con los cambios
         * @param id  el ID del país a actualizar
         * @return el país actualizado
         */
        @PutExchange(value = "/api/v1/country" + SimappeConstant.PATH_UPDATE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<CountryDto> updateCountry(@Valid @RequestBody CountryDto dto, @RequestParam Long id);

        /**
         * Elimina un país por su ID.
         *
         * @param id el ID del país a eliminar
         * @return respuesta vacía indicando el éxito de la operación
         */
        @DeleteExchange(value = "/api/v1/country"
                        + SimappeConstant.PATH_DELETE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<Void> deleteCountry(@RequestParam Long id);

        /**
         * Recupera una lista paginada de países.
         *
         * @param requestQuery los parámetros de consulta
         * @return una página de países
         * @throws SimappeException si ocurre un error en el microservicio
         */
        @PostExchange(value = "/api/v1/country" + SimappeConstant.PATH_PAGE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<PageDto<CountryDto>> getCountryPage(@RequestBody SimappeRequestQuery requestQuery)
                        throws SimappeException;

        // ========== GESTIÓN DE PARÁMETROS DE CLIENTE (ESTÁNDAR) (CUSTOMER PARAMETERS
        // MANAGEMENT) ==========

        /**
         * Verifica la disponibilidad del servicio de parámetros de cliente.
         *
         * @return la respuesta con el estado de disponibilidad del servicio
         */
        @GetExchange(value = "/api/v1/customer-parameters"
                        + SimappeConstant.PATH_INDEX, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<ServiceResponseAvailable> customerParametersIndex();

        /**
         * Recupera todos los parámetros de cliente.
         *
         * @return la lista de parámetros de cliente
         */
        @GetExchange(value = "/api/v1/customer-parameters"
                        + SimappeConstant.PATH_READ, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<CustomerParametersDto>> readCustomerParameters();

        /**
         * Recupera los parámetros de cliente por su estado de activación.
         *
         * @param active el flag de activación
         * @return la lista de parámetros de cliente
         */
        @GetExchange(value = "/api/v1/customer-parameters/read-active", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<CustomerParametersDto>> readCustomerParametersByActive(@RequestParam Boolean active);

        /**
         * Recupera los parámetros de cliente por ID de parámetros de plantilla.
         *
         * @param templateParametersId el ID de los parámetros de plantilla
         * @return la lista de parámetros de cliente
         */
        @GetExchange(value = "/api/v1/customer-parameters/read-template-parameters", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<CustomerParametersDto>> readCustomerParametersByTemplateId(
                        @RequestParam Long templateParametersId);

        /**
         * Recupera los parámetros de cliente por ID de cliente.
         *
         * @param customerId el ID del cliente
         * @return la lista de parámetros de cliente
         */
        @GetExchange(value = "/api/v1/customer-parameters/read-customer", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<CustomerParametersDto>> readCustomerParametersByCustomerId(
                        @RequestParam Long customerId);

        /**
         * Recupera los detalles de los parámetros de cliente por su ID.
         *
         * @param id el ID de los parámetros de cliente
         * @return los detalles de los parámetros de cliente encontrados
         */
        @GetExchange(value = "/api/v1/customer-parameters"
                        + SimappeConstant.PATH_GET, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<CustomerParametersDto> getCustomerParameters(@RequestParam Long id);

        /**
         * Crea nuevos parámetros de cliente.
         *
         * @param dto el DTO de los parámetros de cliente a crear
         * @return los parámetros de cliente creados
         * @throws SimappeException si ocurre un error en el microservicio
         */
        @PostExchange(value = "/api/v1/customer-parameters"
                        + SimappeConstant.PATH_CREATE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<CustomerParametersDto> createCustomerParameters(
                        @Valid @RequestBody CustomerParametersDto dto)
                        throws SimappeException;

        /**
         * Inicializa los parámetros de cliente desde una plantilla.
         *
         * @param customerId el ID del cliente
         * @return respuesta vacía indicando el éxito de la operación
         * @throws SimappeException si ocurre un error en el microservicio
         */
        @PostExchange(value = "/api/v1/customer-parameters/init-parameters", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<Void> initCustomerParameters(@RequestParam Long customerId) throws SimappeException;

        /**
         * Actualiza parámetros de cliente existentes.
         *
         * @param dto el DTO de los parámetros de cliente con los cambios
         * @param id  el ID de los parámetros de cliente a actualizar
         * @return los parámetros de cliente actualizados
         */
        @PutExchange(value = "/api/v1/customer-parameters"
                        + SimappeConstant.PATH_UPDATE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<CustomerParametersDto> updateCustomerParameters(
                        @Valid @RequestBody CustomerParametersDto dto,
                        @RequestParam Long id);

        /**
         * Elimina parámetros de cliente por su ID.
         *
         * @param id el ID de los parámetros de cliente a eliminar
         * @return respuesta vacía indicando el éxito de la operación
         */
        @DeleteExchange(value = "/api/v1/customer-parameters"
                        + SimappeConstant.PATH_DELETE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<Void> deleteCustomerParameters(@RequestParam Long id);

        /**
         * Recupera una lista paginada de parámetros de cliente.
         *
         * @param requestQuery los parámetros de consulta
         * @return una página de parámetros de cliente
         * @throws SimappeException si ocurre un error en el microservicio
         */
        @PostExchange(value = "/api/v1/customer-parameters"
                        + SimappeConstant.PATH_PAGE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<PageDto<CustomerParametersDto>> getCustomerParametersPage(
                        @RequestBody SimappeRequestQuery requestQuery) throws SimappeException;

        // ========== GESTIÓN DE RUTAS DE API (ESTÁNDAR) (API ROUTE MANAGEMENT)
        // ==========

        /**
         * Verifica la disponibilidad del servicio de rutas de API.
         *
         * @return la respuesta con el estado de disponibilidad del servicio
         */
        @GetExchange(value = "/api/v1/api-route"
                        + SimappeConstant.PATH_INDEX, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<ServiceResponseAvailable> apiRouteIndex();

        /**
         * Recupera todas las rutas de API.
         *
         * @return la lista de rutas de API
         */
        @GetExchange(value = "/api/v1/api-route" + SimappeConstant.PATH_READ, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<ApiRouteDto>> readApiRoutes();

        /**
         * Recupera las rutas de API por su estado de registro.
         *
         * @param status el estado de registro
         * @return la lista de rutas de API
         */
        @GetExchange(value = "/api/v1/api-route/read-status", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<ApiRouteDto>> readApiRoutesByStatus(@RequestParam RegistrationStatus status);

        /**
         * Recupera las rutas de API por su método HTTP.
         *
         * @param method el método HTTP
         * @return la lista de rutas de API
         */
        @GetExchange(value = "/api/v1/api-route/read-method", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<ApiRouteDto>> readApiRoutesByMethod(@RequestParam String method);

        /**
         * Recupera los detalles de una ruta de API por su ID.
         *
         * @param id el ID de la ruta de API
         * @return los detalles de la ruta de API encontrada
         */
        @GetExchange(value = "/api/v1/api-route" + SimappeConstant.PATH_GET, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<ApiRouteDto> getApiRoute(@RequestParam Long id);

        /**
         * Recupera los detalles de una ruta de API por su identificador de ruta.
         *
         * @param routeIdentifier el identificador de la ruta
         * @return los detalles de la ruta de API encontrada
         */
        @GetExchange(value = "/api/v1/api-route/get-route-identifier", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<ApiRouteDto> getApiRouteByIdentifier(@RequestParam String routeIdentifier);

        /**
         * Crea una nueva ruta de API.
         *
         * @param dto el DTO de la ruta de API a crear
         * @return la ruta de API creada
         */
        @PostExchange(value = "/api/v1/api-route"
                        + SimappeConstant.PATH_CREATE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<ApiRouteDto> createApiRoute(@Valid @RequestBody ApiRouteDto dto);

        /**
         * Actualiza una ruta de API existente.
         *
         * @param dto el DTO de la ruta de API con los cambios
         * @param id  el ID de la ruta de API a actualizar
         * @return la ruta de API actualizada
         */
        @PutExchange(value = "/api/v1/api-route"
                        + SimappeConstant.PATH_UPDATE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<ApiRouteDto> updateApiRoute(@Valid @RequestBody ApiRouteDto dto, @RequestParam Long id);

        /**
         * Elimina una ruta de API por su ID.
         *
         * @param id el ID de la ruta de API a eliminar
         * @return respuesta vacía indicando el éxito de la operación
         */
        @DeleteExchange(value = "/api/v1/api-route"
                        + SimappeConstant.PATH_DELETE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<Void> deleteApiRoute(@RequestParam Long id);

        /**
         * Recupera una lista paginada de rutas de API.
         *
         * @param requestQuery los parámetros de consulta
         * @return una página de rutas de API
         * @throws SimappeException si ocurre un error en el microservicio
         */
        @PostExchange(value = "/api/v1/api-route"
                        + SimappeConstant.PATH_PAGE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<PageDto<ApiRouteDto>> getApiRoutePage(@RequestBody SimappeRequestQuery requestQuery)
                        throws SimappeException;

        // ========== GESTIÓN DE VENTANAS DE MANTENIMIENTO Y ESTADO (MAINTENANCE WINDOW
        // AND STATUS) ==========

        /**
         * Recupera las ventanas de mantenimiento activas.
         *
         * @return la lista de ventanas de mantenimiento activas
         */
        @GetExchange(value = "/api/v1/maintenance-windows/active", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<MaintenanceWindowDto>> getActiveMaintenanceWindow();

        /**
         * Recupera las ventanas de mantenimiento activas filtradas por scope (customer/company).
         *
         * @param customerId ID del cliente (puede ser null para global)
         * @param companyId  ID de la empresa (puede ser null)
         * @return la lista de ventanas de mantenimiento activas para el scope dado
         */
        @GetExchange(value = "/api/v1/maintenance-windows/active-for-scope", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<MaintenanceWindowDto>> getActiveMaintenanceWindowForScope(
                @RequestParam(required = false) Long customerId,
                @RequestParam(required = false) Long companyId);

        /**
         * Recupera el estado de un endpoint específico.
         *
         * @param path   la ruta del endpoint
         * @param method el método HTTP
         * @return los detalles del estado del endpoint
         */
        @GetExchange(value = "/api/v1/endpoints/status", accept = MediaType.APPLICATION_JSON_VALUE)
        ApiEndpointDto getEndpointStatus(@RequestParam String path, @RequestParam String method);
}