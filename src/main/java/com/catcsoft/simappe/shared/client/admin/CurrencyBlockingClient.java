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
import com.catcsoft.simappe.commons.api.v1.util.SimappeConstant;
import com.catcsoft.simappe.model.admin.dto.currency.CurrencyDto;
import com.catcsoft.simappe.model.admin.enums.CurrencyCode;

import jakarta.validation.Valid;

/**
 * Cliente WebClient para operaciones con el maestro de monedas en Simappe
 * Admin.
 * Proporciona métodos para consultar y gestionar configuraciones de monedas
 * (ISO 4217).
 *
 * <p>
 * Este cliente forma parte de la arquitectura multimoneda v3 y permite:
 * </p>
 * <ul>
 * <li>Consultar configuraciones de monedas (decimales, símbolos, proveedores)</li>
 * <li>Obtener la lista de monedas activas habilitadas en el sistema</li>
 * <li>Consultar decimales efectivos para redondeo correcto</li>
 * <li>Gestionar el maestro de monedas (CRUD completo)</li>
 * </ul>
 *
 * <p>
 * <strong>Ejemplo de uso:</strong>
 * </p>
 *
 * <pre>
 * // Obtener configuración de COP
 * SuccessResponse&lt;CurrencyDto&gt; cop = currencyClient.getByCode(CurrencyCode.COP);
 *
 * // Obtener decimales efectivos para HNL (puede estar customizado)
 * SuccessResponse&lt;Integer&gt; decimals = currencyClient.getEffectiveDecimals(CurrencyCode.HNL);
 *
 * // Obtener todas las monedas activas
 * SuccessResponse&lt;List&lt;CurrencyDto&gt;&gt; activeCurrencies = currencyClient.getAllActive();
 * </pre>
 *
 * <p>
 * <strong>Maestro Parametrizable:</strong>
 * </p>
 * <ul>
 * <li>Enum CurrencyCode define decimales por defecto (USD=2, COP=0, BTC=8)</li>
 * <li>Tabla currency permite override de decimales (ej: COP con 2 decimales en
 * vez de 0)</li>
 * <li>Método getEffectiveDecimals() retorna valor de tabla si existe, si no el
 * default del enum</li>
 * </ul>
 *
 * @author torrescamargo@gmail.com
 * @since 2026-01-30
 * @version 3.0.0
 * @see ExchangeRateWebClient
 * @see CurrencyDto
 * @see CurrencyCode
 */
@HttpExchange("simappe-admin")
public interface CurrencyBlockingClient {

    // ========== CONSULTAS DE CONFIGURACIÓN (CURRENCY QUERIES) ==========

