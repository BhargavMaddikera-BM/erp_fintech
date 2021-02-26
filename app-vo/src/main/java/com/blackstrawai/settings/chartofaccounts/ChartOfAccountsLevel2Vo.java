package com.blackstrawai.settings.chartofaccounts;

import java.util.List;

public class ChartOfAccountsLevel2Vo {
	private int id;
	private String name;
	private List<ChartOfAccountsLevel3Vo>level3;
	
	// To be used in all level hierarchy drop down only
	private List<ChartOfAccountsLevel3Vo>child;
	
	
	public List<ChartOfAccountsLevel3Vo> getLevel3() {
		return level3;
	}
	public void setLevel3(List<ChartOfAccountsLevel3Vo> level3) {
		this.level3 = level3;
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
	public List<ChartOfAccountsLevel3Vo> getChild() {
		return child;
	}
	public void setChild(List<ChartOfAccountsLevel3Vo> child) {
		this.child = child;
	}
	
}
