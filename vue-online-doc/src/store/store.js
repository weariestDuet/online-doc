import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
  //全局状态
  state:{
    layout: 1, //是否平铺布局
    groupAdminVisable: false,
    groupAddVisable: false,
    groupName: '',
    roles: localStorage.getItem('roles') == null ? '' : localStorage.getItem('roles'),
    token: localStorage.getItem('token') == null ? '' : localStorage.getItem('token'),
    name: localStorage.getItem('name') == null ? '' : localStorage.getItem('name')
  },
  mutations:{
    login(state, data) {
      this.state.token = data.token;
      localStorage.setItem('token', data.token);
      this.state.name = data.name;
      localStorage.setItem('name', data.name)
      this.state.roles = data.roles;
      localStorage.setItem('roles', data.roles)
    },
    logout(state) {
      localStorage.removeItem('token');
      this.state.token = '';
      localStorage.removeItem('name');
      this.state.name = '';
      localStorage.removeItem('roles');
      this.state.roles = '';
    },
    refresh(state, token) {
      this.state.token = token;
      localStorage.setItem('token', token);
    },
    setLayout(state,isTile){
      this.state.layout = isTile
    },
    setGroupName(state,name){
      this.state.groupName = name
    },
    setGroupAdminVisable(state,visable){
      this.state.groupAdminVisable = visable
    },
    setGroupAddVisable(state,visable){
      this.state.groupAddVisable = visable
    }
  },
  getters:{
    getLayout(state){
      return this.state.layout
    },
    getGroupName(state){
      return this.state.groupName
    },
    getGroupAdminVisable(state){
      return this.state.groupAdminVisable
    },
    getGroupAdminVisable(state){
      return this.state.groupAdminVisable
    },
  },
  actions:{
    setLayoutStatus({commit,state},isTile){
      commit('setLayout',isTile)
    },
    setCurGroupName({commit,state},name){
      commit('setGroupName',name)
    },
    setGroupAdminVisable({commit,state},visable){
      commit('setGroupAdminVisable',visable)
    },
    setGroupAddVisable({commit,state},visable){
      commit('setGroupAddVisable',visable)
    },
  }
})
