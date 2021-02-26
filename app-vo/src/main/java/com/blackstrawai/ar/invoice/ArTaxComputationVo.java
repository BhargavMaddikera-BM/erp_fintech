package com.blackstrawai.ar.invoice;

import java.util.List;

public class ArTaxComputationVo {

	private List<ArInvoiceProductVo> products;
	
	private List<ArInvoiceTaxDistributionVo> groupedTax;

	public List<ArInvoiceProductVo> getProducts() {
		return products;
	}

	public void setProducts(List<ArInvoiceProductVo> products) {
		this.products = products;
	}

	public List<ArInvoiceTaxDistributionVo> getGroupedTax() {
		return groupedTax;
	}

	public void setGroupedTax(List<ArInvoiceTaxDistributionVo> groupedTax) {
		this.groupedTax = groupedTax;
	}
	
	
	
}
