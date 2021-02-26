package com.blackstrawai.externalintegration.yesbank.dropdowns;

import java.util.List;

import com.blackstrawai.ap.dropdowns.BasicVoucherEntriesVo;
import com.blackstrawai.common.BaseVo;
import com.blackstrawai.keycontact.vendor.VendorBankDetailsVo;
import com.blackstrawai.payroll.payrun.PayRunEmployeeAmountVo;

public class APEmployeesPayRunDropDownVo extends BaseVo{

	private Integer employeeId;
	
	private String employeeName;
	
	private Integer orgId;

	private BasicVoucherEntriesVo paymentRefNoVoucher;

	private List<VendorBankDetailsVo> bankdetails;
	
	private  List<PayRunEmployeeAmountVo>  payruns;

	public Integer getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Integer employeeId) {
		this.employeeId = employeeId;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}

	public BasicVoucherEntriesVo getPaymentRefNoVoucher() {
		return paymentRefNoVoucher;
	}

	public void setPaymentRefNoVoucher(BasicVoucherEntriesVo paymentRefNoVoucher) {
		this.paymentRefNoVoucher = paymentRefNoVoucher;
	}

	public List<VendorBankDetailsVo> getBankdetails() {
		return bankdetails;
	}

	public void setBankdetails(List<VendorBankDetailsVo> bankdetails) {
		this.bankdetails = bankdetails;
	}

	public List<PayRunEmployeeAmountVo> getPayruns() {
		return payruns;
	}

	public void setPayruns(List<PayRunEmployeeAmountVo> payruns) {
		this.payruns = payruns;
	}
	

	
}
