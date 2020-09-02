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
          <div class="core" @mouseenter="pEnter(o.docId)" @mouseleave="pLeave(o.docId)">
            <div style="height:20px" >
              <el-dropdown @command="handleCommand">
                <div style="width:100px">
                    <i class="el-icon-s-tools icon"></i>
                </div>
                <el-dropdown-menu slot="dropdown">
                  <el-dropdown-item command="open">新标签页打开</el-dropdown-item>
                  <el-dropdown-item v-if="!isStar" command="collect" divided>收藏</el-dropdown-item>
                  <el-dropdown-item v-if="isStar" command="uncollect" divided>取消收藏</el-dropdown-item>
                  <el-dropdown-item command="share">分享</el-dropdown-item>
                  <el-dropdown-item command="delete">删除</el-dropdown-item>
                </el-dropdown-menu>
              </el-dropdown>
            </div>
            <img src="../../static/doc.png" @click="goto(o.docId)">
            <div class="fileName">
              <a href="#">{{o.title}}</a>
            </div>
          </div>
      </div>
    </div>

    <!-- 表格视图 -->
    <div v-if="!layout&&FileData.length>0" class="tile" style="margin-top: 20px">
      <el-table :data="FileData" :cell-style="{padding:'1px'}">
        <el-table-column prop="title" min-width="55%">
          <template slot-scope="scope">
            <h3 class="fileItem">{{scope.row.title}}</h3>
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
                    @click="goto(scope.row.docId)">查看</el-button>
                <el-button size="mini" type="primary" v-if="!isStar"
                    @click="collectFile(scope.row.docId)">收藏</el-button>
                <el-button size="mini" type="primary" v-if="isStar"
                    @click="uncollectFile(scope.row.docId)">取消收藏</el-button>
                <el-button size="mini" type="danger" v-if="isCreation"
                    @click="deleteFile(scope.row.docId)">删除</el-button>
            </template>
          </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script>
import user from '@/api/user'
import group from '@/api/group'
import date from '../utils/date.js'
  import file from '@/api/file'
  export default {
    name: "team-file",
    props: { userInfo: Object },
    data(){
      return{
        url: '',
        dialogVisible:false,
        index: 0, //当前高亮的图标
      }
    },
    props:{ 
      type: String,
      FileList: Array
      },
    computed:{
      FileData() {
        if(this.type == 'team') return this.FileList == null ? [] : this.FileList
        if(this.type == 'creation') return this.$store.state.userInfo.creation == null ? [] : this.$store.state.userInfo.creation
        if(this.type == 'star') return this.$store.state.userInfo.favor == null ? [] : this.$store.state.userInfo.favor
        if(this.type == 'recent') return this.$store.state.userInfo.recent == null ? [] : this.$store.state.userInfo.recent
      },
      isStar() { return this.type == "star" },
      isCreation() { return this.type == "creation"},
      layout() {return this.$store.state.layout==1}
    },
    methods:{
      handleCommand(command) {
        if(command == 'open') {
          let routeUrl = this.$router.resolve({path: '/File/'+this.index});
          window.open(routeUrl.href, '_blank') }
        else if(command == 'collect') {this.collectFile(this.index)}
        else if(command == 'uncollect') {this.uncollectFile(this.index)}
        else if(command == 'share') {
          this.url = 'http://39.107.228.168/#/File/'+this.index
          this.dialogVisible = true
        }
        else if(command == 'delete') {this.deleteFile(this.index)}
      },
      getUserInfo() {
        if (this.$store.state.token) {
          user.getUserInfo().then((res) => {
            this.$store.dispatch('setUserInfo',res.data)
          });
        }
      },
      deleteFile(id) {
        this.$confirm('确认删除？').then(_ => {
          file.deleteDoc(id).then(res => {
            this.$notify({title: '提示', type: 'success', message: res.message, duration: 1700});
            this.getUserInfo()
            this.$emit('refresh')
          })
          done();
        }).catch(_ => {});
      },
      collectFile(id){
        file.collectDoc(id).then(res=>{
          this.getUserInfo()
          this.$notify({title: '提示',type: 'success',message: res.message,duration: 1000 });
        })
      },
      uncollectFile(id){
        file.uncollectDoc(id).then(res=>{
          this.getUserInfo()
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
      dateFormat(time) {
        return date.timeago(time)
      },
      pEnter(index) {
        this.index = index
      },
      pLeave(index) {},
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
