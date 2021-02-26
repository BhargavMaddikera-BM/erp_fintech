package com.blackstrawai.aadhar;

import java.io.FileReader;
import java.util.Properties;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.ApplicationRuntimeException;

public class AadhaarApi {

	private static AadhaarApi aadharApi;

	private Logger logger = Logger.getLogger(AadhaarApi.class);

	private static String aadharUrl=null;
	private static String aadharQtApiKey=null;
	private static String aadharQtAgencyId=null;
	private static String aadharPanConsentText=null;
	private static String aadharGstConsentText=null;
	private AadhaarApi(){

	}

	public static AadhaarApi getInstance(){
		if(aadharApi==null){
			aadharApi=new AadhaarApi();
			FileReader reader;
			try {
				reader = new FileReader("/decifer/config/app_config.properties");
				Properties p=new Properties();  
				p.load(reader);  
				aadharUrl=p.getProperty("aadharApi_url");
				aadharQtApiKey=p.getProperty("aadhar_qt_api_key");
				aadharQtAgencyId=p.getProperty("aadhar_qt_agency_id");
				aadharPanConsentText=p.getProperty("aadhar_pan_consent_text");
				aadharGstConsentText=p.getProperty("aadhar_gst_consent_text");
			} catch (Exception e) {
				throw new ApplicationRuntimeException(e);
			}        
		}		

		return aadharApi;
	}

	public String verifyPAN(String panNumber,Boolean isBasic)throws ApplicationException{
		try{

			String URI="/pan";
			String payload="{\r\n" + 
					"	\"pan\":\""+panNumber+"\"\r\n" + 
					"}";
			if(isBasic) {
				URI="/pan-lite";
				payload="{\r\n" + 
						"	\"pan\":\""+panNumber+"\",\r\n" + 
						"	\"consent\": \"Y\",\r\n" + 
						"	\"consent_text\": \""+aadharPanConsentText+"\"\r\n" + 
						"}";
			}

			WebTarget webTarget = ClientBuilder.newClient().target(aadharUrl+URI);
			Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON_TYPE);
			invocationBuilder.header("qt_api_key",aadharQtApiKey);
			invocationBuilder.header("qt_agency_id",aadharQtAgencyId);
			Response response = invocationBuilder.post(Entity.entity(payload, MediaType.APPLICATION_JSON));
			String str= response.readEntity(String.class);
			return str;
		}catch(Exception e){
			throw new ApplicationException(e);
		}


	}

	public String verifyBankAccount(String accountNumber,String ifscCode,Boolean isBasic)throws ApplicationException{
		try{

			String URI="/verify-bank";
			String payload="{\r\n" + 
					"    \"Account\": \""+accountNumber+"\",\r\n" + 
					"    \"IFSC\": \""+ifscCode+"\",\r\n" + 
					"    \"Advance\": \"Y\"\r\n" + 
					"}";
			if(isBasic) {
				payload="{\r\n" + 
						"    \"Account\": \""+accountNumber+"\",\r\n" + 
						"    \"IFSC\": \""+ifscCode+"\"\r\n" + 
						"}";
			}

			WebTarget webTarget = ClientBuilder.newClient().target(aadharUrl+URI);
			Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON_TYPE);
			invocationBuilder.header("qt_api_key",aadharQtApiKey);
			invocationBuilder.header("qt_agency_id",aadharQtAgencyId);
			Response response = invocationBuilder.post(Entity.entity(payload, MediaType.APPLICATION_JSON));
			String str= response.readEntity(String.class);
			return str;
		}catch(Exception e){
			throw new ApplicationException(e);
		}
	}

	public String verifyGstNumber(String gstNumber,Boolean isBasic)throws ApplicationException{
		try{

			String URI="/verify-gst";
			String payload="{\r\n" + 
					"    \"gstin\":\""+gstNumber+"\",\r\n" + 
					"    \"consent\":\"Y\",\r\n" + 
					"    \"consent_text\":\""+aadharGstConsentText+"\"\r\n" + 
					"} ";
			if(isBasic) {
				URI="/verify-gst-lite";
			}

			WebTarget webTarget = ClientBuilder.newClient().target(aadharUrl+URI);
			Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON_TYPE);
			invocationBuilder.header("qt_api_key",aadharQtApiKey);
			invocationBuilder.header("qt_agency_id",aadharQtAgencyId);
			Response response = invocationBuilder.post(Entity.entity(payload, MediaType.APPLICATION_JSON));
			String str= response.readEntity(String.class);
			return str;
		}catch(Exception e){
			throw new ApplicationException(e);
		}
	}
}
