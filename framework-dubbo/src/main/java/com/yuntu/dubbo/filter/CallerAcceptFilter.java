package com.yuntu.dubbo.filter;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.*;
import com.yuntu.base.user.CurrentUserHelp;

/*
 * Copyright (C), 2012-2014
 * Author:
 * Date:     14-11-28
 * Description: 模块目的、功能描述      
 * History: 变更记录
 * <author>           <time>             <version>        <desc>
 * Administrator           14-11-28           00000001         创建文件
 *
 */
@Activate(group = Constants.PROVIDER, order = 20000)
public class CallerAcceptFilter implements Filter {


    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {

        try{
            String caller = invocation.getAttachment(CallerFilter.CALLER);
            if( null !=caller ){
                CurrentUserHelp.setCurrentUserID(caller);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return invoker.invoke(invocation);
    }
}
