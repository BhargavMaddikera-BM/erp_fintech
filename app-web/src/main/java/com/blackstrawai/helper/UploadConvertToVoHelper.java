package com.blackstrawai.helper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;

import com.blackstrawai.keycontact.customer.CustomerBillingAddressVo;
import com.blackstrawai.keycontact.customer.CustomerDeliveryAddressVo;
import com.blackstrawai.keycontact.customer.CustomerPrimaryInfoVo;
import com.blackstrawai.keycontact.customer.CustomerVo;
import com.blackstrawai.keycontact.employee.EmployeeGeneralInfoVo;
import com.blackstrawai.keycontact.employee.EmployeeVo;
import com.blackstrawai.keycontact.vendor.VendorContactVo;
import com.blackstrawai.keycontact.vendor.VendorDestinationAddressVo;
import com.blackstrawai.keycontact.vendor.VendorFinanceVo;
import com.blackstrawai.keycontact.vendor.VendorGeneralInformationVo;
import com.blackstrawai.keycontact.vendor.VendorOriginAddressVo;
import com.blackstrawai.keycontact.vendor.VendorVo;
import com.blackstrawai.request.payroll.PayrollUploadRequest;
import com.blackstrawai.request.upload.AccountingEntriesUploadRequest;
import com.blackstrawai.request.upload.BulkPaymentUploadRequest;
import com.blackstrawai.request.upload.ContraUploadFileRequest;
import com.blackstrawai.request.upload.CustomerUploadRequest;
import com.blackstrawai.request.upload.EmployeeUploadRequest;
import com.blackstrawai.request.upload.ImportFileRequest;
import com.blackstrawai.request.upload.VendorUploadRequest;
import com.blackstrawai.upload.AccountingEntriesUploadVo;
import com.blackstrawai.upload.ContraUploadVo;
import com.blackstrawai.upload.CustomerUploadVo;
import com.blackstrawai.upload.EmployeeUploadVo;
import com.blackstrawai.upload.PayrollUploadVo;
import com.blackstrawai.upload.UploadPayrollVo;
import com.blackstrawai.upload.VendorUploadVo;
import com.blackstrawai.upload.externalintegration.yesbank.BulkPaymentsUploadVo;

public class UploadConvertToVoHelper {
	
	private Logger logger = Logger.getLogger(UploadConvertToVoHelper.class);

	private static UploadConvertToVoHelper uploadConvertToVoHelper;

	public static UploadConvertToVoHelper getInstance() {
		if (uploadConvertToVoHelper == null) {
			uploadConvertToVoHelper = new UploadConvertToVoHelper();
		}
		return uploadConvertToVoHelper;
	}

	public CustomerVo convertCustomerImportFileVoFromCustomerImportFileRequest(CustomerUploadRequest customerRequest,
			Integer orgId, String userId, boolean superAdmin, String roleName) {
		CustomerVo customerVo = new CustomerVo();
		CustomerPrimaryInfoVo customerPrimaryInfo = new CustomerPrimaryInfoVo();
		CustomerBillingAddressVo customerBillingAddress = new CustomerBillingAddressVo();
		CustomerDeliveryAddressVo customerDeliveryAddress = new CustomerDeliveryAddressVo();
		customerPrimaryInfo.setCompanyName(customerRequest.getCompanyName());
		customerPrimaryInfo.setCustomerDisplayName(customerRequest.getDisplayName());
		customerPrimaryInfo.setEmailAddress(customerRequest.getEmail());
		customerPrimaryInfo.setMobileNo(customerRequest.getMobile());
		customerPrimaryInfo.setPrimaryContact(customerRequest.getPrimaryContact());
		customerBillingAddress.setAddress_1(customerRequest.getBillingAddressLine1());
		customerBillingAddress.setAttention(customerRequest.getBillingAddressAttention());
		customerBillingAddress.setCity(customerRequest.getBillingAddressCity());
		customerBillingAddress.setPhoneNo(customerRequest.getBillingAddressPhoneNo());
		customerBillingAddress.setZipCode(customerRequest.getBillingAddressPin());
		customerDeliveryAddress.setAddress_1(customerRequest.getDeliveryAddressLine1());
		customerDeliveryAddress.setAttention(customerRequest.getDeliveryAddressAttention());
		customerDeliveryAddress.setCity(customerRequest.getDeliveryAddressCity());
		customerDeliveryAddress.setPhoneNo(customerRequest.getDeliveryAddressPhoneNo());
		customerDeliveryAddress.setZipCode(customerRequest.getDeliveryAddressPin());
		customerVo.setId(null);
		customerVo.setPrimaryInfo(customerPrimaryInfo);
		customerVo.setBillingAddress(customerBillingAddress);
		customerVo.setDeliveryAddress(customerDeliveryAddress);
		customerVo.setAttachments(null);
		customerVo.setAttachmentsToRemove(null);
		customerVo.setBankDetails(null);
		customerVo.setContacts(null);
		customerVo.setOrganizationId(orgId);
		customerVo.setUserId(userId);
		customerVo.setIsSuperAdmin(superAdmin);
		customerVo.setRoleName(roleName);
		customerVo.setStatus("ACT");
		return customerVo;
	}

