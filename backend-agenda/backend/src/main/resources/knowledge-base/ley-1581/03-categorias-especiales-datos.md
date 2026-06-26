# Ley 1581/2012 — Módulo 3: Categorías Especiales de Datos

> **Artículos:** 5, 6, 7 | **Título:** III
> **Relevancia Cavaltec:** El cuestionario de diagnóstico debe identificar si la empresa trata datos sensibles o datos de menores, ya que esto eleva significativamente el nivel de obligaciones y riesgo regulatorio.

---

## Artículo 5 — Datos Sensibles

### Definición

Son datos sensibles aquellos que **afectan la intimidad del Titular** o cuyo uso indebido puede generar **discriminación**.

### Categorías de datos sensibles (listado taxativo)

| Categoría | Ejemplos prácticos |
|-----------|-------------------|
| Origen racial o étnico | Autoidentificación étnica en formularios RRHH |
| Orientación política | Afiliación a partidos, declaraciones políticas |
| Convicciones religiosas o filosóficas | Religión en fichas de personal |
| Pertenencia a sindicatos | Afiliación sindical de empleados |
| Pertenencia a organizaciones de DDHH o partidos de oposición | Militancia en grupos de sociedad civil |
| **Datos de salud** | Historias clínicas, incapacidades, discapacidades |
| **Vida sexual** | Orientación sexual, vida íntima |
| **Datos biométricos** | Huella dactilar, reconocimiento facial, iris |

> ⚠️ **Alerta para el diagnóstico:** Si la empresa trata cualquiera de estas categorías, el nivel de exigencia aumenta considerablemente.

---

## Artículo 6 — Tratamiento de Datos Sensibles

### Regla general: PROHIBICIÓN

El tratamiento de datos sensibles está **prohibido** por defecto.

### Excepciones que habilitan el tratamiento

El tratamiento de datos sensibles está permitido **solo** en los siguientes casos:

| # | Excepción | Condición |
|---|-----------|-----------|
| a | **Autorización explícita del Titular** | No aplica cuando la ley exime la autorización. La autorización debe ser **explícita** (no implícita). |
| b | **Interés vital del Titular incapacitado** | El titular está física o jurídicamente incapacitado. Los representantes legales deben autorizar. |
| c | **Entidades sin ánimo de lucro** | Solo para miembros o personas con contacto regular por la finalidad de la entidad. **No se pueden transferir a terceros sin autorización.** |
| d | **Reconocimiento, ejercicio o defensa de un derecho** | En el contexto de un proceso judicial. |
| e | **Finalidad histórica, estadística o científica** | Se deben adoptar medidas de **supresión de identidad** de los titulares. |

---

## Artículo 7 — Derechos de Niños, Niñas y Adolescentes (NNA)

### Regla general: PROHIBICIÓN

> **Queda proscrito el Tratamiento de datos personales de NNA**, salvo aquellos datos que sean de **naturaleza pública**.

### Obligaciones del Estado y entidades educativas

- Capacitar a representantes legales y tutores sobre riesgos del tratamiento indebido de datos de NNA.
- Proveer conocimiento sobre uso responsable y seguro de datos personales por parte de NNA.
- El Gobierno Nacional debía reglamentar la materia dentro de los 6 meses siguientes a la promulgación.

---

## Implicaciones para el reto Cavaltec

### Preguntas adicionales recomendadas para el cuestionario

Si la empresa responde que **sí trata** alguna de las categorías especiales, se deben activar preguntas condicionales (lógica del Nivel 2):

```
IF empresa trata datos de salud, biométricos o de orientación sexual:
  → ¿Cuenta con autorización explícita (no genérica) para estos datos?
  → ¿Tiene medidas de seguridad reforzadas para esta categoría?
  → ¿Está esta categoría documentada en su política de tratamiento?

IF empresa trata datos de NNA:
  → ¿Solo trata datos públicos de NNA?
  → ¿Cuenta con autorización del representante legal?
  → ¿Tiene medidas especiales de protección?
```

### Impacto en el puntaje de cumplimiento

El tratamiento de datos sensibles sin las excepciones habilitadoras corresponde a:
- **Violación directa del Art. 6** → Sanción: cierre inmediato y definitivo (Art. 23d)
- Esta es la sanción más grave contemplada en la ley

### Recomendación de IA sugerida para brechas en esta área

> "Su organización trata datos que la ley clasifica como **sensibles**. Esto requiere una autorización **explícita y separada** del titular (no basta con el consentimiento general). Le recomendamos: (1) revisar todos sus formularios de recolección, (2) añadir casillas específicas para cada tipo de dato sensible, y (3) documentar la base legal que habilita cada tratamiento."
