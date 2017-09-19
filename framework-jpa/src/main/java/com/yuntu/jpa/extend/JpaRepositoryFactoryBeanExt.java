package com.yuntu.jpa.extend;

import org.springframework.data.repository.core.support.RepositoryFactorySupport;

import javax.persistence.EntityManager;

/****
 * 扩展为 使用 JpaRepositoryFactoryExt
 */
public class JpaRepositoryFactoryBeanExt extends org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean{

    protected RepositoryFactorySupport createRepositoryFactory(EntityManager entityManager) {
        return new JpaRepositoryFactoryExt(entityManager);
    }
}