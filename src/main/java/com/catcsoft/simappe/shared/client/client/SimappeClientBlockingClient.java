/*
 * CatcSoft - Ecosistema Simappe
 * Copyright (c) 2025.
 * Todos los derechos reservados.
 *
 * Este software es propiedad confidencial de CatcSoft.
 * Su uso está sujeto a los términos de la licencia.
 */
package com.catcsoft.simappe.shared.client.client;

import java.util.List;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.DeleteExchange;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;
import org.springframework.web.service.annotation.PutExchange;

import com.catcsoft.simappe.model.core.query.PageDto;
import com.catcsoft.simappe.model.core.query.SimappeRequestQuery;
import com.catcsoft.simappe.model.core.response.SuccessResponse;
import com.catcsoft.simappe.commons.api.v1.util.SimappeConstant;
import com.catcsoft.simappe.model.admin.enums.ConsecutiveScope;
import com.catcsoft.simappe.model.admin.enums.ConsecutiveType;
import com.catcsoft.simappe.model.client.dto.notification.NotificationJobRequestDTO;
import com.catcsoft.simappe.model.client.dto.notification.NotificationJobStatusDTO;
import com.catcsoft.simappe.model.client.dto.notification.NotificationMessageDTO;
import com.catcsoft.simappe.model.client.dto.notification.enums.NotificationMessageStatus;
import com.catcsoft.simappe.model.config.dto.ConsecutiveConfigurationDto;
import com.catcsoft.simappe.model.core.util.dto.ServiceResponseAvailable;
import com.catcsoft.simappe.model.games.dto.DocumentBase64Dto;
import com.catcsoft.simappe.model.games.enums.SourceType;

import jakarta.validation.Valid;

/**
 * Interfaz de cliente WebClient para interactuar con los servicios de Simappe
 * Client.
 * Proporciona capacidades de cliente HTTP reactivo para operaciones de
 * configuración
 * de consecutivos, documentos base64 y perfiles de usuario.
 *
 * @author torrescamargo@gmail.com
 * @since 2025-11-25
 */
@HttpExchange("simappe-client")
public interface SimappeClientBlockingClient {

	/**
	 * Verifica la disponibilidad del servicio de consecutivos.
	 *
	 * @param token el token de autorización
	 * @return el estado de disponibilidad del servicio
	 */
	@GetExchange(value = "/api/v1/consecutive-configuration"
			+ SimappeConstant.PATH_INDEX, accept = MediaType.APPLICATION_JSON_VALUE)
	SuccessResponse<ServiceResponseAvailable> index(@RequestHeader(SimappeConstant.AUTHORIZATION) String token);

	/**
	 * Recupera todas las configuraciones de consecutivos.
	 *
	 * @param token el token de autorización
	 * @return la lista de configuraciones de consecutivos
	 */
	@GetExchange(value = "/api/v1/consecutive-configuration"
			+ SimappeConstant.PATH_READ, accept = MediaType.APPLICATION_JSON_VALUE)
	SuccessResponse<List<ConsecutiveConfigurationDto>> read(
			@RequestHeader(SimappeConstant.AUTHORIZATION) String token);

	/**
	 * Recupera las configuraciones de consecutivos activas.
	 *
	 * @param token el token de autorización
	 * @return la lista de configuraciones activas
	 */
	@GetExchange(value = "/api/v1/consecutive-configuration/read-active", accept = MediaType.APPLICATION_JSON_VALUE)
	SuccessResponse<List<ConsecutiveConfigurationDto>> readActive(
			@RequestHeader(SimappeConstant.AUTHORIZATION) String token);

	/**
	 * Recupera configuraciones de consecutivos por tipo.
	 *
	 * @param type  el tipo de consecutivo
	 * @param token el token de autorización
	 * @return la lista de configuraciones encontradas
	 */
	@GetExchange(value = "/api/v1/consecutive-configuration/read-by-type", accept = MediaType.APPLICATION_JSON_VALUE)
	SuccessResponse<List<ConsecutiveConfigurationDto>> readByType(@RequestParam ConsecutiveType type,
			@RequestHeader(SimappeConstant.AUTHORIZATION) String token);

