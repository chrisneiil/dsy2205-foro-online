# Arquitectura de Nexo

## Backend por capas

Cada microservicio conserva un límite claro entre transporte, reglas de negocio y persistencia:

| Capa | Paquetes | Responsabilidad |
|---|---|---|
| Presentación | `controller` | Endpoints REST, identidad autenticada y códigos HTTP |
| Aplicación | `service` | Validaciones de negocio, autorización por propiedad y coordinación de operaciones |
| Persistencia | `repository` | Consultas y operaciones JPA contra Oracle |
| Dominio | `model` | Entidades y relaciones persistidas |
| Contrato | `dto` | Solicitudes y respuestas JSON independientes de las entidades |
| Seguridad | `security` | JWT, roles, encoder de contraseñas y reglas de acceso |

`users-service` administra `FORUM_USERS` y firma los JWT. `forum-service` valida ese JWT y gestiona categorías, temas, comentarios y búsquedas. `moderation-service` valida el mismo JWT y concentra las operaciones administrativas. Los tres servicios comparten el esquema Oracle y mantienen procesos y APIs separados.

## Frontend

| Área | Ubicación | Responsabilidad |
|---|---|---|
| Presentación | `frontend/src/app/pages` | Rutas, formularios, mensajes y componentes de pantalla |
| Modelos de pantalla y API | `frontend/src/app/models` | Tipos para datos de dominio y respuestas JSON |
| Clientes | `frontend/src/app/core/api-clients.ts` | Llamadas HTTP a los tres microservicios |
| Token | `frontend/src/app/core` | Persistencia de sesión e interceptor `Authorization` |
| Estado | `frontend/src/app/services` | Carga, conversión y actualización reactiva de los datos visibles |

En desarrollo, `proxy.conf.json` elimina el prefijo `/users`, `/forum` o `/admin` antes de enviar cada solicitud al servicio local. En Docker, Nginx aplica el mismo mapeo. Así el navegador utiliza una sola ruta de origen y no necesita configuración CORS.

## Flujo de autenticación

1. El formulario envía correo o nombre de usuario y contraseña a `users-service`.
2. El servicio valida la contraseña, consulta el rol almacenado y firma un JWT.
3. Angular conserva el token y envía `Authorization: Bearer ...` a los endpoints protegidos.
4. Los tres servicios verifican la firma y vencimiento del JWT. `forum-service` aplica permisos de lectura y autoría; `moderation-service` permite moderar solo al rol `ADMIN`.

## Persistencia y autorización

Las categorías, temas, respuestas, perfiles y moderaciones se leen desde Oracle. Las listas del navegador solo reflejan las respuestas del backend; no se usan cuentas ni conversaciones ficticias como fuente de datos. La creación, edición de perfil y moderación persisten mediante sus APIs correspondientes.
