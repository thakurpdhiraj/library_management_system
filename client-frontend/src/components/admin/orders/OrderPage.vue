<template>
  <Tabs :tabs="tabs" :vertical="true" />
</template>

<script>
import Tabs from "@/components/common/Tabs";
import FindOrders from "./FindOrders";
import FindOrdersHistory from "./FindOrdersHistory";
import FindOrdersOverdue from "./FindOrdersOverdue";
export default {
  name: "OrderPage",
  components: {
    Tabs
  },
  data() {
    return {
      tabs: [
        {
          id: 0,
          name: "Search",
          icon: "mdi-file-find",
          component: FindOrders
        },
        {
          id: 1,
          name: "History",
          icon: "mdi-history",
          component: FindOrdersHistory
        },
        {
          id: 2,
          name: "Overdue",
          icon: "mdi-alert-outline",
          component: FindOrdersOverdue
        }
      ]
    };
  }
};
</script>

<style></style>
