package com.yuntu.base.specification;

public interface ISpecification<T> {

    /**
     * 是否为否定的规则
     * @return
     */
    boolean isNegated();

    /**
     * 否定
     * @return
     */
    default ISpecification not(){
        return not(false);
    };

    /**
     * 否定
     * @param isNew 当为 true 时,返回一个新的(否定属性的)实体,原实体不变
     * @return
     */
    ISpecification not(boolean isNew);

    /**
     * 获取运算结果
     * @param context
     * @return
     */
    boolean evaluate(Object context);
}


