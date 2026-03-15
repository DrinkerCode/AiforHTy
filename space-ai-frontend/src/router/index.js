import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    name: 'Chat',
    component: () => import('@/views/ChatView.vue')
  },
  {
    path: '/launches/yearly',
    name: 'YearlyLaunches',
    component: () => import('@/views/launches/YearlyLaunchesView.vue')
  },
  {
    path: '/launches/cumulative',
    name: 'CumulativeLaunches',
    component: () => import('@/views/launches/CumulativeLaunchesView.vue')
  },
  {
    path: '/launches/monthly',
    name: 'MonthlyLaunches',
    component: () => import('@/views/launches/MonthlyLaunchesView.vue')
  },
  {
    path: '/rockets/longmarch',
    name: 'LongMarchRockets',
    component: () => import('@/views/rockets/LongMarchRocketsView.vue')
  },
  {
    path: '/rockets/commercial',
    name: 'CommercialCompanies',
    component: () => import('@/views/rockets/CommercialCompaniesView.vue')
  },
  {
    path: '/missions/spacestation',
    name: 'SpaceStation',
    component: () => import('@/views/missions/SpaceStationView.vue')
  },
  {
    path: '/missions/deepspace',
    name: 'DeepSpace',
    component: () => import('@/views/missions/DeepSpaceView.vue')
  },
  {
    path: '/satellites/categories',
    name: 'SatelliteCategories',
    component: () => import('@/views/satellites/SatelliteCategoriesView.vue')
  },
  {
    path: '/satellites/beidou',
    name: 'BeidouConstellation',
    component: () => import('@/views/satellites/BeidouConstellationView.vue')
  },
  {
    path: '/launch-sites',
    name: 'LaunchSites',
    component: () => import('@/views/LaunchSitesView.vue')
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
