<template>
  <v-dialog v-model="dialog" fullscreen persistent overlay-opacity="1">
    <template v-slot:activator="{ on, attrs }">
      <v-btn dark color="deep-purple darken-4" v-bind="attrs" v-on="on">
        <v-icon left dark>
          mdi-book-plus
        </v-icon>
        New Order
      </v-btn>
    </template>
    <!-- Re render on open-->
    <v-card v-if="dialog">
      <v-toolbar flat>
        <v-toolbar-title>New Order</v-toolbar-title>
        <v-divider class="mx-5" inset vertical></v-divider>
        <v-spacer />
        <h3 v-if="$vuetify.breakpoint.mobile && stepNumber === 1">
          Choose Book
        </h3>
        <h3 v-if="$vuetify.breakpoint.mobile && stepNumber === 2">
          Search Inventory
        </h3>
        <h3 v-if="$vuetify.breakpoint.mobile && stepNumber === 3">
          Finalize Order
        </h3>
        <v-spacer />
        <v-btn icon @click="close">
          <v-icon>mdi-close-circle</v-icon>
        </v-btn>
      </v-toolbar>
      <v-alert
        dense
        outlined
        type="error"
        transition="scale-transition"
        v-if="errorMessage"
      >
        {{
          errorMessage
            ? errorMessage
            : "Something went wrong. Please try again!"
        }}
      </v-alert>
      <v-stepper v-model="stepNumber">
        <v-stepper-header>
          <v-stepper-step :complete="stepNumber > 1" step="1" color="green">
            Choose Book
          </v-stepper-step>
          <v-divider></v-divider>
          <v-stepper-step :complete="stepNumber > 2" step="2" color="green">
            Search Inventory
          </v-stepper-step>
          <v-divider></v-divider>
          <v-stepper-step step="3" color="green">
            Finalize Order
          </v-stepper-step>
        </v-stepper-header>

        <v-stepper-items>
          <!-- Choose Book -->
          <v-stepper-content step="1" class="pa-2">
            <ChooseBook @searchInventory="searchInventory" />
          </v-stepper-content>
          <!-- Search Inventory -->
          <v-stepper-content step="2">
            <SearchInventory
              :selectedNewBook="selectedNewBook"
              @goBack="goBack"
              @finalizeOrder="finalizeOrder"
            />
          </v-stepper-content>
          <!-- Order Book -->
          <v-stepper-content step="3">
            <OrderBook
              :selectedNewBook="selectedNewBook"
              @goBack="goBack"
              @orderBook="orderBook"
            />
          </v-stepper-content>
        </v-stepper-items>
      </v-stepper>
    </v-card>
  </v-dialog>
</template>

<script>
import ChooseBook from "./ChooseBook";
import SearchInventory from "./SearchInventory";
import OrderBook from "./OrderBook";
export default {
  name: "NewOrder",
  components: {
    ChooseBook,
    SearchInventory,
    OrderBook
  },
  data: () => ({
    dialog: false,
    stepNumber: 1,
    selectedNewBook: null,
    error: null
  }),
  computed: {
    errorMessage() {
      return this.$store.getters.getErrorMessage;
    }
  },
  methods: {
    close() {
      this.stepNumber = 1;
      this.dialog = false;
      this.$store.commit("setErrorMessage", null);
    },
    searchInventory(book) {
      this.goContinue();
      this.selectedNewBook = book;
    },
    finalizeOrder() {
      this.goContinue();
    },
    orderBook() {
      this.$emit("newAdded");
      this.close();
    },
    goBack() {
      this.stepNumber--;
    },
    goContinue() {
      this.stepNumber++;
    }
  }
};
</script>

<style></style>
