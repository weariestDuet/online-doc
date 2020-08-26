<template>
  <div style="height:100%;">
    <Navbar style="height:7% " @chatting="chatting"/>
    <el-container style="height:93%">
      <el-aside  style="width:16%;">
        <Sidebar/>
      </el-aside>
      <el-main style="width: 84%">
        <router-view/>
      </el-main>
    </el-container>
  </div>
</template>

<script>
import Navbar from "@/components/Navbar"; 
import Sidebar from "@/components/Sidebar";
export default {
  name: "Layout",
  components: {
    Navbar,Sidebar
  },
  methods: {
    chatting(){
      this.$refs.chatting_dialog.open();
    },
  }
}
</script>