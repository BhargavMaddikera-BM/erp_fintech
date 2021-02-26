package com.blackstrawai.response.payroll.payrun;

import com.blackstrawai.ap.dropdowns.BasicVoucherEntriesVo;
import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.payroll.dropdowns.PayRunEmployeeDropDownVo;
import com.blackstrawai.payroll.dropdowns.PayRunPaymentCycleDropDownVo;
import com.blackstrawai.payroll.dropdowns.PayRunPeriodDropDownVo;

public class PayRunDropDownResponse extends BaseResponse {

	private PayRunPeriodDropDownVo periodDropDowndata;
		
	private PayRunPaymentCycleDropDownVo paymentCycleDropDowdata;
	
	private PayRunEmployeeDropDownVo payRunEmployeeDropDownVo;
	
	private BasicVoucherEntriesVo payRunReferenceNumber;

	public PayRunPeriodDropDownVo getPeriodDropDowndata() {
		return periodDropDowndata;
	}

	public void setPeriodDropDowndata(PayRunPeriodDropDownVo periodDropDowndata) {
		this.periodDropDowndata = periodDropDowndata;
	}



	public PayRunPaymentCycleDropDownVo getPaymentCycleDropDowdata() {
		return paymentCycleDropDowdata;
	}

	public void setPaymentCycleDropDowdata(PayRunPaymentCycleDropDownVo paymentCycleDropDowdata) {
		this.paymentCycleDropDowdata = paymentCycleDropDowdata;
	}

	public PayRunEmployeeDropDownVo getPayRunEmployeeDropDownVo() {
		return payRunEmployeeDropDownVo;
	}

	public void setPayRunEmployeeDropDownVo(PayRunEmployeeDropDownVo payRunEmployeeDropDownVo) {
		this.payRunEmployeeDropDownVo = payRunEmployeeDropDownVo;
	}

	public BasicVoucherEntriesVo getPayRunReferenceNumber() {
		return payRunReferenceNumber;
	}

	public void setPayRunReferenceNumber(BasicVoucherEntriesVo payRunReferenceNumber) {
		this.payRunReferenceNumber = payRunReferenceNumber;
	}
	
	
	
	
}
