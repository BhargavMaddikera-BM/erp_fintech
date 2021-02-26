package com.blackstrawai.request.ap.creditnote;

public class VendorCreditGeneralInformation {

	private Integer vendorId;
	private String vendorGstNo;
	private Integer locationId;
	private String orgGst;
	private Integer invoiceId;
	private String creditNoteNo;
	private String creditNoteDate;
	private String voucherNo;
	private String voucherDate;
	private String reason;
	private String notes;
	private VendorCreditAddress address;
	private String additionalTerms;
	private String aksharData;

	public Integer getVendorId() {
		return vendorId;
	}

	public void setVendorId(Integer vendorId) {
		this.vendorId = vendorId;
	}

	public String getVendorGstNo() {
		return vendorGstNo;
	}

	public void setVendorGstNo(String vendorGstNo) {
		this.vendorGstNo = vendorGstNo;
	}

	public Integer getLocationId() {
		return locationId;
	}

	public void setLocationId(Integer locationId) {
		this.locationId = locationId;
	}

	public String getOrgGst() {
		return orgGst;
	}

	public void setOrgGst(String orgGst) {
		this.orgGst = orgGst;
	}

	public Integer getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(Integer invoiceId) {
		this.invoiceId = invoiceId;
	}

	public String getCreditNoteNo() {
		return creditNoteNo;
	}

	public void setCreditNoteNo(String creditNoteNo) {
		this.creditNoteNo = creditNoteNo;
	}

	public String getCreditNoteDate() {
		return creditNoteDate;
	}

	public void setCreditNoteDate(String creditNoteDate) {
		this.creditNoteDate = creditNoteDate;
	}

	public String getVoucherNo() {
		return voucherNo;
	}

	public void setVoucherNo(String voucherNo) {
		this.voucherNo = voucherNo;
	}

	public String getVoucherDate() {
		return voucherDate;
	}

	public void setVoucherDate(String voucherDate) {
		this.voucherDate = voucherDate;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public VendorCreditAddress getAddress() {
		return address;
	}

	public void setAddress(VendorCreditAddress address) {
		this.address = address;
	}

	public String getAdditionalTerms() {
		return additionalTerms;
	}

	public void setAdditionalTerms(String additionalTerms) {
		this.additionalTerms = additionalTerms;
	}

	public String getAksharData() {
		return aksharData;
	}

	public void setAksharData(String aksharData) {
		this.aksharData = aksharData;
	}

	@Override
	public String toString() {
		return "VendorCreditGeneralInformation [vendorId=" + vendorId + ", vendorGstNo=" + vendorGstNo + ", locationId="
				+ locationId + ", orgGst=" + orgGst + ", invoiceId=" + invoiceId + ", creditNoteNo=" + creditNoteNo
				+ ", creditNoteDate=" + creditNoteDate + ", voucherNo=" + voucherNo + ", voucherDate=" + voucherDate
				+ ", reason=" + reason + ", notes=" + notes + ", address=" + address + ", additionalTerms="
				+ additionalTerms + ", aksharData=" + aksharData + "]";
	}

}
