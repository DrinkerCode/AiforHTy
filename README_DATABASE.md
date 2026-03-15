# 航天知识 AI 问答系统 - 数据库功能说明

## 📋 新增功能概述

本次更新为系统添加了完整的 MySQL 数据库支持，实现了对航天相关数据的增删改查（CRUD）管理功能。

## 🗄️ 数据库配置

### 1. 环境要求
- MySQL 5.7+ 或 MySQL 8.0+
- Java 17+
- Maven 3.6+

### 2. 数据库初始化

#### 方式一：使用提供的 SQL 脚本
```bash
mysql -u root -p < /workspace/space-ai-backend/src/main/resources/sql/init.sql
```

#### 方式二：手动执行
1. 登录 MySQL
```bash
mysql -u root -p
```

2. 执行初始化脚本内容
```sql
source /workspace/space-ai-backend/src/main/resources/sql/init.sql;
```

### 3. 配置文件修改

编辑 `application.yml` 文件，修改数据库连接信息：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/space_ai_db?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true
    username: root
    password: ${MYSQL_PASSWORD:root}  # 通过环境变量或直接修改密码
    driver-class-name: com.mysql.cj.jdbc.Driver
  
  jpa:
    hibernate:
      ddl-auto: update  # 开发环境使用 update，生产环境建议使用 validate
    show-sql: true
    properties:
      hibernate:
        dialect: org.hibernate.dialect.MySQLDialect
        format_sql: true
```

## 📊 数据表结构

### 1. 火箭发射记录表 (launch_record)
| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键 ID |
| mission_name | VARCHAR(200) | 发射任务名称 |
| rocket_type | VARCHAR(100) | 火箭型号 |
| country | VARCHAR(50) | 发射国家/地区 |
| launch_site | VARCHAR(100) | 发射场名称 |
| launch_time | DATETIME | 发射时间 |
| status | VARCHAR(20) | 发射状态 (SUCCESS/FAILURE/PARTIAL_SUCCESS) |
| payload_type | VARCHAR(100) | 载荷类型 |
| remark | VARCHAR(500) | 备注信息 |

### 2. 卫星数据表 (satellite_data)
| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键 ID |
| satellite_name | VARCHAR(200) | 卫星名称 |
| country | VARCHAR(50) | 所属国家/地区 |
| satellite_type | VARCHAR(30) | 卫星类型 |
| launch_time | DATETIME | 发射时间 |
| orbit_type | VARCHAR(20) | 轨道类型 (LEO/MEO/GEO/HEO) |
| orbit_height | INT | 轨道高度 (km) |
| active | TINYINT(1) | 是否仍在运行 |
| remark | VARCHAR(500) | 备注信息 |

### 3. 航天任务表 (space_mission)
| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键 ID |
| mission_name | VARCHAR(200) | 任务名称 |
| mission_type | VARCHAR(30) | 任务类型 |
| country | VARCHAR(50) | 所属国家/地区 |
| launch_time | DATETIME | 发射时间 |
| status | VARCHAR(20) | 任务状态 |
| description | TEXT | 任务描述 |
| timeline | TEXT | 关键时间节点 (JSON) |
| remark | VARCHAR(500) | 备注信息 |

### 4. 发射场表 (launch_site)
| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键 ID |
| site_name | VARCHAR(200) | 发射场名称 |
| country | VARCHAR(50) | 所属国家/地区 |
| latitude | DECIMAL(10,6) | 纬度 |
| longitude | DECIMAL(10,6) | 经度 |
| built_time | DATETIME | 建成时间 |
| status | VARCHAR(20) | 运营状态 |
| description | TEXT | 简介描述 |
| total_launches | INT | 累计发射次数 |
| remark | VARCHAR(500) | 备注信息 |

## 🔧 API 接口说明

### 发射记录管理 API

| 接口 | 方法 | 描述 |
|------|------|------|
| `/api/admin/launches` | GET | 分页查询所有记录 |
| `/api/admin/launches/{id}` | GET | 根据 ID 查询 |
| `/api/admin/launches` | POST | 新增记录 |
| `/api/admin/launches/{id}` | PUT | 更新记录 |
| `/api/admin/launches/{id}` | DELETE | 删除记录 |
| `/api/admin/launches/search` | GET | 条件搜索 |
| `/api/admin/launches/statistics` | GET | 获取统计信息 |
| `/api/admin/launches/stats/by-year` | GET | 按年份统计 |
| `/api/admin/launches/stats/by-country` | GET | 按国家统计 |
| `/api/admin/launches/stats/by-status` | GET | 按状态统计 |

### 卫星数据管理 API

| 接口 | 方法 | 描述 |
|------|------|------|
| `/api/admin/satellites` | GET | 分页查询所有记录 |
| `/api/admin/satellites/{id}` | GET | 根据 ID 查询 |
| `/api/admin/satellites` | POST | 新增卫星 |
| `/api/admin/satellites/{id}` | PUT | 更新卫星 |
| `/api/admin/satellites/{id}` | DELETE | 删除卫星 |
| `/api/admin/satellites/search` | GET | 条件搜索 |
| `/api/admin/satellites/statistics` | GET | 获取统计信息 |
| `/api/admin/satellites/stats/by-type` | GET | 按类型统计 |
| `/api/admin/satellites/stats/by-orbit` | GET | 按轨道统计 |

## 🖥️ 前端管理页面

### 1. 发射记录管理页面
- 访问路径：`/admin/launches`
- 功能特性：
  - 数据统计卡片（总数、成功数、失败数、成功率）
  - 多条件搜索过滤
  - 表格展示与分页
  - 新增/编辑对话框
  - 删除确认

### 2. 卫星数据管理页面
- 访问路径：`/admin/satellites`
- 功能特性：
  - 数据统计卡片（总数、在轨数、退役数）
  - 多条件搜索过滤（名称、国家、类型、轨道、状态）
  - 表格展示与分页
  - 新增/编辑对话框
  - 删除确认

## 🚀 启动步骤

### 1. 启动 MySQL 数据库
```bash
# Linux/Mac
sudo systemctl start mysql

