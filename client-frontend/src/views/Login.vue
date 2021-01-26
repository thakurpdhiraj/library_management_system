<template>
  <v-container class="pa-5">
    <v-row justify="space-around">
      <v-dialog v-model="dialog" persistent max-width="600px">
        <v-card :loading="loading">
          <v-form @submit.prevent="login">
            <v-card-title class="headline">
              Login
            </v-card-title>

            <v-card-text>
              <v-container>
                <v-row v-if="errorMessage != null">
                  <v-col>
                    <v-alert
                      dense
                      outlined
                      transition="scale-transition"
                      type="error"
                    >
                      {{
                        errorMessage != null
                          ? errorMessage
                          : "Something went wrong. Please try again!"
                      }}
                    </v-alert>
                  </v-col>
                </v-row>
                <v-row>
                  <v-col cols="12">
                    <v-text-field
                      label="Username*"
                      v-model="cred.username"
                      placeholder=" "
                      :rules="[rules.required]"
                    ></v-text-field>
                  </v-col>
                  <v-col cols="12">
                    <v-text-field
                      label="Password*"
                      v-model="cred.password"
                      placeholder=" "
                      :append-icon="showPass ? 'mdi-eye' : 'mdi-eye-off'"
                      :type="showPass ? 'text' : 'password'"
                      @click:append="showPass = !showPass"
                      :rules="[rules.required]"
                    ></v-text-field>
                  </v-col>
                </v-row>
              </v-container>
            </v-card-text>

            <v-card-actions>
              <v-spacer></v-spacer>
              <v-btn
                :disabled="!valid"
                color="green darken-1"
                text
                type="submit"
              >
                Login
              </v-btn>
            </v-card-actions>
          </v-form>
        </v-card>
      </v-dialog>
    </v-row>
  </v-container>
</template>

<script>
import * as auth from "../service/auth";
import * as util from "../util/authUtil";
export default {
  data: () => ({
    cred: {
      username: null,
      password: null
    },
    showPass: false,
    error: false,
    dialog: false,
    rules: {
      required: value => !!value || "Required."
    },
    loading: false
  }),
  methods: {
    login() {
      this.loading = true;
      this.$store.commit("setErrorMessage", null);
      this.error = false;
      auth
        .login(this.cred)
        .then(() => {
          this.loading = false;
          this.dialog = false;
          let url = "/";
          if (util.isAdmin()) {
            url = "/admin";
            this.$store.commit("setHome", url);
          }
          this.$router.push(this.$route.query.redirect || url);
        })
        .catch(err => {
          this.loading = false;
          this.$store.commit("setErrorMessage", err.error_description);
        });
    }
  },
  computed: {
    errorMessage() {
      return this.$store.getters.getErrorMessage;
    },
    valid() {
      let username = this.cred.username;
      let password = this.cred.password;
      return (
        username != null &&
        username.trim() != "" &&
        password != null &&
        password.trim() != "" &&
        !this.loading
      );
    }
  },
  beforeMount() {
    if (util.isAuthenticated()) {
      let url = "/";
      if (util.isAdmin()) {
        url = "/admin/orders";
      }
      this.$router.push(this.$route.query.redirect || url);
    } else {
      this.dialog = true;
    }
  }
};
</script>

<style scoped></style>
