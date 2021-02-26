package com.blackstrawai.helper;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;

import com.blackstrawai.ap.balanceconfirmation.BalanceConfirmationGeneralVo;
import com.blackstrawai.ap.balanceconfirmation.BalanceConfirmationVo;
import com.blackstrawai.ap.billsinvoice.InvoiceFilterVo;
import com.blackstrawai.ap.billsinvoice.InvoiceGeneralInfoVo;
import com.blackstrawai.ap.billsinvoice.InvoiceProductVo;
import com.blackstrawai.ap.billsinvoice.InvoiceTaxDetailsVo;
import com.blackstrawai.ap.billsinvoice.InvoiceTaxDistributionVo;
import com.blackstrawai.ap.billsinvoice.InvoiceTrasactionVo;
import com.blackstrawai.ap.billsinvoice.InvoiceVo;
import com.blackstrawai.ap.billsinvoice.QuickInvoiceVo;
import com.blackstrawai.ap.creditnote.ApplyFundVo;
import com.blackstrawai.ap.creditnote.VendorCreditCreateVo;
import com.blackstrawai.ap.expense.ExpenseFilterVO;
import com.blackstrawai.ap.expense.ExpensesVO;
import com.blackstrawai.ap.payment.noncore.PaymentNonCoreVo;
import com.blackstrawai.ap.purchaseorder.PoAddressVo;
import com.blackstrawai.ap.purchaseorder.PoBillingAddressVo;
import com.blackstrawai.ap.purchaseorder.PoDeliveryAddressVo;
import com.blackstrawai.ap.purchaseorder.PoFilterVo;
import com.blackstrawai.ap.purchaseorder.PoGeneralInfoVo;
import com.blackstrawai.ap.purchaseorder.PoItemsVo;
import com.blackstrawai.ap.purchaseorder.PoProductVo;
import com.blackstrawai.ap.purchaseorder.PoTaxDetailsVo;
import com.blackstrawai.ap.purchaseorder.PoTaxDistributionVo;
import com.blackstrawai.ap.purchaseorder.PurchaseOrderVo;
import com.blackstrawai.ap.vendorportal.purchaseorder.DeclinePoVo;
import com.blackstrawai.ap.vendorportal.purchaseorder.VendorPortalPoFilterVo;
import com.blackstrawai.attachments.UploadFileVo;
import com.blackstrawai.request.ap.balanceconfirmation.BalanceConfirmationGeneralRequest;
import com.blackstrawai.request.ap.balanceconfirmation.BalanceConfirmationRequest;
import com.blackstrawai.request.ap.billsinvoice.InvoiceFilterRequest;
import com.blackstrawai.request.ap.billsinvoice.InvoiceGeneralInfoRequest;
import com.blackstrawai.request.ap.billsinvoice.InvoiceProductRequest;
import com.blackstrawai.request.ap.billsinvoice.InvoiceRequest;
import com.blackstrawai.request.ap.billsinvoice.InvoiceTaxDetailsRequest;
import com.blackstrawai.request.ap.billsinvoice.InvoiceTaxDistributionRequest;
import com.blackstrawai.request.ap.billsinvoice.InvoiceTrasactionRequest;
import com.blackstrawai.request.ap.billsinvoice.QuickInvoiceRequest;
import com.blackstrawai.request.ap.creditnote.ApplyFundCreateRequest;
import com.blackstrawai.request.ap.creditnote.VendorCreditCreateRequest;
import com.blackstrawai.request.ap.expense.ExpenseFilterRequest;
import com.blackstrawai.request.ap.expense.ExpenseRequest;
import com.blackstrawai.request.ap.expense.ExpenseRequestDetails;
import com.blackstrawai.request.ap.payment.noncore.PaymentNonCoreRequest;
import com.blackstrawai.request.ap.purchaseorder.PoBillingAddressRequest;
import com.blackstrawai.request.ap.purchaseorder.PoDeliveryAddressRequest;
import com.blackstrawai.request.ap.purchaseorder.PoFilterRequest;
import com.blackstrawai.request.ap.purchaseorder.PoGeneralInfoRequest;
import com.blackstrawai.request.ap.purchaseorder.PoItemsRequest;
import com.blackstrawai.request.ap.purchaseorder.PoProductRequest;
import com.blackstrawai.request.ap.purchaseorder.PoTaxDetailsRequest;
import com.blackstrawai.request.ap.purchaseorder.PoTaxDistributionRequest;
import com.blackstrawai.request.ap.purchaseorder.PurchaseOrderRequest;
import com.blackstrawai.request.ap.vendorportal.purchaseorder.DeclinePoRequest;
import com.blackstrawai.request.ap.vendorportal.purchaseorder.VendorPortalPoFilterRequest;

public class ApConvertToVoHelper {

	private static ApConvertToVoHelper apConvertToVoHelper;
	private static ModelMapper modelMapper;

	public static ApConvertToVoHelper getInstance() {
		if (apConvertToVoHelper == null) {
			apConvertToVoHelper = new ApConvertToVoHelper();
			modelMapper = new ModelMapper();
		}
		return apConvertToVoHelper;
	}

