package com.blackstrawai;

import org.apache.catalina.connector.Connector;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import com.blackstrawai.common.TestThread;

@SpringBootApplication
@EnableCaching
@ComponentScan({ "com.blackstrawai.service", "com.blackstrawai.dao", "com.blackstrawai.controller" ,"com.blackstrawai"})
public class AppWebApplication extends SpringBootServletInitializer{
	
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(AppWebApplication.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(AppWebApplication.class, args);
		TestThread testThread=new TestThread();
		testThread.start();
	}
	
	
	@Bean
	public ServletWebServerFactory servletContainer() {
	TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();
	tomcat.addAdditionalTomcatConnectors(createStandardConnector());
	return tomcat;
	}

	private Connector createStandardConnector() {
	Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
	connector.setPort(httpPort);
	return connector;
	}
	@Value("${http.port}")
	private int httpPort;

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}
}

