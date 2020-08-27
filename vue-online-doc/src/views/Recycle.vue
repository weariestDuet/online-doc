<template>
  <div class="Recycle">
    <div style="flex:1">
      <div>
        <h2 style="text-indent:1.5em">文件</h2>
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
                      <el-dropdown-item command="recoverFile">恢复</el-dropdown-item>
                      <el-dropdown-item command="deletefile">彻底删除</el-dropdown-item>
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
                        @click="recoverFile(scope.row.fileId)">恢复</el-button>
                    <el-button size="mini" type="danger"
                        @click="deleteFile(scope.row.fileId)">彻底删除</el-button>
                </template>
              </el-table-column>
          </el-table>
        </div>

      </div>
    </div>

    <div style="width:50px">
      <el-tooltip class="item" effect="dark" content="平铺" placement="top" v-if="!$store.state.layout">
        <div class="layout_button">
          <i @click="layoutTile()" class="el-icon-menu"></i>
        </div>
      </el-tooltip>
      <el-tooltip class="item" effect="dark" content="列表" placement="top" v-else>
        <div class="layout_button">
          <i @click="layoutList()" class="el-icon-s-unfold"></i>
        </div>
      </el-tooltip>
    </div>

  </div>

</template>

<script>
  import date from '../utils/date.js'
  import file from '@/api/file'
  export default {
    name: "Recycle",
    data(){
      return{
        FileData:[],
        index: 0,
        keyword: ''
      }
    },
    computed: {
      layout() {
        return this.$store.state.layout == 1
      }
    },
    created() {
      if(localStorage.getItem('name')==''||localStorage.getItem('name')==undefined){
        this.$confirm('您还未登陆/或者登陆已过期', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$router.push({path: '/Login'})
        }).catch(() => {
          // this.$router.push({path: '/Login'})
        });
      }
      file.getDeleted().then((res) => {
        this.FileData = res.data
      })
    },
    methods:{
      pEnter(index) {
        this.index = index
      },
      pLeave(index) {},
      handleCommand(command) {
        if(command == 'recover') {this.recoverFile(this.FileData[this.index].fileId)}
        else if(command == 'delete') {this.deleteFile(this.FileData[this.index].fileId)}
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
      getFileData() {
        file.getRecycle().then((res) => {
          this.FileData = [];
          for (var i = 0; i < res.data.length; i++) {
            if (this.keyword == res.data[i].fileName || this.keyword == res.data[i].modifyTime)
              this.FileData.push(res.data[i]);
            if (this.keyword == '')
              this.FileData = res.data;
          }
        })
      },
      dateFormat(time) {
        return date.timeago(time)
      },
      layoutTile(){
        console.log('切换为平铺布局')
        this.$store.dispatch('setLayoutStatus',1)
      },
      layoutList(){
        console.log('切换为列表布局')
        this.$store.dispatch('setLayoutStatus',0)
      },
    }
  }
</script>

<style scoped>
@import url(../assets/tile);
body {
  margin: 0;
  padding: 0;
  background-color: #fff;
}
</style>
