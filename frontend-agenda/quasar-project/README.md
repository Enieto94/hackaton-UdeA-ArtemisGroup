# Cavaltec Privacy Design Check

Frontend Quasar/Vue para el autodiagnóstico de cumplimiento de protección de datos en fase de diseño, alineado con la Ley 1581 de 2012.

## Instalación

```bash
npm install
```

## Configuración

Cree un archivo `.env` en este directorio si necesita cambiar URLs o activar OAuth:

```bash
VITE_CAVALTEC_API_URL=http://localhost:8080
VITE_CAVALTEC_GOOGLE_CLIENT_ID=cliente-google.apps.googleusercontent.com
VITE_CAVALTEC_MICROSOFT_CLIENT_ID=cliente-microsoft
```

El backend debe permitir el origen del dev server de Quasar, normalmente `http://localhost:9000`.

## Desarrollo

```bash
npm run dev
```

## Build

```bash
npm run build
```

## Funcionalidad migrada

- Login tradicional y OAuth Google/Microsoft contra `/auth/*`.
- Registro de empresa con NIT, sector y tamaño.
- Cuestionario de 11 preguntas con lógica condicional P1/P10.
- Cálculo ponderado 0-100 por bloques.
- Dashboard con gauge, brechas, plan priorizado y descarga de reporte.
- Guardado/carga de evaluaciones vía `/evaluaciones`.
- Asistente contextual vía `/chat/diagnostico`.
