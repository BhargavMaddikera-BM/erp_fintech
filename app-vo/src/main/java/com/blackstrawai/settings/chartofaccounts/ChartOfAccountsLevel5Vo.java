package com.blackstrawai.settings.chartofaccounts;

import java.util.List;

public class ChartOfAccountsLevel5Vo {
	private int id;
	private String name;
	private List<ChartOfAccountsLevel6Vo>level6;
	
	// To be used in all level hierarchy drop down only
	private List<ChartOfAccountsLevel6Vo>child;
	
	
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
	public List<ChartOfAccountsLevel6Vo> getLevel6() {
		return level6;
	}
	public void setLevel6(List<ChartOfAccountsLevel6Vo> level6) {
		this.level6 = level6;
	}
	public List<ChartOfAccountsLevel6Vo> getChild() {
		return child;
	}
	public void setChild(List<ChartOfAccountsLevel6Vo> child) {
		this.child = child;
	}
	
	
}
