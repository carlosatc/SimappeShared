/*
 * Copyright (c) 2024-2026 CatcSoft. Author: Carlos Torres Email: torrescamargo@gmail.com
 */
package com.catcsoft.simappe.shared.tenant.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Petición de reinicio de contraseña por servicio (masters → admin
 * {@code reset-password-by-service}), para el flujo público de "olvidé mi
 * contraseña" de un usuario YA activado.
 *
 * <p>
 * Igual que {@link ServiceActivationRequest}, usa un DTO propio en lugar de
 * {@code TenantUserIdentityDto} porque el campo {@code password} de aquel es
 * {@code WRITE_ONLY}: se deserializa en el receptor pero NO se serializa al
 * enviarlo, por lo que el password nunca llegaría en el request. Este DTO
 * serializa los tres campos que admin necesita ({@code username}/
 * {@code customerId}/{@code password}); admin los deserializa en su
 * {@code TenantUserIdentityDto} por coincidencia de nombres.
 * </p>
 *
 * @author Carlos Torres
 * @version 1.0
 * @since 1.3.10
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServicePasswordResetRequest implements Serializable {

    /** Versión de serialización de la clase. */
    private static final long serialVersionUID = 1L;

    /** Username de la identidad cuya contraseña se reinicia. */
    private String username;

    /** Customer propietario de la identidad. */
    private Long customerId;

    /** Nueva contraseña a fijar (validada contra la política en admin). */
    private String password;
}
