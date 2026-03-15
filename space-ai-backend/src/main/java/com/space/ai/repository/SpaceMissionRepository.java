package com.space.ai.repository;

import com.space.ai.entity.SpaceMission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 航天任务数据访问层
 */
@Repository
public interface SpaceMissionRepository extends JpaRepository<SpaceMission, Long>, JpaSpecificationExecutor<SpaceMission> {

    /**
     * 按国家查询
     */
    List<SpaceMission> findByCountry(String country);

    /**
     * 按任务类型查询
     */
    List<SpaceMission> findByMissionType(String missionType);

    /**
     * 按状态查询
     */
    List<SpaceMission> findByStatus(String status);

    /**
     * 按国家和类型查询
     */
    List<SpaceMission> findByCountryAndMissionType(String country, String missionType);
}
