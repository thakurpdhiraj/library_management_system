<template>
  <v-container>
    <v-row v-if="successMessage">
      <v-col cols="12">
        <v-alert
          dense
          outlined
          dismissible
          transition="scale-transition"
          type="success"
        >
          {{ successMessage }}
        </v-alert>
      </v-col>
    </v-row>
    <v-row>
      <v-col :loading="loading">
        <v-form @submit.prevent="findUser">
          <v-container>
            <v-row>
              <v-col cols="12" sm="4">
                <v-text-field label="User ID" v-model="id"></v-text-field>
              </v-col>
              <v-col cols="12" sm="4">
                <v-btn color="green" dark :disabled="!id" text type="submit">
                  Search
                </v-btn>
              </v-col>
            </v-row>
          </v-container>
        </v-form>
        <v-card v-if="user" class="elevation-20">
          <v-card-text>
            <v-container>
              <v-row>
                <v-col cols="12" sm="4">
                  <v-text-field
                    label="User Id"
                    v-model="user.id"
                    disabled
                  ></v-text-field>
                </v-col>
                <v-col cols="12" sm="4">
                  <v-text-field
                    label="Username"
                    v-model="user.username"
                    disabled
                  ></v-text-field>
                </v-col>
                <v-col cols="12" sm="4">
                  <v-text-field
                    label="Created At"
                    v-model="user.createdAt"
                    disabled
                  ></v-text-field>
                </v-col>
                <v-col cols="12" sm="4">
                  <v-text-field
                    label="Name"
                    v-model="user.name"
                    :rules="[rules.required]"
                  ></v-text-field>
                </v-col>
                <v-col cols="12" sm="4">
                  <v-text-field
                    label="Email"
                    v-model="user.email"
                    :rules="[rules.required, rules.email]"
                  ></v-text-field>
                </v-col>
                <v-col cols="12" sm="4">
                  <v-select
                    v-model="user.userRoles"
                    :items="roles"
                    label="Roles"
                    multiple
                    :rules="[rules.minOne]"
                  ></v-select>
                </v-col>
                <v-col cols="12" sm="4">
                  <v-switch
                    v-model="user.enabled"
                    inset
                    label="Enabled"
                  ></v-switch>
                </v-col>
              </v-row>
            </v-container>
          </v-card-text>
          <v-card-actions>
            <v-btn color="green" dark text @click="updateUser">
              Update
            </v-btn>
            <v-dialog
              v-model="dialog"
              width="30vw"
              overlay-opacity="0.98"
              class="elevation-20"
            >
              <template v-slot:activator="{ on, attrs }">
                <v-btn
                  color="red lighten-2 ml-10"
                  dark
                  v-bind="attrs"
                  v-on="on"
                >
                  Delete
                </v-btn>
              </template>
              <v-card>
                <v-card-text>
                  Are you sure you want to delete ' {{ user.username }} ' ?
                </v-card-text>

                <v-divider></v-divider>

                <v-card-actions>
                  <v-spacer></v-spacer>
                  <v-btn color="red" text @click="deleteUser">
                    Yes
                  </v-btn>
                  <v-btn text @click="dialog = false">
                    No
                  </v-btn>
                </v-card-actions>
              </v-card>
            </v-dialog>
          </v-card-actions>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
import * as userService from "@/service/user";
export default {
  data() {
    return {
      id: null,
      user: null,
      loading: false,
      roles: ["ADMIN", "USER"],
      rules: {
        required: v => !!v || "Required",
        minOne: v => (!!v && v.length > 0) || "Atleast 1 role is required",
        email: v => {
          const pattern = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
          return pattern.test(v) || "Invalid e-mail.";
        }
      },
      successMessage: null,
      dialog: false
    };
  },
  methods: {
    findUser() {
      this.successMessage = null;
      this.loading = true;
      userService
        .getUser(this.id)
        .then(data => {
          this.$store.commit("setErrorMessage", null);
          this.user = data;
          this.loading = false;
        })
        .catch(err => {
          this.loading = false;
          this.user = null;
          this.$store.commit("setErrorMessage", err.error_description);
        });
    },
    updateUser() {
      this.successMessage = null;
      this.loading = true;
      userService
        .updateUser(this.user)
        .then(() => {
          this.successMessage = "User updated successfully";
          this.loading = false;
        })
        .catch(err => {
          this.loading = false;
          this.$store.commit("setErrorMessage", err.error_description);
        });
    },
    deleteUser() {
      this.successMessage = null;
      this.loading = true;
      userService
        .deleteUser(this.user.id)
        .then(() => {
          this.user = null;
          this.successMessage = "User deleted successfully";
          this.loading = false;
          this.dialog = false;
        })
        .catch(err => {
          this.loading = false;
          this.$store.commit("setErrorMessage", err.error_description);
        });
    }
  }
};
</script>

<style></style>
