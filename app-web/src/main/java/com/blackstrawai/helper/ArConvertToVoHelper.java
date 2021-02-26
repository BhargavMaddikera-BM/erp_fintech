package com.blackstrawai.helper;

import java.util.ArrayList;
import java.util.List;

import com.blackstrawai.ar.applycredits.ApplyCreditDetailsVo;
import com.blackstrawai.ar.applycredits.ApplyCreditsInvoiceVo;
import com.blackstrawai.ar.applycredits.ApplyCreditsVo;
import com.blackstrawai.ar.creditnotes.CreditNotesVo;
import com.blackstrawai.ar.invoice.ArInvoiceFilterVo;
import com.blackstrawai.ar.invoice.ArInvoiceGeneralInformationVo;
import com.blackstrawai.ar.invoice.ArInvoiceProductVo;
import com.blackstrawai.ar.invoice.ArInvoiceTaxDetailsVo;
import com.blackstrawai.ar.invoice.ArInvoiceTaxDistributionVo;
import com.blackstrawai.ar.invoice.ArInvoiceVo;
import com.blackstrawai.ar.lut.LetterOfUndertakingVo;
import com.blackstrawai.ar.receipt.ReceiptVo;
import com.blackstrawai.attachments.UploadFileVo;
import com.blackstrawai.keycontact.customer.CustomerBillingAddressVo;
import com.blackstrawai.keycontact.customer.CustomerDeliveryAddressVo;
import com.blackstrawai.request.ar.applycredits.ApplyCreditDetailsRequest;
import com.blackstrawai.request.ar.applycredits.ApplyCreditsInvoiceRequest;
import com.blackstrawai.request.ar.applycredits.ApplyCreditsRequest;
import com.blackstrawai.request.ar.creditnotes.CreditNotesRequest;
import com.blackstrawai.request.ar.invoice.ArInvoiceFilterRequest;
import com.blackstrawai.request.ar.invoice.ArInvoiceGeneralInformationRequest;
import com.blackstrawai.request.ar.invoice.ArInvoiceProductRequest;
import com.blackstrawai.request.ar.invoice.ArInvoiceRequest;
import com.blackstrawai.request.ar.invoice.ArInvoiceTaxDetailsRequest;
import com.blackstrawai.request.ar.invoice.ArInvoiceTaxDistributionRequest;
import com.blackstrawai.request.ar.lut.LetterOfUndertakingRequest;
import com.blackstrawai.request.ar.receipt.ReceiptRequest;
import com.blackstrawai.request.keycontact.customer.CustomerBillingAddressRequest;
import com.blackstrawai.request.keycontact.customer.CustomerDeliveryAddressRequest;

public class ArConvertToVoHelper {

	private static ArConvertToVoHelper arConvertToVoHelper;

	public static ArConvertToVoHelper getInstance() {
		if (arConvertToVoHelper == null) {
			arConvertToVoHelper = new ArConvertToVoHelper();
		}
		return arConvertToVoHelper;
	}

