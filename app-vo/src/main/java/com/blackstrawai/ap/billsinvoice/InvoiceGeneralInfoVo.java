package com.blackstrawai.ap.billsinvoice;

import java.sql.Date;

public class InvoiceGeneralInfoVo {

	private Integer vendorId;
	
	private String invoiceDate;
	
	private Integer locationId;
	
	private String vendorGstNo;
	
	private String gstNumberId;
	
	private boolean isRegistered;
	
	private String invoiceNoPrefix;
	
	private String invoiceNoSuffix;
	
	private String invoiceNo;
	
	private String poReferenceNo;
	
	private Integer paymentTermsId;
	
	private String dueDate;
	
	private String notes;
	
	private String termsAndConditions;
	
	private Date create_ts;
	
	private boolean isQuick;

	private String aksharData;
	

	
	public String getVendorGstNo() {
		return vendorGstNo;
	}

	public void setVendorGstNo(String vendorGstNo) {
		this.vendorGstNo = vendorGstNo;
	}

	public String getInvoiceNoPrefix() {
		return invoiceNoPrefix;
	}

	public void setInvoiceNoPrefix(String invoiceNoPrefix) {
		this.invoiceNoPrefix = invoiceNoPrefix;
	}

	public String getInvoiceNoSuffix() {
		return invoiceNoSuffix;
	}

	public void setInvoiceNoSuffix(String invoiceNoSuffix) {
		this.invoiceNoSuffix = invoiceNoSuffix;
	}

	public String getAksharData() {
		return aksharData;
	}

	public void setAksharData(String aksharData) {
		this.aksharData = aksharData;
	}

	public Integer getVendorId() {
		return vendorId;
	}

	public void setVendorId(Integer vendorId) {
		this.vendorId = vendorId;
	}

	public String getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(String invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public Integer getLocationId() {
		return locationId;
	}

	public void setLocationId(Integer locationId) {
		this.locationId = locationId;
	}

	public String getGstNumberId() {
		return gstNumberId;
	}

	public void setGstNumberId(String gstNumberId) {
		this.gstNumberId = gstNumberId;
	}

	public String getInvoiceNo() {
		return invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public String getPoReferenceNo() {
		return poReferenceNo;
	}

	public void setPoReferenceNo(String poReferenceNo) {
		this.poReferenceNo = poReferenceNo;
	}

	public Integer getPaymentTermsId() {
		return paymentTermsId;
	}

	public void setPaymentTermsId(Integer paymentTermsId) {
		this.paymentTermsId = paymentTermsId;
	}

	public String getDueDate() {
		return dueDate;
	}

	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getTermsAndConditions() {
		return termsAndConditions;
	}

	public void setTermsAndConditions(String termsAndConditions) {
		this.termsAndConditions = termsAndConditions;
	}

	
	public Date getCreate_ts() {
		return create_ts;
	}

	public void setCreate_ts(Date create_ts) {
		this.create_ts = create_ts;
	}
	

	public boolean getIsRegistered() {
		return isRegistered;
	}

	public void setIsRegistered(boolean isRegistered) {
		this.isRegistered = isRegistered;
	}

	
	public boolean getIsQuick() {
		return isQuick;
	}

	public void setIsQuick(boolean isQuick) {
		this.isQuick = isQuick;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("InvoiceGeneralInfoVo [vendorId=");
		builder.append(vendorId);
		builder.append(", invoiceDate=");
		builder.append(invoiceDate);
		builder.append(", locationId=");
		builder.append(locationId);
		builder.append(", gstNumberId=");
		builder.append(gstNumberId);
		builder.append(", isRegistered=");
		builder.append(isRegistered);
		builder.append(", invoiceNo=");
		builder.append(invoiceNo);
		builder.append(", poReferenceNo=");
		builder.append(poReferenceNo);
		builder.append(", paymentTermsId=");
		builder.append(paymentTermsId);
		builder.append(", dueDate=");
		builder.append(dueDate);
		builder.append(", notes=");
		builder.append(notes);
		builder.append(", termsAndConditions=");
		builder.append(termsAndConditions);
		builder.append(", create_ts=");
		builder.append(create_ts);
		builder.append("]");
		return builder.toString();
	}

	
	
	
}
