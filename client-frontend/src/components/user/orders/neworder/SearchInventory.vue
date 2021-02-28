<template>
  <v-card class="mb-12" :loading="loading">
    <v-card-text v-if="selectedNewBook">
      <v-list>
        <v-list-item>
          <v-list-item-content>
            <v-list-item-title>
              Searching inventory for :
              <span class="green--text">{{ selectedNewBook.name }}</span>
            </v-list-item-title>
          </v-list-item-content>
        </v-list-item>
        <v-list-item>
          <v-list-item-content>
            <v-list-item-title>
              <p v-if="count > 0">
                Book left : <span class="green--text">{{ count }}</span>
              </p>
              <p v-else>No book left in inventory.</p>
              <v-btn class="pa-0" text @click="searchInventory">
                <v-icon>mdi-refresh</v-icon> Refresh
              </v-btn>
            </v-list-item-title>
          </v-list-item-content>
        </v-list-item>
      </v-list>
    </v-card-text>
    <v-card-actions>
      <v-btn color="green" text :disabled="count <= 0" @click="finalizeOrder">
        Continue
      </v-btn>

      <v-btn text @click="goBack">
        Back
      </v-btn>
    </v-card-actions>
  </v-card>
</template>

<script>
import * as inventoryService from "@/service/inventory";
export default {
  name: "SearchInventory",
  data() {
    return {
      count: 0,
      loading: false
    };
  },
  props: {
    selectedNewBook: {
      type: Object,
      default: null
    }
  },
  methods: {
    finalizeOrder() {
      this.$emit("finalizeOrder");
    },
    goBack() {
      this.$emit("goBack");
    },
    searchInventory() {
      this.loading = true;
      inventoryService
        .findAvailableBookCount(this.selectedNewBook.id)
        .then(data => {
          this.count = data;
          this.loading = false;
          console.log("Search Inventory for", this.count);
        })
        .catch(err => {
          console.log(err);
          this.loading = false;
          this.count = 0;
        });
    }
  },
  watch: {
    selectedNewBook: {
      handler() {
        if (this.selectedNewBook) {
          this.searchInventory();
        }
      }
    }
  }
};
</script>

<style></style>
