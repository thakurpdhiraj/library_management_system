<template>
  <v-card>
    <v-container>
      <v-row v-if="message">
        <v-col cols="12">
          <v-card class="elevation-5">
            <v-alert
              dense
              outlined
              dismissible
              transition="scale-transition"
              :type="isError ? 'error' : 'success'"
            >
              {{ message }}
            </v-alert>
          </v-card>
        </v-col>
      </v-row>
      <v-row>
        <v-col cols="12">
          <v-form
            @submit.prevent="findOrderHistory"
            v-model="valid"
            ref="searchForm"
          >
            <v-row>
              <v-col cols="12" sm="4" class="pt-0 mt-0">
                <v-text-field
                  label="User Id"
                  v-model="orderData"
                  :rules="[rules.required]"
                ></v-text-field>
              </v-col>
              <v-col cols="12" sm="4">
                <v-btn text color="green" type="submit" dark :disabled="!valid">
                  Search
                </v-btn>
              </v-col>
            </v-row>
          </v-form>
        </v-col>
      </v-row>
    </v-container>
    <v-data-table
      :headers="headers"
      :items="orders"
      class="elevation-20"
      item-key="id"
      :loading="loading"
      fixed-header
      height="50vh"
      :footer-props="{
        'items-per-page-text': 'Orders per page'
      }"
      dense
    >
    </v-data-table>
  </v-card>
</template>

<script>
import * as ruleUtil from "@/util/ruleUtil";
import * as orderService from "@/service/order";
export default {
  name: "FindOrdersHistory",
  data() {
    return {
      headers: [
        {
          text: "Order Id",
          align: "start",
          value: "orderId",
          sortable: false,
          class: "indigo--text darken-4"
        },
        {
          text: "User Id",
          value: "userId",
          sortable: false,
          class: "indigo--text darken-4"
        },
        {
          text: "Book Id",
          value: "bookId",
          sortable: false,
          class: "indigo--text darken-4"
        },
        {
          text: "Book Name",
          value: "bookName",
          sortable: false,
          class: "indigo--text darken-4"
        },
        {
          text: "ISBN",
          value: "bookIsbn",
          sortable: false,
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
          sortable: false,
          class: "indigo--text darken-4"
        },
        {
          text: "Collected At",
          value: "collectedAt",
          sortable: false,
          class: "indigo--text darken-4"
        },
        {
          text: "Returned At",
          value: "returnedAt",
          sortable: false,
          class: "indigo--text darken-4"
        },
        {
          text: "Late Fees",
          value: "lateFees",
          sortable: false,
          class: "indigo--text darken-4"
        }
      ],
      orders: [],
      loading: false,
      valid: false,
      message: null,
      isError: false,
      orderData: null,
      rules: ruleUtil.rules
    };
  },
  methods: {
    findOrderHistory() {
      this.setMarkerParams(true, null, false);
      orderService
        .findOrdersHistoryOfUser(this.orderData)
        .then(data => {
          this.orders = data;
          this.loading = false;
        })
        .catch(err => {
          this.setMarkerParams(false, err.error_description, true);
        });
    },
    setMarkerParams(loading, message, isError) {
      this.loading = loading;
      this.message = message;
      this.isError = isError;
    }
  }
};
</script>

<style></style>
