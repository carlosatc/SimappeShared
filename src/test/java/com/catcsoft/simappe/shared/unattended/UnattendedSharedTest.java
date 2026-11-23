/*
 * Copyright (c) 2026 CatcSoft
 *
 * Author: torrescamargo@gmail.com
 */
package com.catcsoft.simappe.shared.unattended;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.web.reactive.function.client.WebClient;

import com.catcsoft.simappe.commons.api.v1.core.unattended.ExecutionTokenIssuer;
import com.catcsoft.simappe.commons.api.v1.core.unattended.UnattendedTaskConfig;
import com.catcsoft.simappe.commons.api.v1.core.unattended.UnattendedTaskConfigSource;
import com.catcsoft.simappe.commons.api.v1.core.unattended.UnattendedTaskToken;
import com.catcsoft.simappe.model.admin.dto.security.UserSession;
import com.catcsoft.simappe.model.admin.enums.RoleType;
import com.catcsoft.simappe.model.core.database.enums.ConnectionContext;
import com.catcsoft.simappe.shared.unattended.adapter.AdminUnattendedTaskConfigSource;
import com.catcsoft.simappe.shared.unattended.adapter.OAuth2ExecutionTokenIssuer;
import com.catcsoft.simappe.shared.unattended.client.ExecutionTokenWebClient;
import com.catcsoft.simappe.shared.unattended.client.UnattendedTaskConfigWebClient;
import com.catcsoft.simappe.shared.unattended.config.UnattendedClientsAutoConfiguration;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpServer;

import reactor.core.publisher.Mono;

class UnattendedSharedTest {

    static final ObjectMapper JSON = new ObjectMapper();
    static final Clock CLOCK = Clock.fixed(Instant.parse("2026-11-22T05:00:00Z"), ZoneOffset.UTC);

    static UnattendedTaskConfig fila() {
        return new UnattendedTaskConfig(7L, "DIFERIDOS", "proceso.diferidos", 9L, 11L, 3L, "DEV", 50L, "PROCESOS",
                "ROLE_PROCESOS", "business", 900_000L);
    }

    @Nested
    class Emisor {

        ExecutionTokenWebClient client = mock(ExecutionTokenWebClient.class);
        OAuth2ExecutionTokenIssuer issuer = new OAuth2ExecutionTokenIssuer(client, "clave-de-prueba",
                Duration.ofSeconds(2), CLOCK);

        @Test
        void armaLaSesionDelProcesoYDevuelveElTokenConSuVigencia() throws Exception {
            when(client.generate(anyString(), anyLong(), any())).thenReturn(
                    Mono.just(JSON.readTree("{\"accessToken\":\"abc\"}")));
            UnattendedTaskToken t = issuer.issue(fila());
            assertThat(t.accessToken()).isEqualTo("abc");
            assertThat(t.issuedAt()).isEqualTo(CLOCK.instant());
            assertThat(t.expiresAt()).isEqualTo(CLOCK.instant().plusMillis(900_000));
            org.mockito.ArgumentCaptor<UserSession> s = org.mockito.ArgumentCaptor.forClass(UserSession.class);
            verify(client).generate(org.mockito.ArgumentMatchers.eq("clave-de-prueba"),
                    org.mockito.ArgumentMatchers.eq(900_000L), s.capture());
            UserSession u = s.getValue();
            assertThat(u.getUsername()).isEqualTo("proceso.diferidos");
            assertThat(u.getCustomerId()).isEqualTo(9L);
            assertThat(u.getCompanyId()).isEqualTo(11L);
            assertThat(u.getSubsidiaryId()).isEqualTo(3L);
            assertThat(u.getEnvironment()).isEqualTo("DEV");
            assertThat(u.getConnectionContext()).isEqualTo(ConnectionContext.BUSINESS);
            assertThat(u.getCompanyLocked()).isTrue();
            assertThat(u.getRoles()).hasSize(1);
            assertThat(u.getRoles().get(0).getId()).isEqualTo(50L);
            assertThat(u.getRoles().get(0).getAuthority()).isEqualTo("ROLE_PROCESOS");
            assertThat(u.getRoles().get(0).getRoleType()).isEqualTo(RoleType.BUSINESS);
        }

        @Test
        void leeElTokenEnvueltoEnResponse() throws Exception {
            when(client.generate(anyString(), anyLong(), any())).thenReturn(
                    Mono.just(JSON.readTree("{\"response\":{\"accessToken\":\"env\"}}")));
            assertThat(issuer.issue(fila()).accessToken()).isEqualTo("env");
        }

        @Test
        void leeElTokenDelSobreEstandarDeExito() throws Exception {
            when(client.generate(anyString(), anyLong(), any())).thenReturn(Mono.just(JSON.readTree(
                    "{\"response\":{\"metadata\":{\"apiVersion\":\"v1\"},\"content\":{\"accessToken\":\"sobre\","
                            + "\"expiresIn\":60000}}}")));
            assertThat(issuer.issue(fila()).accessToken()).isEqualTo("sobre");
        }

