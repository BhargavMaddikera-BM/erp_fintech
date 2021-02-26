package com.blackstrawai.settings;

import com.blackstrawai.common.BaseVo;

public class ListBasicProductVo extends BaseVo {

	private Integer id;
	private String productId;
	private String name;
	private String unitPricePurchase;
	private String unitPriceSale;
	private Integer organizationId;
	private Boolean isSuperAdmin;
	private String status;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUnitPricePurchase() {
		return unitPricePurchase;
	}

	public void setUnitPricePurchase(String unitPricePurchase) {
		this.unitPricePurchase = unitPricePurchase;
	}

	public String getUnitPriceSale() {
		return unitPriceSale;
	}

	public void setUnitPriceSale(String unitPriceSale) {
		this.unitPriceSale = unitPriceSale;
	}

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

	public Boolean getIsSuperAdmin() {
		return isSuperAdmin;
	}

	public void setIsSuperAdmin(Boolean isSuperAdmin) {
		this.isSuperAdmin = isSuperAdmin;
	}

}
