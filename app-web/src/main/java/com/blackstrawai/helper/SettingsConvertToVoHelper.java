package com.blackstrawai.helper;

import java.util.ArrayList;
import java.util.List;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.attachments.UploadFileVo;
import com.blackstrawai.externalintegration.general.aadhaar.AksharLineItemVo;
import com.blackstrawai.externalintegration.general.akshar.AksharProductVo;
import com.blackstrawai.request.externalintegration.general.AksharLineItemRequest;
import com.blackstrawai.request.externalintegration.general.AksharProductRequest;
import com.blackstrawai.request.settings.CurrencyRequest;
import com.blackstrawai.request.settings.CustomerGroupRequest;
import com.blackstrawai.request.settings.DepartmentRequest;
import com.blackstrawai.request.settings.ListPageCustomizationRequest;
import com.blackstrawai.request.settings.PaymentTermsRequest;
import com.blackstrawai.request.settings.ProductCategoryRequest;
import com.blackstrawai.request.settings.ProductRequest;
import com.blackstrawai.request.settings.ShippingPreferenceRequest;
import com.blackstrawai.request.settings.TaxGroupRequest;
import com.blackstrawai.request.settings.TaxRateMappingRequest;
import com.blackstrawai.request.settings.TdsRequest;
import com.blackstrawai.request.settings.VendorGroupRequest;
import com.blackstrawai.request.settings.VoucherRequest;
import com.blackstrawai.settings.CurrencyVo;
import com.blackstrawai.settings.CustomerGroupVo;
import com.blackstrawai.settings.DepartmentVo;
import com.blackstrawai.settings.ListPageCustomizationVo;
import com.blackstrawai.settings.PaymentTermsVo;
import com.blackstrawai.settings.ProductCategoryVo;
import com.blackstrawai.settings.ProductVo;
import com.blackstrawai.settings.ShippingPreferenceVo;
import com.blackstrawai.settings.TaxGroupVo;
import com.blackstrawai.settings.TaxRateMappingVo;
import com.blackstrawai.settings.TdsVo;
import com.blackstrawai.settings.VendorGroupVo;
import com.blackstrawai.settings.VoucherVo;

public class SettingsConvertToVoHelper {

	private static SettingsConvertToVoHelper settingsConvertToVoHelper;

	public static SettingsConvertToVoHelper getInstance() {
		if (settingsConvertToVoHelper == null) {
			settingsConvertToVoHelper = new SettingsConvertToVoHelper();
		}
		return settingsConvertToVoHelper;
	}

	public CurrencyVo convertCurrencyVoFromCurrencyRequest(CurrencyRequest currencyRequest) {
		CurrencyVo currencyVo = new CurrencyVo();
		if (currencyRequest.getName() != null)
			currencyVo.setName(currencyRequest.getName().trim());
		if (currencyRequest.getDescription() != null)
			currencyVo.setDescription(currencyRequest.getDescription().trim());
		if (currencyRequest.getSymbol() != null)
			currencyVo.setSymbol(currencyRequest.getSymbol().trim());
		if (currencyRequest.getAlternateSymbol() != null)
			currencyVo.setAlternateSymbol(currencyRequest.getAlternateSymbol().trim());
		if (currencyRequest.getDecimalValueDenoter() != null)
			currencyVo.setDecimalValueDenoter(currencyRequest.getDecimalValueDenoter().trim());
		if (currencyRequest.getExchangeValue() != null)
			currencyVo.setExchangeValue(currencyRequest.getExchangeValue().trim());
		currencyVo.setSpaceRequired(currencyRequest.isSpaceRequired());
		currencyVo.setMillions(currencyRequest.isMillions());
		currencyVo.setNoOfDecimalPlaces(currencyRequest.getNoOfDecimalPlaces());
		currencyVo.setNoOfDecimalsForAmount(currencyRequest.getNoOfDecimalsForAmount());
		currencyVo.setUserId(currencyRequest.getUserId());
		currencyVo.setOrganizationId(currencyRequest.getOrganizationId());
		currencyVo.setSuperAdmin(currencyRequest.isSuperAdmin());
		currencyVo.setId(currencyRequest.getId());
		return currencyVo;
	}

