package com.yuntu.common.http;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.Maps;
import com.yuntu.common.utils.JSONUtils;
import com.yuntu.common.utils.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by linxiaohui on 15/11/2.
 */
public class Request {

	private static final Logger logger = org.slf4j.LoggerFactory.getLogger(Request.class);

	private static Charset defaultCharset = Charset.forName("UTF8");

	public static RequestConfig getDefaultRequestConfig() {
		return RequestConfig.custom().setSocketTimeout(8000)
			.setConnectTimeout(8000).setConnectionRequestTimeout(8000).build();
	}

	private RequestBuilder builder;
	
	private MultipartEntityBuilder multipartEntityBuilder;

	private RequestWay way=RequestWay.standard;

	private Map<String,Object> params = null ;
	private String bodyJsonParams = null;
	
	private List<BinaryBody> binaryBodys = null;

	private Request(){

	}


	private Request( RequestBuilder builder ){
		this.builder = builder;
		this.params = new HashMap<String, Object>();
	}

	private static Request newInstance( RequestBuilder builder ){
		return new Request( builder );
	}

	public static Request get(String uri) {
		return newInstance(RequestBuilder.get(uri).setCharset(defaultCharset));
	}

	public static Request head(String uri) {
		return newInstance(RequestBuilder.head(uri).setCharset(defaultCharset));
	}

	public static Request patch(String uri) {
		return newInstance(RequestBuilder.patch(uri).setCharset(defaultCharset) );
	}

	public static Request delete( String uri ) {
		return new Request( RequestBuilder.patch(uri).setCharset(defaultCharset) );
	}

	public static Request trace( String uri) {
		return new Request( RequestBuilder.trace(uri).setCharset(defaultCharset) );
	}

	public static Request put(String uri) {
		return new Request( RequestBuilder.put(uri).setCharset(defaultCharset) );
	}

	public static Request post(String uri) {
		return new Request( RequestBuilder.post(uri).setCharset(defaultCharset) );
	}

	public static Request options(String uri) {
		return new Request( RequestBuilder.options(uri).setCharset(defaultCharset) );
	}


	public Request removeHeader(Header header) {
		builder.removeHeader(header);
		return this;
	}

	public Request setHeader(Header header) {
		builder.setHeader(header);
		return this;
	}

	public Request addHeader(Header header) {
	 	builder.addHeader(header);
		return this;
	}

	public Request copy(HttpRequest request) {
		RequestBuilder.copy(request);
		return this;
	}

	public Request setHeader(String name, String value) {
		builder.setHeader(name, value);
		return this;
	}


	public Map<String,String> getParameters() {
		List<NameValuePair> nameValuePairList = builder.getParameters();
		Map<String,String> params = Maps.newLinkedHashMap();
		for(NameValuePair nameValuePair : nameValuePairList ){
			params.put(nameValuePair.getName(),nameValuePair.getValue());
		}
		return params;
	}

	public Request setConfig(RequestConfig config) {
		builder.setConfig(config);
		return this;
	}

	public Charset getCharset() {
		return builder.getCharset();
	}

	public Header getFirstHeader(String name) {
		return builder.getFirstHeader(name);
	}
	
