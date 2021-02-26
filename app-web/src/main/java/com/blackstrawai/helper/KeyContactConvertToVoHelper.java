package com.blackstrawai.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.blackstrawai.attachments.UploadFileVo;
import com.blackstrawai.common.BaseAddressVo;
import com.blackstrawai.common.BookKeepingSettingsVo;
import com.blackstrawai.keycontact.customer.CustomerBankDetailsVo;
import com.blackstrawai.keycontact.customer.CustomerBillingAddressVo;
import com.blackstrawai.keycontact.customer.CustomerContactsVo;
import com.blackstrawai.keycontact.customer.CustomerDeliveryAddressVo;
import com.blackstrawai.keycontact.customer.CustomerFinanceVo;
import com.blackstrawai.keycontact.customer.CustomerPrimaryInfoVo;
import com.blackstrawai.keycontact.customer.CustomerVo;
import com.blackstrawai.keycontact.employee.EmployeeGeneralInfoVo;
import com.blackstrawai.keycontact.employee.EmployeeVo;
import com.blackstrawai.keycontact.statutorybody.StatutoryBodyVo;
import com.blackstrawai.keycontact.vendor.VendorBankDetailsVo;
import com.blackstrawai.keycontact.vendor.VendorBasedOnGstVo;
import com.blackstrawai.keycontact.vendor.VendorBasicDetailsVo;
import com.blackstrawai.keycontact.vendor.VendorContactVo;
import com.blackstrawai.keycontact.vendor.VendorDestinationAddressVo;
import com.blackstrawai.keycontact.vendor.VendorFinanceVo;
import com.blackstrawai.keycontact.vendor.VendorGeneralInformationVo;
import com.blackstrawai.keycontact.vendor.VendorOriginAddressVo;
import com.blackstrawai.keycontact.vendor.VendorVo;
import com.blackstrawai.request.keycontact.customer.BaseAddressRequest;
import com.blackstrawai.request.keycontact.customer.BookKeepingSettingsRequest;
import com.blackstrawai.request.keycontact.customer.CustomerBankDetailsRequest;
import com.blackstrawai.request.keycontact.customer.CustomerBillingAddressRequest;
import com.blackstrawai.request.keycontact.customer.CustomerContactsRequest;
import com.blackstrawai.request.keycontact.customer.CustomerDeliveryAddressRequest;
import com.blackstrawai.request.keycontact.customer.CustomerFinanceRequest;
import com.blackstrawai.request.keycontact.customer.CustomerPrimaryInfoRequest;
import com.blackstrawai.request.keycontact.customer.CustomerRequest;
import com.blackstrawai.request.keycontact.employee.EmployeeRequest;
import com.blackstrawai.request.keycontact.statutorybody.StatutoryBodyRequest;
import com.blackstrawai.request.keycontact.vendor.VendorAddressBasedOnGstRequest;
import com.blackstrawai.request.keycontact.vendor.VendorBasedOnGstRequest;
import com.blackstrawai.request.keycontact.vendor.VendorDestinationAddressRequest;
import com.blackstrawai.request.keycontact.vendor.VendorOriginAddressRequest;
import com.blackstrawai.request.keycontact.vendor.VendorRequest;

public class KeyContactConvertToVoHelper {


	private static KeyContactConvertToVoHelper keyContactConvertToVoHelper;

	public static KeyContactConvertToVoHelper getInstance() {
		if (keyContactConvertToVoHelper == null) {
			keyContactConvertToVoHelper = new KeyContactConvertToVoHelper();
		}
		return keyContactConvertToVoHelper;
	}

