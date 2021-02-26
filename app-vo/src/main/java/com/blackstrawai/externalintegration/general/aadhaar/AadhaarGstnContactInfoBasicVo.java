package com.blackstrawai.externalintegration.general.aadhaar;

public class AadhaarGstnContactInfoBasicVo {
private AadhaarGstnContactInfoAddressBasicVo contactInfoAddress;
private String natureOfBusnsCarriedAtAddrress;//ntr :Nature of business carried out at the address
public AadhaarGstnContactInfoAddressBasicVo getContactInfoAddress() {
	return contactInfoAddress;
}
public void setContactInfoAddress(AadhaarGstnContactInfoAddressBasicVo contactInfoAddress) {
	this.contactInfoAddress = contactInfoAddress;
}
public String getNatureOfBusnsCarriedAtAddrress() {
	return natureOfBusnsCarriedAtAddrress;
}
public void setNatureOfBusnsCarriedAtAddrress(String natureOfBusnsCarriedAtAddrress) {
	this.natureOfBusnsCarriedAtAddrress = natureOfBusnsCarriedAtAddrress;
}

}
