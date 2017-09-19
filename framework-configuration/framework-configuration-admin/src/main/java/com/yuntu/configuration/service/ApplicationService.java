package com.yuntu.configuration.service;

import com.yuntu.configuration.entity.ApplicationEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Created by linxiaohui on 16/1/14.
 */
public interface ApplicationService {


    void saveOrUpdate(ApplicationEntity application);


    Page<ApplicationEntity> find(Pageable pageable,String key);
}
