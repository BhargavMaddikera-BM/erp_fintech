package com.blackstrawai.ap.creditnote;

import java.util.List;

public class CnTaxDetailsVo {

	private Integer id;
	private String componentName;
	private Integer componentItemId;
	private String groupName;
	private List<CnTaxDistributionVo> taxDistribution;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getComponentName() {
		return componentName;
	}

	public void setComponentName(String componentName) {
		this.componentName = componentName;
	}

	public Integer getComponentItemId() {
		return componentItemId;
	}

	public void setComponentItemId(Integer componentItemId) {
		this.componentItemId = componentItemId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public List<CnTaxDistributionVo> getTaxDistribution() {
		return taxDistribution;
	}

	public void setTaxDistribution(List<CnTaxDistributionVo> taxDistribution) {
		this.taxDistribution = taxDistribution;
	}

	@Override
	public String toString() {
		return "CnTaxDetailsVo [id=" + id + ", componentName=" + componentName + ", componentItemId=" + componentItemId
				+ ", groupName=" + groupName + ", taxDistribution=" + taxDistribution + "]";
	}

}
