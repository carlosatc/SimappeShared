/*
 * Copyright (c) 2024-2026 CatcSoft. Author: Carlos Torres Email: torrescamargo@gmail.com
 */
package com.catcsoft.simappe.shared.ai.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import com.catcsoft.simappe.shared.ai.client.AiClient;

import lombok.extern.slf4j.Slf4j;

/**
 * Auto-configuration for AiClient.
 *
 * Activation: simappe.ai.enabled=true (default)
 *
 * Properties:
 *   simappe.ai.admin-url=http://simappe-admin  (base URL of SimappeAdmin)
 *   simappe.ai.connect-timeout=5000
 *   simappe.ai.read-timeout=60000
 *
 * Usage: Just add SimappeShared as dependency. AiClient bean is auto-created.
 */
@Slf4j
@AutoConfiguration
@ConditionalOnClass(RestTemplate.class)
@ConditionalOnProperty(prefix = "simappe.ai", name = "enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(AiClientProperties.class)
public class AiClientAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public AiClient aiClient(AiClientProperties properties) {
        log.info("[SimappeShared] Creating AiClient - adminUrl: {}, connectTimeout: {}ms, readTimeout: {}ms",
                properties.getAdminUrl(), properties.getConnectTimeout(), properties.getReadTimeout());

        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(properties.getConnectTimeout());
        factory.setReadTimeout(properties.getReadTimeout());

        RestTemplate restTemplate = new RestTemplate(factory);
        return new AiClient(restTemplate, properties.getAdminUrl());
    }
}
