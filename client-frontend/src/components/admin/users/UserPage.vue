<template>
  <Tabs :tabs="tabs" :vertical="true" />
</template>

<script>
import Tabs from "@/components/common/Tabs";
import NewUser from "./NewUser";
import FindUsers from "./FindUsers";
export default {
  name: "UserPage",
  components: {
    Tabs
  },
  data() {
    return {
      tabs: [
        {
          id: 0,
          name: "Find User",
          icon: "mdi-account-search",
          component: FindUsers
        },
        {
          id: 1,
          name: "Add User",
          icon: "mdi-account-plus",
          component: NewUser
        }
      ]
    };
  }
};
</script>

<style></style>
