const STORAGE_KEY = "cavaltec-privacy-design-check";

const CONFIG = {
  apiBaseUrl: window.CAVALTEC_API_URL || "http://localhost:8080",
  googleClientId: window.CAVALTEC_GOOGLE_CLIENT_ID || "",
  microsoftClientId: window.CAVALTEC_MICROSOFT_CLIENT_ID || ""
};

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
  authView: document.querySelector("#authView"),
  appShell: document.querySelector("#appShell"),
  loginTab: document.querySelector("#loginTab"),
  registerTab: document.querySelector("#registerTab"),
  loginForm: document.querySelector("#loginForm"),
  registerForm: document.querySelector("#registerForm"),
  authMessage: document.querySelector("#authMessage"),
  sessionStatus: document.querySelector("#sessionStatus"),
  logoutButton: document.querySelector("#logoutButton"),
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
  saveDraftButton: document.querySelector("#saveDraftButton"),
  completeButton: document.querySelector("#completeButton"),
  companyName: document.querySelector("#companyName"),
  companyNit: document.querySelector("#companyNit"),
  companySector: document.querySelector("#companySector"),
  companySize: document.querySelector("#companySize")
};

init();

function init() {
  bindAuth();
  bindCompanyFields();
  bindChat();
  bindReset();
  bindPersistence();
  renderQuestionOptions();
  renderAppGate();
  renderSession();
  renderCompany();
  renderQuestions();
  renderResults();

  if (state.session) {
    loadLatestEvaluation();
  }

  if (state.chat.length === 0) {
    addAssistantMessage(
      "Seleccione una pregunta y consulte por criterio, evidencia o recomendación. Las respuestas se envían al backend con contexto normativo de Ley 1581."
    );
  } else {
    renderChat();
  }
}