	public CustomerVo convertCustVoFromCustRequest(CustomerRequest custRequest) {
		CustomerVo customerVo = new CustomerVo();
		customerVo.setRoleName(custRequest.getRoleName());
		List<CustomerContactsVo> contactList = new ArrayList<CustomerContactsVo>();
		List<CustomerBankDetailsVo> bankDetailsList = new ArrayList<CustomerBankDetailsVo>();
		List<BaseAddressVo> additionalAddresses = new ArrayList<BaseAddressVo>();
		List<UploadFileVo> uploadList = new ArrayList<UploadFileVo>();
		customerVo.setPrimaryInfo(custRequest.getPrimaryInfo()!=null?convertCustPrimaryInfoVoFromReq(custRequest.getPrimaryInfo()):null);
		customerVo.setBillingAddress(convertCustBillingAddressVoFromReq(custRequest.getBillingAddress()));
		customerVo.setDeliveryAddress(convertCustDeliveryAddressVoFromReq(custRequest.getDeliveryAddress()));
		if (custRequest.getContacts() != null && custRequest.getContacts().size() > 0) {
			custRequest.getContacts().forEach(custContact -> {
				contactList.add(convertCustContactVoFromReq(custContact));
			});
		}
		customerVo.setContacts(contactList);
		if (custRequest.getBankDetails() != null && custRequest.getBankDetails().size() > 0) {
			custRequest.getBankDetails().forEach(bankDetails -> {
				bankDetailsList.add(convertCustBankDetailsVoFromReq(bankDetails));
			});
		}
		if (custRequest.getAttachments() != null && custRequest.getAttachments().size() > 0) {
			custRequest.getAttachments().forEach(file -> {
				uploadList.add(ConvertToVoHelper.getInstance().convertAttachmentFromReq(file));
			});
		}
		if (custRequest.getCustomerAdditionalAddresses() != null && custRequest.getCustomerAdditionalAddresses().size() > 0) {
			custRequest.getCustomerAdditionalAddresses().forEach(address -> {
				additionalAddresses.add(convertCustomerAdditionalAddressesVoFromReq(address));
			});
		}
		
		
		customerVo.setCustomerAdditionalAddresses(additionalAddresses);
		customerVo.setBankDetails(bankDetailsList);
		customerVo.setUserId(custRequest.getUserId());
		customerVo.setAttachments(uploadList);
		customerVo.setOrganizationId(Integer.valueOf(custRequest.getOrganizationId()));
		customerVo.setIsSuperAdmin(custRequest.getIsSuperAdmin());
		customerVo.setId(custRequest.getId());
		
		if(custRequest.getStatus()!=null )
		{
				if(custRequest.getStatus().equals("ACT")|| custRequest.getStatus().equals("ACTIVE")){
					custRequest.setStatus("ACT");
				}
		}
		
		customerVo.setStatus(custRequest.getStatus());
		customerVo.setAttachmentsToRemove(custRequest.getAttachmentsToRemove());
		customerVo.setCustomerBookKeepingSetting(custRequest.getCustomerBookKeepingSetting()!=null?convertCustomerBookKeepingSettingVoFromReq(custRequest.getCustomerBookKeepingSetting()):null);
		customerVo.setCustomerFinance(custRequest.getCustomerFinance()!=null ?convertCustomerFinanceVoFromReq(custRequest.getCustomerFinance()):null);
		customerVo.setDefaultGlTallyId(custRequest.getDefaultGlTallyId());
		customerVo.setDefaultGlTallyName(custRequest.getDefaultGlTallyName());
		customerVo.setContactsToRemove(custRequest.getContactsToRemove());
		customerVo.setBankDetailsToRemove(custRequest.getBankDetailsToRemove());
		return customerVo;
	}
	
	private BaseAddressVo convertCustomerAdditionalAddressesVoFromReq(BaseAddressRequest request) {
		BaseAddressVo vo = new BaseAddressVo();
		vo.setAttention(request.getAttention());
		vo.setCountry(request.getCountry());
		vo.setAddress_1(request.getAddress_1());
		vo.setAddress_2(request.getAddress_2());
		vo.setState(request.getState());
		vo.setStateName(request.getStateName());
		vo.setCity(request.getCity());
		vo.setLandMark(request.getLandMark());
		vo.setZipCode(request.getZipCode());
		vo.setPhoneNo(request.getPhoneNo());
		vo.setId(request.getId());
		vo.setSelectedAddress(request.getSelectedAddress());
		vo.setGstNo(request.getGstNo());
		vo.setAddressType(request.getAddressType());
		
		return vo;

	}
	
	private BaseAddressVo convertVendorAdditionalAddressesVoFromReq(BaseAddressRequest request) {
		BaseAddressVo vo = new BaseAddressVo();
		vo.setAttention(request.getAttention());
		vo.setCountry(request.getCountry());
		vo.setAddress_1(request.getAddress_1());
		vo.setAddress_2(request.getAddress_2());
		vo.setState(request.getState());
		vo.setStateName(request.getStateName());
		vo.setCity(request.getCity());
		vo.setLandMark(request.getLandMark());
		vo.setZipCode(request.getZipCode());
		vo.setPhoneNo(request.getPhoneNo());
		vo.setId(request.getId());
		vo.setSelectedAddress(request.getSelectedAddress());
		vo.setGstNo(request.getGstNo());
		vo.setAddressType(request.getAddressType());
	
		
		return vo;

	}
	private BookKeepingSettingsVo convertCustomerBookKeepingSettingVoFromReq(BookKeepingSettingsRequest request) {
		BookKeepingSettingsVo vo = new BookKeepingSettingsVo();
		vo.setId(request.getId()>0?request.getId():0);
		vo.setDefaultGlCode(request.getDefaultGlCode());
		vo.setDefaultGlName(request.getDefaultGlName());
		vo.setGstNumber(request.getGstNumber());
		vo.setLocationId(request.getLocationId());
		return vo;

	}
	private BookKeepingSettingsVo convertVendorBookKeepingSettingVoFromReq(BookKeepingSettingsRequest request) {
		BookKeepingSettingsVo vo = new BookKeepingSettingsVo();
		vo.setId(request.getId()>0?request.getId():0);
		vo.setDefaultGlCode(request.getDefaultGlCode());
		vo.setDefaultGlName(request.getDefaultGlName());
		vo.setGstNumber(request.getGstNumber());
		vo.setLocationId(request.getLocationId());
		return vo;

	}
	private CustomerFinanceVo convertCustomerFinanceVoFromReq(CustomerFinanceRequest request) {
		CustomerFinanceVo vo = new CustomerFinanceVo();
		
		vo.setId(request.getId()!=null?request.getId():null);
		vo.setCurrencyId(request.getCurrencyId());
		vo.setPaymentTermsid(request.getPaymentTermsid());
		vo.setTdsId(request.getTdsId());
		vo.setOpeningBalance(request.getOpeningBalance());
		vo.setCreateTs(request.getCreateTs());
		vo.setUpdateTs(request.getUpdateTs());
		vo.setCustomerId(request.getCustomerId());
		return vo;

	}

