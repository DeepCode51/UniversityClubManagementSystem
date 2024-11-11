<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input v-model="listQuery.name" placeholder="社团名称" style="width: 200px;" class="filter-item" @keyup.enter.native="handleFilter" />
      <el-select v-model="listQuery.type" placeholder="选择类型" clearable class="filter-item" style="width: 130px">
        <el-option v-for="item in typeList" :key="item.id" :label="item.name" :value="item.id" />
      </el-select>

      <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">
        高级查询
      </el-button>
      <el-button class="filter-item" style="margin-left: 10px;" type="primary" icon="el-icon-edit" @click="handleCreate">
        新建社团
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
      <el-table-column label="ID" prop="id" sortable="custom" align="center" width="80" :class-name="getSortClass('id')">
        <template slot-scope="{row}">
          <span>{{ row.id }}</span>
        </template>
      </el-table-column>
      <el-table-column label="社团名称" width="130px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.name }}</span>
        </template>
      </el-table-column>
      <el-table-column label="社团类型" width="130px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.typeInfo.name }}</span>
        </template>
      </el-table-column>
      <el-table-column label="社长" width="130px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.userInfo.username }}</span>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" width="180px" align="center">
          <template slot-scope="{row}">
            <span>{{ row.createTime | parseTime('{y}-{m}-{d} {h}:{i}') }}</span>
          </template>
      </el-table-column>
      <el-table-column label="状态" class-name="status-col" width="120">
          <template slot-scope="{row}">
            <el-tag :type="row.status | statusFilter">
              <span v-if="row.status==1">正常</span>
              <span v-if="row.status==0" style="color: red">禁用</span>
            </el-tag>
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
        <el-form-item label="社团名称">
          <el-input v-model="temp.name" />
        </el-form-item>
        <el-form-item label="社团类型" >
          <el-select v-model="temp.type" class="filter-item" placeholder="请选择">
            <el-option v-for="item in typeList" :key="item.id" :label="item.name" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="创建时间" >
          <el-date-picker
                          v-model="temp.createTime"
                          type="datetime"
                          value-format="yyyy-MM-dd HH:mm:ss"
                          placeholder="选择时间" />
        </el-form-item>
        <el-form-item label="社长">
          <el-select v-model="temp.userId" class="filter-item" placeholder="选择社长">
            <el-option v-for="item in userList" :key="item.id" :label="item.username" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="社团状态">
          <el-select v-model="temp.status" class="filter-item" placeholder="请选择">
            <el-option v-for="item in statusOptions" :key="id" :label="item.value" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="社团可容纳人数" label-width="100px">
          <el-input-number v-model="temp.astrict" :min="0" />
        </el-form-item>
        <el-form-item label="社团图片">
          <el-upload
            class="avatar-uploader"
            action="http://localhost:9999/team/fileUpload"
            :show-file-list="false"
            :on-success="handleAvatarSuccess"
            :before-upload="beforeAvatarUpload">
            <img v-if="imageUrl" :src="imageUrl" class="avatar">
            <i v-else class="el-icon-plus avatar-uploader-icon" />
          </el-upload>
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

<style>
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
.avatar-uploader .el-upload:hover {
  border-color: #409EFF;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
}
.avatar {
  width: 178px;
  height: 178px;
  display: block;
}
</style>

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
        name: undefined,
        type: undefined
      },
      userList: [],
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
        name: '',
        timestamp: new Date(),
        type: '',
        status: 0,
        images: '',
        astrict: 0
      },
      dialogFormVisible: false,
      dialogStatus: '',
      textMap: {
        update: '编辑社团',
        create: '新建社团'
      },
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
    this.getTypeList()
    this.getUserList()
  },
  methods: {
    handleAvatarSuccess(res, file) {
      console.log(res)
      console.log(file)
      this.imageUrl = URL.createObjectURL(file.raw);
    },
    beforeAvatarUpload(file) {
      const isJPG = file.type === 'image/png'
      const isLt2M = file.size / 1024 / 1024 < 2

      if (!isJPG) {
        this.$message.error('上传头像图片只能是 png 格式!')
      }
      if (!isLt2M) {
        this.$message.error('上传头像图片大小不能超过 2MB!')
      }
      return isJPG && isLt2M
    },
    getUserList() {
      request(
        {
          url: 'http://localhost:9999/userinfo/queryUserInfoList',
          method: 'get'
        }).then(res => {
        if (res.code == 20000) {
          this.userList = res.data
        }
      })
    },
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
    },
    getList() {
      this.listLoading = true// 没有信息之前显示加载内容
      request(
        {
          url: 'http://localhost:9999/team/queryTeamList',
          method: 'get',
          params: this.listQuery
        }).then(res => {
        if (res.code == 20000) {
          this.list = res.data
          this.total = res.total
          this.listLoading = false
        }
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
        imageUrl: '',
        astrict: 0
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
      this.temp.images = this.imageUrl// 图片变量的赋值操作
      this.userList = this.getUserList()
      debugger
      console.log(this.temp)
      request.post('http://localhost:9999/team/addInfo', this.temp).then(res => {
        if (res.code == 20000) {
          this.dialogFormVisible = false
          this.$notify({
            title: 'Success',
            message: '新建社团成功',
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
      this.dialogStatus = 'update'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    updateData() {
      this.temp.images = this.imageUrl// 图片变量的赋值操作

      debugger
      console.log(this.temp)
      request.post('http://localhost:9999/team/updateInfo', this.temp).then(res => {
        if (res.code == 20000) {
          this.dialogFormVisible = false
          this.$notify({
            title: 'Success',
            message: '修改成功',
            type: 'success',
            duration: 2000
          })
          this.getList()// 刷新table组件
        }
      })
    },
    handleDelete(row, index) {
      request('http://localhost:9999/team/deleteByID?id=' + row.id).then(res => {
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
