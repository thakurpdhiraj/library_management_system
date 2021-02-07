<template>
  <v-tabs vertical>
    <v-tab><v-icon left>mdi-account-search</v-icon>Find User</v-tab>
    <v-tab-item transition="slide-y-transition">
      <find-users></find-users>
    </v-tab-item>
    <v-tab><v-icon left>mdi-account-plus</v-icon>Add User</v-tab>
    <v-tab-item transition="slide-y-transition">
      <new-user></new-user>
    </v-tab-item>
  </v-tabs>
</template>

<script>
import NewUser from "../users/NewUser";
import FindUsers from "../users/FindUsers";
export default {
  components: {
    NewUser,
    FindUsers
  }
};
</script>

<style></style>