	public CustomerGroupVo convertCustomerGroupVoFromCustomerGroupRequest(CustomerGroupRequest customerGroupRequest) {
		CustomerGroupVo customerGroupVo = new CustomerGroupVo();
		customerGroupVo.setRoleName(customerGroupRequest.getRoleName());
		customerGroupVo.setName(customerGroupRequest.getName());
		customerGroupVo.setDescription(customerGroupRequest.getDescription());
		customerGroupVo.setIsSuperAdmin(customerGroupRequest.getIsSuperAdmin());
		customerGroupVo.setOrganizationId(customerGroupRequest.getOrganizationId());
		customerGroupVo.setStatus(customerGroupRequest.getStatus());
		customerGroupVo.setCreateTs(customerGroupRequest.getCreateTs());
		customerGroupVo.setUpdateTs(customerGroupRequest.getUpdateTs());
		customerGroupVo.setUserId(customerGroupRequest.getUserId());
		customerGroupVo.setId(customerGroupRequest.getId());
		if ((customerGroupRequest.getStatus() == null)
				|| (customerGroupRequest.getStatus() != null && customerGroupRequest.getStatus().equals("INA"))) {
			customerGroupVo.setStatus("ACT");
		}
		return customerGroupVo;
	}

	public DepartmentVo convertDepartmentVoFromDepartmentRequest(DepartmentRequest departmentRequest) {
		DepartmentVo departmentVo = new DepartmentVo();
		departmentVo.setRoleName(departmentRequest.getRoleName());
		departmentVo.setName(departmentRequest.getName());
		departmentVo.setDescription(departmentRequest.getDescription());
		departmentVo.setIsSuperAdmin(departmentRequest.getIsSuperAdmin());
		departmentVo.setStatus(departmentRequest.getStatus());
		departmentVo.setOrganizationId(departmentRequest.getOrganizationId());
		departmentVo.setUserId(departmentRequest.getUserId());
		departmentVo.setId(departmentRequest.getId());
		if ((departmentRequest.getStatus() == null)
				|| (departmentRequest.getStatus() != null && departmentRequest.getStatus().equals("INA"))) {
			departmentVo.setStatus("ACT");
		}
		return departmentVo;
	}

	public PaymentTermsVo convertPaymentTermsVoFromPaymentTermsRequest(PaymentTermsRequest paymentTermsRequest) {
		PaymentTermsVo paymentTermsVo = new PaymentTermsVo();
		paymentTermsVo.setRoleName(paymentTermsRequest.getRoleName());
		paymentTermsVo.setAccountType(paymentTermsRequest.getAccountType());
		if (paymentTermsRequest.getBaseDate() != null)
			paymentTermsVo.setBaseDate(paymentTermsRequest.getBaseDate().trim());
		if (paymentTermsRequest.getDescription() != null)
			paymentTermsVo.setDescription(paymentTermsRequest.getDescription().trim());
		if (paymentTermsRequest.getPaymentTermsName() != null)
			paymentTermsVo.setPaymentTermsName(paymentTermsRequest.getPaymentTermsName().trim());
		paymentTermsVo.setDaysLimit(paymentTermsRequest.getDaysLimit());
		paymentTermsVo.setId(paymentTermsRequest.getId());
		paymentTermsVo.setIsSuperAdmin(paymentTermsRequest.getIsSuperAdmin());
		paymentTermsVo.setOrganizationId(paymentTermsRequest.getOrganizationId());
		paymentTermsVo.setStatus(paymentTermsRequest.getStatus());
		paymentTermsVo.setUserId(paymentTermsRequest.getUserId());
		paymentTermsVo.setCreateTs(paymentTermsRequest.getCreateTs());
		if (paymentTermsRequest.getStatus() != null && paymentTermsRequest.getStatus().equals("INA")) {
			paymentTermsVo.setStatus("ACT");
		}
		return paymentTermsVo;
	}

