package com.blackstrawai.payroll.payrun;

public class PayRunPeriodVo {
	
	private String id;
	
	private String name;
	private String value;
	
	private String payRunPeriodCycle;
	private String payRunPeriodStartDate;
	private String payRunPeriodEndDate;
	private String payRunPeriodPayFrequencyType;
	
   
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getPayRunPeriodCycle() {
		return payRunPeriodCycle;
	}
	public void setPayRunPeriodCycle(String payRunPeriodCycle) {
		this.payRunPeriodCycle = payRunPeriodCycle;
	}
	public String getPayRunPeriodStartDate() {
		return payRunPeriodStartDate;
	}
	public void setPayRunPeriodStartDate(String payRunPeriodStartDate) {
		this.payRunPeriodStartDate = payRunPeriodStartDate;
	}
	public String getPayRunPeriodEndDate() {
		return payRunPeriodEndDate;
	}
	public void setPayRunPeriodEndDate(String payRunPeriodEndDate) {
		this.payRunPeriodEndDate = payRunPeriodEndDate;
	}
	public String getPayRunPeriodPayFrequencyType() {
		return payRunPeriodPayFrequencyType;
	}
	public void setPayRunPeriodPayFrequencyType(String payRunPeriodPayFrequencyType) {
		this.payRunPeriodPayFrequencyType = payRunPeriodPayFrequencyType;
	}
	
	

}
