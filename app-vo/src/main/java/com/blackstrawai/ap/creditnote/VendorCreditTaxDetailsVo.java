package com.blackstrawai.ap.creditnote;

import java.util.List;

public class VendorCreditTaxDetailsVo {
	private Integer id;
	private String componentName;
	private Integer componentItemId;
	private String groupName;
	private List<VendorCreditTaxDistributionVo> taxDistribution;
	
	
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
	public List<VendorCreditTaxDistributionVo> getTaxDistribution() {
		return taxDistribution;
	}
	public void setTaxDistribution(List<VendorCreditTaxDistributionVo> taxDistribution) {
		this.taxDistribution = taxDistribution;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("InvoiceTaxDetailsVo [id=");
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
