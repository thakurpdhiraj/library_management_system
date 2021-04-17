<template>
  <v-app>
    <v-app-bar app fixed>
      <v-icon>mdi-library</v-icon>
      <v-spacer></v-spacer>
      <v-toolbar-title v-if="$vuetify.breakpoint.smAndUp">{{
        appName
      }}</v-toolbar-title>
      <v-toolbar-title v-else>{{ apShortName }}</v-toolbar-title>
      <v-spacer />
      <v-btn @click="logout">Logout</v-btn>
    </v-app-bar>
    <v-main>
      <router-view />
    </v-main>
  </v-app>
</template>

<script>
import * as auth from "./service/auth";
import * as util from "./util/authUtil";
export default {
  data: () => ({
    appName: "Library Management System",
    apShortName: "LMS"
  }),
  methods: {
    logout() {
      auth.logout().then(() => {
        util.removeSessionUser();
        this.$store.commit("setErrorMessage", null);
        this.$router.push("/login");
      });
    }
  }
};
</script>

<style>
.v-window__container {
  min-height: 80vh;
}
::-webkit-scrollbar-track {
  background-color: transparent;
}

::-webkit-scrollbar {
  width: 20px;
}

::-webkit-scrollbar-thumb {
  background-color: #d6dee1;
  border-radius: 20px;
  border: 6px solid transparent;
  background-clip: content-box;
}

::-webkit-scrollbar-thumb:hover {
  background-color: #a8bbbf;
}
</style>
