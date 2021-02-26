package com.blackstrawai.general;

import java.io.FileReader;
import java.util.Properties;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.blackstrawai.ApplicationException;

public class PythonIntegration {
	
	private static PythonIntegration pythonIntegration;
	
	private Logger logger = Logger.getLogger(PythonIntegration.class);

	private static String url=null;
	
	private PythonIntegration(){
		
	}
	
	public static PythonIntegration getInstance(){
		if(pythonIntegration==null){
			pythonIntegration=new PythonIntegration();
			FileReader reader;
			try {
				reader = new FileReader("/decifer/config/app_config.properties");
				Properties p=new Properties();  
				p.load(reader);  
				url=p.getProperty("decifer_api_url_python");

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}        
		}		
			
		return pythonIntegration;
	}
	
	public void login(String key,String value)throws ApplicationException{
		try{
			
			logger.info("URL is:"+ url);
			WebTarget webTarget = ClientBuilder.newClient().target(url+"/v1/login/");
			Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON_TYPE);
			invocationBuilder.header("keyToken",key);
			invocationBuilder.header("valueToken",value);
			Response response = invocationBuilder.get();
			response.readEntity(String.class);
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	
	public void logout(String key)throws ApplicationException{
		try{
			
			logger.info("URL is:"+ url);
			WebTarget webTarget = ClientBuilder.newClient().target(url+"/v1/logout/");
			Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON_TYPE);
			invocationBuilder.header("keyToken",key);
			Response response = invocationBuilder.get();
			response.readEntity(String.class);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
	}
	
	

}
