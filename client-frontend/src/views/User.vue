<template>
  <v-container class="pa-0">
    <v-row v-if="errorMessage">
      <v-col cols="12">
        <v-alert dense outlined transition="scale-transition" type="error">
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
          <v-tab-item><orders></orders></v-tab-item>

          <v-tab>
            <v-icon left>mdi-account-edit</v-icon>
            Account
          </v-tab>
          <v-tab-item><account></account> </v-tab-item>
        </v-tabs>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
import Orders from "../components/user/Orders";
import Account from "../components/user/Account";
export default {
  methods: {},
  components: {
    Orders,
    Account
  },
  computed: {
    errorMessage() {
      return this.$store.getters.getErrorMessage;
    }
  }
};
</script>