	public BalanceConfirmationVo convertBalanceConfirmationVoFromBalanceConfirmationRequest(
			BalanceConfirmationRequest balanceConfirmationRequest) {
		BalanceConfirmationVo balanceConfirmationVo = new BalanceConfirmationVo();
		List<BalanceConfirmationGeneralVo> listbalanceConfirmationGeneralVo = new ArrayList<BalanceConfirmationGeneralVo>();
		for (BalanceConfirmationGeneralRequest generalInfo : balanceConfirmationRequest.getGeneralInfo()) {
			BalanceConfirmationGeneralVo balanceConfirmationGeneralVo = new BalanceConfirmationGeneralVo();
			balanceConfirmationGeneralVo.setId(generalInfo.getId());
			balanceConfirmationGeneralVo.setClosingBalance(generalInfo.getClosingBalance());
			balanceConfirmationGeneralVo.setCurrencyId(generalInfo.getCurrencyId());
			balanceConfirmationGeneralVo.setOpeningBalance(generalInfo.getOpeningBalance());
			balanceConfirmationGeneralVo.setStatus(generalInfo.getStatus());
			listbalanceConfirmationGeneralVo.add(balanceConfirmationGeneralVo);
		}
		balanceConfirmationVo.setGeneralInfo(listbalanceConfirmationGeneralVo);
		balanceConfirmationVo.setVendorId(balanceConfirmationRequest.getVendorId());
		balanceConfirmationVo.setStartDate(balanceConfirmationRequest.getStartDate());
		balanceConfirmationVo.setEndDate(balanceConfirmationRequest.getEndDate());
		balanceConfirmationVo.setOrganizationId(balanceConfirmationRequest.getOrganizationId());
		balanceConfirmationVo.setIsSuperAdmin(balanceConfirmationRequest.getIsSuperAdmin());
		balanceConfirmationVo.setRoleName(balanceConfirmationRequest.getRoleName());
		balanceConfirmationVo.setStatus(balanceConfirmationRequest.getStatus());
		balanceConfirmationVo.setUserId(balanceConfirmationRequest.getUserId());
		balanceConfirmationVo.setId(balanceConfirmationRequest.getId());
		balanceConfirmationVo.setIsQuick(balanceConfirmationRequest.getIsQuick());
		balanceConfirmationVo.setGeneralInfo(listbalanceConfirmationGeneralVo);
		List<UploadFileVo> uploadList = new ArrayList<UploadFileVo>();
		if (balanceConfirmationRequest.getAttachments() != null
				&& balanceConfirmationRequest.getAttachments().size() > 0) {
			balanceConfirmationRequest.getAttachments().forEach(file -> {
				uploadList.add(ConvertToVoHelper.getInstance().convertAttachmentFromReq(file));
			});
		}
		balanceConfirmationVo.setAttachments(uploadList);
		balanceConfirmationVo.setAttachmentsToRemove(balanceConfirmationRequest.getAttachmentsToRemove());
		return balanceConfirmationVo;
	}

	public InvoiceVo convertBillsInvoiceVoFromRequest(InvoiceRequest invoiceRequest) {
		InvoiceVo invoiceVo = new InvoiceVo();
		invoiceVo.setId(invoiceRequest.getId());
		invoiceVo.setUserId(invoiceRequest.getUserId());
		invoiceVo.setDateFormat(invoiceRequest.getDateFormat());
		invoiceVo.setOrganizationId(invoiceRequest.getOrganizationId());
		invoiceVo.setIsSuperAdmin(invoiceRequest.getIsSuperAdmin());
		invoiceVo.setIsInvoiceWithBills(invoiceRequest.getIsInvoiceWithBills());
		invoiceVo.setGeneralInfo(invoiceRequest.getGeneralInfo() != null
				? convertInvoiceInfoVoFromRequest(invoiceRequest.getGeneralInfo())
						: null);
		invoiceVo.setTransactionDetails(invoiceRequest.getTransactionDetails() != null
				? convertInvoiceTransactionVoFromRequest(invoiceRequest.getTransactionDetails())
						: null);
		List<UploadFileVo> uploadList = new ArrayList<UploadFileVo>();
		if (invoiceRequest.getAttachments() != null && invoiceRequest.getAttachments().size() > 0) {
			invoiceRequest.getAttachments().forEach(file -> {
				uploadList.add(ConvertToVoHelper.getInstance().convertAttachmentFromReq(file));
			});
		}
		invoiceVo.setAttachments(uploadList);
		invoiceVo.setAttachmentsToRemove(invoiceRequest.getAttachmentsToRemove());
		invoiceVo.setStatus(invoiceRequest.getStatus());
		invoiceVo.setRoleName(invoiceRequest.getRoleName());
		invoiceVo.setGeneralLedgerData(invoiceRequest.getGeneralLedgerData());
		return invoiceVo;

	}

