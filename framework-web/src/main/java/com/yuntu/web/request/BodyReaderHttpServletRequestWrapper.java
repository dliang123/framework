package com.yuntu.web.request;

import com.google.common.collect.Maps;
import com.yuntu.web.Constants;

import org.springframework.util.StreamUtils;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class BodyReaderHttpServletRequestWrapper extends
		HttpServletRequestWrapper {

	private final byte[] body;

	private final Map<String, Object> headers;

	public BodyReaderHttpServletRequestWrapper(HttpServletRequest request)
			throws IOException {
		super(request);

		headers = resolveHeaders(request);
		body = StreamUtils.copyToByteArray(request.getInputStream());

		request.setAttribute(Constants.REQUEST_BODY_KEY, body);
	}

	@Override
	public BufferedReader getReader() throws IOException {
		return new BufferedReader(new InputStreamReader(getInputStream()));
	}

	@Override
	public ServletInputStream getInputStream() throws IOException {
		final ByteArrayInputStream bais = new ByteArrayInputStream(body);
		return new ServletInputStream() {
			@Override
			public int read() throws IOException {
				return bais.read();
			}
		};
	}

	public static String requestBody(HttpServletRequest request) {
		Object obj = request.getAttribute(Constants.REQUEST_BODY_KEY);
		if (null == obj)
			return null;
		return new String((byte[]) obj);
	}
	
	@SuppressWarnings("unchecked")
	public static Map<String, Object> requestHeaders(HttpServletRequest request) {
		if( request instanceof BodyReaderHttpServletRequestWrapper ){
			return ((BodyReaderHttpServletRequestWrapper)request).headers;

		}
		return resolveHeaders(request);
	}	

	@SuppressWarnings("unchecked")
	private static Map<String, Object> resolveHeaders(HttpServletRequest request) {

		Map<String, Object> result = (Map<String, Object>)request.getAttribute(Constants.REQUEST_HEADERS_KEY);
		if( null == result ){
			result = Maps.newHashMap();
			for (Enumeration<String> headerNames = request.getHeaderNames(); headerNames
					.hasMoreElements();) {
				String headerName = headerNames.nextElement();
				result.put(headerName, request.getHeader(headerName));
			}
			request.setAttribute(Constants.REQUEST_HEADERS_KEY,result);
		}

		return result;
	}

}