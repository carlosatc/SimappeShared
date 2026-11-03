/*
 * Copyright (c) 2025 CatcSoft
 *
 * This software is the confidential and proprietary information of CatcSoft
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with CatcSoft.
 *
 * @author torrescamargo@gmail.com
 */
package com.catcsoft.simappe.shared.client.admin;

import java.util.List;

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
import com.catcsoft.simappe.model.admin.dto.i18n.LanguageDto;
import com.catcsoft.simappe.model.admin.dto.i18n.LocaleDto;
import com.catcsoft.simappe.model.admin.dto.i18n.TranslationBundleDto;
import com.catcsoft.simappe.model.admin.dto.i18n.TranslationDto;
import com.catcsoft.simappe.model.admin.dto.i18n.TranslationKeyDto;

import jakarta.validation.Valid;

/**
 * Cliente WebClient modular para operaciones de internacionalización en
 * Simappe.
 * Proporciona métodos para la gestión de idiomas, locales, traducciones,
 * paquetes de traducción y claves de traducción.
 * 
 * <p>
 * Este cliente forma parte de la arquitectura modular v2 que reemplaza
 * gradualmente
 * al SimappeAdminWebClient monolítico, ofreciendo una interfaz especializada
 * para i18n.
 * 
 * @author torrescamargo@gmail.com
 * @since 2026-01-18
 * @version 2.6.0
 */
@HttpExchange("simappe-admin")
public interface I18nBlockingClient {

        // ========== GESTIÓN DE IDIOMAS (LANGUAGE MANAGEMENT) ==========

