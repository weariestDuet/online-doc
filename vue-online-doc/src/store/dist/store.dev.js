"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports["default"] = void 0;

var _vue = _interopRequireDefault(require("vue"));

var _vuex = _interopRequireDefault(require("vuex"));

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { "default": obj }; }

function _defineProperty(obj, key, value) { if (key in obj) { Object.defineProperty(obj, key, { value: value, enumerable: true, configurable: true, writable: true }); } else { obj[key] = value; } return obj; }

_vue["default"].use(_vuex["default"]);

var _default = new _vuex["default"].Store({
  //全局状态
  state: {
    layout: 1,
    //是否平铺布局
    groupAdminVisable: false,
    groupAddVisable: false,
    groupName: '',
    roles: localStorage.getItem('roles') == null ? '' : localStorage.getItem('roles'),
    token: localStorage.getItem('token') == null ? '' : localStorage.getItem('token'),
    name: localStorage.getItem('name') == null ? '' : localStorage.getItem('name')
  },
  mutations: {
    login: function login(state, data) {
      this.state.token = data.token;
      localStorage.setItem('token', data.token);
      this.state.name = data.name;
      localStorage.setItem('name', data.name);
      this.state.roles = data.roles;
      localStorage.setItem('roles', data.roles);
    },
    logout: function logout(state) {
      localStorage.removeItem('token');
      this.state.token = '';
      localStorage.removeItem('name');
      this.state.name = '';
      localStorage.removeItem('roles');
      this.state.roles = '';
    },
    refresh: function refresh(state, token) {
      this.state.token = token;
      localStorage.setItem('token', token);
    },
    setLayout: function setLayout(state, isTile) {
      this.state.layout = isTile;
    },
    setGroupName: function setGroupName(state, name) {
      this.state.groupName = name;
    },
    setGroupAdminVisable: function setGroupAdminVisable(state, visable) {
      this.state.groupAdminVisable = visable;
    },
    setGroupAddVisable: function setGroupAddVisable(state, visable) {
      this.state.groupAddVisable = visable;
    }
  },
  getters: _defineProperty({
    getLayout: function getLayout(state) {
      return this.state.layout;
    },
    getGroupName: function getGroupName(state) {
      return this.state.groupName;
    },
    getGroupAdminVisable: function getGroupAdminVisable(state) {
      return this.state.groupAdminVisable;
    }
  }, "getGroupAdminVisable", function getGroupAdminVisable(state) {
    return this.state.groupAdminVisable;
  }),
  actions: {
    setLayoutStatus: function setLayoutStatus(_ref, isTile) {
      var commit = _ref.commit,
          state = _ref.state;
      commit('setLayout', isTile);
    },
    setCurGroupName: function setCurGroupName(_ref2, name) {
      var commit = _ref2.commit,
          state = _ref2.state;
      commit('setGroupName', name);
    },
    setGroupAdminVisable: function setGroupAdminVisable(_ref3, visable) {
      var commit = _ref3.commit,
          state = _ref3.state;
      commit('setGroupAdminVisable', visable);
    },
    setGroupAddVisable: function setGroupAddVisable(_ref4, visable) {
      var commit = _ref4.commit,
          state = _ref4.state;
      commit('setGroupAddVisable', visable);
    }
  }
});

exports["default"] = _default;