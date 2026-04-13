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
 * Properties:
 *   simappe.ai.gateway-url=http://simappe-client     (primary — AI Gateway in SimappeClient)
 *   simappe.ai.fallback-url=http://simappe-admin      (fallback — old API in SimappeAdmin)
 *   simappe.ai.fallback-enabled=true                   (enable/disable fallback)
 *   simappe.ai.connect-timeout=5000
 *   simappe.ai.read-timeout=60000
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
        log.info("[SimappeShared] Creating AiClient - gatewayUrl: {}, fallbackUrl: {}, fallbackEnabled: {}",
                properties.getGatewayUrl(), properties.getFallbackUrl(), properties.isFallbackEnabled());

        RestTemplate restTemplate = createRestTemplate(properties);

        String fallbackUrl = properties.isFallbackEnabled() ? properties.getFallbackUrl() : null;
        return new AiClient(restTemplate, properties.getGatewayUrl(), fallbackUrl);
    }

    private RestTemplate createRestTemplate(AiClientProperties properties) {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(properties.getConnectTimeout());
        factory.setReadTimeout(properties.getReadTimeout());
        return new RestTemplate(factory);
    }
}
