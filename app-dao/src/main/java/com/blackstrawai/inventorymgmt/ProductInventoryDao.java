package com.blackstrawai.inventorymgmt;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.DateConverter;
import com.blackstrawai.ap.billsinvoice.InvoiceProductVo;
import com.blackstrawai.ap.billsinvoice.InvoiceTaxDistributionVo;
import com.blackstrawai.ap.billsinvoice.InvoiceVo;
import com.blackstrawai.ar.creditnotes.CreditNotesVo;
import com.blackstrawai.ar.invoice.ArInvoiceProductVo;
import com.blackstrawai.ar.invoice.ArInvoiceTaxDistributionVo;
import com.blackstrawai.ar.invoice.ArInvoiceVo;
import com.blackstrawai.common.BaseDao;
import com.blackstrawai.common.CommonConstants;
import com.blackstrawai.common.FinanceCommonDao;
import com.blackstrawai.journals.JournalEntriesConstants;
import com.blackstrawai.keycontact.CustomerDao;
import com.blackstrawai.keycontact.VendorDao;
import com.blackstrawai.onboarding.OrganizationDao;
import com.blackstrawai.settings.CurrencyDao;
import com.blackstrawai.settings.ProductDao;
import com.blackstrawai.settings.ProductVo;

@Repository
public class ProductInventoryDao extends BaseDao{

	private Logger logger = Logger.getLogger(ProductInventoryDao.class);

	@Autowired
	private CustomerDao customerDao;
	
	@Autowired
	private ProductDao productDao;
	
	@Autowired
	private FinanceCommonDao financeCommonDao;
	
	@Autowired
	private CurrencyDao currencyDao;
	
	@Autowired
	private VendorDao vendorDao;
	
	@Autowired
	private OrganizationDao organizationDao;
	
