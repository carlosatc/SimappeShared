/*
 * Copyright (c) 2026 CatcSoft.
 *
 * This software is the confidential and proprietary information of CatcSoft
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of a
 * written license agreement entered into with CatcSoft.
 *
 * Author: Carlos Torres
 * Date: 2026-10-08
 */
package com.catcsoft.simappe.shared.client.configurations;

import java.util.List;
import java.util.Optional;

import com.catcsoft.simappe.commons.api.v1.util.SimappeConstant;
import com.catcsoft.simappe.commons.api.v1.tareas.estado.NombreUsuarioTareas;
import com.catcsoft.simappe.commons.api.v1.tareas.sesion.SesionVigente;
import com.catcsoft.simappe.model.configurations.usuario.dto.UsuarioDto;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

/**
 * Busca el nombre de quien lanza la tarea en el usuario de Simappe, con el token de la peticion (mismo
 * camino que los demás avisos). Una falla no impide crear la tarea: se registra y se sigue
 * sin nombre.
 *
 * @author Carlos Torres
 * @since 0.12.0
 */
@Slf4j
public class NombreUsuarioSimappe implements NombreUsuarioTareas {

    /** Cliente del usuario de Simappe. */
    private final UsuarioSimappeBlockingClient cliente;

    /**
     * Crea el resolutor.
     *
     * @param cliente cliente del usuario de Simappe
     */
    public NombreUsuarioSimappe(UsuarioSimappeBlockingClient cliente) {
        this.cliente = cliente;
    }

    @Override
    public Optional<String> nombre(String usuario) {
        HttpServletRequest peticion = SesionVigente.peticion();
        String token = peticion == null ? null : peticion.getHeader(SimappeConstant.AUTHORIZATION);
        if (token == null || token.isBlank() || usuario == null) {
            return Optional.empty();
        }
        try {
            List<UsuarioDto> usuarios = cliente.read(token).getResponse().getContent();
            return usuarios == null ? Optional.empty() : usuarios.stream()
                    .filter(u -> usuario.equalsIgnoreCase(u.getUsername()))
                    .findFirst()
                    .map(u -> unir(u.getNombres(), u.getApellidos()));
        } catch (RuntimeException e) {
            log.warn("[TAREAS] No se encontro el nombre de {}; la tarea muestra el usuario: {}", usuario,
                    e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Nombre y apellido; nulo si no hay ninguno.
     *
     * @param nombres   nombres
     * @param apellidos apellidos
     * @return el nombre completo, o nulo
     */
    static String unir(String nombres, String apellidos) {
        String completo = ((nombres == null ? "" : nombres.trim()) + " "
                + (apellidos == null ? "" : apellidos.trim())).trim();
        return completo.isEmpty() ? null : completo;
    }
}
