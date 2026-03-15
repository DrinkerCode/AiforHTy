package com.space.ai.dto;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 火箭发射记录 DTO
 */
@Data
public class LaunchRecordDTO {

    private Long id;
    private String missionName;
    private String rocketType;
    private String country;
    private String launchSite;
    private LocalDateTime launchTime;
    private String status;
    private String payloadType;
    private String remark;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
