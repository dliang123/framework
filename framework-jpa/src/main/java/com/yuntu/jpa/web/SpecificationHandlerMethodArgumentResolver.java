package com.yuntu.jpa.web;

import com.google.common.base.Enums;
import com.google.common.base.Optional;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.yuntu.base.KeyValue;
import com.yuntu.base.specification.Expression;
import com.yuntu.base.specification.Operator;
import com.yuntu.base.specification.SpecificationComposite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Map;

/**
 * Specification 参数转换 , 支持 parameterName.specification  和 ParameterName[specification] 方式
 *
 * Specification 前端输入格式为
 *          field + #separator + Expression.Operator + ( #separator + group )括号内可为空
 * Group logic   前端输入为
 *          groupParameterName + SpeJunction.Logic
 *
 * 假设 separator = "_" , groupParameterName = "group_"
 *      例1
 *          parameterName =  name_EQ , value = 小明
 *          则查询为   where name = '小明' 的数据
 *      例2
 *          parameterName =  name_EQ_1 , value = 小明
 *          parameterName =  name_EQ_1 , value = 小红
 *          parameterName =  group_1 ,   value = OR
 *          则查询为  where ( name = '小明' OR name = '小红')
 *
 *
 *  2016/10/26.
 */
public class SpecificationHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {


    private static final Logger logger = LoggerFactory.getLogger(SpecificationHandlerMethodArgumentResolver.class);
    /** 默认 defaultGroup (分组) **/
    private static final String DEFAULT_GROUP = "0";
    /** 默认 defaultGroup logic (分组 逻辑) 参数名 **/
    private static final String DEFAULT_LOGIC_PARAMETER_NAME = "group";
    /** 默认 specification(规则) 分割符号 **/
    private static final String DEFAULT_SEPARATOR = "_";

    /** 默认 defaultGroup (分组) **/
    private String defaultGroup = DEFAULT_GROUP;
    /** 分组的逻辑参数名 (分组)  **/
    private String logicParameterName = DEFAULT_LOGIC_PARAMETER_NAME;
    /** specification(规则) 分割符号 **/
    private String separator = DEFAULT_SEPARATOR;


    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return SpecificationComposite.class.equals(parameter.getParameterType());
    }

    @Override
    public SpecificationComposite resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        //获取参数的变量名称
        String variableName = parameter.getParameterName();
        //分组 SpeJunction
        Map<String,SpecificationComposite> map = Maps.newConcurrentMap();
        //迭代所有参数名
        webRequest.getParameterNames().forEachRemaining(e -> {

            //参数名
            String name = parseSpeParameterName(variableName,e);
            //转换为 {key : groupName , value : expression }  格式
            KeyValue<String, Expression> keyValue = convert( name , webRequest.getParameter(e));
            //排空
            if (null != keyValue) {
                String groupName = keyValue.getKey();
                if (!map.containsKey(groupName)) {
                    SpecificationComposite group = byGroupName(groupName, webRequest);
                    map.put(groupName, group);
                }
                SpecificationComposite speJunction = map.get(groupName);
                speJunction.add(keyValue.getValue());
            }

        });
        return SpecificationComposite.and( map.values() );
    }

    /**
     * 解析为符合规则的参数名称,不符合规则的话返回 null
     * @param variableName      变量名称
     * @param parameterName     解析前的参数名
     * @return                  解析后的符合规则参数名,不符合规则返回 null
     */
    private String parseSpeParameterName(String variableName,String parameterName){

        //不符合返回 null
        if( Strings.isNullOrEmpty( parameterName ) ){
            return null;
        }
        //符合 variableName. 模式
        if( parameterName.startsWith(variableName+".") ){
            return parameterName.replace(variableName + ".","");
        }
        //符合 variableName[] 模式
        if( parameterName.matches(variableName +"\\[(\\S*)\\]") ){
            return parameterName.replaceAll(variableName +"\\[(\\S*)\\]","$1");
        }
        return null;
    }

    /** 根据分组名称生成 SpeJunction **/
    private SpecificationComposite byGroupName(String groupName, NativeWebRequest webRequest){

        //获取 分组值
        String value = webRequest.getParameter( logicParameterName + separator + groupName );
        if( Strings.isNullOrEmpty( value ) ){
            value = webRequest.getParameter(logicParameterName + "[" + groupName + "]");
        }
        //根据值生产 speJunction
        SpecificationComposite speJunction = SpecificationComposite.logic(value);
        //打印日志
        logger.debug("groupName {} logic {} value {}",groupName,speJunction.logic(),value);
        //返回
        return speJunction;
    }

    //转换为 {key : groupName , value : expression }
    private KeyValue convert(String name, String value){

        if( !StringUtils.hasText( name ) || !StringUtils.hasText(value)){
            return null;
        }

        if( value != null ){
            value = value.trim();
        }
        logger.debug("specificationComposite name:{},value:{} ",name,value);

        //根据分割符分割
        String [] filter = name.split(separator);
        if( filter.length < 2 ){
            logger.warn("specificationComposite name:{} 不符合规则",name);
            return null;
        }

        Optional<Operator> optional = Enums.getIfPresent(Operator.class,filter[1].toUpperCase());
        if( !optional.isPresent() ){
            logger.warn("specificationComposite name:{} 中 {} 不符合规则",name,filter[1]);
            return null;
        }

        //如果传了分组,则使用分组,没传入使用默认分组
        String groupName = filter.length > 2 ? filter[2] : defaultGroup;
        //生成规则
        Expression expression = Expression.expression(filter[0],optional.get(),value);
        //返回
        return new KeyValue( groupName , expression );
    }

    public String getDefaultGroup() {
        return defaultGroup;
    }

    public void setDefaultGroup(String defaultGroup) {
        this.defaultGroup = defaultGroup;
    }

    public String getLogicParameterName() {
        return logicParameterName;
    }

    public void setLogicParameterName(String logicParameterName) {
        this.logicParameterName = logicParameterName;
    }

    public String getSeparator() {
        return separator;
    }

    public void setSeparator(String separator) {
        this.separator = separator;
    }
}
