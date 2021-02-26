package com.blackstrawai.request.ar.refund;

import java.sql.Timestamp;
import java.util.List;

import com.blackstrawai.attachments.UploadFileVo;
import com.blackstrawai.common.BaseRequest;

public class RefundRequest extends BaseRequest{
	private int id;    
	private String dateOfRefund;    
	private int customerId;    
	private int invoiceReferenceId;    
	private int paymentModeId;    
	private int paymentAccountId;	
	private int creditNoteId;
	private String refundReference;
	private int receiptId;
	private int refundTypeId;
	private String refundReferenceNo;
	private List<UploadFileVo> attachments;
	private List<Integer> attachmentsToRemove;
	private String amount;    
	private String status;    
	private int ledgerId;    
	private String ledgerName;
	private String exchangeRate;
	private String roleName;
	private int locationId;    
	private Boolean isRegistered;
	private String gstNumber;
	private int currencyId;
	private String currencySymbol;
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
	public String getRefundReferenceNo() {
		return refundReferenceNo;
	}
	public void setRefundReferenceNo(String refundReferenceNo) {
		this.refundReferenceNo = refundReferenceNo;
	}
	public int getPaymentAccountId() {
		return paymentAccountId;
	}
	public void setPaymentAccountId(int paymentAccountId) {
		this.paymentAccountId = paymentAccountId;
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
	public void setRefundReference(String refundReference){
		this.refundReference=refundReference;
	}
	public String getRefundReference(){
		return  refundReference; 
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
}