package com.blackstrawai.ap.payment.noncore;

import java.util.List;

public class BillsPayableVo {
	private List<BillsPayableItemVo> bills;
	private String totalAmount;
	private String overdueAmount;
	public List<BillsPayableItemVo> getBills() {
		return bills;
	}
	public void setBills(List<BillsPayableItemVo> bills) {
		this.bills = bills;
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
