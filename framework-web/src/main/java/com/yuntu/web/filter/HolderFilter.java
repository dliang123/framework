package com.yuntu.web.filter;

import com.yuntu.web.util.HolderUtils;
import org.slf4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;
import java.io.PrintWriter;

/*
 * Author:
 * Date:     15-1-22
 * Description: 模块目的、功能描述      
 * History: 变更记录
 * <author>           <time>             <version>        <desc>
 * Administrator           15-1-22           00000001         创建文件
 *
 */
public class HolderFilter implements Filter {

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(HolderFilter.class);
    public static final String RESPONSE_KEY="HTTP_SERVLET_RESPONSE";

    public void init(FilterConfig filterConfig) throws ServletException {

    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletResponse servletResponse = response instanceof AutoResetHttpServletResponse
                ? (HttpServletResponse) response
                :new AutoResetHttpServletResponse((HttpServletResponse)response);
        try {
            request.setAttribute(RESPONSE_KEY,servletResponse);
            HolderUtils.setRequest((HttpServletRequest)request);
            HolderUtils.setResponse(servletResponse);
        } catch (Exception e){
            if( logger.isWarnEnabled() ){
                logger.warn(e.getMessage(),e);
            }
        } finally {
        	try {
        		chain.doFilter( request,servletResponse);
        	} catch (IOException ioEx) {
        		HolderUtils.removeRequest();
        		HolderUtils.removeResponse();
        		throw ioEx;
        	} catch (ServletException svltEx) {
        		HolderUtils.removeRequest();
        		HolderUtils.removeResponse();
        		throw svltEx;
        	}
        }
    }

    public void destroy() {

    }

    private class AutoResetHttpServletResponse extends HttpServletResponseWrapper {

        private ServletOutputStream outputStream;
        private PrintWriter printWriter;

        public AutoResetHttpServletResponse(HttpServletResponse response) {
            super(response);
        }

        @Override
        public ServletOutputStream getOutputStream() throws IOException {
            try{
                if( null == this.outputStream )
                    this.outputStream = super.getOutputStream();
            }catch (IOException e){
                this.flushBuffer();
                this.reset();
                this.outputStream = super.getOutputStream();
            }
            return this.outputStream;
        }

        @Override
        public PrintWriter getWriter() throws IOException {
            try{
                if( null == this.printWriter )
                    this.printWriter = super.getWriter();
            }catch (IOException e){
                this.flushBuffer();
                this.reset();
                this.printWriter = super.getWriter();
            }
            return this.printWriter;
        }
    }
}