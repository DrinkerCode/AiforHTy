<template>
  <div class="page-container fade-in">
    <el-card class="space-card">
      <template #header>
        <div class="card-title">
          <el-icon><Monitor /></el-icon>
          <span>商业航天公司对比</span>
        </div>
      </template>
      
      <div ref="chartRef" class="echart-container"></div>
      
      <div class="company-grid">
        <el-row :gutter="20">
          <el-col :span="8" v-for="(company, index) in companyData" :key="index">
            <div class="company-card">
              <div class="company-name">{{ company.name }}</div>
              <div class="company-country">{{ company.country }}</div>
              <div class="company-stats">
                <div class="stat-item">
                  <span class="label">发射次数</span>
                  <span class="value">{{ company.launchCount }}</span>
                </div>
                <div class="stat-item">
                  <span class="label">成功率</span>
                  <span class="value" :class="{ success: company.successRate >= 90 }">{{ company.successRate }}%</span>
                </div>
              </div>
              <div class="company-rockets">{{ company.rockets }}</div>
            </div>
          </el-col>
        </el-row>
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
const companyData = ref([])

const initChart = async () => {
  if (!chartRef.value) return
  
  try {
    const response = await spaceDataApi.getCommercialCompanies()
    companyData.value = response.data
    
    chartInstance = echarts.init(chartRef.value)
    
    const option = {
      tooltip: {
        trigger: 'axis',
        axisPointer: { type: 'shadow' }
      },
      legend: {
        data: ['发射次数', '成功率 (%)'],
        textStyle: { color: '#b8c5d6' }
      },
      grid: {
        left: '3%',
        right: '4%',
        bottom: '3%',
        containLabel: true
      },
      xAxis: {
        type: 'category',
        data: response.data.map(item => item.name),
        axisLine: { lineStyle: { color: '#3d4c5f' } },
        axisLabel: { 
          color: '#b8c5d6',
          rotate: 15
        }
      },
      yAxis: [
        {
          type: 'value',
          name: '发射次数',
          axisLine: { lineStyle: { color: '#3d4c5f' } },
          axisLabel: { color: '#b8c5d6' },
          splitLine: { lineStyle: { color: '#2a3544', type: 'dashed' } }
        },
        {
          type: 'value',
          name: '成功率 (%)',
          min: 0,
          max: 100,
          axisLine: { lineStyle: { color: '#3d4c5f' } },
          axisLabel: { color: '#b8c5d6' },
          splitLine: { show: false }
        }
      ],
      series: [
        {
          name: '发射次数',
          type: 'bar',
          data: response.data.map(item => ({
            value: item.launchCount,
            itemStyle: {
              color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                { offset: 0, color: '#4fc3f7' },
                { offset: 1, color: '#0288d1' }
              ])
            }
          }))
        },
        {
          name: '成功率 (%)',
          type: 'line',
          yAxisIndex: 1,
          data: response.data.map(item => item.successRate),
          symbol: 'circle',
          symbolSize: 8,
          itemStyle: { color: '#ffb74d' },
          lineStyle: { width: 2, color: '#ffb74d' }
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

.company-grid {
  margin-top: 30px;
}

.company-card {
  background: rgba(42, 53, 68, 0.5);
  border-radius: 8px;
  padding: 16px;
  margin-bottom: 16px;
  border: 1px solid #2a3544;
  transition: all 0.3s;
  
  &:hover {
    transform: translateY(-4px);
    border-color: #4fc3f7;
    box-shadow: 0 4px 20px rgba(79, 195, 247, 0.2);
  }
  
  .company-name {
    font-size: 16px;
    font-weight: 600;
    color: #4fc3f7;
    margin-bottom: 4px;
  }
  
  .company-country {
    font-size: 12px;
    color: #78909c;
    margin-bottom: 12px;
  }
  
  .company-stats {
    display: flex;
    justify-content: space-between;
    margin-bottom: 8px;
    
    .stat-item {
      display: flex;
      flex-direction: column;
      
      .label {
        font-size: 12px;
        color: #b8c5d6;
        margin-bottom: 4px;
      }
      
      .value {
        font-size: 20px;
        font-weight: 600;
        color: #e0e6ed;
        
        &.success {
          color: #81c784;
        }
      }
    }
  }
  
  .company-rockets {
    font-size: 12px;
    color: #b8c5d6;
    padding-top: 8px;
    border-top: 1px solid #2a3544;
  }
}
</style>
