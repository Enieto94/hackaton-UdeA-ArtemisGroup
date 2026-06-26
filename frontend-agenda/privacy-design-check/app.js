const STORAGE_KEY = "cavaltec-privacy-design-check";

const questions = [
  {
    id: 1,
    block: "Política de datos personales",
    article: "Art. 12 y Art. 17(k)",
    weight: 0,
    text: "¿Cuenta con una política de tratamiento de datos personales?",
    help: "La empresa debe tener una política o manual que explique cómo trata los datos y cómo atiende derechos de titulares.",
    evidence: "Manual interno, política publicada, documento aprobado o URL pública.",
    recommendation:
      "Crear y aprobar una política de tratamiento con responsable, finalidades, derechos, canales, tiempos de respuesta y medidas de seguridad."
  },
  {
    id: 2,
    block: "Política de datos personales",
    article: "Art. 11 y Art. 17(k)",
    weight: 10,
    parent: 1,
    text: "¿La política está documentada y publicada en medio de fácil acceso?",
    help: "La política debe poder consultarse sin barreras técnicas por titulares y usuarios.",
    evidence: "URL pública, intranet accesible, documento entregable o aviso de privacidad visible.",
    recommendation:
      "Publicar la política en un canal estable y fácil de consultar, con versión, fecha de actualización y datos de contacto."
  },
  {
    id: 3,
    block: "Política de datos personales",
    article: "Art. 4(b) y Art. 12(a)",
    weight: 10,
    parent: 1,
    text: "¿Define las finalidades del tratamiento de datos?",
    help: "Cada dato recolectado debe tener un propósito legítimo, específico e informado al titular.",
    evidence: "Inventario de finalidades por formulario, proceso, base de datos o sistema.",
    recommendation:
      "Mapear datos contra finalidades específicas y eliminar usos genéricos como 'fines comerciales' sin explicación concreta."
  },
  {
    id: 4,
    block: "Política de datos personales",
    article: "Art. 8 y Art. 12(c)",
    weight: 10,
    parent: 1,
    text: "¿Incluye los derechos de los titulares?",
    help: "La política debe mencionar acceso, actualización, rectificación, revocación, supresión, prueba de autorización y quejas ante la SIC.",
    evidence: "Sección de derechos del titular dentro de la política.",
    recommendation:
      "Agregar una sección clara con derechos de titulares y referencias a acceso, actualización, rectificación, supresión y revocatoria."
  },
  {
    id: 5,
    block: "Política de datos personales",
    article: "Art. 8, Art. 14 y Art. 15",
    weight: 10,
    parent: 1,
    text: "¿Menciona cómo ejercer los derechos de los titulares?",
    help: "Debe existir un canal y procedimiento para consultas, reclamos, rectificaciones y supresiones.",
    evidence: "Correo, formulario, responsable, tiempos de respuesta y procedimiento documentado.",
    recommendation:
      "Definir canales, responsable, plazos, requisitos mínimos y trazabilidad para consultas y reclamos de titulares."
  },
  {
    id: 6,
    block: "Privacidad desde el diseño",
    article: "Art. 17(d)",
    weight: 12,
    text: "¿Incorpora evaluaciones de impacto en privacidad?",
    help: "Una evaluación de impacto permite identificar riesgos antes de lanzar un producto, proceso o sistema.",
    evidence: "Plantilla PIA/DPIA, matriz de riesgos, actas de revisión o aprobación previa al despliegue.",
    recommendation:
      "Adoptar una evaluación de impacto para proyectos que recolecten datos personales, con riesgos, controles y responsables."
  },
  {
    id: 7,
    block: "Privacidad desde el diseño",
    article: "Art. 4(b) y Art. 4(d)",
    weight: 12,
    text: "¿Aplica técnicas de minimización de datos?",
    help: "Solo deben recolectarse los datos estrictamente necesarios para la finalidad declarada.",
    evidence: "Inventario de campos, justificación por dato, formularios reducidos o reglas de retención.",
    recommendation:
      "Revisar formularios y bases de datos para retirar campos innecesarios, opcionales no justificados y duplicados."
  },
  {
    id: 8,
    block: "Privacidad desde el diseño",
    article: "Art. 4(b), Art. 4(f) y Art. 17(d)",
    weight: 12,
    text: "¿Configura sus sistemas para recopilar el mínimo de datos por defecto?",
    help: "La configuración inicial debe ser la más protectora, sin casillas premarcadas ni permisos excesivos.",
    evidence: "Configuraciones por defecto, revisiones de formularios, controles de permisos o políticas de retención.",
    recommendation:
      "Aplicar privacidad por defecto: campos mínimos, permisos limitados, consentimiento explícito y retención reducida."
  },
  {
    id: 9,
    block: "Gobernanza",
    article: "Art. 17(d) y Art. 17(n)",
    weight: 16,
    text: "¿Cuenta con un sistema de administración de riesgos?",
    help: "La empresa debe identificar, medir y mitigar riesgos de pérdida, acceso no autorizado o uso indebido de datos.",
    evidence: "Matriz de riesgos, inventario de activos, plan de tratamiento, controles y respuesta a incidentes.",
    recommendation:
      "Implementar un sistema mínimo de riesgos con inventario, amenazas, controles, responsables, revisión periódica y plan de incidentes."
  },
  {
    id: 10,
    block: "Gobernanza",
    article: "Art. 17(k)",
    weight: 8,
    text: "¿Cuenta con un oficial de protección de datos personales?",
    help: "Debe existir una persona responsable de coordinar cumplimiento, políticas y atención a titulares.",
    evidence: "Rol asignado, funciones, correo de contacto, acta o nombramiento interno.",
    recommendation:
      "Asignar un responsable de protección de datos con funciones, autoridad, canal de contacto y revisión periódica de cumplimiento."
  },
  {
    id: 11,
    block: "Gobernanza",
    article: "Art. 17(k)",
    weight: 0,
    dependsOn: 10,
    complementary: true,
    text: "¿El oficial de protección de datos está designado formalmente?",
    help: "La designación formal evidencia gobernanza real y evita que el cumplimiento dependa de acuerdos informales.",
    evidence: "Acta, resolución interna, contrato, manual de funciones o comunicación oficial.",
    recommendation:
      "Formalizar el nombramiento con alcance, responsabilidades, suplencia, independencia operativa y canal de atención."
  }
];

