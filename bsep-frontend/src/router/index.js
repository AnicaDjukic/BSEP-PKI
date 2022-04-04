import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    name: '',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/LoginView.vue')
  },
  {
    path: '/admin-page',
    name: '',
    component: () => import(/* webpackChunkName: "about" */ '../views/AdminView.vue')
  },
  {
    path: '/create-certificate-page',
    name: '',
    component: () => import(/* webpackChunkName: "about" */ '../views/CreateCertificateView.vue')
  },
  {
    path: '/all-certificates-page',
    name: '',
    component: () => import(/* webpackChunkName: "about" */ '../views/AllCertificatesView.vue')
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router
