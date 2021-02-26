package com.blackstrawai.externalintegration.general.akshar;

import java.util.List;

import com.blackstrawai.common.BaseVo;
import com.blackstrawai.externalintegration.general.aadhaar.AksharLineItemVo;

public class AksharProductVo extends BaseVo{

	public int getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(int organizationId) {
		this.organizationId = organizationId;
	}

	private int organizationId;
	private List<AksharLineItemVo>lineItem;
	public List<AksharLineItemVo> getLineItem() {
		return lineItem;
	}

	public void setLineItem(List<AksharLineItemVo> lineItem) {
		this.lineItem = lineItem;
	}

	
}
