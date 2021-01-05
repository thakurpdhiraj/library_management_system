<template>
  <v-card class="mb-12" :loading="loading">
    <v-card-text>
      <v-list shaped>
        <v-list-item-group v-model="selectedNewBook" v-if="books">
          <v-virtual-scroll
            :bench="books.length"
            :items="books"
            item-height="70"
            height="300"
          >
            <template v-slot="{ item }">
              <v-list-item
                :key="item.id"
                :value="item"
                active-class="green--text text--accent-4"
              >
                <template v-slot:default="{ active }">
                  <v-list-item-content>
                    <v-list-item-title v-text="item.name"></v-list-item-title>
                    <v-list-item-subtitle
                      v-text="
                        'Author: ' +
                          item.author +
                          ', Publisher: ' +
                          item.publication
                      "
                    ></v-list-item-subtitle>
                    <v-list-item-subtitle
                      v-if="active"
                      v-text="item.summary"
                    ></v-list-item-subtitle>
                  </v-list-item-content>
                  <v-list-item-action>
                    <v-checkbox
                      :input-value="active"
                      color="dark-green"
                    ></v-checkbox>
                  </v-list-item-action>
                </template>
              </v-list-item>
            </template>
          </v-virtual-scroll>
        </v-list-item-group>
      </v-list>
    </v-card-text>
    <v-card-actions>
      <v-btn
        color="green"
        text
        @click="searchInventory"
        :disabled="!isSelected"
      >
        Continue
      </v-btn>
      <span class="ml-5" v-if="selectedNewBook">to order :</span>
      <span class="ml-2" v-text="orderName"></span>
    </v-card-actions>
  </v-card>
</template>

<script>
import * as books from "../../service/book";
export default {
  data() {
    return {
      filterData: [
        { value: "name", label: "Name" },
        { value: "author", label: "Author" },
        { value: "publication", label: "Publication" },
      ],
      textFields: [],
      books: [],
      selectedNewBook: null,
      benched: 0,
      loading: false,
    };
  },
  methods: {
    searchInventory() {
      this.$emit("searchInventory", this.selectedNewBook.name);
    },
    addFilter() {
      // create button to add text fields for different filter in filterData
      //TODO: this.textFields.push({})
    },
    searchBooks() {
      //cache result?
      this.loading = true;
      console.log("search book");
      books
        .findAllBooks()
        .then((data) => {
          this.loading = false;
          this.books = data;
        })
        .catch((err) => {});
    },
  },
  computed: {
    isSelected() {
      return this.selectedNewBook != null;
    },
    orderName() {
      if (this.selectedNewBook != null)
        return this.selectedNewBook.name + ",  " + this.selectedNewBook.author;
      else return "";
    },
  },
  created() {
    this.searchBooks();
  },
};
</script>

<style></style>
