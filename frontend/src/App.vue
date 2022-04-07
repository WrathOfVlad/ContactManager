<template>
    <MainGrid />
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

            // const setRef = (ref) => {
            //     console.log(ref.value);
            // }
            return {
                msg,
                setRef
            }
        }
    }
</script>

