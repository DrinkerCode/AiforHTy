package com.space.ai.repository;

import com.space.ai.entity.SatelliteData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 卫星数据数据访问层
 */
@Repository
public interface SatelliteDataRepository extends JpaRepository<SatelliteData, Long>, JpaSpecificationExecutor<SatelliteData> {

    /**
     * 按国家查询
     */
    List<SatelliteData> findByCountry(String country);

    /**
     * 按卫星类型查询
     */
    List<SatelliteData> findBySatelliteType(String satelliteType);

    /**
     * 按轨道类型查询
     */
    List<SatelliteData> findByOrbitType(String orbitType);

    /**
     * 按是否活跃查询
     */
    List<SatelliteData> findByActive(Boolean active);

    /**
     * 按国家和类型查询
     */
    List<SatelliteData> findByCountryAndSatelliteType(String country, String satelliteType);
}
