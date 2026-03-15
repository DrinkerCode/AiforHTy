<template>
  <div class="page-container fade-in">
    <el-card class="space-card">
      <template #header>
        <div class="card-title">
          <el-icon><Rocket /></el-icon>
          <span>长征系列火箭发射记录</span>
        </div>
      </template>
      
      <div ref="chartRef" class="echart-container"></div>
      
      <div class="rocket-table">
        <h3>详细数据</h3>
        <el-table :data="rocketData" style="width: 100%" :header-cell-style="{ background: '#2a3544', color: '#e0e6ed' }">
          <el-table-column prop="name" label="火箭型号" width="150" />
          <el-table-column prop="totalLaunches" label="总发射次数" width="120" />
          <el-table-column prop="successfulLaunches" label="成功次数" width="120" />
          <el-table-column prop="successRate" label="成功率" width="100">
            <template #default="{ row }">
              <el-tag :type="parseFloat(row.successRate) >= 95 ? 'success' : 'warning'">
                {{ row.successRate }}%
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="type" label="主要用途" />
        </el-table>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import * as echarts from 'echarts'
import { spaceDataApi } from '@/api'

const chartRef = ref(null)
let chartInstance = null
const rocketData = ref([])

const initChart = async () => {
  if (!chartRef.value) return
  
  try {
    const response = await spaceDataApi.getLongMarchRockets()
    rocketData.value = response.data
    
    chartInstance = echarts.init(chartRef.value)
    
    const option = {
      tooltip: {
        trigger: 'axis',
        axisPointer: { type: 'shadow' }
      },
      grid: {
        left: '3%',
        right: '4%',
        bottom: '3%',
        containLabel: true
      },
      xAxis: {
        type: 'value',
        name: '发射次数',
        axisLine: { lineStyle: { color: '#3d4c5f' } },
        axisLabel: { color: '#b8c5d6' },
        splitLine: { lineStyle: { color: '#2a3544', type: 'dashed' } }
      },
      yAxis: {
        type: 'category',
        data: response.data.map(item => item.name).reverse(),
        axisLine: { lineStyle: { color: '#3d4c5f' } },
        axisLabel: { color: '#b8c5d6' }
      },
      series: [
        {
          name: '总发射次数',
          type: 'bar',
          stack: 'total',
          data: response.data.map(item => ({
            value: item.totalLaunches,
            itemStyle: {
              color: new echarts.graphic.LinearGradient(0, 0, 1, 0, [
                { offset: 0, color: '#4fc3f7' },
                { offset: 1, color: '#0288d1' }
              ])
            }
          })).reverse()
        },
        {
          name: '失败次数',
          type: 'bar',
          stack: 'total',
          data: response.data.map(item => ({
            value: item.totalLaunches - item.successfulLaunches,
            itemStyle: {
              color: '#e57373'
            }
          })).reverse()
        }
      ]
    }
    
    chartInstance.setOption(option)
  } catch (error) {
    console.error('加载数据失败:', error)
  }
}

onMounted(() => {
  initChart()
  
  window.addEventListener('resize', () => {
    chartInstance?.resize()
  })
})
</script>

<style lang="scss" scoped>
.page-container {
  height: calc(100vh - 40px);
  padding: 20px;
}

.rocket-table {
  margin-top: 30px;
  
  h3 {
    color: #4fc3f7;
    margin-bottom: 16px;
  }
  
  :deep(.el-table) {
    background-color: transparent;
    
    .el-table__body tr {
      background-color: rgba(42, 53, 68, 0.3);
      
      &:hover {
        background-color: rgba(42, 53, 68, 0.6);
      }
    }
    
    td, th {
      border-bottom-color: #2a3544;
      color: #e0e6ed;
    }
  }
}
</style>
