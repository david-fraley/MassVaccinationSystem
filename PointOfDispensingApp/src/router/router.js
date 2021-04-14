import VueRouter from 'vue-router';
import navRoutes from '@/router/navRoutes';

let router = new VueRouter({
  mode: "history",
  routes: navRoutes,
});

export default router;