const state = loadState();

const elements = {
  sessionStatus: document.querySelector("#sessionStatus"),
  scoreValue: document.querySelector("#scoreValue"),
  scoreLevel: document.querySelector("#scoreLevel"),
  scorePolicy: document.querySelector("#scorePolicy"),
  scoreDesign: document.querySelector("#scoreDesign"),
  scoreGovernance: document.querySelector("#scoreGovernance"),
  gaugeFill: document.querySelector("#gaugeFill"),
  questionsContainer: document.querySelector("#questionsContainer"),
  gapsList: document.querySelector("#gapsList"),
  planList: document.querySelector("#planList"),
  chatMessages: document.querySelector("#chatMessages"),
  chatForm: document.querySelector("#chatForm"),
  chatInput: document.querySelector("#chatInput"),
  chatQuestion: document.querySelector("#chatQuestion"),
  resetButton: document.querySelector("#resetButton"),
  companyName: document.querySelector("#companyName"),
  companyNit: document.querySelector("#companyNit"),
  companySector: document.querySelector("#companySector"),
  companySize: document.querySelector("#companySize")
};

init();

function init() {
  bindSessionButtons();
  bindCompanyFields();
  bindChat();
  bindReset();
  renderQuestionOptions();
  renderQuestions();
  renderSession();
  renderCompany();
  renderResults();

  if (state.chat.length === 0) {
    addAssistantMessage(
      "Seleccione una pregunta y consulte por criterio, evidencia o recomendación. Las respuestas usan el mapeo local de la Ley 1581 incluido en el repositorio."
    );
  } else {
    renderChat();
  }
}

function defaultState() {
  return {
    session: null,
    company: {
      name: "",
      nit: "",
      sector: "",
      size: ""
    },
    answers: {},
    evidence: {},
    chat: []
  };
}

