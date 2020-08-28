<template>
  <div style="height:100%">
    <h2 style="text-indent:1.5em;user-select:none;">我的收藏</h2>
    <el-divider></el-divider>
    <file-list :FileData="FileData" type="star"/>
  </div>
</template>

<script>
  import file from '@/api/file'
  import FileList from './FileList'
  export default {
    name: "MyStar",
    components : {FileList},
    props:  {random : Number},
    data(){
      return{
        FileData:[]
      }
    },
    watch:{
      random(val){console.log(val), this.getFile() }
    },
    created() {
      this.getFile()
    },
    methods:{
      getFile(){
        file.getMyCollecting().then((res)=>{
          console.log(res.message)
          console.log(res.data)
          this.FileData=res.data
        })
      }
    }
  }
</script>