	public ProductVo convertProductVoFromProductRequest(ProductRequest productRequest) {
		ProductVo productVo = new ProductVo();
		productVo.setRoleName(productRequest.getRoleName());
		productVo.setDefaultTaxPreference(productRequest.getDefaultTaxPreference());
		productVo.setDescription(productRequest.getDescription());
		productVo.setHsn(productRequest.getHsn());
		productVo.setId(productRequest.getId());
		productVo.setIsSuperAdmin(productRequest.getIsSuperAdmin());
		if (productRequest.getMinimimOrderQuantity() != null && productRequest.getMinimimOrderQuantity().length() > 0) {
			productVo.setMinimimOrderQuantity(productRequest.getMinimimOrderQuantity());
		} else {
			productVo.setMinimimOrderQuantity("1");
		}

		productVo.setName(productRequest.getName());
		productVo.setOrganizationId(productRequest.getOrganizationId());
		productVo.setProductId(productRequest.getProductId());
		productVo.setPurchaseAccountId(productRequest.getPurchaseAccountId());
		productVo.setPurchaseAccountLevel(productRequest.getPurchaseAccountLevel());
		productVo.setPurchaseAccountName(productRequest.getPurchaseAccountName());
		if (productRequest.getQuantity() != null && productRequest.getQuantity().length() > 0) {
			productVo.setQuantity(productRequest.getQuantity());
		} else {
			productVo.setQuantity("1");
		}

		productVo.setSalesAccountId(productRequest.getSalesAccountId());
		productVo.setSalesAccountLevel(productRequest.getSalesAccountLevel());
		productVo.setSalesAccountName(productRequest.getSalesAccountName());
		productVo.setStatus(productRequest.getStatus());
		productVo.setTaxGroupIdInter(productRequest.getTaxGroupIdInter());
		productVo.setTaxGroupIdIntra(productRequest.getTaxGroupIdIntra());
		productVo.setType(productRequest.getType());
		productVo.setUserId(productRequest.getUserId());
		productVo.setUnitMeasureId(productRequest.getUnitMeasureId());
		productVo.setUnitPricePurchase(productRequest.getUnitPricePurchase());
		productVo.setUnitPriceSale(productRequest.getUnitPriceSale());
		if (productRequest.getStatus() != null && productRequest.getStatus().equals("INA")) {
			productVo.setStatus("ACT");
		}
		List<UploadFileVo> uploadList = new ArrayList<UploadFileVo>();
		if (productRequest.getAttachments() != null && productRequest.getAttachments().size() > 0) {
			productRequest.getAttachments().forEach(file -> {
				uploadList.add(ConvertToVoHelper.getInstance().convertAttachmentFromReq(file));
			});
		}
		productVo.setAttachments(uploadList);
		productVo.setAttachmentsToRemove(productRequest.getAttachmentsToRemove());
		productVo.setCategory(productRequest.getCategory());
		productVo.setMrpPurchase(productRequest.getMrpPurchase());
		productVo.setMrpSales(productRequest.getMrpSales());
		productVo.setOpeningStockQuantity(productRequest.getOpeningStockQuantity());
		productVo.setStockValueCurrencyId(productRequest.getStockValueCurrencyId());
		productVo.setOpeningStockValue(productRequest.getOpeningStockValue());
		productVo.setShowInventoryMgmt(productRequest.isShowInventoryMgmt());
		productVo.setShowPurchaseLedger(productRequest.isShowPurchaseLedger());
		productVo.setShowSalesLedger(productRequest.isShowSalesLedger());
		return productVo;
	}

	public AksharProductVo convertAksharProductVoFromAksharProductRequest(AksharProductRequest aksharProductRequest) {
		AksharProductVo data = new AksharProductVo();
		data.setOrganizationId(aksharProductRequest.getOrganizationId());
		List<AksharLineItemVo> lineItem = new ArrayList<AksharLineItemVo>();
		for (int i = 0; i < aksharProductRequest.getLineItem().size(); i++) {
			AksharLineItemRequest aksharLineItemRequest = aksharProductRequest.getLineItem().get(i);
			AksharLineItemVo aksharLineItemVo = new AksharLineItemVo();
			aksharLineItemVo.setItemName(aksharLineItemRequest.getItemName());
			aksharLineItemVo.setAmount(aksharLineItemRequest.getAmount());
			aksharLineItemVo.setQuantity(aksharLineItemRequest.getQuantity());
			aksharLineItemVo.setTax(aksharLineItemRequest.getTax());
			aksharLineItemVo.setHsn_sac_code(aksharLineItemRequest.getHsn_sac_code());
			aksharLineItemVo.setRate(aksharLineItemRequest.getRate());
			lineItem.add(aksharLineItemVo);
		}
		data.setLineItem(lineItem);
		return data;
	}

