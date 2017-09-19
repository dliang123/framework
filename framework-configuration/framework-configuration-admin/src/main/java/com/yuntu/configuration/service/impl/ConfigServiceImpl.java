package com.yuntu.configuration.service.impl;

import com.yuntu.base.Application;
import com.yuntu.configuration.ConfigPathUtils;
import com.yuntu.configuration.dao.ApplicationDao;
import com.yuntu.configuration.dao.ConfigDao;
import com.yuntu.configuration.entity.ApplicationEntity;
import com.yuntu.configuration.entity.ConfigEntity;
import com.yuntu.configuration.entity.ConfigId;
import com.yuntu.configuration.service.ConfigService;
import com.yuntu.zookeeper.ZookeeperHelp;
import org.apache.curator.framework.CuratorFramework;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;

/**
 *  16/1/25.
 */
@Service
public class ConfigServiceImpl implements ConfigService {

    @Autowired
    private ConfigDao configDao;
    @Autowired
    private ApplicationDao applicationDao;
    @Autowired
    private CuratorFramework curatorClient;

    @Override
    @Transactional(rollbackFor = Exception.class,isolation = Isolation.SERIALIZABLE)
    public void saveOrUpdate(ConfigEntity configEntity) throws Exception {


        Assert.notNull(configEntity.getApplicationId());
        Assert.notNull(configEntity.getKey());
        Assert.notNull(configEntity.getValue());

        ApplicationEntity applicationEntity =applicationDao.findOne(configEntity.getApplicationId());
        Assert.notNull(applicationEntity);

        ConfigEntity _config = configDao.findOne(new ConfigId(applicationEntity.getId(),configEntity.getKey()));
        if( null != _config){ //更新
            _config.setValue( configEntity.getValue() );
            _config.setDescribe( configEntity.getDescribe() );
        } else {    //插入
            _config = configEntity;
        }

        Application application = new Application( applicationEntity.getKey(),applicationEntity.getCluster() );
        String path = ConfigPathUtils.configPath(application);
        ZookeeperHelp.waitForStarted(curatorClient);


        if(  curatorClient.checkExists().forPath(path+"/"+configEntity.getKey()) !=null ){
            curatorClient.setData().forPath(path+"/"+configEntity.getKey(),configEntity.getValue().getBytes());
        }else {
            curatorClient.create().creatingParentsIfNeeded().forPath(path+"/"+configEntity.getKey(),configEntity.getValue().getBytes());
        }

        configDao.saveAndFlush(_config);


    }

    @Override
    public Page<ConfigEntity> find(Pageable pageable, String key) {
        return null;
    }

    @Override
    public Page<ConfigEntity> findByApplicationId(Pageable pageable, Long cluster) {
        return configDao.findByApplicationId(pageable,cluster);
    }

    @Override
    public List<ConfigEntity> findByApplicationId(Long cluster) {
        return configDao.findByApplicationId(cluster);
    }

    @Override
    public ConfigEntity findById(ConfigId id) {
        ConfigEntity configEntity = configDao.findOne(id);
        return configEntity;
    }


}
