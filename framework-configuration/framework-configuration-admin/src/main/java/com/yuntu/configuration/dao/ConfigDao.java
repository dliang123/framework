package com.yuntu.configuration.dao;

import com.yuntu.configuration.entity.ConfigEntity;
import com.yuntu.configuration.entity.ConfigId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by linxiaohui on 16/1/14.
 */
@Repository
public interface ConfigDao extends JpaRepository<ConfigEntity,ConfigId>{

    Page<ConfigEntity> findByApplicationId(Pageable pageable, Long cluster);

    List<ConfigEntity> findByApplicationId(Long cluster);
}
