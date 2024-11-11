<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input v-model="listQuery.realname" placeholder="真实姓名" style="width: 200px;" class="filter-item" @keyup.enter.native="handleFilter" />
      <el-input v-model="listQuery.tel" placeholder="电话" style="width: 200px;" class="filter-item" @keyup.enter.native="handleFilter" />
      <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">
        高级查询
      </el-button>
      <el-button class="filter-item" style="margin-left: 10px;" type="primary" icon="el-icon-edit" @click="handleCreate">
        添加成员
      </el-button>
      <span>&nbsp;&nbsp;&nbsp;当前缴费状态：{{this.pay}}</span>
      <el-button class="filter-item" style="margin-left: 10px;" type="success" icon="el-icon-check" @click="getOpen('已开启')">
        开启缴费
      </el-button>
      <el-button class="filter-item" style="margin-left: 10px;" type="danger" icon="el-icon-close" @click="getOpen('已关闭')">
        关闭缴费
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
      <el-table-column label="ID" prop="id" sortable="custom" align="center" width="60" :class-name="getSortClass('id')">
        <template slot-scope="{row}">
          <span>{{ row.id }}</span>
        </template>
      </el-table-column>

      <el-table-column label="姓名" width="130px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.username }}</span>
        </template>
      </el-table-column>
      <el-table-column label="性别" width="50px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.sex }}</span>
        </template>
      </el-table-column>
      <el-table-column label="学院" width="130px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.college }}</span>
        </template>
      </el-table-column>
      <el-table-column label="年级" width="60px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.grade }}</span>
        </template>
      </el-table-column>

      <el-table-column label="电话" width="110px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.tel }}</span>
        </template>
      </el-table-column>
      <el-table-column label="邮箱" width="160px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.email }}</span>
        </template>
      </el-table-column>
      <el-table-column label="所属社团" width="130px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.team.name }}</span>
        </template>
      </el-table-column>
      <el-table-column label="缴费状态" width="80px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.state }}</span>
        </template>
      </el-table-column>
      <el-table-column label="加入时间" width="150px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.joinTime | parseTime('{y}-{m}-{d} {h}:{i}') }}</span>
        </template>
      </el-table-column>

      <el-table-column label="操作" align="center" min-width="200" class-name="small-padding fixed-width">
        <template slot-scope="{row,$index}">
          <el-button type="primary" size="mini" @click="handleUpdate(row)">
            编辑
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

        <el-form-item label="用户名">
          <el-input v-model="temp.username" />
        </el-form-item>
        <el-form-item label="真实姓名">
          <el-input v-model="temp.realname" />
        </el-form-item>
        <el-form-item label="性别">
          <template>
            <el-radio v-model="temp.sex" label="男">男</el-radio>
            <el-radio v-model="temp.sex" label="女">女</el-radio>
          </template>
        </el-form-item>
        <el-form-item label="学院">
          <el-input v-model="temp.college" />
        </el-form-item>
        <el-form-item label="年级">
          <el-input v-model="temp.grade" />
        </el-form-item>
        <el-form-item label="电话">
          <el-input v-model="temp.tel" />
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="temp.email" />
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="temp.password" />
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
      imageUrl: '',
      tableKey: 0,
      list: null,
      total: 0,
      listLoading: true,
      listQuery: {
        page: 1,
        limit: 20,
        importance: undefined,
        realname: undefined,
        tel: undefined
      },
      // 社团团长
      userList: [],
      // 社团类型的对象
      typeList: [],
      importanceOptions: [1, 2, 3],
      calendarTypeOptions,
      sortOptions: [{ label: 'ID Ascending', key: '+id' }, { label: 'ID Descending', key: '-id' }],
      statusOptions: [
        { id: 0, value: '禁用' }, { id: 1, value: '正常' }
      ],
      showReviewer: false,
      temp: {
        id: undefined,
        username: '',
        realname: '',
        sex: '',
        tel: '',
        password: '',
      },
      dialogFormVisible: false,
      dialogStatus: '',
      textMap: {
        update: '编辑成员',
        create: '加入新成员'
      },
      dialogPvVisible: false,
      pvData: [],
      rules: {
        type: [{ required: true, message: 'type is required', trigger: 'change' }],
        timestamp: [{ type: 'date', required: true, message: 'timestamp is required', trigger: 'change' }],
        title: [{ required: true, message: 'title is required', trigger: 'blur' }]
      },
      downloadLoading: false,
      pay:''
    }
  },
  created() {
    this.getList()
    this.getOpen('')
  },
  methods: {
    getOpen(pay){
      request.get('team/open?pay='+pay).then(res => {
        if (res.code == 20000 && pay!='') {
          this.$notify({
            title: 'Success',
            message:res.data,
            type: 'success',
            duration: 2000
          })
          this.pay = res.data
        }else {
          this.pay = res.data
        }
      })
    },
    getList() {
      /**
       * 1、发送请求 返回结果集合
       * 2、根据结果集合渲染页面
       * 3、把加载框取消为false
       */
      this.listLoading = true
      console.log(this.listQuery)
      debugger
      request(
        {
          url: 'teamuser/queryTeamUserList',
          method: 'get',
          params: this.listQuery
        }).then(res => {
        if (res.code == 20000) {
          this.list = res.data
          this.total = res.total
          this.listLoading = false
        }
      }).catch(err => {

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
        status: '',
        type: '',
        imageUrl: ''
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
       * 1、获取form单中的数据信息
       * 2、向后端服务器发送请求  完成请求操作
       * 3、服务器端处理完成后 返回状态码
       * 4、前端如果接收 添加成功
       *    进行table刷新 弹出框关闭
       */
      this.temp.images = this.imageUrl// 图片变量的赋值操作
      debugger
      console.log(this.temp)
      request.post('teamuser/addInfo', this.temp).then(res => {
        if (res.code == 20000) {
          this.dialogFormVisible = false
          this.$notify({
            title: 'Success',
            message: '添加成功',
            type: 'success',
            duration: 2000
          })
          this.getList()// 刷新table组件
        }
      })
    },
    handleUpdate(row) {
      this.temp = Object.assign({}, row) // copy obj
      this.imageUrl = row.images // 把获取到的记录信息进行重新渲染值
      this.temp.timestamp = new Date(this.temp.timestamp)
      this.dialogStatus = 'update'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    updateData() {
      this.temp.images = this.imageUrl
      debugger
      console.log(this.temp)
      request.post('teamuser/updateInfo', this.temp).then(res => {
        if (res.code == 20000) {
          this.dialogFormVisible = false
          this.$notify({
            title: 'Success',
            message: '编辑成员信息成功',
            type: 'success',
            duration: 2000
          })
          this.getList()
        }
      })
    },
    handleDelete(row, index) {
      /**
       * 1、获取要删除记录的id 主键
       * 2、发送请求给后端服务器
       * 3、前后端根据后端服务器返回结果判断是否成功
       * 4、成功的话 重新加载table 展示最新内容
       */
      request('teamuser/deleteById?id=' + row.id).then(res => {
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
