# RETO CAVALTEC — Aplicación Web para Autodiagnóstico de Cumplimiento de Protección de Datos (Fase de Diseño)

## METADATA

```yaml
organizer: Talento TIC / iTraining / Universidad de Antioquia / Universidad de Caldas / Ubicua
sponsor: CAVALTEC (Ciberseguridad y TI)
regulation: Ley 1581 de 2012 (Colombia)
domain: Data Privacy / Web Security / AI
phase_scope: Diseño (Privacy by Design)
```

---

## 1. OBJETIVO DEL SISTEMA

Construir una **aplicación web segura, intuitiva y multiempresa** que permita a organizaciones realizar un autodiagnóstico del nivel de cumplimiento de la **Ley 1581 de 2012** en la fase de diseño.

### Outputs requeridos del sistema

| Output | Descripción |
|---|---|
| `compliance_score` | Porcentaje numérico de cumplimiento (0–100%) |
| `gap_list` | Lista de preguntas/áreas donde la empresa falla |
| `ai_recommendations` | Recomendaciones prácticas generadas por IA |
| `improvement_plan` | Estrategias priorizadas para cerrar brechas |

---

## 2. NIVELES DE PARTICIPACIÓN

El reto se divide en tres niveles de complejidad creciente. Implementar de menor a mayor.

### Nivel 1 — Básico (MVP)

- [ ] Formulario de diagnóstico con las 11 preguntas del módulo
- [ ] Cálculo de resultado en porcentaje
- [ ] Interfaz simple funcional

### Nivel 2 — Intermedio

- [ ] Login con OAuth (Google, Microsoft)
- [ ] Dashboard visual con resultados
- [ ] Lógica condicional de preguntas (pregunta 11 depende de pregunta 10)
- [ ] Recomendaciones básicas por brecha identificada

### Nivel 3 — Avanzado

- [ ] Integración con IA (asistencia contextual y recomendaciones automáticas)
- [ ] Soporte multiempresa (cada empresa tiene sus propias evaluaciones)
- [ ] Roles diferenciados: administrador, evaluador, auditor
- [ ] Reportes descargables (PDF o dashboard exportable)
- [ ] Histórico de evaluaciones por empresa
- [ ] Seguridad avanzada (OWASP Top 10)

---

## 3. MÓDULOS DEL SISTEMA

### 3.1 Módulo de Autenticación y Acceso

| Categoría | Funcionalidad | Especificación |
|---|---|---|
| Autenticación | Login con OAuth | Proveedores: Google, Microsoft (Hotmail/Outlook) |
| Autorización | Gestión de roles | Roles: `admin`, `empresa_evaluada`, `auditor` |
| Seguridad | Sesiones seguras | Tokens JWT, cifrado, manejo seguro de sesiones |

### 3.2 Módulo de Empresa

| Categoría | Funcionalidad | Especificación |
|---|---|---|
| Registro | Datos básicos | Campos: `nombre`, `NIT`, `sector`, `tamaño` |
| Multiempresa | Gestión por cliente | Una empresa puede tener N evaluaciones independientes |

### 3.3 Módulo de Diagnóstico (Fase Diseño)

Este es el módulo central. Contiene 11 preguntas agrupadas en 3 bloques con pesos específicos.

#### Estructura de pesos y preguntas

```
TOTAL MÁXIMO ALCANZABLE: 100%
```

| Bloque | N° | Pregunta | Peso | Observación |
|---|---|---|---|---|
| **Política de datos personales** (máx. 40%) | 1 | ¿Cuenta con una política de tratamiento de datos personales? | 0–40% | Hereda el peso de las preguntas hijas 2–5 |
| | 2 | ¿La política está documentada y publicada en medio de fácil acceso? | 10% | |
| | 3 | ¿Define las finalidades del tratamiento de datos? | 10% | |
| | 4 | ¿Incluye los derechos de los titulares? | 10% | |
| | 5 | ¿Menciona cómo ejercer los derechos de los titulares? | 10% | |
| **Privacidad desde el diseño** (máx. 36%) | 6 | ¿Incorpora evaluaciones de impacto (Privacy Impact Assessments)? | 12% | |
| | 7 | ¿Aplica técnicas de minimización de datos? | 12% | |
| | 8 | ¿Configura sus sistemas para recopilar el mínimo de datos por defecto? | 12% | |
| **Gobernanza** (máx. 24%) | 9 | ¿Cuenta con un sistema de administración de riesgos? | 16% | |
| | 10 | ¿Cuenta con un oficial de protección de datos personales? | 8% | |
| | 11 | ¿Está designado formalmente? | — | Complementaria, no suma al total |

