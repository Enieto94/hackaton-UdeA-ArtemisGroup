export const BLOCKS = {
  policy: {
    key: 'policy',
    label: 'Política de datos personales',
    shortLabel: 'Política',
    max: 40
  },
  design: {
    key: 'design',
    label: 'Privacidad desde el diseño',
    shortLabel: 'Diseño',
    max: 36
  },
  governance: {
    key: 'governance',
    label: 'Gobernanza',
    shortLabel: 'Gobernanza',
    max: 24
  }
}

export const QUESTIONS = [
  {
    id: 1,
    blockKey: 'policy',
    article: 'Art. 12 y Art. 17(k)',
    weight: 0,
    text: '¿Cuenta con una política de tratamiento de datos personales?',
    help: 'La empresa debe tener una política o manual que explique cómo trata los datos y cómo atiende derechos de titulares.',
    evidence: 'Manual interno, política publicada, documento aprobado o URL pública.',
    recommendation:
      'Crear y aprobar una política de tratamiento con responsable, finalidades, derechos, canales, tiempos de respuesta y medidas de seguridad.'
  },
  {
    id: 2,
    blockKey: 'policy',
    article: 'Art. 11 y Art. 17(k)',
    weight: 10,
    parent: 1,
    text: '¿La política está documentada y publicada en medio de fácil acceso?',
    help: 'La política debe poder consultarse sin barreras técnicas por titulares y usuarios.',
    evidence: 'URL pública, intranet accesible, documento entregable o aviso de privacidad visible.',
    recommendation:
      'Publicar la política en un canal estable y fácil de consultar, con versión, fecha de actualización y datos de contacto.'
  },
  {
    id: 3,
    blockKey: 'policy',
    article: 'Art. 4(b) y Art. 12(a)',
    weight: 10,
    parent: 1,
    text: '¿Define las finalidades del tratamiento de datos?',
    help: 'Cada dato recolectado debe tener un propósito legítimo, específico e informado al titular.',
    evidence: 'Inventario de finalidades por formulario, proceso, base de datos o sistema.',
    recommendation:
      'Mapear datos contra finalidades específicas y eliminar usos genéricos como "fines comerciales" sin explicación concreta.'
  },
  {
    id: 4,
    blockKey: 'policy',
    article: 'Art. 8 y Art. 12(c)',
    weight: 10,
    parent: 1,
    text: '¿Incluye los derechos de los titulares?',
    help: 'La política debe mencionar acceso, actualización, rectificación, revocación, supresión, prueba de autorización y quejas ante la SIC.',
    evidence: 'Sección de derechos del titular dentro de la política.',
    recommendation:
      'Agregar una sección clara con derechos de titulares y referencias a acceso, actualización, rectificación, supresión y revocatoria.'
  },
  {
    id: 5,
    blockKey: 'policy',
    article: 'Art. 8, Art. 14 y Art. 15',
    weight: 10,
    parent: 1,
    text: '¿Menciona cómo ejercer los derechos de los titulares?',
    help: 'Debe existir un canal y procedimiento para consultas, reclamos, rectificaciones y supresiones.',
    evidence: 'Correo, formulario, responsable, tiempos de respuesta y procedimiento documentado.',
    recommendation:
      'Definir canales, responsable, plazos, requisitos mínimos y trazabilidad para consultas y reclamos de titulares.'
  },
  {
    id: 6,
    blockKey: 'design',
    article: 'Art. 17(d)',
    weight: 12,
    text: '¿Incorpora evaluaciones de impacto en privacidad?',
    help: 'Una evaluación de impacto permite identificar riesgos antes de lanzar un producto, proceso o sistema.',
    evidence: 'Plantilla PIA/DPIA, matriz de riesgos, actas de revisión o aprobación previa al despliegue.',
    recommendation:
      'Adoptar una evaluación de impacto para proyectos que recolecten datos personales, con riesgos, controles y responsables.'
  },
  {
    id: 7,
    blockKey: 'design',
    article: 'Art. 4(b) y Art. 4(d)',
    weight: 12,
    text: '¿Aplica técnicas de minimización de datos?',
    help: 'Solo deben recolectarse los datos estrictamente necesarios para la finalidad declarada.',
    evidence: 'Inventario de campos, justificación por dato, formularios reducidos o reglas de retención.',
    recommendation:
      'Revisar formularios y bases de datos para retirar campos innecesarios, opcionales no justificados y duplicados.'
  },
  {
    id: 8,
    blockKey: 'design',
    article: 'Art. 4(b), Art. 4(f) y Art. 17(d)',
    weight: 12,
    text: '¿Configura sus sistemas para recopilar el mínimo de datos por defecto?',
    help: 'La configuración inicial debe ser la más protectora, sin casillas premarcadas ni permisos excesivos.',
    evidence: 'Configuraciones por defecto, revisiones de formularios, controles de permisos o políticas de retención.',
    recommendation:
      'Aplicar privacidad por defecto: campos mínimos, permisos limitados, consentimiento explícito y retención reducida.'
  },
  {
    id: 9,
    blockKey: 'governance',
    article: 'Art. 17(d) y Art. 17(n)',
    weight: 16,
    text: '¿Cuenta con un sistema de administración de riesgos?',
    help: 'La empresa debe identificar, medir y mitigar riesgos de pérdida, acceso no autorizado o uso indebido de datos.',
    evidence: 'Matriz de riesgos, inventario de activos, plan de tratamiento, controles y respuesta a incidentes.',
    recommendation:
      'Implementar un sistema mínimo de riesgos con inventario, amenazas, controles, responsables, revisión periódica y plan de incidentes.'
  },
  {
    id: 10,
    blockKey: 'governance',
    article: 'Art. 17(k)',
    weight: 8,
    text: '¿Cuenta con un oficial de protección de datos personales?',
    help: 'Debe existir una persona responsable de coordinar cumplimiento, políticas y atención a titulares.',
    evidence: 'Rol asignado, funciones, correo de contacto, acta o nombramiento interno.',
    recommendation:
      'Asignar un responsable de protección de datos con funciones, autoridad, canal de contacto y revisión periódica de cumplimiento.'
  },
  {
    id: 11,
    blockKey: 'governance',
    article: 'Art. 17(k)',
    weight: 0,
    dependsOn: 10,
    complementary: true,
    text: '¿El oficial de protección de datos está designado formalmente?',
    help: 'La designación formal evidencia gobernanza real y evita que el cumplimiento dependa de acuerdos informales.',
    evidence: 'Acta, resolución interna, contrato, manual de funciones o comunicación oficial.',
    recommendation:
      'Formalizar el nombramiento con alcance, responsabilidades, suplencia, independencia operativa y canal de atención.'
  }
].map(question => ({
  ...question,
  block: BLOCKS[question.blockKey].label
}))

