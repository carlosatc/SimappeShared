# DESVIACIONES — SimappeShared 2.5.0 (rama `feature/configurations-tasks`)

Port de los clientes HTTP compartidos que usan configuraciones y tareas, y del gateway de consecutivos.
Mapeo clase por clase en `/mnt/personal/Catcsoft/v4/MAPEO_MODELOS.md`.

## Aplicadas (sin cambio de comportamiento de negocio)

| # | Qué | Por qué | Alternativa |
|---|---|---|---|
| D-01 | Clientes portados (bloqueante y reactivo): `ControlTareas*Client` (`client.tareas`), `ExecutionToken*Client` (`client.oauth2`), cliente del maestro de usuarios → `UsuarioSimappe*Client` (`client.configurations`), `RolCliente*Client` (`client.admin`). | Son los que usan configuraciones, tareas y el marco de tareas. | Reutilizar `TenantCustomerRoleWebClient` en lugar de `RolClienteBlockingClient` (descartado: el origen es bloqueante y lo usa la precedencia de roles con `RestClient`; mismo endpoint `/simappe-admin/api/v1/tenant/customer-role/page-response`). |
| D-02 | Rutas de los servicios de origen → `/simappe-configurations/...`, `/simappe-tasks/...` (detalle en `MAPEO_MODELOS.md` §4.5). | Nombre de servicio v4. | — |
| D-03 | La autoconfiguración de clientes de origen → `SharedClientsAutoConfiguration`, solo con los clientes portados; propiedades `simappe.shared.clients.{enabled,base-url,environment,blocking.enabled,reactive.enabled}`. **Opt-in** (`enabled=true`). | No alterar a los servicios existentes; el prefijo evita chocar con `simappe.clients.v2` de SimappeCommons. | — |
| D-04 | `AvisoControlTareasCliente` y `NombreUsuarioSimappe` vienen del marco de tareas (origen en la librería transversal) y se arman en la nueva `TareasClientesAutoConfiguration` (activa con `simappe.tareas.enabled=true`), que además aporta `EmisorTokenEjecucionCliente` para `TokenEjecucionComponent`. | Evitar el ciclo SimappeCommons ↔ SimappeShared (ver DESVIACIONES de SimappeCommons D-03/D-04). | — |
| D-05 | `ConsecutivoDbGateway` en PostgreSQL: procedimientos `configurations.pkg_consecutivo_p_{emitir,reservar,confirmar,anular}` y función `configurations.pkg_consecutivo_f_liberar_vencidas` (sin `FROM dual`); se añade `SQLSTATE_PREFIJO = "CN0"` y `codigoRegla(sqlState)` para traducir el SQLSTATE `CN001..CN008` al código de regla `-20001..-20008` de siempre. | PostgreSQL no tiene paquetes ni `raise_application_error` numérico. | El agente de SimappeConfigurations debe crear las rutinas con esos nombres y `RAISE ... USING ERRCODE = 'CN00x'`. |
| D-06 | `ConsecutivoDbAutoConfiguration` pasa a opt-in (`simappe.consecutivo.db.enabled=true`) y se ordena por nombre tras `org.springframework.boot.hibernate.autoconfigure.HibernateJpaAutoConfiguration` (Boot 4 movió la clase). En origen se registraba en todo servicio con JPA. | No alterar a Vote/BI al subir de versión. | — |
| D-07 | `ConsecutivoEmitidoDb`/`ConsecutivoReservadoDb` viven en SimappeModel (`model.configurations.consecutivo.record`). | Los modelos van a SimappeModel. | — |
| D-08 | Pruebas: se portan `ExecutionTokenClientContractTest`, `ConsecutivoDbGatewayTest` (nombres de rutina PostgreSQL + prueba de `codigoRegla`), `ConsecutivoDbAutoConfigurationTest` (con la propiedad), la prueba del aviso (`AvisoControlTareasClienteTest`) y una nueva `SharedClientsAutoConfigurationTest`. | — | — |
| D-09 | Cabecera de la clave de servicio parametrizable por contexto: propiedad `simappe.security.service-key-header` (config-server; default `X-Simappe-Service-Key`, mismo nombre que leen SimappeAdmin y SimappeOauth2Server). Las interfaces `@HttpExchange` no resuelven placeholders en `@RequestHeader`, así que `ExecutionToken*Client`, `RegistrationWebClient`, `EmailNotificationWebClient` y `TenantUserIdentityWebClient` declaran `ServiceKeyHeader.DEFAULT_NAME` y el filtro (WebClient) o el interceptor (RestClient) de `security.ServiceKeyHeader` la renombra al valor configurado antes de enviar. Lo instalan `SharedClientsAutoConfiguration` y `TenantClientsAutoConfiguration`. Se eliminó el literal de cabecera del ERP de referencia de la librería (también en los clientes preexistentes) y de sus comentarios. | Decisión del dueño (15-oct-2026): el código de vps-catcsoft refleja Catcsoft y el nombre es parámetro de contexto. | Parámetro `@RequestHeader Map` (descartado: cambia las firmas que ya usan los consumidores). |
| D-10 | `TenantClientsAutoConfiguration` clona el `WebClient.Builder` del contexto antes de fijar base y filtro. | Sin clonar, el filtro y la base quedaban en el builder compartido y se acumulaban en cada cliente. | — |
| D-11 | Se retiró `TipoDocumento*Client` (y su bean y aserción de prueba). | Decisión del dueño (antes P-02): los tipos de documento de identificación son el maestro genérico `TIPO_IDENTIFICACION` de SimappeConfigurations, consultado localmente. | — |

**Despliegue:** mientras SimappeAdmin y SimappeOauth2Server no lean `simappe.security.service-key-header` con el mismo valor, los ambientes deben fijar en el config-server el nombre que hoy esperan esos servidores, o desplegar los tres juntos.

## Pendientes de autorización

Ninguno (P-01 y P-02 resueltos por el dueño: D-09 y D-11).

## Notas

- La versión depende de SimappeCommons 5.14.0 y SimappeModel 4.7.0 (antes 5.10.0 y 4.6.0).
- Nada publicado ni desplegado; `mvn install` solo local. No se tocaron versiones 1.x en `~/.m2`.
