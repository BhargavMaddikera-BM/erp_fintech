package com.blackstrawai.settings;

import java.util.List;

import com.blackstrawai.attachments.UploadFileVo;
import com.blackstrawai.common.BaseVo;

public class ProductVo extends BaseVo {

	private Integer id;
	private String type;
	private String productId;
	private String name;
	private String hsn;
	private String description;
	private String category;
	private Integer unitMeasureId;
	private String defaultTaxPreference;
	private Integer taxGroupIdInter;
	private Integer taxGroupIdIntra;
	private String unitPricePurchase;
	private Integer purchaseAccountId;
	private String purchaseAccountLevel;
	private String purchaseAccountName;
	private String minimimOrderQuantity;
	private String mrpPurchase;
	private Integer salesAccountId;
	private String salesAccountName;
	private String salesAccountLevel;
	private String quantity;
	private String unitPriceSale;
	private String mrpSales;
	private Integer organizationId;
	private Boolean isSuperAdmin;
	private String status;
	private String roleName="Super Admin";
	private boolean isExist;
	private List<UploadFileVo> attachments;
	private List<Integer> attachmentsToRemove;
	private String openingStockQuantity;
	private Integer stockValueCurrencyId;
	private String openingStockValue;
	private boolean showPurchaseLedger;
	private boolean showSalesLedger;
	private boolean showInventoryMgmt;
	
	
	
	public boolean isShowPurchaseLedger() {
		return showPurchaseLedger;
	}

	public void setShowPurchaseLedger(boolean showPurchaseLedger) {
		this.showPurchaseLedger = showPurchaseLedger;
	}

	public boolean isShowSalesLedger() {
		return showSalesLedger;
	}

	public void setShowSalesLedger(boolean showSalesLedger) {
		this.showSalesLedger = showSalesLedger;
	}

	public boolean isShowInventoryMgmt() {
		return showInventoryMgmt;
	}

	public void setShowInventoryMgmt(boolean showInventoryMgmt) {
		this.showInventoryMgmt = showInventoryMgmt;
	}

	public String getOpeningStockQuantity() {
		return openingStockQuantity;
	}

	public void setOpeningStockQuantity(String openingStockQuantity) {
		this.openingStockQuantity = openingStockQuantity;
	}

	public Integer getStockValueCurrencyId() {
		return stockValueCurrencyId;
	}

	public void setStockValueCurrencyId(Integer stockValueCurrencyId) {
		this.stockValueCurrencyId = stockValueCurrencyId;
	}

	public String getOpeningStockValue() {
		return openingStockValue;
	}

	public void setOpeningStockValue(String openingStockValue) {
		this.openingStockValue = openingStockValue;
	}

	public List<UploadFileVo>  getAttachments() {
		return attachments;
	}

	public void setAttachments(List<UploadFileVo>  attachments) {
		this.attachments = attachments;
	}

	public List<Integer> getAttachmentsToRemove() {
		return attachmentsToRemove;
	}

	public void setAttachmentsToRemove(List<Integer> attachmentsToRemove) {
		this.attachmentsToRemove = attachmentsToRemove;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getMrpPurchase() {
		return mrpPurchase;
	}

	public void setMrpPurchase(String mrpPurchase) {
		this.mrpPurchase = mrpPurchase;
	}

	public String getMrpSales() {
		return mrpSales;
	}

	public void setMrpSales(String mrpSales) {
		this.mrpSales = mrpSales;
	}

	public boolean isExist() {
		return isExist;
	}

	public void setExist(boolean isExist) {
		this.isExist = isExist;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHsn() {
		return hsn;
	}

	public void setHsn(String hsn) {
		this.hsn = hsn;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getUnitMeasureId() {
		return unitMeasureId;
	}

	public void setUnitMeasureId(Integer unitMeasureId) {
		this.unitMeasureId = unitMeasureId;
	}

	public String getDefaultTaxPreference() {
		return defaultTaxPreference;
	}

	public void setDefaultTaxPreference(String defaultTaxPreference) {
		this.defaultTaxPreference = defaultTaxPreference;
	}

	

	public Integer getTaxGroupIdInter() {
		return taxGroupIdInter;
	}

	public void setTaxGroupIdInter(Integer taxGroupIdInter) {
		this.taxGroupIdInter = taxGroupIdInter;
	}

	public Integer getTaxGroupIdIntra() {
		return taxGroupIdIntra;
	}

	public void setTaxGroupIdIntra(Integer taxGroupIdIntra) {
		this.taxGroupIdIntra = taxGroupIdIntra;
	}

	public String getUnitPricePurchase() {
		return unitPricePurchase;
	}

	public void setUnitPricePurchase(String unitPricePurchase) {
		this.unitPricePurchase = unitPricePurchase;
	}

	public Integer getPurchaseAccountId() {
		return purchaseAccountId;
	}

	public void setPurchaseAccountId(Integer purchaseAccountId) {
		this.purchaseAccountId = purchaseAccountId;
	}

	public String getPurchaseAccountLevel() {
		return purchaseAccountLevel;
	}

	public void setPurchaseAccountLevel(String purchaseAccountLevel) {
		this.purchaseAccountLevel = purchaseAccountLevel;
	}

	public String getPurchaseAccountName() {
		return purchaseAccountName;
	}

	public void setPurchaseAccountName(String purchaseAccountName) {
		this.purchaseAccountName = purchaseAccountName;
	}

	public String getMinimimOrderQuantity() {
		return minimimOrderQuantity;
	}

	public void setMinimimOrderQuantity(String minimimOrderQuantity) {
		this.minimimOrderQuantity = minimimOrderQuantity;
	}

	public Integer getSalesAccountId() {
		return salesAccountId;
	}

	public void setSalesAccountId(Integer salesAccountId) {
		this.salesAccountId = salesAccountId;
	}

	public String getSalesAccountName() {
		return salesAccountName;
	}

	public void setSalesAccountName(String salesAccountName) {
		this.salesAccountName = salesAccountName;
	}

	public String getSalesAccountLevel() {
		return salesAccountLevel;
	}

	public void setSalesAccountLevel(String salesAccountLevel) {
		this.salesAccountLevel = salesAccountLevel;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public String getUnitPriceSale() {
		return unitPriceSale;
	}

	public void setUnitPriceSale(String unitPriceSale) {
		this.unitPriceSale = unitPriceSale;
	}

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

	public Boolean getIsSuperAdmin() {
		return isSuperAdmin;
	}

	public void setIsSuperAdmin(Boolean isSuperAdmin) {
		this.isSuperAdmin = isSuperAdmin;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