function loadState() {
  try {
    const saved = JSON.parse(localStorage.getItem(STORAGE_KEY));
    return { ...defaultState(), ...saved };
  } catch {
    return defaultState();
  }
}

function saveState() {
  localStorage.setItem(STORAGE_KEY, JSON.stringify(state));
}

function bindSessionButtons() {
  document.querySelectorAll(".oauth-button").forEach((button) => {
    button.addEventListener("click", () => {
      const provider = button.dataset.provider;
      state.session = {
        provider,
        email: `usuario@${provider.toLowerCase()}.oauth`,
        role: "evaluador",
        startedAt: new Date().toISOString()
      };
      saveState();
      renderSession();
    });
  });
}

function bindCompanyFields() {
  const fieldMap = {
    companyName: "name",
    companyNit: "nit",
    companySector: "sector",
    companySize: "size"
  };

  Object.entries(fieldMap).forEach(([elementId, stateKey]) => {
    const input = document.querySelector(`#${elementId}`);
    input.addEventListener("input", () => {
      state.company[stateKey] = input.value.trim();
      saveState();
    });
  });
}

function bindChat() {
  elements.chatForm.addEventListener("submit", (event) => {
    event.preventDefault();
    const text = elements.chatInput.value.trim();
    if (!text) return;

    const question = getQuestion(Number(elements.chatQuestion.value));
    addUserMessage(text);
    addAssistantMessage(buildAssistantReply(text, question));
    elements.chatInput.value = "";
  });

  document.querySelectorAll(".quick-action").forEach((button) => {
    button.addEventListener("click", () => {
      const question = getQuestion(Number(elements.chatQuestion.value));
      const prompt = button.dataset.prompt;
      addUserMessage(prompt);
      addAssistantMessage(buildAssistantReply(prompt, question));
    });
  });
}

function bindReset() {
  elements.resetButton.addEventListener("click", () => {
    localStorage.removeItem(STORAGE_KEY);
    Object.assign(state, defaultState());
    renderSession();
    renderCompany();
    renderQuestions();
    renderResults();
    renderChat();
    addAssistantMessage(
      "Diagnóstico reiniciado. Seleccione una pregunta para consultar criterios y evidencias."
    );
  });
}

function renderSession() {
  if (!state.session) {
    elements.sessionStatus.textContent = "Sin sesión iniciada";
    return;
  }

  elements.sessionStatus.textContent = `${state.session.provider} · ${state.session.role}`;
}

function renderCompany() {
  elements.companyName.value = state.company.name;
  elements.companyNit.value = state.company.nit;
  elements.companySector.value = state.company.sector;
  elements.companySize.value = state.company.size;
}

function renderQuestionOptions() {
  elements.chatQuestion.textContent = "";
  questions.forEach((question) => {
    const option = document.createElement("option");
    option.value = question.id;
    option.textContent = `P${question.id} · ${question.block}`;
    elements.chatQuestion.append(option);
  });
}

function renderQuestions() {
  elements.questionsContainer.textContent = "";

  questions.forEach((question) => {
    if (question.dependsOn && state.answers[question.dependsOn] !== true) {
      state.answers[question.id] = null;
      return;
    }

    const disabled = isQuestionDisabled(question);
    const card = document.createElement("article");
    card.className = `question-card${disabled ? " is-disabled" : ""}`;

    const content = document.createElement("div");

    const meta = document.createElement("div");
    meta.className = "question-meta";
    meta.append(createPill(`P${question.id}`));
    meta.append(createPill(question.block));
    meta.append(createPill(question.weight ? `${question.weight}%` : "Complementaria", "weight"));
    meta.append(createPill(question.article));

    const title = document.createElement("h3");
    title.className = "question-title";
    title.textContent = question.text;

    const help = document.createElement("p");
    help.className = "question-help";
    help.textContent = question.help;

    const evidenceLabel = document.createElement("label");
    evidenceLabel.className = "evidence-field";
    evidenceLabel.textContent = "Evidencia o comentario";

    const evidence = document.createElement("textarea");
    evidence.value = state.evidence[question.id] || "";
    evidence.disabled = disabled;
    evidence.placeholder = question.evidence;
    evidence.addEventListener("input", () => {
      state.evidence[question.id] = evidence.value.trim();
      saveState();
    });
    evidenceLabel.append(evidence);

    content.append(meta, title, help, evidenceLabel);

    const answerGroup = document.createElement("div");
    answerGroup.className = "answer-group";
    answerGroup.setAttribute("role", "group");
    answerGroup.setAttribute("aria-label", `Respuesta pregunta ${question.id}`);

    const yesButton = createAnswerButton("Sí", "yes", question, disabled);
    const noButton = createAnswerButton("No", "no", question, disabled);
    answerGroup.append(yesButton, noButton);

    card.append(content, answerGroup);
    elements.questionsContainer.append(card);
  });
}

