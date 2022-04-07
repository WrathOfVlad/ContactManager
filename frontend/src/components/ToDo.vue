<template>
    <h1>{{ title }}</h1>
    <ul>
        <li :style="{color:isRemove ? 'red' : 'black' , cursor: isRemove ? 'pointer':'default'}" @click="elementClicked(index)" v-for="(element,index) in todoElements" v-bind:key="index"> 
            {{ element }}
        </li>
    </ul>
    <input v-model="newItem" />
    <button @click="addElement">Add</button>

    <button @click="isRemove = !isRemove">Toggle Remove</button>
</template>

<script>
    import { ref } from 'vue'
    export default {
        name:"ToDo",
        props:{
          title: { 
              type: String,
              default: "ToDo"
            }
        },

        setup() {
            const todoElements = ref(['John',"bananna"]);
            const newItem = ref("");
            const isRemove = ref(false);

            const elementClicked = (key) => {
                if(isRemove.value){
                    console.log(key);
                    todoElements.value.splice(key,1);
                }
            }

            const addElement = () => {
                todoElements.value.push(newItem.value);
                newItem.value = "";
            }

            return{
                todoElements,
                newItem,
                isRemove,
                addElement,
                elementClicked
            }
        }
    }
</script>