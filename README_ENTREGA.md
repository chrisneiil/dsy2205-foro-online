# Arquitectura y alcance

La entrega integra dos servicios independientes que comparten el esquema Oracle del foro.

## Servicios

**Usuarios (`users-service`)**

- Registro e inicio de sesión con JWT.
- Consulta, modificación y eliminación de cuentas.
- Autorización basada en los roles `USER` y `ADMIN`.
- Reporte de cuentas por rol y estado.

**Foro (`forum-service`)**

- Consulta de categorías.
- Creación, consulta, modificación y eliminación de temas y comentarios.
- Búsqueda de temas por texto, categoría y autor.
- Reporte de actividad por categoría.
- Moderación administrativa de temas y comentarios.

## Modelo Oracle

- `FORUM_USERS` se relaciona con `FORUM_TOPICS` y `FORUM_COMMENTS`.
- `FORUM_CATEGORIES` se relaciona con `FORUM_TOPICS`.
- `FORUM_TOPICS` se relaciona con `FORUM_COMMENTS`.

El esquema define las tablas y restricciones en `database/01_schema.sql`. `database/02_seed.sql` crea cinco categorías. `database/03_verification.sql` consulta las filas y resume los conteos.

## Evidencias

- `postman/Foro_Online.postman_collection.json` recorre las operaciones principales y los permisos.
- `docs/guion_kaltura.md` organiza una demostración de 7 a 10 minutos.
- El esquema y los datos se pueden comprobar en SQL Developer.