	/**
	 * Recupera configuraciones de consecutivos por ámbito.
	 *
	 * @param scope el ámbito del consecutivo
	 * @param token el token de autorización
	 * @return la lista de configuraciones encontradas
	 */
	@GetExchange(value = "/api/v1/consecutive-configuration/read-by-scope", accept = MediaType.APPLICATION_JSON_VALUE)
	SuccessResponse<List<ConsecutiveConfigurationDto>> readByScope(@RequestParam ConsecutiveScope scope,
			@RequestHeader(SimappeConstant.AUTHORIZATION) String token);

	/**
	 * Recupera configuraciones de consecutivos por empresa.
	 *
	 * @param companyId el ID de la empresa
	 * @param token     el token de autorización
	 * @return la lista de configuraciones encontradas
	 */
	@GetExchange(value = "/api/v1/consecutive-configuration/read-by-company", accept = MediaType.APPLICATION_JSON_VALUE)
	SuccessResponse<List<ConsecutiveConfigurationDto>> readByCompany(@RequestParam Long companyId,
			@RequestHeader(SimappeConstant.AUTHORIZATION) String token);

	/**
	 * Recupera una configuración de consecutivo por su ID.
	 *
	 * @param id    el ID de la configuración
	 * @param token el token de autorización
	 * @return los detalles de la configuración encontrada
	 */
	@GetExchange(value = "/api/v1/consecutive-configuration"
			+ SimappeConstant.PATH_GET, accept = MediaType.APPLICATION_JSON_VALUE)
	SuccessResponse<ConsecutiveConfigurationDto> get(@RequestParam Long id,
			@RequestHeader(SimappeConstant.AUTHORIZATION) String token);

	/**
	 * Recupera una configuración de consecutivo por su código.
	 *
	 * @param code  el código del consecutivo
	 * @param token el token de autorización
	 * @return los detalles de la configuración encontrada
	 */
	@GetExchange(value = "/api/v1/consecutive-configuration/get-by-code", accept = MediaType.APPLICATION_JSON_VALUE)
	SuccessResponse<ConsecutiveConfigurationDto> getByCode(@RequestParam String code,
			@RequestHeader(SimappeConstant.AUTHORIZATION) String token);

	/**
	 * Crea una nueva configuración de consecutivo.
	 *
	 * @param dto   el DTO de la configuración a crear
	 * @param token el token de autorización
	 * @return la configuración creada
	 */
	@PostExchange(value = "/api/v1/consecutive-configuration"
			+ SimappeConstant.PATH_CREATE, accept = MediaType.APPLICATION_JSON_VALUE)
	SuccessResponse<ConsecutiveConfigurationDto> create(@Valid @RequestBody ConsecutiveConfigurationDto dto,
			@RequestHeader(SimappeConstant.AUTHORIZATION) String token);

	/**
	 * Actualiza una configuración de consecutivo existente.
	 *
	 * @param dto   el DTO de la configuración con los cambios
	 * @param id    el ID de la configuración a actualizar
	 * @param token el token de autorización
	 * @return la configuración actualizada
	 */
	@PutExchange(value = "/api/v1/consecutive-configuration"
			+ SimappeConstant.PATH_UPDATE, accept = MediaType.APPLICATION_JSON_VALUE)
	SuccessResponse<ConsecutiveConfigurationDto> update(@Valid @RequestBody ConsecutiveConfigurationDto dto,
			@RequestParam Long id, @RequestHeader(SimappeConstant.AUTHORIZATION) String token);

	/**
	 * Elimina una configuración de consecutivo por su ID.
	 *
	 * @param id    el ID de la configuración
	 * @param token el token de autorización
	 * @return respuesta exitosa
	 */
	@DeleteExchange(value = "/api/v1/consecutive-configuration"
			+ SimappeConstant.PATH_DELETE, accept = MediaType.APPLICATION_JSON_VALUE)
	SuccessResponse<Void> delete(@RequestParam Long id,
			@RequestHeader(SimappeConstant.AUTHORIZATION) String token);

