package com.blackstrawai.ap.creditnote;

import java.util.Objects;

public class VendorCreditNoteDetailsVo {

	private Integer id;
	private String status;
	private Integer creditNoteId;
	private String creditNoteRefNo;
	private Double creditAmount;
	private Double refundAmount;

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

	public Integer getCreditNoteId() {
		return creditNoteId;
	}

	public void setCreditNoteId(Integer creditNoteId) {
		this.creditNoteId = creditNoteId;
	}

	public String getCreditNoteRefNo() {
		return creditNoteRefNo;
	}

	public void setCreditNoteRefNo(String creditNoteRefNo) {
		this.creditNoteRefNo = creditNoteRefNo;
	}

	public Double getCreditAmount() {
		return creditAmount;
	}

	public void setCreditAmount(Double creditAmount) {
		this.creditAmount = creditAmount;
	}

	public Double getRefundAmount() {
		return refundAmount;
	}

	public void setRefundAmount(Double refundAmount) {
		this.refundAmount = refundAmount;
	}

	@Override
	public int hashCode() {
		return Objects.hash(creditAmount, creditNoteId, creditNoteRefNo, id, refundAmount, status);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof VendorCreditNoteDetailsVo))
			return false;
		VendorCreditNoteDetailsVo other = (VendorCreditNoteDetailsVo) obj;
		return Objects.equals(creditAmount, other.creditAmount) && Objects.equals(creditNoteId, other.creditNoteId)
				&& Objects.equals(creditNoteRefNo, other.creditNoteRefNo) && Objects.equals(id, other.id)
				&& Objects.equals(refundAmount, other.refundAmount) && Objects.equals(status, other.status);
	}

	@Override
	public String toString() {
		return "VendorCreditNoteDetailsVo [id=" + id + ", status=" + status + ", creditNoteId=" + creditNoteId
				+ ", creditNoteRefNo=" + creditNoteRefNo + ", creditAmount=" + creditAmount + ", refundAmount="
				+ refundAmount + "]";
	}

}
