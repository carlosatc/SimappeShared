/*
 * Copyright (c) 2026 CatcSoft.
 *
 * This software is the confidential and proprietary information of CatcSoft
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of a
 * written license agreement entered into with CatcSoft.
 *
 * Author: Carlos Torres
 * Date: 2026-11-02
 */
package com.catcsoft.simappe.shared.client.config;

import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.boot.web.context.reactive.ConfigurableReactiveWebEnvironment;
import org.springframework.boot.web.context.reactive.ReactiveWebApplicationContext;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * Decide si se registran los clientes bloqueantes ({@code XxxBlockingClient}).
 *
 * <p>Si el servicio fija {@code simappe.shared.clients.blocking.enabled}, manda su valor. Si no la fija, los clientes
 * bloqueantes existen en una aplicación servlet o sin web y no existen en una aplicación web reactiva: allí una llamada
 * bloqueante dentro de un hilo de Netty detiene el event loop, así que solo se ofrecen si el servicio los pide.</p>
 *
 * @author Carlos Torres
 * @since 4.0.0
 */
class BlockingClientsCondition extends SpringBootCondition {

    /** Propiedad que enciende o apaga los clientes bloqueantes. */
    static final String PROPERTY = "simappe.shared.clients.blocking.enabled";

    /**
     * Evalúa la propiedad y, si falta, el tipo de aplicación.
     *
     * @param context  contexto de la evaluación
     * @param metadata metadatos de la clase anotada
     * @return coincide si los clientes bloqueantes deben registrarse
     */
    @Override
    public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
        String value = context.getEnvironment().getProperty(PROPERTY);
        if (value != null) {
            return Boolean.parseBoolean(value.trim())
                    ? ConditionOutcome.match(PROPERTY + "=true")
                    : ConditionOutcome.noMatch(PROPERTY + "=false");
        }
        if (isReactiveWebApplication(context)) {
            return ConditionOutcome.noMatch("reactive web application without " + PROPERTY);
        }
        return ConditionOutcome.match("non-reactive application without " + PROPERTY);
    }

    /**
     * Mismo criterio que usa Spring Boot para {@code @ConditionalOnWebApplication(type = REACTIVE)}.
     *
     * @param context contexto de la evaluación
     * @return si la aplicación es web reactiva
     */
    private static boolean isReactiveWebApplication(ConditionContext context) {
        return context.getEnvironment() instanceof ConfigurableReactiveWebEnvironment
                || context.getResourceLoader() instanceof ReactiveWebApplicationContext;
    }
}