	private InvoiceGeneralInfoVo convertInvoiceInfoVoFromRequest(InvoiceGeneralInfoRequest generalInfoRequest) {
		InvoiceGeneralInfoVo generalInfoVo = new InvoiceGeneralInfoVo();
		generalInfoVo.setDueDate(generalInfoRequest.getDueDate());
		generalInfoVo.setGstNumberId(generalInfoRequest.getGstNumberId());
		generalInfoVo.setInvoiceDate(generalInfoRequest.getInvoiceDate());
		generalInfoVo.setInvoiceNo(generalInfoRequest.getInvoiceNo());
		generalInfoVo.setLocationId(generalInfoRequest.getLocationId());
		generalInfoVo.setIsRegistered(generalInfoRequest.getIsRegistered());
		generalInfoVo.setNotes(generalInfoRequest.getNotes());
		generalInfoVo.setPaymentTermsId(generalInfoRequest.getPaymentTermsId());
		generalInfoVo.setPoReferenceNo(generalInfoRequest.getPoReferenceNo());
		generalInfoVo.setTermsAndConditions(generalInfoRequest.getTermsAndConditions());
		generalInfoVo.setVendorId(generalInfoRequest.getVendorId());
		generalInfoVo.setAksharData(generalInfoRequest.getAksharData());
		generalInfoVo.setVendorGstNo(generalInfoRequest.getVendorGstNo());
		generalInfoVo.setInvoiceNoPrefix(generalInfoRequest.getInvoiceNoPrefix());
		generalInfoVo.setInvoiceNoSuffix(generalInfoRequest.getInvoiceNoSuffix());
		return generalInfoVo;

	}
	
	public List<ApplyFundVo> convertVendorCreditVoFromRequest(List<ApplyFundCreateRequest> applyCreditsRequests) {
		List<ApplyFundVo> applyFundVos = new ArrayList<>();
		for (ApplyFundCreateRequest applyCreditsRequest : applyCreditsRequests) {
			ApplyFundVo applyFundVo = new ApplyFundVo();
			applyFundVo.setAppliedAmount(applyCreditsRequest.getAppliedAmount());
			applyFundVo.setInvoiceId(applyCreditsRequest.getInvoiceId());
			applyFundVos.add(applyFundVo);
		}
		return applyFundVos;

	}
	
	public VendorCreditCreateVo convertToVendorCreditCreateVoFromRequest(
			VendorCreditCreateRequest vendorCreditCreateRequest) {
		return modelMapper.map(vendorCreditCreateRequest, VendorCreditCreateVo.class);

	}

	private InvoiceTrasactionVo convertInvoiceTransactionVoFromRequest(InvoiceTrasactionRequest trasactionRequest) {
		InvoiceTrasactionVo trasactionVo = new InvoiceTrasactionVo();
		trasactionVo.setAdjustment(trasactionRequest.getAdjustment());
		trasactionVo.setAdjustmentAccountId(trasactionRequest.getAdjustmentAccountId());
		trasactionVo.setCurrencyid(trasactionRequest.getCurrencyid());
		trasactionVo.setDestinationOfSupplyId(trasactionRequest.getDestinationOfSupplyId());
		trasactionVo.setDiscountAccountId(trasactionRequest.getDiscountAccountId());
		trasactionVo.setDiscountAmount(trasactionRequest.getDiscountAmount());
		trasactionVo.setDiscountTypeId(trasactionRequest.getDiscountTypeId());
		trasactionVo.setDiscountValue(trasactionRequest.getDiscountValue());
		trasactionVo.setExchangeRate(trasactionRequest.getExchangeRate());
		trasactionVo.setIsApplyAfterTax(trasactionRequest.getIsApplyAfterTax());
		trasactionVo.setSourceOfSupplyId(trasactionRequest.getSourceOfSupplyId());
		trasactionVo.setSubTotal(trasactionRequest.getSubTotal());
		trasactionVo.setTaxApplicationMethodId(trasactionRequest.getTaxApplicationMethodId());
		trasactionVo.setTdsId(trasactionRequest.getTdsId());
		trasactionVo.setTdsValue(trasactionRequest.getTdsValue());
		trasactionVo.setTotal(trasactionRequest.getTotal());
		trasactionVo.setAdjustmentAccountName(trasactionRequest.getAdjustmentAccountName());
		trasactionVo.setAdjustmentAccountLevel(trasactionRequest.getAdjustmentAccountLevel());
		trasactionVo.setDiscountAccountName(trasactionRequest.getDiscountAccountName());
		trasactionVo.setDiscountAccountLevel(trasactionRequest.getDiscountAccountLevel());
		List<InvoiceProductVo> products = new ArrayList<InvoiceProductVo>();
		if (trasactionRequest.getProducts() != null && trasactionRequest.getProducts().size() > 0) {
			trasactionRequest.getProducts().forEach(product -> {
				products.add(convertInvoiceProductVoFromRequest(product));
			});
		}
		trasactionVo.setProducts(products);
		return trasactionVo;

	}