	public ApplyCreditsVo convertApplyCreditsVoFromApplyCreditsRequest(ApplyCreditsRequest applyCreditsRequest) {
		ApplyCreditsVo applyCreditsVo = new ApplyCreditsVo();
		List<ApplyCreditsInvoiceVo> listInvoice = new ArrayList<ApplyCreditsInvoiceVo>();
		List<ApplyCreditDetailsVo> creditDetails = new ArrayList<ApplyCreditDetailsVo>();
		List<UploadFileVo> uploadList = new ArrayList<UploadFileVo>();
		applyCreditsVo.setAdjustedCreditAmount(applyCreditsRequest.getAdjustedCreditAmount());
		applyCreditsVo.setAvailableFund(applyCreditsRequest.getAvailableFund());
		applyCreditsVo.setCurrencyId(applyCreditsRequest.getCurrencyId());
		applyCreditsVo.setLedgerId(applyCreditsRequest.getLedgerId());
		applyCreditsVo.setCustomerId(applyCreditsRequest.getCustomerId());
		applyCreditsVo.setDate(applyCreditsRequest.getDate());
		applyCreditsVo.setId(applyCreditsRequest.getId());
		applyCreditsVo.setInvoiceTotalAmount(applyCreditsRequest.getInvoiceTotalAmount());
		applyCreditsVo.setOrganizationId(applyCreditsRequest.getOrganizationId());
		applyCreditsVo.setStatus(applyCreditsRequest.getStatus());
		applyCreditsVo.setVoucherNo(applyCreditsRequest.getVoucherNo());
		applyCreditsVo.setIsSuperAdmin(applyCreditsRequest.getIsSuperAdmin());
		applyCreditsVo.setUserId(applyCreditsRequest.getUserId());
		applyCreditsVo.setLedgerName(applyCreditsRequest.getLedgerName());
		applyCreditsVo.setCustomTableList(applyCreditsRequest.getCustomTableList());
		applyCreditsVo.setRoleName(applyCreditsRequest.getRoleName());
		applyCreditsVo.setInvoiceItemsToRemove(applyCreditsRequest.getInvoiceItemsToRemove());
		applyCreditsVo.setCreditsItemsToRemove(applyCreditsRequest.getCreditsItemsToRemove());
		applyCreditsVo.setExchangeRate(applyCreditsRequest.getExchangeRate());
		for (ApplyCreditDetailsRequest applyCreditDetailsRequest : applyCreditsRequest.getCreditDetails()) {
			ApplyCreditDetailsVo applyCreditDetailsVo = new ApplyCreditDetailsVo();
			applyCreditDetailsVo.setAdjustmentAmount(applyCreditDetailsRequest.getAdjustmentAmount());
			applyCreditDetailsVo.setAvailableAmount(applyCreditDetailsRequest.getAvailableAmount());
			applyCreditDetailsVo.setId(applyCreditDetailsRequest.getId());
			applyCreditDetailsVo.setOriginalAmount(applyCreditDetailsRequest.getOriginalAmount());
			applyCreditDetailsVo.setReferenceId(applyCreditDetailsRequest.getReferenceId());
			applyCreditDetailsVo.setStatus(applyCreditDetailsRequest.getStatus());
			applyCreditDetailsVo.setCreditType(applyCreditDetailsRequest.getCreditType());
			applyCreditDetailsVo.setLedgerName(applyCreditDetailsRequest.getLedgerName());
			applyCreditDetailsVo.setLedgerId(applyCreditDetailsRequest.getLedgerId());
			creditDetails.add(applyCreditDetailsVo);
		}
		for (ApplyCreditsInvoiceRequest applyCreditsInvoiceRequest : applyCreditsRequest.getInvoiceDetails()) {
			ApplyCreditsInvoiceVo applyCreditsInvoiceVo = new ApplyCreditsInvoiceVo();
			applyCreditsInvoiceVo.setAppliedAmount(applyCreditsInvoiceRequest.getAppliedAmount());
			applyCreditsInvoiceVo.setId(applyCreditsInvoiceRequest.getId());
			applyCreditsInvoiceVo.setInvoiceAmount(applyCreditsInvoiceRequest.getInvoiceAmount());
			applyCreditsInvoiceVo.setInvoiceId(applyCreditsInvoiceRequest.getInvoiceId());
			applyCreditsInvoiceVo.setStatus(applyCreditsInvoiceRequest.getStatus());
			applyCreditsInvoiceVo.setBankCharges(applyCreditsInvoiceRequest.getBankCharges());
			applyCreditsInvoiceVo.setTdsDeducted(applyCreditsInvoiceRequest.getTdsDeducted());
			applyCreditsInvoiceVo.setOthers1(applyCreditsInvoiceRequest.getOthers1());
			applyCreditsInvoiceVo.setOthers2(applyCreditsInvoiceRequest.getOthers2());
			applyCreditsInvoiceVo.setOthers3(applyCreditsInvoiceRequest.getOthers3());
			listInvoice.add(applyCreditsInvoiceVo);
		}
		if (applyCreditsRequest.getAttachments() != null && applyCreditsRequest.getAttachments().size() > 0) {
			applyCreditsRequest.getAttachments().forEach(file -> {
				uploadList.add(ConvertToVoHelper.getInstance().convertAttachmentFromReq(file));
			});
		}
		applyCreditsVo.setCreditDetails(creditDetails);
		applyCreditsVo.setInvoiceDetails(listInvoice);
		applyCreditsVo.setAttachments(uploadList);
		applyCreditsVo.setAttachmentsToRemove(applyCreditsRequest.getAttachmentsToRemove());
		return applyCreditsVo;
	}


