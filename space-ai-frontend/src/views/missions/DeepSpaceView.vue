<template>
  <div class="page-container fade-in">
    <el-card class="space-card">
      <template #header>
        <div class="card-title">
          <el-icon><Planet /></el-icon>
          <span>深空探测任务分布</span>
        </div>
      </template>
      
      <div class="mission-layout">
        <div ref="chartRef" class="echart-container"></div>
        
        <div class="mission-list">
          <h3>任务列表</h3>
          <div 
            v-for="(mission, index) in missionData" 
            :key="index"
            class="mission-item"
            :class="{ planned: mission.status === 0 }"
          >
            <div class="mission-info">
              <div class="mission-name">{{ mission.name }}</div>
              <div class="mission-target">
                <el-tag size="small" type="info">{{ mission.target }}</el-tag>
              </div>
            </div>
            <div class="mission-details">
              <span class="mission-year">{{ mission.year }}年</span>
              <span class="mission-type">{{ mission.missionType }}</span>
              <el-tag 
                size="small" 
                :type="mission.status === 100 ? 'success' : 'warning'"
              >
                {{ mission.status === 100 ? '已完成' : '计划中' }}
              </el-tag>
            </div>
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
const missionData = ref([])

const initChart = async () => {
  if (!chartRef.value) return
  
  try {
    const response = await spaceDataApi.getDeepSpaceMissions()
    missionData.value = response.data
    
    // 按目标天体统计
    const targetCount = {}
    response.data.forEach(mission => {
      if (mission.status === 100) {
        targetCount[mission.target] = (targetCount[mission.target] || 0) + 1
      }
    })
    
    const pieData = Object.entries(targetCount).map(([name, value]) => ({
      name,
      value,
      itemStyle: {
        color: ['#4fc3f7', '#81c784', '#ffb74d', '#e57373', '#ba68c8'][Object.keys(targetCount).indexOf(name) % 5]
      }
    }))
    
    chartInstance = echarts.init(chartRef.value)
    
    const option = {
      title: {
        text: '已完成任务 - 目标天体分布',
        left: 'center',
        textStyle: { color: '#4fc3f7', fontSize: 16 }
      },
      tooltip: {
        trigger: 'item',
        formatter: '{b}: {c}个任务'
      },
      series: [
        {
          name: '探测任务',
          type: 'pie',
          radius: ['40%', '70%'],
          center: ['50%', '55%'],
          avoidLabelOverlap: false,
          itemStyle: {
            borderRadius: 8,
            borderColor: '#1a2332',
            borderWidth: 2
          },
          label: {
            show: true,
            formatter: '{b}\n{c}个',
            color: '#e0e6ed'
          },
          emphasis: {
            label: {
              show: true,
              fontSize: 14,
              fontWeight: 'bold'
            }
          },
          data: pieData
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

.mission-layout {
  display: flex;
  gap: 30px;
}

.echart-container {
  flex: 1;
  height: 500px;
}

.mission-list {
  width: 400px;
  
  h3 {
    color: #4fc3f7;
    margin-bottom: 16px;
  }
}

.mission-item {
  background: rgba(42, 53, 68, 0.5);
  border-radius: 8px;
  padding: 14px 16px;
  margin-bottom: 12px;
  border: 1px solid #2a3544;
  transition: all 0.3s;
  
  &:hover {
    border-color: #4fc3f7;
    transform: translateX(4px);
  }
  
  &.planned {
    opacity: 0.7;
    border-style: dashed;
  }
  
  .mission-info {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 8px;
    
    .mission-name {
      font-size: 16px;
      font-weight: 600;
      color: #e0e6ed;
    }
  }
  
  .mission-details {
    display: flex;
    gap: 12px;
    align-items: center;
    font-size: 13px;
    color: #b8c5d6;
    
    .mission-year {
      color: #4fc3f7;
      font-weight: 500;
    }
    
    .mission-type {
      flex: 1;
    }
  }
}
</style>
