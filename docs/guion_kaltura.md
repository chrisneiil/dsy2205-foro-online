# Guion para la presentación en Kaltura

Duración sugerida: 8 minutos. Grabar con ambos servicios iniciados y Postman, VS Code y SQL Developer listos.

| Tiempo | Qué mostrar | Qué explicar |
|---|---|---|
| 0:00–0:40 | Portada y estructura de carpetas | Caso de foro en línea. Se desarrollaron usuarios y foro; la moderación de administrador se integró al servicio del foro. No hay frontend. |
| 0:40–1:25 | Paquetes de ambos servicios | Spring Boot, capas controller/service/repository/model, conexión Oracle y autenticación con token Bearer. |
| 1:25–2:10 | Aplicaciones en ejecución | Mostrar `users-service` en 8081 y `forum-service` en 8082, y `/actuator/health` para ambos. |
| 2:10–3:00 | Postman: registrar e iniciar sesión | Registrar tres cuentas; mostrar que el registro asigna `USER` sin permitir elegir privilegios; iniciar sesión y guardar token. |
| 3:00–3:40 | Categorías del foro | Consultar categorías y mostrar que hay cinco o más opciones. |
| 3:40–5:05 | Temas y comentarios | Crear tres temas y tres comentarios, consultar detalle y comentarios, actualizar un tema/comentario y buscar por texto/categoría. |
| 5:05–5:45 | Informes y persistencia | Mostrar informe de actividad por categoría y ejecutar `database/03_verification.sql` en SQL Developer para comprobar usuarios, temas y comentarios almacenados. |
| 5:45–6:35 | Git y seguridad | Mostrar el repositorio/historial. Promover la cuenta propia a `ADMIN` con el SQL documentado y volver a iniciar sesión para renovar el token. |
| 6:35–7:35 | Moderación | Con token USER, mostrar un intento rechazado de moderación; con token ADMIN, ocultar un tema o comentario y comprobar que deja de aparecer en la consulta pública. |
| 7:35–8:10 | Cierre | Resumir CRUD, roles, búsqueda, persistencia Oracle y material entregado. |

## Preparación de la grabación

1. Ejecutar el esquema y las categorías en Oracle.
2. Iniciar ambos microservicios y probar `/actuator/health`.
3. Importar `postman/Foro_Online.postman_collection.json` y ejecutar primero el registro, login, categorías, tres temas y tres comentarios.
4. Mostrar las consultas de verificación en SQL Developer antes de ejecutar solicitudes de eliminación.
5. Cambiar el rol de la cuenta de demostración en Oracle y volver a iniciar sesión; ejecutar la solicitud `12a` de la colección antes de las solicitudes ADMIN.
6. Grabar entre 7 y 10 minutos y subir el video junto con el ZIP al AVA. Compartir el enlace Git solicitado.
