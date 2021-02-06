<template>
  <v-dialog
    v-model="dialog"
    :fullscreen="$vuetify.breakpoint.mobile"
    persistent
    overlay-opacity="1"
    :height="$vuetify.breakpoint.mobile ? '100vh' : '50vh'"
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
    <v-toolbar flat>
      <v-toolbar-title>My Order History</v-toolbar-title>
      <v-divider class="mx-5" inset vertical></v-divider>
      <v-spacer />
      <v-btn icon @click="close">
        <v-icon>mdi-close-circle</v-icon>
      </v-btn>
    </v-toolbar>
    <v-card elevation="0">
      <v-timeline dense clipped>
        <v-timeline-item
          v-for="order in history"
          :key="order.orderId"
          class="mb-4"
          color="green"
          small
        >
          <v-card class="elevation-2">
            <v-card-title>
              <span
                class="green--text mr-5"
                v-text="order.orderedAt.substring(0, 10)"
              ></span>
              {{ order.bookName }}
            </v-card-title>
            <v-card-text>
              <v-row>
                <v-col cols="12" sm="6" md="3">
                  <span>Order Id :</span>
                  <span v-text="order.orderId" class="green--text ml-2"></span>
                </v-col>
                <v-col cols="12" sm="6" md="3">
                  <span>Reference Id :</span>
                  <span
                    v-text="order.bookReferenceId"
                    class="green--text ml-2"
                  ></span>
                </v-col>
                <v-col cols="12" sm="6" md="3">
                  <span>Collected At :</span>
                  <span
                    v-text="order.collectedAt"
                    class="green--text ml-2"
                  ></span>
                </v-col>
                <v-col cols="12" sm="6" md="3">
                  <span>Returned At:</span>
                  <span
                    v-text="order.returnedAt"
                    class="green--text ml-2"
                  ></span>
                </v-col>
              </v-row>
            </v-card-text>
          </v-card>
        </v-timeline-item>
      </v-timeline>
    </v-card>
  </v-dialog>
</template>

<script>
import * as orders from "@/service/order";
export default {
  data: () => ({
    dialog: false,
    loading: false,
    history: []
  }),
  methods: {
    close() {
      this.dialog = false;
    },
    findAllOrdersHistory() {
      this.loading = true;
      orders
        .getUsersOrderHistory()
        .then(data => {
          this.loading = false;
          this.history.length = 0;
          this.history.push(...data);
        })
        .catch(err => {
          console.log(err);
          this.loading = false;
          this.$store.commit("setErrorMessage", err.error_description);
        });
    }
  },
  watch: {
    dialog() {
      if (this.dialog) {
        this.findAllOrdersHistory();
      }
    }
  }
};
</script>

<style></style>