	private CustomerPrimaryInfoVo convertCustPrimaryInfoVoFromReq(CustomerPrimaryInfoRequest primaryInfoRequest) {
		CustomerPrimaryInfoVo primaryInfoVo = new CustomerPrimaryInfoVo();
		primaryInfoVo.setPrimaryContact(primaryInfoRequest.getPrimaryContact());
		primaryInfoVo.setCompanyName(primaryInfoRequest.getCompanyName());
		primaryInfoVo.setCustomerDisplayName(primaryInfoRequest.getCustomerDisplayName());
		primaryInfoVo.setEmailAddress(primaryInfoRequest.getEmailAddress());
		primaryInfoVo.setPhoneNo(primaryInfoRequest.getPhoneNo());
		primaryInfoVo.setMobileNo(primaryInfoRequest.getMobileNo());
		primaryInfoVo.setWebsiteAddress(primaryInfoRequest.getWebsiteAddress());
		primaryInfoVo.setPanNo(primaryInfoRequest.getPanNo());
		primaryInfoVo.setCurrencyId(primaryInfoRequest.getCurrencyId());
		primaryInfoVo.setCustomerGroupId(primaryInfoRequest.getCustomerGroupId());
		primaryInfoVo.setGstNumber(primaryInfoRequest.getGstNumber());
		primaryInfoVo.setGstRegnTypeId(primaryInfoRequest.getGstRegnTypeId());
		primaryInfoVo.setOpeningBal(primaryInfoRequest.getOpeningBal());
		primaryInfoVo.setOrganisationTypeId(primaryInfoRequest.getOrganisationTypeId());
		primaryInfoVo.setPaymentTermsId(primaryInfoRequest.getPaymentTermsId());
		primaryInfoVo.setOtherGsts(primaryInfoRequest.getOtherGsts());
		return primaryInfoVo;

	}

	private CustomerBillingAddressVo convertCustBillingAddressVoFromReq(
			CustomerBillingAddressRequest billingAddressRequest) {
		CustomerBillingAddressVo billingAddressVo = new CustomerBillingAddressVo();
		billingAddressVo.setAddress_1(billingAddressRequest.getAddress_1());
		billingAddressVo.setAddress_2(billingAddressRequest.getAddress_2());
		billingAddressVo.setAttention(billingAddressRequest.getAttention());
		billingAddressVo.setCity(billingAddressRequest.getCity());
		billingAddressVo.setCountry(billingAddressRequest.getCountry());
		billingAddressVo.setLandMark(billingAddressRequest.getLandMark());
		billingAddressVo.setPhoneNo(billingAddressRequest.getPhoneNo());
		billingAddressVo.setState(billingAddressRequest.getState());
		billingAddressVo.setZipCode(billingAddressRequest.getZipCode());
		billingAddressVo.setSelectedAddress(billingAddressRequest.getSelectedAddress());
		billingAddressVo.setGstNo(billingAddressRequest.getGstNo());
		billingAddressVo.setAddressType(billingAddressRequest.getAddressType());
		billingAddressVo.setIsSameOriginDestAddress(billingAddressRequest.getIsSameOriginDestAddress());
		return billingAddressVo;

	}

	private CustomerDeliveryAddressVo convertCustDeliveryAddressVoFromReq(
			CustomerDeliveryAddressRequest deliveryAddressRequest) {
		CustomerDeliveryAddressVo deliveryAddressVo = new CustomerDeliveryAddressVo();
		deliveryAddressVo.setAddress_1(deliveryAddressRequest.getAddress_1());
		deliveryAddressVo.setAddress_2(deliveryAddressRequest.getAddress_2());
		deliveryAddressVo.setAttention(deliveryAddressRequest.getAttention());
		deliveryAddressVo.setCity(deliveryAddressRequest.getCity());
		deliveryAddressVo.setCountry(deliveryAddressRequest.getCountry());
		deliveryAddressVo.setLandMark(deliveryAddressRequest.getLandMark());
		deliveryAddressVo.setPhoneNo(deliveryAddressRequest.getPhoneNo());
		deliveryAddressVo.setState(deliveryAddressRequest.getState());
		deliveryAddressVo.setZipCode(deliveryAddressRequest.getZipCode());
		deliveryAddressVo.setSelectedAddress(deliveryAddressRequest.getSelectedAddress());
		deliveryAddressVo.setGstNo(deliveryAddressRequest.getGstNo());
		deliveryAddressVo.setAddressType(deliveryAddressRequest.getAddressType());
		return deliveryAddressVo;

	}

