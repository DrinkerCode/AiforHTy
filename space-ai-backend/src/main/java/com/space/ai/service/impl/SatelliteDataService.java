package com.space.ai.service;

import com.space.ai.dto.PageRequestDTO;
import com.space.ai.dto.PageResponseDTO;
import com.space.ai.entity.SatelliteData;
import com.space.ai.repository.SatelliteDataRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import jakarta.persistence.criteria.Predicate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 卫星数据服务实现
 */
@Slf4j
@Service
@Transactional
public class SatelliteDataService {

    private final SatelliteDataRepository satelliteDataRepository;

    public SatelliteDataService(SatelliteDataRepository satelliteDataRepository) {
        this.satelliteDataRepository = satelliteDataRepository;
    }

    @Transactional(readOnly = true)
    public PageResponseDTO<SatelliteData> findAll(PageRequestDTO pageRequest) {
        Sort sort = pageRequest.getSortDirection().equalsIgnoreCase("asc") 
            ? Sort.by(pageRequest.getSortBy()).ascending()
            : Sort.by(pageRequest.getSortBy()).descending();
        
        PageRequest pageReq = PageRequest.of(
            pageRequest.getPage() - 1, 
            pageRequest.getSize(), 
            sort
        );
        
        Page<SatelliteData> page = satelliteDataRepository.findAll(pageReq);
        return PageResponseDTO.of(page.getContent(), pageRequest.getPage(), pageRequest.getSize(), page.getTotalElements());
    }

    @Transactional(readOnly = true)
    public SatelliteData findById(Long id) {
        return satelliteDataRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("卫星数据不存在，ID: " + id));
    }

    public SatelliteData create(SatelliteData data) {
        data.setId(null);
        SatelliteData saved = satelliteDataRepository.save(data);
        log.info("创建卫星数据：{}", saved.getSatelliteName());
        return saved;
    }

    public SatelliteData update(Long id, SatelliteData data) {
        SatelliteData existing = satelliteDataRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("卫星数据不存在，ID: " + id));
        
        existing.setSatelliteName(data.getSatelliteName());
        existing.setCountry(data.getCountry());
        existing.setSatelliteType(data.getSatelliteType());
        existing.setLaunchTime(data.getLaunchTime());
        existing.setOrbitType(data.getOrbitType());
        existing.setOrbitHeight(data.getOrbitHeight());
        existing.setActive(data.getActive());
        existing.setRemark(data.getRemark());
        
        SatelliteData updated = satelliteDataRepository.save(existing);
        log.info("更新卫星数据：{}", updated.getSatelliteName());
        return updated;
    }

    public void delete(Long id) {
        if (!satelliteDataRepository.existsById(id)) {
            throw new RuntimeException("卫星数据不存在，ID: " + id);
        }
        satelliteDataRepository.deleteById(id);
        log.info("删除卫星数据，ID: {}", id);
    }

    @Transactional(readOnly = true)
    public List<SatelliteData> search(Map<String, String> filters) {
        Specification<SatelliteData> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            if (StringUtils.hasText(filters.get("country"))) {
                predicates.add(cb.equal(root.get("country"), filters.get("country")));
            }
            if (StringUtils.hasText(filters.get("satelliteType"))) {
                predicates.add(cb.equal(root.get("satelliteType"), filters.get("satelliteType")));
            }
            if (StringUtils.hasText(filters.get("orbitType"))) {
                predicates.add(cb.equal(root.get("orbitType"), filters.get("orbitType")));
            }
            if (filters.get("active") != null) {
                predicates.add(cb.equal(root.get("active"), Boolean.parseBoolean(filters.get("active"))));
            }
            if (StringUtils.hasText(filters.get("satelliteName"))) {
                predicates.add(cb.like(root.get("satelliteName"), "%" + filters.get("satelliteName") + "%"));
            }
            
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        
        return satelliteDataRepository.findAll(spec);
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getStatistics() {
        long total = satelliteDataRepository.count();
        long activeCount = satelliteDataRepository.findByActive(true).size();
        
        Map<String, Object> stats = new HashMap<>();
        stats.put("total", total);
        stats.put("activeCount", activeCount);
        stats.put("inactiveCount", total - activeCount);
        
        return stats;
    }

    @Transactional(readOnly = true)
    public List<Map<String, Object>> countByType() {
        List<SatelliteData> all = satelliteDataRepository.findAll();
        Map<String, Long> typeCount = all.stream()
            .collect(Collectors.groupingBy(
                SatelliteData::getSatelliteType,
                Collectors.counting()
            ));
        
        return typeCount.entrySet().stream()
            .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
            .map(e -> {
                Map<String, Object> item = new HashMap<>();
                item.put("type", e.getKey());
                item.put("count", e.getValue());
                return item;
            })
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<Map<String, Object>> countByOrbit() {
        List<SatelliteData> all = satelliteDataRepository.findAll();
        Map<String, Long> orbitCount = all.stream()
            .collect(Collectors.groupingBy(
                SatelliteData::getOrbitType,
                Collectors.counting()
            ));
        
        return orbitCount.entrySet().stream()
            .map(e -> {
                Map<String, Object> item = new HashMap<>();
                item.put("orbit", e.getKey());
                item.put("count", e.getValue());
                return item;
            })
            .collect(Collectors.toList());
    }
}
