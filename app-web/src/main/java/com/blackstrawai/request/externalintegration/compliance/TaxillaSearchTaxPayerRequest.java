package com.blackstrawai.request.externalintegration.compliance;

import java.util.List;

import com.blackstrawai.common.BaseRequest;

public class TaxillaSearchTaxPayerRequest extends BaseRequest{

	private List<String> gst;

	public List<String> getGst() {
		return gst;
	}

	public void setGst(List<String> gst) {
		this.gst = gst;
	}
	

}
