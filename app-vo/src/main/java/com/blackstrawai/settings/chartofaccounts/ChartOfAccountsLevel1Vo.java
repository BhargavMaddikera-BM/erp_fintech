package com.blackstrawai.settings.chartofaccounts;

import java.util.List;

public class ChartOfAccountsLevel1Vo {
	
	private int id;
	private String name;
	private List<ChartOfAccountsLevel2Vo>level2;
	
	// To be used in all level hierarchy drop down only
	private List<ChartOfAccountsLevel2Vo>child;
	
	
	public List<ChartOfAccountsLevel2Vo> getLevel2() {
		return level2;
	}
	public void setLevel2(List<ChartOfAccountsLevel2Vo> level2) {
		this.level2 = level2;
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
	public List<ChartOfAccountsLevel2Vo> getChild() {
		return child;
	}
	public void setChild(List<ChartOfAccountsLevel2Vo> child) {
		this.child = child;
	}
	
}
