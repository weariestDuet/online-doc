<template>
  <div class="file_layout">
    <div class="file" v-if="readable">
      <div class="header">
        <div class="title">{{this.title}}</div>
        <el-button @click="editing = true" v-if="writable&&!editing">编辑</el-button>
        <el-button @click="updateFile" v-if="writable&&editing">更新保存</el-button>
        <!-- 权限设置 -->
        <el-cascader
          v-if="authable&&!editing"
          style="width:120px"
          v-model="value"
          :options="options"
          @change="handleChange"
        ></el-cascader>

        <el-tooltip content="展开评论">
          <span>
            <i @click="isActive = !isActive" class="el-icon-d-caret"></i>
          </span>
        </el-tooltip>
      </div>

      <div class="body">
        <el-scrollbar v-if="false" style="height:100%">
          <div v-if="!editing" v-html="this.content">{{this.content}}</div>
        </el-scrollbar>

        <edit v-if="editing" :editBody="content"></edit>
      </div>

      <el-popover
        style="position: fixed; bottom: 40px; right: 40px"
        placement="top-start"
        title="文档信息"
        width="300"
        trigger="hover"
        :content="fileInfo"
      >
        <div v-html="fileInfo">{{fileInfo}}</div>
        <el-button slot="reference">
          <a class="cd-top">Info</a>
        </el-button>
      </el-popover>
    </div>

    <!-- 评论区 -->
    <div class="comment">
      <collapse>
        <el-card v-if="isActive&&!editing" show="never">
          <div class="box">
            <textarea id="textpanel" placeholder="请输入评论内容" v-model="messageBody"></textarea>
            <div class="boxButton">
              <el-button type="primary" plain @click="sendMessage">发送评论</el-button>
              <el-button type="info" @click="messageBody = "" ">取消评论</el-button>
            </div>
          </div>

          <div>
            <div v-for="d in discuss" :key="d.discussId">
              <div class="comment">
                <img v-if="!d.avatar" src="../../static/avatar.svg" />
                <img
                  v-else
                  :src="d.avatar"
                  style="width:50px; height:50px"
                  onerror="javascript:this.src='../../static/avatar.svg'"
                />
                <span>
                  <div>
                    <el-link class="userName" :underline="false">{{d.name}}</el-link>
                    <span class="timeAgo">{{(dateFormat(d.time))}}</span>
                  </div>
                  <div class="content">{{d.content}}</div>
                  <el-divider content-position="right">
                    <el-link
                      :underline="false"
                      class="el-icon-delete"
                      v-if="this.$store.state.name == d.name"
                      @click="deleteMessage(d.discussId)"
                    />
                  </el-divider>
                </span>
              </div>
            </div>
            <div v-if="discuss.length ==0">还没有评论，快来抢沙发吧！</div>
          </div>
        </el-card>
      </collapse>
    </div>
  </div>
</template>

