/*
 * Copyright (c) 2026 CatcSoft
 *
 * This software is the confidential and proprietary information of CatcSoft
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of a
 * written license agreement entered into with CatcSoft.
 */
package com.catcsoft.simappe.shared.consecutivo;

import com.catcsoft.simappe.model.configurations.consecutivo.record.ConsecutivoEmitidoDb;
import com.catcsoft.simappe.model.configurations.consecutivo.record.ConsecutivoReservadoDb;
import java.sql.Timestamp;


import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;

/**
 * Única puerta de entrada a las rutinas {@code configurations.pkg_consecutivo_*} desde Java.
 *
 * <h2>Qué hace y qué no</h2>
 * <p>
 * Invoca las rutinas y traduce su resultado a un objeto. <b>No contiene ninguna
 * regla de negocio</b>: la regla de la serie —definición, vigencia, idempotencia,
 * contador, techo y formato— vive completa y únicamente en la base. Esta clase
 * no la conoce ni la duplica; si la duplicara, el número de un documento
 * dependería de por dónde se entró a pedirlo.
 * </p>
 *
 * <h2>Por qué {@link StoredProcedureQuery} y no una consulta nativa</h2>
 * <p>
 * La emisión escribe el contador y la emisión, y devuelve varios valores: va como
 * procedimiento con parámetros de salida. PostgreSQL no tiene paquetes; el antiguo
 * paquete de consecutivos se expresa como rutinas con el prefijo
 * {@code pkg_consecutivo_} en el esquema {@code configurations}. El saneo del
 * barrido es una función de lectura y escritura sin parámetros de salida y va por
 * consulta.
 * </p>
 *
 * <h2>Transacción</h2>
 * <p>
 * Corre en la transacción de quien llama y no abre ninguna propia. Eso es el
 * objetivo entero del cambio: si la operación de negocio se revierte, el número
 * no se consume y la serie no queda con un hueco.
 * </p>
 *
 * <h2>En la libreria</h2>
 * <p>
 * Vive en {@code SimappeShared} (HT_FAT_003 V2, 2-oct-2026): la usa
 * {@code simappe-configurations} y cualquier modulo autorizado. La registra
 * {@code ConsecutivoDbAutoConfiguration} en el servicio con JPA que la active; no depende
 * de nada del dominio de ningun modulo.
 * </p>
 */
public class ConsecutivoDbGateway {

    /** Nombre calificado del procedimiento de emisión. */
    private static final String PROC_EMITIR = "configurations.pkg_consecutivo_p_emitir";

    /** Nombre calificado del procedimiento de reserva. */
    private static final String PROC_RESERVAR = "configurations.pkg_consecutivo_p_reservar";

    /** Nombre calificado del procedimiento de confirmación. */
    private static final String PROC_CONFIRMAR = "configurations.pkg_consecutivo_p_confirmar";

    /** Nombre calificado del procedimiento de anulación. */
    private static final String PROC_ANULAR = "configurations.pkg_consecutivo_p_anular";

    // Nombres de los parametros del paquete: una sola vez cada uno.
    private static final String P_CODIGO = "p_codigo";
    private static final String P_CLIENT_ID = "p_client_id";
    private static final String P_COMPANY_ID = "p_company_id";
    private static final String P_SUBSIDIARY_ID = "p_subsidiary_id";
    private static final String P_CLAVE_IDEMPOTENCIA = "p_clave_idempotencia";
    private static final String P_CONTEXTO = "p_contexto";
    private static final String P_USUARIO = "p_usuario";
    private static final String P_VALOR_NUMERICO = "p_valor_numerico";
    private static final String P_VALOR_FORMATEADO = "p_valor_formateado";
    private static final String P_PERIODO = "p_periodo";
    private static final String P_TTL_MINUTOS = "p_ttl_minutos";
    private static final String P_TOKEN = "p_token";
    private static final String P_EXPIRA_EN = "p_expira_en";

