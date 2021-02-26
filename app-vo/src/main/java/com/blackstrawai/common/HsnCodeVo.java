package com.blackstrawai.common;

public class HsnCodeVo extends BaseVo{

	private Integer id;
	
	private String commodityName;
	
	private String hsnCode;
	
	private String interStateGst;
	
	private String intraStateGst;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCommodityName() {
		return commodityName;
	}

	public void setCommodityName(String commodityName) {
		this.commodityName = commodityName;
	}

	public String getHsnCode() {
		return hsnCode;
	}

	public void setHsnCode(String hsnCode) {
		this.hsnCode = hsnCode;
	}

	public String getInterStateGst() {
		return interStateGst;
	}

	public void setInterStateGst(String interStateGst) {
		this.interStateGst = interStateGst;
	}

	public String getIntraStateGst() {
		return intraStateGst;
	}

	public void setIntraStateGst(String intraStateGst) {
		this.intraStateGst = intraStateGst;
	}
	
	
	
}
