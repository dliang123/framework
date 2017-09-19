/**
 * <p>Copyright:Copyright(c) 2016</p>
 * <p>Company:上海运图投资有限公司</p>
 * <p>包名:com.yuntu.common.json.jackson.databind</p>
 * <p>文件名:CamelCaseStrategy.java</p>
 * <p>类更新历史信息</p>
 * @todo <a href="mailto:fankenie@yaomaiche.com">vernal(聂超)</a> 创建于 2016年3月11日 下午2:08:07
 */
package com.yuntu.common.json.jackson.databind;

import com.fasterxml.jackson.databind.PropertyNamingStrategy.PropertyNamingStrategyBase;

/** 
 * 驼峰命名策略
 * <p>Company:上海运图投资有限公司</p>
 * @author <a href="mailto:fankenie@yaomaiche.com">vernal(聂超)</a> 
 * @date 2016年3月11日 下午2:08:07 
 * @version 1.0.2016
 */
public class CamelCaseStrategy extends PropertyNamingStrategyBase{

	private static final long serialVersionUID = -3065044897229147519L;

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.PropertyNamingStrategy.PropertyNamingStrategyBase#translate(java.lang.String)
	 * @author <a href="mailto:fankenie@yaomaiche.com">vernal(聂超)</a>
	 * @date 2016年3月11日 下午2:08:43
	 */
	@Override
	public String translate(String input) {
        if (input == null || input.length() == 0){
            return input;
        }
        char c = input.charAt(0);
        char uc = Character.toLowerCase(c);
        if (c == uc) {
            return input;
        }
        StringBuilder sb = new StringBuilder(input);
        sb.setCharAt(0, uc);
        return sb.toString();
	}

}
