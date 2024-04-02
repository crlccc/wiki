<template>
  <a-layout-header class="header">
    <div class="logo"/>
    <a class="login-menu" v-show="user.id" @click="logout">
      <span>退出登录</span>
    </a>
    <a class="login-menu" v-show="user.id" style="margin-right: 20px">
      <span>您好：{{user.name}}</span>
    </a>
    <a class="login-menu" v-show="!user.id" @click="showLoginModal">
      <span>登录</span>
    </a>
    <a-menu
        theme="dark"
        mode="horizontal"
        :style="{ lineHeight: '64px' }"
    >
      <a-menu-item key="home">
        <router-link to="/">首页</router-link>
      </a-menu-item>
      <a-menu-item key="admin/user" v-if="user.id">
        <router-link to="/admin/user">用户管理</router-link>
      </a-menu-item>
      <a-menu-item key="admin/ebook" v-if="user.id">
        <router-link to="/admin/ebook">电子书管理</router-link>
      </a-menu-item>
      <a-menu-item key="admin/category" v-if="user.id">
        <router-link to="/admin/category">分类管理</router-link>
      </a-menu-item>
      <a-menu-item key="about">
        <router-link to="/about">关于我们</router-link>
      </a-menu-item>
    </a-menu>

    <a-modal
        title="登录"
        v-model:visible="loginModalVisible"
        :confirm-loading="loginModalLoading"
        destroyOnClose:true
        @ok="login"
    >
      <a-form :model="loginUser" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
        <a-form-item label="登录名">
          <a-input v-model:value="loginUser.loginName"/>
        </a-form-item>
        <a-form-item label="密码">
          <a-input v-model:value="loginUser.password" type="password"/>
        </a-form-item>
      </a-form>
    </a-modal>
  </a-layout-header>

</template>

<script lang="ts">
import {computed, defineComponent, ref} from 'vue';
import axios from "axios";
import {message} from "ant-design-vue";
import store from "@/store";
import router from "@/router";



declare let hexMd5: any;
declare let KEY: any;


export default defineComponent({
  name: 'the-header',
  props: {
    msg: String,
  },


  setup() {
    const user = computed(() => store.state.user);
    const loginUser = ref({
      loginName: 'test',
      password: 'e70e2222a9d67c4f2eae107533359aa4',
    });
    const loginModalVisible = ref(false);
    const loginModalLoading = ref(false);


    const showLoginModal = () => {
      loginModalVisible.value = true;
    };
    // 登录
    const login = () => {
      console.log("开始登录");
      loginModalLoading.value = true;
      loginUser.value.password = hexMd5(loginUser.value.password + KEY);
      axios.post('/user/login', loginUser.value).then((response) => {
        loginModalLoading.value = false;
        const data = response.data;
        if (data.success) {
          loginModalVisible.value = false;
          message.success("登录成功！");
          store.commit('setUser', data.content);
        } else {
          message.error(data.message);
        }
      });
    };

    //退出登录
    const logout = () => {
      console.log("退出登录");
      axios.get('/user/logout/' + user.value.token).then((response) => {
        const data = response.data;
        if (data.success) {
          message.success("退出成功！");
          store.commit('setUser', {});
          router.push('/');
        } else {
          message.error(data.message);
        }
      });
    };

    return {
      loginUser,
      loginModalVisible,
      loginModalLoading,
      showLoginModal,
      login,
      logout,
      user,
    };


  }
});


</script>

<style>
.login-menu {
  float: right;
  color: white;
}
</style>

