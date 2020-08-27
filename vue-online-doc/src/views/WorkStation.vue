<template>
  <div class="workStation">
    <div style="flex:1">
      <el-tabs v-model="activeName" @tab-click="handleClick">
        <el-tab-pane label="我的创建" name="MyCreation">
          <my-creation :random="refreshCreation" />
        </el-tab-pane>
        <el-tab-pane label="我的收藏" name="MyStar">
          <my-star :random="refreshStar" />
        </el-tab-pane>
        <el-tab-pane label="最近浏览" name="Recent">
          <recent :random="refreshRecent" />
        </el-tab-pane>
      </el-tabs>
    </div>

    <div style="width:50px">
      <el-tooltip effect="dark" content="平铺" placement="top" v-if="!$store.state.layout">
        <div class="layout_button">
          <i @click="layoutTile()" class="el-icon-menu"></i>
        </div>
      </el-tooltip>
      <el-tooltip effect="dark" content="列表" placement="top" v-else>
        <div class="layout_button">
          <i @click="layoutList()" class="el-icon-s-unfold"></i>
        </div>
      </el-tooltip>
    </div>

  </div>
</template>


<script>
import Recent from "../components/Recent";
import MyStar from "../components/MyStar";
import MyCreation from "../components/MyCreation";

export default {
  name: "WorkStation",
  components: {
    Recent,
    MyStar,
    MyCreation,
  },
  data() {
    return {
      isTile: true,
      isActive: true, //默认不隐藏
      activeName: "MyCreation",
      refreshCreation: 0,
      refreshStar: 0,
      refreshRecent: 0,
    };
  },
  created() {
    if (
      localStorage.getItem("name") == "" ||
      localStorage.getItem("name") == undefined
    ) {
      this.$confirm("您还未登陆/或者登陆已过期", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          this.$router.push({ path: "/Login" });
        })
        .catch(() => {
          // this.$router.push({path: '/Login'})
        });
    }
  },
  methods: {
    newFile() {
      this.$router.push({ path: "/Edit" });
    },
    gotoTLibrary() {
      this.$router.push({ path: "/TemplateLibrary" });
    },
    layoutTile() {
      this.$store.dispatch("setLayoutStatus", 1);
    },
    layoutList() {
      this.$store.dispatch("setLayoutStatus", 0);
    },
    handleClick(tab, event) {
      if (this.activeName == "MyStar") {
        this.refreshStar = Math.random();
      } else if (this.activeName == "Recent") {
        this.refreshRecent = Math.random();
      }
    },
  },
};
</script>
<style scoped>
@import url(../assets/tile);
body {
  margin: 0;
  padding: 0;
  background-color: #fff;
}
</style>
