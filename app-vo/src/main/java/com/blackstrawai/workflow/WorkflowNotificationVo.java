package com.blackstrawai.workflow;

public class WorkflowNotificationVo {
	public boolean inApp;
	public boolean whatsApp;
	public boolean email;
	private boolean sms;
	public boolean isInApp() {
		return inApp;
	}
	public void setInApp(boolean inApp) {
		this.inApp = inApp;
	}
	public boolean isWhatsApp() {
		return whatsApp;
	}
	public void setWhatsApp(boolean whatsApp) {
		this.whatsApp = whatsApp;
	}
	public boolean isEmail() {
		return email;
	}
	public void setEmail(boolean email) {
		this.email = email;
	}
	public boolean isSms() {
		return sms;
	}
	public void setSms(boolean sms) {
		this.sms = sms;
	}
	
		
}