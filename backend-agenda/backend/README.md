# Backend Cavaltec Privacy Design Check

Backend Spring Boot para autenticación, empresas, evaluaciones, respuestas y recomendaciones del autodiagnóstico Cavaltec Privacy Design Check.

## Requisitos

- Java 21
- PostgreSQL
- Maven Wrapper incluido en el proyecto

## Configuración local

1. Copia `.env.example` a `.env`.
2. Ajusta las variables según tu entorno local.
3. Ejecuta PostgreSQL con una base de datos que coincida con `DB_NAME`.

El archivo `.env` está ignorado por Git porque contiene credenciales y secretos locales.

### Google OAuth

Para el flujo Authorization Code configure estas variables en `.env`:

```bash
GOOGLE_CLIENT_ID=416457771927-lee59eqjf22nkftfcf00p37qlaupql9h.apps.googleusercontent.com
GOOGLE_CLIENT_SECRET=...
GOOGLE_REDIRECT_URI=http://localhost:8080/auth/google/callback
FRONTEND_AUTH_CALLBACK_URL=http://localhost:9000/auth/callback
```

El `GOOGLE_REDIRECT_URI` debe coincidir exactamente con el URI autorizado en Google Cloud. Si usa `http://172.25.20.4:9000` para el frontend, normalmente el callback de frontend debe ser `http://172.25.20.4:9000/auth/callback` y el redirect autorizado de Google debe apuntar al backend, por ejemplo `http://172.25.20.4:8080/auth/google/callback`.

## Ejecutar

```bash
./mvnw spring-boot:run
```

Si el wrapper no tiene permisos de ejecución:

```bash
bash mvnw spring-boot:run
```

## Probar

```bash
./mvnw test
```

## Documentación API

Con el backend corriendo, Swagger queda disponible en:

```text
http://localhost:8080/swagger-ui/index.html
```
