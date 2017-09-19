package com.yuntu.jpa.extend;

import com.yuntu.base.specification.ISpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * 扩展支持 ISpecification
 *  2016/10/31.
 */
public interface JpaSpecificationExecutorExt<T> extends JpaSpecificationExecutor<T> {

    /**
     * Returns a single entity matching the given {@link Specification}.
     *
     * @param spec
     * @return
     */
    T findOne(ISpecification<T> spec);

    /**
     * Returns all entities matching the given {@link Specification}.
     *
     * @param spec
     * @return
     */
    List<T> findAll(ISpecification<T> spec);

    /**
     * Returns a {@link Page} of entities matching the given {@link Specification}.
     *
     * @param spec
     * @param pageable
     * @return
     */
    Page<T> findAll(ISpecification<T> spec, Pageable pageable);

    /**
     * Returns all entities matching the given {@link Specification} and {@link Sort}.
     *
     * @param spec
     * @param sort
     * @return
     */
    List<T> findAll(ISpecification<T> spec, Sort sort);

    /**
     * Returns the number of instances that the given {@link Specification} will return.
     *
     * @param spec the {@link Specification} to count instances for
     * @return the number of instances
     */
    long count(ISpecification<T> spec);
}
