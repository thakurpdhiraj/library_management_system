<template>
  <v-container>
    <v-row>
      <v-col>
        <v-tabs>
          <v-tab><v-icon left>mdi-account-search</v-icon>Find User</v-tab>
          <v-tab-item :loading="loading">
            <v-form @submit.prevent="findUser">
              <v-container>
                <v-row>
                  <v-col cols="12" sm="4">
                    <v-text-field label="User ID" v-model="id"></v-text-field>
                  </v-col>
                  <v-col cols="12" sm="4">
                    <v-btn color="green" dark text type="submit">Search</v-btn>
                  </v-col>
                </v-row>
              </v-container>
            </v-form>
            <v-card v-if="user">
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
                      ></v-text-field>
                    </v-col>
                    <v-col cols="12" sm="4">
                      <v-text-field
                        label="Email"
                        v-model="user.email"
                        :rules="[rules.email]"
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
                <v-btn class="ml-10" color="orange" @click="deleteUser">
                  Delete
                </v-btn>
              </v-card-actions>
            </v-card>
          </v-tab-item>
          <v-tab><v-icon left>mdi-account-plus</v-icon>Add User</v-tab>
          <v-tab-item><new-user></new-user></v-tab-item>
        </v-tabs>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
import NewUser from "../users/NewUser";
import * as userService from "../../../service/user";
export default {
  components: {
    NewUser
  },
  data() {
    return {
      id: null,
      user: null,
      loading: false,
      roles: ["ADMIN", "USER"],
      rules: {
        minOne: v => (!!v && v.length > 0) || "Atleast 1 role is required",
        email: v => {
          const pattern = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
          return pattern.test(v) || "Invalid e-mail.";
        }
      }
    };
  },
  methods: {
    findUser() {
      console.log("find user");
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
    updateUser() {}
  }
};
</script>

<style></style>
