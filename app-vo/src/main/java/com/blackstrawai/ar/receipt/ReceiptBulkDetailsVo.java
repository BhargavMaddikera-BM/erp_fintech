package com.blackstrawai.ar.receipt;

import java.sql.Timestamp;


public class ReceiptBulkDetailsVo{
	private Integer id;    
	private Integer typeId;
	private Integer referenceId;    
	private String referenceType;   
	private String parentType ;   
	private String invoiceDueAmount;
	private String amount;    
	private Integer receiptId;    
	private String data;
	private String bankCharges;
	private String tdsDeducted;
	private String others1;
	private String others2;
	private String others3;
	private String status;   
	private Timestamp createTs;    
	private Timestamp updateTs;
	
	
	
	public String getParentType() {
		return parentType;
	}
	public void setParentType(String parentType) {
		this.parentType = parentType;
	}
	public String getInvoiceDueAmount() {
		return invoiceDueAmount;
	}
	public void setInvoiceDueAmount(String invoiceDueAmount) {
		this.invoiceDueAmount = invoiceDueAmount;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
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
	public String getOthers2() {
		return others2;
	}
	public void setOthers2(String others2) {
		this.others2 = others2;
	}
	public String getOthers3() {
		return others3;
	}
	public void setOthers3(String others3) {
		this.others3 = others3;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getTypeId() {
		return typeId;
	}
	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}
	public Integer getReferenceId() {
		return referenceId;
	}
	public void setReferenceId(Integer referenceId) {
		this.referenceId = referenceId;
	}
	public String getReferenceType() {
		return referenceType;
	}
	public void setReferenceType(String referenceType) {
		this.referenceType = referenceType;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public Integer getReceiptId() {
		return receiptId;
	}
	public void setReceiptId(Integer receiptId) {
		this.receiptId = receiptId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Timestamp getCreateTs() {
		return createTs;
	}
	public void setCreateTs(Timestamp createTs) {
		this.createTs = createTs;
	}
	public Timestamp getUpdateTs() {
		return updateTs;
	}
	public void setUpdateTs(Timestamp updateTs) {
		this.updateTs = updateTs;
	}
	@Override
	public String toString() {
		return "ReceiptBulkDetailsVo [id=" + id + ", type=" + typeId + ", referenceId=" + referenceId + ", referenceType="
				+  ", amount=" + amount + ", receiptId=" + receiptId + ", status=" + status
				+ ", createTs=" + createTs + ", updateTs=" + updateTs + "]";
	}
	

}