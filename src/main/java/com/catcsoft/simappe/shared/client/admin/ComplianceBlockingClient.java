/*
 * Copyright (c) 2025 CatcSoft
 *
 * This software is the confidential and proprietary information of CatcSoft
 * ("Confidential Information"You shall not disclose such Confidential
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
import com.catcsoft.simappe.model.admin.dto.gdpr.DataRetentionPolicyDto;
import com.catcsoft.simappe.model.admin.dto.gdpr.GdprConsentRecordDto;
import com.catcsoft.simappe.model.admin.dto.gdpr.GdprDataBreachDto;
import com.catcsoft.simappe.model.admin.dto.gdpr.GdprDataRequestDto;
import com.catcsoft.simappe.model.admin.dto.gdpr.GdprProcessingActivityDto;

import jakarta.validation.Valid;

/**
 * Cliente WebClient modular para operaciones de cumplimiento y GDPR en Simappe.
 * Proporciona métodos para la gestión de consentimientos, brechas de datos,
 * solicitudes de datos,
 * actividades de procesamiento GDPR y políticas de retención de datos.
 * 
 * <p>
 * Este cliente forma parte de la arquitectura modular v2 que reemplaza
 * gradualmente
 * al SimappeAdminWebClient monolítico, ofreciendo una interfaz especializada
 * para compliance.
 * 
 * @author torrescamargo@gmail.com
 * @since 2026-01-18
 * @version 2.6.0
 */
@HttpExchange("simappe-admin")
public interface ComplianceBlockingClient {

    // ========== GESTIÓN DE POLÍTICAS DE RETENCIÓN DE DATOS (DATA RETENTION POLICY
    // MANAGEMENT) ==========

