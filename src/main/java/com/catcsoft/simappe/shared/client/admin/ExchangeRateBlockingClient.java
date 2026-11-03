/*
 * Copyright (c) 2026 CatcSoft
 *
 * This software is the confidential and proprietary information of CatcSoft
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with CatcSoft.
 *
 * @author torrescamargo@gmail.com
 */
package com.catcsoft.simappe.shared.client.admin;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

import com.catcsoft.simappe.commons.api.v1.core.error.SimappeException;
import com.catcsoft.simappe.model.core.response.SuccessResponse;
import com.catcsoft.simappe.model.admin.dto.exchangerate.ExchangeRateDto;
import com.catcsoft.simappe.model.admin.enums.CurrencyCode;

import jakarta.validation.Valid;

/**
 * Cliente WebClient para operaciones con tasas de cambio en Simappe Admin.
 * Proporciona métodos para consultar tasas de cambio actuales e históricas
 * entre diferentes monedas.
 *
 * <p>
 * Este cliente forma parte de la arquitectura multimoneda v3 y permite:
 * </p>
 * <ul>
 * <li>Obtener tasas de cambio activas entre pares de monedas</li>
 * <li>Consultar tasas históricas en fechas específicas</li>
 * <li>Realizar consultas en lote para múltiples conversiones</li>
 * <li>Crear tasas de cambio manuales (override)</li>
 * </ul>
 *
 * <p>
 * <strong>Ejemplo de uso:</strong>
 * </p>
 *
 * <pre>
 * // Obtener tasa actual USD → HNL
 * SuccessResponse&lt;ExchangeRateDto&gt; rate = exchangeRateWebClient
 *     .getActiveRate(CurrencyCode.USD, CurrencyCode.HNL);
 *
 * // Obtener tasa histórica EUR → COP en fecha específica
 * SuccessResponse&lt;ExchangeRateDto&gt; historicalRate = exchangeRateWebClient
 *     .getRateAt(CurrencyCode.EUR, CurrencyCode.COP, LocalDateTime.of(2026, 1, 15, 8, 0));
 *
 * // Consulta en lote: USD a múltiples monedas
 * SuccessResponse&lt;List&lt;ExchangeRateDto&gt;&gt; batchRates = exchangeRateWebClient
 *     .getBatchRates(CurrencyCode.USD, List.of(CurrencyCode.EUR, CurrencyCode.HNL, CurrencyCode.COP));
 * </pre>
 *
 * <p>
 * <strong>Arquitectura:</strong>
 * </p>
 * <ul>
 * <li>Conversiones directas sin pivote USD (EUR → HNL directo, no EUR → USD →
 * HNL)</li>
 * <li>Caché de 5 minutos en SimappeAdmin para reducir carga</li>
 * <li>Fail-open strategy: disponibilidad sobre precisión</li>
 * <li>Integración con Open Exchange Rates API (actualización diaria 8 AM)</li>
 * </ul>
 *
 * @author torrescamargo@gmail.com
 * @since 2026-01-30
 * @version 3.0.0
 * @see CurrencyWebClient
 * @see ExchangeRateDto
 * @see CurrencyCode
 */
@HttpExchange("simappe-admin")
public interface ExchangeRateBlockingClient {

    // ========== CONSULTA DE TASAS DE CAMBIO (EXCHANGE RATE QUERIES) ==========

