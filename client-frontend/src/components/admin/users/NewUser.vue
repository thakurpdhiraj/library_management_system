<template>
  <v-container>
    <v-row v-if="successMessage">
      <v-col cols="12">
        <v-card v-if="successData" class="elevation-0">
          <v-alert dense outlined transition="scale-transition" type="success">
            {{ successMessage }}
          </v-alert>
          <v-container>
            <v-row>
              <v-col
                cols="6"
                sm="3"
                v-for="(value, key, i) in successData"
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
                <v-btn text @click="successData = null">Close</v-btn>
              </v-col>
            </v-row>
          </v-container>
        </v-card>
      </v-col>
    </v-row>
    <v-row>
      <v-col :loading="loading">
        <v-card class="elevation-15">
          <v-form
            @submit.prevent="saveUser"
            :loading="loading"
            v-model="valid"
            ref="form"
          >
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
                    :rules="[rules.minOne]"
                    label="Roles"
                  ></v-select>
                </v-col>
                <v-col cols="12" sm="4">
                  <v-btn color="green" dark :disabled="!valid" type="submit">
                    Add
                  </v-btn>
                </v-col>
              </v-row>
            </v-container>
          </v-form>
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
      user: {
        name: null,
        username: null,
        email: null,
        userRoles: null
      },
      valid: false,
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
      successData: null
    };
  },
  methods: {
    saveUser() {
      this.successMessage = null;
      this.successData = null;
      this.loading = true;
      userService
        .saveUser(this.user)
        .then(data => {
          this.successData = {
            "User Id": data.id,
            "User Name": data.username,
            Name: data.name,
            Email: data.email,
            "Added At": data.createdAt
          };
          this.loading = false;
          this.successMessage = "User added successfully";
          this.$refs.form.reset();
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
