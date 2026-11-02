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
import com.catcsoft.simappe.model.admin.dto.CompanyDto;
import com.catcsoft.simappe.model.admin.dto.CustomerDto;
import com.catcsoft.simappe.model.admin.dto.parameter.CompanyParametersDto;
import com.catcsoft.simappe.model.admin.dto.parameter.CustomerParametersDto;
import com.catcsoft.simappe.model.admin.enums.CustomerType;

import jakarta.validation.Valid;

/**
 * Cliente WebClient modular para operaciones organizacionales en Simappe.
 * Proporciona métodos para la gestión de clientes (customers), empresas
 * (companies) y subsidiarias.
 * 
 * <p>
 * Este cliente forma parte de la arquitectura modular v2 que reemplaza
 * gradualmente
 * al SimappeAdminWebClient monolítico, ofreciendo una interfaz especializada
 * para organization.
 * 
 * @author torrescamargo@gmail.com
 * @since 2026-01-18
 * @version 2.6.0
 */
@HttpExchange("simappe-admin")
public interface OrganizationBlockingClient {

    // ========== GESTIÓN DE CLIENTES (CUSTOMER MANAGEMENT) ==========

    @GetExchange(value = "/api/v1/customer" + SimappeConstant.PATH_READ, accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<List<CustomerDto>> readCustomers();

    @GetExchange(value = "/api/v1/customer/read-customer-type", accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<List<CustomerDto>> readCustomersByType(@RequestParam CustomerType customerType);

    @GetExchange(value = "/api/v1/customer/read-city", accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<List<CustomerDto>> readCustomersByCity(@RequestParam Long cityId);

    @GetExchange(value = "/api/v1/customer" + SimappeConstant.PATH_GET, accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<CustomerDto> getCustomerId(@RequestParam Long id);

    /**
     * Same as {@link #getCustomerId(Long)} but propagates the caller's
     * Authorization header (must be a "Bearer &lt;jwt&gt;" string) so the gateway's
     * AuthFilter validates the JWT and forwards the request. Use from internal
     * flows that already hold a valid access token (e.g. selectCompany /
     * addCompany in oauth2).
     */
    @GetExchange(value = "/api/v1/customer" + SimappeConstant.PATH_GET, accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<CustomerDto> getCustomerId(
            @RequestHeader(SimappeConstant.AUTHORIZATION) String authorization,
            @RequestParam Long id);

    @GetExchange(value = "/api/v1/customer/get-customer-name", accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<CustomerDto> getCustomerByName(@RequestParam String customerName);

    @GetExchange(value = "/api/v1/customer/get-postal-code", accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<CustomerDto> getCustomerByPostalCode(@RequestParam String postalCode);

    @PostExchange(value = "/api/v1/customer" + SimappeConstant.PATH_CREATE, accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<CustomerDto> createCustomer(@Valid @RequestBody CustomerDto dto) throws SimappeException;

    @PutExchange(value = "/api/v1/customer" + SimappeConstant.PATH_UPDATE, accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<CustomerDto> updateCustomer(@Valid @RequestBody CustomerDto dto, @RequestParam Long id);

    @DeleteExchange(value = "/api/v1/customer" + SimappeConstant.PATH_DELETE, accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<Void> deleteCustomer(@RequestParam Long id) throws SimappeException;

    @PostExchange(value = "/api/v1/customer" + SimappeConstant.PATH_PAGE, accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<PageDto<CustomerDto>> getCustomerPage(@RequestBody SimappeRequestQuery requestQuery)
            throws SimappeException;

    // ========== GESTIÓN DE PARÁMETROS DE CLIENTE (CUSTOMER PARAMETERS) ==========

    @GetExchange(value = "/api/v1/customer-parameters/read-active", accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<List<CustomerParametersDto>> readActiveCustomerParameters();

    @GetExchange(value = "/api/v1/customer-parameters/read-template-parameters", accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<List<CustomerParametersDto>> readTemplateCustomerParameters();

    @GetExchange(value = "/api/v1/customer-parameters/read-customer", accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<List<CustomerParametersDto>> readCustomerParametersByCustomer(@RequestParam Long customerId);

    @GetExchange(value = "/api/v1/customer-parameters"
            + SimappeConstant.PATH_GET, accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<CustomerParametersDto> getCustomerParametersId(@RequestParam Long id);

    @PostExchange(value = "/api/v1/customer-parameters/init-parameters", accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<CustomerParametersDto> initializeCustomerParameters(
            @Valid @RequestBody CustomerParametersDto dto) throws SimappeException;

    // ========== GESTIÓN DE EMPRESAS (COMPANY MANAGEMENT) ==========

    @GetExchange(value = "/api/v1/company" + SimappeConstant.PATH_READ, accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<List<CompanyDto>> readCompanies();

    @GetExchange(value = "/api/v1/company/read-customer", accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<List<CompanyDto>> readCompaniesByCustomer(@RequestParam Long customerId);

    @GetExchange(value = "/api/v1/company/read-city", accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<List<CompanyDto>> readCompaniesByCity(@RequestParam Long cityId);

    @GetExchange(value = "/api/v1/company" + SimappeConstant.PATH_GET, accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<CompanyDto> getCompany(@RequestParam Long id);

    /**
     * Same as {@link #getCompany(Long)} but propagates the caller's
     * Authorization header (must be a "Bearer &lt;jwt&gt;" string) so the gateway's
     * AuthFilter validates the JWT and forwards the request. Use from internal
     * flows that already hold a valid access token (e.g. selectCompany /
     * addCompany in oauth2).
     */
    @GetExchange(value = "/api/v1/company" + SimappeConstant.PATH_GET, accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<CompanyDto> getCompany(
            @RequestHeader(SimappeConstant.AUTHORIZATION) String authorization,
            @RequestParam Long id);

    @GetExchange(value = "/api/v1/company/get-name", accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<CompanyDto> getCompanyByName(@RequestParam String name);

    @GetExchange(value = "/api/v1/company/get-nit", accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<CompanyDto> getCompanyByNit(@RequestParam String nit);

    @GetExchange(value = "/api/v1/company/get-email", accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<CompanyDto> getCompanyByEmail(@RequestParam String email);

    @GetExchange(value = "/api/v1/company/get-postal-code", accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<CompanyDto> getCompanyByPostalCode(@RequestParam String postalCode);

    @PostExchange(value = "/api/v1/company" + SimappeConstant.PATH_CREATE, accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<CompanyDto> createCompany(@Valid @RequestBody CompanyDto dto) throws SimappeException;

    @PutExchange(value = "/api/v1/company" + SimappeConstant.PATH_UPDATE, accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<CompanyDto> updateCompany(@Valid @RequestBody CompanyDto dto, @RequestParam Long id);

    @PutExchange(value = "/api/v1/company/update-customer", accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<CompanyDto> updateCompanyCustomer(@RequestParam Long companyId,
            @RequestParam Long customerId);

    @DeleteExchange(value = "/api/v1/company" + SimappeConstant.PATH_DELETE, accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<Void> deleteCompany(@RequestParam Long id);

    @PostExchange(value = "/api/v1/company" + SimappeConstant.PATH_PAGE, accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<PageDto<CompanyDto>> getCompanyPage(@RequestBody SimappeRequestQuery requestQuery)
            throws SimappeException;

    // ========== GESTIÓN DE PARÁMETROS DE EMPRESA (COMPANY PARAMETERS) ==========

    @GetExchange(value = "/api/v1/company-parameters/read-active", accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<List<CompanyParametersDto>> readActiveCompanyParameters();

    @GetExchange(value = "/api/v1/company-parameters/read-template-parameters", accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<List<CompanyParametersDto>> readTemplateCompanyParameters();

    @GetExchange(value = "/api/v1/company-parameters/read-company", accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<List<CompanyParametersDto>> readCompanyParametersByCompany(@RequestParam Long companyId);

    @GetExchange(value = "/api/v1/company-parameters"
            + SimappeConstant.PATH_GET, accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<CompanyParametersDto> getCompanyParametersId(@RequestParam Long id);

    @PostExchange(value = "/api/v1/company-parameters/init-parameters", accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<CompanyParametersDto> initializeCompanyParameters(
            @Valid @RequestBody CompanyParametersDto dto) throws SimappeException;
}
