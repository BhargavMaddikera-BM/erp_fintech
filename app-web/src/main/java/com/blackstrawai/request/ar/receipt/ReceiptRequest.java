package com.blackstrawai.request.ar.receipt;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.blackstrawai.ap.creditnote.VendorCreditNoteDetailsVo;
import com.blackstrawai.ar.dropdowns.BasicInvoiceDetailsVo;
import com.blackstrawai.ar.receipt.ReceiptBulkDetailsVo;
import com.blackstrawai.ar.receipt.ReceiptSettingsVo;
import com.blackstrawai.ar.receipt.VendorRefundReceiptDetailsVo;
import com.blackstrawai.attachments.UploadFileVo;
import com.blackstrawai.common.BaseRequest;
import com.blackstrawai.journals.GeneralLedgerVo;

public class ReceiptRequest extends BaseRequest{
	private Integer id;    
	private String bankId;    
	private String bankType;
	private String receiptType;    
	private String receiptNo;
	private String receiptNoPrefix;
	private String receiptNoSuffix;
	private String receiptDate;    
	private Integer customerId;
	private Integer vendorId;
	private Integer contactId;
	private String contactType;
	private String contactName;
	private String exchangeRate;
	private int currencyId;
	private String amount;
	private String total;    
	private String notes;    
	private int organizationId; 
	private String adjustedAmount;    
	private String differenceAmount;
	private boolean recordBillDetails;
	private List<Integer> itemsToRemove=new ArrayList<Integer>();
	private List<ReceiptBulkDetailsVo> receiptBulkDetails=new ArrayList<ReceiptBulkDetailsVo>();
	private List<BasicInvoiceDetailsVo> invoices=new ArrayList<BasicInvoiceDetailsVo>();
	private List<ReceiptSettingsVo> customTableList;
	private List<UploadFileVo> attachments;
	private List<Integer> attachmentsToRemove;
	private String status;    
	private String roleName;    
	private Timestamp createTs;    
	private Timestamp updateTs;    
	private String userId;    
	private boolean isBulk;
	private boolean addNotes;
	private String vendorName;
	private String customerName;
	private String invoiceNo;
	private List<VendorRefundReceiptDetailsVo> vendorReceiptDetails;
	private List<VendorCreditNoteDetailsVo> vendorCreditNoteDetailsVos;
	private boolean recordVendorCreditDetails ;
	private List<Integer> vendorReceiptItemsToRemove;
	private GeneralLedgerVo generalLedgerData;
	
	
	
	public GeneralLedgerVo getGeneralLedgerData() {
		return generalLedgerData;
	}
	public void setGeneralLedgerData(GeneralLedgerVo generalLedgerData) {
		this.generalLedgerData = generalLedgerData;
	}
	public String getContactName() {
		return contactName;
	}
	public void setContactName(String contactName) {
		this.contactName = contactName;
	}
	public List<VendorRefundReceiptDetailsVo> getVendorReceiptDetails() {
		return vendorReceiptDetails;
	}
	public void setVendorReceiptDetails(List<VendorRefundReceiptDetailsVo> vendorReceiptDetails) {
		this.vendorReceiptDetails = vendorReceiptDetails;
	}
	public List<Integer> getVendorReceiptItemsToRemove() {
		return vendorReceiptItemsToRemove;
	}
	public void setVendorReceiptItemsToRemove(List<Integer> vendorReceiptItemsToRemove) {
		this.vendorReceiptItemsToRemove = vendorReceiptItemsToRemove;
	}
	
