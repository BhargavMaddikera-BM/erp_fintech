package com.blackstrawai.request.ap.billsinvoice;

public class InvoiceTaxDistributionRequest {
	
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
		
		
		
		
}
