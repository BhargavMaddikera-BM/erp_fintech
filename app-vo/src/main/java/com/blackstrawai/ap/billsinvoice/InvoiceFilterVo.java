package com.blackstrawai.ap.billsinvoice;

import com.blackstrawai.common.BaseVo;

public class InvoiceFilterVo extends BaseVo {

	private Integer orgId;

	private Boolean isInvoiceWithBills;

	private String status;

	private Integer vendorId;

	private Double fromAmount;

	private Double toAmount;

	private String startDate;

	private String endDate;

	private String roleName;

	private Boolean dueAmountGreaterZero;

	private Integer currencyId;

	public Integer getCurrencyId() {
		return currencyId;
	}

	public void setCurrencyId(Integer currencyId) {
		this.currencyId = currencyId;
	}

	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}

	public Boolean getIsInvoiceWithBills() {
		return isInvoiceWithBills;
	}

	public void setIsInvoiceWithBills(Boolean isInvoiceWithBills) {
		this.isInvoiceWithBills = isInvoiceWithBills;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getVendorId() {
		return vendorId;
	}

	public void setVendorId(Integer vendorId) {
		this.vendorId = vendorId;
	}

	public Double getFromAmount() {
		return fromAmount;
	}

	public void setFromAmount(Double fromAmount) {
		this.fromAmount = fromAmount;
	}

	public Double getToAmount() {
		return toAmount;
	}

	public void setToAmount(Double toAmount) {
		this.toAmount = toAmount;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public Boolean getDueAmountGreaterZero() {
		return dueAmountGreaterZero;
	}

	public void setDueAmountGreaterZero(Boolean dueAmountGreaterZero) {
		this.dueAmountGreaterZero = dueAmountGreaterZero;
	}

	@Override
	public String toString() {
		return "InvoiceFilterVo [orgId=" + orgId + ", isInvoiceWithBills=" + isInvoiceWithBills + ", status=" + status
				+ ", vendorId=" + vendorId + ", fromAmount=" + fromAmount + ", toAmount=" + toAmount + ", startDate="
				+ startDate + ", endDate=" + endDate + ", roleName=" + roleName + ", dueAmountGreaterZero="
				+ dueAmountGreaterZero + ", currencyId=" + currencyId + "]";
	}

}
