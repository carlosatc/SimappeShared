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
 * Respuesta de {@code /registration/seal}: el blob cifrado que se incrusta en
 * la URL del correo de activación.
 *
 * @author Carlos Torres
 * @version 1.0
 * @since 1.4.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationSealResponse implements Serializable {

    /** Versión de serialización de la clase. */
    private static final long serialVersionUID = 1L;

    /** Blob cifrado (Base64 URL-safe) del contexto de activación. */
    private String data;
}
