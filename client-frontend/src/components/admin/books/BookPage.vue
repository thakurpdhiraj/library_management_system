<template>
  <Tabs :tabs="tabs" :vertical="true" />
</template>

<script>
import Tabs from "@/components/common/Tabs";
import NewBook from "./NewBook";
import FindBooks from "./FindBooks";
export default {
  name: "BookPage",
  components: {
    Tabs
  },
  data() {
    return {
      tabs: [
        { id: 0, name: "Find Book", icon: "mdi-magnify", component: FindBooks },
        {
          id: 1,
          name: "Add Book",
          icon: "mdi-book-plus",
          component: NewBook
        }
      ]
    };
  }
};
</script>

<style></style>
