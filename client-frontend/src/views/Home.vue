<template>
  <v-container class="pa-0">
    <v-row class="_error_row" v-if="error">
      <v-col cols="12">
        <v-snackbar v-model="error" multi-line timeout="5000">
          {{ errorMessage }}
          <template v-slot:action="{ attrs }">
            <v-btn color="red" text v-bind="attrs" @click="error = false">
              Close
            </v-btn>
          </template>
        </v-snackbar>
      </v-col>
    </v-row>
    <v-row>
      <v-col>
        <v-btn @click="get">Get</v-btn>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
import * as userService from "../service/user";
export default {
  data: () => ({
    error: false,
  }),
  methods: {
    get() {
      userService
        .getAllUsers()
        .then((data) => console.log(data))
        .catch((err) => console.log(err));
    },
  },
};
</script>

<style></style>
