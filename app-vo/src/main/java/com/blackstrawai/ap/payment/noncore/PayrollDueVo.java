package com.blackstrawai.ap.payment.noncore;

import java.util.List;

public class PayrollDueVo {
	private List<PayrollDueItemVo> payrolls;
	private String totalAmount;
	private String overdueAmount;
	public List<PayrollDueItemVo> getPayrolls() {
		return payrolls;
	}
	public void setPayrolls(List<PayrollDueItemVo> payrolls) {
		this.payrolls = payrolls;
	}
	public String getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}
	public String getOverdueAmount() {
		return overdueAmount;
	}
	public void setOverdueAmount(String overdueAmount) {
		this.overdueAmount = overdueAmount;
	}
	
	
}
