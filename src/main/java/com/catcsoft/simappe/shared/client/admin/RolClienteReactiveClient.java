/*
 * Copyright (c) 2026 CatcSoft.
 *
 * This software is the confidential and proprietary information of CatcSoft
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of a
 * written license agreement entered into with CatcSoft.
 */
package com.catcsoft.simappe.shared.client.admin;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

import com.catcsoft.simappe.commons.api.v1.core.query.PageResponse;
import com.catcsoft.simappe.commons.api.v1.core.query.SimappeRequestQuery;
import com.catcsoft.simappe.commons.api.v1.core.response.SuccessResponse;
import com.catcsoft.simappe.model.admin.record.tenant.TenantCustomerRoleResponse;

import reactor.core.publisher.Mono;

/**
 * Cliente HTTP <b>reactivo</b>. Mismos contratos que {@link RolClienteBlockingClient}, con retorno {@link Mono} de los roles del cliente (servicio de plataforma
 * <b>simappe-admin</b>, {@code /simappe-admin/api/v1/tenant/customer-role}).
 *
 * <p>Entrega cada rol del customer del token con su {@code authority} y su
 * {@code precedence}: menor numero, mayor prioridad. Lo usa la resolucion de parametros de
 * configuracion para desempatar entre roles (HT_ARQ_012, RN-PAR-021). El endpoint filtra por el
 * customer del JWT, que se pasa <b>explicitamente</b> en cada llamada.</p>
 *
 * <p><b>UBICACION TEMPORAL:</b> este cliente consume un servicio de plataforma (Simappe), por lo
 * que su hogar natural es <b>simappe-shared</b> (Catcsoft), como {@link CompanyBlockingClient}.</p>
 *
 * @author Carlos Torres
 * @since 0.16.0
 */
@HttpExchange(url = "/simappe-admin/api/v1/tenant/customer-role", accept = "application/json",
        contentType = "application/json")
public interface RolClienteReactiveClient {

    /**
     * Pagina de los roles del customer del token, con su precedencia.
     *
     * @param query         paginacion y filtros de la plataforma.
     * @param authorization header {@code Authorization: Bearer <jwt>}.
     * @return {@link Mono} con el envelope de la pagina de {@link TenantCustomerRoleResponse}.
     */
    @PostExchange("/page-response")
    Mono<SuccessResponse<PageResponse<TenantCustomerRoleResponse>>> pageResponse(@RequestBody SimappeRequestQuery query,
            @RequestHeader("Authorization") String authorization);
}
