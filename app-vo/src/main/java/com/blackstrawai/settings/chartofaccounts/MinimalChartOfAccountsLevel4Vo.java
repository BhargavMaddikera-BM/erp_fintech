package com.blackstrawai.settings.chartofaccounts;

public class MinimalChartOfAccountsLevel4Vo {
	
	private Integer id;
	
	private String name;
	
	private String level;
	
	private Integer value;
	
	private Integer level3Id;

	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	public Integer getLevel3Id() {
		return level3Id;
	}

	public void setLevel3Id(Integer level3Id) {
		this.level3Id = level3Id;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("MinimalChartOfAccountsLevel4Vo [id=");
		builder.append(id);
		builder.append(", name=");
		builder.append(name);
		builder.append(", level=");
		builder.append(level);
		builder.append(", value=");
		builder.append(value);
		builder.append(", level3Id=");
		builder.append(level3Id);
		builder.append("]");
		return builder.toString();
	}
	
	
}