	public InvoiceProductVo convertInvoiceProductVoFromRequest(InvoiceProductRequest productRequest) {
		InvoiceProductVo productVo = new InvoiceProductVo();
		productVo.setId(productRequest.getId());
		productVo.setAmount(productRequest.getAmount());
		productVo.setCustomerId(productRequest.getCustomerId());
		productVo.setInputTaxCredit(productRequest.getInputTaxCredit());
		productVo.setIsBillable(productRequest.getIsBillable());
		productVo.setProductAccountId(productRequest.getProductAccountId());
		productVo.setProductId(productRequest.getProductId());
		productVo.setQuantity(productRequest.getQuantity());
		productVo.setMeasureId(productRequest.getMeasureId());
		productVo.setStatus(productRequest.getStatus());
		productVo.setTaxDetails(convertInvoiceTaxDetailsVoFromRequest(productRequest.getTaxDetails()));
		productVo.setTaxRateId(productRequest.getTaxRateId());
		productVo.setUnitPrice(productRequest.getUnitPrice());
		productVo.setProductAccountName(productRequest.getProductAccountName());
		productVo.setProductAccountLevel(productRequest.getProductAccountLevel());
		productVo.setDescription(productRequest.getDescription());
		productVo.setTempId(productRequest.getTempId());
		return productVo;

	}


	private InvoiceTaxDetailsVo convertInvoiceTaxDetailsVoFromRequest(InvoiceTaxDetailsRequest detailsRequest) {
		if (detailsRequest != null) {
			InvoiceTaxDetailsVo taxDetailsVo = new InvoiceTaxDetailsVo();
			taxDetailsVo.setId(detailsRequest.getId());
			taxDetailsVo.setComponentName(detailsRequest.getComponentName());
			taxDetailsVo.setComponentItemId(detailsRequest.getComponentItemId());
			taxDetailsVo.setGroupName(detailsRequest.getGroupName());
			List<InvoiceTaxDistributionVo> taxDistributionVos = new ArrayList<InvoiceTaxDistributionVo>();
			detailsRequest.getTaxDistribution().forEach(taxDistribution -> {
				taxDistributionVos.add(convertInvoiceTaxDistributionVoFromReq(taxDistribution));
			});
			taxDetailsVo.setTaxDistribution(taxDistributionVos);
			return taxDetailsVo;
		}
		return null;

	}


	private InvoiceTaxDistributionVo convertInvoiceTaxDistributionVoFromReq(
			InvoiceTaxDistributionRequest distributionRequest) {
		InvoiceTaxDistributionVo distributionVo = new InvoiceTaxDistributionVo();
		distributionVo.setTaxAmount(distributionRequest.getTaxAmount());
		distributionVo.setTaxName(distributionRequest.getTaxName());
		distributionVo.setTaxRate(distributionRequest.getTaxRate());
		return distributionVo;

	}

	public InvoiceFilterVo convertInvoiceFilterVoFromRequest(InvoiceFilterRequest filterRequest) {
		InvoiceFilterVo filterVo = new InvoiceFilterVo();
		filterVo.setOrgId(filterRequest.getOrgId());
		filterVo.setIsInvoiceWithBills(filterRequest.getIsInvoiceWithBills());
		filterVo.setStatus(filterRequest.getStatus());
		filterVo.setVendorId(filterRequest.getVendorId());
		filterVo.setFromAmount(filterRequest.getFromAmount());
		filterVo.setToAmount(filterRequest.getToAmount());
		filterVo.setStartDate(filterRequest.getStartDate());
		filterVo.setEndDate(filterRequest.getEndDate());
		filterVo.setUserId(filterRequest.getUserId());
		filterVo.setRoleName(filterRequest.getRoleName());
		filterVo.setDueAmountGreaterZero(filterRequest.getDueAmountGreaterZero());
		return filterVo;
	}

	public QuickInvoiceVo convertQuickInvoiceVoFromRequest(QuickInvoiceRequest quickInvoiceRequest) {
		QuickInvoiceVo vo = new QuickInvoiceVo();
		vo.setBaseAmount(quickInvoiceRequest.getBaseAmount());
		vo.setCurrencyId(quickInvoiceRequest.getCurrencyId());
		vo.setDueDate(quickInvoiceRequest.getDueDate());
		vo.setGstAmount(quickInvoiceRequest.getGstAmount());
		vo.setId(quickInvoiceRequest.getId());
		vo.setInvoiceDate(quickInvoiceRequest.getInvoiceDate());
		vo.setInvoiceNumber(quickInvoiceRequest.getInvoiceNumber());
		vo.setIsInvoiceWithBills(quickInvoiceRequest.getIsInvoiceWithBills());
		vo.setIsRcmApplicable(quickInvoiceRequest.getIsRcmApplicable());
		vo.setIsSuperAdmin(quickInvoiceRequest.getIsSuperAdmin());
		vo.setOrganizationId(quickInvoiceRequest.getOrganizationId());
		vo.setTotalAmount(quickInvoiceRequest.getTotalAmount());
		vo.setUserId(quickInvoiceRequest.getUserId());
		vo.setVendorId(quickInvoiceRequest.getVendorId());
		vo.setRemarks(quickInvoiceRequest.getRemarks());
		vo.setAksharData(quickInvoiceRequest.getAksharData());
		List<UploadFileVo> uploadList = new ArrayList<UploadFileVo>();
		if (quickInvoiceRequest.getAttachments() != null && quickInvoiceRequest.getAttachments().size() > 0) {
			quickInvoiceRequest.getAttachments().forEach(file -> {
				uploadList.add(ConvertToVoHelper.getInstance().convertAttachmentFromReq(file));
			});
		}
		vo.setAttachments(uploadList);
		vo.setAttachmentsToRemove(quickInvoiceRequest.getAttachmentsToRemove());
		vo.setRoleName(quickInvoiceRequest.getRoleName());
		return vo;

	}

