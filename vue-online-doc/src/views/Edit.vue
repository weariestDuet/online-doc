<template>
  <el-container>
      <el-header v-if="editBody==null">
        <div class="input-group">
          <label>
            <input v-model="title" placeholder="请输入标题" style="width:60%"></input>&nbsp;
          </label>
        </div>
      </el-header>

      <el-main style="width: 80%">
        <quill-editor
        v-model="content" style="height: 60vh"
        ref="myQuillEditor"
        :options="editorOption"
      ></quill-editor>
      </el-main>
      <el-footer>
        <el-button @click="saveFile">保存</el-button>
      </el-footer>
  </el-container>
</template>

<script>
  // 引入样式
  import 'quill/dist/quill.core.css'
  import 'quill/dist/quill.snow.css'
  import 'quill/dist/quill.bubble.css'

  import user from '@/api/user'
  import template from '@/api/template'

  import { quillEditor } from 'vue-quill-editor'
  import { addQuillTitle } from '../quill-title.js'
  import Quill from 'quill' //引入编辑器
  import { ImageDrop } from 'quill-image-drop-module'
  Quill.register('modules/imageDrop', ImageDrop);
  var fonts = ['Microsoft-YaHei','SimSun', 'SimHei','KaiTi','Arial','Times-New-Roman'];
  var Font = Quill.import('formats/font');
  Font.whitelist = fonts; //将字体加入到白名单
  import file from '@/api/file'
  export default {
    name: "Edit",
    components:{ quillEditor },
    props: {editBody : String},
    data() {
      return {
        title: '',
        content:null,
        editorOption:{
            theme:'snow',
            modules:{
              imageDrop:true,
              toolbar:[
                [{ 'size': ['12px', false , '20px' ,'24px', '32px','48px']}],
                ['bold', 'italic', 'underline', 'strike'],    //加粗，斜体，下划线，删除线
                ['blockquote', 'code-block'],     //引用，代码块
                [{ 'header': 1 }, { 'header': 2 }],        // 标题，键值对的形式；1、2表示字体大小
                [{ 'list': 'ordered'}, { 'list': 'bullet' }],     //列表
                [{ 'script': 'sub'}, { 'script': 'super' }],   // 上下标
                [{ 'indent': '-1'}, { 'indent': '+1' }],     // 缩进
                [{ 'direction': 'rtl' }],             // 文本方向
                [{ 'font': fonts }],
                //[{ 'size': ['small', false, 'large', 'huge'] }], // 字体大小
                [{ 'header': [1, 2, 3, 4, 5, 6, false] }],     //几级标题
                [{ 'color': [] }, { 'background': [] }],     // 字体颜色，字体背景颜色
                //[{ 'font': [] }],     //字体
                [{ 'align': [] }],    //对齐方式
                ['clean'],    //清除字体样式
                ['image','video']    //上传图片、上传视频
              ]
            }
          },
      }
    },
     mounted() {
      addQuillTitle();
      if(this.editBody!=null) this.content = this.editBody
      if(this.$route.params.type.length<4){
        this.loadTemplate()
      }
    },
    methods:{
      loadTemplate(){
        template.getTemplate(this.$route.params.type).then(res=>{
          this.title = res.data.title
          this.content = res.data.body
        })
      },
      getUserInfo() {
        if (this.$store.state.token) {
          user.getUserInfo().then((res) => {
            this.$store.dispatch('setUserInfo',res.data)
          });
        }
      },
      saveFile(){
        console.info(this.title)
        if(this.title=='') this.$notify({title: '提示',type: 'warning',message: '请填写标题',duration: 2500 })
        else if(this.content==null || this.content=='') 
        this.$notify({title: '提示',type: 'warning',message: '请输入文档内容',duration: 2500 })
        else{
          if(this.$route.params.type == 'User'){
            file.saveUserDoc(this.title,this.content)
            .then(res=>{
              this.getUserInfo()
              this.$notify({title: '提示',type: 'success',message: res.message,duration: 1000 });
              this.$router.push({path:'/'})
            })
          }else{
            file.saveTeamDoc(this.title,this.content,this.$store.state.groupName)
            .then(res=>{
              this.$notify({title: '提示',type: 'success',message: res.message,duration: 1500 });
              this.$router.push({path:'/TeamSpace'})
            })
          }
        }
      }
    }
  }
</script>

<style scoped>
  .hd{
    text-align: left;
    font-size: 30px;
    font-weight: bold;
    line-height: 50px;
    width:75%
  }
  .bd{
    width: 75%;
    margin-left: 50px;
    margin-top: 30px;
  }
  .el-header{
    width: 100%;
    /* box-shadow:  0 2px 6px 0 rgba(0,0,0,.05); */
  }
  .el-container{
    height: 100%;
  }
  .el-aside{
    background-color:  #f7f7f7;
    max-width: 194px;
  }
  .el-main{
    height: calc(100% - 62px);
    max-width: calc(100% - 194px);
  }
  .input-group {display: flex;align-items: center;justify-content: flex-start;}
  .input-group label {margin: 0;flex: 1;}

  label {display: block;margin-bottom: 24px;width: 100%;}

  input {border: 0;outline: 0;font-size: 16px;border-radius: 320px;padding: 1rem;background-color: #EBECF0;text-shadow: 1px 1px 0 #FFF;}
  input {margin-right: 8px;box-shadow: inset 2px 2px 5px #BABECC, inset -5px -5px 10px #FFF;width: 100%;box-sizing: border-box;transition: all 0.2s ease-in-out;appearance: none;-webkit-appearance: none;}
  input:focus {box-shadow: inset 1px 1px 2px #BABECC, inset -1px -1px 2px #FFF;}

  button{border: 0;outline: 0;font-size: 16px;border-radius: 320px;padding: 1rem;background-color: #F7F7F7;}
  /*#F7F7F7 #EBECF0*/
  button{color: #61677C;box-shadow: -5px -5px 20px #FFF, 5px 5px 20px #BABECC;transition: all 0.2s ease-in-out;cursor: pointer;font-weight: 600;}
  button:hover {box-shadow: -2px -2px 5px #FFF, 2px 2px 5px #BABECC;}
  button:hover{
    transform: scale(0.95);
  }
  button:active {box-shadow: inset 1px 1px 2px #BABECC, inset -1px -1px 2px #FFF;}

</style>
