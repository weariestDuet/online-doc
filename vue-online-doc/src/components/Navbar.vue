<template>
  <div class="box-card">
    <a class="nav">&nbsp;</a>
    <span style="width:250px;margin-right:20px">
      <el-input v-model="search" placeholder="请输入内容" @keyup.enter.native="click_search">
        <el-button @click="click_search" style="width:40px" slot="append" icon="el-icon-search"></el-button>
      </el-input>
    </span>

    <!--实现全屏功能-->
    <span>
      <el-button type="primary" icon="el-icon-full-screen" size="small" @click="screen" circle></el-button>
    </span>

    <span @click="handleCommand_notice">
      <el-badge v-if="unreadCnt" :value="unreadCnt">
        <i class="el-icon-bell notify"></i>
      </el-badge>
      <i v-else class="el-icon-bell notify"></i>
    </span>

    <span>
      <el-dropdown @command="handleCommand_info">
        <span class="el-dropdown-link">
          <img v-if="validAvatar()" :src="avatar" class="avatar" slot="reference" />
          <img v-else src="../../static/avatar.svg" class="avatar" slot="reference" />
        </span>
        <el-dropdown-menu slot="dropdown">
          <el-dropdown-item command="goUserInfo">个人中心</el-dropdown-item>
          <el-dropdown-item divided>
            <a @click="loginout">退出登陆</a>
          </el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </span>
    <span style="margin-right:20px"><a @click="info_drawer = true">{{name}}</a></span>

    <!--侧栏消息抽屉-->
    <el-drawer
      :visible.sync="notice_drawer"
      :show-close="true"
      :with-header="false"
      size="27%"
      :append-to-body="true"
      :modal="false"
      style="margin-top:60px;"
    >
      <el-tabs v-model="activeName_notice" type="border-card" style="padding:5px">
        <el-tab-pane label="系统通知" name="2">
          <div class="message">
            <el-scrollbar>
              <el-timeline>
                <el-timeline-item
                  v-for="notice in noticeList"
                  :key="notice.noticeId"
                  placement="top"
                  :timestamp="dateFormat(notice.time)"
                >
                  <div class="messageList">
                    <span style="float:left">
                      <img class="doc" src="../../static/doc.png" />
                    </span>
                    <span>
                      <div style="margin-bottom:10px">
                        <a @click="gotoUserPage(notice.sender)">{{notice.sender}}</a>
                        &nbsp;&nbsp;{{notice.action}}
                        <b>「 {{notice.item}}」</b>
                      </div>
                      <div class="messageButton">
                        <span style="float:right;margin-right:25px">
                          <el-button
                            size="mini"
                            v-if="notice.isRead == 0"
                            @click="permit(notice.noticeId)"
                          >同意</el-button>
                        </span>
                      </div>
                    </span>
                  </div>
                </el-timeline-item>
                <el-timeline-item v-if="noticeList.length == 0" placement="top">
                  <el-card>
                    <span>空空如也~</span>
                  </el-card>
                </el-timeline-item>
              </el-timeline>
            </el-scrollbar>
          </div>
        </el-tab-pane>
        <el-tab-pane :label="unreadTitle" name="1">
          <div class="message">
            <el-scrollbar>
              <el-timeline>
                <el-timeline-item
                  v-for="unread in unreadList"
                  :key="unread.id"
                  placement="top"
                  :timestamp="dateFormat(unread.time)"
                >
                  <div class="messageList">
                    <span style="float:left">
                      <img class="doc" src="../../static/doc.png" />
                    </span>
                    <span>
                      <div style="margin-bottom:10px">
                        <a @click="gotoUserPage(unread.sender)">{{unread.sender}}</a>
                        &nbsp;&nbsp;{{unread.action}}
                        <b>「 {{unread.item}}」</b>
                      </div>
                      <div class="messageButton">
                        <span
                          @click="ignore(unread.uid)"
                          class="el-icon-bell readButton"
                        ></span>
                        <span v-if="unread.isRead == 0">
                          <el-button size="mini" @click="permit(unread.noticeId)">同意</el-button>
                        </span>
                      </div>
                    </span>
                  </div>
                </el-timeline-item>
                <el-timeline-item v-if="unreadList.length == 0" placement="top">
                  <el-card>
                    <span>空空如也~</span>
                  </el-card>
                </el-timeline-item>
              </el-timeline>
            </el-scrollbar>
          </div>
        </el-tab-pane>
      </el-tabs>
    </el-drawer>
    <!--侧栏个人信息抽屉-->
    <el-drawer
      :visible.sync="info_drawer"
      :show-close="true"
      :with-header="false"
      size="30%"
      :append-to-body="true"
      style="height: 100%; "
    >
      <el-tabs
        style="margin-top: 50px; height: 100%;"
        type="border-card"
        tab-position="left"
        v-model="activeName_info"
      >
        <el-tab-pane label="个人中心" name="0">
          <span slot="label">
            <i class="el-icon-user-solid"></i> 个人中心
          </span>

          <el-form label-position="left" :model="userInfo" label-width="100px" ref="userInfo">
            <el-form-item label="用户头像" :label-width="labelWidth">
              <el-upload
                class="avatar-uploader"
                action="123"
                :show-file-list="false"
                :on-change="fileChange"
                :http-request="SubbmitFile"
              >
                <img
                  v-if="userInfo.avatar"
                  :src="userInfo.avatar"
                  class="avatar"
                  onerror="javascript:this.src='../../static/avatar.png'"
                />
                <i v-else class="el-icon-plus avatar-uploader-icon"></i>
              </el-upload>
            </el-form-item>
            <el-form-item label="邮箱" :label-width="labelWidth" prop="mail">
              <div style="width: 100%">{{userInfo.mail}}</div>
            </el-form-item>
            <el-form-item label="简介" :label-width="labelWidth">
              <el-input
                type="textarea"
                :autosize="{ minRows: 5, maxRows: 10}"
                placeholder="请输入内容"
                style="width: 100%"
                v-model="userInfo.info"
              ></el-input>
            </el-form-item>

            <el-form-item>
              <el-button type="primary" @click="submitForm()">保 存</el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>
        <el-tab-pane label="密码邮箱" name="5">
          <span slot="label">
            <i class="el-icon-s-tools"></i> 修改密码
          </span>
          <el-collapse v-model="activeNames">
            <el-collapse-item title="更改密码" name="1"></el-collapse-item>
          </el-collapse>
          <el-form ref="form" label-position="left" :model="userInfo" label-width="100px">
            <el-form-item label="旧密码" :label-width="labelWidth" prop="oldPwd">
              <el-input type="password" v-model="oldPassword" style="width: 100%"></el-input>
            </el-form-item>
            <el-form-item label="新密码" :label-width="labelWidth" prop="newPwd">
              <el-input type="password" v-model="newPassword" style="width: 100%"></el-input>
            </el-form-item>
            <el-form-item label="重复密码" :label-width="labelWidth" prop="newPwd2">
              <el-input type="password" v-model="confirmPassword" style="width: 100%"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="updatePwd()">提 交</el-button>
            </el-form-item>
          </el-form>

          <el-collapse v-model="activeNames">
            <el-collapse-item title="改绑邮箱" name="1"></el-collapse-item>
          </el-collapse>
          <el-form ref="form" label-position="left" :model="userInfo" label-width="100px">
            <el-form-item label="新邮箱">
              <el-input v-model="newMail" placeholder="新邮箱"></el-input>
              <el-button type="text" style="width: 85%;" @click="updateMailSendMailToNew()">
                发送验证码
                <i class="el-icon-loading" v-if="updateMailToNewSendFlag" />
              </el-button>
            </el-form-item>
            <el-form-item label="新邮箱验证码">
              <el-input v-model="newMailCode" placeholder="新邮箱验证码"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="updateMail()">改绑邮箱</el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>
      </el-tabs>
    </el-drawer>
  </div>
