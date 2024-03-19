<template>
  <a-layout>
    <a-layout-sider width="200" style="background: #fff">
      <a-menu
          v-model:selectedKeys="selectedKeys2"
          v-model:openKeys="openKeys"
          mode="inline"
          :style="{ height: '100%', borderRight: 0 }"
          @click="handleClick"
      >
        <a-menu-item key="welcome">
          <MailOutlined/>
          <span>欢迎</span>
        </a-menu-item>
        <a-sub-menu v-for="item in level1" :key="item.id">
          <template v-slot:title>
              <span>
                <user-outlined/>
                {{ item.name }}
              </span>
          </template>
          <a-menu-item v-for="child in item.children" :key="child.id">{{ child.name }}</a-menu-item>
        </a-sub-menu>
      </a-menu>
    </a-layout-sider>
    <a-layout-content
        :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
    >
      <div class="welcome" v-show="isShowWelcome">
        <h1>欢迎使用大菜狗的知识网站</h1>
      </div>
      <a-list v-show="!isShowWelcome" item-layout="vertical" size="large" :grid="{ gutter: 50, column: 3 }"
              :data-source="ebooks">
        <template #renderItem="{ item }">
          <a-list-item key="item.name">
            <template #actions>
          <span v-for="{ icon, text } in actions" :key="icon">
            <component :is="icon" style="margin-right: 8px"/>
            {{ text }}
          </span>
            </template>
            <a-list-item-meta :description="item.description">
              <template #title>
                <router-link :to="'/doc?ebookId=' + item.id">
                  {{ item.name }}
                </router-link>
              </template>
              <template #avatar>
                <a-avatar :src="item.cover"/>
              </template>
            </a-list-item-meta>
          </a-list-item>
        </template>
      </a-list>
    </a-layout-content>
  </a-layout>

</template>

<script lang="ts">
import {defineComponent, onMounted, ref} from 'vue';
import {
  LaptopOutlined,
  LikeOutlined,
  MessageOutlined,
  NotificationOutlined,
  StarOutlined,
  UserOutlined
} from "@ant-design/icons-vue"; // @ is an alias to /src
import axios from "axios";
import defaultProps from "ant-design-vue/es/vc-slick/default-props";
import responsive = defaultProps.responsive;
import {Tool} from "@/util/tool";
import {message} from "ant-design-vue";


export default defineComponent({
  name: 'Home',
  components: {
    LaptopOutlined,
  },
  setup() {
    const ebooks = ref();
    let categoryId2 = 0;


    const handleQueryEbook = () => {
      axios.get("/ebook/list", {
        params: {
          page: 1,
          size: 1000,
          categoryId2: categoryId2
        }
      }).then((response) => {
        const data = response.data;
        ebooks.value = data.content.list;
      });
    }


    /**
     * 查询所有分类
     */
    const level1 = ref();
    let categorys: any;
    const handleQueryCategory = () => {
      axios.get("/category/all").then((response) => {
        const data = response.data;
        if (data.success) {
          categorys = data.content;
          level1.value = [];
          level1.value = Tool.array2Tree(data.content, 0);

        } else {
          message.error(data.message);
        }

      });
    };


    const isShowWelcome = ref(true);
    const handleClick = (value: any) => {
      // console.log("menu click")
      if (value.key === "welcome") {
        isShowWelcome.value = true;
      } else {
        categoryId2 = value.key;
        console.log("categoryId2=", categoryId2);
        isShowWelcome.value = false;
        handleQueryEbook();
      }
      // isShowWelcome.value = value.key === 'welcome';
    };


    onMounted(() => {
      handleQueryCategory();
      // console.log('onMounted');
    });

    return {
      ebooks,
      level1,
      handleClick,
      isShowWelcome,

      selectedKeys2: ref(['1']),
      openKeys: ref(['sub1']),
      pagination: {
        onChange: (page: any) => {
          console.log(page);
        },
        pageSize: 3,
      },
      actions: [
        {icon: StarOutlined, text: '156'},
        {icon: LikeOutlined, text: '156'},
        {icon: MessageOutlined, text: '12'},
      ],
    }
        ;
  }
});


</script>
<style scoped>
.ant-avatar {
  width: 50px;
  height: 50px;
  line-height: 50px;
  border-radius: 8%;
  margin: 5px 0;
}
</style>