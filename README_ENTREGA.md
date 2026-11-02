# Decisiones de alcance para la entrega

## Servicios seleccionados

1. **Usuarios:** crear, consultar, modificar y eliminar cuentas; inicio de sesión; roles `USER` y `ADMIN`.
2. **Foro:** categorías, búsqueda/visualización/publicación de temas, comentarios y moderación por administrador.

La moderación queda implementada como una capacidad administrativa del servicio del foro. Esto respeta la instrucción de construir dos de los tres servicios y mantiene explícita la diferencia de privilegios entre los dos roles.

## Modelo de datos

`FORUM_USERS` 1—N `FORUM_TOPICS`; `FORUM_CATEGORIES` 1—N `FORUM_TOPICS`; `FORUM_TOPICS` 1—N `FORUM_COMMENTS`; `FORUM_USERS` 1—N `FORUM_COMMENTS`.

Las cinco categorías superan el mínimo de cinco y se incluyen desde el script de carga. La colección Postman incorpora solicitudes para registrar tres usuarios y crear tres temas y tres comentarios; ejecútalas una vez en una base recién preparada para cumplir el mínimo de tres registros por tabla. Las solicitudes de eliminación se dejan para después de mostrar la evidencia en Oracle.

## Evidencia de evaluación

- Git: historial de commits del trabajo y enlace al repositorio.
- Oracle: script de tablas/categorías y consultas SQL que muestren inserción, modificación, consulta y eliminación.
- Postman: colección con operaciones CRUD, búsqueda por filtros, autenticación, autorización y moderación.
- Video: recorrido de 7 a 10 minutos con los servicios ejecutándose y los resultados visibles en Postman y Oracle.

## Punto que se debe confirmar con el docente

La pauta menciona dos microservicios complejos, pero la instrucción permite escoger dos de tres y no identifica cuáles consultas califican como complejas. Se implementan filtros combinables en temas y reportes agregados en ambos servicios para entregar evidencia de consultas no triviales.
