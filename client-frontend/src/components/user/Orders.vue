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
              <orders-history></orders-history>
              <new-order @newAdded="newAdded = true"></new-order>
              <v-list>
                <v-list-group :value="false" prepend-icon="mdi-filter">
                  <template v-slot:activator>
                    <v-list-item-title>Filters</v-list-item-title>
                  </template>
                  <v-list-item link @click="filterCollectionOverdue">
                    <v-list-item-title>Collection Overdue</v-list-item-title>
                  </v-list-item>
                  <v-list-item link @click="filterReturnOverdue">
                    <v-list-item-title>Return Overdue</v-list-item-title>
                  </v-list-item>
                  <v-list-item link @click="filterAll">
                    <v-list-item-title>All</v-list-item-title>
                  </v-list-item>
                </v-list-group>
              </v-list>
            </template>
            <v-toolbar flat v-if="!$vuetify.breakpoint.mobile">
              <v-toolbar-title>My Orders</v-toolbar-title>
              <v-divider class="mx-5" inset vertical></v-divider>
              <v-menu bottom transition="slide-y-transition">
                <template v-slot:activator="{ on, attrs }">
                  <v-btn color="indigo darken-4" dark v-bind="attrs" v-on="on">
                    <v-icon left dark>
                      mdi-filter
                    </v-icon>
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
                  <v-list-item link @click="filterAll">
                    <v-list-item-title>All</v-list-item-title>
                  </v-list-item>
                </v-list>
              </v-menu>
              <v-spacer></v-spacer>
              <orders-history></orders-history>
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
import OrdersHistory from "./OrdersHistory";
import * as orders from "../../service/order";
export default {
  data: () => ({
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
        class: "indigo--text darken-43"
      },
      { text: "Return By", value: "returnBy", class: "indigo--text darken-4" }
    ],
    orders: [],
    newAdded: false,
    loading: false
  }),
  methods: {
    findAllOrders() {
      console.log("finding orders");
      this.loading = true;
      orders
        .getUsersOrder()
        .then(data => {
          this.loading = false;
          this.newAdded = false;
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
      orders
        .getUsersOrderCollectionOverdue()
        .then(data => {
          this.loading = false;
          this.newAdded = false;
          this.replaceArray(this.orders, data);
        })
        .catch(err => {
          console.log(err);
          this.loading = false;
          this.$store.commit("setErrorMessage", err.error_description);
        });
    },
    filterReturnOverdue() {
      orders
        .getUsersOrderReturnOverdue()
        .then(data => {
          this.loading = false;
          this.newAdded = false;
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
    console.log("orders created");
    this.findAllOrders();
  },
  components: {
    NewOrder,
    OrdersHistory
  },
  watch: {
    newAdded() {
      if (this.newAdded) {
        this.findAllOrders();
      }
    }
  }
};
</script>

<style></style>
