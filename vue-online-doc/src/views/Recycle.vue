<template>
  <div class="Recycle">
    <div style="flex:1">
      <h2 style="text-indent:1.5em;user-select:none;">回收站</h2>
      <el-divider></el-divider>
      <file-list :FileData="FileData" type="recycle" />
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
import FileList from '../components/FileList'
  import file from '@/api/file'
  export default {
    name: "Recycle",
    components: {FileList},
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
      foreverDeleteFile(id) {
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
