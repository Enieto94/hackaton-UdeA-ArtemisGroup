# Ley 1581/2012 — Módulo 7: Vigilancia, Sanciones y Registro Nacional de Bases de Datos

> **Artículos:** 19, 20, 21, 22, 23, 24, 25 | **Título:** VII (Capítulos I, II, III)
> **Relevancia Cavaltec:** Contexto regulatorio para dimensionar el riesgo de incumplimiento. El módulo de resultados del diagnóstico debe comunicar las consecuencias potenciales de cada brecha identificada.

---

## CAPÍTULO I — Autoridad de Protección de Datos

### Artículo 19 — Autoridad competente: La SIC

La **Superintendencia de Industria y Comercio (SIC)**, a través de su **Delegatura para la Protección de Datos Personales**, es la autoridad de vigilancia y control.

> **Parágrafo 1:** El Gobierno debía incorporar un despacho de Superintendente Delegado dentro de los 6 meses de entrada en vigencia.
> **Parágrafo 2:** Los datos regulados por la Ley 1266 de 2008 (datos financieros) siguen las reglas de esa norma específica.

### Artículo 20 — Recursos de la SIC

La SIC cuenta con recursos del Presupuesto General de la Nación para ejercer sus funciones.

### Artículo 21 — Funciones de la SIC

| # | Función |
|---|---------|
| a | Velar por el cumplimiento de la legislación de protección de datos. |
| b | Adelantar investigaciones de oficio o a petición de parte y ordenar medidas (acceso, rectificación, actualización o supresión de datos). |
| c | **Bloquear temporalmente** datos cuando exista riesgo cierto de vulneración de derechos fundamentales. |
| d | Promover y divulgar los derechos de las personas en materia de datos personales. |
| e | Impartir instrucciones sobre medidas y procedimientos de adecuación. |
| f | Solicitar información a Responsables y Encargados. |
| g | **Proferir declaraciones de conformidad** sobre transferencias internacionales de datos. |
| h | Administrar el **Registro Nacional Público de Bases de Datos**. |
| i | Sugerir ajustes normativos ante cambios tecnológicos. |
| j | Requerir colaboración de entidades internacionales cuando se afecten titulares fuera del territorio colombiano. |
| k | Las demás que le asigne la ley. |

---

## CAPÍTULO II — Procedimiento y Sanciones

### Artículo 22 — Trámite

Una vez la SIC establece el incumplimiento, adoptará medidas o impondrá sanciones. En lo no reglado, se aplica el Código Contencioso Administrativo.

### Artículo 23 — Sanciones (solo para entidades privadas)

| Tipo de sanción | Descripción |
|-----------------|-------------|
| **a) Multa personal e institucional** | Hasta **2.000 salarios mínimos mensuales legales vigentes (SMMLV)**. Pueden ser **sucesivas** mientras subsista el incumplimiento. |
| **b) Suspensión temporal** | Suspensión de actividades relacionadas con el Tratamiento hasta por **6 meses**. El acto indica correctivos a adoptar. |
| **c) Cierre temporal** | Cierre de operaciones una vez transcurrido el período de suspensión sin haber adoptado los correctivos. |
| **d) Cierre inmediato y definitivo** | Para operaciones que involucren **tratamiento de datos sensibles** (la sanción más grave). |

> **Parágrafo:** Las sanciones aplican solo a personas **privadas**. Si la SIC detecta incumplimiento de una **autoridad pública**, remite la actuación a la **Procuraduría General de la Nación**.

### Referencia económica (multas)

```
2.000 SMMLV =  2.000 × [salario mínimo vigente al momento de la sanción]
               (en 2024: ~$1.300.000 COP/mes → multa hasta ~$2.600.000.000 COP)
```

### Artículo 24 — Criterios para graduar las sanciones

La SIC gradúa las sanciones considerando:

| Criterio | Descripción |
|----------|-------------|
| **a)** | Dimensión del daño o peligro a los intereses jurídicos tutelados. |
| **b)** | Beneficio económico obtenido por el infractor o terceros. |
| **c)** | Reincidencia en la comisión de la infracción. |
| **d)** | Resistencia, negativa u obstrucción a la investigación de la SIC. |
| **e)** | Renuencia o desacato a cumplir órdenes de la SIC. |
| **f)** | Reconocimiento o aceptación expresa de la infracción antes de la sanción (**factor atenuante**). |

---

## CAPÍTULO III — Registro Nacional de Bases de Datos

### Artículo 25 — Definición y obligaciones

El **Registro Nacional de Bases de Datos (RNBD)** es el directorio público de bases de datos sujetas a tratamiento que operan en Colombia.

| Característica | Descripción |
|----------------|-------------|
| **Administrador** | Superintendencia de Industria y Comercio |
| **Acceso** | De libre consulta para los ciudadanos |
| **Obligación** | Los Responsables deben registrar sus bases de datos ante la SIC |
| **Requisito** | Aportar las políticas de tratamiento de la información |
| **Consecuencia de incumplimiento** | Las sanciones del Art. 23 |

> **Parágrafo:** El Gobierno debía reglamentar el RNBD dentro del año siguiente a la promulgación (→ Decreto 886 de 2014).

> **Nota:** Las políticas de tratamiento registradas **no pueden ser inferiores** a los deberes contenidos en la ley.

---

## Tabla de riesgo regulatorio para el diagnóstico

Esta tabla puede usarse para comunicar al usuario las consecuencias de cada brecha:

| Brecha detectada | Artículo incumplido | Sanción potencial | Severidad |
|-----------------|---------------------|-------------------|-----------|
| Sin política de tratamiento | Art. 17(k) | Multa hasta 2.000 SMMLV | 🔴 Alta |
| Sin autorización del titular | Art. 9 | Multa hasta 2.000 SMMLV + suspensión | 🔴 Alta |
| Tratamiento de datos sensibles sin excepción | Art. 6 | Cierre inmediato y definitivo | 🔴🔴 Crítica |
| Sin canales para ejercer derechos | Art. 14-15 | Multa + medidas de la SIC | 🟠 Media-Alta |
| Sin manual interno de procedimientos | Art. 17(k) | Multa | 🟠 Media-Alta |
| Sin registro ante la SIC (RNBD) | Art. 25 | Multa | 🟡 Media |
| Sin medidas de seguridad | Art. 17(d) | Multa hasta 2.000 SMMLV | 🔴 Alta |
| Transferencia internacional no autorizada | Art. 26 | Multa + bloqueo | 🔴 Alta |

---

## Texto sugerido para el módulo de resultados de Cavaltec

```
NIVEL CRÍTICO (0-40%):
"Su organización presenta brechas estructurales que la exponen a sanciones graves 
por parte de la SIC, incluyendo multas de hasta 2.000 SMMLV (~$2.600 millones COP) 
y posible cierre de operaciones. Se recomienda acción inmediata."

NIVEL BAJO (41-60%):
"Su organización tiene elementos básicos de cumplimiento pero presenta brechas 
significativas que generan riesgo legal. La SIC puede iniciar investigación de oficio 
ante incidentes o denuncias de titulares."

NIVEL MEDIO (61-80%):
"Su organización está en camino al cumplimiento. Las brechas identificadas son 
subsanables en el corto plazo y reducen el riesgo regulatorio significativamente."

NIVEL ALTO (81-100%):
"Su organización demuestra un alto nivel de madurez en protección de datos. 
Se recomienda mantener el programa activo, realizar auditorías periódicas y 
capacitar continuamente al equipo."
```
