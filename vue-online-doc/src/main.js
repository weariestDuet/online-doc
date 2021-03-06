import Vue from 'vue'
import axios from 'axios'
import qs from 'qs'
import App from './App'
import router from './router'
import Vuex from 'vuex'
import store from './store/store'
import './assets/element.css'
import ElementUI from 'element-ui'
import 'quill/dist/quill.core.css'
import 'quill/dist/quill.snow.css'
import 'quill/dist/quill.bubble.css'

import md5 from 'js-md5'
Vue.config.productionTip = false

Vue.use(ElementUI,Vuex)
Vue.http = Vue.prototype.$http = axios
Vue.prototype.$qs = qs
Vue.prototype.$md5 = md5
Vue.prototype.$HOST='https://'//国外镜像
Vue.prototype.$appid= ""
Vue.prototype.$appsecret= ""
let appid= ""
let appsecret= ""
Vue.prototype.$sign=md5(appid + appsecret)

import VueClipboard from 'vue-clipboard2'
VueClipboard.config.autoSetContainer = true
Vue.use(VueClipboard)

import VueParticles from 'vue-particles'
Vue.use(VueParticles)

Vue.config.productionTip = false
/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  components: { App },
  template: '<App/>',
  store,
  render: h => h(App)
})
