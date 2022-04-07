<template>
    

    <div class="container">
        
        <div class="card" style="width: 50rem;">
            <div class="card-header">
                <h3>Log In</h3>
            </div>
            <div class="card-body">
                <div class="input-group">
                    <span class="input-group-text" id="basic-addon1">@</span>
                    <div class="form-floating flex-grow-1" >
                        <input id="username" class="form-control" placeholder="Username" v-model="username" required>
                        <label for="username">Username</label>
                    </div>
    
                </div>
                <br />
                <div class="input-group">
                    <span class="input-group-text" id="basic-addon1"><i class="fa-solid fa-lock"></i></span>
                    <div class="form-floating flex-grow-1">
                        <input id="password" type="password" class="form-control" placeholder="Password" v-model="password" required>
                        <label for="password">Password</label>
                    </div>
    
                </div>
                <br >
                
                <button @click="sendData()" class="btn btn-primary">Log In</button>
        
            </div>
        </div>

    </div>
</template>

<script>

    import { ref } from 'vue';
    export default {
        setup(){
            const username = ref("");
            const password = ref("");

            const sendData = () => {
                $.ajax({
                    method: "post",
                    data: {
                        username: username.value,
                        password: password.value
                    },
                    url:"http://localhost:8080/api/login",

                    success: result => {
                        console.log(result);
                    },
                    error: error => {
                        console.error(error);
                    }
                })
            }

            return {
                username,
                password,
                sendData
            }
        }
    }
</script>