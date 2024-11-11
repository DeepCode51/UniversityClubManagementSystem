<template>
  <div>
    <!--头部-->
    <top />
    <!--导航-->
    <navs />

    <!--核心内容start-->
    <section id="top" class="heading-page header-text">
      <div class="container">
        <div class="row">
          <div class="col-lg-12">
            <h2>已加入社团</h2>
          </div>
        </div>
      </div>
    </section>

    <!--核心内容start-->
    <section id="meetings" class="meetings-page">
      <div class="container">
        <div class="row">

          <div class="col-lg-12">
            <div class="row">
              <div class="col-lg-12">
                <div class="row grid">

                  <div v-for="team in teamList" class="col-lg-4 templatemo-item-col all soon">
                    <div class="meeting-item">
                      <div class="thumb">
                        <!-- <a href="#/teaminfo"><img :src="team.images" width="200px" height="200px" alt=""></a>-->
                        <router-link :to="{path:'/teaminfo',query:{teamId:team.id}}"><img :src="team.images" width="200px" height="200px" alt=""></router-link>
                      </div>
                      <div class="down-content">
                        <div class="date">
                          <h6>MAY </h6>
                        </div>
                        <h4>{{ team.name }}</h4>
                        <p>社长：{{ team.userInfo.username }}</p>
                      </div>
                    </div>
                  </div>

                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <!--底部-->
      <foot />
    </section>

    <!--核心内容end-->

  </div>
</template>

<script>
import top from '../../qdutils/top/top'
import navs from '../../qdutils/navs/index'
import foot from '../../qdutils/footer/index'
import request from '@/utils/request'
export default {
  name: 'Index',
  components: { top, navs, foot },
  // 初始数据
  data() {
    return {
      teamList: []
    }
  },

  // 获取数据信息
  created() {
    this.getList()
  },

  // 方法列表
  methods: {
    // 获取方法
    getList() {
      request({
        url: 'team/queryMyTeamList',
        method: 'get',
        params: ''
      }).then(res => {
        this.teamList = res.data
      })
    }
  }

}
</script>

<style scoped>

  @import "../../../vendor/bootstrap/css/bootstrap.min.css";
  @import "../../../assets/css/templatemo-edu-meeting.css";
</style>
