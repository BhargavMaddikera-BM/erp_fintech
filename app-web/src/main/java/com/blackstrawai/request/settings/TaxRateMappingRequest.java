package com.blackstrawai.request.settings;

import java.sql.Timestamp;

import com.blackstrawai.common.BaseRequest;

public class TaxRateMappingRequest extends BaseRequest{
	
	private int id;
	private String name;
	private int taxRateTypeId;
	private String rate;
	private boolean isBase;
	private String status;
	private int organizationId;
	private boolean isSuperAdmin;
	private Timestamp createTs;
	private Timestamp updateTs;
	private String roleName;
	
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
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
	public boolean isSuperAdmin() {
		return isSuperAdmin;
	}
	public void setSuperAdmin(boolean isSuperAdmin) {
		this.isSuperAdmin = isSuperAdmin;
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
		return "TaxRateMappingRequest [id=" + id + ", name=" + name + ", taxRateTypeId=" + taxRateTypeId + ", rate="
				+ rate + ", isBase=" + isBase + ", status=" + status + ", organizationId=" + organizationId
				+ ", isSuperAdmin=" + isSuperAdmin + ", createTs=" + createTs + ", updateTs=" + updateTs + "]";
	}
	
	
	
}
