const state = {
  authenticated: false,
};

const getters = {
  isAuthenticated: (state) => state.authenticated,
};

const mutations = {
  loggedIn: (state) => (state.authenticated = true),
  loggedOut: (state) => (state.authenticated = false),
};

const actions = {};

export default {
  state,
  getters,
  mutations,
  actions,
};
