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
    <v-card>
      <v-toolbar flat>
        <v-toolbar-title>New Order</v-toolbar-title>
        <v-divider class="mx-5" inset vertical></v-divider>
        <v-spacer />
        <h3 v-if="$vuetify.breakpoint.mobile && o1 === 1">
          Choose Book
        </h3>
        <h3 v-if="$vuetify.breakpoint.mobile && o1 === 2">
          Search Inventory
        </h3>
        <h3 v-if="$vuetify.breakpoint.mobile && o1 === 3">
          Finalize Order
        </h3>
        <v-spacer />
        <v-btn icon @click="close">
          <v-icon>mdi-close-circle</v-icon>
        </v-btn>
      </v-toolbar>
      <v-stepper v-model="o1">
        <v-stepper-header>
          <v-stepper-step :complete="o1 > 1" step="1" color="green">
            Choose Book
          </v-stepper-step>
          <v-divider></v-divider>
          <v-stepper-step :complete="o1 > 2" step="2" color="green">
            Search Inventory
          </v-stepper-step>
          <v-divider></v-divider>
          <v-stepper-step step="3" color="green">
            Finalize Order
          </v-stepper-step>
        </v-stepper-header>

        <v-stepper-items>
          <v-stepper-content step="1" class="pa-2">
            <choose-book @searchInventory="searchInventory"></choose-book>
          </v-stepper-content>

          <v-stepper-content step="2">
            <v-card class="mb-12">
              <v-card-text></v-card-text>
              <v-card-actions>
                <v-btn color="green" text @click="finalizeOrder">
                  Continue
                </v-btn>

                <v-btn text @click="o1--">
                  Back
                </v-btn>
              </v-card-actions>
            </v-card>
          </v-stepper-content>

          <v-stepper-content step="3">
            <v-card class="mb-12">
              <v-card-text></v-card-text>
              <v-card-actions>
                <v-btn text color="green" @click="orderBook">
                  Continue
                </v-btn>
                <v-btn text @click="o1--">
                  Back
                </v-btn>
              </v-card-actions>
            </v-card>
          </v-stepper-content>
        </v-stepper-items>
      </v-stepper>
    </v-card>
  </v-dialog>
</template>

<script>
import ChooseBook from "./ChooseBook.vue";
export default {
  components: { ChooseBook },
  data: () => ({
    dialog: false,
    o1: 1,
    selectedNewBook: null,
  }),
  methods: {
    close() {
      this.o1 = 1;
      this.dialog = false;
    },
    searchInventory(book) {
      console.log("search inventory");
      this.o1++; // after success
      this.selectedNewBook = book;
      console.log("order book L " + this.selectedNewBook);
    },
    finalizeOrder() {
      console.log("finalize order");
      this.o1++;
    },
    orderBook() {
      console.log("order book");
      //this.$emit("newAdded"); if successfully ordered
    },
  },
};
</script>

<style></style>