        @GetExchange(value = "/api/v1/language" + SimappeConstant.PATH_READ, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<LanguageDto>> readLanguages();

        @GetExchange(value = "/api/v1/language" + SimappeConstant.PATH_GET, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<LanguageDto> getLanguage(@RequestParam Long id);

        @PostExchange(value = "/api/v1/language"
                        + SimappeConstant.PATH_CREATE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<LanguageDto> createLanguage(@Valid @RequestBody LanguageDto dto) throws SimappeException;

        @PutExchange(value = "/api/v1/language"
                        + SimappeConstant.PATH_UPDATE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<LanguageDto> updateLanguage(@Valid @RequestBody LanguageDto dto, @RequestParam Long id);

        @DeleteExchange(value = "/api/v1/language"
                        + SimappeConstant.PATH_DELETE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<Void> deleteLanguage(@RequestParam Long id);

        @PostExchange(value = "/api/v1/language" + SimappeConstant.PATH_PAGE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<PageDto<LanguageDto>> getLanguagePage(@RequestBody SimappeRequestQuery requestQuery)
                        throws SimappeException;

        // ========== GESTIÓN DE LOCALIZACIÓN (LOCALE MANAGEMENT) ==========

        @GetExchange(value = "/api/v1/locale" + SimappeConstant.PATH_READ, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<LocaleDto>> readLocales();

        @GetExchange(value = "/api/v1/locale" + SimappeConstant.PATH_GET, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<LocaleDto> getLocale(@RequestParam Long id);

        @PostExchange(value = "/api/v1/locale" + SimappeConstant.PATH_CREATE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<LocaleDto> createLocale(@Valid @RequestBody LocaleDto dto) throws SimappeException;

        @PutExchange(value = "/api/v1/locale" + SimappeConstant.PATH_UPDATE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<LocaleDto> updateLocale(@Valid @RequestBody LocaleDto dto, @RequestParam Long id);

        @DeleteExchange(value = "/api/v1/locale"
                        + SimappeConstant.PATH_DELETE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<Void> deleteLocale(@RequestParam Long id);

        @PostExchange(value = "/api/v1/locale" + SimappeConstant.PATH_PAGE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<PageDto<LocaleDto>> getLocalePage(@RequestBody SimappeRequestQuery requestQuery)
                        throws SimappeException;

        // ========== GESTIÓN DE TRADUCCIONES (TRANSLATION MANAGEMENT) ==========

        @GetExchange(value = "/api/v1/translation"
                        + SimappeConstant.PATH_READ, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<TranslationDto>> readTranslations();

        @GetExchange(value = "/api/v1/translation"
                        + SimappeConstant.PATH_GET, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<TranslationDto> getTranslation(@RequestParam Long id);

        @PostExchange(value = "/api/v1/translation"
                        + SimappeConstant.PATH_CREATE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<TranslationDto> createTranslation(@Valid @RequestBody TranslationDto dto)
                        throws SimappeException;

        @PutExchange(value = "/api/v1/translation"
                        + SimappeConstant.PATH_UPDATE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<TranslationDto> updateTranslation(@Valid @RequestBody TranslationDto dto,
                        @RequestParam Long id);

        @DeleteExchange(value = "/api/v1/translation"
                        + SimappeConstant.PATH_DELETE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<Void> deleteTranslation(@RequestParam Long id);

        @PostExchange(value = "/api/v1/translation"
                        + SimappeConstant.PATH_PAGE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<PageDto<TranslationDto>> getTranslationPage(@RequestBody SimappeRequestQuery requestQuery)
                        throws SimappeException;

        // ========== GESTIÓN DE PAQUETES DE TRADUCCIÓN (TRANSLATION BUNDLE MANAGEMENT)
        // ==========
        // DEPRECATED: el CRUD translation-bundle es legacy; las traducciones en runtime se sirven por
        // GET /api/v1/translation/bundle/{langCode}. Se elimina en el próximo MAJOR.

        /**
         * Recupera todos los paquetes de traducción registrados.
         *
         * @return la lista de paquetes de traducción
         * @deprecated CRUD legacy de {@code translation_bundles}; se elimina en el próximo MAJOR.
         */
        @Deprecated(since = "5.10.0", forRemoval = true)
        @SuppressWarnings("java:S1133")
        @GetExchange(value = "/api/v1/translation-bundle"
                        + SimappeConstant.PATH_READ, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<TranslationBundleDto>> readTranslationBundles();

        /**
         * Recupera un paquete de traducción por su ID.
         *
         * @param id el ID del paquete
         * @return el paquete de traducción
         * @deprecated CRUD legacy de {@code translation_bundles}; se elimina en el próximo MAJOR.
         */
        @Deprecated(since = "5.10.0", forRemoval = true)
        @SuppressWarnings("java:S1133")
        @GetExchange(value = "/api/v1/translation-bundle"
                        + SimappeConstant.PATH_GET, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<TranslationBundleDto> getTranslationBundle(@RequestParam Long id);

        /**
         * Crea un nuevo paquete de traducción.
         *
         * @param dto el DTO del paquete de traducción
         * @return el paquete de traducción creado
         * @throws SimappeException si ocurre un error durante la creación
         * @deprecated CRUD legacy de {@code translation_bundles}; se elimina en el próximo MAJOR.
         */
        @Deprecated(since = "5.10.0", forRemoval = true)
        @SuppressWarnings("java:S1133")
        @PostExchange(value = "/api/v1/translation-bundle"
                        + SimappeConstant.PATH_CREATE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<TranslationBundleDto> createTranslationBundle(
                        @Valid @RequestBody TranslationBundleDto dto)
                        throws SimappeException;

        /**
         * Actualiza un paquete de traducción existente.
         *
         * @param dto el DTO del paquete con los cambios
         * @param id  el ID del paquete a actualizar
         * @return el paquete de traducción actualizado
         * @deprecated CRUD legacy de {@code translation_bundles}; se elimina en el próximo MAJOR.
         */
        @Deprecated(since = "5.10.0", forRemoval = true)
        @SuppressWarnings("java:S1133")
        @PutExchange(value = "/api/v1/translation-bundle"
                        + SimappeConstant.PATH_UPDATE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<TranslationBundleDto> updateTranslationBundle(
                        @Valid @RequestBody TranslationBundleDto dto,
                        @RequestParam Long id);

        /**
         * Elimina un paquete de traducción por su ID.
         *
         * @param id el ID del paquete a eliminar
         * @return una respuesta vacía indicando el resultado de la operación
         * @deprecated CRUD legacy de {@code translation_bundles}; se elimina en el próximo MAJOR.
         */
        @Deprecated(since = "5.10.0", forRemoval = true)
        @SuppressWarnings("java:S1133")
        @DeleteExchange(value = "/api/v1/translation-bundle"
                        + SimappeConstant.PATH_DELETE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<Void> deleteTranslationBundle(@RequestParam Long id);

        /**
         * Recupera una lista paginada de paquetes de traducción.
         *
         * @param requestQuery los parámetros de consulta y paginación
         * @return una página de paquetes de traducción
         * @throws SimappeException si ocurre un error durante la consulta
         * @deprecated CRUD legacy de {@code translation_bundles}; se elimina en el próximo MAJOR.
         */
        @Deprecated(since = "5.10.0", forRemoval = true)
        @SuppressWarnings("java:S1133")
        @PostExchange(value = "/api/v1/translation-bundle"
                        + SimappeConstant.PATH_PAGE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<PageDto<TranslationBundleDto>> getTranslationBundlePage(
                        @RequestBody SimappeRequestQuery requestQuery) throws SimappeException;

        // ========== GESTIÓN DE CLAVES DE TRADUCCIÓN (TRANSLATION KEY MANAGEMENT)
        // ==========

        @GetExchange(value = "/api/v1/translation-key"
                        + SimappeConstant.PATH_READ, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<List<TranslationKeyDto>> readTranslationKeys();

        @GetExchange(value = "/api/v1/translation-key"
                        + SimappeConstant.PATH_GET, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<TranslationKeyDto> getTranslationKey(@RequestParam Long id);

        @PostExchange(value = "/api/v1/translation-key"
                        + SimappeConstant.PATH_CREATE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<TranslationKeyDto> createTranslationKey(@Valid @RequestBody TranslationKeyDto dto)
                        throws SimappeException;

        @PutExchange(value = "/api/v1/translation-key"
                        + SimappeConstant.PATH_UPDATE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<TranslationKeyDto> updateTranslationKey(@Valid @RequestBody TranslationKeyDto dto,
                        @RequestParam Long id);

        @DeleteExchange(value = "/api/v1/translation-key"
                        + SimappeConstant.PATH_DELETE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<Void> deleteTranslationKey(@RequestParam Long id);

        @PostExchange(value = "/api/v1/translation-key"
                        + SimappeConstant.PATH_PAGE, accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<PageDto<TranslationKeyDto>> getTranslationKeyPage(
                        @RequestBody SimappeRequestQuery requestQuery) throws SimappeException;

        // ========== RESOLUCIÓN DE TRADUCCIONES (i18n Runtime) ==========

        /**
         * Resuelve una traducción específica por código de clave y código de idioma.
         * Utilizado por
         * {@link com.catcsoft.simappe.commons.api.v1.core.i18n.SimappeMessageSource}
         * para obtener traducciones bajo demanda.
         * 
         * @param key          Código de la clave de traducción (ej. "error.notfound")
         * @param languageCode Código ISO 639-1 del idioma (ej. "es", "en", "fr")
         * @return La traducción encontrada
         * @since 2.7.0
         */
        @GetExchange(value = "/api/v1/translation/resolve", accept = MediaType.APPLICATION_JSON_VALUE)
        SuccessResponse<TranslationDto> getTranslationByKeyAndLanguage(
                        @RequestParam String key,
                        @RequestParam String languageCode);
}
