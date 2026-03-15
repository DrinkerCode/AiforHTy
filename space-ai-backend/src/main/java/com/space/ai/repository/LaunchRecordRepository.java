package com.space.ai.repository;

import com.space.ai.entity.LaunchRecord;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 火箭发射记录数据访问层
 */
@Repository
public interface LaunchRecordRepository extends JpaRepository<LaunchRecord, Long>, JpaSpecificationExecutor<LaunchRecord> {

    /**
     * 按国家查询
     */
    List<LaunchRecord> findByCountry(String country);

    /**
     * 按火箭型号查询
     */
    List<LaunchRecord> findByRocketType(String rocketType);

    /**
     * 按状态查询
     */
    List<LaunchRecord> findByStatus(String status);

    /**
     * 按时间范围查询
     */
    List<LaunchRecord> findByLaunchTimeBetween(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 按国家和时间范围查询
     */
    List<LaunchRecord> findByCountryAndLaunchTimeBetween(String country, LocalDateTime startTime, LocalDateTime endTime);
}