	public CustomerUploadVo convertCustomerUploadVoFromCustomerUploadRequest(
			CustomerUploadRequest customerUploadRequest) {
		CustomerUploadVo customerUploadVo = new CustomerUploadVo();
		customerUploadVo.setBillingAddressAttention(customerUploadRequest.getBillingAddressAttention());
		customerUploadVo.setBillingAddressCity(customerUploadRequest.getBillingAddressCity());
		customerUploadVo.setBillingAddressCountry(customerUploadRequest.getBillingAddressCountry());
		customerUploadVo.setBillingAddressLine1(customerUploadRequest.getBillingAddressLine1());
		customerUploadVo.setBillingAddressPhoneNo(customerUploadRequest.getBillingAddressPhoneNo());
		customerUploadVo.setBillingAddressPin(customerUploadRequest.getBillingAddressPin());
		customerUploadVo.setBillingAddressState(customerUploadRequest.getBillingAddressState());
		customerUploadVo.setCompanyName(customerUploadRequest.getCompanyName());
		customerUploadVo.setCurrency(customerUploadRequest.getCurrency());
		customerUploadVo.setDeliveryAddressAttention(customerUploadRequest.getDeliveryAddressAttention());
		customerUploadVo.setDeliveryAddressCity(customerUploadRequest.getDeliveryAddressCity());
		customerUploadVo.setDeliveryAddressCountry(customerUploadRequest.getDeliveryAddressCountry());
		customerUploadVo.setDeliveryAddressLine1(customerUploadRequest.getDeliveryAddressLine1());
		customerUploadVo.setDeliveryAddressPhoneNo(customerUploadRequest.getDeliveryAddressPhoneNo());
		customerUploadVo.setDeliveryAddressPin(customerUploadRequest.getDeliveryAddressPin());
		customerUploadVo.setDeliveryAddressState(customerUploadRequest.getDeliveryAddressState());
		customerUploadVo.setDisplayName(customerUploadRequest.getDisplayName());
		customerUploadVo.setEmail(customerUploadRequest.getEmail());
		customerUploadVo.setMobile(customerUploadRequest.getMobile());
		customerUploadVo.setPrimaryContact(customerUploadRequest.getPrimaryContact());
		return customerUploadVo;
	}

