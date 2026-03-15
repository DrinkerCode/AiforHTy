<template>
  <div class="page-container fade-in">
    <el-card class="space-card">
      <template #header>
        <div class="card-title">
          <el-icon><Connection /></el-icon>
          <span>中国空间站建设时间线</span>
        </div>
      </template>
      
      <div class="timeline-container">
        <div class="timeline">
          <div 
            v-for="(event, index) in timelineData" 
            :key="index"
            class="timeline-item"
          >
            <div class="timeline-marker">
              <div class="marker-dot"></div>
              <div v-if="index !== timelineData.length - 1" class="marker-line"></div>
            </div>
            <div class="timeline-content">
              <div class="timeline-date">{{ event.date }}</div>
              <div class="timeline-event">{{ event.event }}</div>
              <div class="timeline-description">{{ event.description }}</div>
            </div>
          </div>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { spaceDataApi } from '@/api'

const timelineData = ref([])

const loadTimeline = async () => {
  try {
    const response = await spaceDataApi.getSpaceStationTimeline()
    timelineData.value = response.data
  } catch (error) {
    console.error('加载数据失败:', error)
  }
}

onMounted(() => {
  loadTimeline()
})
</script>

<style lang="scss" scoped>
.page-container {
  height: calc(100vh - 40px);
  padding: 20px;
  overflow-y: auto;
}

.timeline-container {
  padding: 20px 0;
}

.timeline {
  position: relative;
  max-width: 900px;
  margin: 0 auto;
}

.timeline-item {
  display: flex;
  margin-bottom: 30px;
  position: relative;
  
  &:last-child {
    margin-bottom: 0;
  }
}

.timeline-marker {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-right: 20px;
  
  .marker-dot {
    width: 16px;
    height: 16px;
    border-radius: 50%;
    background: linear-gradient(135deg, #4fc3f7 0%, #0288d1 100%);
    box-shadow: 0 0 12px rgba(79, 195, 247, 0.5);
    z-index: 1;
    flex-shrink: 0;
  }
  
  .marker-line {
    width: 2px;
    flex: 1;
    background: linear-gradient(to bottom, #4fc3f7, #2a3544);
    min-height: 60px;
    margin-top: 8px;
  }
}

.timeline-content {
  flex: 1;
  background: rgba(42, 53, 68, 0.5);
  border-radius: 8px;
  padding: 16px 20px;
  border: 1px solid #2a3544;
  transition: all 0.3s;
  
  &:hover {
    border-color: #4fc3f7;
    transform: translateX(8px);
    box-shadow: 0 4px 20px rgba(79, 195, 247, 0.15);
  }
  
  .timeline-date {
    font-size: 14px;
    color: #4fc3f7;
    font-weight: 600;
    margin-bottom: 8px;
  }
  
  .timeline-event {
    font-size: 18px;
    color: #e0e6ed;
    font-weight: 600;
    margin-bottom: 8px;
  }
  
  .timeline-description {
    font-size: 14px;
    color: #b8c5d6;
    line-height: 1.6;
  }
}
</style>