    /**
     * Obtiene todas las monedas activas habilitadas en el sistema.
     *
     * <p>
     * Retorna solo monedas con active = true. Útil para poblar selects o listas
     * de monedas disponibles para transacciones.
     * </p>
     *
     * @return Lista de monedas activas
     */
    @GetExchange(value = "/api/v1/currencies/read-active", accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<List<CurrencyDto>> getAllActive();

    /**
     * Obtiene la configuración completa de una moneda específica por su código ISO
     * 4217.
     *
     * @param code código de moneda ISO 4217 (ej: USD, COP, EUR)
     * @return La configuración de la moneda
     * @throws SimappeException si la moneda no existe en el maestro
     */
    @GetExchange(value = "/api/v1/currencies/{code}", accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<CurrencyDto> getByCode(@PathVariable("code") CurrencyCode code) throws SimappeException;

    /**
     * Obtiene los decimales efectivos para una moneda específica.
     *
     * <p>
     * Retorna los decimales configurados en el maestro si existe, si no retorna
     * los decimales por defecto del enum CurrencyCode. Crítico para redondeo
     * correcto en conversiones monetarias.
     * </p>
     *
     * <p>
     * Ejemplos:
     * </p>
     * <ul>
     * <li>USD → 2 decimales (default)</li>
     * <li>COP → 0 decimales (default), pero puede configurarse con 2</li>
     * <li>JPY → 0 decimales (sin fracciones)</li>
     * <li>BTC → 8 decimales (criptomoneda)</li>
     * </ul>
     *
     * @param code código de moneda ISO 4217
     * @return El número de decimales efectivos (0-12)
     */
    @GetExchange(value = "/api/v1/currencies/decimals/{code}", accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<Integer> getEffectiveDecimals(@PathVariable("code") CurrencyCode code);

    /**
     * Obtiene el símbolo de una moneda específica.
     *
     * @param code código de moneda ISO 4217
     * @return El símbolo (ej: "$", "€", "L")
     */
    @GetExchange(value = "/api/v1/currencies/symbol/{code}", accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<String> getSymbol(@PathVariable("code") CurrencyCode code);

    /**
     * Valida si una moneda está activa y disponible para operaciones.
     *
     * @param code código de moneda ISO 4217
     * @return True si la moneda está activa, false si no
     */
    @GetExchange(value = "/api/v1/currencies/validate/{code}", accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<Boolean> isActive(@PathVariable("code") CurrencyCode code);

    // ========== GESTIÓN DEL MAESTRO (CURRENCY MANAGEMENT) ==========

    /**
     * Obtiene todas las monedas registradas (activas e inactivas).
     *
     * @return Lista completa de monedas
     */
    @GetExchange(value = "/api/v1/currencies" + SimappeConstant.PATH_READ, accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<List<CurrencyDto>> readCurrencies();

    /**
     * Obtiene una moneda por su ID de base de datos.
     *
     * @param id identificador numérico de la moneda
     * @return La configuración de la moneda
     * @throws SimappeException si el ID no existe
     */
    @GetExchange(value = "/api/v1/currencies" + SimappeConstant.PATH_GET, accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<CurrencyDto> getCurrencyById(@RequestParam Long id) throws SimappeException;

    /**
     * Crea una nueva configuración de moneda en el maestro.
     *
     * <p>
     * Permite customizar decimales, símbolo, proveedor preferido, etc.
     * </p>
     *
     * @param dto datos de la moneda a crear
     * @return La moneda creada
     * @throws SimappeException si el código ya existe o la validación falla
     */
    @PostExchange(value = "/api/v1/currencies" + SimappeConstant.PATH_CREATE, accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<CurrencyDto> createCurrency(@Valid @RequestBody CurrencyDto dto) throws SimappeException;

    /**
     * Actualiza la configuración de una moneda existente.
     *
     * @param dto datos actualizados de la moneda
     * @param id  identificador de la moneda a actualizar
     * @return La moneda actualizada
     * @throws SimappeException si el ID no existe o la validación falla
     */
    @PutExchange(value = "/api/v1/currencies" + SimappeConstant.PATH_UPDATE, accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<CurrencyDto> updateCurrency(@Valid @RequestBody CurrencyDto dto, @RequestParam Long id)
            throws SimappeException;

    /**
     * Elimina una moneda del maestro.
     *
     * <p>
     * <strong>Precaución:</strong> Solo eliminar si no hay referencias en
     * transacciones o tasas de cambio.
     * </p>
     *
     * @param id identificador de la moneda a eliminar
     * @return sin contenido
     * @throws SimappeException si existen referencias o el ID no existe
     */
    @DeleteExchange(value = "/api/v1/currencies" + SimappeConstant.PATH_DELETE, accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<Void> deleteCurrency(@RequestParam Long id) throws SimappeException;

    /**
     * Obtiene una página de monedas con filtros y ordenamiento.
     *
     * @param requestQuery criterios de búsqueda, filtros, paginación y ordenamiento
     * @return Página de resultados
     * @throws SimappeException si la consulta falla
     */
    @PostExchange(value = "/api/v1/currencies" + SimappeConstant.PATH_PAGE, accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<PageDto<CurrencyDto>> getCurrencyPage(@RequestBody SimappeRequestQuery requestQuery)
            throws SimappeException;

    // ========== OPERACIONES MASIVAS (BULK OPERATIONS) ==========

    /**
     * Inicializa el maestro de monedas con las monedas estándar ISO 4217.
     *
     * <p>
     * Crea registros para las monedas más comunes (USD, EUR, COP, HNL, MXN, etc.)
     * si no existen. Útil para setup inicial del sistema.
     * </p>
     *
     * @return El número de monedas inicializadas
     */
    @PostExchange(value = "/api/v1/currencies/initialize", accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<Integer> initializeStandardCurrencies();

    /**
     * Activa o desactiva una moneda en el sistema.
     *
     * @param code   código de moneda ISO 4217
     * @param active true para activar, false para desactivar
     * @return La moneda actualizada
     */
    @PutExchange(value = "/api/v1/currencies/toggle-active", accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<CurrencyDto> toggleActive(@RequestParam CurrencyCode code, @RequestParam Boolean active);
}
