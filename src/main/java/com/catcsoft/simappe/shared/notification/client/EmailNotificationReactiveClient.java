/*
 * Copyright (c) 2024-2026 CatcSoft. Author: Carlos Torres Email: torrescamargo@gmail.com
 */
package com.catcsoft.simappe.shared.notification.client;

import java.util.Map;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

import com.catcsoft.simappe.model.admin.notification.dto.EmailNotificationDto;
import com.catcsoft.simappe.shared.security.ServiceKeyHeader;

import reactor.core.publisher.Mono;

/**
 * Cliente reactivo (WebClient) de notificaciones por correo de SimappeAdmin
 * ({@code /api/v1/email-notifications}). SimappeAdmin entrega el correo por AWS SES, el proveedor
 * oficial; el llamador solo pide el envío.
 *
 * <p>Todas las operaciones responden 202 sin cuerpo que el llamador necesite: la entrega es
 * asíncrona por defecto. Su gemelo {@code EmailNotificationBlockingClient}
 * expone las mismas rutas y parámetros.</p>
 *
 * @author Carlos Torres
 * @since 4.0.0
 */
@HttpExchange("simappe-admin/api/v1/email-notifications")
public interface EmailNotificationReactiveClient {

    /**
     * {@code POST /send}: envía un correo armado por el llamador.
     *
     * @param notification contenido del correo (asunto, cuerpo, destinatarios, plantilla, datos, prioridad)
     * @param async si la entrega es asíncrona (por defecto {@code true}: SimappeAdmin acepta con 202 y entrega por Kafka)
     * @param authorization header {@code Bearer <jwt>}; opcional para los flujos que no tienen sesión
     * @return señal de aceptación
     */
    @PostExchange("/send")
    Mono<Void> send(
            @RequestBody EmailNotificationDto notification,
            @RequestParam(name = "async", defaultValue = "true") boolean async,
            @RequestHeader(name = "Authorization", required = false) String authorization);

    /**
     * {@code POST /send-by-service}: envía un correo por servicio, sin JWT de usuario, autenticado con la clave de servicio ({@link ServiceKeyHeader}); el tenant viaja en el propio DTO.
     *
     * @param notification contenido del correo (asunto, cuerpo, destinatarios, plantilla, datos, prioridad)
     * @param async si la entrega es asíncrona (por defecto {@code true}: SimappeAdmin acepta con 202 y entrega por Kafka)
     * @param serviceKey secreto de servicio (cabecera de clave de servicio, {@link ServiceKeyHeader})
     * @return señal de aceptación
     */
    @PostExchange("/send-by-service")
    Mono<Void> sendByService(
            @RequestBody EmailNotificationDto notification,
            @RequestParam(name = "async", defaultValue = "true") boolean async,
            @RequestHeader(ServiceKeyHeader.DEFAULT_NAME) String serviceKey);

    /**
     * {@code POST /send-templated}: envía un correo con plantilla.
     *
     * @param notification contenido del correo (asunto, cuerpo, destinatarios, plantilla, datos, prioridad)
     * @param async si la entrega es asíncrona (por defecto {@code true}: SimappeAdmin acepta con 202 y entrega por Kafka)
     * @param authorization header {@code Bearer <jwt>}; opcional para los flujos que no tienen sesión
     * @return señal de aceptación
     */
    @PostExchange("/send-templated")
    Mono<Void> sendTemplated(
            @RequestBody EmailNotificationDto notification,
            @RequestParam(name = "async", defaultValue = "true") boolean async,
            @RequestHeader(name = "Authorization", required = false) String authorization);

    /**
     * {@code POST /send-process}: envía un correo de proceso.
     *
     * @param notification contenido del correo (asunto, cuerpo, destinatarios, plantilla, datos, prioridad)
     * @param async si la entrega es asíncrona (por defecto {@code true}: SimappeAdmin acepta con 202 y entrega por Kafka)
     * @param authorization header {@code Bearer <jwt>}; opcional para los flujos que no tienen sesión
     * @return señal de aceptación
     */
    @PostExchange("/send-process")
    Mono<Void> sendProcess(
            @RequestBody EmailNotificationDto notification,
            @RequestParam(name = "async", defaultValue = "true") boolean async,
            @RequestHeader(name = "Authorization", required = false) String authorization);