</template>

<script>
import user from "@/api/user";
import message from "@/api/message";
import axios from "axios";
import screenfull from "screenfull"; //重新安装package.json中的依赖时，一定要注意其版本，发现在5.0上版本时全屏不可用，可用重新安装依赖: npm install --save screenfull@4.2.1
import date from '../utils/date'
export default {
  name: "Navbar",
  data() {
    return {
      userInfo: {},
      search: '',
      name: "未登录",
      avatar: "",
      unreadCnt: 0,
      noticeList: [], //系统通知
      unreadList: [],
      notice_drawer: false,
      info_drawer: false,
      activeNames: ["1", "2"], //激活的折叠面板
      activeName_info: "0", //默认标签
      activeName_notice: "1",
      file: {}, //头像图片
      oldPassword: "",
      newPassword: "",
      confirmPassword: "",
      updatePwdMailCode: "", // 修改密码验证码
      mail: "", //用户原邮箱
      newMail: "", //新邮箱
      newMailCode: "", //新邮箱验证码
      updatePwdSendFlag: false,
      updateMailToOldSendFlag: false,
      updateMailToNewSendFlag: false,
      //侧栏基础元素
      activeNames: ["1", "2"], //激活的折叠面板
      activeName: "0", // 激活的标签
      labelWidth: "85px",
    };
  },
  computed: {
    unreadTitle() {
      return "未读(" + this.unreadCnt + ")";
    },
  },
  components: {},
  created() {
    this.name = localStorage.getItem("name");
    this.getUserInfo();
    this.getMessage();
  },
  methods: {
    click_search(){
      alert('搜索'+this.search)
    },
    getMessage() {
      this.unreadCnt = 0;
      this.unreadList = [];
      message.getNotice().then((res) => {
        this.noticeList = res.data;
        this.total = res.data.length;
        for (var i = 0; i < this.total; i++) {
          if(res.data[i].isRead == 0){
            this.unreadList.push(res.data[i]);
            this.unreadCnt++;
          }
        }
      });
    },
    
    permit(id) {
      console.log("同意");
      message.permit(id).then((res) => {
        console.log(res.message);
        this.$notify({
          title: "提示",
          type: "success",
          message: res.message,
          duration: 1500,
        });
        this.getMessage();
      });
    },
    ignore(id) {
      message.readNotice(id).then((res) => {
        console.log(res.message);
        this.getMessage();
      });
    },
    validAvatar() {
      if (this.avatar == null || this.avatar == undefined) return false;
      return this.avatar.length > 0 ? true : false;
    },
    loginout() {
      user.logout().then((res) => {
        this.avatar = "";
        this.$store.commit("logout"); //清除token等信息
        this.$router.push({ path: "/Login" });
      });
    },
    handleCommand_notice() {
      this.notice_drawer = true;
    },
    handleCommand_info(command) {
      // 点击头像触发的动作
      switch (command) {
        case "logout":
          {
            this.logout();
          }
          break;
        case "goUserInfo":
          {
            this.info_drawer = true;
          }
          break;
      }
    },
    gotoUserPage(name){
      this.$router.push({path:'/Profile/'+name})
      this.notice_drawer = false
    },
    getUserInfo() {
      //用户信息
      if (this.$store.state.token) {
        user.getUserInfo().then((res) => {
          this.userInfo = res.data
          this.$store.dispatch('setUserInfo',res.data)
        });
      }
    },
    submitForm() {
      //修改用户信息
      var a = this.userInfo;
      user.editUser(a.gender, a.birth, a.job, a.info).then((response) => {
        if (response.code == 200) {
          this.$message({
            type: "success",
            message: "更新成功",
          });
        } else {
          this.$message({
            type: "error",
            message: "更新失败",
          });
        }
      });
    },
    updatePwd() {
      //修改密码
      if (this.oldPassword.length <= 0) {
        this.$notify({
          title: "提示",
          type: "warning",
          message: "原密码不能为空",
          duration: 2000,
        });
        return;
      }
      if (this.newPassword != this.confirmPassword) {
        this.$notify({
          title: "提示",
          type: "warning",
          message: "两次输入密码不一致",
          duration: 2000,
        });
        return;
      }
      user.updatePassword(this.oldPassword, this.newPassword).then((res) => {
        this.$notify({
          title: "提示",
          type: "success",
          message: "修改成功",
          duration: 2500,
        });
        this.drawer = false;
        this.$store.commit("logout"); //清除token等信息
        this.$router.push({ path: "/Login" });
      });
    },
    updateMailSendMailToNew() {
      //更改密码发送验证码到新邮箱
      this.updateMailToNewSendFlag = true;
      user
        .sendmail(this.newMail)
        .then((res) => {
          this.$notify({
            title: "提示",
            type: "success",
            message: res.message,
            duration: 2000,
          });
          this.updateMailToNewSendFlag = false;
        })
        .catch((error) => {
          console.log(error), (this.updateMailToNewSendFlag = false);
        });
    },
    updateMail() {
      //修改邮箱
      var reg = new RegExp(
        /^([a-zA-Z0-9._-])+@([a-zA-Z0-9_-])+(\.[a-zA-Z0-9_-])+/
      );
      if (!reg.test(this.newMail)) {
        //检测字符串是否符合正则表达式
        this.$notify({
          title: "提示",
          type: "warning",
          message: "邮箱格式不正确",
          duration: 2000,
        });
        return;
      }
      if (this.newMailCode.length <= 0) {
        //
        this.$notify({
          title: "提示",
          type: "warning",
          message: "请填写验证码",
          duration: 2000,
        });
        return;
      }
      user.updateMail(this.newMail, this.newMailCode).then((res) => {
        this.$notify({
          title: "提示",
          type: "success",
          message: "改绑成功",
          duration: 2500,
        });
        this.drawer = false;
        this.newMail = "";
        this.newMailCode = "";
        this.getUserInfo();
      });
    },
    fileChange(e, list) {
      //上传头像.子函数
      this.file = e;
    },
    SubbmitFile() {
      //上传头像
      let param = new FormData();
      param.append("file", this.file.raw);
      let config = {
        headers: { "Content-Type": "multipart/form-data" },
      };
      config.headers["Authorization"] = this.$store.state.token;
      console.log(param);
      axios
        .post("/api/user/uploadAvatar/", param, config, { timeout: 900000 })
        .then((response) => {
          if (response) {
            this.$router.go(0);
            this.file = {};
            // console.log(response.data);
          } else {
            alert(response.data.msg);
          }
        })
        .catch(function (err) {
          console.log(err);
        });
    },
    dateFormat(time) {//将时间戳转化为几分钟前，几小时前
      return date.timeago(time);
    },
    screen() {
      if (!screenfull.enabled) {
        this.$message("无法全屏");
        return false;
      }
      screenfull.toggle();
    },
  },
};
</script>

