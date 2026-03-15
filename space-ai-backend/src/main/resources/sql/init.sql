-- 航天知识 AI 问答系统 - MySQL 数据库初始化脚本
-- 数据库名：space_ai_db

CREATE DATABASE IF NOT EXISTS space_ai_db 
DEFAULT CHARACTER SET utf8mb4 
DEFAULT COLLATE utf8mb4_unicode_ci;

USE space_ai_db;

-- 火箭发射记录表
DROP TABLE IF EXISTS launch_record;
CREATE TABLE launch_record (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键 ID',
    mission_name VARCHAR(200) NOT NULL COMMENT '发射任务名称',
    rocket_type VARCHAR(100) NOT NULL COMMENT '火箭型号',
    country VARCHAR(50) NOT NULL COMMENT '发射国家/地区',
    launch_site VARCHAR(100) COMMENT '发射场名称',
    launch_time DATETIME NOT NULL COMMENT '发射时间',
    status VARCHAR(20) NOT NULL COMMENT '发射状态：SUCCESS, FAILURE, PARTIAL_SUCCESS',
    payload_type VARCHAR(100) COMMENT '载荷类型',
    remark VARCHAR(500) COMMENT '备注信息',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_country (country),
    INDEX idx_rocket_type (rocket_type),
    INDEX idx_status (status),
    INDEX idx_launch_time (launch_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='火箭发射记录表';

-- 卫星数据表
DROP TABLE IF EXISTS satellite_data;
CREATE TABLE satellite_data (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键 ID',
    satellite_name VARCHAR(200) NOT NULL COMMENT '卫星名称',
    country VARCHAR(50) NOT NULL COMMENT '所属国家/地区',
    satellite_type VARCHAR(30) NOT NULL COMMENT '卫星类型',
    launch_time DATETIME COMMENT '发射时间',
    orbit_type VARCHAR(20) COMMENT '轨道类型',
    orbit_height INT COMMENT '轨道高度 (km)',
    active TINYINT(1) NOT NULL DEFAULT 1 COMMENT '是否仍在运行',
    remark VARCHAR(500) COMMENT '备注信息',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_country (country),
    INDEX idx_satellite_type (satellite_type),
    INDEX idx_orbit_type (orbit_type),
    INDEX idx_active (active)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='卫星数据表';

-- 航天任务表
DROP TABLE IF EXISTS space_mission;
CREATE TABLE space_mission (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键 ID',
    mission_name VARCHAR(200) NOT NULL COMMENT '任务名称',
    mission_type VARCHAR(30) NOT NULL COMMENT '任务类型',
    country VARCHAR(50) NOT NULL COMMENT '所属国家/地区',
    launch_time DATETIME COMMENT '发射时间',
    status VARCHAR(20) COMMENT '任务状态',
    description TEXT COMMENT '任务描述',
    timeline TEXT COMMENT '关键时间节点',
    remark VARCHAR(500) COMMENT '备注信息',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_country (country),
    INDEX idx_mission_type (mission_type),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='航天任务表';

-- 发射场表
DROP TABLE IF EXISTS launch_site;
CREATE TABLE launch_site (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键 ID',
    site_name VARCHAR(200) NOT NULL COMMENT '发射场名称',
    country VARCHAR(50) NOT NULL COMMENT '所属国家/地区',
    latitude DECIMAL(10,6) COMMENT '纬度',
    longitude DECIMAL(10,6) COMMENT '经度',
    built_time DATETIME COMMENT '建成时间',
    status VARCHAR(20) COMMENT '运营状态',
    description TEXT COMMENT '简介描述',
    total_launches INT COMMENT '累计发射次数',
    remark VARCHAR(500) COMMENT '备注信息',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_country (country),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='发射场表';

-- 插入示例数据 - 火箭发射记录
INSERT INTO launch_record (mission_name, rocket_type, country, launch_site, launch_time, status, payload_type, remark) VALUES
('神舟十七号载人飞船', '长征二号 F', '中国', '酒泉卫星发射中心', '2023-10-26 11:14:00', 'SUCCESS', '载人飞船', '中国空间站任务'),
('天舟六号货运飞船', '长征七号', '中国', '文昌航天发射场', '2023-05-10 21:22:00', 'SUCCESS', '货运飞船', '空间站补给任务'),
('嫦娥六号', '长征五号', '中国', '文昌航天发射场', '2024-05-03 17:27:00', 'SUCCESS', '月球探测器', '月球背面采样返回'),
('星舰 IFT-4', 'Starship', '美国', '博卡奇卡发射场', '2024-06-06 07:50:00', 'PARTIAL_SUCCESS', '试验飞行', 'SpaceX 星舰第四次试飞'),
('猎鹰 9 号 Starlink', 'Falcon 9', '美国', '肯尼迪航天中心', '2024-01-15 05:30:00', 'SUCCESS', '通信卫星', '星链组网任务'),
('阿丽亚娜 6 号首飞', 'Ariane 6', '欧洲', '库鲁航天中心', '2024-07-09 15:00:00', 'SUCCESS', '试验载荷', '欧洲新一代运载火箭'),
('H3 火箭首飞', 'H3', '日本', '种子岛宇宙中心', '2023-03-07 10:37:00', 'FAILURE', '试验载荷', '日本新一代主力火箭'),
('GSLV Mk III', 'GSLV Mk III', '印度', '萨迪什·达万航天中心', '2023-07-14 14:35:00', 'SUCCESS', '月球探测器', '月船三号任务');

-- 插入示例数据 - 卫星数据
INSERT INTO satellite_data (satellite_name, country, satellite_type, launch_time, orbit_type, orbit_height, active, remark) VALUES
('北斗三号 G1', '中国', 'NAVIGATION', '2018-11-01 23:57:00', 'GEO', 35786, TRUE, '北斗导航卫星'),
('北斗三号 M15', '中国', 'NAVIGATION', '2023-05-17 10:49:00', 'MEO', 21528, TRUE, '北斗导航卫星'),
('天宫空间站', '中国', 'SCIENCE', '2021-04-29 11:23:00', 'LEO', 400, TRUE, '中国空间站核心舱'),
('高分十四号', '中国', 'REMOTE_SENSING', '2023-08-21 01:53:00', 'LEO', 600, TRUE, '光学遥感卫星'),
('GPS III SV06', '美国', 'NAVIGATION', '2023-01-18 17:24:00', 'MEO', 20200, TRUE, 'GPS 导航卫星'),
('James Webb', '美国', 'SCIENCE', '2021-12-25 12:20:00', 'HEO', 1500000, TRUE, '詹姆斯韦伯太空望远镜'),
('Galileo FM19', '欧洲', 'NAVIGATION', '2023-04-26 12:14:00', 'MEO', 23222, TRUE, '伽利略导航卫星'),
('OneWeb-0625', '英国', 'COMMUNICATION', '2023-06-23 03:08:00', 'LEO', 1200, TRUE, 'OneWeb 通信卫星');

-- 插入示例数据 - 航天任务
INSERT INTO space_mission (mission_name, mission_type, country, launch_time, status, description, timeline, remark) VALUES
('中国空间站建设', 'SPACE_STATION', '中国', '2021-04-29 00:00:00', 'COMPLETED', '中国自主建设的近地轨道空间站', '[{"date":"2021-04-29","event":"天和核心舱发射"},{"date":"2022-07-24","event":"问天实验舱发射"},{"date":"2022-10-31","event":"梦天实验舱发射"}]', 'T 字形构型'),
('嫦娥探月工程', 'LUNAR_EXPLORATION', '中国', '2007-10-24 00:00:00', 'IN_PROGRESS', '中国月球探测计划', '[{"date":"2007-10-24","event":"嫦娥一号发射"},{"date":"2013-12-01","event":"嫦娥三号着陆"},{"date":"2020-12-17","event":"嫦娥五号返回"}]', '绕落回三步走'),
('天问火星探测', 'MARS_EXPLORATION', '中国', '2020-07-23 00:00:00', 'COMPLETED', '中国首次火星探测任务', '[{"date":"2020-07-23","event":"天问一号发射"},{"date":"2021-02-10","event":"进入火星轨道"},{"date":"2021-05-15","event":"祝融号着陆"}]', '一次实现绕落巡'),
('Artemis 登月计划', 'LUNAR_EXPLORATION', '美国', '2022-11-16 00:00:00', 'IN_PROGRESS', '美国重返月球计划', '[{"date":"2022-11-16","event":"Artemis I 发射"},{"date":"2024-09-26","event":"Artemis II 计划"},{"date":"2025-12-00","event":"Artemis III 计划"}]', '建立月球基地'),
('载人航天工程', 'MANNED', '中国', '2003-10-15 00:00:00', 'IN_PROGRESS', '中国载人航天工程', '[{"date":"2003-10-15","event":"神舟五号首飞"},{"date":"2008-09-27","event":"首次太空行走"},{"date":"2021-06-17","event":"空间站阶段首飞"}]', '三步走战略');

-- 插入示例数据 - 发射场
INSERT INTO launch_site (site_name, country, latitude, longitude, built_time, status, description, total_launches, remark) VALUES
('酒泉卫星发射中心', '中国', 40.958500, 100.291700, '1958-01-01 00:00:00', 'ACTIVE', '中国创建最早、规模最大的综合型导弹、卫星发射中心', 150, '东风航天城'),
('文昌航天发射场', '中国', 19.614200, 110.951100, '2014-01-01 00:00:00', 'ACTIVE', '中国首个滨海发射基地，主要承担地球同步轨道卫星、大质量极轨卫星等发射任务', 50, '低纬度优势'),
('西昌卫星发射中心', '中国', 28.246700, 102.026700, '1970-01-01 00:00:00', 'ACTIVE', '主要用于发射地球同步轨道卫星', 180, '月亮城'),
('太原卫星发射中心', '中国', 38.849000, 111.608300, '1967-01-01 00:00:00', 'ACTIVE', '主要承担太阳同步轨道气象、资源、通信等多种型号的中、低轨道卫星和运载火箭的发射任务', 120, '黄土高原'),
('肯尼迪航天中心', '美国', 28.572900, -80.649000, '1962-01-01 00:00:00', 'ACTIVE', '美国航空航天局进行载人和不载人航天器测试和发射的主要场地', 400, '阿波罗登月起点'),
('范登堡太空军基地', '美国', 34.757200, -120.521700, '1957-01-01 00:00:00', 'ACTIVE', '美国西海岸的主要航天发射场，主要用于发射极地轨道卫星', 250, '极地轨道'),
('库鲁航天中心', '法国', 5.239000, -52.768300, '1968-01-01 00:00:00', 'ACTIVE', '欧洲空间局的主要航天发射场，位于法属圭亚那', 300, '欧洲太空港'),
('拜科努尔航天发射场', '哈萨克斯坦', 45.965000, 63.305000, '1955-01-01 00:00:00', 'ACTIVE', '世界上第一个也是最大的航天发射场，苏联/俄罗斯主要航天发射基地', 1500, '人类航天发源地');
