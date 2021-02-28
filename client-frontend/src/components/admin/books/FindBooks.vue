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
        <v-dialog v-model="editDialog" overlay-opacity="0.8" persistent>
          <v-form
            @submit.prevent="editBook"
            :loading="loading"
            v-model="valid"
            ref="editForm"
          >
            <v-card flat v-if="selectedBook">
              <v-card-text>
                <v-container>
                  <v-row>
                    <v-col cols="12" sm="4">
                      <v-text-field
                        label="Name *"
                        clearable
                        v-model="selectedBook.name"
                        :rules="[rules.required]"
                      ></v-text-field>
                    </v-col>
                    <v-col cols="12" sm="4">
                      <v-text-field
                        label="Author *"
                        clearable
                        v-model="selectedBook.author"
                        :rules="[rules.required]"
                      ></v-text-field>
                    </v-col>
                    <v-col cols="12" sm="4">
                      <v-text-field
                        label="Pages *"
                        clearable
                        v-model="selectedBook.pages"
                        :rules="[rules.required, rules.number]"
                      ></v-text-field>
                    </v-col>
                    <v-col cols="12" sm="4">
                      <v-select
                        v-model="selectedBook.category"
                        item-text="name"
                        return-object
                        :items="categories"
                        :rules="[rules.required]"
                        label="Category *"
                      ></v-select>
                    </v-col>
                    <v-col cols="12" sm="4">
                      <v-text-field
                        label="Publication *"
                        clearable
                        v-model="selectedBook.publication"
                        :rules="[rules.required]"
                      ></v-text-field>
                    </v-col>
                    <v-col cols="12" sm="4">
                      <v-text-field
                        label="Published Year *"
                        clearable
                        v-model="selectedBook.publicationYear"
                        :rules="[rules.year]"
                      ></v-text-field>
                    </v-col>
                    <v-col cols="12">
                      <v-textarea
                        label="Summary"
                        rows="4"
                        counter="500"
                        maxlength="500"
                        clearable
                        no-resize
                        v-model="selectedBook.summary"
                      ></v-textarea>
                    </v-col>
                  </v-row>
                </v-container>
              </v-card-text>
              <v-card-actions>
                <v-btn color="green" text dark :disabled="!valid" type="submit">
                  Update
                </v-btn>
                <v-btn color="blue darken-1" text dark @click="closeEdit">
                  Close
                </v-btn>
              </v-card-actions>
            </v-card>
          </v-form>
        </v-dialog>
        <v-dialog
          v-model="deleteDialog"
          :max-width="$vuetify.breakpoint.mobile ? '100vw' : '50vw'"
          overlay-opacity="0.97"
          persistent
        >
          <v-card v-if="selectedBook">
            <v-card-title>
              Are you sure you want to delete
              <p class="red--text my-0 mx-3 pa-0">{{ selectedBook.name }}</p>
              ?
            </v-card-title>
            <v-card-actions>
              <v-btn color="green darken-1" text @click="deleteBook">
                OK
              </v-btn>
              <v-btn color="grey darken-1" text @click="closeDelete">
                Cancel
              </v-btn>
            </v-card-actions>
          </v-card>
        </v-dialog>
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
export default {
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
      editDialog: false
    };
  },
  methods: {
    getBook() {
      this.loading = true;
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
      this.editDialog = true;
    },
    closeEdit() {
      this.selectedBook = null;
      this.editDialog = false;
    },
    editBook() {
      this.message = null;
      bookService
        .updateBook(this.selectedBook)
        .then(() => {
          this.editDialog = false;
          this.selectedBook = null;
          this.getBook();
          this.message = "Book updated successfully";
        })
        .catch(err => {
          this.editDialog = false;
          this.isError = true;
          this.message = err.error_description;
        });
    },
    showDelete(item) {
      this.selectedBook = item;
      this.deleteDialog = true;
    },
    closeDelete() {
      this.selectedBook = null;
      this.deleteDialog = false;
    },
    deleteBook() {
      this.message = null;
      bookService
        .deleteBook(this.selectedBook.id)
        .then(() => {
          this.deleteDialog = false;
          this.selectedBook = null;
          this.getBook();
          this.message = "Book deleted successfully";
        })
        .catch(err => {
          this.deleteDialog = false;
          this.isError = true;
          this.message = err.error_description;
        });
    },
    clearForm() {
      this.$refs.editForm.reset();
    }
  },
  mounted() {
    this.getBook();
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
