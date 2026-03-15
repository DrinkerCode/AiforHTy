<template>
  <div class="launch-management">
    <el-card class="header-card">
      <div class="header-content">
        <h2>🚀 火箭发射记录管理</h2>
        <div class="actions">
          <el-button type="primary" @click="openCreateDialog">
            <el-icon><Plus /></el-icon> 新增记录
          </el-button>
          <el-button @click="loadData">
            <el-icon><Refresh /></el-icon> 刷新
          </el-button>
        </div>
      </div>
    </el-card>

    <!-- 统计卡片 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-item">
            <div class="stat-label">总记录数</div>
            <div class="stat-value">{{ statistics.total || 0 }}</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-item success">
            <div class="stat-label">成功次数</div>
            <div class="stat-value">{{ statistics.successCount || 0 }}</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-item danger">
            <div class="stat-label">失败次数</div>
            <div class="stat-value">{{ statistics.failureCount || 0 }}</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-item warning">
            <div class="stat-label">成功率</div>
            <div class="stat-value">{{ (statistics.successRate || 0).toFixed(1) }}%</div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 搜索过滤 -->
    <el-card class="filter-card">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="任务名称">
          <el-input v-model="searchForm.missionName" placeholder="请输入任务名称" clearable />
        </el-form-item>
        <el-form-item label="国家">
          <el-input v-model="searchForm.country" placeholder="请输入国家" clearable />
        </el-form-item>
        <el-form-item label="火箭型号">
          <el-input v-model="searchForm.rocketType" placeholder="请输入火箭型号" clearable />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="请选择状态" clearable>
            <el-option label="成功" value="SUCCESS" />
            <el-option label="失败" value="FAILURE" />
            <el-option label="部分成功" value="PARTIAL_SUCCESS" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 数据表格 -->
    <el-card class="table-card">
      <el-table 
        :data="tableData" 
        v-loading="loading"
        border
        stripe
        style="width: 100%"
      >
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="missionName" label="任务名称" min-width="180" />
        <el-table-column prop="rocketType" label="火箭型号" width="120" />
        <el-table-column prop="country" label="国家" width="100" />
        <el-table-column prop="launchSite" label="发射场" width="150" />
        <el-table-column prop="launchTime" label="发射时间" width="170">
          <template #default="{ row }">
            {{ formatDateTime(row.launchTime) }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">
              {{ getStatusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="payloadType" label="载荷类型" width="120" />
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <el-button size="small" @click="openEditDialog(row)">编辑</el-button>
            <el-button size="small" type="danger" @click="handleDelete(row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <el-pagination
        v-model:current-page="pagination.page"
        v-model:page-size="pagination.size"
        :total="pagination.total"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="loadData"
        @current-change="loadData"
        style="margin-top: 20px; justify-content: flex-end"
      />
    </el-card>

    <!-- 新增/编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="isEdit ? '编辑发射记录' : '新增发射记录'"
      width="600px"
    >
      <el-form :model="formData" :rules="formRules" ref="formRef" label-width="100px">
        <el-form-item label="任务名称" prop="missionName">
          <el-input v-model="formData.missionName" placeholder="请输入任务名称" />
        </el-form-item>
        <el-form-item label="火箭型号" prop="rocketType">
          <el-input v-model="formData.rocketType" placeholder="请输入火箭型号" />
        </el-form-item>
        <el-form-item label="国家" prop="country">
          <el-input v-model="formData.country" placeholder="请输入国家" />
        </el-form-item>
        <el-form-item label="发射场" prop="launchSite">
          <el-input v-model="formData.launchSite" placeholder="请输入发射场" />
        </el-form-item>
        <el-form-item label="发射时间" prop="launchTime">
          <el-date-picker
            v-model="formData.launchTime"
            type="datetime"
            placeholder="选择发射时间"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="formData.status" placeholder="请选择状态" style="width: 100%">
            <el-option label="成功" value="SUCCESS" />
            <el-option label="失败" value="FAILURE" />
            <el-option label="部分成功" value="PARTIAL_SUCCESS" />
          </el-select>
        </el-form-item>
        <el-form-item label="载荷类型" prop="payloadType">
          <el-input v-model="formData.payloadType" placeholder="请输入载荷类型" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="formData.remark" type="textarea" :rows="3" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitting">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Refresh } from '@element-plus/icons-vue'
import api from '@/api'

const loading = ref(false)
const submitting = ref(false)
const dialogVisible = ref(false)
const isEdit = ref(false)
const formRef = ref(null)

const searchForm = reactive({
  missionName: '',
  country: '',
  rocketType: '',
  status: ''
})

const pagination = reactive({
  page: 1,
  size: 10,
  total: 0
})

const tableData = ref([])
const statistics = ref({})

const formData = reactive({
  id: null,
  missionName: '',
  rocketType: '',
  country: '',
  launchSite: '',
  launchTime: '',
  status: 'SUCCESS',
  payloadType: '',
  remark: ''
})

const formRules = {
  missionName: [{ required: true, message: '请输入任务名称', trigger: 'blur' }],
  rocketType: [{ required: true, message: '请输入火箭型号', trigger: 'blur' }],
  country: [{ required: true, message: '请输入国家', trigger: 'blur' }],
  launchTime: [{ required: true, message: '请选择发射时间', trigger: 'change' }],
  status: [{ required: true, message: '请选择状态', trigger: 'change' }]
}

// 加载数据
const loadData = async () => {
  loading.value = true
  try {
    const res = await api.get('/admin/launches', {
      params: {
        page: pagination.page,
        size: pagination.size,
        sortBy: 'createTime',
        sortDirection: 'desc'
      }
    })
    tableData.value = res.data.content || []
    pagination.total = res.data.total || 0
  } catch (error) {
    ElMessage.error('加载数据失败')
  } finally {
    loading.value = false
  }
}

// 加载统计信息
const loadStatistics = async () => {
  try {
    const res = await api.get('/admin/launches/statistics')
    statistics.value = res.data
  } catch (error) {
    console.error('加载统计信息失败', error)
  }
}

// 搜索
const handleSearch = () => {
  pagination.page = 1
  loadData()
}

// 重置搜索
const resetSearch = () => {
  searchForm.missionName = ''
  searchForm.country = ''
  searchForm.rocketType = ''
  searchForm.status = ''
  handleSearch()
}

// 打开新增对话框
const openCreateDialog = () => {
  isEdit.value = false
  Object.assign(formData, {
    id: null,
    missionName: '',
    rocketType: '',
    country: '',
    launchSite: '',
    launchTime: '',
    status: 'SUCCESS',
    payloadType: '',
    remark: ''
  })
  dialogVisible.value = true
}

// 打开编辑对话框
const openEditDialog = (row) => {
  isEdit.value = true
  Object.assign(formData, {
    id: row.id,
    missionName: row.missionName,
    rocketType: row.rocketType,
    country: row.country,
    launchSite: row.launchSite,
    launchTime: row.launchTime,
    status: row.status,
    payloadType: row.payloadType,
    remark: row.remark
  })
  dialogVisible.value = true
}

// 提交表单
const handleSubmit = async () => {
  if (!formRef.value) return
  
  await formRef.value.validate(async (valid) => {
    if (!valid) return
    
    submitting.value = true
    try {
      if (isEdit.value) {
        await api.put(`/admin/launches/${formData.id}`, formData)
        ElMessage.success('更新成功')
      } else {
        await api.post('/admin/launches', formData)
        ElMessage.success('创建成功')
      }
      dialogVisible.value = false
      loadData()
      loadStatistics()
    } catch (error) {
      ElMessage.error(isEdit.value ? '更新失败' : '创建失败')
    } finally {
      submitting.value = false
    }
  })
}

// 删除
const handleDelete = (id) => {
  ElMessageBox.confirm('确定要删除这条记录吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await api.delete(`/admin/launches/${id}`)
      ElMessage.success('删除成功')
      loadData()
      loadStatistics()
    } catch (error) {
      ElMessage.error('删除失败')
    }
  })
}

// 格式化日期时间
const formatDateTime = (datetime) => {
  if (!datetime) return ''
  const date = new Date(datetime)
  return date.toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit'
  })
}

