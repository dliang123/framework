package com.yuntu.jpa;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import org.hibernate.engine.query.spi.HQLQueryPlan;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.jpa.internal.metamodel.EntityTypeImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;
import javax.persistence.metamodel.SingularAttribute;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Map;

@Component
public class HibernateUtils {

    private static final Logger logger = LoggerFactory.getLogger(HibernateUtils.class);

    @PersistenceContext
    private EntityManager entityManager;
    private Map<Class,String> tableNames = Maps.newConcurrentMap();

    /**
     * 解析hql 为 sql
     * @param hql
     * @return
     */
    public String parseHqlToSql(String hql){
        return parseHqlToSqlArray(hql)[0];
    }

    /**
     * 解析hql 为 sql 数组
     * @param hql
     * @return
     */
    public String [] parseHqlToSqlArray(String hql){
        return getHQLQueryPlan(hql).getSqlStrings();
    }

    /**
     * 获取 hql 执行计划
     * @param hql
     * @return
     */
    public HQLQueryPlan getHQLQueryPlan(String hql){

        SessionImplementor session =(SessionImplementor) entityManager.getDelegate();
        // 获取 hql 执行计划
        // 步骤 从缓存中查看是否已有相同语句的执行计划 没有 建立新执行计划 并放入缓存
        // 执行计划为 final 修饰 防止 在它处 被更改 影响 hql 查出来的结果
        final HQLQueryPlan queryPlan = session.getFactory().getQueryPlanCache().getHQLQueryPlan(
                hql,
                false,
                session.getLoadQueryInfluencers().getEnabledFilters()
        );
        // 判断此执行计划 是否 有 更新或删除操作 有 抛出异常
        if ( queryPlan.getTranslators()[0].isManipulationStatement() ) {
            throw new IllegalArgumentException( "Update/delete queries cannot be typed" );
        }

        return queryPlan;
    }

    public Serializable findId(Object entity){
        if( null == entity ){
            return null;
        }
        try {
            return findByMetaModel(entity);
        }catch (Throwable throwable){
            logger.warn("",throwable);
            SessionImplementor session = (SessionImplementor) entityManager.getDelegate();
            return session.getContextEntityIdentifier(entity);
        }
    }

    private Serializable findByMetaModel(Object entity){
        if( null == entity ){
            return null;
        }
        try {
            EntityTypeImpl type = (EntityTypeImpl)entityManager.getMetamodel().entity( entity.getClass() );
            SingularAttribute idAttribute = type.getId( type.getIdType().getJavaType() );
            Field id = (Field)idAttribute.getJavaMember();
            return (Serializable) id.get(entity);
        } catch (Throwable e) {
            logger.warn("",e);
            return null;
        }
    }

    public String getTableNameByEntity(Object entity){

        return getTableNameByEntityClass((Class<? extends Serializable>) entity.getClass());

    }

    private String getTableNameByEntityClass(Class<? extends Serializable> entityClass){

        if( null == entityClass)
            return null;
        String name = tableNames.get(entityClass);
        if( Strings.isNullOrEmpty(name) ){
            Table table=entityClass.getAnnotation(Table.class);
            if( null == table )
                return null;
            tableNames.put(entityClass,table.name());
        }
        return tableNames.get(entityClass);

    }

    public static void main(String[] args) {

     //   Object obj = AnnotationUtils.findAnnotation(Id.class, PurchaseOrderEntity.class,true);

    }
}