    /** Consulta que invoca la función de saneo del barrido. */
    private static final String SQL_LIBERAR =
            "SELECT configurations.pkg_consecutivo_f_liberar_vencidas(?, ?, ?)";

    /** Código de la regla (equivale al SQLSTATE {@code CN0xx} de la rutina): la definición no existe para el tenant. */
    public static final int ERR_NO_ENCONTRADO = -20001;

    /** Código de la regla (equivale al SQLSTATE {@code CN0xx} de la rutina): la definición no es de modo UNICIDAD. */
    public static final int ERR_MODO = -20002;

    /** Código de la regla (equivale al SQLSTATE {@code CN0xx} de la rutina): la serie está suspendida. */
    public static final int ERR_SUSPENDIDO = -20003;

    /** Código de la regla (equivale al SQLSTATE {@code CN0xx} de la rutina): la serie llegó a su número final. */
    public static final int ERR_AGOTADO = -20004;

    /** Código de la regla (equivale al SQLSTATE {@code CN0xx} de la rutina): la vigencia de la serie no cubre hoy. */
    public static final int ERR_EXPIRADO = -20005;

    /** Código de la regla (equivale al SQLSTATE {@code CN0xx} de la rutina): la reserva no existe en el tenant. */
    public static final int ERR_RESERVA_NO_HAY = -20006;

    /** Código de la regla (equivale al SQLSTATE {@code CN0xx} de la rutina): la reserva no está en estado RESERVADA. */
    public static final int ERR_RESERVA_ESTADO = -20007;

    /** Código de la regla (equivale al SQLSTATE {@code CN0xx} de la rutina): la reserva venció antes de confirmarse. */
    public static final int ERR_RESERVA_VENCIDA = -20008;

    /**
     * SQLSTATE con que las rutinas de PostgreSQL señalan cada regla: {@code CN0} seguido de los dos
     * ultimos digitos del codigo de la regla ({@code -20001} es {@code CN001}). PostgreSQL no tiene
     * codigos numericos de error de aplicacion; el que traduce la excepcion lee el SQLSTATE.
     */
    public static final String SQLSTATE_PREFIJO = "CN0";

