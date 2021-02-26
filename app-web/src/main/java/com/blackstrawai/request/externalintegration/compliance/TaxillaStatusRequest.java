package com.blackstrawai.request.externalintegration.compliance;

import java.util.List;

import com.blackstrawai.common.BaseRequest;

public class TaxillaStatusRequest extends BaseRequest {
	private List<String> gstList;

	public List<String> getGstList() {
		return gstList;
	}

	public void setGstList(List<String> gstList) {
		this.gstList = gstList;
	}
}
