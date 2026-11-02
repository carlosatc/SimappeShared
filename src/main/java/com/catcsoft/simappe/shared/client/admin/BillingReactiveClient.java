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

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

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
import com.catcsoft.simappe.model.admin.dto.billing.InvoiceDto;
import com.catcsoft.simappe.model.admin.dto.billing.InvoiceItemDto;
import com.catcsoft.simappe.model.admin.dto.billing.PaymentDto;
import com.catcsoft.simappe.model.admin.dto.billing.PaymentMethodDto;
import com.catcsoft.simappe.model.admin.dto.billing.stripe.StripeCustomerDto;
import com.catcsoft.simappe.model.admin.dto.billing.stripe.StripePaymentIntentDto;
import com.catcsoft.simappe.model.admin.dto.billing.stripe.StripePaymentMethodDto;
import com.catcsoft.simappe.model.admin.dto.billing.stripe.StripeRefundDto;
import com.catcsoft.simappe.model.admin.enums.CurrencyCode;

import jakarta.validation.Valid;
import reactor.core.publisher.Mono;

/**
 * Cliente WebClient modular para operaciones de facturación y pagos en Simappe.
 * Proporciona métodos para la gestión de facturas,pagos, métodos de pago e
 * integración con Stripe.
 * 
 * <p>
 * Este cliente forma parte de la arquitectura modular v2 que reemplaza
 * gradualmente
 * al SimappeAdminWebClient monolítico, ofreciendo una interfaz especializada
 * para billing.
 * 
 * @author torrescamargo@gmail.com
 * @since 2026-01-18
 * @version 2.6.0
 */
@HttpExchange("simappe-admin")
public interface BillingReactiveClient {

    // ========== GESTIÓN DE FACTURAS (INVOICE MANAGEMENT) ==========

