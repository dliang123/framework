package com.yuntu.configuration.dao;

import com.yuntu.configuration.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *  16/1/14.
 */
@Repository
public interface UserDao extends JpaRepository<UserEntity,Long> {

    UserEntity findUserByUserNameAndPassword(String userName,String password);

    Page<UserEntity> findByNameLike(Pageable pageable,String name);
}
