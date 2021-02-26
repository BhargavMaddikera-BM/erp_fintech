package com.blackstrawai.request.settings;

import java.sql.Timestamp;
import java.util.List;

import com.blackstrawai.common.BaseRequest;

public class PaymentTermsRequest extends BaseRequest{

	private Integer id;
	private String paymentTermsName;
	private String description;
	private String baseDate;
	private Integer daysLimit;
	private List<String> accountType;
	private Integer organizationId;
	private Boolean isSuperAdmin;
	private String status;
	private Timestamp createTs;
	private String roleName;
	
	
	
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public Timestamp getCreateTs() {
		return createTs;
	}
	public void setCreateTs(Timestamp createTs) {
		this.createTs = createTs;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getPaymentTermsName() {
		return paymentTermsName;
	}
	public void setPaymentTermsName(String paymentTermsName) {
		this.paymentTermsName = paymentTermsName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getBaseDate() {
		return baseDate;
	}
	public void setBaseDate(String baseDate) {
		this.baseDate = baseDate;
	}
	public Integer getDaysLimit() {
		return daysLimit;
	}
	public void setDaysLimit(Integer daysLimit) {
		this.daysLimit = daysLimit;
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
	public List<String> getAccountType() {
		return accountType;
	}
	public void setAccountType(List<String> accountType) {
		this.accountType = accountType;
	}
	
	
	
}
