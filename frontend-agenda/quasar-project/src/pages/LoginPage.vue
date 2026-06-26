<template>
  <q-page class="auth-page">
    <section class="auth-card">
      <div class="brand-row q-mb-md">
        <div class="brand-mark">C</div>
        <div>
          <div class="text-h5 text-weight-bold">Cavaltec</div>
          <div class="text-grey-7">Privacy Design Check</div>
        </div>
      </div>

      <q-tabs v-model="mode" dense active-color="primary" indicator-color="primary" align="justify" class="q-mb-md">
        <q-tab name="login" label="Iniciar sesión" />
        <q-tab name="register" label="Registrar empresa" />
      </q-tabs>

      <q-form v-if="mode === 'login'" @submit.prevent="handleLogin" class="q-gutter-md">
        <q-input v-model.trim="loginForm.email" label="Email" type="email" outlined dense :rules="emailRules" />
        <q-input
          v-model="loginForm.password"
          label="Contraseña"
          :type="showPassword ? 'text' : 'password'"
          dense
          outlined
          :rules="[requiredRule]"
        >
          <template v-slot:append>
            <q-icon :name="showPassword ? 'visibility' : 'visibility_off'" class="cursor-pointer" @click="showPassword = !showPassword" />
          </template>
        </q-input>

        <q-btn label="Entrar" type="submit" color="primary" unelevated class="full-width" :loading="loading" />
      </q-form>

      <q-form v-else @submit.prevent="handleRegister" class="q-gutter-md">
        <div class="form-grid">
          <q-input v-model.trim="registerForm.empresa.nombre" label="Empresa" outlined dense :rules="[requiredRule]" />
          <q-input v-model.trim="registerForm.empresa.nit" label="NIT" outlined dense :rules="[requiredRule]" />
          <q-select v-model="registerForm.empresa.sector" :options="sectorOptions" label="Sector" outlined dense emit-value map-options />
          <q-select v-model="registerForm.empresa.tamano" :options="sizeOptions" label="Tamaño" outlined dense emit-value map-options />
        </div>

        <q-input v-model.trim="registerForm.nombre" label="Nombre del usuario" outlined dense :rules="[requiredRule]" />
        <q-input v-model.trim="registerForm.email" label="Email" type="email" outlined dense :rules="emailRules" />
        <q-input v-model="registerForm.password" label="Contraseña" type="password" outlined dense :rules="[requiredRule, minPasswordRule]" />

        <q-btn label="Crear cuenta y entrar" type="submit" color="primary" unelevated class="full-width" :loading="loading" />
      </q-form>

      <q-separator spaced />

      <div class="text-caption text-weight-bold text-uppercase text-grey-7 q-mb-sm">OAuth 2.0</div>
      <div class="oauth-actions">
        <q-btn outline icon="account_circle" label="Google" :loading="oauthLoading === 'google'" @click="handleOAuth('google')" />
        <q-btn outline icon="window" label="Microsoft" :loading="oauthLoading === 'microsoft'" @click="handleOAuth('microsoft')" />
      </div>
    </section>
  </q-page>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from 'src/stores/auth'
import { useEvaluationStore } from 'src/stores/evaluation'
import { useQuasar } from 'quasar'

const router = useRouter()
const authStore = useAuthStore()
const evaluationStore = useEvaluationStore()
const $q = useQuasar()

const mode = ref('login')
const loading = ref(false)
const oauthLoading = ref('')
const showPassword = ref(false)

const loginForm = reactive({
  email: '',
  password: ''
})

const registerForm = reactive({
  nombre: '',
  email: '',
  password: '',
  role: 'EVALUADOR',
  empresa: {
    nombre: '',
    nit: '',
    sector: '',
    tamano: ''
  }
})

const sectorOptions = ['Tecnología', 'Salud', 'Educación', 'Comercio', 'Financiero', 'Servicios', 'Otro']
const sizeOptions = [
  { label: 'Micro', value: 'micro' },
  { label: 'Pequeña', value: 'pequeña' },
  { label: 'Mediana', value: 'mediana' },
  { label: 'Grande', value: 'grande' }
]

const requiredRule = value => Boolean(value) || 'Campo obligatorio'
const minPasswordRule = value => value.length >= 6 || 'Mínimo 6 caracteres'
const emailRules = [
  requiredRule,
  value => /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(value) || 'Formato de email inválido'
]

async function afterAuthentication() {
  try {
    await evaluationStore.loadLatestEvaluation()
  } catch {
    // El usuario puede no tener evaluaciones todavía.
  }
  router.push('/')
}

async function handleLogin() {
  loading.value = true
  try {
    await authStore.login(loginForm)
    $q.notify({ type: 'positive', message: 'Sesión iniciada', position: 'top' })
    await afterAuthentication()
  } catch (error) {
    $q.notify({ type: 'negative', message: error.response?.data?.message || 'No se pudo iniciar sesión', position: 'top' })
  } finally {
    loading.value = false
  }
}

async function handleRegister() {
  loading.value = true
  try {
    await authStore.register(registerForm)
    evaluationStore.setCompany(registerForm.empresa)
    $q.notify({ type: 'positive', message: 'Empresa registrada', position: 'top' })
    await afterAuthentication()
  } catch (error) {
    $q.notify({ type: 'negative', message: error.response?.data?.message || 'No se pudo registrar la empresa', position: 'top' })
  } finally {
    loading.value = false
  }
}

async function handleOAuth(provider) {
  oauthLoading.value = provider
  const empresa = mode.value === 'register' ? registerForm.empresa : null

  try {
    if (provider === 'google') {
      await authStore.loginWithGoogle(empresa)
    } else {
      await authStore.loginWithMicrosoft(empresa)
    }

    if (empresa) evaluationStore.setCompany(empresa)
    $q.notify({ type: 'positive', message: 'Sesión OAuth iniciada', position: 'top' })
    await afterAuthentication()
  } catch (error) {
    $q.notify({ type: 'negative', message: error.message || 'No se pudo completar OAuth', position: 'top' })
  } finally {
    oauthLoading.value = ''
  }
}
</script>