	private CustomerContactsVo convertCustContactVoFromReq(CustomerContactsRequest contactsRequest) {
		CustomerContactsVo contactsVo = new CustomerContactsVo();
		contactsVo.setId(contactsRequest.getId());
		contactsVo.setEmailAddress(contactsRequest.getEmailAddress());
		contactsVo.setFirstName(contactsRequest.getFirstName());
		contactsVo.setLastName(contactsRequest.getLastName());
		contactsVo.setMobileNo(contactsRequest.getMobileNo());
		contactsVo.setSalutation(contactsRequest.getSalutation());
		contactsVo.setStatus(contactsRequest.getStatus());
		contactsVo.setWorkNo(contactsRequest.getWorkNo());
		return contactsVo;
	}

	private CustomerBankDetailsVo convertCustBankDetailsVoFromReq(CustomerBankDetailsRequest bankDetailsRequest) {
		CustomerBankDetailsVo bankDetailsVo = new CustomerBankDetailsVo();
		bankDetailsVo.setId(bankDetailsRequest.getId());
		bankDetailsVo.setAccountHolderName(bankDetailsRequest.getAccountHolderName());
		bankDetailsVo.setAccountNumber(bankDetailsRequest.getAccountNumber());
		bankDetailsVo.setBankName(bankDetailsRequest.getBankName());
		bankDetailsVo.setBranchName(bankDetailsRequest.getBranchName());
		bankDetailsVo.setIsDefault(bankDetailsRequest.getIsDefault());
		bankDetailsVo.setIfscCode(bankDetailsRequest.getIfscCode());
		bankDetailsVo.setStatus(bankDetailsRequest.getStatus());
		bankDetailsVo.setUpiId(bankDetailsRequest.getUpiId());
		return bankDetailsVo;

	}

	public EmployeeVo convertEmployeeVoFromEmployeeRequest(EmployeeRequest employeeRequest) {
		EmployeeVo employeeVo = new EmployeeVo();
		employeeVo.setRoleName(employeeRequest.getRoleName());
		employeeVo.setAttachmentsToRemove(employeeRequest.getAttachmentsToRemove());
		List<UploadFileVo> uploadList = new ArrayList<UploadFileVo>();
		EmployeeGeneralInfoVo employeeGeneralInfoVo = new EmployeeGeneralInfoVo();
		employeeGeneralInfoVo.setDateOfJoining(employeeRequest.getEmployeeGeneralInfo().getDateOfJoining());
		employeeGeneralInfoVo.setDepartment(employeeRequest.getEmployeeGeneralInfo().getDepartment());
		employeeGeneralInfoVo.setEmail(employeeRequest.getEmployeeGeneralInfo().getEmail());
		employeeGeneralInfoVo.setEmployeeId(employeeRequest.getEmployeeGeneralInfo().getEmployeeId());
		employeeGeneralInfoVo.setEmployeeStatus(employeeRequest.getEmployeeGeneralInfo().getEmployeeStatus());
		employeeGeneralInfoVo.setEmployeeType(employeeRequest.getEmployeeGeneralInfo().getEmployeeType());
		employeeGeneralInfoVo.setFirstName(employeeRequest.getEmployeeGeneralInfo().getFirstName());
		employeeGeneralInfoVo.setLastName(employeeRequest.getEmployeeGeneralInfo().getLastName());
		employeeGeneralInfoVo.setMobileNo(employeeRequest.getEmployeeGeneralInfo().getMobileNo());
		employeeGeneralInfoVo.setReportingTo(employeeRequest.getEmployeeGeneralInfo().getReportingTo());
		employeeVo.setIsSuperAdmin(employeeRequest.getIsSuperAdmin());
		employeeVo.setOrganizationId(employeeRequest.getOrganizationId());
		employeeVo.setStatus(employeeRequest.getStatus());
		employeeVo.setUpdateTs(employeeRequest.getUpdateTs());
		employeeVo.setId(employeeRequest.getId());
		List<VendorBankDetailsVo> BankDetailsList = new ArrayList<VendorBankDetailsVo>();
		int vendorBankDetailsCount = employeeRequest.getBankDetails().size();
		for (int i = 0; i < vendorBankDetailsCount; i++) {
			VendorBankDetailsVo BankDetailsVo = new VendorBankDetailsVo();
			BankDetailsVo.setAccountNumber(employeeRequest.getBankDetails().get(i).getAccountNumber());
			BankDetailsVo.setAccountHolderName(employeeRequest.getBankDetails().get(i).getAccountHolderName());
			BankDetailsVo.setBankName(employeeRequest.getBankDetails().get(i).getBankName());
			BankDetailsVo.setBranchName(employeeRequest.getBankDetails().get(i).getBranchName());
			BankDetailsVo.setConfirmAccountNumber(employeeRequest.getBankDetails().get(i).getConfirmAccountNumber());
			BankDetailsVo.setIfscCode(employeeRequest.getBankDetails().get(i).getIfscCode());
			BankDetailsVo.setUpiId(employeeRequest.getBankDetails().get(i).getUpiId());
			BankDetailsVo.setIsDefault(employeeRequest.getBankDetails().get(i).getIsDefault());
			BankDetailsVo.setStatus(employeeRequest.getBankDetails().get(i).getStatus());
			BankDetailsVo.setId(employeeRequest.getBankDetails().get(i).getId());
			BankDetailsList.add(BankDetailsVo);
		}
		if(employeeRequest.getAttachments()!=null){
			employeeRequest.getAttachments().forEach(file -> {
				uploadList.add(ConvertToVoHelper.getInstance().convertAttachmentFromReq(file));
			});
		}
		
		employeeVo.setAttachments(uploadList);
		employeeVo.setBankDetails(BankDetailsList);
		employeeVo.setEmployeeGeneralInfo(employeeGeneralInfoVo);
		employeeVo.setUserId(employeeRequest.getUserId());
		employeeVo.setItemsToRemove(employeeRequest.getItemsToRemove());
		
		BookKeepingSettingsVo bookKeepingSettingsVo=new BookKeepingSettingsVo();
		bookKeepingSettingsVo.setId(employeeRequest.getBookKeepingSettings().getId());
		bookKeepingSettingsVo.setDefaultGlName(employeeRequest.getBookKeepingSettings().getDefaultGlName());
		employeeVo.setBookKeepingSettings(bookKeepingSettingsVo);
		
		return employeeVo;
	}

