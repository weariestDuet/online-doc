<template>
  <div class="library">
    <!-- 平铺视图 -->
      <div class="item" v-for="(o, index) in templateData" :key="index">
        <div class="core" @mouseenter="pEnter(index)" @mouseleave="pLeave(index)">
          <img :src="o.cover" @click="gotoTemplete(o.templateId)">
          <div style="margin-top: 14px; text-align: center">
            <a>{{o.title}}</a>
          </div>
        </div>
    </div>
  </div>
</template>

<script>
  import template from '@/api/template'
  export default {
    name: "TempleteLibrary",
    data(){
      return{
        templateData:[],
        index: 0, 
      }
    },
    created() {
      this.getTemplateList()
    },
    methods:{
      pEnter(index) {
        this.index = index
      },
      pLeave(index) {},
      getTemplateList(){
        template.getTemplateList().then((res)=>{
          // console.log(res.data)
          this.templateData=res.data
        })
      },
      gotoTemplete(id){
        this.$router.push({path: '/Edit/'+id})
      }
    }
  }
</script>

<style scoped>
.library {
  display: flex;
  flex-flow: row wrap;
  justify-content: left;
  align-items: center;
}
.item {
  display: flex;
  flex-flow: column nowrap;
  align-items: center;
  width: 300px;
  height: 350px;
}
.core {
  width: 230px;
}
.core img {
  margin: 10px auto;
  width: 200px;
  cursor:pointer;
  display: block;
}
.core:hover{
  background: #E9E9E9;
  border: 1px solid rgba(70,70,70,.1);
  border-radius: 3%;
}
.core:hover img{
  transform: scale(0.95);
}

</style>