<style scoped>
a:hover {
  cursor: pointer;
  color: #0098ea;
}
img {
  height: 33px;
  vertical-align: middle;
}
.notify {
  color: grey;
  font-size:33px;
  margin-top:3px
}
.readButton {
  font-size:20px;
  margin-left:20px;
  width:40px
}
.messageButton {
  display: flex;
  flex-flow: row-reverse nowrap;
  align-items: center;
  font-size: 14px;
}
.box-card {
  z-index: 100;
  position: relative;
  display: -webkit-flex; /* Safari */
  display: flex;
  justify-content: flex-end;
  align-items: center;
  height: 60px;
  width: 100%;
  box-shadow: 0 2px 2px 0 #e6e6e6;
}
.box-card span {
  min-width: 55px;
}
.avatar {
  width: 33px;
  border-radius: 50%;
}
.el-dropdown-link {
  cursor: pointer;
}
.nav {
  flex: 1;
  /* float: left;
  width: 170px; */
  margin-left: 20px;
  background-image: url(../../static/logo.png);
  background-size: 106px;
  background-repeat: no-repeat;
}
.doc {
  width: 50px;
  height: 60px;
  margin-right: 10px;
}
.message {
  width: 100%;
  height: 90vh;
}
.el-scrollbar {
  height: 100%;
}
.el-timeline {
  margin-left: -40px;
}
.messageList {
  height: 90px;
  width: 100%;
  margin: 0 auto;
  font-size: 16px;
  border-bottom: 1px solid rgb(222, 222, 222);
}
</style>
