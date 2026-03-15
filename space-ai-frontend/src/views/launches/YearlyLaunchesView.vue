<template>
  <div class="page-container fade-in">
    <el-card class="space-card">
      <template #header>
        <div class="card-title">
          <el-icon><TrendCharts /></el-icon>
          <span>历年火箭发射统计 (2014-2024)</span>
        </div>
      </template>
      
      <div ref="chartRef" class="echart-container"></div>
      
      <div class="data-summary">
        <el-row :gutter="20">
          <el-col :span="6" v-for="(item, index) in summaryData" :key="index">
            <el-statistic :title="item.title" :value="item.value">
              <template #prefix>
                <el-icon :color="item.color"><Rocket /></el-icon>
              </template>
            </el-statistic>
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

const summaryData = ref([
  { title: '中国总发射', value: 0, color: '#4fc3f7' },
  { title: '美国总发射', value: 0, color: '#81c784' },
  { title: '俄罗斯总发射', value: 0, color: '#ffb74d' },
  { title: '其他国家', value: 0, color: '#e57373' }
])

const initChart = async () => {
  if (!chartRef.value) return
  
  try {
    const response = await spaceDataApi.getYearlyLaunches()
    const data = response.data
    
    chartInstance = echarts.init(chartRef.value)
    
    const option = {
      tooltip: {
        trigger: 'axis',
        axisPointer: { type: 'shadow' }
      },
      legend: {
        data: ['中国', '美国', '俄罗斯', '其他国家'],
        textStyle: { color: '#b8c5d6' },
        top: 0
      },
      grid: {
        left: '3%',
        right: '4%',
        bottom: '3%',
        containLabel: true
      },
      xAxis: {
        type: 'category',
        data: data.years,
        axisLine: { lineStyle: { color: '#3d4c5f' } },
        axisLabel: { color: '#b8c5d6' }
      },
      yAxis: {
        type: 'value',
        name: '发射次数',
        axisLine: { lineStyle: { color: '#3d4c5f' } },
        axisLabel: { color: '#b8c5d6' },
        splitLine: { lineStyle: { color: '#2a3544', type: 'dashed' } }
      },
      series: [
        {
          name: '中国',
          type: 'bar',
          data: data.china,
          itemStyle: {
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
              { offset: 0, color: '#4fc3f7' },
              { offset: 1, color: '#0288d1' }
            ])
          }
        },
        {
          name: '美国',
          type: 'bar',
          data: data.usa,
          itemStyle: {
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
              { offset: 0, color: '#81c784' },
              { offset: 1, color: '#388e3c' }
            ])
          }
        },
        {
          name: '俄罗斯',
          type: 'bar',
          data: data.russia,
          itemStyle: {
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
              { offset: 0, color: '#ffb74d' },
              { offset: 1, color: '#f57c00' }
            ])
          }
        },
        {
          name: '其他国家',
          type: 'bar',
          data: data.other,
          itemStyle: {
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
              { offset: 0, color: '#e57373' },
              { offset: 1, color: '#c62828' }
            ])
          }
        }
      ]
    }
    
    chartInstance.setOption(option)
    
    // 计算统计数据
    const totalChina = data.china.reduce((a, b) => a + b, 0)
    const totalUsa = data.usa.reduce((a, b) => a + b, 0)
    const totalRussia = data.russia.reduce((a, b) => a + b, 0)
    const totalOther = data.other.reduce((a, b) => a + b, 0)
    
    summaryData.value = [
      { title: '中国总发射', value: totalChina, color: '#4fc3f7' },
      { title: '美国总发射', value: totalUsa, color: '#81c784' },
      { title: '俄罗斯总发射', value: totalRussia, color: '#ffb74d' },
      { title: '其他国家', value: totalOther, color: '#e57373' }
    ]
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

.data-summary {
  margin-top: 30px;
  padding-top: 20px;
  border-top: 1px solid #2a3544;
}
</style>
