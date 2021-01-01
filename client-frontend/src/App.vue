<template>
  <v-app>
    <v-app-bar app fixed>
      <v-spacer />
      <v-toolbar-title>{{ appName }}</v-toolbar-title>
      <v-spacer />
    </v-app-bar>
    <v-navigation-drawer permanent expand-on-hover app>
      <v-list nav>
        <v-list-item link>
          <v-list-item-content>
            <v-list-item-title>
              <v-icon>mdi-library</v-icon>
              LMS
            </v-list-item-title>
          </v-list-item-content>
        </v-list-item>

        <v-divider></v-divider>
        <v-list-item-group color="primary" mandatory>
          <v-list-item
            v-for="item in items"
            :key="item.title"
            router
            :to="item.route"
            class="mt-10"
          >
            <v-list-item-icon>
              <v-icon>{{ item.icon }}</v-icon>
            </v-list-item-icon>
            <v-list-item-content>
              <v-list-item-title class="title">
                {{ item.title }}
              </v-list-item-title>
            </v-list-item-content>
          </v-list-item>
        </v-list-item-group>
      </v-list>
      <template v-slot:append>
        <v-divider></v-divider>
        <v-list>
          <v-list-item href="#" @click="logout">
            <v-list-item-icon>
              <v-icon>mdi-logout</v-icon>
            </v-list-item-icon>
            <v-list-item-content>
              <v-list-item-title class="title">
                Logout
              </v-list-item-title>
            </v-list-item-content>
          </v-list-item>
        </v-list>
      </template>
    </v-navigation-drawer>
    <v-main>
      <v-container fluid class="pa-0">
        <router-view />
      </v-container>
    </v-main>
  </v-app>
</template>

<script>
import * as auth from "./service/auth";
export default {
  data: () => ({
    appName: "Library Management System",
    items: [
      { icon: "mdi-home", title: "My Orders", route: "/" },
      { icon: "mdi-cart-plus", title: "New Order", route: "/add" },
    ],
  }),
  methods: {
    logout() {
      console.log("logout");
      auth.logout().then(() => {
        this.$store.commit("loggedOut");
        this.$router.push("/login");
      });
    },
  },
};
</script>
