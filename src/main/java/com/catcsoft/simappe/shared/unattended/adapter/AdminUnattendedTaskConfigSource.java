/*
 * Copyright (c) 2026 CatcSoft
 *
 * Author: torrescamargo@gmail.com
 */
package com.catcsoft.simappe.shared.unattended.adapter;

import java.time.Duration;
import java.util.List;
import java.util.Objects;

import com.catcsoft.simappe.commons.api.v1.core.unattended.UnattendedTaskConfig;
import com.catcsoft.simappe.commons.api.v1.core.unattended.UnattendedTaskConfigSource;
import com.catcsoft.simappe.shared.unattended.client.UnattendedTaskConfigWebClient;

/**
 * Fuente de configuración de tareas desatendidas sobre SimappeAdmin.
 *
 * @author torrescamargo@gmail.com
 * @since 1.4.0
 */
public class AdminUnattendedTaskConfigSource implements UnattendedTaskConfigSource {

    private final UnattendedTaskConfigWebClient client;
    private final Duration timeout;

    /**
     * @param client  cliente de SimappeAdmin
     * @param timeout espera máxima de la respuesta
     */
    public AdminUnattendedTaskConfigSource(UnattendedTaskConfigWebClient client, Duration timeout) {
        this.client = Objects.requireNonNull(client, "client no puede ser null");
        this.timeout = Objects.requireNonNull(timeout, "timeout no puede ser null");
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalStateException si SimappeAdmin no responde: la corrida no
     *                               se crea vacía por un error de red
     */
    @Override
    public List<UnattendedTaskConfig> listActive(String taskType, String environment) {
        List<UnattendedTaskConfig> filas;
        try {
            filas = client.listActive(taskType, environment).block(timeout);
        } catch (RuntimeException e) {
            throw new IllegalStateException("SimappeAdmin no devolvió la configuración de la tarea " + taskType
                    + " en " + environment + ": " + e.getMessage(), e);
        }
        return filas == null ? List.of() : filas;
    }
}
