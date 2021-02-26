package com.blackstrawai.ar.lut;

import java.sql.Timestamp;
import java.util.List;

import com.blackstrawai.attachments.UploadFileVo;
import com.blackstrawai.common.TokenVo;


public class LetterOfUndertakingVo extends TokenVo{
	private int id;    
	private String dateOfCreation;    
	private String expiryDate;    
	private String ackNo;
	private String finYear;    
	private String gstNumber;
	private int locationId;
	private List<UploadFileVo> attachments;
	private List<Integer> attachmentsToRemove;
	private String status;    
	private int organizationId;    
	private Timestamp createTs;    
	private Timestamp updateTs;    
	private Boolean isSuperAdmin;   
	private String roleName;
	
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}	

	public String getFinYear() {
		return finYear;
	}
	public void setFinYear(String finYear) {
		this.finYear = finYear;
	}
	public String getGstNumber() {
		return gstNumber;
	}
	public void setGstNumber(String gstNumber) {
		this.gstNumber = gstNumber;
	}
	public int getLocationId() {
		return locationId;
	}
	public void setLocationId(int locationId) {
		this.locationId = locationId;
	}
	public List<UploadFileVo> getAttachments() {
		return attachments;
	}
	public void setAttachments(List<UploadFileVo> attachments) {
		this.attachments = attachments;
	}
	public List<Integer> getAttachmentsToRemove() {
		return attachmentsToRemove;
	}
	public void setAttachmentsToRemove(List<Integer> attachmentsToRemove) {
		this.attachmentsToRemove = attachmentsToRemove;
	}
	public void setId(int id){
		this.id=id;
	}
	public int getId(){
		return  id; 
	}
	
	public String getDateOfCreation() {
		return dateOfCreation;
	}
	public void setDateOfCreation(String dateOfCreation) {
		this.dateOfCreation = dateOfCreation;
	}
	public void setExpiryDate(String expiryDate){
		this.expiryDate=expiryDate;
	}
	public String getExpiryDate(){
		return  expiryDate; 
	}
	public void setAckNo(String ackNo){
		this.ackNo=ackNo;
	}
	public String getAckNo(){
		return  ackNo; 
	}
	
	public void setStatus(String status){
		this.status=status;
	}
	public String getStatus(){
		return  status; 
	}
	
	public void setOrganizationId(int organizationId){
		this.organizationId=organizationId;
	}
	public int getOrganizationId(){
		return  organizationId; 
	}
	public void setCreateTs(Timestamp createTs){
		this.createTs=createTs;
	}
	public Timestamp getCreateTs(){
		return  createTs; 
	}
	public void setUpdateTs(Timestamp updateTs){
		this.updateTs=updateTs;
	}
	public Timestamp getUpdateTs(){
		return  updateTs; 
	}
	public void setIsSuperAdmin(Boolean isSuperAdmin){
		this.isSuperAdmin=isSuperAdmin;
	}
	public Boolean getIsSuperAdmin(){
		return  isSuperAdmin; 
	}
}