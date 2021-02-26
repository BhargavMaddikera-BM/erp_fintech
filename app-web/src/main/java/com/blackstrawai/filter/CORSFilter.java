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
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class CORSFilter implements Filter {
	
	private  Logger logger = Logger.getLogger(CORSFilter.class);
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
			System.out.println("CORS Filter");
			HttpServletResponse httpResponse = (HttpServletResponse) response;
			HttpServletRequest httpRequest=(HttpServletRequest)request;
			httpResponse.setHeader("Access-Control-Allow-Origin", "*");
			httpResponse.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE");
			//httpResponse.setHeader("Access-Control-Allow-Headers", "X-Auth-Token, Content-Type, Authorization, token");
			 httpResponse.setHeader("Access-Control-Allow-Headers", "Access-Control-Allow-Headers, "
			 		+ "Origin,Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, "
			 		+ "Access-Control-Request-Headers,X-Auth-Token, Authorization, token,keyToken,valueToken");
		//	httpResponse.setHeader("Access-Control-Expose-Headers", "custom-header1, custom-header2, Authorization, token");
			httpResponse.setHeader("Access-Control-Expose-Headers", "custom-header1, custom-header2, Authorization, token,keyToken,valueToken");
			httpResponse.setHeader("Access-Control-Allow-Credentials", "false");
		//	httpResponse.setHeader("Access-Control-Max-Age", "4800");
			httpResponse.setHeader("Access-Control-Max-Age", "-1");
			System.out.println("---CORS Configuration Completed---");
			logger.info("---CORS Configuration Completed---");
			chain.doFilter(request, response);
	}
			
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}
	@Override
	public void destroy() {
	}
} 