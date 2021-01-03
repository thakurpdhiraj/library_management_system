<template>
  <v-dialog
    v-model="dialog"
    :fullscreen="$vuetify.breakpoint.mobile"
    persistent
  >
    <template v-slot:activator="{ on, attrs }">
      <v-btn dark color="deep-purple darken-4" v-bind="attrs" v-on="on">
        <v-icon left dark>
          mdi-book-plus
        </v-icon>
        New Order
      </v-btn>
    </template>
    <v-card>
      <v-btn @click="close">Close</v-btn>
    </v-card>
  </v-dialog>
</template>

<script>
export default {
  data: () => ({
    dialog: false,
  }),
  methods: {
    close() {
      this.dialog = false;
      this.$emit("closeDialog");
      this.$emit("newAdded");
    },
  },
};
</script>

<style></style>
