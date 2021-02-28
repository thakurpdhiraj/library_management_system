<template>
  <Tabs :tabs="tabs" :vertical="true" />
</template>

<script>
import Tabs from "@/components/common/Tabs";
import ChangePassword from "./ChangePassword";
import EditAccount from "./EditAccount";
export default {
  name: "AccountPages",
  components: {
    Tabs
  },
  data() {
    return {
      tabs: [
        {
          id: 0,
          name: "Edit Account",
          icon: "mdi-account",
          component: EditAccount
        },
        {
          id: 1,
          name: "Edit Password",
          icon: "mdi-lock",
          component: ChangePassword
        }
      ]
    };
  }
};
</script>

<style></style>
