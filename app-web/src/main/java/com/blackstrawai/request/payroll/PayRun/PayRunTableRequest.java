package com.blackstrawai.request.payroll.PayRun;

import java.util.List;

import com.blackstrawai.common.BaseRequest;

public class PayRunTableRequest extends BaseRequest {

	List<PayRunTableInfoRequest> payRunTableInfo;

	public List<PayRunTableInfoRequest> getPayRunTableInfo() {
		return payRunTableInfo;
	}

	public void setPayRunTableInfo(List<PayRunTableInfoRequest> payRunTableInfo) {
		this.payRunTableInfo = payRunTableInfo;
	}

    
	
	
	
}
