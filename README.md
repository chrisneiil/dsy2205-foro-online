# Nexo Foro en línea

Aplicación Full Stack para la evaluación sumativa DSY2205, Experiencia 2. Continúa el foro de la Experiencia 1 con un frontend Angular y tres microservicios Spring Boot conectados a la misma base Oracle.

## Componentes

| Componente | Responsabilidad | Puerto local |
|---|---|---:|
| `frontend` | Interfaz Angular adaptable; consume autenticación y foro mediante HTTP | 4200 o 80 con Docker |
| `users-service` | Registro, autenticación JWT, perfiles y administración de usuarios | 8081 |
| `forum-service` | Categorías, temas, comentarios, búsquedas y reportes | 8082 |
| `moderation-service` | Administración de temas y comentarios; requiere rol `ADMIN` | 8083 |
| `database` | Esquema Oracle, categorías iniciales y consultas de verificación | — |
| `postman` | Colección y entorno para recorrer las APIs | — |

## Estructura y responsabilidades

El backend sigue una arquitectura por capas. Los controladores reciben y validan las solicitudes HTTP; los servicios contienen las reglas de negocio; los repositorios acceden a Oracle; las entidades representan las tablas; y los DTO definen el contrato público de la API. La autorización JWT se mantiene en la capa de seguridad.

El frontend separa páginas, modelos, clientes HTTP y estado. `UsersApi`, `ForumApi` y el cliente de moderación llaman a sus servicios; los stores adaptan las respuestas del backend al modelo de pantalla; y las páginas muestran el estado y las validaciones. El token se conserva en el navegador y un interceptor lo agrega a las operaciones protegidas.

## Funcionalidades

- Consultar las cinco categorías y conversaciones guardadas en Oracle; buscar y filtrar por categoría.
- Registrar usuarios, iniciar sesión con correo o nombre de usuario y modificar el perfil.
- Crear temas y respuestas persistentes.
- Distinguir `USER` y `ADMIN` a partir del token emitido por `users-service`.
- Mostrar a `ADMIN` usuarios, temas y comentarios, y guardar acciones de moderación en Oracle mediante un servicio independiente.
- Validar formularios y contraseña: entre 10 y 64 caracteres, mayúscula, minúscula, número y símbolo.
- Adaptar la interfaz a móvil, tablet y escritorio mediante Bootstrap y estilos propios.

La pantalla de recuperación de acceso informa que el restablecimiento automático no está habilitado; requiere configurar un canal de correo y un flujo seguro de recuperación.

## Ejecutar localmente

Se requiere JDK 17, Node.js 22.12 o superior, Oracle disponible y el archivo local de configuración. Ejecuta los scripts `database/01_schema.sql` y `database/02_seed.sql` una vez.

1. Copia `local-oracle.properties.example` a `local-oracle.properties` y configura `DB_URL`, `DB_USERNAME`, `DB_PASSWORD` y `JWT_SECRET`. No compartas ese archivo.
2. En una terminal, desde `users-service`, ejecuta `mvn spring-boot:run`.
3. En otra terminal, desde `forum-service`, ejecuta `mvn spring-boot:run`.
4. En otra terminal, desde `moderation-service`, ejecuta `mvn spring-boot:run`.
5. Desde `frontend`, instala dependencias y arranca Angular:

```powershell
npm ci
npm start
```

Abre `http://localhost:4200`. El proxy local conecta `/users/api`, `/forum/api` y `/admin/api` con los tres servicios. Para iniciar sesión, usa una cuenta existente en la base de datos o crea una desde el formulario de registro. La cuenta de administrador debe tener el rol `ADMIN` en Oracle.

## Ejecutar con Docker

1. Copia `.env.example` a `.env` y define `DB_URL`, `DB_USERNAME`, `DB_PASSWORD` y un `JWT_SECRET` propio de al menos 32 bytes.
2. Si usas Autonomous Database, coloca el Wallet en la carpeta local ignorada `wallet/` y configura la URL Oracle para usarlo.
3. Desde la raíz del proyecto ejecuta:

```powershell
docker compose up --build
```

El frontend estará en `http://localhost`; los servicios también quedan publicados en `http://localhost:8081`, `http://localhost:8082` y `http://localhost:8083`. Para enviar solicitudes por Nginx, usa `/users/api`, `/forum/api` y `/admin/api`. El Wallet y `.env` se excluyen de Git y del paquete de entrega.

## Entrega

El ZIP incluye fuentes Angular y Spring Boot, scripts SQL, colección y entorno de Postman, arquetipo Maven, los README del proyecto y configuración Docker. No debe incluir `.env`, `local-oracle.properties`, Wallet ni archivos de compilación.
