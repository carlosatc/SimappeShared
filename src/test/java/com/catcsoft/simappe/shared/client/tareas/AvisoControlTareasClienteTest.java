/*
 * Copyright (c) 2026 CatcSoft.
 *
 * This software is the confidential and proprietary information of CatcSoft
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of a
 * written license agreement entered into with CatcSoft.
 */
package com.catcsoft.simappe.shared.client.tareas;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.catcsoft.simappe.commons.api.v1.util.SimappeConstant;

/**
 * El aviso al control central por su cliente (HT_ARQ_013).
 */
class AvisoControlTareasClienteTest {

    @AfterEach
    void limpiar() {
        RequestContextHolder.resetRequestAttributes();
    }

    @Test
    @DisplayName("El aviso va con el token del hilo; sin token no se envia; si el control falla no tumba nada")
    void avisoConElTokenDelHilo() {
        ControlTareasBlockingClient cliente = mock(ControlTareasBlockingClient.class);
        AvisoControlTareasCliente aviso = new AvisoControlTareasCliente(cliente);
        aviso.pedirReconciliacion("s", "T1");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(new MockHttpServletRequest()));
        aviso.pedirReconciliacion("s", "T1");
        verify(cliente, never()).reconciliar(any(), any(), any());
        MockHttpServletRequest peticion = new MockHttpServletRequest();
        peticion.addHeader(SimappeConstant.AUTHORIZATION, "Bearer tok");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(peticion));
        aviso.pedirReconciliacion("s", "T1");
        verify(cliente).reconciliar("s", "T1", "Bearer tok");
        when(cliente.reconciliar(any(), any(), any())).thenThrow(new IllegalStateException("caido"));
        aviso.pedirReconciliacion("s", "T2");
    }
}
