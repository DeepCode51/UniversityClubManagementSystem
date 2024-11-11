<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input v-model="listQuery.name" placeholder="消费名称" style="width: 200px;" class="filter-item" @keyup.enter.native="handleFilter" />

      <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">
        高级查询
      </el-button>
      <el-button class="filter-item" style="margin-left: 10px;" type="primary" icon="el-icon-edit" @click="handleCreate">
        新增消费记录
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
      <el-table-column label="系统编号" prop="id" sortable="custom" align="center" width="100px" :class-name="getSortClass('id')">
        <template slot-scope="{row}">
          <span>{{ row.id }}</span>
        </template>
      </el-table-column>

      <el-table-column label="消费名称" width="120px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.name }}</span>
        </template>
      </el-table-column>

      <el-table-column label="活动名称" width="120px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.actName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="所属社团" width="120px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.team.name }}</span>
        </template>
      </el-table-column>

      <el-table-column label="费用" width="150px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.price }}</span>
        </template>
      </el-table-column>

      <el-table-column label="创建时间" width="180px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.ctime | parseTime('{y}-{m}-{d} {h}:{i}') }}</span>
        </template>
      </el-table-column>

      <el-table-column label="备注" width="250px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.remarks }}</span>
        </template>
      </el-table-column>

      <el-table-column label="操作" align="center" width="250px" class-name="small-padding fixed-width">
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
        <el-form-item label="消费名称" >
          <el-input v-model="temp.name" />
        </el-form-item>
        <el-form-item label="活动名称" >
          <el-select v-model="temp.actId" class="filter-item" placeholder="请选择">
            <el-option v-for="item in actList" :key="item.id" :label="item.name" :value="item.id" />
          </el-select>
        </el-form-item>

        <el-form-item label="费用">
          <el-input v-model="temp.price" />
        </el-form-item>

        <el-form-item label="备注">
          <el-input v-model="temp.remarks" :autosize="{ minRows: 2, maxRows: 4}" type="textarea" placeholder="请输入" />
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
        importance: undefined,
        name: undefined,
        type: undefined,
        sort: '+id'
      },

      importanceOptions: [1, 2, 3],
      sortOptions: [{ label: 'ID Ascending', key: '+id' }, { label: 'ID Descending', key: '-id' }],
      statusOptions: ['published', 'draft', 'deleted'],
      showReviewer: false,
      temp: {
        id: undefined,
        remarks: '',
        name: '',
        price: 0,
        status: 'published',
        actId:''
      },
      dialogFormVisible: false,
      dialogStatus: '',
      textMap: {
        update: '在线编辑',
        create: '新增记录'
      },
      dialogPvVisible: false,
      pvData: [],
      actList: [],
      rules: {
        type: [{ required: true, message: 'type is required', trigger: 'change' }],
        timestamp: [{ type: 'date', required: true, message: 'timestamp is required', trigger: 'change' }],
        name: [{ required: true, message: '消费名称必填', trigger: 'blur' }]
      },
      downloadLoading: false
    }
  },
  created() {
    const actId = this.$route.query.actId// 活动的id
    if (actId!=undefined){
        this.resetTemp()
        this.dialogStatus = 'create'
        this.dialogFormVisible = true
        this.$nextTick(() => {
          this.$refs['dataForm'].clearValidate()
          this.temp.actId = actId
        })
    }

    this.getList()
    this.getActList()
  },
  methods: {
    getActList() {
      request(
        {
          url: 'http://localhost:9999/activity/queryActivityInfoList',
          method: 'get'
        }).then(res => {
        if (res.code == 20000) {
          this.actList = res.data
        }
      })
    },
    getList() {
      this.listLoading = true
      request({
        url: 'costlist/queryCostList',
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

      request.post('costlist/addInfo', this.temp).then(res => {
        if (res.code == 20000) {
          // 关闭弹出框
          this.dialogFormVisible = false
          this.$notify({
            title: 'Success',
            message: '添加成功',
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
      request.post('costlist/updateInfo', this.temp).then(res => {
        if (res.code == 20000) {
          // 关闭弹出框
          this.dialogFormVisible = false
          this.$notify({
            title: 'Success',
            message: '修改成功',
            type: 'success',
            duration: 2000
          })
          this.getList()// 重载table信息内容
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
      request('costlist/deleteById?id=' + row.id).then(res => {
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
