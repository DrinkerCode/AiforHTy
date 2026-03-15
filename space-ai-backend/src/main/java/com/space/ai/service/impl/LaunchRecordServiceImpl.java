package com.space.ai.service.impl;

import com.space.ai.dto.LaunchRecordDTO;
import com.space.ai.dto.PageRequestDTO;
import com.space.ai.dto.PageResponseDTO;
import com.space.ai.entity.LaunchRecord;
import com.space.ai.repository.LaunchRecordRepository;
import com.space.ai.service.LaunchRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import jakarta.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 火箭发射记录服务实现
 */
@Slf4j
@Service
@Transactional
public class LaunchRecordServiceImpl implements LaunchRecordService {

    private final LaunchRecordRepository launchRecordRepository;

    public LaunchRecordServiceImpl(LaunchRecordRepository launchRecordRepository) {
        this.launchRecordRepository = launchRecordRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponseDTO<LaunchRecordDTO> findAll(PageRequestDTO pageRequest) {
        Sort sort = pageRequest.getSortDirection().equalsIgnoreCase("asc") 
            ? Sort.by(pageRequest.getSortBy()).ascending()
            : Sort.by(pageRequest.getSortBy()).descending();
        
        PageRequest pageReq = PageRequest.of(
            pageRequest.getPage() - 1, 
            pageRequest.getSize(), 
            sort
        );
        
        Page<LaunchRecord> page = launchRecordRepository.findAll(pageReq);
        
        List<LaunchRecordDTO> content = page.getContent().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
        
        return PageResponseDTO.of(content, pageRequest.getPage(), pageRequest.getSize(), page.getTotalElements());
    }

    @Override
    @Transactional(readOnly = true)
    public LaunchRecordDTO findById(Long id) {
        LaunchRecord record = launchRecordRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("记录不存在，ID: " + id));
        return convertToDTO(record);
    }

    @Override
    public LaunchRecordDTO create(LaunchRecordDTO dto) {
        LaunchRecord record = convertToEntity(dto);
        record.setId(null); // 确保是新记录
        LaunchRecord saved = launchRecordRepository.save(record);
        log.info("创建发射记录：{}", saved.getMissionName());
        return convertToDTO(saved);
    }

    @Override
    public LaunchRecordDTO update(Long id, LaunchRecordDTO dto) {
        LaunchRecord record = launchRecordRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("记录不存在，ID: " + id));
        
        BeanUtils.copyProperties(dto, record, "id", "createTime");
        LaunchRecord updated = launchRecordRepository.save(record);
        log.info("更新发射记录：{}", updated.getMissionName());
        return convertToDTO(updated);
    }

    @Override
    public void delete(Long id) {
        if (!launchRecordRepository.existsById(id)) {
            throw new RuntimeException("记录不存在，ID: " + id);
        }
        launchRecordRepository.deleteById(id);
        log.info("删除发射记录，ID: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LaunchRecordDTO> search(Map<String, String> filters) {
        Specification<LaunchRecord> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            if (StringUtils.hasText(filters.get("country"))) {
                predicates.add(cb.equal(root.get("country"), filters.get("country")));
            }
            if (StringUtils.hasText(filters.get("rocketType"))) {
                predicates.add(cb.equal(root.get("rocketType"), filters.get("rocketType")));
            }
            if (StringUtils.hasText(filters.get("status"))) {
                predicates.add(cb.equal(root.get("status"), filters.get("status")));
            }
            if (StringUtils.hasText(filters.get("missionName"))) {
                predicates.add(cb.like(root.get("missionName"), "%" + filters.get("missionName") + "%"));
            }
            
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        
        return launchRecordRepository.findAll(spec).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getStatistics() {
        long total = launchRecordRepository.count();
        long successCount = launchRecordRepository.findByStatus("SUCCESS").size();
        long failureCount = launchRecordRepository.findByStatus("FAILURE").size();
        
        Map<String, Object> stats = new HashMap<>();
        stats.put("total", total);
        stats.put("successCount", successCount);
        stats.put("failureCount", failureCount);
        stats.put("successRate", total > 0 ? (double) successCount / total * 100 : 0);
        
        return stats;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> countByYear() {
        List<LaunchRecord> all = launchRecordRepository.findAll();
        Map<Integer, Long> yearCount = all.stream()
            .filter(r -> r.getLaunchTime() != null)
            .collect(Collectors.groupingBy(
                r -> r.getLaunchTime().getYear(),
                Collectors.counting()
            ));
        
        return yearCount.entrySet().stream()
            .sorted(Map.Entry.comparingByKey())
            .map(e -> {
                Map<String, Object> item = new HashMap<>();
                item.put("year", e.getKey());
                item.put("count", e.getValue());
                return item;
            })
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> countByCountry() {
        List<LaunchRecord> all = launchRecordRepository.findAll();
        Map<String, Long> countryCount = all.stream()
            .collect(Collectors.groupingBy(
                LaunchRecord::getCountry,
                Collectors.counting()
            ));
        
        return countryCount.entrySet().stream()
            .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
            .map(e -> {
                Map<String, Object> item = new HashMap<>();
                item.put("country", e.getKey());
                item.put("count", e.getValue());
                return item;
            })
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> countByStatus() {
        List<LaunchRecord> all = launchRecordRepository.findAll();
        Map<String, Long> statusCount = all.stream()
            .collect(Collectors.groupingBy(
                LaunchRecord::getStatus,
                Collectors.counting()
            ));
        
        return statusCount.entrySet().stream()
            .map(e -> {
                Map<String, Object> item = new HashMap<>();
                item.put("status", e.getKey());
                item.put("count", e.getValue());
                return item;
            })
            .collect(Collectors.toList());
    }

    private LaunchRecordDTO convertToDTO(LaunchRecord entity) {
        LaunchRecordDTO dto = new LaunchRecordDTO();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }

    private LaunchRecord convertToEntity(LaunchRecordDTO dto) {
        LaunchRecord entity = new LaunchRecord();
        BeanUtils.copyProperties(dto, entity);
        return entity;
    }
}
