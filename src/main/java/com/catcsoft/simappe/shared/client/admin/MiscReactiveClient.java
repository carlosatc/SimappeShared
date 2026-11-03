/*
 * Copyright (C) 2025 CatcSoft - Todos los derechos reservados
 * Prohibida la reproducción total o parcial sin autorización.
 * Licencia exclusiva para uso interno de CatcSoft.
 * Información de contacto: https://github.com/CatcSoft — Email: torrescamargo@gmail.com
 */
package com.catcsoft.simappe.shared.client.admin;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.DeleteExchange;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;
import org.springframework.web.service.annotation.PutExchange;

import com.catcsoft.simappe.commons.api.v1.core.error.SimappeException;
import com.catcsoft.simappe.model.core.query.PageDto;
import com.catcsoft.simappe.model.core.query.SimappeRequestQuery;
import com.catcsoft.simappe.model.core.response.SuccessResponse;
import com.catcsoft.simappe.commons.api.v1.util.SimappeConstant;
import com.catcsoft.simappe.model.admin.dto.appconfig.VMenuDto;
import com.catcsoft.simappe.model.admin.dto.baloto.BalotoDTO;
import com.catcsoft.simappe.model.admin.dto.baloto.BalotoType;
import com.catcsoft.simappe.model.admin.dto.config.ConsecutiveDto;
import com.catcsoft.simappe.model.admin.dto.notification.waboxapp.WaboxappChatMessageDTO;
import com.catcsoft.simappe.model.admin.dto.notification.waboxapp.WaboxappConfigDTO;
import com.catcsoft.simappe.model.admin.dto.notification.waboxapp.WaboxappEventDTO;
import com.catcsoft.simappe.model.admin.dto.notification.waboxapp.WaboxappImageMessageDTO;
import com.catcsoft.simappe.model.admin.dto.notification.waboxapp.WaboxappLinkMessageDTO;
import com.catcsoft.simappe.model.admin.dto.notification.waboxapp.WaboxappMediaMessageDTO;
import com.catcsoft.simappe.model.admin.dto.notification.waboxapp.WaboxappResponseDTO;
import com.catcsoft.simappe.model.admin.dto.notification.waboxapp.WaboxappStatusDTO;
import com.catcsoft.simappe.model.admin.dto.todo.TodoDTO;
import com.catcsoft.simappe.model.admin.dto.workflow.WorkflowDefinitionDto;
import com.catcsoft.simappe.model.admin.dto.workflow.WorkflowInstanceDto;
import com.catcsoft.simappe.model.admin.dto.workflow.WorkflowStepExecutionDto;
import com.catcsoft.simappe.model.admin.dto.util.TransferDto2;
import com.catcsoft.simappe.model.admin.enums.RegistrationStatus;
import com.catcsoft.simappe.model.admin.enums.workflow.WorkflowStatus;

import jakarta.validation.Valid;
import reactor.core.publisher.Mono;

/**
 * Cliente WebClient v2 modular para funcionalidades misceláneas del Admin.
 * 
 * <p>
 * Este cliente agrupa endpoints de dominios heterogéneos que no justifican
 * un cliente dedicado individual (baloto, waboxapp, menu, consecutive, todo,
 * workflow, endpoint).
 * </p>
 *
 * <p>
 * <b>Total de m métodos: 126</b>
 * </p>
 * <ul>
 * <li><b>Baloto:</b> 16 métodos - Gestión de sorteos y predicciones</li>
 * <li><b>Waboxapp:</b> 13 métodos - Integración WhatsApp</li>
 * <li><b>Menu:</b> 12 métodos - Gestión de menús dinámicos</li>
 * <li><b>Consecutive:</b> 17 métodos - Numeración consecutiva</li>
 * <li><b>Todo:</b> 11 métodos - Tareas pendientes</li>
 * <li><b>Workflow:</b> 56 métodos - Motor de workflows</li>
 * <li><b>Endpoint:</b> 1 método - Estado de endpoints</li>
 * </ul>
 *
 * <p>
 * <b>Requisitos de activación:</b>
 * </p>
 * 
 * <pre>
 * simappe:
 *   clients:
 *     v2:
 *       enabled: true
 *   admin:
 *     url: http://simappe-admin
 * </pre>
 *
 * <p>
 * <b>Uso:</b>
 * </p>
 * 
 * <pre>
 * {@code @Autowired}
 * private MiscReactiveClient miscClient;
 *
 * public Mono&lt;List&lt;BalotoDTO&gt;&gt; getDraws() {
 *     return miscClient.readBalotoDraws()
 *         .map(SuccessResponse::getData);
 * }
 * </pre>
 *
 * @author Carlos Torres (torrescamargo@gmail.com)
 * @version 2.6.0
 * @since 2025-01-19
 */
