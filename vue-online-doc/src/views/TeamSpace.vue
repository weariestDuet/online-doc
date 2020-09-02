<template>
  <div class="workStation">
    <!-- 左侧文档视图 -->
    <div style="flex:1">   
      <el-tabs v-model="activeTeam" style="height:100%"  @tab-click="handleClick" type="card">
        <el-tab-pane :label="o.name" :name="o.name" v-for="(o,id) in groups" :key="id">
          <h2 style="text-indent:1.5em;user-select:none;">团队文件</h2>
          <el-divider></el-divider>
          <file-list type="team" :FileList="FileData" @refresh="getGroups" />
        </el-tab-pane>
      </el-tabs>
    </div>

    <div style="width:50px">
      <el-tooltip effect="dark" content="平铺" placement="top" v-if="!$store.state.layout">
        <div class="layout_button">
          <i @click="layoutTile()" class="el-icon-menu"></i>
        </div>
      </el-tooltip>
      <el-tooltip effect="dark" content="列表" placement="top" v-else>
        <div class="layout_button">
          <i @click="layoutList()" class="el-icon-s-unfold"></i>
        </div>
      </el-tooltip>
    </div>

    <!-- 加入/创建团队 -->
    <el-dialog title="申请团队" :visible.sync="$store.state.groupAddVisable" width="26%" :before-close="handleClose" :show-close="false">
      <div style="margin: 0px 20px 20px 20px">
        <el-input placeholder="请输入团队名" v-model="group_keyword" suffix="el-icon-search"></el-input>
        <div style="margin-top:50px">
        <span slot="footer"><el-button @click="attendGroup()" class="button" type="primary">申请加入</el-button></span>
        <span slot="footer" ><el-button @click="createGroup()" class="button" type="info">创建团队</el-button></span>
        </div>
      </div>
    </el-dialog>

    <!-- 管理团队 -->
    <el-dialog title="协作空间" :visible.sync="$store.state.groupAdminVisable" top="7vh" width="37%" :before-close="handleClose" :show-close="false">
      <div style="margin: 0px 20px 20px 20px">
        <el-input placeholder="请输入协作者用户名/邮箱" v-model="user_keyword" suffix="el-icon-search"></el-input>
        <div style="margin-top: 30px; margin-bottom:30px">
          <div>
            <span style="font-size:16px">管理者</span>
            <div style="margin-top:-20px"><el-divider></el-divider></div>
            <el-table  :data="group.admin" style="width: 100%" :show-header="false">
              <el-table-column   label="头像" width="70">
                <template slot-scope="scope">
                    <img v-if="scope.row.avatar" class="avatar" :src="scope.row.avatar">
                    <img v-else class="avatar" src="../../static/avatar.png">
                    </template>
              </el-table-column>
              <el-table-column  prop="name" label="用户名" width="120">
              </el-table-column>
              <el-table-column  prop="mail" label="邮箱" width="220">
              </el-table-column>
              <el-table-column label="操作"> <template><el-button v-if="isAdmin" style="width:110px" plain >管理者</el-button>
              </template>
              </el-table-column>
            </el-table>
          </div>
          <div style="margin-top:30px">
            <div style="height:300px">
              <el-scrollbar style="height:100%">
                <span style="font-size:16px">协作者</span>
                <div style="margin-top:-20px"><el-divider></el-divider></div>
                <el-table  :data="group.member" style="width: 100%" :show-header="false">
                  <el-table-column   label="头像" width="70">
                    <template slot-scope="scope">
                    <img v-if="scope.row.avatar" class="avatar" :src="scope.row.avatar">
                    <img v-else class="avatar" src="../../static/avatar.png">
                    </template>
                  </el-table-column>
                  <el-table-column  prop="name" label="用户名" width="120">
                  </el-table-column>
                  <el-table-column  prop="mail" label="邮箱" width="220">
                  </el-table-column>
                  <el-table-column label="操作">
                    <template slot-scope="scope">
                      <el-button v-if="isAdmin" plain @click="removeMem(scope.row.name)">移除协作者</el-button>
                  </template>
                  </el-table-column>
                </el-table>
              </el-scrollbar>
            </div>
          </div>
        </div>

        <div>
        <span v-if="isAdmin" slot="footer" ><el-button  @click="addMem()" class="button" type="info">添加协作者</el-button></span>
        <span v-if="!isAdmin" slot="footer" ><el-button  @click="leaveGroup()" class="button" type="info">退出团队</el-button></span>
        <span v-if="isAdmin" slot="footer" ><el-button @click="deleteGroup()" class="button" >解散团队</el-button></span>
        </div>
      </div>
    </el-dialog>
    
  </div>
</template>



