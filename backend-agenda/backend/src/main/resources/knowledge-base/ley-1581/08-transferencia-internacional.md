# Ley 1581/2012 — Módulo 8: Transferencia Internacional y Disposiciones Finales

> **Artículos:** 26, 27, 28, 29, 30 | **Título:** VIII y IX
> **Relevancia Cavaltec:** Crítico para arquitecturas multiempresa con datos en la nube (AWS, GCP, Azure) o con proveedores extranjeros. Aplica al diseño técnico de la plataforma.

---

## Artículo 26 — Transferencia de Datos a Terceros Países

### Regla general: PROHIBICIÓN

> Se **prohíbe** la transferencia de datos personales a países que no proporcionen **niveles adecuados de protección de datos**.

### ¿Cuándo un país ofrece nivel adecuado?

Cuando cumple los estándares fijados por la **SIC**, los cuales no pueden ser inferiores a los que la Ley 1581 exige.

### Excepciones a la prohibición

| # | Excepción | Requisito |
|---|-----------|-----------|
| **a)** | **Autorización expresa e inequívoca del Titular** | El titular ha dado su permiso específico para la transferencia internacional. |
| **b)** | **Datos médicos** | Cuando el tratamiento del titular lo exija por razones de salud o higiene pública. |
| **c)** | **Transferencias bancarias o bursátiles** | Conforme a la legislación aplicable al sector. |
| **d)** | **Tratados internacionales** | Transferencias en el marco de tratados donde Colombia sea parte, con principio de reciprocidad. |
| **e)** | **Ejecución de contrato** | Para ejecutar un contrato entre el Titular y el Responsable, o para medidas precontractuales, **con autorización del Titular**. |
| **f)** | **Interés público o defensa de derecho** | Cuando la ley lo exija para salvaguardar el interés público o para reconocimiento, ejercicio o defensa de un derecho en proceso judicial. |

### Proceso cuando no aplica ninguna excepción

```
Responsable del Tratamiento
  → Solicita declaración de conformidad a la SIC
  → La SIC puede requerir información y adelantar diligencias
  → SIC emite la declaración (o la niega)
  → Solo con declaración positiva se puede transferir
```

> **Parágrafo 1:** La SIC es la autoridad que emite la declaración de conformidad para transferencias no cubiertas por las excepciones.
> **Parágrafo 2:** Este artículo aplica a **todos** los datos personales, incluyendo los de la Ley 1266 de 2008.

---

## Implicaciones para arquitecturas cloud (Cavaltec)

### Escenario típico del reto

La plataforma web de Cavaltec probablemente use servicios en la nube (AWS, Google Cloud, Azure, etc.) con servidores fuera de Colombia. Esto constituye una **transferencia internacional de datos**.

### Análisis de excepciones aplicables

```
¿Los servidores están en un país con nivel adecuado según la SIC?
  → SIC mantiene lista actualizada en: https://www.sic.gov.co
  → UE (GDPR), Argentina, Uruguay tienen reconocimiento frecuente

¿Los usuarios (titulares) autorizan expresamente la transferencia?
  → Excepción a) del Art. 26
  → La política de tratamiento debe mencionarlo explícitamente
  → El formulario de autorización debe incluir este punto

¿El proveedor cloud actúa como Encargado del Tratamiento?
  → Sí → debe firmarse un contrato de encargo de tratamiento
  → El proveedor queda obligado a los deberes del Art. 18
```

### Recomendación de arquitectura para el diagnóstico

```
OPCIÓN 1 — Servidores en Colombia (más seguro normativamente)
  Usar proveedores con región en Colombia (AWS Bogotá, etc.)
  No hay transferencia internacional

OPCIÓN 2 — Servidores en país con nivel adecuado
  Verificar con la SIC
  Documentar en la política de tratamiento

OPCIÓN 3 — Autorización expresa en el onboarding
  Incluir en los términos de uso y política de privacidad
  Casilla específica para transferencia internacional
  Requisito: autorización "expresa e inequívoca"
```

---

## Artículo 27 — Normas Corporativas Vinculantes (NCV)

El Gobierno Nacional expedirá reglamentación sobre **Normas Corporativas Vinculantes** para:
- Certificar buenas prácticas en protección de datos personales.
- Certificar la transferencia a terceros países.

> Equivalente colombiano a las **Binding Corporate Rules (BCR)** del GDPR europeo. Aplica a grupos empresariales multinacionales.

---

## Artículo 28 — Régimen de Transición

Las personas que ejercían actividades reguladas a la entrada en vigencia de la ley tuvieron **6 meses** para adecuarse.

> Este artículo ya cumplió su vigencia (2012-2013). Sin embargo, su espíritu es relevante: la ley contempla períodos de gracia para la adecuación, lo que sugiere que las organizaciones deben tener **planes de implementación progresiva**.

---

## Artículo 29 — Derogatorias

La ley deroga todas las disposiciones contrarias a ella, **excepto** las contempladas en el Art. 2° (bases de datos exceptuadas).

---

## Artículo 30 — Vigencia

La ley rige a partir de su promulgación (18 de octubre de 2012).

---

## Decreto 886 de 2014 — Registro Nacional de Bases de Datos (RNBD)

> Aunque no es parte del texto de la Ley 1581, el Decreto 886 de 2014 reglamenta el Art. 25 y es esencial para el cumplimiento.

Aspectos clave:
- Los Responsables deben registrar sus bases de datos en el RNBD de la SIC.
- El registro se realiza a través del portal de la SIC.
- Se deben declarar: finalidades, tipo de datos, titulares, canales de atención, transferencias internacionales, etc.

---

## Checklist de transferencia internacional para el diagnóstico

Si la empresa usa servicios externos (cloud, CRM, ERP, etc.) que procesen datos fuera de Colombia:

```
□ ¿Se identificaron todos los proveedores que reciben datos personales?
□ ¿Se verificó si el país del proveedor tiene nivel adecuado ante la SIC?
□ ¿Se firmó contrato de encargo de tratamiento con cada proveedor?
□ ¿La política de tratamiento menciona las transferencias internacionales?
□ ¿Se obtiene autorización expresa del titular para la transferencia?
□ ¿Se tiene declaración de conformidad de la SIC (si aplica)?
```

---

## Resumen normativo completo — Ley 1581 de 2012

| Título | Artículos | Contenido |
|--------|-----------|-----------|
| I | 1-3 | Objeto, ámbito y definiciones |
| II | 4 | Principios rectores (8 principios) |
| III | 5-7 | Datos sensibles y datos de NNA |
| IV | 8-13 | Derechos del titular y condiciones de legalidad |
| V | 14-16 | Procedimientos: consultas y reclamos |
| VI | 17-18 | Deberes del Responsable y del Encargado |
| VII | 19-25 | Vigilancia (SIC), sanciones y RNBD |
| VIII | 26 | Transferencia internacional de datos |
| IX | 27-30 | NCV, transición, derogatorias y vigencia |
