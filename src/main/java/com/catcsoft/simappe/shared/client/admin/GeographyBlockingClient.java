/*
 * Copyright (c) 2025 CatcSoft
 *
 * This software is the confidential and proprietary information of CatcSoft
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with CatcSoft.
 *
 * CatcSoft MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE,
 * OR NON-INFRINGEMENT. CatcSoft SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED
 * BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE
 * OR ITS DERIVATIVES.
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
import com.catcsoft.simappe.model.admin.dto.territory.CityDto;
import com.catcsoft.simappe.model.admin.dto.territory.CountryDto;
import com.catcsoft.simappe.model.admin.dto.territory.StateDto;

import jakarta.validation.Valid;

/**
 * Cliente WebClient modular para operaciones geográficas en Simappe.
 * Proporciona métodos para la gestión de países, regiones, estados
 * (departamentos) y ciudades.
 * 
 * <p>
 * Este cliente forma parte de la arquitectura modular v2 que reemplaza
 * gradualmente
 * al SimappeAdminWebClient monolítico, ofreciendo una interfaz especializada y
 * más mantenible
 * para operaciones geográficas.
 * 
 * @author torrescamargo@gmail.com
 * @since 2026-01-18
 * @version 2.6.0
 */
@HttpExchange("simappe-admin")
public interface GeographyBlockingClient {

    // ========== GESTIÓN DE PAÍSES (COUNTRY MANAGEMENT) ==========

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
    @PostExchange(value = "/api/v1/country" + SimappeConstant.PATH_CREATE, accept = MediaType.APPLICATION_JSON_VALUE)
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
    @DeleteExchange(value = "/api/v1/country" + SimappeConstant.PATH_DELETE, accept = MediaType.APPLICATION_JSON_VALUE)
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

    // ========== GESTIÓN DE REGIONES (REGION MANAGEMENT) ==========

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
    @DeleteExchange(value = "/api/v1/region" + SimappeConstant.PATH_DELETE, accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<Void> deleteRegion(@RequestParam Long id);

    /**
     * Recupera una lista paginada de regiones.
     *
     * @param query los parámetros de consulta
     * @return una página de regiones
     */
    @PostExchange(value = "/api/v1/region" + SimappeConstant.PATH_PAGE, accept = MediaType.APPLICATION_JSON_VALUE)
    SuccessResponse<PageDto<?>> getRegionPage(@RequestBody SimappeRequestQuery query);

    // ========== GESTIÓN DE ESTADOS/DEPARTAMENTOS (STATE MANAGEMENT) ==========

    /**
     * Recupera todos los departamentos.
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

    // ========== GESTIÓN DE CIUDADES (CITY MANAGEMENT) ==========

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
}
