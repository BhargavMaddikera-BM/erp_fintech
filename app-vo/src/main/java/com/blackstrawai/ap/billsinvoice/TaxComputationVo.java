package com.blackstrawai.ap.billsinvoice;

import java.util.List;

import com.blackstrawai.common.BaseVo;

public class TaxComputationVo extends BaseVo{

	private List<InvoiceProductVo> products;
	
	private List<InvoiceTaxDistributionVo> groupedTax;

	public List<InvoiceProductVo> getProducts() {
		return products;
	}

	public void setProducts(List<InvoiceProductVo> products) {
		this.products = products;
	}

	public List<InvoiceTaxDistributionVo> getGroupedTax() {
		return groupedTax;
	}

	public void setGroupedTax(List<InvoiceTaxDistributionVo> groupedTax) {
		this.groupedTax = groupedTax;
	}
	
	
}
