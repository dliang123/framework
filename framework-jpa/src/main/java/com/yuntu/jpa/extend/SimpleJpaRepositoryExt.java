package com.yuntu.jpa.extend;

import com.yuntu.base.specification.ISpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.List;

/**
 * 扩展为支持 JpaSpecificationExecutorExt
 *              可设置事务为,来优化减少只读事务开启 @Transactional(propagation = Propagation.SUPPORTS)
 * @param <T>
 * @param <ID>
 */
@NoRepositoryBean
@Transactional(readOnly = true)
public class SimpleJpaRepositoryExt<T, ID extends Serializable>
        extends org.springframework.data.jpa.repository.support.SimpleJpaRepository<T, ID>
        implements JpaSpecificationExecutorExt<T> {



    public SimpleJpaRepositoryExt(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
    }

    public SimpleJpaRepositoryExt(Class<T> domainClass, EntityManager em) {
        super(domainClass, em);
    }

    @Override
    public T findOne(ISpecification<T> spec) {
        return super.findOne( SpecificationUtils.specification(spec) );
    }

    @Override
    public List<T> findAll(ISpecification<T> spec) {
        return super.findAll( SpecificationUtils.specification(spec) ) ;
    }

    @Override
    public Page<T> findAll(ISpecification<T> spec, Pageable pageable) {
        return super.findAll( SpecificationUtils.specification(spec),pageable ) ;
    }

    @Override
    public List<T> findAll(ISpecification<T> spec, Sort sort) {
        return super.findAll(SpecificationUtils.specification(spec),sort) ;
    }

    @Override
    public long count(ISpecification<T> spec) {
        return super.count( SpecificationUtils.specification(spec) ) ;
    }
}