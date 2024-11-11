<template>
  <div>
    <!--头部-->
    <top />
    <!--导航-->
    <navs />

    <!--核心内容start-->

    <section id="meetings" class="meetings-page">
      <div class="container">
        <div v-if="acitity!=null" class="row">
          <div class="col-lg-12">
            <div class="row">
              <div class="col-lg-12">
                <div class="meeting-single-item">
                  <div class="thumb">
                    <div class="date">
                      <h6>MAY <span>13</span></h6>
                    </div>

                  </div>
                  <div class="down-content">
                    <a href="meeting-details.html"><h4>活动主题</h4></a>
                    <p>{{ acitity.name }}</p>
                    <p class="description">
                      {{ acitity.ctInfo }} </p>
                    <div class="row">
                      <div class="col-lg-4">
                        <div class="hours">
                          <h5>时间</h5>
                          <p>{{ acitity.acTime }}</p>

                        </div>
                      </div>
                      <div class="col-lg-4">
                        <div class="location">
                          <h5>地点</h5>
                          <p>{{ acitity.ctAddress }}</p>
                        </div>
                      </div>
                      <div class="col-lg-4">
                        <div class="book now">
                          <h5>主持人</h5>
                          <p>{{ acitity.host }}</p>
                        </div>
                      </div>

                    </div>
                  </div>
                </div>
              </div>

            </div>
          </div>
        </div>

      </div>
    </section>
    <section class="contact-us" >

      <div class="container" >
        <div class="row">
          <div class="col-lg-12 align-self-center">
            <div class="row">
              <div class="col-lg-12">

                <el-form id="contact" ref="dataForm" :rules="rules" :model="temp">
                  <div class="row">
                    <div class="col-lg-12">
                      <h2>加入社团</h2>
                    </div>
                    <div class="col-lg-4">
                      <fieldset>
                        <el-form-item prop="realname">
                          <input v-model="temp.realname" type="text" placeholder="姓名..." required="">
                        </el-form-item>
                      </fieldset>
                    </div>
                    <div class="col-lg-4">
                      <fieldset>
                        <input v-model="temp.sex" type="text" placeholder="性别" required="">
                      </fieldset>
                    </div>
                    <div class="col-lg-4">
                      <fieldset>
                        <el-form-item prop="tel">
                          <input v-model="temp.tel" type="text" placeholder="电话" required="">
                        </el-form-item>
                      </fieldset>
                    </div>

                    <div class="col-lg-4">
                      <fieldset>
                        <el-form-item prop="college">
                          <input v-model="temp.college" type="text" placeholder="学院" required="">
                        </el-form-item>
                      </fieldset>
                    </div>
                    <div class="col-lg-4">
                      <fieldset>
                        <el-form-item prop="grade">
                          <input v-model="temp.grade" type="text" placeholder="年级" required="">
                        </el-form-item>
                      </fieldset>
                    </div>

                    <div class="col-lg-4">
                      <fieldset>
                        <el-form-item prop="email">
                          <input v-model="temp.email" type="text" pattern="[^ @]*@[^ @]*" placeholder="邮箱" required="">
                        </el-form-item>
                      </fieldset>
                    </div>
                    <div class="col-lg-4">
                      <fieldset>
                        <input v-model="temp.address" type="text" placeholder="地址" required="">
                      </fieldset>
                    </div>
                    <div class="col-lg-4">
                      <fieldset>
                        <input v-model="temp.weixin" type="text" placeholder="微信" required="">
                      </fieldset>
                    </div>

                    <div class="col-lg-12">
                      <fieldset>
                        <textarea id="message" v-model="temp.content" type="text" class="form-control" placeholder="申请内容" required="" />
                      </fieldset>
                    </div>

                    <div class="col-lg-12">
                      <fieldset>
                        <button id="form-submit" type="submit" class="button" @click="addSave()">提交</button>
                      </fieldset>
                    </div>

                  </div>
                </el-form>

                <el-form v-if="isShow" id="con2">
                  <div class="row">
                    <div class="col-lg-12" style="margin-bottom: 30px">
                      <h2>{{title}}
                        <el-button class="filter-item" style="float: right" type="success" icon="el-icon-check" @click="getOpen()">
                          缴费
                        </el-button>
                      </h2>
                    </div>
                    <div class="col-lg-4" >
                      <fieldset>
                        <el-form-item label="社团名称">
                          <input v-model="team.name" readonly="readonly">
                        </el-form-item>
                      </fieldset>
                    </div>
                    <div class="col-lg-4">
                      <fieldset>
                        <el-form-item label="社团类型">
                        <input v-model="team.typeName" readonly>
                        </el-form-item>
                      </fieldset>
                    </div>
                    <div class="col-lg-4">
                      <fieldset>
                        <el-form-item label="社长">
                          <input v-model="team.userInfo.realname" >
                        </el-form-item>
                      </fieldset>
                    </div>

                    <div class="col-lg-4">
                      <fieldset>
                        <el-form-item label="创建时间">
                          <input v-model="team.createTime" readonly>
                        </el-form-item>
                      </fieldset>
                    </div>
                    <div class="col-lg-4">
                      <fieldset>
                        <el-form-item label="社团容纳人数">
                          <input v-model="team.astrict" readonly>
                        </el-form-item>
                      </fieldset>
                    </div>


                  </div>
                </el-form>

              </div>
            </div>
          </div>


        </div>
      </div>

    </section>

    <foot />

    <!--核心内容end-->

