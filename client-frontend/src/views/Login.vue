<template>
  <v-container class="pa-5">
    <v-row justify="space-around">
      <v-dialog v-model="dialog" persistent max-width="600px">
        <v-card :loading="loading">
          <v-card-title class="headline">
            Login
          </v-card-title>

          <v-card-text>
            <v-container>
              <v-row v-if="error">
                <v-col>
                  <v-alert dense outlined type="error">
                    {{ errorMessage }}
                  </v-alert>
                </v-col>
              </v-row>
              <v-row>
                <v-col cols="12">
                  <v-text-field
                    label="Username*"
                    v-model="cred.username"
                    :rules="[rules.required]"
                  ></v-text-field>
                </v-col>
                <v-col cols="12">
                  <v-text-field
                    label="Password*"
                    v-model="cred.password"
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
              @click="login"
            >
              Login
            </v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>
    </v-row>
  </v-container>
</template>

<script>
import * as auth from "../service/auth";
export default {
  data: () => ({
    cred: {
      username: null,
      password: null,
    },
    showPass: false,
    error: false,
    dialog: true,
    rules: {
      required: (value) => !!value || "Required.",
    },
    loading: false,
  }),
  methods: {
    login() {
      this.loading = true;
      this.$store.commit("setErrorMessage", "");
      this.error = false;
      auth
        .login(this.cred)
        .then(() => {
          this.loading = false;
          this.dialog = false;
          this.$store.commit("loggedIn");
          this.$router.push(this.$route.query.redirect || "/");
        })
        .catch((err) => {
          console.log(err);
          this.loading = false;
          this.error = true;
          this.$store.commit("setErrorMessage", err.error_description);
        });
    },
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
        password.trim() != ""
      );
    },
  },
};
</script>

<style scoped></style>