function createPill(text, extraClass = "") {
  const pill = document.createElement("span");
  pill.className = `pill ${extraClass}`.trim();
  pill.textContent = text;
  return pill;
}

function createAnswerButton(label, value, question, disabled) {
  const button = document.createElement("button");
  button.type = "button";
  button.className = "answer-button";
  button.dataset.value = value;
  button.textContent = label;
  button.disabled = disabled;

  const expected = value === "yes";
  if (state.answers[question.id] === expected) {
    button.classList.add("is-selected");
  }

  button.addEventListener("click", () => {
    state.answers[question.id] = expected;

    if (question.id === 1 && expected === false) {
      [2, 3, 4, 5].forEach((id) => {
        state.answers[id] = null;
      });
    }

    if (question.id === 10 && expected === false) {
      state.answers[11] = null;
    }

    saveState();
    renderQuestions();
    renderResults();
  });

  return button;
}

function isQuestionDisabled(question) {
  return Boolean(question.parent && state.answers[question.parent] !== true);
}

function renderResults() {
  const result = calculateResult();

  elements.scoreValue.textContent = `${result.total}%`;
  elements.scoreLevel.textContent = scoreLevel(result.total);
  elements.scorePolicy.textContent = `${result.blocks.policy}/40`;
  elements.scoreDesign.textContent = `${result.blocks.design}/36`;
  elements.scoreGovernance.textContent = `${result.blocks.governance}/24`;

  const color = result.total >= 75 ? "#1f8a70" : result.total >= 45 ? "#c0822c" : "#bf3f3f";
  elements.gaugeFill.style.background = `conic-gradient(${color} ${result.total * 3.6}deg, #dde4ed 0deg 360deg)`;

  renderGaps(result.gaps);
  renderPlan(result.gaps);
}

function calculateResult() {
  const blocks = {
    policy: 0,
    design: 0,
    governance: 0
  };

  const gaps = [];

  questions.forEach((question) => {
    if (question.complementary) {
      if (question.dependsOn && state.answers[question.dependsOn] === true && state.answers[question.id] === false) {
        gaps.push(question);
      }
      return;
    }

    if (question.id === 1) {
      if (state.answers[1] === false) gaps.push(question);
      return;
    }

    if (question.parent && state.answers[question.parent] !== true) {
      return;
    }

    if (state.answers[question.id] === true) {
      if (question.block === "Política de datos personales") blocks.policy += question.weight;
      if (question.block === "Privacidad desde el diseño") blocks.design += question.weight;
      if (question.block === "Gobernanza") blocks.governance += question.weight;
    }

    if (state.answers[question.id] === false) {
      gaps.push(question);
    }
  });

  const total = blocks.policy + blocks.design + blocks.governance;
  return { total, blocks, gaps };
}

function scoreLevel(score) {
  if (score >= 85) return "Alto";
  if (score >= 65) return "Adecuado";
  if (score >= 45) return "En desarrollo";
  if (score > 0) return "Crítico";
  return "Sin evaluar";
}