function defaultState() {
  return {
    session: null,
    activeEvaluationId: null,
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

function bindAuth() {
  elements.loginTab.addEventListener("click", () => setAuthMode("login"));
  elements.registerTab.addEventListener("click", () => setAuthMode("register"));

  elements.loginForm.addEventListener("submit", async (event) => {
    event.preventDefault();
    await authenticate("/auth/login", {
      email: document.querySelector("#loginEmail").value.trim(),
      password: document.querySelector("#loginPassword").value
    });
  });

  elements.registerForm.addEventListener("submit", async (event) => {
    event.preventDefault();
    const company = readRegisterCompany();
    const authenticated = await authenticate("/auth/register", {
      nombre: document.querySelector("#registerName").value.trim(),
      email: document.querySelector("#registerEmail").value.trim(),
      password: document.querySelector("#registerPassword").value,
      role: "EVALUADOR",
      empresa: company
    });
    if (authenticated) {
      syncCompanyFromRegistration(company);
    }
  });

  document.querySelectorAll(".oauth-button").forEach((button) => {
    button.addEventListener("click", () => startOAuth(button.dataset.provider));
  });

  elements.logoutButton.addEventListener("click", () => {
    state.session = null;
    saveState();
    renderAppGate();
    renderSession();
  });
}

function setAuthMode(mode) {
  const isRegister = mode === "register";
  elements.loginTab.classList.toggle("is-active", !isRegister);
  elements.registerTab.classList.toggle("is-active", isRegister);
  elements.loginForm.classList.toggle("is-hidden", isRegister);
  elements.registerForm.classList.toggle("is-hidden", !isRegister);
  elements.authMessage.textContent = "";
}

async function authenticate(path, payload) {
  try {
    setAuthMessage("Validando credenciales...");
    const response = await apiFetch(path, {
      method: "POST",
      body: JSON.stringify(payload),
      auth: false
    });
    setSession(response);
    setAuthMessage("");
    await loadLatestEvaluation();
    return true;
  } catch (error) {
    setAuthMessage(error.message);
    return false;
  }
}

async function startOAuth(provider) {
  const isRegister = !elements.registerForm.classList.contains("is-hidden");
  const empresa = isRegister ? readRegisterCompany() : null;

  if (provider === "Google") {
    await startGoogleOAuth(empresa);
    return;
  }

  await startMicrosoftOAuth(empresa);
}

async function startGoogleOAuth(empresa) {
  if (!CONFIG.googleClientId) {
    setAuthMessage("Configure window.CAVALTEC_GOOGLE_CLIENT_ID para usar Google OAuth.");
    return;
  }

  if (!window.google?.accounts?.id) {
    setAuthMessage("El SDK de Google Identity Services no está disponible.");
    return;
  }

  window.google.accounts.id.initialize({
    client_id: CONFIG.googleClientId,
    callback: async (response) => {
      const authenticated = await authenticate("/auth/google", { token: response.credential, empresa });
      if (authenticated && empresa) syncCompanyFromRegistration(empresa);
    }
  });
  window.google.accounts.id.prompt();
}

async function startMicrosoftOAuth(empresa) {
  if (!CONFIG.microsoftClientId) {
    setAuthMessage("Configure window.CAVALTEC_MICROSOFT_CLIENT_ID para usar Microsoft OAuth.");
    return;
  }

  if (!window.msal) {
    setAuthMessage("MSAL no está disponible.");
    return;
  }

  try {
    const client = new window.msal.PublicClientApplication({
      auth: {
        clientId: CONFIG.microsoftClientId,
        authority: "https://login.microsoftonline.com/common",
        redirectUri: window.location.origin + window.location.pathname
      }
    });
    const result = await client.loginPopup({ scopes: ["User.Read"] });
    const authenticated = await authenticate("/auth/microsoft", { token: result.accessToken, empresa });
    if (authenticated && empresa) syncCompanyFromRegistration(empresa);
  } catch (error) {
    setAuthMessage(error.message || "No se pudo iniciar sesión con Microsoft.");
  }
}

function readRegisterCompany() {
  return {
    nombre: document.querySelector("#registerCompanyName").value.trim(),
    nit: document.querySelector("#registerCompanyNit").value.trim(),
    sector: document.querySelector("#registerCompanySector").value,
    tamano: document.querySelector("#registerCompanySize").value
  };
}

function syncCompanyFromRegistration(company) {
  state.company = {
    name: company.nombre,
    nit: company.nit,
    sector: company.sector,
    size: company.tamano
  };
  saveState();
  renderCompany();
}

function setSession(response) {
  state.session = {
    token: response.token,
    role: response.role,
    email: response.email,
    nombre: response.nombre,
    empresaId: response.empresaId,
    startedAt: new Date().toISOString()
  };
  saveState();
  renderAppGate();
  renderSession();
}

function setAuthMessage(message) {
  elements.authMessage.textContent = message;
}

function renderAppGate() {
  const authenticated = Boolean(state.session?.token);
  elements.authView.classList.toggle("is-hidden", authenticated);
  elements.appShell.classList.toggle("is-hidden", !authenticated);
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
  elements.chatForm.addEventListener("submit", async (event) => {
    event.preventDefault();
    const text = elements.chatInput.value.trim();
    if (!text) return;

    const question = getQuestion(Number(elements.chatQuestion.value));
    addUserMessage(text);
    elements.chatInput.value = "";
    await requestAssistantReply(text, question);
  });

  document.querySelectorAll(".quick-action").forEach((button) => {
    button.addEventListener("click", async () => {
      const question = getQuestion(Number(elements.chatQuestion.value));
      const prompt = button.dataset.prompt;
      addUserMessage(prompt);
      await requestAssistantReply(prompt, question);
    });
  });
}

async function requestAssistantReply(text, question) {
  try {
    const response = await apiFetch("/chat/diagnostico", {
      method: "POST",
      body: JSON.stringify({
        mensaje: text,
        preguntaNumero: question.id,
        respuesta: state.answers[question.id] ?? null,
        evidencia: state.evidence[question.id] || ""
      })
    });
    addAssistantMessage(response.respuesta);
  } catch (error) {
    addAssistantMessage(`No se pudo consultar el backend: ${error.message}`);
  }
}

function bindReset() {
  elements.resetButton.addEventListener("click", () => {
    state.activeEvaluationId = null;
    state.answers = {};
    state.evidence = {};
    state.chat = [];
    saveState();
    renderQuestions();
    renderResults();
    renderChat();
    addAssistantMessage("Diagnóstico reiniciado. Seleccione una pregunta para consultar criterios y evidencias.");
  });
}

function bindPersistence() {
  elements.saveDraftButton.addEventListener("click", () => saveEvaluation(false));
  elements.completeButton.addEventListener("click", () => saveEvaluation(true));
}

async function saveEvaluation(completar) {
  try {
    const payload = {
      completar,
      respuestas: questions.map((question) => ({
        preguntaNumero: question.id,
        respuesta: state.answers[question.id] ?? null,
        evidencia: state.evidence[question.id] || ""
      }))
    };

    const path = state.activeEvaluationId ? `/evaluaciones/${state.activeEvaluationId}` : "/evaluaciones";
    const method = state.activeEvaluationId ? "PUT" : "POST";
    const response = await apiFetch(path, { method, body: JSON.stringify(payload) });
    applyEvaluationResponse(response);
    addAssistantMessage(completar ? "Evaluación completada y guardada en el backend." : "Borrador guardado en el backend.");
  } catch (error) {
    addAssistantMessage(`No se pudo guardar la evaluación: ${error.message}`);
  }
}

async function loadLatestEvaluation() {
  if (!state.session?.token) return;

  try {
    const evaluations = await apiFetch("/evaluaciones");
    if (evaluations.length > 0) {
      applyEvaluationResponse(evaluations[0]);
    }
  } catch {
    // El usuario puede estar recién registrado; no hay histórico que cargar.
  }
}

function applyEvaluationResponse(evaluation) {
  state.activeEvaluationId = evaluation.id;
  evaluation.respuestas?.forEach((item) => {
    state.answers[item.preguntaNumero] = item.respuesta;
    state.evidence[item.preguntaNumero] = item.evidencia || "";
  });
  saveState();
  renderQuestions();
  renderResults();
}

function renderSession() {
  if (!state.session) {
    elements.sessionStatus.textContent = "Sin sesión iniciada";
    return;
  }

  const role = state.session.role?.toLowerCase() || "evaluador";
  elements.sessionStatus.textContent = `${state.session.email || state.session.nombre} · ${role}`;
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
    answerGroup.append(
      createAnswerButton("Sí", "yes", question, disabled),
      createAnswerButton("No", "no", question, disabled)
    );

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

    if (question.parent && state.answers[question.parent] !== true) return;

    if (state.answers[question.id] === true) {
      if (question.block === "Política de datos personales") blocks.policy += question.weight;
      if (question.block === "Privacidad desde el diseño") blocks.design += question.weight;
      if (question.block === "Gobernanza") blocks.governance += question.weight;
    }

    if (state.answers[question.id] === false) {
      gaps.push(question);
    }
  });

  return { total: blocks.policy + blocks.design + blocks.governance, blocks, gaps };
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
    elements.planList.append(createResultItem(priorityLabel(question), question.recommendation, severityFor(question)));
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

function getQuestion(id) {
  return questions.find((question) => question.id === id) || questions[0];
}

async function apiFetch(path, options = {}) {
  const headers = {
    "Content-Type": "application/json",
    ...(options.headers || {})
  };

  if (options.auth !== false && state.session?.token) {
    headers.Authorization = `Bearer ${state.session.token}`;
  }

  const response = await fetch(`${CONFIG.apiBaseUrl}${path}`, {
    ...options,
    headers
  });

  if (!response.ok) {
    let message = "Solicitud fallida";
    try {
      const error = await response.json();
      message = error.message || message;
    } catch {
      message = response.statusText || message;
    }
    throw new Error(message);
  }

  if (response.status === 204) {
    return null;
  }

  return response.json();
}
