package com.space.ai.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

/**
 * 发射场数据实体
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "launch_site")
public class LaunchSite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 发射场名称
     */
    @Column(nullable = false, length = 200)
    private String siteName;

    /**
     * 所属国家/地区
     */
    @Column(nullable = false, length = 50)
    private String country;

    /**
     * 地理位置 (纬度)
     */
    @Column(precision = 10, scale = 6)
    private Double latitude;

    /**
     * 地理位置 (经度)
     */
    @Column(precision = 10, scale = 6)
    private Double longitude;

    /**
     * 建成时间
     */
    @Column
    private LocalDateTime builtTime;

    /**
     * 运营状态：ACTIVE, INACTIVE, UNDER_CONSTRUCTION
     */
    @Column(length = 20)
    private String status;

    /**
     * 简介描述
     */
    @Column(length = 1000)
    private String description;

    /**
     * 累计发射次数
     */
    @Column
    private Integer totalLaunches;

    /**
     * 备注信息
     */
    @Column(length = 500)
    private String remark;

    /**
     * 创建时间
     */
    @Column(nullable = false, updatable = false)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @Column(nullable = false)
    private LocalDateTime updateTime;

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        updateTime = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }
}
