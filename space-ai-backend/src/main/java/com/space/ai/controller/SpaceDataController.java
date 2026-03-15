package com.space.ai.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * 航天数据可视化接口
 * 提供模拟的航天发射数据和其他相关数据
 */
@RestController
@RequestMapping("/data")
@CrossOrigin(origins = "*")
public class SpaceDataController {
    
    private final Random random = new Random();
    
    /**
     * 获取历年火箭发射统计（2014-2024）
     */
    @GetMapping("/launches/yearly")
    public Map<String, Object> getYearlyLaunches() {
        String[] years = {"2014", "2015", "2016", "2017", "2018", "2019", "2020", "2021", "2022", "2023", "2024"};
        int[] chinaLaunches = {16, 19, 22, 18, 39, 34, 39, 55, 64, 67, 68};
        int[] usaLaunches = {9, 20, 22, 29, 31, 27, 44, 51, 87, 98, 105};
        int[] russiaLaunches = {22, 25, 19, 20, 21, 22, 17, 19, 22, 20, 18};
        int[] otherLaunches = {45, 48, 42, 46, 48, 45, 42, 48, 52, 55, 58};
        
        return Map.of(
            "years", Arrays.asList(years),
            "china", Arrays.stream(chinaLaunches).boxed().toList(),
            "usa", Arrays.stream(usaLaunches).boxed().toList(),
            "russia", Arrays.stream(russiaLaunches).boxed().toList(),
            "other", Arrays.stream(otherLaunches).boxed().toList()
        );
    }
    
    /**
     * 获取各国累计发射次数
     */
    @GetMapping("/launches/cumulative")
    public List<Map<String, Object>> getCumulativeLaunches() {
        return Arrays.asList(
            Map.of("name", "美国", "value", 6500),
            Map.of("name", "俄罗斯/苏联", "value", 5500),
            Map.of("name", "中国", "value", 500),
            Map.of("name", "日本", "value", 180),
            Map.of("name", "印度", "value", 120),
            Map.of("name", "欧洲", "value", 250),
            Map.of("name", "其他国家", "value", 300)
        );
    }
    
    /**
     * 获取长征系列火箭发射记录
     */
    @GetMapping("/rockets/longmarch")
    public List<Map<String, Object>> getLongMarchRockets() {
        return Arrays.asList(
            createRocketData("长征二号 F", 17, 17, "载人航天"),
            createRocketData("长征三号乙", 85, 83, "地球同步轨道"),
            createRocketData("长征五号", 12, 11, "重型运载"),
            createRocketData("长征六号", 8, 8, "小型运载"),
            createRocketData("长征七号", 10, 10, "中型运载"),
            createRocketData("长征八号", 5, 5, "商业发射"),
            createRocketData("长征十一号", 15, 15, "固体快响"),
            createRocketData("长征二号丙", 75, 73, "太阳同步轨道"),
            createRocketData("长征四号乙", 30, 29, "气象卫星"),
            createRocketData("其他型号", 50, 48, "各类任务")
        );
    }
    
    private Map<String, Object> createRocketData(String name, int total, int success, String type) {
        return Map.of(
            "name", name,
            "totalLaunches", total,
            "successfulLaunches", success,
            "type", type,
            "successRate", String.format("%.1f", (double) success / total * 100)
        );
    }
    
    /**
     * 获取中国空间站建设时间线
     */
    @GetMapping("/spacestation/timeline")
    public List<Map<String, Object>> getSpaceStationTimeline() {
        return Arrays.asList(
            createTimelineEvent("2021-04-29", "天和核心舱发射", "中国空间站首个舱段成功发射"),
            createTimelineEvent("2021-05-29", "天舟二号货运飞船", "为首个航天员乘组运送物资"),
            createTimelineEvent("2021-06-17", "神舟十二号", "聂海胜等 3 名航天员进驻核心舱"),
            createTimelineEvent("2021-10-16", "神舟十三号", "翟志刚等 3 名航天员驻留 6 个月"),
            createTimelineEvent("2022-07-24", "问天实验舱", "首个实验舱成功对接"),
            createTimelineEvent("2022-10-31", "梦天实验舱", "第二个实验舱成功对接"),
            createTimelineEvent("2022-11-03", "T 字基本构型完成", "空间站基本构型组装完成"),
            createTimelineEvent("2023-05-30", "神舟十六号", "景海鹏等执行任务"),
            createTimelineEvent("2023-10-26", "神舟十七号", "汤洪波等执行任务"),
            createTimelineEvent("2024-04-25", "神舟十八号", "叶光富等执行任务")
        );
    }
    
    private Map<String, Object> createTimelineEvent(String date, String event, String description) {
        return Map.of("date", date, "event", event, "description", description);
    }
    
