package com.blackstrawai.ar.applycredits;

import java.sql.Date;
import java.util.List;

import com.blackstrawai.ar.receipt.ReceiptSettingsVo;
import com.blackstrawai.attachments.UploadFileVo;
import com.blackstrawai.common.BaseVo;

public class ApplyCreditsVo extends BaseVo {

	private String voucherNo;
	private Date date;
	private Integer customerId;
	private Integer ledgerId;
	private String ledgerName;
	private Integer currencyId;
	private String exchangeRate;
	private List<ApplyCreditsInvoiceVo> invoiceDetails;
	private String invoiceTotalAmount;
	private List<ApplyCreditDetailsVo> creditDetails;
	private String adjustedCreditAmount;
	private String availableFund;
	private List<UploadFileVo> attachments;
	private List<Integer> attachmentsToRemove;
	private Integer organizationId;
	private Integer id;
	private String status;
	private Boolean isSuperAdmin;
	private List<ReceiptSettingsVo> customTableList;
	private String roleName;
	private List<Integer> invoiceItemsToRemove;
	private List<Integer> creditsItemsToRemove;
	private Date createTs;

	
	public Date getCreateTs() {
		return createTs;
	}

	public void setCreateTs(Date createTs) {
		this.createTs = createTs;
	}

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

	public List<ReceiptSettingsVo> getCustomTableList() {
		return customTableList;
	}

	public void setCustomTableList(List<ReceiptSettingsVo> customTableList) {
		this.customTableList = customTableList;
	}

	public String getLedgerName() {
		return ledgerName;
	}

	public void setLedgerName(String ledgerName) {
		this.ledgerName = ledgerName;
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

	public List<ApplyCreditsInvoiceVo> getInvoiceDetails() {
		return invoiceDetails;
	}

	public void setInvoiceDetails(List<ApplyCreditsInvoiceVo> invoiceDetails) {
		this.invoiceDetails = invoiceDetails;
	}

	public String getInvoiceTotalAmount() {
		return invoiceTotalAmount;
	}

	public void setInvoiceTotalAmount(String invoiceTotalAmount) {
		this.invoiceTotalAmount = invoiceTotalAmount;
	}

	public List<ApplyCreditDetailsVo> getCreditDetails() {
		return creditDetails;
	}

	public void setCreditDetails(List<ApplyCreditDetailsVo> creditDetails) {
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

	public List<UploadFileVo> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<UploadFileVo> attachments) {
		this.attachments = attachments;
	}

	public List<Integer> getAttachmentsToRemove() {
		return attachmentsToRemove;
	}

	public void setAttachmentsToRemove(List<Integer> attachmentsToRemove) {
		this.attachmentsToRemove = attachmentsToRemove;
	}

}
