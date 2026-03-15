package com.space.ai.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

/**
 * 卫星数据实体
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "satellite_data")
public class SatelliteData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 卫星名称
     */
    @Column(nullable = false, length = 200)
    private String satelliteName;

    /**
     * 所属国家/地区
     */
    @Column(nullable = false, length = 50)
    private String country;

    /**
     * 卫星类型：COMMUNICATION, NAVIGATION, REMOTE_SENSING, SCIENCE, OTHER
     */
    @Column(nullable = false, length = 30)
    private String satelliteType;

    /**
     * 发射时间
     */
    @Column
    private LocalDateTime launchTime;

    /**
     * 轨道类型：LEO, MEO, GEO, HEO
     */
    @Column(length = 20)
    private String orbitType;

    /**
     * 轨道高度 (km)
     */
    @Column
    private Integer orbitHeight;

    /**
     * 是否仍在运行
     */
    @Column(nullable = false)
    private Boolean active;

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