#### Lógica de cálculo del score

```
Pregunta 1 actúa como PADRE de preguntas 2–5:
  - Si P1 = NO → P2, P3, P4, P5 no aplican (score bloque = 0%)
  - Si P1 = SÍ  → sumar pesos de P2, P3, P4, P5 según respuestas

Pregunta 11 es COMPLEMENTARIA de Pregunta 10:
  - Si P10 = NO → P11 no aplica
  - Si P10 = SÍ → mostrar P11 (no suma al total, pero registrar)

score_total = suma de pesos de preguntas respondidas con SÍ
```

### 3.4 Módulo de Inteligencia Artificial

| Categoría | Funcionalidad | Descripción técnica |
|---|---|---|
| Asistencia | Explicación de preguntas | Traducir términos legales de la Ley 1581 a lenguaje sencillo |
| Apoyo | Orientación para responder | Sugerir cómo interpretar cada pregunta según el contexto empresarial |
| Recomendación | Plan de acción | Generar acciones concretas para cerrar brechas identificadas |
| Análisis | Interpretación de resultados | Explicar qué significa el nivel de cumplimiento obtenido |

**Nota de implementación:** La IA debe recibir como contexto: (a) el texto de la pregunta, (b) la respuesta dada por el usuario, (c) el artículo relevante de la Ley 1581, (d) el bloque al que pertenece.

### 3.5 Módulo de Resultados

| Categoría | Funcionalidad | Especificación |
|---|---|---|
| Indicador | Nivel de cumplimiento (%) | Basado en respuestas positivas ponderadas |
| Visualización | Medidor tipo gauge | Indicador estilo velocímetro, escala 0–100% |
| Brechas | Identificación automática | Listar las preguntas respondidas negativamente |
| Recomendaciones | Plan de mejora | Acciones priorizadas por impacto/peso |
| Reportes | Exportables | Formato PDF o dashboard descargable |

### 3.6 Módulo de Seguridad

| Categoría | Funcionalidad | Estándar |
|---|---|---|
| Protección | Validación de entradas | Prevención de inyección, XSS, CSRF |
| Seguridad | OWASP Top 10 | Mitigación de los 10 riesgos más críticos |
| Privacidad | Protección de datos | Buen manejo de información empresarial sensible |

---

## 4. CRITERIOS DE EVALUACIÓN

Los evaluadores puntuarán la solución con los siguientes pesos:

| Categoría | Peso | Aspectos evaluados |
|---|---|---|
| Alineación con la Ley 1581 | 20% | Interpretación correcta de la normativa; coherencia del diagnóstico |
| Desarrollo técnico | 20% | Calidad de código; arquitectura; buenas prácticas |
| Seguridad | 15% | Implementación de controles; manejo de autenticación/autorización |
| Uso de IA | 15% | Utilidad real; precisión de recomendaciones; valor agregado al usuario |
| Experiencia de usuario | 10% | Facilidad de uso; diseño intuitivo |
| Calidad del diagnóstico | 10% | Claridad de resultados; accionabilidad |
| Innovación | 10% | Diferenciadores; funcionalidades adicionales |

---

## 5. RESULTADO ESPERADO

Una aplicación que permita a cualquier empresa colombiana:

1. **Entender** su nivel de cumplimiento en la fase de diseño de protección de datos
2. **Identificar** rápidamente sus debilidades (brechas por pregunta/bloque)
3. **Recibir** orientación clara y accionable para mejorar
4. **Adoptar** principios de Privacy by Design (Privacidad desde el Diseño)

---

## 6. REQUERIMIENTOS NO FUNCIONALES

