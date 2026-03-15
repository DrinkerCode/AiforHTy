package com.space.ai.repository;

import com.space.ai.entity.LaunchSite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 发射场数据访问层
 */
@Repository
public interface LaunchSiteRepository extends JpaRepository<LaunchSite, Long>, JpaSpecificationExecutor<LaunchSite> {

    /**
     * 按国家查询
     */
    List<LaunchSite> findByCountry(String country);

    /**
     * 按状态查询
     */
    List<LaunchSite> findByStatus(String status);
}
