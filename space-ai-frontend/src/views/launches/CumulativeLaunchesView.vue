<template>
  <div class="page-container fade-in">
    <el-card class="space-card">
      <template #header>
        <div class="card-title">
          <el-icon><PieChart /></el-icon>
          <span>各国累计发射次数对比</span>
        </div>
      </template>
      
      <div class="chart-wrapper">
        <div ref="chartRef" class="echart-container"></div>
        
        <div class="data-info">
          <h3>数据说明</h3>
          <p>本图表展示了世界主要航天国家/地区的累计火箭发射次数统计。</p>
          <p>数据来源：公开航天发射记录整理（截至 2024 年）</p>
        </div>
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

const initChart = async () => {
  if (!chartRef.value) return
  
  try {
    const response = await spaceDataApi.getCumulativeLaunches()
    const data = response.data
    
    chartInstance = echarts.init(chartRef.value)
    
    const option = {
      tooltip: {
        trigger: 'item',
        formatter: '{b}: {c}次 ({d}%)'
      },
      legend: {
        orient: 'vertical',
        left: 'left',
        textStyle: { color: '#b8c5d6' }
      },
      series: [
        {
          name: '发射次数',
          type: 'pie',
          radius: ['40%', '70%'],
          center: ['60%', '50%'],
          avoidLabelOverlap: false,
          itemStyle: {
            borderRadius: 10,
            borderColor: '#1a2332',
            borderWidth: 2
          },
          label: {
            show: true,
            formatter: '{b}\n{c}次',
            color: '#e0e6ed'
          },
          emphasis: {
            label: {
              show: true,
              fontSize: 16,
              fontWeight: 'bold'
            }
          },
          labelLine: {
            show: true,
            lineStyle: { color: '#3d4c5f' }
          },
          data: data.map((item, index) => ({
            name: item.name,
            value: item.value,
            itemStyle: {
              color: [
                '#4fc3f7', '#81c784', '#ffb74d', 
                '#e57373', '#ba68c8', '#4db6ac', '#ffd54f'
              ][index % 7]
            }
          }))
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

.chart-wrapper {
  display: flex;
  gap: 20px;
}

.echart-container {
  flex: 1;
  height: 500px;
}

.data-info {
  width: 300px;
  padding: 20px;
  background: rgba(42, 53, 68, 0.5);
  border-radius: 8px;
  
  h3 {
    color: #4fc3f7;
    margin-bottom: 12px;
  }
  
  p {
    color: #b8c5d6;
    font-size: 14px;
    line-height: 1.8;
    margin-bottom: 8px;
  }
}
</style>
