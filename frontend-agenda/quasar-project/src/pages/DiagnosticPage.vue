<template>
  <q-page class="diagnostic-page">
    <section class="hero-band">
      <div>
        <div class="eyebrow">Ley 1581 de 2012</div>
        <h1>Autodiagnóstico de cumplimiento en fase de diseño</h1>
        <p>
          Evalúe política de datos personales, privacidad desde el diseño y gobernanza con cálculo ponderado,
          brechas accionables y asistencia contextual.
        </p>
      </div>
      <div class="hero-actions">
        <q-btn outline color="primary" label="Guardar borrador" icon="save" :loading="saving === 'draft'" @click="save(false)" />
        <q-btn color="primary" unelevated label="Completar" icon="task_alt" :loading="saving === 'complete'" @click="save(true)" />
        <q-btn flat color="grey-8" label="Reiniciar" icon="restart_alt" @click="confirmReset" />
      </div>
    </section>

    <section class="score-grid">
      <article class="score-card gauge-card">
        <q-circular-progress
          show-value
          :value="result.total"
          size="150px"
          :thickness="0.18"
          :color="scoreColor"
          track-color="grey-3"
          class="text-weight-bold"
        >
          <div class="text-center">
            <div class="text-h4">{{ result.total }}%</div>
            <div class="text-caption">{{ scoreLevel(result.total) }}</div>
          </div>
        </q-circular-progress>
      </article>
      <article v-for="block in blockCards" :key="block.key" class="score-card">
        <span>{{ block.shortLabel }}</span>
        <strong>{{ result.blocks[block.key] }}/{{ block.max }}</strong>
        <q-linear-progress :value="result.blocks[block.key] / block.max" rounded size="10px" :color="scoreColor" />
      </article>
    </section>

    <section id="empresa" class="section-band">
      <div class="section-heading">
        <div>
          <div class="eyebrow">Multiempresa</div>
          <h2>Datos de la organización</h2>
        </div>
        <q-chip square color="blue-grey-1" text-color="blue-grey-9">{{ authStore.displayName }}</q-chip>
      </div>

      <div class="company-grid">
        <q-input v-model.trim="company.nombre" label="Nombre" outlined dense @update:model-value="syncCompany" />
        <q-input v-model.trim="company.nit" label="NIT" outlined dense @update:model-value="syncCompany" />
        <q-select v-model="company.sector" :options="sectorOptions" label="Sector" outlined dense @update:model-value="syncCompany" />
        <q-select v-model="company.tamano" :options="sizeOptions" label="Tamaño" outlined dense emit-value map-options @update:model-value="syncCompany" />
      </div>
    </section>

    <section id="diagnostico" class="section-band">
      <div class="section-heading">
        <div>
          <div class="eyebrow">Diagnóstico</div>
          <h2>Cuestionario ponderado</h2>
        </div>
        <q-badge color="primary" outline>{{ evaluationStore.answeredCount }} respuestas registradas</q-badge>
      </div>

      <div class="questions-stack">
        <article
          v-for="question in visibleQuestions"
          :key="question.id"
          class="question-card"
          :class="{ 'is-disabled': isQuestionDisabled(question, evaluationStore.answers) }"
        >
          <div>
            <div class="question-meta">
              <q-badge outline color="primary">P{{ question.id }}</q-badge>
              <q-badge color="blue-grey-1" text-color="blue-grey-9">{{ question.block }}</q-badge>
              <q-badge color="teal-1" text-color="teal-10">{{ question.weight ? `${question.weight}%` : 'Complementaria' }}</q-badge>
              <q-badge color="grey-2" text-color="grey-9">{{ question.article }}</q-badge>
            </div>
            <h3>{{ question.text }}</h3>
            <p>{{ question.help }}</p>
            <q-input
              :model-value="evaluationStore.evidence[question.id]"
              type="textarea"
              autogrow
              outlined
              dense
              label="Evidencia o comentario"
              :placeholder="question.evidence"
              :disable="isQuestionDisabled(question, evaluationStore.answers)"
              @update:model-value="value => evaluationStore.setEvidence(question.id, value)"
            />
          </div>

          <div class="answer-actions">
            <q-btn
              :unelevated="evaluationStore.answers[question.id] === true"
              :outline="evaluationStore.answers[question.id] !== true"
              color="positive"
              label="Sí"
              :disable="isQuestionDisabled(question, evaluationStore.answers)"
              @click="evaluationStore.setAnswer(question, true)"
            />
            <q-btn
              :unelevated="evaluationStore.answers[question.id] === false"
              :outline="evaluationStore.answers[question.id] !== false"
              color="negative"
              label="No"
              :disable="isQuestionDisabled(question, evaluationStore.answers)"
              @click="evaluationStore.setAnswer(question, false)"
            />
          </div>
        </article>
      </div>
    </section>

    <section id="asistente" class="section-band assistant-grid">
      <div>
        <div class="section-heading">
          <div>
            <div class="eyebrow">Asistente IA</div>
            <h2>Apoyo normativo contextual</h2>
          </div>
        </div>

        <div ref="chatContainer" class="chat-panel">
          <div v-for="(message, index) in evaluationStore.chat" :key="index" class="chat-message" :class="message.role">
            {{ message.text }}
          </div>
        </div>

        <q-form class="chat-form" @submit.prevent="sendChat">
          <q-select v-model="selectedQuestionId" :options="questionOptions" outlined dense emit-value map-options />
          <q-input v-model.trim="chatText" outlined dense maxlength="240" placeholder="Pregunte por criterio, evidencia o recomendación" />
          <q-btn type="submit" color="primary" unelevated icon="send" :loading="asking" />
        </q-form>
      </div>

      <div class="quick-panel">
        <q-btn outline label="Explicar" @click="quickAsk('Explique la pregunta seleccionada')" />
        <q-btn outline label="Evidencia" @click="quickAsk('Qué evidencia demuestra cumplimiento')" />
        <q-btn outline label="Recomendaciones" @click="quickAsk('Recomiende acciones para cerrar brechas')" />
      </div>
    </section>

    <section id="resultados" class="section-band">
      <div class="section-heading">
        <div>
          <div class="eyebrow">Resultados</div>
          <h2>Brechas y estrategias de mejora</h2>
        </div>
        <q-btn outline color="primary" icon="download" label="Descargar reporte" @click="downloadReport" />
      </div>

      <div class="results-grid">
        <div>
          <h3>Brechas identificadas</h3>
          <div class="stack-list">
            <article v-if="result.gaps.length === 0" class="result-item low">
              <strong>Sin brechas registradas</strong>
              <span>Responda el cuestionario o mantenga evidencias para sustentar el resultado.</span>
            </article>
            <article v-for="gap in result.gaps" :key="gap.id" class="result-item" :class="severityFor(gap)">
              <strong>P{{ gap.id }} · {{ gap.block }}</strong>
              <span>{{ gap.text }} Base: {{ gap.article }}.</span>
            </article>
          </div>
        </div>

        <div>
          <h3>Plan priorizado</h3>
          <div class="stack-list">
            <article v-if="orderedPlan.length === 0" class="result-item low">
              <strong>Mantener controles</strong>
              <span>Revise evidencias periódicamente y actualice políticas cuando cambien finalidades, sistemas o encargados.</span>
            </article>
            <article v-for="item in orderedPlan" :key="item.id" class="result-item" :class="severityFor(item)">
              <strong>{{ priorityLabel(item) }}</strong>
              <span>{{ item.recommendation }}</span>
            </article>
          </div>
        </div>
      </div>
    </section>
  </q-page>