    /**
     * {@code POST /simple}: envía un correo simple del cliente.
     *
     * @param to destinatario
     * @param subject asunto
     * @param content cuerpo del correo
     * @param clientId cliente (tenant) dueño de la configuración de correo
     * @param async si la entrega es asíncrona (por defecto {@code true}: SimappeAdmin acepta con 202 y entrega por Kafka)
     * @param authorization header {@code Bearer <jwt>}; opcional para los flujos que no tienen sesión
     * @return señal de aceptación
     */
    @PostExchange("/simple")
    Mono<Void> sendSimple(
            @RequestParam("to") String to,
            @RequestParam("subject") String subject,
            @RequestBody String content,
            @RequestParam("clientId") Long clientId,
            @RequestParam(name = "async", defaultValue = "true") boolean async,
            @RequestHeader(name = "Authorization", required = false) String authorization);

    /**
     * {@code POST /simple/company}: envía un correo simple con la configuración de la empresa.
     *
     * @param to destinatario
     * @param subject asunto
     * @param content cuerpo del correo
     * @param clientId cliente (tenant) dueño de la configuración de correo
     * @param companyId empresa cuya configuración de correo se usa
     * @param async si la entrega es asíncrona (por defecto {@code true}: SimappeAdmin acepta con 202 y entrega por Kafka)
     * @param authorization header {@code Bearer <jwt>}; opcional para los flujos que no tienen sesión
     * @return señal de aceptación
     */
    @PostExchange("/simple/company")
    Mono<Void> sendSimpleWithCompany(
            @RequestParam("to") String to,
            @RequestParam("subject") String subject,
            @RequestBody String content,
            @RequestParam("clientId") Long clientId,
            @RequestParam("companyId") Long companyId,
            @RequestParam(name = "async", defaultValue = "true") boolean async,
            @RequestHeader(name = "Authorization", required = false) String authorization);

    /**
     * {@code POST /templated/company}: envía un correo con plantilla y la configuración de la empresa.
     *
     * @param to destinatario
     * @param recipientName nombre del destinatario (opcional)
     * @param templateId identificador de la plantilla
     * @param templateData datos de la plantilla
     * @param clientId cliente (tenant) dueño de la configuración de correo
     * @param companyId empresa cuya configuración de correo se usa
     * @param async si la entrega es asíncrona (por defecto {@code true}: SimappeAdmin acepta con 202 y entrega por Kafka)
     * @param authorization header {@code Bearer <jwt>}; opcional para los flujos que no tienen sesión
     * @return señal de aceptación
     */
    @PostExchange("/templated/company")
    Mono<Void> sendTemplatedWithCompany(
            @RequestParam("to") String to,
            @RequestParam(name = "recipientName", required = false) String recipientName,
            @RequestParam("templateId") String templateId,
            @RequestBody Map<String, Object> templateData,
            @RequestParam("clientId") Long clientId,
            @RequestParam("companyId") Long companyId,
            @RequestParam(name = "async", defaultValue = "true") boolean async,
            @RequestHeader(name = "Authorization", required = false) String authorization);

    /**
     * {@code POST /process/company}: envía un correo de proceso con la configuración de la empresa.
     *
     * @param to destinatario
     * @param recipientName nombre del destinatario (opcional)
     * @param processId identificador del proceso
     * @param processData datos del proceso
     * @param clientId cliente (tenant) dueño de la configuración de correo
     * @param companyId empresa cuya configuración de correo se usa
     * @param async si la entrega es asíncrona (por defecto {@code true}: SimappeAdmin acepta con 202 y entrega por Kafka)
     * @param authorization header {@code Bearer <jwt>}; opcional para los flujos que no tienen sesión
     * @return señal de aceptación
     */
    @PostExchange("/process/company")
    Mono<Void> sendProcessWithCompany(
            @RequestParam("to") String to,
            @RequestParam(name = "recipientName", required = false) String recipientName,
            @RequestParam("processId") String processId,
            @RequestBody Map<String, Object> processData,
            @RequestParam("clientId") Long clientId,
            @RequestParam("companyId") Long companyId,
            @RequestParam(name = "async", defaultValue = "true") boolean async,
            @RequestHeader(name = "Authorization", required = false) String authorization);
}
