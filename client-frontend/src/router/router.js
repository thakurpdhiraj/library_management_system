import Vue from "vue";
import VueRouter from "vue-router";
import store from "../store/store";
import Home from "../views/Home.vue";
import Login from "../views/Login.vue";

Vue.use(VueRouter);

const routes = [
  {
    path: "/",
    name: "Home",
    component: Home,
    meta: {
      reqAuth: true,
      reqRole: "USER",
    },
  },
  {
    path: "/add",
    name: "Add",
    component: Home,
    meta: {
      reqAuth: true,
      reqRole: "USER",
    },
  },
  {
    path: "/login",
    name: "Login",
    component: Login,
    meta: {
      reqAuth: false,
      reqRole: "ANY",
    },
  },
];

const router = new VueRouter({
  mode: "history",
  routes,
});

router.beforeEach((to, from, next) => {
  if (to.matched.some((record) => record.meta.reqAuth)) {
    if (
      !store.getters.isAuthenticated &&
      sessionStorage.getItem("user") == null
    ) {
      //check if user has admin role
      next({
        path: "/login",
        query: { redirect: to.fullPath },
      });
    } else {
      let user = sessionStorage.getItem("user");
      console.log(JSON.parse(user));
      next();
    }
  } else {
    next();
  }
});

export default router;
