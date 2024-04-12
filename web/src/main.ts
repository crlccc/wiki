import {createApp} from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import Antd from 'ant-design-vue';
import * as Icon from "@ant-design/icons-vue";
import axios  from "axios";
import {Tool} from "@/util/tool";
import {message} from "ant-design-vue";


axios.defaults.baseURL = process.env.VUE_APP_SERVER;

/**
 * axios 拦截器
 */
axios.interceptors.request.use(function (config) {
    console.log('请求参数：', config);
    const token = store.state.user.token;
    if(Tool.isNotEmpty(token)){
        config.headers.token = token;
        console.log('请求参数header增加token：', token);
    }
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
    const response = error.response;
    const status = response.status;
    if (status === 401) {
        // 判断状态码是401 跳转到首页或登录页
        console.log("未登录，跳到首页");
        store.commit("setUser", {});
        message.error("未登录或登录超时");
        router.push('/');
    }
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
