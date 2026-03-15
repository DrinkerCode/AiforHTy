<template>
  <div class="satellite-management">
    <el-card class="header-card">
      <div class="header-content">
        <h2>🛰️ 卫星数据管理</h2>
        <div class="actions">
          <el-button type="primary" @click="openCreateDialog">
            <el-icon><Plus /></el-icon> 新增卫星
          </el-button>
          <el-button @click="loadData">
            <el-icon><Refresh /></el-icon> 刷新
          </el-button>
        </div>
      </div>
    </el-card>

    <!-- 统计卡片 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :span="8">
        <el-card class="stat-card">
          <div class="stat-item">
            <div class="stat-label">卫星总数</div>
            <div class="stat-value">{{ statistics.total || 0 }}</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card class="stat-card">
          <div class="stat-item success">
            <div class="stat-label">在轨运行</div>
            <div class="stat-value">{{ statistics.activeCount || 0 }}</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card class="stat-card">
          <div class="stat-item danger">
            <div class="stat-label">已退役</div>
            <div class="stat-value">{{ statistics.inactiveCount || 0 }}</div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 搜索过滤 -->
    <el-card class="filter-card">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="卫星名称">
          <el-input v-model="searchForm.satelliteName" placeholder="请输入卫星名称" clearable />
        </el-form-item>
        <el-form-item label="国家">
          <el-input v-model="searchForm.country" placeholder="请输入国家" clearable />
        </el-form-item>
        <el-form-item label="类型">
          <el-select v-model="searchForm.satelliteType" placeholder="请选择类型" clearable>
            <el-option label="通信卫星" value="COMMUNICATION" />
            <el-option label="导航卫星" value="NAVIGATION" />
            <el-option label="遥感卫星" value="REMOTE_SENSING" />
            <el-option label="科学卫星" value="SCIENCE" />
            <el-option label="其他" value="OTHER" />
          </el-select>
        </el-form-item>
        <el-form-item label="轨道">
          <el-select v-model="searchForm.orbitType" placeholder="请选择轨道" clearable>
            <el-option label="低地球轨道 (LEO)" value="LEO" />
            <el-option label="中地球轨道 (MEO)" value="MEO" />
            <el-option label="地球同步轨道 (GEO)" value="GEO" />
            <el-option label="高椭圆轨道 (HEO)" value="HEO" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.active" placeholder="请选择状态" clearable>
            <el-option label="在轨运行" :value="true" />
            <el-option label="已退役" :value="false" />
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
        <el-table-column prop="id" label="ID" width="70" />
        <el-table-column prop="satelliteName" label="卫星名称" min-width="150" />
        <el-table-column prop="country" label="国家" width="100" />
        <el-table-column prop="satelliteType" label="类型" width="120">
          <template #default="{ row }">
            {{ getSatelliteTypeText(row.satelliteType) }}
          </template>
        </el-table-column>
        <el-table-column prop="orbitType" label="轨道类型" width="100" />
        <el-table-column prop="orbitHeight" label="轨道高度 (km)" width="100" />
        <el-table-column prop="launchTime" label="发射时间" width="170">
          <template #default="{ row }">
            {{ formatDateTime(row.launchTime) }}
          </template>
        </el-table-column>
        <el-table-column prop="active" label="状态" width="80">
          <template #default="{ row }">
            <el-tag :type="row.active ? 'success' : 'info'">
              {{ row.active ? '在轨' : '退役' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150" fixed="right">
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
      :title="isEdit ? '编辑卫星数据' : '新增卫星数据'"
      width="600px"
    >
      <el-form :model="formData" :rules="formRules" ref="formRef" label-width="100px">
        <el-form-item label="卫星名称" prop="satelliteName">
          <el-input v-model="formData.satelliteName" placeholder="请输入卫星名称" />
        </el-form-item>
        <el-form-item label="国家" prop="country">
          <el-input v-model="formData.country" placeholder="请输入国家" />
        </el-form-item>
        <el-form-item label="卫星类型" prop="satelliteType">
          <el-select v-model="formData.satelliteType" placeholder="请选择类型" style="width: 100%">
            <el-option label="通信卫星" value="COMMUNICATION" />
            <el-option label="导航卫星" value="NAVIGATION" />
            <el-option label="遥感卫星" value="REMOTE_SENSING" />
            <el-option label="科学卫星" value="SCIENCE" />
            <el-option label="其他" value="OTHER" />
          </el-select>
        </el-form-item>
        <el-form-item label="轨道类型" prop="orbitType">
          <el-select v-model="formData.orbitType" placeholder="请选择轨道类型" style="width: 100%">
            <el-option label="低地球轨道 (LEO)" value="LEO" />
            <el-option label="中地球轨道 (MEO)" value="MEO" />
            <el-option label="地球同步轨道 (GEO)" value="GEO" />
            <el-option label="高椭圆轨道 (HEO)" value="HEO" />
          </el-select>
        </el-form-item>
        <el-form-item label="轨道高度" prop="orbitHeight">
          <el-input-number v-model="formData.orbitHeight" :min="0" :max="100000" style="width: 100%" />
        </el-form-item>
        <el-form-item label="发射时间" prop="launchTime">
          <el-date-picker
            v-model="formData.launchTime"
            type="datetime"
            placeholder="选择发射时间"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="是否活跃" prop="active">
          <el-switch v-model="formData.active" active-text="在轨" inactive-text="退役" />
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
  satelliteName: '',
  country: '',
  satelliteType: '',
  orbitType: '',
  active: null
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
  satelliteName: '',
  country: '',
  satelliteType: 'COMMUNICATION',
  orbitType: 'LEO',
  orbitHeight: 500,
  launchTime: '',
  active: true,
  remark: ''
})

