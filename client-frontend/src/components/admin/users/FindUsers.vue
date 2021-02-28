<template>
  <v-card flat>
    <v-container>
      <v-row v-if="message">
        <v-col cols="12">
          <v-alert
            dense
            outlined
            dismissible
            transition="scale-transition"
            :type="isError ? 'error' : 'success'"
          >
            {{ message }}
          </v-alert>
        </v-col>
      </v-row>
      <v-row>
        <v-col :loading="loading">
          <v-container>
            <v-row>
              <v-col cols="12" sm="4">
                <v-text-field label="User ID" v-model="id"></v-text-field>
              </v-col>
              <v-col cols="12" sm="4">
                <v-btn
                  color="green"
                  dark
                  :disabled="!id"
                  text
                  @click="findUser"
                >
                  Search
                </v-btn>
              </v-col>
            </v-row>
          </v-container>
          <v-form v-model="valid" @submit.prevent="updateUser">
            <v-card v-if="user" class="elevation-5">
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
                        label="Updated At"
                        v-model="user.updatedAt"
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
                        :rules="[rules.minLength(1, user.userRoles, 'role')]"
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
                <v-btn color="green" dark text type="submit" :disabled="!valid">
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
          </v-form>
        </v-col>
      </v-row>
    </v-container>
  </v-card>
</template>

<script>
import * as userService from "@/service/user";
import * as ruleUtil from "@/util/ruleUtil";
export default {
  name: "FindUsers",
  data() {
    return {
      id: null,
      user: null,
      loading: false,
      roles: ["ADMIN", "USER"],
      rules: ruleUtil.rules,
      message: null,
      dialog: false,
      isError: false,
      valid: false
    };
  },
  methods: {
    findUser() {
      this.message = null;
      this.loading = true;
      userService
        .getUser(this.id)
        .then(data => {
          this.isError = false;
          this.user = data;
          this.loading = false;
        })
        .catch(err => {
          this.isError = true;
          this.loading = false;
          this.user = null;
          this.message = err.error_description;
        });
    },
    updateUser() {
      this.message = null;
      this.loading = true;
      userService
        .updateUser(this.user)
        .then(data => {
          this.isError = false;
          this.message = "User updated successfully";
          this.loading = false;
          this.user = data;
        })
        .catch(err => {
          this.isError = true;
          this.loading = false;
          this.message = err.error_description;
        });
    },
    deleteUser() {
      this.message = null;
      this.loading = true;
      userService
        .deleteUser(this.user.id)
        .then(() => {
          this.isError = false;
          this.user = null;
          this.message = "User deleted successfully";
          this.loading = false;
          this.dialog = false;
        })
        .catch(err => {
          this.isError = true;
          this.loading = false;
          this.message = err.error_description;
        });
    }
  }
};
</script>

<style></style>
