package com.catcsoft.simappe.shared.client.configurations;

import java.util.List;

import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import com.catcsoft.simappe.commons.api.v1.core.response.SuccessResponse;
import com.catcsoft.simappe.model.configurations.usuario.dto.UsuarioDto;

import reactor.core.publisher.Mono;

/**
 * Cliente HTTP <b>reactivo</b> del maestro de usuarios de Simappe (simappe-configurations).
 *
 * <p>Declara la MISMA operacion que su par bloqueante. El modulo compartido publica siempre el
 * par: definir uno solo dejaria a la mitad de los consumidores sin via.</p>
 *
 * @author Carlos Torres
 * @since 1.0
 */
@HttpExchange(url = "/simappe-configurations/api/v1/usuario", accept = "application/json",
        contentType = "application/json")
public interface UsuarioSimappeReactiveClient {

    /**
     * {@code GET /read}; los usuarios del tenant, con su username y su correo.
     *
     * @param authorization token de la sesion, con su prefijo Bearer.
     * @return los usuarios de la empresa en sesion.
     */
    @GetExchange("/read")
    Mono<SuccessResponse<List<UsuarioDto>>> read(@RequestHeader("Authorization") String authorization);
}
