package com.blackstrawai.payroll.dropdowns;

import java.util.List;

import com.blackstrawai.ap.dropdowns.PaymentTypeVo;

public class PayPeriodDropDownVo {
	private List<PaymentTypeVo> frequency;
	private List<PayPeriodCycleVo> cycle;
	private List<PaymentTypeVo> period;
	public List<PaymentTypeVo> getFrequency() {
		return frequency;
	}
	public void setFrequency(List<PaymentTypeVo> frequency) {
		this.frequency = frequency;
	}
	public List<PayPeriodCycleVo> getCycle() {
		return cycle;
	}
	public void setCycle(List<PayPeriodCycleVo> cycle) {
		this.cycle = cycle;
	}
	public List<PaymentTypeVo> getPeriod() {
		return period;
	}
	public void setPeriod(List<PaymentTypeVo> period) {
		this.period = period;
	}
	
	
}