export function isQuestionDisabled(question, answers) {
  return Boolean(question.parent && answers[question.parent] !== true)
}

export function isQuestionVisible(question, answers) {
  return !question.dependsOn || answers[question.dependsOn] === true
}

export function calculateResult(answers) {
  const blocks = {
    policy: 0,
    design: 0,
    governance: 0
  }
  const gaps = []

  QUESTIONS.forEach(question => {
    if (!isQuestionVisible(question, answers)) return

    if (question.complementary) {
      if (answers[question.id] === false) gaps.push(question)
      return
    }

    if (question.id === 1) {
      if (answers[1] === false) gaps.push(question)
      return
    }

    if (isQuestionDisabled(question, answers)) return

    if (answers[question.id] === true) {
      blocks[question.blockKey] += question.weight
    }

    if (answers[question.id] === false) {
      gaps.push(question)
    }
  })

  return {
    total: blocks.policy + blocks.design + blocks.governance,
    blocks,
    gaps
  }
}

export function scoreLevel(score) {
  if (score >= 85) return 'Alto'
  if (score >= 65) return 'Adecuado'
  if (score >= 45) return 'En desarrollo'
  if (score > 0) return 'Crítico'
  return 'Sin evaluar'
}

export function severityFor(question) {
  if (question.weight >= 12 || question.id === 1) return 'high'
  if (question.weight >= 8) return 'medium'
  return 'low'
}

export function priorityLabel(question) {
  if (question.id === 1) return 'Prioridad alta · Política base'
  if (question.weight >= 12) return `Prioridad alta · P${question.id}`
  if (question.weight >= 8) return `Prioridad media · P${question.id}`
  return `Prioridad complementaria · P${question.id}`
}
