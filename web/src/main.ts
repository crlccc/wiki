import {createApp} from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import Antd from 'ant-design-vue';
import * as Icon from "@ant-design/icons-vue";
import axios  from "axios";


axios.defaults.baseURL = process.env.VUE_APP_SERVER;

/**
 * axios 拦截器
 */
axios.interceptors.request.use(function (config) {
    console.log('请求参数：', config);
    return config;
},error => {
    return Promise.reject(error);
    }
);
axios.interceptors.response.use(function (response) {
    console.log('响应参数：', response);
    return response;
},error => {
    console.log('响应参数：', error);
    return Promise.reject(error);
    }
);



const app = createApp(App);
app.use(store).use(router).use(Antd).mount('#app')
const icons: any = Icon;
for (const i in Icon) {
    app.component(i, icons[i]);
}

console.log(axios.defaults.baseURL);
