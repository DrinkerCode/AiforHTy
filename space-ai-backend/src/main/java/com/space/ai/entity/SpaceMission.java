package com.space.ai.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

/**
 * 航天任务实体
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "space_mission")
public class SpaceMission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 任务名称
     */
    @Column(nullable = false, length = 200)
    private String missionName;

    /**
     * 任务类型：MANNED, LUNAR_EXPLORATION, MARS_EXPLORATION, SPACE_STATION, DEEP_SPACE
     */
    @Column(nullable = false, length = 30)
    private String missionType;

    /**
     * 所属国家/地区
     */
    @Column(nullable = false, length = 50)
    private String country;

    /**
     * 发射时间
     */
    @Column
    private LocalDateTime launchTime;

    /**
     * 任务状态：PLANNING, IN_PROGRESS, COMPLETED, FAILED
     */
    @Column(length = 20)
    private String status;

    /**
     * 任务描述
     */
    @Column(length = 1000)
    private String description;

    /**
     * 关键时间节点 (JSON 格式存储多个时间点)
     */
    @Column(length = 2000)
    private String timeline;

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
