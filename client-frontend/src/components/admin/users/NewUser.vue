<template>
  <v-card flat>
    <v-container>
      <v-row v-if="message">
        <v-col cols="12">
          <v-card class="elevation-5">
            <v-alert
              dense
              outlined
              dismissible
              transition="scale-transition"
              :type="isError ? 'error' : 'success'"
            >
              {{ message }}
            </v-alert>
            <v-container v-if="userData">
              <v-row>
                <v-col
                  cols="6"
                  sm="3"
                  v-for="(value, key, i) in userData"
                  :key="i"
                >
                  <v-list-item two-line>
                    <v-list-item-content>
                      <v-list-item-title>{{ value }}</v-list-item-title>
                      <v-list-item-subtitle>{{ key }}</v-list-item-subtitle>
                    </v-list-item-content>
                  </v-list-item>
                </v-col>
                <v-col cols="6" sm="3">
                  <v-btn text @click="userData = null" class="green--text">
                    Close
                  </v-btn>
                </v-col>
              </v-row>
            </v-container>
          </v-card>
        </v-col>
      </v-row>
      <v-row>
        <v-col :loading="loading">
          <v-form
            @submit.prevent="saveUser"
            :loading="loading"
            v-model="valid"
            ref="form"
          >
            <v-card elevation="0">
              <v-card-text>
                <v-container>
                  <v-row>
                    <v-col cols="12" sm="4">
                      <v-text-field
                        label="Name"
                        v-model="user.name"
                        :rules="[rules.required]"
                      ></v-text-field>
                    </v-col>
                    <v-col cols="12" sm="4">
                      <v-text-field
                        label="Username"
                        v-model="user.username"
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
                        multiple
                        :rules="[rules.minLength(1, user.userRoles, 'role')]"
                        label="Roles"
                      ></v-select>
                    </v-col>
                  </v-row>
                </v-container>
              </v-card-text>
              <v-card-actions>
                <v-btn color="green" dark :disabled="!valid" type="submit">
                  Add
                </v-btn>
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
  name: "NewUser",
  data() {
    return {
      user: {
        name: null,
        username: null,
        email: null,
        userRoles: null
      },
      valid: false,
      loading: false,
      roles: ["ADMIN", "USER"],
      rules: ruleUtil.rules,
      message: null,
      isError: false,
      userData: null
    };
  },
  methods: {
    saveUser() {
      this.isError = false;
      this.message = null;
      this.userData = null;
      this.loading = true;
      userService
        .saveUser(this.user)
        .then(data => {
          this.userData = {
            "User Id": data.id,
            Username: data.username,
            Name: data.name,
            Email: data.email,
            "Added At": data.createdAt
          };
          this.loading = false;
          this.message = "User added successfully";
          this.$refs.form.reset();
        })
        .catch(err => {
          this.loading = false;
          this.isError = true;
          this.message = err.error_description;
        });
    }
  }
};
</script>

<style></style>
