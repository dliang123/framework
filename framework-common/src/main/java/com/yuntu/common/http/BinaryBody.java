/**
 * <p>Copyright:Copyright(c) 2016</p>
 * <p>Company:</p>
 * <p>包名:com.yuntu.common.http</p>
 * <p>文件名:BinaryBody.java</p>
 * <p>类更新历史信息</p>
 * @todo  创建于 2016年3月8日 下午1:34:17
 */
package com.yuntu.common.http;

import java.io.InputStream;
import java.io.Serializable;

import org.springframework.util.Assert;

/** 
 * 文件上传二进制对象
 * <p>Company:</p>
 * @author
 * @date 2016年3月8日 下午1:34:17 
 * @version 1.0.2016
 */
public class BinaryBody implements Serializable{

	private static final long serialVersionUID = -8404803719838398735L;

	/**
	 * 属性名
	 */
	private String name;
	
	/**
	 * 二进制，支持File、byte[]、Inputsteam;
	 */
	private Object stream;
	
	private String fileName;
	
	public BinaryBody(String name,Object stream,String fileName){
		Assert.notNull(name,"name不能为空");
		Assert.notNull(stream,"stream不能为空");
		
		if(this.stream instanceof byte[] || this.stream instanceof InputStream){
			Assert.notNull(fileName,"fileName不能为空");
		}
		
		this.name = name;
		this.stream = stream;
		this.fileName = fileName;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @return the stream
	 */
	public Object getStream() {
		return this.stream;
	}

	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return this.fileName;
	}
	
}