</template>

<script setup>
import { computed, nextTick, reactive, ref, watch } from 'vue'
import { useQuasar } from 'quasar'
import { BLOCKS, QUESTIONS, isQuestionDisabled, priorityLabel, scoreLevel, severityFor } from 'src/data/diagnosticCatalog'
import { useAuthStore } from 'src/stores/auth'
import { useEvaluationStore } from 'src/stores/evaluation'

const $q = useQuasar()
const authStore = useAuthStore()
const evaluationStore = useEvaluationStore()

const saving = ref('')
const asking = ref(false)
const chatText = ref('')
const selectedQuestionId = ref(1)
const chatContainer = ref(null)
const company = reactive({ ...evaluationStore.company })

const sectorOptions = ['Tecnología', 'Salud', 'Educación', 'Comercio', 'Financiero', 'Servicios', 'Otro']
const sizeOptions = [
  { label: 'Micro', value: 'micro' },
  { label: 'Pequeña', value: 'pequeña' },
  { label: 'Mediana', value: 'mediana' },
  { label: 'Grande', value: 'grande' }
]

const result = computed(() => evaluationStore.result)
const visibleQuestions = computed(() => evaluationStore.visibleQuestions)
const blockCards = computed(() => Object.values(BLOCKS))
const scoreColor = computed(() => {
  if (result.value.total >= 75) return 'positive'
  if (result.value.total >= 45) return 'warning'
  return 'negative'
})
const questionOptions = computed(() =>
  visibleQuestions.value.map(question => ({
    label: `P${question.id} · ${question.block}`,
    value: question.id
  }))
)
const selectedQuestion = computed(() => QUESTIONS.find(question => question.id === selectedQuestionId.value) || QUESTIONS[0])
const orderedPlan = computed(() =>
  [...result.value.gaps].sort((a, b) => b.weight - a.weight || a.id - b.id).slice(0, 6)
)

