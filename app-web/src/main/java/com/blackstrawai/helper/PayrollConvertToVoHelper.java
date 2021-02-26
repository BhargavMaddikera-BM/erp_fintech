package com.blackstrawai.helper;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.blackstrawai.attachments.UploadFileVo;
import com.blackstrawai.payroll.PayCycleVo;
import com.blackstrawai.payroll.PayItemVo;
import com.blackstrawai.payroll.PayPeriodVo;
import com.blackstrawai.payroll.PayTypeVo;
import com.blackstrawai.payroll.payrun.PayRunImportVo;
import com.blackstrawai.payroll.payrun.PayRunTableVo;
import com.blackstrawai.payroll.payrun.PayRunVo;
import com.blackstrawai.request.payroll.PayCycleRequest;
import com.blackstrawai.request.payroll.PayItemRequest;
import com.blackstrawai.request.payroll.PayPeriodRequest;
import com.blackstrawai.request.payroll.PayTypeRequest;
import com.blackstrawai.request.payroll.PayRun.PayRunImportRequest;
import com.blackstrawai.request.payroll.PayRun.PayRunRequest;
import com.blackstrawai.request.payroll.PayRun.PayRunTableRequest;
import com.fasterxml.jackson.core.JsonProcessingException;

public class PayrollConvertToVoHelper {
	private Logger logger = Logger.getLogger(PayrollConvertToVoHelper.class);

	
	private static PayrollConvertToVoHelper payrollConvertToVoHelper;

	public static PayrollConvertToVoHelper getInstance() {
		if (payrollConvertToVoHelper == null) {
			payrollConvertToVoHelper = new PayrollConvertToVoHelper();
		}
		return payrollConvertToVoHelper;
	}

	public PayCycleVo convertPayCycleVoFromPayCycleRequest(PayCycleRequest request) {
		PayCycleVo payCycle = new PayCycleVo();
		payCycle.setId(request.getId());
		payCycle.setName(request.getName());
		payCycle.setCycle(request.getCycle());
		payCycle.setStartDate(request.getStartDate());
		payCycle.setEndDate(request.getEndDate());
		payCycle.setStatus(request.getStatus());
		payCycle.setOrganizationId(request.getOrganizationId());
		payCycle.setUserId(request.getUserId());
		payCycle.setRoleName(request.getRoleName());
		payCycle.setUpdateUserId(request.getUpdateUserId());
		payCycle.setUpdateRoleName(request.getUpdateRoleName());
		return payCycle;
	}

	public PayItemVo convertPayItemVoFromPayItemRequest(PayItemRequest payItemRequest) {
		PayItemVo data = new PayItemVo();
		data.setRoleName(payItemRequest.getRoleName());
		data.setName(payItemRequest.getName());
		data.setDescription(payItemRequest.getDescription());
		data.setIsSuperAdmin(payItemRequest.getIsSuperAdmin());
		data.setOrganizationId(payItemRequest.getOrganizationId());
		data.setUserId(payItemRequest.getUserId());
		data.setId(payItemRequest.getId());
		data.setPayType(payItemRequest.getPayType());
		data.setLedgerId(payItemRequest.getLedgerId());
		data.setLedgerName(payItemRequest.getLedgerName());
		return data;
	}

	public PayPeriodVo convertPayPeriodVoFromPayPeriodRequest(PayPeriodRequest request) {

		PayPeriodVo payPeriod = new PayPeriodVo();
		payPeriod.setId(request.getId());
		payPeriod.setFrequency(request.getFrequency());
		payPeriod.setPeriod(request.getPeriod());
		payPeriod.setOrganizationId(request.getOrganizationId());
		payPeriod.setRoleName(request.getRoleName());
		payPeriod.setUpdateRoleName(request.getUpdateRoleName());
		payPeriod.setUpdateUserId(request.getUpdateUserId());
		payPeriod.setUserId(request.getUserId());
		payPeriod.setCycle(request.getCycle());
		payPeriod.setPayFrequencyList(request.getPayFrequencyList());
		return payPeriod;

	}

