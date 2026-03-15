package com.space.ai.controller;

import com.space.ai.dto.PageRequestDTO;
import com.space.ai.dto.PageResponseDTO;
import com.space.ai.entity.SatelliteData;
import com.space.ai.service.impl.SatelliteDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * 卫星数据管理控制器
 */
@Slf4j
@RestController
@RequestMapping("/admin/satellites")
public class SatelliteDataController {

    private final SatelliteDataService satelliteDataService;

    public SatelliteDataController(SatelliteDataService satelliteDataService) {
        this.satelliteDataService = satelliteDataService;
    }

    @GetMapping
    public ResponseEntity<PageResponseDTO<SatelliteData>> findAll(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createTime") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {
        
        PageRequestDTO pageRequest = new PageRequestDTO();
        pageRequest.setPage(page);
        pageRequest.setSize(size);
        pageRequest.setSortBy(sortBy);
        pageRequest.setSortDirection(sortDirection);
        
        return ResponseEntity.ok(satelliteDataService.findAll(pageRequest));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SatelliteData> findById(@PathVariable Long id) {
        return ResponseEntity.ok(satelliteDataService.findById(id));
    }

    @PostMapping
    public ResponseEntity<SatelliteData> create(@Valid @RequestBody SatelliteData data) {
        log.info("接收到创建请求：{}", data.getSatelliteName());
        return ResponseEntity.ok(satelliteDataService.create(data));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SatelliteData> update(
            @PathVariable Long id, 
            @Valid @RequestBody SatelliteData data) {
        return ResponseEntity.ok(satelliteDataService.update(id, data));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        satelliteDataService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<SatelliteData>> search(
            @RequestParam(required = false) String country,
            @RequestParam(required = false) String satelliteType,
            @RequestParam(required = false) String orbitType,
            @RequestParam(required = false) String active,
            @RequestParam(required = false) String satelliteName) {
        
        Map<String, String> filters = new java.util.HashMap<>();
        if (country != null) filters.put("country", country);
        if (satelliteType != null) filters.put("satelliteType", satelliteType);
        if (orbitType != null) filters.put("orbitType", orbitType);
        if (active != null) filters.put("active", active);
        if (satelliteName != null) filters.put("satelliteName", satelliteName);
        
        return ResponseEntity.ok(satelliteDataService.search(filters));
    }

    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getStatistics() {
        return ResponseEntity.ok(satelliteDataService.getStatistics());
    }

    @GetMapping("/stats/by-type")
    public ResponseEntity<List<Map<String, Object>>> countByType() {
        return ResponseEntity.ok(satelliteDataService.countByType());
    }

    @GetMapping("/stats/by-orbit")
    public ResponseEntity<List<Map<String, Object>>> countByOrbit() {
        return ResponseEntity.ok(satelliteDataService.countByOrbit());
    }
}