    @GetExchange(value = "/api/v1/invoice" + SimappeConstant.PATH_READ, accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<List<InvoiceDto>>> readInvoices();

    @GetExchange(value = "/api/v1/invoice/read-by-customer", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<List<InvoiceDto>>> readInvoicesByCustomer(@RequestParam Long customerId);

    @GetExchange(value = "/api/v1/invoice/read-by-status", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<List<InvoiceDto>>> readInvoicesByStatus(@RequestParam String status);

    @GetExchange(value = "/api/v1/invoice/find-overdue", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<List<InvoiceDto>>> findOverdueInvoices();

    @GetExchange(value = "/api/v1/invoice" + SimappeConstant.PATH_GET, accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<InvoiceDto>> getInvoice(@RequestParam Long id);

    @PostExchange(value = "/api/v1/invoice/generate-from-subscription", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<InvoiceDto>> generateInvoiceFromSubscription(@RequestParam Long subscriptionId)
            throws SimappeException;

    @PutExchange(value = "/api/v1/invoice/mark-as-paid", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<InvoiceDto>> markInvoiceAsPaid(@RequestParam Long invoiceId,
            @RequestParam(required = false) Long paymentId) throws SimappeException;

    @PostExchange(value = "/api/v1/invoice" + SimappeConstant.PATH_CREATE, accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<InvoiceDto>> createInvoice(@Valid @RequestBody InvoiceDto dto) throws SimappeException;

    @PutExchange(value = "/api/v1/invoice" + SimappeConstant.PATH_UPDATE, accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<InvoiceDto>> updateInvoice(@Valid @RequestBody InvoiceDto dto, @RequestParam Long id);

    @DeleteExchange(value = "/api/v1/invoice" + SimappeConstant.PATH_DELETE, accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<Void>> deleteInvoice(@RequestParam Long id);

    @PostExchange(value = "/api/v1/invoice" + SimappeConstant.PATH_PAGE, accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<PageDto<InvoiceDto>>> getInvoicePage(@RequestBody SimappeRequestQuery requestQuery)
            throws SimappeException;

    // ========== GESTIÓN DE ÍTEMS DE FACTURA (INVOICE ITEM MANAGEMENT) ==========

    @GetExchange(value = "/api/v1/invoice-item" + SimappeConstant.PATH_READ, accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<List<InvoiceItemDto>>> readInvoiceItems();

    @GetExchange(value = "/api/v1/invoice-item/read-by-invoice", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<List<InvoiceItemDto>>> readInvoiceItemsByInvoice(@RequestParam Long invoiceId);

    @GetExchange(value = "/api/v1/invoice-item" + SimappeConstant.PATH_GET, accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<InvoiceItemDto>> getInvoiceItem(@RequestParam Long id);

    @PostExchange(value = "/api/v1/invoice-item"
            + SimappeConstant.PATH_CREATE, accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<InvoiceItemDto>> createInvoiceItem(@Valid @RequestBody InvoiceItemDto dto)
            throws SimappeException;

    @PutExchange(value = "/api/v1/invoice-item"
            + SimappeConstant.PATH_UPDATE, accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<InvoiceItemDto>> updateInvoiceItem(@Valid @RequestBody InvoiceItemDto dto,
            @RequestParam Long id);

    @DeleteExchange(value = "/api/v1/invoice-item"
            + SimappeConstant.PATH_DELETE, accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<Void>> deleteInvoiceItem(@RequestParam Long id);

    @PostExchange(value = "/api/v1/invoice-item" + SimappeConstant.PATH_PAGE, accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<PageDto<InvoiceItemDto>>> getInvoiceItemPage(@RequestBody SimappeRequestQuery requestQuery)
            throws SimappeException;

    // ========== GESTIÓN DE PAGOS (PAYMENT MANAGEMENT) ==========

    @GetExchange(value = "/api/v1/payment" + SimappeConstant.PATH_READ, accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<List<PaymentDto>>> readPayments();

    @GetExchange(value = "/api/v1/payment/read-by-customer", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<List<PaymentDto>>> readPaymentsByCustomer(@RequestParam Long customerId);

    @GetExchange(value = "/api/v1/payment/read-by-invoice", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<List<PaymentDto>>> readPaymentsByInvoice(@RequestParam Long invoiceId);

    @GetExchange(value = "/api/v1/payment/read-successful-by-customer", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<List<PaymentDto>>> readSuccessfulPaymentsByCustomer(@RequestParam Long customerId);

    @GetExchange(value = "/api/v1/payment" + SimappeConstant.PATH_GET, accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<PaymentDto>> getPayment(@RequestParam Long id);

    @PostExchange(value = "/api/v1/payment/process-payment", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<PaymentDto>> processPayment(@RequestParam Long invoiceId,
            @RequestParam Long paymentMethodId,
            @RequestParam BigDecimal amount) throws SimappeException;

    @PostExchange(value = "/api/v1/payment/register-payment", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<PaymentDto>> registerPayment(@RequestParam Long invoiceId, @RequestParam BigDecimal amount,
            @RequestParam(required = false) Long paymentMethodId,
            @RequestParam(required = false) String reference)
            throws SimappeException;

    @PostExchange(value = "/api/v1/payment/refund", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<PaymentDto>> refundPayment(@RequestParam Long paymentId, @RequestParam BigDecimal amount,
            @RequestParam(required = false) String reason) throws SimappeException;

    @PostExchange(value = "/api/v1/payment" + SimappeConstant.PATH_CREATE, accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<PaymentDto>> createPayment(@Valid @RequestBody PaymentDto dto) throws SimappeException;

    @PutExchange(value = "/api/v1/payment" + SimappeConstant.PATH_UPDATE, accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<PaymentDto>> updatePayment(@Valid @RequestBody PaymentDto dto, @RequestParam Long id);

    @DeleteExchange(value = "/api/v1/payment" + SimappeConstant.PATH_DELETE, accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<Void>> deletePayment(@RequestParam Long id);

    @PostExchange(value = "/api/v1/payment" + SimappeConstant.PATH_PAGE, accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<PageDto<PaymentDto>>> getPaymentPage(@RequestBody SimappeRequestQuery requestQuery)
            throws SimappeException;

    // ========== GESTIÓN DE MÉTODOS DE PAGO (PAYMENT METHOD MANAGEMENT) ==========

    @GetExchange(value = "/api/v1/payment-method"
            + SimappeConstant.PATH_READ, accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<List<PaymentMethodDto>>> readPaymentMethods();

    @GetExchange(value = "/api/v1/payment-method/read-by-customer", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<List<PaymentMethodDto>>> readPaymentMethodsByCustomer(@RequestParam Long customerId);

    @GetExchange(value = "/api/v1/payment-method/read-active-by-customer", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<List<PaymentMethodDto>>> readActivePaymentMethodsByCustomer(@RequestParam Long customerId);

    @GetExchange(value = "/api/v1/payment-method/get-default-by-customer", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<PaymentMethodDto>> getDefaultPaymentMethodByCustomer(@RequestParam Long customerId);

    @GetExchange(value = "/api/v1/payment-method" + SimappeConstant.PATH_GET, accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<PaymentMethodDto>> getPaymentMethod(@RequestParam Long id);

    @PutExchange(value = "/api/v1/payment-method/set-as-default", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<PaymentMethodDto>> setPaymentMethodAsDefault(@RequestParam Long paymentMethodId)
            throws SimappeException;

    @PostExchange(value = "/api/v1/payment-method"
            + SimappeConstant.PATH_CREATE, accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<PaymentMethodDto>> createPaymentMethod(@Valid @RequestBody PaymentMethodDto dto)
            throws SimappeException;

    @PutExchange(value = "/api/v1/payment-method"
            + SimappeConstant.PATH_UPDATE, accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<PaymentMethodDto>> updatePaymentMethod(@Valid @RequestBody PaymentMethodDto dto,
            @RequestParam Long id);

    @DeleteExchange(value = "/api/v1/payment-method"
            + SimappeConstant.PATH_DELETE, accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<Void>> deletePaymentMethod(@RequestParam Long id);

    @PostExchange(value = "/api/v1/payment-method"
            + SimappeConstant.PATH_PAGE, accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<PageDto<PaymentMethodDto>>> getPaymentMethodPage(
            @RequestBody SimappeRequestQuery requestQuery)
            throws SimappeException;

    // ========== GESTIÓN DE WEBHOOKS DE STRIPE (STRIPE WEBHOOK MANAGEMENT)
    // ==========

    @PostExchange(value = "/api/v1/stripe/webhook", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<Map<String, Object>>> handleStripeWebhook(@RequestBody String payload,
            @RequestHeader(value = "Stripe-Signature", required = false) String signature);

    @PostExchange(value = "/api/v1/stripe/test/payment-intent", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<StripePaymentIntentDto>> createTestPaymentIntent(@RequestParam Long amount,
            @RequestParam CurrencyCode currency, @RequestParam(required = false) String customerId,
            @RequestParam(required = false) String paymentMethodId);

    @PostExchange(value = "/api/v1/stripe/test/confirm-payment-intent", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<StripePaymentIntentDto>> confirmTestPaymentIntent(@RequestParam String paymentIntentId);

    @PostExchange(value = "/api/v1/stripe/test/customer", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<StripeCustomerDto>> createTestCustomer(@RequestParam String email,
            @RequestParam String name);

    @PostExchange(value = "/api/v1/stripe/test/attach-payment-method", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<StripePaymentMethodDto>> attachTestPaymentMethod(@RequestParam String paymentMethodId,
            @RequestParam String customerId);

    @PostExchange(value = "/api/v1/stripe/test/refund", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<StripeRefundDto>> createTestRefund(@RequestParam String paymentIntentId,
            @RequestParam Long amount, @RequestParam(required = false) String reason);
}
