import { createRouter, createWebHistory } from 'vue-router';
import Login from "../views/Login.vue";
import ToDo from "../views/ToDo.vue";

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {
            path: '/login',
            name: 'Login',
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

router.beforeEach(async (to, from) => {
    if ( !isAuthenticated && to.name !== 'Login') {
      return { name: 'Login' }
    }
  })


export default router;
