<template>
  <v-card :loading="loading" flat>
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
    <v-form @submit.prevent="changePassword" v-model="valid" ref="form">
      <v-card-text>
        <v-container>
          <v-row>
            <v-col cols="12">
              <v-text-field
                label="Password*"
                v-model="cred.password"
                type="password"
                :rules="[
                  rules.required,
                  rules.minLength(4, cred.password, 'characters')
                ]"
              ></v-text-field>
            </v-col>
            <v-col cols="12">
              <v-text-field
                label="Confirm Password*"
                v-model="cred.confirmPassword"
                :append-icon="showPass ? 'mdi-eye' : 'mdi-eye-off'"
                :type="showPass ? 'text' : 'password'"
                @click:append="showPass = !showPass"
                :error-messages="confirmErrorMessage"
              ></v-text-field>
            </v-col>
          </v-row>
        </v-container>
      </v-card-text>

      <v-card-actions>
        <v-btn color="green darken-1" :disabled="!valid" text type="submit">
          Change
        </v-btn>
      </v-card-actions>
    </v-form>
  </v-card>
</template>

<script>
import * as userService from "@/service/user";
import * as ruleUtil from "@/util/ruleUtil";
export default {
  name: "ChangePassword",
  data() {
    return {
      loading: false,
      rules: ruleUtil.rules,
      showPass: false,
      cred: {
        password: null,
        confirmPassword: null
      },
      confirmErrorMessage: null,
      valid: false,
      message: null,
      isError: false
    };
  },
  methods: {
    changePassword() {
      this.loading = true;
      this.message = null;
      this.isError = false;
      userService
        .changePassword(this.cred)
        .then(() => {
          this.loading = false;
          this.$refs.form.reset();
          this.message = "Password changed successfully!";
        })
        .catch(err => {
          this.loading = false;
          this.isError = true;
          this.message = err.error_description;
        });
    }
  },
  watch: {
    cred: {
      handler() {
        if (this.cred.password !== this.cred.confirmPassword) {
          this.confirmErrorMessage = "Passwords do not match";
          this.valid = false;
        } else {
          this.confirmErrorMessage = null;
          this.valid = true;
        }
      },
      deep: true
    }
  }
};
</script>

<style></style>