	public Map<String, List<AccountingEntriesUploadVo>> convertAccountingEntryImportFileVoFromAccountingEntryImportFileRequest(
			List<AccountingEntriesUploadRequest> listAccountingEntriesRequest) {

		List<AccountingEntriesUploadVo> AccountingEntriesList = new ArrayList<AccountingEntriesUploadVo>();
		for (AccountingEntriesUploadRequest accountingEntriesRequest : listAccountingEntriesRequest) {
			AccountingEntriesUploadVo accountingEntriesVo = new AccountingEntriesUploadVo();
			accountingEntriesVo.setDate(accountingEntriesRequest.getDate());
			accountingEntriesVo.setType(accountingEntriesRequest.getType());
			accountingEntriesVo.setJournalNo(accountingEntriesRequest.getJournalNo());
			accountingEntriesVo.setLocation(accountingEntriesRequest.getLocation());
			accountingEntriesVo.setLedger(accountingEntriesRequest.getLedger());
			accountingEntriesVo.setSubLedger(accountingEntriesRequest.getSubLedger());
			accountingEntriesVo.setCurrency(accountingEntriesRequest.getCurrency());
			accountingEntriesVo.setExchangeRate(accountingEntriesRequest.getExchangeRate());
			accountingEntriesVo.setCredits(accountingEntriesRequest.getCredits());
			accountingEntriesVo.setDebits(accountingEntriesRequest.getDebits());
			AccountingEntriesList.add(accountingEntriesVo);
		}

		Map<String, List<AccountingEntriesUploadVo>> accountingEntriesMap = AccountingEntriesList.stream()
				.collect(Collectors.groupingBy(AccountingEntriesUploadVo::getJournalNo, Collectors
						.mapping(AccountingEntriesUploadVo -> AccountingEntriesUploadVo, Collectors.toList())));
		return accountingEntriesMap;

	}

	public Map<String, List<ContraUploadVo>> convertContraImportFileVoFromContraImportFileRequest(
			List<ContraUploadFileRequest> contraRequestList) {

		List<ContraUploadVo> contraList = new ArrayList<ContraUploadVo>();
		for (ContraUploadFileRequest contraRequest : contraRequestList) {
			ContraUploadVo contraVo = new ContraUploadVo();
			contraVo.setAccountName(contraRequest.getAccountName());
			contraVo.setAccountype(contraRequest.getAccountype());
			contraVo.setCredit(contraRequest.getCredit());
			contraVo.setCurrency(contraRequest.getCurrency());
			contraVo.setDate(contraRequest.getDate());
			contraVo.setDebit(contraRequest.getDebit());
			contraVo.setExchangeRate(contraRequest.getExchangeRate());
			contraVo.setReferenceNo(contraRequest.getReferenceNo());
			contraList.add(contraVo);
		}

		Map<String, List<ContraUploadVo>> contraEntriesMap = contraList.stream()
				.collect(Collectors.groupingBy(ContraUploadVo::getReferenceNo,
						Collectors.mapping(ContraUploadVo -> ContraUploadVo, Collectors.toList())));
		return contraEntriesMap;

	}

	public  List<BulkPaymentsUploadVo> convertBulkpaymentImportFileVoFromBulkpaymentImportFileRequest(
			List<BulkPaymentUploadRequest> bulkpaymentRequestList) {

		List<BulkPaymentsUploadVo> bulkpaymentList = new ArrayList<BulkPaymentsUploadVo>();
		for (BulkPaymentUploadRequest bulkpaymentRequest : bulkpaymentRequestList) {
			BulkPaymentsUploadVo bulkpaymentVo = new BulkPaymentsUploadVo();
			bulkpaymentVo.setAmount(bulkpaymentRequest.getAmount());
			bulkpaymentVo.setBeneficiaryAccountNo(bulkpaymentRequest.getBeneficiaryAccountNo());
			bulkpaymentVo.setBeneficiaryName(bulkpaymentRequest.getBeneficiaryName());
			bulkpaymentVo.setIfsc(bulkpaymentRequest.getIfsc());
			bulkpaymentVo.setRemarks(bulkpaymentRequest.getRemarks());
			bulkpaymentVo.setTransactionType(bulkpaymentRequest.getTransactionType());
			bulkpaymentVo.setTransactionTypeId(bulkpaymentRequest.getTransactionTypeId());
			bulkpaymentList.add(bulkpaymentVo);
		}

		
		return bulkpaymentList;

	}
	
