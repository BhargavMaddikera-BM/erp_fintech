package com.blackstrawai.keycontact.vendor;

import java.util.List;

import com.blackstrawai.common.BaseAddressVo;

public class VendorBasedOnGstVo {

	private String gstNo;
	private VendorOriginAddressVo vendorOriginAddress;
	private VendorDestinationAddressVo vendorDestinationAddress;
	private List<BaseAddressVo> vendorAdditionalAddresses;

	public String getGstNo() {
		return gstNo;
	}

	public VendorOriginAddressVo getVendorOriginAddress() {
		return vendorOriginAddress;
	}

	public VendorDestinationAddressVo getVendorDestinationAddress() {
		return vendorDestinationAddress;
	}

	public List<BaseAddressVo> getVendorAdditionalAddresses() {
		return vendorAdditionalAddresses;
	}

	public void setGstNo(String gstNo) {
		this.gstNo = gstNo;
	}

	public void setVendorOriginAddress(VendorOriginAddressVo vendorOriginAddress) {
		this.vendorOriginAddress = vendorOriginAddress;
	}

	public void setVendorDestinationAddress(VendorDestinationAddressVo vendorDestinationAddress) {
		this.vendorDestinationAddress = vendorDestinationAddress;
	}

	public void setVendorAdditionalAddresses(List<BaseAddressVo> vendorAdditionalAddresses) {
		this.vendorAdditionalAddresses = vendorAdditionalAddresses;
	}

}
