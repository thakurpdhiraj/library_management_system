<template>
  <v-card :loading="loading" v-if="user">
    <v-alert
      dense
      outlined
      dismissible
      transition="scale-transition"
      :type="isError ? 'error' : 'success'"
      v-if="message"
    >
      {{ message }}
    </v-alert>
    <v-form @submit.prevent="updateUser" v-model="valid" ref="form">
      <v-card-text>
        <v-container>
          <v-row>
            <v-col md="6" cols="12">
              <v-text-field
                label="Name*"
                v-model="user.name"
                :rules="[rules.required]"
              ></v-text-field>
            </v-col>
            <v-col md="6" cols="12">
              <v-text-field
                label="Username"
                v-model="user.username"
                disabled
              ></v-text-field>
            </v-col>
            <v-col cols="12">
              <v-text-field
                label="Email*"
                v-model="user.email"
                :rules="[rules.required, rules.email]"
              ></v-text-field>
            </v-col>

            <v-col md="6" cols="12">
              <v-text-field
                label="Joined At"
                v-model="user.createdAt"
                disabled
              ></v-text-field>
            </v-col>
            <v-col md="6" cols="12">
              <v-text-field
                label="Updated At"
                v-model="user.updatedAt"
                disabled
              ></v-text-field>
            </v-col>
          </v-row>
        </v-container>
      </v-card-text>

      <v-card-actions>
        <v-btn :disabled="!valid" color="green darken-1" text type="submit">
          Update
        </v-btn>
        <v-spacer></v-spacer>
      </v-card-actions>
    </v-form>
  </v-card>
</template>

<script>
import * as userService from "@/service/user";
import * as ruleUtil from "@/util/ruleUtil";
export default {
  name: "EditAccount",
  data() {
    return {
      user: null,
      valid: false,
      message: null,
      isError: false,
      loading: false,
      rules: ruleUtil.rules
    };
  },
  methods: {
    updateUser() {
      this.loading = true;
      this.isError = false;
      this.message = null;
      userService
        .updateMe(this.user)
        .then(data => {
          this.loading = false;
          this.user = data;
          this.message = "Details updated successfully!";
        })
        .catch(err => {
          this.loading = false;
          this.isError = true;
          this.message = err.error_description;
        });
    }
  },
  mounted() {
    this.isError = false;
    this.loading = true;
    userService
      .getLoggedInUser()
      .then(data => {
        this.loading = false;
        this.user = data;
      })
      .catch(err => {
        console.log(err);
        this.loading = false;
        this.isError = true;
        this.message = err.error_description;
      });
  }
};
</script>

<style></style>
