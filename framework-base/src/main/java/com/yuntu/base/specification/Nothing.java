package com.yuntu.base.specification;

/**
 * Created by linxiaohui on 2017/4/19.
 */
public class Nothing implements ISpecification {

    public static final Nothing NOTHING = new Nothing();

    private Nothing(){

    }

    @Override
    public boolean isNegated() {
        return false;
    }

    @Override
    public ISpecification not(boolean isNew) {
        return this;
    }

    @Override
    public boolean evaluate(Object context) {
        return true;
    }
}
