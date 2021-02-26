package com.blackstrawai.request.payroll.PayRun;

import java.math.BigDecimal;
import java.util.List;

public class PayRunInformationRequest {
	
	private Integer payRunInfoId;
	private Integer payRunId;
	private String employeeId;
	private String employeeName;
	private BigDecimal earningsBasic;
	private BigDecimal earningsHRA;
	private BigDecimal earningsSpecialAllowance;
	private BigDecimal earningsOvertime;
	private BigDecimal earningsConveyance;
	private BigDecimal earningsFuel;
	private BigDecimal earningsTelephone;
	private BigDecimal deductionsIncomeTax;
	private BigDecimal deductionsProfessionalTax;
	private BigDecimal deductionsPf;
	private BigDecimal totalEarnings;
	private BigDecimal totalDeductions;
	private BigDecimal netPayable;
	private String status;
	private BigDecimal earningsLeaveEncashment ;
	private BigDecimal  earningsBonus;
	private BigDecimal earningsTravel ;
	private BigDecimal deductionsPfEmployeeContribution ;
	private BigDecimal  deductionsPfEmployerContribution ;
	private BigDecimal deductionsEsiEmployeeContribution ;
	private BigDecimal  deductionsEsiEmployerContribution ;
	private String otherColumns;
	
	public BigDecimal getEarningsLeaveEncashment() {
		return earningsLeaveEncashment;
	}
	public void setEarningsLeaveEncashment(BigDecimal earningsLeaveEncashment) {
		this.earningsLeaveEncashment = earningsLeaveEncashment;
	}
	public BigDecimal getEarningsBonus() {
		return earningsBonus;
	}
	public void setEarningsBonus(BigDecimal earningsBonus) {
		this.earningsBonus = earningsBonus;
	}
	public BigDecimal getEarningsTravel() {
		return earningsTravel;
	}
	public void setEarningsTravel(BigDecimal earningsTravel) {
		this.earningsTravel = earningsTravel;
	}
	public BigDecimal getDeductionsPfEmployeeContribution() {
		return deductionsPfEmployeeContribution;
	}
	public void setDeductionsPfEmployeeContribution(BigDecimal deductionsPfEmployeeContribution) {
		this.deductionsPfEmployeeContribution = deductionsPfEmployeeContribution;
	}
	public BigDecimal getDeductionsPfEmployerContribution() {
		return deductionsPfEmployerContribution;
	}
	public void setDeductionsPfEmployerContribution(BigDecimal deductionsPfEmployerContribution) {
		this.deductionsPfEmployerContribution = deductionsPfEmployerContribution;
	}
	public BigDecimal getDeductionsEsiEmployeeContribution() {
		return deductionsEsiEmployeeContribution;
	}
	public void setDeductionsEsiEmployeeContribution(BigDecimal deductionsEsiEmployeeContribution) {
		this.deductionsEsiEmployeeContribution = deductionsEsiEmployeeContribution;
	}
	public BigDecimal getDeductionsEsiEmployerContribution() {
		return deductionsEsiEmployerContribution;
	}
	public void setDeductionsEsiEmployerContribution(BigDecimal deductionsEsiEmployerContribution) {
		this.deductionsEsiEmployerContribution = deductionsEsiEmployerContribution;
	}
	public Integer getPayRunInfoId() {
		return payRunInfoId;
	}
	public void setPayRunInfoId(Integer payRunInfoId) {
		this.payRunInfoId = payRunInfoId;
	}
	public Integer getPayRunId() {
		return payRunId;
	}
	public void setPayRunId(Integer payRunId) {
		this.payRunId = payRunId;
	}
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
	public BigDecimal getEarningsBasic() {
		return earningsBasic;
	}
	public void setEarningsBasic(BigDecimal earningsBasic) {
		this.earningsBasic = earningsBasic;
	}
	public BigDecimal getEarningsHRA() {
		return earningsHRA;
	}
	public void setEarningsHRA(BigDecimal earningsHRA) {
		this.earningsHRA = earningsHRA;
	}
	public BigDecimal getEarningsSpecialAllowance() {
		return earningsSpecialAllowance;
	}
	public void setEarningsSpecialAllowance(BigDecimal earningsSpecialAllowance) {
		this.earningsSpecialAllowance = earningsSpecialAllowance;
	}
	public BigDecimal getEarningsOvertime() {
		return earningsOvertime;
	}
	public void setEarningsOvertime(BigDecimal earningsOvertime) {
		this.earningsOvertime = earningsOvertime;
	}
	public BigDecimal getEarningsConveyance() {
		return earningsConveyance;
	}
	public void setEarningsConveyance(BigDecimal earningsConveyance) {
		this.earningsConveyance = earningsConveyance;
	}
	public BigDecimal getEarningsFuel() {
		return earningsFuel;
	}
	public void setEarningsFuel(BigDecimal earningsFuel) {
		this.earningsFuel = earningsFuel;
	}
	public BigDecimal getEarningsTelephone() {
		return earningsTelephone;
	}
	public void setEarningsTelephone(BigDecimal earningsTelephone) {
		this.earningsTelephone = earningsTelephone;
	}
	public BigDecimal getDeductionsIncomeTax() {
		return deductionsIncomeTax;
	}
	public void setDeductionsIncomeTax(BigDecimal deductionsIncomeTax) {
		this.deductionsIncomeTax = deductionsIncomeTax;
	}
	public BigDecimal getDeductionsProfessionalTax() {
		return deductionsProfessionalTax;
	}
	public void setDeductionsProfessionalTax(BigDecimal deductionsProfessionalTax) {
		this.deductionsProfessionalTax = deductionsProfessionalTax;
	}
	public BigDecimal getDeductionsPf() {
		return deductionsPf;
	}
	public void setDeductionsPf(BigDecimal deductionsPf) {
		this.deductionsPf = deductionsPf;
	}
	public BigDecimal getTotalEarnings() {
		return totalEarnings;
	}
	public void setTotalEarnings(BigDecimal totalEarnings) {
		this.totalEarnings = totalEarnings;
	}
	public BigDecimal getTotalDeductions() {
		return totalDeductions;
	}
	public void setTotalDeductions(BigDecimal totalDeductions) {
		this.totalDeductions = totalDeductions;
	}
	public BigDecimal getNetPayable() {
		return netPayable;
	}
	public void setNetPayable(BigDecimal netPayable) {
		this.netPayable = netPayable;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getOtherColumns() {
		return otherColumns;
	}
	public void setOtherColumns(String otherColumns) {
		this.otherColumns = otherColumns;
	}
	
	
	
	
	
	
	
	
	
	
	
}
