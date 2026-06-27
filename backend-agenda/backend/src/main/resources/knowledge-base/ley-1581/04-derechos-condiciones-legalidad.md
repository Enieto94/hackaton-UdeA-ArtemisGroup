# Ley 1581/2012 — Módulo 4: Derechos del Titular y Condiciones de Legalidad

> **Artículos:** 8, 9, 10, 11, 12, 13 | **Título:** IV
> **Relevancia Cavaltec:** Este módulo fundamenta el **Bloque 1 del diagnóstico** (Política de datos personales, máx. 40%). Las preguntas 1 a 5 del cuestionario se derivan directamente de estos artículos.

---

## Artículo 8 — Derechos de los Titulares

El titular de datos personales tiene los siguientes derechos (denominados colectivamente **habeas data**):

| Derecho | Descripción | Artículo de referencia |
|---------|-------------|------------------------|
| **a) Acceso y rectificación** | Conocer, actualizar y rectificar sus datos frente al Responsable o Encargado. Aplica para datos inexactos, incompletos, fraccionados o cuyo tratamiento no haya sido autorizado. | Art. 8(a) |
| **b) Prueba de autorización** | Solicitar prueba de la autorización otorgada al Responsable (salvo excepciones del Art. 10). | Art. 8(b) |
| **c) Información sobre uso** | Ser informado sobre el uso dado a sus datos, previa solicitud. | Art. 8(c) |
| **d) Queja ante la SIC** | Presentar quejas ante la Superintendencia de Industria y Comercio por infracciones. | Art. 8(d) |
| **e) Revocación y supresión** | Revocar la autorización y/o solicitar supresión del dato cuando no se respeten principios o derechos. La revocatoria procede cuando la SIC haya determinado conductas contrarias a la ley. | Art. 8(e) |
| **f) Acceso gratuito** | Acceder en forma **gratuita** a sus datos que hayan sido objeto de tratamiento. | Art. 8(f) |

> **Nota para el diagnóstico — Pregunta 4:** La política de tratamiento debe **incluir** estos derechos de forma explícita y comprensible para el titular.

---

## Artículo 9 — Autorización del Titular

> Sin perjuicio de las excepciones previstas en la ley, el Tratamiento requiere la **autorización previa e informada** del Titular.

### Requisitos de la autorización válida

1. **Previa:** Debe obtenerse antes del tratamiento, no durante ni después.
2. **Expresa:** Debe ser una manifestación positiva (no silencio, no casillas pre-marcadas).
3. **Informada:** El titular debe conocer qué datos se tratan, para qué y sus derechos.
4. **Verificable:** Debe poder ser objeto de consulta posterior (registro de la autorización).

### Formas válidas de obtener autorización

- Formulario físico firmado
- Checkbox en formulario digital (no pre-marcado)
- Grabación de voz (call center)
- Correo electrónico con aceptación expresa
- Cualquier medio que deje trazabilidad

---

## Artículo 10 — Casos en que NO es necesaria la autorización

| Caso | Descripción |
|------|-------------|
| **a)** Entidad pública o judicial | Información requerida por entidad pública en ejercicio de funciones legales o por orden judicial. |
| **b)** Datos públicos | Datos de naturaleza pública (nombre en directorio telefónico, datos del Registro Mercantil, etc.). |
| **c)** Urgencia médica o sanitaria | Situaciones de emergencia donde la vida o salud está en riesgo. |
| **d)** Fines históricos, estadísticos o científicos | Tratamiento autorizado por la ley para estos fines específicos. |
| **e)** Registro Civil | Datos relacionados con el estado civil de las personas. |

> ⚠️ Quien acceda a datos sin autorización previa debe igualmente cumplir las disposiciones de la ley.

---

## Artículo 11 — Suministro de la Información

- La información puede suministrarse por **cualquier medio**, incluyendo electrónicos.
- Debe ser de **fácil lectura**, sin barreras técnicas.
- Debe corresponder exactamente a lo que reposa en la base de datos.

> **Implicación UX:** La plataforma Cavaltec debe garantizar que los resultados del diagnóstico sean accesibles y comprensibles para el titular (diseño inclusivo).

---

## Artículo 12 — Deber de Informar al Titular

Al solicitar la autorización, el Responsable debe informar **de manera clara y expresa**:

| Elemento obligatorio | Descripción |
|---------------------|-------------|
| **a)** Finalidad del tratamiento | Qué se hará con los datos y para qué. |
| **b)** Carácter facultativo | Para preguntas sobre datos sensibles o de NNA, indicar que la respuesta es voluntaria. |
| **c)** Derechos del titular | Enumerar los derechos del Art. 8. |
| **d)** Identificación del Responsable | Nombre, dirección física o electrónica y teléfono. |

> **Parágrafo:** El Responsable debe conservar prueba del cumplimiento de este artículo y entregársela al titular cuando lo solicite.

> **Nota para el diagnóstico — Pregunta 5:** La política debe indicar **cómo** ejercer los derechos. Este artículo establece que el titular debe conocer sus derechos antes del tratamiento, no después.

---

## Artículo 13 — Personas a quienes se puede suministrar la información

La información solo puede entregarse a:

1. **Los Titulares**, sus causahabientes (herederos) o representantes legales.
2. **Entidades públicas o administrativas** en ejercicio de funciones legales o por orden judicial.
3. **Terceros autorizados** por el Titular o por la ley.

---

## Mapeo directo con el diagnóstico Cavaltec (Bloque 1)

```
Pregunta 1: ¿Cuenta con política de tratamiento?
  → Base: Art. 12 (deber de informar), Art. 17(k) (deber de adoptar manual)
  → Sin política = incumplimiento estructural de Art. 12

Pregunta 2: ¿Política documentada y publicada en medio de fácil acceso?
  → Base: Art. 11 (fácil acceso, sin barreras técnicas), Art. 25 (Registro Nacional)
  → La política debe ser consultable por cualquier titular

Pregunta 3: ¿Define las finalidades del tratamiento?
  → Base: Art. 4(b) (principio de finalidad), Art. 12(a) (deber de informar finalidad)
  → Las finalidades deben ser específicas, no genéricas

Pregunta 4: ¿Incluye los derechos de los titulares?
  → Base: Art. 8 (lista de derechos), Art. 12(c) (deber de informar derechos)
  → Debe enumerar los 6 derechos del Art. 8

Pregunta 5: ¿Menciona cómo ejercer los derechos?
  → Base: Art. 14 (consultas), Art. 15 (reclamos), Art. 12(d) (datos de contacto)
  → Debe indicar canal, plazo y responsable de atender solicitudes
```

---

## Vocabulario para la IA asistente (traducción a lenguaje sencillo)

| Término legal | Traducción sencilla |
|---------------|---------------------|
| Autorización previa e informada | Permiso que la empresa pide al usuario **antes** de guardar sus datos, explicándole claramente para qué los usará |
| Causahabiente | Heredero o sucesor legal del titular de los datos |
| Carácter facultativo | Que la respuesta es **opcional** (nadie puede obligarte a dar ese dato) |
| Revocar la autorización | Retirar el permiso que diste anteriormente para usar tus datos |
| Supresión del dato | Pedir que borren toda la información que tienen sobre ti |
