package com.blackstrawai.ap.dropdowns;

import java.sql.Timestamp;

public class PaymentNonCoreGstDetailsDropDownVo {

	private int id;
	private String name;
	private int taxRateTypeId;
	private String taxRateTypeName;
	private String roleName="Super Admin";
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public String getTaxRateTypeName() {
		return taxRateTypeName;
	}
	public void setTaxRateTypeName(String taxRateTypeName) {
		this.taxRateTypeName = taxRateTypeName;
	}
	private String rate;
	private boolean isBase;
	private String status;
	private int organizationId;
	private boolean isSuperAdmin;
	private boolean isInter;
	private int value;
	
	public boolean isInter() {
		return isInter;
	}
	public void setInter(boolean isInter) {
		this.isInter = isInter;
	}
	private Timestamp createTs;
	private Timestamp updateTs;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getTaxRateTypeId() {
		return taxRateTypeId;
	}
	public void setTaxRateTypeId(int taxRateTypeId) {
		this.taxRateTypeId = taxRateTypeId;
	}
	public String getRate() {
		return rate;
	}
	public void setRate(String rate) {
		this.rate = rate;
	}
	public boolean isBase() {
		return isBase;
	}
	public void setBase(boolean isBase) {
		this.isBase = isBase;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(int organizationId) {
		this.organizationId = organizationId;
	}
	
	public void setSuperAdmin(boolean isSuperAdmin) {
		this.isSuperAdmin = isSuperAdmin;
	}
	public boolean isSuperAdmin() {
		return isSuperAdmin;
	}
	public Timestamp getCreateTs() {
		return createTs;
	}
	public void setCreateTs(Timestamp createTs) {
		this.createTs = createTs;
	}
	public Timestamp getUpdateTs() {
		return updateTs;
	}
	public void setUpdateTs(Timestamp updateTs) {
		this.updateTs = updateTs;
	}
	@Override
	public String toString() {
		return "TaxRateMappingVo [id=" + id + ", name=" + name + ", taxRateTypeId=" + taxRateTypeId + ", rate=" + rate
				+ ", isBase=" + isBase + ", status=" + status + ", organizationId=" + organizationId + ", isSuperAdmin="
				+ isSuperAdmin + ", createTs=" + createTs + ", updateTs=" + updateTs + "]";
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}

	

}
