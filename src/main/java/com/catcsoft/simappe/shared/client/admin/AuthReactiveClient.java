/*
 * Copyright (c) 2025 CatcSoft
 *
 * This software is the confidential and proprietary information of CatcSoft
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of a
 * written license agreement entered into with CatcSoft.
 *
 * Author: torrescamargo@gmail.com
 */
package com.catcsoft.simappe.shared.client.admin;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
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
import com.catcsoft.simappe.model.admin.dto.customerrole.CustomerRoleDto;
import com.catcsoft.simappe.model.admin.dto.i18n.ResolvedLanguageDTO;
import com.catcsoft.simappe.model.admin.dto.security.RoleDto;
import com.catcsoft.simappe.model.admin.dto.security.access.RolePermissionDto;
import com.catcsoft.simappe.model.admin.dto.security.UserDto;
import com.catcsoft.simappe.model.admin.dto.security.UserIdentityDto;
import com.catcsoft.simappe.model.admin.dto.security.session.UserSessionDto;
import com.catcsoft.simappe.model.admin.dto.util.TransferDto2;

import jakarta.validation.Valid;
import reactor.core.publisher.Mono;

/**
 * Cliente WebClient modular expandido para autenticación, usuarios, roles y
 * sesiones.
 * 
 * <p>
 * Funcionalidad completa:
 * </p>
 * <ul>
 * <li>CRUD completo de usuarios (user)</li>
 * <li>CRUD completo de roles (role)</li>
 * <li>Gestión de permisos por rol (role-permission)</li>
 * <li>Gestión de sesiones de usuario (user-session)</li>
 * <li>Identidad de usuario para OAuth2 (user-identity)</li>
 * <li>Roles de cliente B2B2C (customer-role)</li>
 * </ul>
 * 
 * @author Carlos Torres (torrescamargo@gmail.com)
 * @since 2.5.0 (expandido en 2.6.0)
 * @version 2.6.0
 */
@HttpExchange("simappe-admin")
public interface AuthReactiveClient {

        // ========== USER MANAGEMENT (CRUD COMPLETO) ==========

        @GetExchange(value = "/api/v1/user/get-username", accept = MediaType.APPLICATION_JSON_VALUE)
        Mono<SuccessResponse<UserDto>> getUsername(@RequestParam("username") String username);

        @GetExchange(value = "/api/v1/user" + SimappeConstant.PATH_GET, accept = MediaType.APPLICATION_JSON_VALUE)
        Mono<SuccessResponse<UserDto>> getUser(@RequestParam Long id);

        @GetExchange(value = "/api/v1/user/get-by-user-identity", accept = MediaType.APPLICATION_JSON_VALUE)
        Mono<SuccessResponse<UserDto>> getUserByUserIdentityId(@RequestParam Long userIdentityId);

        @GetExchange(value = "/api/v1/user/get-useremail", accept = MediaType.APPLICATION_JSON_VALUE)
        Mono<SuccessResponse<UserDto>> getUseremail(@RequestParam String useremail);

        @PostExchange(value = "/api/v1/user" + SimappeConstant.PATH_CREATE, accept = MediaType.APPLICATION_JSON_VALUE)
        Mono<SuccessResponse<UserDto>> createUser(@Valid @RequestBody UserDto dto) throws SimappeException;

        @PutExchange(value = "/api/v1/user" + SimappeConstant.PATH_UPDATE, accept = MediaType.APPLICATION_JSON_VALUE)
        Mono<SuccessResponse<UserDto>> updateUser(@Valid @RequestBody UserDto dto, @RequestParam Long id);

        @PutExchange(value = "/api/v1/user/update-customer", accept = MediaType.APPLICATION_JSON_VALUE)
        Mono<SuccessResponse<UserDto>> updateUserCustomer(@RequestParam Long userId, @RequestParam Long customerId);

        @PutExchange(value = "/api/v1/user/update-password", accept = MediaType.APPLICATION_JSON_VALUE)
        Mono<SuccessResponse<UserDto>> updateUserPassword(@RequestParam Long userId, @RequestParam String newPassword);