	public List<UploadPayrollVo> convertUploadPayrollVoFromPayrollRequest(List<PayrollUploadRequest> payroll,
			Integer orgId, String userId, String roleName) {
		logger.info("convertUploadPayrollVoFromPayrollRequest - start ");
		List<UploadPayrollVo> data = new ArrayList<UploadPayrollVo>();
		logger.info("convertUploadPayrollVoFromPayrollRequest - data " + payroll +
				"orgId "+orgId+"userId "+userId+"roleName "+roleName);
		
		for (int i = 0; i < payroll.size(); i++) {
			PayrollUploadRequest requestData = payroll.get(i);
			
			
			if (requestData.getEarningsBasic() != null) {
			
				UploadPayrollVo voData = new UploadPayrollVo();			
				voData.setEmployeeId(requestData.getEmployeeId());
				voData.setEmployeeName(requestData.getEmployeeName());
				voData.setOrganizationId(orgId);
				voData.setUserId(userId);
				voData.setRoleName(roleName);
				voData.setPayItemName("Basic");
				voData.setPayItemValue(requestData.getEarningsBasic());
				voData.setType("Debit");
				
				data.add(voData);
				
			}
			if (requestData.getEarningsHRA() != null) {
				UploadPayrollVo voData = new UploadPayrollVo();
				voData.setEmployeeId(requestData.getEmployeeId());
				voData.setEmployeeName(requestData.getEmployeeName());
				voData.setOrganizationId(orgId);
				voData.setUserId(userId);
				voData.setRoleName(roleName);
				voData.setPayItemName("HRA");
				voData.setPayItemValue(requestData.getEarningsHRA());
				voData.setType("Debit");
				data.add(voData);
			}
			if (requestData.getEarningsSpecialAllowance() != null) {
				UploadPayrollVo voData = new UploadPayrollVo();
				voData.setEmployeeId(requestData.getEmployeeId());
				voData.setEmployeeName(requestData.getEmployeeName());
				voData.setOrganizationId(orgId);
				voData.setUserId(userId);
				voData.setRoleName(roleName);
				voData.setPayItemName("Special Allowance");
				voData.setPayItemValue(requestData.getEarningsSpecialAllowance());
				voData.setType("Debit");
				data.add(voData);
			}
			if (requestData.getEarningsConveyance() != null) {
				UploadPayrollVo voData = new UploadPayrollVo();
				voData.setEmployeeId(requestData.getEmployeeId());
				voData.setEmployeeName(requestData.getEmployeeName());
				voData.setOrganizationId(orgId);
				voData.setUserId(userId);
				voData.setRoleName(roleName);
				voData.setPayItemName("Conveyance");
				voData.setPayItemValue(requestData.getEarningsConveyance());
				voData.setType("Debit");
				data.add(voData);
			}
			if (requestData.getDeductionsPfEmployerContributionDebit() != null) {
				UploadPayrollVo voData = new UploadPayrollVo();
				voData.setEmployeeId(requestData.getEmployeeId());
				voData.setEmployeeName(requestData.getEmployeeName());
				voData.setOrganizationId(orgId);
				voData.setUserId(userId);
				voData.setRoleName(roleName);
				voData.setPayItemName("PF-Employer Contribution");
				voData.setPayItemValue(requestData.getDeductionsPfEmployerContributionDebit());
				voData.setType("Debit");
				data.add(voData);
			}
			if (requestData.getDeductionsEsiEmployerContributionDebit() != null) {
				UploadPayrollVo voData = new UploadPayrollVo();
				voData.setEmployeeId(requestData.getEmployeeId());
				voData.setEmployeeName(requestData.getEmployeeName());
				voData.setOrganizationId(orgId);
				voData.setUserId(userId);
				voData.setRoleName(roleName);
				voData.setPayItemName("ESI-Employer Contribution");
				voData.setPayItemValue(requestData.getDeductionsEsiEmployerContributionDebit());
				voData.setType("Debit");
				data.add(voData);
			}
			if (requestData.getEarningsLeaveEncashment() != null) {
				UploadPayrollVo voData = new UploadPayrollVo();
				voData.setEmployeeId(requestData.getEmployeeId());
				voData.setEmployeeName(requestData.getEmployeeName());
				voData.setOrganizationId(orgId);
				voData.setUserId(userId);
				voData.setRoleName(roleName);
				voData.setPayItemName("Leave Encashment");
				voData.setPayItemValue(requestData.getEarningsLeaveEncashment());
				voData.setType("Debit");
				data.add(voData);
			}
			if (requestData.getEarningsBonus() != null) {
				UploadPayrollVo voData = new UploadPayrollVo();
				voData.setEmployeeId(requestData.getEmployeeId());
				voData.setEmployeeName(requestData.getEmployeeName());
				voData.setOrganizationId(orgId);
				voData.setUserId(userId);
				voData.setRoleName(roleName);
				voData.setPayItemName("Bonus");
				voData.setPayItemValue(requestData.getEarningsBonus());
				voData.setType("Debit");
				data.add(voData);
			}
			if (requestData.getEarningsOvertime() != null) {
				UploadPayrollVo voData = new UploadPayrollVo();
				voData.setEmployeeId(requestData.getEmployeeId());
				voData.setEmployeeName(requestData.getEmployeeName());
				voData.setOrganizationId(orgId);
				voData.setUserId(userId);
				voData.setRoleName(roleName);
				voData.setPayItemName("Overtime");
				voData.setPayItemValue(requestData.getEarningsOvertime());
				voData.setType("Debit");
				data.add(voData);
			}
			if (requestData.getDeductionsPfEmployeeContributionCredit() != null) {
				UploadPayrollVo voData = new UploadPayrollVo();
				voData.setEmployeeId(requestData.getEmployeeId());
				voData.setEmployeeName(requestData.getEmployeeName());
				voData.setOrganizationId(orgId);
				voData.setUserId(userId);
				voData.setRoleName(roleName);
				voData.setPayItemName("PF-Employee Contribution");
				voData.setPayItemValue(requestData.getDeductionsPfEmployeeContributionCredit());
				voData.setType("Credit");
				data.add(voData);
			}
			if (requestData.getDeductionsPfEmployerContributionCredit() != null) {
				UploadPayrollVo voData = new UploadPayrollVo();
				voData.setEmployeeId(requestData.getEmployeeId());
				voData.setEmployeeName(requestData.getEmployeeName());
				voData.setOrganizationId(orgId);
				voData.setUserId(userId);
				voData.setRoleName(roleName);
				voData.setPayItemName("PF-Employer Contribution");
				voData.setPayItemValue(requestData.getDeductionsPfEmployerContributionCredit());
				voData.setType("Credit");
				data.add(voData);
			}
			if (requestData.getDeductionsEsiEmployeeContributionCredit() != null) {
				UploadPayrollVo voData = new UploadPayrollVo();
				voData.setEmployeeId(requestData.getEmployeeId());
				voData.setEmployeeName(requestData.getEmployeeName());
				voData.setOrganizationId(orgId);
				voData.setUserId(userId);
				voData.setRoleName(roleName);
				voData.setPayItemName("ESI-Employee Contribution");
				voData.setPayItemValue(requestData.getDeductionsEsiEmployeeContributionCredit());
				voData.setType("Credit");
				data.add(voData);
			}
			if (requestData.getDeductionsEsiEmployerContributionCredit() != null) {
				UploadPayrollVo voData = new UploadPayrollVo();
				voData.setEmployeeId(requestData.getEmployeeId());
				voData.setEmployeeName(requestData.getEmployeeName());
				voData.setOrganizationId(orgId);
				voData.setUserId(userId);
				voData.setRoleName(roleName);
				voData.setPayItemName("ESI-Employer Contribution");
				voData.setPayItemValue(requestData.getDeductionsEsiEmployerContributionCredit());
				voData.setType("Credit");
				data.add(voData);
			}
			if (requestData.getDeductionsProfessionalTax() != null) {
				UploadPayrollVo voData = new UploadPayrollVo();
				voData.setEmployeeId(requestData.getEmployeeId());
				voData.setEmployeeName(requestData.getEmployeeName());
				voData.setOrganizationId(orgId);
				voData.setUserId(userId);
				voData.setRoleName(roleName);
				voData.setPayItemName("Professional Tax");
				voData.setPayItemValue(requestData.getDeductionsProfessionalTax());
				voData.setType("Credit");
				data.add(voData);
			}
			if (requestData.getDeductionsIncomeTax() != null) {
				UploadPayrollVo voData = new UploadPayrollVo();
				voData.setEmployeeId(requestData.getEmployeeId());
				voData.setEmployeeName(requestData.getEmployeeName());
				voData.setOrganizationId(orgId);
				voData.setUserId(userId);
				voData.setRoleName(roleName);
				voData.setPayItemName("Income Tax");
				voData.setPayItemValue(requestData.getDeductionsIncomeTax());
				voData.setType("Credit");
				data.add(voData);
			}
			if (requestData.getNetPayable() != null) {
				UploadPayrollVo voData = new UploadPayrollVo();
				voData.setEmployeeId(requestData.getEmployeeId());
				voData.setEmployeeName(requestData.getEmployeeName());
				voData.setOrganizationId(orgId);
				voData.setUserId(userId);
				voData.setRoleName(roleName);
				voData.setPayItemName("Salary payable");
				voData.setPayItemValue(requestData.getNetPayable());
				voData.setType("Credit");
				data.add(voData);
			}
		}
		logger.info("convertUploadPayrollVoFromPayrollRequest - data final " + data);
		return data;
	}

