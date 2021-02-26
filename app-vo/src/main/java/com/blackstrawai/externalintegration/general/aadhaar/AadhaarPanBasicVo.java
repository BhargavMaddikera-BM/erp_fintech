package com.blackstrawai.externalintegration.general.aadhaar;

public class AadhaarPanBasicVo {

	private String panStatus;
	private String fullName;
	private String message;
	private String panNumber;
	public String getPanNumber() {
		return panNumber;
	}
	public void setPanNumber(String panNumber) {
		this.panNumber = panNumber;
	}
	
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getPanStatus() {
		return panStatus;
	}
	public void setPanStatus(String panStatus) {
		this.panStatus = panStatus;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	
		
}
