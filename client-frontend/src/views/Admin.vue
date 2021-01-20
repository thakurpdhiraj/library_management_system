<template>
  <v-container class="pa-0">
    <v-row v-if="errorMessage">
      <v-col cols="12">
        <v-alert
          dense
          outlined
          dismissible
          transition="scale-transition"
          type="error"
        >
          {{
            errorMessage
              ? errorMessage
              : "Something went wrong. Please try again!"
          }}
        </v-alert>
      </v-col>
    </v-row>
    <v-row>
      <v-col>
        <v-tabs right>
          <v-tab>
            <v-icon left>mdi-book-multiple</v-icon>
            Orders
          </v-tab>
          <v-tab-item><user-orders></user-orders></v-tab-item>

          <v-tab>
            <v-icon left>mdi-account-multiple</v-icon>
            Users
          </v-tab>
          <v-tab-item><find-users></find-users></v-tab-item>
        </v-tabs>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
import UserOrders from "../components/admin/orders/UserOrders";
import FindUsers from "../components/admin/users/FindUsers";
export default {
  methods: {},
  components: {
    UserOrders,
    FindUsers
  },
  computed: {
    errorMessage() {
      return this.$store.getters.getErrorMessage;
    }
  }
};
</script>

<style></style>
