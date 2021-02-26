package com.blackstrawai.settings;

import java.sql.Timestamp;
import java.util.List;

import com.blackstrawai.common.BaseVo;

public class TaxGroupVo extends BaseVo{

	private int id;
	private String name;
	private String taxesIncluded;
	private List<TaxRateMappingVo> taxRates;
	private String combinedRate;
	private boolean isInter;
	private boolean isBase;
	private String status;
	private int organizationId;
	private boolean isSuperAdmin;
	private Timestamp createTs;
	private Timestamp updateTs;
	private String roleName="Super Admin";
	
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public List<TaxRateMappingVo> getTaxRates() {
		return taxRates;
	}
	public void setTaxRates(List<TaxRateMappingVo> taxRates) {
		this.taxRates = taxRates;
	}
	public boolean isInter() {
		return isInter;
	}
	public void setInter(boolean isInter) {
		this.isInter = isInter;
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
	public String getTaxesIncluded() {
		return taxesIncluded;
	}
	public void setTaxesIncluded(String taxesIncluded) {
		this.taxesIncluded = taxesIncluded;
	}
	public String getCombinedRate() {
		return combinedRate;
	}
	public void setCombinedRate(String combinedRate) {
		this.combinedRate = combinedRate;
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
		StringBuilder builder = new StringBuilder();
		builder.append("TaxGroupVo [id=");
		builder.append(id);
		builder.append(", name=");
		builder.append(name);
		builder.append(", taxesIncluded=");
		builder.append(taxesIncluded);
		builder.append(", combinedRate=");
		builder.append(combinedRate);
		builder.append(", isBase=");
		builder.append(isBase);
		builder.append(", status=");
		builder.append(status);
		builder.append(", organizationId=");
		builder.append(organizationId);
		builder.append(", isSuperAdmin=");
		builder.append(isSuperAdmin);
		builder.append(", createTs=");
		builder.append(createTs);
		builder.append(", updateTs=");
		builder.append(updateTs);
		builder.append("]");
		return builder.toString();
	}
	
	
}
