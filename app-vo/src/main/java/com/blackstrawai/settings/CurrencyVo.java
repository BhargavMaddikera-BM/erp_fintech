package com.blackstrawai.settings;

import java.sql.Timestamp;

import com.blackstrawai.common.TokenVo;

public class CurrencyVo extends TokenVo{
	
	private int id;
	private String name;
	private String description;
	private String symbol;
	private String alternateSymbol;
	private boolean isSpaceRequired;
	private boolean isMillions;
	private int noOfDecimalPlaces;
    private String decimalValueDenoter;
    private int noOfDecimalsForAmount;
    private String exchangeValue;
    private Timestamp createTs;
    private Timestamp updateTs;
    private int organizationId;
    private boolean isSuperAdmin;
    private String status;
    private String directRate;
	private String indirectRate;
	
	
	public String getDirectRate() {
		return directRate;
	}
	public void setDirectRate(String directRate) {
		this.directRate = directRate;
	}
	public String getIndirectRate() {
		return indirectRate;
	}
	public void setIndirectRate(String indirectRate) {
		this.indirectRate = indirectRate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(int organizationId) {
		this.organizationId = organizationId;
	}
	
	public boolean isSuperAdmin() {
		return isSuperAdmin;
	}
	public void setSuperAdmin(boolean isSuperAdmin) {
		this.isSuperAdmin = isSuperAdmin;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	public String getAlternateSymbol() {
		return alternateSymbol;
	}
	public void setAlternateSymbol(String alternateSymbol) {
		this.alternateSymbol = alternateSymbol;
	}
	public boolean isSpaceRequired() {
		return isSpaceRequired;
	}
	public void setSpaceRequired(boolean isSpaceRequired) {
		this.isSpaceRequired = isSpaceRequired;
	}
	public boolean isMillions() {
		return isMillions;
	}
	public void setMillions(boolean isMillions) {
		this.isMillions = isMillions;
	}
	public int getNoOfDecimalPlaces() {
		return noOfDecimalPlaces;
	}
	public void setNoOfDecimalPlaces(int noOfDecimalPlaces) {
		this.noOfDecimalPlaces = noOfDecimalPlaces;
	}
	public String getDecimalValueDenoter() {
		return decimalValueDenoter;
	}
	public void setDecimalValueDenoter(String decimalValueDenoter) {
		this.decimalValueDenoter = decimalValueDenoter;
	}
	public int getNoOfDecimalsForAmount() {
		return noOfDecimalsForAmount;
	}
	public void setNoOfDecimalsForAmount(int noOfDecimalsForAmount) {
		this.noOfDecimalsForAmount = noOfDecimalsForAmount;
	}
	public String getExchangeValue() {
		return exchangeValue;
	}
	public void setExchangeValue(String exchangeValue) {
		this.exchangeValue = exchangeValue;
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
	@Override
	public String toString() {
		return "CurrencyVo [id=" + id + ", name=" + name + ", description=" + description + ", symbol=" + symbol
				+ ", alternateSymbol=" + alternateSymbol + ", isSpaceRequired=" + isSpaceRequired + ", isMillions="
				+ isMillions + ", noOfDecimalPlaces=" + noOfDecimalPlaces + ", decimalValueDenoter="
				+ decimalValueDenoter + ", noOfDecimalsForAmount=" + noOfDecimalsForAmount + ", exchangeValue="
				+ exchangeValue + ", createTs=" + createTs + ", updateTs=" + updateTs + ", organizationId="
				+ organizationId + ", isSuperAdmin=" + isSuperAdmin + ", status=" + status + ", directRate="
				+ directRate + ", indirectRate=" + indirectRate + "]";
	}
	
	
    
}
