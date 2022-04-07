<template>
  <h1>{{ msg }}</h1>
  <p id="p"></p>
  <input v-model="msg">
  <ToDo id="First"></ToDo>
  <button @click=ajaxClick()>Get</button>
</template>


<script>
    import { ref } from 'vue'
    import ToDo from './components/ToDo.vue'
    import MainGrid from './components/MainGrid.vue';    

    export default {
        name: "App",
        components: {
        ToDo,
        MainGrid
    },
        setup() {
            const msg = ref('Hello World!');

            const ajaxClick = async function() {
                console.log("clicked");
                $.ajax({
                    url:"http://localhost:8080/api",
                    success: result => {
                        $("#first").todoElements=ref(result);
                        console.log(result);
                    },
                    error: error => {
                        console.error(error);
                        console.log(error.error)
                    }

                })

            }
            return {
                msg,
                ajaxClick,
            }
        }
    }
</script>

