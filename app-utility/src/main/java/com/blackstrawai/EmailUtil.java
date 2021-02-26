package com.blackstrawai;

import java.io.FileReader;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailUtil {

	private static EmailUtil emailUtil;
	private static String smtpHost;
	private static String smtpPort;
	private static String email;
	private static String password;

	private EmailUtil() {

	}

	public static EmailUtil getInstance() {
		if (emailUtil == null) {
			try {
				emailUtil = new EmailUtil();
				FileReader reader;
				reader = new FileReader("/decifer/config/app_config.properties");
				Properties p=new Properties();  
				p.load(reader);  
				smtpHost=p.getProperty("smtp_host");
				smtpPort=p.getProperty("smtp_port");
				email=p.getProperty("email_userName");
				password=p.getProperty("email_password");

			} catch (Exception e) {
				throw new ApplicationRuntimeException(e);
				// TODO Auto-generated catch block
			}   
		}
		return emailUtil;
	}

	public void sendEmail (String to,String subject,String msg)throws ApplicationException{  

		try{  

			Properties properties = System.getProperties();
			properties.put("mail.smtp.host", smtpHost);
			properties.put("mail.smtp.port",smtpPort);
			properties.put("mail.smtp.ssl.enable", "true");
			properties.put("mail.smtp.auth", "true");

			Session session = Session.getInstance(properties, new javax.mail.Authenticator() {

				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(email,password);
				}
			});

			session.setDebug(true);
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(email));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			message.setSubject(subject);
			message.setText(msg);
			Transport.send(message);
		
		}catch (Exception mex)
		{
			throw new ApplicationException(mex);
		}  
	} 
	
	 public static void main(String[] args) throws ApplicationException{
		 String message="Hi abcd,\r\n\r\n  Your Profile is successfully registered. please click https://decifer.blackstrawlab.com/auth/login to proceed.\r\n\r\n Regards,\r\n Decifer Admin";
		 EmailUtil.getInstance().sendEmail("bhargav.maddikera@blackstraw.ai", "hi", message);
		 
	 }

}
