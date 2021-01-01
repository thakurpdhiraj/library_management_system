import axios from "axios";
import router from "../router/router";
import store from "../store/store";

const axiosInstance = axios.create({
  baseURL: "http://localhost:8086/lms",
  withCredentials: true,
  timeout: 10000,
});

axiosInstance.interceptors.request.use((config) => {
  if (config.url === "/login") {
    return config;
  } else {
    const token = sessionStorage.getItem("user");
    if (token != null) {
      return config;
    } else {
      sessionStorage.removeItem("user");
      router.push("/login");
    }
  }
});

axiosInstance.interceptors.response.use(
  (response) => {
    if (response.status == 401) {
      sessionStorage.removeItem("user");
      store.commit("setErrorMessage", response.data.error_description);
      router.push("/login");
    } else if (response.status == 403) {
      console.log("not enough permission");
      store.commit("setErrorMessage", response.data.error_description);
    }
    return response;
  },
  (error) => {
    if (error.response && error.response.data) {
      return Promise.reject(error.response.data);
    }
    return Promise.reject(error.message);
  }
);

export default axiosInstance;