	public CreditNotesVo convertCreditNotesVoFromCreditNotesRequest(CreditNotesRequest creditNotesRequest) {

		CreditNotesVo creditNotesVo = new CreditNotesVo();
		creditNotesVo.setId(creditNotesRequest.getId());
		creditNotesVo.setCustomerId(creditNotesRequest.getCustomerId());
		creditNotesVo.setOriginalInvoiceId(creditNotesRequest.getOriginalInvoiceId());
		creditNotesVo.setCreditNoteTypeId(creditNotesRequest.getCreditNoteTypeId());
		creditNotesVo.setCreditNoteNumber(creditNotesRequest.getCreditNoteNumber());
		creditNotesVo.setCreditNoteNumberPrefix(creditNotesRequest.getCreditNoteNumberPrefix());
		creditNotesVo.setCreditNoteNumberSuffix(creditNotesRequest.getCreditNoteNumberSuffix());
		creditNotesVo.setCreditNoteDate(creditNotesRequest.getCreditNoteDate());
		creditNotesVo.setReason(creditNotesRequest.getReason());
		creditNotesVo.setReasonId(creditNotesRequest.getReasonId());
		creditNotesVo.setExchangeRate(creditNotesRequest.getExchangeRate());
		creditNotesVo.setTnc(creditNotesRequest.getTnc());
		creditNotesVo.setNote(creditNotesRequest.getNote());
		creditNotesVo.setSubTotal(creditNotesRequest.getSubTotal());
		creditNotesVo.setTdsId(creditNotesRequest.getTdsId());
		creditNotesVo.setTdsValue(creditNotesRequest.getTdsValue());
		creditNotesVo.setDiscLedgerId(creditNotesRequest.getDiscLedgerId());
		creditNotesVo.setDiscLedgerName(creditNotesRequest.getDiscLedgerName());
		creditNotesVo.setDiscountValue(creditNotesRequest.getDiscountValue());
		creditNotesVo.setDiscountAccountLevel(creditNotesRequest.getDiscountAccountLevel());
		creditNotesVo.setAttachments(creditNotesRequest.getAttachments());
		creditNotesVo.setAttachmentsToRemove(creditNotesRequest.getAttachmentsToRemove());
		creditNotesVo.setAdjValue(creditNotesRequest.getAdjValue());
		creditNotesVo.setAdjLedgerId(creditNotesRequest.getAdjLedgerId());
		creditNotesVo.setAdjLedgerName(creditNotesRequest.getAdjLedgerName());
		creditNotesVo.setAdjAccountLevel(creditNotesRequest.getAdjAccountLevel());
		creditNotesVo.setProducts(creditNotesRequest.getProducts());
		creditNotesVo.setGroupedTax(creditNotesRequest.getGroupedTax());
		creditNotesVo.setTotal(creditNotesRequest.getTotal());
		creditNotesVo.setItemsToRemove(creditNotesRequest.getItemsToRemove());
		creditNotesVo.setOrganizationId(creditNotesRequest.getOrganizationId());
		creditNotesVo.setRoleName(creditNotesRequest.getRoleName());
		creditNotesVo.setUserId(creditNotesRequest.getUserId());
		creditNotesVo.setIsSuperAdmin(creditNotesRequest.getIsSuperAdmin());
		creditNotesVo.setStatus(creditNotesRequest.getStatus());
		creditNotesVo.setCreateTs(creditNotesRequest.getCreateTs());
		creditNotesVo.setUpdateTs(creditNotesRequest.getUpdateTs());
		creditNotesVo.setGeneralLedgerData(creditNotesRequest.getGeneralLedgerData());
		return creditNotesVo;
	}

