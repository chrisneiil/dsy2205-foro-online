# Guion para la presentación de la Experiencia 2

Duración recomendada: 8 a 9 minutos. Antes de grabar, inicia los cuatro contenedores locales, verifica Oracle XE y prepara Postman con el entorno Docker local. Si `NEXOFORO` no queda disponible, muestra la ejecución local e indica con claridad que la base Oracle Cloud permanece en aprovisionamiento; no presentes una URL pública como funcional si aún no está desplegada.

## 0:00–0:40 | Presentación

“Hola, presentaré Nexo, un sistema de foros en línea para la Experiencia 2. Continúa el backend de la Experiencia 1 con una interfaz Angular adaptable conectada a los microservicios y a Oracle.”

Muestra el nombre del proyecto y el repositorio GitHub.

## 0:40–1:30 | Diseño responsive y categorías

“La interfaz usa Angular 21 y Bootstrap con una grilla de 12 columnas. Revisaré las categorías y cómo cambia la distribución al pasar de escritorio a tablet y móvil.”

Muestra las cinco categorías leídas desde Oracle, la búsqueda y el filtro. Reduce la ventana o cambia el tamaño para demostrar las tres vistas.

## 1:30–3:00 | Conversaciones

“Un miembro puede buscar conversaciones, abrir un tema, leer respuestas, publicar un tema y responder. Las publicaciones y comentarios se guardan a través de `forum-service` en Oracle.”

Abre un tema, recorre sus respuestas y publica un tema o comentario de demostración.

## 3:00–4:30 | Formularios y perfil

“El registro valida nombre de usuario, nombre, correo y contraseña. La contraseña exige entre 10 y 64 caracteres, mayúsculas, minúsculas, número y símbolo. El inicio de sesión autentica contra `users-service`, y los cambios de perfil se guardan en Oracle.”

Muestra brevemente los avisos de validación, crea una cuenta de prueba y abre el perfil. La recuperación automática requiere configurar un servicio de correo y se informa en pantalla.

## 4:30–5:30 | Rol de administrador

“La aplicación diferencia los roles `USER` y `ADMIN`. El administrador tiene acceso al panel de comunidad, donde puede ocultar conversaciones y comentarios mediante `moderation-service`. Un usuario normal no puede acceder a esas operaciones.”

Cierra la sesión del miembro e inicia con una cuenta existente con rol `ADMIN`. Oculta un tema y un comentario, y muestra que desaparecen del foro público.

## 5:30–7:30 | APIs con Postman

“Ahora mostraré los tres microservicios Spring Boot ejecutándose por separado. El servicio de usuarios gestiona registro, acceso y perfiles; el servicio del foro gestiona categorías, temas, comentarios y reportes; y el servicio de moderación controla el contenido administrativo.”

En Postman, ejecuta solicitudes de categorías, registro/login, búsqueda, lectura, publicación y comentarios. Muestra una petición protegida que falla con un `USER` y luego la misma operación autorizada con el token `ADMIN`. Enseña un reporte y una acción de moderación.

## 7:30–8:30 | Docker, Oracle y cierre

“Docker ejecuta el frontend y los tres microservicios. Nginx canaliza las solicitudes y los servicios consultan la base Oracle XE local. La instancia de AWS está creada, pero la base de Oracle Cloud sigue en aprovisionamiento, así que esta demostración corresponde al entorno local.”

Muestra `docker compose ps`, la respuesta de categorías en Postman y las tablas en SQL Developer. Si AWS todavía no tiene la aplicación desplegada, muestra la instancia en la consola solo como avance de infraestructura y aclara que la URL pública no está habilitada. Cierra mostrando el repositorio y el ZIP de la entrega.

“Con esto presenté el diseño, la adaptación responsive, los formularios, los roles, las funcionalidades del foro, las APIs, Oracle Cloud y la ejecución en Docker. Gracias.”

## Preparación antes de grabar

- Confirma que la URL pública del frontend y los endpoints Postman respondan.
- Inicia sesión nuevamente después de cambiar el rol de una cuenta en Oracle.
- Evita repetir registros si la cuenta ya existe; usa las variables de la colección para ajustar usuarios e IDs.
- Ejecuta las solicitudes de eliminación al final y no muestres contraseñas reales, secretos JWT ni el Wallet.