	public StatutoryBodyVo convertStatutoryBodyVoFromStatutoryBodyRequest(StatutoryBodyRequest statutoryBodyRequest)
			 {

		StatutoryBodyVo statutoryBodyVo = new StatutoryBodyVo();
		statutoryBodyVo.setName(statutoryBodyRequest.getName());
		statutoryBodyVo.setId(statutoryBodyRequest.getId());
		statutoryBodyVo.setDepartmentName(statutoryBodyRequest.getDepartmentName());
		statutoryBodyVo.setType(statutoryBodyRequest.getType());
		statutoryBodyVo.setRegistrationNo(statutoryBodyRequest.getRegistrationNo());
		statutoryBodyVo.setDate(statutoryBodyRequest.getDate());
		statutoryBodyVo.setState(statutoryBodyRequest.getState());
		statutoryBodyVo.setCity(statutoryBodyRequest.getCity());
		statutoryBodyVo.setAddr1(statutoryBodyRequest.getAddr1());
		statutoryBodyVo.setAddr2(statutoryBodyRequest.getAddr2());
		statutoryBodyVo.setPincode(statutoryBodyRequest.getPincode());
		statutoryBodyVo.setWebsite(statutoryBodyRequest.getWebsite());
		statutoryBodyVo.setStatus(statutoryBodyRequest.getStatus());
		statutoryBodyVo.setUserId(statutoryBodyRequest.getUserId());
		statutoryBodyVo.setOrganizationId(statutoryBodyRequest.getOrganizationId());
		statutoryBodyVo.setRoleName(statutoryBodyRequest.getRoleName());
		statutoryBodyVo.setUpdateUserId(statutoryBodyRequest.getUpdateUserId());
		statutoryBodyVo.setUpdateRoleName(statutoryBodyRequest.getUpdateRoleName());
		statutoryBodyVo.setStatutoryName(statutoryBodyRequest.getStatutoryName());
		if(statutoryBodyRequest.getBookKeepingSettings()!=null) {
		BookKeepingSettingsVo bookKeepingSettingsVo=new BookKeepingSettingsVo();
		bookKeepingSettingsVo.setId(statutoryBodyRequest.getBookKeepingSettings().getId());
		bookKeepingSettingsVo.setDefaultGlName(statutoryBodyRequest.getBookKeepingSettings().getDefaultGlName());
		statutoryBodyVo.setBookKeepingSettings(bookKeepingSettingsVo);
		}
		return statutoryBodyVo;
	}

