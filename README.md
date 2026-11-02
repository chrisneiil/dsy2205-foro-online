# Foro en línea

Backend para la actividad sumativa DSY2205. La solución implementa dos servicios Spring Boot independientes con Java 17: gestión de usuarios y funcionalidades del foro. El administrador puede moderar temas y comentarios desde el servicio del foro, así se cubren los roles sin construir un tercer servicio.

## Alcance

- `users-service`: registro, inicio de sesión con JWT, consulta, modificación y eliminación de usuarios, y resumen de usuarios por rol.
- `forum-service`: categorías, búsqueda y CRUD de temas, comentarios, moderación administrativa e informe de actividad por categoría.
- Oracle como persistencia; `database/01_schema.sql` crea las tablas y `database/02_seed.sql` carga cinco categorías y datos de demostración.
- Colección de Postman para recorrer los casos principales.

## Requisitos

- JDK 21 o superior y Maven 3.6.3 o superior.
- Una instancia/esquema Oracle accesible por JDBC. Para Oracle Autonomous con Wallet, configura `TNS_ADMIN` y `DB_URL` según el README de cada servicio.
- No subas el Wallet, contraseñas ni secretos al repositorio.

## Orden de preparación

1. Ejecuta `database/01_schema.sql` y `database/02_seed.sql` en el esquema Oracle.
2. Configura `DB_URL`, `DB_USERNAME`, `DB_PASSWORD` y `JWT_SECRET` en el entorno. Para Autonomous Database con Wallet, usa el servicio indicado en `tnsnames.ora`, por ejemplo `jdbc:oracle:thin:@<servicio>?TNS_ADMIN=<ruta-absoluta-al-wallet>`. El secreto JWT debe tener al menos 32 caracteres y ser idéntico en ambos servicios. No subas el Wallet ni contraseñas al repositorio.
3. Inicia cada servicio en una terminal:

   ```powershell
   cd users-service
   mvn spring-boot:run
   ```

   ```powershell
   cd forum-service
   mvn spring-boot:run
   ```

4. Importa `postman/Foro_Online.postman_collection.json`. En una base recién preparada, ejecuta una vez las solicitudes de registro, login, creación de tres temas y tres comentarios; la colección incluye tres cuentas. Captura primero la verificación de los datos antes de ejecutar las solicitudes de eliminación. Para probar administración, asigna `ADMIN` a tu cuenta mediante el SQL indicado abajo y vuelve a iniciar sesión con la solicitud `12a`.

## Puertos y autenticación

| Servicio | Puerto | Base URL |
|---|---:|---|
| Usuarios | 8081 | `http://localhost:8081/api` |
| Foro | 8082 | `http://localhost:8082/api` |

El inicio de sesión devuelve un JWT. En Postman se envía como `Authorization: Bearer {{token}}`. El servicio de usuarios crea cuentas como `USER`; la asignación de `ADMIN` requiere una acción administrativa en Oracle:

```sql
UPDATE FORUM_USERS SET USER_ROLE = 'ADMIN' WHERE USERNAME = 'tu_usuario';
COMMIT;
```

Después, inicia sesión otra vez para obtener un token con el rol actualizado. No se permite elegir el rol durante el registro.

## Consultas que evidencian lógica de negocio

- Usuarios: `GET /api/users/report/roles` agrupa las cuentas por rol y estado.
- Foro: `GET /api/topics/search?term=java&categoryId=1&authorId=1` filtra temas por texto, categoría y autor.
- Foro: `GET /api/reports/categories` resume cuántos temas y comentarios hay por categoría.

## Entrega y demo

El ZIP de entrega debe incluir ambos proyectos, scripts SQL y colección Postman. Antes de subirlo, completa las variables de conexión a Oracle en tu equipo, registra las operaciones en Git, ejecuta las solicitudes de Postman y captura la verificación de datos en SQL Developer. El guion de `docs/guion_kaltura.md` organiza una presentación de 8 minutos con arranque, registro/login, CRUD, búsquedas, permisos y persistencia Oracle.

## Estado de preparación

La conexión real a Oracle, la asignación del rol administrador, el enlace GitHub y el video dependen de las credenciales y cuentas del estudiante. No se incluyen datos secretos en este repositorio.
