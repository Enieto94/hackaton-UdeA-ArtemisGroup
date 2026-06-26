# Ley 1581/2012 — Módulo 6: Deberes de Responsables y Encargados del Tratamiento

> **Artículos:** 17, 18 | **Título:** VI
> **Relevancia Cavaltec:** Este módulo fundamenta el **Bloque 3 del diagnóstico** (Gobernanza, máx. 24%) y parte del **Bloque 2** (Privacidad desde el diseño). Preguntas 6–11 del cuestionario.

---

## Artículo 17 — Deberes del Responsable del Tratamiento

El Responsable debe cumplir los siguientes 15 deberes:

### Grupo 1: Derechos del Titular

| Literal | Deber |
|---------|-------|
| **a)** | Garantizar al Titular, **en todo tiempo**, el pleno y efectivo ejercicio del habeas data. |
| **b)** | Solicitar y conservar copia de la autorización otorgada por el Titular. |
| **c)** | Informar debidamente al Titular sobre la finalidad de la recolección y los derechos que le asisten. |

### Grupo 2: Seguridad y calidad de la información

| Literal | Deber |
|---------|-------|
| **d)** | Conservar la información bajo **condiciones de seguridad** necesarias para impedir adulteración, pérdida, consulta, uso o acceso no autorizado o fraudulento. |
| **e)** | Garantizar que la información suministrada al Encargado sea **veraz, completa, exacta, actualizada, comprobable y comprensible**. |
| **f)** | **Actualizar la información** y comunicar oportunamente al Encargado todas las novedades respecto de los datos. |
| **g)** | **Rectificar** la información cuando sea incorrecta y comunicarlo al Encargado. |
| **h)** | Suministrar al Encargado **únicamente datos cuyo tratamiento esté previamente autorizado**. |

### Grupo 3: Control del Encargado

| Literal | Deber |
|---------|-------|
| **i)** | Exigir al Encargado, **en todo momento**, el respeto a las condiciones de seguridad y privacidad de la información. |

### Grupo 4: Gestión de consultas, reclamos y política interna

| Literal | Deber |
|---------|-------|
| **j)** | Tramitar consultas y reclamos en los términos del Título V (Art. 14-15). |
| **k)** | **Adoptar un manual interno de políticas y procedimientos** para garantizar el cumplimiento de la ley y la atención de consultas y reclamos. ← **Pregunta 1 del diagnóstico** |
| **l)** | Informar al Encargado cuando una información está en discusión por el Titular (reclamo en curso). |
| **m)** | Informar al Titular sobre el uso dado a sus datos, a solicitud de este. |

### Grupo 5: Cumplimiento regulatorio

| Literal | Deber |
|---------|-------|
| **n)** | **Informar a la SIC** cuando se presenten violaciones a los códigos de seguridad y existan riesgos en la administración de la información. |
| **o)** | Cumplir instrucciones y requerimientos de la Superintendencia de Industria y Comercio. |

---

## Artículo 18 — Deberes del Encargado del Tratamiento

El Encargado (quien procesa datos por cuenta del Responsable) debe:

| Literal | Deber |
|---------|-------|
| **a)** | Garantizar al Titular el pleno y efectivo ejercicio del habeas data. |
| **b)** | Conservar la información bajo **condiciones de seguridad** para impedir adulteración, pérdida o acceso no autorizado. |
| **c)** | Realizar oportunamente la **actualización, rectificación o supresión** de los datos. |
| **d)** | Actualizar la información reportada por los Responsables dentro de los **5 días hábiles** contados desde su recibo. |
| **e)** | Tramitar consultas y reclamos en los términos de la ley. |
| **f)** | Adoptar un **manual interno de políticas y procedimientos** para el cumplimiento de la ley. |
| **g)** | Registrar en la base de datos la leyenda **"reclamo en trámite"** según lo regula la ley. |
| **h)** | Insertar la leyenda **"información en discusión judicial"** cuando sea notificado por la autoridad competente. |
| **i)** | Abstenerse de circular información cuyo bloqueo haya sido ordenado por la SIC. |
| **j)** | Permitir el acceso a la información **únicamente a las personas que pueden tener acceso a ella**. |
| **k)** | Informar a la SIC sobre violaciones a los códigos de seguridad. |
| **l)** | Cumplir instrucciones y requerimientos de la SIC. |

> **Parágrafo:** Si una misma persona es tanto Responsable como Encargado, debe cumplir los deberes de **ambos roles**.

---

## Mapeo directo con el diagnóstico Cavaltec (Bloques 2 y 3)

### Bloque 2 — Privacidad desde el diseño (máx. 36%)

```
Pregunta 6: ¿Incorpora evaluaciones de impacto (PIA)?
  → Base: Art. 17(d) — deber de seguridad como parte del diseño
  → Un PIA es la herramienta técnica para cumplir el deber de seguridad antes de operar

Pregunta 7: ¿Aplica técnicas de minimización de datos?
  → Base: Art. 4(b) finalidad + Art. 4(d) veracidad/calidad
  → Solo recolectar lo estrictamente necesario para la finalidad declarada

Pregunta 8: ¿Configura sistemas para recopilar el mínimo de datos por defecto?
  → Base: Art. 17(d) y (e) — seguridad y calidad desde el diseño técnico
  → "Privacy by Default": la configuración predeterminada debe ser la más privada
```

### Bloque 3 — Gobernanza (máx. 24%)

```
Pregunta 9: ¿Cuenta con sistema de administración de riesgos?
  → Base: Art. 17(d) — medidas técnicas, humanas y administrativas de seguridad
  → Art. 17(n) — deber de informar a la SIC sobre violaciones de seguridad
  → Implica tener identificados, medidos y mitigados los riesgos de tratamiento

Pregunta 10: ¿Cuenta con oficial de protección de datos?
  → Base: Art. 17(k) — adoptar manual interno y política de datos
  → Implica designar a alguien responsable de ese manual y su cumplimiento
  → Aunque la ley no nombra explícitamente "oficial de datos", la responsabilidad del Art. 17 recae en una persona identificable

Pregunta 11: ¿Está designado formalmente? (complementaria)
  → Verifica que el Art. 17(k) tenga un responsable nominal
  → No suma puntaje pero diferencia entre tener una política-documento y tener gobernanza real
```

---

## Recomendaciones de IA para brechas frecuentes

### Brecha: Sin manual interno de políticas (Art. 17k)

> "No contar con un **manual interno** es el incumplimiento más común y el que la SIC más investiga. Su manual debe incluir: (1) quién es el Responsable del Tratamiento, (2) qué datos se tratan y para qué, (3) cómo se obtiene y conserva la autorización, (4) cómo se atienden consultas y reclamos, y (5) las medidas de seguridad implementadas. Le recomendamos crear este documento antes de operar."

### Brecha: Sin sistema de administración de riesgos (Art. 17d)

> "Un sistema de administración de riesgos de datos personales no necesita ser complejo. Como mínimo debe incluir: (1) inventario de bases de datos y tipos de datos tratados, (2) evaluación de amenazas (pérdida, acceso no autorizado, etc.), (3) controles implementados, y (4) plan de respuesta ante incidentes. Herramientas como la **Metodología MAPA** de la SIC pueden guiar este proceso."

### Brecha: Sin oficial de protección de datos (Art. 17k)

> "Designar formalmente un **Oficial de Protección de Datos** (Data Protection Officer) es una buena práctica alineada con estándares internacionales (GDPR) y con el espíritu del Art. 17(k). Esta persona debe: conocer la normativa, supervisar el cumplimiento, ser el punto de contacto para los titulares y para la SIC, y revisar periódicamente las políticas."
