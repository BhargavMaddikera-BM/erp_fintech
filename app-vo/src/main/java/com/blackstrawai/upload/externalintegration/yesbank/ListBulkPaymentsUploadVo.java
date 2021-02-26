package com.blackstrawai.upload.externalintegration.yesbank;

import java.util.List;

import com.blackstrawai.common.BaseVo;

public class ListBulkPaymentsUploadVo extends BaseVo{
	private List<BulkPaymentsUploadVo> bulkpayments;

	public List<BulkPaymentsUploadVo> getBulkpayments() {
		return bulkpayments;
	}

	public void setBulkpayments(List<BulkPaymentsUploadVo> bulkpayments) {
		this.bulkpayments = bulkpayments;
	}

	
}
