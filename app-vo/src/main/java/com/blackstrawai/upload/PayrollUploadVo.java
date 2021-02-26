package com.blackstrawai.upload;

import com.blackstrawai.common.BaseVo;

public class PayrollUploadVo extends BaseVo{
	
	private String employeeId = "0" ;
	private String employeeName= "0" ;
	private String earningsBasic= "0" ;
	private String earningsHRA= "0" ;
	private String earningsSpecialAllowance= "0" ;
	private String earningsOvertime= "0" ;
	private String earningsConveyance= "0" ;
	private String earningsFuel= "0" ;
	private String earningsTelephone= "0" ;
	private String deductionsIncomeTax= "0" ;
	private String deductionsProfessionalTax= "0" ;
	private String deductionsPf= "0" ;
	private String totalEarnings= "0" ;
	private String totalDeductions= "0" ;
	private String netPayable= "0" ;
	private String status  ;
	private String earningsLeaveEncashment = "0" ;
	private String  earningsBonus= "0" ;
	private String earningsTravel = "0" ;
	private String deductionsPfEmployeeContribution = "0" ;
	private String  deductionsPfEmployerContribution = "0" ;
	private String deductionsEsiEmployeeContribution = "0" ;
	private String  deductionsEsiEmployerContribution = "0" ;
    private String payPeriod= "0" ;
    private String paymentCycle= "0" ;
	private String responseStatus;
	private String responseMessage;
	
	
	public String getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
	public String getEmployeeName() {
		return employeeName;
	}
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}
	public String getEarningsBasic() {
		return earningsBasic;
	}
	public void setEarningsBasic(String earningsBasic) {
		this.earningsBasic = earningsBasic;
	}
	public String getEarningsHRA() {
		return earningsHRA;
	}
	public void setEarningsHRA(String earningsHRA) {
		this.earningsHRA = earningsHRA;
	}
	public String getEarningsSpecialAllowance() {
		return earningsSpecialAllowance;
	}
	public void setEarningsSpecialAllowance(String earningsSpecialAllowance) {
		this.earningsSpecialAllowance = earningsSpecialAllowance;
	}
	public String getEarningsOvertime() {
		return earningsOvertime;
	}
	public void setEarningsOvertime(String earningsOvertime) {
		this.earningsOvertime = earningsOvertime;
	}
	public String getEarningsConveyance() {
		return earningsConveyance;
	}
	public void setEarningsConveyance(String earningsConveyance) {
		this.earningsConveyance = earningsConveyance;
	}
	public String getEarningsFuel() {
		return earningsFuel;
	}
	public void setEarningsFuel(String earningsFuel) {
		this.earningsFuel = earningsFuel;
	}
	public String getEarningsTelephone() {
		return earningsTelephone;
	}
	public void setEarningsTelephone(String earningsTelephone) {
		this.earningsTelephone = earningsTelephone;
	}
	public String getDeductionsIncomeTax() {
		return deductionsIncomeTax;
	}
	public void setDeductionsIncomeTax(String deductionsIncomeTax) {
		this.deductionsIncomeTax = deductionsIncomeTax;
	}
	public String getDeductionsProfessionalTax() {
		return deductionsProfessionalTax;
	}
	public void setDeductionsProfessionalTax(String deductionsProfessionalTax) {
		this.deductionsProfessionalTax = deductionsProfessionalTax;
	}
	public String getDeductionsPf() {
		return deductionsPf;
	}
	public void setDeductionsPf(String deductionsPf) {
		this.deductionsPf = deductionsPf;
	}
	public String getTotalEarnings() {
		return totalEarnings;
	}
	public void setTotalEarnings(String totalEarnings) {
		this.totalEarnings = totalEarnings;
	}
	public String getTotalDeductions() {
		return totalDeductions;
	}
	public void setTotalDeductions(String totalDeductions) {
		this.totalDeductions = totalDeductions;
	}
	public String getNetPayable() {
		return netPayable;
	}
	public void setNetPayable(String netPayable) {
		this.netPayable = netPayable;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getEarningsLeaveEncashment() {
		return earningsLeaveEncashment;
	}
	public void setEarningsLeaveEncashment(String earningsLeaveEncashment) {
		this.earningsLeaveEncashment = earningsLeaveEncashment;
	}
	public String getEarningsBonus() {
		return earningsBonus;
	}
	public void setEarningsBonus(String earningsBonus) {
		this.earningsBonus = earningsBonus;
	}
	public String getEarningsTravel() {
		return earningsTravel;
	}
	public void setEarningsTravel(String earningsTravel) {
		this.earningsTravel = earningsTravel;
	}
	public String getDeductionsPfEmployeeContribution() {
		return deductionsPfEmployeeContribution;
	}
	public void setDeductionsPfEmployeeContribution(String deductionsPfEmployeeContribution) {
		this.deductionsPfEmployeeContribution = deductionsPfEmployeeContribution;
	}
	public String getDeductionsPfEmployerContribution() {
		return deductionsPfEmployerContribution;
	}
	public void setDeductionsPfEmployerContribution(String deductionsPfEmployerContribution) {
		this.deductionsPfEmployerContribution = deductionsPfEmployerContribution;
	}
	public String getDeductionsEsiEmployeeContribution() {
		return deductionsEsiEmployeeContribution;
	}
	public void setDeductionsEsiEmployeeContribution(String deductionsEsiEmployeeContribution) {
		this.deductionsEsiEmployeeContribution = deductionsEsiEmployeeContribution;
	}
	public String getDeductionsEsiEmployerContribution() {
		return deductionsEsiEmployerContribution;
	}
	public void setDeductionsEsiEmployerContribution(String deductionsEsiEmployerContribution) {
		this.deductionsEsiEmployerContribution = deductionsEsiEmployerContribution;
	}

	public String getResponseStatus() {
		return responseStatus;
	}
	public void setResponseStatus(String responseStatus) {
		this.responseStatus = responseStatus;
	}
	public String getResponseMessage() {
		return responseMessage;
	}
	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}
	
	
	
	public String getPayPeriod() {
		return payPeriod;
	}
	public void setPayPeriod(String payPeriod) {
		this.payPeriod = payPeriod;
	}
	public String getPaymentCycle() {
		return paymentCycle;
	}
	public void setPaymentCycle(String paymentCycle) {
		this.paymentCycle = paymentCycle;
	}
	@Override
	public String toString() {
		return "PayrollUploadVo [employeeId=" + employeeId + ", employeeName=" + employeeName + ", earningsBasic="
				+ earningsBasic + ", earningsHRA=" + earningsHRA + ", earningsSpecialAllowance="
				+ earningsSpecialAllowance + ", earningsOvertime=" + earningsOvertime + ", earningsConveyance="
				+ earningsConveyance + ", earningsFuel=" + earningsFuel + ", earningsTelephone=" + earningsTelephone
				+ ", deductionsIncomeTax=" + deductionsIncomeTax + ", deductionsProfessionalTax="
				+ deductionsProfessionalTax + ", deductionsPf=" + deductionsPf + ", totalEarnings=" + totalEarnings
				+ ", totalDeductions=" + totalDeductions + ", netPayable=" + netPayable + ", status=" + status
				+ ", earningsLeaveEncashment=" + earningsLeaveEncashment + ", earningsBonus=" + earningsBonus
				+ ", earningsTravel=" + earningsTravel + ", deductionsPfEmployeeContribution="
				+ deductionsPfEmployeeContribution + ", deductionsPfEmployerContribution="
				+ deductionsPfEmployerContribution + ", deductionsEsiEmployeeContribution="
				+ deductionsEsiEmployeeContribution + ", deductionsEsiEmployerContribution="
				+ deductionsEsiEmployerContribution + ", payPeriod=" + payPeriod + ", paymentCycle=" + paymentCycle
				+ ", responseStatus=" + responseStatus + ", responseMessage=" + responseMessage + "]";
	}

	

  
}