	public com.blackstrawai.upload.UploadFileVo convertImportFileVoFromImportFileRequest(
			ImportFileRequest importFileRequest) {
		com.blackstrawai.upload.UploadFileVo uploadFileVo = new com.blackstrawai.upload.UploadFileVo();
		uploadFileVo.setData(importFileRequest.getData());
		uploadFileVo.setModuleName(importFileRequest.getModuleName());
		uploadFileVo.setFileType(importFileRequest.getFileType());
		return uploadFileVo;
	}

	public VendorVo convertVendorImportFileVoFromVendorImportFileRequest(VendorUploadRequest vendorRequest,
			Integer orgId, String userId, boolean isSuperAdmin, String roleName) {
		VendorVo vendor = new VendorVo();
		VendorGeneralInformationVo vendorGeneralInformationVo = new VendorGeneralInformationVo();
		VendorOriginAddressVo vendorOriginAddressVo = new VendorOriginAddressVo();
		VendorDestinationAddressVo vendorDestinationAddressVo = new VendorDestinationAddressVo();
		VendorFinanceVo VendorFinanceVo = new VendorFinanceVo();
		vendorGeneralInformationVo.setId(null);
		vendorGeneralInformationVo.setCompanyName(vendorRequest.getCompanyName());
		vendorGeneralInformationVo.setPrimaryContact(vendorRequest.getPrimaryContact());
		vendorGeneralInformationVo.setVendorDisplayName(vendorRequest.getVendorDisplayName());
		vendorGeneralInformationVo.setEmail(vendorRequest.getEmail());
		vendorGeneralInformationVo.setIsPanOrGstAvailable(false);
		vendorGeneralInformationVo.setOverSeasVendor(false);
		vendorGeneralInformationVo.setVendorWithoutPan(true);
		vendorOriginAddressVo.setAttention(vendorRequest.getOriginAddressAttention());
		vendorOriginAddressVo.setAddress_1(vendorRequest.getOriginAddressLine1());
		vendorOriginAddressVo.setCity(vendorRequest.getOriginAddressCity());
		vendorOriginAddressVo.setPhoneNo(vendorRequest.getOriginAddressPhone());
		vendorOriginAddressVo.setZipCode(vendorRequest.getOriginAddressPin());
		vendorDestinationAddressVo.setAttention(vendorRequest.getBillingAddressAttention());
		vendorDestinationAddressVo.setAddress_1(vendorRequest.getBillingAddressLine1());
		vendorDestinationAddressVo.setCity(vendorRequest.getBillingAddressCity());
		vendorDestinationAddressVo.setPhoneNo(vendorRequest.getBillingAddressPhone());
		vendorDestinationAddressVo.setZipCode(vendorRequest.getBillingAddressPin());
		VendorFinanceVo.setTdsId(1);
		vendor.setOrganizationId(orgId);
		vendor.setIsSuperAdmin(isSuperAdmin);
		vendor.setUserId(userId);
		vendor.setAttachments(null);
		vendor.setAttachmentsToRemove(null);
		vendor.setBankDetails(null);
		VendorContactVo vendorContactVo = new VendorContactVo();
		vendorContactVo.setEmailAddress(vendorRequest.getEmail());
		vendorContactVo.setFirstName(vendorRequest.getVendorDisplayName());
		vendorContactVo.setMobileNo(vendorRequest.getOriginAddressPhone());
		vendor.setContacts(Collections.singletonList(vendorContactVo));
		vendor.setVendorGeneralInformation(vendorGeneralInformationVo);
		vendor.setVendorDestinationAddress(vendorDestinationAddressVo);
		vendor.setVendorOriginAddress(vendorOriginAddressVo);
		vendor.setVendorFinance(VendorFinanceVo);
		vendor.setRoleName(roleName);
		vendor.setStatus("ACT");
		return vendor;
	}

