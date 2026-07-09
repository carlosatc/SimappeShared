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
 * Petición de {@code /registration/open}: el blob cifrado recibido del enlace
 * de activación a descifrar por oauth2.
 *
 * @author Carlos Torres
 * @version 1.0
 * @since 1.4.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationOpenRequest implements Serializable {

    /** Versión de serialización de la clase. */
    private static final long serialVersionUID = 1L;

    /** Blob cifrado (Base64 URL-safe) del enlace de activación. */
    private String data;
}
