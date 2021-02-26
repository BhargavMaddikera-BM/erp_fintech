package com.blackstrawai.keycontact.vendor;

import com.blackstrawai.common.BaseAddressVo;

public class VendorOriginAddressVo extends BaseAddressVo {

	private Boolean sameBillingDestAddress;

	public Boolean getSameBillingDestAddress() {
		return sameBillingDestAddress;
	}

	public void setSameBillingDestAddress(Boolean sameBillingDestAddress) {
		this.sameBillingDestAddress = sameBillingDestAddress;
	}

}
