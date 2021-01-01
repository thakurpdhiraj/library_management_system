<template>
  <v-container>
    <v-row>
      <v-col>
        <v-alert dense outlined type="error">
          {{
            errorMessage != null
              ? errorMessage
              : "Something went wrong. Please try again!"
          }}
        </v-alert>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
export default {
  computed: {
    errorMessage() {
      return this.$store.getters.getErrorMessage;
    },
  },
};
</script>

<style></style>
