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
import org.springframework.web.bind.annotation.PathVariable;
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
import com.catcsoft.simappe.model.admin.dto.webhook.WebhookDeliveryDto;
import com.catcsoft.simappe.model.admin.dto.webhook.WebhookEndpointDto;
import com.catcsoft.simappe.model.admin.dto.webhook.WebhookEventDto;

import jakarta.validation.Valid;
import reactor.core.publisher.Mono;

/**
 * Cliente WebClient modular para operaciones de webhooks en Simappe.
 * Proporciona métodos para la gestión de endpoints, eventos y entregas de
 * webhooks.
 * 
 * <p>
 * Este cliente forma parte de la arquitectura modular v2 que reemplaza
 * gradualmente
 * al SimappeAdminWebClient monolítico, ofreciendo una interfaz especializada
 * para webhooks.
 * 
 * @author torrescamargo@gmail.com
 * @since 2026-01-18
 * @version 2.6.0
 */
@HttpExchange("simappe-admin")
public interface WebhookReactiveClient {

    // ========== GESTIÓN DE ENDPOINTS DE WEBHOOK (WEBHOOK ENDPOINT MANAGEMENT)
    // ==========

    @GetExchange(value = "/api/v1/webhook-endpoint/read", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<List<WebhookEndpointDto>>> readWebhookEndpoints();

    @GetExchange(value = "/api/v1/webhook-endpoint/get", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<WebhookEndpointDto>> getWebhookEndpoint(@RequestParam Long id);

    @PostExchange(value = "/api/v1/webhook-endpoint/create", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<WebhookEndpointDto>> createWebhookEndpoint(@Valid @RequestBody WebhookEndpointDto dto)
            throws SimappeException;

    @PutExchange(value = "/api/v1/webhook-endpoint/update", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<WebhookEndpointDto>> updateWebhookEndpoint(@Valid @RequestBody WebhookEndpointDto dto,
            @RequestParam Long id);

    @DeleteExchange(value = "/api/v1/webhook-endpoint/delete", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<Void>> deleteWebhookEndpoint(@RequestParam Long id);

    @PostExchange(value = "/api/v1/webhook-endpoint/page", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<PageDto<WebhookEndpointDto>>> getWebhookEndpointPage(
            @RequestBody SimappeRequestQuery requestQuery) throws SimappeException;

    @GetExchange(value = "/api/v1/webhook-endpoint/by-customer", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<List<WebhookEndpointDto>>> findEndpointsByCustomer(@RequestParam Long customerId);

    @PostExchange(value = "/api/v1/webhook-endpoint/{id}/enable", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<WebhookEndpointDto>> enableWebhookEndpoint(@PathVariable Long id);

    @PostExchange(value = "/api/v1/webhook-endpoint/{id}/disable", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<WebhookEndpointDto>> disableWebhookEndpoint(@PathVariable Long id);

    // ========== GESTIÓN DE EVENTOS DE WEBHOOK (WEBHOOK EVENT MANAGEMENT)
    // ==========

    @GetExchange(value = "/api/v1/webhook-event/read", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<List<WebhookEventDto>>> readWebhookEvents();

    @GetExchange(value = "/api/v1/webhook-event/get", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<WebhookEventDto>> getWebhookEvent(@RequestParam Long id);

    @PostExchange(value = "/api/v1/webhook-event/create", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<WebhookEventDto>> createWebhookEvent(@Valid @RequestBody WebhookEventDto dto)
            throws SimappeException;

    @PutExchange(value = "/api/v1/webhook-event/update", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<WebhookEventDto>> updateWebhookEvent(@Valid @RequestBody WebhookEventDto dto,
            @RequestParam Long id);

    @DeleteExchange(value = "/api/v1/webhook-event/delete", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<Void>> deleteWebhookEvent(@RequestParam Long id);

    @PostExchange(value = "/api/v1/webhook-event/page", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<PageDto<WebhookEventDto>>> getWebhookEventPage(@RequestBody SimappeRequestQuery requestQuery)
            throws SimappeException;

    @GetExchange(value = "/api/v1/webhook-event/by-category", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<List<WebhookEventDto>>> findEventsByCategory(@RequestParam String category);

    @GetExchange(value = "/api/v1/webhook-event/active", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<List<WebhookEventDto>>> findActiveWebhookEvents();

    // ========== GESTIÓN DE ENTREGAS DE WEBHOOK (WEBHOOK DELIVERY MANAGEMENT)
    // ==========

    @GetExchange(value = "/api/v1/webhook-delivery/read", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<List<WebhookDeliveryDto>>> readWebhookDeliveries();

    @GetExchange(value = "/api/v1/webhook-delivery/get", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<WebhookDeliveryDto>> getWebhookDelivery(@RequestParam Long id);

    @PostExchange(value = "/api/v1/webhook-delivery/create", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<WebhookDeliveryDto>> createWebhookDelivery(@Valid @RequestBody WebhookDeliveryDto dto)
            throws SimappeException;

    @PutExchange(value = "/api/v1/webhook-delivery/update", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<WebhookDeliveryDto>> updateWebhookDelivery(@Valid @RequestBody WebhookDeliveryDto dto,
            @RequestParam Long id);

    @DeleteExchange(value = "/api/v1/webhook-delivery/delete", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<Void>> deleteWebhookDelivery(@RequestParam Long id);

    @PostExchange(value = "/api/v1/webhook-delivery/page", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<PageDto<WebhookDeliveryDto>>> getWebhookDeliveryPage(
            @RequestBody SimappeRequestQuery requestQuery) throws SimappeException;

    @GetExchange(value = "/api/v1/webhook-delivery/by-endpoint", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<List<WebhookDeliveryDto>>> findDeliveriesByEndpoint(@RequestParam Long endpointId);

    @GetExchange(value = "/api/v1/webhook-delivery/by-event", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<List<WebhookDeliveryDto>>> findDeliveriesByEvent(@RequestParam Long eventId);

    @PostExchange(value = "/api/v1/webhook-delivery/{id}/retry", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<WebhookDeliveryDto>> retryWebhookDelivery(@PathVariable Long id);
}
