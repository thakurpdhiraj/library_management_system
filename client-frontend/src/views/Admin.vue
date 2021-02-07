<template>
  <v-container class="px-0">
    <v-row v-if="errorMessage" class="mt-3">
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
          <v-tab-item><order-page></order-page></v-tab-item>

          <v-tab>
            <v-icon left>mdi-account-multiple</v-icon>
            Users
          </v-tab>
          <v-tab-item><user-page></user-page></v-tab-item>
        </v-tabs>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
import OrderPage from "../components/admin/orders/OrderPage";
import UserPage from "../components/admin/users/UserPage";
export default {
  methods: {},
  components: {
    OrderPage,
    UserPage
  },
  computed: {
    errorMessage() {
      return this.$store.getters.getErrorMessage;
    }
  }
};
</script>
