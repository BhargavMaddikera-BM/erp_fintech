package com.blackstrawai.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.common.CacheService;
import com.blackstrawai.common.Constants;
import com.blackstrawai.common.ErrorResponse;
import com.blackstrawai.common.ErrorVo;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
@Order(2)
public class AuthenticationFilter implements Filter {
	
	@Autowired
	CacheService cacheService;
	
	private  Logger logger = Logger.getLogger(AuthenticationFilter.class);
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
			System.out.println("Test change Feb 27 ******************");
        	HttpServletResponse httpResponse = (HttpServletResponse) response;
        	HttpServletRequest httpRequest=(HttpServletRequest)request;
        	System.out.println("The Header Tokens are:"+httpRequest.getHeader("keyToken")+":"+httpRequest.getHeader("valueToken"));
        	String requestURI=httpRequest.getRequestURI().toString();
        	System.out.println("Request URI:**********"+requestURI );
        	if((requestURI.endsWith("/login")) || (requestURI.endsWith("/registration"))|| (requestURI.contains("/basic/")) || (requestURI.endsWith("/decifer/perfios/v1/callback")) ){
        		logger.info("Authentication not required for uri:"+requestURI);        		
        	}else{
        		try {
					boolean isValid=cacheService.validateToken(httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
					if(!isValid){
						logger.error("Authentication Failed for Token:"+httpRequest.getHeader("keyToken")+":,"+httpRequest.getHeader("valueToken") );
						ErrorResponse errorResponse=new ErrorResponse();
						errorResponse.setResponseCode(Constants.USER_LOGIN_FAILURE);
						errorResponse.setResponseDescription(Constants.AUTHENTICATION_FAILED);
						errorResponse.setResponseStatus(Constants.FAILURE);
						errorResponse.setResponseMessage(Constants.AUTHENTICATION_FAILED);
						errorResponse.setKeyToken(httpRequest.getHeader("keyToken"));
						errorResponse.setValueToken(httpRequest.getHeader("valueToken"));
						ErrorVo errorVo=new ErrorVo();
						((ErrorResponse) errorResponse).setData(errorVo);
						ObjectMapper Obj = new ObjectMapper();
						String jsonStr = Obj.writeValueAsString(errorResponse); 
						httpResponse.setContentType("application/json;charset=UTF-8");
						httpResponse.getWriter().write(jsonStr);
						httpResponse.getWriter().flush();
						httpResponse.getWriter().close();
						return;
						//return with response code
					}
				} catch (ApplicationException e) {
					// TODO Auto-generated catch block
					throw new ServletException(e);
				}        		
        	}        	
	        chain.doFilter(request, response);
	}
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}
	@Override
	public void destroy() {
	}
} 