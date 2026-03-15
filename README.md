# 航天知识 AI 问答系统

基于 Spring Boot + Vue 的航天知识 AI 问答与数据可视化系统，集成阿里云通义千问大模型。

## 技术栈

### 后端
- **Spring Boot 3.2.0** - Java 17
- **阿里云 DashScope API** - 通义千问大模型 (Qwen-Max)
- **Maven** - 依赖管理
- **Lombok** - 代码简化

### 前端
- **Vue 3.4** - Composition API
- **Vite 5.0** - 构建工具
- **Element Plus** - UI 组件库
- **ECharts 5.4** - 数据可视化
- **Vue Router 4** - 路由管理
- **Pinia** - 状态管理
- **Axios** - HTTP 请求

## 功能特性

### AI 问答
- 集成阿里云通义千问大模型
- 专注于航天领域知识问答
- 支持多轮对话
- 实时流式响应

### 数据可视化（10 个页面）
1. **历年发射统计** - 2014-2024 年各国火箭发射对比
2. **各国累计对比** - 历史累计发射次数饼图
3. **月度发射趋势** - 当年月度发射折线图
4. **长征系列火箭** - 中国长征火箭发射记录
5. **商业航天公司** - 全球商业航天企业对比
6. **空间站建设** - 中国空间站建设时间线
7. **深空探测任务** - 嫦娥、天问等深空探测任务
8. **卫星分类统计** - 卫星应用类型分布
9. **北斗星座构成** - 北斗导航系统轨道分布
10. **发射场分布** - 全球主要航天发射场

### 主题设计
- 复古航天蓝配色方案
- 深色模式界面
- 响应式布局
- 流畅动画效果

## 快速开始

### 环境要求
- JDK 17+
- Node.js 18+
- Maven 3.8+

### 后端启动

```bash
cd space-ai-backend

# 配置阿里云 API Key
export ALIYUN_API_KEY=your-api-key-here

# 或使用 .env 文件
echo "ALIYUN_API_KEY=your-api-key-here" > .env

# 编译并运行
mvn clean package
java -jar target/space-ai-backend-1.0.0.jar
```

后端服务将在 `http://localhost:8080/api` 启动

### 前端启动

```bash
cd space-ai-frontend

# 安装依赖
npm install

# 开发模式运行
npm run dev

# 生产构建
npm run build
```

前端服务将在 `http://localhost:5173` 启动

## 项目结构

```
space-ai-system/
├── space-ai-backend/          # 后端服务
│   ├── src/main/java/com/space/ai/
│   │   ├── config/           # 配置类
│   │   ├── controller/       # 控制器
│   │   ├── model/            # 数据模型
│   │   ├── service/          # 服务层
│   │   └── SpaceAiApplication.java
│   └── src/main/resources/
│       └── application.yml   # 配置文件
│
└── space-ai-frontend/         # 前端应用
    ├── src/
    │   ├── api/              # API 接口
    │   ├── assets/styles/    # 样式文件
    │   ├── components/       # 组件
    │   ├── router/           # 路由配置
    │   ├── views/            # 页面视图
    │   ├── App.vue           # 根组件
    │   └── main.js           # 入口文件
    └── package.json
```

## API 接口

### AI 问答
```
POST /api/chat
Body: { "question": "你的问题" }
Response: { "answer": "AI 回答", "success": true }
```

### 数据接口
```
GET /api/data/launches/yearly      # 历年发射统计
GET /api/data/launches/cumulative  # 累计发射对比
GET /api/data/launches/monthly     # 月度趋势
GET /api/data/rockets/longmarch    # 长征系列
GET /api/data/commercial/companies # 商业航天
GET /api/data/spacestation/timeline # 空间站时间线
GET /api/data/deep-space/missions  # 深空探测
GET /api/data/satellites/categories # 卫星分类
GET /api/data/beidou/constellation # 北斗星座
GET /api/data/launch-sites         # 发射场分布
```

## 获取阿里云 API Key

1. 访问 [阿里云 DashScope](https://dashscope.console.aliyun.com/)
2. 注册/登录阿里云账号
3. 开通 DashScope 服务
4. 创建 API Key
5. 将 API Key 配置到环境变量或配置文件中

## 配色方案

| 颜色名称 | 色值 | 用途 |
|---------|------|------|
| 深空蓝 | #0d1821 | 主背景 |
| 航天蓝 | #1a2332 | 卡片背景 |
| 星云蓝 | #2a3544 | 边框/悬停 |
| 天际蓝 | #3d4c5f | 次要元素 |
| 科技蓝 | #4fc3f7 | 强调色/主色 |
| 海洋蓝 | #29b6f6 | 辅助强调 |
| 星光金 | #ffb74d | 警告/高亮 |
| 月光银 | #cfd8dc | 次要文字 |

## 注意事项

1. 首次使用需要配置有效的阿里云 API Key
2. 确保前后端端口没有被占用
3. 生产环境请修改默认配置
4. 建议使用 HTTPS 部署

## License

MIT License
