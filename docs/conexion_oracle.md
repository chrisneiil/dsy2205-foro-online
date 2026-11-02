# Configurar la conexión Oracle

Los servicios leen las mismas propiedades locales desde `../local-oracle.properties`. Desde cada carpeta de servicio, esa ruta apunta a la raíz del proyecto. El archivo real queda ignorado por Git.

## Oracle Autonomous Database con Wallet

1. Descarga el Wallet de cliente de la instancia y descomprímelo en la carpeta `wallet` de la raíz del proyecto. No lo agregues al repositorio.
2. Abre `wallet/tnsnames.ora` y elige uno de los alias de servicio disponibles, por ejemplo el servicio `low`, `medium` o `high` de tu instancia.
3. Copia `local-oracle.properties.example` como `local-oracle.properties` en la raíz del proyecto.
4. Reemplaza el alias JDBC por el servicio de tu `tnsnames.ora`, y completa usuario y contraseña del esquema Oracle. La URL sigue el formato `jdbc:oracle:thin:@<alias>?TNS_ADMIN=<ruta-al-wallet>` publicado por [Oracle](https://docs.oracle.com/en/database/oracle/oracle-database/26/rdfrm/using-wallet-based-mtls-connection.html).
5. Cambia `JWT_SECRET` por una cadena aleatoria propia de al menos 32 bytes, idéntica en ambos servicios.

## Oracle local

Si usas una instancia Oracle local sin Wallet, deja el URL de ejemplo que entrega tu entorno, por ejemplo `jdbc:oracle:thin:@localhost:1521/FREEPDB1`, y completa el usuario/clave del esquema.

## Crear esquema y levantar los servicios

Conéctate desde SQL Developer como el usuario dueño de las tablas y ejecuta, en este orden:

1. `database/01_schema.sql`
2. `database/02_seed.sql`

Luego, en dos terminales, desde la raíz del proyecto:

```powershell
cd users-service
mvn spring-boot:run
```

```powershell
cd forum-service
mvn spring-boot:run
```

Comprueba `http://localhost:8081/actuator/health` y `http://localhost:8082/actuator/health`. Después importa la colección Postman, registra los datos de muestra y verifica su persistencia con `database/03_verification.sql`.

Si la conexión falla, copia el mensaje de error sin incluir la contraseña ni el contenido del Wallet; normalmente indicará si falla el alias, la ruta `TNS_ADMIN`, el usuario o el acceso de red.
