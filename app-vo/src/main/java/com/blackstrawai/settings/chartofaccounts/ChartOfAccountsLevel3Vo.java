package com.blackstrawai.settings.chartofaccounts;

import java.util.List;

public class ChartOfAccountsLevel3Vo {
	
	private int id;
	private String name;
	private List<ChartOfAccountsLevel4Vo>level4;

	// To be used in all level hierarchy drop down only
	private List<ChartOfAccountsLevel4Vo>child;
	
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
	public List<ChartOfAccountsLevel4Vo> getLevel4() {
		return level4;
	}
	public void setLevel4(List<ChartOfAccountsLevel4Vo> level4) {
		this.level4 = level4;
	}
	public List<ChartOfAccountsLevel4Vo> getChild() {
		return child;
	}
	public void setChild(List<ChartOfAccountsLevel4Vo> child) {
		this.child = child;
	}

}
