package com.blackstrawai.ar.invoice;

import java.util.List;


public class ArInvoiceTaxDetailsVo {

	
	private Integer id;
	
	private String componentName;
	
	private Integer componentItemId;
	
	private String groupName;
	
	private List<ArInvoiceTaxDistributionVo> taxDistribution;

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

	public List<ArInvoiceTaxDistributionVo> getTaxDistribution() {
		return taxDistribution;
	}

	public void setTaxDistribution(List<ArInvoiceTaxDistributionVo> taxDistribution) {
		this.taxDistribution = taxDistribution;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ARInvoiceTaxDetailsVo [id=");
		builder.append(id);
		builder.append(", componentName=");
		builder.append(componentName);
		builder.append(", componentItemId=");
		builder.append(componentItemId);
		builder.append(", groupName=");
		builder.append(groupName);
		builder.append(", taxDistribution=");
		builder.append(taxDistribution);
		builder.append("]");
		return builder.toString();
	}
	
	
}
