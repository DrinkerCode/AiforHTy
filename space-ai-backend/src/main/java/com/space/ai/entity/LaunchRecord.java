package com.space.ai.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

/**
 * 火箭发射记录实体
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "launch_record")
public class LaunchRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 发射任务名称
     */
    @Column(nullable = false, length = 200)
    private String missionName;

    /**
     * 火箭型号
     */
    @Column(nullable = false, length = 100)
    private String rocketType;

    /**
     * 发射国家/地区
     */
    @Column(nullable = false, length = 50)
    private String country;

    /**
     * 发射场名称
     */
    @Column(length = 100)
    private String launchSite;

    /**
     * 发射时间
     */
    @Column(nullable = false)
    private LocalDateTime launchTime;

    /**
     * 发射状态：SUCCESS, FAILURE, PARTIAL_SUCCESS
     */
    @Column(nullable = false, length = 20)
    private String status;

    /**
     * 载荷类型
     */
    @Column(length = 100)
    private String payloadType;

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