	public ArInvoiceFilterVo convertArInvoiceFilterVoFromRequest(ArInvoiceFilterRequest filterRequest) {
		ArInvoiceFilterVo filterVo = new ArInvoiceFilterVo();
		filterVo.setOrgId(filterRequest.getOrgId());
		filterVo.setCustomerId(filterRequest.getCustomerId());
		filterVo.setStatus(filterRequest.getStatus());
		filterVo.setFromAmount(filterRequest.getFromAmount());
		filterVo.setToAmount(filterRequest.getToAmount());
		filterVo.setStartDate(filterRequest.getStartDate());
		filterVo.setEndDate(filterRequest.getEndDate());
		filterVo.setUserId(filterRequest.getUserId());
		filterVo.setRoleName(filterRequest.getRoleName());
		return filterVo;
	}

	public ArInvoiceVo convertArInvoiceVoFromRequest(ArInvoiceRequest invoiceRequest) {
		ArInvoiceVo invoiceVo = new ArInvoiceVo();
		invoiceVo.setInvoiceId(invoiceRequest.getInvoiceId());
		invoiceVo.setUserId(invoiceRequest.getUserId());
		invoiceVo.setOrgId(invoiceRequest.getOrgId());
		invoiceVo.setIsSuperAdmin(invoiceRequest.getIsSuperAdmin());
		invoiceVo.setRoleName(invoiceRequest.getRoleName());
		invoiceVo.setGeneralInformation(invoiceRequest.getGeneralInformation() != null
				? convertArInvoiceInfoVoFromRequest(invoiceRequest.getGeneralInformation())
						: null);
		List<ArInvoiceProductVo> products = new ArrayList<ArInvoiceProductVo>();
		if (invoiceRequest.getProducts() != null) {
			invoiceRequest.getProducts().forEach(product -> {
				products.add(convertArInvoiceProductVoFromRequest(product));
			});
		}
		invoiceVo.setProducts(products.size() > 0 ? products : null);
		List<UploadFileVo> uploadList = new ArrayList<UploadFileVo>();
		if (invoiceRequest.getAttachments() != null && invoiceRequest.getAttachments().size() > 0) {
			invoiceRequest.getAttachments().forEach(file -> {
				uploadList.add(ConvertToVoHelper.getInstance().convertAttachmentFromReq(file));
			});
		}
		invoiceVo.setAttachments(uploadList);
		invoiceVo.setAttachmentsToRemove(invoiceRequest.getAttachmentsToRemove());
		invoiceVo.setStatus(invoiceRequest.getStatus());
		return invoiceVo;

	}

