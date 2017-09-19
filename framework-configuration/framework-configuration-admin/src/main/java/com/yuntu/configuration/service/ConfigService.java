package com.yuntu.configuration.service;

import com.yuntu.configuration.entity.ConfigEntity;
import com.yuntu.configuration.entity.ConfigId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by linxiaohui on 16/1/25.
 */
public interface ConfigService {

    void saveOrUpdate(ConfigEntity application) throws Exception;

    Page<ConfigEntity> find(Pageable pageable, String key);

    Page<ConfigEntity> findByApplicationId(Pageable pageable, Long cluster);

    List<ConfigEntity> findByApplicationId(Long cluster);

    ConfigEntity findById(ConfigId id);
}
