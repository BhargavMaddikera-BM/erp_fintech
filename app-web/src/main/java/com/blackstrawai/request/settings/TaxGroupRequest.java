package com.blackstrawai.request.settings;

import java.sql.Timestamp;
import java.util.List;

import com.blackstrawai.common.BaseRequest;
import com.blackstrawai.settings.TaxRateMappingVo;

public class TaxGroupRequest extends BaseRequest{

	private int id;
	private String name;
	private List<TaxRateMappingVo> taxRates;
	private String combinedRate;
	private boolean isInter;
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
	public List<TaxRateMappingVo> getTaxRates() {
		return taxRates;
	}
	public void setTaxRates(List<TaxRateMappingVo> taxRates) {
		this.taxRates = taxRates;
	}
	public Timestamp getUpdateTs() {
		return updateTs;
	}
	public void setUpdateTs(Timestamp updateTs) {
		this.updateTs = updateTs;
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
	
	public String getCombinedRate() {
		return combinedRate;
	}
	public void setCombinedRate(String combinedRate) {
		this.combinedRate = combinedRate;
	}
	public boolean isInter() {
		return isInter;
	}
	public void setInter(boolean isInter) {
		this.isInter = isInter;
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
	
	
	
}