        @Test
        void respuestaSinTokenEsError() throws Exception {
            for (String body : new String[] { "{}", "{\"accessToken\":1}", "{\"response\":{}}", "{\"response\":{\"content\":{}}}", "null",
                    "{\"accessToken\":\"\"}" }) {
                when(client.generate(anyString(), anyLong(), any())).thenReturn(Mono.just(JSON.readTree(body)));
                assertThatThrownBy(() -> issuer.issue(fila())).as(body).isInstanceOf(IllegalStateException.class)
                        .hasMessageContaining("sin accessToken");
            }
            when(client.generate(anyString(), anyLong(), any())).thenReturn(Mono.empty());
            assertThatThrownBy(() -> issuer.issue(fila())).isInstanceOf(IllegalStateException.class);
        }

        @Test
        void filaIncompletaNoLlamaAlServidor() {
            UnattendedTaskConfig sinRol = new UnattendedTaskConfig(7L, "X", "u", 9L, 11L, null, "DEV", null, null,
                    null, null, 1000L);
            assertThatThrownBy(() -> issuer.issue(sinRol)).hasMessageContaining("rol");
            assertThatThrownBy(() -> issuer.issue(null)).isInstanceOf(NullPointerException.class);
            verify(client, never()).generate(anyString(), anyLong(), any());
        }

        @Test
        void tipoDeRolDesconocidoOVacioQuedaNulo() throws Exception {
            when(client.generate(anyString(), anyLong(), any())).thenReturn(
                    Mono.just(JSON.readTree("{\"accessToken\":\"abc\"}")));
            for (String tipo : new String[] { null, " ", "OTRO" }) {
                UnattendedTaskConfig f = new UnattendedTaskConfig(7L, "X", "u", 9L, 11L, null, "DEV", 50L, "R",
                        "ROLE_R", tipo, 1000L);
                issuer.issue(f);
            }
            org.mockito.ArgumentCaptor<UserSession> s = org.mockito.ArgumentCaptor.forClass(UserSession.class);
            verify(client, org.mockito.Mockito.times(3)).generate(anyString(), anyLong(), s.capture());
            assertThat(s.getAllValues()).allMatch(u -> u.getRoles().get(0).getRoleType() == null);
        }

