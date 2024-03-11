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
import {createApp, createVNode, defineComponent, onMounted, ref} from 'vue';
import axios from 'axios';
import {message, Modal} from "ant-design-vue";
import {Tool} from "@/util/tool";
import {useRoute} from "vue-router";
import {ExclamationCircleOutlined} from "@ant-design/icons-vue";

export default defineComponent({
  name: 'AdminDoc',
  setup() {
    const docs = ref();

    const router = useRoute()
    const param = ref();
    param.value = {}

    const deleteIds: Array<String> = [];
    const deleteNames: Array<String> = [];

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
     * 将某节点及其子孙节点全部置为disabled
     */
    const setDisable = (treeSelectData: any, id: any) => {
      // console.log(treeSelectData, id);
      // 遍历数组，即遍历某一层节点
      for (let i = 0; i < treeSelectData.length; i++) {
        const node = treeSelectData[i];
        if (node.id === id) {
          // 如果当前节点就是目标节点
          // console.log("disabled", node);
          // 将目标节点设置为disabled
          node.disabled = true;

          // 遍历所有子节点，将所有子节点全部都加上disabled
          const children = node.children;
          if (Tool.isNotEmpty(children)) {
            for (let j = 0; j < children.length; j++) {
              setDisable(children, children[j].id)
            }
          }
        } else {
          // 如果当前节点不是目标节点，则到其子节点再找找看。
          const children = node.children;
          if (Tool.isNotEmpty(children)) {
            setDisable(children, id);
          }
        }
      }
    };

    // const ids: Array<String> = [];
    /**
     *查找整个文档树枝
     */
    const getDeleteIds = (treeSelectData: any, id: any) => {
      // console.log(treeSelectData, id);
      // 遍历数组，即遍历某一层节点
      for (let i = 0; i < treeSelectData.length; i++) {
        const node = treeSelectData[i];
        if (node.id === id) {
          // 如果当前节点就是目标节点，将ID放入结果集

          // ids.push(node.id);

          deleteIds.push(node.id);
          deleteNames.push(node.name);
          // 遍历所有子节点
          const children = node.children;
          if (Tool.isNotEmpty(children)) {
            for (let j = 0; j < children.length; j++) {
              getDeleteIds(children, children[j].id)
            }
          }
        } else {
          // 如果当前节点不是目标节点，则到其子节点再找找看。
          const children = node.children;
          if (Tool.isNotEmpty(children)) {
            getDeleteIds(children, id);
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
      setDisable(treeSelectData.value, record.id);

      treeSelectData.value.unshift({id: 0, name: '无'})
    };

    /**
     * 新增
     */
    const add = () => {
      modalVisible.value = true;
      doc.value = {
        ebookId: router.query.ebookId,
      };
      treeSelectData.value = Tool.copy(level1.value)
      treeSelectData.value.unshift({id: 0, name: '无'})

    };

    /**
     * 删除
     * @param id
     */
    const handleDelete = (id: number) => {

      // 清空数组，否则多次删除时，数组会一直增加
      deleteIds.length = 0;
      deleteNames.length = 0;
      getDeleteIds(level1.value, id);

      Modal.confirm(
          {
            title: '确定删除吗？',
            icon: createVNode(ExclamationCircleOutlined),
            content: '将删除：【' + deleteNames.join("，") + "】删除后不可恢复！确认删除？",
            okText() {
              axios.delete("/doc/delete/" + deleteIds.join(",")).then((response) => {
                const data = response.data;//data=commonResp

                if (data.success) {

                  //重新加载数据
                  handleQuery();
                }
              });
            },
            okType: 'danger',
            centered: true
          }
      )
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