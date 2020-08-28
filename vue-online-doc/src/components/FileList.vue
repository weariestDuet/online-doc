<template>
  <div style="height:100%">
    <!-- 分享框 -->
    <el-dialog title="分享"  :visible.sync="dialogVisible"  width="30%">
      <el-input v-model="url" :readonly="true">
        <el-button slot="append" v-clipboard:copy="url" v-clipboard:success="onCopy" v-clipboard:error="onError">复制链接</el-button>
      </el-input>
    </el-dialog>
    
    <div v-if="FileData.length==0" class="none">
      <h1>無</h1>
      <h2>没有文件</h2>
    </div>
    <!-- 平铺视图 -->
    <div  v-if="layout&&FileData.length>0" class="tile" >
      <div class="item" v-for="(o, fileId) in FileData" :key="fileId">
          <div class="core" @mouseenter="pEnter(o.fileId)" @mouseleave="pLeave(o.fileId)">
            <div style="height:20px" >
              <el-dropdown @command="handleCommand">
                <div style="width:100px">
                    <i class="el-icon-s-tools icon"></i>
                </div>
                <el-dropdown-menu slot="dropdown">
                  <el-dropdown-item command="open">新标签页打开</el-dropdown-item>
                  <el-dropdown-item v-if="!isRecycle&&!isStar" command="collect" divided>收藏</el-dropdown-item>
                  <el-dropdown-item v-if="isStar" command="collect" divided>取消收藏</el-dropdown-item>
                  <el-dropdown-item v-if="!isRecycle" command="share">分享</el-dropdown-item>
                  <el-dropdown-item v-if="isCreation" command="remove">删除</el-dropdown-item>
                  <el-dropdown-item v-if="isRecycle" command="recover">恢复</el-dropdown-item>
                  <el-dropdown-item v-if="isRecycle" command="delete">彻底删除</el-dropdown-item>
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
    <div v-if="!layout&&FileData.length>0" class="tile" style="margin-top: 20px">
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
                <el-button size="mini" type="primary" v-if="!isRecycle&&!isStar"
                    @click="collectFile(scope.row.fileId)">收藏</el-button>
                <el-button size="mini" type="primary" v-if="isStar"
                    @click="collectFile(scope.row.fileId)">取消收藏</el-button>
                <el-button size="mini" type="danger" v-if="isCreation"
                    @click="removeFile(scope.row.fileId)">删除</el-button>
                <el-button size="mini" type="primary" v-if="isRecycle"
                    @click="recoverFile(scope.row.fileId)">恢复</el-button>
                <el-button size="mini" type="danger" v-if="isRecycle"
                    @click="deleteFile(scope.row.fileId)">彻底删除</el-button>
            </template>
          </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script>
import date from '../utils/date.js'
  import file from '@/api/file'
  export default {
    name: "team-file",
    data(){
      return{
        url: '',
        dialogVisible:false,
        index: 0, //当前高亮的图标
      }
    },
    props:{ 
      type: String,
      groupName: String, 
      FileData: Array, 
      },
    computed:{
      isRecycle() {
        return this.type == "recycle"
      },
      isStar() {
        return this.type == "star"
      },
      isCreation() {
        return this.type == "creation"
      },
      layout() {
        return this.$store.state.layout==1
      }
    },
    watch: {
      groupName(val) {
        // console.log(val)
        this.$store.dispatch('setCurGroupName',val)
        console.log(this.$store.state.groupName)
        // this.getFile()
      }
    },
    created() {
      if(this.$store.state.groupName!=''){
        // this.getFile()
      }
    },
    methods:{
      dateFormat(time) {
        return date.timeago(time)
      },
      pEnter(index) {
        this.index = index
      },
      pLeave(index) {},
      handleCommand(command) {
        if(command == 'open') {
          let routeUrl = this.$router.resolve({path: '/File/'+this.index});
          window.open(routeUrl.href, '_blank') }
        else if(command == 'collect') {this.collectFile(this.index)}
        else if(command == 'share') {
          this.url = 'http://39.107.228.168/#/File/'+this.index
          this.dialogVisible = true
        }
        else if(command == 'remove') {this.deleteFile(this.index)}
        else if(command == 'recover') {this.recoverFile(this.index)}
        else if(command == 'delete') {this.deleteFile(this.index)}
      },
      recoverFile(id) {
        file.recoverDeleted(id).then(res => {
          this.$notify({title: '提示', type: 'success', message: res.message, duration: 1700});
          file.getDeleted().then((res) => {
            this.FileData = res.data
          })
        })
      },
      deleteFile(id) {
        this.$confirm('确认删除？')
          .then(_ => {
            file.foreverDeleted(id).then(res => {
              this.$notify({title: '提示', type: 'success', message: res.message, duration: 1700});
              file.getDeleted().then((res) => {
                this.FileData = res.data
              })
            })
            done();
          })
          .catch(_ => {});
      },
      removeFile(id){
        console.info('delete file: id='+id)
        file.Deleted(id).then(res=>{
          this.$notify({title: '提示',type: 'success',message: res.message,duration: 1700 });
          file.getFileList().then((res)=>{this.FileData=res.data})
        })
      },
      collectFile(id){
        file.collectDocument(id).then(res=>{
          this.$notify({title: '提示',type: 'success',message: res.message,duration: 1000 });
        })
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
    }
  }
</script>

<style scoped>
@import url(../assets/tile);
.none {
  display: flex;
  flex-flow: column nowrap;
  align-items: center;
  margin-top:150px;
  user-select:none;
  color: rgb(230,230,230);
}
.none h1 {
  margin: 0;
  height: 120px;
  font-size: 100px;
  font-weight: 400;
}
.none h2 {
  margin: 20px;
  height: 30px;
  line-height: 20px;
  font-size: 20px;
  font-weight: 400;
}
</style>