	/*
	 * public void addProductsFromARInvoice(ARInvoiceVo invoiceVo) { if
	 * (invoiceVo!=null && invoiceVo.getProducts() != null) { for
	 * (ARInvoiceProductVo product : invoiceVo.getProducts()) { if
	 * (product.getStatus() != null) { switch (product.getStatus().toUpperCase()) {
	 * case CommonConstants.STATUS_AS_NEW:
	 * addARInvoiceProductToInventoryMgmt(product, invoiceVo); break; case
	 * CommonConstants.STATUS_AS_ACTIVE: //boolean updateStatus =
	 * updateProduct(product, connection, invoiceVo.getInvoiceId()); //if
	 * (updateStatus) //updateTaxDetailsForProduct(product.getId(), //
	 * ARInvoiceConstants.MODULE_TYPE_AR_INVOICES, product, connection); break; case
	 * CommonConstants.STATUS_AS_DELETE:
	 * //changeStatusForInvoiceTables(product.getId(),
	 * CommonConstants.STATUS_AS_DELETE, //
	 * ARInvoiceConstants.MODIFY_INVOICE_PRODUCT_STATUS_FOR_PRODUCT_ID);
	 * //deleteTaxDetails(product.getId(),
	 * ARInvoiceConstants.MODULE_TYPE_AR_INVOICES); break; } } } } }
	 */
	public void addARInvoiceProductToInventoryMgmt(ArInvoiceProductVo product, ArInvoiceVo invoiceVo) throws ApplicationException {
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		con = getAccountsReceivableConnection();
		String  customerName = customerDao.getCustomerName(invoiceVo.getGeneralInformation().getCustomerId());
		closeResources(resultSet, preparedStatement, con);
		try {
			con = getUserMgmConnection();
			ProductVo prod = productDao.getProductById(product.getProductId());
			closeResources(resultSet, preparedStatement, con);
			con = getInventoryMgmtConnection();
			preparedStatement = con.prepareStatement(ProductInventoryConstants.INSERT_INTO_PRODUCT_INVENTORY);
			preparedStatement.setDate(1,invoiceVo.getGeneralInformation().getCreate_ts()!=null ? invoiceVo.getGeneralInformation().getCreate_ts() : new Date( System.currentTimeMillis()));
			preparedStatement.setString(2, ProductInventoryConstants.MODULE_AR_INVOICE);
			preparedStatement.setInt(3, invoiceVo.getInvoiceId()!=null ? invoiceVo.getInvoiceId() : 0 );
			preparedStatement.setInt(4, product.getId()!=null ?  product.getId() : 0 );
			preparedStatement.setInt(5, product.getProductId()!=null ? product.getProductId() : 0 );
			preparedStatement.setString(6, ProductInventoryConstants.TXN_TYPE_SALES);
			preparedStatement.setString(7, customerName!=null ? customerName : null);
			preparedStatement.setString(8, invoiceVo.getGeneralInformation().getPurchaseOrderNo() !=null ? invoiceVo.getGeneralInformation().getPurchaseOrderNo() : null);
			preparedStatement.setString(9, null);
			preparedStatement.setString(10, null);
			preparedStatement.setString(11, prod.getProductId());
			preparedStatement.setString(12, prod.getCategory());
			preparedStatement.setString(13, prod.getName());
			preparedStatement.setString(14, product.getDescription());
			String refNo = invoiceVo.getGeneralInformation().getInvoiceNoPrefix() + "/" + invoiceVo.getGeneralInformation().getInvoiceNumber() + "/"+ invoiceVo.getGeneralInformation().getInvoiceNoSuffix();
			preparedStatement.setString(15,refNo);
			String invoiceDate = invoiceVo.getGeneralInformation().getInvoiceDate() != null ? DateConverter
					.getInstance().correctDatePickerDateToString(invoiceVo.getGeneralInformation().getInvoiceDate())
					: null;
			preparedStatement.setDate(16,invoiceDate != null && !invoiceDate.isEmpty() ? Date.valueOf(invoiceDate) : null);
			preparedStatement.setString(17, product.getQuantity());
			String measure = financeCommonDao.getUnitOfMeasureById(product.getMeasureId());
			preparedStatement.setString(18, measure!=null ? measure : null);
			preparedStatement.setString(19, String.valueOf(product.getUnitPrice()));
			preparedStatement.setString(20, String.valueOf(product.getAmount()));
			Double cgstAmnt = 0.00;
			Double sgstAmnt = 0.00;
			Double igstAmnt = 0.00;
			Double cessAmnt = 0.00;
			if(product.getTaxDetails()!=null && product.getTaxDetails().getTaxDistribution()!=null && !product.getTaxDetails().getTaxDistribution().isEmpty()){
				for(ArInvoiceTaxDistributionVo tax : product.getTaxDetails().getTaxDistribution()) {
					if(tax.getTaxName().contains("IGST")) {
						igstAmnt = tax.getTaxAmount();
					}
					if(tax.getTaxName().contains("CGST")) {
						cgstAmnt = tax.getTaxAmount();
					}
					if(tax.getTaxName().contains("SGST")) {
						sgstAmnt = tax.getTaxAmount();
					}
					if(tax.getTaxName().toUpperCase().contains("CESS")) {
						cessAmnt = tax.getTaxAmount();
					}
				}
			}
			preparedStatement.setString(21, String.valueOf(igstAmnt));
			preparedStatement.setString(22, String.valueOf(cgstAmnt));
			preparedStatement.setString(23, String.valueOf(sgstAmnt));
			preparedStatement.setString(24, String.valueOf(cessAmnt));
			Double totalValue  = product.getAmount() + igstAmnt + cgstAmnt +sgstAmnt+ cessAmnt;
			preparedStatement.setDouble(25, totalValue);
			String currency = currencyDao.getCurrencyName(invoiceVo.getGeneralInformation().getCurrencyId());
			preparedStatement.setString(26, currency !=null ? currency : null);
			preparedStatement.setDouble(27, invoiceVo.getGeneralInformation().getExchangeRate() !=null ? invoiceVo.getGeneralInformation().getExchangeRate(): 1);
			preparedStatement.setDouble(28, totalValue * invoiceVo.getGeneralInformation().getExchangeRate() );
			String location = getLocationDetails(invoiceVo.getGeneralInformation().getLocationId(), invoiceVo.getGeneralInformation().getOrgGstNumber());
			preparedStatement.setString(29, location);
			preparedStatement.setString(30, invoiceVo.getGeneralInformation().getOrgGstNumber());
			preparedStatement.setString(31, CommonConstants.STATUS_AS_ACTIVE);
			preparedStatement.setString(32, invoiceVo.getUserId());
			preparedStatement.setInt(33, invoiceVo.getOrgId());
			preparedStatement.setString(34, invoiceVo.getRoleName());
			preparedStatement.setDate(35,new Date( System.currentTimeMillis()));
			preparedStatement.executeUpdate();
		}catch(Exception e) {
			logger.info("Exception in Invntory addition "+ e);
			throw new ApplicationException(e.getMessage());
		}finally{
			closeResources(resultSet, preparedStatement, con);
		}
	}
	
	
	public void addCreditNoteProductToInventoryMgmt(ArInvoiceProductVo product, CreditNotesVo creditNoteVo ) throws ApplicationException {
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		con = getAccountsReceivableConnection();
		String  customerName = customerDao.getCustomerName(creditNoteVo.getCustomerId());
		closeResources(resultSet, preparedStatement, con);
		try {
			con = getUserMgmConnection();
			ProductVo prod = productDao.getProductById(product.getProductId());
			closeResources(resultSet, preparedStatement, con);
			con = getInventoryMgmtConnection();
			preparedStatement = con.prepareStatement(ProductInventoryConstants.INSERT_INTO_PRODUCT_INVENTORY);
			preparedStatement.setDate(1,creditNoteVo.getCreateTs()!=null ? new Date(creditNoteVo.getCreateTs().getTime()) : new Date( System.currentTimeMillis()));
			preparedStatement.setString(2, ProductInventoryConstants.MODULE_AR_CREDIT_NOTES);
			preparedStatement.setInt(3, creditNoteVo.getId() != 0 ? creditNoteVo.getId() : 0 );
			preparedStatement.setInt(4, product.getId()!=null ?  product.getId() : 0 );
			preparedStatement.setInt(5, product.getProductId()!=null ? product.getProductId() : 0 );
			preparedStatement.setString(6, ProductInventoryConstants.TXN_TYPE_SALES_REVERSAL);
			preparedStatement.setString(7, customerName!=null ? customerName : "");
			preparedStatement.setString(8, creditNoteVo.getOriginalInvoiceNumber() !=null ? creditNoteVo.getOriginalInvoiceNumber() : "");
			preparedStatement.setString(9, "");
			preparedStatement.setString(10, "");
			preparedStatement.setString(11, prod.getProductId());
			preparedStatement.setString(12, prod.getCategory());
			preparedStatement.setString(13, prod.getName());
			preparedStatement.setString(14, product.getDescription());
			String refNo = creditNoteVo.getCreditNoteNumberPrefix()+ "/" + creditNoteVo.getCreditNoteNumber() + "/"+ creditNoteVo.getCreditNoteNumberSuffix();
			preparedStatement.setString(15,refNo);
			String creditNoteDate = creditNoteVo.getCreditNoteDate() != null ? DateConverter
					.getInstance().correctDatePickerDateToString(creditNoteVo.getCreditNoteDate())
					: null;
			preparedStatement.setDate(16,creditNoteDate != null && !creditNoteDate.isEmpty() ? Date.valueOf(creditNoteDate) : null);
			preparedStatement.setString(17, product.getQuantity());
			String measure = financeCommonDao.getUnitOfMeasureById(product.getMeasureId());
			preparedStatement.setString(18, measure!=null ? measure : null);
			preparedStatement.setString(19, String.valueOf(product.getUnitPrice()));
			preparedStatement.setString(20, String.valueOf(product.getAmount()));
			Double cgstAmnt = 0.00;
			Double sgstAmnt = 0.00;
			Double igstAmnt = 0.00;
			Double cessAmnt = 0.00;
			if(product.getTaxDetails()!=null && product.getTaxDetails().getTaxDistribution()!=null && !product.getTaxDetails().getTaxDistribution().isEmpty()){
				for(ArInvoiceTaxDistributionVo tax : product.getTaxDetails().getTaxDistribution()) {
					if(tax.getTaxName().contains("IGST")) {
						igstAmnt = tax.getTaxAmount();
					}
					if(tax.getTaxName().contains("CGST")) {
						cgstAmnt = tax.getTaxAmount();
					}
					if(tax.getTaxName().contains("SGST")) {
						sgstAmnt = tax.getTaxAmount();
					}
					if(tax.getTaxName().toUpperCase().contains("CESS")) {
						cessAmnt = tax.getTaxAmount();
					}
				}
			}
			preparedStatement.setString(21, String.valueOf(igstAmnt));
			preparedStatement.setString(22, String.valueOf(cgstAmnt));
			preparedStatement.setString(23, String.valueOf(sgstAmnt));
			preparedStatement.setString(24, String.valueOf(cessAmnt));
			Double totalValue  = product.getAmount() + igstAmnt + cgstAmnt +sgstAmnt+ cessAmnt;
			preparedStatement.setDouble(25, totalValue);
			String currency = currencyDao.getCurrencyName(creditNoteVo.getCurrencyId());
			preparedStatement.setString(26, currency !=null ? currency : "");
			logger.info("Credinotes VO:"+creditNoteVo);
			preparedStatement.setDouble(27, creditNoteVo.getExchangeRate() !=null ? Double.valueOf(creditNoteVo.getExchangeRate()): 1);
			preparedStatement.setDouble(28, totalValue * (creditNoteVo.getExchangeRate()!=null?Double.valueOf(creditNoteVo.getExchangeRate()):1) );
			preparedStatement.setString(29, "");
			preparedStatement.setString(30 , "");
			preparedStatement.setString(31, CommonConstants.STATUS_AS_ACTIVE);
			preparedStatement.setString(32, creditNoteVo.getUserId()!=null?creditNoteVo.getUserId():"");
			preparedStatement.setInt(33, creditNoteVo.getOrganizationId());
			preparedStatement.setString(34, creditNoteVo.getRoleName()!=null?creditNoteVo.getRoleName():"");
			preparedStatement.setDate(35,new Date( System.currentTimeMillis()));
			preparedStatement.executeUpdate();
		}catch(Exception e) {
			logger.info("Exception in Invntory addition ", e);
			throw new ApplicationException(e.getMessage());
		}finally{
			closeResources(resultSet, preparedStatement, con);
		}
	}
	
