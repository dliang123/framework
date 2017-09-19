package com.yuntu.jpa;

import org.springframework.transaction.support.TransactionSynchronizationManager;

public class TransactionUtils {

    /**
     * 存在非只读事务
     * @return
     */
    public static boolean isCurrentTransactionNotReadOnly(){
       return isActualTransactionActive()
               && !TransactionSynchronizationManager.isCurrentTransactionReadOnly();
    }

    /**
     * 存在事务
     * @return
     */
    public static boolean isActualTransactionActive(){
        return TransactionSynchronizationManager.isActualTransactionActive();
    }

    /**
     * 存在只读事务
     * @return
     */
    public static boolean isCurrentTransactionReadOnly(){
        return isActualTransactionActive() && TransactionSynchronizationManager.isActualTransactionActive();
    }
}