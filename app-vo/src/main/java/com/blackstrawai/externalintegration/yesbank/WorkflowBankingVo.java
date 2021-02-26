package com.blackstrawai.externalintegration.yesbank;

import com.blackstrawai.externalintegration.yesbank.Request.paymenttransfer.PaymentTransferVo;
import com.blackstrawai.externalintegration.yesbank.Response.payments.CopyPaymentTransactionVo;

public class WorkflowBankingVo {
	
	private Integer id;
	private String amount;
	private PaymentTransferVo paymentTransferPaymentsVo;
    private String finalJsonRequest;
    private int organizationId;
    private String userId;
    private String roleName;
    private String uiJsonRequest;
    private String paymentType;
    private String fileIdentifier;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	
	public PaymentTransferVo getPaymentTransferPaymentsVo() {
		return paymentTransferPaymentsVo;
	}
	public void setPaymentTransferPaymentsVo(PaymentTransferVo paymentTransferPaymentsVo) {
		this.paymentTransferPaymentsVo = paymentTransferPaymentsVo;
	}
	public String getFinalJsonRequest() {
		return finalJsonRequest;
	}
	public void setFinalJsonRequest(String finalJsonRequest) {
		this.finalJsonRequest = finalJsonRequest;
	}
	public int getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(int organizationId) {
		this.organizationId = organizationId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public String getUiJsonRequest() {
		return uiJsonRequest;
	}
	public void setUiJsonRequest(String uiJsonRequest) {
		this.uiJsonRequest = uiJsonRequest;
	}
	public String getFileIdentifier() {
		return fileIdentifier;
	}
	public void setFileIdentifier(String fileIdentifier) {
		this.fileIdentifier = fileIdentifier;
	}
	public String getPaymentType() {
		return paymentType;
	}
	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}
	@Override
	public String toString() {
		return "WorkflowBankingVo [id=" + id + ", amount=" + amount + "]";
	}
	
	
}