	private ArInvoiceGeneralInformationVo convertArInvoiceInfoVoFromRequest(
			ArInvoiceGeneralInformationRequest generalInfoRequest) {
		ArInvoiceGeneralInformationVo generalInfoVo = new ArInvoiceGeneralInformationVo();
		generalInfoVo.setAdjustmentAccount(generalInfoRequest.getAdjustmentAccount());
		generalInfoVo.setAdjustmentAccountId(generalInfoRequest.getAdjustmentAccountId());
		generalInfoVo.setIsRegistered(generalInfoRequest.getIsRegistered());
		generalInfoVo.setAdjustmentValue(generalInfoRequest.getAdjustmentValue());
		generalInfoVo.setBankId(generalInfoRequest.getBankId());
		generalInfoVo.setBankType(generalInfoRequest.getBankType());
		generalInfoVo.setCurrencyId(generalInfoRequest.getCurrencyId());
		generalInfoVo.setCustomerAccountId(generalInfoRequest.getCustomerAccountId());
		generalInfoVo.setCustomerAccount(generalInfoRequest.getCustomerAccount());
		generalInfoVo.setCustomerGstNumber(generalInfoRequest.getCustomerGstNumber());
		generalInfoVo.setCustomerId(generalInfoRequest.getCustomerId());
		generalInfoVo.setDiscountAccount(generalInfoRequest.getDiscountAccount());
		generalInfoVo.setDiscountAccountId(generalInfoRequest.getDiscountAccountId());
		generalInfoVo.setDiscountValue(generalInfoRequest.getDiscountValue());
		generalInfoVo.setDueDate(generalInfoRequest.getDueDate());
		generalInfoVo.setEcommerceGstIn(generalInfoRequest.getEcommerceGstIn());
		generalInfoVo.setExchangeRate(
				generalInfoRequest.getInvoiceTypeId().equals(1) ? 1.0 : generalInfoRequest.getExchangeRate());
		generalInfoVo.setTdsId(generalInfoRequest.getTdsId());
		generalInfoVo.setTdsValue(generalInfoRequest.getTdsValue());
		generalInfoVo.setExportTypeId(generalInfoRequest.getExportTypeId());
		generalInfoVo.setInvoiceDate(generalInfoRequest.getInvoiceDate());
		generalInfoVo.setInvoiceNoPrefix(generalInfoRequest.getInvoiceNoPrefix());
		generalInfoVo.setInvoiceNoSuffix(generalInfoRequest.getInvoiceNoSuffix());
		generalInfoVo.setInvoiceNumber(generalInfoRequest.getInvoiceNumber());
		generalInfoVo.setInvoiceTypeId(generalInfoRequest.getInvoiceTypeId());
		generalInfoVo.setIsReverseCharge(generalInfoRequest.getIsReverseCharge());
		generalInfoVo.setIsSoldByEcommerce(generalInfoRequest.getIsSoldByEcommerce());
		generalInfoVo.setIsWopUnderLut(generalInfoRequest.getIsWopUnderLut());
		generalInfoVo.setLocationId(generalInfoRequest.getLocationId());
		generalInfoVo.setLutDate(generalInfoRequest.getLutDate());
		generalInfoVo.setLutNumber(generalInfoRequest.getLutNumber());
		generalInfoVo.setOrgGstNumber(generalInfoRequest.getOrgGstNumber());
		generalInfoVo.setPaymentTermsId(generalInfoRequest.getPaymentTermsId());
		generalInfoVo.setPlaceOfSupply(generalInfoRequest.getPlaceOfSupply());
		generalInfoVo.setPortCode(generalInfoRequest.getPortCode());
		generalInfoVo.setPurchaseOrderNo(generalInfoRequest.getPurchaseOrderNo());
		generalInfoVo.setReceiverName(generalInfoRequest.getReceiverName());
		generalInfoVo.setShippingBillDate(generalInfoRequest.getShippingBillDate());
		generalInfoVo.setShippingBillNo(generalInfoRequest.getShippingBillNo());
		generalInfoVo.setSubTotal(generalInfoRequest.getSubTotal());
		generalInfoVo.setSupplyServiceId(generalInfoRequest.getSupplyServiceId());
		generalInfoVo.setTaxApplicationId(generalInfoRequest.getTaxApplicationId());
		generalInfoVo.setTermsAndConditions(generalInfoRequest.getTermsAndConditions());
		generalInfoVo.setTotal(generalInfoRequest.getTotal());
		generalInfoVo.setDiscountAccountLevel(generalInfoRequest.getDiscountAccountLevel());
		generalInfoVo.setAdjustmentAccountLevel(generalInfoRequest.getAdjustmentAccountLevel());
		generalInfoVo.setBillingAddress(convertCustBillingAddressVoFromReq(generalInfoRequest.getBillingAddress()));
		generalInfoVo.setDeliveryAddress(convertCustDeliveryAddressVoFromReq(generalInfoRequest.getDeliveryAddress()));
		generalInfoVo.setNotes(generalInfoRequest.getNotes());
		generalInfoVo.setTogglePayment(generalInfoRequest.getTogglePayment());
		generalInfoVo.setGeneralLedgerData(generalInfoRequest.getGeneralLedgerData());
		generalInfoVo.setCreate_ts(generalInfoRequest.getCreate_ts());
		return generalInfoVo;

	}


