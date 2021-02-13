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
import Orders from "@/components/user/Orders";
import Account from "@/components/user/Account";
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
          component: Orders
        },
        {
          id: 1,
          name: "Users",
          icon: "mdi-account-edit",
          component: Account
        }
      ]
    };
  }
};
</script>
