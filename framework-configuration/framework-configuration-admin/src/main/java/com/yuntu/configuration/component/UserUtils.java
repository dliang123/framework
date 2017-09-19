package com.yuntu.configuration.component;

import com.yuntu.base.user.CurrentUserHelp;
import com.yuntu.base.user.ICurrentUser;
import com.yuntu.configuration.entity.UserEntity;
import com.yuntu.web.util.WebUtils;
import org.springframework.data.domain.AuditorAware;

/**
 * Created by linxiaohui on 16/1/14.
 */
public class UserUtils implements ICurrentUser<Long,UserEntity> , AuditorAware<Long> {

    public static final String USER_ID_KE="USER_ID_KE";

    public UserUtils(){
        CurrentUserHelp.setDelegate(this);
    }

    public Long currentUserID() {
        return (Long)WebUtils.getSession().getAttribute(USER_ID_KE);
    }

    public UserEntity currentUser() {
        throw new RuntimeException("不支持此方法");
    }

    public Long getCurrentAuditor() {
        return currentUserID();
    }
}