    /**
     * 获取深空探测任务分布
     */
    @GetMapping("/deep-space/missions")
    public List<Map<String, Object>> getDeepSpaceMissions() {
        return Arrays.asList(
            createMissionData("嫦娥一号", "月球", 2007, "绕月探测", 100),
            createMissionData("嫦娥二号", "月球", 2010, "高分辨率成像", 100),
            createMissionData("嫦娥三号", "月球", 2013, "月面软着陆", 100),
            createMissionData("嫦娥四号", "月球背面", 2018, "人类首次月背着陆", 100),
            createMissionData("嫦娥五号", "月球", 2020, "采样返回", 100),
            createMissionData("天问一号", "火星", 2020, "环绕 + 着陆 + 巡视", 100),
            createMissionData("嫦娥六号", "月球背面", 2024, "月背采样返回", 100),
            createMissionData("天问二号", "小行星", 2025, "采样返回（计划）", 0),
            createMissionData("天问三号", "火星", 2028, "采样返回（计划）", 0),
            createMissionData("天问四号", "木星系", 2030, "探测（计划）", 0)
        );
    }
    
    private Map<String, Object> createMissionData(String name, String target, int year, String missionType, int status) {
        return Map.of(
            "name", name,
            "target", target,
            "year", year,
            "missionType", missionType,
            "status", status
        );
    }
    
    /**
     * 获取卫星应用分类统计
     */
    @GetMapping("/satellites/categories")
    public List<Map<String, Object>> getSatelliteCategories() {
        return Arrays.asList(
            Map.of("category", "通信卫星", "count", 180, "percentage", 35),
            Map.of("category", "遥感卫星", "count", 150, "percentage", 29),
            Map.of("category", "导航卫星", "count", 85, "percentage", 17),
            Map.of("category", "科学试验", "count", 55, "percentage", 11),
            Map.of("category", "技术验证", "count", 40, "percentage", 8)
        );
    }
    
    /**
     * 获取北斗导航系统星座构成
     */
    @GetMapping("/beidou/constellation")
    public List<Map<String, Object>> getBeidouConstellation() {
        return Arrays.asList(
            Map.of("orbitType", "GEO（地球静止轨道）", "count", 3, "altitude", "35786km"),
            Map.of("orbitType", "IGSO（倾斜地球同步轨道）", "count", 3, "altitude", "35786km"),
            Map.of("orbitType", "MEO（中圆地球轨道）", "count", 24, "altitude", "21528km"),
            Map.of("orbitType", "备份卫星", "count", 5, "altitude", "混合轨道")
        );
    }
    
    /**
     * 获取月度发射趋势（当年）
     */
    @GetMapping("/launches/monthly")
    public Map<String, Object> getMonthlyLaunches() {
        String[] months = {"1 月", "2 月", "3 月", "4 月", "5 月", "6 月", 
                          "7 月", "8 月", "9 月", "10 月", "11 月", "12 月"};
        int[] launches = {6, 4, 7, 5, 8, 6, 7, 5, 6, 8, 4, 2};
        
        return Map.of(
            "months", Arrays.asList(months),
            "launches", Arrays.stream(launches).boxed().toList()
        );
    }
    
    /**
     * 获取航天发射场分布
     */
    @GetMapping("/launch-sites")
    public List<Map<String, Object>> getLaunchSites() {
        return Arrays.asList(
            createLaunchSite("酒泉", "中国", 180, "载人航天、太阳同步轨道"),
            createLaunchSite("西昌", "中国", 160, "地球同步轨道、探月"),
            createLaunchSite("太原", "中国", 70, "太阳同步轨道、极地轨道"),
            createLaunchSite("文昌", "中国", 45, "大型载荷、空间站、深空探测"),
            createLaunchSite("卡纳维拉尔角", "美国", 400, "各类轨道"),
            createLaunchSite("范登堡", "美国", 200, "极地轨道、太阳同步轨道"),
            createLaunchSite("拜科努尔", "哈萨克斯坦", 150, "载人航天、各类轨道"),
            createLaunchSite("库鲁", "法属圭亚那", 80, "地球同步轨道")
        );
    }
    
    private Map<String, Object> createLaunchSite(String name, String country, int launches, String mainMissions) {
        return Map.of(
            "name", name,
            "country", country,
            "launchCount", launches,
            "mainMissions", mainMissions
        );
    }
    
    /**
     * 获取商业航天公司对比
     */
    @GetMapping("/commercial/companies")
    public List<Map<String, Object>> getCommercialCompanies() {
        return Arrays.asList(
            createCompanyData("SpaceX", "美国", 280, "猎鹰 9 号、星舰", 95),
            createCompanyData("蓝色起源", "美国", 8, "新谢泼德、新格伦", 100),
            createCompanyData("维珍银河", "英国", 6, "太空船二号", 100),
            createCompanyData("火箭实验室", "新西兰", 45, "电子号", 98),
            createCompanyData("星际荣耀", "中国", 3, "双曲线系列", 67),
            createCompanyData("蓝箭航天", "中国", 5, "朱雀系列", 80),
            createCompanyData("星河动力", "中国", 8, "谷神星", 100),
            createCompanyData("中科宇航", "中国", 2, "力箭系列", 100)
        );
    }
    
    private Map<String, Object> createCompanyData(String name, String country, int launches, String rockets, int successRate) {
        return Map.of(
            "name", name,
            "country", country,
            "launchCount", launches,
            "rockets", rockets,
            "successRate", successRate
        );
    }
}
