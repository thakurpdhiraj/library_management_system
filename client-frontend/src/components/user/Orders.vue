<template>
  <v-container>
    <v-row>
      <v-col>
        <v-data-table
          :headers="headers"
          :items="orders"
          class="elevation-1"
          :loading="loading"
          loading-text="Loading...Please wait"
          fixed-header
          height="50vh"
        >
          <template v-slot:[`item.collectBy`]="{ item }">
            <span :class="getDateTextColor(item.collectBy, item.collectedAt)">
              {{ item.collectBy }}
            </span>
          </template>
          <template v-slot:[`item.returnBy`]="{ item }">
            <span :class="getDateTextColor(item.returnBy, item.returnedAt)">
              {{ item.returnBy }}
            </span>
          </template>
          <template v-slot:top>
            <v-toolbar flat>
              <v-toolbar-title>My Orders</v-toolbar-title>
              <v-divider class="mx-5" inset vertical></v-divider>
              <v-menu top offset-x>
                <template v-slot:activator="{ on, attrs }">
                  <v-btn color="primary" dark v-bind="attrs" v-on="on">
                    Filter
                  </v-btn>
                </template>

                <v-list>
                  <v-list-item link @click="filterCollectionOverdue">
                    <v-list-item-title>Collection Overdue</v-list-item-title>
                  </v-list-item>
                  <v-list-item link @click="filterReturnOverdue">
                    <v-list-item-title>Return Overdue</v-list-item-title>
                  </v-list-item>
                </v-list>
              </v-menu>
              <v-spacer></v-spacer>
              <new-order @newAdded="newAdded = true"></new-order>
            </v-toolbar>
          </template>
        </v-data-table>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
import NewOrder from "./NewOrder";
import * as orders from "../../service/order";
export default {
  data: () => ({
    headers: [
      { text: "Book Name", align: "start", value: "bookName" },
      { text: "Book Reference Id", value: "bookReferenceId", sortable: false },
      { text: "Ordered At", value: "orderedAt" },
      { text: "Collected At", value: "collectedAt" },
      { text: "Collect By", value: "collectBy" },
      { text: "Return By", value: "returnBy" },
    ],
    orders: [],
    newAdded: false,
    loading: false,
  }),
  methods: {
    findAllOrders() {
      console.log("finding orders");
      this.loading = true;
      orders
        .getUsersOrder()
        .then((data) => {
          this.loading = false;
          this.newAdded = false;
          console.log(data);
          console.log(data.length);
          this.orders = data;
        })
        .catch((err) => {
          console.log(err);
          this.loading = false;
          this.$store.commit("setErrorMessage", err.error_description);
        });
    },
    getDateTextColor(affectedDate, dependentDate) {
      if (dependentDate != null) {
        return "white--text";
      }
      let time = Date.parse(affectedDate);
      let now = Date.now();
      let diff = time - now;
      if (diff <= 0) {
        return "red--text";
      } else if (diff / (1000 * 3600 * 24) <= 3) {
        return "orange--text";
      } else {
        return "white--text";
      }
    },
    filterCollectionOverdue() {
      console.log("coll over");
    },
    filterReturnOverdue() {
      console.log("ret over");
    },
  },
  mounted() {
    console.log("orders mounted");
  },
  created() {
    console.log("orders created");
    this.findAllOrders();
  },
  components: {
    NewOrder,
  },
  watch: {
    newAdded(value) {
      if (this.newAdded) {
        this.findAllOrders();
      }
    },
  },
};
</script>

<style></style>
