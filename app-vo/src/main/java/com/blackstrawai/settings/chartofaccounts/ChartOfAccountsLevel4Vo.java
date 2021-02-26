package com.blackstrawai.settings.chartofaccounts;

import java.util.List;

public class ChartOfAccountsLevel4Vo {
	private int id;
	private String name;
	private List<ChartOfAccountsLevel5Vo>level5;
	
	// To be used in all level hierarchy drop down only
	private List<ChartOfAccountsLevel5Vo>child;
	
	
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
	public List<ChartOfAccountsLevel5Vo> getLevel5() {
		return level5;
	}
	public void setLevel5(List<ChartOfAccountsLevel5Vo> level5) {
		this.level5 = level5;
	}
	public List<ChartOfAccountsLevel5Vo> getChild() {
		return child;
	}
	public void setChild(List<ChartOfAccountsLevel5Vo> child) {
		this.child = child;
	}

	
}
