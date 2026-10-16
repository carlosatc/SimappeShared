package com.catcsoft.simappe.shared.client.configurations;

import java.util.List;

import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import com.catcsoft.simappe.commons.api.v1.core.response.SuccessResponse;
import com.catcsoft.simappe.model.configurations.usuario.dto.UsuarioDto;

/**
 * Cliente HTTP <b>bloqueante</b> del maestro de usuarios de Simappe (simappe-configurations).
 *
 * <p>Publica la operacion que ya existe en el recurso -el listado del tenant- para que un modulo
 * pueda resolver el CORREO de una persona a partir de su <b>username</b>, que es el eje por el que
 * el negocio la referencia. Ninguna operacion se inventa: la que se declara existe en el recurso
 * al que apunta.</p>
 *
 * <p>El token se pasa explicitamente en cada llamada ({@code Authorization: Bearer <jwt>}); el
 * tenant se resuelve del token en masters. El bean lo provee
 * {@code SharedClientsAutoConfiguration} cuando {@code simappe.shared.clients.enabled=true}.</p>
 *
 * @author Carlos Torres
 * @since 1.0
 */
@HttpExchange(url = "/simappe-configurations/api/v1/usuario", accept = "application/json",
        contentType = "application/json")
public interface UsuarioSimappeBlockingClient {

    /**
     * {@code GET /read}; los usuarios del tenant, con su username y su correo.
     *
     * @param authorization token de la sesion, con su prefijo Bearer.
     * @return los usuarios de la empresa en sesion.
     */
    @GetExchange("/read")
    SuccessResponse<List<UsuarioDto>> read(@RequestHeader("Authorization") String authorization);
}