	public VendorUploadVo convertVendorUploadVoFromVendorUploadRequest(VendorUploadRequest vendorUploadRequest) {
		VendorUploadVo vendorUploadVo = new VendorUploadVo();
		vendorUploadVo.setBillingAddressAttention(vendorUploadRequest.getBillingAddressAttention());
		vendorUploadVo.setBillingAddressCity(vendorUploadRequest.getBillingAddressCity());
		vendorUploadVo.setBillingAddressCountry(vendorUploadRequest.getBillingAddressCountry());
		vendorUploadVo.setBillingAddressLine1(vendorUploadRequest.getBillingAddressLine1());
		vendorUploadVo.setBillingAddressPhone(vendorUploadRequest.getBillingAddressPhone());
		vendorUploadVo.setBillingAddressPin(vendorUploadRequest.getBillingAddressPin());
		vendorUploadVo.setBillingAddressState(vendorUploadRequest.getBillingAddressState());
		vendorUploadVo.setCompanyName(vendorUploadRequest.getCompanyName());
		vendorUploadVo.setCurrency(vendorUploadRequest.getCurrency());
		vendorUploadVo.setEmail(vendorUploadRequest.getEmail());
		vendorUploadVo.setOriginAddressAttention(vendorUploadRequest.getOriginAddressAttention());
		vendorUploadVo.setOriginAddressCity(vendorUploadRequest.getOriginAddressCity());
		vendorUploadVo.setOriginAddressCountry(vendorUploadRequest.getOriginAddressCountry());
		vendorUploadVo.setOriginAddressLine1(vendorUploadRequest.getOriginAddressLine1());
		vendorUploadVo.setOriginAddressPhone(vendorUploadRequest.getOriginAddressPhone());
		vendorUploadVo.setOriginAddressPin(vendorUploadRequest.getOriginAddressPin());
		vendorUploadVo.setOriginAddressState(vendorUploadRequest.getOriginAddressState());
		vendorUploadVo.setPaymentTerms(vendorUploadRequest.getPaymentTerms());
		vendorUploadVo.setPrimaryContact(vendorUploadRequest.getPrimaryContact());
		vendorUploadVo.setVendorDisplayName(vendorUploadRequest.getVendorDisplayName());
		vendorUploadVo.setTds(vendorUploadRequest.getTds());
		return vendorUploadVo;
	}

