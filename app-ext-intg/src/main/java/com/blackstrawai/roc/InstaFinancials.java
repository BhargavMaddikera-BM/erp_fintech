package com.blackstrawai.roc;

import java.io.FileReader;
import java.util.Properties;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.ApplicationRuntimeException;

public class InstaFinancials {
	
	private static InstaFinancials instaFinancials;
	
	private Logger logger = Logger.getLogger(InstaFinancials.class);

	private static String key=null;
	private static String cinUrl=null;
	
	private InstaFinancials(){
		
	}
	
	public static InstaFinancials getInstance(){
		if(instaFinancials==null){
			instaFinancials=new InstaFinancials();
			FileReader reader;
			try {
				reader = new FileReader("/decifer/config/app_config.properties");
				Properties p=new Properties();  
				p.load(reader);  
				key=p.getProperty("instaFinancials_key");
				cinUrl=p.getProperty("instaFinancials_cin_url");

			} catch (Exception e) {
				// TODO Auto-generated catch block
				throw new ApplicationRuntimeException(e);
			}        
		}		
			
		return instaFinancials;
	}
	
	public String getByCin(String cin)throws ApplicationException{
		try{
			
			//https://instafinancials.com/api/InstaSummary/V1/json/CompanyCIN/L27209GJ1999PLC036656
			
			//String url="https://instafinancials.com/api/InstaSummary/V1/json/CompanyCIN/"+cin;
			String url=cinUrl+cin;
			logger.info("URL is:"+ url);
			System.out.println("Insta Financials URL is:"+url);
			WebTarget webTarget = ClientBuilder.newClient().target(url);
			Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON_TYPE);
		   // invocationBuilder.header("user-key", "wv2rcoHqZrCrfZbAAstvxd4wOuMz0+NUEa6x3j2OJCod/jPW0wdIGQ==");
			invocationBuilder.header("user-key",key);
			Response response = invocationBuilder.get();
			String str= response.readEntity(String.class);
			return str;
		}catch(Exception e){
			throw new ApplicationException(e);
		}
		
		
	}
	
	/*public String getByPan(String pan)throws ApplicationException{
		try{
			
			//https://instafinancials.com/api/GetCIN/V1/json/Search/CompanyPAN/AAECS6731M
		  // String url="https://instafinancials.com/api/GetCIN/V1/json/Search/CompanyPAN/"+pan;
		   String url=panUrl+pan;
		   WebTarget webTarget = ClientBuilder.newClient().target(url);		 
		   Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON_TYPE);
		  // invocationBuilder.header("user-key", "wv2rcoHqZrCrfZbAAstvxd4wOuMz0+NUEa6x3j2OJCod/jPW0wdIGQ==");	
		   invocationBuilder.header("user-key",key);
		   Response response = invocationBuilder.get();
		   String str= response.readEntity(String.class);
		   return str;
	}catch(Exception e){
		throw new ApplicationException(e);
	}
	}
	
	public String getByCinAndPan(String cin,String pan)throws ApplicationException{
		return null;
	}*/

}
