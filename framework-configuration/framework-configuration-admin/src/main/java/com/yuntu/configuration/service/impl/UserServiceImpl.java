package com.yuntu.configuration.service.impl;

import com.yuntu.common.utils.DigestUtils;
import com.yuntu.configuration.dao.UserDao;
import com.yuntu.configuration.entity.UserEntity;
import com.yuntu.configuration.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *  16/1/14.
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;


    public UserEntity findUserByUserNameAndPassword(String username, String password) {

        return userDao.findUserByUserNameAndPassword(username, DigestUtils.digest(password, DigestUtils.Digest.MD5_32));
    }

    @Transactional(rollbackFor = {Exception.class})
    public void saveOrUpdate(UserEntity userEntity) {

        userEntity.setPassword(DigestUtils.digest(userEntity.getPassword(), DigestUtils.Digest.MD5_32));
        userDao.save(userEntity);


    }

    public Page<UserEntity> find(Pageable pageable, String name) {

        if( null == name || name.trim().length()<1 )
            return userDao.findAll(pageable);
        return userDao.findByNameLike(pageable,"%"+name+"%");
    }
}
