<template>
  <q-layout view="lHh Lpr lFf" class="cavaltec-layout">
    <q-header class="cavaltec-header">
      <q-toolbar>
        <q-btn flat dense round icon="menu" aria-label="Menú" @click="toggleLeftDrawer" />

        <q-toolbar-title>
          Cavaltec Privacy Design Check
        </q-toolbar-title>

        <q-space />
        <q-chip v-if="authStore.isAuthenticated" square color="teal-1" text-color="teal-10">
          {{ normalizedRole }}
        </q-chip>
      </q-toolbar>
    </q-header>

    <q-drawer v-model="leftDrawerOpen" show-if-above bordered class="cavaltec-drawer">
      <q-list>
        <q-item-label header class="text-weight-bold">
          Autodiagnóstico Ley 1581
        </q-item-label>

        <q-item clickable v-ripple @click="navigateTo('/')">
          <q-item-section avatar>
            <q-icon name="dashboard" />
          </q-item-section>
          <q-item-section>
            <q-item-label>Diagnóstico</q-item-label>
            <q-item-label caption>Cuestionario, brechas y plan</q-item-label>
          </q-item-section>
        </q-item>

        <q-item clickable v-ripple href="#empresa" v-if="authStore.isAuthenticated">
          <q-item-section avatar>
            <q-icon name="business" />
          </q-item-section>
          <q-item-section>
            <q-item-label>Empresa</q-item-label>
          </q-item-section>
        </q-item>

        <q-item clickable v-ripple href="#asistente" v-if="authStore.isAuthenticated">
          <q-item-section avatar>
            <q-icon name="smart_toy" />
          </q-item-section>
          <q-item-section>
            <q-item-label>Asistente IA</q-item-label>
          </q-item-section>
        </q-item>

        <q-item clickable v-ripple href="#resultados" v-if="authStore.isAuthenticated">
          <q-item-section avatar>
            <q-icon name="fact_check" />
          </q-item-section>
          <q-item-section>
            <q-item-label>Resultados</q-item-label>
          </q-item-section>
        </q-item>

        <q-separator spaced />

        <q-item clickable v-ripple @click="handleLogout" v-if="authStore.isAuthenticated">
          <q-item-section avatar>
            <q-icon name="logout" />
          </q-item-section>
          <q-item-section>
            <q-item-label>Cerrar sesión</q-item-label>
            <q-item-label caption>{{ authStore.displayName }}</q-item-label>
          </q-item-section>
        </q-item>
      </q-list>
    </q-drawer>

    <q-page-container>
      <router-view />
    </q-page-container>
  </q-layout>
</template>

<script setup>
import { computed, ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from 'src/stores/auth'

const router = useRouter()
const authStore = useAuthStore()

const leftDrawerOpen = ref(false)
const normalizedRole = computed(() => (authStore.user?.role || 'EVALUADOR').toLowerCase())

function toggleLeftDrawer() {
  leftDrawerOpen.value = !leftDrawerOpen.value
}

function navigateTo(path) {
  router.push(path)
  leftDrawerOpen.value = false
}

function handleLogout() {
  authStore.logout()
  router.push('/login')
  leftDrawerOpen.value = false
}
</script>
