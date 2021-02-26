package com.blackstrawai.externalintegration.general.aadhaar;

public class AadhaarPanAdvanceVo {

	private String panStatus;
	private String lastName;
	private String firstName;
	private String panHolderTitle;
	private String panLastUpdatedDate;
	private String panNumber;
	public String getPanNumber() {
		return panNumber;
	}
	public void setPanNumber(String panNumber) {
		this.panNumber = panNumber;
	}
	
	public String getPanStatus() {
		return panStatus;
	}
	public void setPanStatus(String panStatus) {
		this.panStatus = panStatus;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getPanHolderTitle() {
		return panHolderTitle;
	}
	public void setPanHolderTitle(String panHolderTitle) {
		this.panHolderTitle = panHolderTitle;
	}
	public String getPanLastUpdatedDate() {
		return panLastUpdatedDate;
	}
	public void setPanLastUpdatedDate(String panLastUpdatedDate) {
		this.panLastUpdatedDate = panLastUpdatedDate;
	}
	
		
}
