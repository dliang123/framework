/**
 * <p>Copyright:Copyright(c) 2016</p>
 * <p>Company:</p>
 * <p>包名:com.yuntu.swagger.bean</p>
 * <p>文件名:DefaultParameters.java</p>
 * <p>类更新历史信息</p>
 * @todo  创建于 2016年3月11日 下午4:22:53
 */
package com.yuntu.swagger.bean;

import java.io.Serializable;

import com.mangofactory.swagger.models.dto.AllowableValues;

/** 
 * 中文类名
 * <p>Company:</p>
 * @author
 * @date 2016年3月11日 下午4:22:53 
 * @version 1.0.2016
 */
public class DefaultParameters implements Serializable{

	private static final long serialVersionUID = -3226049918031105392L;
	
	  private  String name;
	  private  String description;
	  private  String defaultValue;
	  private  Boolean required;
	  private  Boolean allowMultiple;
	  private  AllowableValues allowableValues;
	  private  String paramType;
	  private  String paramAccess;
	  private String dataType;

	/**
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return this.description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the defaultValue
	 */
	public String getDefaultValue() {
		return this.defaultValue;
	}
	/**
	 * @param defaultValue the defaultValue to set
	 */
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	/**
	 * @return the required
	 */
	public Boolean getRequired() {
		return this.required;
	}
	/**
	 * @param required the required to set
	 */
	public void setRequired(Boolean required) {
		this.required = required;
	}
	/**
	 * @return the allowMultiple
	 */
	public Boolean getAllowMultiple() {
		return this.allowMultiple;
	}
	/**
	 * @param allowMultiple the allowMultiple to set
	 */
	public void setAllowMultiple(Boolean allowMultiple) {
		this.allowMultiple = allowMultiple;
	}
	/**
	 * @return the allowableValues
	 */
	public AllowableValues getAllowableValues() {
		return this.allowableValues;
	}
	/**
	 * @param allowableValues the allowableValues to set
	 */
	public void setAllowableValues(AllowableValues allowableValues) {
		this.allowableValues = allowableValues;
	}
	/**
	 * @return the paramType
	 */
	public String getParamType() {
		return this.paramType;
	}
	/**
	 * @param paramType the paramType to set
	 */
	public void setParamType(String paramType) {
		this.paramType = paramType;
	}
	/**
	 * @return the paramAccess
	 */
	public String getParamAccess() {
		return this.paramAccess;
	}
	/**
	 * @param paramAccess the paramAccess to set
	 */
	public void setParamAccess(String paramAccess) {
		this.paramAccess = paramAccess;
	}
	/**
	 * @return the dataType
	 */
	public String getDataType() {
		return this.dataType;
	}
	/**
	 * @param dataType the dataType to set
	 */
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}	
	  
}
