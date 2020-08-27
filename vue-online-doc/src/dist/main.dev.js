"use strict";

var _vue = _interopRequireDefault(require("vue"));

var _axios = _interopRequireDefault(require("axios"));

var _qs = _interopRequireDefault(require("qs"));

var _App = _interopRequireDefault(require("./App"));

var _router = _interopRequireDefault(require("./router"));

var _vuex = _interopRequireDefault(require("vuex"));

var _store = _interopRequireDefault(require("./store/store"));

require("./assets/element.css");

var _elementUi = _interopRequireDefault(require("element-ui"));

require("quill/dist/quill.core.css");

require("quill/dist/quill.snow.css");

require("quill/dist/quill.bubble.css");

var _jsMd = _interopRequireDefault(require("js-md5"));

var _vueClipboard = _interopRequireDefault(require("vue-clipboard2"));

var _vueParticles = _interopRequireDefault(require("vue-particles"));

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { "default": obj }; }

// import 'element-ui/lib/theme-chalk/index.css'
// import 'element-ui/lib/theme-chalk/display.css';
_vue["default"].config.productionTip = false;

_vue["default"].use(_elementUi["default"], _vuex["default"]);

_vue["default"].http = _vue["default"].prototype.$http = _axios["default"];
_vue["default"].prototype.$qs = _qs["default"];
_vue["default"].prototype.$md5 = _jsMd["default"];
_vue["default"].prototype.$HOST = 'https://'; //国外镜像

_vue["default"].prototype.$appid = "";
_vue["default"].prototype.$appsecret = "";
var appid = "";
var appsecret = "";
_vue["default"].prototype.$sign = (0, _jsMd["default"])(appid + appsecret);
_vueClipboard["default"].config.autoSetContainer = true;

_vue["default"].use(_vueClipboard["default"]);

_vue["default"].use(_vueParticles["default"]);

_vue["default"].config.productionTip = false;
/* eslint-disable no-new */

new _vue["default"]({
  el: '#app',
  router: _router["default"],
  components: {
    App: _App["default"]
  },
  template: '<App/>',
  store: _store["default"],
  render: function render(h) {
    return h(_App["default"]);
  }
});