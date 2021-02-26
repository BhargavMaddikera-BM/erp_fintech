package com.blackstrawai.ap.dropdowns;

import java.util.List;

import com.blackstrawai.ap.payment.noncore.CreditDetailsVo;
import com.blackstrawai.ap.payment.noncore.MultiLevelContactVo;
import com.blackstrawai.ap.purchaseorder.PoListVo;
import com.blackstrawai.payroll.BasicPayTypeVo;
import com.blackstrawai.settings.TaxRateTypeVo;
import com.blackstrawai.settings.chartofaccounts.MinimalChartOfAccountsVo;

public class PaymentNonCoreDropDownVo {
	private List<MultiLevelContactVo> paidVia;
	private List<PaymentTypeVo> paymentTypes;
	private List<PaymentTypeVo> vendorNames;
	private List<MinimalChartOfAccountsVo> vendorAccounts;
	private List<BasicCurrencyVo> currency;
	private List<String> paymentRefNos;
	private List<PaymentTypeVo> paidTo;
	private List<BasicPayTypeVo> payType;
	private List<PaymentTypeVo> employee;
	private List<TDSVo> tds;
	private List<CreditDetailsVo> creditDetails;
	private List<InvoiceDetailsDropDownVo> invoiceDetails;
	private List<PaymentNonCoreBillDetailsDropDownVo> billDetails;
	private List<PoListVo> poList;
/*	private List<PaymentNonCoreGstDetailsDropDownVo> gstDetails;
*/	private List<MinimalChartOfAccountsVo> contactAccounts;
	private List<PaymentModeDropDownVo> paymentModes;
	private BasicVoucherEntriesVo payRunVoucher;
	private BasicVoucherEntriesVo paymentRefNoVoucher;
	private BasicVoucherEntriesVo poRefNoVoucher;
	private PaymentNonCoreCustomTableVo customTableList;
	private List<MultiLevelContactVo> contact;
	private BasicCurrencyVo defaultCurrency;
	private List<TaxRateTypeVo> taxRateType;
	private List<PaymentTypeVo> payRun;
	private List<PaymentTypeVo> paidFor;
	private List<PaymentTypeVo> statutoryBody;
	
