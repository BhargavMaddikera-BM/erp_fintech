package com.blackstrawai.ar.dropdowns;

import java.util.List;

import com.blackstrawai.keycontact.customer.CustomerBillingAddressVo;
import com.blackstrawai.keycontact.customer.CustomerDeliveryAddressVo;
import com.blackstrawai.keycontact.customer.CustomerPrimaryInfoVo;
import com.blackstrawai.settings.CommonVo;

public class BasicCustomerDetailsVo {

	private Integer id;

	private List<CommonVo> customerLedgers;
	
	private CustomerPrimaryInfoVo primaryInfo;

	private CustomerBillingAddressVo billingAddress;

	private CustomerDeliveryAddressVo deliveryAddress;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public List<CommonVo> getCustomerLedgers() {
		return customerLedgers;
	}

	public void setCustomerLedgers(List<CommonVo> customerLedgers) {
		this.customerLedgers = customerLedgers;
	}

	public CustomerPrimaryInfoVo getPrimaryInfo() {
		return primaryInfo;
	}

	public void setPrimaryInfo(CustomerPrimaryInfoVo primaryInfo) {
		this.primaryInfo = primaryInfo;
	}

	public CustomerBillingAddressVo getBillingAddress() {
		return billingAddress;
	}

	public void setBillingAddress(CustomerBillingAddressVo billingAddress) {
		this.billingAddress = billingAddress;
	}

	public CustomerDeliveryAddressVo getDeliveryAddress() {
		return deliveryAddress;
	}

	public void setDeliveryAddress(CustomerDeliveryAddressVo deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
	}
	
	
}
