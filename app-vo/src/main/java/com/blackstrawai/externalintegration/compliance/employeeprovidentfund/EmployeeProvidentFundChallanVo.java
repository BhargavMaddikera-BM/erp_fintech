package com.blackstrawai.externalintegration.compliance.employeeprovidentfund;

import java.util.List;

public class EmployeeProvidentFundChallanVo {
	private int id;
	private String trrn;
	private String wageMonth;
	private String ecrType;
	private String uploadDate;
	private String status;
	private String ac1;
	private String ac2;
	private String ac10;
	private String ac21;
	private String ac22;
	private String totalAmount;
	private String crn;
	private List<EmployeeProvidentFundAttachmentVo> attachments;
	
	public String getTrrn() {
		return trrn;
	}
	public void setTrrn(String trrn) {
		this.trrn = trrn;
	}
	public String getWageMonth() {
		return wageMonth;
	}
	public void setWageMonth(String wageMonth) {
		this.wageMonth = wageMonth;
	}
	public String getEcrType() {
		return ecrType;
	}
	public void setEcrType(String ecrType) {
		this.ecrType = ecrType;
	}
	public String getUploadDate() {
		return uploadDate;
	}
	public void setUploadDate(String uploadDate) {
		this.uploadDate = uploadDate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getAc1() {
		return ac1;
	}
	public void setAc1(String ac1) {
		this.ac1 = ac1;
	}
	public String getAc2() {
		return ac2;
	}
	public void setAc2(String ac2) {
		this.ac2 = ac2;
	}
	public String getAc10() {
		return ac10;
	}
	public void setAc10(String ac10) {
		this.ac10 = ac10;
	}
	public String getAc21() {
		return ac21;
	}
	public void setAc21(String ac21) {
		this.ac21 = ac21;
	}
	public String getAc22() {
		return ac22;
	}
	public void setAc22(String ac22) {
		this.ac22 = ac22;
	}
	public String getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}
	public String getCrn() {
		return crn;
	}
	public void setCrn(String crn) {
		this.crn = crn;
	}
	public List<EmployeeProvidentFundAttachmentVo> getAttachments() {
		return attachments;
	}
	public void setAttachments(List<EmployeeProvidentFundAttachmentVo> attachments) {
		this.attachments = attachments;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	
}