	public EmployeeVo convertEmployeeImportFileVoFromEmployeeImportFileRequest(EmployeeUploadRequest employeeRequest,
			Integer orgId, String userId, boolean superAdmin, String roleName) {
		EmployeeVo employee = new EmployeeVo();
		EmployeeGeneralInfoVo EmployeeGeneralInfo = new EmployeeGeneralInfoVo();
		EmployeeGeneralInfo.setEmployeeId(employeeRequest.getEmployeeId());
		EmployeeGeneralInfo.setEmployeeStatus(employeeRequest.getEmployeeStatus());
		EmployeeGeneralInfo.setFirstName(employeeRequest.getFirstName());
		EmployeeGeneralInfo.setLastName(employeeRequest.getLastName());
		EmployeeGeneralInfo.setMobileNo(employeeRequest.getMobileNo());
		employee.setEmployeeGeneralInfo(EmployeeGeneralInfo);
		employee.setUserId(userId);
		employee.setOrganizationId(orgId);
		employee.setIsSuperAdmin(superAdmin);
		employeeRequest.getUserId();
		employee.setAttachments(null);
		employee.setAttachmentsToRemove(null);
		employee.setBankDetails(null);
		employee.setId(null);
		employee.setRoleName(roleName);
		employee.setStatus("ACT");
		return employee;
	}

