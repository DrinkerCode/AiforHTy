package com.space.ai.service;

import com.space.ai.dto.LaunchRecordDTO;
import com.space.ai.dto.PageRequestDTO;
import com.space.ai.dto.PageResponseDTO;

import java.util.List;
import java.util.Map;

/**
 * 火箭发射记录服务接口
 */
public interface LaunchRecordService {

    /**
     * 分页查询所有记录
     */
    PageResponseDTO<LaunchRecordDTO> findAll(PageRequestDTO pageRequest);

    /**
     * 根据 ID 查询
     */
    LaunchRecordDTO findById(Long id);

    /**
     * 新增记录
     */
    LaunchRecordDTO create(LaunchRecordDTO dto);

    /**
     * 更新记录
     */
    LaunchRecordDTO update(Long id, LaunchRecordDTO dto);

    /**
     * 删除记录
     */
    void delete(Long id);

    /**
     * 按条件查询
     */
    List<LaunchRecordDTO> search(Map<String, String> filters);

    /**
     * 获取统计信息
     */
    Map<String, Object> getStatistics();

    /**
     * 按年份统计
     */
    List<Map<String, Object>> countByYear();

    /**
     * 按国家统计
     */
    List<Map<String, Object>> countByCountry();

    /**
     * 按状态统计
     */
    List<Map<String, Object>> countByStatus();
}
