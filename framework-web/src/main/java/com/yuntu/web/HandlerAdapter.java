package com.yuntu.web;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.MethodParameter;
import org.springframework.core.Ordered;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Sets;
import com.yuntu.base.Response;
import com.yuntu.base.utils.TemplateUtils;
import com.yuntu.common.utils.JSONUtils;
import com.yuntu.web.processor.CacheProcessor;
import com.yuntu.web.processor.DegradeProcessor;
import com.yuntu.web.processor.ResponseProcessor;
import com.yuntu.web.request.BodyReaderHttpServletRequestWrapper;
import com.yuntu.web.util.ApplicationContextUtils;

/**
 * Created by linxiaohui on 15/11/12.
 */
public class HandlerAdapter extends RequestMappingHandlerAdapter {

    private static final Logger LOG = LoggerFactory.getLogger(HandlerAdapter.class);

    private boolean includeDefaults=true;

    private CacheProcessor cacheProcessor = null;
    private DegradeProcessor degradeProcessor = null;
    private ResponseProcessor responseProcessor = null;
    private Set<Method> responseBodySet = null;
    private Set<Method> requestBodySet = null;
    private List<String> skipCustomHandlerAdapterWithMethod = new ArrayList<String>();


	public HandlerAdapter(){

        //初始化默认排序
        setOrder(Ordered.LOWEST_PRECEDENCE -1);
        //初始化缓存处理器
        cacheProcessor = new CacheProcessor();
        //初始化降级处理器
        degradeProcessor = new DegradeProcessor();
        //初始化结果判断集合(@ResponseBody)
        responseBodySet = Sets.newConcurrentHashSet();
        //初始化请求体判断集合
        requestBodySet = Sets.newConcurrentHashSet();
        //初始化默认响应转换器
        responseProcessor = new DefaultResponseProcessor();
    }


    @Override
    protected ModelAndView handleInternal(HttpServletRequest request, HttpServletResponse response, HandlerMethod handlerMethod) throws Exception {

        if( requestBodySet.contains(handlerMethod.getMethod()) && !(request instanceof BodyReaderHttpServletRequestWrapper)){
            return super.handleInternal( new BodyReaderHttpServletRequestWrapper(request) , response, handlerMethod);
        }
        return super.handleInternal(request,response,handlerMethod);
    }

    protected ServletInvocableHandlerMethod createInvocableHandlerMethod(final HandlerMethod handlerMethod) {
    	if(skipCustomHandlerAdapterWithMethod.contains(getHandlerMethodFullName(handlerMethod))){
    		return super.createInvocableHandlerMethod(handlerMethod);
    	}

        return new ServletInvocableHandlerMethod(handlerMethod){

            public Object invokeForRequest(NativeWebRequest request, ModelAndViewContainer mavContainer, Object... providedArgs) throws Exception {

                Object value = null;
                Exception exception = null;
                Method method = handlerMethod.getMethod();

                try {
                    //获取缓存结果
                    value = cacheProcessor.methodValue(method);

                    if (null != value) { //获取缓存结果成功
                        saveResultAndType(request,value,ResultType.cache);
                    }else { //没有缓存结果
                        value = super.invokeForRequest(request, mavContainer, providedArgs);
                        saveResultAndType(request,value,ResultType.execute);

                        if( !(value instanceof Response) || ((Response<?>)value).isSuccess()){
                            cacheProcessor.methodValue(method, value);
                            degradeProcessor.methodValue(method, value);
                        }
                    }
                } catch (Exception e) {

                    //保存异常信息到请求域
                    request.setAttribute(Constants.EXCEPTION_KEY,e,RequestAttributes.SCOPE_REQUEST);
                    value = degradeProcessor.degradeForException(e, method);
                    if (value != null) {//降级成功
                        saveResultAndType(request,value,ResultType.degrade);
                    }else { //降级失败
                        exception = e;
                    }
                }finally {

                    //获取响应结果类型
                    ResponseType type = responseBodySet.contains(method)
                            ? ResponseType.responseBody
                            : ResponseType.viewAndModel;
                    //保存到 request 作用域
                    request.setAttribute(Constants.RESPONSE_TYPE_KEY, type, RequestAttributes.SCOPE_REQUEST);


					if (null != responseProcessor) { // 结果转换

						value = (null == exception)
								? responseProcessor.processor(value, type)
								: responseProcessor.processorForException(exception, type);
					}

                }

                request.setAttribute(Constants.RESULT_FINAL_KEY, value, RequestAttributes.SCOPE_REQUEST);
                //返回结果
                return value;
            }
        };
    }


