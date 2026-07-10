/*
 * Copyright (c) 2024-2026 CatcSoft. Author: Carlos Torres Email: torrescamargo@gmail.com
 */
package com.catcsoft.simappe.shared.notification.client;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

import com.catcsoft.simappe.model.admin.notification.dto.EmailNotificationDto;

import reactor.core.publisher.Mono;

/**
 * WebClient de notificaciones por correo de SimappeAdmin
 * ({@code /api/v1/email-notifications} en SimappeAdmin). Permite a otros
 * servicios disparar el envío de correos (por ejemplo, el correo de invitación
 * o activación de un usuario) delegando la entrega asíncrona en SimappeAdmin
 * (SES / MailerSend / Kafka), sin que el llamador espere la entrega.
 *
 * <p>Estilo canónico de los WebClients de SimappeShared ({@code @HttpExchange}
 * + {@code HttpServiceProxyFactory} sobre {@code WebClient}), con token
 * explícito por header.</p>
 *
 * @author Carlos Torres
 * @version 1.0
 * @since 1.3.7
 */
@HttpExchange("simappe-admin/api/v1/email-notifications")
public interface EmailNotificationWebClient {

    /**
     * Solicita a SimappeAdmin el envío de un correo. Con {@code async=true}
     * SimappeAdmin acepta la solicitud (HTTP 202) y entrega el correo de forma
     * asíncrona; el llamador no espera la entrega.
     *
     * @param notification  contenido del correo (asunto, cuerpo, destinatarios,
     *                      plantilla, datos de plantilla, prioridad, etc.)
     * @param async         si la entrega es asíncrona (recomendado: {@code true})
     * @param authorization header {@code Bearer <jwt>}
     * @return señal de aceptación (cuerpo vacío)
     */
    @PostExchange("/send")
    Mono<Void> send(
            @RequestBody EmailNotificationDto notification,
            @RequestParam("async") boolean async,
            @RequestHeader("Authorization") String authorization);

    /**
     * Envío de correo por SERVICIO (flujo público sin JWT de usuario), autenticado por el secreto de
     * servicio compartido {@code X-Nebula-Service-Key}. Para casos donde no hay sesión de la que tomar
     * un JWT (p.ej. el "olvidé mi contraseña" de auto-servicio). El tenant/configuración de correo viaja
     * en el propio {@link EmailNotificationDto} (su base de negocio), no en el token.
     *
     * @param notification contenido del correo (asunto, cuerpo, destinatarios, tenant en su base, etc.)
     * @param async        si la entrega es asíncrona (recomendado: {@code true})
     * @param serviceKey   secreto de servicio ({@code X-Nebula-Service-Key})
     * @return señal de aceptación (cuerpo vacío)
     */
    @PostExchange("/send-by-service")
    Mono<Void> sendByService(
            @RequestBody EmailNotificationDto notification,
            @RequestParam("async") boolean async,
            @RequestHeader("X-Nebula-Service-Key") String serviceKey);
}
