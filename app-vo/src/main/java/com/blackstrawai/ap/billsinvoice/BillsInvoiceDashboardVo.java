package com.blackstrawai.ap.billsinvoice;

public class BillsInvoiceDashboardVo implements Comparable<BillsInvoiceDashboardVo>{
	private int id;
	private String date;
	private String referenceNumber;
	private String paymentType;
	private String amount;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getReferenceNumber() {
		return referenceNumber;
	}
	public void setReferenceNumber(String referenceNumber) {
		this.referenceNumber = referenceNumber;
	}
	public String getPaymentType() {
		return paymentType;
	}
	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	@Override
	public int compareTo(BillsInvoiceDashboardVo invoice) {
		if(invoice!=null && invoice.getPaymentType().equalsIgnoreCase("OVERDUE")){
			return 1;
		}else {
			return -1;
		}
	}
	
}
