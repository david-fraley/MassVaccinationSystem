import VueRouter from 'vue-router';
import navRoutes from '@/router/navRoutes';
import store from '@/store/store';

let router = new VueRouter({
  mode: "history",
  routes: navRoutes,
});

router.beforeEach((to, from, next) => {
  if (to.matched.some(record => record.meta.requiresAuth)) {
    if (!store.getters.isLoggedIn) {
      next({
        path: '/UserLogin',
        params: { next: to.fullPath }
      });
    }
    console.log(store.state.currentUser.exp);
    // if(Date.now() > store.state.currentUser.exp || 0){
    //   store.dispatch("logoutUser");
    //   next({
    //     path: '/UserLogin',
    //     params: { next: to.fullPath }
    //   });
    // }
  }
  next();
});

export default router;
