package com.space.ai.dto;

import lombok.Data;
import java.util.List;

/**
 * 通用分页响应 DTO
 */
@Data
public class PageResponseDTO<T> {

    /**
     * 当前页码
     */
    private int page;

    /**
     * 每页大小
     */
    private int size;

    /**
     * 总记录数
     */
    private long total;

    /**
     * 总页数
     */
    private int totalPages;

    /**
     * 数据列表
     */
    private List<T> content;

    public static <T> PageResponseDTO<T> of(List<T> content, int page, int size, long total) {
        PageResponseDTO<T> response = new PageResponseDTO<>();
        response.setContent(content);
        response.setPage(page);
        response.setSize(size);
        response.setTotal(total);
        response.setTotalPages((int) Math.ceil((double) total / size));
        return response;
    }
}
