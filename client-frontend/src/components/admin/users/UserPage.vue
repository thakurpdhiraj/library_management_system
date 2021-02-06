<template>
  <v-container>
    <v-row>
      <v-col>
        <v-tabs vertical>
          <v-tab><v-icon left>mdi-account-search</v-icon>Find User</v-tab>
          <v-tab-item><find-users></find-users></v-tab-item>
          <v-tab><v-icon left>mdi-account-plus</v-icon>Add User</v-tab>
          <v-tab-item><new-user></new-user></v-tab-item>
        </v-tabs>
      </v-col>
    </v-row>
  </v-container>
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
