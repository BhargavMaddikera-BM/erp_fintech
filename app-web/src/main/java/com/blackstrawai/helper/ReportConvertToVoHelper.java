package com.blackstrawai.helper;

import com.blackstrawai.externalintegration.compliance.taxilla.TaxillaReportVo;
import com.blackstrawai.request.externalintegration.compliance.TaxillaReportRequest;

public class ReportConvertToVoHelper {
	private static ReportConvertToVoHelper reportConvertToVoHelper;

	public static ReportConvertToVoHelper getInstance() {
		if (reportConvertToVoHelper == null) {
			reportConvertToVoHelper = new ReportConvertToVoHelper();
		}
		return reportConvertToVoHelper;
	}
	
	public TaxillaReportVo convertTaxillaReportRequestToVo(TaxillaReportRequest request) {
		TaxillaReportVo tr = new TaxillaReportVo();
		tr.setGstin(request.getGstin());
		tr.setGstSearchType(request.getGstSearchType());
		tr.setGstType(request.getGstType());
		tr.setOrganizationId(request.getOrganizationId());
		tr.setRetPeriod(request.getRetPeriod());
		tr.setRoleName(request.getRoleName());
		tr.setStateCd(request.getStateCd());
		tr.setUserId(request.getUserId());
		tr.setUsername(request.getUsername());
		return tr;
	}
}