- **Escalabilidad:** Arquitectura multiempresa (multi-tenant)
- **Usabilidad:** Interfaz amigable, sin conocimiento legal previo requerido
- **Seguridad:** Basada en OWASP Top 10 y principios de privacidad por diseño
- **Accesibilidad:** Sin barreras técnicas para el usuario final
- **Mantenibilidad:** Código limpio, bien documentado y con buenas prácticas

---

## 7. CONTEXTO REGULATORIO RELEVANTE (Ley 1581 de 2012)

Los siguientes artículos son los más relevantes para la lógica del diagnóstico:

| Artículo | Tema | Relevancia para el diagnóstico |
|---|---|---|
| Art. 4 | Principios rectores | Legalidad, finalidad, libertad, veracidad, transparencia, seguridad, confidencialidad |
| Art. 8 | Derechos de los titulares | Conocer, actualizar, rectificar, revocar, acceder, presentar quejas |
| Art. 9 | Autorización del titular | Consentimiento previo, expreso e informado |
| Art. 12 | Deber de informar | El responsable debe informar finalidad, derechos y su identificación |
| Art. 17 | Deberes del responsable | Manual de políticas, seguridad, actualización, reclamos |
| Art. 18 | Deberes del encargado | Actualización en 5 días, leyendas en base de datos, no circular datos bloqueados |
| Art. 19 | Autoridad de control | Superintendencia de Industria y Comercio (SIC) |
| Art. 23 | Sanciones | Multas hasta 2.000 SMMLV, suspensión, cierre |

---

## 8. GUÍA DE IMPLEMENTACIÓN PARA AGENTES

### Stack tecnológico sugerido

```
Frontend:   React / Vue / Angular  →  formulario, dashboard, gauge
Backend:    Node.js / Python (FastAPI/Django)  →  API REST, lógica de scoring
Auth:       OAuth 2.0 (Google Identity, Microsoft Entra ID)
IA:         API de LLM (e.g., Claude, GPT) para explicaciones y recomendaciones
DB:         PostgreSQL / MySQL  →  multiempresa con schema por tenant o campo empresa_id
Seguridad:  JWT para sesiones, HTTPS, validación server-side, rate limiting
```

### Modelo de datos mínimo

```sql
-- Empresas
CREATE TABLE empresas (
  id UUID PRIMARY KEY,
  nombre VARCHAR NOT NULL,
  nit VARCHAR UNIQUE NOT NULL,
  sector VARCHAR,
  tamano VARCHAR,  -- micro, pequeña, mediana, grande
  created_at TIMESTAMP
);

-- Usuarios
CREATE TABLE usuarios (
  id UUID PRIMARY KEY,
  empresa_id UUID REFERENCES empresas(id),
  email VARCHAR UNIQUE NOT NULL,
  oauth_provider VARCHAR,  -- google | microsoft
  rol VARCHAR NOT NULL,    -- admin | evaluador | auditor
  created_at TIMESTAMP
);

-- Evaluaciones
CREATE TABLE evaluaciones (
  id UUID PRIMARY KEY,
  empresa_id UUID REFERENCES empresas(id),
  usuario_id UUID REFERENCES usuarios(id),
  fecha TIMESTAMP,
  score_total DECIMAL(5,2),  -- 0.00 a 100.00
  estado VARCHAR  -- borrador | completada
);

-- Respuestas
CREATE TABLE respuestas (
  id UUID PRIMARY KEY,
  evaluacion_id UUID REFERENCES evaluaciones(id),
  pregunta_numero INT NOT NULL,  -- 1 al 11
  respuesta BOOLEAN,             -- true = SÍ, false = NO, null = no aplica
  peso_aplicado DECIMAL(5,2),
  ai_recomendacion TEXT
);
```

### Algoritmo de scoring

