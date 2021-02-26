package com.blackstrawai.request.payroll;

import java.util.List;

import com.blackstrawai.common.BaseRequest;
import com.blackstrawai.payroll.PayFrequencyListVo;

public class PayPeriodRequest extends BaseRequest{

	private Integer id;
	private String frequency;
	private String period;
	private Integer organizationId;
	private String roleName;
	private String updateRoleName;
	private String updateUserId;
	private String userId;
	private String cycle;
	private List<PayFrequencyListVo> payFrequencyList;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getFrequency() {
		return frequency;
	}
	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}
	public String getPeriod() {
		return period;
	}
	public void setPeriod(String period) {
		this.period = period;
	}
	public Integer getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public String getUpdateRoleName() {
		return updateRoleName;
	}
	public void setUpdateRoleName(String updateRoleName) {
		this.updateRoleName = updateRoleName;
	}
	public String getUpdateUserId() {
		return updateUserId;
	}
	public void setUpdateUserId(String updateUserId) {
		this.updateUserId = updateUserId;
	}
	public List<PayFrequencyListVo> getPayFrequencyList() {
		return payFrequencyList;
	}
	public void setPayFrequencyList(List<PayFrequencyListVo> payFrequencyList) {
		this.payFrequencyList = payFrequencyList;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getCycle() {
		return cycle;
	}
	public void setCycle(String cycle) {
		this.cycle = cycle;
	}
	
	

}
