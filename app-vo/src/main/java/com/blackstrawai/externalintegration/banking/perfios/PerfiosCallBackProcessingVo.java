package com.blackstrawai.externalintegration.banking.perfios;

public class PerfiosCallBackProcessingVo {
	
	private boolean isCallBackProcessingStarted=false;
	private boolean isCallBackProcessingCompleted=false;	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	private String message=null;
	public boolean isCallBackProcessingStarted() {
		return isCallBackProcessingStarted;
	}
	public void setCallBackProcessingStarted(boolean isCallBackProcessingStarted) {
		this.isCallBackProcessingStarted = isCallBackProcessingStarted;
	}
	public boolean isCallBackProcessingCompleted() {
		return isCallBackProcessingCompleted;
	}
	public void setCallBackProcessingCompleted(boolean isCallBackProcessingCompleted) {
		this.isCallBackProcessingCompleted = isCallBackProcessingCompleted;
	}
	

}
