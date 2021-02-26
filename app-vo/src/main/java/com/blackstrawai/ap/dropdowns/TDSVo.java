package com.blackstrawai.ap.dropdowns;

public class TDSVo {
	
	private int id;
	private String name;	
	private String tdsRateIdentifier;
	private String tdsRatePercentage;
	private String description;
	private String applicableFor;
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getApplicableFor() {
		return applicableFor;
	}
	public void setApplicableFor(String applicableFor) {
		this.applicableFor = applicableFor;
	}
	public String getTdsRatePercentage() {
		return tdsRatePercentage;
	}
	public void setTdsRatePercentage(String tdsRatePercentage) {
		this.tdsRatePercentage = tdsRatePercentage;
	}
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
	public String getTdsRateIdentifier() {
		return tdsRateIdentifier;
	}
	public void setTdsRateIdentifier(String tdsRateIdentifier) {
		this.tdsRateIdentifier = tdsRateIdentifier;
	}
	public Integer getValue() {
		return value;
	}
	public void setValue(Integer value) {
		this.value = value;
	}
	
	
}
