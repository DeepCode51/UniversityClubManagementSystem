<template>
  <div>
    <!--头部-->
    <top />
    <!--导航-->
    <navs />

    <!--核心内容start-->
    <section class="meetings-page" id="meetings">
      <div class="container">
        <div class="filter-container">
          <el-input v-model="listQuery.name" placeholder="社团名称" style="width: 200px;" class="filter-item" @keyup.enter.native="handleFilter" />
          <el-select v-model="listQuery.type" placeholder="选择类型" clearable class="filter-item" style="width: 130px">
            <el-option v-for="item in typeList" :key="item.id" :label="item.name" :value="item.id" />
          </el-select>

          <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">
            查询
          </el-button>
        </div>
        <div class="row">
          <div class="col-lg-12">
            <div class="row">
              <div class="col-lg-12">
                <div class="row grid">
                  <div v-for="team in teamList" class="col-lg-4 templatemo-item-col all soon">
                    <div class="meeting-item">
                      <div class="thumb">
                        <router-link :to="{path:'/teaminfo',query:{teamId:team.id}}">
                          <img :src="team.images" width="200px" height="200px" alt="">
                        </router-link>
                      </div>
                      <div class="down-content">
                        <div class="date">
                          <h6>MAY </h6>
                        </div>
                        <h4>{{team.name}}</h4>
                        <p>社长：{{ team.userInfo.username }} <span style="float: right">人数：{{team.member}}/{{team.astrict}}</span> </p>
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
      <foot></foot>
    </section>
    <!--核心内容end-->

  </div>
</template>

<script>
import top from '../../qdutils/top/top'
import navs from '../../qdutils/navs/index'
import foot from '../../qdutils/footer/index'
import request from '@/utils/request'
import waves from '@/directive/waves' // waves directive
export default {
  name: 'Index',
  components: { top, navs, foot },
  directives: { waves },
  // 初始数据
  data() {
    return {
      teamList: [],
          list: null,
          total: 0,
          listLoading: true,
          listQuery: {
            page: 1,
            limit: 20,
            importance: undefined,
            name: undefined,
            type: undefined
          },
          typeList: []

    }
  },

  // 获取数据信息
  created() {
    this.getList()
    this.getTypeList()
  },

  // 方法列表
  methods: {
    // 获取方法
    getList() {
      request({
        url: 'team/queryTeamAll',
        method: 'get',
        params: ''
      }).then(res => {
        this.teamList = res.data
      })
    },
    handleFilter() {
      this.listQuery.page = 1
      this.getList2()
    },
    //社团列表查询
    getList2() {
      this.listLoading = true// 没有信息之前显示加载内容
      request(
        {
          url: 'http://localhost:9999/team/queryTeamList',
          method: 'get',
          params: this.listQuery
        }).then(res => {
        if (res.code == 20000) {
          this.teamList = res.data
          this.total = res.total
          this.listLoading = false
        }
      })
    },
    //社团类型
    getTypeList() {
      request(
        {
          url: 'http://localhost:9999/typeInfo/queryTypeList',
          method: 'get'
        }).then(res => {
        if (res.code == 20000) {
          this.typeList = res.data
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