<script>
import date from "../utils/date";
import file from "@/api/file";
import Edit from "./Edit";
import collapse from "../assets/collapse.js";
import message from "@/api/message";
export default {
  name: "file",
  components: { Edit, collapse },
  data() {
    return {
      activeNames: ["1"],
      isActive: false,
      messageBody:'',
      fileId: 0,
      fileInfo: "",
      url: "",
      value: ["other", "RD"],
      options: [
        {
          value: "other",
          label: "权限设置-其他用户",
          children: [
            { value: "RD", label: "允许查看与评论" },
            { value: "R", label: "仅允许查看" },
            { value: "N", label: "不允许查看" },
          ],
        },
        {
          value: "group",
          label: "权限设置-团队用户",
          children: [
            { value: "RDW", label: "允许查看评论与编辑" },
            { value: "RD", label: "允许查看与评论" },
            { value: "R", label: "仅允许查看" },
          ],
        },
      ],
      authList: [],
      discuss: [],
      title: "",
      editing: false,
      readable: true,
      writable: true,
      authable: false,
      discussable: false,
      auth: {},
      content: null,
    };
  },
  created() {
    this.loadFile();
    this.url = "http://39.107.228.168/#" + this.$route.path;
  },
  methods: {
    handleChange() {
      file.updateAuth(this.fileId, this.value[0], this.value[1]).then((res) => {
        this.$notify({
          title: "提示",
          type: "success",
          message: res.message,
          duration: 1000,
        });
      });
    },
    sendMessage() {
      if (this.messageBody.length <= 0) {
        this.$notify({
          title: "提示",
          type: "error",
          message: "字段不完整",
          duration: 2000,
        });
        return;
      }
      message.sendMessage(this.fileId, this.messageBody).then((res) => {
        this.$notify({
          title: "提示",
          type: "success",
          message: "留言成功",
          duration: 1500,
        });
        this.messageBody = "";
        this.loadFile();
      });
    },
    deleteMessage(id) {
      this.$confirm("是否删除此留言?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          message.deleteMessage(id).then((res) => {
            this.$notify({
              title: "提示",
              type: "success",
              message: "删除成功",
              duration: 2000,
            });
            this.loadMessage();
          });
        })
        .catch(() => {});
    },
    loadFile() {
      this.fileId = this.$route.params.fileId;
      file.getDocument(this.fileId).then((res) => {
        console.log(res.data);
        // this.$store.commit("login", res.data.map); //存储token
        var a = res.data;
        this.auth = a.auth;
        this.title = a.title;
        this.content = a.body;
        this.discuss = a.discuss == null ? [] : a.discuss
        var sort = a.userId != 0 ? "个人文档" : "团队文档";
        var updateTime = this.dateFormat(a.time);
        var info =
          "文档名：" +
          a.title +
          "<br/>类别：" +
          sort +
          "<br/>更新时间：" +
          updateTime;
          this.fileInfo = info
        // this.init_authority();
      });
    },
    init_authority() {
      //初始化其他用户权限设置选项  ！！bug:不能初始化团队选项
      if (this.auth.otherRead == 0) this.value = ["other", "N"];
      else if (this.auth.otherDiscuss === 0) this.value = ["other", "R"];

      if (this.$store.state.roles == "USER") {
        this.readable = true;
        this.writable = true;
        this.authable = true;
        this.discussable = true;
      } else if (this.$store.state.roles == "OTHER") {
        if (this.auth.otherRead == 1) this.readable = true;
        else this.readable = false;
        if (this.auth.otherDiscuss == 1) this.discussable = true;
        else this.discussable = false;
      } else if (this.$store.state.roles == "GROUP") {
        this.readable = true;
        if (this.auth.groupDiscuss == 1) this.discussable = true;
        else this.discussable = false;
        if (this.auth.groupWrite == 1) {
          this.writable = true;
        } else this.writable = false;
      }
      if (this.readable == false) {
        this.$notify({
          title: "提示",
          type: "error",
          message: "无访问权限",
          duration: 2500,
        });
      }
    },
    updateFile() {
      file.updateDoc(this.fileId, this.title, this.content).then((res) => {
        this.$notify({
          title: "提示",
          type: "success",
          message: res.message,
          duration: 1000,
        });
      });
    },
    handleClose(done) {
      done();
    },
    gotoUserPage(name) {
      this.$router.push({ path: "/Profile/" + name });
    },
    dateFormat(time) {
      return date.timeago(time);
    },
  },
};
</script>

<style>
.file {
  display: flex;
  flex-flow: column nowrap;
  flex: 1;
  background-color: #fff;
  border-right: 2px solid #e6e6e6;
}
.header {
  display: flex;
  align-items: center;
  height: 50px;
  padding: 30px 70px;
  border-bottom: 2px solid #f4f4f4;
}
.header .el-button {
  width: 120px;
}
.body {
  height: calc(100vh - 166px);
  padding: 0 80px;
}
.title {
  flex: 1;
  text-align: left;
  font-size: 30px;
  font-weight: bold;
}
.file i {
  font-size: 35px;
  color: grey;
}

.comment {
  width: 100%;
  margin: 0 auto;
}
.comment img {
  float: left;
  width: 50px;
  border-radius: 50%;
  margin-right: 20px;
}
.comment span {
  width: 80px;
  user-select: none;
}
.comment .userName {
  font-size: 17px;
  color: #303133;
}
.comment .timeAgo {
  color: #909399;
}
.comment .content {
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
}

.file_layout {
  display: flex;
  flex-flow: row nowrap;
  height: calc(100%-60px);
  width: 100%;
}
.comment {
  width: 400px;
}
.box {
  width: 90%;
  margin: 0 auto 40px;
}
.box textarea {
  color: #606266;
  padding: 10px 5px 5px 10px;
  resize: none;
  width: 100%;
  height: 100%;
}
.boxButton {
  display: flex;
  justify-content: flex-end;
}
.boxButton .el-button {
  height: 35px;
  width: 100px;
  margin: 10px 0px 10px 10px;
}

.el-scrollbar__wrap {
  overflow-x: hidden !important;
}
.cd-top {
  display: inline-block;
  height: 40px;
  width: 40px;
  position: fixed;
  bottom: 40px;
  right: 40px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.05);
  overflow: hidden;
  text-indent: 100%;
  white-space: nowrap;
  background: rgba(0, 0, 0, 0.8) url(../../static/info.png) no-repeat center;
}
</style>
