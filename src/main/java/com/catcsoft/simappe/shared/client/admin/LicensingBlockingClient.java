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
import java.util.Map;

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
import com.catcsoft.simappe.model.admin.dto.appconfig.ApplicationDto;
import com.catcsoft.simappe.model.admin.dto.contract.ApplicationContractDto;
import com.catcsoft.simappe.model.admin.dto.subscription.CustomerSubscriptionDto;
import com.catcsoft.simappe.model.admin.dto.subscription.LicenseDto;
import com.catcsoft.simappe.model.admin.dto.subscription.SubscriptionPlanDto;

import jakarta.validation.Valid;

/**
 * Cliente WebClient modular para operaciones de licenciamiento y suscripciones
 * en Simappe.
 * Proporciona métodos para la gestión de aplicaciones, contratos, licencias,
 * planes de suscripción y suscripciones de clientes.
 * 
 * <p>
 * Este cliente forma parte de la arquitectura modular v2 que reemplaza
 * gradualmente
 * al SimappeAdminWebClient monolítico, ofreciendo una interfaz especializada
 * para licensing.
 * 
 * @author torrescamargo@gmail.com
 * @since 2026-01-18
 * @version 2.6.0
 */
@HttpExchange("simappe-admin")
public interface LicensingBlockingClient {

    // ========== GESTIÓN DE APLICACIONES (APPLICATION MANAGEMENT) ==========

