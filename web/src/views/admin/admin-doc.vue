<template>
  <a-layout>
    <a-layout-content
        :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
    >
      <p>
        <a-form layout="inline" :model="param">
          <a-form-item>
            <a-button type="primary">
              刷新
            </a-button>
          </a-form-item>
          <a-form-item>
            <a-button type="primary" @click="add()">
              新增
            </a-button>
          </a-form-item>
        </a-form>
      </p>
      <a-table
          :columns="columns"
          :row-key="record => record.id"
          :data-source="level1"
          :loading="loading"
          :pagination="false"
      >
        <template #cover="{ text: cover }">
          <img v-if="cover" :src="cover" alt="avatar"/>
        </template>
        <template v-slot:action="{ text, record }">
          <a-space size="small">
            <a-button type="primary" @click="edit(record)">
              编辑
            </a-button>
            <a-popconfirm
                title="删除不可恢复，是否确认删除？"
                ok-text="是"
                cancel-text="否"
                @confirm="handleDelete(record.id)"
            >
              <a-button type="primary" danger>
                删除
              </a-button>
            </a-popconfirm>
          </a-space>
        </template>
      </a-table>
    </a-layout-content>
  </a-layout>

  <a-modal
      title="文档表单"
      v-model:visible="modalVisible"
      :confirm-loading="modalLoading"
      @ok="handleModalOk"
  >
    <a-form :model="doc" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
      <a-form-item label="名称">
        <a-input v-model:value="doc.name"/>
      </a-form-item>
      <a-form-item label="父文档">
        <a-tree-select
            v-model:value="doc.parent"
            show-search
            style="width: 100%"
            :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
            placeholder="请选择父文档"
            allow-clear
            tree-default-expand-all
            :tree-data="treeSelectData"
            :fieldNames="{label :'name', key: 'id',value: 'id'}"
        >
        </a-tree-select>
      </a-form-item>
      <a-form-item label="顺序">
        <a-input v-model:value="doc.sort"/>
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script lang="ts">
import {defineComponent, onMounted, ref} from 'vue';
import axios from 'axios';
import defaultProps from "ant-design-vue/es/vc-slick/default-props";
import responsive = defaultProps.responsive;
import {message} from "ant-design-vue";
import {Tool} from "@/util/tool";

export default defineComponent({
  name: 'AdminDoc',
  setup() {
    const docs = ref();

    const param = ref();
    param.value = {}


    const loading = ref(false);

    const columns = [
      {
        title: '名称',
        dataIndex: 'name'
      },
      {
        title: '父文档',
        key: 'parent',
        dataIndex: 'parent'
      },
      {
        title: '顺序',
        dataIndex: 'sort'
      },
      {
        title: 'Action',
        key: 'action',
        slots: {customRender: 'action'}
      }
    ];

    /**
     * 数据查询
     **/

    const level1 = ref(); //一级文档树，children属性就是二级文档
    const handleQuery = () => {
      loading.value = true;
      axios.get("/doc/all").then((response) => {
        loading.value = false;
        const data = response.data;
        if (data.success) {
          docs.value = data.content;
          level1.value = [];
          level1.value = Tool.array2Tree(data.content, 0);

        } else {
          message.error(data.message);
        }

      });
    };


    // -------- 表单 ---------

    // 因为树选择组件的属性状态，会随当前编辑的节点而变化，所以单独声明一个响应式变量
    const treeSelectData = ref();
    treeSelectData.value = [];

    const doc = ref({});
    const modalVisible = ref(false);
    const modalLoading = ref(false);
    const handleModalOk = () => {
      modalLoading.value = true;
      axios.post("/doc/save", doc.value).then((response) => {
        modalLoading.value = false;
        const data = response.data;//data=commonResp
        if (data.success) {
          modalVisible.value = false;


          //重新加载数据
          handleQuery();
        } else {
          message.error(data.message);
        }
      });
    };

    /**
     *将某一节点的子节点增加disabled属性
     */
    const setDisabled = (treeSelectData: any, id: any) => {
      for (let i = 0; i < treeSelectData.length; i++) {
        const node = treeSelectData[i];
        if (node.id == id) {
          node.disabled = true;

          const children = node.children;
          if (Tool.isEmpty(children)) {
            for (let j=0;j<children.length;j++) {
              setDisabled(children,children[j].id);
            }
          }
        }else {
          const children = node.children;
          if (Tool.isEmpty(children)) {
            setDisabled(children, id);
          }
        }
      }
    };


    /**
     * 编辑
     */
    const edit = (record: any) => {
      modalVisible.value = true;
      doc.value = record
      doc.value = Tool.copy(record);

      treeSelectData.value = Tool.copy(level1.value)
      setDisabled(treeSelectData.value, record.id);

      treeSelectData.value.unshift({id: 0, name: '无'})
    };

    /**
     * 新增
     */
    const add = () => {
      modalVisible.value = true;
      doc.value = {}
      treeSelectData.value = Tool.copy(level1.value)
      treeSelectData.value.unshift({id: 0, name: '无'})

    };

    const handleDelete = (id: number) => {
      axios.delete("/doc/delete/" + id).then((response) => {
        const data = response.data;//data=commonResp

        if (data.success) {

          //重新加载数据
          handleQuery();
        }
      });
    }


    onMounted(() => {
      handleQuery();
    });

    return {
      param,
      docs,
      columns,
      loading,
      level1,

      edit,
      add,

      doc,
      modalVisible,
      modalLoading,
      handleModalOk,
      handleDelete,
      treeSelectData

    }
  }
});
</script>

<style scoped>
img {
  width: 50px;
  height: 50px;
}
</style>