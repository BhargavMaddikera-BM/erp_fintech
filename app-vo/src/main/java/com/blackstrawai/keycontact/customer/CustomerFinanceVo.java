package com.blackstrawai.keycontact.customer;

import java.sql.Timestamp;

public class CustomerFinanceVo {

	private Integer id;
	private Integer currencyId;
	private Integer paymentTermsid;
	private Integer tdsId;
	private String openingBalance;
	private Timestamp createTs;
	private Timestamp updateTs;
	private Integer customerId;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getCurrencyId() {
		return currencyId;
	}
	public void setCurrencyId(Integer currencyId) {
		this.currencyId = currencyId;
	}
	public Integer getPaymentTermsid() {
		return paymentTermsid;
	}
	public void setPaymentTermsid(Integer paymentTermsid) {
		this.paymentTermsid = paymentTermsid;
	}
	public Integer getTdsId() {
		return tdsId;
	}
	public void setTdsId(Integer tdsId) {
		this.tdsId = tdsId;
	}
	public String getOpeningBalance() {
		return openingBalance;
	}
	public void setOpeningBalance(String openingBalance) {
		this.openingBalance = openingBalance;
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
	public Integer getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}
		
}
