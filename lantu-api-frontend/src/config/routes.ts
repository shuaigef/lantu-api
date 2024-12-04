import Login from "../views/Login.vue"
import Register from "../views/Register.vue"
import BasicLayout from "../layouts/BasicLayout.vue"
import LoginLayout from "../layouts/LoginLayout.vue"
import InterfaceList from "../views/Index/InterfaceList.vue"
import InterfaceInfo from "../views/Index/InterfaceInfo.vue"
import InterfaceManage from "../views/interfaceManage/InterfaceManage.vue"
import InterfaceAnalysis from "../views/interfaceManage/InterfaceAnalysis.vue"
import UserManage from "../views/systemManage/UserManage.vue"
import RoleManage from "../views/systemManage/RoleManage.vue"
import AuthorityManage from "../views/systemManage/AuthorityManage.vue"
import AccountSetting from "../views/accountSetting/AccountSetting.vue"
import {LocalStorageEnum} from "../constants/index";
import {Message} from "@arco-design/web-vue";
import * as VueRouter from "vue-router";

const baseRoutes = [
    {
        path: '/index',
        redirect: '/interfaceList',
    },
    {
        path: '/user',
        name: 'loginLayout',
        component: LoginLayout,
        children: [
            { path: 'login', name: 'login', component: Login},
            { path: 'register', name: 'register', component: Register},
        ]
    },
    {
        path: '/',
        name: 'basicLayout',
        // alias: '/',
        component: BasicLayout,
        beforeEnter: (to, from, next) => {
            const jwt = localStorage.getItem(LocalStorageEnum.JWT)
            if (jwt) {
                next()
            } else {
                Message.error("请先登录")
                next({name: 'login'})
            }
        },
        children: [
            { path: 'interfaceList', name: 'interfaceList', component: InterfaceList, meta: { title: '接口列表'} },
            { path: 'interfaceInfo/:id', name: 'interfaceInfo', component: InterfaceInfo, meta: { title: '查看接口文档'} },
            { path: 'interfaceManage/index', name: 'interfaceManage', component: InterfaceManage, meta: { title: '接口管理'} },
            { path: 'interfaceManage/analysis', name: 'interfaceAnalysis', component: InterfaceAnalysis, meta: { title: '接口分析'} },
            { path: 'systemManage/userManage', name: 'userManage', component: UserManage, meta: { title: '用户管理'} },
            { path: 'systemManage/roleManage', name: 'roleManage', component: RoleManage, meta: { title: '角色管理'} },
            { path: 'systemManage/authorityManage', name: 'authorityManage', component: AuthorityManage, meta: { title: '权限管理'} },
            { path: 'accountSetting', name: 'accountSetting', component: AccountSetting, meta: { title: '账号设置'} },
        ]
    }
]

const router = VueRouter.createRouter({
    // 4. 内部提供了 history 模式的实现。为了简单起见，我们在这里使用 hash 模式。
    history: VueRouter.createWebHashHistory(),
    routes: baseRoutes, // `routes: routes` 的缩写
})

export default router;
