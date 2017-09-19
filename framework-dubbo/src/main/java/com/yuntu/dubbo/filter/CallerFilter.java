package com.yuntu.dubbo.filter;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.*;
import com.yuntu.base.user.CurrentUserHelp;


@Activate(group = Constants.CONSUMER, order = 20000)
public class CallerFilter implements Filter {

    public static final String CALLER="CALLER";

    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {

        try{
            if( invocation instanceof RpcInvocation ){
                RpcInvocation rpcInvocation = (RpcInvocation) invocation;
                if( null == rpcInvocation.getAttachment(CALLER) ){
                    Object UID = CurrentUserHelp.getCurrentUserID();
                    if( null != UID ){
                        rpcInvocation.setAttachment(CALLER, String.valueOf(UID));
                    }
                }
            }
        }catch(Exception e) {};
            
        
        return invoker.invoke(invocation);
    }
}