# Windows
net start mysql
```

### 2. 初始化数据库
```bash
mysql -u root -p < /workspace/space-ai-backend/src/main/resources/sql/init.sql
```

### 3. 启动后端服务
```bash
cd /workspace/space-ai-backend
export MYSQL_PASSWORD=your_password
export ALIYUN_API_KEY=your_api_key
mvn spring-boot:run
```

### 4. 启动前端服务
```bash
cd /workspace/space-ai-frontend
npm install
npm run dev
```

### 5. 访问管理系统
- 前端地址：http://localhost:5173
- 点击左侧菜单 "数据管理" -> "发射记录管理" 或 "卫星数据管理"

## 📝 示例数据

系统已预置以下示例数据：
- 8 条火箭发射记录（包含中国、美国、欧洲、日本、印度等）
- 8 条卫星数据（北斗、GPS、Galileo 等导航卫星及科学卫星）
- 5 条航天任务（中国空间站、嫦娥工程、天问火星、Artemis 等）
- 8 个发射场（酒泉、文昌、西昌、肯尼迪、库鲁等）

## 🔐 安全建议

1. **生产环境配置**
   - 修改默认数据库密码
   - 禁用 SQL 日志输出
   - 设置 `ddl-auto: validate`
   - 启用 SSL 连接

2. **权限控制**
   - 添加用户认证机制
   - 实现基于角色的访问控制 (RBAC)
   - 对管理接口进行权限验证

3. **数据备份**
   ```bash
   mysqldump -u root -p space_ai_db > backup_$(date +%Y%m%d).sql
   ```

## 🛠️ 技术栈

- **后端**: Spring Boot 3.2 + Spring Data JPA + MySQL
- **前端**: Vue 3 + Element Plus + ECharts
- **ORM**: Hibernate
- **数据库连接池**: HikariCP (Spring Boot 默认)

## 📞 常见问题

### Q: 数据库连接失败？
A: 检查 MySQL 服务是否启动，用户名密码是否正确，防火墙是否开放 3306 端口。

### Q: 表结构未自动创建？
A: 确保 `ddl-auto` 设置为 `update` 或 `create`，检查数据库用户是否有建表权限。

### Q: 中文乱码？
A: 确保数据库字符集为 `utf8mb4`，JDBC URL 包含 `useUnicode=true&characterEncoding=utf8`。
