<template>
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
</template>

<script>
import * as bookService from "@/service/book";
import * as ruleUtil from "@/util/ruleUtil";
export default {
  data() {
    return {
      loading: false,
      valid: false,
      message: null,
      isError: false,
      rules: ruleUtil.rules
    };
  },
  props: {
    selectedBook: {
      type: Object,
      default: null
    },
    editDialog: {
      type: Boolean,
      default: false
    },
    categories: {
      type: Array,
      default: () => []
    }
  },
  methods: {
    editBook() {
      this.message = null;
      bookService
        .updateBook(this.selectedBook)
        .then(() => {
          this.$emit("editSuccess");
        })
        .catch(err => {
          this.isError = true;
          this.message = err.error_description;
        });
    },
    closeEdit() {
      this.$emit("closeEdit");
    },
    clearForm() {
      this.$refs.editForm.reset();
    }
  }
};
</script>

<style></style>
