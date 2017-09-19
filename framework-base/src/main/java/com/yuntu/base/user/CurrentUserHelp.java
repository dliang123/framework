package com.yuntu.base.user;


import java.io.Serializable;

/**
 * 获取当前用户 id 的工具类
 */
public class CurrentUserHelp {

    private static ThreadLocal<Serializable> UID = new ThreadLocal<Serializable>() {};
    private static ICurrentUser<?, ?> delegate;

    public static void setCurrentUserID(Serializable userID){
        UID.set(userID);
    }

    public static Serializable getCurrentUserID(){

        if( null != delegate && null != delegate.currentUserID()){
            return delegate.currentUserID();
        }
        return CurrentUserHelp.UID.get();
    }

    public static Serializable getCurrentUse(){

        if( null != delegate && null != delegate.currentUser()){
            return delegate.currentUser();
        }
        return CurrentUserHelp.UID.get();
    }

    public static ICurrentUser<?, ?> getDelegate() {
        return delegate;
    }

    public static void setDelegate(ICurrentUser<?, ?> delegate) {
        CurrentUserHelp.delegate = delegate;
    }
}