    /**
     * Obtiene la tasa de cambio activa actual entre dos monedas.
     *
     * <p>
     * Retorna la tasa más reciente que esté dentro de su ventana de validez
     * (effectiveDate &le; ahora &lt; expirationDate).
     * </p>
     *
     * @param source moneda origen (ISO 4217)
     * @param target moneda destino (ISO 4217)
     * @return La tasa de cambio activa
     * @throws SimappeException si no existe tasa activa para el par de monedas
     */
    @GetExchange(value = "/api/v1/exchange-rates/active", accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<ExchangeRateDto> getActiveRate(
            @RequestParam("source") CurrencyCode source,
            @RequestParam("target") CurrencyCode target) throws SimappeException;

    /**
     * Obtiene la tasa de cambio que estaba activa en una fecha/hora específica.
     *
     * <p>
     * Útil para conversiones históricas o auditoría financiera.
     * Retorna la tasa que estaba vigente en el momento especificado.
     * </p>
     *
     * @param source   moneda origen (ISO 4217)
     * @param target   moneda destino (ISO 4217)
     * @param datetime fecha y hora para consultar la tasa (ISO 8601)
     * @return La tasa de cambio histórica
     * @throws SimappeException si no existe tasa para el par de monedas en esa
     *                          fecha
     */
    @GetExchange(value = "/api/v1/exchange-rates/active/at", accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<ExchangeRateDto> getRateAt(
            @RequestParam("source") CurrencyCode source,
            @RequestParam("target") CurrencyCode target,
            @RequestParam("datetime") @DateTimeFormat(iso = ISO.DATE_TIME) LocalDateTime datetime)
            throws SimappeException;

    /**
     * Obtiene tasas de cambio en lote desde una moneda origen a múltiples monedas
     * destino.
     *
     * <p>
     * Optimización para evitar múltiples llamadas HTTP cuando se necesitan varias
     * conversiones desde la misma moneda origen (ej: USD a EUR, HNL, COP, MXN).
     * </p>
     *
     * @param source  moneda origen común (ISO 4217)
     * @param targets lista de monedas destino (ISO 4217)
     * @return Lista de tasas de cambio activas
     * @throws SimappeException si alguna de las tasas no existe
     */
    @GetExchange(value = "/api/v1/exchange-rates/batch", accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<List<ExchangeRateDto>> getBatchRates(
            @RequestParam("source") CurrencyCode source,
            @RequestParam("targets") List<CurrencyCode> targets) throws SimappeException;

    /**
     * Obtiene la tasa de cambio inversa automática.
     *
     * <p>
     * Si existe USD → HNL = 24.50, retorna HNL → USD = 0.0408163
     * (calculada como 1 / 24.50).
     * </p>
     *
     * @param source moneda origen (ISO 4217)
     * @param target moneda destino (ISO 4217)
     * @return La tasa de cambio inversa calculada
     * @throws SimappeException si no existe tasa para el par inverso
     */
    @GetExchange(value = "/api/v1/exchange-rates/inverse", accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<ExchangeRateDto> getInverseRate(
            @RequestParam("source") CurrencyCode source,
            @RequestParam("target") CurrencyCode target) throws SimappeException;

    // ========== GESTIÓN DE TASAS DE CAMBIO (EXCHANGE RATE MANAGEMENT) ==========

    /**
     * Crea una tasa de cambio manual (override).
     *
     * <p>
     * Permite a empresas establecer tasas personalizadas que anulan las tasas
     * globales de Open Exchange Rates. Útil para:
     * </p>
     * <ul>
     * <li>Empresas con tasas de cambio corporativas negociadas</li>
     * <li>Corrección de tasas erróneas del proveedor</li>
     * <li>Monedas no soportadas por Open Exchange Rates</li>
     * </ul>
     *
     * @param dto datos de la tasa de cambio a crear
     * @return La tasa creada
     * @throws SimappeException si la validación falla
     */
    @PostExchange(value = "/api/v1/exchange-rates/create", accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<ExchangeRateDto> createExchangeRate(@Valid @RequestBody ExchangeRateDto dto)
            throws SimappeException;

    /**
     * Obtiene todas las tasas de cambio activas actualmente.
     *
     * <p>
     * Retorna todas las tasas que estén dentro de su ventana de validez.
     * Útil para sincronización o reportes.
     * </p>
     *
     * @return Lista de todas las tasas activas
     */
    @GetExchange(value = "/api/v1/exchange-rates/read-active", accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<List<ExchangeRateDto>> readActiveRates();

    /**
     * Obtiene todas las tasas de cambio para una moneda origen específica.
     *
     * @param source moneda origen (ISO 4217)
     * @return Lista de tasas desde la moneda origen
     */
    @GetExchange(value = "/api/v1/exchange-rates/read-by-source", accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<List<ExchangeRateDto>> readRatesBySource(@RequestParam("source") CurrencyCode source);

    /**
     * Fuerza la actualización de tasas desde Open Exchange Rates API.
     *
     * <p>
     * Normalmente las tasas se actualizan automáticamente a las 8 AM.
     * Este método permite actualizaciones bajo demanda.
     * </p>
     *
     * @return El número de tasas actualizadas
     * @throws SimappeException si el proveedor externo falla
     */
    @PostExchange(value = "/api/v1/exchange-rates/refresh", accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<Integer> refreshRates() throws SimappeException;
}
