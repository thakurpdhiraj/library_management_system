<template>
  <v-container>
    <v-row>
      <v-col>
        <v-data-table
          :headers="headers"
          :items="orders"
          class="elevation-10"
          :loading="loading"
          loading-text="Loading...Please wait"
          fixed-header
          height="50vh"
          :sort-by="['orderedAt']"
          :sort-desc="['true']"
          :footer-props="{
            'items-per-page-text': 'Orders per page'
          }"
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
            <template v-if="$vuetify.breakpoint.mobile">
              <MyOrdersHistory />
              <NewOrder @newAdded="newAdded = true" />
              <FilterOrder
                @collectionOrders="filterCollectionOverdue"
                @returnOrders="filterReturnOverdue"
                @allOrders="filterAll"
              />
            </template>

            <v-toolbar flat v-else>
              <v-toolbar-title>My Orders</v-toolbar-title>
              <v-divider class="mx-5" inset vertical></v-divider>
              <FilterOrder
                @collectionOrders="filterCollectionOverdue"
                @returnOrders="filterReturnOverdue"
                @allOrders="filterAll"
              />
              <v-spacer></v-spacer>
              <MyOrdersHistory />
              <NewOrder @newAdded="newAdded = true" />
            </v-toolbar>
          </template>
        </v-data-table>
        <v-snackbar :value="newAdded" top timeout="5000" color="success">
          <v-icon>mdi-check-circle</v-icon>
          New Book successfully ordered!
          <template v-slot:action="{ attrs }">
            <v-btn color="white" text v-bind="attrs" @click="newAdded = false">
              Close
            </v-btn>
          </template>
        </v-snackbar>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
import NewOrder from "./neworder/NewOrder";
import MyOrdersHistory from "./history/MyOrdersHistory";
import FilterOrder from "./utility/FilterOrder";
import * as orderService from "@/service/order";
export default {
  name: "MyOrdersPage",
  data() {
    return {
      headers: [
        {
          text: "Book Name",
          align: "start",
          value: "bookName",
          class: "indigo--text darken-4"
        },
        {
          text: "Book Reference Id",
          value: "bookReferenceId",
          sortable: false,
          class: "indigo--text darken-4"
        },
        {
          text: "ISBN",
          value: "bookIsbn",
          class: "indigo--text darken-4"
        },
        {
          text: "Ordered At",
          value: "orderedAt",
          class: "indigo--text darken-4"
        },
        {
          text: "Collected At",
          value: "collectedAt",
          class: "indigo--text darken-4"
        },
        {
          text: "Collect By",
          value: "collectBy",
          class: "indigo--text darken-4"
        },
        { text: "Return By", value: "returnBy", class: "indigo--text darken-4" }
      ],
      orders: [],
      newAdded: false,
      loading: false
    };
  },
  methods: {
    findAllOrders() {
      console.log("finding orders");
      this.loading = true;
      orderService
        .getUsersOrder()
        .then(data => {
          this.loading = false;
          this.replaceArray(this.orders, data);
        })
        .catch(err => {
          console.log(err);
          this.loading = false;
          this.$store.commit("setErrorMessage", err.error_description);
        });
    },
    getDateTextColor(affectedDate, dependentDate) {
      let plainColor = this.$vuetify.theme.isDark
        ? "white--text"
        : "black--text";
      if (dependentDate != null) {
        return plainColor;
      }
      let diff = this.getDayDifferenceFromNow(affectedDate);

      if (diff <= 0) {
        return this.$vuetify.theme.isDark
          ? "red--text darken-4"
          : "red--text darken-4";
      } else if (diff <= 3) {
        return this.$vuetify.theme.isDark
          ? "yellow--text lighten-1"
          : "orange--text lighten-2";
      } else {
        return plainColor;
      }
    },
    filterCollectionOverdue() {
      orderService
        .getUsersOrderCollectionOverdue()
        .then(data => {
          this.loading = false;
          this.replaceArray(this.orders, data);
        })
        .catch(err => {
          console.log(err);
          this.loading = false;
          this.$store.commit("setErrorMessage", err.error_description);
        });
    },
    filterReturnOverdue() {
      orderService
        .getUsersOrderReturnOverdue()
        .then(data => {
          this.loading = false;
          this.replaceArray(this.orders, data);
        })
        .catch(err => {
          console.log(err);
          this.loading = false;
          this.$store.commit("setErrorMessage", err.error_description);
        });
    },
    filterAll() {
      this.findAllOrders();
    },
    replaceArray(dest, src) {
      dest.length = 0;
      dest.push(...src);
    },
    getDayDifferenceFromNow(date) {
      let time = Date.parse(date);
      let now = Date.now();
      let diff = time - now;
      return diff / (1000 * 3600 * 24);
    }
  },
  created() {
    this.findAllOrders();
  },
  components: {
    NewOrder,
    MyOrdersHistory,
    FilterOrder
  },
  watch: {
    newAdded() {
      if (this.newAdded) {
        setTimeout(() => (this.newAdded = false), 4000);
        this.findAllOrders();
      }
    }
  }
};
</script>

<style></style>
