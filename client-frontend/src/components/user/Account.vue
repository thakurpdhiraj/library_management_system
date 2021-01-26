<template>
  <v-container>
    <v-row>
      <v-col>
        <v-tabs>
          <v-tab><v-icon left>mdi-account</v-icon>Details</v-tab>
          <v-tab-item transition="slide-y-transition">
            <v-card :loading="loading" v-if="user">
              <v-form @submit.prevent="updateUser">
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
                          :rules="[rules.required]"
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
                  <v-spacer></v-spacer>
                  <v-btn color="green darken-1" text type="submit">
                    Update
                  </v-btn>
                </v-card-actions>
              </v-form>
            </v-card>
          </v-tab-item>

          <v-tab><v-icon left>mdi-lock</v-icon>Change Password</v-tab>
          <v-tab-item transition="slide-y-transition">
            <v-card :loading="loading" flat v-if="user">
              <v-form @submit.prevent="changePassword">
                <v-card-text>
                  <v-container>
                    <v-row>
                      <v-col cols="12">
                        <v-text-field
                          label="Password*"
                          v-model="password"
                          type="password"
                          :rules="[rules.required, rules.length]"
                        ></v-text-field>
                      </v-col>
                      <v-col cols="12">
                        <v-text-field
                          label="Confirm Password*"
                          v-model="confirmPassword"
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
                  <v-spacer></v-spacer>
                  <v-btn
                    color="green darken-1"
                    :disabled="!valid"
                    text
                    type="submit"
                  >
                    Change
                  </v-btn>
                </v-card-actions>
              </v-form>
            </v-card>
          </v-tab-item>
        </v-tabs>
        <v-snackbar :value="updateText" top timeout="4000" color="success">
          <v-icon>mdi-check-circle</v-icon>
          {{ updateText }}
          <template v-slot:action="{ attrs }">
            <v-btn color="white" text v-bind="attrs" @click="updateText = null">
              Close
            </v-btn>
          </template>
        </v-snackbar>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
import * as users from "../../service/user";
export default {
  computed: {
    cred() {
      return {
        password: this.password,
        confirmPassword: this.confirmPassword
      };
    }
  },
  data: () => ({
    user: null,
    loading: false,
    rules: {
      required: value => !!value || "Required.",
      length: value =>
        (!!value && value.length >= 4) || "Minimum 4 characters required"
    },
    showPass: false,
    password: null,
    confirmPassword: null,
    confirmErrorMessage: null,
    valid: false,
    updateText: null
  }),
  methods: {
    updateUser() {
      this.loading = true;
      this.updateText = null;
      users
        .updateMe(this.user)
        .then(data => {
          this.loading = false;
          this.user = data;
          this.updateText = "Details updated successfully!";
        })
        .catch(err => {
          console.log(err);
          this.loading = false;
          this.updateText = null;
          this.$store.commit("setErrorMessage", err.error_description);
        });
    },
    changePassword() {
      this.loading = true;
      this.updateText = null;
      users
        .changePassword(this.cred)
        .then(() => {
          this.loading = false;
          this.password = "";
          this.confirmPassword = "";
          this.updateText = "Password changed successfully!";
        })
        .catch(err => {
          console.log(err);
          this.loading = false;
          this.updateText = null;
          this.$store.commit("setErrorMessage", err.error_description);
        });
    }
  },
  mounted() {
    users
      .getLoggedInUser()
      .then(data => {
        this.loading = false;
        this.user = data;
      })
      .catch(err => {
        console.log(err);
        this.loading = false;
        this.$store.commit("setErrorMessage", err.error_description);
      });
  },
  watch: {
    cred: {
      handler() {
        if (this.password !== this.confirmPassword) {
          this.confirmErrorMessage = "Passwords do not match";
          this.valid = false;
        } else {
          this.confirmErrorMessage = null;
          this.valid = true;
        }
      }
    }
  }
};
</script>

<style></style>