	public String getAdjustedAmount() {
		return adjustedAmount;
	}
	public void setAdjustedAmount(String adjustedAmount) {
		this.adjustedAmount = adjustedAmount;
	}
	public String getDifferenceAmount() {
		return differenceAmount;
	}
	public void setDifferenceAmount(String differenceAmount) {
		this.differenceAmount = differenceAmount;
	}
	public boolean getRecordBillDetails() {
		return recordBillDetails;
	}
	public void setRecordBillDetails(boolean recordBillDetails) {
		this.recordBillDetails = recordBillDetails;
	}
	public boolean getAddNotes() {
		return addNotes;
	}
	public void setAddNotes(boolean addNotes) {
		this.addNotes = addNotes;
	}
	public String getExchangeRate() {
		return exchangeRate;
	}
	public void setExchangeRate(String exchangeRate) {
		this.exchangeRate = exchangeRate;
	}
	public List<Integer> getItemsToRemove() {
		return itemsToRemove;
	}
	public void setItemsToRemove(List<Integer> itemsToRemove) {
		this.itemsToRemove = itemsToRemove;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getInvoiceNo() {
		return invoiceNo;
	}
	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	public String getTotal() {
		return total;
	}
	public void setTotal(String total) {
		this.total = total;
	}
	public Integer getContactId() {
		return contactId;
	}
	public void setContactId(Integer contactId) {
		this.contactId = contactId;
	}
	public String getContactType() {
		return contactType;
	}
	public void setContactType(String contactType) {
		this.contactType = contactType;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getBankId() {
		return bankId;
	}
	public void setBankId(String bankId) {
		this.bankId = bankId;
	}
	public String getBankType() {
		return bankType;
	}
	public void setBankType(String bankType) {
		this.bankType = bankType;
	}
	public String getReceiptType() {
		return receiptType;
	}
	public void setReceiptType(String receiptType) {
		this.receiptType = receiptType;
	}
	public String getReceiptNo() {
		return receiptNo;
	}
	public void setReceiptNo(String receiptNo) {
		this.receiptNo = receiptNo;
	}
	public String getReceiptNoPrefix() {
		return receiptNoPrefix;
	}
	public void setReceiptNoPrefix(String receiptNoPrefix) {
		this.receiptNoPrefix = receiptNoPrefix;
	}
	public String getReceiptNoSuffix() {
		return receiptNoSuffix;
	}
	public void setReceiptNoSuffix(String receiptNoSuffix) {
		this.receiptNoSuffix = receiptNoSuffix;
	}
	public String getReceiptDate() {
		return receiptDate;
	}
	public void setReceiptDate(String receiptDate) {
		this.receiptDate = receiptDate;
	}
	public Integer getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}
	public int getCurrencyId() {
		return currencyId;
	}
	public void setCurrencyId(int currencyId) {
		this.currencyId = currencyId;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public int getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(int organizationId) {
		this.organizationId = organizationId;
	}

	public List<ReceiptBulkDetailsVo> getReceiptBulkDetails() {
		return receiptBulkDetails;
	}
	public void setReceiptBulkDetails(List<ReceiptBulkDetailsVo> receiptBulkDetails) {
		this.receiptBulkDetails = receiptBulkDetails;
	}
	public List<BasicInvoiceDetailsVo> getInvoices() {
		return invoices;
	}
	public void setInvoices(List<BasicInvoiceDetailsVo> invoices) {
		this.invoices = invoices;
	}
	
	public List<ReceiptSettingsVo> getCustomTableList() {
		return customTableList;
	}
	public void setCustomTableList(List<ReceiptSettingsVo> customTableList) {
		this.customTableList = customTableList;
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
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
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public Integer getVendorId() {
		return vendorId;
	}
	public void setVendorId(Integer vendorId) {
		this.vendorId = vendorId;
	}
	public boolean getIsBulk() {
		return isBulk;
	}
	public void setIsBulk(boolean isBulk) {
		this.isBulk = isBulk;
	}
	
	public String getVendorName() {
		return vendorName;
	}
	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}
	public List<VendorCreditNoteDetailsVo> getVendorCreditNoteDetailsVos() {
		return vendorCreditNoteDetailsVos;
	}
	public void setVendorCreditNoteDetailsVos(List<VendorCreditNoteDetailsVo> vendorCreditNoteDetailsVos) {
		this.vendorCreditNoteDetailsVos = vendorCreditNoteDetailsVos;
	}
	public boolean isRecordVendorCreditDetails() {
		return recordVendorCreditDetails;
	}
	public void setRecordVendorCreditDetails(boolean recordVendorCreditDetails) {
		this.recordVendorCreditDetails = recordVendorCreditDetails;
	}
	@Override
	public String toString() {
		return "ReceiptRequest [id=" + id + ", bankId=" + bankId + ", bankType=" + bankType + ", receiptType="
				+ receiptType + ", receiptNo=" + receiptNo + ", receiptNoPrefix=" + receiptNoPrefix
				+ ", receiptNoSuffix=" + receiptNoSuffix + ", receiptDate=" + receiptDate + ", customerId=" + customerId
				+ ", vendorId=" + vendorId + ", contactId=" + contactId + ", contactType=" + contactType
				+ ", contactName=" + contactName + ", exchangeRate=" + exchangeRate + ", currencyId=" + currencyId
				+ ", amount=" + amount + ", total=" + total + ", notes=" + notes + ", organizationId=" + organizationId
				+ ", adjustedAmount=" + adjustedAmount + ", differenceAmount=" + differenceAmount
				+ ", recordBillDetails=" + recordBillDetails + ", itemsToRemove=" + itemsToRemove
				+ ", receiptBulkDetails=" + receiptBulkDetails + ", invoices=" + invoices + ", customTableList="
				+ customTableList + ", attachments=" + attachments + ", attachmentsToRemove=" + attachmentsToRemove
				+ ", status=" + status + ", roleName=" + roleName + ", createTs=" + createTs + ", updateTs=" + updateTs
				+ ", userId=" + userId + ", isBulk=" + isBulk + ", addNotes=" + addNotes + ", vendorName=" + vendorName
				+ ", customerName=" + customerName + ", invoiceNo=" + invoiceNo + ", vendorReceiptDetails="
				+ vendorReceiptDetails + ", vendorCreditNoteDetailsVos=" + vendorCreditNoteDetailsVos
				+ ", recordVendorCreditDetails=" + recordVendorCreditDetails + ", vendorReceiptItemsToRemove="
				+ vendorReceiptItemsToRemove + ", generalLedgerData=" + generalLedgerData + "]";
	}
	
		
}