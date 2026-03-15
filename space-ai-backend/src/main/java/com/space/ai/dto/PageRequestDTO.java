package com.space.ai.dto;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 通用分页请求 DTO
 */
@Data
public class PageRequestDTO {

    /**
     * 页码 (从 1 开始)
     */
    private int page = 1;

    /**
     * 每页大小
     */
    private int size = 10;

    /**
     * 排序字段
     */
    private String sortBy = "createTime";

    /**
     * 排序方向：asc, desc
     */
    private String sortDirection = "desc";
}