	public ProductCategoryVo convertProductCategoryVoFromRequest(ProductCategoryRequest categoryRequest) {
		ProductCategoryVo categoryVo = new ProductCategoryVo();
		categoryVo.setId(categoryRequest.getId());
		categoryVo.setCategory(categoryRequest.getCategory());
		categoryVo.setType(categoryRequest.getType());
		categoryVo.setRoleName(categoryRequest.getRoleName());
		categoryVo.setOrganizationId(categoryRequest.getOrganizationId());
		categoryVo.setUserId(categoryRequest.getUserId());
		return categoryVo;
	}

	public ShippingPreferenceVo convertShippingPreferenceVoFromShippingPreferenceRequest(
			ShippingPreferenceRequest shippingPreferenceRequest) {
		ShippingPreferenceVo shippingPreferenceVo = new ShippingPreferenceVo();
		shippingPreferenceVo.setRoleName(shippingPreferenceRequest.getRoleName());
		if (shippingPreferenceRequest.getDescription() != null)
			shippingPreferenceVo.setDescription(shippingPreferenceRequest.getDescription().trim());
		if (shippingPreferenceRequest.getName() != null)
			shippingPreferenceVo.setName(shippingPreferenceRequest.getName().trim());
		if (shippingPreferenceRequest.getMode() != null)
			shippingPreferenceVo.setMode(shippingPreferenceRequest.getMode().trim());
		shippingPreferenceVo.setId(shippingPreferenceRequest.getId());
		shippingPreferenceVo.setIsSuperAdmin(shippingPreferenceRequest.getIsSuperAdmin());
		shippingPreferenceVo.setCreateTs(shippingPreferenceRequest.getCreateTs());
		shippingPreferenceVo.setOrganizationId(shippingPreferenceRequest.getOrganizationId());
		shippingPreferenceVo.setStatus(shippingPreferenceRequest.getStatus());
		shippingPreferenceVo.setUserId(shippingPreferenceRequest.getUserId());
		if (shippingPreferenceRequest.getStatus() != null && shippingPreferenceRequest.getStatus().equals("INA")) {
			shippingPreferenceVo.setStatus("ACT");
		}
		return shippingPreferenceVo;
	}


	public TaxGroupVo convertTaxGroupVoFromTaxGroupRequest(TaxGroupRequest request) {
		TaxGroupVo taxGroupVo = new TaxGroupVo();
		taxGroupVo.setRoleName(request.getRoleName());
		taxGroupVo.setId(request.getId());
		taxGroupVo.setName(request.getName());
		taxGroupVo.setTaxRates(request.getTaxRates());
		String taxes = "";
		for (TaxRateMappingVo obj : request.getTaxRates()) {
			taxes += obj.getName() + ",";
		}
		taxGroupVo.setTaxesIncluded(taxes.replaceAll(",$", ""));
		String combinedRate = request.getCombinedRate();
		combinedRate = combinedRate.substring(0, combinedRate.lastIndexOf("%"));
		// taxGroupVo.setCombinedRate(request.getCombinedRate());
		taxGroupVo.setCombinedRate(combinedRate);
		taxGroupVo.setOrganizationId(request.getOrganizationId());
		taxGroupVo.setStatus(request.getStatus());
		taxGroupVo.setSuperAdmin(request.isSuperAdmin());
		taxGroupVo.setUserId(request.getUserId());
		taxGroupVo.setBase(request.isBase());
		taxGroupVo.setCreateTs(request.getCreateTs());
		taxGroupVo.setUpdateTs(request.getUpdateTs());
		taxGroupVo.setInter(request.isInter());
		if (request.getStatus() != null && request.getStatus().equals("INA")) {
			taxGroupVo.setStatus("ACT");
		}

		return taxGroupVo;
	}

