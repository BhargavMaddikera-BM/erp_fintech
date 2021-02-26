package com.blackstrawai.externalintegration.banking.common;

public class BasicAccountDetailsVo {
	private Integer id;
	private String name;
	private String type;
	private Integer value;
	private Integer cutomerId;
	private String bankName;
	private boolean isAPIBankingEnabled;
	private String accountNo;
	private Integer orgId;
	private int userId;
	private String roleName;
	private boolean isDefault;
	
	public boolean getIsDefault() {
		return isDefault;
	}
	public void setIsDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public Integer getOrgId() {
		return orgId;
	}
	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}
	public String getAccountNo() {
		return accountNo;
	}
	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public boolean isAPIBankingEnabled() {
		return isAPIBankingEnabled;
	}
	public void setAPIBankingEnabled(boolean isAPIBankingEnabled) {
		this.isAPIBankingEnabled = isAPIBankingEnabled;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Integer getValue() {
		return value;
	}
	public void setValue(Integer value) {
		this.value = value;
	}
	public Integer getCutomerId() {
		return cutomerId;
	}
	public void setCutomerId(Integer cutomerId) {
		this.cutomerId = cutomerId;
	}
	@Override
	public String toString() {
		return "BasicAccountDetailsVo [id=" + id + ", name=" + name + ", type=" + type + ", value=" + value
				+ ", cutomerId=" + cutomerId + ", bankName=" + bankName + ", isAPIBankingEnabled=" + isAPIBankingEnabled
				+ ", accountNo=" + accountNo + ", orgId=" + orgId + ", userId=" + userId + ", roleName=" + roleName
				+ ", isDefault=" + isDefault + "]";
	}
	
	
	
}