<!--    社团缴费-->
    <el-dialog title="社团缴费" :visible.sync="dialogFormVisible">
      <el-form ref="dataForm2"  label-position="left" label-width="120px" style="width: 400px; margin-left:50px;">
        <el-form-item label="需缴纳金额(元)">
          <el-input v-model="money" readonly />
        </el-form-item>
        <el-form-item label="社团支付宝账号">
          <el-input v-model="zfb" readonly />
        </el-form-item>
        <el-form-item label="社团微信账号">
          <el-input v-model="wx" readonly />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">
          未支付
        </el-button>
        <el-button type="primary" @click="submitPay()">
          已支付
        </el-button>
      </div>
    </el-dialog>

  </div>

</template>

<style>
section.contact-us #con2 {
  background-color: #fff;
  border-radius: 20px;
  padding: 40px;
}
</style>

<script>
import top from '../../qdutils/top/top'
import navs from '../../qdutils/navs/index'
import foot from '../../qdutils/footer/index'
import request from '@/utils/request'
import Cookies from "js-cookie";
import {Message} from "element-ui";
export default {
  name: 'Index',
  components: { top, navs, foot },

  data() {
    return {
      acitity: {},
      team: {},
      temp: {
        realname: '',
        tel: '',
        email: '',
        address: '',
        qq: '',
        weixin: '',
        content: '',
        college: '',
        grade: ''
      },
      teamId: '',

      rules: {
        realname: [{ required: true, message: '请填写正确的姓名格式(3个长度以上)', trigger: 'blur' }],
        tel: [{ required: true, message: '请填写电话', trigger: 'blur' }, {
          pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/,
          message: '请输入正确的手机号码',
          trigger: 'blur'
        }],
        email: [{ required: true, message: '请填写邮箱', trigger: 'blur' }, {
          pattern: /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((.[a-zA-Z0-9_-]{2,3}){1,2})$/,
          message: '请输入正确的邮箱',
          trigger: 'blur'
        }]
      },
      title:'',
      isShow:'',
      dialogFormVisible:false,
      zfb:"13966668888",
      wx:"qwer88888888",
      money:50

    }
  },
  created() {
    const teamId = this.$route.query.teamId// 社团的id
    this.teamId = teamId
    if (Cookies.get('Admin-Token') != undefined){
      this.title = "社团信息"
      this.isShow = true
    }else {
      this.title = "加入社团"
      this.isShow = false
    }
    this.getAcitityInfoByTeamId(teamId)
    this.getTeamById(teamId)
  },
  methods: {
    submitPay(){
      request({
        url: 'teamuser/submitPay',
        method: 'get'
      }).then(res => {
          if (res.code == 20000) {
            this.$message({
              message: '缴费成功！',
              type: 'success',
              duration:3000
            })
            this.dialogFormVisible = false
          }else {
            this.$message({
              message: res.data,
              type: 'error',
              duration:3000
            })
            this.dialogFormVisible = true
          }
      })
    },
    getOpen(){
      if (this.team.pay=="已关闭"){
        this.$message({
          message: '无需缴费',
          type: 'warning',
          duration:3000
        })
      }else {
        this.dialogFormVisible = true
      }
    },
    getAcitityInfoByTeamId(teamId) {
      request('activity/findByTeamId?id=' + teamId).then(res => {
        this.acitity = res.data
      })
    },
    getTeamById(teamId) {
      request('team/findTeamById?id=' + teamId).then(res => {
        this.team = res.data
      })
    },
    // 添加
    addSave(temp) {
      this.temp.teamId = this.teamId
      // 加验证规则
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          // 向后端发送请求，实现添加功能
          request.post('applyInfo/addInfo', this.temp).then(res => {
            if (res.code == 20000) {
              // 关闭弹出框
              this.$alert(res.msg)
            } else {
              this.$alert('申请失败...')
            }
          })
        }
      })
    }

  }
}
</script>

<style scoped>
@import "../../../vendor/bootstrap/css/bootstrap.min.css";
@import "../../../assets/css/templatemo-edu-meeting.css";
</style>
