package com.yuntu.configuration.service;

import com.yuntu.configuration.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Created by linxiaohui on 16/1/14.
 */
public interface UserService {

    UserEntity findUserByUserNameAndPassword(String username,String password);

    void saveOrUpdate(UserEntity userEntity);

    Page<UserEntity> find(Pageable pageable,String name);
}
