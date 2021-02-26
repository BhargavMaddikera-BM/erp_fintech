package com.blackstrawai.ar.dropdowns;

import java.util.List;

import com.blackstrawai.ap.dropdowns.BasicUnitOfMeasureVo;
import com.blackstrawai.ap.dropdowns.BasicVoucherEntriesVo;
import com.blackstrawai.ap.dropdowns.TDSVo;
import com.blackstrawai.ap.dropdowns.TaxRateVo;
import com.blackstrawai.ar.invoice.ArInvoiceVo;
import com.blackstrawai.onboarding.organization.BasicGSTLocationDetailsVo;
import com.blackstrawai.settings.CommonVo;

public class CreditNoteDropdownVo {

	
	private List<BasicUnitOfMeasureVo> measures;	
	private List<TaxRateVo> taxGroups;	
	private List<TDSVo> tds;		
	private BasicVoucherEntriesVo creditNoteNo;
	private List<ArInvoiceVo> invoices;
	private List<CommonVo> reasons;
	private BasicGSTLocationDetailsVo locationDetails;
	
	public BasicGSTLocationDetailsVo getLocationDetails() {
		return locationDetails;
	}
	public void setLocationDetails(BasicGSTLocationDetailsVo locationDetails) {
		this.locationDetails = locationDetails;
	}
	public List<CommonVo> getReasons() {
		return reasons;
	}
	public void setReasons(List<CommonVo> reasons) {
		this.reasons = reasons;
	}
	public List<BasicUnitOfMeasureVo> getMeasures() {
		return measures;
	}
	public void setMeasures(List<BasicUnitOfMeasureVo> measures) {
		this.measures = measures;
	}
	public List<TaxRateVo> getTaxGroups() {
		return taxGroups;
	}
	public void setTaxGroups(List<TaxRateVo> taxGroups) {
		this.taxGroups = taxGroups;
	}
	public List<TDSVo> getTds() {
		return tds;
	}
	public void setTds(List<TDSVo> tds) {
		this.tds = tds;
	}
	
	
	public BasicVoucherEntriesVo getCreditNoteNo() {
		return creditNoteNo;
	}
	public void setCreditNoteNo(BasicVoucherEntriesVo creditNoteNo) {
		this.creditNoteNo = creditNoteNo;
	}
	public List<ArInvoiceVo> getInvoices() {
		return invoices;
	}
	public void setInvoices(List<ArInvoiceVo> invoices) {
		this.invoices = invoices;
	}
	
	
	
		
}