	public VendorVo convertVendorVoFromVendorRequest(VendorRequest vendorRequest) {
		VendorVo vendorVo = new VendorVo();
		List<UploadFileVo> uploadList = new ArrayList<UploadFileVo>();
		vendorVo.setAttachmentsToRemove(vendorRequest.getAttachmentsToRemove());
		VendorGeneralInformationVo vendorGeneralInformationVo = new VendorGeneralInformationVo();
		vendorGeneralInformationVo.setOtherGsts(vendorRequest.getVendorGeneralInformation().getOtherGsts());
		vendorGeneralInformationVo.setPrimaryContact(vendorRequest.getVendorGeneralInformation().getPrimaryContact());
		vendorGeneralInformationVo.setCompanyName(vendorRequest.getVendorGeneralInformation().getCompanyName());
		vendorGeneralInformationVo.setEmail(vendorRequest.getVendorGeneralInformation().getEmail());
		vendorGeneralInformationVo.setMobileNo(vendorRequest.getVendorGeneralInformation().getMobileNo());
		vendorGeneralInformationVo.setPhoneNo(vendorRequest.getVendorGeneralInformation().getPhoneNo());
		vendorGeneralInformationVo.setVendorGroupId(vendorRequest.getVendorGeneralInformation().getVendorGroupId());
		vendorGeneralInformationVo
		.setVendorOrganizationId(vendorRequest.getVendorGeneralInformation().getVendorOrganizationId());
		vendorGeneralInformationVo.setVendorGstTypeId(vendorRequest.getVendorGeneralInformation().getVendorGstTypeId());
		vendorGeneralInformationVo.setGstNo(vendorRequest.getVendorGeneralInformation().getGstNo());
		vendorGeneralInformationVo.setPan(vendorRequest.getVendorGeneralInformation().getPan());
		vendorGeneralInformationVo
		.setVendorDisplayName(vendorRequest.getVendorGeneralInformation().getVendorDisplayName());
		vendorGeneralInformationVo.setWebsite(vendorRequest.getVendorGeneralInformation().getWebsite());
		vendorGeneralInformationVo.setId(vendorRequest.getVendorGeneralInformation().getId());
		vendorGeneralInformationVo
		.setIsMsmeRegistered(vendorRequest.getVendorGeneralInformation().getIsMsmeRegistered());
		vendorGeneralInformationVo.setMsmeNumber(vendorRequest.getVendorGeneralInformation().getMsmeNumber());
		vendorGeneralInformationVo.setIsPanOrGstAvailable(vendorRequest.getVendorGeneralInformation().getIsPanOrGstAvailable());
		vendorGeneralInformationVo.setVendorWithoutPan(vendorRequest.getVendorGeneralInformation().getVendorWithoutPan());
		vendorGeneralInformationVo.setOverSeasVendor(vendorRequest.getVendorGeneralInformation().getOverSeasVendor());
		vendorVo.setVendorGeneralInformation(vendorGeneralInformationVo);
		if (vendorRequest.getVendorFinance() != null) {
			VendorFinanceVo vendorFinanceVo = new VendorFinanceVo();
			vendorFinanceVo.setCurrencyId(vendorRequest.getVendorFinance().getCurrencyId());
			vendorFinanceVo.setOpeningBalance(vendorRequest.getVendorFinance().getOpeningBalance());
			vendorFinanceVo.setPaymentTermsid(vendorRequest.getVendorFinance().getPaymentTermsid());
			vendorFinanceVo.setSourceOfSupplyId(vendorRequest.getVendorFinance().getSourceOfSupplyId());
			vendorFinanceVo.setTdsId(vendorRequest.getVendorFinance().getTdsId());
			vendorFinanceVo.setId(vendorRequest.getVendorFinance().getId());
			vendorVo.setVendorFinance(vendorFinanceVo);
		}
		if (vendorRequest.getVendorOriginAddress() != null) {
			VendorOriginAddressVo vendorOriginAddressVo = convertToVendorOriginAddressVo(vendorRequest.getVendorOriginAddress());
			vendorVo.setVendorOriginAddress(vendorOriginAddressVo);
		}
		if (vendorRequest.getVendorDestinationAddress() != null) {
			VendorDestinationAddressVo vendorDestinationAddressVo = convertVendorDestinationAddrressToVendorDestinationAddrressVo(
					vendorRequest.getVendorDestinationAddress());
			vendorVo.setVendorDestinationAddress(vendorDestinationAddressVo);
		}
		
		if(vendorRequest.getOriginalAndDestinationAddressBasedOnGst() != null) {
			List<VendorBasedOnGstVo> vendorBasedOnGstVos = new ArrayList<>();
			for (VendorBasedOnGstRequest vendorBasedOnGstRequest : vendorRequest
					.getOriginalAndDestinationAddressBasedOnGst()) {
				
				
				VendorBasedOnGstVo vendorBasedOnGstVo = new VendorBasedOnGstVo();
				vendorBasedOnGstVo.setGstNo(vendorBasedOnGstRequest.getGstNo());
				
				vendorBasedOnGstVo.setVendorAdditionalAddresses(vendorBasedOnGstRequest.getVendorAdditionalAddresses()
						.stream().map(vendorAdditionalAddress -> convertVendorAdditionalAddressesVoFromReq(
								vendorAdditionalAddress))
						.collect(Collectors.toList()));
				
				vendorBasedOnGstVo.setVendorDestinationAddress(
						convertVendorDestinationAddrressToVendorDestinationAddrressVo(vendorBasedOnGstRequest.getVendorDestinationAddress()));
				
				vendorBasedOnGstVo.setVendorOriginAddress(convertToVendorOriginAddressVo(vendorBasedOnGstRequest.getVendorOriginAddress()));
				
				vendorBasedOnGstVos.add(vendorBasedOnGstVo);
				
				
			}
			vendorVo.setOriginalAndDestinationAddressBasedOnGst(vendorBasedOnGstVos);
		}
		List<VendorContactVo> vendorContactList = new ArrayList<>();
		if (vendorRequest.getContacts() != null) {
			int vendorContactCount = vendorRequest.getContacts().size();
			for (int i = 0; i < vendorContactCount; i++) {
				VendorContactVo vendorContactVo = new VendorContactVo();
				vendorContactVo.setEmailAddress(vendorRequest.getContacts().get(i).getEmailAddress());
				vendorContactVo.setFirstName(vendorRequest.getContacts().get(i).getFirstName());
				vendorContactVo.setLastName(vendorRequest.getContacts().get(i).getLastName());
				vendorContactVo.setMobileNo(vendorRequest.getContacts().get(i).getMobileNo());
				vendorContactVo.setSalutation(vendorRequest.getContacts().get(i).getSalutation());
				vendorContactVo.setWorkNo(vendorRequest.getContacts().get(i).getWorkNo());
				vendorContactVo.setStatus(vendorRequest.getContacts().get(i).getStatus());
				vendorContactVo.setId(vendorRequest.getContacts().get(i).getId());
				vendorContactVo.setIsDefault(vendorRequest.getContacts().get(i).getIsDefault());
				vendorContactList.add(vendorContactVo);
			}
			vendorVo.setContacts(vendorContactList);
		}
		if (vendorRequest.getBankDetails() != null) {
			List<VendorBankDetailsVo> vendorBankDetailsList = new ArrayList<>();
			int vendorBankDetailsCount = vendorRequest.getBankDetails().size();
			for (int i = 0; i < vendorBankDetailsCount; i++) {
				VendorBankDetailsVo vendorBankDetailsVo = new VendorBankDetailsVo();
				vendorBankDetailsVo.setAccountNumber(vendorRequest.getBankDetails().get(i).getAccountNumber());
				vendorBankDetailsVo.setAccountHolderName(vendorRequest.getBankDetails().get(i).getAccountHolderName());
				vendorBankDetailsVo.setBankName(vendorRequest.getBankDetails().get(i).getBankName());
				vendorBankDetailsVo.setBranchName(vendorRequest.getBankDetails().get(i).getBranchName());
				vendorBankDetailsVo
						.setConfirmAccountNumber(vendorRequest.getBankDetails().get(i).getConfirmAccountNumber());
				vendorBankDetailsVo.setIfscCode(vendorRequest.getBankDetails().get(i).getIfscCode());
				vendorBankDetailsVo.setUpiId(vendorRequest.getBankDetails().get(i).getUpiId());
				vendorBankDetailsVo.setIsDefault(vendorRequest.getBankDetails().get(i).getIsDefault());
				vendorBankDetailsVo.setStatus(vendorRequest.getBankDetails().get(i).getStatus());
				vendorBankDetailsVo.setId(vendorRequest.getBankDetails().get(i).getId());
				vendorBankDetailsList.add(vendorBankDetailsVo);
			}
			vendorVo.setBankDetails(vendorBankDetailsList);
		}
		if (vendorRequest.getAttachments() != null && vendorRequest.getAttachments().size() > 0) {
			vendorRequest.getAttachments().forEach(file -> {
				uploadList.add(ConvertToVoHelper.getInstance().convertAttachmentFromReq(file));
			});
		}
		List<BaseAddressVo>  additionalAddresses= new ArrayList<>();
		if (vendorRequest.getVendorAdditionalAddresses() != null && vendorRequest.getVendorAdditionalAddresses().size() > 0) {
			vendorRequest.getVendorAdditionalAddresses().forEach(address -> {
				additionalAddresses.add(convertVendorAdditionalAddressesVoFromReq(address));
			});
		}
		vendorVo.setVendorAdditionalAddresses(additionalAddresses);
		vendorVo.setAttachments(uploadList);
		
		vendorVo.setIsSuperAdmin(vendorRequest.getIsSuperAdmin());
		if(vendorRequest.getStatus()!=null )
		{
				if(vendorRequest.getStatus().equals("ACT")|| vendorRequest.getStatus().equals("ACTIVE")){
					vendorRequest.setStatus("ACT");
				}
		}
		vendorVo.setStatus(vendorRequest.getStatus());
		vendorVo.setOrganizationId(vendorRequest.getOrganizationId());
		vendorVo.setVendorId(vendorRequest.getVendorId());
		vendorVo.setUserId(vendorRequest.getUserId());
		vendorVo.setIsVendorOnBoarding(vendorRequest.getIsVendorOnboarding());
		vendorVo.setRoleName(vendorRequest.getRoleName());
		vendorVo.setVendorBookKeepingSetting(vendorRequest.getVendorBookKeepingSetting()!=null?convertVendorBookKeepingSettingVoFromReq(vendorRequest.getVendorBookKeepingSetting()):null);
		vendorVo.setDefaultGlTallyId(vendorRequest.getDefaultGlTallyId());
		vendorVo.setDefaultGlTallyName(vendorRequest.getDefaultGlTallyName());
		vendorVo.setContactsToRemove(vendorRequest.getContactsToRemove());
		vendorVo.setBankDetailsToRemove(vendorRequest.getBankDetailsToRemove());
		
		return vendorVo;
	}

