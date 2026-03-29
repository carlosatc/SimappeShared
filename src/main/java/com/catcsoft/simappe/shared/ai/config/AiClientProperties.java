/*
 * Copyright (c) 2024-2026 CatcSoft. Author: Carlos Torres Email: torrescamargo@gmail.com
 */
package com.catcsoft.simappe.shared.ai.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "simappe.ai")
public class AiClientProperties {

    /** Enable/disable AI client autoconfiguration */
    private boolean enabled = true;

    /** Base URL of SimappeAdmin (where AI endpoints live) */
    private String adminUrl = "http://simappe-admin";

    /** Connect timeout in milliseconds */
    private int connectTimeout = 5000;

    /** Read timeout in milliseconds */
    private int readTimeout = 60000;
}