const formRules = {
  satelliteName: [{ required: true, message: '请输入卫星名称', trigger: 'blur' }],
  country: [{ required: true, message: '请输入国家', trigger: 'blur' }],
  satelliteType: [{ required: true, message: '请选择卫星类型', trigger: 'change' }],
  orbitType: [{ required: true, message: '请选择轨道类型', trigger: 'change' }]
}

// 加载数据
const loadData = async () => {
  loading.value = true
  try {
    const res = await api.get('/admin/satellites', {
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
    const res = await api.get('/admin/satellites/statistics')
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
  searchForm.satelliteName = ''
  searchForm.country = ''
  searchForm.satelliteType = ''
  searchForm.orbitType = ''
  searchForm.active = null
  handleSearch()
}

// 打开新增对话框
const openCreateDialog = () => {
  isEdit.value = false
  Object.assign(formData, {
    id: null,
    satelliteName: '',
    country: '',
    satelliteType: 'COMMUNICATION',
    orbitType: 'LEO',
    orbitHeight: 500,
    launchTime: '',
    active: true,
    remark: ''
  })
  dialogVisible.value = true
}

// 打开编辑对话框
const openEditDialog = (row) => {
  isEdit.value = true
  Object.assign(formData, {
    id: row.id,
    satelliteName: row.satelliteName,
    country: row.country,
    satelliteType: row.satelliteType,
    orbitType: row.orbitType,
    orbitHeight: row.orbitHeight,
    launchTime: row.launchTime,
    active: row.active,
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
        await api.put(`/admin/satellites/${formData.id}`, formData)
        ElMessage.success('更新成功')
      } else {
        await api.post('/admin/satellites', formData)
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
  ElMessageBox.confirm('确定要删除这条卫星数据吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await api.delete(`/admin/satellites/${id}`)
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

// 获取卫星类型文本
const getSatelliteTypeText = (type) => {
  const texts = {
    COMMUNICATION: '通信卫星',
    NAVIGATION: '导航卫星',
    REMOTE_SENSING: '遥感卫星',
    SCIENCE: '科学卫星',
    OTHER: '其他'
  }
  return texts[type] || type
}

onMounted(() => {
  loadData()
  loadStatistics()
})
</script>

<style scoped lang="scss">
.satellite-management {
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
