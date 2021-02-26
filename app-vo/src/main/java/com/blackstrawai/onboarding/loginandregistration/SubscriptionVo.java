package com.blackstrawai.onboarding.loginandregistration;



import java.sql.Date;

import com.blackstrawai.common.TokenVo;

public class SubscriptionVo extends TokenVo {
	
	private int id;
	private String type;
	private Date startPeriod;
	private Date endPeriod;
	private double preDiscountAmount;
	private double discountAmount;
	private double finalAmount;
	private String modules;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Date getStartPeriod() {
		return startPeriod;
	}
	public void setStartPeriod(Date startPeriod) {
		this.startPeriod = startPeriod;
	}
	public Date getEndPeriod() {
		return endPeriod;
	}
	public void setEndPeriod(Date endPeriod) {
		this.endPeriod = endPeriod;
	}
	public double getPreDiscountAmount() {
		return preDiscountAmount;
	}
	public void setPreDiscountAmount(double preDiscountAmount) {
		this.preDiscountAmount = preDiscountAmount;
	}
	public double getDiscountAmount() {
		return discountAmount;
	}
	public void setDiscountAmount(double discountAmount) {
		this.discountAmount = discountAmount;
	}
	public double getFinalAmount() {
		return finalAmount;
	}
	public void setFinalAmount(double finalAmount) {
		this.finalAmount = finalAmount;
	}
	public String getModules() {
		return modules;
	}
	public void setModules(String modules) {
		this.modules = modules;
	}
	

}
