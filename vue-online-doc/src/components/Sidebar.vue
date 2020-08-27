<template>
  <div class="sidebar">
    <div class="user">
      <img v-if="validAvatar()" :src="avatar"/>
      <img v-else src="../../static/handsome.jpg"/>
      <h3>Zero</h3>
    </div>
    <el-menu :default-active="this.$route.path" :router="true">
      <el-menu-item index="/">
        <i class="el-icon-tickets"></i>
        <span class="font" slot="title">工作台</span>
      </el-menu-item>
      <el-menu-item index="/TeamSpace">
        <i class="el-icon-menu"></i>
        <span class="font" slot="title">团队空间</span>
      </el-menu-item>
      <el-menu-item index="/Recycle">
        <i class="el-icon-delete"></i>
        <span class="font" slot="title">回收站</span>
      </el-menu-item>
      <el-menu-item index="/Edit">
        <i class="el-icon-delete"></i>
        <span class="font" slot="title">新建</span>
      </el-menu-item>
      <el-menu-item index="/TemplateLibrary">
        <i class="el-icon-delete"></i>
        <span class="font" slot="title">模板库</span>
      </el-menu-item>
    </el-menu>
  </div>
</template>

<script>
export default {
  name: "sidebar",
  methods: {
    validAvatar() {
      return false
      // if (this.avatar == null || this.avatar == undefined) return false;
      // return this.avatar.length > 0 ? true : false;
    },
  }
};
</script>

<style scoped>
.sidebar {
  display: flex;
  flex-flow: column nowrap;
  min-width:300px;
  height:calc(100% - 4px);
  border:2px solid #e6e6e6;
  background-color: #f4f4f4;
}
.user {
  width: 100%;
  text-align: center;
  border-bottom: 1px solid #dedede;
  margin-bottom: 50px;
}
.user img {
  margin-top: 40px;
  height: 70px;
  width: 70px;
}
.el-menu {
  width: 100%;
  text-align: center;
  background-color: #f4f4f4;
  border: 0;
}
.el-menu-item {
  border-bottom: 1px solid #dedede;
  margin-left: -30px
}
.el-container {
  height: 100%;
}
.item {
  color: #424e67;
  font-size: 14px;
}
.font {
  font-size: 17px;
  color: rgb(90, 90, 90);
}

span {
  text-align: center;
}
</style>
