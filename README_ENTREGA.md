# Resumen de entrega DSY2205 Experiencia 2

Nexo es un foro web adaptable conectado a tres microservicios Spring Boot y una base Oracle compartida. El frontend usa respuestas reales de las APIs: las cuentas, publicaciones, comentarios y acciones administrativas se guardan en Oracle.

## Incluye

- Frontend Angular 21 con Bootstrap, navegación responsive, formularios validados, perfil y panel de administración.
- `users-service`: registro, acceso JWT, consulta/edición de perfil y operaciones administrativas de usuarios.
- `forum-service`: categorías, temas, comentarios, búsqueda y reportes.
- `moderation-service`: consulta, ocultamiento y restauración de temas y comentarios para el rol `ADMIN`.
- Scripts SQL, colección y entorno Postman, arquetipo Maven, Dockerfiles, Compose y configuración Nginx.

## Arquitectura

Los tres servicios backend se organizan por capas `controller`, `service`, `repository`, `model`, `dto` y `security`. El frontend separa `pages`, `models`, `core` (clientes HTTP y token) y `services` (estado de sesión y foro).

La recuperación automática de contraseña no se encuentra habilitada porque requiere configurar el envío seguro de correo. La pantalla lo informa y deriva al administrador.

## Configuración sensible

El archivo `local-oracle.properties`, `.env`, Wallet de Oracle, tokens y credenciales reales se configuran localmente y no se incluyen en Git ni en el ZIP.
