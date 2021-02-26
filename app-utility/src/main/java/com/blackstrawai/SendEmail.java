package com.blackstrawai;

import java.io.File;
import java.io.InputStream;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.log4j.Logger;


public class SendEmail {
	private static final Logger LOGGER = Logger.getLogger(SendEmail.class);
		public  String sendEmail(String toEmail, String cc, String bcc, String content, String attachement,String attachementFileName){
			String status="";
			Properties props = new Properties();
//			System.out.println("**************** BCC: "+bcc);
			InputStream input = null;

			try {
				Thread currentThread = Thread.currentThread();
				ClassLoader contextClassLoader = currentThread.getContextClassLoader();
				InputStream propertiesStream = contextClassLoader.getResourceAsStream("dataproperty.properties");
				if (propertiesStream != null) {
				  props.load(propertiesStream);
				} else {
					LOGGER.info("Properties not found");
				}
			}catch(Exception e){
				e.printStackTrace();
				LOGGER.error("email properties are not available"+e.getLocalizedMessage());
			}
			LOGGER.info("email Content: "+content);

			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.smtp.host", "smtp.gmail.com");
			props.put("mail.smtp.port", "587");

			final String username=props.getProperty("gmail.username");
			final String password=props.getProperty("gmail.password");
//			String toMail=props.getProperty("gmail.toMail");
			String subject=props.getProperty("subject");
			
			String fromMail=username;

			Session session = Session.getInstance(props,
					new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(username, password);
				}
			});
			try {

				Message message = new MimeMessage(session);
				message.setFrom(new InternetAddress(fromMail));
				message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(toEmail));
				message.setRecipients(Message.RecipientType.BCC,InternetAddress.parse(bcc));
				message.setSubject(subject);

				BodyPart messageBodyPart = new MimeBodyPart();

				String htmlText=content;
				messageBodyPart.setContent(htmlText, "text/html");

				Multipart multipart = new MimeMultipart();
				multipart.addBodyPart(messageBodyPart);
				messageBodyPart = new MimeBodyPart();
				if (attachement != null&&!attachement.equalsIgnoreCase("")){
					File file=new File(attachement);
					DataSource source = new FileDataSource(attachement);
					messageBodyPart.setDataHandler(new DataHandler(source));
					messageBodyPart.setFileName(file.getName());
					multipart.addBodyPart(messageBodyPart);
				}
				message.setContent(multipart);
				Transport.send(message);
				LOGGER.info("email hes been sent successfully");
				 status="success";

			} catch (MessagingException e) {
				e.printStackTrace();
				LOGGER.error("Unable to send the email due to "+e.getLocalizedMessage());
			}
			return status;
		}
	

	public static void main(String[] args) {
		
		
		SendEmail email = new SendEmail();
		email.sendEmail("test@test.com", "", "","", "", "");
	}

}
