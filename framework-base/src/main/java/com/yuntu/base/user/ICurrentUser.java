package com.yuntu.base.user;

import java.io.Serializable;

/**
 * 获取当前登陆用户的 id
 * @param <ID>
 */
public interface ICurrentUser<ID extends Serializable,USER extends Serializable> {

    ID currentUserID();

    USER currentUser();
}
