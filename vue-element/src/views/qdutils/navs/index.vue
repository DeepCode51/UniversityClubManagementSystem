<template>
  <div>
    <header class="header-area header-sticky">
      <div class="container">
        <div class="row">
          <div class="col-12">
            <nav class="main-nav">
              <!-- ***** Logo Start ***** -->
              <a href="index.html" class="logo">
                大学社团
              </a>
              <!-- ***** Logo End ***** -->
              <!-- ***** Menu Start ***** -->
              <ul class="nav">
                <li v-if="isShow" class="scroll-to-section">
                  <a  @click="handleUpdate">
                    个人信息
                  </a>
                </li>
                <li class="scroll-to-section"><a href="#/indexs">主页</a></li>
                <li><a href="#/teamlist">社团列表</a></li>
                <li class="scroll-to-section"><a href="#/myteam">我的社团</a></li>
                <li><a href="#/leave">在线留言</a></li>
                <li v-if="type!=2" class="scroll-to-section"><a href="#/">后台管理</a></li>
              </ul>
              <a class="menu-trigger">
                <span>Menu</span>
              </a>
              <!-- ***** Menu End ***** -->
            </nav>
          </div>
        </div>
      </div>
    </header>

    <el-dialog title="个人信息" :visible.sync="dialogFormVisible">
      <el-form ref="dataForm" :model="temp" label-position="left" label-width="70px" style="width: 400px; margin-left:50px;">
        <el-form-item label="用户姓名">
          <el-input v-model="temp.username" />
        </el-form-item>
        <el-form-item label="真实姓名">
          <el-input v-model="temp.realname" />
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="temp.password" />
        </el-form-item>

        <el-form-item label="用户性别">
          <el-radio v-model="temp.sex" label="男">男</el-radio>
          <el-radio v-model="temp.sex" label="女">女</el-radio>
        </el-form-item>

        <el-form-item label="用户类型">
          <el-select v-model="temp.type" placeholder="请选择" disabled>
            <el-option label="超级管理员" :value="1" />
            <el-option label="社团管理员" :value="0" />
            <el-option label="社团成员" :value="2" />
          </el-select>

        </el-form-item>

        <el-form-item label="电话">
          <el-input v-model="temp.tel" />
        </el-form-item>

        <el-form-item label="邮箱">
          <el-input v-model="temp.email" />
        </el-form-item>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">
          取消
        </el-button>
        <el-button type="primary" @click="updateData()">
          提交
        </el-button>
      </div>
    </el-dialog>
  </div>


</template>

<script>
import request from "@/utils/request";
import Cookies from 'js-cookie'

export default {
  name: 'index',
  data() {
    return {
      type: localStorage.getItem('type'),
      temp: {
        id: undefined,
        username: undefined,
        realname: undefined,
        password: undefined,
        tel: undefined,
        email: undefined,
        type: undefined,
        sex: undefined,
      },
      role: localStorage.getItem('roles'),
      dialogFormVisible: false,
      isShow:false
    }
  },
  created() {
    this.isShow = Cookies.get('Admin-Token') == undefined?false:true;
    //alert(Cookies.get('Admin-Token'))
    // alert(this.isShow)
  },
  methods:{
    handleUpdate() {
      if (this.isShow){
        //this.temp =
        this.dialogFormVisible = true
        this.$nextTick(() => {
          this.$refs['dataForm'].clearValidate()
          request({
            url: 'userinfo/queryUserInfo',
            method: 'get'
          }).then(res => {
            this.temp = res.data// 数据信息
          })
        })
      }

    },
    updateData() {
      /**
       * 1、获取信息  注意id
       * 2、把获取到的内容通过修改接口传入后端服务器
       * 3、根据后端返回结果集 进行判断提示
       * 4、如果成功 重新加载table 关闭form
       */
      console.log(this.temp)
      debugger
      request.post('userinfo/updateInfo', this.temp).then(res => {
        if (res.code == 20000) {
          // 关闭弹出框
          this.dialogFormVisible = false
          this.$notify({
            title: 'Success',
            message: '修改成功',
            type: 'success',
            duration: 2000
          })
          this.getList()
        } else {
          this.$notify({
            title: '失败',
            message: '修改失败',
            type: 'fail',
            duration: 2000
          })
        }
      })
    }
  }
}
</script>

<style scoped>

</style>