	/**
	 * Recupera una lista paginada de configuraciones de consecutivos.
	 *
	 * @param requestQuery los parámetros de consulta
	 * @param token        el token de autorización
	 * @return la página de configuraciones encontradas
	 */
	@PostExchange(value = "/api/v1/consecutive-configuration"
			+ SimappeConstant.PATH_PAGE, accept = MediaType.APPLICATION_JSON_VALUE)
	SuccessResponse<PageDto<ConsecutiveConfigurationDto>> getPage(
			@Valid @RequestBody SimappeRequestQuery requestQuery,
			@RequestHeader(SimappeConstant.AUTHORIZATION) String token);

	/**
	 * Genera el siguiente valor de consecutivo para un código específico.
	 *
	 * @param code  el código del consecutivo
	 * @param token el token de autorización
	 * @return el valor de consecutivo formateado
	 */
	@PostExchange(value = "/api/v1/consecutive-configuration/generate", accept = MediaType.APPLICATION_JSON_VALUE)
	SuccessResponse<String> generateConsecutive(@RequestParam String code,
			@RequestHeader(SimappeConstant.AUTHORIZATION) String token);

	/**
	 * Recupera todos los documentos en base64.
	 *
	 * @param token el token de autorización
	 * @return la lista de documentos encontrados
	 */
	@GetExchange(value = "/api/v1/document-base64"
			+ SimappeConstant.PATH_READ, accept = MediaType.APPLICATION_JSON_VALUE)
	SuccessResponse<List<DocumentBase64Dto>> readDoc64(
			@RequestHeader(SimappeConstant.AUTHORIZATION) String token);

	/**
	 * Busca documentos por su origen.
	 *
	 * @param sourceParent el padre del origen
	 * @param sourceId     el ID del origen
	 * @param token        el token de autorización
	 * @return la lista de documentos encontrados
	 */
	@GetExchange(value = "/api/v1/document-base64/find-by-source", accept = MediaType.APPLICATION_JSON_VALUE)
	SuccessResponse<List<DocumentBase64Dto>> findBySource(@RequestParam String sourceParent,
			@RequestParam Long sourceId, @RequestHeader(SimappeConstant.AUTHORIZATION) String token);

	/**
	 * Busca documentos por su tipo de origen.
	 *
	 * @param sourceType el tipo de origen
	 * @param token      el token de autorización
	 * @return la lista de documentos encontrados
	 */
	@GetExchange(value = "/api/v1/document-base64/find-by-type", accept = MediaType.APPLICATION_JSON_VALUE)
	SuccessResponse<List<DocumentBase64Dto>> findByType(@RequestParam SourceType sourceType,
			@RequestHeader(SimappeConstant.AUTHORIZATION) String token);

	/**
	 * Busca un documento por su código.
	 *
	 * @param code  el código del documento
	 * @param token el token de autorización
	 * @return los detalles del documento encontrado
	 */
	@GetExchange(value = "/api/v1/document-base64/find-by-code", accept = MediaType.APPLICATION_JSON_VALUE)
	SuccessResponse<DocumentBase64Dto> findByCode(@RequestParam String code,
			@RequestHeader(SimappeConstant.AUTHORIZATION) String token);

	/**
	 * Busca documentos por su nombre de archivo.
	 *
	 * @param filename el nombre del archivo
	 * @param token    el token de autorización
	 * @return la lista de documentos encontrados
	 */
	@GetExchange(value = "/api/v1/document-base64/find-by-filename", accept = MediaType.APPLICATION_JSON_VALUE)
	SuccessResponse<List<DocumentBase64Dto>> findByFilename(@RequestParam String filename,
			@RequestHeader(SimappeConstant.AUTHORIZATION) String token);

