package com.yuntu.jpa;

import com.yuntu.base.SimpleCompensationTemplate;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

/**
 * Created by linxiaohui on 15/12/8.
 */
public abstract class ObjectOptimisticLockingCompensation<T> implements SimpleCompensationTemplate<T> {

    @SuppressWarnings("unchecked")
	private static final Class<? extends Exception> [] retriesFor = new Class[]{ObjectOptimisticLockingFailureException.class};
    private int retries=3;
    public ObjectOptimisticLockingCompensation(){

    }

    public ObjectOptimisticLockingCompensation(int retries){
        this.retries=3;
    }

    public int retries() {
        return retries;
    }

    public Class<? extends Exception>[] retriesFor() {
        return retriesFor;
    }

}