	public ExpensesVO getExpenseVOFromExpenseRequest(ExpenseRequest expenseRequest) {
		ExpensesVO expensesVO = new ExpensesVO();
		List<UploadFileVo> uploadList = new ArrayList<UploadFileVo>();
		ExpenseRequestDetails expenseRequestDetails = expenseRequest.getExpenseGeneralInfo();
		expensesVO.setId(expenseRequestDetails.getId());
		expensesVO.setCash(expenseRequestDetails.getCash());
		expensesVO.setEmployeeId(expenseRequestDetails.getEmployeeId());
		expensesVO.setAccountTypeId(expenseRequestDetails.getAccountTypeId());
		expensesVO.setOrganizationId(expenseRequest.getOrganizationId());
		expensesVO.setSuperAdmin(expenseRequest.getSuperAdmin());
		expensesVO.setStatusId(expenseRequestDetails.getStatusId());
		expensesVO.setNatureOfSpendingId(expenseRequestDetails.getNatureOfSpendingId());
		expensesVO.setExpenseTimeStamp(expenseRequestDetails.getExpenseTimeStamp());
		expensesVO.setExpenseStatusUpdateTimeStamp(expenseRequest.getUpdateTs());
		expensesVO.setUserId(expenseRequest.getUserId());
		expensesVO.setStatus(expenseRequestDetails.getStatus());
		expensesVO.setExpensesAddedVoList(expenseRequestDetails.getExpensesAddedVoList());

		expenseRequest.getAttachments().forEach(file -> {
			uploadList.add(ConvertToVoHelper.getInstance().convertAttachmentFromReq(file));
		});
		expensesVO.setAttachments(uploadList);
		expensesVO.setAttachmentsToRemove(expenseRequest.getAttachmentsToRemove());
		return expensesVO;
	}

	public ExpenseFilterVO getExpenseFilterVOFromExpenseFilterRequest(ExpenseFilterRequest expenseFilterRequest) {
		ExpenseFilterVO expenseFilterVO = new ExpenseFilterVO();
		expenseFilterVO.setAttachment(expenseFilterRequest.getAttachment());
		expenseFilterVO.setMaxAmountRange(expenseFilterRequest.getMaxAmountRange());
		expenseFilterVO.setMinAmountRange(expenseFilterRequest.getMinAmountRange());
		expenseFilterVO.setNatureOfSpendingId(expenseFilterRequest.getNatureOfSpendingId());
		expenseFilterVO.setStatusId(expenseFilterRequest.getStatusId());
		expenseFilterVO.setUserId(expenseFilterRequest.getUserId());
		expenseFilterVO.setStartDateRange(expenseFilterRequest.getStartDateRange());
		expenseFilterVO.setEndDateRange(expenseFilterRequest.getEndDateRange());
		return expenseFilterVO;
	}