        @Test
        void exigeClaveYDependencias() {
            assertThatThrownBy(() -> new OAuth2ExecutionTokenIssuer(client, " ", Duration.ofSeconds(1), CLOCK))
                    .isInstanceOf(IllegalArgumentException.class);
            assertThatThrownBy(() -> new OAuth2ExecutionTokenIssuer(client, null, Duration.ofSeconds(1), CLOCK))
                    .isInstanceOf(IllegalArgumentException.class);
            assertThatThrownBy(() -> new OAuth2ExecutionTokenIssuer(null, "k", Duration.ofSeconds(1), CLOCK))
                    .isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> new OAuth2ExecutionTokenIssuer(client, "k", null, CLOCK))
                    .isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> new OAuth2ExecutionTokenIssuer(client, "k", Duration.ofSeconds(1), null))
                    .isInstanceOf(NullPointerException.class);
        }
    }

    @Nested
    class Fuente {

        UnattendedTaskConfigWebClient client = mock(UnattendedTaskConfigWebClient.class);
        AdminUnattendedTaskConfigSource source = new AdminUnattendedTaskConfigSource(client, Duration.ofSeconds(2));

        @Test
        void devuelveLasFilas() {
            when(client.listActive("DIFERIDOS", "DEV")).thenReturn(Mono.just(List.of(fila())));
            assertThat(source.listActive("DIFERIDOS", "DEV")).containsExactly(fila());
        }

        @Test
        void vacioONuloEsListaVacia() {
            when(client.listActive("DIFERIDOS", "DEV")).thenReturn(Mono.empty());
            assertThat(source.listActive("DIFERIDOS", "DEV")).isEmpty();
        }

        @Test
        void unErrorDeRedNoSeConvierteEnCorridaVacia() {
            when(client.listActive("DIFERIDOS", "DEV")).thenReturn(Mono.error(new IOException("conexión rechazada")));
            assertThatThrownBy(() -> source.listActive("DIFERIDOS", "DEV")).isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("DIFERIDOS").hasMessageContaining("DEV");
            when(client.listActive("DIFERIDOS", "DEV")).thenReturn(Mono.never());
            AdminUnattendedTaskConfigSource corto = new AdminUnattendedTaskConfigSource(client, Duration.ofMillis(50));
            assertThatThrownBy(() -> corto.listActive("DIFERIDOS", "DEV")).isInstanceOf(IllegalStateException.class);
        }

        @Test
        void exigeDependencias() {
            assertThatThrownBy(() -> new AdminUnattendedTaskConfigSource(null, Duration.ofSeconds(1)))
                    .isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> new AdminUnattendedTaskConfigSource(client, null))
                    .isInstanceOf(NullPointerException.class);
        }
    }

    @Nested
    class Cable {

        HttpServer server;
        final Map<String, String> visto = new ConcurrentHashMap<>();

        @BeforeEach
        void up() throws IOException {
            server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
            server.createContext("/", ex -> {
                visto.put("method", ex.getRequestMethod());
                visto.put("path", ex.getRequestURI().getPath());
                visto.put("query", String.valueOf(ex.getRequestURI().getQuery()));
                visto.put("key", String.valueOf(ex.getRequestHeaders().getFirst("X-Nebula-Service-Key")));
                visto.put("body", new String(ex.getRequestBody().readAllBytes(), StandardCharsets.UTF_8));
                String r = ex.getRequestURI().getPath().contains("execution-token")
                        ? "{\"success\":true,\"response\":{\"accessToken\":\"cable\"}}"
                        : "[{\"id\":7,\"taskType\":\"DIFERIDOS\",\"username\":\"proceso.diferidos\",\"clientId\":9,"
                                + "\"companyId\":11,\"subsidiaryId\":null,\"environment\":\"DEV\",\"roleId\":50,"
                                + "\"roleName\":\"PROCESOS\",\"roleAuthority\":\"ROLE_PROCESOS\","
                                + "\"roleType\":\"BUSINESS\",\"expirationMs\":900000}]";
                byte[] b = r.getBytes(StandardCharsets.UTF_8);
                ex.getResponseHeaders().add("Content-Type", "application/json");
                ex.sendResponseHeaders(200, b.length);
                try (OutputStream os = ex.getResponseBody()) {
                    os.write(b);
                }
            });
            server.start();
        }

        @AfterEach
        void down() {
            server.stop(0);
        }

        String base() {
            return "http://127.0.0.1:" + server.getAddress().getPort();
        }

        ApplicationContextRunner runner() {
            return new ApplicationContextRunner()
                    .withConfiguration(AutoConfigurations.of(UnattendedClientsAutoConfiguration.class))
                    .withBean(WebClient.Builder.class, WebClient::builder)
                    .withPropertyValues("simappe.admin.url=" + base(), "simappe.oauth2.url=" + base());
        }

        @Test
        void fuenteYEmisorHablanConAdminYOAuth2PorSuRuta() {
            runner().withPropertyValues("simappe.unattended.service-key=clave-de-prueba").run(ctx -> {
                assertThat(ctx).hasNotFailed();
                List<UnattendedTaskConfig> filas = ctx.getBean(UnattendedTaskConfigSource.class)
                        .listActive("DIFERIDOS", "DEV");
                assertThat(visto.get("method")).isEqualTo("GET");
                assertThat(visto.get("path")).isEqualTo("/simappe-admin/api/v1/internal/unattended-task-configs");
                assertThat(visto.get("query")).contains("taskType=DIFERIDOS").contains("environment=DEV");
                assertThat(filas).hasSize(1);
                assertThat(filas.get(0).companyId()).isEqualTo(11L);
                assertThat(filas.get(0).expirationMs()).isEqualTo(900_000L);

                UnattendedTaskToken t = ctx.getBean(ExecutionTokenIssuer.class).issue(filas.get(0));
                assertThat(t.accessToken()).isEqualTo("cable");
                assertThat(visto.get("method")).isEqualTo("POST");
                assertThat(visto.get("path")).isEqualTo("/simappe-oauth2-server/api/v1/execution-token/generate");
                assertThat(visto.get("query")).isEqualTo("expirationMs=900000");
                assertThat(visto.get("key")).isEqualTo("clave-de-prueba");
                JsonNode body = JSON.readTree(visto.get("body"));
                assertThat(body.get("username").asText()).isEqualTo("proceso.diferidos");
                assertThat(body.get("customerId").asLong()).isEqualTo(9L);
                assertThat(body.get("companyId").asLong()).isEqualTo(11L);
                assertThat(body.get("connectionContext").asText()).isEqualTo("BUSINESS");
                assertThat(body.get("companyLocked").asBoolean()).isTrue();
                assertThat(body.get("roles").get(0).get("authority").asText()).isEqualTo("ROLE_PROCESOS");
            });
        }

        @Test
        void tomaLaClaveExistenteDeNebulaSiNoHayLaPropia() {
            runner().withPropertyValues("nebula.execution-token.service-key=clave-nebula").run(ctx -> {
                assertThat(ctx).hasSingleBean(ExecutionTokenIssuer.class);
                ctx.getBean(ExecutionTokenIssuer.class).issue(fila());
                assertThat(visto.get("key")).isEqualTo("clave-nebula");
            });
        }

        @Test
        void sinClaveNoCreaNadaYElServicioArranca() {
            runner().run(ctx -> {
                assertThat(ctx).hasNotFailed();
                assertThat(ctx).doesNotHaveBean(ExecutionTokenIssuer.class);
                assertThat(ctx).doesNotHaveBean(UnattendedTaskConfigSource.class);
            });
        }

        @Test
        void respetaLosBeansPropiosDelServicio() {
            ExecutionTokenIssuer propio = c -> null;
            runner().withPropertyValues("simappe.unattended.service-key=k")
                    .withBean(ExecutionTokenIssuer.class, () -> propio)
                    .run(ctx -> assertThat(ctx.getBean(ExecutionTokenIssuer.class)).isSameAs(propio));
        }
    }
}
