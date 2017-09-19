/**  **/
package com.yuntu.file;

import java.io.InputStream;

import com.aliyun.oss.ClientConfiguration;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;

/**
 * @author
 * @date 2017年1月12日
 */
public class OssFileOperator implements FileOperator {

	private String endpoint;
	
	private String key;
	
	private String secret;
	
	@Override
	public File uploadFile(String bucket, String path, String fileName,
			InputStream fileStream, long contentLength) throws Exception {
		OSSClient client = newOSSClient();
		if (!client.doesBucketExist(bucket)) {
			client.createBucket(bucket);
		}
		String location = client.getBucketLocation(bucket);
		String uri = getFileUri(path, fileName);
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(contentLength);
		
		PutObjectResult result = client.putObject(bucket, uri, fileStream, metadata);
		
		File file = new File();
		file.setFileId(result.getETag());
		StringBuffer url = new StringBuffer("http://");
		url.append(bucket);
		url.append(".");
		url.append(location);
		url.append(".aliyuncs.com/");
		url.append(uri);
		file.setUrl(url.toString());
		return file;
	}

	private OSSClient newOSSClient() {
		ClientConfiguration conf = new ClientConfiguration();
		// 设置HTTP最大连接数为10
		conf.setMaxConnections(10);
		// 设置TCP连接超时为5000毫秒
		conf.setConnectionTimeout(5000);
		// 设置最大的重试次数为3
		conf.setMaxErrorRetry(3);
		// 设置Socket传输数据超时的时间为2000毫秒
		conf.setSocketTimeout(2000);
		// 初始化一个OSSClient
		OSSClient client = new OSSClient(endpoint, key, secret, conf);
		return client;
	}
	
	private String getFileUri(String path, String fileName) {
		StringBuffer uri = new StringBuffer();
		uri.append(path.startsWith("/") ? path.substring(1) : path);
		if(!path.endsWith("/")) {
			uri.append('/');
		}
		fileName = fileName.replace('/', '\0');
		return uri.append(fileName).toString();
	}

	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}
}
