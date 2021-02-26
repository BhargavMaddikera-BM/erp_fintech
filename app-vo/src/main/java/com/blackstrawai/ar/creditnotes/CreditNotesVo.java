package com.blackstrawai.ar.creditnotes;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.blackstrawai.ar.invoice.ArInvoiceProductVo;
import com.blackstrawai.ar.invoice.ArInvoiceTaxDistributionVo;
import com.blackstrawai.attachments.UploadFileVo;
import com.blackstrawai.common.TokenVo;
import com.blackstrawai.journals.GeneralLedgerVo;


public class CreditNotesVo extends TokenVo{
	private int id;    
	private int customerId;
	private String customerName;  
	private String originalInvoiceNumber;  
	private String originalInvoiceId;  
	private int creditNoteTypeId;
	private int creditNoteType;
	private int currencyId;    
	private String creditNoteNumber;
	private String creditNoteNumberPrefix;
	private String creditNoteNumberSuffix;
	private String creditNoteDate;
	private String exchangeRate;
	private int reasonId;
	private String reason;    
	private String tnc;    
	private String note;    
	private String subTotal;    
	private int tdsId;    
	private String tdsValue;    
	private int discLedgerId;    
	private String discLedgerName;    
	private String discountValue;    
	private String discountAccountLevel;    
	private String adjValue;    
	private int adjLedgerId;  
	private String adjLedgerName;    
	private List<UploadFileVo> attachments;
	private List<Integer> attachmentsToRemove;
	private List<ArInvoiceProductVo> products;
	private List<ArInvoiceTaxDistributionVo> groupedTax;
	private List<Integer> itemsToRemove=new ArrayList<Integer>();
	private String adjAccountLevel;    
	private String total;    
	private String balanceDue;
	private String amount;
	private boolean isEditable;
	private int refundTypeId;
	private int organizationId;    
	private String roleName;    
	private boolean isSuperAdmin;    
	private String status;    
	private Timestamp createTs;    
	private Timestamp updateTs;    
	private GeneralLedgerVo generalLedgerData ;
	private String pendingApprovalStatus;
	


