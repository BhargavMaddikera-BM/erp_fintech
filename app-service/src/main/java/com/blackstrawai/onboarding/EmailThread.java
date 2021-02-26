package com.blackstrawai.onboarding;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.ApplicationRuntimeException;
import com.blackstrawai.EmailUtil;

public class EmailThread extends Thread{
	
	private String message;
	private String  toAddress;
	private String subject;
	
	public EmailThread(String message,String  toAddress,String subject){
		this.message=message;
		this.toAddress=toAddress;
		this.subject=subject;
	}
	
	public void run(){
		try {
			EmailUtil.getInstance().sendEmail(toAddress, subject, message);

		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			throw new ApplicationRuntimeException(e);
		}
	}

}
