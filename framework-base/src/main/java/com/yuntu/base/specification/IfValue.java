package com.yuntu.base.specification;

/**
 * Created by linxiaohui on 2017/4/19.
 */
public interface IfValue {

    boolean ifValue(Expression expression);

    IfValue ifValueNull = ( e -> null == e || null == e.getValue() );
    IfValue ifValueNotNull = (e -> !ifValueNull.ifValue(e));
    IfValue ifValueBlank = (e -> ifValueNull.ifValue(e) || ( (e.getValue() instanceof String) && ((String)e.getValue()).trim().isEmpty() ) );
    IfValue ifValueNotBlank = (e -> !ifValueBlank.ifValue(e));
}
