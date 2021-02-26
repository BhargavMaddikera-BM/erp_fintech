package com.blackstrawai.msg91;

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

public class MessagingApi {

	private static MessagingApi messagingApi;

	private Logger logger = Logger.getLogger(MessagingApi.class);

	private static String ms91SendOtpUrl=null;
	private static String ms91VerifyOtpUrl=null;
	private static String ms91ReSendOtpUrl=null;
	private static String msg91AuthKey=null;
	private static String msg91OtpTemplateId=null;
	private MessagingApi(){

	}

	public static MessagingApi getInstance(){
		if(messagingApi==null){
			messagingApi=new MessagingApi();
			FileReader reader;
			try {
				reader = new FileReader("/decifer/config/app_config.properties");
				Properties p=new Properties();  
				p.load(reader);  
				ms91SendOtpUrl=p.getProperty("msg91_send_otp_url");
				ms91VerifyOtpUrl=p.getProperty("msg91_verify_otp_url");
				ms91ReSendOtpUrl=p.getProperty("msg91_re_send_otp_url");
				msg91AuthKey=p.getProperty("msg91_auth_key");
				msg91OtpTemplateId=p.getProperty("msg91_otp_template_id");
			} catch (Exception e) {
				throw new ApplicationRuntimeException(e);
			}        
		}		

		return messagingApi;
	}


	public String sendOtp(String mobileNumber)throws ApplicationException{
		try{
			final int OTP_EXPIRY_TIME_IN_MINS=5;
			WebTarget webTarget = ClientBuilder.newClient().target(ms91SendOtpUrl+"authkey="+msg91AuthKey+"&template_id="+msg91OtpTemplateId+"&mobile="+mobileNumber+"&otp_expiry="+OTP_EXPIRY_TIME_IN_MINS);
			Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON_TYPE);
			Response response = invocationBuilder.get();
			String str= response.readEntity(String.class);
			return str;
		}catch(Exception e){
			throw new ApplicationException(e);
		}
	}

	public String verifyOtp(String mobileNumber,String otp)throws ApplicationException{
		try{

			WebTarget webTarget = ClientBuilder.newClient().target(ms91VerifyOtpUrl+"authkey="+msg91AuthKey+"&template_id="+msg91OtpTemplateId+"&mobile="+mobileNumber+"&otp="+otp);
			Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON_TYPE);
			Response response = invocationBuilder.get();
			String str= response.readEntity(String.class);
			return str;
		}catch(Exception e){
			throw new ApplicationException(e);
		}
	}
	
	public String reSendOtp(String mobileNumber)throws ApplicationException{
		try{

			WebTarget webTarget = ClientBuilder.newClient().target(ms91ReSendOtpUrl);
			Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON_TYPE);
			Response response = invocationBuilder.get();
			String str= response.readEntity(String.class);
			return str;
		}catch(Exception e){
			throw new ApplicationException(e);
		}
	}

	public static void main(String[] args) throws ApplicationException {
		//System.out.println("Response:"+SmsApi.getInstance().sendOtp("+919551785619"));;
		//{"request_id":"306c61706852383836303730","type":"success"}
		//System.out.println("Response:"+SmsApi.getInstance().verifyOtp("+919551785619","3539"));;
		//{"message":"OTP not match","type":"error"}

	}
}
