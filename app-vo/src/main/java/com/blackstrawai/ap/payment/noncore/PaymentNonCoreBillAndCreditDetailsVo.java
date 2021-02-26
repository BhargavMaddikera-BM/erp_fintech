package com.blackstrawai.ap.payment.noncore;

import java.util.List;

import com.blackstrawai.ap.dropdowns.PaymentNonCoreBillDetailsDropDownVo;

public class PaymentNonCoreBillAndCreditDetailsVo {
	private List<PaymentNonCoreBillDetailsDropDownVo> billDetails;
	private List<CreditDetailsVo> creditDetails;
	private String advanceBalance;
	private String billBalance;
	
	public List<PaymentNonCoreBillDetailsDropDownVo> getBillDetails() {
		return billDetails;
	}
	public void setBillDetails(List<PaymentNonCoreBillDetailsDropDownVo> billDetails) {
		this.billDetails = billDetails;
	}
	public List<CreditDetailsVo> getCreditDetails() {
		return creditDetails;
	}
	public void setCreditDetails(List<CreditDetailsVo> creditDetails) {
		this.creditDetails = creditDetails;
	}
	public String getAdvanceBalance() {
		return advanceBalance;
	}
	public void setAdvanceBalance(String advanceBalance) {
		this.advanceBalance = advanceBalance;
	}
	public String getBillBalance() {
		return billBalance;
	}
	public void setBillBalance(String billBalance) {
		this.billBalance = billBalance;
	}
	
}
