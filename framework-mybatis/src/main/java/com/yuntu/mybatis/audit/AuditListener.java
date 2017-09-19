package com.yuntu.mybatis.audit;


import com.google.common.collect.Maps;
import com.yuntu.base.user.CurrentUserHelp;
import com.yuntu.mybatis.extend.EntityEvent;
import com.yuntu.mybatis.extend.EntityListener;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;

import java.io.Serializable;
import java.util.Date;

/*
 * Copyright (C), 2012-2014
 * Author:
 * Date:     14-11-27
 * Description: 实现审计功能的侦听器
 * History: 变更记录
 * <author>           <time>             <version>        <desc>
 *            14-11-27           00000001         创建文件
 *
 */
@SuppressWarnings({"rawtypes","unchecked"})
public class AuditListener implements EntityListener<AuditEntity> {

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(AuditListener.class);
    private int order;
    private java.util.Map<Class,Class> classPapping= Maps.newConcurrentMap();

    public AuditListener(){
        if( logger.isInfoEnabled() ){
            logger.info("实体审计侦听器启动!");
        }
    }

    public void beforeChange(EntityEvent<AuditEntity> event) {

        try{
			AuditEntity<Serializable> auditEntity=event.getSource();
            switch ( event.getTriggerType() ){
                case INSERT:{
                    auditEntity.setCreateTime(new Date());
                    auditEntity.setCreator(getCurrentUserID(auditEntity));
                }break;
                case UPDATE:{
                    auditEntity.setModifyTime(new Date());
                    auditEntity.setModifier(getCurrentUserID(auditEntity));
                }break;
			default:
				break;
            }
        }catch (Exception e){
            if (logger.isWarnEnabled()){
                logger.warn(e.getMessage(),e);
            }
        }

    }

    private Serializable getCurrentUserID(AuditEntity auditEntity){

        try{

            Class type = classPapping.get(auditEntity.getClass());
            if( null == type ){
                type = PropertyUtils.getPropertyType(auditEntity, "creator");
                classPapping.put(auditEntity.getClass(),type );
            }
            Object userId = CurrentUserHelp.getCurrentUserID();
            if( null != userId && userId.getClass() == type )
                return (Serializable)userId;
            return (Serializable) ConvertUtils.convert(userId, type);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public void afterChange(EntityEvent<AuditEntity> event) {

    }

    public static Logger getLogger() {
        return logger;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }


}
