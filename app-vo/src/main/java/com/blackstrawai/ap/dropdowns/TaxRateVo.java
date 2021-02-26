package com.blackstrawai.ap.dropdowns;

public class TaxRateVo {
	private int id;
	private String name;
	private String description;
	private Integer combinedRate;
	private Boolean isInter;
	private Integer value;
	
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
	public Integer getCombinedRate() {
		return combinedRate;
	}
	public void setCombinedRate(Integer combinedRate) {
		this.combinedRate = combinedRate;
	}
	public Integer getValue() {
		return value;
	}
	public void setValue(Integer value) {
		this.value = value;
	}
	public Boolean getIsInter() {
		return isInter;
	}
	public void setIsInter(Boolean isInter) {
		this.isInter = isInter;
	}
	
	
}
