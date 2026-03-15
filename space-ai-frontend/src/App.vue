<template>
  <div id="app">
    <el-container class="layout-container">
      <!-- 侧边栏 -->
      <el-aside width="240px" class="sidebar">
        <div class="logo">
          <el-icon :size="32"><Rocket /></el-icon>
          <span>航天 AI 系统</span>
        </div>
        
        <el-menu
          :default-active="activeMenu"
          background-color="#1a2332"
          text-color="#b8c5d6"
          active-text-color="#4fc3f7"
          router
        >
          <el-menu-item index="/">
            <el-icon><ChatDotRound /></el-icon>
            <span>AI 问答</span>
          </el-menu-item>
          
          <el-sub-menu index="launches">
            <template #title>
              <el-icon><DataLine /></el-icon>
              <span>发射数据</span>
            </template>
            <el-menu-item index="/launches/yearly">历年发射统计</el-menu-item>
            <el-menu-item index="/launches/cumulative">各国累计对比</el-menu-item>
            <el-menu-item index="/launches/monthly">月度发射趋势</el-menu-item>
          </el-sub-menu>
          
          <el-sub-menu index="rockets">
            <template #title>
              <el-icon><Platform /></el-icon>
              <span>火箭数据</span>
            </template>
            <el-menu-item index="/rockets/longmarch">长征系列</el-menu-item>
            <el-menu-item index="/rockets/commercial">商业航天</el-menu-item>
          </el-sub-menu>
          
          <el-sub-menu index="missions">
            <template #title>
              <el-icon><Connection /></el-icon>
              <span>任务数据</span>
            </template>
            <el-menu-item index="/missions/spacestation">空间站建设</el-menu-item>
            <el-menu-item index="/missions/deepspace">深空探测</el-menu-item>
          </el-sub-menu>
          
          <el-sub-menu index="satellites">
            <template #title>
              <el-icon><Coordinate /></el-icon>
              <span>卫星导航</span>
            </template>
            <el-menu-item index="/satellites/categories">卫星分类</el-menu-item>
            <el-menu-item index="/satellites/beidou">北斗系统</el-menu-item>
          </el-sub-menu>
          
          <el-menu-item index="/launch-sites">
            <el-icon><Location /></el-icon>
            <span>发射场分布</span>
          </el-menu-item>
          
          <el-divider border-style="dashed" />
          
          <el-sub-menu index="admin">
            <template #title>
              <el-icon><Setting /></el-icon>
              <span>数据管理</span>
            </template>
            <el-menu-item index="/admin/launches">发射记录管理</el-menu-item>
            <el-menu-item index="/admin/satellites">卫星数据管理</el-menu-item>
          </el-sub-menu>
        </el-menu>
      </el-aside>
      
      <!-- 主内容区 -->
      <el-main class="main-content">
        <router-view />
      </el-main>
    </el-container>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()

const activeMenu = computed(() => {
  const path = route.path
  if (path === '/') return '/'
  if (path.includes('/launches/')) return path
  if (path.includes('/rockets/')) return path
  if (path.includes('/missions/')) return path
  if (path.includes('/satellites/')) return path
  if (path === '/launch-sites') return '/launch-sites'
  return path
})
</script>

<style lang="scss" scoped>
.layout-container {
  height: 100vh;
}

.sidebar {
  background-color: #1a2332;
  border-right: 1px solid #2a3544;
  
  .logo {
    display: flex;
    align-items: center;
    gap: 12px;
    padding: 24px 20px;
    color: #4fc3f7;
    font-size: 20px;
    font-weight: bold;
    border-bottom: 1px solid #2a3544;
    
    .el-icon {
      flex-shrink: 0;
    }
  }
  
  .el-menu {
    border-right: none;
  }
}

.main-content {
  background: linear-gradient(135deg, #0d1821 0%, #1a2332 100%);
  padding: 20px;
  overflow-y: auto;
}
</style>
