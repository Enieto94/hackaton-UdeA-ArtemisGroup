import { defineStore } from 'pinia'
import { api } from 'boot/axios'
import { calculateResult, QUESTIONS } from 'src/data/diagnosticCatalog'

const STORAGE_KEY = 'cavaltec_privacy_design_draft'

const defaultCompany = () => ({
  nombre: '',
  nit: '',
  sector: '',
  tamano: ''
})

const defaultDraft = () => ({
  activeEvaluationId: null,
  company: defaultCompany(),
  answers: {},
  evidence: {},
  chat: [
    {
      role: 'assistant',
      text: 'Seleccione una pregunta y consulte por criterio, evidencia o recomendación. El asistente usa el contexto normativo de Ley 1581.'
    }
  ],
  lastSavedAt: null,
  status: null
})

function loadDraft() {
  try {
    return {
      ...defaultDraft(),
      ...JSON.parse(localStorage.getItem(STORAGE_KEY))
    }
  } catch {
    return defaultDraft()
  }
}

export const useEvaluationStore = defineStore('evaluation', {
  state: () => ({
    ...loadDraft()
  }),

  getters: {
    result: state => calculateResult(state.answers),
    answeredCount: state => QUESTIONS.filter(question => state.answers[question.id] !== undefined && state.answers[question.id] !== null).length,
    visibleQuestions: state => QUESTIONS.filter(question => !question.dependsOn || state.answers[question.dependsOn] === true)
  },

  actions: {
    persist() {
      localStorage.setItem(
        STORAGE_KEY,
        JSON.stringify({
          activeEvaluationId: this.activeEvaluationId,
          company: this.company,
          answers: this.answers,
          evidence: this.evidence,
          chat: this.chat,
          lastSavedAt: this.lastSavedAt,
          status: this.status
        })
      )
    },

    setCompany(company) {
      this.company = {
        ...this.company,
        ...company
      }
      this.persist()
    },

    setAnswer(question, value) {
      this.answers[question.id] = value

      if (question.id === 1 && value === false) {
        const childQuestions = [2, 3, 4, 5]
        childQuestions.forEach(id => {
          this.answers[id] = null
        })
      }

      if (question.id === 10 && value === false) {
        this.answers[11] = null
      }

      this.persist()
    },

    setEvidence(questionId, value) {
      this.evidence[questionId] = value
      this.persist()
    },

    resetDraft() {
      Object.assign(this, defaultDraft())
      this.persist()
    },

    applyEvaluation(evaluation) {
      this.activeEvaluationId = evaluation.id
      this.status = evaluation.estado
      this.lastSavedAt = evaluation.completedAt || evaluation.createdAt || new Date().toISOString()

      evaluation.respuestas?.forEach(item => {
        this.answers[item.preguntaNumero] = item.respuesta
        this.evidence[item.preguntaNumero] = item.evidencia || ''
      })

      this.persist()
    },

    async loadLatestEvaluation() {
      const { data } = await api.get('/evaluaciones')
      if (Array.isArray(data) && data.length > 0) {
        this.applyEvaluation(data[0])
      }
      return data
    },

    async saveEvaluation(completar = false) {
      const payload = {
        completar,
        respuestas: QUESTIONS.map(question => ({
          preguntaNumero: question.id,
          respuesta: this.answers[question.id] ?? null,
          evidencia: this.evidence[question.id] || ''
        }))
      }

      const request = this.activeEvaluationId
        ? api.put(`/evaluaciones/${this.activeEvaluationId}`, payload)
        : api.post('/evaluaciones', payload)

      const { data } = await request
      this.applyEvaluation(data)
      return data
    },

    addMessage(role, text) {
      this.chat.push({ role, text })
      this.chat = this.chat.slice(-40)
      this.persist()
    },

    async requestAssistantReply(message, question) {
      this.addMessage('user', message)
      const { data } = await api.post('/chat/diagnostico', {
        mensaje: message,
        preguntaNumero: question.id,
        respuesta: this.answers[question.id] ?? null,
        evidencia: this.evidence[question.id] || ''
      })
      this.addMessage('assistant', data.respuesta)
      return data.respuesta
    }
  }
})
