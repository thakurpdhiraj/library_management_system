<template>
  <v-dialog v-model="dialog" max-width="60%" persistent>
    <template v-slot:activator="{ on, attrs }">
      <v-btn color="primary" dark class="mb-2" v-bind="attrs" v-on="on">
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
