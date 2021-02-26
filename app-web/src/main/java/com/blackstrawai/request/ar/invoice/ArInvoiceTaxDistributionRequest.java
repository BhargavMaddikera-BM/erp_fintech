package com.blackstrawai.request.ar.invoice;

public class ArInvoiceTaxDistributionRequest {

	private String taxName;
	
	private String taxRate;
	
	private Double taxAmount;
	
	public String getTaxName() {
		return taxName;
	}
	public void setTaxName(String taxName) {
		this.taxName = taxName;
	}
	public String getTaxRate() {
		return taxRate;
	}
	public void setTaxRate(String taxRate) {
		this.taxRate = taxRate;
	}
	public Double getTaxAmount() {
		return taxAmount;
	}
	public void setTaxAmount(Double taxAmount) {
		this.taxAmount = taxAmount;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("InvoiceTaxDistribution [taxName=");
		builder.append(taxName);
		builder.append(", taxRate=");
		builder.append(taxRate);
		builder.append(", taxAmount=");
		builder.append(taxAmount);
		builder.append("]");
		return builder.toString();
	}
	
}
