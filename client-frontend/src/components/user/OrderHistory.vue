<template>
  <v-dialog
    v-model="dialog"
    :fullscreen="$vuetify.breakpoint.mobile"
    persistent
    overlay-opacity="0.95"
    :height="$vuetify.breakpoint.mobile ? '100vh' : '50vh'"
    :footer-props="{
      'items-per-page-text': 'Orders per page',
    }"
  >
    <template v-slot:activator="{ on, attrs }">
      <v-btn
        class="mx-5"
        dark
        color="blue-grey darken-4"
        v-bind="attrs"
        v-on="on"
      >
        <v-icon left dark>
          mdi-history
        </v-icon>
        History
      </v-btn>
    </template>
    <v-data-table
      :headers="headers"
      :items="history"
      class="elevation-10"
      :loading="loading"
      loading-text="Loading...Please wait"
    >
      <template v-slot:top>
        <v-toolbar flat>
          <v-toolbar-title>My Order History</v-toolbar-title>
          <v-divider class="mx-5" inset vertical></v-divider>
          <v-spacer />
          <v-btn icon @click="close">
            <v-icon>mdi-close-circle</v-icon>
          </v-btn>
        </v-toolbar>
      </template>
    </v-data-table>
  </v-dialog>
</template>

<script>
import * as orders from "../../service/order";
export default {
  data: () => ({
    dialog: false,
    loading: false,
    headers: [
      { text: "Book Name", align: "start", value: "bookName" },
      { text: "Book Reference Id", value: "bookReferenceId", sortable: false },
      { text: "Ordered At", value: "orderedAt" },
      { text: "Collected At", value: "collectedAt" },
      { text: "Returned At", value: "returnedAt" },
    ],
    history: [],
  }),
  methods: {
    close() {
      this.dialog = false;
    },
    findAllOrdersHistory() {
      console.log("finding orders");
      this.loading = true;
      orders
        .getUsersOrderHistory()
        .then((data) => {
          this.loading = false;
          this.history.length = 0;
          this.history.push(...data);
        })
        .catch((err) => {
          console.log(err);
          this.loading = false;
          this.$store.commit("setErrorMessage", err.error_description);
        });
    },
  },
  created() {
    console.log("orders history created");
  },
  watch: {
    dialog(value) {
      if (this.dialog) {
        this.findAllOrdersHistory();
      }
    },
  },
};
</script>

<style></style>