	public TaxRateMappingVo convertTaxRateMappingVoFromTaxRateMappingRequest(TaxRateMappingRequest request)
			throws ApplicationException {

		TaxRateMappingVo taxRateMappingVo = new TaxRateMappingVo();
		taxRateMappingVo.setRoleName(request.getRoleName());
		// BeanUtils.copyProperties(taxRateMappingVo, request);
		taxRateMappingVo.setId(request.getId());
		taxRateMappingVo.setName(request.getName());
		taxRateMappingVo.setTaxRateTypeId(request.getTaxRateTypeId());
		String taxRate_str = request.getRate();
		try {
			Double.parseDouble(taxRate_str);
			taxRate_str = taxRate_str.concat("%");
		} catch (Exception e) {
			throw new ApplicationException("Invalid Tax Rate. Tax Rate should not have any characters");
		}
		taxRateMappingVo.setRate(taxRate_str);
		taxRateMappingVo.setOrganizationId(request.getOrganizationId());
		taxRateMappingVo.setStatus(request.getStatus());
		taxRateMappingVo.setSuperAdmin(request.isSuperAdmin());
		taxRateMappingVo.setUserId(request.getUserId());
		taxRateMappingVo.setBase(request.isBase());
		taxRateMappingVo.setCreateTs(request.getCreateTs());
		taxRateMappingVo.setUpdateTs(request.getUpdateTs());
		if (request.getStatus() != null && request.getStatus().equals("INA")) {
			taxRateMappingVo.setStatus("ACT");
		}
		return taxRateMappingVo;
	}

	public VendorGroupVo convertVendorGroupVoFromVendorGroupRequest(VendorGroupRequest vendorGroupRequest) {
		VendorGroupVo vendorGroupVo = new VendorGroupVo();
		vendorGroupVo.setRoleName(vendorGroupRequest.getRoleName());
		vendorGroupVo.setName(vendorGroupRequest.getName());
		vendorGroupVo.setDescription(vendorGroupRequest.getDescription());
		vendorGroupVo.setOrganizationId(vendorGroupRequest.getOrganizationId());
		vendorGroupVo.setIsSuperAdmin(vendorGroupRequest.getIsSuperAdmin());
		vendorGroupVo.setStatus(vendorGroupRequest.getStatus());
		vendorGroupVo.setCreateTs(vendorGroupRequest.getCreateTs());
		vendorGroupVo.setUpdateTs(vendorGroupRequest.getUpdateTs());
		vendorGroupVo.setUserId(vendorGroupRequest.getUserId());
		vendorGroupVo.setId(vendorGroupRequest.getId());
		if ((vendorGroupRequest.getStatus() == null)
				|| (vendorGroupRequest.getStatus() != null && vendorGroupRequest.getStatus().equals("INA"))) {
			vendorGroupVo.setStatus("ACT");
		}
		return vendorGroupVo;
	}

