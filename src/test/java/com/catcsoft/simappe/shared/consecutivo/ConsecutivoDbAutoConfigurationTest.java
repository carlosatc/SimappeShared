/*
 * Copyright (c) 2026 CatcSoft.
 *
 * This software is the confidential and proprietary information of CatcSoft
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of a
 * written license agreement entered into with CatcSoft.
 *
 * Author: Carlos Torres
 * Date: 2026-10-02
 */
package com.catcsoft.simappe.shared.consecutivo;


import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import com.catcsoft.simappe.shared.consecutivo.config.ConsecutivoDbAutoConfiguration;

import jakarta.persistence.EntityManagerFactory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

/**
 * La puerta al paquete de consecutivos se registra donde hay base de datos JPA, y solo ahi.
 *
 * @author Carlos Torres
 * @since 0.15.0
 */
class ConsecutivoDbAutoConfigurationTest {

    private final ApplicationContextRunner contexto = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(ConsecutivoDbAutoConfiguration.class));

    @Test
    void conBaseDeDatosYActivadoSeRegistraElGateway() {
        contexto.withPropertyValues("simappe.consecutivo.db.enabled=true").withBean(EntityManagerFactory.class, () -> mock(EntityManagerFactory.class))
                .run(ctx -> assertThat(ctx).hasSingleBean(ConsecutivoDbGateway.class));
    }

    @Test
    void sinBaseDeDatosNoSeRegistra() {
        contexto.withPropertyValues("simappe.consecutivo.db.enabled=true")
                .run(ctx -> assertThat(ctx).doesNotHaveBean(ConsecutivoDbGateway.class));
    }

    @Test
    void sinActivarNoSeRegistraAunqueHayaBaseDeDatos() {
        contexto.withBean(EntityManagerFactory.class, () -> mock(EntityManagerFactory.class))
                .run(ctx -> assertThat(ctx).doesNotHaveBean(ConsecutivoDbGateway.class));
    }
}