    private Object saveResultAndType(NativeWebRequest request,Object value,ResultType type){
        request.setAttribute(Constants.RESULT_KEY, value, RequestAttributes.SCOPE_REQUEST);
        request.setAttribute(Constants.RESULT_TYPE_KEY, type, RequestAttributes.SCOPE_REQUEST);
        return value;
    }


    protected void initApplicationContext(ApplicationContext context) {

        super.initApplicationContext(context);

        Collection<HandlerMethod> handlerMethods = ApplicationContextUtils.findHandlerMethods(context);

        try {
            cacheProcessor = context.getBean(CacheProcessor.class);
        }catch (Exception e){

        }

        try {
            cacheProcessor.init(context,handlerMethods);
        }catch (Exception e){
            LOG.warn("缓存处理器初始化失败!~",e);
        }

        try {
            degradeProcessor.init(context,handlerMethods);
        }catch (Exception e){
            LOG.warn("降级处理器初始化失败!~",e);
        }

        try {
            responseProcessor = context.getBean(ResponseProcessor.class);
        }catch (Exception e){
            LOG.warn("没有找到结果转换器!~");
        }

        try {
            if(!ObjectUtils.isEmpty(handlerMethods)){

                for(HandlerMethod handlerMethod : handlerMethods){
                    if( null != handlerMethod.getMethodAnnotation(ResponseBody.class)
                            || null != handlerMethod.getBean().getClass().getAnnotation(RestController.class) ){
                        responseBodySet.add(handlerMethod.getMethod());
                    }
                }
            }
        }catch (Exception e){
            LOG.warn("",e);
        }

        try {
            if(!ObjectUtils.isEmpty(handlerMethods)){
                for(HandlerMethod handlerMethod : handlerMethods){
                    if( hasAnnotation(RequestBody.class,handlerMethod,true) ){
                        requestBodySet.add(handlerMethod.getMethod());
                    }
                }
            }
        }catch (Exception e){
            LOG.warn("",e);
        }


        try {
            Map<String,RequestMappingHandlerAdapter> map = context.getBeansOfType(RequestMappingHandlerAdapter.class);
            for(RequestMappingHandlerAdapter adapter : map.values()){
                if( adapter.getClass() == RequestMappingHandlerAdapter.class ){

                    this.setWebBindingInitializer(adapter.getWebBindingInitializer());
                    this.setInitBinderArgumentResolvers(adapter.getInitBinderArgumentResolvers());
                    this.setMessageConverters(adapter.getMessageConverters());
                    if( includeDefaults ){
                        this.setCustomArgumentResolvers(adapter.getCustomArgumentResolvers());
                        this.setCustomReturnValueHandlers( adapter.getCustomReturnValueHandlers() );
                    }
                    break;
                }
            }

        }catch (Exception e){
            LOG.warn("",e);
        }

        TemplateUtils.executeWithNoThrow( () -> {

            //查找 MappingJackson2HttpMessageConverter
            MappingJackson2HttpMessageConverter jackson =(MappingJackson2HttpMessageConverter) getMessageConverters()
                    .stream().filter(converter -> converter instanceof MappingJackson2HttpMessageConverter)
                    .findFirst().get();

            //设置默认 mapper
            jackson.setObjectMapper(JSONUtils.objectMapperCopy());

            //覆盖 mapper( 如果获取不到会出异常阻断执行 )
            jackson.setObjectMapper(context.getBean(ObjectMapper.class));
        } );

    }


    @SuppressWarnings({ "unchecked", "rawtypes" })
	private boolean hasAnnotation(Class annotation ,HandlerMethod handlerMethod,boolean includeParameters){

        if( null != handlerMethod.getMethodAnnotation(annotation) ){
            return true;
        }

        if( includeParameters ){
            MethodParameter[] methodParameter = handlerMethod.getMethodParameters();
            if( null != methodParameter ){
                for ( MethodParameter parameter : methodParameter ){
                    if( parameter.hasParameterAnnotation(annotation) ){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean isIncludeDefaults() {
        return includeDefaults;
    }

    public void setIncludeDefaults(boolean includeDefaults) {
        this.includeDefaults = includeDefaults;
    }

	public void setSkipCustomHandlerAdapterWithMethod(
			List<String> skipCustomHandlerAdapterWithMethod) {
		this.skipCustomHandlerAdapterWithMethod = skipCustomHandlerAdapterWithMethod;
	}    
	
	String getHandlerMethodFullName(HandlerMethod handlerMethod){
		if(null != handlerMethod){
			Method method = handlerMethod.getMethod();
			StringBuilder sb = new StringBuilder(method.getDeclaringClass().getTypeName());
			sb.append(".");
			sb.append(method.getName());
			return sb.toString();
		}
		return "";
	}
}