    @GetExchange(value = "/api/v1/application" + SimappeConstant.PATH_READ, accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<List<?>> readApplications();

    @GetExchange(value = "/api/v1/application/read-type", accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<List<?>> readApplicationsByType(@RequestParam String type);

    @GetExchange(value = "/api/v1/application/read-status", accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<List<?>> readApplicationsByStatus(@RequestParam String status);

    @GetExchange(value = "/api/v1/application" + SimappeConstant.PATH_GET, accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<?> getApplication(@RequestParam Long id);

    @GetExchange(value = "/api/v1/application/get-name", accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<?> getApplicationByName(@RequestParam String name);

    @GetExchange(value = "/api/v1/application/get-code", accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<?> getApplicationByCode(@RequestParam String code);

    @PostExchange(value = "/api/v1/application"
            + SimappeConstant.PATH_CREATE, accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<?> createApplication(@Valid @RequestBody ApplicationDto dto) throws SimappeException;

    @PutExchange(value = "/api/v1/application" + SimappeConstant.PATH_UPDATE, accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<?> updateApplication(@Valid @RequestBody ApplicationDto dto, @RequestParam Long id);

    @PutExchange(value = "/api/v1/application/update-path", accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<?> updateApplicationPath(@RequestBody Map<String, Object> config);

    @PutExchange(value = "/api/v1/application/update-config", accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<?> updateApplicationConfig(@RequestBody Map<String, Object> config);

    @PutExchange(value = "/api/v1/application/add-module-package", accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<?> addModulePackageToApplication(@RequestBody Map<String, Object> data);

    @DeleteExchange(value = "/api/v1/application/delete-module-package", accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<Void> deleteModulePackageFromApplication(@RequestBody Map<String, Object> data);

    @DeleteExchange(value = "/api/v1/application"
            + SimappeConstant.PATH_DELETE, accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<Void> deleteApplication(@RequestParam Long id);

    @PostExchange(value = "/api/v1/application" + SimappeConstant.PATH_PAGE, accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<PageDto<?>> getApplicationPage(@RequestBody SimappeRequestQuery requestQuery)
            throws SimappeException;

    // ========== GESTIÓN DE CONTRATOS DE APLICACIÓN (APPLICATION CONTRACT
    // MANAGEMENT) ==========

    @GetExchange(value = "/api/v1/application-contract"
            + SimappeConstant.PATH_READ, accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<List<ApplicationContractDto>> readApplicationContracts();

    @GetExchange(value = "/api/v1/application-contract/find-by-number", accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<ApplicationContractDto> findContractByNumber(@RequestParam String number);

    @GetExchange(value = "/api/v1/application-contract/find-by-customer", accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<List<ApplicationContractDto>> findContractsByCustomer(@RequestParam Long customerId);

    @GetExchange(value = "/api/v1/application-contract/find-by-application", accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<List<ApplicationContractDto>> findContractsByApplication(@RequestParam Long applicationId);

    @GetExchange(value = "/api/v1/application-contract/find-by-status", accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<List<ApplicationContractDto>> findContractsByStatus(@RequestParam String status);

    @GetExchange(value = "/api/v1/application-contract/find-by-contract-status", accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<List<ApplicationContractDto>> findContractsByContractStatus(
            @RequestParam String contractStatus);

    @GetExchange(value = "/api/v1/application-contract/find-expired", accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<List<ApplicationContractDto>> findExpiredContracts();

    @GetExchange(value = "/api/v1/application-contract/find-by-date-range", accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<List<ApplicationContractDto>> findContractsByDateRange(@RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate);

    @GetExchange(value = "/api/v1/application-contract"
            + SimappeConstant.PATH_GET, accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<ApplicationContractDto> getApplicationContract(@RequestParam Long id);

    @PostExchange(value = "/api/v1/application-contract"
            + SimappeConstant.PATH_CREATE, accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<ApplicationContractDto> createApplicationContract(
            @Valid @RequestBody ApplicationContractDto dto) throws SimappeException;

    @PutExchange(value = "/api/v1/application-contract"
            + SimappeConstant.PATH_UPDATE, accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<ApplicationContractDto> updateApplicationContract(
            @Valid @RequestBody ApplicationContractDto dto, @RequestParam Long id);

    @DeleteExchange(value = "/api/v1/application-contract"
            + SimappeConstant.PATH_DELETE, accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<Void> deleteApplicationContract(@RequestParam Long id);

    @PostExchange(value = "/api/v1/application-contract"
            + SimappeConstant.PATH_PAGE, accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<PageDto<ApplicationContractDto>> getApplicationContractPage(
            @RequestBody SimappeRequestQuery requestQuery) throws SimappeException;

    // ========== GESTIÓN DE LICENCIAS (LICENSE MANAGEMENT) ==========

    @GetExchange(value = "/api/v1/license/read", accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<List<LicenseDto>> readLicenses();

    @GetExchange(value = "/api/v1/license/read-by-customer", accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<List<LicenseDto>> readLicensesByCustomer(@RequestParam Long customerId);

    @GetExchange(value = "/api/v1/license/read-by-company", accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<List<LicenseDto>> readLicensesByCompany(@RequestParam Long companyId);

    @GetExchange(value = "/api/v1/license/get", accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<LicenseDto> getLicense(@RequestParam Long id);

    @GetExchange(value = "/api/v1/license/get-by-key", accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<LicenseDto> getLicenseByKey(@RequestParam String key);

    @GetExchange(value = "/api/v1/license/validate", accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<Boolean> validateLicense(@RequestParam String key);

    @PostExchange(value = "/api/v1/license/generate", accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<LicenseDto> generateLicense(@Valid @RequestBody LicenseDto dto) throws SimappeException;

    @PutExchange(value = "/api/v1/license/revoke", accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<LicenseDto> revokeLicense(@RequestParam Long id);

    @PostExchange(value = "/api/v1/license/create", accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<LicenseDto> createLicense(@Valid @RequestBody LicenseDto dto) throws SimappeException;

    @PutExchange(value = "/api/v1/license/update", accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<LicenseDto> updateLicense(@Valid @RequestBody LicenseDto dto, @RequestParam Long id);

    @DeleteExchange(value = "/api/v1/license/delete", accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<Void> deleteLicense(@RequestParam Long id);

    @PostExchange(value = "/api/v1/license/page", accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<PageDto<LicenseDto>> getLicensePage(@RequestBody SimappeRequestQuery requestQuery)
            throws SimappeException;

    // ========== GESTIÓN DE PLANES DE SUSCRIPCIÓN (SUBSCRIPTION PLAN MANAGEMENT)
    // ==========

    @GetExchange(value = "/api/v1/subscription-plan/read", accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<List<SubscriptionPlanDto>> readSubscriptionPlans();

    @GetExchange(value = "/api/v1/subscription-plan/read-active", accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<List<SubscriptionPlanDto>> readActiveSubscriptionPlans();

    @GetExchange(value = "/api/v1/subscription-plan/available", accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<List<SubscriptionPlanDto>> getAvailableSubscriptionPlans();

    @GetExchange(value = "/api/v1/subscription-plan/get", accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<SubscriptionPlanDto> getSubscriptionPlan(@RequestParam Long id);

    @GetExchange(value = "/api/v1/subscription-plan/get-by-code", accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<SubscriptionPlanDto> getSubscriptionPlanByCode(@RequestParam String code);

    @PostExchange(value = "/api/v1/subscription-plan/create", accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<SubscriptionPlanDto> createSubscriptionPlan(@Valid @RequestBody SubscriptionPlanDto dto)
            throws SimappeException;

    @PutExchange(value = "/api/v1/subscription-plan/update", accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<SubscriptionPlanDto> updateSubscriptionPlan(@Valid @RequestBody SubscriptionPlanDto dto,
            @RequestParam Long id);

    @DeleteExchange(value = "/api/v1/subscription-plan/delete", accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<Void> deleteSubscriptionPlan(@RequestParam Long id);

    @PostExchange(value = "/api/v1/subscription-plan/page", accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<PageDto<SubscriptionPlanDto>> getSubscriptionPlanPage(
            @RequestBody SimappeRequestQuery requestQuery) throws SimappeException;

    // ========== GESTIÓN DE SUSCRIPCIONES DE CLIENTES (CUSTOMER SUBSCRIPTION
    // MANAGEMENT) ==========

    @GetExchange(value = "/api/v1/customer-subscription/read", accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<List<CustomerSubscriptionDto>> readCustomerSubscriptions();

    @GetExchange(value = "/api/v1/customer-subscription/read-by-customer", accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<List<CustomerSubscriptionDto>> readSubscriptionsByCustomer(@RequestParam Long customerId);

    @GetExchange(value = "/api/v1/customer-subscription/read-active-by-customer", accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<List<CustomerSubscriptionDto>> readActiveSubscriptionsByCustomer(
            @RequestParam Long customerId);

    @GetExchange(value = "/api/v1/customer-subscription/get", accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<CustomerSubscriptionDto> getCustomerSubscription(@RequestParam Long id);

    @PostExchange(value = "/api/v1/customer-subscription/subscribe", accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<CustomerSubscriptionDto> subscribe(@Valid @RequestBody CustomerSubscriptionDto dto)
            throws SimappeException;

    @PutExchange(value = "/api/v1/customer-subscription/cancel", accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<CustomerSubscriptionDto> cancelSubscription(@RequestParam Long id);

    @PostExchange(value = "/api/v1/customer-subscription/create", accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<CustomerSubscriptionDto> createCustomerSubscription(
            @Valid @RequestBody CustomerSubscriptionDto dto) throws SimappeException;

    @PutExchange(value = "/api/v1/customer-subscription/update", accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<CustomerSubscriptionDto> updateCustomerSubscription(
            @Valid @RequestBody CustomerSubscriptionDto dto, @RequestParam Long id);

    @DeleteExchange(value = "/api/v1/customer-subscription/delete", accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<Void> deleteCustomerSubscription(@RequestParam Long id);

    @PostExchange(value = "/api/v1/customer-subscription/page", accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<PageDto<CustomerSubscriptionDto>> getCustomerSubscriptionPage(
            @RequestBody SimappeRequestQuery requestQuery) throws SimappeException;
}
