import Vue from "vue";
import VueRouter from "vue-router";
import store from "@/store/store";
import * as util from "@/util/authUtil";

import UserView from "@/views/UserView.vue";
import LoginView from "@/views/LoginView.vue";
import AdminView from "@/views/AdminView.vue";
import ErrorView from "@/views/ErrorView.vue";

Vue.use(VueRouter);

const routes = [
  {
    path: "/",
    name: "UserView",
    component: UserView,
    meta: {
      reqAuth: true,
      reqRole: "USER"
    }
  },
  {
    path: "/admin*",
    name: "AdminView",
    component: AdminView,
    meta: {
      reqAuth: true,
      reqRole: "ADMIN"
    }
  },
  {
    path: "/login",
    name: "LoginView",
    component: LoginView,
    meta: {
      reqAuth: false,
      reqRole: "ANY"
    }
  },
  {
    path: "/error",
    name: "ErrorView",
    component: ErrorView,
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
          next("/admin");
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
