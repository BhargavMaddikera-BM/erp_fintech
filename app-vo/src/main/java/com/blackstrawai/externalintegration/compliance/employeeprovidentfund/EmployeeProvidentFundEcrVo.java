package com.blackstrawai.externalintegration.compliance.employeeprovidentfund;

import java.util.List;

public class EmployeeProvidentFundEcrVo {
	private int id;
	private String trrn;
	private String wageMonth;
	private String ecrType;
	private String salaryDisbDate;
	private String contrRate;
	private String uploadDate;
	private String status;
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
	public String getSalaryDisbDate() {
		return salaryDisbDate;
	}
	public void setSalaryDisbDate(String salaryDisbDate) {
		this.salaryDisbDate = salaryDisbDate;
	}
	public String getContrRate() {
		return contrRate;
	}
	public void setContrRate(String contrRate) {
		this.contrRate = contrRate;
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
