# Ejecución y demostración

## Preparar Oracle

1. Usa Oracle XE para la ejecución local o configura una Autonomous Database para la entrega en nube.
2. Ejecuta `database/01_schema.sql` y `database/02_seed.sql` una vez con el usuario del esquema.
3. Para Autonomous Database, guarda el Wallet fuera de Git y conserva el alias de servicio de `tnsnames.ora`.
4. Copia `local-oracle.properties.example` a `local-oracle.properties` y añade la URL, usuario, contraseña y secreto JWT. Nunca incluyas ese archivo en la entrega.

## Iniciar localmente

Abre cuatro terminales en IntelliJ o PowerShell y ejecuta:

```powershell
cd users-service
mvn spring-boot:run
```

```powershell
cd forum-service
mvn spring-boot:run
```

```powershell
cd moderation-service
mvn spring-boot:run
```

```powershell
cd frontend
npm ci
npm start
```

Angular estará disponible en `http://localhost:4200`. El proxy de desarrollo reenvía `/users/api`, `/forum/api` y `/admin/api` a los tres servicios en los puertos 8081, 8082 y 8083. El panel de administración requiere iniciar sesión con una cuenta que tenga el rol `ADMIN` en Oracle.

## Ejecutar con Docker

1. Copia `.env.example` a `.env` y configura las credenciales de Oracle y un secreto JWT propio.
2. Para Autonomous Database, coloca el Wallet en `wallet/` y configura `DB_URL` con el alias de servicio y `TNS_ADMIN=/app/wallet`.
3. Desde la raíz ejecuta `docker compose up --build`.
4. Abre `http://localhost`. Las APIs también quedan disponibles en 8081, 8082 y 8083.

## Recorrido de Postman

Importa `postman/Foro_Online.postman_collection.json` y el entorno que corresponda: `Foro_Online_Docker.postman_environment.json` para Docker local o `Foro_Online_AWS.postman_environment.json` para AWS. En el entorno AWS reemplaza `replace-with-ec2-ip` por la IPv4 pública vigente. Configura `password` y `adminPassword` localmente como variables secretas. Prueba categorías, registro, inicio de sesión, perfil, creación/lectura de temas, comentarios, permisos por rol, reportes y moderación. Usa cuentas de prueba propias y deja las operaciones de eliminación para el final.

## Datos de acceso

La colección Postman no incluye contraseñas. Configura `password` y `adminPassword` como variables secretas locales del entorno. Crea una cuenta desde el formulario; para probar administración, usa una cuenta cuya fila tenga `USER_ROLE = 'ADMIN'`. Si actualizas el rol en Oracle, inicia sesión nuevamente para recibir un JWT con el rol actualizado.

La pantalla de recuperación muestra que no hay un proveedor de correo configurado y solicita contactar al administrador. No confirma ni envía correos.

## Video

En 7–10 minutos muestra el propósito del foro, tamaños responsive, registro/inicio de sesión, perfil, publicación, respuestas, rol `USER`, panel `ADMIN`, Postman, tablas Oracle y contenedores. Si el despliegue de AWS/Oracle Cloud no está disponible durante la grabación, explica la configuración preparada y no presentes una URL no desplegada como funcional. Oculta credenciales, tokens y Wallet.