	/**
	 * @param vendorRequest
	 * @return
	 */
	private VendorOriginAddressVo convertToVendorOriginAddressVo(VendorOriginAddressRequest originAddressRequest) {
		if (originAddressRequest != null) {
			VendorOriginAddressVo vendorOriginAddressVo = new VendorOriginAddressVo();
			vendorOriginAddressVo.setAddress_1(originAddressRequest.getAddress_1());
			vendorOriginAddressVo.setAddress_2(originAddressRequest.getAddress_2());
			vendorOriginAddressVo.setAttention(originAddressRequest.getAttention());
			vendorOriginAddressVo.setCity(originAddressRequest.getCity());
			vendorOriginAddressVo.setCountry(originAddressRequest.getCountry());
			vendorOriginAddressVo.setLandMark(originAddressRequest.getLandMark());
			vendorOriginAddressVo.setPhoneNo(originAddressRequest.getPhoneNo());
			vendorOriginAddressVo.setState(originAddressRequest.getState());
			vendorOriginAddressVo.setSelectedAddress(originAddressRequest.getSelectedAddress());
			vendorOriginAddressVo.setGstNo(originAddressRequest.getGstNo());
			vendorOriginAddressVo.setAddressType(originAddressRequest.getAddressType());
			vendorOriginAddressVo.setSameBillingDestAddress((originAddressRequest.getSameBillingDestAddress()));
			vendorOriginAddressVo.setZipCode(originAddressRequest.getZipCode());
			vendorOriginAddressVo.setId(originAddressRequest.getId());
			return vendorOriginAddressVo;
		}
		return null;
	}

