package com.blackstrawai.razorpay;

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

public class IFSCCode {
	
private static IFSCCode ifscCode;
	
	private Logger logger = Logger.getLogger(IFSCCode.class);

	private static String url=null;
	
	private IFSCCode(){
		
	}
	
	public static IFSCCode getInstance(){
		if(ifscCode==null){
			ifscCode=new IFSCCode();
			FileReader reader;
			try {
				reader = new FileReader("/decifer/config/app_config.properties");
				Properties p=new Properties();  
				p.load(reader);  
				url=p.getProperty("ifscCode_url");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				throw new ApplicationRuntimeException(e);
			}        
		}		
			
		return ifscCode;
	}
	
	public String getBankDetails(String code)throws ApplicationException{
		try{
			
			//https://ifsc.razorpay.com/
		
			WebTarget webTarget = ClientBuilder.newClient().target(url+"/"+code);
			Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON_TYPE);
			Response response = invocationBuilder.get();
			String str= response.readEntity(String.class);
			return str;
		}catch(Exception e){
			throw new ApplicationException(e);
		}
		
		
	}

}
