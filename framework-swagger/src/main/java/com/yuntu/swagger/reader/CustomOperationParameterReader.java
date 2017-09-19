/**
 * <p>Copyright:Copyright(c) 2016</p>
 * <p>Company:</p>
 * <p>包名:com.yuntu.swagger.reader</p>
 * <p>文件名:CustomOperationParameterReader.java</p>
 * <p>类更新历史信息</p>
 * @todo  创建于 2016年3月9日 下午4:51:30
 */
package com.yuntu.swagger.reader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.mangofactory.swagger.models.dto.Parameter;
import com.mangofactory.swagger.models.dto.builder.ParameterBuilder;
import com.mangofactory.swagger.readers.operation.RequestMappingReader;
import com.mangofactory.swagger.scanners.RequestMappingContext;
import com.yuntu.base.utils.ConfigUtils;
import com.yuntu.common.utils.JSONUtils;
import com.yuntu.common.utils.StringUtils;
import com.yuntu.swagger.bean.DefaultParameters;

/**
 * swagger插件请求参数读取
 * <p>
 * Company:
 * </p>
 * 
 * @author
 * @date 2016年3月9日 下午4:51:30
 * @version 1.0.2016
 */
public class CustomOperationParameterReader implements RequestMappingReader {

	final Logger logger = LoggerFactory.getLogger(getClass());
	
	private String defaultParameters =  ConfigUtils.v("swagger.default_parameters",Boolean.TRUE);
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mangofactory.swagger.readers.Command#execute(java.lang.Object)
	 * 
	 * @author
	 * 
	 * @date 2016年3月9日 下午4:57:42
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void execute(RequestMappingContext context) {
		List<Parameter> parameters = (List<Parameter>) context
				.get("parameters");
		if (parameters == null) {
			parameters = Lists.newArrayList();
		}
		
		initialize(parameters);

		context.put("parameters", parameters);
	}
	

	public void initialize(List<Parameter> parameters) {
		List<DefaultParameters> list = parseParameters();		
		
		List<String> compareParameters = Lists.newArrayList();
		if(null != parameters && parameters.size()>0){
			compareParameters = Lists.transform(parameters, new Function<Parameter, String>() {
				@Override
				public String apply(Parameter input) {
					return input.getName()+"_"+input.getParamType();
				}
			});
		}
		
		if(null != list){
			for(DefaultParameters customParameter : list){
				Parameter parameter = new ParameterBuilder().name(customParameter.getName())
						.description(customParameter.getDescription())
						.defaultValue(customParameter.getDefaultValue())
						.required(customParameter.getRequired())
						.allowMultiple(customParameter.getAllowMultiple())
						.dataType(customParameter.getDataType())
						.parameterAccess(customParameter.getParamAccess())
						.parameterType(customParameter.getParamType())
						.build();
				if (!parameters.contains(parameter)) {
					if(!compareParameters.contains(parameter.getName()+"_"+parameter.getParamType())){
						parameters.add(parameter);
					}
				}
			}
		}
	}
	
	private List<DefaultParameters> parseParameters(){
		if(StringUtils.isNotBlank(defaultParameters)){
			try {
				ObjectMapper objectMapper = JSONUtils.getObjectMapper();
				JavaType javaType = objectMapper.getTypeFactory().constructParametrizedType(ArrayList.class, ArrayList.class, DefaultParameters.class);
				return objectMapper.readValue(defaultParameters, javaType);
			} catch (JsonParseException e) {
				logger.error("加载default parameters出错，无法解析json：{}",defaultParameters,e);
			} catch (JsonMappingException e) {
				logger.error("加载default parameters出错，无法映射json：{}",defaultParameters,e);
			} catch (IOException e) {
				logger.error("加载default parameters出错，IO异常：{}",defaultParameters,e);
			}
		}
		return null;
	}
}
