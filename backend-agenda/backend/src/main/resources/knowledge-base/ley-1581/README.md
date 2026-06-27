# Knowledge Base — Ley 1581 de 2012
## Protección de Datos Personales (Colombia)

> **Uso para el backend y agentes de IA:** Esta Knowledge Base convierte la Ley Estatutaria 1581 de 2012 en archivos Markdown estructurados, optimizados para ser cargados desde el classpath de Spring Boot como contexto normativo del diagnóstico Cavaltec.

---

## Estructura de archivos

| Archivo | Contenido | Artículos | Relevancia para el reto |
|---------|-----------|-----------|-------------------------|
| `01-objeto-ambito-definiciones.md` | Objeto, ámbito de aplicación y definiciones clave | Art. 1–3 | Base conceptual: qué es dato personal, tratamiento, titular, responsable |
| `02-principios-rectores.md` | Los 8 principios que rigen el tratamiento de datos | Art. 4 | Fundamento del diagnóstico de cumplimiento (bloque Política) |
| `03-categorias-especiales-datos.md` | Datos sensibles y derechos de niños/adolescentes | Art. 5–7 | Preguntas del cuestionario sobre tipos de datos tratados |
| `04-derechos-condiciones-legalidad.md` | Derechos del titular, autorización, deber de informar | Art. 8–13 | Bloque "Política de datos personales" del diagnóstico (preguntas 1–5) |
| `05-procedimientos.md` | Consultas, reclamos y requisito de procedibilidad | Art. 14–16 | Indicador de canales de ejercicio de derechos |
| `06-deberes-responsables-encargados.md` | Obligaciones del Responsable y del Encargado | Art. 17–18 | Bloque "Gobernanza" del diagnóstico (preguntas 9–11) |
| `07-vigilancia-sanciones-registro.md` | SIC como autoridad, sanciones y Registro Nacional | Art. 19–25 | Contexto regulatorio y riesgo de incumplimiento |
| `08-transferencia-internacional.md` | Transferencia de datos a terceros países | Art. 26–30 | Escenarios multiempresa y transferencias cross-border |

---

## Ubicación en el backend

Ruta classpath:

```text
knowledge-base/ley-1581/
```

El alcance funcional del reto está en:

```text
knowledge-base/cavaltec-autodiagnostico-requisitos.md
```

---

## Mapeo normativo → Módulo de diagnóstico Cavaltec

```
BLOQUE 1 — Política de datos personales (máx. 40%)
  ├── Pregunta 1: ¿Cuenta con política de tratamiento? → Art. 17(k), 25
  ├── Pregunta 2: ¿Política documentada y publicada?  → Art. 11, 17(k)
  ├── Pregunta 3: ¿Define finalidades del tratamiento? → Art. 4(b), 12(a)
  ├── Pregunta 4: ¿Incluye derechos de los titulares?  → Art. 8, 12(c)
  └── Pregunta 5: ¿Cómo ejercer los derechos?         → Art. 8, 14, 15

BLOQUE 2 — Privacidad desde el diseño (máx. 36%)
  ├── Pregunta 6: ¿Evaluaciones de impacto (PIA)?     → Art. 4(g), 17(d)
  ├── Pregunta 7: ¿Minimización de datos?             → Art. 4(d), 4(f)
  └── Pregunta 8: ¿Mínimo de datos por defecto?       → Art. 4(b), 4(f)

BLOQUE 3 — Gobernanza (máx. 24%)
  ├── Pregunta 9:  ¿Sistema de administración de riesgos? → Art. 17(d), 18(b)
  ├── Pregunta 10: ¿Oficial de protección de datos?       → Art. 17(k)
  └── Pregunta 11: ¿Está designado formalmente?           → Art. 17(k) (complementaria)
```

---

## Datos normativos clave

- **Nombre completo:** Ley Estatutaria 1581 de 2012
- **Fecha:** 17 de octubre de 2012
- **Publicación:** Diario Oficial 48587, 18 de octubre de 2012
- **Autoridad de control:** Superintendencia de Industria y Comercio (SIC)
- **Reglamentos asociados:** Decreto 1377 de 2013, Decreto 1081 de 2015, Decreto 886 de 2014, Decreto 255 de 2022
- **Sentencia constitucional:** C-748 de 2011
