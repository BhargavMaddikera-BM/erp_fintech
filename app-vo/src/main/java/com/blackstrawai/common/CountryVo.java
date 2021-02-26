package com.blackstrawai.common;

import java.util.ArrayList;
import java.util.List;

public class CountryVo {

	private int id;
	private String name;
	private String code;
	private List<StateVo>state=new ArrayList<StateVo>();
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
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public List<StateVo> getState() {
		return state;
	}
	public void setState(List<StateVo> state) {
		this.state = state;
	}
}