    /**
     * Codigo de la regla que corresponde a un SQLSTATE de las rutinas.
     *
     * @param sqlState SQLSTATE de la excepcion; puede ser nulo
     * @return el codigo de la regla ({@code -20001} a {@code -20008}), o 0 si no es de las rutinas
     */
    public static int codigoRegla(String sqlState) {
        if (sqlState == null || sqlState.length() != 5 || !sqlState.startsWith(SQLSTATE_PREFIJO)) {
            return 0;
        }
        try {
            return -20000 - Integer.parseInt(sqlState.substring(3));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Pide a la base el siguiente número de la serie.
     *
     * @param codigo            código de la serie
     * @param clientId          cliente del tenant
     * @param companyId         compañía del tenant
     * @param subsidiaryId      sucursal del tenant, admite nulo
     * @param claveIdempotencia clave con la que un reintento recupera el mismo
     *                          número en vez de consumir otro; admite nulo
     * @param contexto          referencia libre de la operación que lo pide
     * @param usuario           usuario al que se atribuye la emisión
     * @return el número emitido, su forma con formato y el periodo
     */
    public ConsecutivoEmitidoDb emitir(String codigo, Long clientId, Long companyId,
            Long subsidiaryId, String claveIdempotencia, String contexto, String usuario) {

        StoredProcedureQuery q = entityManager.createStoredProcedureQuery(PROC_EMITIR);

        q.registerStoredProcedureParameter(P_CODIGO, String.class, ParameterMode.IN);
        q.registerStoredProcedureParameter(P_CLIENT_ID, Long.class, ParameterMode.IN);
        q.registerStoredProcedureParameter(P_COMPANY_ID, Long.class, ParameterMode.IN);
        q.registerStoredProcedureParameter(P_SUBSIDIARY_ID, Long.class, ParameterMode.IN);
        q.registerStoredProcedureParameter(P_CLAVE_IDEMPOTENCIA, String.class, ParameterMode.IN);
        q.registerStoredProcedureParameter(P_CONTEXTO, String.class, ParameterMode.IN);
        q.registerStoredProcedureParameter(P_USUARIO, String.class, ParameterMode.IN);
        q.registerStoredProcedureParameter(P_VALOR_NUMERICO, Long.class, ParameterMode.OUT);
        q.registerStoredProcedureParameter(P_VALOR_FORMATEADO, String.class, ParameterMode.OUT);
        q.registerStoredProcedureParameter(P_PERIODO, String.class, ParameterMode.OUT);

        q.setParameter(P_CODIGO, codigo);
        q.setParameter(P_CLIENT_ID, clientId);
        q.setParameter(P_COMPANY_ID, companyId);
        q.setParameter(P_SUBSIDIARY_ID, subsidiaryId);
        q.setParameter(P_CLAVE_IDEMPOTENCIA, claveIdempotencia);
        q.setParameter(P_CONTEXTO, contexto);
        q.setParameter(P_USUARIO, usuario);

        q.execute();

        return new ConsecutivoEmitidoDb(
                (Long) q.getOutputParameterValue(P_VALOR_NUMERICO),
                (String) q.getOutputParameterValue(P_VALOR_FORMATEADO),
                (String) q.getOutputParameterValue(P_PERIODO));
    }

    /**
     * Toma el siguiente número de una serie ESTRICTA y lo retiene bajo un token.
     *
     * @param codigo         código de la serie
     * @param clientId       cliente del tenant
     * @param companyId      compañía del tenant
     * @param subsidiaryId   sucursal del tenant, admite nulo
     * @param contexto       referencia libre de la operación que lo pide
     * @param usuario        usuario al que se atribuye la reserva
     * @param ttlMinutos     minutos de vigencia de la reserva
     * @return la reserva con su token y su vencimiento
     */
    public ConsecutivoReservadoDb reservar(String codigo, Long clientId, Long companyId,
            Long subsidiaryId, String contexto, String usuario, long ttlMinutos) {

        StoredProcedureQuery q = entityManager.createStoredProcedureQuery(PROC_RESERVAR);

        q.registerStoredProcedureParameter(P_CODIGO, String.class, ParameterMode.IN);
        q.registerStoredProcedureParameter(P_CLIENT_ID, Long.class, ParameterMode.IN);
        q.registerStoredProcedureParameter(P_COMPANY_ID, Long.class, ParameterMode.IN);
        q.registerStoredProcedureParameter(P_SUBSIDIARY_ID, Long.class, ParameterMode.IN);
        q.registerStoredProcedureParameter(P_CONTEXTO, String.class, ParameterMode.IN);
        q.registerStoredProcedureParameter(P_USUARIO, String.class, ParameterMode.IN);
        q.registerStoredProcedureParameter(P_TTL_MINUTOS, Long.class, ParameterMode.IN);
        q.registerStoredProcedureParameter(P_TOKEN, String.class, ParameterMode.OUT);
        q.registerStoredProcedureParameter(P_VALOR_NUMERICO, Long.class, ParameterMode.OUT);
        q.registerStoredProcedureParameter(P_VALOR_FORMATEADO, String.class, ParameterMode.OUT);
        q.registerStoredProcedureParameter(P_PERIODO, String.class, ParameterMode.OUT);
        q.registerStoredProcedureParameter(P_EXPIRA_EN, Timestamp.class, ParameterMode.OUT);

        q.setParameter(P_CODIGO, codigo);
        q.setParameter(P_CLIENT_ID, clientId);
        q.setParameter(P_COMPANY_ID, companyId);
        q.setParameter(P_SUBSIDIARY_ID, subsidiaryId);
        q.setParameter(P_CONTEXTO, contexto);
        q.setParameter(P_USUARIO, usuario);
        q.setParameter(P_TTL_MINUTOS, ttlMinutos);

        q.execute();

        Timestamp expira = (Timestamp) q.getOutputParameterValue(P_EXPIRA_EN);
        return new ConsecutivoReservadoDb(
                (String) q.getOutputParameterValue(P_TOKEN),
                (Long) q.getOutputParameterValue(P_VALOR_NUMERICO),
                (String) q.getOutputParameterValue(P_VALOR_FORMATEADO),
                (String) q.getOutputParameterValue(P_PERIODO),
                expira == null ? null : expira.toLocalDateTime());
    }

    /**
     * Cierra una reserva y deja registrada la emisión definitiva.
     *
     * @param token        token de la reserva
     * @param clientId     cliente del tenant
     * @param companyId    compañía del tenant
     * @param subsidiaryId sucursal del tenant, admite nulo
     * @param usuario      usuario al que se atribuye la confirmación
     */
    public void confirmar(String token, Long clientId, Long companyId, Long subsidiaryId,
            String usuario) {
        ejecutarSobreReserva(PROC_CONFIRMAR, token, clientId, companyId, subsidiaryId, usuario);
    }

    /**
     * Descarta una reserva que el consumidor canceló.
     *
     * @param token        token de la reserva
     * @param clientId     cliente del tenant
     * @param companyId    compañía del tenant
     * @param subsidiaryId sucursal del tenant, admite nulo
     * @param usuario      usuario al que se atribuye la anulación
     */
    public void anular(String token, Long clientId, Long companyId, Long subsidiaryId,
            String usuario) {
        ejecutarSobreReserva(PROC_ANULAR, token, clientId, companyId, subsidiaryId, usuario);
    }

    /**
     * Marca EXPIRADA toda reserva del tenant cuyo vencimiento ya pasó.
     *
     * <p>
     * Va por consulta y no por procedimiento porque el saneo es una función: no
     * tiene parámetros de salida y devuelve cuántas liberó.
     * </p>
     *
     * @param clientId     cliente del tenant
     * @param companyId    compañía del tenant
     * @param subsidiaryId sucursal del tenant, admite nulo
     * @return cuántas reservas se liberaron
     */
    public int liberarVencidas(Long clientId, Long companyId, Long subsidiaryId) {
        Object n = entityManager.createNativeQuery(SQL_LIBERAR)
                .setParameter(1, clientId)
                .setParameter(2, companyId)
                .setParameter(3, subsidiaryId)
                .getSingleResult();
        return n == null ? 0 : ((Number) n).intValue();
    }

    /**
     * Invoca un procedimiento que solo actúa sobre una reserva ya existente.
     *
     * @param procedimiento nombre calificado del procedimiento
     * @param token         token de la reserva
     * @param clientId      cliente del tenant
     * @param companyId     compañía del tenant
     * @param subsidiaryId  sucursal del tenant, admite nulo
     * @param usuario       usuario al que se atribuye la operación
     */
    private void ejecutarSobreReserva(String procedimiento, String token, Long clientId,
            Long companyId, Long subsidiaryId, String usuario) {

        StoredProcedureQuery q = entityManager.createStoredProcedureQuery(procedimiento);

        q.registerStoredProcedureParameter(P_TOKEN, String.class, ParameterMode.IN);
        q.registerStoredProcedureParameter(P_CLIENT_ID, Long.class, ParameterMode.IN);
        q.registerStoredProcedureParameter(P_COMPANY_ID, Long.class, ParameterMode.IN);
        q.registerStoredProcedureParameter(P_SUBSIDIARY_ID, Long.class, ParameterMode.IN);
        q.registerStoredProcedureParameter(P_USUARIO, String.class, ParameterMode.IN);

        q.setParameter(P_TOKEN, token);
        q.setParameter(P_CLIENT_ID, clientId);
        q.setParameter(P_COMPANY_ID, companyId);
        q.setParameter(P_SUBSIDIARY_ID, subsidiaryId);
        q.setParameter(P_USUARIO, usuario);

        q.execute();
    }
}
