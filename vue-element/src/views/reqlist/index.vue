<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input v-model="listQuery.tel" placeholder="姓名" style="width: 200px;" class="filter-item" @keyup.enter.native="handleFilter" />
      <el-select v-model="listQuery.teamId" placeholder="选择社团" clearable class="filter-item" style="width: 130px">
        <el-option v-for="item in teamList" :key="item.id" :label="item.name" :value="item.id" />
      </el-select>
      <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">
        高级查询
      </el-button>

    </div>

    <el-table
      :key="tableKey"
      v-loading="listLoading"
      :data="list"
      border
      fit
      highlight-current-row
      style="width: 100%;"
      @sort-change="sortChange"
    >
      <el-table-column label="系统编号" prop="id" sortable="custom" align="center" width="80px" :class-name="getSortClass('id')">
        <template slot-scope="{row}">
          <span>{{ row.id }}</span>
        </template>
      </el-table-column>
      <el-table-column label="真实名称" width="100px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.realname }}</span>
        </template>
      </el-table-column>
      <el-table-column label="性别" width="100px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.sex }}</span>
        </template>
      </el-table-column>

      <el-table-column label="学院" width="150px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.college }}</span>
        </template>
      </el-table-column>
      <el-table-column label="年级" width="100px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.grade }}</span>
        </template>
      </el-table-column>

      <el-table-column label="申请社团" width="150px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.team.name }}</span>
        </template>
      </el-table-column>

      <el-table-column label="电话" width="150px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.tel }}</span>
        </template>
      </el-table-column>

      <el-table-column label="邮箱" width="150px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.email }}</span>
        </template>
      </el-table-column>

      <el-table-column label="申请时间" width="180px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.createTime | parseTime('{y}-{m}-{d} {h}:{i}') }}</span>
        </template>
      </el-table-column>

      <el-table-column label="状态" class-name="status-col" width="120">
        <template slot-scope="{row}">
          <el-tag :type="row.status | statusFilter">
            <span v-if="row.status==1" style="color: green">审核通过</span>
            <span v-if="row.status==2" style="color: red">拒绝</span>
            <span v-if="row.status==0" style="color: blue">未审核</span>
          </el-tag>
        </template>
      </el-table-column>

      <el-table-column label="操作" align="center" width="250" class-name="small-padding fixed-width">
        <template slot-scope="{row,$index}">
          <el-button type="primary" size="mini" @click="handleQuery(row)">
            详细
          </el-button>
          <el-button
            v-if="row.status==0 && role!='Admin'"
            size="mini"
            type="success"
            @click="handleUpdate(row)">
            审核
          </el-button>

          <el-button v-if="row.status!='deleted'" size="mini" type="danger" @click="handleDelete(row,$index)">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getList" />

    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form ref="dataForm" :rules="rules" :model="temp" label-position="left" label-width="70px" style="width: 400px; margin-left:50px;">

        <el-form-item label="申请人">
          <el-input v-model="temp.realname" readonly />
        </el-form-item>

        <el-form-item label="性别">
          <el-input v-model="temp.sex" readonly />
        </el-form-item>

        <el-form-item label="学院">
          <el-input v-model="temp.college" readonly />
        </el-form-item>

        <el-form-item label="年级">
          <el-input v-model="temp.grade" readonly />
        </el-form-item>

        <el-form-item label="电话">
          <el-input v-model="temp.tel" readonly />
        </el-form-item>

        <el-form-item label="邮箱">
          <el-input v-model="temp.email" readonly />
        </el-form-item>

        <el-form-item label="地址">
          <el-input v-model="temp.address" readonly />
        </el-form-item>

        <el-form-item label="申请内容">
          <el-input
            v-model="temp.content"
            readonly
            :autosize="{ minRows: 2, maxRows: 4}"
            type="textarea"
            placeholder="Please input"
          />
        </el-form-item>

        <el-divider />

        <el-form-item label="审核内容">
          <el-input
            v-model="temp2.content"
            :autosize="{ minRows: 2, maxRows: 4}"
            type="textarea"
            placeholder="请输入"
          />
        </el-form-item>

        <el-form-item label="审核状态">
          <el-select v-model="temp2.status" class="filter-item" placeholder="Please select">
            <el-option v-for="item in statusList" :key="item" :label="item.value" :value="item.id" />
          </el-select>
        </el-form-item>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">
          取消
        </el-button>
        <el-button type="primary" @click="dialogStatus==='create'?createData():updateData()">
          提交
        </el-button>
      </div>
    </el-dialog>

    <!---详细页面的开始-->

    <el-dialog title="申请详细" :visible.sync="dialogFormVisible2">

      <el-form :inline="true" :model="temp" class="demo-form-inline">

        <el-form-item label="姓名">
          <el-input v-model="temp.realname" readonly />
        </el-form-item>
        <el-form-item label="性别">
          <el-input v-model="temp.sex" readonly />
        </el-form-item>

        <el-form-item label="学院">
          <el-input v-model="temp.college" readonly />
        </el-form-item>

        <el-form-item label="年级">
          <el-input v-model="temp.grade" readonly />
        </el-form-item>

        <el-form-item label="社团">
          <el-input v-model="temp.team.name" readonly />
        </el-form-item>

        <el-form-item label="电话">
          <el-input v-model="temp.tel" readonly />
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="temp.email" readonly />
        </el-form-item>

        <el-form-item label="时间">
          <el-input v-model="temp.createTime" readonly />
        </el-form-item>

        <el-form-item label="家庭地址">
          <el-input v-model="temp.address" readonly />
        </el-form-item>
        <br>
        <el-form-item label="申请内容">
          {{ temp.content }}
        </el-form-item>

      </el-form>

      <span v-if="activities.length>0" style="color: red">审核记录</span>
      <el-divider />
      <div class="block">
        <el-timeline :reverse="reverse">
          <el-timeline-item
            v-for="(activity, index) in activities"
            :key="index"
            :timestamp="activity.appTime"
          >
            {{ activity.content }}
          </el-timeline-item>
        </el-timeline>
      </div>

      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible2 = false">
          关闭
        </el-button>

      </div>
    </el-dialog>

    <!---详细页面设计结束-->

    <el-dialog :visible.sync="dialogPvVisible" title="Reading statistics">
      <el-table :data="pvData" border fit highlight-current-row style="width: 100%">
        <el-table-column prop="key" label="Channel" />
        <el-table-column prop="pv" label="Pv" />
      </el-table>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="dialogPvVisible = false">Confirm</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { fetchList, fetchPv, createArticle, updateArticle } from '@/api/article'
