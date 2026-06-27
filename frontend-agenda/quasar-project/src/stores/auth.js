import { defineStore } from 'pinia'
import { api } from 'boot/axios'

const TOKEN_KEY = 'cavaltec_token'
const USER_KEY = 'cavaltec_user'

function loadMicrosoftScript() {
  return loadExternalScript('https://alcdn.msauth.net/browser/2.38.2/js/msal-browser.min.js', 'microsoft-msal')
}

function loadExternalScript(src, id) {
  return new Promise((resolve, reject) => {
    if (document.getElementById(id)) {
      resolve()
      return
    }

    const script = document.createElement('script')
    script.id = id
    script.src = src
    script.async = true
    script.defer = true
    script.onload = resolve
    script.onerror = () => reject(new Error(`No se pudo cargar ${src}`))
    document.head.appendChild(script)
  })
}

const parseJwt = token => {
  try {
    const base64Url = token.split('.')[1]
    const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/')
    return JSON.parse(decodeURIComponent(escape(window.atob(base64))))
  } catch {
    return {}
  }
}

const readStoredJson = key => {
  try {
    return JSON.parse(localStorage.getItem(key))
  } catch {
    return null
  }
}

const normalizeSession = response => {
  const jwtPayload = response.token ? parseJwt(response.token) : {}
  return {
    id: jwtPayload.sub || jwtPayload.id || response.email,
    email: response.email || jwtPayload.email || jwtPayload.sub,
    nombre: response.nombre || jwtPayload.name || jwtPayload.nombre || '',
    role: response.role || jwtPayload.role || 'EVALUADOR',
    empresaId: response.empresaId || jwtPayload.empresaId || null,
    startedAt: new Date().toISOString()
  }
}

function encodeBase64UrlJson(value) {
  const json = JSON.stringify(value)
  const bytes = new TextEncoder().encode(json)
  let binary = ''
  bytes.forEach(byte => {
    binary += String.fromCharCode(byte)
  })

  return window.btoa(binary).replace(/\+/g, '-').replace(/\//g, '_').replace(/=+$/g, '')
}

export const useAuthStore = defineStore('auth', {
  state: () => ({
    token: localStorage.getItem(TOKEN_KEY) || null,
    user: readStoredJson(USER_KEY),
    oauthConfig: {
      microsoftClientId: import.meta.env.VITE_CAVALTEC_MICROSOFT_CLIENT_ID || ''
    }
  }),

  getters: {
    isAuthenticated: state => !!state.token,
    displayName: state => state.user?.nombre || state.user?.email || 'Usuario'
  },

  actions: {
    setSession(response) {
      this.token = response.token
      this.user = normalizeSession(response)
      localStorage.setItem(TOKEN_KEY, this.token)
      localStorage.setItem(USER_KEY, JSON.stringify(this.user))
    },

    async login(credentials) {
      const { data } = await api.post('/auth/login', credentials)
      this.setSession(data)
    },

    async register(payload) {
      const { data } = await api.post('/auth/register', payload)
      this.setSession(data)
    },

    async loginWithGoogle(empresa = null) {
      const baseUrl = api.defaults.baseURL.replace(/\/+$/, '')
      const params = new URLSearchParams()
      if (empresa) {
        params.set('empresa', encodeBase64UrlJson(empresa))
      }

      window.location.assign(`${baseUrl}/auth/google${params.toString() ? `?${params}` : ''}`)
    },

    async loginWithMicrosoft(empresa = null) {
      if (!this.oauthConfig.microsoftClientId) {
        throw new Error('Configure VITE_CAVALTEC_MICROSOFT_CLIENT_ID en el frontend para usar Microsoft OAuth.')
      }

      await loadMicrosoftScript()

      if (!window.msal) {
        throw new Error('MSAL no está disponible.')
      }

      const client = new window.msal.PublicClientApplication({
        auth: {
          clientId: this.oauthConfig.microsoftClientId,
          authority: 'https://login.microsoftonline.com/common',
          redirectUri: window.location.origin + window.location.pathname
        }
      })
      await client.initialize?.()
      const result = await client.loginPopup({ scopes: ['User.Read'] })
      const { data } = await api.post('/auth/microsoft', { token: result.accessToken, empresa })
      this.setSession(data)
    },

    logout() {
      this.token = null
      this.user = null
      localStorage.removeItem(TOKEN_KEY)
      localStorage.removeItem(USER_KEY)
    }
  }
})
