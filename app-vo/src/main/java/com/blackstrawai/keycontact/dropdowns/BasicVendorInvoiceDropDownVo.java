package com.blackstrawai.keycontact.dropdowns;

import java.util.List;

import com.blackstrawai.ap.billsinvoice.InvoiceListVo;
import com.blackstrawai.common.BaseVo;
import com.blackstrawai.settings.CommonVo;

public class BasicVendorInvoiceDropDownVo extends BaseVo{

	private List<InvoiceListVo> invoiceList;
	
	private List<CommonVo> vendorLedgers;

	public List<InvoiceListVo> getInvoiceList() {
		return invoiceList;
	}

	public void setInvoiceList(List<InvoiceListVo> invoiceList) {
		this.invoiceList = invoiceList;
	}

	public List<CommonVo> getVendorLedgers() {
		return vendorLedgers;
	}

	public void setVendorLedgers(List<CommonVo> vendorLedgers) {
		this.vendorLedgers = vendorLedgers;
	}

	
	
	
	
	
}