	/**
	 * @param vendorRequest
	 * @return
	 */
	private VendorDestinationAddressVo convertVendorDestinationAddrressToVendorDestinationAddrressVo(
			VendorDestinationAddressRequest destinationAddressRequest) {
		if(destinationAddressRequest != null) {
		VendorDestinationAddressVo vendorDestinationAddressVo = new VendorDestinationAddressVo();
		vendorDestinationAddressVo.setAddress_1(destinationAddressRequest.getAddress_1());
		vendorDestinationAddressVo.setAddress_2(destinationAddressRequest.getAddress_2());
		vendorDestinationAddressVo.setAttention(destinationAddressRequest.getAttention());
		vendorDestinationAddressVo.setCity(destinationAddressRequest.getCity());
		vendorDestinationAddressVo.setCountry(destinationAddressRequest.getCountry());
		vendorDestinationAddressVo.setLandMark(destinationAddressRequest.getLandMark());
		vendorDestinationAddressVo.setPhoneNo(destinationAddressRequest.getPhoneNo());
		vendorDestinationAddressVo.setState(destinationAddressRequest.getState());
		vendorDestinationAddressVo.setZipCode(destinationAddressRequest.getZipCode());
		vendorDestinationAddressVo.setId(destinationAddressRequest.getId());
		vendorDestinationAddressVo
				.setSelectedAddress(destinationAddressRequest.getSelectedAddress());
		vendorDestinationAddressVo.setGstNo(destinationAddressRequest.getGstNo());
		vendorDestinationAddressVo.setAddressType(destinationAddressRequest.getAddressType());
		return vendorDestinationAddressVo;
		}
			return null;
		
	}
	
	public VendorBasedOnGstVo convertVendorAddressBasedOnGstAndVendorId(VendorAddressBasedOnGstRequest addressBasedOnGstRequest) {
		VendorBasedOnGstVo addresVo = new VendorBasedOnGstVo();
		addresVo.setVendorOriginAddress(convertToVendorOriginAddressVo(addressBasedOnGstRequest.getVendorOriginAddress()));
		addresVo.setVendorDestinationAddress(convertVendorDestinationAddrressToVendorDestinationAddrressVo(addressBasedOnGstRequest.getVendorDestinationAddress()));
		addresVo.setGstNo(addressBasedOnGstRequest.getGstNo());
		return addresVo;
	}

	public List<VendorBasicDetailsVo> convertVendorListToVendorBasicDetails(List<VendorVo> vendorList) {
		List<VendorBasicDetailsVo> vendorBasicDetailsList = new ArrayList<VendorBasicDetailsVo>();
		for (int i = 0; i < vendorList.size(); i++) {
			VendorBasicDetailsVo VendorBasicDetailsVo = new VendorBasicDetailsVo();
			VendorBasicDetailsVo.setMobileNumber(vendorList.get(i).getVendorGeneralInformation().getMobileNo());
			VendorBasicDetailsVo.setNoOfOpenPo(vendorList.get(i).getVendorGeneralInformation().getPoCount());
			VendorBasicDetailsVo.setPhoneNumber(vendorList.get(i).getVendorGeneralInformation().getPhoneNo());
			VendorBasicDetailsVo.setPrimaryContact(vendorList.get(i).getVendorGeneralInformation().getPrimaryContact());
			VendorBasicDetailsVo
			.setVendorDisplayName(vendorList.get(i).getVendorGeneralInformation().getVendorDisplayName());
			VendorBasicDetailsVo.setVendorId(vendorList.get(i).getVendorGeneralInformation().getId());
			VendorBasicDetailsVo.setStatus(vendorList.get(i).getStatus());
			vendorBasicDetailsList.add(VendorBasicDetailsVo);
		}
		return vendorBasicDetailsList;
	}

}
