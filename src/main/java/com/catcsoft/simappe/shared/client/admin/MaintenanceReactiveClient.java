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
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import com.catcsoft.simappe.model.core.response.SuccessResponse;
import com.catcsoft.simappe.model.admin.dto.maintenance.MaintenanceWindowDto;

import reactor.core.publisher.Mono;

/**
 * Cliente WebClient modular para operaciones de ventanas de mantenimiento en
 * Simappe.
 * Proporciona métodos para obtener las ventanas de mantenimiento activas.
 * 
 * <p>
 * Este cliente forma parte de la arquitectura modular v2 que reemplaza
 * gradualmente
 * al SimappeAdminWebClient monolítico.
 * 
 * @author torrescamargo@gmail.com
 * @since 2026-02-01
 * @version 2.8.1
 */
@HttpExchange("simappe-admin")
public interface MaintenanceReactiveClient {

    /**
     * Recupera las ventanas de mantenimiento activas.
     *
     * @return la lista de ventanas de mantenimiento activas
     */
    @GetExchange(value = "/api/v1/maintenance-windows/active", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<List<MaintenanceWindowDto>>> getActiveMaintenanceWindow();
}
