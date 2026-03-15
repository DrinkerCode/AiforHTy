import axios from 'axios'

const request = axios.create({
  baseURL: '/api',
  timeout: 60000
})

// AI 问答接口
export const chatApi = {
  // 发送问题获取 AI 回答
  sendQuestion(data) {
    return request.post('/chat', data)
  }
}

// 航天数据接口
export const spaceDataApi = {
  // 历年发射统计
  getYearlyLaunches() {
    return request.get('/data/launches/yearly')
  },
  
  // 各国累计发射次数
  getCumulativeLaunches() {
    return request.get('/data/launches/cumulative')
  },
  
  // 月度发射趋势
  getMonthlyLaunches() {
    return request.get('/data/launches/monthly')
  },
  
  // 长征系列火箭
  getLongMarchRockets() {
    return request.get('/data/rockets/longmarch')
  },
  
  // 商业航天公司
  getCommercialCompanies() {
    return request.get('/data/commercial/companies')
  },
  
  // 空间站建设时间线
  getSpaceStationTimeline() {
    return request.get('/data/spacestation/timeline')
  },
  
  // 深空探测任务
  getDeepSpaceMissions() {
    return request.get('/data/deep-space/missions')
  },
  
  // 卫星分类统计
  getSatelliteCategories() {
    return request.get('/data/satellites/categories')
  },
  
  // 北斗星座构成
  getBeidouConstellation() {
    return request.get('/data/beidou/constellation')
  },
  
  // 发射场分布
  getLaunchSites() {
    return request.get('/data/launch-sites')
  }
}

export default request
