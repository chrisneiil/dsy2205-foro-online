# Arquetipo backend Spring Boot

Plantilla de microservicio con separación por responsabilidades: controller, service, repository, model y configuración. Los microservicios `users-service` y `forum-service` siguen esta organización.

## Generar un microservicio

Desde esta carpeta, instala el arquetipo en el repositorio Maven local:

```powershell
mvn install
```

Luego genera un proyecto nuevo:

```powershell
mvn archetype:generate `
  -DarchetypeGroupId=cl.duoc.dsy2205 `
  -DarchetypeArtifactId=foro-springboot-archetype `
  -DarchetypeVersion=1.0.0 `
  -DgroupId=cl.duoc.dsy2205 `
  -DartifactId=nombre-del-servicio `
  -Dpackage=cl.duoc.dsy2205.nombre `
  -DinteractiveMode=false
```

La plantilla entrega una aplicación Spring Boot 3, Java 17, endpoints REST, persistencia con Spring Data JPA y el driver Oracle. Los datos de conexión se inyectan por variables de entorno.
