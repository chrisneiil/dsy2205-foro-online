# Foro en línea

Backend para la evaluación sumativa DSY2205. Contiene dos microservicios Spring Boot independientes y una base Oracle compartida.

## Componentes

| Componente | Funcionalidad | Puerto |
|---|---|---:|
| `users-service` | Registro, login JWT, gestión de cuentas y reportes por rol | 8081 |
| `forum-service` | Categorías, temas, comentarios, búsqueda, reportes y moderación | 8082 |
| `database` | Esquema, categorías y consultas de verificación Oracle | — |
| `postman` | Solicitudes para recorrer la API | — |

## Requisitos

- JDK 17
- Maven 3.6.3 o superior
- Oracle XE local con servicio `XEPDB1`, o una instancia Oracle accesible por JDBC

## Configurar Oracle

Conéctate a Oracle con el usuario del esquema y ejecuta una sola vez:

1. `database/01_schema.sql`
2. `database/02_seed.sql`

Copia `local-oracle.properties.example` como `local-oracle.properties` en la raíz y configura el usuario, la contraseña y un secreto JWT propio. Este archivo no se incluye en Git.

La instancia local usada durante el desarrollo tiene host `localhost`, puerto `1521`, servicio `XEPDB1` y esquema `DSY2205_FORUM_APP`.

## Ejecutar desde IntelliJ IDEA

Abre `users-service/pom.xml` y `forum-service/pom.xml` como proyectos Maven. Configura JDK 17 y usa como directorio de trabajo la carpeta de cada servicio. Ejecuta `UsersApplication` y `ForumApplication` en configuraciones separadas.

También puedes iniciarlos desde terminales ubicadas en las carpetas respectivas:

```powershell
cd users-service
mvn spring-boot:run
```

```powershell
cd forum-service
mvn spring-boot:run
```

Los indicadores de estado están disponibles en:

- `http://localhost:8081/actuator/health`
- `http://localhost:8082/actuator/health`

## Recorrer la API

Importa `postman/Foro_Online.postman_collection.json`. En una base nueva, ejecuta el registro antes del login y crea temas y comentarios antes de consultarlos o modificarlos. Los usuarios registrados reciben el rol `USER`.

Para habilitar las solicitudes administrativas, registra la cuenta que usarás y ejecuta en SQL Developer:

```sql
UPDATE FORUM_USERS
SET USER_ROLE = 'ADMIN'
WHERE USERNAME = '&ADMIN_USERNAME';

COMMIT;
```

SQL Developer solicitará el nombre de usuario. Inicia sesión de nuevo con la solicitud `12a` para emitir un token con el rol actualizado. No se permite elegir el rol durante el registro.

`database/03_verification.sql` muestra los usuarios, las categorías, los temas, los comentarios y los conteos por categoría.
