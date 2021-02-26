package com.blackstrawai.settings;

import java.sql.Timestamp;

import com.blackstrawai.common.TokenVo;

public class BaseCurrencyVo extends TokenVo{
	
	private int id;
	private String name;
	private String description;
	private String symbol;
	private String alternateSymbol;
	private boolean isSpaceRequired;
	private boolean isMillions;
	private int numberOfDecimalPlaces;
	private String decimalValueDenoter;
	private int noOfDecimalsForAmoutInWords;
	private String exchangeValue;
	private String valueFormat;
	private Timestamp createTs;
	private Timestamp updateTs;
	public void setCreateTs(Timestamp createTs) {
		this.createTs = createTs;
	}
	public Timestamp getUpdateTs() {
		return updateTs;
	}
	public void setUpdateTs(Timestamp updateTs) {
		this.updateTs = updateTs;
	}
	public Timestamp getCreateTs() {
		return createTs;
	}
	public String getValueFormat() {
		return valueFormat;
	}
	public void setValueFormat(String valueFormat) {
		this.valueFormat = valueFormat;
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
	public int getNumberOfDecimalPlaces() {
		return numberOfDecimalPlaces;
	}
	public void setNumberOfDecimalPlaces(int numberOfDecimalPlaces) {
		this.numberOfDecimalPlaces = numberOfDecimalPlaces;
	}
	public String getDecimalValueDenoter() {
		return decimalValueDenoter;
	}
	public void setDecimalValueDenoter(String decimalValueDenoter) {
		this.decimalValueDenoter = decimalValueDenoter;
	}
	public int getNoOfDecimalsForAmoutInWords() {
		return noOfDecimalsForAmoutInWords;
	}
	public void setNoOfDecimalsForAmoutInWords(int noOfDecimalsForAmoutInWords) {
		this.noOfDecimalsForAmoutInWords = noOfDecimalsForAmoutInWords;
	}
	public String getExchangeValue() {
		return exchangeValue;
	}
	public void setExchangeValue(String exchangeValue) {
		this.exchangeValue = exchangeValue;
	}
	
	

}
