package com.blackstrawai.payroll.payrun;

public class PayRunPaymentTypeVo {
	
	private String id;
	private String name;
	private String value;
	private String payRunPaymentDesc;
	private String payRunPaymentParentName;
	private Boolean payRunPaymentIsBase;
	private String payRunPaymentDebitOrCredit;
	
	
	public String getPayRunPaymentDesc() {
		return payRunPaymentDesc;
	}
	public void setPayRunPaymentDesc(String payRunPaymentDesc) {
		this.payRunPaymentDesc = payRunPaymentDesc;
	}
	public String getPayRunPaymentParentName() {
		return payRunPaymentParentName;
	}
	public void setPayRunPaymentParentName(String payRunPaymentParentName) {
		this.payRunPaymentParentName = payRunPaymentParentName;
	}
	public Boolean getPayRunPaymentIsBase() {
		return payRunPaymentIsBase;
	}
	public void setPayRunPaymentIsBase(Boolean payRunPaymentIsBase) {
		this.payRunPaymentIsBase = payRunPaymentIsBase;
	}
	public String getPayRunPaymentDebitOrCredit() {
		return payRunPaymentDebitOrCredit;
	}
	public void setPayRunPaymentDebitOrCredit(String payRunPaymentDebitOrCredit) {
		this.payRunPaymentDebitOrCredit = payRunPaymentDebitOrCredit;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	
	
	

}
