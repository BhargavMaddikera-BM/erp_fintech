package com.blackstrawai.ar.invoice;

import java.sql.Date;

import com.blackstrawai.journals.GeneralLedgerVo;
import com.blackstrawai.keycontact.customer.CustomerBillingAddressVo;
import com.blackstrawai.keycontact.customer.CustomerDeliveryAddressVo;
import com.blackstrawai.onboarding.organization.BasicLocationDetailsVo;
import com.blackstrawai.settings.CurrencyVo;

public class ArInvoiceGeneralInformationVo {

	private Integer invoiceTypeId;
	
	private Integer locationId;
	
	private Boolean isRegistered;
	
	private BasicLocationDetailsVo location;
	
	private String orgGstNumber;
	
	private Integer customerId;
	
	private Integer customerAccountId;
	
	private String customerAccount;

	private String customerGstNumber;
	
	private String receiverName;
	
	private Boolean isWopUnderLut;
	
	private String LutDate;
	
	private String lutNumber;
	
	private String lutNo;
	
	private Integer placeOfSupply;
	
	private Integer supplyServiceId;
	
	private String supplyServiceName;
	
	private Boolean isReverseCharge;
	
	private Boolean isSoldByEcommerce;
	
	private String ecommerceGstIn;
	
	private String purchaseOrderNo;
	
	private String invoiceNoPrefix;
	
	private String invoiceNoSuffix;

	private String invoiceNumber;
	
	private String invoiceDate;
	
	private Integer exportTypeId;
	
	private String 	portCode;
	
	private String shippingBillNo;
	
	private String shippingBillDate;
	
	private Integer paymentTermsId;
	
	private String dueDate;
	
	private Integer taxApplicationId;
	
	private String termsAndConditions;
	
	private Integer bankId;
	
	private String bankType;
	
	private Double subTotal;
	
	private Double discountValue;
	
	private Integer discountAccountId;
	
	private String discountAccount;
	
	private Double adjustmentValue;
	
	private Integer adjustmentAccountId;
	
	private String adjustmentAccount;
	
	private Double total;
	private Double balanceDue;
	
	private Integer currencyId;
	
	private CurrencyVo currency;
	
	private Double exchangeRate;
	
	private Integer tdsId;
	
	private Double tdsValue;
	
	private String discountAccountLevel;
	
	private String adjustmentAccountLevel;
	
	private CustomerBillingAddressVo billingAddress;
	
	private CustomerDeliveryAddressVo deliveryAddress;
	
	private Date create_ts;
	
	private String notes;
	
	private Boolean togglePayment;
	
	private GeneralLedgerVo generalLedgerData ;
	
	public GeneralLedgerVo getGeneralLedgerData() {
		return generalLedgerData;
	}

	public void setGeneralLedgerData(GeneralLedgerVo generalLedgerData) {
		this.generalLedgerData = generalLedgerData;
	}

	public Boolean getTogglePayment() {
		return togglePayment;
	}