// 获取状态类型
const getStatusType = (status) => {
  const types = {
    SUCCESS: 'success',
    FAILURE: 'danger',
    PARTIAL_SUCCESS: 'warning'
  }
  return types[status] || 'info'
}

// 获取状态文本
const getStatusText = (status) => {
  const texts = {
    SUCCESS: '成功',
    FAILURE: '失败',
    PARTIAL_SUCCESS: '部分成功'
  }
  return texts[status] || status
}

onMounted(() => {
  loadData()
  loadStatistics()
})
</script>

<style scoped lang="scss">
.launch-management {
  padding: 20px;
  
  .header-card {
    margin-bottom: 20px;
    
    .header-content {
      display: flex;
      justify-content: space-between;
      align-items: center;
      
      h2 {
        margin: 0;
        color: #1a2332;
      }
      
      .actions {
        display: flex;
        gap: 10px;
      }
    }
  }
  
  .stats-row {
    margin-bottom: 20px;
    
    .stat-card {
      .stat-item {
        text-align: center;
        
        .stat-label {
          font-size: 14px;
          color: #666;
          margin-bottom: 8px;
        }
        
        .stat-value {
          font-size: 28px;
          font-weight: bold;
          color: #1a2332;
        }
        
        &.success .stat-value {
          color: #67c23a;
        }
        
        &.danger .stat-value {
          color: #f56c6c;
        }
        
        &.warning .stat-value {
          color: #e6a23c;
        }
      }
    }
  }
  
  .filter-card {
    margin-bottom: 20px;
  }
  
  .table-card {
    margin-bottom: 20px;
  }
}
</style>