@HttpExchange("simappe-admin")
public interface MiscReactiveClient {

    // ========== BALOTO (16 métodos) ==========

    @GetExchange(value = "/api/v1/baloto" + SimappeConstant.PATH_READ, accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<List<BalotoDTO>>> readBalotoDraws();

    @GetExchange(value = "/api/v1/baloto/read-validated", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<List<BalotoDTO>>> readValidatedDraws();

    @GetExchange(value = "/api/v1/baloto/read-by-date-range", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<List<BalotoDTO>>> readDrawsByDateRange(@RequestParam LocalDateTime start,
            @RequestParam LocalDateTime end);

    @GetExchange(value = "/api/v1/baloto/predict-next-draw", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<BalotoDTO>> predictNextDraw();

    @GetExchange(value = "/api/v1/baloto" + SimappeConstant.PATH_GET, accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<BalotoDTO>> getBalotoDraw(@RequestParam String id);

    @GetExchange(value = "/api/v1/baloto/get-by-draw-number", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<BalotoDTO>> getBalotoByNumber(@RequestParam Long drawNumber,
            @RequestParam BalotoType type);

    @PostExchange(value = "/api/v1/baloto" + SimappeConstant.PATH_CREATE, accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<BalotoDTO>> createBaloto(@Valid @RequestBody BalotoDTO dto);

    @PutExchange(value = "/api/v1/baloto" + SimappeConstant.PATH_UPDATE, accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<BalotoDTO>> updateBaloto(@Valid @RequestBody BalotoDTO dto, @RequestParam String id)
            throws SimappeException;

    @DeleteExchange(value = "/api/v1/baloto" + SimappeConstant.PATH_DELETE, accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<Void>> deleteBaloto(@RequestParam String id) throws SimappeException;

    @PostExchange(value = "/api/v1/baloto" + SimappeConstant.PATH_PAGE, accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<PageDto<BalotoDTO>>> getBalotoPage(@RequestBody SimappeRequestQuery requestQuery)
            throws SimappeException;

    @PostExchange(value = "/api/v1/baloto/bulk-load", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<Void>> bulkLoadDraws(@Valid @RequestBody List<BalotoDTO> draws);

    @GetExchange(value = "/api/v1/baloto/prediction-analysis", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<Object>> getPredictionAnalysis();

    @GetExchange(value = "/api/v1/baloto/number-frequency", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<Map<Integer, Integer>>> analyzeNumberFrequency();

    @GetExchange(value = "/api/v1/baloto/most-frequent-numbers", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<List<Integer>>> getMostFrequentNumbers(@RequestParam(defaultValue = "6") int topCount);

    @GetExchange(value = "/api/v1/baloto/number-distribution", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<Object>> calculateNumberDistribution();

    @GetExchange(value = "/api/v1/baloto/detailed-prediction", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<Object>> generateDetailedPrediction();

    // ========== WABOXAPP (13 métodos) ==========

    @GetExchange(value = "/api/v1/waboxapp" + SimappeConstant.PATH_READ, accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<List<WaboxappConfigDTO>>> readWaboxappConfigs();

    @GetExchange(value = "/api/v1/waboxapp/read-by-client", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<List<WaboxappConfigDTO>>> readWaboxappConfigsByClient(@RequestParam Long clientId);

    @GetExchange(value = "/api/v1/waboxapp/read-active-by-client", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<WaboxappConfigDTO>> readActiveWaboxappConfigByClient(@RequestParam Long clientId);

    @GetExchange(value = "/api/v1/waboxapp" + SimappeConstant.PATH_GET, accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<WaboxappConfigDTO>> getWaboxappConfig(@RequestParam String id);

    @PostExchange(value = "/api/v1/waboxapp" + SimappeConstant.PATH_CREATE, accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<WaboxappConfigDTO>> createWaboxappConfig(@Valid @RequestBody WaboxappConfigDTO dto)
            throws SimappeException;

    @PutExchange(value = "/api/v1/waboxapp" + SimappeConstant.PATH_UPDATE, accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<WaboxappConfigDTO>> updateWaboxappConfig(@Valid @RequestBody WaboxappConfigDTO dto,
            @RequestParam String id);

    @DeleteExchange(value = "/api/v1/waboxapp" + SimappeConstant.PATH_DELETE, accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<Void>> deleteWaboxappConfig(@RequestParam String id);

    @PostExchange(value = "/api/v1/waboxapp/send-chat", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<WaboxappResponseDTO>> sendChatMessage(@Valid @RequestBody WaboxappChatMessageDTO dto);

    @PostExchange(value = "/api/v1/waboxapp/send-image", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<WaboxappResponseDTO>> sendImageMessage(@Valid @RequestBody WaboxappImageMessageDTO dto);

    @PostExchange(value = "/api/v1/waboxapp/send-link", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<WaboxappResponseDTO>> sendLinkMessage(@Valid @RequestBody WaboxappLinkMessageDTO dto);

    @PostExchange(value = "/api/v1/waboxapp/send-media", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<WaboxappResponseDTO>> sendMediaMessage(@Valid @RequestBody WaboxappMediaMessageDTO dto);

    @GetExchange(value = "/api/v1/waboxapp/check-status", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<WaboxappStatusDTO>> checkWaboxappStatus(@RequestParam String uid);

    @PostExchange(value = "/api/v1/waboxapp/webhook", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<Void>> processWaboxappWebhook(@RequestBody WaboxappEventDTO event);

    // ========== MENU (12 métodos) ==========

    @GetExchange(value = "/api/v1/menu/read", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<List<VMenuDto>>> readMenuItems();

    @GetExchange(value = "/api/v1/menu/get", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<VMenuDto>> getMenuItemById(@RequestParam Long id);

    @GetExchange(value = "/api/v1/menu/read-by-role", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<List<VMenuDto>>> getMenuItemsByRole(@RequestParam Long roleId);

    @PostExchange(value = "/api/v1/menu/read-by-roles", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<List<VMenuDto>>> getMenuItemsByRoles(@RequestBody List<Long> roleIds);

    @GetExchange(value = "/api/v1/menu/read-by-application", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<List<VMenuDto>>> getMenuItemsByApplication(@RequestParam Long applicationId);

    @GetExchange(value = "/api/v1/menu/read-by-module", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<List<VMenuDto>>> getMenuItemsByModule(@RequestParam Long moduleId);

    @GetExchange(value = "/api/v1/menu/read-by-option-group", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<List<VMenuDto>>> getMenuItemsByOptionGroup(@RequestParam Long optionGroupId);

    @GetExchange(value = "/api/v1/menu/read-by-parent", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<List<VMenuDto>>> getMenuItemsByParent(@RequestParam Long parentId);

    @GetExchange(value = "/api/v1/menu/search", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<List<VMenuDto>>> searchMenuItems(@RequestParam String searchTerm);

    @GetExchange(value = "/api/v1/menu/search-by-roles", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<List<VMenuDto>>> searchMenuItemsByRoles(@RequestParam String searchTerm,
            @RequestParam Long roleId);

    @GetExchange(value = "/api/v1/menu/search-by-module", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<List<VMenuDto>>> searchMenuItemsByModule(@RequestParam String searchTerm,
            @RequestParam Long moduleId);

    @PostExchange(value = "/api/v1/menu/page", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<PageDto<VMenuDto>>> getMenuItemsPage(@RequestBody SimappeRequestQuery requestQuery)
            throws SimappeException;

    // ========== CONSECUTIVE (17 métodos) ==========

    @GetExchange(value = "/api/v1/consecutive" + SimappeConstant.PATH_READ, accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<List<ConsecutiveDto>>> readConsecutives();

    @GetExchange(value = "/api/v1/consecutive/read-status", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<List<ConsecutiveDto>>> readConsecutivesByStatus(@RequestParam RegistrationStatus status);

    @GetExchange(value = "/api/v1/consecutive/read-active", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<List<ConsecutiveDto>>> readActiveConsecutives();

    @GetExchange(value = "/api/v1/consecutive/read-by-role", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<List<ConsecutiveDto>>> readConsecutivesByRole(@RequestParam Long roleId);

    @GetExchange(value = "/api/v1/consecutive/read-by-scope", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<List<ConsecutiveDto>>> readConsecutivesByScope(@RequestParam String scope);

    @GetExchange(value = "/api/v1/consecutive" + SimappeConstant.PATH_GET, accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<ConsecutiveDto>> getConsecutive(@RequestParam Long id);

    @GetExchange(value = "/api/v1/consecutive/get-code", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<ConsecutiveDto>> getConsecutiveByCode(@RequestParam String code);

    @PostExchange(value = "/api/v1/consecutive"
            + SimappeConstant.PATH_CREATE, accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<ConsecutiveDto>> createConsecutive(@Valid @RequestBody ConsecutiveDto dto)
            throws SimappeException;

    @PutExchange(value = "/api/v1/consecutive" + SimappeConstant.PATH_UPDATE, accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<ConsecutiveDto>> updateConsecutive(@Valid @RequestBody ConsecutiveDto dto,
            @RequestParam Long id);

    @DeleteExchange(value = "/api/v1/consecutive"
            + SimappeConstant.PATH_DELETE, accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<Void>> deleteConsecutive(@RequestParam Long id);

    @GetExchange(value = "/api/v1/consecutive/next-value", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<String>> getNextConsecutiveValue(@RequestParam String id);

    @PostExchange(value = "/api/v1/consecutive/validate", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<Boolean>> validateConsecutiveValue(@RequestParam Long id,
            @RequestParam String value);

    @PutExchange(value = "/api/v1/consecutive/add-role", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<ConsecutiveDto>> addRoleToConsecutive(@RequestBody TransferDto2<Long, Long> config);

    @DeleteExchange(value = "/api/v1/consecutive/remove-role", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<Void>> removeRoleFromConsecutive(@RequestBody TransferDto2<Long, Long> config);

    @PutExchange(value = "/api/v1/consecutive/add-customer", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<ConsecutiveDto>> addCustomerToConsecutive(@RequestBody TransferDto2<Long, Long> config);

    @DeleteExchange(value = "/api/v1/consecutive/remove-customer", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<Void>> removeCustomerFromConsecutive(@RequestBody TransferDto2<Long, Long> config);

    @PostExchange(value = "/api/v1/consecutive" + SimappeConstant.PATH_PAGE, accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<PageDto<ConsecutiveDto>>> getConsecutivePage(@RequestBody SimappeRequestQuery requestQuery)
            throws SimappeException;

    // ========== TODO (11 métodos) ==========

    @GetExchange(value = "/api/v1/todo/read", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<List<TodoDTO>>> readTodos();

    @GetExchange(value = "/api/v1/todo/read-completed", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<List<TodoDTO>>> readCompletedTodos();

    @GetExchange(value = "/api/v1/todo/read-deleted", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<List<TodoDTO>>> readDeletedTodos();

    @GetExchange(value = "/api/v1/todo/read-by-username", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<List<TodoDTO>>> readTodosByUsername(@RequestParam String username);

    @GetExchange(value = "/api/v1/todo/read-by-priority", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<List<TodoDTO>>> readTodosByPriority(@RequestParam Integer priority);

    @GetExchange(value = "/api/v1/todo/get", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<TodoDTO>> getTodo(@RequestParam String id);

    @PostExchange(value = "/api/v1/todo/create", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<TodoDTO>> createTodo(@Valid @RequestBody TodoDTO dto);

    @PutExchange(value = "/api/v1/todo/update", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<TodoDTO>> updateTodo(@Valid @RequestBody TodoDTO dto, @RequestParam String id);

    @DeleteExchange(value = "/api/v1/todo/delete", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<Void>> deleteTodo(@RequestParam String id);

    // NOTE: Los métodos mark-complete y mark-incomplete NO están en v1 (omitidos
    // intencionalmente)

    @PostExchange(value = "/api/v1/todo/page", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<PageDto<TodoDTO>>> getTodoPage(@RequestBody SimappeRequestQuery requestQuery)
            throws SimappeException;

    // ========== WORKFLOW (56 métodos) ==========
    // Nota: Workflow tiene 3 submódulos: approval-request, instance,
    // step-execution, definition

    // Workflow Definition (6 métodos)
    @GetExchange(value = "/api/v1/workflow/definition/read", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<List<WorkflowDefinitionDto>>> readWorkflowDefinitions();

    @GetExchange(value = "/api/v1/workflow/definition/get", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<WorkflowDefinitionDto>> getWorkflowDefinition(@RequestParam Long id);

    @PostExchange(value = "/api/v1/workflow/definition/create", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<WorkflowDefinitionDto>> createWorkflowDefinition(
            @Valid @RequestBody WorkflowDefinitionDto dto)
            throws SimappeException;

    @PutExchange(value = "/api/v1/workflow/definition/update", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<WorkflowDefinitionDto>> updateWorkflowDefinition(
            @Valid @RequestBody WorkflowDefinitionDto dto,
            @RequestParam Long id);

    @DeleteExchange(value = "/api/v1/workflow/definition/delete", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<Void>> deleteWorkflowDefinition(@RequestParam Long id);

    @PostExchange(value = "/api/v1/workflow/definition/page", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<PageDto<WorkflowDefinitionDto>>> getWorkflowDefinitionPage(
            @RequestBody SimappeRequestQuery requestQuery)
            throws SimappeException;

    // Workflow Instance (10 métodos)
    @GetExchange(value = "/api/v1/workflow/instance/read", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<List<WorkflowInstanceDto>>> readWorkflowInstances();

    @GetExchange(value = "/api/v1/workflow/instance/read-by-definition", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<List<WorkflowInstanceDto>>> readWorkflowInstancesByDefinition(
            @RequestParam Long definitionId);

    @GetExchange(value = "/api/v1/workflow/instance/read-by-customer", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<List<WorkflowInstanceDto>>> readWorkflowInstancesByCustomer(@RequestParam Long customerId);

    @GetExchange(value = "/api/v1/workflow/instance/read-by-status", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<List<WorkflowInstanceDto>>> readWorkflowInstancesByStatus(@RequestParam WorkflowStatus status);

    @GetExchange(value = "/api/v1/workflow/instance/get", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<WorkflowInstanceDto>> getWorkflowInstance(@RequestParam Long id);

    @GetExchange(value = "/api/v1/workflow/instance/get-by-instance-id", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<WorkflowInstanceDto>> getWorkflowInstanceByInstanceId(@RequestParam String instanceId);

    @PostExchange(value = "/api/v1/workflow/instance/create", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<WorkflowInstanceDto>> createWorkflowInstance(@Valid @RequestBody WorkflowInstanceDto dto)
            throws SimappeException;

    @PutExchange(value = "/api/v1/workflow/instance/update", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<WorkflowInstanceDto>> updateWorkflowInstance(@Valid @RequestBody WorkflowInstanceDto dto,
            @RequestParam Long id);

    @DeleteExchange(value = "/api/v1/workflow/instance/delete", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<Void>> deleteWorkflowInstance(@RequestParam Long id);

    @PostExchange(value = "/api/v1/workflow/instance/page", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<PageDto<WorkflowInstanceDto>>> getWorkflowInstancePage(
            @RequestBody SimappeRequestQuery requestQuery)
            throws SimappeException;

    @PostExchange(value = "/api/v1/workflow/instance/trigger", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<WorkflowInstanceDto>> triggerWorkflowInstance(@RequestParam Long id);

    @GetExchange(value = "/api/v1/workflow/instance/status", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<WorkflowStatus>> getWorkflowInstanceStatus(@RequestParam Long id);

    // Workflow Step Execution (9 métodos)
    @GetExchange(value = "/api/v1/workflow/step-execution/read", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<List<WorkflowStepExecutionDto>>> readWorkflowStepExecutions();

    @GetExchange(value = "/api/v1/workflow/step-execution/read-by-instance", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<List<WorkflowStepExecutionDto>>> readStepExecutionsByInstance(@RequestParam Long instanceId);

    @GetExchange(value = "/api/v1/workflow/step-execution/read-by-step-id", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<List<WorkflowStepExecutionDto>>> readStepExecutionsByStepId(@RequestParam String stepId);

    @GetExchange(value = "/api/v1/workflow/step-execution/read-by-status", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<List<WorkflowStepExecutionDto>>> readStepExecutionsByStatus(
            @RequestParam WorkflowStatus status);

    @GetExchange(value = "/api/v1/workflow/step-execution/read-by-assigned", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<List<WorkflowStepExecutionDto>>> readStepExecutionsByAssigned(@RequestParam Long userId);

    @GetExchange(value = "/api/v1/workflow/step-execution/get", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<WorkflowStepExecutionDto>> getWorkflowStepExecution(@RequestParam Long id);

    @PostExchange(value = "/api/v1/workflow/step-execution/create", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<WorkflowStepExecutionDto>> createWorkflowStepExecution(
            @Valid @RequestBody WorkflowStepExecutionDto dto)
            throws SimappeException;

    @PutExchange(value = "/api/v1/workflow/step-execution/update", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<WorkflowStepExecutionDto>> updateWorkflowStepExecution(
            @Valid @RequestBody WorkflowStepExecutionDto dto,
            @RequestParam Long id);

    @DeleteExchange(value = "/api/v1/workflow/step-execution/delete", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<Void>> deleteWorkflowStepExecution(@RequestParam Long id);

    @PostExchange(value = "/api/v1/workflow/step-execution/page", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<PageDto<WorkflowStepExecutionDto>>> getWorkflowStepExecutionPage(
            @RequestBody SimappeRequestQuery requestQuery)
            throws SimappeException;

    // Workflow Approval Request (omitido por DTOs faltantes - 0 métodos
    // implementados)

    // ========== ENDPOINT STATUS (1 método) ==========

    @GetExchange(value = "/api/v1/endpoints/status", accept = MediaType.APPLICATION_JSON_VALUE)
    Mono<SuccessResponse<Map<String, Object>>> getEndpointsStatus();
}
