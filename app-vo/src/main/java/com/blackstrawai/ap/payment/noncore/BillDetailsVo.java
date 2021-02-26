package com.blackstrawai.ap.payment.noncore;

import java.sql.Date;

public class BillDetailsVo {
	private Integer id;
	private String status;   
//	private String billRef;  
	private int billRef;
	private String billAmount;
	private String totalAmount;
	private String dueAmount;    
	private Date dueDate;
	private String bankCharges;
	private String tdsDeducted; 
	private String others1;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getBillRef() {
		return billRef;
	}
	public void setBillRef(int billRef) {
		this.billRef = billRef;
	}
	public String getBillAmount() {
		return billAmount;
	}
	public void setBillAmount(String billAmount) {
		this.billAmount = billAmount;
	}
	public String getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}
	public String getDueAmount() {
		return dueAmount;
	}
	public void setDueAmount(String dueAmount) {
		this.dueAmount = dueAmount;
	}
	public Date getDueDate() {
		return dueDate;
	}
	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}
	public String getBankCharges() {
		return bankCharges;
	}
	public void setBankCharges(String bankCharges) {
		this.bankCharges = bankCharges;
	}
	public String getTdsDeducted() {
		return tdsDeducted;
	}
	public void setTdsDeducted(String tdsDeducted) {
		this.tdsDeducted = tdsDeducted;
	}
	public String getOthers1() {
		return others1;
	}
	public void setOthers1(String others1) {
		this.others1 = others1;
	} 
	
	       
}
