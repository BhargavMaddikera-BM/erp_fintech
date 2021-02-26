package com.blackstrawai.request.keycontact.vendor;

import java.util.List;

import com.blackstrawai.request.keycontact.customer.BaseAddressRequest;

public class VendorBasedOnGstRequest {

	private String gstNo;
	private VendorOriginAddressRequest vendorOriginAddress;
	private VendorDestinationAddressRequest vendorDestinationAddress;
	private List<BaseAddressRequest> vendorAdditionalAddresses;

	public String getGstNo() {
		return gstNo;
	}

	public VendorOriginAddressRequest getVendorOriginAddress() {
		return vendorOriginAddress;
	}

	public VendorDestinationAddressRequest getVendorDestinationAddress() {
		return vendorDestinationAddress;
	}

	public List<BaseAddressRequest> getVendorAdditionalAddresses() {
		return vendorAdditionalAddresses;
	}

	public void setGstNo(String gstNo) {
		this.gstNo = gstNo;
	}

	public void setVendorOriginAddress(VendorOriginAddressRequest vendorOriginAddress) {
		this.vendorOriginAddress = vendorOriginAddress;
	}

	public void setVendorDestinationAddress(VendorDestinationAddressRequest vendorDestinationAddress) {
		this.vendorDestinationAddress = vendorDestinationAddress;
	}

	public void setVendorAdditionalAddresses(List<BaseAddressRequest> vendorAdditionalAddresses) {
		this.vendorAdditionalAddresses = vendorAdditionalAddresses;
	}

	@Override
	public String toString() {
		return "VendorBasedOnGstRequest [gstNo=" + gstNo + ", vendorOriginAddress=" + vendorOriginAddress
				+ ", vendorDestinationAddress=" + vendorDestinationAddress + ", vendorAdditionalAddresses="
				+ vendorAdditionalAddresses + "]";
	}

}
