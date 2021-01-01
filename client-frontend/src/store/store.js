import Vue from "vue";
import Vuex from "vuex";
import userStore from "./modules/userStore";

Vue.use(Vuex);

export default new Vuex.Store({
  state: {
    errorMessage: null,
  },
  getters: {
    getErrorMessage: (state) => state.errorMessage,
  },
  mutations: {
    setErrorMessage(state, message) {
      state.errorMessage = message;
    },
  },
  actions: {},
  modules: {
    userStore,
  },
});