	/**
	 * Busca documentos por su tipo MIME.
	 *
	 * @param mimeType el tipo MIME
	 * @param token    el token de autorización
	 * @return la lista de documentos encontrados
	 */
	@GetExchange(value = "/api/v1/document-base64/find-by-mime-type", accept = MediaType.APPLICATION_JSON_VALUE)
	SuccessResponse<List<DocumentBase64Dto>> findByMimeType(@RequestParam String mimeType,
			@RequestHeader(SimappeConstant.AUTHORIZATION) String token);

	/**
	 * Recupera un documento por su ID.
	 *
	 * @param id    el ID del documento
	 * @param token el token de autorización
	 * @return los detalles del documento encontrado
	 */
	@GetExchange(value = "/api/v1/document-base64"
			+ SimappeConstant.PATH_GET, accept = MediaType.APPLICATION_JSON_VALUE)
	SuccessResponse<DocumentBase64Dto> getDoc64(@RequestParam Long id,
			@RequestHeader(SimappeConstant.AUTHORIZATION) String token);

	/**
	 * Crea un nuevo documento en base64.
	 *
	 * @param dto   el DTO del documento a crear
	 * @param token el token de autorización
	 * @return el documento creado
	 */
	@PostExchange(value = "/api/v1/document-base64"
			+ SimappeConstant.PATH_CREATE, accept = MediaType.APPLICATION_JSON_VALUE)
	SuccessResponse<DocumentBase64Dto> createDoc64(@Valid @RequestBody DocumentBase64Dto dto,
			@RequestHeader(SimappeConstant.AUTHORIZATION) String token);

	/**
	 * Actualiza un documento existente.
	 *
	 * @param dto   el DTO del documento con los cambios
	 * @param id    el ID del documento a actualizar
	 * @param token el token de autorización
	 * @return el documento actualizado
	 */
	@PutExchange(value = "/api/v1/document-base64"
			+ SimappeConstant.PATH_UPDATE, accept = MediaType.APPLICATION_JSON_VALUE)
	SuccessResponse<DocumentBase64Dto> updateDoc64(@Valid @RequestBody DocumentBase64Dto dto,
			@RequestParam Long id,
			@RequestHeader(SimappeConstant.AUTHORIZATION) String token);

	/**
	 * Elimina un documento por su ID.
	 *
	 * @param id    el ID del documento a eliminar
	 * @param token el token de autorización
	 * @return respuesta exitosa
	 */
	@DeleteExchange(value = "/api/v1/document-base64"
			+ SimappeConstant.PATH_DELETE, accept = MediaType.APPLICATION_JSON_VALUE)
	SuccessResponse<Void> deleteDoc64(@RequestParam Long id,
			@RequestHeader(SimappeConstant.AUTHORIZATION) String token);

	/**
	 * Recupera una lista paginada de documentos.
	 *
	 * @param requestQuery los parámetros de consulta
	 * @param token        el token de autorización
	 * @return la página de documentos encontrados
	 */
	@PostExchange(value = "/api/v1/document-base64"
			+ SimappeConstant.PATH_PAGE, accept = MediaType.APPLICATION_JSON_VALUE)
	SuccessResponse<PageDto<DocumentBase64Dto>> getPageDoc64(@Valid @RequestBody SimappeRequestQuery requestQuery,
			@RequestHeader(SimappeConstant.AUTHORIZATION) String token);

	// ========================================
	// ENDPOINTS DE PERFIL DE USUARIO
	// ========================================

	/**
	 * Recupera el perfil de usuario por el ID de identidad de usuario.
	 *
	 * @param userIdentityId el ID de la identidad de usuario
	 * @param token          el token de autorización
	 * @return los detalles del perfil de usuario
	 */
	@GetExchange(value = "/api/v1/user-profile/get-by-identity", accept = MediaType.APPLICATION_JSON_VALUE)
	SuccessResponse<com.catcsoft.simappe.model.client.dto.UserProfileDto> getUserProfileByIdentityId(
			@RequestParam("userIdentityId") Long userIdentityId,
			@RequestHeader(SimappeConstant.AUTHORIZATION) String token);

