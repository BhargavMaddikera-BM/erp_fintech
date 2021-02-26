package com.blackstrawai.ar.dropdowns;

import java.util.List;

import com.blackstrawai.ap.dropdowns.BasicCurrencyVo;
import com.blackstrawai.ap.dropdowns.BasicCustomerVo;
import com.blackstrawai.ap.dropdowns.BasicVoucherEntriesVo;
import com.blackstrawai.onboarding.organization.BasicGSTLocationDetailsVo;
import com.blackstrawai.settings.CommonVo;
import com.blackstrawai.settings.chartofaccounts.MinimalChartOfAccountsVo;

public class RefundDropdownVo {
	private List<BasicCustomerVo> customersList;	
	private List<PaymentModeVo> paymentModeList;
	private List<MinimalChartOfAccountsVo> accountsList;
	private BasicVoucherEntriesVo refundRefernce;
	private List<BasicCurrencyVo> currency;
	private List<CommonVo> refundTypes;
	private int currencyId;
	private BasicGSTLocationDetailsVo locationDetails;
	private int locationId;
	
	
	

	public List<CommonVo> getRefundTypes() {
		return refundTypes;
	}

	public void setRefundTypes(List<CommonVo> refundTypes) {
		this.refundTypes = refundTypes;
	}


	public int getCurrencyId() {
		return currencyId;
	}

	public void setCurrencyId(int currencyId) {
		this.currencyId = currencyId;
	}

	public int getLocationId() {
		return locationId;
	}

	public void setLocationId(int locationId) {
		this.locationId = locationId;
	}

	public BasicGSTLocationDetailsVo getLocationDetails() {
		return locationDetails;
	}

	public void setLocationDetails(BasicGSTLocationDetailsVo locationDetails) {
		this.locationDetails = locationDetails;
	}

	public BasicVoucherEntriesVo getRefundRefernce() {
		return refundRefernce;
	}
	public List<BasicCurrencyVo> getCurrency() {
		return currency;
	}
	public void setCurrency(List<BasicCurrencyVo> currency) {
		this.currency = currency;
	}
	public void setRefundRefernce(BasicVoucherEntriesVo refundRefernce) {
		this.refundRefernce = refundRefernce;
	}
	
	public List<BasicCustomerVo> getCustomersList() {
		return customersList;
	}

	public void setCustomersList(List<BasicCustomerVo> customersList) {
		this.customersList = customersList;
	}

	
	public List<PaymentModeVo> getPaymentModeList() {
		return paymentModeList;
	}
	public void setPaymentModeList(List<PaymentModeVo> paymentModeList) {
		this.paymentModeList = paymentModeList;
	}
	public List<MinimalChartOfAccountsVo> getAccountsList() {
		return accountsList;
	}
	public void setAccountsList(List<MinimalChartOfAccountsVo> accountsList) {
		this.accountsList = accountsList;
	}


}
