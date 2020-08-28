<template>
  <div style="height:100%">
    <h2 style="text-indent:1.5em;user-select:none;">我的创建</h2>
    <el-divider></el-divider>
    <file-list :FileData="FileData" type="creation"/>
  </div>
</template>

<script>
  import file from '@/api/file'
  import FileList from './FileList'
  export default {
    name: "my-creation",
    components : {FileList},
    data(){
      return{
        FileData:[]
      }
    },
    created() {
      this.getFile()
    },
    methods:{
      getFile(){
        file.getCreation().then((res)=>{
          console.log(res.message)
          this.FileData=res.data
        })
      }
    }
  }
</script>
