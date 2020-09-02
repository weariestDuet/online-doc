<template>
  <div style="height:100%">
    <navbar/>
    <div class="layout">
      <sidebar/>
      <router-view/>
    </div>
  </div>
</template>

<script>
import navbar from "@/components/Navbar"; 
import sidebar from "@/components/Sidebar";
import user from '@/api/user'
export default {
  name: "Layout",
  components: { navbar,sidebar }
}
</script>
<style scoped>
.layout {
  display: flex;
  flex-flow: row nowrap;
  height:calc(100% - 60px)
}
</style>