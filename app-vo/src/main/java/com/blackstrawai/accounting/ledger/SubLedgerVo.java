package com.blackstrawai.accounting.ledger;

public class SubLedgerVo {

	private String name;
	
	private String description;
	
	private boolean isBase;
	
	private String status;
	
	private Integer orgId;
	
	private Integer userId;
	
	private Boolean isSuperAdmin;
	
	private Integer level5Id;
	
	private String displayName;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Boolean getIsSuperAdmin() {
		return isSuperAdmin;
	}

	public void setIsSuperAdmin(Boolean isSuperAdmin) {
		this.isSuperAdmin = isSuperAdmin;
	}

	public Integer getLevel5Id() {
		return level5Id;
	}

	public void setLevel5Id(Integer level5Id) {
		this.level5Id = level5Id;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SubLedgerVo [name=");
		builder.append(name);
		builder.append(", description=");
		builder.append(description);
		builder.append(", isBase=");
		builder.append(isBase);
		builder.append(", status=");
		builder.append(status);
		builder.append(", orgId=");
		builder.append(orgId);
		builder.append(", userId=");
		builder.append(userId);
		builder.append(", isSuperAdmin=");
		builder.append(isSuperAdmin);
		builder.append(", level5Id=");
		builder.append(level5Id);
		builder.append(", displayName=");
		builder.append(displayName);
		builder.append("]");
		return builder.toString();
	}
	
	
	
}