	public void setTogglePayment(Boolean togglePayment) {
		this.togglePayment = togglePayment;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public Date getCreate_ts() {
		return create_ts;
	}

	public void setCreate_ts(Date create_ts) {
		this.create_ts = create_ts;
	}

	public String getLutNo() {
		return lutNo;
	}

	public void setLutNo(String lutNo) {
		this.lutNo = lutNo;
	}

	public CurrencyVo getCurrency() {
		return currency;
	}

	public void setCurrency(CurrencyVo currency) {
		this.currency = currency;
	}

	public String getSupplyServiceName() {
		return supplyServiceName;
	}

	public void setSupplyServiceName(String supplyServiceName) {
		this.supplyServiceName = supplyServiceName;
	}

	public BasicLocationDetailsVo getLocation() {
		return location;
	}

	public void setLocation(BasicLocationDetailsVo location) {
		this.location = location;
	}

	public Double getBalanceDue() {
		return balanceDue;
	}

	public void setBalanceDue(Double balanceDue) {
		this.balanceDue = balanceDue;
	}

	public Integer getInvoiceTypeId() {
		return invoiceTypeId;
	}

	public void setInvoiceTypeId(Integer invoiceTypeId) {
		this.invoiceTypeId = invoiceTypeId;
	}

	public Integer getLocationId() {
		return locationId;
	}

	public void setLocationId(Integer locationId) {
		this.locationId = locationId;
	}

	public String getOrgGstNumber() {
		return orgGstNumber;
	}

	public void setOrgGstNumber(String orgGstNumber) {
		this.orgGstNumber = orgGstNumber;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public Integer getCustomerAccountId() {
		return customerAccountId;
	}

	public void setCustomerAccountId(Integer customerAccountId) {
		this.customerAccountId = customerAccountId;
	}

	public String getCustomerGstNumber() {
		return customerGstNumber;
	}

	public void setCustomerGstNumber(String customerGstNumber) {
		this.customerGstNumber = customerGstNumber;
	}

	public String getReceiverName() {
		return receiverName;
	}

	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	public Boolean getIsWopUnderLut() {
		return isWopUnderLut;
	}

	public void setIsWopUnderLut(Boolean isWopUnderLut) {
		this.isWopUnderLut = isWopUnderLut;
	}

	public String getLutDate() {
		return LutDate;
	}

	public void setLutDate(String lutDate) {
		LutDate = lutDate;
	}

	public String getLutNumber() {
		return lutNumber;
	}

	public void setLutNumber(String lutNumber) {
		this.lutNumber = lutNumber;
	}

	public Integer getPlaceOfSupply() {
		return placeOfSupply;
	}

	public void setPlaceOfSupply(Integer placeOfSupply) {
		this.placeOfSupply = placeOfSupply;
	}

	public Integer getSupplyServiceId() {
		return supplyServiceId;
	}

	public void setSupplyServiceId(Integer supplyServiceId) {
		this.supplyServiceId = supplyServiceId;
	}

	public Boolean getIsReverseCharge() {
		return isReverseCharge;
	}

	public void setIsReverseCharge(Boolean isReverseCharge) {
		this.isReverseCharge = isReverseCharge;
	}

	public Boolean getIsSoldByEcommerce() {
		return isSoldByEcommerce;
	}

	public void setIsSoldByEcommerce(Boolean isSoldByEcommerce) {
		this.isSoldByEcommerce = isSoldByEcommerce;
	}

	public String getEcommerceGstIn() {
		return ecommerceGstIn;
	}

	public void setEcommerceGstIn(String ecommerceGstIn) {
		this.ecommerceGstIn = ecommerceGstIn;
	}

	public String getPurchaseOrderNo() {
		return purchaseOrderNo;
	}

	public void setPurchaseOrderNo(String purchaseOrderNo) {
		this.purchaseOrderNo = purchaseOrderNo;
	}

	public String getInvoiceNoPrefix() {
		return invoiceNoPrefix;
	}

	public void setInvoiceNoPrefix(String invoiceNoPrefix) {
		this.invoiceNoPrefix = invoiceNoPrefix;
	}

	public String getInvoiceNoSuffix() {
		return invoiceNoSuffix;
	}

	public void setInvoiceNoSuffix(String invoiceNoSuffix) {
		this.invoiceNoSuffix = invoiceNoSuffix;
	}

	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public String getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(String invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public Integer getExportTypeId() {
		return exportTypeId;
	}

	public void setExportTypeId(Integer exportTypeId) {
		this.exportTypeId = exportTypeId;
	}

	public String getPortCode() {
		return portCode;
	}

	public void setPortCode(String portCode) {
		this.portCode = portCode;
	}

	public String getShippingBillNo() {
		return shippingBillNo;
	}

	public void setShippingBillNo(String shippingBillNo) {
		this.shippingBillNo = shippingBillNo;
	}

	public String getShippingBillDate() {
		return shippingBillDate;
	}

	public void setShippingBillDate(String shippingBillDate) {
		this.shippingBillDate = shippingBillDate;
	}

	public Integer getPaymentTermsId() {
		return paymentTermsId;
	}

	public void setPaymentTermsId(Integer paymentTermsId) {
		this.paymentTermsId = paymentTermsId;
	}

	public String getDueDate() {
		return dueDate;
	}

	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}

	public Integer getTaxApplicationId() {
		return taxApplicationId;
	}

	public void setTaxApplicationId(Integer taxApplicationId) {
		this.taxApplicationId = taxApplicationId;
	}

	public String getTermsAndConditions() {
		return termsAndConditions;
	}

	public void setTermsAndConditions(String termsAndConditions) {
		this.termsAndConditions = termsAndConditions;
	}

	public Integer getBankId() {
		return bankId;
	}

	public void setBankId(Integer bankId) {
		this.bankId = bankId;
	}

	public String getBankType() {
		return bankType;
	}

	public void setBankType(String bankType) {
		this.bankType = bankType;
	}

	public Double getSubTotal() {
		return subTotal;
	}

	public void setSubTotal(Double subTotal) {
		this.subTotal = subTotal;
	}

	public Double getDiscountValue() {
		return discountValue;
	}

	public void setDiscountValue(Double discountValue) {
		this.discountValue = discountValue;
	}

	public Integer getDiscountAccountId() {
		return discountAccountId;
	}

	public void setDiscountAccountId(Integer discountAccountId) {
		this.discountAccountId = discountAccountId;
	}

	public String getDiscountAccount() {
		return discountAccount;
	}

	public void setDiscountAccount(String discountAccount) {
		this.discountAccount = discountAccount;
	}

	public Double getAdjustmentValue() {
		return adjustmentValue;
	}

	public void setAdjustmentValue(Double adjustmentValue) {
		this.adjustmentValue = adjustmentValue;
	}

	public Integer getAdjustmentAccountId() {
		return adjustmentAccountId;
	}

	public void setAdjustmentAccountId(Integer adjustmentAccountId) {
		this.adjustmentAccountId = adjustmentAccountId;
	}

	public String getAdjustmentAccount() {
		return adjustmentAccount;
	}

	public void setAdjustmentAccount(String adjustmentAccount) {
		this.adjustmentAccount = adjustmentAccount;
	}

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

	public Integer getCurrencyId() {
		return currencyId;
	}

	public void setCurrencyId(Integer currencyId) {
		this.currencyId = currencyId;
	}

	public Double getExchangeRate() {
		return exchangeRate;
	}

	public void setExchangeRate(Double exchangeRate) {
		this.exchangeRate = exchangeRate;
	}

	public String getCustomerAccount() {
		return customerAccount;
	}

	public void setCustomerAccount(String customerAccount) {
		this.customerAccount = customerAccount;
	}

	public Integer getTdsId() {
		return tdsId;
	}

	public void setTdsId(Integer tdsId) {
		this.tdsId = tdsId;
	}

	public Double getTdsValue() {
		return tdsValue;
	}

	public void setTdsValue(Double tdsValue) {
		this.tdsValue = tdsValue;
	}

	public CustomerBillingAddressVo getBillingAddress() {
		return billingAddress;
	}

	public void setBillingAddress(CustomerBillingAddressVo billingAddress) {
		this.billingAddress = billingAddress;
	}

	public CustomerDeliveryAddressVo getDeliveryAddress() {
		return deliveryAddress;
	}

	public void setDeliveryAddress(CustomerDeliveryAddressVo deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
	}

	public String getDiscountAccountLevel() {
		return discountAccountLevel;
	}

	public void setDiscountAccountLevel(String discountAccountLevel) {
		this.discountAccountLevel = discountAccountLevel;
	}

	public String getAdjustmentAccountLevel() {
		return adjustmentAccountLevel;
	}

	public void setAdjustmentAccountLevel(String adjustmentAccountLevel) {
		this.adjustmentAccountLevel = adjustmentAccountLevel;
	}

	public Boolean getIsRegistered() {
		return isRegistered;
	}

	public void setIsRegistered(Boolean isRegistered) {
		this.isRegistered = isRegistered;
	}
	

	
}
