<template>
  <q-page class="auth-page">
    <section class="auth-card">
      <div class="brand-row q-mb-md">
        <div class="brand-mark">C</div>
        <div>
          <div class="text-h5 text-weight-bold">Cavaltec</div>
          <div class="text-grey-7">Completando inicio de sesión</div>
        </div>
      </div>

      <q-spinner color="primary" size="42px" />
    </section>
  </q-page>
</template>

<script setup>
import { onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useQuasar } from 'quasar'
import { useAuthStore } from 'src/stores/auth'
import { useEvaluationStore } from 'src/stores/evaluation'

const route = useRoute()
const router = useRouter()
const $q = useQuasar()
const authStore = useAuthStore()
const evaluationStore = useEvaluationStore()

onMounted(async () => {
  const { token, role, email, nombre, empresaId, error } = route.query

  if (error) {
    $q.notify({ type: 'negative', message: String(error), position: 'top' })
    router.replace('/login')
    return
  }

  if (!token) {
    $q.notify({ type: 'negative', message: 'No se recibió la sesión OAuth.', position: 'top' })
    router.replace('/login')
    return
  }

  authStore.setSession({
    token: String(token),
    role: role ? String(role) : undefined,
    email: email ? String(email) : undefined,
    nombre: nombre ? String(nombre) : undefined,
    empresaId: empresaId ? String(empresaId) : null
  })

  try {
    await evaluationStore.loadLatestEvaluation()
  } catch {
    // El usuario puede no tener evaluaciones todavía.
  }

  $q.notify({ type: 'positive', message: 'Sesión OAuth iniciada', position: 'top' })
  router.replace('/')
})
</script>