	public ArInvoiceProductVo convertArInvoiceProductVoFromRequest(ArInvoiceProductRequest productRequest) {
		ArInvoiceProductVo productVo = new ArInvoiceProductVo();
		productVo.setId(productRequest.getId());
		productVo.setTempId(productRequest.getTempId());
		productVo.setAmount(productRequest.getAmount());
		productVo.setDiscountAmount(productRequest.getDiscountAmount());
		productVo.setDiscountType(productRequest.getDiscountType());
		productVo.setDiscount(productRequest.getDiscount());
		productVo.setHsn(productRequest.getHsn());
		productVo.setProductAccountId(productRequest.getProductAccountId());
		productVo.setProductId(productRequest.getProductId());
		productVo.setQuantity(productRequest.getQuantity());
		productVo.setMeasureId(productRequest.getMeasureId());
		productVo.setStatus(productRequest.getStatus());
		productVo.setTaxDetails(convertArInvoiceTaxDetailsVoFromRequest(productRequest.getTaxDetails()));
		productVo.setTaxRateId(productRequest.getTaxRateId());
		productVo.setUnitPrice(productRequest.getUnitPrice());
		productVo.setProductAccountName(productRequest.getProductAccountName());
		productVo.setProductAccountLevel(productRequest.getProductAccountLevel());
		productVo.setNetAmount(productRequest.getNetAmount());
		productVo.setBaseUnitPrice(productRequest.getBaseUnitPrice());
		productVo.setDescription(productRequest.getDescription());
		return productVo;

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
		return deliveryAddressVo;

	}
	private ArInvoiceTaxDetailsVo convertArInvoiceTaxDetailsVoFromRequest(ArInvoiceTaxDetailsRequest detailsRequest) {
		if (detailsRequest != null) {
			ArInvoiceTaxDetailsVo taxDetailsVo = new ArInvoiceTaxDetailsVo();
			taxDetailsVo.setId(detailsRequest.getId());
			taxDetailsVo.setComponentName(detailsRequest.getComponentName());
			taxDetailsVo.setComponentItemId(detailsRequest.getComponentItemId());
			taxDetailsVo.setGroupName(detailsRequest.getGroupName());
			List<ArInvoiceTaxDistributionVo> taxDistributionVos = new ArrayList<ArInvoiceTaxDistributionVo>();
			detailsRequest.getTaxDistribution().forEach(taxDistribution -> {
				taxDistributionVos.add(convertArInvoiceTaxDistributionVoFromReq(taxDistribution));
			});
			taxDetailsVo.setTaxDistribution(taxDistributionVos);
			return taxDetailsVo;
		}
		return null;

	}


	private ArInvoiceTaxDistributionVo convertArInvoiceTaxDistributionVoFromReq(
			ArInvoiceTaxDistributionRequest distributionRequest) {
		ArInvoiceTaxDistributionVo distributionVo = new ArInvoiceTaxDistributionVo();
		distributionVo.setTaxAmount(distributionRequest.getTaxAmount());
		distributionVo.setTaxName(distributionRequest.getTaxName());
		distributionVo.setTaxRate(distributionRequest.getTaxRate());
		return distributionVo;

	}

