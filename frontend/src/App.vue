<template>
  <div id="nav">
      <router-link to="/todo">Todo</router-link> |
      <router-link to="/login">login</router-link>
  </div>
  <router-view />
</template>


<script>
    import { ref } from 'vue'
    // import RouterView from "./views/RouterView.vue"
    import ToDo from './views/ToDo.vue'
    import MainGrid from './views/MainGrid.vue'; 
    import Login from './views/Login.vue';  
    

    export default {
        name: "App",
        components: {
            // RouterView
            ToDo,
            MainGrid,
            Login

    },
        setup() {
            const msg = ref('Hello World!');

            // const setRef = (ref) => {
            //     console.log(ref.value);
            // }
            return {
                msg,
                // setRef
            }
        }
    }
</script>

