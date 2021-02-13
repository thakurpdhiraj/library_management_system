<template>
  <Tabs :vertical="true" />
</template>

<script>
import Tabs from "@/components/common/Tabs";
export default {
  data() {
    return {
      tabs: [
        {
          id: 0,
          name: "Find User",
          icon: "mdi-file-find",
          component: null
        }
      ]
    };
  },
  components: { Tabs }
};
</script>

<style></style>
