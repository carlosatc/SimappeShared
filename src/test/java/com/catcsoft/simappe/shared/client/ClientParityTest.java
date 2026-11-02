/*
 * Copyright (c) 2026 CatcSoft.
 *
 * This software is the confidential and proprietary information of CatcSoft
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of a
 * written license agreement entered into with CatcSoft.
 */
package com.catcsoft.simappe.shared.client;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.type.filter.RegexPatternTypeFilter;
import org.springframework.web.service.annotation.HttpExchange;

import java.util.regex.Pattern;

/**
 * Cada {@code XxxReactiveClient} de SimappeShared tiene su gemelo {@code XxxBlockingClient} en el
 * mismo paquete, con el mismo contrato: misma base, y por cada operación el mismo método HTTP, la
 * misma ruta y los mismos parámetros con sus anotaciones. Solo cambia el retorno: {@code Mono<T>}
 * pasa a {@code T}, {@code Mono<Void>} a {@code void} y {@code Flux<T>} a {@code List<T>}.
 */
class ClientParityTest {

    private static final String BASE = "com.catcsoft.simappe.shared";

    /** Interfaces de cliente del paquete base, por name completo. */
    private static Map<String, Class<?>> clients() throws ClassNotFoundException {
        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false) {
            @Override
            protected boolean isCandidateComponent(AnnotatedBeanDefinition definition) {
                return definition.getMetadata().isInterface();
            }
        };
        scanner.addIncludeFilter(new RegexPatternTypeFilter(Pattern.compile(".*(Blocking|Reactive)Client")));
        Map<String, Class<?>> found = new TreeMap<>();
        for (var definition : scanner.findCandidateComponents(BASE)) {
            found.put(definition.getBeanClassName(), Class.forName(definition.getBeanClassName()));
        }
        return found;
    }

    @Test
    void everyClientHasItsTwin() throws Exception {
        Map<String, Class<?>> all = clients();
        List<String> withoutTwin = new ArrayList<>();
        for (String name : all.keySet()) {
            String twin = name.endsWith("ReactiveClient")
                    ? name.replaceFirst("ReactiveClient$", "BlockingClient")
                    : name.replaceFirst("BlockingClient$", "ReactiveClient");
            if (!all.containsKey(twin)) {
                withoutTwin.add(name);
            }
        }
        assertThat(all).isNotEmpty();
        assertThat(withoutTwin).as("clientes sin gemelo").isEmpty();
    }

    @Test
    void twinsShareTheSameContract() throws Exception {
        List<String> differences = new ArrayList<>();
        for (Map.Entry<String, Class<?>> e : clients().entrySet()) {
            if (!e.getKey().endsWith("ReactiveClient")) {
                continue;
            }
            Class<?> reactive = e.getValue();
            Class<?> blocking = Class.forName(e.getKey().replaceFirst("ReactiveClient$", "BlockingClient"));
            compare(reactive, blocking, differences);
        }
        assertThat(differences).as("diferencias de contrato").isEmpty();
    }

    private static void compare(Class<?> reactive, Class<?> blocking, List<String> differences) {
        String pair = reactive.getSimpleName();
        if (!exchange(reactive).equals(exchange(blocking))) {
            differences.add(pair + ": base distinta");
        }
        Map<String, Method> rs = methods(reactive);
        Map<String, Method> bs = methods(blocking);
        if (!rs.keySet().equals(bs.keySet())) {
            differences.add(pair + ": operaciones distintas " + rs.keySet() + " vs " + bs.keySet());
            return;
        }
        for (String signature : rs.keySet()) {
            Method r = rs.get(signature);
            Method b = bs.get(signature);
            if (!exchange(r).equals(exchange(b))) {
                differences.add(pair + "." + signature + ": método o ruta distintos");
            }
            if (!parameters(r).equals(parameters(b))) {
                differences.add(pair + "." + signature + ": parámetros distintos");
            }
            String expected = plainReturnType(r.getGenericReturnType());
            if (!expected.equals(b.getGenericReturnType().getTypeName())) {
                differences.add(pair + "." + signature + ": retorno " + b.getGenericReturnType().getTypeName()
                        + ", se esperaba " + expected);
            }
        }
    }

    /** Operaciones por name y tipos de parámetros (admite sobrecargas). */
    private static Map<String, Method> methods(Class<?> type) {
        return Arrays.stream(type.getDeclaredMethods())
                .filter(m -> !m.isSynthetic() && !m.isDefault() && !java.lang.reflect.Modifier.isStatic(m.getModifiers()))
                .collect(Collectors.toMap(
                        m -> m.getName() + Arrays.stream(m.getGenericParameterTypes()).map(Type::getTypeName)
                                .collect(Collectors.joining(",", "(", ")")),
                        m -> m, (a, b) -> a, TreeMap::new));
    }

    /** Método HTTP, ruta y medios declarados, en forma comparable. */
    private static String exchange(java.lang.reflect.AnnotatedElement element) {
        HttpExchange h = AnnotatedElementUtils.findMergedAnnotation(element, HttpExchange.class);
        if (h == null) {
            return "-";
        }
        return h.method() + " " + h.value() + "|" + h.url() + "|" + h.contentType() + "|" + Arrays.toString(h.accept())
                + "|" + Arrays.toString(h.headers());
    }

    private static List<String> parameters(Method m) {
        List<String> out = new ArrayList<>();
        for (Parameter p : m.getParameters()) {
            out.add(p.getParameterizedType().getTypeName() + " " + p.getName() + " "
                    + Arrays.toString(p.getAnnotations()));
        }
        return out;
    }

    /** {@code Mono<T>} a {@code T}, {@code Mono<Void>} a {@code void}, {@code Flux<T>} a {@code List<T>}. */
    private static String plainReturnType(Type type) {
        if (type instanceof ParameterizedType pt) {
            String raw = pt.getRawType().getTypeName();
            String inner = pt.getActualTypeArguments()[0].getTypeName();
            if ("reactor.core.publisher.Mono".equals(raw)) {
                return "java.lang.Void".equals(inner) ? "void" : inner;
            }
            if ("reactor.core.publisher.Flux".equals(raw)) {
                return "java.util.List<" + inner + ">";
            }
        }
        return type.getTypeName();
    }
}
