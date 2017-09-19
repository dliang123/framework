package com.yuntu.configuration.service.impl;

import com.yuntu.configuration.dao.ApplicationDao;
import com.yuntu.configuration.entity.ApplicationEntity;
import com.yuntu.configuration.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *  16/1/14.
 */
@Service
public class ApplicationServiceImpl implements ApplicationService{


    @Autowired
    private ApplicationDao applicationDao;

    @Transactional
    public void saveOrUpdate(ApplicationEntity application) {
        applicationDao.save(application);
    }

    public Page<ApplicationEntity> find(Pageable pageable, String key) {
        if( null !=key && key.trim().length()<1  ){
            return applicationDao.findByKeyLike(pageable,"%"+key+"%");
        }
        return applicationDao.findAll(pageable);
    }
}