        @PutExchange(value = "/api/v1/user/add-role", accept = MediaType.APPLICATION_JSON_VALUE)
        Mono<SuccessResponse<UserDto>> addRoleToUser(@RequestParam Long userId, @RequestParam Long roleId);

        @DeleteExchange(value = "/api/v1/user/delete-role", accept = MediaType.APPLICATION_JSON_VALUE)
        Mono<SuccessResponse<Void>> deleteRoleFromUser(@RequestParam Long userId, @RequestParam Long roleId);

        @DeleteExchange(value = "/api/v1/user" + SimappeConstant.PATH_DELETE, accept = MediaType.APPLICATION_JSON_VALUE)
        Mono<SuccessResponse<Void>> deleteUser(@RequestParam Long id);

        @PostExchange(value = "/api/v1/user" + SimappeConstant.PATH_PAGE, accept = MediaType.APPLICATION_JSON_VALUE)
        Mono<SuccessResponse<PageDto<UserDto>>> getUserPage(@RequestBody SimappeRequestQuery requestQuery)
                        throws SimappeException;

        // ========== USER IDENTITY (AUTHENTICATION) ==========

        @GetExchange(value = "/api/v1/user-identity/get-username", accept = MediaType.APPLICATION_JSON_VALUE)
        Mono<SuccessResponse<UserIdentityDto>> getUserIdentityByUsername(@RequestParam("username") String username);

        @GetExchange(value = "/api/v1/user-identity/get-useremail", accept = MediaType.APPLICATION_JSON_VALUE)
        Mono<SuccessResponse<UserIdentityDto>> getUserIdentityByEmail(@RequestParam String useremail);

        // ========== CUSTOMER ROLES (B2B2C) ==========

        @GetExchange(value = "/api/v1/user-identity-customer-role/user/{userIdentityId}", accept = MediaType.APPLICATION_JSON_VALUE)
        Mono<SuccessResponse<List<CustomerRoleDto>>> getUserCustomerRoles(
                        @PathVariable("userIdentityId") Long userIdentityId);

        // ========== ROLE MANAGEMENT (CRUD COMPLETO) ==========

        @GetExchange(value = "/api/v1/role" + SimappeConstant.PATH_READ, accept = MediaType.APPLICATION_JSON_VALUE)
        Mono<SuccessResponse<List<RoleDto>>> readRoles();

        @GetExchange(value = "/api/v1/role" + SimappeConstant.PATH_GET, accept = MediaType.APPLICATION_JSON_VALUE)
        Mono<SuccessResponse<RoleDto>> getRole(@RequestParam Long id);

        @GetExchange(value = "/api/v1/role/get-name", accept = MediaType.APPLICATION_JSON_VALUE)
        Mono<SuccessResponse<RoleDto>> getRoleByName(@RequestParam String name);

        @PostExchange(value = "/api/v1/role" + SimappeConstant.PATH_CREATE, accept = MediaType.APPLICATION_JSON_VALUE)
        Mono<SuccessResponse<RoleDto>> createRole(@Valid @RequestBody RoleDto dto) throws SimappeException;

        @PutExchange(value = "/api/v1/role" + SimappeConstant.PATH_UPDATE, accept = MediaType.APPLICATION_JSON_VALUE)
        Mono<SuccessResponse<RoleDto>> updateRole(@Valid @RequestBody RoleDto dto, @RequestParam Long id);

        @PutExchange(value = "/api/v1/role/add-option", accept = MediaType.APPLICATION_JSON_VALUE)
        Mono<SuccessResponse<RoleDto>> addOptionToRole(@RequestParam Long roleId, @RequestParam Long optionId);

        @DeleteExchange(value = "/api/v1/role/delete-option", accept = MediaType.APPLICATION_JSON_VALUE)
        Mono<SuccessResponse<Void>> deleteOptionFromRole(@RequestParam Long roleId, @RequestParam Long optionId);

        @DeleteExchange(value = "/api/v1/role" + SimappeConstant.PATH_DELETE, accept = MediaType.APPLICATION_JSON_VALUE)
        Mono<SuccessResponse<Void>> deleteRole(@RequestParam Long id);

