# Guion breve para presentar Nexo

**Duración objetivo: 7–8 minutos.** Antes de grabar, inicia Docker, abre `http://localhost`, prepara Postman y deja abierta la conexión de Oracle XE en SQL Developer. Usa cuentas y publicaciones de prueba.

| Tiempo | Qué mostrar | Qué decir |
|---|---|---|
| 0:00–0:30 | Portada de Nexo y página inicial. | “Presento Nexo, un foro en línea desarrollado para la Experiencia 2. La aplicación permite conversar por categorías y administra los contenidos por roles.” |
| 0:30–1:20 | Categorías; cambia el ancho de la ventana para mostrar escritorio, tablet y móvil. | “El inicio carga las categorías y conversaciones desde los servicios. La interfaz se adapta a distintos tamaños de pantalla.” |
| 1:20–2:30 | Busca un tema, cambia entre recientes y populares, filtra una categoría, abre una conversación y agrega una respuesta. | “Los usuarios pueden explorar, buscar y responder conversaciones. Los cambios se guardan en la base de datos.” |
| 2:30–3:25 | Muestra una validación del registro. Inicia sesión como `usuario_demo_s5@correo.cl` y abre el perfil. | “El registro valida los datos y la contraseña. Al iniciar sesión, cada persona puede consultar y actualizar su perfil.” |
| 3:25–4:35 | Cierra sesión; entra como ADMIN. Abre Administración, oculta una publicación de prueba y pulsa **Mostrar** para restaurarla. | “La aplicación separa los permisos de usuario y administrador. El panel permite ocultar contenido y volver a mostrarlo.” |
| 4:35–6:10 | En Postman, muestra una consulta de categorías, el inicio de sesión, una operación del foro y una acción administrativa. | “El sistema está dividido en tres microservicios: usuarios, foro y moderación. Postman permite revisar sus operaciones y el control de acceso.” |
| 6:10–7:05 | Muestra los contenedores Docker y las tablas o registros en SQL Developer. | “Docker ejecuta el frontend y los tres servicios. En esta demostración se conectan a Oracle XE local.” |
| 7:05–7:40 | Muestra brevemente el estado de Oracle Cloud y el repositorio GitHub; termina en la aplicación. | “La base Oracle Cloud no pudo aprovisionarse porque la región informó falta de capacidad. Por eso presento la integración local y no una URL cloud funcional. Este es el repositorio del proyecto.” |

## Antes de grabar

- Comprueba que el sitio abre en `http://localhost` y que los cuatro contenedores están activos.
- Ten preparadas las contraseñas localmente; no muestres claves ni tokens en pantalla.
- Usa una publicación de prueba para ocultar y restaurar durante la demostración.
- No presentes recuperación de contraseña como funcional: requiere configurar el envío de correo.
- No digas que la app está desplegada en AWS u Oracle Cloud; el despliegue mostrado es local.