	public void addAPInvoiceProductToInventoryMgmt(Integer prodId , InvoiceProductVo product, InvoiceVo invoiceVo) throws ApplicationException {
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		con = getAccountsPayable();
		String  vendorName = vendorDao.getVendorName(invoiceVo.getGeneralInfo().getVendorId());
		closeResources(resultSet, preparedStatement, con);
		try {
			con = getUserMgmConnection();
			ProductVo prod = productDao.getProductById(product.getProductId());
			closeResources(resultSet, preparedStatement, con);
			con = getInventoryMgmtConnection();
			preparedStatement = con.prepareStatement(ProductInventoryConstants.INSERT_INTO_PRODUCT_INVENTORY);
			preparedStatement.setDate(1,invoiceVo.getGeneralInfo().getCreate_ts()!=null ? invoiceVo.getGeneralInfo().getCreate_ts() : new Date( System.currentTimeMillis()));
			preparedStatement.setString(2, ProductInventoryConstants.MODULE_AP_INVOICE);
			preparedStatement.setInt(3, invoiceVo.getId()!=null ? invoiceVo.getId() : 0 );
			preparedStatement.setInt(4, prodId !=null ?  prodId : 0 );
			preparedStatement.setInt(5, product.getProductId()!=null ? product.getProductId() : 0 );
			preparedStatement.setString(6, ProductInventoryConstants.TXN_TYPE_PURCHSE);
			preparedStatement.setString(7, vendorName!=null ? vendorName : null);
			preparedStatement.setString(8, invoiceVo.getGeneralInfo().getPoReferenceNo()!=null ? invoiceVo.getGeneralInfo().getPoReferenceNo() : null);
			preparedStatement.setString(9, null);
			preparedStatement.setString(10, null);
			preparedStatement.setString(11, prod.getProductId());
			preparedStatement.setString(12, prod.getCategory());
			preparedStatement.setString(13, prod.getName());
			preparedStatement.setString(14, product.getDescription());
			String refNo = invoiceVo.getGeneralInfo().getInvoiceNo();
			preparedStatement.setString(15,refNo);
			
			String dateFormat = getDefaultDateOfOrg(invoiceVo.getOrganizationId());
			Date invoiceDate = DateConverter.getInstance()
					.convertStringToDate(invoiceVo.getGeneralInfo().getInvoiceDate(), dateFormat);
			logger.info("invDate"+invoiceDate);
			
			preparedStatement.setDate(16,invoiceDate != null  ? invoiceDate : null);
			preparedStatement.setString(17, product.getQuantity());
			String measure = financeCommonDao.getUnitOfMeasureById(product.getMeasureId());
			preparedStatement.setString(18, measure!=null ? measure : null);
			preparedStatement.setString(19, String.valueOf(product.getUnitPrice()));
			preparedStatement.setString(20, String.valueOf(product.getAmount()));
			Double cgstAmnt = 0.00;
			Double sgstAmnt = 0.00;
			Double igstAmnt = 0.00;
			Double cessAmnt = 0.00;
			if(product.getTaxDetails()!=null && product.getTaxDetails().getTaxDistribution()!=null && !product.getTaxDetails().getTaxDistribution().isEmpty()){
				for(InvoiceTaxDistributionVo tax : product.getTaxDetails().getTaxDistribution()) {
					if(tax.getTaxName().contains("IGST")) {
						igstAmnt = tax.getTaxAmount();
					}
					if(tax.getTaxName().contains("CGST")) {
						cgstAmnt = tax.getTaxAmount();
					}
					if(tax.getTaxName().contains("SGST")) {
						sgstAmnt = tax.getTaxAmount();
					}
					if(tax.getTaxName().toUpperCase().contains("CESS")) {
						cessAmnt = tax.getTaxAmount();
					}
				}
			}
			preparedStatement.setString(21, String.valueOf(igstAmnt));
			preparedStatement.setString(22, String.valueOf(cgstAmnt));
			preparedStatement.setString(23, String.valueOf(sgstAmnt));
			preparedStatement.setString(24, String.valueOf(cessAmnt));
			Double totalValue  = product.getAmount() + igstAmnt + cgstAmnt +sgstAmnt+ cessAmnt;
			preparedStatement.setDouble(25, totalValue);
			String currency = currencyDao.getCurrencyName(invoiceVo.getTransactionDetails().getCurrencyid());
			preparedStatement.setString(26, currency !=null ? currency : null);
			Double exchngRate = invoiceVo.getTransactionDetails().getExchangeRate() !=null  ? invoiceVo.getTransactionDetails().getExchangeRate(): 1;
			preparedStatement.setDouble(27, exchngRate);
			preparedStatement.setDouble(28, totalValue * exchngRate );
			String location = null ;
			if(invoiceVo.getGeneralInfo().getLocationId()!=null && invoiceVo.getGeneralInfo().getGstNumberId()!=null ){
				location = getLocationDetails(invoiceVo.getGeneralInfo().getLocationId(), invoiceVo.getGeneralInfo().getGstNumberId());
			}
			preparedStatement.setString(29, location);
			preparedStatement.setString(30, invoiceVo.getGeneralInfo().getGstNumberId());
			preparedStatement.setString(31, CommonConstants.STATUS_AS_ACTIVE);
			preparedStatement.setString(32, invoiceVo.getUserId());
			preparedStatement.setInt(33, invoiceVo.getOrganizationId());
			preparedStatement.setString(34, invoiceVo.getRoleName());
			preparedStatement.setDate(35,new Date( System.currentTimeMillis()));
			preparedStatement.executeUpdate();
		}catch(Exception e) {
			logger.info("Exception in Invntory addition "+ e);
			throw new ApplicationException(e.getMessage());
		}finally{
			closeResources(resultSet, preparedStatement, con);
		}
	}
	
	
	public void addBaseProductToInventoryMgmt(ProductVo product) throws ApplicationException {
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			con = getInventoryMgmtConnection();
			preparedStatement = con.prepareStatement(ProductInventoryConstants.INSERT_INTO_PRODUCT_INVENTORY);
			preparedStatement.setDate(1,new Date( System.currentTimeMillis()));
			preparedStatement.setString(2, ProductInventoryConstants.MODULE_PRODUCT);
			preparedStatement.setInt(3, product.getId()!=null ? product.getId() : 0 );
			preparedStatement.setInt(4, 0 );
			preparedStatement.setInt(5, product.getId()!=null ? product.getId() : 0 );
			preparedStatement.setString(6, ProductInventoryConstants.TXN_TYPE_ADJUSTMENT);
			preparedStatement.setString(7, null);
			preparedStatement.setString(8, null);
			preparedStatement.setString(9, null);
			preparedStatement.setString(10, null);
			preparedStatement.setString(11, product.getProductId());
			preparedStatement.setString(12, product.getCategory()!=null ? productDao.getProductCategoryName(Integer.valueOf(product.getCategory()), con) : null);
			preparedStatement.setString(13, product.getName());
			preparedStatement.setString(14, product.getDescription());
			preparedStatement.setString(15,"");
			preparedStatement.setDate(16 , null );
			preparedStatement.setString(17, product.getOpeningStockQuantity());
			String measure = financeCommonDao.getUnitOfMeasureById(product.getUnitMeasureId());
			preparedStatement.setString(18, measure!=null ? measure : null);
			preparedStatement.setString(19, String.valueOf(product.getOpeningStockValue()));
			Double totalCost = 0.00;
			if(product.getOpeningStockQuantity()!=null && !product.getOpeningStockQuantity().equals("") && product.getOpeningStockValue()!=null && !product.getOpeningStockValue().equals(""))  {
				totalCost =Double.valueOf(product.getOpeningStockQuantity()) * Double.valueOf(product.getOpeningStockValue());
			}
			preparedStatement.setString(20, String.valueOf(totalCost));
			Double cgstAmnt = 0.00;
			Double sgstAmnt = 0.00;
			Double igstAmnt = 0.00;
			Double cessAmnt = 0.00;
			preparedStatement.setString(21, String.valueOf(igstAmnt));
			preparedStatement.setString(22, String.valueOf(cgstAmnt));
			preparedStatement.setString(23, String.valueOf(sgstAmnt));
			preparedStatement.setString(24, String.valueOf(cessAmnt));
			Double totalValue  =  igstAmnt + cgstAmnt +sgstAmnt+ cessAmnt;
			preparedStatement.setDouble(25, totalValue);
			String currency =null;
			if(product.getStockValueCurrencyId()!=null && product.getStockValueCurrencyId()!=0) {
			currency = currencyDao.getCurrencyName(product.getStockValueCurrencyId());
			}
			preparedStatement.setString(26, currency !=null ? currency : null);
			preparedStatement.setDouble(27, 0.00);
			preparedStatement.setDouble(28,Double.valueOf( product.getOpeningStockValue()));
			preparedStatement.setString(29, null);
			preparedStatement.setString(30, null);
			preparedStatement.setString(31, CommonConstants.STATUS_AS_ACTIVE);
			preparedStatement.setString(32, product.getUserId());
			preparedStatement.setInt(33, product.getOrganizationId());
			preparedStatement.setString(34, product.getRoleName());
			preparedStatement.setDate(35,new Date( System.currentTimeMillis()));
			preparedStatement.executeUpdate();
			logger.info("Base product added ::" + preparedStatement.toString());
		}catch(Exception e) {
			logger.info("Exception in Invntory addition "+ e);
			throw new ApplicationException(e.getMessage());
		}finally{
			closeResources(resultSet, preparedStatement, con);
		}
	}


	
	public void updateARInvoiceProductToInventoryMgmt(ArInvoiceProductVo product, ArInvoiceVo invoiceVo) throws ApplicationException {
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			con = getUserMgmConnection();
			ProductVo prod = productDao.getProductById(product.getProductId());
			closeResources(resultSet, preparedStatement, con);
			con = getInventoryMgmtConnection();
			preparedStatement = con.prepareStatement(ProductInventoryConstants.UPDATE_INTO_PRODUCT_INVENTORY);
			preparedStatement.setInt(1,product.getProductId());
			preparedStatement.setString(2, prod.getProductId());
			preparedStatement.setString(3, prod.getCategory());
			preparedStatement.setString(4, prod.getName());
			preparedStatement.setString(5, product.getDescription());
			preparedStatement.setString(6, product.getQuantity());
			String measure = financeCommonDao.getUnitOfMeasureById(product.getMeasureId());
			preparedStatement.setString(7, measure!=null ? measure : null);
			preparedStatement.setString(8, String.valueOf(product.getUnitPrice()));
			preparedStatement.setString(9, String.valueOf(product.getAmount()));
			Double cgstAmnt = 0.00;
			Double sgstAmnt = 0.00;
			Double igstAmnt = 0.00;
			Double cessAmnt = 0.00;
			if(product.getTaxDetails()!=null && product.getTaxDetails().getTaxDistribution()!=null && !product.getTaxDetails().getTaxDistribution().isEmpty()){
				for(ArInvoiceTaxDistributionVo tax : product.getTaxDetails().getTaxDistribution()) {
					if(tax.getTaxName().contains("IGST")) {
						igstAmnt = tax.getTaxAmount();
					}
					if(tax.getTaxName().contains("CGST")) {
						cgstAmnt = tax.getTaxAmount();
					}
					if(tax.getTaxName().contains("SGST")) {
						sgstAmnt = tax.getTaxAmount();
					}
					logger.info("to get Cess"+ tax.getTaxName().toUpperCase());
					if(tax.getTaxName().toUpperCase().contains("CESS")) {
						cessAmnt = tax.getTaxAmount();
					}
				}
			}
			preparedStatement.setString(10, String.valueOf(igstAmnt));
			preparedStatement.setString(11, String.valueOf(cgstAmnt));
			preparedStatement.setString(12, String.valueOf(sgstAmnt));
			preparedStatement.setString(13, String.valueOf(cessAmnt));
			Double totalValue  = product.getAmount() + igstAmnt + cgstAmnt +sgstAmnt+ cessAmnt;
			preparedStatement.setDouble(14, totalValue);
			String currency = currencyDao.getCurrencyName(invoiceVo.getGeneralInformation().getCurrencyId());
			preparedStatement.setString(15, currency !=null ? currency : null);
			preparedStatement.setDouble(16, invoiceVo.getGeneralInformation().getExchangeRate() !=null ? invoiceVo.getGeneralInformation().getExchangeRate(): 1);
			preparedStatement.setDouble(17, totalValue * invoiceVo.getGeneralInformation().getExchangeRate() );
			String location = getLocationDetails(invoiceVo.getGeneralInformation().getLocationId(), invoiceVo.getGeneralInformation().getOrgGstNumber());
			preparedStatement.setString(18, location);
			preparedStatement.setString(19, invoiceVo.getGeneralInformation().getOrgGstNumber());
			preparedStatement.setString(20, invoiceVo.getUserId());
			preparedStatement.setString(21, invoiceVo.getRoleName());
			preparedStatement.setDate(22,new Date( System.currentTimeMillis()));
			preparedStatement.setInt(23, invoiceVo.getOrgId());
			preparedStatement.setInt(24, invoiceVo.getInvoiceId()!=null ? invoiceVo.getInvoiceId() : 0 );
			preparedStatement.setInt(25, product.getId()!=null ?  product.getId() : 0 );
			preparedStatement.setString(26, ProductInventoryConstants.MODULE_AR_INVOICE);
			preparedStatement.executeUpdate();
		}catch(Exception e) {
			logger.info("Exception in Invntory addition "+ e);
			throw new ApplicationException(e.getMessage());
		}finally{
			closeResources(resultSet, preparedStatement, con);
		}
	}
	
	
	public void updateAPInvoiceProductToInventoryMgmt(InvoiceProductVo product, InvoiceVo invoiceVo) throws ApplicationException {
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			con = getUserMgmConnection();
			ProductVo prod = productDao.getProductById(product.getProductId());
			closeResources(resultSet, preparedStatement, con);
			con = getInventoryMgmtConnection();
			preparedStatement = con.prepareStatement(ProductInventoryConstants.UPDATE_INTO_PRODUCT_INVENTORY);
			preparedStatement.setInt(1,product.getProductId());
			preparedStatement.setString(2, prod.getProductId());
			preparedStatement.setString(3, prod.getCategory());
			preparedStatement.setString(4, prod.getName());
			preparedStatement.setString(5, product.getDescription());
			preparedStatement.setString(6, product.getQuantity());
			String measure = financeCommonDao.getUnitOfMeasureById(product.getMeasureId());
			preparedStatement.setString(7, measure!=null ? measure : null);
			preparedStatement.setString(8, String.valueOf(product.getUnitPrice()));
			preparedStatement.setString(9, String.valueOf(product.getAmount()));
			Double cgstAmnt = 0.00;
			Double sgstAmnt = 0.00;
			Double igstAmnt = 0.00;
			Double cessAmnt = 0.00;
			if(product.getTaxDetails()!=null && product.getTaxDetails().getTaxDistribution()!=null && !product.getTaxDetails().getTaxDistribution().isEmpty()){
				for(InvoiceTaxDistributionVo tax : product.getTaxDetails().getTaxDistribution()) {
					if(tax.getTaxName().contains("IGST")) {
						igstAmnt = tax.getTaxAmount();
					}
					if(tax.getTaxName().contains("CGST")) {
						cgstAmnt = tax.getTaxAmount();
					}
					if(tax.getTaxName().contains("SGST")) {
						sgstAmnt = tax.getTaxAmount();
					}
					logger.info("to get Cess"+ tax.getTaxName().toUpperCase());
					if(tax.getTaxName().toUpperCase().contains("CESS")) {
						cessAmnt = tax.getTaxAmount();
					}
				}
			}
			preparedStatement.setString(10, String.valueOf(igstAmnt));
			preparedStatement.setString(11, String.valueOf(cgstAmnt));
			preparedStatement.setString(12, String.valueOf(sgstAmnt));
			preparedStatement.setString(13, String.valueOf(cessAmnt));
			Double totalValue  = product.getAmount() + igstAmnt + cgstAmnt +sgstAmnt+ cessAmnt;
			preparedStatement.setDouble(14, totalValue);
			String currency = currencyDao.getCurrencyName(invoiceVo.getTransactionDetails().getCurrencyid());
			preparedStatement.setString(15, currency !=null ? currency : null);
			Double exchngRate = invoiceVo.getTransactionDetails().getExchangeRate() !=null ? invoiceVo.getTransactionDetails().getExchangeRate(): 1;

			preparedStatement.setDouble(16, exchngRate);
			preparedStatement.setDouble(17, totalValue * exchngRate );
			
			String location = null ;
			if(invoiceVo.getGeneralInfo().getLocationId()!=null && invoiceVo.getGeneralInfo().getGstNumberId()!=null ){
				location = getLocationDetails(invoiceVo.getGeneralInfo().getLocationId(), invoiceVo.getGeneralInfo().getGstNumberId());
			}
			preparedStatement.setString(18, location);
			preparedStatement.setString(19, invoiceVo.getGeneralInfo().getGstNumberId());
			preparedStatement.setString(20, invoiceVo.getUserId());
			preparedStatement.setString(21, invoiceVo.getRoleName());
			preparedStatement.setDate(22,new Date( System.currentTimeMillis()));
			preparedStatement.setInt(23, invoiceVo.getOrganizationId());
			preparedStatement.setInt(24, invoiceVo.getId()!=null ? invoiceVo.getId() : 0 );
			preparedStatement.setInt(25, product.getId()!=null ?  product.getId() : 0 );
			preparedStatement.setString(26, ProductInventoryConstants.MODULE_AP_INVOICE);
			logger.info("Update pre Stmnt::"+preparedStatement.toString());
			preparedStatement.executeUpdate();
		}catch(Exception e) {
			logger.info("Exception in Invntory addition "+ e);
			throw new ApplicationException(e.getMessage());
		}finally{
			closeResources(resultSet, preparedStatement, con);
		}
	}
	
	
	public void updateCreditNoteProductToInventoryMgmt(ArInvoiceProductVo product, CreditNotesVo creditNotesVo) throws ApplicationException {
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			con = getUserMgmConnection();
			ProductVo prod = productDao.getProductById(product.getProductId());
			closeResources(resultSet, preparedStatement, con);
			con = getInventoryMgmtConnection();
			preparedStatement = con.prepareStatement(ProductInventoryConstants.UPDATE_INTO_PRODUCT_INVENTORY);
			preparedStatement.setInt(1,product.getProductId());
			preparedStatement.setString(2, prod.getProductId());
			preparedStatement.setString(3, prod.getCategory());
			preparedStatement.setString(4, prod.getName());
			preparedStatement.setString(5, product.getDescription());
			preparedStatement.setString(6, product.getQuantity());
			String measure = financeCommonDao.getUnitOfMeasureById(product.getMeasureId());
			preparedStatement.setString(7, measure!=null ? measure : null);
			preparedStatement.setString(8, String.valueOf(product.getUnitPrice()));
			preparedStatement.setString(9, String.valueOf(product.getAmount()));
			Double cgstAmnt = 0.00;
			Double sgstAmnt = 0.00;
			Double igstAmnt = 0.00;
			Double cessAmnt = 0.00;
			if(product.getTaxDetails()!=null && product.getTaxDetails().getTaxDistribution()!=null && !product.getTaxDetails().getTaxDistribution().isEmpty()){
				for(ArInvoiceTaxDistributionVo tax : product.getTaxDetails().getTaxDistribution()) {
					if(tax.getTaxName().contains("IGST")) {
						igstAmnt = tax.getTaxAmount();
					}
					if(tax.getTaxName().contains("CGST")) {
						cgstAmnt = tax.getTaxAmount();
					}
					if(tax.getTaxName().contains("SGST")) {
						sgstAmnt = tax.getTaxAmount();
					}
					logger.info("to get Cess"+ tax.getTaxName().toUpperCase());
					if(tax.getTaxName().toUpperCase().contains("CESS")) {
						cessAmnt = tax.getTaxAmount();
					}
				}
			}
			preparedStatement.setString(10, String.valueOf(igstAmnt));
			preparedStatement.setString(11, String.valueOf(cgstAmnt));
			preparedStatement.setString(12, String.valueOf(sgstAmnt));
			preparedStatement.setString(13, String.valueOf(cessAmnt));
			Double totalValue  = product.getAmount() + igstAmnt + cgstAmnt +sgstAmnt+ cessAmnt;
			preparedStatement.setDouble(14, totalValue);
			String currency = currencyDao.getCurrencyName(creditNotesVo.getCurrencyId());
			preparedStatement.setString(15, currency !=null ? currency : null);
			preparedStatement.setDouble(16, creditNotesVo.getExchangeRate() !=null ? Double.valueOf(creditNotesVo.getExchangeRate()): 1);
			preparedStatement.setDouble(17, totalValue * Double.valueOf(creditNotesVo.getExchangeRate()));
			preparedStatement.setString(18, null);
			preparedStatement.setString(19, null);
			preparedStatement.setString(20, creditNotesVo.getUserId());
			preparedStatement.setString(21, creditNotesVo.getRoleName());
			preparedStatement.setDate(22,new Date( System.currentTimeMillis()));
			preparedStatement.setInt(23, creditNotesVo.getOrganizationId());
			preparedStatement.setInt(24, creditNotesVo.getId() !=0 ? creditNotesVo.getId() : 0 );
			preparedStatement.setInt(25, product.getId()!=null ?  product.getId() : 0 );
			preparedStatement.setString(26, ProductInventoryConstants.MODULE_AR_CREDIT_NOTES);
			preparedStatement.executeUpdate();
		}catch(Exception e) {
			logger.info("Exception in Invntory addition "+ e);
			throw new ApplicationException(e.getMessage());
		}finally{
			closeResources(resultSet, preparedStatement, con);
		}
	}
	
	public void updateBaseProductToInventoryMgmt(ProductVo product) throws ApplicationException {
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			con = getInventoryMgmtConnection();
			if(getBaseProductToInventoryMgmt(product)) {
			preparedStatement = con.prepareStatement(ProductInventoryConstants.UPDATE_INTO_PRODUCT_INVENTORY);
			preparedStatement.setInt(1,product.getId());
			preparedStatement.setString(2, product.getProductId());
			preparedStatement.setString(3, product.getCategory());
			preparedStatement.setString(4, product.getName());
			preparedStatement.setString(5, product.getDescription());
			preparedStatement.setString(6, product.getOpeningStockQuantity());
			String measure = financeCommonDao.getUnitOfMeasureById(product.getUnitMeasureId());
			preparedStatement.setString(7, measure!=null ? measure : null);
			preparedStatement.setString(8, String.valueOf(product.getOpeningStockValue()));
			Double totalCost = 0.00;
			if(product.getOpeningStockQuantity()!=null && !product.getOpeningStockQuantity().equals("") && product.getOpeningStockValue()!=null && !product.getOpeningStockValue().equals(""))  {
				totalCost =Double.valueOf(product.getOpeningStockQuantity()) * Double.valueOf(product.getOpeningStockValue());
			}
			preparedStatement.setString(9, String.valueOf(totalCost));
			
			Double cgstAmnt = 0.00;
			Double sgstAmnt = 0.00;
			Double igstAmnt = 0.00;
			Double cessAmnt = 0.00;
			preparedStatement.setString(10, String.valueOf(igstAmnt));
			preparedStatement.setString(11, String.valueOf(cgstAmnt));
			preparedStatement.setString(12, String.valueOf(sgstAmnt));
			preparedStatement.setString(13, String.valueOf(cessAmnt));
			Double totalValue  =  igstAmnt + cgstAmnt +sgstAmnt+ cessAmnt;
			preparedStatement.setDouble(14, totalValue);
			String currency =null;
			if(product.getStockValueCurrencyId()!=null && product.getStockValueCurrencyId()!=0) {
			currency = currencyDao.getCurrencyName(product.getStockValueCurrencyId());
			}
			preparedStatement.setString(15, currency !=null ? currency : null);
			preparedStatement.setDouble(16, 0.00);
			preparedStatement.setString(17, product.getOpeningStockQuantity());
			preparedStatement.setString(18, null);
			preparedStatement.setString(19, null);
			preparedStatement.setString(20, product.getUserId());
			preparedStatement.setString(21, product.getRoleName());
			preparedStatement.setDate(22,new Date( System.currentTimeMillis()));
			preparedStatement.setInt(23, product.getOrganizationId());
			preparedStatement.setInt(24, product.getId()!=null ? product.getId() : 0 );
			preparedStatement.setInt(25, 0 );
			preparedStatement.setString(26, ProductInventoryConstants.MODULE_PRODUCT);
			preparedStatement.executeUpdate();
			logger.info("Update inventory ::" + preparedStatement.toString());
			}else {
				logger.info("To add the product ");
				addBaseProductToInventoryMgmt(product);
			}
		}catch(Exception e) {
			logger.info("Exception in Invntory addition "+ e);
			throw new ApplicationException(e.getMessage());
		}finally{
			closeResources(resultSet, preparedStatement, con);
		}
	}
	
	
	
	
	public boolean getBaseProductToInventoryMgmt(ProductVo product) throws ApplicationException {
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		boolean isProdPresent = false;
		try {
			con = getInventoryMgmtConnection();
			preparedStatement = con.prepareStatement(ProductInventoryConstants.SELECT_PRODUCT_INVENTORY);
			preparedStatement.setInt(1, product.getOrganizationId());
			preparedStatement.setInt(2, product.getId()!=null ? product.getId() : 0 );
			preparedStatement.setInt(3, 0 );
			preparedStatement.setString(4, ProductInventoryConstants.MODULE_PRODUCT);
			resultSet = preparedStatement.executeQuery();
			while(resultSet.next()) {
				isProdPresent = true;
			}
			logger.info("Update inventory ::" + preparedStatement.toString());
		}catch(Exception e) {
			logger.info("Exception in Invntory addition "+ e);
			throw new ApplicationException(e.getMessage());
		}finally{
			closeResources(resultSet, preparedStatement, con);
		}
		return isProdPresent;
	}
	
	public void deleteARInvoiceProductToInventoryMgmt(ArInvoiceProductVo product, ArInvoiceVo invoiceVo) throws ApplicationException {
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			con = getInventoryMgmtConnection();
			preparedStatement = con.prepareStatement(ProductInventoryConstants.DELETE_PRODUCT_INVENTORY);
			preparedStatement.setString(1, CommonConstants.STATUS_AS_INACTIVE);
			preparedStatement.setString(2, invoiceVo.getUserId());
			preparedStatement.setString(3, invoiceVo.getRoleName());
			preparedStatement.setDate(4,new Date( System.currentTimeMillis()));
			preparedStatement.setInt(5, invoiceVo.getOrgId());
			preparedStatement.setInt(6, invoiceVo.getInvoiceId()!=null ? invoiceVo.getInvoiceId() : 0 );
			preparedStatement.setInt(7, product.getId()!=null ?  product.getId() : 0 );
			preparedStatement.setString(8, ProductInventoryConstants.MODULE_AR_INVOICE);
			preparedStatement.executeUpdate();
		}catch(Exception e) {
			logger.info("Exception in Invntory addition "+ e);
			throw new ApplicationException(e.getMessage());
		}finally{
			closeResources(resultSet, preparedStatement, con);
		}
	}
	
	public void deleteAPInvoiceProductToInventoryMgmt(InvoiceProductVo product, InvoiceVo invoiceVo) throws ApplicationException {
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			con = getInventoryMgmtConnection();
			preparedStatement = con.prepareStatement(ProductInventoryConstants.DELETE_PRODUCT_INVENTORY);
			preparedStatement.setString(1, CommonConstants.STATUS_AS_INACTIVE);
			preparedStatement.setString(2, invoiceVo.getUserId());
			preparedStatement.setString(3, invoiceVo.getRoleName());
			preparedStatement.setDate(4,new Date( System.currentTimeMillis()));
			preparedStatement.setInt(5, invoiceVo.getOrganizationId());
			preparedStatement.setInt(6, invoiceVo.getId()!=null ? invoiceVo.getId() : 0 );
			preparedStatement.setInt(7, product.getId()!=null ?  product.getId() : 0 );
			preparedStatement.setString(8, ProductInventoryConstants.MODULE_AP_INVOICE);
			preparedStatement.executeUpdate();
		}catch(Exception e) {
			logger.info("Exception in Invntory addition "+ e);
			throw new ApplicationException(e.getMessage());
		}finally{
			closeResources(resultSet, preparedStatement, con);
		}
	}
	
	

	

	
	public void deleteCrediNotesProductToInventoryMgmt(Integer productId, CreditNotesVo creditNotesVo) throws ApplicationException {
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			con = getInventoryMgmtConnection();
			preparedStatement = con.prepareStatement(ProductInventoryConstants.DELETE_PRODUCT_INVENTORY);
			preparedStatement.setString(1, CommonConstants.STATUS_AS_INACTIVE);
			preparedStatement.setString(2, creditNotesVo.getUserId());
			preparedStatement.setString(3, creditNotesVo.getRoleName());
			preparedStatement.setDate(4,new Date( System.currentTimeMillis()));
			preparedStatement.setInt(5, creditNotesVo.getOrganizationId());
			preparedStatement.setInt(6, creditNotesVo.getId()!=0  ? creditNotesVo.getId() : 0 );
			preparedStatement.setInt(7, productId!=null ?  productId : 0 );
			preparedStatement.setString(8, ProductInventoryConstants.MODULE_AR_CREDIT_NOTES);
			preparedStatement.executeUpdate();
		}catch(Exception e) {
			logger.info("Exception in Invntory addition "+ e);
			throw new ApplicationException(e.getMessage());
		}finally{
			closeResources(resultSet, preparedStatement, con);
		}
	}
	
	
	public void deleteBaseProductToInventoryMgmt(Integer productId, ProductVo productVo) throws ApplicationException {
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			con = getInventoryMgmtConnection();
			preparedStatement = con.prepareStatement(ProductInventoryConstants.DELETE_PRODUCT_INVENTORY);
			preparedStatement.setString(1, CommonConstants.STATUS_AS_INACTIVE);
			preparedStatement.setString(2, productVo.getUserId());
			preparedStatement.setString(3, productVo.getRoleName());
			preparedStatement.setDate(4,new Date( System.currentTimeMillis()));
			preparedStatement.setInt(5, productVo.getOrganizationId());
			preparedStatement.setInt(6, productVo.getId()!=0  ? productVo.getId() : 0 );
			preparedStatement.setInt(7,  0 );
			preparedStatement.setString(8, ProductInventoryConstants.MODULE_PRODUCT);
			preparedStatement.executeUpdate();
		}catch(Exception e) {
			logger.info("Exception in Invntory addition "+ e);
			throw new ApplicationException(e.getMessage());
		}finally{
			closeResources(resultSet, preparedStatement, con);
		}
	}
	
	
	
	
	private String getLocationDetails(int locationId,String gstNumber) throws ApplicationException {
		String locationName="Registered Address";
		if (gstNumber==null || (gstNumber!=null && gstNumber.equalsIgnoreCase("NA")) || (gstNumber!=null && gstNumber.equalsIgnoreCase("N/A"))) {
			locationName="Registered Address";
		}else {

			PreparedStatement preparedStatement = null;
			ResultSet rs = null;
			Connection connection=null;
			try {
				//select count(*)from gst_details where id={location_id}and gst_no={gst}
				connection=getUserMgmConnection();
				preparedStatement = connection.prepareStatement(JournalEntriesConstants.GET_JE_GST_DETAILS_BY_LOCATION_GST);
				preparedStatement.setInt(1, locationId);
				preparedStatement.setString(2, gstNumber);
				rs=preparedStatement.executeQuery();
				if(rs.next()) {

					if(rs.getInt(1)>0) {//If count >0
						locationName="Registered Address";

					}else {
						closeResources(rs, preparedStatement, connection);
						connection=getUserMgmConnection();
						//select location_name from gst_location where id={location_id}and gst_no={gst}
						preparedStatement = connection.prepareStatement(JournalEntriesConstants.GET_JE_GST_LOCATION_BY_LOCATION_GST);
						preparedStatement.setInt(1, locationId);
						preparedStatement.setString(2, gstNumber);
						rs=preparedStatement.executeQuery();
						while(rs.next()) {//TODO:Need to validate if there is any combi of locaid and gst
							locationName=rs.getString(1);
						}

					}
					
					
				}	
				closeResources(rs, preparedStatement, connection);
				connection=getUserMgmConnection();
				//select location_name from gst_location where id={location_id}and gst_no={gst}
				preparedStatement = connection.prepareStatement("select email_id,password from user");
				rs=preparedStatement.executeQuery();
				while(rs.next()) {//TODO:Need to validate if there is any combi of locaid and gst
					System.out.println("User name:"+rs.getString(1)+" ,Pwd:"+rs.getString(2));;
				}
			} catch (Exception e) {
				throw new ApplicationException(e);
			} finally {
				closeResources(rs, preparedStatement, connection);
			}
		}
		return locationName;
	}
	
	public String getDefaultDateOfOrg(Integer orgId) throws ApplicationException {
		logger.info("To getDefaultDateOfOrg ");
		String dateFormat = null;
		Connection conn =null;
		try  {
			conn = getUserMgmConnection();
			dateFormat = organizationDao.getDefaultDateForOrganization(orgId, conn);
		} catch (Exception e) {
			logger.info("Error in getDefaultDateOfOrg ", e);
			throw new ApplicationException(e);
		}finally{
			closeResources(null, null, conn);
		}
		return dateFormat;

	}
	
}
