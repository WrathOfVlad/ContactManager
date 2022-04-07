<template>
    <RouterView />
</template>


<script>
    import { ref } from 'vue'
    import ToDo from './views/ToDo.vue'
    import MainGrid from './views/MainGrid.vue'; 
    import Login from './views/Login.vue';   
    

    export default {
        name: "App",
        components: {
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