	public LetterOfUndertakingVo convertLetterOfUndertakingVoFromLetterOfUndertakingRequest(
			LetterOfUndertakingRequest letterOfUndertaking) {

		LetterOfUndertakingVo letterOfUnderstandingVo = new LetterOfUndertakingVo();
		letterOfUnderstandingVo.setId(letterOfUndertaking.getId());
		letterOfUnderstandingVo.setDateOfCreation(letterOfUndertaking.getDateOfCreation());
		letterOfUnderstandingVo.setExpiryDate(letterOfUndertaking.getExpiryDate());
		letterOfUnderstandingVo.setAckNo(letterOfUndertaking.getAckNo());
		letterOfUnderstandingVo.setAttachments(letterOfUndertaking.getAttachments());
		letterOfUnderstandingVo.setAttachmentsToRemove(letterOfUndertaking.getAttachmentsToRemove());
		letterOfUnderstandingVo.setStatus(letterOfUndertaking.getStatus());
		letterOfUnderstandingVo.setUserId(letterOfUndertaking.getUserId());
		letterOfUnderstandingVo.setLocationId(letterOfUndertaking.getLocationId());
		letterOfUnderstandingVo.setGstNumber(letterOfUndertaking.getGstNumber());
		letterOfUnderstandingVo.setFinYear(letterOfUndertaking.getFinYear());
		letterOfUnderstandingVo.setOrganizationId(letterOfUndertaking.getOrganizationId());
		letterOfUnderstandingVo.setCreateTs(letterOfUndertaking.getCreateTs());
		letterOfUnderstandingVo.setUpdateTs(letterOfUndertaking.getUpdateTs());
		letterOfUnderstandingVo.setIsSuperAdmin(letterOfUndertaking.getIsSuperAdmin());
		letterOfUnderstandingVo.setRoleName(letterOfUndertaking.getRoleName());
		return letterOfUnderstandingVo;
	}

	public ReceiptVo convertReceiptVoFromReceiptRequest(ReceiptRequest receiptRequest) {

		ReceiptVo receiptVo = new ReceiptVo();
		receiptVo.setId(receiptRequest.getId());
		receiptVo.setBankId(receiptRequest.getBankId());
		receiptVo.setBankType(receiptRequest.getBankType());
		receiptVo.setReceiptBulkDetails(receiptRequest.getReceiptBulkDetails());
		receiptVo.setReceiptType(receiptRequest.getReceiptType());
		receiptVo.setReceiptNoPrefix(receiptRequest.getReceiptNoPrefix());
		receiptVo.setReceiptNo(receiptRequest.getReceiptNo());
		receiptVo.setReceiptNoSuffix(receiptRequest.getReceiptNoSuffix());
		receiptVo.setReceiptDate(receiptRequest.getReceiptDate());
		receiptVo.setCustomerId(receiptRequest.getCustomerId());
		receiptVo.setContactId(receiptRequest.getContactId());
		receiptVo.setContactType(receiptRequest.getContactType());
		receiptVo.setContactName(receiptRequest.getContactName());
		receiptVo.setAttachments(receiptRequest.getAttachments());
		receiptVo.setAttachmentsToRemove(receiptRequest.getAttachmentsToRemove());
		receiptVo.setAmount(receiptRequest.getAmount());
		receiptVo.setCustomTableList(receiptRequest.getCustomTableList());
		receiptVo.setItemsToRemove(receiptRequest.getItemsToRemove());
		receiptVo.setExchangeRate(receiptRequest.getExchangeRate());
		receiptVo.setCurrencyId(receiptRequest.getCurrencyId());
		receiptVo.setStatus(receiptRequest.getStatus());
		receiptVo.setOrganizationId(receiptRequest.getOrganizationId());
		receiptVo.setRoleName(receiptRequest.getRoleName());
		receiptVo.setCreateTs(receiptRequest.getCreateTs());
		receiptVo.setUpdateTs(receiptRequest.getUpdateTs());
		receiptVo.setUserId(receiptRequest.getUserId());
		receiptVo.setNotes(receiptRequest.getNotes());
		receiptVo.setAddNotes(receiptRequest.getAddNotes());
		receiptVo.setVendorId(receiptRequest.getVendorId());
		receiptVo.setAdjustedAmount(receiptRequest.getAdjustedAmount());
		receiptVo.setDifferenceAmount(receiptRequest.getDifferenceAmount());
		receiptVo.setRecordBillDetails(receiptRequest.getRecordBillDetails());
		receiptVo.setVendorReceiptDetails(receiptRequest.getVendorReceiptDetails());
		receiptVo.setVendorReceiptItemsToRemove(receiptRequest.getVendorReceiptItemsToRemove());
		receiptVo.setIsBulk(receiptRequest.getIsBulk());
		receiptVo.setGeneralLedgerData(receiptRequest.getGeneralLedgerData());
		return receiptVo;
	}



}
