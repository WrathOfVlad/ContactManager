
<template>
    <h1>Test</h1>
    <router-link to="/todo">Todo</router-link>
    <router-view />
</template>

<script>
    // import { ref } from 'vue'
    import ToDo from './ToDo.vue'
    import MainGrid from './MainGrid.vue'; 
    import Login from './Login.vue';  
    export default {
        name:"RouterView",
        components: {
            ToDo,
            MainGrid,
            Login
        },
        setup(){
            return{}
        }
    }
</script>