	/**
	 * Crea un nuevo perfil de usuario.
	 *
	 * @param dto   el DTO del perfil de usuario a crear
	 * @param token el token de autorización
	 * @return el perfil de usuario creado
	 */
	@PostExchange(value = "/api/v1/user-profile/create", contentType = MediaType.APPLICATION_JSON_VALUE, accept = MediaType.APPLICATION_JSON_VALUE)
	SuccessResponse<com.catcsoft.simappe.model.client.dto.UserProfileDto> createUserProfile(
			@Valid @RequestBody com.catcsoft.simappe.model.client.dto.UserProfileDto dto,
			@RequestHeader(SimappeConstant.AUTHORIZATION) String token);

	/**
	 * Actualiza un perfil de usuario existente.
	 *
	 * @param dto   el DTO del perfil de usuario con los cambios
	 * @param id    el ID del perfil de usuario a actualizar
	 * @param token el token de autorización
	 * @return el perfil de usuario actualizado
	 */
	@PutExchange(value = "/api/v1/user-profile/update", contentType = MediaType.APPLICATION_JSON_VALUE, accept = MediaType.APPLICATION_JSON_VALUE)
	SuccessResponse<com.catcsoft.simappe.model.client.dto.UserProfileDto> updateUserProfile(
			@Valid @RequestBody com.catcsoft.simappe.model.client.dto.UserProfileDto dto,
			@RequestParam Long id,
			@RequestHeader(SimappeConstant.AUTHORIZATION) String token);

	// ========================================
	// ENDPOINTS DE NOTIFICATION JOBS
	// ========================================

	@PostExchange(value = "/api/v1/notification/jobs/create", contentType = MediaType.APPLICATION_JSON_VALUE, accept = MediaType.APPLICATION_JSON_VALUE)
	Map<String, Object> createNotificationJob(
			@RequestBody NotificationJobRequestDTO dto,
			@RequestHeader(SimappeConstant.AUTHORIZATION) String token);

	@GetExchange(value = "/api/v1/notification/jobs/{jobId}/status", accept = MediaType.APPLICATION_JSON_VALUE)
	NotificationJobStatusDTO getNotificationJobStatus(
			@PathVariable("jobId") Long jobId,
			@RequestHeader(SimappeConstant.AUTHORIZATION) String token);

	@GetExchange(value = "/api/v1/notification/jobs/{jobId}/messages", accept = MediaType.APPLICATION_JSON_VALUE)
	List<NotificationMessageDTO> getNotificationJobMessages(
			@PathVariable("jobId") Long jobId,
			@RequestParam(required = false) NotificationMessageStatus status,
			@RequestHeader(SimappeConstant.AUTHORIZATION) String token);

	@PostExchange(value = "/api/v1/notification/jobs/{jobId}/pause", accept = MediaType.APPLICATION_JSON_VALUE)
	Map<String, String> pauseNotificationJob(
			@PathVariable("jobId") Long jobId,
			@RequestHeader(SimappeConstant.AUTHORIZATION) String token);

	@PostExchange(value = "/api/v1/notification/jobs/{jobId}/resume", accept = MediaType.APPLICATION_JSON_VALUE)
	Map<String, String> resumeNotificationJob(
			@PathVariable("jobId") Long jobId,
			@RequestHeader(SimappeConstant.AUTHORIZATION) String token);

	@PostExchange(value = "/api/v1/notification/jobs/{jobId}/cancel", accept = MediaType.APPLICATION_JSON_VALUE)
	Map<String, String> cancelNotificationJob(
			@PathVariable("jobId") Long jobId,
			@RequestHeader(SimappeConstant.AUTHORIZATION) String token);

	@PostExchange(value = "/api/v1/notification/jobs/{jobId}/retry-failed", accept = MediaType.APPLICATION_JSON_VALUE)
	Map<String, Object> retryFailedNotificationJob(
			@PathVariable("jobId") Long jobId,
			@RequestHeader(SimappeConstant.AUTHORIZATION) String token);
}