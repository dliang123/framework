/**  **/
package com.yuntu.file;

import java.io.InputStream;

/**
 * @author icewang
 * @date 2017年1月12日
 */
public interface FileOperator {
	
	public File uploadFile(String bucket, String path, String fileName, 
			InputStream fileStream, long contentLength) throws Exception;
}