```python
PREGUNTAS = {
    1: {"peso": None, "bloque": "politica", "es_padre": True, "hijos": [2, 3, 4, 5]},
    2: {"peso": 0.10, "bloque": "politica"},
    3: {"peso": 0.10, "bloque": "politica"},
    4: {"peso": 0.10, "bloque": "politica"},
    5: {"peso": 0.10, "bloque": "politica"},
    6: {"peso": 0.12, "bloque": "privacidad_diseno"},
    7: {"peso": 0.12, "bloque": "privacidad_diseno"},
    8: {"peso": 0.12, "bloque": "privacidad_diseno"},
    9: {"peso": 0.16, "bloque": "gobernanza"},
    10: {"peso": 0.08, "bloque": "gobernanza"},
    11: {"peso": None, "bloque": "gobernanza", "complementaria": True, "depende_de": 10},
}

def calcular_score(respuestas: dict) -> float:
    """
    respuestas: {1: True/False, 2: True/False, ..., 11: True/False}
    Retorna score entre 0.0 y 100.0
    """
    score = 0.0

    # Pregunta padre: si P1 = False, las hijas no suman
    p1_activa = respuestas.get(1, False)

    for num, config in PREGUNTAS.items():
        if config.get("es_padre") or config.get("complementaria"):
            continue  # no tienen peso propio
        
        if num in [2, 3, 4, 5] and not p1_activa:
            continue  # padre negativo, hijas no aplican
        
        if respuestas.get(num) is True:
            score += config["peso"] * 100

    return round(score, 2)

def identificar_brechas(respuestas: dict) -> list:
    """Retorna lista de números de pregunta donde la empresa falló."""
    brechas = []
    p1_activa = respuestas.get(1, False)
    
    for num in range(1, 12):
        config = PREGUNTAS.get(num, {})
        if config.get("complementaria"):
            continue
        if num in [2, 3, 4, 5] and not p1_activa:
            continue
        if not respuestas.get(num, False):
            brechas.append(num)
    
    return brechas
```

### Prompt base para el módulo de IA

```
Sistema: Eres un experto en protección de datos personales bajo la Ley 1581 de 2012 de Colombia.
Tu tarea es ayudar a las empresas a entender y mejorar su cumplimiento normativo.

Contexto de la pregunta:
- Número: {pregunta_numero}
- Texto: {texto_pregunta}
- Bloque: {nombre_bloque}
- Peso en el diagnóstico: {peso}%
- Respuesta de la empresa: {respuesta_empresa}  (SÍ / NO)
- Artículo relevante Ley 1581: {articulo_relevante}

Tareas:
1. EXPLICACIÓN: Explica esta pregunta en lenguaje sencillo (máx. 2 oraciones, sin jerga legal).
2. ORIENTACIÓN: Indica qué evidencia o práctica concreta demuestra cumplimiento.
3. RECOMENDACIÓN (solo si respondió NO): Proporciona 2-3 acciones concretas y priorizadas para cerrar esta brecha.
```

---

## 9. CHECKLIST DE ENTREGABLES POR NIVEL

### Nivel 1 — Básico ✓

- [ ] Formulario HTML/React con las 11 preguntas
- [ ] Validación de respuestas obligatorias (salvo las condicionales)
- [ ] Cálculo correcto del score según los pesos definidos
- [ ] Visualización del resultado en porcentaje
- [ ] Identificación de brechas (preguntas contestadas con NO)

### Nivel 2 — Intermedio ✓

- [ ] Autenticación OAuth funcional (al menos un proveedor)
- [ ] Sesiones seguras con tokens
- [ ] Dashboard con gauge/velocímetro visual
- [ ] Lógica condicional implementada (P1→P2-5, P10→P11)
- [ ] Recomendaciones básicas hardcoded o con template por pregunta
- [ ] Registro de empresa (nombre, NIT, sector, tamaño)

### Nivel 3 — Avanzado ✓

- [ ] Integración con API de LLM para recomendaciones dinámicas
- [ ] Explicación de preguntas en lenguaje sencillo vía IA
- [ ] Soporte multiempresa con aislamiento de datos
- [ ] Tres roles implementados: admin, evaluador, auditor
- [ ] Reporte exportable en PDF con resultados y plan de acción
- [ ] Histórico de evaluaciones con comparativa temporal
- [ ] Validaciones OWASP: prevención XSS, CSRF, inyección SQL
- [ ] Rate limiting y manejo seguro de sesiones
