/*
 * Copyright (c) 2024-2026 CatcSoft. Author: Carlos Torres Email: torrescamargo@gmail.com
 */
package com.catcsoft.simappe.shared.ai.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * AI Client configuration properties. Values come from SimappeConfig (config-server).
 *
 * Properties (in simappe-vote-{env}.yml):
 *   simappe.ai.enabled          - Enable/disable AI client (default: true)
 *   simappe.ai.gateway-url      - Primary: SimappeClient AI Gateway URL
 *   simappe.ai.fallback-url     - Fallback: SimappeAdmin legacy AI URL
 *   simappe.ai.fallback-enabled - Enable fallback to SimappeAdmin (default: true)
 *   simappe.ai.connect-timeout  - Connection timeout ms (default: 5000)
 *   simappe.ai.read-timeout     - Read timeout ms (default: 60000)
 */
@Data
@ConfigurationProperties(prefix = "simappe.ai")
public class AiClientProperties {

    /** Enable/disable AI client autoconfiguration */
    private boolean enabled = true;

    /** Base URL of SimappeClient AI Gateway (from config-server) */
    private String gatewayUrl;

    /** Base URL of SimappeAdmin legacy AI endpoints (fallback, from config-server) */
    private String fallbackUrl;

    /** Enable fallback to SimappeAdmin when SimappeClient fails */
    private boolean fallbackEnabled = true;

    /** Connect timeout in milliseconds */
    private int connectTimeout = 5000;

    /** Read timeout in milliseconds */
    private int readTimeout = 60000;
}
