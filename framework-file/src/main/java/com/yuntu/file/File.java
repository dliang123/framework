/**  **/
package com.yuntu.file;

import java.io.Serializable;

/**
 * @author icewang
 * @date 2017年1月12日
 */
public class File implements Serializable {

	private static final long serialVersionUID = 1L;

	private String fileId;
	
	private String url;

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
