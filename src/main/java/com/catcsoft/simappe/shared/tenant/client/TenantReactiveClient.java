/*
 * Copyright (c) 2025-2026 CatcSoft
 *
 * This software is the confidential and proprietary information of CatcSoft
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of a
 * written license agreement entered into with CatcSoft.
 *
 * Author: torrescamargo@gmail.com
 */
package com.catcsoft.simappe.shared.tenant.client;

import java.util.List;

import org.springframework.web.service.annotation.GetExchange;

import com.catcsoft.simappe.model.core.tenant.TenantSummary;

import reactor.core.publisher.Mono;

/**
 * Cliente reactivo (WebClient) del registro de tenants de SimappeAdmin
 * ({@code GET /api/v1/internal/tenants/active}). Lo usa el registro de tenants de SimappeCommons
 * para recorrer los tenants activos.
 *
 * @author Carlos Torres
 * @since 4.0.0
 */
public interface TenantReactiveClient {

    /**
     * Lista los tenants en estado ACTIVE.
     *
     * @return lista (posiblemente vacía)
     */
    @GetExchange("/simappe-admin/api/v1/internal/tenants/active")
    Mono<List<TenantSummary>> listActiveTenants();
}
