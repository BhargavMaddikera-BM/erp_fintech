package com.blackstrawai.request.ar.invoice;

import java.util.List;


public class ArInvoiceTaxDetailsRequest {

	
	private Integer id;
	
	private String componentName;
	
	private Integer componentItemId;
	
	private String groupName;
	
	private List<ArInvoiceTaxDistributionRequest> taxDistribution;

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

	public List<ArInvoiceTaxDistributionRequest> getTaxDistribution() {
		return taxDistribution;
	}

	public void setTaxDistribution(List<ArInvoiceTaxDistributionRequest> taxDistribution) {
		this.taxDistribution = taxDistribution;
	}
	
	
}
