# Ley 1581/2012 — Módulo 1: Objeto, Ámbito de Aplicación y Definiciones

> **Artículos:** 1, 2, 3 | **Título:** I
> **Relevancia Cavaltec:** Vocabulario normativo base para el cuestionario, las preguntas y el motor de IA.

---

## Artículo 1 — Objeto

La ley desarrolla el derecho constitucional que tienen todas las personas a:

- **Conocer** las informaciones recogidas sobre ellas en bases de datos o archivos.
- **Actualizar** dicha información.
- **Rectificar** información incorrecta.

Fundamento constitucional: artículo 15 (habeas data) y artículo 20 (derecho a la información) de la Constitución Política de Colombia.

---

## Artículo 2 — Ámbito de Aplicación

### ¿A quién aplica?

La ley aplica al tratamiento de datos personales registrados en **cualquier base de datos** (pública o privada) cuando:

1. El tratamiento se efectúe **en territorio colombiano**, o
2. El Responsable/Encargado **no esté en Colombia** pero le sea aplicable la legislación colombiana por normas o tratados internacionales.

### Excepciones (la ley NO aplica a):

| Excepción | Descripción |
|-----------|-------------|
| a) Personal o doméstico | Bases de datos de uso exclusivamente privado. Si se suministran a terceros, sí aplica la ley. |
| b) Seguridad y defensa nacional | Incluye prevención, detección y control de lavado de activos y financiamiento del terrorismo. |
| c) Inteligencia y contrainteligencia | Bases de datos con ese fin específico. |
| d) Periodismo y editorial | Información periodística y contenidos editoriales. |
| e) Ley 1266 de 2008 | Datos financieros y crediticios (centrales de riesgo). |
| f) Ley 79 de 1993 | Datos del sector cooperativo. |

> **Parágrafo:** Los principios de protección de datos **sí aplican** a todas las bases de datos, incluso a las exceptuadas, dentro de los límites de la ley y sin contradecir la reserva legal.

---

## Artículo 3 — Definiciones

Estas definiciones son la base del vocabulario normativo. El agente de IA debe usarlas para traducir términos legales a lenguaje sencillo en el cuestionario.

| Término | Definición legal |
|---------|-----------------|
| **Autorización** | Consentimiento previo, expreso e informado del Titular para llevar a cabo el Tratamiento de datos personales. |
| **Base de Datos** | Conjunto organizado de datos personales que sea objeto de Tratamiento. |
| **Dato personal** | Cualquier información vinculada o que pueda asociarse a una o varias personas naturales determinadas o determinables. |
| **Encargado del Tratamiento** | Persona natural o jurídica, pública o privada, que realice el Tratamiento de datos personales **por cuenta del Responsable**. |
| **Responsable del Tratamiento** | Persona natural o jurídica, pública o privada, que **decide** sobre la base de datos y/o el Tratamiento de los datos. |
| **Titular** | Persona natural cuyos datos personales sean objeto de Tratamiento. |
| **Tratamiento** | Cualquier operación sobre datos personales: recolección, almacenamiento, uso, circulación o supresión. |

### Diferencia clave: Responsable vs. Encargado

```
RESPONSABLE → Decide el "qué" y "para qué" del tratamiento
ENCARGADO   → Ejecuta el tratamiento en nombre del Responsable
              (ej: un proveedor de servicios cloud, una empresa de call center)

Una misma entidad puede ser ambos simultáneamente (Art. 18, parágrafo).
```

---

## Notas para el desarrollo

- **Dato personal** incluye nombre, cédula, correo, teléfono, dirección IP, datos biométricos, etc.
- **Tratamiento** abarca todo el ciclo de vida del dato: desde la recolección hasta la eliminación.
- La distinción **Responsable/Encargado** es clave para determinar a quién se le aplican los deberes del Título VI.
- En el contexto del reto: la empresa que use la plataforma Cavaltec actúa como **Responsable** frente a sus clientes/empleados; Cavaltec podría ser **Encargado** si procesa datos en nombre de esa empresa.
