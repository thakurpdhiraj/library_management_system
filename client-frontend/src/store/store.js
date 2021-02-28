import Vue from "vue";
import Vuex from "vuex";
import userStore from "./modules/userStore";

Vue.use(Vuex);

export default new Vuex.Store({
  state: {
    errorMessage: null,
    home: "/"
  },
  getters: {
    getErrorMessage: state => state.errorMessage,
    getHome: state => state.home
  },
  mutations: {
    setErrorMessage(state, message) {
      state.errorMessage = message;
    },
    setHome(state, value) {
      state.home = value;
    }
  },
  actions: {},
  modules: {
    userStore
  }
});