	public PayRunVo convertPayRunVoFromRequest(PayRunRequest request) throws JsonProcessingException {
		PayRunVo payRunVo =new PayRunVo();
		payRunVo.setPayRunId(request.getPayRunId());
		payRunVo.setOrgId(request.getOrgId());
		payRunVo.setUserId(request.getUserId());
		payRunVo.setIsSuperAdmin(request.getIsSuperAdmin());
		payRunVo.setRoleName(request.getRoleName());
		payRunVo.setPayPeriod(request.getPayPeriod());
		payRunVo.setPayrunDate(request.getPayrunDate());
		payRunVo.setPayrunReference(request.getPayrunReference());
		payRunVo.setStatus(request.getStatus());	
		payRunVo.setCopyPreviousPayRun(request.getCopyPreviousPayRun());
		payRunVo.setPaymentCycle(request.getPaymentCycle());
		logger.info("Payruninfo -convertor" + request.getPayRunInformation() );
		payRunVo.setPayRunInformation(request.getPayRunInformation());
//		if(request.getPayRunInformation() != null ) {
//			List<PayRunInformationVo> payRunInfo = new ArrayList<PayRunInformationVo>();
//			request.getPayRunInformation().forEach(info -> {
//				payRunInfo.add(convertPayRunInfoVoFromRequest(info , payRunId));
//			});
//			payRunVo.setPayRunInformation(payRunInfo);	
//
//
//		}

		if(request.getAttachments() != null && request.getAttachments().size() > 0) {
			List<UploadFileVo> uploadList = new ArrayList<UploadFileVo>();
			request.getAttachments().forEach(file -> {
				uploadList.add(ConvertToVoHelper.getInstance().convertAttachmentFromReq(file));
			});
			payRunVo.setAttachments(uploadList);
		}
		payRunVo.setAttachmentsToRemove(request.getAttachmentsToRemove());
		payRunVo.setPayRunRefPrefix(request.getPayRunRefPrefix());
		payRunVo.setPayRunRefSuffix(request.getPayRunRefSuffix());
		
		List<PayRunTableVo> payRunTableVos = new ArrayList<>();
		for(PayRunTableVo info : request.getSettingsData()) {
			PayRunTableVo payRunTableVo = new PayRunTableVo();
			 payRunTableVo.setId(info.getId());
			 payRunTableVo.setName(info.getName());
			 payRunTableVo.setValue(info.getValue());
			 payRunTableVo.setPayType(info.getPayType());
			 payRunTableVo.setIsShowColumn(info.getIsShowColumn());
			 payRunTableVo.setFinalName(info.getFinalName());
			 payRunTableVos.add(payRunTableVo);
		}
		
		payRunVo.setSettingsData(payRunTableVos);
		payRunVo.setCurrencyId(request.getCurrencyId());
		payRunVo.setGeneralLedgerData(request.getGeneralLedgerData());
		logger.info("Payrun Convertor "+payRunVo);
		return payRunVo;

	}

