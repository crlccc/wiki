<template>
  <a-layout-footer style="text-align: center">
    大菜狗的知识网站 ©2021 Created by 大菜狗
    <a v-show="user.id">欢迎：{{user.name}}</a>
  </a-layout-footer>

</template>

<script lang="ts">
import {computed, defineComponent} from 'vue';
import store from "@/store";

export default defineComponent({
  name: 'the-footer',
  props: {
    msg: String,
  },

  setup() {
    const user =  computed(() => store.state.user);
    console.log("user{}",user.value);

    return {
      user,
    }
  }
});
</script>

