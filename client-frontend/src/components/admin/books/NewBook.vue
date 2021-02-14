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
        <v-col :loading="loading">
          <v-form
            @submit.prevent="saveBook"
            :loading="loading"
            v-model="valid"
            ref="form"
          >
            <v-card flat>
              <v-card-text>
                <v-container>
                  <v-row>
                    <v-col cols="12" sm="4">
                      <v-text-field
                        label="Name *"
                        clearable
                        v-model="book.name"
                        :rules="[rules.required]"
                      ></v-text-field>
                    </v-col>
                    <v-col cols="12" sm="4">
                      <v-text-field
                        label="Author *"
                        clearable
                        v-model="book.author"
                        :rules="[rules.required]"
                      ></v-text-field>
                    </v-col>
                    <v-col cols="12" sm="4">
                      <v-text-field
                        label="Pages *"
                        clearable
                        v-model="book.pages"
                        :rules="[rules.required, rules.number]"
                      ></v-text-field>
                    </v-col>
                    <v-col cols="12" sm="4">
                      <v-select
                        v-model="book.category"
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
                        v-model="book.publication"
                        :rules="[rules.required]"
                      ></v-text-field>
                    </v-col>
                    <v-col cols="12" sm="4">
                      <v-text-field
                        label="Published Year *"
                        clearable
                        v-model="book.publicationYear"
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
                        v-model="book.summary"
                      ></v-textarea>
                    </v-col>
                  </v-row>
                </v-container>
              </v-card-text>
              <v-card-actions>
                <v-btn color="green" dark :disabled="!valid" type="submit">
                  Add
                </v-btn>
                <v-btn color="yellow" text dark @click="clear">
                  Clear
                </v-btn>
              </v-card-actions>
            </v-card>
          </v-form>
        </v-col>
      </v-row>
    </v-container>
  </v-card>
</template>

<script>
import * as bookService from "@/service/book";
import * as ruleUtil from "@/util/ruleUtil";
export default {
  name: "NewBook",
  data() {
    return {
      book: {
        name: null,
        author: null,
        publication: null,
        pages: null,
        publicationYear: null,
        summary: null,
        category: null
      },
      valid: false,
      loading: false,
      rules: ruleUtil.rules,
      message: null,
      isError: false,
      categories: []
    };
  },
  methods: {
    saveBook() {
      this.message = null;
      this.loading = true;
      bookService
        .saveBook(this.book)
        .then(data => {
          this.isError = false;
          this.loading = false;
          this.message = "Book " + data.name + " added successfully";
          this.$refs.form.reset();
        })
        .catch(err => {
          this.loading = false;
          this.isError = true;
          this.message = err.error_description;
        });
    },
    clear() {
      this.$refs.form.reset();
    }
  },
  mounted() {
    // search category
    this.loading = true;
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

<style></style>
