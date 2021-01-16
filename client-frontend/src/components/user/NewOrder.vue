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
          <!-- Choose Book -->
          <v-stepper-content step="1" class="pa-2">
            <choose-book @searchInventory="searchInventory"></choose-book>
          </v-stepper-content>

          <!-- Search Inventory -->
          <v-stepper-content step="2">
            <search-inventory
              :selectedNewBook="selectedNewBook"
              @goBack="goBack"
              @finalizeOrder="finalizeOrder"
            ></search-inventory>
          </v-stepper-content>

          <!-- Order Book -->
          <v-stepper-content step="3">
            <v-card class="mb-12">
              <v-card-text>
                <v-card-title>Your Order Details</v-card-title>
                <v-list v-if="selectedNewBook">
                  <v-list-item>
                    <v-list-item-icon>
                      <v-icon>mdi-book-open-page-variant</v-icon>
                    </v-list-item-icon>
                    <v-list-item-content>
                      <v-list-item-title>Name</v-list-item-title>
                      <v-list-item-subtitle>
                        <span class="green--text">
                          {{ selectedNewBook.name }}
                        </span>
                      </v-list-item-subtitle>
                    </v-list-item-content>
                  </v-list-item>
                  <v-list-item>
                    <v-list-item-icon>
                      <v-icon>mdi-account</v-icon>
                    </v-list-item-icon>
                    <v-list-item-content>
                      <v-list-item-title>Author</v-list-item-title>
                      <v-list-item-subtitle>
                        <span class="green--text">
                          {{ selectedNewBook.author }}
                        </span>
                      </v-list-item-subtitle>
                    </v-list-item-content>
                  </v-list-item>
                  <v-list-item>
                    <v-list-item-icon>
                      <v-icon>mdi-home</v-icon>
                    </v-list-item-icon>
                    <v-list-item-content>
                      <v-list-item-title>Publication</v-list-item-title>
                      <v-list-item-subtitle>
                        <span class="green--text">
                          {{ selectedNewBook.publication }}
                        </span>
                      </v-list-item-subtitle>
                    </v-list-item-content>
                  </v-list-item>
                </v-list>
              </v-card-text>
              <v-card-actions>
                <v-btn text color="green" @click="orderBook">
                  Order
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
import SearchInventory from "./SearchInventory.vue";
import * as orders from "../../service/order";
export default {
  components: { ChooseBook, SearchInventory },
  data: () => ({
    dialog: false,
    o1: 1,
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
      this.o1 = 1;
      this.dialog = false;
      this.$store.commit("setErrorMessage", null);
    },
    searchInventory(book) {
      this.goContinue();
      this.selectedNewBook = book;
    },
    finalizeOrder() {
      console.log("finalize order");
      this.goContinue();
    },
    orderBook() {
      console.log("order book");
      orders
        .orderNewBook(this.selectedNewBook)
        .then(() => {
          this.$emit("newAdded");
          this.close();
        })
        .catch(err => {
          this.$store.commit("setErrorMessage", err.error_description);
        });
    },
    goBack() {
      this.o1--;
    },
    goContinue() {
      this.o1++;
    }
  }
};
</script>

<style></style>
