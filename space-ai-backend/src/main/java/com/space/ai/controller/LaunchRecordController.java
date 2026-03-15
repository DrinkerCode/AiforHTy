package com.space.ai.controller;

import com.space.ai.dto.LaunchRecordDTO;
import com.space.ai.dto.PageRequestDTO;
import com.space.ai.dto.PageResponseDTO;
import com.space.ai.service.LaunchRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * 火箭发射记录管理控制器
 */
@Slf4j
@RestController
@RequestMapping("/admin/launches")
public class LaunchRecordController {

    private final LaunchRecordService launchRecordService;

    public LaunchRecordController(LaunchRecordService launchRecordService) {
        this.launchRecordService = launchRecordService;
    }

    /**
     * 分页查询所有记录
     */
    @GetMapping
    public ResponseEntity<PageResponseDTO<LaunchRecordDTO>> findAll(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createTime") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {
        
        PageRequestDTO pageRequest = new PageRequestDTO();
        pageRequest.setPage(page);
        pageRequest.setSize(size);
        pageRequest.setSortBy(sortBy);
        pageRequest.setSortDirection(sortDirection);
        
        return ResponseEntity.ok(launchRecordService.findAll(pageRequest));
    }

    /**
     * 根据 ID 查询
     */
    @GetMapping("/{id}")
    public ResponseEntity<LaunchRecordDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(launchRecordService.findById(id));
    }

    /**
     * 新增记录
     */
    @PostMapping
    public ResponseEntity<LaunchRecordDTO> create(@Valid @RequestBody LaunchRecordDTO dto) {
        log.info("接收到创建请求：{}", dto.getMissionName());
        LaunchRecordDTO created = launchRecordService.create(dto);
        return ResponseEntity.ok(created);
    }

    /**
     * 更新记录
     */
    @PutMapping("/{id}")
    public ResponseEntity<LaunchRecordDTO> update(
            @PathVariable Long id, 
            @Valid @RequestBody LaunchRecordDTO dto) {
        return ResponseEntity.ok(launchRecordService.update(id, dto));
    }

    /**
     * 删除记录
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        launchRecordService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * 搜索记录
     */
    @GetMapping("/search")
    public ResponseEntity<List<LaunchRecordDTO>> search(
            @RequestParam(required = false) String country,
            @RequestParam(required = false) String rocketType,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String missionName) {
        
        Map<String, String> filters = new java.util.HashMap<>();
        if (country != null) filters.put("country", country);
        if (rocketType != null) filters.put("rocketType", rocketType);
        if (status != null) filters.put("status", status);
        if (missionName != null) filters.put("missionName", missionName);
        
        return ResponseEntity.ok(launchRecordService.search(filters));
    }

    /**
     * 获取统计信息
     */
    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getStatistics() {
        return ResponseEntity.ok(launchRecordService.getStatistics());
    }

    /**
     * 按年份统计
     */
    @GetMapping("/stats/by-year")
    public ResponseEntity<List<Map<String, Object>>> countByYear() {
        return ResponseEntity.ok(launchRecordService.countByYear());
    }

    /**
     * 按国家统计
     */
    @GetMapping("/stats/by-country")
    public ResponseEntity<List<Map<String, Object>>> countByCountry() {
        return ResponseEntity.ok(launchRecordService.countByCountry());
    }

    /**
     * 按状态统计
     */
    @GetMapping("/stats/by-status")
    public ResponseEntity<List<Map<String, Object>>> countByStatus() {
        return ResponseEntity.ok(launchRecordService.countByStatus());
    }
}
