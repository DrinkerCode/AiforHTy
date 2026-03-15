<template>
  <div class="page-container fade-in">
    <el-card class="space-card">
      <template #header>
        <div class="card-title">
          <el-icon><Location /></el-icon>
          <span>全球航天发射场分布</span>
        </div>
      </template>
      
      <div ref="chartRef" class="echart-container"></div>
      
      <div class="sites-grid">
        <el-row :gutter="20">
          <el-col :span="6" v-for="(site, index) in sitesData" :key="index">
            <div class="site-card">
              <div class="site-header">
                <div class="site-flag">{{ site.country }}</div>
                <div class="site-rank">#{{ index + 1 }}</div>
              </div>
              <div class="site-name">{{ site.name }}</div>
              <div class="site-count">
                <span class="count-number">{{ site.launchCount }}</span>
                <span class="count-label">次发射</span>
              </div>
              <div class="site-missions">{{ site.mainMissions }}</div>
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
const sitesData = ref([])

const initChart = async () => {
  if (!chartRef.value) return
  
  try {
    const response = await spaceDataApi.getLaunchSites()
    sitesData.value = response.data
    
    chartInstance = echarts.init(chartRef.value)
    
    // 按国家分组统计
    const countryStats = {}
    response.data.forEach(site => {
      if (!countryStats[site.country]) {
        countryStats[site.country] = { name: site.country, value: 0, sites: [] }
      }
      countryStats[site.country].value += site.launchCount
      countryStats[site.country].sites.push(site.name)
    })
    
    const pieData = Object.values(countryStats).map((item, index) => ({
      name: item.name,
      value: item.value,
      itemStyle: {
        color: ['#4fc3f7', '#81c784', '#ffb74d', '#e57373', '#ba68c8', '#4db6ac'][index % 6]
      }
    }))
    
    const option = {
      title: {
        text: '各国发射次数占比',
        left: 'center',
        textStyle: { color: '#4fc3f7', fontSize: 16 }
      },
      tooltip: {
        trigger: 'item',
        formatter: '{b}: {c}次\n发射场：{data.sites}'
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
          center: ['55%', '50%'],
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
              fontSize: 14,
              fontWeight: 'bold'
            }
          },
          data: pieData.map((item, index) => ({
            ...item,
            sites: Object.values(countryStats)[index].sites.join('、')
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
  overflow-y: auto;
}

.echart-container {
  height: 400px;
  margin-bottom: 30px;
}

.sites-grid {
  margin-top: 20px;
}

.site-card {
  background: rgba(42, 53, 68, 0.5);
  border-radius: 8px;
  padding: 16px;
  margin-bottom: 16px;
  border: 1px solid #2a3544;
  transition: all 0.3s;
  
  &:hover {
    border-color: #4fc3f7;
    transform: translateY(-4px);
    box-shadow: 0 8px 24px rgba(79, 195, 247, 0.2);
  }
  
  .site-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 12px;
    
    .site-flag {
      font-size: 12px;
      color: #78909c;
      padding: 4px 8px;
      background: rgba(79, 195, 247, 0.1);
      border-radius: 4px;
    }
    
    .site-rank {
      font-size: 14px;
      font-weight: 600;
      color: #ffb74d;
    }
  }
  
  .site-name {
    font-size: 16px;
    font-weight: 600;
    color: #e0e6ed;
    margin-bottom: 12px;
  }
  
  .site-count {
    display: flex;
    align-items: baseline;
    gap: 6px;
    margin-bottom: 8px;
    
    .count-number {
      font-size: 28px;
      font-weight: 700;
      color: #4fc3f7;
    }
    
    .count-label {
      font-size: 13px;
      color: #b8c5d6;
    }
  }
  
  .site-missions {
    font-size: 12px;
    color: #78909c;
    padding-top: 8px;
    border-top: 1px dashed #2a3544;
    line-height: 1.5;
  }
}
</style>