	public List<TaxRateTypeVo> getTaxRateType() {
		return taxRateType;
	}
	public void setTaxRateType(List<TaxRateTypeVo> taxRateType) {
		this.taxRateType = taxRateType;
	}
	public List<MultiLevelContactVo> getPaidVia() {
		return paidVia;
	}
	public void setPaidVia(List<MultiLevelContactVo> paidVia) {
		this.paidVia = paidVia;
	}
	public List<PaymentTypeVo> getPaymentTypes() {
		return paymentTypes;
	}
	public void setPaymentTypes(List<PaymentTypeVo> paymentTypes) {
		this.paymentTypes = paymentTypes;
	}
	public List<PaymentTypeVo> getVendorNames() {
		return vendorNames;
	}
	public void setVendorNames(List<PaymentTypeVo> vendorNames) {
		this.vendorNames = vendorNames;
	}
	public List<MinimalChartOfAccountsVo> getVendorAccounts() {
		return vendorAccounts;
	}
	public void setVendorAccounts(List<MinimalChartOfAccountsVo> vendorAccounts) {
		this.vendorAccounts = vendorAccounts;
	}
	public List<BasicCurrencyVo> getCurrency() {
		return currency;
	}
	public void setCurrency(List<BasicCurrencyVo> currency) {
		this.currency = currency;
	}
	public List<String> getPaymentRefNos() {
		return paymentRefNos;
	}
	public void setPaymentRefNos(List<String> paymentRefNos) {
		this.paymentRefNos = paymentRefNos;
	}
	
	
	public List<PaymentTypeVo> getPaidTo() {
		return paidTo;
	}
	public void setPaidTo(List<PaymentTypeVo> paidTo) {
		this.paidTo = paidTo;
	}
	public List<BasicPayTypeVo> getPayType() {
		return payType;
	}
	public void setPayType(List<BasicPayTypeVo> payType) {
		this.payType = payType;
	}
	public List<PaymentTypeVo> getEmployee() {
		return employee;
	}
	public void setEmployee(List<PaymentTypeVo> employee) {
		this.employee = employee;
	}
	public List<TDSVo> getTds() {
		return tds;
	}
	public void setTds(List<TDSVo> tds) {
		this.tds = tds;
	}
	public List<CreditDetailsVo> getCreditDetails() {
		return creditDetails;
	}
	public void setCreditDetails(List<CreditDetailsVo> creditDetails) {
		this.creditDetails = creditDetails;
	}
	public List<InvoiceDetailsDropDownVo> getInvoiceDetails() {
		return invoiceDetails;
	}
	public void setInvoiceDetails(List<InvoiceDetailsDropDownVo> invoiceDetails) {
		this.invoiceDetails = invoiceDetails;
	}
	public List<PaymentNonCoreBillDetailsDropDownVo> getBillDetails() {
		return billDetails;
	}
	public void setBillDetails(List<PaymentNonCoreBillDetailsDropDownVo> billDetails) {
		this.billDetails = billDetails;
	}
	public List<PoListVo> getPoList() {
		return poList;
	}
	public void setPoList(List<PoListVo> poList) {
		this.poList = poList;
	}
	/*public List<PaymentNonCoreGstDetailsDropDownVo> getGstDetails() {
		return gstDetails;
	}
	public void setGstDetails(List<PaymentNonCoreGstDetailsDropDownVo> gstDetails) {
		this.gstDetails = gstDetails;
	}*/
	public List<MinimalChartOfAccountsVo> getContactAccounts() {
		return contactAccounts;
	}
	public void setContactAccounts(List<MinimalChartOfAccountsVo> contactAccounts) {
		this.contactAccounts = contactAccounts;
	}
	public List<PaymentModeDropDownVo> getPaymentModes() {
		return paymentModes;
	}
	public void setPaymentModes(List<PaymentModeDropDownVo> paymentModes) {
		this.paymentModes = paymentModes;
	}
	public BasicVoucherEntriesVo getPayRunVoucher() {
		return payRunVoucher;
	}
	public void setPayRunVoucher(BasicVoucherEntriesVo payRunVoucher) {
		this.payRunVoucher = payRunVoucher;
	}
	public BasicVoucherEntriesVo getPaymentRefNoVoucher() {
		return paymentRefNoVoucher;
	}
	public void setPaymentRefNoVoucher(BasicVoucherEntriesVo paymentRefNoVoucher) {
		this.paymentRefNoVoucher = paymentRefNoVoucher;
	}
	public BasicVoucherEntriesVo getPoRefNoVoucher() {
		return poRefNoVoucher;
	}
	public void setPoRefNoVoucher(BasicVoucherEntriesVo poRefNoVoucher) {
		this.poRefNoVoucher = poRefNoVoucher;
	}
	public PaymentNonCoreCustomTableVo getCustomTableList() {
		return customTableList;
	}
	public void setCustomTableList(PaymentNonCoreCustomTableVo customTableList) {
		this.customTableList = customTableList;
	}
	public List<MultiLevelContactVo> getContact() {
		return contact;
	}
	public void setContact(List<MultiLevelContactVo> contact) {
		this.contact = contact;
	}
	public BasicCurrencyVo getDefaultCurrency() {
		return defaultCurrency;
	}
	public void setDefaultCurrency(BasicCurrencyVo defaultCurrency) {
		this.defaultCurrency = defaultCurrency;
	}
	public List<PaymentTypeVo> getPayRun() {
		return payRun;
	}
	public void setPayRun(List<PaymentTypeVo> payRun) {
		this.payRun = payRun;
	}
	public List<PaymentTypeVo> getPaidFor() {
		return paidFor;
	}
	public void setPaidFor(List<PaymentTypeVo> paidFor) {
		this.paidFor = paidFor;
	}
	public List<PaymentTypeVo> getStatutoryBody() {
		return statutoryBody;
	}
	public void setStatutoryBody(List<PaymentTypeVo> statutoryBody) {
		this.statutoryBody = statutoryBody;
	}
	
	
}