	public EmployeeUploadVo convertEmployeeUploadFromEmployeeUloadRequest(EmployeeUploadRequest employeeUploadRequest) {
		EmployeeUploadVo employeeUploadVo = new EmployeeUploadVo();
		employeeUploadVo.setDepartmentName(employeeUploadRequest.getDepartmentName());
		employeeUploadVo.setEmployeeId(employeeUploadRequest.getEmployeeId());
		employeeUploadVo.setEmployeeStatus(employeeUploadRequest.getEmployeeStatus());
		employeeUploadVo.setEmployeeType(employeeUploadRequest.getEmployeeType());
		employeeUploadVo.setFirstName(employeeUploadRequest.getFirstName());
		employeeUploadVo.setLastName(employeeUploadRequest.getLastName());
		employeeUploadVo.setMobileNo(employeeUploadRequest.getMobileNo());
		return employeeUploadVo;
	}

	public List<PayrollUploadVo> convertPayrollUploadVoFromPayrollRequest(List<PayrollUploadRequest> payrollRequest) {
		List<PayrollUploadVo> payrollUploadVos = new ArrayList<>();
		
		
		for(PayrollUploadRequest data : payrollRequest) {
			PayrollUploadVo payrollUploadVo = new PayrollUploadVo();
			payrollUploadVo.setEmployeeId(data.getEmployeeId());
			payrollUploadVo.setEmployeeName(data.getEmployeeName());
			payrollUploadVo.setEarningsBasic(data.getEarningsBasic());
			payrollUploadVo.setEarningsHRA(data.getEarningsHRA());
			payrollUploadVo.setEarningsSpecialAllowance(data.getEarningsSpecialAllowance());
			payrollUploadVo.setEarningsOvertime(data.getEarningsOvertime());
			payrollUploadVo.setEarningsConveyance(data.getEarningsConveyance());
			payrollUploadVo.setEarningsTravel(data.getEarningsTravel());
			payrollUploadVo.setEarningsBonus(data.getEarningsBonus());
			payrollUploadVo.setEarningsOvertime(data.getEarningsOvertime());
			payrollUploadVo.setEarningsLeaveEncashment(data.getEarningsLeaveEncashment());
			payrollUploadVo.setDeductionsIncomeTax(data.getDeductionsIncomeTax());
			payrollUploadVo.setDeductionsProfessionalTax(data.getDeductionsProfessionalTax());
			payrollUploadVo.setDeductionsEsiEmployeeContribution(data.getDeductionsEsiEmployeeContributionCredit());
			payrollUploadVo.setDeductionsPfEmployeeContribution(data.getDeductionsPfEmployeeContributionCredit());
			payrollUploadVo.setPaymentCycle(data.getPaymentCycle());
			payrollUploadVo.setPayPeriod(data.getPayPeriod());
			payrollUploadVos.add(payrollUploadVo);
		}
		return payrollUploadVos;
	}







}
