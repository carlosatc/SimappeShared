/*
 * Copyright (c) 2026 CatcSoft.
 *
 * This software is the confidential and proprietary information of CatcSoft
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of a
 * written license agreement entered into with CatcSoft.
 *
 * Author: Carlos Torres
 * Date: 2026-10-02
 */
package com.catcsoft.simappe.shared.consecutivo;

import com.catcsoft.simappe.model.configurations.consecutivo.record.ConsecutivoEmitidoDb;
import com.catcsoft.simappe.model.configurations.consecutivo.record.ConsecutivoReservadoDb;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.StoredProcedureQuery;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * La puerta al paquete de consecutivos traduce cada procedimiento a su resultado, sin regla propia.
 *
 * @author Carlos Torres
 * @since 0.15.0
 */
class ConsecutivoDbGatewayTest {

    private final EntityManager entityManager = mock(EntityManager.class);
    private final StoredProcedureQuery procedimiento = mock(StoredProcedureQuery.class);
    private final ConsecutivoDbGateway gateway = new ConsecutivoDbGateway();

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(gateway, "entityManager", entityManager);
        when(entityManager.createStoredProcedureQuery(any())).thenReturn(procedimiento);
    }

    @Test
    void emitirDevuelveElNumeroDelPaquete() {
        when(procedimiento.getOutputParameterValue("p_valor_numerico")).thenReturn(5L);
        when(procedimiento.getOutputParameterValue("p_valor_formateado")).thenReturn("FE5");
        when(procedimiento.getOutputParameterValue("p_periodo")).thenReturn("2026");

        ConsecutivoEmitidoDb emitido = gateway.emitir("FAC-FV-FE", 7L, 5L, null, "DOCFAC:60", "ctx", "u");

        assertThat(emitido.valorNumerico()).isEqualTo(5L);
        assertThat(emitido.valorFormateado()).isEqualTo("FE5");
        assertThat(emitido.periodo()).isEqualTo("2026");
        verify(entityManager).createStoredProcedureQuery("configurations.pkg_consecutivo_p_emitir");
        verify(procedimiento).setParameter("p_clave_idempotencia", "DOCFAC:60");
        verify(procedimiento).execute();
    }

    @Test
    void reservarDevuelveTokenYVencimiento() {
        LocalDateTime vence = LocalDateTime.of(2026, 10, 2, 12, 0);
        when(procedimiento.getOutputParameterValue("p_token")).thenReturn("T1");
        when(procedimiento.getOutputParameterValue("p_valor_numerico")).thenReturn(7L);
        when(procedimiento.getOutputParameterValue("p_valor_formateado")).thenReturn("R7");
        when(procedimiento.getOutputParameterValue("p_expira_en")).thenReturn(Timestamp.valueOf(vence));

        ConsecutivoReservadoDb reserva = gateway.reservar("R", 7L, 5L, null, "ctx", "u", 10);

        assertThat(reserva.token()).isEqualTo("T1");
        assertThat(reserva.valorNumerico()).isEqualTo(7L);
        assertThat(reserva.expiraEn()).isEqualTo(vence);
        verify(procedimiento).setParameter("p_ttl_minutos", 10L);
    }

    @Test
    void reservarSinVencimientoNoFalla() {
        assertThat(gateway.reservar("R", 7L, 5L, null, "ctx", "u", 10).expiraEn()).isNull();
    }

    @Test
    void confirmarYAnularVanASuProcedimiento() {
        gateway.confirmar("T1", 7L, 5L, null, "u");
        gateway.anular("T2", 7L, 5L, null, "u");

        verify(entityManager).createStoredProcedureQuery("configurations.pkg_consecutivo_p_confirmar");
        verify(entityManager).createStoredProcedureQuery("configurations.pkg_consecutivo_p_anular");
        verify(procedimiento).setParameter("p_token", "T1");
        verify(procedimiento).setParameter("p_token", "T2");
    }

    @Test
    void liberarVencidasDevuelveCuantas() {
        Query consulta = mock(Query.class);
        when(entityManager.createNativeQuery(any(String.class))).thenReturn(consulta);
        when(consulta.setParameter(anyInt(), any())).thenReturn(consulta);
        when(consulta.getSingleResult()).thenReturn(3, (Object) null);

        assertThat(gateway.liberarVencidas(7L, 5L, null)).isEqualTo(3);
        assertThat(gateway.liberarVencidas(7L, 5L, null)).isZero();
    }

    @Test
    void elSqlstateDeLasRutinasSeTraduceAlCodigoDeLaRegla() {
        assertThat(ConsecutivoDbGateway.codigoRegla("CN001")).isEqualTo(ConsecutivoDbGateway.ERR_NO_ENCONTRADO);
        assertThat(ConsecutivoDbGateway.codigoRegla("CN008")).isEqualTo(ConsecutivoDbGateway.ERR_RESERVA_VENCIDA);
        assertThat(ConsecutivoDbGateway.codigoRegla("23505")).isZero();
        assertThat(ConsecutivoDbGateway.codigoRegla("CN0xx")).isZero();
        assertThat(ConsecutivoDbGateway.codigoRegla(null)).isZero();
    }
}
