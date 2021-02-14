<template>
  <v-container>
    <v-row>
      <v-col>
        <v-alert dense outlined transition="scale-transition" type="error">
          {{
            errorMessage != null
              ? errorMessage
              : "Something went wrong. Please try again!"
          }}
        </v-alert>
      </v-col>
    </v-row>
    <v-row>
      <v-col>
        <v-btn @click="returnHome">
          <v-icon left dark>mdi-home</v-icon>
          Home
        </v-btn>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
export default {
  computed: {
    errorMessage() {
      return this.$store.getters.getErrorMessage;
    }
  },
  methods: {
    returnHome() {
      this.$store.commit("setErrorMessage", null);
      this.$router.push(this.$store.getters.getHome);
    }
  }
};
</script>

<style></style>
