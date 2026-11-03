# Oracle XE local

La instancia usada por ambos servicios está en `localhost:1521/XEPDB1`. El esquema es `DSY2205_FORUM_APP`.

## Preparación

1. Conéctate desde SQL Developer como `DSY2205_FORUM_APP`.
2. Ejecuta `database/01_schema.sql` y luego `database/02_seed.sql`. Estos scripts se ejecutan una vez en un esquema vacío.
3. Copia `local-oracle.properties.example` a `local-oracle.properties` en la raíz del proyecto y configura la contraseña y el secreto JWT.

Las credenciales reales permanecen en el archivo local, excluido de Git.

## IntelliJ IDEA

Abre cada `pom.xml` como proyecto Maven, selecciona JDK 17 y espera a que finalice la importación. En las configuraciones de `UsersApplication` y `ForumApplication`, establece el directorio de trabajo en la carpeta del servicio correspondiente. Así `../local-oracle.properties` apunta a la raíz del proyecto.

Inicia ambos servicios en procesos separados. Comprueba los endpoints:

- `http://localhost:8081/actuator/health`
- `http://localhost:8082/actuator/health`

## Asignar rol administrador

Las cuentas se registran con rol `USER`. Para habilitar la moderación, ejecuta en SQL Developer:

```sql
UPDATE FORUM_USERS
SET USER_ROLE = 'ADMIN'
WHERE USERNAME = '&ADMIN_USERNAME';

COMMIT;
```

Al ejecutar el script, SQL Developer solicitará el nombre de la cuenta. Vuelve a iniciar sesión desde Postman para emitir un token actualizado.
