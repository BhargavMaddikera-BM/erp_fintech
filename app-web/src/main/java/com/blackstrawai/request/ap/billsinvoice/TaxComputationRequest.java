package com.blackstrawai.request.ap.billsinvoice;

import java.util.List;

import com.blackstrawai.common.BaseRequest;

public class TaxComputationRequest extends BaseRequest{
	private Boolean isExclusive;
	private List<InvoiceProductRequest> products;
	private Integer organizationId;
	public List<InvoiceProductRequest> getProducts() {
		return products;
	}
	public void setProducts(List<InvoiceProductRequest> products) {
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