	public PaymentNonCoreVo convertPaymentNonCoreVoFromPaymentRequest(PaymentNonCoreRequest request) {
		PaymentNonCoreVo paymentVo = new PaymentNonCoreVo();
		paymentVo.setId(request.getId());
		paymentVo.setUserId(request.getUserId());
		paymentVo.setOrganizationId(request.getOrganizationId());
		paymentVo.setPaidVia(request.getPaidVia());
		paymentVo.setPaymentType(request.getPaymentType());
		paymentVo.setPaymentRefNo(request.getPaymentRefNo());
		paymentVo.setPaymentDate(request.getPaymentDate());
		paymentVo.setVendor(request.getVendor());
		paymentVo.setVendorAccountId(request.getVendorAccountId());
		paymentVo.setVendorAccountName(request.getVendorAccountName());
		paymentVo.setCurrency(request.getCurrency());
		paymentVo.setCurrencySymbol(request.getCurrencySymbol());
		paymentVo.setAmountPaid(request.getAmountPaid());
		paymentVo.setNotes(request.getNotes());
		paymentVo.setCreditList(request.getCreditList());
		paymentVo.setAttachments(request.getAttachments());
		paymentVo.setAttachmentsToRemove(request.getAttachmentsToRemove());
		paymentVo.setPoRefNo(request.getPoRefNo());
		paymentVo.setPaidTo(request.getPaidTo());
		paymentVo.setContactAccountId(request.getContactAccountId());
		paymentVo.setContactAccountName(request.getContactAccountName());
		paymentVo.setContactId(request.getContactId());
		paymentVo.setPayType(request.getPayType());
		paymentVo.setPayPeriod(request.getPayPeriod());
		paymentVo.setSuperAdmin(request.isSuperAdmin());
		paymentVo.setBillAmount(request.getBillAmount());
		paymentVo.setAdjustedAmount(request.getAdjustedAmount());
		paymentVo.setDifferenceAmount(request.getDifferenceAmount());
		paymentVo.setTotalAmount(request.getTotalAmount());
		paymentVo.setCustomTableList(request.getCustomTableList());
		paymentVo.setStatus(request.getStatus());
		paymentVo.setBankFees(request.getBankFees());
		paymentVo.setDueAmount(request.getDueAmount());
		paymentVo.setTdsDeductions(request.getTdsDeductions());
		paymentVo.setPayRunRefNo(request.getPayRunRefNo());
		paymentVo.setOthers1(request.getOthers1());
		paymentVo.setOthers2(request.getOthers2());
		paymentVo.setOthers3(request.getOthers3());
		paymentVo.setTax(request.getTax());
		paymentVo.setInterest(request.getInterest());
		paymentVo.setPenalty(request.getPenalty());
		paymentVo.setRecord(request.isRecord());
		paymentVo.setItemsToRemove(request.getItemsToRemove());
		paymentVo.setPayments(request.getPayments());
		paymentVo.setRoleName(request.getRoleName());
		paymentVo.setPaymentMode(request.getPaymentMode());
		paymentVo.setContactType(request.getContactType());
		paymentVo.setCreditAccountId(request.getCreditAccountId());
		paymentVo.setItemsToRemoveCredit(request.getItemsToRemoveCredit());
		paymentVo.setStatutoryBody(request.getStatutoryBody());
		paymentVo.setCustomerName(request.getCustomerName());
		paymentVo.setEmployeeName(request.getEmployeeName());
		paymentVo.setPaidFor(request.getPaidFor());
		paymentVo.setCurrencyCode(request.getCurrencyCode());
		paymentVo.setExchangeRate(request.getExchangeRate());
		paymentVo.setBillRef(request.getBillRef());
		paymentVo.setReferenceId(request.getReferenceId());
		paymentVo.setInvoiceId(request.getInvoiceId());
		paymentVo.setReferenceType(request.getReferenceType());
		paymentVo.setBulk(request.isBulk());
		paymentVo.setPaidViaName(request.getPaidViaName());
		paymentVo.setContactName(request.getContactName());
		paymentVo.setInvRefName(request.getInvRefName());
		paymentVo.setBaseCurrencyCode(request.getBaseCurrencyCode());
		paymentVo.setGeneralLedgerData(request.getGeneralLedgerData());
		paymentVo.setDisplayDueAmount(request.getDisplayDueAmount());
		paymentVo.setBankReferenceNumber(request.getBankReferenceNumber());
		return paymentVo;
	}


	public PurchaseOrderVo convertPoVoFromRequest(PurchaseOrderRequest purchaseOrderRequest) {
		PurchaseOrderVo purchaseOrderVo = new PurchaseOrderVo();
		purchaseOrderVo.setUserId(purchaseOrderRequest.getUserId());
		purchaseOrderVo.setGeneralInformation(purchaseOrderRequest.getGeneralInformation() != null
				? convertPoGeneralInfoVoFromRequest(purchaseOrderRequest.getGeneralInformation())
						: null);
		purchaseOrderVo.setId(purchaseOrderRequest.getId() != null ? purchaseOrderRequest.getId() : null);
		List<UploadFileVo> uploadList = new ArrayList<UploadFileVo>();
		if (purchaseOrderRequest.getAttachments() != null && purchaseOrderRequest.getAttachments().size() > 0) {
			purchaseOrderRequest.getAttachments().forEach(file -> {
				uploadList.add(ConvertToVoHelper.getInstance().convertAttachmentFromReq(file));
			});
		}
		purchaseOrderVo.setAttachments(uploadList);
		purchaseOrderVo.setAttachmentsToRemove(purchaseOrderRequest.getAttachmentsToRemove());
		purchaseOrderVo.setIsSuperAdmin(
				purchaseOrderRequest.getIsSuperAdmin() != null ? purchaseOrderRequest.getIsSuperAdmin() : null);
		purchaseOrderVo.setItems(
				purchaseOrderRequest.getItems() != null ? convertPoItemsVoFromRequest(purchaseOrderRequest.getItems())
						: null);
		purchaseOrderVo.setOrganizationId(purchaseOrderRequest.getOrganizationId());
		purchaseOrderVo.setStatus(purchaseOrderRequest.getStatus());
		purchaseOrderVo.setRoleName(purchaseOrderRequest.getRoleName());
		if (purchaseOrderRequest.getAddress() != null) {
			PoAddressVo addressVo = new PoAddressVo();
			addressVo.setIsSameBillingAddress(purchaseOrderRequest.getAddress().getIsSameBillingAddress());
			addressVo.setBillingAddress(purchaseOrderRequest.getAddress().getBillingAddress() != null
					? convertPoBillingAdressVoRequest(purchaseOrderRequest.getAddress().getBillingAddress())
							: null);
			addressVo.setDeliveryAddress(purchaseOrderRequest.getAddress().getDeliveryAddress() != null
					? convertDeliveryAdressVoFromDeliveryAdressReq(
							purchaseOrderRequest.getAddress().getDeliveryAddress())
							: null);
			purchaseOrderVo.setAddress(addressVo);
		}
		return purchaseOrderVo;
	}


