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
        <Tabs :tabs="tabs" :right="true" />
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
import Tabs from "@/components/common/Tabs";
import MyOrdersPage from "@/components/user/orders/MyOrdersPage";
import AccountPage from "@/components/user/account/AccountPage";
export default {
  methods: {},
  components: {
    Tabs
  },
  computed: {
    errorMessage() {
      return this.$store.getters.getErrorMessage;
    }
  },
  data() {
    return {
      tabs: [
        {
          id: 0,
          name: "Orders",
          icon: "mdi-book-multiple",
          component: MyOrdersPage
        },
        {
          id: 1,
          name: "Account",
          icon: "mdi-account-edit",
          component: AccountPage
        }
      ]
    };
  }
};
</script>
