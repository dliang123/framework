/**
 * <p>Copyright:Copyright(c) 2016</p>
 * <p>Company:上海运图投资有限公司</p>
 * <p>包名:com.yuntu.fairy.cgi.swagger</p>
 * <p>文件名:SwaggerAPIConfig.java</p>
 * <p>类更新历史信息</p>
 * @todo <a href="mailto:fankenie@yaomaiche.com">vernal(聂超)</a> 创建于 2016年2月29日 下午1:13:12
 */
package com.yuntu.swagger.configuration;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.google.common.collect.Lists;
import com.mangofactory.swagger.configuration.SpringSwaggerConfig;
import com.mangofactory.swagger.models.dto.ApiInfo;
import com.mangofactory.swagger.plugin.EnableSwagger;
import com.mangofactory.swagger.plugin.SwaggerSpringMvcPlugin;
import com.mangofactory.swagger.readers.operation.RequestMappingReader;
import com.yuntu.base.utils.ConfigUtils;
import com.yuntu.common.utils.StringUtils;
import com.yuntu.swagger.reader.CustomOperationParameterReader;

/**
 * swagger插件配置
 * <p>
 * Company:上海运图投资有限公司
 * </p>
 * 
 * @author <a href="mailto:fankenie@yaomaiche.com">vernal(聂超)</a>
 * @date 2016年2月29日 下午1:13:12
 * @version 1.0.2016
 */
@EnableWebMvc
@EnableSwagger
public class CustomSwaggerPluginConfig extends WebMvcConfigurerAdapter {
	
	final Logger logger = LoggerFactory.getLogger(getClass());
	 
	private SpringSwaggerConfig springSwaggerConfig;

	private ApiInfo apiInfo;

	final String apidocsUrl = "/api-docs";
	
	private String enabled =  ConfigUtils.v("swagger.enabled","true");
	
	private String customReaders =  ConfigUtils.v("swagger.custom_readers",Boolean.TRUE);
	
    private  String title =  ConfigUtils.v("swagger.title",Boolean.TRUE);
    private  String description =  ConfigUtils.v("swagger.description",Boolean.TRUE);
    private  String termsOfServiceUrl =  ConfigUtils.v("swagger.termsOfServiceUrl",Boolean.TRUE);
    private  String contact =  ConfigUtils.v("swagger.contact",Boolean.TRUE);
    private  String license =  ConfigUtils.v("swagger.license",Boolean.TRUE);
    private  String licenseUrl =  ConfigUtils.v("swagger.licenseUrl",Boolean.TRUE);
    
    private List<RequestMappingReader> customAnnotationReaders = Lists.newArrayList();
	
	@Autowired
	public void setSpringSwaggerConfig(SpringSwaggerConfig springSwaggerConfig) {
		this.springSwaggerConfig = springSwaggerConfig;
	}

	@Bean
	public SwaggerSpringMvcPlugin customImplementation() {
		return new SwaggerSpringMvcPlugin(
				this.springSwaggerConfig)
				.apiInfo(apiInfo)
				.includePatterns(".*?")
				.customAnnotationReaders(customAnnotationReaders)
				.enable(Boolean.valueOf(enabled))
				.build();
	}

	@PostConstruct
	protected void initialize() {
		this.apiInfo = new ApiInfo(this.title, this.description, 
				this.termsOfServiceUrl, this.contact, this.license, this.licenseUrl);
		
		addCustomAnnotationReaders(new CustomOperationParameterReader());
		
		if(StringUtils.isNotBlank(customReaders)){
			String[] customReadersArray = customReaders.split(",");
			
			for(String customReader : customReadersArray){
				try {
					RequestMappingReader requestMappingReader = (RequestMappingReader) Class.forName(customReader).newInstance();
					addCustomAnnotationReaders(requestMappingReader);
				} catch (ClassNotFoundException e) {
					logger.error("加载custom reader出错，找不到类：{}",customReader,e);
				} catch (InstantiationException e) {
					logger.error("加载custom reader出错，无法实例化类：{}",customReader,e);
				} catch (IllegalAccessException e) {
					logger.error("加载custom reader出错，访问了受保护的类或方法：{}",customReader,e);
				}
			}
		}
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(customSwaggerPluginInterceptor());
		super.addInterceptors(registry);
	}

	@Bean
	public HandlerInterceptor customSwaggerPluginInterceptor() {
		return new HandlerInterceptorAdapter() {
			@Override
			public boolean preHandle(HttpServletRequest request,
					HttpServletResponse response, Object handler)
					throws Exception {
				String url = request.getRequestURL().toString();
				if (url.contains(apidocsUrl)) {
					return false;
				}
				return true;
			}
		};
	}
	
	private void addCustomAnnotationReaders(RequestMappingReader requestMappingReader){
		if(!this.customAnnotationReaders.contains(requestMappingReader)){
			this.customAnnotationReaders.add(requestMappingReader);
		}
	}
}
