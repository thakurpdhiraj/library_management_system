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
        <v-col cols="12" sm="3" v-if="!showForm">
          <v-btn
            block
            color="primary"
            dark
            class="w-50"
            @click="showForm = true"
          >
            Refine Search
          </v-btn>
        </v-col>
        <v-col cols="12" v-else>
          <v-form
            @submit.prevent="getBooks"
            :loading="loading"
            v-model="valid"
            ref="searchForm"
          >
            <v-row>
              <v-col cols="12" sm="3">
                <v-text-field
                  label="Book Id"
                  clearable
                  v-model="book.id"
                ></v-text-field>
              </v-col>
              <v-col cols="12" sm="3">
                <v-text-field
                  label="Book Name"
                  clearable
                  v-model="book.name"
                ></v-text-field>
              </v-col>
              <v-col cols="12" sm="3">
                <v-text-field
                  label="Author Name"
                  clearable
                  v-model="book.author"
                ></v-text-field>
              </v-col>
              <v-col cols="12" sm="3">
                <v-text-field
                  label="Publication Name"
                  clearable
                  v-model="book.publication"
                ></v-text-field>
              </v-col>
              <v-col cols="12" sm="3">
                <v-text-field
                  label="ISBN"
                  clearable
                  v-model="book.isbn"
                ></v-text-field>
              </v-col>
              <v-col cols="12" sm="3">
                <v-btn color="green" text dark :disabled="!valid" type="submit">
                  Search
                </v-btn>
                <v-btn color="blue darken-1" text dark @click="clearForm">
                  Clear
                </v-btn>
              </v-col>
            </v-row>
          </v-form>
        </v-col>
      </v-row>
    </v-container>
    <v-data-table
      :headers="headers"
      :items="bookList"
      class="elevation-20"
      item-key="id"
      :loading="loading"
      fixed-header
      height="50vh"
      :footer-props="{
        'items-per-page-text': 'Books per page'
      }"
    >
      <template v-slot:top>
        <EditBooks
          :editDialog="editDialog"
          :selectedBook="selectedBook"
          :categories="categories"
          @closeEdit="closeEdit"
          @editSuccess="editSuccess"
        />
        <DeleteBooks
          @closeDelete="closeDelete"
          @deleteSuccess="deleteSuccess"
          @deleteFail="deleteFail"
          :deleteDialog="deleteDialog"
          :selectedBook="selectedBook"
        />
      </template>
      <template v-slot:[`item.actions`]="{ item }">
        <v-icon small class="mr-2" @click="showEdit(item)">
          mdi-pencil
        </v-icon>
        <v-icon small @click="showDelete(item)">
          mdi-delete
        </v-icon>
      </template>
    </v-data-table>
  </v-card>
</template>

<script>
import * as bookService from "@/service/book";
import * as ruleUtil from "@/util/ruleUtil";
import DeleteBooks from "./DeleteBooks.vue";
import EditBooks from "./EditBooks.vue";
export default {
  components: { DeleteBooks, EditBooks },
  name: "FindBooks",
  data() {
    return {
      loading: false,
      valid: false,
      message: null,
      isError: false,
      book: {
        id: null,
        name: null,
        author: null,
        publication: null,
        isbn: null,
        categoryId: null
      },
      selectedBook: null,
      bookList: [],
      categories: [],
      rules: ruleUtil.rules,
      headers: [
        {
          text: "Id",
          align: "start",
          value: "id",
          class: "indigo--text darken-4"
        },
        {
          text: "Name",
          value: "name",
          class: "indigo--text darken-4"
        },
        {
          text: "ISBN",
          value: "isbn",
          class: "indigo--text darken-4"
        },
        {
          text: "Author",
          value: "author",
          class: "indigo--text darken-4"
        },
        {
          text: "Category",
          value: "category.name",
          class: "indigo--text darken-4"
        },
        {
          text: "Publication",
          value: "publication",
          class: "indigo--text darken-4"
        },
        {
          text: "Added at",
          value: "addedAt",
          class: "indigo--text darken-4"
        },
        {
          text: "Action",
          value: "actions",
          class: "indigo--text darken-4"
        }
      ],
      deleteDialog: false,
      editDialog: false,
      showForm: true
    };
  },
  methods: {
    getBooks() {
      this.loading = true;
      this.message = null;
      this.showForm = false;
      bookService
        .findAllBooks(this.book)
        .then(data => {
          this.bookList = data;
          this.loading = false;
        })
        .catch(err => {
          this.loading = false;
          this.isError = true;
          this.message = err.error_description;
        });
    },
    showEdit(item) {
      this.selectedBook = item;
      this.message = null;
      this.editDialog = true;
    },
    closeEdit() {
      this.selectedBook = null;
      this.editDialog = false;
    },
    editSuccess() {
      this.editDialog = false;
      this.selectedBook = null;
      this.message = "Book updated successfully";
    },
    showDelete(item) {
      this.selectedBook = item;
      this.message = null;
      this.deleteDialog = true;
    },
    closeDelete() {
      this.selectedBook = null;
      this.deleteDialog = false;
    },
    deleteSuccess() {
      this.deleteDialog = false;
      //this.getBook();
      const findIndex = this.bookList.findIndex(
        a => a.id === this.selectedBook.id
      );
      findIndex !== -1 && this.bookList.splice(findIndex, 1);
      this.selectedBook = null;
      this.message = "Book / Inventory deleted successfully";
    },
    deleteFail(err) {
      this.deleteDialog = false;
      this.isError = true;
      this.message = err.error_description;
    },
    clearForm() {
      this.$refs.searchForm.reset();
    }
  },
  mounted() {
    bookService
      .findAllCategories()
      .then(data => {
        this.loading = false;
        this.categories = data;
      })
      .catch(err => {
        this.loading = false;
        this.isError = true;
        this.message = err.error_description;
      });
  }
};
</script>

<style scoped>
.wb {
  word-break: break-all;
}
</style>