	private PoGeneralInfoVo convertPoGeneralInfoVoFromRequest(PoGeneralInfoRequest poInformationRequest) {
		PoGeneralInfoVo poInformationVo = new PoGeneralInfoVo();
		poInformationVo.setVendorId(poInformationRequest.getVendorId());
		poInformationVo.setVendorGstNumber(poInformationRequest.getVendorGstNumber());
		poInformationVo.setLocationId(poInformationRequest.getLocationId());
		poInformationVo.setLocationGstNumber(poInformationRequest.getLocationGstNumber());
		poInformationVo.setPurchaseOrderDate(poInformationRequest.getPurchaseOrderDate());
		poInformationVo.setPoOrderNoPrefix(poInformationRequest.getPoOrderNoPrefix());
		poInformationVo.setPurchaseOrderNo(poInformationRequest.getPurchaseOrderNo());
		poInformationVo.setPoOrderNoSuffix(poInformationRequest.getPoOrderNoSuffix());
		poInformationVo.setDeliveryDate(poInformationRequest.getDeliveryDate());
		poInformationVo.setReferenceNo(poInformationRequest.getReferenceNo());
		poInformationVo.setPurchaseOrderTypeId(poInformationRequest.getPurchaseOrderTypeId());
		poInformationVo.setShippingPreferenceId(poInformationRequest.getShippingPreferenceId());
		poInformationVo.setShippingMethodId(poInformationRequest.getShippingMethodId());
		poInformationVo.setPaymentTermsId(poInformationRequest.getPaymentTermsId());
		poInformationVo.setNotes(poInformationRequest.getNotes());
		poInformationVo.setTermsConditions(poInformationRequest.getTermsConditions());
		return poInformationVo;

	}

	private PoBillingAddressVo convertPoBillingAdressVoRequest(PoBillingAddressRequest billingAddressRequest) {
		PoBillingAddressVo billingAddressVo = new PoBillingAddressVo();
		billingAddressVo.setAddress_1(billingAddressRequest.getAddress_1());
		billingAddressVo.setAddress_2(billingAddressRequest.getAddress_2());
		billingAddressVo.setCity(billingAddressRequest.getCity());
		billingAddressVo.setCountry(billingAddressRequest.getCountry());
		billingAddressVo.setState(billingAddressRequest.getState());
		billingAddressVo.setZipCode(billingAddressRequest.getZipCode());
		billingAddressVo.setLandMark(billingAddressRequest.getLandMark());
		billingAddressVo.setPhoneNo(billingAddressRequest.getPhoneNo());
		billingAddressVo.setAttention(billingAddressRequest.getAttention());
		return billingAddressVo;

	}

	private PoDeliveryAddressVo convertDeliveryAdressVoFromDeliveryAdressReq(
			PoDeliveryAddressRequest deliveryAddressRequest) {
		PoDeliveryAddressVo deliveryAddressVo = new PoDeliveryAddressVo();
		deliveryAddressVo.setAddress_1(deliveryAddressRequest.getAddress_1());
		deliveryAddressVo.setAddress_2(deliveryAddressRequest.getAddress_2());
		deliveryAddressVo.setCity(deliveryAddressRequest.getCity());
		deliveryAddressVo.setCountry(deliveryAddressRequest.getCountry());
		deliveryAddressVo.setState(deliveryAddressRequest.getState());
		deliveryAddressVo.setZipCode(deliveryAddressRequest.getZipCode());
		deliveryAddressVo.setLandMark(deliveryAddressRequest.getLandMark());
		deliveryAddressVo.setPhoneNo(deliveryAddressRequest.getPhoneNo());
		deliveryAddressVo.setAttention(deliveryAddressRequest.getAttention());
		return deliveryAddressVo;

	}

