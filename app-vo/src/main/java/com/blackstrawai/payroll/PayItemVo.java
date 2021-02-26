package com.blackstrawai.payroll;

import com.blackstrawai.common.BaseVo;

public class PayItemVo extends BaseVo {
	
	private String name;
	private String description;
	private int organizationId;
	private int payType;
	private int paidTo;
	private String paidToType;
	private String paidToDisplayName;
	private Boolean isBase;
	private String roleName="Super Admin";
	private Boolean showColumn=false;
	public Boolean getShowColumn() {
		return showColumn;
	}
	public void setShowColumn(Boolean showColumn) {
		this.showColumn = showColumn;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public Boolean getIsBase() {
		return isBase;
	}
	public void setIsBase(Boolean isBase) {
		this.isBase = isBase;
	}
	public String getPaidToDisplayName() {
		return paidToDisplayName;
	}
	public void setPaidToDisplayName(String paidToDisplayName) {
		this.paidToDisplayName = paidToDisplayName;
	}
	public int getPaidTo() {
		return paidTo;
	}
	public void setPaidTo(int paidTo) {
		this.paidTo = paidTo;
	}
	public String getPaidToType() {
		return paidToType;
	}
	public void setPaidToType(String paidToType) {
		this.paidToType = paidToType;
	}
	private String payTypeName;
	public String getPayTypeName() {
		return payTypeName;
	}
	public void setPayTypeName(String payTypeName) {
		this.payTypeName = payTypeName;
	}
	private int ledgerId;
	private String ledgerName;
	private String status;
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
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
	public int getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(int organizationId) {
		this.organizationId = organizationId;
	}
	public int getPayType() {
		return payType;
	}
	public void setPayType(int payType) {
		this.payType = payType;
	}
	public int getLedgerId() {
		return ledgerId;
	}
	public void setLedgerId(int ledgerId) {
		this.ledgerId = ledgerId;
	}
	public String getLedgerName() {
		return ledgerName;
	}
	public void setLedgerName(String ledgerName) {
		this.ledgerName = ledgerName;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Boolean getIsSuperAdmin() {
		return isSuperAdmin;
	}
	public void setIsSuperAdmin(Boolean isSuperAdmin) {
		this.isSuperAdmin = isSuperAdmin;
	}
	private int id;
	private Boolean isSuperAdmin;
	

}
