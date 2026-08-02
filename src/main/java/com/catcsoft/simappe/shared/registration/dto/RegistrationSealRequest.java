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
 * Contexto de activación a cifrar por oauth2 ({@code /registration/seal}): el
 * tenant, el username de la identidad que se activa y el token de activación de
 * un solo uso.
 *
 * @author Carlos Torres
 * @version 1.0
 * @since 1.4.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationSealRequest implements Serializable {

    /** Versión de serialización de la clase. */
    private static final long serialVersionUID = 1L;

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
