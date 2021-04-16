<template>
  <v-dialog v-model="dialog" fullscreen persistent overlay-opacity="1">
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
    <v-card>
      <v-data-iterator
        :items="history"
        disable-pagination
        hide-default-footer
        :loading="loading"
      >
        <template v-slot:default="props">
          <v-container>
            <v-row>
              <v-col
                v-for="order in props.items"
                :key="order.orderId"
                cols="12"
                sm="6"
                md="4"
                lg="4"
              >
                <v-card elevation="10">
                  <v-card-title class="subheading font-weight-bold green--text">
                    {{ order.bookName }}
                  </v-card-title>
                  <v-divider></v-divider>
                  <v-list dense>
                    <v-list-item>
                      <v-list-item-content>
                        Book Reference Id:
                      </v-list-item-content>
                      <v-list-item-content class="align-end green--text">
                        {{ order.bookReferenceId }}
                      </v-list-item-content>
                    </v-list-item>
                    <v-list-item>
                      <v-list-item-content>
                        ISBN:
                      </v-list-item-content>
                      <v-list-item-content class="align-end green--text">
                        {{ order.bookIsbn }}
                      </v-list-item-content>
                    </v-list-item>
                    <v-list-item>
                      <v-list-item-content>
                        Ordered At:
                      </v-list-item-content>
                      <v-list-item-content class="align-end">
                        {{ order.orderedAt }}
                      </v-list-item-content>
                    </v-list-item>
                    <v-list-item>
                      <v-list-item-content>
                        Collected At:
                      </v-list-item-content>
                      <v-list-item-content class="align-end">
                        {{ order.collectedAt }}
                      </v-list-item-content>
                    </v-list-item>
                    <v-list-item>
                      <v-list-item-content>
                        Returned At:
                      </v-list-item-content>
                      <v-list-item-content class="align-end">
                        {{ order.returnedAt }}
                      </v-list-item-content>
                    </v-list-item>
                  </v-list>
                </v-card>
              </v-col>
            </v-row>
          </v-container>
        </template>
      </v-data-iterator>
    </v-card>
  </v-dialog>
</template>

<script>
import * as orderService from "@/service/order";
export default {
  name: "MyOrdersHistory",
  data() {
    return {
      dialog: false,
      loading: false,
      history: []
    };
  },
  methods: {
    close() {
      this.dialog = false;
    },
    findAllOrdersHistory() {
      this.loading = true;
      orderService
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
