package com.blackstrawai.request.payroll;

public class PayrollUploadRequest {
	
	private String employeeId;
	private String employeeName;
	private String earningsBasic="0";
	private String earningsHRA="0";
	private String earningsSpecialAllowance="0";
	private String earningsConveyance="0";
	private String deductionsPfEmployerContributionDebit="0";
	private String deductionsEsiEmployeeContributionDebit="0";
	private String deductionsEsiEmployerContributionDebit="0";
	private String earningsLeaveEncashment="0";
	private String earningsTravel="0";
	private String earningsBonus="0";
	private String earningsOvertime="0";
	private String deductionsPfEmployeeContributionCredit="0";
	private String deductionsPfEmployerContributionCredit="0";
	private String deductionsEsiEmployeeContributionCredit="0";
	private String deductionsEsiEmployerContributionCredit="0";
	private String deductionsProfessionalTax="0";
	private String deductionsIncomeTax="0";
	private String netPayable="0";
    private String payPeriod = "0";
    private String paymentCycle= "0";
	
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
	public String getEarningsConveyance() {
		return earningsConveyance;
	}
	public void setEarningsConveyance(String earningsConveyance) {
		this.earningsConveyance = earningsConveyance;
	}
	public String getDeductionsPfEmployerContributionDebit() {
		return deductionsPfEmployerContributionDebit;
	}
	public void setDeductionsPfEmployerContributionDebit(String deductionsPfEmployerContributionDebit) {
		this.deductionsPfEmployerContributionDebit = deductionsPfEmployerContributionDebit;
	}
	public String getDeductionsEsiEmployeeContributionDebit() {
		return deductionsEsiEmployeeContributionDebit;
	}
	public void setDeductionsEsiEmployeeContributionDebit(String deductionsEsiEmployeeContributionDebit) {
		this.deductionsEsiEmployeeContributionDebit = deductionsEsiEmployeeContributionDebit;
	}
	public String getEarningsLeaveEncashment() {
		return earningsLeaveEncashment;
	}
	public void setEarningsLeaveEncashment(String earningsLeaveEncashment) {
		this.earningsLeaveEncashment = earningsLeaveEncashment;
	}
	public String getEarningsTravel() {
		return earningsTravel;
	}
	public void setEarningsTravel(String earningsTravel) {
		this.earningsTravel = earningsTravel;
	}
	public String getEarningsOvertime() {
		return earningsOvertime;
	}
	public void setEarningsOvertime(String earningsOvertime) {
		this.earningsOvertime = earningsOvertime;
	}
	public String getDeductionsPfEmployeeContributionCredit() {
		return deductionsPfEmployeeContributionCredit;
	}
	public void setDeductionsPfEmployeeContributionCredit(String deductionsPfEmployeeContributionCredit) {
		this.deductionsPfEmployeeContributionCredit = deductionsPfEmployeeContributionCredit;
	}
	public String getDeductionsPfEmployerContributionCredit() {
		return deductionsPfEmployerContributionCredit;
	}
	public void setDeductionsPfEmployerContributionCredit(String deductionsPfEmployerContributionCredit) {
		this.deductionsPfEmployerContributionCredit = deductionsPfEmployerContributionCredit;
	}
	public String getDeductionsEsiEmployeeContributionCredit() {
		return deductionsEsiEmployeeContributionCredit;
	}
	public void setDeductionsEsiEmployeeContributionCredit(String deductionsEsiEmployeeContributionCredit) {
		this.deductionsEsiEmployeeContributionCredit = deductionsEsiEmployeeContributionCredit;
	}
	public String getDeductionsEsiEmployerContributionCredit() {
		return deductionsEsiEmployerContributionCredit;
	}
	public void setDeductionsEsiEmployerContributionCredit(String deductionsEsiEmployerContributionCredit) {
		this.deductionsEsiEmployerContributionCredit = deductionsEsiEmployerContributionCredit;
	}
	public String getDeductionsProfessionalTax() {
		return deductionsProfessionalTax;
	}
	public void setDeductionsProfessionalTax(String deductionsProfessionalTax) {
		this.deductionsProfessionalTax = deductionsProfessionalTax;
	}
	public String getDeductionsIncomeTax() {
		return deductionsIncomeTax;
	}
	public void setDeductionsIncomeTax(String deductionsIncomeTax) {
		this.deductionsIncomeTax = deductionsIncomeTax;
	}
	public String getNetPayable() {
		return netPayable;
	}
	public void setNetPayable(String netPayable) {
		this.netPayable = netPayable;
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
	public String getDeductionsEsiEmployerContributionDebit() {
		return deductionsEsiEmployerContributionDebit;
	}
	public void setDeductionsEsiEmployerContributionDebit(String deductionsEsiEmployerContributionDebit) {
		this.deductionsEsiEmployerContributionDebit = deductionsEsiEmployerContributionDebit;
	}
	public String getEarningsBonus() {
		return earningsBonus;
	}
	public void setEarningsBonus(String earningsBonus) {
		this.earningsBonus = earningsBonus;
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
		return "PayrollUploadRequest [employeeId=" + employeeId + ", employeeName=" + employeeName + ", earningsBasic="
				+ earningsBasic + ", earningsHRA=" + earningsHRA + ", earningsSpecialAllowance="
				+ earningsSpecialAllowance + ", earningsConveyance=" + earningsConveyance
				+ ", deductionsPfEmployerContributionDebit=" + deductionsPfEmployerContributionDebit
				+ ", deductionsEsiEmployeeContributionDebit=" + deductionsEsiEmployeeContributionDebit
				+ ", deductionsEsiEmployerContributionDebit=" + deductionsEsiEmployerContributionDebit
				+ ", earningsLeaveEncashment=" + earningsLeaveEncashment + ", earningsTravel=" + earningsTravel
				+ ", earningsBonus=" + earningsBonus + ", earningsOvertime=" + earningsOvertime
				+ ", deductionsPfEmployeeContributionCredit=" + deductionsPfEmployeeContributionCredit
				+ ", deductionsPfEmployerContributionCredit=" + deductionsPfEmployerContributionCredit
				+ ", deductionsEsiEmployeeContributionCredit=" + deductionsEsiEmployeeContributionCredit
				+ ", deductionsEsiEmployerContributionCredit=" + deductionsEsiEmployerContributionCredit
				+ ", deductionsProfessionalTax=" + deductionsProfessionalTax + ", deductionsIncomeTax="
				+ deductionsIncomeTax + ", netPayable=" + netPayable + ", payPeriod=" + payPeriod + ", paymentCycle="
				+ paymentCycle + ", responseStatus=" + responseStatus + ", responseMessage=" + responseMessage + "]";
	}
	
	
	
	
	

  
}
