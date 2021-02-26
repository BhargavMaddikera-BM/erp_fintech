package com.blackstrawai.externalintegration.general.aadhaar;

import java.util.List;

public class AadhaarGstnBasicVo {

	private String stateJursdiction;
	private String legalName;
	private String stateJurisdictionCode;
	private String taxpayerType;
	private List<AadhaarGstnAdditionalPlacesVo> additionalPlacesOfBusnessInState;//additionalPlaces Of Business in state of registration;
	private String DateOfCancelofReg;//Date of Cancellation of Registration
	private String gstin;
	private List<String> natureOfBusiness;//Nature of business registered under GST (REQUIRED)
	private String lastupdated;
	private String registrationDate;// Registration date under GST (REQUIRED) 
	private String constOfBusiness;//ctb : Constitution of Business (proprietorship, partnership, private limited company, public limited company etc) (REQUIRED)
	private AadhaarGstnContactInfoBasicVo primaryBusinessContactInfo;//pradr : Primary business contact information including (REQUIRED)	
	private String tradeName;
	private String currentStatus;//sts : Current status of registration under GST (REQUIRED)
	private String centralJurisdictionCode;
	private String centralJurisdiction;
	public String getStateJursdiction() {
		return stateJursdiction;
	}
	public void setStateJursdiction(String stateJursdiction) {
		this.stateJursdiction = stateJursdiction;
	}
	public String getLegalName() {
		return legalName;
	}
	public void setLegalName(String legalName) {
		this.legalName = legalName;
	}
	public String getStateJurisdictionCode() {
		return stateJurisdictionCode;
	}
	public void setStateJurisdictionCode(String stateJurisdictionCode) {
		this.stateJurisdictionCode = stateJurisdictionCode;
	}
	public String getTaxpayerType() {
		return taxpayerType;
	}
	public void setTaxpayerType(String taxpayerType) {
		this.taxpayerType = taxpayerType;
	}
	public List<AadhaarGstnAdditionalPlacesVo> getAddiPlacesBusnsInStateReg() {
		return additionalPlacesOfBusnessInState;
	}
	public void setAddiPlacesBusnsInStateReg(List<AadhaarGstnAdditionalPlacesVo> addiPlacesBusnsInStateReg) {
		this.additionalPlacesOfBusnessInState = addiPlacesBusnsInStateReg;
	}
	public String getDateOfCancelofReg() {
		return DateOfCancelofReg;
	}
	public void setDateOfCancelofReg(String dateOfCancelofReg) {
		DateOfCancelofReg = dateOfCancelofReg;
	}
	public String getGstin() {
		return gstin;
	}
	public void setGstin(String gstin) {
		this.gstin = gstin;
	}
	public List<String> getNatureOfBusiness() {
		return natureOfBusiness;
	}
	public void setNatureOfBusiness(List<String> natureOfBusiness) {
		this.natureOfBusiness = natureOfBusiness;
	}
	public String getLastupdated() {
		return lastupdated;
	}
	public void setLastupdated(String lastupdated) {
		this.lastupdated = lastupdated;
	}
	public String getRegistrationDate() {
		return registrationDate;
	}
	public void setRegistrationDate(String registrationDate) {
		this.registrationDate = registrationDate;
	}
	public String getConstOfBusiness() {
		return constOfBusiness;
	}
	public void setConstOfBusiness(String constOfBusiness) {
		this.constOfBusiness = constOfBusiness;
	}
	public AadhaarGstnContactInfoBasicVo getPrimaryBusinessContactInfo() {
		return primaryBusinessContactInfo;
	}
	public void setPrimaryBusinessContactInfo(AadhaarGstnContactInfoBasicVo primaryBusinessContactInfo) {
		this.primaryBusinessContactInfo = primaryBusinessContactInfo;
	}
	public String getTradeName() {
		return tradeName;
	}
	public void setTradeName(String tradeName) {
		this.tradeName = tradeName;
	}
	public String getCurrentStatus() {
		return currentStatus;
	}
	public void setCurrentStatus(String currentStatus) {
		this.currentStatus = currentStatus;
	}
	public String getCentralJurisdictionCode() {
		return centralJurisdictionCode;
	}
	public void setCentralJurisdictionCode(String centralJurisdictionCode) {
		this.centralJurisdictionCode = centralJurisdictionCode;
	}
	public String getCentralJurisdiction() {
		return centralJurisdiction;
	}
	public void setCentralJurisdiction(String centralJurisdiction) {
		this.centralJurisdiction = centralJurisdiction;
	}
	


}
