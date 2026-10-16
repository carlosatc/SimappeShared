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
package com.catcsoft.simappe.shared.consecutivo.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

import com.catcsoft.simappe.shared.consecutivo.ConsecutivoDbGateway;

import jakarta.persistence.EntityManagerFactory;

/**
 * Registra la puerta a las rutinas {@code configurations.pkg_consecutivo_*} en el servicio con base
 * de datos JPA que la pida con {@code simappe.consecutivo.db.enabled=true}; donde no hay
 * {@code EntityManagerFactory} no se registra (HT_FAT_003 V2). El consecutivo corre en la
 * transaccion de quien lo pide: el rollback del documento devuelve el numero.
 *
 * <p>Es opt-in para que los servicios que ya usan SimappeShared (Vote, BI...) no cambien al
 * subir de version.</p>
 *
 * @author Carlos Torres
 * @since 0.15.0
 */
@AutoConfiguration(afterName = "org.springframework.boot.hibernate.autoconfigure.HibernateJpaAutoConfiguration")
@ConditionalOnProperty(prefix = "simappe.consecutivo.db", name = "enabled", havingValue = "true")
@ConditionalOnBean(EntityManagerFactory.class)
public class ConsecutivoDbAutoConfiguration {

    /**
     * La puerta a las rutinas de consecutivos.
     *
     * @return el gateway
     */
    @Bean
    @ConditionalOnMissingBean
    public ConsecutivoDbGateway consecutivoDbGateway() {
        return new ConsecutivoDbGateway();
    }
}
