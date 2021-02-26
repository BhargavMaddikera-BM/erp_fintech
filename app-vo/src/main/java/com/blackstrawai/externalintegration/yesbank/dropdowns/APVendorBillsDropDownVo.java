package com.blackstrawai.externalintegration.yesbank.dropdowns;

import java.util.List;

import com.blackstrawai.ap.dropdowns.BasicVoucherEntriesVo;
import com.blackstrawai.ap.dropdowns.PaymentNonCoreBillDetailsDropDownVo;
import com.blackstrawai.ap.dropdowns.PoDetailsDropDownVo;
import com.blackstrawai.common.BaseVo;
import com.blackstrawai.keycontact.vendor.VendorBankDetailsVo;

public class APVendorBillsDropDownVo extends BaseVo{

	private Integer vendorId;
	
	private String vendorName;
	
	private Integer orgId;

	private BasicVoucherEntriesVo paymentRefNoVoucher;

	private List<VendorBankDetailsVo> bankdetails;
	
	private List<PaymentNonCoreBillDetailsDropDownVo> bills;
	
	private List<PoDetailsDropDownVo> purchaseOrders;

	

	public BasicVoucherEntriesVo getPaymentRefNoVoucher() {
		return paymentRefNoVoucher;
	}

	public void setPaymentRefNoVoucher(BasicVoucherEntriesVo paymentRefNoVoucher) {
		this.paymentRefNoVoucher = paymentRefNoVoucher;
	}

	public Integer getVendorId() {
		return vendorId;
	}

	public void setVendorId(Integer vendorId) {
		this.vendorId = vendorId;
	}

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	public List<VendorBankDetailsVo> getBankdetails() {
		return bankdetails;
	}

	public void setBankdetails(List<VendorBankDetailsVo> bankdetails) {
		this.bankdetails = bankdetails;
	}

	public List<PaymentNonCoreBillDetailsDropDownVo> getBills() {
		return bills;
	}

	public void setBills(List<PaymentNonCoreBillDetailsDropDownVo> bills) {
		this.bills = bills;
	}

	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}

	public List<PoDetailsDropDownVo> getPurchaseOrders() {
		return purchaseOrders;
	}

	public void setPurchaseOrders(List<PoDetailsDropDownVo> purchaseOrders) {
		this.purchaseOrders = purchaseOrders;
	}

	
}
