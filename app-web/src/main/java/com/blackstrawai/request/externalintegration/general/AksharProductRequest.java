package com.blackstrawai.request.externalintegration.general;

import java.util.List;

import com.blackstrawai.common.BaseRequest;

public class AksharProductRequest extends BaseRequest{

	
	private int organizationId;
	private List<AksharLineItemRequest>lineItem;

	public List<AksharLineItemRequest> getLineItem() {
		return lineItem;
	}

	public void setLineItem(List<AksharLineItemRequest> lineItem) {
		this.lineItem = lineItem;
	}
	public int getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(int organizationId) {
		this.organizationId = organizationId;
	}

}
