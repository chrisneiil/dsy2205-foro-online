# Oracle XE local

La instancia usada por ambos servicios está en `localhost:1521/XEPDB1`. El esquema es `DSY2205_FORUM_APP`.

## Preparación

1. Conéctate desde SQL Developer como `DSY2205_FORUM_APP`.
2. Ejecuta `database/01_schema.sql` y luego `database/02_seed.sql`. Estos scripts se ejecutan una vez en un esquema vacío.
3. Copia `local-oracle.properties.example` a `local-oracle.properties` en la raíz del proyecto y configura la contraseña y el secreto JWT.

Las credenciales reales permanecen en el archivo local, excluido de Git.

## Oracle Cloud

Se recreó `NEXOFORO` como Autonomous AI Database Always Free, con carga Transaction Processing y acceso seguro desde Internet para que la aplicación pueda conectarse desde AWS. La solicitud está en estado `In progress`; el último detalle visible indicaba 18% y no mostraba mensajes de error. Todavía no hay una base `Available` ni un Wallet.

Antes de configurar los microservicios con OCI, confirma en la consola que la base tenga estado `Available`. Si el aprovisionamiento falla, revisa los mensajes del Work Request y la disponibilidad de capacidad en la región; Oracle indica que la falta de capacidad puede hacer fallar un aprovisionamiento ([documentación de aprovisionamiento](https://docs.oracle.com/en/cloud/paas/autonomous-database/serverless/adbsb/autonomous-provision.html)). No cambies la cuenta a recursos pagados para resolverlo.

Cuando la base esté disponible, descarga su Wallet en `wallet/`, configura `DB_URL` con el alias de servicio del archivo `tnsnames.ora` y añade `?TNS_ADMIN=/app/wallet` a la URL JDBC. La carpeta del Wallet y las credenciales quedan fuera de Git y del ZIP.

### Preparar el esquema en Autonomous Database

1. En la página de `NEXOFORO`, descarga el Wallet de instancia y extrae sus archivos en `wallet/`. Conserva la contraseña del Wallet de forma privada.
2. Abre Database Actions como `ADMIN` y crea el usuario de la aplicación con una contraseña propia:

```sql
CREATE USER DSY2205_FORUM_APP IDENTIFIED BY "<contraseña segura>";
GRANT CREATE SESSION, CREATE TABLE TO DSY2205_FORUM_APP;
GRANT UNLIMITED TABLESPACE TO DSY2205_FORUM_APP;
```

3. En Database Actions, cambia a `DSY2205_FORUM_APP` y ejecuta `database/01_schema.sql` y luego `database/02_seed.sql`. No ejecutes el esquema como `ADMIN`.
4. Copia `.env.example` a `.env`. Toma el alias exacto de `tnsnames.ora` y configura `DB_URL` con el formato `jdbc:oracle:thin:@<alias>?TNS_ADMIN=/app/wallet`; usa el usuario de aplicación y su contraseña en `DB_USERNAME` y `DB_PASSWORD`.
5. Genera un `JWT_SECRET` propio de al menos 32 bytes. No reutilices la contraseña Oracle ni guardes secretos en Git.

Oracle documenta la creación del usuario de aplicación, los permisos y la conexión JDBC Thin con Wallet en sus guías de [administración de usuarios](https://docs.oracle.com/en/cloud/paas/autonomous-database/serverless/adbsb/manage-users-privileges.html) y [conexión JDBC con Wallet](https://docs.oracle.com/en/cloud/paas/autonomous-database/serverless/adbsb/connect-jdbc-thin-wallet.html).

## IntelliJ IDEA

Abre cada `pom.xml` como proyecto Maven, selecciona JDK 17 y espera a que finalice la importación. En las configuraciones de `UsersApplication` y `ForumApplication`, establece el directorio de trabajo en la carpeta del servicio correspondiente. Así `../local-oracle.properties` apunta a la raíz del proyecto.

Inicia ambos servicios en procesos separados. Comprueba los endpoints:

- `http://localhost:8081/actuator/health`
- `http://localhost:8082/actuator/health`
- `http://localhost:8083/actuator/health`

## Asignar rol administrador

Las cuentas se registran con rol `USER`. Para habilitar la moderación, ejecuta en SQL Developer:

```sql
UPDATE FORUM_USERS
SET USER_ROLE = 'ADMIN'
WHERE USERNAME = '&ADMIN_USERNAME';

COMMIT;
```

Al ejecutar el script, SQL Developer solicitará el nombre de la cuenta. Vuelve a iniciar sesión desde Postman para emitir un token actualizado.
