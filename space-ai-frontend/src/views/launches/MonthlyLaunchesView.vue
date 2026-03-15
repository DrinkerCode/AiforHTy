<template>
  <div class="page-container fade-in">
    <el-card class="space-card">
      <template #header>
        <div class="card-title">
          <el-icon><DataAnalysis /></el-icon>
          <span>月度发射趋势 (2024 年)</span>
        </div>
      </template>
      
      <div ref="chartRef" class="echart-container"></div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import * as echarts from 'echarts'
import { spaceDataApi } from '@/api'

const chartRef = ref(null)
let chartInstance = null

const initChart = async () => {
  if (!chartRef.value) return
  
  try {
    const response = await spaceDataApi.getMonthlyLaunches()
    const data = response.data
    
    chartInstance = echarts.init(chartRef.value)
    
    const option = {
      tooltip: {
        trigger: 'axis',
        formatter: '{b}: {c}次发射'
      },
      grid: {
        left: '3%',
        right: '4%',
        bottom: '3%',
        containLabel: true
      },
      xAxis: {
        type: 'category',
        data: data.months,
        axisLine: { lineStyle: { color: '#3d4c5f' } },
        axisLabel: { color: '#b8c5d6' }
      },
      yAxis: {
        type: 'value',
        name: '发射次数',
        minInterval: 1,
        axisLine: { lineStyle: { color: '#3d4c5f' } },
        axisLabel: { color: '#b8c5d6' },
        splitLine: { lineStyle: { color: '#2a3544', type: 'dashed' } }
      },
      series: [
        {
          name: '发射次数',
          type: 'line',
          smooth: true,
          data: data.launches,
          symbol: 'circle',
          symbolSize: 10,
          itemStyle: {
            color: '#4fc3f7'
          },
          lineStyle: {
            width: 3,
            color: new echarts.graphic.LinearGradient(0, 0, 1, 0, [
              { offset: 0, color: '#4fc3f7' },
              { offset: 1, color: '#29b6f6' }
            ])
          },
          areaStyle: {
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
              { offset: 0, color: 'rgba(79, 195, 247, 0.3)' },
              { offset: 1, color: 'rgba(79, 195, 247, 0.05)' }
            ])
          },
          markPoint: {
            data: [
              { type: 'max', name: '最大值' },
              { type: 'min', name: '最小值' }
            ],
            itemStyle: { color: '#ffb74d' }
          }
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
</style>