<script>
  import FileList from "../components/FileList"
  import MyTeam from "../components/MyTeam"
  import group from '@/api/group'
  import file from '@/api/file'
  import Edit from '@/views/Edit'
  export default {
    name:"TeamSpace",
    components: { FileList, MyTeam, Edit},
    data() {
      return {
        FileData: [],
        user_keyword: '', //添加协作者用户名
        group_keyword: '', //申请加入/创建团队名
        optionVisible: false,
        attendVisible: false,
        activeTeam: '',
        //当前标签页团队属性
        isAdmin: false,
        groups: [],
        group:{}
      };
    },
    created(){
      this.getGroups()
    },
    methods: {
      gotoTLibrary(){
        this.$router.push({path:'/TemplateLibrary'})
      },
      validAvatar(avatar){
        if(avatar == null || avatar == undefined) return false
        return avatar.length>0?true:false
      },
      changeTeam(){
        for(let i=0; i<this.groups.length;++i){
          if(this.groups[i].name == this.activeTeam){
            var admin = []
            admin.push(this.groups[i].admin)
            var team = {groupId:this.groups[i].groupId, admin :admin, name: this.groups[i].name, member:this.groups[i].member, creation:this.groups[i].creation}
            this.group = team
            this.FileData = this.groups[i].creation
            // console.info(this.group)
            if(admin[0].name==this.$store.state.name) this.isAdmin = true
            else this.isAdmin = false
          }
        }
      },
      getGroups(){
        group.getGroups().then(res=>{
          console.log(res.data)
          this.groups = res.data
          if(this.groups.length>0) {
            this.$store.dispatch('setCurGroupName',this.groups[0].name)
            this.activeTeam = this.groups[0].name
            this.changeTeam()
          }
        })
      },
      attendGroup(){
        if(this.group_keyword!=''){
          group.attendGroup(this.group_keyword).then(res=>{
            console.log(res.message)
            if(res.code == 201)
              this.$notify({title: '提示',type: 'error',message: res.message,duration: 2500 });
            else
              this.$notify({title: '提示',type: 'success',message: res.message,duration: 1500 });
            this.group_keyword=''
            this.attendVisible =false
          })
        }
      },
      createGroup(){
        if(this.group_keyword!=''){
          group.createGroup(this.group_keyword).then(res=>{
            if(res.code == 201)
              this.$notify({title: '提示',type: 'error',message: res.message,duration: 2500 });
            else
              this.$notify({title: '提示',type: 'success',message: res.message,duration: 1500 });
            this.group_keyword=''
            this.attendVisible = false
          })
          this.$router.go(0)
          this.getGroups()
        }
      },
      addMem(){
        if(this.user_keyword!=''){
          group.addMem(this.user_keyword,this.$store.state.groupName).then(res=>{
            if(res.code == 201)
              this.$notify({title: '提示',type: 'error',message: res.message,duration: 2500 });
            else
              this.$notify({title: '提示',type: 'success',message: res.message,duration: 1500 });
            this.user_keyword=''
            this.optionVisible =false
          })
        }
      },
      removeMem(name){
        console.log('移除成员：'+name)
        group.removeMem(this.$store.state.groupName,name).then(res=>{
          this.$notify({title: '提示',type: 'success',message: res.message,duration: 1500 });
          this.getGroups()
          this.optionVisible = false
        })
      },
      leaveGroup(){
        group.removeMem(this.$store.state.groupName,this.$store.state.name).then(res=>{
          this.$notify({title: '提示',type: 'success',message: res.message,duration: 1500 });
          this.$router.go(0)
        })
      },
      deleteGroup(){
        group.deleteGroup(this.$store.state.groupName).then(res=>{
          this.$notify({title: '提示',type: 'success',message: res.message,duration: 1500 });
          this.$router.go(0)
        })
      },
      teamEdit(){
        this.$router.push({path: 'EditTeam'})
      },
      layoutTile(){
        this.$store.dispatch('setLayoutStatus',1)
      },
      layoutList(){
        this.$store.dispatch('setLayoutStatus',0)
      },
      handleClick(tab, event) {
        this.changeTeam()
      },
      handleClose(done) {
        done();
      }
    }
  };
</script>
<style scoped>
@import url(../assets/tile);
  body{
    margin: 0;
    padding: 0;
  }
.layout_button{
  width: 50px;
  height: 35px;
}
.layout_button:hover {
  background-color: rgba(70, 70, 70, 0.1);
}
.layout_button  i{
  line-height: 35px;
  margin-left:7px;
  font-size: 30px;
  color: grey;
}







  .fontStyle{
    font-size: 17px;
    color:rgb(90, 90, 90)
  }

  .left {
    display: inline-block;
    margin-top: -40px;
    width: 22%;
    height: 100%;
  }
  .left img {
    margin: 0 auto;
    width: 50px;
    border-radius: 50%;
  }
  .right {
    display: inline-block;
    width: 70%;
  }
  .rightTop {
    text-align: left;
    margin-left: 5px;
    margin-top: -10px;
    height: 22px;
  }
  .rightTop .userName {
    text-align: left;
    color: #303133;
    margin-left: 10px;
    font-size: 16px;
    font-weight: bold;
  }
  .rightCenter {
    text-align: left;
    margin-left: 17px;
    height: 20px;
    margin-top: 15px;
  }
  .avatar{
    width: 50px;
    height: 50px;
    border-radius:50%
  }
  .card{
    margin-top:25px;
    border-radius: 5%
  }
  .el-scrollbar__wrap {
    overflow-x: hidden;
  }
  /*.button{
    margin-left:20px;
    width:170px
  }*/
  button{border: 0;outline: 0;font-size: 16px;border-radius: 320px;padding: 1rem;background-color: #F7F7F7;}
  /*#F7F7F7 #EBECF0*/
  button{color: #61677C;box-shadow: -5px -5px 20px #FFF, 5px 5px 20px #BABECC;transition: all 0.2s ease-in-out;cursor: pointer;font-weight: 600;}
  button:hover {box-shadow: -2px -2px 5px #FFF, 2px 2px 5px #BABECC;}
  button:hover{
    transform: scale(0.95);
  }
  button:active {box-shadow: inset 1px 1px 2px #BABECC, inset -1px -1px 2px #FFF;}
</style>
