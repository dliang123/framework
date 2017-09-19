/**
 * <p>Copyright:Copyright(c) 2016</p>
 * <p>Company:</p>
 * <p>包名:com.yuntu.swagger.controller</p>
 * <p>文件名:CustomSwaggerPluginController.java</p>
 * <p>类更新历史信息</p>
 * @todo  创建于 2016年3月9日 下午3:12:00
 */
package com.yuntu.swagger.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mangofactory.swagger.annotations.ApiIgnore;
import com.mangofactory.swagger.core.SwaggerCache;
import com.mangofactory.swagger.models.dto.ApiListing;
import com.mangofactory.swagger.models.dto.ResourceListing;
import com.yuntu.base.utils.ConfigUtils;

/** 
 * swagger插件控制器
 * <p>Company:</p>
 * @author
 * @date 2016年3月9日 下午3:12:00 
 * @version 1.0.2016
 */
@Controller
@RequestMapping(path="swagger")
public class CustomSwaggerPluginController {

	  public static final String DOCUMENTATION_BASE_PATH = "/api/docs";

	  @Autowired
	  private SwaggerCache swaggerCache;
	  
	  private String systemKey = ConfigUtils.v("system.key","0ceda49edcf547d5");
	  
	  @ApiIgnore
	  @RequestMapping(value = {DOCUMENTATION_BASE_PATH + "/{accessKey}"}, method = RequestMethod.GET)
	  public
	  @ResponseBody
	  ResponseEntity<ResourceListing> getResourceListing(
	      @RequestParam(value = "group",  required = false) String swaggerGroup, @PathVariable String accessKey) {
		 
		if(null == accessKey){
			return null;
		}
		if(!accessKey.equals(systemKey)){
			return null;
		}		
	    return getSwaggerResourceListing(swaggerGroup);
	  }

	  @ApiIgnore
	  @RequestMapping(value = {DOCUMENTATION_BASE_PATH + "/{accessKey}/{swaggerGroup}/{apiDeclaration}"}, method = RequestMethod.GET)
	  public
	  @ResponseBody
	  ResponseEntity<ApiListing> getApiListing(@PathVariable String accessKey,@PathVariable String swaggerGroup, @PathVariable String apiDeclaration) {
		if(null == accessKey){
			return null;
		}
		if(!accessKey.equals(systemKey)){
			return null;
		}			  
	    return getSwaggerApiListing(swaggerGroup, apiDeclaration);
	  }

	  private ResponseEntity<ApiListing> getSwaggerApiListing(String swaggerGroup, String apiDeclaration) {
	    ResponseEntity<ApiListing> responseEntity = new ResponseEntity<ApiListing>(HttpStatus.NOT_FOUND);
	    Map<String, ApiListing> apiListingMap = swaggerCache.getSwaggerApiListingMap().get(swaggerGroup);
	    if (null != apiListingMap) {
	      ApiListing apiListing = apiListingMap.get(apiDeclaration);
	      if (null != apiListing) {
	        responseEntity = new ResponseEntity<ApiListing>(apiListing, HttpStatus.OK);
	      }
	    }
	    return responseEntity;
	  }

	  private ResponseEntity<ResourceListing> getSwaggerResourceListing(String swaggerGroup) {
	    ResponseEntity<ResourceListing> responseEntity = new ResponseEntity<ResourceListing>(HttpStatus.NOT_FOUND);
	    ResourceListing resourceListing = null;

	    if (null == swaggerGroup) {
	      resourceListing = swaggerCache.getSwaggerApiResourceListingMap().values().iterator().next();
	    } else {
	      if (swaggerCache.getSwaggerApiResourceListingMap().containsKey(swaggerGroup)) {
	        resourceListing = swaggerCache.getSwaggerApiResourceListingMap().get(swaggerGroup);
	      }
	    }
	    if (null != resourceListing) {
	      responseEntity = new ResponseEntity<ResourceListing>(resourceListing, HttpStatus.OK);
	    }
	    return responseEntity;
	  }
}
