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
                      <v-text-field
                        label="ISBN *"
                        clearable
                        v-model="book.isbn"
                        :rules="[rules.required]"
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
                    <v-col cols="12">
                      <v-switch
                        v-model="count.enable"
                        color="green"
                        label="Add Inventory & Generate Barcode"
                      ></v-switch>
                      <v-text-field
                        label="Number of books"
                        clearable
                        v-model="count.number"
                        :rules="[rules.number]"
                        v-if="count.enable"
                      ></v-text-field>
                    </v-col>
                  </v-row>
                </v-container>
              </v-card-text>
              <v-card-actions>
                <v-btn color="green" text dark :disabled="!valid" type="submit">
                  Add
                </v-btn>
                <v-btn color="blue darken-1" text dark @click="clear">
                  Clear
                </v-btn>
              </v-card-actions>
            </v-card>
          </v-form>
        </v-col>
      </v-row>
    </v-container>
    <v-dialog v-model="barcodeDialog" overlay-opacity="0.95" persistent>
      <v-card>
        <v-toolbar dark>
          <v-toolbar-title class="green--text">Print Barcode</v-toolbar-title>
          <v-spacer></v-spacer>
          <v-btn
            dark
            text
            color="green"
            @click="print()"
            :loading="downloading"
          >
            Print
          </v-btn>
          <v-btn icon dark @click="barcodeDialog = false">
            <v-icon>mdi-close</v-icon>
          </v-btn>
        </v-toolbar>
        <v-card-text>
          <div ref="barcodeDiv" class="mt-3">
            <span v-for="x in barcodeList" :key="x" v-html="x" class="pa-2">
            </span>
          </div>
        </v-card-text>
      </v-card>
    </v-dialog>
  </v-card>
</template>

<script>
import * as bookService from "@/service/book";
import * as inventoryService from "@/service/inventory";
import * as ruleUtil from "@/util/ruleUtil";
import { DOMImplementation, XMLSerializer } from "xmldom";
import JsBarcode from "jsbarcode";
import { jsPDF } from "jspdf";
import Canvg from "canvg";
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
        category: null,
        isbn: null
      },
      count: {
        enable: false,
        number: null
      },
      valid: false,
      loading: false,
      downloading: false,
      rules: ruleUtil.rules,
      message: null,
      isError: false,
      categories: [],
      barcodeDialog: false,
      barcodeList: []
    };
  },
  methods: {
    saveBook() {
      this.message = null;
      this.loading = true;
      bookService
        .saveBook(this.book)
        .then(data => {
          this.addInventory(data);
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
    addInventory(book) {
      if (this.count.enable) {
        let inventory = {
          bookId: book.id,
          isbn: book.isbn,
          categoryId: book.category.id
        };
        inventoryService
          .save(inventory, this.count.number)
          .then(data => {
            const xmlSerializer = new XMLSerializer();
            data.forEach(element => {
              const document = new DOMImplementation().createDocument(
                "http://www.w3.org/1999/xhtml",
                "html",
                null
              );
              const svgNode = document.createElementNS(
                "http://www.w3.org/2000/svg",
                "svg"
              );
              JsBarcode(svgNode, element.bookReferenceId, {
                xmlDocument: document
              });
              const svgText = xmlSerializer.serializeToString(svgNode);
              this.barcodeList.push(svgText);
            });

            this.barcodeDialog = true;
          })
          .catch(err => {
            this.loading = false;
            this.isError = true;
            this.message = err.error_description;
          });
      }
    },
    print() {
      this.downloading = true;
      const doc = new jsPDF("p", "pt", "a4");
      var width = doc.internal.pageSize.width;
      var height = doc.internal.pageSize.height;
      var padding = 20;
      var x = padding;
      var y = padding;
      var h = 100;
      var w = 150;
      this.barcodeList.forEach(svg => {
        var canvas = document.createElement("canvas");
        var context = canvas.getContext("2d");
        context.clearRect(0, 0, canvas.width, canvas.height);
        var v = Canvg.fromString(context, svg);
        v.start();
        var imgData = canvas.toDataURL("image/jpeg");
        // Generate PDF
        doc.addImage(imgData, "JPEG", x, y, w, h);
        if (x + 2 * w > width - padding) {
          x = padding;
          y += h + padding;
          if (y + h > height - padding) {
            doc.addPage();
            x = padding;
            y = padding;
          }
        } else {
          x += w + padding;
        }
      });
      doc.save("barcode.pdf");
      this.downloading = false;
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
