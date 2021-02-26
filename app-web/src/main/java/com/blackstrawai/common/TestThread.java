package com.blackstrawai.common;

import java.io.FileReader;
import java.util.Properties;

import javax.crypto.SecretKeyFactory;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.blackstrawai.DeciferAES256;

public class TestThread extends Thread{
	public void run(){
		try {
			FileReader reader;
			reader = new FileReader("/decifer/config/app_config.properties");
			Properties p=new Properties();  
			p.load(reader);  
			String url =p.getProperty("decifer_url");			
			do{
				DeciferAES256.setEncyrptionBankingFactory(SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256"));
				DeciferAES256.setEncyrptionComplianceFactory(SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256"));
				DeciferAES256.setDecyrptionBankingFactory(SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256"));
				DeciferAES256.setDecyrptionComplianceFactory(SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256"));
				
				WebTarget webTarget = ClientBuilder.newClient().target(url+"/"+"decifer/v1/basic/test");
				Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON_TYPE);
				Response response = invocationBuilder.get();
				System.out.println("Response Status:"+response.getStatus());				
				this.sleep(10000);
			}while(true);		
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			
		}
		
	}

}