import waves from '@/directive/waves' // waves directive
import { parseTime } from '@/utils'
import Pagination from '@/components/Pagination'
import request from '@/utils/request' // secondary package based on el-pagination

const calendarTypeOptions = [
  { key: 'CN', display_name: 'China' },
  { key: 'US', display_name: 'USA' },
  { key: 'JP', display_name: 'Japan' },
  { key: 'EU', display_name: 'Eurozone' }
]

// arr to obj, such as { CN : "China", US : "USA" }
const calendarTypeKeyValue = calendarTypeOptions.reduce((acc, cur) => {
  acc[cur.key] = cur.display_name
  return acc
}, {})

export default {
  name: 'ComplexTable',
  components: { Pagination },
  directives: { waves },
  filters: {
    statusFilter(status) {
      const statusMap = {
        published: 'success',
        draft: 'info',
        deleted: 'danger'
      }
      return statusMap[status]
    },
    typeFilter(type) {
      return calendarTypeKeyValue[type]
    }
  },
  data() {
    return {
      tableKey: 0,
      list: null,
      total: 0,
      listLoading: true,
      listQuery: {
        page: 1,
        limit: 20,
        tel: undefined,
        teamID: undefined
      },
      activities: [], // 代表是审核记录的集合对象
      role: localStorage.getItem('roles'),
      // 状态码
      statusList: [
        { id: 2, value: '不同意' },
        { id: 1, value: '同意' }
      ],
      importanceOptions: [1, 2, 3],
      calendarTypeOptions,
      sortOptions: [{ label: 'ID Ascending', key: '+id' }, { label: 'ID Descending', key: '-id' }],
      statusOptions: ['published', 'draft', 'deleted'],
      showReviewer: false,
      temp: {
        id: undefined,
        remarks: '',
        name: '',
        status: 'published',
        team: {

        }
      },
      temp2: {
        content: '',
        status: 1
      },
      dialogFormVisible: false,
      dialogFormVisible2: false,
      dialogStatus: '',
      textMap: {
        update: '在线审核',
        create: '新建类型'
      },
      teamList: [],
      dialogPvVisible: false,
      pvData: [],
      rules: {
        type: [{ required: true, message: 'type is required', trigger: 'change' }],
        timestamp: [{ type: 'date', required: true, message: 'timestamp is required', trigger: 'change' }],
        title: [{ required: true, message: 'title is required', trigger: 'blur' }]
      },
      downloadLoading: false
    }
  },
  created() {
    this.getList()
    this.getTeamList()
  },
  methods: {
    // 获取社团列表信息
    getTeamList() {
      request({
        url: 'team/queryTeamList',
        method: 'get',
        params: ''
      }).then(res => {
        this.teamList = res.data// 数据信息
      })
    },

    getList() {
      this.listLoading = true
      request({
        url: 'applyInfo/queryApplyInfoList',
        method: 'get',
        params: this.listQuery
      }).then(res => {
        this.listLoading = false
        this.list = res.data// 数据信息
        this.total = res.total
      })
    },

    getAppList(id) {
      request({
        url: 'applyInfo/queryListByAppId',
        method: 'get',
        params: { appId: id }
      }).then(res => {
        this.activities = res.data
      })
    },

    handleFilter() {
      this.listQuery.page = 1
      this.getList()
    },
    handleModifyStatus(row, status) {
      this.$message({
        message: '操作Success',
        type: 'success'
      })
      row.status = status
    },
    sortChange(data) {
      const { prop, order } = data
      if (prop === 'id') {
        this.sortByID(order)
      }
    },
    sortByID(order) {
      if (order === 'ascending') {
        this.listQuery.sort = '+id'
      } else {
        this.listQuery.sort = '-id'
      }
      this.handleFilter()
    },
    resetTemp() {
      this.temp = {
        id: undefined,
        importance: 1,
        remark: '',
        timestamp: new Date(),
        title: '',
        status: 'published',
        type: ''
      }
    },
    handleCreate() {
      this.resetTemp()
      this.dialogStatus = 'create'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    createData() {
      /**
       * 1、获取值信息
       * 2、提交数据信息
       * 3、获取后端接口返回的状态码
       * 4、如果成功 重新加载table 让显示最新的数据 同时form窗口隐藏
       */

      request.post('http://localhost:9999/typeInfo/addInfo', this.temp).then(res => {
        if (res.code == 20000) {
          // 关闭弹出框
          this.dialogFormVisible = false
          this.$notify({
            title: 'Success',
            message: '添加社团类型成功',
            type: 'success',
            duration: 2000
          })
          this.getList()// 重载table信息内容
        }
      })
    },
    handleUpdate(row) {
      this.temp = Object.assign({}, row) // copy obj
      this.temp.timestamp = new Date(this.temp.timestamp)
      this.dialogStatus = 'update'
      this.dialogFormVisible = true
    },

    // 用来详细查询信息渲染方法
    handleQuery(row) {
      this.temp = Object.assign({}, row) // copy obj
      this.temp.timestamp = new Date(this.temp.timestamp)
      this.dialogStatus = 'update'
      this.dialogFormVisible2 = true
      // 获取记录集合对象
      this.activities = this.getAppList(row.id)
    },
    updateData() {
      this.temp2.appId = this.temp.id// 把当前对象的id 赋值给 appId
      console.log(this.temp)
      debugger
      request.post('applyInfo/addAppInfo', this.temp2).then(res => {
        if (res.code == 20000) {
          // 关闭弹出框
          this.dialogFormVisible = false
          this.$notify({
            title: 'Success',
            message: '审核记录成功',
            type: 'success',
            duration: 2000
          })
          this.getList()// 重载table信息内容
        } else {
          this.$notify({
            title: '失败',
            message: '审核失败',
            type: 'fail',
            duration: 2000
          })
        }
      })
    },
    handleDelete(row, index) {
      request('applyInfo/deleteById?id=' + row.id).then(res => {
        if (res.code == 20000) {
          this.$notify({
            title: 'Success',
            message: '删除成功',
            type: 'success',
            duration: 2000
          })
          this.getList()
        }
      })

      // this.list.splice(index, 1)
    },
    handleFetchPv(pv) {
      fetchPv(pv).then(response => {
        this.pvData = response.data.pvData
        this.dialogPvVisible = true
      })
    },
    handleDownload() {
      this.downloadLoading = true
      import('@/vendor/Export2Excel').then(excel => {
        const tHeader = ['timestamp', 'title', 'type', 'importance', 'status']
        const filterVal = ['timestamp', 'title', 'type', 'importance', 'status']
        const data = this.formatJson(filterVal)
        excel.export_json_to_excel({
          header: tHeader,
          data,
          filename: 'table-list'
        })
        this.downloadLoading = false
      })
    },
    formatJson(filterVal) {
      return this.list.map(v => filterVal.map(j => {
        if (j === 'timestamp') {
          return parseTime(v[j])
        } else {
          return v[j]
        }
      }))
    },
    getSortClass: function(key) {
      const sort = this.listQuery.sort
      return sort === `+${key}` ? 'ascending' : 'descending'
    }
  }
}
</script>
