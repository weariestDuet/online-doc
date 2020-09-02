import Vue from 'vue'
import Router from 'vue-router'

import Edit from "../views/Edit";
import File from "../views/File";
import Login from "../views/Login";
import Register from "../views/Register";
import WorkStation from "../views/WorkStation";
import TeamSpace from "../views/TeamSpace";
import TemplateLibrary from '../views/TemplateLibrary'
import Profile from '../views/Profile'
import layout from '@/layout/layout'
Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/Login',
      name: 'Login',
      component: Login
    },
    {
      path: '/Register',
      name: 'Register',
      component: Register
    },
    {
      path: '/',
      name: 'WorkStation',
      component: layout,
      children: [{
        path: '',
        component: WorkStation,
      }]
    },
    {
      path:'/TeamSpace',
      name:'TeamSpace',
      component: layout,
      children: [{
        path: '',
        component: TeamSpace,
      }]
    },
    {
      path: '/Edit/:type',
      name: 'Edit',
      component: layout,
      children: [{
        path: '',
        component: Edit,
      }]
    },
    {
      path: '/File/:fileId',
      name: 'File',
      component: layout,
      children: [{
        path: '',
        component: File,
      }]
    },
    {
      path: '/TemplateLibrary',
      name: 'TemplateLibrary',
      component: layout,
      children: [{
        path: '',
        component: TemplateLibrary,
      }]
    },
    {
      path: '/Profile/:userName',
      name: 'Profile',
      component: layout,
      children: [{
        path: '',
        component: Profile,
      }]
    }
  ]
})
