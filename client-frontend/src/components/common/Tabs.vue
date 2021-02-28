<template>
  <v-tabs v-model="tab" dark :vertical="vertical" :right="right">
    <v-tab
      :class="{ 'mt-4': vertical }"
      v-for="item in tabs"
      :key="item.id"
      :tabindex="item.id"
    >
      <v-icon left> {{ item.icon }} </v-icon>
      {{ item.name }}
    </v-tab>
    <v-tabs-items v-model="tab" :vertical="vertical">
      <v-tab-item v-for="item in tabs" :key="item.id">
        <component :is="item.component" />
      </v-tab-item>
    </v-tabs-items>
  </v-tabs>
</template>

<script>
export default {
  name: "Tabs",
  data() {
    return {
      tab: 0
    };
  },
  props: {
    tabs: {
      type: Array,
      required: true,
      default: () => []
    },
    vertical: {
      type: Boolean,
      default: false
    },
    right: {
      type: Boolean,
      default: false
    }
  }
};
</script>

<style></style>
