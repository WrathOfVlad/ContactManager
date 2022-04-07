import { createRouter, createWebHistory } from 'vue-router';
import Login from "../views/Login.vue";
import ToDo from "../views/ToDo.vue";

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {
            path: '/login',
            name: 'login',
            component: Login
        },
        {
            path: "/todo",
            name: 'To Do',
            component: ToDo
        },
        {

        }
    ]
})


// const router = createRouter({
//   history: createWebHistory(import.meta.env.BASE_URL),
//   routes: routes,
// });

export default router;
