package com.blackstrawai.settings;

import java.sql.Timestamp;
import java.util.List;

import com.blackstrawai.common.TokenVo;

public class TdsVo extends TokenVo{
	
	public boolean isBase() {
		return isBase;
	}
	public void setBase(boolean isBase) {
		this.isBase = isBase;
	}
	private Integer id;
	private String tdsName;
	private String description;
	private String applicableFor;
    private String tdsRatePercentage;
    private String tdsRateIdentifier;
	private Integer organizationId;
	private String status;
	private Timestamp createTs;
	private String roleName="Super Admin";
	private boolean isBase=false;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getTdsName() {
		return tdsName;
	}
	public void setTdsName(String tdsName) {
		this.tdsName = tdsName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getApplicableFor() {
		return applicableFor;
	}
	public void setApplicableFor(String applicableFor) {
		this.applicableFor = applicableFor;
	}
	public String getTdsRatePercentage() {
		return tdsRatePercentage;
	}
	public void setTdsRatePercentage(String tdsRatePercentage) {
		this.tdsRatePercentage = tdsRatePercentage;
	}
	public String getTdsRateIdentifier() {
		return tdsRateIdentifier;
	}
	public void setTdsRateIdentifier(String tdsRateIdentifier) {
		this.tdsRateIdentifier = tdsRateIdentifier;
	}
	public Integer getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
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
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	
 


}
