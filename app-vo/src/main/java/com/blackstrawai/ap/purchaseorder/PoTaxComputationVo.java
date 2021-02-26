package com.blackstrawai.ap.purchaseorder;

import java.util.List;

public class PoTaxComputationVo {
	
private List<PoProductVo> products;
	
	private List<PoTaxDistributionVo> groupedTax;

	public List<PoProductVo> getProducts() {
		return products;
	}

	public void setProducts(List<PoProductVo> products) {
		this.products = products;
	}

	public List<PoTaxDistributionVo> getGroupedTax() {
		return groupedTax;
	}

	public void setGroupedTax(List<PoTaxDistributionVo> groupedTax) {
		this.groupedTax = groupedTax;
	}
}
