# Despliegue en AWS Academy

## Estado actual

La instancia EC2 `NexoForo-S5` ya fue creada en `us-east-1` y figura como `Running`. Usa Amazon Linux 2023, `t3.small`, disco gp3 de 20 GiB y el grupo de seguridad `nexoforo-s5-sg`. La web podrá recibir tráfico HTTP por el puerto 80; SSH está restringido a la dirección IPv6 del equipo que abrió el laboratorio. Los puertos internos de los servicios no están publicados.

La máquina todavía no tiene Docker ni la aplicación desplegada. La base `NEXOFORO` en OCI sigue en 18% (`In progress` al 23 de noviembre de 2026); todavía no hay Wallet ni conexión validada con Oracle Cloud. Como alternativa de demostración, los cuatro contenedores se iniciaron localmente con Oracle XE: Nginx respondió por el puerto 80 y el foro devolvió cinco categorías y cinco temas. Esta verificación local no equivale a una integración con OCI.

## Tamaño de instancia

Para los tres servicios Spring Boot y el frontend se recomienda una instancia `t3.small` con Amazon Linux 2023 y disco EBS de 20 GB. El asistente de AWS ofrece `t3.micro` como nivel gratuito, pero tiene 1 GiB de memoria, que deja poco margen para tres servicios Java. Compose limita cada JVM a 256 MB de heap para reducir el consumo.

La consola de AWS mostró Linux `t3.small` a `$0.0208` por hora, más costos de disco, dirección IPv4 pública y transferencia. El Learner Lab mostraba un presupuesto de `$50`. Detén la instancia cuando no la uses y revisa el consumo del laboratorio. [Tarifas de instancias T3](https://aws.amazon.com/ec2/instance-types/t3/).

## Red

La aplicación quedará publicada por HTTP en el puerto `80`, detrás de Nginx. El grupo de seguridad debe permitir `TCP 80` desde `0.0.0.0/0`. Nginx se comunica con los microservicios por la red interna de Docker. Los puertos `8081`, `8082` y `8083` están vinculados a `127.0.0.1` para permitir pruebas locales en la máquina sin exponerlos a Internet.

## Preparar la instancia

En EC2, selecciona Amazon Linux 2023 de 64 bits x86, `t3.small` y disco gp3 de 20 GB. Crea un grupo de seguridad con estas reglas de entrada:

| Tipo | Puerto | Origen |
|---|---:|---|
| HTTP | 80 | `0.0.0.0/0` |
| SSH | 22 | IPv6 actual del equipo (`/128`) |

No agregues reglas de entrada para `8081`, `8082` ni `8083`. AWS recomienda restringir SSH a las IP que necesitan conectarse y permite HTTP público en el puerto 80 para un servidor web ([reglas de grupos de seguridad](https://docs.aws.amazon.com/AWSEC2/latest/UserGuide/security-group-rules-reference.html)).

Al conectar por SSH, instala Docker y habilita su servicio:

```bash
sudo dnf update -y
sudo dnf install -y docker unzip
sudo systemctl enable --now docker
sudo usermod -aG docker ec2-user
```

Desconéctate y vuelve a entrar para aplicar el grupo `docker`. Instala el complemento Compose desde las instrucciones oficiales para Linux; luego confirma con `docker compose version` ([instalación de Docker en Amazon Linux 2023](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/install-docker.html), [instalación de Compose](https://docs.docker.com/compose/install/linux/)).

## Publicar la aplicación

1. Espera que `NEXOFORO` esté `Available`; crea `DSY2205_FORUM_APP`, descarga y configura el Wallet, y ejecuta el esquema y las categorías siguiendo `conexion_oracle.md`.
2. Prepara el paquete de entrega sin secretos ni archivos compilados. Transfiérelo a la instancia por SSH/SCP. Transfiere `.env` y el directorio `wallet/` por separado mediante SCP; nunca los incluyas en Git ni en el ZIP.
3. En la raíz del proyecto de la instancia, comprueba la configuración sin mostrar valores secretos:

```bash
docker compose config --quiet
```

4. Construye e inicia los cuatro contenedores, y revisa su estado:

```bash
docker compose up -d --build
docker compose ps
```

5. Si algún servicio no está activo, consulta `docker compose logs --tail=100 users-service forum-service moderation-service frontend`. Verifica desde la instancia `curl -I http://localhost` y desde el navegador `http://<IP_PUBLICA>`.
6. Importa `postman/Foro_Online_Docker.postman_environment.json`, configura la IP pública como host y recorre registro, login, foro y administración. Confirma que las operaciones persisten en Oracle.

Al terminar la demostración, detén o termina la instancia y confirma el consumo del laboratorio. La instancia pública no tendrá HTTPS; para una entrega de curso, limita la exposición al puerto 80 y no introduzcas contraseñas reales en el video.

Los archivos `.env`, `.cloud-secrets.properties`, el Wallet y las claves nunca se copian al repositorio público ni al ZIP.
