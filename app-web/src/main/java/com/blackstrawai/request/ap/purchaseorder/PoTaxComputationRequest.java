package com.blackstrawai.request.ap.purchaseorder;

import java.util.List;

public class PoTaxComputationRequest {
	private Boolean isExclusive;
	private List<PoProductRequest> products;
	private Integer organizationId;
	public List<PoProductRequest> getProducts() {
		return products;
	}
	public void setProducts(List<PoProductRequest> products) {
		this.products = products;
	}
	public Integer getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

	public Boolean getIsExclusive() {
		return isExclusive;
	}

	public void setIsExclusive(Boolean isExclusive) {
		this.isExclusive = isExclusive;
	}

}
