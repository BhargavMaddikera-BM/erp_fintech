package com.blackstrawai.ar.refund;

import java.sql.Timestamp;
import java.util.List;

import com.blackstrawai.attachments.UploadFileVo;
import com.blackstrawai.common.TokenVo;


public class RefundVo extends TokenVo{
	private int id;    
	private String dateOfRefund;    
	private int customerId;    
	private String customerName;    
	private int invoiceReferenceId;
	private int creditNoteId;
	private String invoiceReference;
	private int paymentModeId;
	private String paymentMode;
	private int paymentAccountId;
	private String paymentAccountName;
	private int receiptId;
	private int refundTypeId;
	private String refundReferenceNo;   
	private String refundReference;
	private int locationId;    
	private List<UploadFileVo> attachments;
	private List<Integer> attachmentsToRemove;
	private Boolean isRegistered;
	private boolean isLocal;
	private String gstNumber;
	private String exchangeRate;
	private int currencyId;
	private String currencySymbol;
	private String amount;    
	private String status;    
	private int ledgerId;    
	private String ledgerName;
	private String roleName;
	private int organizationId;    
	private Timestamp createTs;    
	private Timestamp updateTs;    
	private Boolean isSuperAdmin; 	

	public String getExchangeRate() {
		return exchangeRate;
	}
	public void setExchangeRate(String exchangeRate) {
		this.exchangeRate = exchangeRate;
	}
	public boolean isLocal() {
		return isLocal;
	}
	public void setLocal(boolean isLocal) {
		this.isLocal = isLocal;
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
	public int getCreditNoteId() {
		return creditNoteId;
	}
	public void setCreditNoteId(int creditNoteId) {
		this.creditNoteId = creditNoteId;
	}
	
	public int getReceiptId() {
		return receiptId;
	}
	public void setReceiptId(int receiptId) {
		this.receiptId = receiptId;
	}
	public int getRefundTypeId() {
		return refundTypeId;
	}
	public void setRefundTypeId(int refundTypeId) {
		this.refundTypeId = refundTypeId;
	}
	public Boolean getIsRegistered() {
		return isRegistered;
	}
	public void setIsRegistered(Boolean isRegistered) {
		this.isRegistered = isRegistered;
	}
	public String getGstNumber() {
		return gstNumber;
	}
	public void setGstNumber(String gstNumber) {
		this.gstNumber = gstNumber;
	}
	public int getLocationId() {
		return locationId;
	}
	public void setLocationId(int locationId) {
		this.locationId = locationId;
	}
	public int getCurrencyId() {
		return currencyId;
	}
	public void setCurrencyId(int currencyId) {
		this.currencyId = currencyId;
	}
	public String getCurrencySymbol() {
		return currencySymbol;
	}
	public void setCurrencySymbol(String currencySymbol) {
		this.currencySymbol = currencySymbol;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getInvoiceReference() {
		return invoiceReference;
	}
	public void setInvoiceReference(String invoiceReference) {
		this.invoiceReference = invoiceReference;
	}
		
	public String getPaymentMode() {
		return paymentMode;
	}
	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}
	public int getPaymentAccountId() {
		return paymentAccountId;
	}
	public void setPaymentAccountId(int paymentAccountId) {
		this.paymentAccountId = paymentAccountId;
	}
	public String getPaymentAccountName() {
		return paymentAccountName;
	}
	public void setPaymentAccountName(String paymentAccountName) {
		this.paymentAccountName = paymentAccountName;
	}
	public void setId(int id){
		this.id=id;
	}
	public int getId(){
		return  id; 
	}
	public void setDateOfRefund(String dateOfRefund){
		this.dateOfRefund=dateOfRefund;
	}
	public String getDateOfRefund(){
		return  dateOfRefund; 
	}
	public void setCustomerId(int customerId){
		this.customerId=customerId;
	}
	public int getCustomerId(){
		return  customerId; 
	}
	public void setInvoiceReferenceId(int invoiceReferenceId){
		this.invoiceReferenceId=invoiceReferenceId;
	}
	public int getInvoiceReferenceId(){
		return  invoiceReferenceId; 
	}
	public void setPaymentModeId(int paymentModeId){
		this.paymentModeId=paymentModeId;
	}
	public int getPaymentModeId(){
		return  paymentModeId; 
	}
	
	public String getRefundReferenceNo() {
		return refundReferenceNo;
	}
	public void setRefundReferenceNo(String refundReferenceNo) {
		this.refundReferenceNo = refundReferenceNo;
	}
	public String getRefundReference() {
		return refundReference;
	}
	public void setRefundReference(String refundReference) {
		this.refundReference = refundReference;
	}
	public void setAmount(String amount){
		this.amount=amount;
	}
	public String getAmount(){
		return  amount; 
	}
	public void setStatus(String status){
		this.status=status;
	}
	public String getStatus(){
		return  status; 
	}
	public void setLedgerId(int ledgerId){
		this.ledgerId=ledgerId;
	}
	public int getLedgerId(){
		return  ledgerId; 
	}
	public void setLedgerName(String ledgerName){
		this.ledgerName=ledgerName;
	}
	public String getLedgerName(){
		return  ledgerName; 
	}
	
	public void setOrganizationId(int organizationId){
		this.organizationId=organizationId;
	}
	public int getOrganizationId(){
		return  organizationId; 
	}
	public void setCreateTs(Timestamp createTs){
		this.createTs=createTs;
	}
	public Timestamp getCreateTs(){
		return  createTs; 
	}
	public void setUpdateTs(Timestamp updateTs){
		this.updateTs=updateTs;
	}
	public Timestamp getUpdateTs(){
		return  updateTs; 
	}
	public void setIsSuperAdmin(Boolean isSuperAdmin){
		this.isSuperAdmin=isSuperAdmin;
	}
	public Boolean getIsSuperAdmin(){
		return  isSuperAdmin; 
	}
	@Override
	public String toString() {
		return "RefundVo [id=" + id + ", dateOfRefund=" + dateOfRefund + ", customerId=" + customerId
				+ ", customerName=" + customerName + ", invoiceReferenceId=" + invoiceReferenceId
				+ ", invoiceReference=" + invoiceReference + ", paymentModeId=" + paymentModeId + ", paymentMode="
				+ paymentMode + ", paymentAccountId=" + paymentAccountId + ", paymentAccountName=" + paymentAccountName
				+ ", refundReferenceNo=" + refundReferenceNo + ", refundReference=" + refundReference + ", amount="
				+ amount + ", status=" + status + ", ledgerId=" + ledgerId + ", ledgerName=" + ledgerName
				+ ", organizationId=" + organizationId + ", createTs=" + createTs + ", updateTs=" + updateTs
				+ ", isSuperAdmin=" + isSuperAdmin + ", getCustomerName()=" + getCustomerName()
				+ ", getInvoiceReference()=" + getInvoiceReference() + ", getPaymentMode()=" + getPaymentMode()
				+ ", getPaymentAccountId()=" + getPaymentAccountId() + ", getPaymentAccountName()="
				+ getPaymentAccountName() + ", getId()=" + getId() + ", getDateOfRefund()=" + getDateOfRefund()
				+ ", getCustomerId()=" + getCustomerId() + ", getInvoiceReferenceId()=" + getInvoiceReferenceId()
				+ ", getPaymentModeId()=" + getPaymentModeId() + ", getRefundReferenceNo()=" + getRefundReferenceNo()
				+ ", getRefundReference()=" + getRefundReference() + ", getAmount()=" + getAmount() + ", getStatus()="
				+ getStatus() + ", getLedgerId()=" + getLedgerId() + ", getLedgerName()=" + getLedgerName()
				+ ", getOrganizationId()=" + getOrganizationId() + ", getCreateTs()=" + getCreateTs()
				+ ", getUpdateTs()=" + getUpdateTs() + ", getIsSuperAdmin()=" + getIsSuperAdmin() + ", getKeyToken()="
				+ getKeyToken() + ", getValueToken()=" + getValueToken() + ", getUserId()=" + getUserId()
				+ ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()=" + super.toString()
				+ "]";
	}
	
	
}