import request from '@/utils/request'
import qs from 'qs';

export default {
  getRecycle() {
    return request({
      url:'/doc/getRecycle',
      method:'get',
    })
  },
  getGroupDoc(teamName){
    return request({
      url: '/doc/getTeamDoc/' + teamName,
      method: 'get'
    })
  },
  saveTeamDoc(title, body,teamName){
    return request({
      url: '/doc/saveTeamDoc',
      method: 'post',
      data: qs.stringify({'title': title, 'body': body,'teamName':teamName})
    })
  },
  saveUserDoc(title, body) {
    return request({
      url: '/doc/saveDoc',
      method: 'post',
      data: qs.stringify({'title':title, 'body':body})
    })
  },
  updateDoc(docId,title, body) {
    return request({
      url: '/doc/update',
      method: 'post',
      data: qs.stringify({'docId': docId, 'title': title, 'body': body})
    })
  },
  getDocument(docId) {
    return request({
      url: '/doc/'+docId,
      method: 'get'
    })
  },
  deleteDoc(docId){
    return request({
      url: '/doc/delete/' + docId,
      method: 'delete'
    })
  },

  // 编辑锁相关
  isEditable(docId){ 
    return request({
      url: '/doc/' + docId + '/isEditable',
      method: 'post',
    })
  },
  isEditing(docId){   
    return request({
      url: '/doc/' + docId + '/isEditing',
      method: 'post',
    })
  },
  updateAuth(docId,role,auth){
    return request({
      url: '/doc/' + docId + '/updateAuth',
      method: 'post',
      data: qs.stringify({'role':role,'auth':auth})
      // data: {auth:auth}
    })
  },
  collectDoc(docId){
    return request({
      url: '/doc/collect/' + docId,
      method: 'post'
    })
  },
  uncollectDoc(docId){
    return request({
      url: '/doc/unCollect/' + docId,
      method: 'delete'
    })
  },
}
