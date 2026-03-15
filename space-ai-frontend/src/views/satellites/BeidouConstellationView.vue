<template>
  <div class="page-container fade-in">
    <el-card class="space-card">
      <template #header>
        <div class="card-title">
          <el-icon><Guide /></el-icon>
          <span>北斗导航系统星座构成</span>
        </div>
      </template>
      
      <div class="beidou-layout">
        <div ref="chartRef" class="echart-container"></div>
        
        <div class="orbit-info">
          <h3>轨道类型说明</h3>
          <div 
            v-for="(orbit, index) in orbitData" 
            :key="index"
            class="orbit-card"
          >
            <div class="orbit-header">
              <div class="orbit-icon" :style="{ background: getOrbitColor(index) }">
                <el-icon :size="24"><Location /></el-icon>
              </div>
              <div class="orbit-name">{{ orbit.orbitType.split('（')[0] }}</div>
            </div>
            <div class="orbit-details">
              <div class="orbit-full">{{ orbit.orbitType }}</div>
              <div class="orbit-stats">
                <span>卫星数量：<strong>{{ orbit.count }}颗</strong></span>
                <span>轨道高度：<strong>{{ orbit.altitude }}</strong></span>
              </div>
            </div>
          </div>
          
          <div class="beidou-summary">
            <h4>北斗三号系统概况</h4>
            <p>北斗卫星导航系统（BDS）是中国自主建设、独立运行的全球卫星导航系统。</p>
            <p>系统由空间段、地面段和用户段三部分组成，可提供全球全天候、高精度的定位、导航和授时服务。</p>
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
const orbitData = ref([])

const orbitColors = ['#4fc3f7', '#81c784', '#ffb74d', '#e57373']

const getOrbitColor = (index) => orbitColors[index % orbitColors.length]

const initChart = async () => {
  if (!chartRef.value) return
  
  try {
    const response = await spaceDataApi.getBeidouConstellation()
    orbitData.value = response.data
    
    chartInstance = echarts.init(chartRef.value)
    
    const option = {
      title: {
        text: '北斗星座卫星分布',
        left: 'center',
        textStyle: { color: '#4fc3f7', fontSize: 16 }
      },
      tooltip: {
        trigger: 'item',
        formatter: '{b}: {c}颗'
      },
      series: [
        {
          name: '卫星数量',
          type: 'pie',
          radius: ['40%', '70%'],
          center: ['50%', '55%'],
          avoidLabelOverlap: false,
          itemStyle: {
            borderRadius: 10,
            borderColor: '#1a2332',
            borderWidth: 2
          },
          label: {
            show: true,
            formatter: '{ab|{b}}\n{value|{c}颗}',
            rich: {
              ab: {
                fontSize: 14,
                fontWeight: 'bold',
                color: '#e0e6ed',
                padding: [4, 0]
              },
              value: {
                fontSize: 12,
                color: '#4fc3f7'
              }
            }
          },
          emphasis: {
            label: {
              show: true,
              fontSize: 16,
              fontWeight: 'bold'
            }
          },
          data: response.data.map((item, index) => ({
            name: item.orbitType.split('（')[0],
            value: item.count,
            itemStyle: { color: orbitColors[index % orbitColors.length] }
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

.beidou-layout {
  display: flex;
  gap: 30px;
}

.echart-container {
  flex: 1;
  height: 500px;
}

.orbit-info {
  width: 400px;
  
  h3 {
    color: #4fc3f7;
    margin-bottom: 20px;
  }
}

.orbit-card {
  background: rgba(42, 53, 68, 0.5);
  border-radius: 8px;
  padding: 16px;
  margin-bottom: 16px;
  border: 1px solid #2a3544;
  transition: all 0.3s;
  
  &:hover {
    border-color: #4fc3f7;
    transform: translateX(4px);
  }
  
  .orbit-header {
    display: flex;
    align-items: center;
    gap: 12px;
    margin-bottom: 12px;
    
    .orbit-icon {
      width: 44px;
      height: 44px;
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
      color: #0d1821;
    }
    
    .orbit-name {
      font-size: 16px;
      font-weight: 600;
      color: #e0e6ed;
    }
  }
  
  .orbit-details {
    .orbit-full {
      font-size: 13px;
      color: #b8c5d6;
      margin-bottom: 8px;
    }
    
    .orbit-stats {
      display: flex;
      justify-content: space-between;
      font-size: 13px;
      color: #78909c;
      
      strong {
        color: #4fc3f7;
      }
    }
  }
}

.beidou-summary {
  margin-top: 24px;
  padding: 16px;
  background: rgba(79, 195, 247, 0.1);
  border-radius: 8px;
  border-left: 3px solid #4fc3f7;
  
  h4 {
    color: #4fc3f7;
    margin-bottom: 12px;
  }
  
  p {
    font-size: 13px;
    color: #b8c5d6;
    line-height: 1.8;
    margin-bottom: 8px;
    
    &:last-child {
      margin-bottom: 0;
    }
  }
}
</style>
