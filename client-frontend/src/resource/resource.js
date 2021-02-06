import axios from "axios";
import router from "../router/router";
import store from "../store/store";
import * as util from "../util/authUtil";

const axiosInstance = axios.create({
  baseURL: "http://localhost:8086/lms",
  withCredentials: true,
  timeout: 10000
});

axiosInstance.interceptors.request.use(config => {
  store.commit("setErrorMessage", null);
  if (config.url === "/login") {
    return config;
  } else {
    if (util.isAuthenticated()) {
      if (config.url.startsWith("/admin")) {
        if (util.isAdmin()) {
          return config;
        } else {
          store.commit(
            "setErrorMessage",
            "Not enough permission to access resource"
          );
          return Promise.reject("Not enough permission to access resource");
        }
      }
      return config;
    } else {
      store.commit("setErrorMessage", "Kindly Login to continue");
      router.push("/login");
    }
  }
});

axiosInstance.interceptors.response.use(
  response => {
    return response;
  },
  error => {
    if (error.response && error.response.data) {
      if (error.response.status == 401) {
        console.log("401");
        util.removeSessionUser();
        store.commit("setErrorMessage", error.response.data.error_description);
        router.push("/login");
        return;
      } else if (error.response.status == 403) {
        console.log("403 resource");
        store.commit("setErrorMessage", error.response.data.error_description);
      }
      return Promise.reject(error.response.data);
    }
    return Promise.reject(error);
  }
);

export default axiosInstance;
