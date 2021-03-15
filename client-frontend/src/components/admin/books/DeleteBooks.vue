<template>
  <v-dialog
    v-model="deleteDialog"
    :max-width="$vuetify.breakpoint.mobile ? '100vw' : '50vw'"
    overlay-opacity="0.97"
    persistent
  >
    <v-card v-if="selectedBook">
      <v-alert v-if="errorMessage" dense outlined dismissible type="error">
        {{ errorMessage }}
      </v-alert>
      <v-card-title>
        Are you sure you want to delete
        <p class="red--text my-0 mx-3 pa-0">{{ selectedBook.name }}</p>
        ?
      </v-card-title>
      <v-card-text>
        <div>
          <v-alert type="info" colored-border color="blue" border="left" dense>
            Book cannot be deleted if one of the copies is loaned
          </v-alert>
        </div>
        <div>
          <v-switch
            v-model="showRef"
            label="Delete Inventory"
            color="success"
            hide-details
          ></v-switch>
        </div>
        <div v-if="showRef">
          <v-checkbox
            v-for="item in references"
            :key="item.bookReferenceId"
            v-model="bookReferencesToDelete"
            :label="item.bookReferenceId"
            :value="item.bookReferenceId"
            :disabled="!item.available"
          ></v-checkbox>
        </div>
      </v-card-text>
      <v-card-actions>
        <v-btn
          color="green darken-1"
          text
          :disabled="!valid"
          @click="deleteBook"
        >
          OK
        </v-btn>
        <v-btn text @click="closeDelete">
          Cancel
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script>
import * as bookService from "@/service/book";
import * as inventoryService from "@/service/inventory";
import find from "lodash/find";
export default {
  data() {
    return {
      bookReferencesToDelete: [],
      references: [],
      errorMessage: null,
      showRef: false
    };
  },
  computed: {
    valid() {
      if (this.showRef) {
        return this.bookReferencesToDelete.length > 0;
      } else {
        return find(this.references, { available: false }) === undefined;
      }
    }
  },
  props: {
    selectedBook: {
      type: Object,
      default: null
    },
    deleteDialog: {
      type: Boolean,
      default: false
    }
  },
  methods: {
    deleteBook() {
      console.log(this.selectedBook.id, this.bookReferencesToDelete);
      bookService
        .deleteBook(this.selectedBook.id, this.bookReferencesToDelete)
        .then(() => {
          this.$emit("deleteSuccess");
        })
        .catch(err => {
          this.$emit("deleteFail", err);
        });
    },
    closeDelete() {
      this.$emit("closeDelete");
    },
    findAllBookReferences() {
      inventoryService
        .findAllBookReferences(this.selectedBook.id)
        .then(data => {
          this.references = data;
          this.bookReferencesToDelete = [];
        })
        .catch(err => {
          this.errorMessage = err.error_description;
        });
    }
  },
  watch: {
    deleteDialog() {
      if (this.deleteDialog) {
        this.showRef = false;
        this.findAllBookReferences();
      }
    }
  }
};
</script>

<style></style>
