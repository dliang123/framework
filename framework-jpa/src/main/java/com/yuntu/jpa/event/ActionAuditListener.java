package com.yuntu.jpa.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PreDestroy;
import javax.persistence.PostLoad;
import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

/**
 * 事件侦听器
 */
public class ActionAuditListener  {

    private static final Logger logger = LoggerFactory.getLogger(ActionAuditListener.class);

    @PostLoad
    private void postLoad(Object entity){

    }

    @PostPersist
    private void postPersist(Object entity) {
        //判断是否是操作审计实体
        notice(entity, OperateType.create);

    }

    @PostRemove
    private void PostRemove(Object entity){

        notice(entity, OperateType.remove);
    }


    @PostUpdate
    private void PostUpdate(Object entity){

        notice(entity, OperateType.update);
    }


    private void notice(Object entity, OperateType type) {

        logger.info("{} 执行了 {} 操作",entity,type.getDescription());
        ActionEventManager.notice( new ActionEvent(type,entity) );
    }
}