	private PoItemsVo convertPoItemsVoFromRequest(PoItemsRequest itemsRequest) {
		PoItemsVo itemsVo = new PoItemsVo();
		itemsVo.setAdjustment(itemsRequest.getAdjustment());
		itemsVo.setCurrencyId(itemsRequest.getCurrencyId());
		itemsVo.setDiscountAmount(itemsRequest.getDiscountAmount());
		itemsVo.setDiscountTypeId(itemsRequest.getDiscountTypeId());
		itemsVo.setDiscountValue(itemsRequest.getDiscountValue());
		itemsVo.setExchangeRate(itemsRequest.getExchangeRate());
		itemsVo.setIsApplyAfterTax(itemsRequest.getIsApplyAfterTax());
		List<PoProductVo> products = new ArrayList<PoProductVo>();
		if (itemsRequest.getProducts() != null)
			itemsRequest.getProducts().forEach(product -> {
				products.add(convertPoProductVoFromRequest(product));
			});

		itemsVo.setProducts(products);
		itemsVo.setSourceOfSupplyId(itemsRequest.getSourceOfSupplyId());
		itemsVo.setSubTotal(itemsRequest.getSubTotal());
		itemsVo.setTaxApplicationMethodId(itemsRequest.getTaxApplicationMethodId());
		itemsVo.setTdsId(itemsRequest.getTdsId());
		itemsVo.setTdsValue(itemsRequest.getTdsValue());
		itemsVo.setTotal(itemsRequest.getTotal());
		itemsVo.setIsRCMApplicable(itemsRequest.getIssRCMApplicable());
		return itemsVo;
	}


	public PoProductVo convertPoProductVoFromRequest(PoProductRequest productRequest) {
		PoProductVo productVo = new PoProductVo();
		productVo.setId(productRequest.getId());
		productVo.setAmount(productRequest.getAmount());
		productVo.setProductAccountId(productRequest.getProductAccountId());
		productVo.setProductId(productRequest.getProductId());
		productVo.setQuantity(productRequest.getQuantity());
		productVo.setMeasureId(productRequest.getMeasureId());
		productVo.setStatus(productRequest.getStatus());
		productVo.setTaxDetails(convertPoTaxDetailsVoFromRequest(productRequest.getTaxDetails()));
		productVo.setTaxRateId(productRequest.getTaxRateId());
		productVo.setUnitPrice(productRequest.getUnitPrice());
		productVo.setProductAccountName(productRequest.getProductAccountName());
		productVo.setProductAccountLevel(null);
		productVo.setDescription(productRequest.getDescription());
		return productVo;

	}



	private PoTaxDetailsVo convertPoTaxDetailsVoFromRequest(PoTaxDetailsRequest detailsRequest) {
		if (detailsRequest != null) {
			PoTaxDetailsVo taxDetailsVo = new PoTaxDetailsVo();
			taxDetailsVo.setId(detailsRequest.getId());
			taxDetailsVo.setComponentName(detailsRequest.getComponentName());
			taxDetailsVo.setComponentItemId(detailsRequest.getComponentItemId());
			taxDetailsVo.setGroupName(detailsRequest.getGroupName());
			List<PoTaxDistributionVo> taxDistributionVos = new ArrayList<PoTaxDistributionVo>();
			detailsRequest.getTaxDistribution().forEach(taxDistribution -> {
				taxDistributionVos.add(convertPoTaxDistributionVoFromReq(taxDistribution));
			});
			taxDetailsVo.setTaxDistribution(taxDistributionVos);
			return taxDetailsVo;
		}
		return null;

	}

	private PoTaxDistributionVo convertPoTaxDistributionVoFromReq(PoTaxDistributionRequest distributionRequest) {
		PoTaxDistributionVo distributionVo = new PoTaxDistributionVo();
		distributionVo.setTaxAmount(distributionRequest.getTaxAmount());
		distributionVo.setTaxName(distributionRequest.getTaxName());
		distributionVo.setTaxRate(distributionRequest.getTaxRate());
		return distributionVo;

	}

	public PoFilterVo convertPoFilterVoFromPoRequest(PoFilterRequest filterRequest) {
		PoFilterVo po = new PoFilterVo();
		po.setOrgId(filterRequest.getOrgId());
		po.setVendorId(filterRequest.getVendorId());
		po.setStatus(filterRequest.getStatus());
		po.setEndDate(filterRequest.getEndDate());
		po.setStartDate(filterRequest.getStartDate());
		po.setFromAmount(filterRequest.getFromAmount());
		po.setToAmount(filterRequest.getToAmount());
		po.setUserId(filterRequest.getUserId());
		po.setRoleName(filterRequest.getRoleName());
		return po;

	}

	public VendorPortalPoFilterVo convertVendorPortalPoFilterVoFromPoRequest(
			VendorPortalPoFilterRequest filterRequest) {
		VendorPortalPoFilterVo po = new VendorPortalPoFilterVo();
		po.setOrgId(filterRequest.getOrgId());
		po.setVendorId(filterRequest.getVendorId());
		po.setStatus(filterRequest.getStatus());
		po.setEndDate(filterRequest.getEndDate());
		po.setStartDate(filterRequest.getStartDate());
		po.setFromAmount(filterRequest.getFromAmount());
		po.setToAmount(filterRequest.getToAmount());
		po.setUserId(filterRequest.getUserId());
		return po;

	}

	public DeclinePoVo convertVpDeclineVoFromRequest(DeclinePoRequest request) {
		DeclinePoVo po = new DeclinePoVo();
		po.setOrgId(request.getOrgId());
		po.setPoId(request.getPoId());
		po.setReason(request.getReason());
		po.setReasonId(request.getReasonId());
		po.setRoleName(request.getRoleName());
		return po;

	}
}
