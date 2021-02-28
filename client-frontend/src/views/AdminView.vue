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
        <Tabs :tabs="tabs" />
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
import Tabs from "@/components/common/Tabs";
import OrderPage from "@/components/admin/orders/OrderPage";
import UserPage from "@/components/admin/users/UserPage";
import BookPage from "@/components/admin/books/BookPage";
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
          component: OrderPage
        },
        {
          id: 1,
          name: "Books",
          icon: "mdi-book-open-page-variant",
          component: BookPage
        },
        {
          id: 2,
          name: "Users",
          icon: "mdi-account-multiple",
          component: UserPage
        },
        {
          id: 3,
          name: "Account",
          icon: "mdi-account-edit",
          component: AccountPage
        }
      ]
    };
  }
};
</script>