function renderGaps(gaps) {
  elements.gapsList.textContent = "";

  if (gaps.length === 0) {
    elements.gapsList.append(
      createResultItem("Sin brechas registradas", "Responda el cuestionario o mantenga evidencias para sustentar el resultado.", "low")
    );
    return;
  }

  gaps.forEach((question) => {
    elements.gapsList.append(
      createResultItem(
        `P${question.id} · ${question.block}`,
        `${question.text} Base: ${question.article}.`,
        severityFor(question)
      )
    );
  });
}

function renderPlan(gaps) {
  elements.planList.textContent = "";

  const ordered = [...gaps].sort((a, b) => b.weight - a.weight || a.id - b.id);

  if (ordered.length === 0) {
    elements.planList.append(
      createResultItem(
        "Mantener controles",
        "Revise evidencias periódicamente y actualice políticas cuando cambien finalidades, sistemas o encargados.",
        "low"
      )
    );
    return;
  }

  ordered.slice(0, 6).forEach((question) => {
    elements.planList.append(
      createResultItem(
        priorityLabel(question),
        question.recommendation,
        severityFor(question)
      )
    );
  });
}

function createResultItem(title, body, severity) {
  const item = document.createElement("article");
  item.className = `result-item ${severity}`;

  const strong = document.createElement("strong");
  strong.textContent = title;

  const span = document.createElement("span");
  span.textContent = body;

  item.append(strong, span);
  return item;
}

function severityFor(question) {
  if (question.weight >= 12 || question.id === 1) return "high";
  if (question.weight >= 8) return "medium";
  return "low";
}

function priorityLabel(question) {
  if (question.id === 1) return "Prioridad alta · Política base";
  if (question.weight >= 12) return `Prioridad alta · P${question.id}`;
  if (question.weight >= 8) return `Prioridad media · P${question.id}`;
  return `Prioridad complementaria · P${question.id}`;
}

function addUserMessage(text) {
  state.chat.push({ role: "user", text });
  saveState();
  renderChat();
}

function addAssistantMessage(text) {
  state.chat.push({ role: "assistant", text });
  saveState();
  renderChat();
}

function renderChat() {
  elements.chatMessages.textContent = "";

  state.chat.slice(-40).forEach((message) => {
    const bubble = document.createElement("div");
    bubble.className = `message ${message.role}`;
    bubble.textContent = message.text;
    elements.chatMessages.append(bubble);
  });

  elements.chatMessages.scrollTop = elements.chatMessages.scrollHeight;
}

function buildAssistantReply(prompt, question) {
  const lower = prompt.toLowerCase();
  const answer = state.answers[question.id];
  const answerText = answer === true ? "Sí" : answer === false ? "No" : "Sin respuesta";
  const evidence = state.evidence[question.id] || "No se ha registrado evidencia.";

  if (lower.includes("evidencia") || lower.includes("cumplimiento")) {
    return `Criterio P${question.id}: responda Sí solo si puede mostrar evidencia verificable. Evidencia esperada: ${question.evidence} Base normativa: ${question.article}. Evidencia registrada: ${evidence}`;
  }

  if (lower.includes("brecha") || lower.includes("recom")) {
    if (answer === true) {
      return `P${question.id} no aparece como brecha porque la respuesta actual es Sí. Mantenga evidencia vigente: ${question.evidence}`;
    }
    return `Brecha P${question.id}: ${question.recommendation} Esta acción se prioriza por su relación con ${question.article} y por su peso de ${question.weight || "madurez complementaria"}.`;
  }

  if (lower.includes("score") || lower.includes("resultado") || lower.includes("porcentaje")) {
    const result = calculateResult();
    return `Resultado actual: ${result.total}% (${scoreLevel(result.total)}). Política ${result.blocks.policy}/40, Privacidad desde el diseño ${result.blocks.design}/36, Gobernanza ${result.blocks.governance}/24.`;
  }

  if (lower.includes("ley") || lower.includes("art")) {
    return `La P${question.id} se fundamenta en ${question.article}. En términos prácticos: ${question.help}`;
  }

  return `P${question.id}: ${question.help} Respuesta actual: ${answerText}. Para responder correctamente, valide si existe evidencia como: ${question.evidence}`;
}

function getQuestion(id) {
  return questions.find((question) => question.id === id) || questions[0];
}
