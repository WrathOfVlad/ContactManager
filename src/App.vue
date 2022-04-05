<script setup>
import { ref } from 'vue'
import ToDo from './views/ToDo.vue';

const msg = ref('Hello World!')
</script>

<template>
  <h1>{{ msg }}</h1>
  <input v-model="msg">
  <ToDo title="First Todo"></ToDo>
  <ToDo></ToDo>
</template>