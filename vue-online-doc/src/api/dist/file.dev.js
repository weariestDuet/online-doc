"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports["default"] = void 0;

var _request = _interopRequireDefault(require("@/utils/request"));

var _qs = _interopRequireDefault(require("qs"));

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { "default": obj }; }

var _default = {
  getCreation: function getCreation() {
    return (0, _request["default"])({
      url: '/doc/getCreation',
      method: 'get'
    });
  },
  getDesktop: function getDesktop() {
    return (0, _request["default"])({
      url: '/doc/getRelativeFile',
      method: 'get'
    });
  },
  getHistory: function getHistory(fileId) {
    return (0, _request["default"])({
      url: '/doc/getHistoryFile/' + fileId,
      method: 'get'
    });
  },
  getHistoryFile: function getHistoryFile(fileId, modifyCnt) {
    return (0, _request["default"])({
      url: '/doc/getHistoryFile/' + fileId + '/' + modifyCnt,
      method: 'get'
    });
  },
  getStar: function getStar() {
    return (0, _request["default"])({
      url: '/doc/getStar',
      method: 'get'
    });
  },
  getRecent: function getRecent() {
    return (0, _request["default"])({
      url: '/doc/getRecent',
      method: 'get'
    });
  },
  getRecycle: function getRecycle() {
    return (0, _request["default"])({
      url: '/doc/getRecycle',
      method: 'get'
    });
  },
  getGroupFile: function getGroupFile(teamName) {
    return (0, _request["default"])({
      url: '/doc/getTeamFile/' + teamName,
      method: 'get'
    });
  },
  saveFileList: function saveFileList(docTitle, docBody, teamName) {
    return (0, _request["default"])({
      url: '/doc/saveTeamFile',
      method: 'post',
      data: _qs["default"].stringify({
        'docTitle': docTitle,
        'docBody': docBody,
        'teamName': teamName
      })
    });
  },
  sendDocument: function sendDocument(docTitle, docBody) {
    return (0, _request["default"])({
      url: '/doc/saveFile',
      method: 'post',
      data: _qs["default"].stringify({
        'docTitle': docTitle,
        'docBody': docBody
      })
    });
  },
  updateDocument: function updateDocument(docId, docTitle, docBody) {
    return (0, _request["default"])({
      url: '/doc/update',
      method: 'post',
      data: _qs["default"].stringify({
        'docId': docId,
        'docTitle': docTitle,
        'docBody': docBody
      })
    });
  },
  getDocument: function getDocument(fileId) {
    return (0, _request["default"])({
      url: '/doc/' + fileId,
      method: 'get'
    });
  },
  // 回收站相关
  Deleted: function Deleted(fileId) {
    return (0, _request["default"])({
      url: '/doc/delete/' + fileId,
      method: 'delete'
    });
  },
  getDeleted: function getDeleted() {
    return (0, _request["default"])({
      url: '/doc/getDelete',
      method: 'get'
    });
  },
  recoverDeleted: function recoverDeleted(fileId) {
    return (0, _request["default"])({
      url: '/doc/recoverDelete/' + fileId,
      method: 'post'
    });
  },
  foreverDeleted: function foreverDeleted(fileId) {
    return (0, _request["default"])({
      url: '/doc/foreverDelete/' + fileId,
      method: 'post'
    });
  },
  // 编辑锁相关
  isEditable: function isEditable(docId) {
    return (0, _request["default"])({
      url: '/doc/' + docId + '/isEditable',
      method: 'post'
    });
  },
  isEditing: function isEditing(docId) {
    return (0, _request["default"])({
      url: '/doc/' + docId + '/isEditing',
      method: 'post'
    });
  },
  updateAuth: function updateAuth(docId, role, auth) {
    return (0, _request["default"])({
      url: '/doc/' + docId + '/updateAuth',
      method: 'post',
      data: _qs["default"].stringify({
        'role': role,
        'auth': auth
      }) // data: {auth:auth}

    });
  },
  collectDocument: function collectDocument(docId) {
    return (0, _request["default"])({
      url: '/doc/collect/' + docId,
      method: 'post'
    });
  },
  removeCollectedDocument: function removeCollectedDocument(docId) {
    return (0, _request["default"])({
      url: '/doc/removeCollectedDoc/' + docId,
      method: 'delete'
    });
  },
  getMyCollecting: function getMyCollecting() {
    return (0, _request["default"])({
      url: '/doc/getCollect',
      method: 'get'
    });
  }
};
exports["default"] = _default;