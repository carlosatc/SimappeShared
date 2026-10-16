# DESVIACIONES — SimappeShared 2.5.0 (rama `feature/configurations-tasks`)

Port de los clientes HTTP compartidos que usan configuraciones y tareas, y del gateway de consecutivos.
Mapeo clase por clase en `/mnt/personal/Catcsoft/v4/MAPEO_MODELOS.md`.

## Aplicadas (sin cambio de comportamiento de negocio)

| # | Qué | Por qué | Alternativa |
|---|---|---|---|
| D-01 | Clientes portados (bloqueante y reactivo): `ControlTareas*Client` (`client.tareas`), `ExecutionToken*Client` (`client.oauth2`), cliente del maestro de usuarios → `UsuarioSimappe*Client` (`client.configurations`), `TipoDocumento*Client` (`client.accounting`), `RolCliente*Client` (`client.admin`). | Son los que usan configuraciones, tareas y el marco de tareas. | Reutilizar `TenantCustomerRoleWebClient` en lugar de `RolClienteBlockingClient` (descartado: el origen es bloqueante y lo usa la precedencia de roles con `RestClient`; mismo endpoint `/simappe-admin/api/v1/tenant/customer-role/page-response`). |
| D-02 | Rutas de los servicios de origen → `/simappe-configurations/...`, `/simappe-tasks/...`, `/simappe-accounting-core/...` (detalle en `MAPEO_MODELOS.md` §4.5). | Nombre de servicio v4. | — |
| D-03 | La autoconfiguración de clientes de origen → `SharedClientsAutoConfiguration`, solo con los clientes portados; propiedades `simappe.shared.clients.{enabled,base-url,environment,blocking.enabled,reactive.enabled}`. **Opt-in** (`enabled=true`). | No alterar a los servicios existentes; el prefijo evita chocar con `simappe.clients.v2` de SimappeCommons. | — |
| D-04 | `AvisoControlTareasCliente` y `NombreUsuarioSimappe` vienen del marco de tareas (origen en la librería transversal) y se arman en la nueva `TareasClientesAutoConfiguration` (activa con `simappe.tareas.enabled=true`), que además aporta `EmisorTokenEjecucionCliente` para `TokenEjecucionComponent`. | Evitar el ciclo SimappeCommons ↔ SimappeShared (ver DESVIACIONES de SimappeCommons D-03/D-04). | — |
| D-05 | `ConsecutivoDbGateway` en PostgreSQL: procedimientos `configurations.pkg_consecutivo_p_{emitir,reservar,confirmar,anular}` y función `configurations.pkg_consecutivo_f_liberar_vencidas` (sin `FROM dual`); se añade `SQLSTATE_PREFIJO = "CN0"` y `codigoRegla(sqlState)` para traducir el SQLSTATE `CN001..CN008` al código de regla `-20001..-20008` de siempre. | PostgreSQL no tiene paquetes ni `raise_application_error` numérico. | El agente de SimappeConfigurations debe crear las rutinas con esos nombres y `RAISE ... USING ERRCODE = 'CN00x'`. |
| D-06 | `ConsecutivoDbAutoConfiguration` pasa a opt-in (`simappe.consecutivo.db.enabled=true`) y se ordena por nombre tras `org.springframework.boot.hibernate.autoconfigure.HibernateJpaAutoConfiguration` (Boot 4 movió la clase). En origen se registraba en todo servicio con JPA. | No alterar a Vote/BI al subir de versión. | — |
| D-07 | `ConsecutivoEmitidoDb`/`ConsecutivoReservadoDb` viven en SimappeModel (`model.configurations.consecutivo.record`). | Los modelos van a SimappeModel. | — |
| D-08 | Pruebas: se portan `ExecutionTokenClientContractTest`, `ConsecutivoDbGatewayTest` (nombres de rutina PostgreSQL + prueba de `codigoRegla`), `ConsecutivoDbAutoConfigurationTest` (con la propiedad), la prueba del aviso (`AvisoControlTareasClienteTest`) y una nueva `SharedClientsAutoConfigurationTest`. | — | — |

## Pendientes de autorización

| # | Qué | Por qué no se aplicó |
|---|---|---|
| P-01 | La cabecera de la clave de servicio de `ExecutionToken*Client` sigue siendo el literal de cabecera del servidor oauth2 v4 desplegado (el mismo que ya usa `RegistrationWebClient` en esta librería), que contiene el nombre del ERP de referencia. Renombrarla a `X-Simappe-Service-Key` exige cambiar a la vez el servidor oauth2; hacerlo solo aquí rompe la emisión del token de ejecución. Los comentarios ya no lo mencionan. | Cambio de contrato con un servicio desplegado (comportamiento). |
| P-02 | `TipoDocumento*Client` apunta a `/simappe-accounting-core/api/v1/tipo-documento`, servicio que no existe en v4. Se portó literal porque lo usa la sincronización de usuarios de configuraciones; hay que decidir qué servicio v4 sirve el maestro de tipos de documento (o si se porta a configurations). | Decidir el destino cambia el alcance funcional. |

## Notas

- La versión depende de SimappeCommons 5.14.0 y SimappeModel 4.7.0 (antes 5.10.0 y 4.6.0).
- Nada publicado ni desplegado; `mvn install` solo local. No se tocaron versiones 1.x en `~/.m2`.
