package com.catcsoft.simappe.shared.client.configurations;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

import com.catcsoft.simappe.commons.api.v1.core.query.PageResponse;
import com.catcsoft.simappe.commons.api.v1.core.query.SimappeRequestQuery;
import com.catcsoft.simappe.commons.api.v1.core.response.SuccessResponse;
import com.catcsoft.simappe.model.configurations.maestrogenerico.record.TipoMaestroResponse;

/**
 * Cliente HTTP <b>bloqueante</b> de los tipos de maestro generico de Simappe
 * (simappe-configurations).
 *
 * <p>Publica la operacion que ya existe en el recurso -el listado paginado- para que un modulo
 * pueda verificar que un tipo de maestro existe y esta ACTIVO a partir de su <b>codigo</b>, y
 * resolver su descripcion. Quien llama arma la consulta con los criterios {@code codigo} y
 * {@code estadoTipoMaestro}. Ninguna operacion se inventa: la que se declara existe en el recurso
 * al que apunta.</p>
 *
 * <p>El token se pasa explicitamente en cada llamada ({@code Authorization: Bearer <jwt>}); el
 * tenant se resuelve del token en configuraciones. El bean lo provee
 * {@code SharedClientsAutoConfiguration} cuando {@code simappe.shared.clients.enabled=true}.</p>
 *
 * @author Carlos Torres
 * @since 2.6.0
 */
@HttpExchange(url = "/simappe-configurations/api/v1/tipos-maestro", accept = "application/json",
        contentType = "application/json")
public interface TipoMaestroBlockingClient {

    /**
     * {@code POST /page-response}; los tipos de maestro del tenant que cumplen la consulta.
     *
     * @param authorization token de la sesion, con su prefijo Bearer.
     * @param query criterios, orden y paginacion; por ejemplo {@code codigo} y
     *              {@code estadoTipoMaestro = ACTIVO}.
     * @return la pagina de tipos de maestro.
     */
    @PostExchange("/page-response")
    SuccessResponse<PageResponse<TipoMaestroResponse>> pageResponse(
            @RequestHeader("Authorization") String authorization, @RequestBody SimappeRequestQuery query);
}
