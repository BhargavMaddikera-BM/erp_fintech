package com.blackstrawai.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.blackstrawai.keycontact.customer.CustomerVo;
import com.blackstrawai.request.payroll.PayrollUploadRequest;
import com.blackstrawai.response.keycontact.customer.CustomerBriefResponse;
import com.blackstrawai.upload.PayrollUploadVo;

public class ConvertToResponseHelper {

	private static ConvertToResponseHelper convertToResponseHelper;

	private ConvertToResponseHelper() {

	}

	final String active_status = "ACTIVE";
	final String act_status = "ACT";
	
	public static ConvertToResponseHelper getInstance() {
		if (convertToResponseHelper == null) {
			convertToResponseHelper = new ConvertToResponseHelper();
		}
		return convertToResponseHelper;
	}
	
	public CustomerBriefResponse convertCustVoToCustBriefResponse(CustomerVo customerVo) {
		CustomerBriefResponse briefResponse = null;
		if(customerVo!= null) {
			briefResponse = new CustomerBriefResponse();
			briefResponse.setId(customerVo.getId());
			briefResponse.setPrimaryContact(customerVo.getPrimaryInfo().getPrimaryContact());
			briefResponse.setCity(customerVo.getBillingAddress().getCity());
			briefResponse.setDisplayName(customerVo.getPrimaryInfo().getCustomerDisplayName());
			briefResponse.setMobileNumber(customerVo.getPrimaryInfo().getMobileNo());
			briefResponse.setCompanyName(customerVo.getPrimaryInfo().getCompanyName());
			briefResponse.setStatus(customerVo.getStatus());
			briefResponse.setIsActive(customerVo.getStatus()!=null ? (customerVo.getStatus().equalsIgnoreCase(act_status) || customerVo.getStatus().equalsIgnoreCase(active_status)) ? true : false : null);
			briefResponse.setPhoneNumber(customerVo.getPrimaryInfo().getPhoneNo());
		}
		return briefResponse;
	}
	
	public List<PayrollUploadVo> convertPayrollUploadVoFromPayrollRequest(List<PayrollUploadRequest> payroll,
			Map<String,String>status) {
		List<PayrollUploadVo> data=new ArrayList<PayrollUploadVo>();
		String statusValue=null,message=null;
		if(status!=null && status.containsKey("Success")){
			statusValue="Success";
			message=status.get("Success");
		}else{
			statusValue="Failure";
			message=status.get("Failure");
		}
		for(int i=0;i<payroll.size();i++){
			PayrollUploadRequest requestData=payroll.get(i);			
			PayrollUploadVo voData=new PayrollUploadVo(); 
				voData.setEmployeeId(requestData.getEmployeeId());
				voData.setEmployeeName(requestData.getEmployeeName());
				voData.setEarningsBasic(requestData.getEarningsBasic());
				voData.setEarningsHRA(requestData.getEarningsHRA());
				voData.setEarningsSpecialAllowance(requestData.getEarningsSpecialAllowance());
				voData.setEarningsConveyance(requestData.getEarningsConveyance());
				voData.setDeductionsPfEmployerContribution(requestData.getDeductionsEsiEmployerContributionDebit());
				voData.setDeductionsEsiEmployerContribution(requestData.getDeductionsEsiEmployerContributionDebit());
				voData.setEarningsLeaveEncashment(requestData.getEarningsLeaveEncashment());
				voData.setEarningsBonus(requestData.getEarningsBonus());
				voData.setEarningsOvertime(requestData.getEarningsOvertime());
				voData.setDeductionsPfEmployeeContribution(requestData.getDeductionsEsiEmployerContributionCredit());
				voData.setDeductionsEsiEmployeeContribution(requestData.getDeductionsEsiEmployeeContributionCredit());
				voData.setDeductionsProfessionalTax(requestData.getDeductionsProfessionalTax());
				voData.setDeductionsIncomeTax(requestData.getDeductionsIncomeTax());
				voData.setNetPayable(requestData.getNetPayable());
				voData.setResponseStatus(statusValue);
				voData.setResponseMessage(message);
				data.add(voData);
		}
		return data;
	}
	
}