watch(
  () => evaluationStore.company,
  value => Object.assign(company, value),
  { deep: true }
)

watch(
  () => evaluationStore.chat.length,
  async () => {
    await nextTick()
    if (chatContainer.value) {
      chatContainer.value.scrollTop = chatContainer.value.scrollHeight
    }
  }
)

function syncCompany() {
  evaluationStore.setCompany(company)
}

async function save(completar) {
  saving.value = completar ? 'complete' : 'draft'
  try {
    await evaluationStore.saveEvaluation(completar)
    $q.notify({
      type: 'positive',
      message: completar ? 'Evaluación completada y guardada' : 'Borrador guardado',
      position: 'top'
    })
  } catch (error) {
    $q.notify({
      type: 'negative',
      message: error.response?.data?.message || 'No se pudo guardar la evaluación',
      position: 'top'
    })
  } finally {
    saving.value = ''
  }
}

function confirmReset() {
  $q.dialog({
    title: 'Reiniciar diagnóstico',
    message: 'Se limpiarán respuestas, evidencias y conversación local.',
    cancel: true,
    persistent: true
  }).onOk(() => {
    evaluationStore.resetDraft()
    Object.assign(company, evaluationStore.company)
  })
}

async function sendChat() {
  if (!chatText.value) return
  const message = chatText.value
  chatText.value = ''
  await askAssistant(message)
}

async function quickAsk(message) {
  await askAssistant(message)
}

async function askAssistant(message) {
  asking.value = true
  try {
    await evaluationStore.requestAssistantReply(message, selectedQuestion.value)
  } catch (error) {
    evaluationStore.addMessage('assistant', `No se pudo consultar el backend: ${error.response?.data?.message || error.message}`)
  } finally {
    asking.value = false
  }
}

function downloadReport() {
  const reportLines = [
    'Cavaltec Privacy Design Check',
    `Empresa: ${company.nombre || 'Sin nombre'} (${company.nit || 'Sin NIT'})`,
    `Score total: ${result.value.total}% - ${scoreLevel(result.value.total)}`,
    '',
    'Brechas:',
    ...(result.value.gaps.length
      ? result.value.gaps.map(gap => `- P${gap.id}: ${gap.text}`)
      : ['- Sin brechas registradas']),
    '',
    'Plan priorizado:',
    ...(orderedPlan.value.length
      ? orderedPlan.value.map(item => `- ${priorityLabel(item)}: ${item.recommendation}`)
      : ['- Mantener controles y evidencias actualizadas'])
  ]
  const blob = new Blob([reportLines.join('\n')], { type: 'text/plain;charset=utf-8' })
  const url = URL.createObjectURL(blob)
  const anchor = document.createElement('a')
  anchor.href = url
  anchor.download = `cavaltec-reporte-${new Date().toISOString().slice(0, 10)}.txt`
  anchor.click()
  URL.revokeObjectURL(url)
}
</script>
