# Cavaltec Privacy Design Check - Frontend

## Inicio rápido

```bash
cd frontend-agenda/quasar-project
npm install
npm run dev
```

## Configuración

El frontend se conecta por defecto a `http://localhost:8080`. Puede cambiarse con:

```bash
VITE_CAVALTEC_API_URL=http://localhost:8080
VITE_CAVALTEC_MICROSOFT_CLIENT_ID=...
```

## Endpoints requeridos

### Autenticación

- `POST /auth/login`
- `POST /auth/register`
- `GET /auth/google`
- `GET /auth/google/callback`
- `POST /auth/microsoft`

Registro:

```json
{
  "nombre": "Ana Pérez",
  "email": "ana@empresa.com",
  "password": "secret123",
  "role": "EVALUADOR",
  "empresa": {
    "nombre": "Empresa SAS",
    "nit": "900000000-1",
    "sector": "Tecnología",
    "tamano": "mediana"
  }
}
```

Respuesta:

```json
{
  "token": "jwt",
  "role": "EVALUADOR",
  "email": "ana@empresa.com",
  "nombre": "Ana Pérez",
  "empresaId": "uuid"
}
```

### Evaluaciones

- `GET /evaluaciones`
- `GET /evaluaciones/{id}`
- `POST /evaluaciones`
- `PUT /evaluaciones/{id}`

Payload:

```json
{
  "completar": false,
  "respuestas": [
    {
      "preguntaNumero": 1,
      "respuesta": true,
      "evidencia": "Política aprobada y publicada"
    }
  ]
}
```

### Asistente IA

- `POST /chat/diagnostico`

```json
{
  "mensaje": "Qué evidencia demuestra cumplimiento",
  "preguntaNumero": 6,
  "respuesta": false,
  "evidencia": ""
}
```

## Autenticación

- El token JWT se almacena en `localStorage` con la clave `cavaltec_token`.
- Axios incluye automáticamente `Authorization: Bearer <token>`.
- La ruta principal exige sesión autenticada.
- Google OAuth usa Authorization Code en backend. El frontend redirige a `GET /auth/google` y recibe el JWT propio de la app en `/auth/callback`.
- Microsoft OAuth sigue requiriendo `VITE_CAVALTEC_MICROSOFT_CLIENT_ID`.

## Roles

- `EVALUADOR`: responde diagnósticos y guarda evaluaciones.
- `AUDITOR`: revisa resultados y evidencias.
- `ADMIN`: rol reservado para administración multiempresa.

## Navegación

- `/`: Dashboard de autodiagnóstico.
- `/login`: Inicio de sesión, registro y OAuth.
