<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input v-model="listQuery.username" placeholder="用户名" style="width: 200px;" class="filter-item" @keyup.enter.native="handleFilter" />
      <el-input v-model="listQuery.tel" placeholder="电话" style="width: 200px;" class="filter-item" @keyup.enter.native="handleFilter" />

      <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">
        高级查询
      </el-button>
      <el-button v-if="role!='manager'" class="filter-item" style="margin-left: 10px;" type="primary" icon="el-icon-edit" @click="handleCreate">
        新增用户
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

      <el-table-column label="用户名" width="140px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.username }}</span>
        </template>
      </el-table-column>

      <el-table-column label="真实姓名" width="120px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.realname }}</span>
        </template>
      </el-table-column>

      <el-table-column label="性别" width="100px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.sex }}</span>
        </template>
      </el-table-column>

      <el-table-column label="用户类型" width="120px" align="center">
        <template slot-scope="{row}">
          <span v-if="row.type==1">超级管理员</span>
          <span v-if="row.type==0">社团管理员</span>
        </template>
      </el-table-column>

      <el-table-column label="电话" width="150px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.tel }}</span>
        </template>
      </el-table-column>

      <el-table-column label="邮箱" width="200px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.email }}</span>
        </template>
      </el-table-column>


      <el-table-column label="操作" align="center" width="350" class-name="small-padding fixed-width">
        <template slot-scope="{row,$index}">
          <el-button size="mini" type="success" @click="handleCreate2(row)">
            修改密码
          </el-button>

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
        <el-form-item label="用户姓名">
          <el-input v-model="temp.username" />
        </el-form-item>
        <el-form-item label="真实姓名">
          <el-input v-model="temp.realname" />
        </el-form-item>

        <el-form-item label="用户性别">
          <el-radio v-model="temp.sex" label="男">男</el-radio>
          <el-radio v-model="temp.sex" label="女">女</el-radio>
        </el-form-item>

        <el-form-item label="用户类型">
          <el-select v-model="temp.type" placeholder="请选择">
            <el-option v-if="role!='manager'" label="超级管理员" value="1" />
            <el-option label="社团管理员" value="0" />
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

    <el-dialog title="修改密码" :visible.sync="dialogFormVisible2">
      <el-form :rules="rules" :model="temp2" label-position="left" label-width="70px" style="width: 400px; margin-left:50px;">
        <el-form-item label="输入密码">
          <el-input v-model="temp2.password" />
        </el-form-item>
        <el-form-item label="再次输入">
          <el-input v-model="temp2.password2" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible2 = false">
          取消
        </el-button>
        <el-button type="primary" @click="updatePassWord()">
          提交
        </el-button>
      </div>
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
        username: undefined,
        tel: undefined
      },
      // 获取登录角色
      role: localStorage.getItem('roles'),
      importanceOptions: [1, 2, 3],
      calendarTypeOptions,
      sortOptions: [{ label: 'ID Ascending', key: '+id' }, { label: 'ID Descending', key: '-id' }],
      statusOptions: ['published', 'draft', 'deleted'],
      showReviewer: false,
      temp: {
        id: undefined,
        username: undefined,
        realname: undefined,
        tel: undefined,
        email: undefined,
        type: undefined,
        sex: undefined,
      },
      temp2: {
        password: '',
        password2: '',
        id: ''
      },
      dialogFormVisible: false,
      dialogStatus: '',
      textMap: {
        update: '在线编辑',
        create: '新增用户'
      },
      dialogPvVisible: false,
      dialogFormVisible2: false,
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
  },
  methods: {
    getList() {
      this.listLoading = true
      request({
        url: 'userinfo/queryUserInfoListAll',
        method: 'get',
        params: this.listQuery
      }).then(res => {
        this.listLoading = false
        this.list = res.data// 数据信息
        this.total = res.total
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
        status: 0,
        username: undefined,
        realname: undefined,
        tel: undefined,
        email: undefined,
        type: undefined,
        sex: undefined
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

    handleCreate2(row) {
      this.dialogFormVisible2 = true
      this.temp2.id = row.id
    },
    createData() {
      /**
       * 1、获取值信息
       * 2、提交数据信息
       * 3、获取后端接口返回的状态码
       * 4、如果成功 重新加载table 让显示最新的数据 同时form窗口隐藏
       */
      console.log(this.temp)
      debugger
      request.post('userinfo/addInfo', this.temp).then(res => {
        if (res.code == 20000) {
          // 关闭弹出框
          this.dialogFormVisible = false
          this.$notify({
            title: 'Success',
            message: '添加成功',
            type: 'success',
            duration: 2000
          })
          this.getList()
        }
      })
    },
    handleUpdate(row) {
      this.temp = Object.assign({}, row) // copy obj
      this.temp.timestamp = new Date(this.temp.timestamp)
      // 把数据类型转换成一致  type
      this.temp.type = String(row.type)
      this.dialogStatus = 'update'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
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
    },
    handleDelete(row, index) {
      /**
       * 1、获取要删除记录的id 主键
       * 2、发送请求给后端服务器
       * 3、前后端根据后端服务器返回结果判断是否成功
       * 4、成功的话 重新加载table 展示最新内容
       */
      request('userinfo/deleteById?id=' + row.id).then(res => {
        if (res.code == 20000) {
          this.$notify({
            title: 'Success',
            message: '删除成功 ',
            type: 'success',
            duration: 2000
          })
          this.getList()
        }
      })
    },

    // 修改密码方法
    updatePassWord() {
      debugger
      if (this.temp2.password == this.temp2.password2) {
        // 向后端发布请求 修改密码
        request.post('userinfo/updatePassword', this.temp2).then(res => {
          if (res.code == 20000) {
            debugger
            // 关闭弹出框
            this.dialogFormVisible2 = false
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
      } else {
        alert('两次密码不一致，不能提交')
      }
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
