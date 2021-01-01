import Vue from "vue";
import VueRouter from "vue-router";
import store from "../store/store";
import * as util from "../util/authUtil";

import Home from "../views/Home.vue";
import Login from "../views/Login.vue";
import Admin from "../views/Admin.vue";
import Error from "../views/Error.vue";

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
    path: "/admin",
    name: "Admin",
    component: Admin,
    meta: {
      reqAuth: true,
      reqRole: "ADMIN",
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
  {
    path: "/error",
    name: "Error",
    component: Error,
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
    debugger;
    if (!util.isAuthenticated()) {
      next({
        path: "/login",
        query: { redirect: to.fullPath },
      });
    } else {
      if (to.matched.some((record) => record.meta.reqRole == "ADMIN")) {
        if (util.isAdmin()) {
          next();
        } else {
          store.commit("setErrorMessage", "Insufficient Privilege.");
          next("/error");
        }
      }
      next();
    }
  } else {
    next();
  }
});

export default router;
