<template>
  <div class="page-container fade-in">
    <el-card class="space-card">
      <template #header>
        <div class="card-title">
          <el-icon><Histogram /></el-icon>
          <span>卫星应用分类统计</span>
        </div>
      </template>
      
      <div class="chart-layout">
        <div ref="chartRef" class="echart-container"></div>
        
        <div class="category-list">
          <h3>分类详情</h3>
          <div 
            v-for="(item, index) in categoryData" 
            :key="index"
            class="category-item"
          >
            <div class="category-header">
              <span class="category-name">{{ item.category }}</span>
              <span class="category-count">{{ item.count }}颗</span>
            </div>
            <el-progress 
              :percentage="item.percentage" 
              :color="getCategoryColor(index)"
              :format="() => item.percentage + '%'"
            />
          </div>
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
const categoryData = ref([])

const colors = ['#4fc3f7', '#81c784', '#ffb74d', '#e57373', '#ba68c8']

const getCategoryColor = (index) => colors[index % colors.length]

const initChart = async () => {
  if (!chartRef.value) return
  
  try {
    const response = await spaceDataApi.getSatelliteCategories()
    categoryData.value = response.data
    
    chartInstance = echarts.init(chartRef.value)
    
    const option = {
      tooltip: {
        trigger: 'item',
        formatter: '{b}: {c}颗 ({d}%)'
      },
      legend: {
        orient: 'vertical',
        left: 'left',
        textStyle: { color: '#b8c5d6' }
      },
      series: [
        {
          name: '卫星数量',
          type: 'pie',
          radius: ['30%', '70%'],
          center: ['60%', '50%'],
          roseType: 'area',
          itemStyle: {
            borderRadius: 8,
            borderColor: '#1a2332',
            borderWidth: 2
          },
          label: {
            show: true,
            formatter: '{b}\n{c}颗',
            color: '#e0e6ed'
          },
          data: response.data.map((item, index) => ({
            name: item.category,
            value: item.count,
            itemStyle: { color: colors[index % colors.length] }
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

.chart-layout {
  display: flex;
  gap: 30px;
}

.echart-container {
  flex: 1;
  height: 500px;
}

.category-list {
  width: 350px;
  
  h3 {
    color: #4fc3f7;
    margin-bottom: 20px;
  }
}

.category-item {
  margin-bottom: 20px;
  
  .category-header {
    display: flex;
    justify-content: space-between;
    margin-bottom: 8px;
    
    .category-name {
      color: #e0e6ed;
      font-weight: 500;
    }
    
    .category-count {
      color: #4fc3f7;
      font-weight: 600;
    }
  }
}
</style>
