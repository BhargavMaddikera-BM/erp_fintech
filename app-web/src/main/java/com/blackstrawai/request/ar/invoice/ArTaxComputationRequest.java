package com.blackstrawai.request.ar.invoice;

import java.util.List;

import com.blackstrawai.common.BaseRequest;

public class ArTaxComputationRequest extends BaseRequest{

	private Boolean isLocalType;
	
	private Boolean istaxExclusive;
	
	private List<ArInvoiceProductRequest> products;
	
	private Integer organizationId;
	
	public Boolean getIsLocalType() {
		return isLocalType;
	}
	public void setIsLocalType(Boolean isLocalType) {
		this.isLocalType = isLocalType;
	}
	public List<ArInvoiceProductRequest> getProducts() {
		return products;
	}
	public void setProducts(List<ArInvoiceProductRequest> products) {
		this.products = products;
	}
	public Integer getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}
	public Boolean getIstaxExclusive() {
		return istaxExclusive;
	}
	public void setIstaxExclusive(Boolean istaxExclusive) {
		this.istaxExclusive = istaxExclusive;
	}
	
	
}
