package com.blackstrawai.externalintegration.general.aadhaar;

import java.util.List;

public class AadhaarGstnAdvanceVo {

	private List<String> listOFAuthorizedSignOfBusiness;//additionalPlaces Of Business in state of registration;
	private String canFlag;
	private AadhaarGstnContactInfoAdvanceVo primaryBusinessContactInfo;//pradr : Primary business contact information including (REQUIRED)
	private String tradeName;
	private AadhaarGstnAdvContactDetailsVo contactDetails;
	private String gstin;
	private List<String> natureOfBusiness;//Nature of business registered under GST (REQUIRED)
	private String stateJurisdictionCode;
	private String stateJurisdiction;
	private String ppr;
	private String taxpayerType;
	private String complianceRatingOFBusiness;//Compliance rating of the business
	private String lastupdated;
	private String constOfBusiness;//ctb : Constitution of Business (proprietorship, partnership, private limited company, public limited company etc) (REQUIRED)
	private String currentStatus;//sts : Current status of registration under GST (REQUIRED)
	private String DateOfCancelofReg;//Date of Cancellation of Registration
	private List<AadhaarGstnAdditionalPlacesVo> additionalPlacesOfBusnessInState;//additionalPlaces Of Business in state of registration;
	private String legalName;
	private String centralJurisdictionCode;
	private String centralJurisdiction;//
	private String registrationDate;// Registration date under GST (REQUIRED) 
	
	public List<String> getListOFAuthorizedSignOfBusiness() {
		return listOFAuthorizedSignOfBusiness;
	}
	public void setListOFAuthorizedSignOfBusiness(List<String> listOFAuthorizedSignOfBusiness) {
		this.listOFAuthorizedSignOfBusiness = listOFAuthorizedSignOfBusiness;
	}
	public String getCanFlag() {
		return canFlag;
	}
	public void setCanFlag(String canFlag) {
		this.canFlag = canFlag;
	}
	public AadhaarGstnContactInfoAdvanceVo getPrimaryBusinessContactInfo() {
		return primaryBusinessContactInfo;
	}
	public void setPrimaryBusinessContactInfo(AadhaarGstnContactInfoAdvanceVo primaryBusinessContactInfo) {
		this.primaryBusinessContactInfo = primaryBusinessContactInfo;
	}
	public String getTradeName() {
		return tradeName;
	}
	public void setTradeName(String tradeName) {
		this.tradeName = tradeName;
	}
	
	
	public AadhaarGstnAdvContactDetailsVo getContactDetails() {
		return contactDetails;
	}
	public void setContactDetails(AadhaarGstnAdvContactDetailsVo contactDetails) {
		this.contactDetails = contactDetails;
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
	public String getStateJurisdictionCode() {
		return stateJurisdictionCode;
	}
	public void setStateJurisdictionCode(String stateJurisdictionCode) {
		this.stateJurisdictionCode = stateJurisdictionCode;
	}
	public String getStateJurisdiction() {
		return stateJurisdiction;
	}
	public void setStateJurisdiction(String stateJurisdiction) {
		this.stateJurisdiction = stateJurisdiction;
	}
	public String getPpr() {
		return ppr;
	}
	public void setPpr(String ppr) {
		this.ppr = ppr;
	}
	public String getTaxpayerType() {
		return taxpayerType;
	}
	public void setTaxpayerType(String taxpayerType) {
		this.taxpayerType = taxpayerType;
	}
	public String getComplianceRatingOFBusiness() {
		return complianceRatingOFBusiness;
	}
	public void setComplianceRatingOFBusiness(String complianceRatingOFBusiness) {
		this.complianceRatingOFBusiness = complianceRatingOFBusiness;
	}
	public String getLastupdated() {
		return lastupdated;
	}
	public void setLastupdated(String lastupdated) {
		this.lastupdated = lastupdated;
	}
	public String getConstOfBusiness() {
		return constOfBusiness;
	}
	public void setConstOfBusiness(String constOfBusiness) {
		this.constOfBusiness = constOfBusiness;
	}
	public String getCurrentStatus() {
		return currentStatus;
	}
	public void setCurrentStatus(String currentStatus) {
		this.currentStatus = currentStatus;
	}
	public String getDateOfCancelofReg() {
		return DateOfCancelofReg;
	}
	public void setDateOfCancelofReg(String dateOfCancelofReg) {
		DateOfCancelofReg = dateOfCancelofReg;
	}
	
	public List<AadhaarGstnAdditionalPlacesVo> getAdditionalPlacesOfBusnessInState() {
		return additionalPlacesOfBusnessInState;
	}
	public void setAdditionalPlacesOfBusnessInState(List<AadhaarGstnAdditionalPlacesVo> additionalPlacesOfBusnessInState) {
		this.additionalPlacesOfBusnessInState = additionalPlacesOfBusnessInState;
	}
	public String getLegalName() {
		return legalName;
	}
	public void setLegalName(String legalName) {
		this.legalName = legalName;
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
	public String getRegistrationDate() {
		return registrationDate;
	}
	public void setRegistrationDate(String registrationDate) {
		this.registrationDate = registrationDate;
	}
	
		}
