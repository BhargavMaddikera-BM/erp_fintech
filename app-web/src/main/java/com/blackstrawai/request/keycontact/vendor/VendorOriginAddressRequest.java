package com.blackstrawai.request.keycontact.vendor;

import com.blackstrawai.request.keycontact.customer.BaseAddressRequest;

public class VendorOriginAddressRequest extends BaseAddressRequest{

	private Boolean sameBillingDestAddress;
	
	public Boolean getSameBillingDestAddress() {
		return sameBillingDestAddress;
	}

	public void setSameBillingDestAddress(Boolean sameBillingDestAddress) {
		this.sameBillingDestAddress = sameBillingDestAddress;
	}


}