    @GetExchange(value = "/api/v1/data-retention-policy"
            + SimappeConstant.PATH_READ, accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<List<DataRetentionPolicyDto>> readDataRetentionPolicies();

    @GetExchange(value = "/api/v1/data-retention-policy"
            + SimappeConstant.PATH_GET, accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<DataRetentionPolicyDto> getDataRetentionPolicyId(@RequestParam Long id);

    @PostExchange(value = "/api/v1/data-retention-policy"
            + SimappeConstant.PATH_CREATE, accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<DataRetentionPolicyDto> createDataRetentionPolicy(
            @Valid @RequestBody DataRetentionPolicyDto dto) throws SimappeException;

    @PutExchange(value = "/api/v1/data-retention-policy"
            + SimappeConstant.PATH_UPDATE, accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<DataRetentionPolicyDto> updateDataRetentionPolicy(
            @Valid @RequestBody DataRetentionPolicyDto dto, @RequestParam Long id);

    @DeleteExchange(value = "/api/v1/data-retention-policy"
            + SimappeConstant.PATH_DELETE, accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<Void> deleteDataRetentionPolicy(@RequestParam Long id);

    @PostExchange(value = "/api/v1/data-retention-policy"
            + SimappeConstant.PATH_PAGE, accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<PageDto<DataRetentionPolicyDto>> getDataRetentionPolicyPage(
            @RequestBody SimappeRequestQuery requestQuery) throws SimappeException;

    // ========== GESTIÓN DE REGISTROS DE CONSENTIMIENTO GDPR (GDPR CONSENT RECORD
    // MANAGEMENT) ==========

    @GetExchange(value = "/api/v1/gdpr-consent-record"
            + SimappeConstant.PATH_READ, accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<List<GdprConsentRecordDto>> readGdprConsentRecords();

    @GetExchange(value = "/api/v1/gdpr-consent-record"
            + SimappeConstant.PATH_GET, accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<GdprConsentRecordDto> getGdprConsentRecordId(@RequestParam Long id);

    @PostExchange(value = "/api/v1/gdpr-consent-record"
            + SimappeConstant.PATH_CREATE, accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<GdprConsentRecordDto> createGdprConsentRecord(@Valid @RequestBody GdprConsentRecordDto dto)
            throws SimappeException;

    @PutExchange(value = "/api/v1/gdpr-consent-record"
            + SimappeConstant.PATH_UPDATE, accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<GdprConsentRecordDto> updateGdprConsentRecord(@Valid @RequestBody GdprConsentRecordDto dto,
            @RequestParam Long id);

    @DeleteExchange(value = "/api/v1/gdpr-consent-record"
            + SimappeConstant.PATH_DELETE, accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<Void> deleteGdprConsentRecord(@RequestParam Long id);

    @PostExchange(value = "/api/v1/gdpr-consent-record"
            + SimappeConstant.PATH_PAGE, accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<PageDto<GdprConsentRecordDto>> getGdprConsentRecordPage(
            @RequestBody SimappeRequestQuery requestQuery) throws SimappeException;

    // ========== GESTIÓN DE BRECHAS DE DATOS GDPR (GDPR DATA BREACH MANAGEMENT)
    // ==========

    @GetExchange(value = "/api/v1/gdpr-data-breach"
            + SimappeConstant.PATH_READ, accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<List<GdprDataBreachDto>> readGdprDataBreaches();

    @GetExchange(value = "/api/v1/gdpr-data-breach"
            + SimappeConstant.PATH_GET, accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<GdprDataBreachDto> getGdprDataBreachId(@RequestParam Long id);

    @PostExchange(value = "/api/v1/gdpr-data-breach"
            + SimappeConstant.PATH_CREATE, accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<GdprDataBreachDto> createGdprDataBreach(@Valid @RequestBody GdprDataBreachDto dto)
            throws SimappeException;

    @PutExchange(value = "/api/v1/gdpr-data-breach"
            + SimappeConstant.PATH_UPDATE, accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<GdprDataBreachDto> updateGdprDataBreach(@Valid @RequestBody GdprDataBreachDto dto,
            @RequestParam Long id);

    @DeleteExchange(value = "/api/v1/gdpr-data-breach"
            + SimappeConstant.PATH_DELETE, accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<Void> deleteGdprDataBreach(@RequestParam Long id);

    @PostExchange(value = "/api/v1/gdpr-data-breach"
            + SimappeConstant.PATH_PAGE, accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<PageDto<GdprDataBreachDto>> getGdprDataBreachPage(
            @RequestBody SimappeRequestQuery requestQuery) throws SimappeException;

    // ========== GESTIÓN DE SOLICITUDES DE DATOS GDPR (GDPR DATA REQUEST
    // MANAGEMENT) ==========

    @GetExchange(value = "/api/v1/gdpr-data-request"
            + SimappeConstant.PATH_READ, accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<List<GdprDataRequestDto>> readGdprDataRequests();

    @GetExchange(value = "/api/v1/gdpr-data-request"
            + SimappeConstant.PATH_GET, accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<GdprDataRequestDto> getGdprDataRequestId(@RequestParam Long id);

    @PostExchange(value = "/api/v1/gdpr-data-request"
            + SimappeConstant.PATH_CREATE, accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<GdprDataRequestDto> createGdprDataRequest(@Valid @RequestBody GdprDataRequestDto dto)
            throws SimappeException;

    @PutExchange(value = "/api/v1/gdpr-data-request"
            + SimappeConstant.PATH_UPDATE, accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<GdprDataRequestDto> updateGdprDataRequest(@Valid @RequestBody GdprDataRequestDto dto,
            @RequestParam Long id);

    @DeleteExchange(value = "/api/v1/gdpr-data-request"
            + SimappeConstant.PATH_DELETE, accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<Void> deleteGdprDataRequest(@RequestParam Long id);

    @PostExchange(value = "/api/v1/gdpr-data-request"
            + SimappeConstant.PATH_PAGE, accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<PageDto<GdprDataRequestDto>> getGdprDataRequestPage(
            @RequestBody SimappeRequestQuery requestQuery) throws SimappeException;

    // ========== GESTIÓN DE ACTIVIDADES DE PROCESAMIENTO GDPR (GDPR PROCESSING
    // ACTIVITY MANAGEMENT) ==========

    @GetExchange(value = "/api/v1/gdpr-processing-activity"
            + SimappeConstant.PATH_READ, accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<List<GdprProcessingActivityDto>> readGdprProcessingActivities();

    @GetExchange(value = "/api/v1/gdpr-processing-activity"
            + SimappeConstant.PATH_GET, accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<GdprProcessingActivityDto> getGdprProcessingActivityId(@RequestParam Long id);

    @PostExchange(value = "/api/v1/gdpr-processing-activity"
            + SimappeConstant.PATH_CREATE, accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<GdprProcessingActivityDto> createGdprProcessingActivity(
            @Valid @RequestBody GdprProcessingActivityDto dto) throws SimappeException;

    @PutExchange(value = "/api/v1/gdpr-processing-activity"
            + SimappeConstant.PATH_UPDATE, accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<GdprProcessingActivityDto> updateGdprProcessingActivity(
            @Valid @RequestBody GdprProcessingActivityDto dto, @RequestParam Long id);

    @DeleteExchange(value = "/api/v1/gdpr-processing-activity"
            + SimappeConstant.PATH_DELETE, accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<Void> deleteGdprProcessingActivity(@RequestParam Long id);

    @PostExchange(value = "/api/v1/gdpr-processing-activity"
            + SimappeConstant.PATH_PAGE, accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<PageDto<GdprProcessingActivityDto>> getGdprProcessingActivityPage(
            @RequestBody SimappeRequestQuery requestQuery) throws SimappeException;
}
