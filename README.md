# Cavaltec Privacy Design Check

Aplicación web para que organizaciones colombianas realicen un autodiagnóstico del nivel de cumplimiento de la Ley 1581 de 2012, con foco en la fase de diseño y en prácticas de privacidad desde el diseño.

La solución busca entregar:

- Nivel porcentual de cumplimiento.
- Identificación de brechas por bloque y por pregunta.
- Recomendaciones prácticas asistidas por IA.
- Estrategias priorizadas para cerrar brechas.
- Experiencia multiempresa, segura e intuitiva.

## Objetivo

Construir una plataforma web segura, escalable y multiempresa que permita a una organización registrar sus datos básicos, iniciar sesión mediante OAuth, responder un diagnóstico guiado y obtener un resultado visual claro sobre su nivel de cumplimiento en protección de datos personales.

El diagnóstico se basa en los módulos documentados en [knowledge-base/ley-1581](backend-agenda/backend/src/main/resources/knowledge-base/ley-1581/README.md) y en el alcance funcional descrito en [cavaltec-autodiagnostico-requisitos.md](backend-agenda/backend/src/main/resources/knowledge-base/cavaltec-autodiagnostico-requisitos.md).

## Estado actual

El repositorio fue reutilizado desde un proyecto anterior de agenda de citas. Por eso existe un backend Spring Boot funcional bajo `backend-agenda/backend`, pero su dominio original todavía corresponde a usuarios, citas y agenda.

La actualización actual deja el objetivo correcto del repositorio y agrega una primera aplicación web funcional en `frontend-agenda/privacy-design-check` para validar el flujo del reto:

- Acceso con proveedores OAuth representados en la interfaz.
- Captura de datos básicos de empresa.
- Cuestionario ponderado de 11 preguntas.
- Lógica condicional P1 -> P2-P5 y P10 -> P11.
- Cálculo automático del porcentaje de cumplimiento.
- Diagnóstico visual con brechas, bloques y plan de mejora.
- Chat de apoyo que explica preguntas, criterios de evaluación y recomendaciones con base normativa local.

## Estructura

```text
.
├── PROJECT_DOCUMENTATION.md          # Documentación técnica actualizada
├── frontend-agenda/
│   ├── package-lock.json
│   ├── quasar-project/              # Gitlink heredado sin implementación activa
│   └── privacy-design-check/
│       ├── index.html                # App web estática del autodiagnóstico
│       ├── styles.css                # UI responsive
│       └── app.js                    # Scoring, chat, diagnóstico y estado
└── backend-agenda/backend/           # Backend Spring Boot heredado
    └── src/main/resources/
        └── knowledge-base/
            ├── cavaltec-autodiagnostico-requisitos.md
            └── ley-1581/             # Base normativa que consultará el backend
```

## Ejecutar la interfaz

La primera versión del frontend no requiere instalación de dependencias.

Abra este archivo en el navegador:

[frontend-agenda/privacy-design-check/index.html](frontend-agenda/privacy-design-check/index.html)

También puede servirlo localmente si desea probarlo vía HTTP:

```bash
cd frontend-agenda/privacy-design-check
python3 -m http.server 9000
```

Luego visite `http://localhost:9000`.

## Modelo de diagnóstico

El score máximo es 100%.

| Bloque | Preguntas | Peso |
|---|---:|---:|
| Política de datos personales | 1-5 | 40% |
| Privacidad desde el diseño | 6-8 | 36% |
| Gobernanza | 9-11 | 24% |

Reglas principales:

- P1 no suma directamente. Si P1 = No, P2-P5 no aplican y el bloque de política queda en 0%.
- P2-P5 suman 10% cada una cuando P1 = Sí.
- P6-P8 suman 12% cada una.
- P9 suma 16% y P10 suma 8%.
- P11 es complementaria. Solo aparece si P10 = Sí y no suma al total.

## Seguridad y privacidad por diseño

El objetivo técnico de la plataforma es evolucionar hacia:

- OAuth 2.0 con Google y Microsoft.
- JWT de vida corta y refresh tokens protegidos.
- Aislamiento multiempresa por `empresa_id` y autorización por roles.
- Validación server-side de entradas.
- Protección contra OWASP Top 10: XSS, inyección, CSRF cuando aplique, control de acceso roto y exposición de datos sensibles.
- Minimización de datos empresariales capturados.
- Trazabilidad de evaluaciones y reportes sin almacenar información innecesaria.

## Próximos pasos técnicos

1. Migrar el dominio del backend desde agenda hacia empresas, usuarios, evaluaciones y respuestas.
2. Implementar OAuth real con Google y Microsoft Entra ID.
3. Persistir empresas, evaluaciones, respuestas, brechas y recomendaciones.
4. Integrar un proveedor de IA con prompts que usen `src/main/resources/knowledge-base`.
5. Agregar reportes PDF y un histórico de evaluaciones por empresa.
