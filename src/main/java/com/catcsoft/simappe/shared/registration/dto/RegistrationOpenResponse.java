/*
 * Copyright (c) 2024-2026 CatcSoft. Author: Carlos Torres Email: torrescamargo@gmail.com
 */
package com.catcsoft.simappe.shared.registration.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Respuesta de {@code /registration/open}: el JWT de registro (contexto de
 * tenant) con el que masters resuelve la conexión Oracle en la activación
 * pública, más los datos del payload (username e token de activación).
 *
 * @author Carlos Torres
 * @version 1.0
 * @since 1.4.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationOpenResponse implements Serializable {

    /** Versión de serialización de la clase. */
    private static final long serialVersionUID = 1L;

    /** JWT de registro con contexto de tenant (uso backend, TTL corto). */
    private String registrationToken;

    /** Identificador del cliente (customer) del contexto. */
    private Long clientId;

    /** Identificador de la compañía del contexto. */
    private Long companyId;

    /** Identificador de la sucursal del contexto (opcional). */
    private Long subsidiaryId;

    /** Username de la identidad Simappe que se activa. */
    private String username;

    /** Token de activación de un solo uso. */
    private String token;
}
