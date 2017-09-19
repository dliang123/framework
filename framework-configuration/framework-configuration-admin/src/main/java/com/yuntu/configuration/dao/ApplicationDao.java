package com.yuntu.configuration.dao;

import com.yuntu.configuration.entity.ApplicationEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *  16/1/14.
 */
@Repository
public interface ApplicationDao extends JpaRepository<ApplicationEntity,Long> {
    Page<ApplicationEntity> findByKeyLike(Pageable pageable,String key);
}