	public Response execute() throws IOException {

		HttpEntity responseEntity = null;
		CloseableHttpClient client =  null;
		try {
			Long start = System.currentTimeMillis();
			//获取客户端
			client = HttpClients.createDefault();

			switch ( way ){
				case standard:{
					for(Map.Entry<String,Object> param : params.entrySet()){
						builder.addParameter(param.getKey(), StringUtils.nullToEmpty(param.getValue()));
					}
				}break;
				case bodyJson:{
					if( null == bodyJsonParams ){
						 bodyJsonParams = JSONUtils.toJSON(params) ;
					}
					//String json = new String( bodyJsonParams.getBytes("UTF-8"),"ISO-8859-1");
					builder.setEntity(new StringEntity( String.format("%s", bodyJsonParams) , ContentType.APPLICATION_JSON));
				}break;
				case multipart:{
			        multipartEntityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
			        multipartEntityBuilder.setCharset(defaultCharset);
		        	for (BinaryBody binaryBody : binaryBodys) {
		        		if(binaryBody.getStream() instanceof File){
		        			multipartEntityBuilder.addBinaryBody(binaryBody.getName(), (File)binaryBody.getStream());
		        		}else if(binaryBody.getStream() instanceof byte[]){
		        			multipartEntityBuilder.addBinaryBody(binaryBody.getName(), (byte[])binaryBody.getStream(),ContentType.DEFAULT_BINARY,binaryBody.getFileName());
		        		}else if(binaryBody.getStream() instanceof InputStream){
		        			multipartEntityBuilder.addBinaryBody(binaryBody.getName(), (InputStream)binaryBody.getStream(),ContentType.DEFAULT_BINARY,binaryBody.getFileName());
		        		}
		        	}	
			        builder.setEntity(multipartEntityBuilder.build());
				}
			}

			//获取响应
			CloseableHttpResponse response = client.execute(builder.build());
			//获取相应体
			responseEntity =response.getEntity();
			//获取响应状态码
			int code = response.getStatusLine().getStatusCode();
			//获取响应字符串
			String responseBody = EntityUtils.toString(responseEntity);

			if( logger.isDebugEnabled() ){

				logger.debug("请求{} \n参数 {} 成功 \n用时{} \n响应{} ",builder.getUri(),bodyJsonParams == null ? params:bodyJsonParams,System.currentTimeMillis()-start,responseBody);
			}
			return new Response(code,responseBody);
		} catch (IOException e) {
			logger.debug("请求 {} 参数 {} 失败 响应",builder.getUri(),params, e);
			throw e;
		}finally {
			//关闭响应体
			if( null != responseEntity ){
				EntityUtils.consumeQuietly(responseEntity);
			}
			//关闭客户端
			if( null != client ){
				try {
					client.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}



	public Request removeHeaders(String name) {
	 	builder.removeHeaders(name);
		return this;
	}


	public Header getLastHeader(String name) {
		return builder.getLastHeader(name);
	}

	public Header[] getHeaders(String name) {
		return builder.getHeaders(name);
	}

	public Request setCharset(Charset charset) {
		builder.setCharset(charset);
		return this;
	}

	public RequestConfig getConfig() {
		return builder.getConfig();
	}


	public URI getUri() {
		return builder.getUri();
	}


	public String getMethod() {
		return builder.getMethod();
	}



	public Request addHeader(String name, String value) {
		builder.addHeader(name, value);
		return this;
	}

	public Request addParameters(Map<String,String> params) {
		this.params.putAll(params);
		return this;
	}

	public RequestWay getWay() {
		return way;
	}

	public Request setWay(RequestWay way) {
		this.way = way;
		return this;
	}

	/**
	 * 调用此方法 会使之前的 parameter 失效
	 * @param jsonParams
	 * @return
     */
	public Request bodyJson(String jsonParams) {
		this.way = RequestWay.bodyJson;
		this.bodyJsonParams = jsonParams;
		return this;
	}

	/**
	 * 调用此方法 会使之前的 parameter 失效
	 * @param objectParams
	 * @return
	 */
	public Request bodyJson(Object objectParams) throws JsonProcessingException {
		this.way = RequestWay.bodyJson;
		this.bodyJsonParams = JSONUtils.toJSON(objectParams);
		return this;
	}

	public Request addParameter(String name, String value) {
		params.put(name,value);
		return this;
	}

	public Request addParameter(String name, Number value) {
		params.put(name,value);
		return this;
	}
	
	public Request addBinaryBody(List<BinaryBody> binaryBodys){
		this.way = RequestWay.multipart;
		this.binaryBodys = binaryBodys;
		if(null == this.multipartEntityBuilder){
			this.multipartEntityBuilder = MultipartEntityBuilder.create();		
		}
		return this;
	}
}