	public String getPendingApprovalStatus() {
		return pendingApprovalStatus;
	}
	public void setPendingApprovalStatus(String pendingApprovalStatus) {
		this.pendingApprovalStatus = pendingApprovalStatus;
	}
	public GeneralLedgerVo getGeneralLedgerData() {
		return generalLedgerData;
	}
	public void setGeneralLedgerData(GeneralLedgerVo generalLedgerData) {
		this.generalLedgerData = generalLedgerData;
	}
	public int getCurrencyId() {
		return currencyId;
	}
	public void setCurrencyId(int currencyId) {
		this.currencyId = currencyId;
	}
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
	public int getRefundTypeId() {
		return refundTypeId;
	}
	public void setRefundTypeId(int refundTypeId) {
		this.refundTypeId = refundTypeId;
	}
	public boolean isEditable() {
		return isEditable;
	}
	public void setEditable(boolean isEditable) {
		this.isEditable = isEditable;
	}
	public int getReasonId() {
		return reasonId;
	}
	public void setReasonId(int reasonId) {
		this.reasonId = reasonId;
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
	public String getBalanceDue() {
		return balanceDue;
	}
	public void setBalanceDue(String balanceDue) {
		this.balanceDue = balanceDue;
	}
	public int getCreditNoteType() {
		return creditNoteType;
	}
	public void setCreditNoteType(int creditNoteType) {
		this.creditNoteType = creditNoteType;
	}
	public List<ArInvoiceTaxDistributionVo> getGroupedTax() {
		return groupedTax;
	}
	public void setGroupedTax(List<ArInvoiceTaxDistributionVo> groupedTax) {
		this.groupedTax = groupedTax;
	}
	public List<ArInvoiceProductVo> getProducts() {
		return products;
	}
	public void setProducts(List<ArInvoiceProductVo> products) {
		this.products = products;
	}
	public String getCreditNoteNumberPrefix() {
		return creditNoteNumberPrefix;
	}
	public void setCreditNoteNumberPrefix(String creditNoteNumberPrefix) {
		this.creditNoteNumberPrefix = creditNoteNumberPrefix;
	}
	public String getCreditNoteNumberSuffix() {
		return creditNoteNumberSuffix;
	}
	public void setCreditNoteNumberSuffix(String creditNoteNumberSuffix) {
		this.creditNoteNumberSuffix = creditNoteNumberSuffix;
	}
	public void setSuperAdmin(boolean isSuperAdmin) {
		this.isSuperAdmin = isSuperAdmin;
	}
	public void setId(int id){
		this.id=id;
	}
	public int getId(){
		return  id; 
	}
	public void setCustomerId(int customerId){
		this.customerId=customerId;
	}
	public int getCustomerId(){
		return  customerId; 
	}

	public String getOriginalInvoiceNumber() {
		return originalInvoiceNumber;
	}
	public void setOriginalInvoiceNumber(String originalInvoiceNumber) {
		this.originalInvoiceNumber = originalInvoiceNumber;
	}

	public String getOriginalInvoiceId() {
		return originalInvoiceId;
	}
	public void setOriginalInvoiceId(String originalInvoiceId) {
		this.originalInvoiceId = originalInvoiceId;
	}
	public void setCreditNoteTypeId(int creditNoteTypeId){
		this.creditNoteTypeId=creditNoteTypeId;
	}
	public int getCreditNoteTypeId(){
		return  creditNoteTypeId; 
	}
	public void setCreditNoteNumber(String creditNoteNumber){
		this.creditNoteNumber=creditNoteNumber;
	}
	public String getCreditNoteNumber(){
		return  creditNoteNumber; 
	}

	public String getCreditNoteDate() {
		return creditNoteDate;
	}
	public void setCreditNoteDate(String creditNoteDate) {
		this.creditNoteDate = creditNoteDate;
	}
	public void setReason(String reason){
		this.reason=reason;
	}
	public String getReason(){
		return  reason; 
	}
	public void setTnc(String tnc){
		this.tnc=tnc;
	}
	public String getTnc(){
		return  tnc; 
	}
	public void setNote(String note){
		this.note=note;
	}
	public String getNote(){
		return  note; 
	}
	public void setSubTotal(String subTotal){
		this.subTotal=subTotal;
	}
	public String getSubTotal(){
		return  subTotal; 
	}
	public void setTdsId(int tdsId){
		this.tdsId=tdsId;
	}
	public int getTdsId(){
		return  tdsId; 
	}
	public void setTdsValue(String tdsValue){
		this.tdsValue=tdsValue;
	}
	public String getTdsValue(){
		return  tdsValue; 
	}
	public void setDiscLedgerId(int discLedgerId){
		this.discLedgerId=discLedgerId;
	}
	public int getDiscLedgerId(){
		return  discLedgerId; 
	}
	public void setDiscLedgerName(String discLedgerName){
		this.discLedgerName=discLedgerName;
	}
	public String getDiscLedgerName(){
		return  discLedgerName; 
	}
	public void setDiscountValue(String discountValue){
		this.discountValue=discountValue;
	}
	public String getDiscountValue(){
		return  discountValue; 
	}
	public void setDiscountAccountLevel(String discountAccountLevel){
		this.discountAccountLevel=discountAccountLevel;
	}
	public String getDiscountAccountLevel(){
		return  discountAccountLevel; 
	}
	public void setAdjValue(String adjValue){
		this.adjValue=adjValue;
	}
	public String getAdjValue(){
		return  adjValue; 
	}
	public void setAdjLedgerId(int adjLedgerId){
		this.adjLedgerId=adjLedgerId;
	}
	public int getAdjLedgerId(){
		return  adjLedgerId; 
	}
	public void setAdjLedgerName(String adjLedgerName){
		this.adjLedgerName=adjLedgerName;
	}
	public String getAdjLedgerName(){
		return  adjLedgerName; 
	}
	public void setAdjAccountLevel(String adjAccountLevel){
		this.adjAccountLevel=adjAccountLevel;
	}
	public String getAdjAccountLevel(){
		return  adjAccountLevel; 
	}
	public void setTotal(String total){
		this.total=total;
	}
	public String getTotal(){
		return  total; 
	}
	public void setOrganizationId(int organizationId){
		this.organizationId=organizationId;
	}
	public int getOrganizationId(){
		return  organizationId; 
	}
	public void setRoleName(String roleName){
		this.roleName=roleName;
	}
	public String getRoleName(){
		return  roleName; 
	}
	public void setIsSuperAdmin(boolean isSuperAdmin){
		this.isSuperAdmin=isSuperAdmin;
	}
	public boolean getIsSuperAdmin(){
		return  isSuperAdmin; 
	}
	public void setStatus(String status){
		this.status=status;
	}
	public String getStatus(){
		return  status; 
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
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	@Override
	public String toString() {
		return "CreditNotesVo [id=" + id + ", customerId=" + customerId + ", customerName=" + customerName
				+ ", originalInvoiceNumber=" + originalInvoiceNumber + ", originalInvoiceId=" + originalInvoiceId
				+ ", creditNoteTypeId=" + creditNoteTypeId + ", creditNoteType=" + creditNoteType + ", currencyId="
				+ currencyId + ", creditNoteNumber=" + creditNoteNumber + ", creditNoteNumberPrefix="
				+ creditNoteNumberPrefix + ", creditNoteNumberSuffix=" + creditNoteNumberSuffix + ", creditNoteDate="
				+ creditNoteDate + ", exchangeRate=" + exchangeRate + ", reasonId=" + reasonId + ", reason=" + reason
				+ ", tnc=" + tnc + ", note=" + note + ", subTotal=" + subTotal + ", tdsId=" + tdsId + ", tdsValue="
				+ tdsValue + ", discLedgerId=" + discLedgerId + ", discLedgerName=" + discLedgerName
				+ ", discountValue=" + discountValue + ", discountAccountLevel=" + discountAccountLevel + ", adjValue="
				+ adjValue + ", adjLedgerId=" + adjLedgerId + ", adjLedgerName=" + adjLedgerName + ", attachments="
				+ attachments + ", attachmentsToRemove=" + attachmentsToRemove + ", products=" + products
				+ ", groupedTax=" + groupedTax + ", itemsToRemove=" + itemsToRemove + ", adjAccountLevel="
				+ adjAccountLevel + ", total=" + total + ", balanceDue=" + balanceDue + ", isEditable=" + isEditable
				+ ", refundTypeId=" + refundTypeId + ", organizationId=" + organizationId + ", roleName=" + roleName
				+ ", isSuperAdmin=" + isSuperAdmin + ", status=" + status + ", createTs=" + createTs + ", updateTs="
				+ updateTs + ", generalLedgerData=" + generalLedgerData + ", pendingApprovalStatus="
				+ pendingApprovalStatus + ", getPendingApprovalStatus()=" + getPendingApprovalStatus()
				+ ", getGeneralLedgerData()=" + getGeneralLedgerData() + ", getCurrencyId()=" + getCurrencyId()
				+ ", getExchangeRate()=" + getExchangeRate() + ", getAttachments()=" + getAttachments()
				+ ", getAttachmentsToRemove()=" + getAttachmentsToRemove() + ", getRefundTypeId()=" + getRefundTypeId()
				+ ", isEditable()=" + isEditable() + ", getReasonId()=" + getReasonId() + ", getItemsToRemove()="
				+ getItemsToRemove() + ", getCustomerName()=" + getCustomerName() + ", getBalanceDue()="
				+ getBalanceDue() + ", getCreditNoteType()=" + getCreditNoteType() + ", getGroupedTax()="
				+ getGroupedTax() + ", getProducts()=" + getProducts() + ", getCreditNoteNumberPrefix()="
				+ getCreditNoteNumberPrefix() + ", getCreditNoteNumberSuffix()=" + getCreditNoteNumberSuffix()
				+ ", getId()=" + getId() + ", getCustomerId()=" + getCustomerId() + ", getOriginalInvoiceNumber()="
				+ getOriginalInvoiceNumber() + ", getOriginalInvoiceId()=" + getOriginalInvoiceId()
				+ ", getCreditNoteTypeId()=" + getCreditNoteTypeId() + ", getCreditNoteNumber()="
				+ getCreditNoteNumber() + ", getCreditNoteDate()=" + getCreditNoteDate() + ", getReason()="
				+ getReason() + ", getTnc()=" + getTnc() + ", getNote()=" + getNote() + ", getSubTotal()="
				+ getSubTotal() + ", getTdsId()=" + getTdsId() + ", getTdsValue()=" + getTdsValue()
				+ ", getDiscLedgerId()=" + getDiscLedgerId() + ", getDiscLedgerName()=" + getDiscLedgerName()
				+ ", getDiscountValue()=" + getDiscountValue() + ", getDiscountAccountLevel()="
				+ getDiscountAccountLevel() + ", getAdjValue()=" + getAdjValue() + ", getAdjLedgerId()="
				+ getAdjLedgerId() + ", getAdjLedgerName()=" + getAdjLedgerName() + ", getAdjAccountLevel()="
				+ getAdjAccountLevel() + ", getTotal()=" + getTotal() + ", getOrganizationId()=" + getOrganizationId()
				+ ", getRoleName()=" + getRoleName() + ", getIsSuperAdmin()=" + getIsSuperAdmin() + ", getStatus()="
				+ getStatus() + ", getCreateTs()=" + getCreateTs() + ", getUpdateTs()=" + getUpdateTs()
				+ ", getKeyToken()=" + getKeyToken() + ", getValueToken()=" + getValueToken() + ", getUserId()="
				+ getUserId() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()="
				+ super.toString() + "]";
	}

	
	
}