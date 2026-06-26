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