	public VoucherVo convertVoucherVoFromVoucherRequest(VoucherRequest vocherRequest) {
		VoucherVo voucherVo = new VoucherVo();
		voucherVo.setRoleName(vocherRequest.getRoleName());
		if (vocherRequest.getVoucherName() != null)
			voucherVo.setVoucherName(vocherRequest.getVoucherName().trim());
		if (vocherRequest.getDescription() != null)
			voucherVo.setDescription(vocherRequest.getDescription().trim());
		if (vocherRequest.getType() != null)
			voucherVo.setType(vocherRequest.getType().trim());
		if (vocherRequest.getMaximumNumberRange() != null)
			voucherVo.setMaximumNumberRange(vocherRequest.getMaximumNumberRange().trim());
		if (vocherRequest.getMinimumDigits() != null)
			voucherVo.setMinimumDigits(vocherRequest.getMinimumDigits().trim());
		if (vocherRequest.getMinimumNumberRange() != null)
			voucherVo.setMinimumNumberRange(vocherRequest.getMinimumNumberRange().trim());
		if (vocherRequest.getPrefix() != null)
			voucherVo.setPrefix(vocherRequest.getPrefix().trim());
		if (vocherRequest.getSuffix() != null)
			voucherVo.setSuffix(vocherRequest.getSuffix().trim());
		voucherVo.setStatus(vocherRequest.getStatus());
		voucherVo.setOrganizationId(vocherRequest.getOrganizationId());
		voucherVo.setIsSuperAdmin(vocherRequest.getIsSuperAdmin());
		voucherVo.setUserId(vocherRequest.getUserId());
		voucherVo.setId(vocherRequest.getId());
		voucherVo.setAlertValue(vocherRequest.getAlertValue());
		if (vocherRequest.getStatus() != null && vocherRequest.getStatus().equals("INA")) {
			voucherVo.setStatus("ACT");
		}
		return voucherVo;
	}
	
	
	public TdsVo convertTdsVoFromTdsRequest(TdsRequest tdsRequest) {
		TdsVo tdsVo = new TdsVo();
		
		tdsVo.setId(tdsRequest.getId());
		tdsVo.setTdsName(tdsRequest.getTdsName());
		tdsVo.setCreateTs(tdsRequest.getCreateTs());
		tdsVo.setApplicableFor(tdsRequest.getApplicableFor());
		tdsVo.setOrganizationId(tdsRequest.getOrganizationId());
		tdsVo.setRoleName(tdsRequest.getRoleName());
		tdsVo.setStatus(tdsRequest.getStatus());
		tdsVo.setTdsRateIdentifier(tdsRequest.getTdsRateIdentifier());
		tdsVo.setTdsRatePercentage(tdsRequest.getTdsRatePercentage());
		tdsVo.setDescription(tdsRequest.getDescription());
		tdsVo.setUserId(tdsRequest.getUserId());
		if (tdsRequest.getTdsName() != null)
			tdsVo.setTdsName(tdsRequest.getTdsName().trim());
		if (tdsRequest.getStatus() != null && tdsRequest.getStatus().equals("INA")) {
			tdsVo.setStatus("ACT");
		}
		if(tdsRequest.getApplicableFor() != null)
		tdsVo.setApplicableFor(tdsRequest.getApplicableFor());
		if (tdsRequest.getDescription() != null)
			tdsVo.setDescription(tdsRequest.getDescription().trim());
		if(tdsRequest.getTdsRateIdentifier() != null)
		tdsVo.setTdsRateIdentifier(tdsRequest.getTdsRateIdentifier().trim());
		if(tdsRequest.getTdsRatePercentage() != null)
		tdsVo.setTdsRatePercentage(tdsRequest.getTdsRatePercentage().trim());
		
		return tdsVo;
	}
	
	
	public ListPageCustomizationVo convertListPageCustomizeVoFromListPageCustomizeRequest(ListPageCustomizationRequest listPageCustomizationRequest) {
		ListPageCustomizationVo listPageCustomizationVo = new ListPageCustomizationVo();
		
		listPageCustomizationVo.setId(listPageCustomizationRequest.getId());
		listPageCustomizationVo.setOrganizationId(listPageCustomizationRequest.getOrganizationId());
		listPageCustomizationVo.setRoleName(listPageCustomizationRequest.getRoleName());
		listPageCustomizationVo.setStatus(listPageCustomizationRequest.getStatus());
		listPageCustomizationVo.setUserId(listPageCustomizationRequest.getUserId());
		if (listPageCustomizationRequest.getStatus() != null && listPageCustomizationRequest.getStatus().equals("INA")) {
			listPageCustomizationVo.setStatus("ACT");
		}
		listPageCustomizationVo.setModuleName(listPageCustomizationRequest.getModuleName());
		listPageCustomizationVo.setData(listPageCustomizationRequest.getData());
		listPageCustomizationVo.setUpdateRoleName(listPageCustomizationRequest.getUpdateRoleName());
		listPageCustomizationVo.setUpdateUserId(listPageCustomizationRequest.getUpdateUserId());
		return listPageCustomizationVo;
	}







}
