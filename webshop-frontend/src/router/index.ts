import { createRouter, createWebHashHistory, RouteRecordRaw } from 'vue-router'
import StoreHome from '../components/StoreHome.vue';
import StoreManager from '../components/StoreManager.vue';
import 'bootstrap/dist/css/bootstrap.min.css';
import StoreCart from '@/components/StoreCart.vue';


const router = createRouter({
  history: createWebHashHistory(),
  routes : [
    {path: '/', redirect: '/storehome'},
    {path: '/storehome', component: StoreHome},
    {path: '/manageproduct', component: StoreManager},
    {path: '/storecart', component: StoreCart}
  ]
})

export default router
