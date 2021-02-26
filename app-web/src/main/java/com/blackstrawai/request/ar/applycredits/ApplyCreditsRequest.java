package com.blackstrawai.request.ar.applycredits;

import java.sql.Date;
import java.util.List;

import com.blackstrawai.ar.receipt.ReceiptSettingsVo;
import com.blackstrawai.common.BaseRequest;
import com.blackstrawai.request.attachments.UploadFileRequest;

public class ApplyCreditsRequest extends BaseRequest {

	private String voucherNo;
	private Date date;
	private Integer customerId;
	private Integer ledgerId;
	private Integer currencyId;
	private String exchangeRate;
	private List<ApplyCreditsInvoiceRequest> invoiceDetails;
	private String invoiceTotalAmount;
	private List<ApplyCreditDetailsRequest> creditDetails;
	private String adjustedCreditAmount;
	private String availableFund;
	private List<UploadFileRequest> attachments;
	private List<Integer> attachmentsToRemove;
	private Integer organizationId;
	private Integer id;
	private String status;
	private Boolean isSuperAdmin;
	private List<ReceiptSettingsVo> customTableList;
	private String ledgerName;
	private String roleName;
	private List<Integer> invoiceItemsToRemove;
	private List<Integer> creditsItemsToRemove;

	public String getExchangeRate() {
		return exchangeRate;
	}

	public void setExchangeRate(String exchangeRate) {
		this.exchangeRate = exchangeRate;
	}

	public List<Integer> getCreditsItemsToRemove() {
		return creditsItemsToRemove;
	}

	public void setCreditsItemsToRemove(List<Integer> creditsItemsToRemove) {
		this.creditsItemsToRemove = creditsItemsToRemove;
	}

	public List<Integer> getInvoiceItemsToRemove() {
		return invoiceItemsToRemove;
	}

	public void setInvoiceItemsToRemove(List<Integer> invoiceItemsToRemove) {
		this.invoiceItemsToRemove = invoiceItemsToRemove;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getLedgerName() {
		return ledgerName;
	}

	public void setLedgerName(String ledgerName) {
		this.ledgerName = ledgerName;
	}

	public List<ReceiptSettingsVo> getCustomTableList() {
		return customTableList;
	}

	public void setCustomTableList(List<ReceiptSettingsVo> customTableList) {
		this.customTableList = customTableList;
	}

	public Integer getCurrencyId() {
		return currencyId;
	}

	public void setCurrencyId(Integer currencyId) {
		this.currencyId = currencyId;
	}

	public Boolean getIsSuperAdmin() {
		return isSuperAdmin;
	}

	public void setIsSuperAdmin(Boolean isSuperAdmin) {
		this.isSuperAdmin = isSuperAdmin;
	}

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

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

	public List<ApplyCreditsInvoiceRequest> getInvoiceDetails() {
		return invoiceDetails;
	}

	public void setInvoiceDetails(List<ApplyCreditsInvoiceRequest> invoiceDetails) {
		this.invoiceDetails = invoiceDetails;
	}

	public List<UploadFileRequest> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<UploadFileRequest> attachments) {
		this.attachments = attachments;
	}

	public List<Integer> getAttachmentsToRemove() {
		return attachmentsToRemove;
	}

	public void setAttachmentsToRemove(List<Integer> attachmentsToRemove) {
		this.attachmentsToRemove = attachmentsToRemove;
	}

	public String getVoucherNo() {
		return voucherNo;
	}

	public void setVoucherNo(String voucherNo) {
		this.voucherNo = voucherNo;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public Integer getLedgerId() {
		return ledgerId;
	}

	public void setLedgerId(Integer ledgerId) {
		this.ledgerId = ledgerId;
	}

	public String getInvoiceTotalAmount() {
		return invoiceTotalAmount;
	}

	public void setInvoiceTotalAmount(String invoiceTotalAmount) {
		this.invoiceTotalAmount = invoiceTotalAmount;
	}

	public List<ApplyCreditDetailsRequest> getCreditDetails() {
		return creditDetails;
	}

	public void setCreditDetails(List<ApplyCreditDetailsRequest> creditDetails) {
		this.creditDetails = creditDetails;
	}

	public String getAdjustedCreditAmount() {
		return adjustedCreditAmount;
	}

	public void setAdjustedCreditAmount(String adjustedCreditAmount) {
		this.adjustedCreditAmount = adjustedCreditAmount;
	}

	public String getAvailableFund() {
		return availableFund;
	}

	public void setAvailableFund(String availableFund) {
		this.availableFund = availableFund;
	}

}