        @PostExchange(value = "/api/v1/role" + SimappeConstant.PATH_PAGE, accept = MediaType.APPLICATION_JSON_VALUE)
        Mono<SuccessResponse<PageDto<RoleDto>>> getRolePage(@RequestBody SimappeRequestQuery requestQuery)
                        throws SimappeException;

        // ========== ROLE PERMISSION MANAGEMENT ==========

        @GetExchange(value = "/api/v1/role-permission"
                        + SimappeConstant.PATH_READ, accept = MediaType.APPLICATION_JSON_VALUE)
        Mono<SuccessResponse<List<RolePermissionDto>>> readRolePermissions();

        @GetExchange(value = "/api/v1/role-permission/by-role", accept = MediaType.APPLICATION_JSON_VALUE)
        Mono<SuccessResponse<List<RolePermissionDto>>> readRolePermissionsByRole(@RequestParam Long roleId);

        @GetExchange(value = "/api/v1/role-permission"
                        + SimappeConstant.PATH_GET, accept = MediaType.APPLICATION_JSON_VALUE)
        Mono<SuccessResponse<RolePermissionDto>> getRolePermission(@RequestParam Long id);

        @PostExchange(value = "/api/v1/role-permission"
                        + SimappeConstant.PATH_CREATE, accept = MediaType.APPLICATION_JSON_VALUE)
        Mono<SuccessResponse<RolePermissionDto>> createRolePermission(@Valid @RequestBody RolePermissionDto dto)
                        throws SimappeException;

        @PutExchange(value = "/api/v1/role-permission"
                        + SimappeConstant.PATH_UPDATE, accept = MediaType.APPLICATION_JSON_VALUE)
        Mono<SuccessResponse<RolePermissionDto>> updateRolePermission(@Valid @RequestBody RolePermissionDto dto,
                        @RequestParam Long id);

        @DeleteExchange(value = "/api/v1/role-permission"
                        + SimappeConstant.PATH_DELETE, accept = MediaType.APPLICATION_JSON_VALUE)
        Mono<SuccessResponse<Void>> deleteRolePermission(@RequestParam Long id);

        @PostExchange(value = "/api/v1/role-permission/check", accept = MediaType.APPLICATION_JSON_VALUE)
        Mono<SuccessResponse<Boolean>> checkRolePermission(@RequestBody RolePermissionDto dto);

        @PostExchange(value = "/api/v1/role-permission/bulk-assign", accept = MediaType.APPLICATION_JSON_VALUE)
        Mono<SuccessResponse<List<RolePermissionDto>>> bulkAssignRolePermissions(
                        @RequestBody List<RolePermissionDto> dtos)
                        throws SimappeException;

        @PostExchange(value = "/api/v1/role-permission/page", accept = MediaType.APPLICATION_JSON_VALUE)
        Mono<SuccessResponse<PageDto<RolePermissionDto>>> getRolePermissionPage(
                        @RequestBody SimappeRequestQuery requestQuery) throws SimappeException;

        // ========== USER SESSION MANAGEMENT ==========

        @GetExchange(value = "/api/v1/user-session"
                        + SimappeConstant.PATH_READ, accept = MediaType.APPLICATION_JSON_VALUE)
        Mono<SuccessResponse<List<UserSessionDto>>> readUserSessions();

        @GetExchange(value = "/api/v1/user-session/active-by-user", accept = MediaType.APPLICATION_JSON_VALUE)
        Mono<SuccessResponse<List<UserSessionDto>>> readActiveUserSessionsByUser(@RequestParam Long userId);

        @GetExchange(value = "/api/v1/user-session/history-by-user", accept = MediaType.APPLICATION_JSON_VALUE)
        Mono<SuccessResponse<List<UserSessionDto>>> readUserSessionHistoryByUser(@RequestParam Long userId);

        @GetExchange(value = "/api/v1/user-session"
                        + SimappeConstant.PATH_GET, accept = MediaType.APPLICATION_JSON_VALUE)
        Mono<SuccessResponse<UserSessionDto>> getUserSession(@RequestParam Long id);

        @PostExchange(value = "/api/v1/user-session"
                        + SimappeConstant.PATH_CREATE, accept = MediaType.APPLICATION_JSON_VALUE)
        Mono<SuccessResponse<UserSessionDto>> createUserSession(@Valid @RequestBody UserSessionDto dto)
                        throws SimappeException;

