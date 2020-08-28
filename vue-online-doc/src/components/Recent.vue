<template>
  <div style="height:100%">
    <h2 style="text-indent:1.5em;user-select:none;">最近浏览</h2>
    <el-divider></el-divider>
    <file-list :FileData="FileData" type="recent"/>
  </div>
</template>

<script>
  import file from '@/api/file'
  import FileList from './FileList'
  export default {
    name: "Recent",
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
        file.getRecent().then((res)=>{
          this.FileData=res.data
        })
      }
    }
  }
</script>