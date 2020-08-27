<template>
  <div style="height:100%">
    <!-- 分享框 -->
    <el-dialog title="分享"  :visible.sync="dialogVisible"  width="30%">
      <el-input v-model="url" :readonly="true">
        <el-button slot="append" v-clipboard:copy="url" v-clipboard:success="onCopy" v-clipboard:error="onError">复制链接</el-button>
      </el-input>
    </el-dialog>
    <h2 style="text-indent:1.5em">我创建的</h2>
    <el-divider></el-divider>
    <!-- 平铺视图 -->
    <div  v-if="layout" class="tile" >
      <div class="item" v-for="(o, fileId) in FileData" :key="fileId">
          <div class="core" @mouseenter="pEnter(o.fileId)" @mouseleave="pLeave(o.fileId)">
            <div style="height:20px" >
              <el-dropdown @command="handleCommand">
                <div style="width:100px">
                    <i class="el-icon-s-tools icon"></i>
                </div>
                <el-dropdown-menu slot="dropdown">
                  <el-dropdown-item command="open">新标签页打开</el-dropdown-item>
                  <el-dropdown-item command="collect" divided>收藏</el-dropdown-item>
                  <el-dropdown-item command="share">分享</el-dropdown-item>
                  <el-dropdown-item command="delete">删除</el-dropdown-item>
                </el-dropdown-menu>
              </el-dropdown>
            </div>
            <img src="../../static/doc.png" @click="goto(o.fileId)">
            <div class="fileName">
              <a href="#">{{o.fileName}}</a>
            </div>
          </div>
      </div>
    </div>

    <!-- 表格视图 -->
    <div v-if="!layout" class="tile" style="margin-top: 20px">
      <el-table :data="FileData" :cell-style="{padding:'1px'}">
        <el-table-column prop="fileName" min-width="55%">
          <template slot-scope="scope">
            <h3 class="fileItem">{{scope.row.fileName}}</h3>
          </template>
        </el-table-column>
        <el-table-column prop="modifyTime"  label="最后修改时间" min-width="25%">
          <template slot-scope="scope">
            {{dateFormat(scope.row.modifyTime)}}
          </template>
        </el-table-column>
        <el-table-column label="操作" min-width="20%">
            <template slot-scope="scope">
                <el-button size="mini"
                    @click="goto(scope.row.fileId)">查看</el-button>
                <el-button size="mini" type="primary"
                    @click="collectFile(scope.row.fileId)">收藏</el-button>
                <el-button size="mini" type="danger"
                    @click="deleteFile(scope.row.fileId)">删除</el-button>
            </template>
          </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script>
  import file from '@/api/file'
  import date from '../utils/date.js'
  export default {
    name: "my-creation",
    data(){
      return{
        url: '',
        FileData:[],
        index: 0, //当前选中文档id
        dialogVisible:false,
        keyword:''
      }
    },
    computed:{
      layout() {
        return this.$store.state.layout==1
      }
    },
    created() {
      this.getFile()
    },
    methods:{
      handleCommand(command) {
        if(command == 'open') {
          let routeUrl = this.$router.resolve({path: '/File/'+this.index});
          window.open(routeUrl.href, '_blank') }
        else if(command == 'collect') {this.collectFile(this.index)}
        else if(command == 'share') {
          this.url = 'http://39.107.228.168/#/File/'+this.index
          this.dialogVisible = true
        }
        else if(command == 'delete') {this.deleteFile(this.index)}
      },
      getFile(){
        file.getCreation().then((res)=>{
          console.log(res.message)
          this.FileData=res.data
        })
      },
      deleteFile(id){
        file.Deleted(id).then(res=>{
          this.$notify({title: '提示',type: 'success',message: res.message,duration: 1700 });
          this.getFile()
        })
      },
      collectFile(id){
        file.collectDocument(id).then(res=>{
          this.$notify({title: '提示',type: 'success',message: res.message,duration: 1000 });
        })
      },
      getFileData(keyword){
        file.getCreation().then((res)=>{
          this.FileData=[];
          for(var i=0;i<res.data.length;i++){
            if(this.keyword==res.data[i].fileName)
              this.FileData.push(res.data[i]);
            if(this.keyword=='')
              this.FileData=res.data;
          }
        })
      },
      dateFormat(time) {
        return date.timeago(time)
      },
      goto(id){
        this.$router.push({path: '/File/'+id})
      },
      onCopy(){
        this.$message({
          message: '复制成功',
          type: 'success'
        });
      },
      onError(){
        this.$message.error('复制失败');
      },
      pEnter(index) {
        this.index = index
      },
      pLeave(index) {}
    }
  }
</script>

<style scoped>
@import url(../assets/tile);
</style>