        @PutExchange(value = "/api/v1/user-session"
                        + SimappeConstant.PATH_UPDATE, accept = MediaType.APPLICATION_JSON_VALUE)
        Mono<SuccessResponse<UserSessionDto>> updateUserSession(@Valid @RequestBody UserSessionDto dto,
                        @RequestParam Long id);

        @DeleteExchange(value = "/api/v1/user-session"
                        + SimappeConstant.PATH_DELETE, accept = MediaType.APPLICATION_JSON_VALUE)
        Mono<SuccessResponse<Void>> deleteUserSession(@RequestParam Long id);

        @PutExchange(value = "/api/v1/user-session/invalidate", accept = MediaType.APPLICATION_JSON_VALUE)
        Mono<SuccessResponse<UserSessionDto>> invalidateUserSession(@RequestParam Long sessionId);

        @PutExchange(value = "/api/v1/user-session/invalidate-all", accept = MediaType.APPLICATION_JSON_VALUE)
        Mono<SuccessResponse<List<UserSessionDto>>> invalidateAllUserSessions(@RequestParam Long userId);

        @PostExchange(value = "/api/v1/user-session/page", accept = MediaType.APPLICATION_JSON_VALUE)
        Mono<SuccessResponse<PageDto<UserSessionDto>>> getUserSessionPage(@RequestBody SimappeRequestQuery requestQuery)
                        throws SimappeException;

        // ========== USER IDENTITY OPERATIONS (OAUTH2) ==========

        @GetExchange(value = "/api/v1/user-identity"
                        + SimappeConstant.PATH_GET, accept = MediaType.APPLICATION_JSON_VALUE)
        Mono<SuccessResponse<UserIdentityDto>> getUserIdentity(@RequestParam Long id);

        @PostExchange(value = "/api/v1/user-identity"
                        + SimappeConstant.PATH_CREATE, accept = MediaType.APPLICATION_JSON_VALUE)
        Mono<SuccessResponse<UserIdentityDto>> createUserIdentity(@Valid @RequestBody UserIdentityDto dto)
                        throws SimappeException;

        @PutExchange(value = "/api/v1/user-identity"
                        + SimappeConstant.PATH_UPDATE, accept = MediaType.APPLICATION_JSON_VALUE)
        Mono<SuccessResponse<UserIdentityDto>> updateUserIdentity(@Valid @RequestBody UserIdentityDto dto,
                        @RequestParam Long id);

        @PutExchange(value = "/api/v1/user-identity/update-password", accept = MediaType.APPLICATION_JSON_VALUE)
        Mono<SuccessResponse<Void>> updateUserIdentityPassword(@RequestParam Long userId,
                        @RequestBody TransferDto2<String, String> password) throws SimappeException;

        @PutExchange(value = "/api/v1/user-identity/add-role", accept = MediaType.APPLICATION_JSON_VALUE)
        Mono<SuccessResponse<RoleDto>> addUserIdentityRole(@RequestBody TransferDto2<Long, Long> role);

        @DeleteExchange(value = "/api/v1/user-identity/delete-role", accept = MediaType.APPLICATION_JSON_VALUE)
        Mono<SuccessResponse<Void>> deleteUserIdentityRole(@RequestBody TransferDto2<Long, Long> role);

        @DeleteExchange(value = "/api/v1/user-identity"
                        + SimappeConstant.PATH_DELETE, accept = MediaType.APPLICATION_JSON_VALUE)
        Mono<SuccessResponse<Void>> deleteUserIdentity(@RequestParam Long id);

        @PostExchange(value = "/api/v1/user-identity"
                        + SimappeConstant.PATH_PAGE, accept = MediaType.APPLICATION_JSON_VALUE)
        Mono<SuccessResponse<PageDto<UserIdentityDto>>> getUserIdentityPage(
                        @RequestBody SimappeRequestQuery requestQuery)
                        throws SimappeException;
        
        @GetExchange(value = "/api/v1/user-identity/resolved-language")
        Mono<ResolvedLanguageDTO> getResolvedLanguage(@RequestParam Long userId);
}
