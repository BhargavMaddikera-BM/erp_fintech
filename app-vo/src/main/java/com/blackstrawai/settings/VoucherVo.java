package com.blackstrawai.settings;

import java.sql.Timestamp;

import com.blackstrawai.common.BaseVo;

public class VoucherVo extends BaseVo{
	
	private Integer id;
	private String voucherName;
	private String type;
	private String description;
	private String prefix;
	private String suffix;
	private String minimumDigits;
	private String minimumNumberRange;
	private String maximumNumberRange;
	private String alertValue;
	private Integer organizationId;
	private Boolean isSuperAdmin;
	private String status;
	private Timestamp createTs;
	private Timestamp updateTs;
	private String roleName="Super Admin";
	
	
	
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public String getMinimumNumberRange() {
		return minimumNumberRange;
	}
	public void setMinimumNumberRange(String minimumNumberRange) {
		this.minimumNumberRange = minimumNumberRange;
	}
	public String getMaximumNumberRange() {
		return maximumNumberRange;
	}
	public void setMaximumNumberRange(String maximumNumberRange) {
		this.maximumNumberRange = maximumNumberRange;
	}
	public String getAlertValue() {
		return alertValue;
	}
	public void setAlertValue(String alertValue) {
		this.alertValue = alertValue;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getVoucherName() {
		return voucherName;
	}
	public void setVoucherName(String voucherName) {
		this.voucherName = voucherName;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getPrefix() {
		return prefix;
	}
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	public String getSuffix() {
		return suffix;
	}
	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}
	public String getMinimumDigits() {
		return minimumDigits;
	}
	public void setMinimumDigits(String minimumDigits) {
		this.minimumDigits = minimumDigits;
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
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
	
	
}
