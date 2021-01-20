import Vue from "vue";
import VueRouter from "vue-router";
import store from "../store/store";
import * as util from "../util/authUtil";

import User from "../views/User.vue";
import Login from "../views/Login.vue";
import Admin from "../views/Admin.vue";
import Error from "../views/Error.vue";

Vue.use(VueRouter);

const routes = [
  {
    path: "/",
    name: "User",
    component: User,
    meta: {
      reqAuth: true,
      reqRole: "USER"
    }
  },
  {
    path: "/admin",
    name: "Admin",
    component: Admin,
    meta: {
      reqAuth: true,
      reqRole: "ADMIN"
    }
  },
  {
    path: "/login",
    name: "Login",
    component: Login,
    meta: {
      reqAuth: false,
      reqRole: "ANY"
    }
  },
  {
    path: "/error",
    name: "Error",
    component: Error,
    meta: {
      reqAuth: false,
      reqRole: "ANY"
    }
  }
];

const router = new VueRouter({
  mode: "history",
  routes
});

router.beforeEach((to, from, next) => {
  if (to.matched.some(record => record.meta.reqAuth)) {
    if (!util.isAuthenticated()) {
      next({
        path: "/login",
        query: { redirect: to.fullPath }
      });
    } else {
      if (to.matched.some(record => record.meta.reqRole == "ADMIN")) {
        if (util.isAdmin()) {
          next();
        } else {
          store.commit("setErrorMessage", "Insufficient Privilege.");
          next("/error");
        }
      } else if (to.matched.some(record => record.meta.reqRole == "USER")) {
        if (!util.isAdmin()) {
          next();
        } else {
          store.commit("setErrorMessage", "Insufficient Privilege.");
          next("/error");
        }
      } else {
        next();
      }
    }
  } else {
    next();
  }
});

export default router;
