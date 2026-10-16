/*
 * Copyright (c) 2026 CatcSoft
 *
 * This software is the confidential and proprietary information of CatcSoft
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of a
 * written license agreement entered into with CatcSoft.
 */
package com.catcsoft.simappe.shared.client.oauth2;

import java.lang.reflect.Method;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

import com.catcsoft.simappe.model.admin.dto.security.UserSession;

import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Fija el contrato de red del par de clientes del token de ejecucion.
 *
 * <h2>Por que esto se prueba con reflexion y no con una llamada</h2>
 * <p>
 * Estos clientes son interfaces declarativas: quien construye la peticion es el proxy que
 * Spring genera a partir de las anotaciones, no un cuerpo de metodo que se pueda ejercitar.
 * La ruta, el verbo y la cabecera de autenticacion <b>son</b> la implementacion, y estan
 * escritos en un sitio donde un cambio no rompe la compilacion de nadie: se descubre en
 * ejecucion, contra el emisor, con un 404 o un 401 que no dice por que.
 * </p>
 * <p>
 * De ahi que lo que se afirma aqui sea justamente eso — a donde va, con que verbo y con que
 * cabecera — y que se afirme sobre <b>los dos</b> clientes: en Simappe un cliente compartido
 * se publica siempre bloqueante y reactivo, y si sus contratos se separan, el consumidor que
 * elija la variante equivocada llama a otro sitio.
 * </p>
 */
@DisplayName("Clientes del token de ejecucion — contrato")
class ExecutionTokenClientContractTest {

    /** Ruta base del recurso en el emisor, atravesando el gateway. */
    private static final String URL_BASE = "/simappe-oauth2-server/api/v1/execution-token";

    /** Cabecera con la que el emisor reconoce al servicio que pide el token. */
    private static final String CABECERA_SERVICIO = "X-Nebula-Service-Key";

    /**
     * Recupera el unico metodo {@code generate} de la interfaz indicada.
     *
     * @param cliente interfaz del cliente
     * @return el metodo
     */
    private Method generate(Class<?> cliente) {
        return java.util.Arrays.stream(cliente.getDeclaredMethods())
                .filter(m -> "generate".equals(m.getName()))
                .findFirst()
                .orElseThrow(() -> new AssertionError("La interfaz no declara generate(): " + cliente));
    }

    @Nested
    @DisplayName("Cliente bloqueante")
    class Bloqueante {

        @Test
        @DisplayName("apunta al recurso del emisor a traves del gateway")
        void rutaBase() {
            HttpExchange exchange = ExecutionTokenBlockingClient.class.getAnnotation(HttpExchange.class);

            assertThat(exchange).isNotNull();
            assertThat(exchange.url()).isEqualTo(URL_BASE);
            assertThat(exchange.contentType()).isEqualTo("application/json");
            assertThat(exchange.accept()).contains("application/json");
        }

        @Test
        @DisplayName("emite por POST /generate")
        void verboYRuta() {
            PostExchange post = generate(ExecutionTokenBlockingClient.class).getAnnotation(PostExchange.class);

            assertThat(post).isNotNull();
            assertThat(post.value()).isEqualTo("/generate");
        }

        /**
         * Es la particularidad de este cliente frente a todos los demas de Simappe: se pide
         * precisamente cuando no hay token vigente que ofrecer, de modo que la unica
         * credencial que puede llevar es el secreto de servicio. Si alguien le agregara un
         * {@code Authorization}, la llamada dejaria de funcionar en el caso para el que existe.
         */
        @Test
        @DisplayName("se autentica con el secreto de servicio y con nada mas")
        void soloSecretoDeServicio() {
            Method m = generate(ExecutionTokenBlockingClient.class);

            var cabeceras = java.util.Arrays.stream(m.getParameters())
                    .map(p -> p.getAnnotation(RequestHeader.class))
                    .filter(java.util.Objects::nonNull)
                    .map(RequestHeader::value)
                    .toList();

            assertThat(cabeceras)
                    .containsExactly(CABECERA_SERVICIO)
                    .noneMatch(h -> h.equalsIgnoreCase("Authorization"));
        }

        @Test
        @DisplayName("manda la sesion del lanzamiento en el cuerpo, no una credencial")
        void cuerpoEsLaSesion() {
            Method m = generate(ExecutionTokenBlockingClient.class);

            var cuerpo = java.util.Arrays.stream(m.getParameters())
                    .filter(p -> p.getAnnotation(RequestBody.class) != null)
                    .toList();

            assertThat(cuerpo).hasSize(1);
            assertThat(cuerpo.get(0).getType()).isEqualTo(UserSession.class);
        }
    }

    @Nested
    @DisplayName("Cliente reactivo")
    class Reactivo {

        @Test
        @DisplayName("comparte ruta base y verbo con el bloqueante")
        void mismoContratoDeRed() {
            HttpExchange exchange = ExecutionTokenReactiveClient.class.getAnnotation(HttpExchange.class);
            PostExchange post = generate(ExecutionTokenReactiveClient.class).getAnnotation(PostExchange.class);

            assertThat(exchange).isNotNull();
            assertThat(exchange.url()).isEqualTo(URL_BASE);
            assertThat(post).isNotNull();
            assertThat(post.value()).isEqualTo("/generate");
        }

        @Test
        @DisplayName("se autentica igual: secreto de servicio y sesion en el cuerpo")
        void mismaAutenticacion() {
            Method m = generate(ExecutionTokenReactiveClient.class);

            var cabeceras = java.util.Arrays.stream(m.getParameters())
                    .map(p -> p.getAnnotation(RequestHeader.class))
                    .filter(java.util.Objects::nonNull)
                    .map(RequestHeader::value)
                    .toList();

            assertThat(cabeceras).containsExactly(CABECERA_SERVICIO);
            assertThat(java.util.Arrays.stream(m.getParameters())
                    .filter(p -> p.getAnnotation(RequestBody.class) != null)
                    .map(java.lang.reflect.Parameter::getType))
                    .containsExactly(UserSession.class);
        }

        /**
         * Lo unico que debe diferenciarlos. Si el reactivo devolviera el envelope desnudo,
         * bloquearia el hilo del llamante y dejaria de ser reactivo sin que nadie lo note.
         */
        @Test
        @DisplayName("y solo se diferencia en que devuelve Mono")
        void retornoReactivo() {
            assertThat(generate(ExecutionTokenReactiveClient.class).getReturnType()).isEqualTo(Mono.class);
            assertThat(generate(ExecutionTokenBlockingClient.class).getReturnType()).isNotEqualTo(Mono.class);
        }
    }
}