	/*private PayRunInformationVo convertPayRunInfoVoFromRequest(PayRunInformationRequest info, Integer payRunId) {
		PayRunInformationVo payRunInfo = new PayRunInformationVo();
		
		payRunInfo.setPayRunInfoId(info.getPayRunInfoId());
		payRunInfo.setPayRunId(payRunId);
		payRunInfo.setEmployeeId(info.getEmployeeId());
		payRunInfo.setEmployeeName(info.getEmployeeName());
		payRunInfo.setEarningsBasic(info.getEarningsBasic());
		payRunInfo.setEarningsConveyance(info.getEarningsConveyance());
		payRunInfo.setEarningsFuel(info.getEarningsFuel());
		payRunInfo.setEarningsHRA(info.getEarningsHRA());
		payRunInfo.setEarningsOvertime(info.getEarningsOvertime());
		payRunInfo.setEarningsSpecialAllowance(info.getEarningsSpecialAllowance());
		payRunInfo.setEarningsTelephone(info.getEarningsTelephone());
		payRunInfo.setDeductionsIncomeTax(info.getDeductionsIncomeTax());
		payRunInfo.setDeductionsPf(info.getDeductionsPf());
		payRunInfo.setDeductionsProfessionalTax(info.getDeductionsProfessionalTax());
		payRunInfo.setTotalDeductions(info.getTotalDeductions());
		payRunInfo.setTotalEarnings(info.getTotalEarnings());
		payRunInfo.setNetPayable(info.getNetPayable());
		payRunInfo.setEarningsBonus(info.getEarningsBonus());
		payRunInfo.setEarningsLeaveEncashment(info.getEarningsLeaveEncashment());
		payRunInfo.setEarningsTravel(info.getEarningsTravel());
		payRunInfo.setDeductionsEsiEmployeeContribution(info.getDeductionsEsiEmployeeContribution());
		payRunInfo.setDeductionsEsiEmployerContribution(info.getDeductionsEsiEmployerContribution());
		payRunInfo.setDeductionsPfEmployeeContribution(info.getDeductionsPfEmployeeContribution());
		payRunInfo.setDeductionsPfEmployerContribution(info.getDeductionsPfEmployerContribution());
		payRunInfo.setStatus(info.getStatus());
//	    logger.info("Other Columns"+ info.getOtherColumns());
	    payRunInfo.setOtherColumns(info.getOtherColumns().toString());
		return payRunInfo;
	}
*/
	public PayTypeVo convertPayTypeVoFromPayTypeRequest(PayTypeRequest payTypeRequest) {
		PayTypeVo data = new PayTypeVo();
		data.setRoleName(payTypeRequest.getRoleName());
		data.setName(payTypeRequest.getName());
		data.setDescription(payTypeRequest.getDescription());
		data.setIsSuperAdmin(payTypeRequest.getIsSuperAdmin());
		data.setOrganizationId(payTypeRequest.getOrganizationId());
		data.setUserId(payTypeRequest.getUserId());
		data.setId(payTypeRequest.getId());
		data.setParentId(payTypeRequest.getParentId());
		data.setParentName(payTypeRequest.getParentName());
		data.setIsBase(false);
		return data;
	}

	public List<PayRunTableVo> convertPayRunTableVoFromRequest(PayRunTableRequest request) {
		
		List<PayRunTableVo> payRunTableVos = new ArrayList<>();
		
		if( request.getPayRunTableInfo() != null ) {	
			 request.getPayRunTableInfo().forEach(info -> {
				 PayRunTableVo payRunTableVo =new PayRunTableVo();
				 payRunTableVo.setId(info.getId());
				 payRunTableVo.setName(info.getName());
				 payRunTableVo.setValue(info.getValue());
				 payRunTableVo.setPayType(info.getPayType());
				 payRunTableVo.setIsShowColumn(info.getIsShowColumn());
				 payRunTableVos.add(payRunTableVo);
			 });
		}

		return payRunTableVos;
	}

	public PayRunImportVo convertPayRunVoImportFromRequest(PayRunImportRequest request) {
		
		PayRunImportVo payRunImportVo =new PayRunImportVo();
		payRunImportVo.setPayRunId(request.getPayRunId());
		payRunImportVo.setOrgId(request.getOrgId());
		payRunImportVo.setUserId(request.getUserId());
		payRunImportVo.setIsSuperAdmin(request.getIsSuperAdmin());
		payRunImportVo.setRoleName(request.getRoleName());
		if(request.getPayRunAttachment() != null ) {
			payRunImportVo.setPayRunAttachment(ConvertToVoHelper.getInstance().convertAttachmentFromReqImport(request.getPayRunAttachment()));
		
		}
		

		return payRunImportVo;
	}

}
