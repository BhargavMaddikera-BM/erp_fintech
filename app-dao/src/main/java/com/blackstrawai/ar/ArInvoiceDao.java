package com.blackstrawai.ar;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.DateConverter;
import com.blackstrawai.ar.applycredits.InvoiceDetailsVo;
import com.blackstrawai.ar.invoice.ArInvoiceFilterVo;
import com.blackstrawai.ar.invoice.ArInvoiceGeneralInformationVo;
import com.blackstrawai.ar.invoice.ArInvoiceListVo;
import com.blackstrawai.ar.invoice.ArInvoiceProductVo;
import com.blackstrawai.ar.invoice.ArInvoiceTaxDetailsVo;
import com.blackstrawai.ar.invoice.ArInvoiceTaxDistributionVo;
import com.blackstrawai.ar.invoice.ArInvoiceVo;
import com.blackstrawai.ar.lut.LetterOfUndertakingVo;
import com.blackstrawai.ar.receipt.BasicReceiptVo;
import com.blackstrawai.attachments.AttachmentsConstants;
import com.blackstrawai.attachments.AttachmentsDao;
import com.blackstrawai.attachments.AttachmentsVo;
import com.blackstrawai.attachments.UploadFileVo;
import com.blackstrawai.common.BaseDao;
import com.blackstrawai.common.CommonConstants;
import com.blackstrawai.inventorymgmt.ProductInventoryDao;
import com.blackstrawai.journals.GeneralLedgerVo;
import com.blackstrawai.keycontact.CustomerDao;
import com.blackstrawai.keycontact.customer.CustomerBillingAddressVo;
import com.blackstrawai.onboarding.OrganizationDao;
import com.blackstrawai.settings.CurrencyDao;
import com.blackstrawai.settings.CurrencyVo;
import com.blackstrawai.workflow.WorkflowInvoiceVo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysql.jdbc.Statement;

@Repository
public class ArInvoiceDao extends BaseDao {

	private Logger logger = Logger.getLogger(ArInvoiceDao.class);

	@Autowired
	private CustomerDao customerDao;

	@Autowired
	private OrganizationDao organizationDao;

	@Autowired
	private LetterOfUndertakingDao lutDao;

	@Autowired
	private CurrencyDao currencyDao;

	@Autowired
	private AttachmentsDao attachmentsDao;

	@Autowired
	private ArBalanceUpdateDao balanceUpdateDao;

	@Autowired
	private ProductInventoryDao inventoryDao;

	public ArInvoiceVo createInvoice(ArInvoiceVo invoiceVo) throws ApplicationException, SQLException {
		logger.info("To create a new Invoice in BillsInvoiceDao:::" + invoiceVo);
		Connection connection = null;
		if (invoiceVo != null) {
			try {
				connection = getAccountsReceivableConnection();
				connection.setAutoCommit(false);
				createInvoiceGeneralInfo(invoiceVo, connection);
				if (invoiceVo.getInvoiceId() != null && invoiceVo.getProducts() != null
						&& invoiceVo.getProducts().size() > 0) {
					for (ArInvoiceProductVo product : invoiceVo.getProducts()) {
						Integer productId = createProduct(product, connection, invoiceVo.getInvoiceId());
						product.setId(productId);
						createTaxDetailsForProduct(productId, product, connection);
						inventoryDao.addARInvoiceProductToInventoryMgmt(product, invoiceVo);
					}
				}
				attachmentsDao.createAttachments(invoiceVo.getOrgId(), invoiceVo.getUserId(),
						invoiceVo.getAttachments(), AttachmentsConstants.MODULE_TYPE_ACCOUNTS_RECEIVABLES_INVOICE,
						invoiceVo.getInvoiceId(), invoiceVo.getRoleName());
				connection.commit();
				logger.info("Succecssfully created invoice in BillsInvoiceDao");
			} catch (ApplicationException | SQLException e) {
				logger.info("Error in createInvoice:: ", e);
				connection.rollback();
				logger.info("Before throw in dao :: " + e.getMessage());
				throw new ApplicationException(e.getMessage());
			} finally {
				if (connection != null) {
					connection.close();
				}
			}
		}
		return invoiceVo;
	}

	private ArInvoiceVo createInvoiceGeneralInfo(ArInvoiceVo invoiceVo, Connection connection)
			throws ApplicationException {
		logger.info("To insert into table ar_invoice_general_information ");
		if (invoiceVo != null && invoiceVo.getGeneralInformation() != null && invoiceVo.getOrgId() != null) {
			if (invoiceVo.getGeneralInformation().getInvoiceNumber() == null) {
				throw new ApplicationException("Invoice number is mandatory");
			}
			try (final PreparedStatement preparedStatement = connection.prepareStatement(
					ArInvoiceConstants.INSERT_INTO_INVOICE_GENERAL_INFO, Statement.RETURN_GENERATED_KEYS)) {
				StringBuilder invoiceNumberBuilder = new StringBuilder();
				invoiceNumberBuilder
				.append(invoiceVo.getGeneralInformation().getInvoiceNoPrefix() != null && !invoiceVo.getGeneralInformation().getInvoiceNoPrefix().isEmpty()
				? invoiceVo.getGeneralInformation().getInvoiceNoPrefix()
						: " ")
				.append("~").append(invoiceVo.getGeneralInformation().getInvoiceNumber()).append("~")
				.append(invoiceVo.getGeneralInformation().getInvoiceNoSuffix() != null && !invoiceVo.getGeneralInformation().getInvoiceNoSuffix().isEmpty()
				? invoiceVo.getGeneralInformation().getInvoiceNoSuffix()
						: " ");
				String invoiceNumber = invoiceNumberBuilder.toString();
				boolean isInvoiceExist = checkInvoiceExistForAnOrganization(invoiceVo, connection,
						invoiceNumber);
				if (isInvoiceExist) {
					throw new ApplicationException("Invoice number already exist for the organization");
				}
				// String dateFormat = getDefaultDateOfOrg(invoiceVo.getOrganizationId());
				preparedStatement.setInt(1,
						invoiceVo.getGeneralInformation().getLocationId() != null
						? invoiceVo.getGeneralInformation().getLocationId()
								: 0);
				preparedStatement.setString(2,
						invoiceVo.getGeneralInformation().getOrgGstNumber() != null
						? invoiceVo.getGeneralInformation().getOrgGstNumber()
								: null);
				preparedStatement.setInt(3,
						invoiceVo.getGeneralInformation().getInvoiceTypeId() != null
						? invoiceVo.getGeneralInformation().getInvoiceTypeId()
								: 0);
				preparedStatement.setInt(4,
						invoiceVo.getGeneralInformation().getCustomerId() != null
						? invoiceVo.getGeneralInformation().getCustomerId()
								: 0);
				preparedStatement.setInt(5,
						invoiceVo.getGeneralInformation().getCustomerAccountId() != null
						? invoiceVo.getGeneralInformation().getCustomerAccountId()
								: 0);
				preparedStatement.setString(6,
						invoiceVo.getGeneralInformation().getCustomerAccount() != null
						? invoiceVo.getGeneralInformation().getCustomerAccount()
								: null);
				preparedStatement.setString(7,
						invoiceVo.getGeneralInformation().getCustomerGstNumber() != null
						? invoiceVo.getGeneralInformation().getCustomerGstNumber()
								: null);
				preparedStatement.setString(8,
						invoiceVo.getGeneralInformation().getReceiverName() != null
						? invoiceVo.getGeneralInformation().getReceiverName()
								: null);
				preparedStatement.setInt(9,
						invoiceVo.getGeneralInformation().getPlaceOfSupply() != null
						? invoiceVo.getGeneralInformation().getPlaceOfSupply()
								: 0);
				preparedStatement.setInt(10,
						invoiceVo.getGeneralInformation().getSupplyServiceId() != null
						? invoiceVo.getGeneralInformation().getSupplyServiceId()
								: 0);
				preparedStatement.setBoolean(11,
						invoiceVo.getGeneralInformation().getIsReverseCharge() != null
						? invoiceVo.getGeneralInformation().getIsReverseCharge()
								: false);
				preparedStatement.setBoolean(12,
						invoiceVo.getGeneralInformation().getIsSoldByEcommerce() != null
						? invoiceVo.getGeneralInformation().getIsSoldByEcommerce()
								: false);
				preparedStatement.setString(13,
						invoiceVo.getGeneralInformation().getEcommerceGstIn() != null
						? invoiceVo.getGeneralInformation().getEcommerceGstIn()
								: null);
				preparedStatement.setBoolean(14,
						invoiceVo.getGeneralInformation().getIsWopUnderLut() != null
						? invoiceVo.getGeneralInformation().getIsWopUnderLut()
								: false);
				preparedStatement.setString(15,
						invoiceVo.getGeneralInformation().getLutNumber() != null
						? invoiceVo.getGeneralInformation().getLutNumber()
								: null);
				String lutDate = invoiceVo.getGeneralInformation().getLutDate() != null ? DateConverter.getInstance().correctDatePickerDateToString(invoiceVo.getGeneralInformation().getLutDate() ): null;
				preparedStatement.setDate(16, lutDate != null && !lutDate.isEmpty() ? Date.valueOf(lutDate) : null);
				preparedStatement.setString(17,
						invoiceVo.getGeneralInformation().getPurchaseOrderNo() != null
						? invoiceVo.getGeneralInformation().getPurchaseOrderNo()
								: null);
				preparedStatement.setString(18, invoiceNumber != null ? invoiceNumber : null);
				String invoiceDate = invoiceVo.getGeneralInformation().getInvoiceDate() != null ? DateConverter
						.getInstance().correctDatePickerDateToString(invoiceVo.getGeneralInformation().getInvoiceDate())
						: null;
						preparedStatement.setDate(19,
								invoiceDate != null && !invoiceDate.isEmpty() ? Date.valueOf(invoiceDate) : null);
						preparedStatement.setInt(20,
								invoiceVo.getGeneralInformation().getPaymentTermsId() != null
								? invoiceVo.getGeneralInformation().getPaymentTermsId()
										: 0);
						String dueDate = invoiceVo.getGeneralInformation().getDueDate() != null ? DateConverter.getInstance()
								.correctDatePickerDateToString(invoiceVo.getGeneralInformation().getDueDate()) : null;
								preparedStatement.setDate(21, dueDate != null && !dueDate.isEmpty() ? Date.valueOf(dueDate) : null);
								preparedStatement.setInt(22,
										invoiceVo.getGeneralInformation().getTaxApplicationId() != null
										? invoiceVo.getGeneralInformation().getTaxApplicationId()
												: 0);
								preparedStatement.setInt(23,
										invoiceVo.getGeneralInformation().getExportTypeId() != null
										? invoiceVo.getGeneralInformation().getExportTypeId()
												: 0);
								preparedStatement.setString(24,
										invoiceVo.getGeneralInformation().getPortCode() != null
										? invoiceVo.getGeneralInformation().getPortCode()
												: null);
								preparedStatement.setString(25,
										invoiceVo.getGeneralInformation().getShippingBillNo() != null
										? invoiceVo.getGeneralInformation().getShippingBillNo()
												: null);
								String shippingBillDate = invoiceVo.getGeneralInformation().getShippingBillDate() != null
										? DateConverter.getInstance().correctDatePickerDateToString(
												invoiceVo.getGeneralInformation().getShippingBillDate())
												: null;
												preparedStatement.setDate(26,
														shippingBillDate != null && !shippingBillDate.isEmpty() ? Date.valueOf(shippingBillDate)
																: null);
												preparedStatement.setDouble(27,
														invoiceVo.getGeneralInformation().getSubTotal() != null
														? invoiceVo.getGeneralInformation().getSubTotal()
																: 0.00);
												preparedStatement.setInt(28,
														invoiceVo.getGeneralInformation().getTdsId() != null
														? invoiceVo.getGeneralInformation().getTdsId()
																: 0);
												preparedStatement.setDouble(29,
														invoiceVo.getGeneralInformation().getTdsValue() != null
														? invoiceVo.getGeneralInformation().getTdsValue()
																: 0.00);
												preparedStatement.setInt(30,
														invoiceVo.getGeneralInformation().getDiscountAccountId() != null
														? invoiceVo.getGeneralInformation().getDiscountAccountId()
																: 0);
												preparedStatement.setString(31,
														invoiceVo.getGeneralInformation().getDiscountAccount() != null
														? invoiceVo.getGeneralInformation().getDiscountAccount()
																: null);
												preparedStatement.setDouble(32,
														invoiceVo.getGeneralInformation().getDiscountValue() != null
														? invoiceVo.getGeneralInformation().getDiscountValue()
																: 0.00);
												preparedStatement.setDouble(33,
														invoiceVo.getGeneralInformation().getAdjustmentValue() != null
														? invoiceVo.getGeneralInformation().getAdjustmentValue()
																: 0.00);
												preparedStatement.setInt(34,
														invoiceVo.getGeneralInformation().getAdjustmentAccountId() != null
														? invoiceVo.getGeneralInformation().getAdjustmentAccountId()
																: 0);
												preparedStatement.setString(35,
														invoiceVo.getGeneralInformation().getAdjustmentAccount() != null
														? invoiceVo.getGeneralInformation().getAdjustmentAccount()
																: null);
												preparedStatement.setDouble(36,
														invoiceVo.getGeneralInformation().getTotal() != null
														? invoiceVo.getGeneralInformation().getTotal()
																: 0.00);
												preparedStatement.setString(37,
														invoiceVo.getGeneralInformation().getTermsAndConditions() != null
														? invoiceVo.getGeneralInformation().getTermsAndConditions()
																: null);
												preparedStatement.setInt(38,
														invoiceVo.getGeneralInformation().getBankId() != null
														? invoiceVo.getGeneralInformation().getBankId()
																: 0);
												preparedStatement.setString(39,
														invoiceVo.getGeneralInformation().getBankType() != null
														? invoiceVo.getGeneralInformation().getBankType()
																: null);
												preparedStatement.setInt(40,
														invoiceVo.getGeneralInformation().getCurrencyId() != null
														? invoiceVo.getGeneralInformation().getCurrencyId()
																: 0);
												preparedStatement.setDouble(41,
														invoiceVo.getGeneralInformation().getExchangeRate() != null
														? invoiceVo.getGeneralInformation().getExchangeRate()
																: 0.00);
												String status = CommonConstants.STATUS_AS_ACTIVE.equals(invoiceVo.getStatus())
														? CommonConstants.STATUS_AS_ACTIVE
																: CommonConstants.STATUS_AS_DRAFT;
												preparedStatement.setString(42, status);
												preparedStatement.setDate(43, new Date(System.currentTimeMillis()));
												preparedStatement.setInt(44, Integer.valueOf(invoiceVo.getUserId()));
												preparedStatement.setInt(45, invoiceVo.getOrgId());
												preparedStatement.setBoolean(46, invoiceVo.getIsSuperAdmin());
												preparedStatement.setString(47,
														invoiceVo.getRoleName() != null ? invoiceVo.getRoleName() : CommonConstants.ROLE_SUPER_ADMIN);
												preparedStatement.setString(48, invoiceVo.getGeneralInformation().getAdjustmentAccountLevel());
												preparedStatement.setString(49, invoiceVo.getGeneralInformation().getDiscountAccountLevel());
												preparedStatement.setDouble(50,
														invoiceVo.getGeneralInformation().getTotal() != null
														? invoiceVo.getGeneralInformation().getTotal()
																: 0.00);
												preparedStatement.setBoolean(51,
														invoiceVo.getGeneralInformation().getIsRegistered() != null
														? invoiceVo.getGeneralInformation().getIsRegistered()
																: false);
												preparedStatement.setString(52, invoiceVo.getGeneralInformation().getNotes()!=null ? invoiceVo.getGeneralInformation().getNotes() : null);
												preparedStatement.setBoolean(53, invoiceVo.getGeneralInformation().getTogglePayment()!=null ?  invoiceVo.getGeneralInformation().getTogglePayment() : false);
												ObjectMapper mapper = new ObjectMapper();
												String newJsonData =invoiceVo.getGeneralInformation().getGeneralLedgerData()!=null ? mapper.writeValueAsString(invoiceVo.getGeneralInformation().getGeneralLedgerData()) : null;
												logger.info("newJsonData"+newJsonData);
												preparedStatement.setString(54, newJsonData);
												int rowAffected = preparedStatement.executeUpdate();
												if (rowAffected == 1) {
													try (ResultSet rs = preparedStatement.getGeneratedKeys()) {
														while (rs.next()) {
															invoiceVo.setInvoiceId(rs.getInt(1));
														}
													}
												}
												if (invoiceVo.getGeneralInformation().getCustomerGstNumber() != null
														&& invoiceVo.getGeneralInformation().getCustomerId() != null) {
													customerDao.updateGstForCustomerId(invoiceVo.getGeneralInformation().getCustomerGstNumber(),
															invoiceVo.getGeneralInformation().getCustomerId(), Integer.valueOf(invoiceVo.getUserId()),
															connection);
													updateGSTNumberForCustomers(invoiceVo.getGeneralInformation().getCustomerId(), invoiceVo.getOrgId(), invoiceVo.getGeneralInformation().getCustomerGstNumber(), connection);
												}
												if (invoiceVo.getGeneralInformation().getCustomerId() != null
														&& invoiceVo.getGeneralInformation().getBillingAddress() != null
														&& invoiceVo.getGeneralInformation().getDeliveryAddress() != null) {
													customerDao.updateCustomerBillingAddress(invoiceVo.getGeneralInformation().getBillingAddress(),
															invoiceVo.getGeneralInformation().getCustomerId(), connection);
													customerDao.updateCustomerDeliveryAddress(invoiceVo.getGeneralInformation().getDeliveryAddress(),
															invoiceVo.getGeneralInformation().getCustomerId(), connection);
												}
												logger.info("Successfully inserted into table ar_invoice_general_information ");
			} catch (Exception e) {
				logger.info("Error in inserting to table ar_invoice_general_information ", e);
				throw new ApplicationException(e.getMessage());
			}
		}
		return invoiceVo;
	}

	private Integer createProduct(ArInvoiceProductVo product, Connection connection, Integer invoiceId)
			throws ApplicationException {
		logger.info("To insert into table ar_invoice_items " + product);
		Integer generatedId = null;
		if (product != null && invoiceId != null) {
			try (final PreparedStatement preparedStatement = connection
					.prepareStatement(ArInvoiceConstants.INSERT_INTO_INVOICE_ITEMS, Statement.RETURN_GENERATED_KEYS)) {
				preparedStatement.setInt(1, product.getProductId() != null ? product.getProductId() : 0);
				preparedStatement.setInt(2, product.getProductAccountId() != null ? product.getProductAccountId() : 0);
				preparedStatement.setString(3, product.getProductAccountName());
				preparedStatement.setString(4, product.getProductAccountLevel());
				preparedStatement.setString(5, product.getQuantity() != null ? product.getQuantity() : null);
				preparedStatement.setInt(6, product.getMeasureId() != null ? product.getMeasureId() : 0);
				preparedStatement.setDouble(7, product.getUnitPrice() != null ? product.getUnitPrice() : 0.00);
				preparedStatement.setDouble(8, product.getDiscount() != null ? product.getDiscount() : 0.00);
				preparedStatement.setInt(9, product.getDiscountType() != null ? product.getDiscountType() : 0);
				preparedStatement.setDouble(10,
						product.getDiscountAmount() != null ? product.getDiscountAmount() : 0.00);
				preparedStatement.setInt(11, product.getTaxRateId() != null ? product.getTaxRateId() : 0);
				preparedStatement.setDouble(12, product.getAmount());
				preparedStatement.setString(13, CommonConstants.STATUS_AS_ACTIVE);
				preparedStatement.setDate(14, new Date(System.currentTimeMillis()));
				preparedStatement.setInt(15, invoiceId);
				preparedStatement.setDouble(16, product.getNetAmount());
				preparedStatement.setDouble(17, product.getBaseUnitPrice()!=null ? product.getBaseUnitPrice() : 0.00);
				preparedStatement.setString(18, product.getDescription()!=null ? product.getDescription() : null );
				preparedStatement.setInt(19, product.getTempId()!=null ? product.getTempId() : 0);
				int rowAffected = preparedStatement.executeUpdate();
				if (rowAffected == 1) {
					try (ResultSet rs = preparedStatement.getGeneratedKeys()) {
						while (rs.next()) {
							generatedId = rs.getInt(1);
						}
					}
				}
				logger.info("Successfully inserted into table ar_invoice_items with Id ::" + generatedId);
			} catch (Exception e) {
				logger.info("Error in inserting to table ar_invoice_items ", e);
				throw new ApplicationException(e);
			}
		}
		return generatedId;
	}

	// WHen tax rate is not applicable the tax rate id would be 0. So validating tax
	// rate is not 0
	private void createTaxDetailsForProduct(Integer productId, ArInvoiceProductVo product, Connection connection)
			throws ApplicationException {
		logger.info("To insert into table ar_tax_details " + product);
		if (productId != null && product != null && product.getTaxRateId() != null && product.getTaxRateId() != 0
				&& product.getTaxDetails() != null && product.getTaxDetails().getTaxDistribution() != null) {
			for (ArInvoiceTaxDistributionVo distributionVo : product.getTaxDetails().getTaxDistribution()) {
				try (final PreparedStatement preparedStatement = connection
						.prepareStatement(ArInvoiceConstants.INSERT_INTO_TAX_DETAILS)) {
					preparedStatement.setString(1, ArInvoiceConstants.MODULE_TYPE_AR_INVOICES);
					preparedStatement.setInt(2, productId);
					preparedStatement.setString(3, product.getTaxDetails().getGroupName().replace("%", ""));
					preparedStatement.setString(4, distributionVo.getTaxName());
					preparedStatement.setString(5, distributionVo.getTaxRate());
					preparedStatement.setDouble(6, distributionVo.getTaxAmount());
					preparedStatement.setDate(7, new Date(System.currentTimeMillis()));
					preparedStatement.executeUpdate();
					logger.info("Successfully inserted into table ar_tax_details");
				} catch (Exception e) {
					logger.info("Error in inserting to table ar_tax_details ", e);
					throw new ApplicationException(e);
				}
			}
		}

	}

	// TO check of PO already Exist For Organization
	private boolean checkInvoiceExistForAnOrganization(ArInvoiceVo invoiceVo, Connection con, String invoiceNo)
			throws ApplicationException {
		Boolean isInvoiceExist = false;
		try (final PreparedStatement preparedStatement = con
				.prepareStatement(ArInvoiceConstants.CHECK_INVOICE_EXIST_FOR_ORG)) {
			preparedStatement.setInt(1, invoiceVo.getOrgId());
			preparedStatement.setString(2, invoiceNo);
			try (ResultSet rs = preparedStatement.executeQuery()) {
				if (rs.next()) {
					if( invoiceVo.getInvoiceId()==null) {//create scenario
						return true;	
					}else {
						if(invoiceVo.getInvoiceId()!=rs.getInt(1)) {//If it is not same record
							return true;
						}
					}

				}
			}
			logger.info("checkInvoiceExistForAnOrganization " + isInvoiceExist);
		} catch (Exception e) {
			logger.info("Error in checkInvoiceExistForAnOrganization  ", e);
			throw new ApplicationException(e);
		}
		return isInvoiceExist;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<ArInvoiceListVo> getInvoiceFilteredList(ArInvoiceFilterVo filterVo) throws ApplicationException {
		logger.info("To get the Filteredlist with filter values :::" + filterVo);
		Connection connection = null;
		List<ArInvoiceListVo> listVos = new ArrayList<ArInvoiceListVo>();
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			connection = getUserMgmConnection();
			String dateFormat = organizationDao.getDefaultDateForOrganization(filterVo.getOrgId(), connection);
			closeResources(null, null, connection);
			StringBuilder filterQuery = new StringBuilder(ArInvoiceConstants.GET_ALL_INVOICE_FOR_ORG);
			List paramsList = new ArrayList<>();
			paramsList.add(filterVo.getOrgId());
			paramsList.add(CommonConstants.STATUS_AS_DELETE);
			if(!CommonConstants.ROLE_SUPER_ADMIN.equals(filterVo.getRoleName())) {
				filterQuery.append(" and igi.user_id = ? and igi.role_name in(?) ");
				paramsList.add(filterVo.getUserId());
				paramsList.add(filterVo.getRoleName());
			}
			if (filterVo.getStatus() != null) {
				filterQuery.append("and igi.status = ? ");
				paramsList.add(filterVo.getStatus());
			}
			if (filterVo.getFromAmount() != null) {
				filterQuery.append(" and igi.total >= ?");
				paramsList.add(filterVo.getFromAmount());
			}
			if (filterVo.getToAmount() != null) {
				filterQuery.append(" and igi.total <= ?");
				paramsList.add(filterVo.getToAmount());
			}
			if (filterVo.getCustomerId() != null) {
				filterQuery.append(" and igi.customer_id = ?");
				paramsList.add(filterVo.getCustomerId());
			}
			if (filterVo.getStartDate() != null) {
				if (filterVo.getEndDate() == null) {
					java.sql.Date date = new java.sql.Date(System.currentTimeMillis());
					filterVo.setEndDate(DateConverter.getInstance().getDatePickerDateFormat(date));
					logger.info("End date is null and so setting the sysdate::" + filterVo.getEndDate());
				}

				if (filterVo.getStartDate() != null && filterVo.getEndDate() != null) {
					filterQuery.append(" and igi.invoice_date between ? and ? ");
					String fromdate = DateConverter.getInstance()
							.correctDatePickerDateToString(filterVo.getStartDate());
					logger.info("convertedFromDate::" + fromdate);
					paramsList.add(fromdate);
					String todate = DateConverter.getInstance().correctDatePickerDateToString(filterVo.getEndDate());
					logger.info("convertedEndDate::" + todate);
					paramsList.add(todate);
				}
			}
			filterQuery.append("  order by igi.id desc");
			logger.info(filterQuery.toString());
			logger.info(paramsList);
			connection = getAccountsReceivableConnection();
			preparedStatement = connection.prepareStatement(filterQuery.toString());
			int counter = 1;
			for (int i = 0; i < paramsList.size(); i++) {
				logger.info(counter);
				preparedStatement.setObject(counter, paramsList.get(i));
				counter++;
			}
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				ArInvoiceListVo listVo = new ArInvoiceListVo();
				int invoiceId = rs.getInt(1);
				// BasicInvoiceDetailsVo
				// invoiceDetailsVo=getInvoiceDueBalanceAndStatus(invoiceId);
				listVo.setInvoiceId(invoiceId);
				listVo.setCustomerName(rs.getString(2));
				String invoiceNo = rs.getString(3) != null ? rs.getString(3).replace("~", "/") : null;
				logger.info("invoiceNo is::" + invoiceNo);
				listVo.setInvoiceNumber(invoiceNo);
				listVo.setPoNumber(rs.getString(4));
				Date invoiceDate = rs.getDate(5);
				if (invoiceDate != null && dateFormat != null) {
					String invoiceConvertedDate = DateConverter.getInstance().convertDateToGivenFormat(invoiceDate,
							dateFormat);
					listVo.setInvoiceDate(invoiceConvertedDate);
				}
				Date dueDate = rs.getDate(6);
				if (dueDate != null && dateFormat != null) {
					String duedate = DateConverter.getInstance().convertDateToGivenFormat(dueDate, dateFormat);
					listVo.setDueDate(duedate);
				}
				String status = rs.getString(7);
				String displayStatus = null;
				logger.info("Invoice Id:" + invoiceId + ",Status:" + status);
				if (status != null) {
					switch (status) {
					case CommonConstants.STATUS_AS_ACTIVE:
						displayStatus = CommonConstants.DISPLAY_STATUS_AS_ACTIVE;
						break;
					case CommonConstants.STATUS_AS_VOID:
						displayStatus = CommonConstants.DISPLAY_STATUS_AS_VOID;
						break;
					case CommonConstants.STATUS_AS_DRAFT:
						displayStatus = CommonConstants.DISPLAY_STATUS_AS_DRAFT;
						break;
					case CommonConstants.STATUS_AS_INACTIVE:
						displayStatus = CommonConstants.DISPLAY_STATUS_AS_INACTIVE;
						break;
					case CommonConstants.STATUS_AS_ACCEPT:
						displayStatus = CommonConstants.DISPLAY_STATUS_AS_ACCEPT;
						break;
					case CommonConstants.STATUS_AS_DECLINE:
						displayStatus = CommonConstants.DISPLAY_STATUS_AS_DECLINE;
						break;
					case CommonConstants.STATUS_AS_EXPIRED:
						displayStatus = CommonConstants.DISPLAY_STATUS_AS_EXPIRED;
						break;
					case CommonConstants.STATUS_AS_OPEN:
						displayStatus = CommonConstants.DISPLAY_STATUS_AS_OPEN;
						break;
					case CommonConstants.STATUS_AS_PAID:
						displayStatus = CommonConstants.DISPLAY_STATUS_AS_PAID;
						break;
					case CommonConstants.STATUS_AS_UNPAID:
						displayStatus = CommonConstants.DISPLAY_STATUS_AS_UNPAID;
						break;
					case CommonConstants.STATUS_AS_OVERDUE:
						displayStatus = CommonConstants.DISPLAY_STATUS_AS_OVERDUE;
						break;
					case CommonConstants.STATUS_AS_PARTIALLY_PAID:
						displayStatus = CommonConstants.DISPLAY_STATUS_AS_PARTIALLY_PAID;
						break;

					}
				}
				listVo.setStatus(displayStatus);
				int digits = rs.getInt(12);
				BigDecimal bdTotal = new BigDecimal(rs.getDouble(8) > 0 ? rs.getDouble(8) : 0).setScale(digits,
						RoundingMode.HALF_UP);
				listVo.setTotal(bdTotal.doubleValue());
				BigDecimal bdBalDue = new BigDecimal(rs.getDouble(9) > 0 ? rs.getDouble(9) : 0).setScale(digits,
						RoundingMode.HALF_UP);
				listVo.setBalanceDue(bdBalDue.doubleValue());

				listVo.setCurrencySymbol(rs.getString(10));
				listVo.setCurrencyId(rs.getInt(11));
				listVo.setCustomerId(rs.getInt(13));
				listVo.setReceipts(getActiveReceiptsForInvoice(listVo.getInvoiceId()));
				listVo.setCreditNoteTotal(
						balanceUpdateDao.getTotalAmountOfInvoiceFromCreditNote(listVo.getInvoiceId()).doubleValue());
				listVo.setPendingApprovalStatus(rs.getString(14));
				listVos.add(listVo);
			}
			logger.info("listVos size::" + listVos.size());
			logger.info("Retrieved Pos ");

		} catch (Exception e) {
			logger.info("Error in getAllFilteredPos:: ", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, connection);
		}
		return listVos;

	}

	
	
	
	public ArInvoiceVo getInvoiceById(Integer invoiceId) throws ApplicationException {
		logger.info("To get Invoice in ARInvoiceDao for:::" + invoiceId);
		ArInvoiceVo invoiceVo = null;
		if (invoiceId != null) {
			Connection con =null;
			try  {
				con = getAccountsReceivableConnection();
				invoiceVo = new ArInvoiceVo();
				invoiceVo.setInvoiceId(invoiceId);
				getInvoiceGeneralInfoById(invoiceVo, con);
				List<ArInvoiceProductVo> products = getProductListForInvoices(invoiceId, con);
				invoiceVo.setProducts(products);
				List<UploadFileVo> uploadFileVos = new ArrayList<UploadFileVo>();
				for (AttachmentsVo attachments : attachmentsDao.getAttachmentDetails(invoiceId,
						AttachmentsConstants.MODULE_TYPE_ACCOUNTS_RECEIVABLES_INVOICE)) {
					UploadFileVo uploadFileVo = new UploadFileVo();
					uploadFileVo.setId(attachments.getId());
					uploadFileVo.setName(attachments.getFileName());
					uploadFileVo.setSize(attachments.getSize());
					uploadFileVos.add(uploadFileVo);
				}
				invoiceVo.setAttachments(uploadFileVos);
			} catch (Exception e) {
				logger.info("Error in getInvoiceById:: ", e);
				throw new ApplicationException(e);
			}finally{
				closeResources(null, null, con);
			}
		}
		return invoiceVo;
	}

	public List<ArInvoiceProductVo> getProductListForInvoices(Integer invoiceId, Connection con)
			throws ApplicationException {
		logger.info("To get  in getProductListForInvoices ");
		List<ArInvoiceProductVo> productVos = new ArrayList<ArInvoiceProductVo>();
		try (final PreparedStatement preparedStatement = con
				.prepareStatement(ArInvoiceConstants.GET_ITEMS_BY_INVOICE_ID)) {
			preparedStatement.setInt(1, invoiceId);
			preparedStatement.setString(2, CommonConstants.STATUS_AS_DELETE);
			try (ResultSet rs = preparedStatement.executeQuery()) {
				while (rs.next()) {
					ArInvoiceProductVo productVo = new ArInvoiceProductVo();
					productVo.setId(rs.getInt(1));
					productVo.setProductId(rs.getInt(2));
					productVo.setProductDisplayname(rs.getString(3));
					productVo.setType(rs.getString(4));
					productVo.setProductAccountId(rs.getInt(5));
					productVo.setProductAccountName(rs.getString(6));
					productVo.setProductAccountLevel(rs.getString(7));
					productVo.setQuantity(rs.getString(8));
					productVo.setMeasureId(rs.getInt(9));
					productVo.setUnitPrice(rs.getDouble(10));
					productVo.setTaxRateId(rs.getInt(11));
					productVo.setDiscount(rs.getDouble(12));
					productVo.setDiscountType(rs.getInt(13));
					productVo.setDiscountAmount(rs.getDouble(14));
					productVo.setAmount(rs.getDouble(15));
					productVo.setStatus(rs.getString(16));
					productVo.setHsn(rs.getString(17));
					productVo.setNetAmount(rs.getDouble(18));
					productVo.setBaseUnitPrice(rs.getDouble(19));
					productVo.setDescription(rs.getString(20));
					productVo.setTempId(rs.getInt(21));
					productVo.setTaxDetails(getTaxDetails(rs.getInt(1), con));
					logger.info(productVo);
					productVos.add(productVo);
				}
			}
			logger.info("Successfully fetched  getProductListForInvoices " + productVos);
		} catch (Exception e) {
			logger.info("Error in getProductListForInvoices ", e);
			throw new ApplicationException(e);
		}
		return productVos;
	}

	public void getInvoiceGeneralInfoById(ArInvoiceVo invoiceVo, Connection con) throws ApplicationException {
		ArInvoiceGeneralInformationVo generalInfoVo = new ArInvoiceGeneralInformationVo();
		try (PreparedStatement preparedStatement = con
				.prepareStatement(ArInvoiceConstants.GET_INVOICE_GENERAL_INFO_BY_ID)) {
			preparedStatement.setInt(1, invoiceVo.getInvoiceId());
			try (ResultSet rs = preparedStatement.executeQuery()) {
				while (rs.next()) {
					generalInfoVo.setLocationId(rs.getInt(1));
					generalInfoVo.setOrgGstNumber(rs.getString(2));
					generalInfoVo.setInvoiceTypeId(rs.getInt(3));
					generalInfoVo.setCustomerId(rs.getInt(4));
					generalInfoVo.setCustomerAccountId(rs.getInt(5));
					generalInfoVo.setCustomerAccount(rs.getString(6));
					generalInfoVo.setCustomerGstNumber(rs.getString(7));
					generalInfoVo.setReceiverName(rs.getString(8));
					generalInfoVo.setPlaceOfSupply(rs.getInt(9));
					generalInfoVo.setSupplyServiceId(rs.getInt(10));
					generalInfoVo.setIsReverseCharge(rs.getBoolean(11));
					generalInfoVo.setIsSoldByEcommerce(rs.getBoolean(12));
					generalInfoVo.setEcommerceGstIn(rs.getString(13));
					generalInfoVo.setIsWopUnderLut(rs.getBoolean(14));
					String lutId = rs.getString(15);
					generalInfoVo.setLutNumber(lutId);
					LetterOfUndertakingVo letterOfUndertakingVo = lutDao
							.getLetterOfUndertakingById(lutId != null ? Integer.parseInt(lutId) : 0);
					generalInfoVo.setLutNo(letterOfUndertakingVo != null ? letterOfUndertakingVo.getAckNo() : "");
					Date lutDate = rs.getDate(16);
					generalInfoVo.setLutDate(
							lutDate != null ? DateConverter.getInstance().getDatePickerDateFormat(lutDate) : null);
					generalInfoVo.setPurchaseOrderNo(rs.getString(17));
					String[] invoiceNum = rs.getString(18).split("~");
					logger.info("1::"+invoiceNum[0]+"2::"+invoiceNum[1]);

					if (invoiceNum.length > 1) {
						generalInfoVo.setInvoiceNoPrefix(invoiceNum[0]!=null && invoiceNum[0].trim().length()>0?invoiceNum[0]:"");
						generalInfoVo.setInvoiceNumber(invoiceNum[1]);
						generalInfoVo.setInvoiceNoSuffix(invoiceNum[2]!=null && invoiceNum[2].trim().length()>0?invoiceNum[2]:"");
					}
					Date invDate = rs.getDate(19);
					generalInfoVo.setInvoiceDate(
							invDate != null ? DateConverter.getInstance().getDatePickerDateFormat(invDate) : null);
					generalInfoVo.setPaymentTermsId(rs.getInt(20));
					Date dueDate = rs.getDate(21);
					generalInfoVo.setDueDate(
							dueDate != null ? DateConverter.getInstance().getDatePickerDateFormat(dueDate) : null);
					generalInfoVo.setTaxApplicationId(rs.getInt(22));
					generalInfoVo.setExportTypeId(rs.getInt(23));
					generalInfoVo.setPortCode(rs.getString(24));
					generalInfoVo.setShippingBillNo(rs.getString(25));
					Date shippingBillDate = rs.getDate(26);
					generalInfoVo.setShippingBillDate(shippingBillDate != null
							? DateConverter.getInstance().getDatePickerDateFormat(shippingBillDate)
									: null);
					generalInfoVo.setSubTotal(rs.getDouble(27));
					generalInfoVo.setTdsId(rs.getInt(28));
					generalInfoVo.setTdsValue(rs.getDouble(29));
					generalInfoVo.setDiscountAccountId(rs.getInt(30));
					generalInfoVo.setDiscountAccount(rs.getString(31));
					generalInfoVo.setDiscountValue(rs.getDouble(32));
					generalInfoVo.setAdjustmentValue(rs.getDouble(33));
					generalInfoVo.setAdjustmentAccountId(rs.getInt(34));
					generalInfoVo.setAdjustmentAccount(rs.getString(35));
					generalInfoVo.setTotal(rs.getDouble(36));
					generalInfoVo.setTermsAndConditions(rs.getString(37));
					generalInfoVo.setBankId(rs.getInt(38));
					generalInfoVo.setBankType(rs.getString(39));
					generalInfoVo.setCurrencyId(rs.getInt(40));
					generalInfoVo.setExchangeRate(rs.getDouble(41));
					invoiceVo.setStatus(rs.getString(42));
					invoiceVo.setUserId(rs.getString(43));
					invoiceVo.setOrgId(rs.getInt(44));
					invoiceVo.setIsSuperAdmin(rs.getBoolean(45));
					invoiceVo.setRoleName(rs.getString(46));
					generalInfoVo.setAdjustmentAccountLevel(rs.getString(47));
					generalInfoVo.setDiscountAccountLevel(rs.getString(48));
					generalInfoVo.setBalanceDue(rs.getDouble(49));
					generalInfoVo.setIsRegistered(rs.getBoolean(50));
					generalInfoVo.setCreate_ts(rs.getDate(51));
					generalInfoVo.setNotes(rs.getString(52));
					generalInfoVo.setTogglePayment(rs.getBoolean(53));
					ObjectMapper mapper = new ObjectMapper();
					String json = rs.getString(54);
					if(json!=null) {
						GeneralLedgerVo gldata = mapper.readValue(json, GeneralLedgerVo.class);
						logger.info("Json map "+gldata);
						generalInfoVo.setGeneralLedgerData(gldata);
					}
					CustomerBillingAddressVo billingAddress = new CustomerBillingAddressVo();
					generalInfoVo.setBillingAddress(
							customerDao.getCustomerBillingAddress(billingAddress, generalInfoVo.getCustomerId(), con));
					generalInfoVo.setDeliveryAddress(
							customerDao.getCustomerDeliveryAddress(generalInfoVo.getCustomerId(), con));
					invoiceVo.setGeneralInformation(generalInfoVo);
				}
			}

		} catch (Exception e) {
			logger.info("Error in getInvoiceById:: ", e);
			throw new ApplicationException(e);
		}

	}

	// to activate or deactivate the customer
	public void activateOrDeActivateInvoice(Integer id, String status, String userId, String roleName)
			throws ApplicationException {
		logger.info("Entry into method: activateOrDeActivateInvoice");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = getAccountsReceivableConnection();
			String sql = ArInvoiceConstants.ACTIVATE_DEACTIVATE_INVOICE;
			preparedStatement = con.prepareStatement(sql);
			preparedStatement.setString(1, status);
			preparedStatement.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
			preparedStatement.setString(3, roleName);
			preparedStatement.setString(4, userId);
			preparedStatement.setInt(5, id);
			preparedStatement.executeUpdate();
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}

	}

	private ArInvoiceTaxDetailsVo getTaxDetails(Integer productId, Connection con) throws ApplicationException {
		logger.info("To get  in getTaxDetails for prodId:" + productId);
		ArInvoiceTaxDetailsVo taxDetailsVo = null;
		try (final PreparedStatement preparedStatement = con
				.prepareStatement(ArInvoiceConstants.GET_TAX_DETAILS_BY_ITEM_ID)) {
			preparedStatement.setInt(1, productId);
			preparedStatement.setString(2, ArInvoiceConstants.MODULE_TYPE_AR_INVOICES);
			taxDetailsVo = new ArInvoiceTaxDetailsVo();
			List<ArInvoiceTaxDistributionVo> distributionVos = new ArrayList<ArInvoiceTaxDistributionVo>();
			try (ResultSet rs = preparedStatement.executeQuery()) {
				while (rs.next()) {
					ArInvoiceTaxDistributionVo distributionVo = new ArInvoiceTaxDistributionVo();
					taxDetailsVo.setId(rs.getInt(1));
					taxDetailsVo.setComponentName(rs.getString(2));
					taxDetailsVo.setComponentItemId(rs.getInt(3));
					taxDetailsVo.setGroupName(rs.getString(4));
					distributionVo.setTaxName(rs.getString(5));
					distributionVo.setTaxRate(rs.getString(6));
					distributionVo.setTaxAmount(rs.getDouble(7));
					distributionVos.add(distributionVo);
				}
			}
			taxDetailsVo.setTaxDistribution(distributionVos);
			logger.info("Successfully fetched  getTaxDetails " + taxDetailsVo);
		} catch (Exception e) {
			logger.info("Error in getTaxDetails ", e);
			throw new ApplicationException(e);
		}
		return taxDetailsVo;
	}

	public Map<String, Integer> getMaxAmountForOrg(Integer organizationId) throws ApplicationException {
		logger.info("To getMinMaxValuesOfOrganisation");
		Connection connection = null;
		Map<String, Integer> maxAmountMap = null;
		if (organizationId != null) {
			connection = getAccountsReceivableConnection();
			PreparedStatement preparedStatement =null;
			ResultSet rs = null;
			try {
				preparedStatement = connection.prepareStatement(ArInvoiceConstants.GET_MAX_AMOUNT_FOR_ORG);
				preparedStatement.setInt(1, organizationId);
				rs = preparedStatement.executeQuery();
				while (rs.next()) {
					maxAmountMap = new HashMap<String, Integer>();
					Integer maxAmt = rs.getInt(1);
					if (maxAmt != null) {
						maxAmountMap.put("maxAmount", maxAmt);
					}
				}

			} catch (Exception e) {
				logger.info(e);
				throw new ApplicationException(e);
			} finally {
				closeResources(rs, preparedStatement, connection);

			}

		}
		return maxAmountMap;

	}




	public CurrencyVo getInvoiceCurrencyDetails(Integer invoiceId) throws ApplicationException {
		logger.info("To get  in getInvoiceCurrencyDetails for invoiceId:" + invoiceId);
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		CurrencyVo currencyVo = new CurrencyVo();
		try {
			con = getAccountsReceivableConnection();
			preparedStatement = con.prepareStatement(ArInvoiceConstants.GET_CURRENCY_FOR_INVOICE);
			preparedStatement.setInt(1, invoiceId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				Integer currencyId = rs.getInt(1);
				if (currencyId != null && currencyId.intValue() > 0) {
					currencyVo = currencyDao.getCurrency(currencyId);
				}
			}
			logger.info("Successfully fetched  getInvoiceCurrencyDetails " + currencyVo);
		} catch (Exception e) {
			logger.info("Error in getInvoiceCurrencyDetails ", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);

		}
		return currencyVo;
	}

	public ArInvoiceVo updateInvoice(ArInvoiceVo invoiceVo) throws SQLException, ApplicationException {
		logger.info("To update a new Invoice in BillsInvoiceDao:::" + invoiceVo);
		Connection connection = null;
		if (invoiceVo != null && invoiceVo.getInvoiceId() != null) {
			try {
				connection = getAccountsReceivableConnection();
				connection.setAutoCommit(false);
				updateInvoiceGeneralInfo(invoiceVo, connection);
				if (invoiceVo.getProducts() != null) {
					for (ArInvoiceProductVo product : invoiceVo.getProducts()) {
						if (product.getStatus() != null) {
							switch (product.getStatus().toUpperCase()) {
							case CommonConstants.STATUS_AS_NEW:
								Integer productId = createProduct(product, connection, invoiceVo.getInvoiceId());
								product.setId(productId);
								createTaxDetailsForProduct(productId, product, connection);
								inventoryDao.addARInvoiceProductToInventoryMgmt(product, invoiceVo);
								break;
							case CommonConstants.STATUS_AS_ACTIVE:
								boolean updateStatus = updateProduct(product, connection, invoiceVo.getInvoiceId());
								if (updateStatus)
									updateTaxDetailsForProduct(product.getId(),
											ArInvoiceConstants.MODULE_TYPE_AR_INVOICES, product, connection);
								inventoryDao.updateARInvoiceProductToInventoryMgmt(product, invoiceVo);
								break;
							case CommonConstants.STATUS_AS_DELETE:
								changeStatusForInvoiceTables(product.getId(), CommonConstants.STATUS_AS_DELETE,
										connection, ArInvoiceConstants.MODIFY_INVOICE_PRODUCT_STATUS_FOR_PRODUCT_ID);
								deleteTaxDetails(product.getId(), ArInvoiceConstants.MODULE_TYPE_AR_INVOICES,
										connection);
								inventoryDao.deleteARInvoiceProductToInventoryMgmt(product, invoiceVo);
								break;
							}
						}
					}
				}

				if (invoiceVo.getAttachments() != null && invoiceVo.getAttachments().size() > 0) {
					attachmentsDao.createAttachments(invoiceVo.getOrgId(), invoiceVo.getUserId(),
							invoiceVo.getAttachments(), AttachmentsConstants.MODULE_TYPE_ACCOUNTS_RECEIVABLES_INVOICE,
							invoiceVo.getInvoiceId(), invoiceVo.getRoleName());
				}

				if (invoiceVo.getAttachmentsToRemove() != null && invoiceVo.getAttachmentsToRemove().size() > 0) {
					for (Integer attachmentId : invoiceVo.getAttachmentsToRemove()) {
						attachmentsDao.changeStatusForSingleAttachment(attachmentId, CommonConstants.STATUS_AS_DELETE,
								invoiceVo.getUserId(), invoiceVo.getRoleName());
					}
				}
				connection.commit();
				logger.info("Succecssfully created invoice in BillsInvoiceDao");
			} catch (Exception e) {
				logger.info("Error in createInvoice:: ", e);
				connection.rollback();
				throw new ApplicationException(e.getMessage());
			} finally {
				if (connection != null) {
					connection.close();
				}
			}
		}
		return invoiceVo;
	}

	private void changeStatusForInvoiceTables(Integer id, String status, Connection connection, String query)
			throws ApplicationException {
		logger.info("To Change the status with query :: " + query);
		if (query != null) {
			try (final PreparedStatement preparedStatement = connection.prepareStatement(query)) {
				preparedStatement.setString(1, status);
				preparedStatement.setDate(2, new Date(System.currentTimeMillis()));
				preparedStatement.setInt(3, id);
				preparedStatement.executeUpdate();
				logger.info("Status chaneged successfully ");
			} catch (Exception e) {
				logger.info("Error in changeStatusForInvoiceTables ", e);
				throw new ApplicationException(e);
			}
		}

	}

	private void deleteTaxDetails(Integer productId, String module, Connection connection) throws ApplicationException {
		logger.info("To delete into table ar_tax_details ");
		try (PreparedStatement preparedStatement = connection
				.prepareStatement(ArInvoiceConstants.MODIFY_TAX_DETAILS_STATUS)) {
			preparedStatement.setDate(1, new Date(System.currentTimeMillis()));
			preparedStatement.setString(2, CommonConstants.STATUS_AS_DELETE);
			preparedStatement.setString(3, module);
			preparedStatement.setInt(4, productId);
			preparedStatement.execute();
		} catch (Exception e) {
			logger.info("Error in deleteTaxDetails to table ar_tax_details ", e);
			throw new ApplicationException(e);
		}
	}

	public void updateTaxDetailsForProduct(Integer prodId, String module, ArInvoiceProductVo product,
			Connection connection) throws ApplicationException {
		logger.info("To update into table accounts_payable_tax_details " + product);
		/*
		 * To delete the existing entries and then adding the updated entries.. This is
		 * because during Update we do tax recalculation the Primary id would be missed
		 * out and so following this way of updating
		 */
		deleteTaxDetails(prodId, module, connection);
		createTaxDetailsForProduct(prodId, product, connection);

	}

	private boolean updateProduct(ArInvoiceProductVo product, Connection connection, Integer invoiceId)
			throws ApplicationException {
		logger.info("To update into table invoice_items " + product);
		boolean isUpdatedSuccessfully = false;
		if (product != null && invoiceId != null && product.getId() != null) {
			try (final PreparedStatement preparedStatement = connection
					.prepareStatement(ArInvoiceConstants.MODIFY_INVOICE_ITEMS_BY_ID)) {
				preparedStatement.setInt(1, product.getProductId() != null ? product.getProductId() : 0);
				preparedStatement.setInt(2, product.getProductAccountId() != null ? product.getProductAccountId() : 0);
				preparedStatement.setString(3, product.getProductAccountName());
				preparedStatement.setString(4, product.getProductAccountLevel());
				preparedStatement.setString(5, product.getQuantity() != null ? product.getQuantity() : null);
				preparedStatement.setInt(6, product.getMeasureId() != null ? product.getMeasureId() : 0);
				preparedStatement.setDouble(7, product.getUnitPrice() != null ? product.getUnitPrice() : 0.00);
				preparedStatement.setDouble(8, product.getDiscount() != null ? product.getDiscount() : 0.00);
				preparedStatement.setInt(9, product.getDiscountType() != null ? product.getDiscountType() : 0);
				preparedStatement.setDouble(10,
						product.getDiscountAmount() != null ? product.getDiscountAmount() : 0.00);
				preparedStatement.setInt(11, product.getTaxRateId() != null ? product.getTaxRateId() : 0);
				preparedStatement.setDouble(12, product.getAmount());
				preparedStatement.setString(13, CommonConstants.STATUS_AS_ACTIVE);
				preparedStatement.setDate(14, new Date(System.currentTimeMillis()));
				preparedStatement.setDouble(15, product.getNetAmount());
				preparedStatement.setDouble(16, product.getBaseUnitPrice()!=null ? product.getBaseUnitPrice() : 0.00);
				preparedStatement.setString(17, product.getDescription()!=null ? product.getDescription() : null );
				preparedStatement.setInt(18, product.getTempId()!=null ? product.getTempId() : 0);
				preparedStatement.setInt(19, product.getId());
				preparedStatement.setInt(20, invoiceId);
				preparedStatement.executeUpdate();
				isUpdatedSuccessfully = true;
				logger.info("Successfully updated into table invoice_items with Id ");
			} catch (Exception e) {
				isUpdatedSuccessfully = false;
				logger.info("Error in updating to table invoice_items ", e);
				throw new ApplicationException(e.getMessage());
			}
		}
		return isUpdatedSuccessfully;
	}

	private ArInvoiceVo updateInvoiceGeneralInfo(ArInvoiceVo invoiceVo, Connection connection)
			throws ApplicationException {
		logger.info("To update into table invoice_general_information ");
		if (invoiceVo != null && invoiceVo.getInvoiceId() != null && invoiceVo.getGeneralInformation() != null) {
			if (invoiceVo.getGeneralInformation().getInvoiceNumber() == null) {
				throw new ApplicationException("Invoice number is mandatory");
			}
			try (final PreparedStatement preparedStatement = connection
					.prepareStatement(ArInvoiceConstants.MODIFY_GENERAL_INFO_BY_ID)) {
				String invoiceNumber = null;
				String existinginvoiceNo = getInvoiceOrderNo(invoiceVo.getInvoiceId(), invoiceVo.getOrgId(),
						connection);
				String invoiceNumToUpdate = new StringBuilder()
						.append(invoiceVo.getGeneralInformation().getInvoiceNoPrefix() != null && !invoiceVo.getGeneralInformation().getInvoiceNoPrefix().isEmpty()
						? invoiceVo.getGeneralInformation().getInvoiceNoPrefix()
								: " ")
						.append("~").append(invoiceVo.getGeneralInformation().getInvoiceNumber()).append("~")
						.append(invoiceVo.getGeneralInformation().getInvoiceNoSuffix() != null && !invoiceVo.getGeneralInformation().getInvoiceNoSuffix().isEmpty()
						? invoiceVo.getGeneralInformation().getInvoiceNoSuffix()
								: " ")
						.toString();
				logger.info("invoiceNumber::" + invoiceNumToUpdate);
				logger.info("existingPurchaseOrderNo::" + existinginvoiceNo);
				if (existinginvoiceNo.trim().equalsIgnoreCase(invoiceNumToUpdate.trim())) {
					invoiceNumber = invoiceNumToUpdate;
				} else {
					boolean isInvoiceExist = checkInvoiceExistForAnOrganization(invoiceVo, connection,
							invoiceNumToUpdate);
					if (isInvoiceExist) {
						throw new ApplicationException("Invoice number already exist for the organization");
					} else {
						logger.info("To add the new invoice in the Update purchse order api ");
						invoiceNumber = invoiceNumToUpdate;
					}
				}
				logger.info("The invoice number to be inserted is :: " + invoiceNumber);

				// String dateFormat =getDefaultDateOfOrg(invoiceVo.getOrgId());
				preparedStatement.setInt(1,
						invoiceVo.getGeneralInformation().getLocationId() != null
						? invoiceVo.getGeneralInformation().getLocationId()
								: 0);
				preparedStatement.setString(2,
						invoiceVo.getGeneralInformation().getOrgGstNumber() != null
						? invoiceVo.getGeneralInformation().getOrgGstNumber()
								: null);
				preparedStatement.setInt(3,
						invoiceVo.getGeneralInformation().getInvoiceTypeId() != null
						? invoiceVo.getGeneralInformation().getInvoiceTypeId()
								: 0);
				preparedStatement.setInt(4,
						invoiceVo.getGeneralInformation().getCustomerId() != null
						? invoiceVo.getGeneralInformation().getCustomerId()
								: 0);
				preparedStatement.setInt(5,
						invoiceVo.getGeneralInformation().getCustomerAccountId() != null
						? invoiceVo.getGeneralInformation().getCustomerAccountId()
								: 0);
				preparedStatement.setString(6,
						invoiceVo.getGeneralInformation().getCustomerAccount() != null
						? invoiceVo.getGeneralInformation().getCustomerAccount()
								: null);
				preparedStatement.setString(7,
						invoiceVo.getGeneralInformation().getCustomerGstNumber() != null
						? invoiceVo.getGeneralInformation().getCustomerGstNumber()
								: null);
				preparedStatement.setString(8,
						invoiceVo.getGeneralInformation().getReceiverName() != null
						? invoiceVo.getGeneralInformation().getReceiverName()
								: null);
				preparedStatement.setInt(9,
						invoiceVo.getGeneralInformation().getPlaceOfSupply() != null
						? invoiceVo.getGeneralInformation().getPlaceOfSupply()
								: 0);
				preparedStatement.setInt(10,
						invoiceVo.getGeneralInformation().getSupplyServiceId() != null
						? invoiceVo.getGeneralInformation().getSupplyServiceId()
								: 0);
				preparedStatement.setBoolean(11,
						invoiceVo.getGeneralInformation().getIsReverseCharge() != null
						? invoiceVo.getGeneralInformation().getIsReverseCharge()
								: false);
				preparedStatement.setBoolean(12,
						invoiceVo.getGeneralInformation().getIsSoldByEcommerce() != null
						? invoiceVo.getGeneralInformation().getIsSoldByEcommerce()
								: false);
				preparedStatement.setString(13,
						invoiceVo.getGeneralInformation().getEcommerceGstIn() != null
						? invoiceVo.getGeneralInformation().getEcommerceGstIn()
								: null);
				preparedStatement.setBoolean(14,
						invoiceVo.getGeneralInformation().getIsWopUnderLut() != null
						? invoiceVo.getGeneralInformation().getIsWopUnderLut()
								: false);
				preparedStatement.setString(15,
						invoiceVo.getGeneralInformation().getLutNumber() != null
						? invoiceVo.getGeneralInformation().getLutNumber()
								: null);
				String lutDate = invoiceVo.getGeneralInformation().getLutDate() != null ? DateConverter.getInstance()
						.correctDatePickerDateToString(invoiceVo.getGeneralInformation().getLutDate()) : null;
						preparedStatement.setDate(16, lutDate != null && !lutDate.isEmpty() ? Date.valueOf(lutDate) : null);
						preparedStatement.setString(17,
								invoiceVo.getGeneralInformation().getPurchaseOrderNo() != null
								? invoiceVo.getGeneralInformation().getPurchaseOrderNo()
										: null);
						preparedStatement.setString(18, invoiceNumber != null ? invoiceNumber : null);
						String invoiceDate = invoiceVo.getGeneralInformation().getInvoiceDate() != null ? DateConverter
								.getInstance().correctDatePickerDateToString(invoiceVo.getGeneralInformation().getInvoiceDate())
								: null;
								preparedStatement.setDate(19,
										invoiceDate != null && !invoiceDate.isEmpty() ? Date.valueOf(invoiceDate) : null);
								preparedStatement.setInt(20,
										invoiceVo.getGeneralInformation().getPaymentTermsId() != null
										? invoiceVo.getGeneralInformation().getPaymentTermsId()
												: 0);
								String dueDate = invoiceVo.getGeneralInformation().getDueDate() != null ? DateConverter.getInstance()
										.correctDatePickerDateToString(invoiceVo.getGeneralInformation().getDueDate()) : null;
										preparedStatement.setDate(21, dueDate != null && !dueDate.isEmpty() ? Date.valueOf(dueDate) : null);
										preparedStatement.setInt(22,
												invoiceVo.getGeneralInformation().getTaxApplicationId() != null
												? invoiceVo.getGeneralInformation().getTaxApplicationId()
														: 0);
										preparedStatement.setInt(23,
												invoiceVo.getGeneralInformation().getExportTypeId() != null
												? invoiceVo.getGeneralInformation().getExportTypeId()
														: 0);
										preparedStatement.setString(24,
												invoiceVo.getGeneralInformation().getPortCode() != null
												? invoiceVo.getGeneralInformation().getPortCode()
														: null);
										preparedStatement.setString(25,
												invoiceVo.getGeneralInformation().getShippingBillNo() != null
												? invoiceVo.getGeneralInformation().getShippingBillNo()
														: null);
										String shippingBillDate = invoiceVo.getGeneralInformation().getShippingBillDate() != null
												? DateConverter.getInstance().correctDatePickerDateToString(
														invoiceVo.getGeneralInformation().getShippingBillDate())
														: null;
														preparedStatement.setDate(26,
																shippingBillDate != null && !shippingBillDate.isEmpty() ? Date.valueOf(shippingBillDate)
																		: null);
														preparedStatement.setDouble(27,
																invoiceVo.getGeneralInformation().getSubTotal() != null
																? invoiceVo.getGeneralInformation().getSubTotal()
																		: 0.00);
														preparedStatement.setInt(28,
																invoiceVo.getGeneralInformation().getTdsId() != null
																? invoiceVo.getGeneralInformation().getTdsId()
																		: 0);
														preparedStatement.setDouble(29,
																invoiceVo.getGeneralInformation().getTdsValue() != null
																? invoiceVo.getGeneralInformation().getTdsValue()
																		: 0.00);
														preparedStatement.setInt(30,
																invoiceVo.getGeneralInformation().getDiscountAccountId() != null
																? invoiceVo.getGeneralInformation().getDiscountAccountId()
																		: 0);
														preparedStatement.setString(31,
																invoiceVo.getGeneralInformation().getDiscountAccount() != null
																? invoiceVo.getGeneralInformation().getDiscountAccount()
																		: null);
														preparedStatement.setDouble(32,
																invoiceVo.getGeneralInformation().getDiscountValue() != null
																? invoiceVo.getGeneralInformation().getDiscountValue()
																		: 0.00);
														preparedStatement.setDouble(33,
																invoiceVo.getGeneralInformation().getAdjustmentValue() != null
																? invoiceVo.getGeneralInformation().getAdjustmentValue()
																		: 0.00);
														preparedStatement.setInt(34,
																invoiceVo.getGeneralInformation().getAdjustmentAccountId() != null
																? invoiceVo.getGeneralInformation().getAdjustmentAccountId()
																		: 0);
														preparedStatement.setString(35,
																invoiceVo.getGeneralInformation().getAdjustmentAccount() != null
																? invoiceVo.getGeneralInformation().getAdjustmentAccount()
																		: null);
														preparedStatement.setDouble(36,
																invoiceVo.getGeneralInformation().getTotal() != null
																? invoiceVo.getGeneralInformation().getTotal()
																		: 0.00);
														preparedStatement.setString(37,
																invoiceVo.getGeneralInformation().getTermsAndConditions() != null
																? invoiceVo.getGeneralInformation().getTermsAndConditions()
																		: null);
														preparedStatement.setInt(38,
																invoiceVo.getGeneralInformation().getBankId() != null
																? invoiceVo.getGeneralInformation().getBankId()
																		: 0);
														preparedStatement.setString(39,
																invoiceVo.getGeneralInformation().getBankType() != null
																? invoiceVo.getGeneralInformation().getBankType()
																		: null);
														preparedStatement.setInt(40,
																invoiceVo.getGeneralInformation().getCurrencyId() != null
																? invoiceVo.getGeneralInformation().getCurrencyId()
																		: 0);
														preparedStatement.setDouble(41,
																invoiceVo.getGeneralInformation().getExchangeRate() != null
																? invoiceVo.getGeneralInformation().getExchangeRate()
																		: 0.00);
														String status =invoiceVo.getStatus();
														preparedStatement.setString(42, status);
														preparedStatement.setDate(43, new Date(System.currentTimeMillis()));
														preparedStatement.setInt(44, Integer.valueOf(invoiceVo.getUserId()));
														preparedStatement.setInt(45, invoiceVo.getOrgId());
														preparedStatement.setBoolean(46, invoiceVo.getIsSuperAdmin());
														preparedStatement.setString(47,
																invoiceVo.getRoleName() != null ? invoiceVo.getRoleName() : CommonConstants.ROLE_SUPER_ADMIN);
														preparedStatement.setString(48, invoiceVo.getGeneralInformation().getAdjustmentAccountLevel());
														preparedStatement.setString(49, invoiceVo.getGeneralInformation().getDiscountAccountLevel());
														preparedStatement.setDouble(50,
																invoiceVo.getGeneralInformation().getTotal() != null
																? invoiceVo.getGeneralInformation().getTotal()
																		: 0.00);
														preparedStatement.setBoolean(51,
																invoiceVo.getGeneralInformation().getIsRegistered() != null
																? invoiceVo.getGeneralInformation().getIsRegistered()
																		: false);
														preparedStatement.setString(52, invoiceVo.getGeneralInformation().getNotes()!=null ? invoiceVo.getGeneralInformation().getNotes() : null);
														preparedStatement.setBoolean(53, invoiceVo.getGeneralInformation().getTogglePayment()!=null ?  invoiceVo.getGeneralInformation().getTogglePayment() : false);
														ObjectMapper mapper = new ObjectMapper();
														String newJsonData =invoiceVo.getGeneralInformation().getGeneralLedgerData()!=null ? mapper.writeValueAsString(invoiceVo.getGeneralInformation().getGeneralLedgerData()) : null;
														logger.info(newJsonData);
														preparedStatement.setString(54, newJsonData);
														preparedStatement.setInt(55, invoiceVo.getInvoiceId());
														logger.info("PrepStmnt" + preparedStatement.toString());
														preparedStatement.executeUpdate();

														if (invoiceVo.getGeneralInformation().getCustomerGstNumber() != null
																&& invoiceVo.getGeneralInformation().getCustomerId() != null) {
															customerDao.updateGstForCustomerId(invoiceVo.getGeneralInformation().getCustomerGstNumber(),
																	invoiceVo.getGeneralInformation().getCustomerId(), Integer.valueOf(invoiceVo.getUserId()),
																	connection);
															//update the Customer GST in invoice table for other Invoices against that customer 
															updateGSTNumberForCustomers(invoiceVo.getGeneralInformation().getCustomerId(), invoiceVo.getOrgId(), invoiceVo.getGeneralInformation().getCustomerGstNumber(), connection);
														}
														if (invoiceVo.getGeneralInformation().getCustomerId() != null
																&& invoiceVo.getGeneralInformation().getBillingAddress() != null
																&& invoiceVo.getGeneralInformation().getDeliveryAddress() != null) {
															customerDao.updateCustomerBillingAddress(invoiceVo.getGeneralInformation().getBillingAddress(),
																	invoiceVo.getGeneralInformation().getCustomerId(), connection);
															customerDao.updateCustomerDeliveryAddress(invoiceVo.getGeneralInformation().getDeliveryAddress(),
																	invoiceVo.getGeneralInformation().getCustomerId(), connection);
														}

														logger.info("Successfully updated into table invoice_general_information ");
			} catch (Exception e) {
				logger.info("Error in updating  to table invoice_general_information ", e);
				throw new ApplicationException(e.getMessage());
			}
		}
		return invoiceVo;
	}

	public String getInvoiceOrderNo(Integer invoiceId, Integer orgId, Connection connection)
			throws ApplicationException {
		String invoiceNo = null;
		try (final PreparedStatement preparedStatement = connection
				.prepareStatement(ArInvoiceConstants.GET_INVOICE_FOR_INVOICE_ID)) {
			preparedStatement.setInt(1, orgId);
			preparedStatement.setInt(2, invoiceId);
			try (ResultSet rs = preparedStatement.executeQuery()) {
				while (rs.next()) {
					invoiceNo = rs.getString(1).replace("~", "/");
				}
			}
		} catch (Exception e) {
			logger.info("Error in getInvoiceOrderNo  ", e);
			throw new ApplicationException(e);
		}

		return invoiceNo;
	}

	private void updateGSTNumberForCustomers(Integer customerId, Integer orgId,String gstNumber ,Connection connection)
			throws ApplicationException {
		try (final PreparedStatement preparedStatement = connection
				.prepareStatement(ArInvoiceConstants.MODIFY_CUSTOMER_GST_NUMBER)) {
			preparedStatement.setString(1, gstNumber);
			preparedStatement.setInt(2, orgId);
			preparedStatement.setInt(3, customerId);
			preparedStatement.executeUpdate();
		} catch (Exception e) {
			logger.info("Error in getInvoiceOrderNo  ", e);
			throw new ApplicationException(e);
		}
	}

	/*
	 * // to get the default date of an organization private String
	 * getDefaultDateOfOrg(Integer orgId) throws ApplicationException {
	 * logger.info("To getDefaultDateOfOrg "); String dateFormat = null; try (final
	 * Connection conn = getUserMgmConnection()) { dateFormat =
	 * organizationDao.getDefaultDateForOrganization(orgId, conn);
	 * 
	 * } catch (ApplicationException | SQLException e) {
	 * logger.info("Error in getDefaultDateOfOrg ", e); throw new
	 * ApplicationException(e); } return dateFormat;
	 * 
	 * }
	 */

	public List<BasicReceiptVo> getActiveReceiptsForInvoice(Integer invoiceId) throws ApplicationException {
		List<BasicReceiptVo> receipts = new ArrayList<BasicReceiptVo>();
		Connection con =null;
		PreparedStatement preparedStatement =null;
		ResultSet rs =null;
		try {
			con = getAccountsReceivableConnection();
			preparedStatement = con.prepareStatement(ArInvoiceConstants.GET_ACTIVE_RECEIPTS_FOR_INVOICE);
			preparedStatement.setString(1, "Receipt");
			preparedStatement.setString(2, CommonConstants.STATUS_AS_ACTIVE);
			preparedStatement.setInt(3, invoiceId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				BasicReceiptVo vo = new BasicReceiptVo();
				vo.setReceiptId(rs.getInt(1));
				vo.setInvoiceAmount(rs.getDouble(2));
				vo.setReceivedAmount(rs.getDouble(3));
				vo.setRefundTypeId(rs.getInt(4));
				vo.setReceiptStatus(rs.getString(5));
				vo.setReceiptNo(rs.getString(6));
				receipts.add(vo);
			}
		} catch (ApplicationException | SQLException e) {
			logger.info("Error in getDefaultDateOfOrg ", e);
			throw new ApplicationException(e);
		}finally{
			closeResources(rs, preparedStatement, con);
		}
		return receipts;
	}

	public List<InvoiceDetailsVo> getInvoiceDetailsByCustomerId(int orgId,int customerId, int currencyId)
			throws ApplicationException {
		logger.info("To get the getInvoiceDetailsByCustomerId");
		List<InvoiceDetailsVo> invoiceList = new ArrayList<InvoiceDetailsVo>();
		Connection con = null;
		PreparedStatement preparedStatement =null;
		ResultSet rs =null;
		try {
			con = getAccountsReceivableConnection();
			preparedStatement = con.prepareStatement(ArInvoiceConstants.GET_INVOICE_DETAILS_FOR_CUSTOMER);
			preparedStatement.setInt(1, orgId);
			preparedStatement.setInt(2, customerId);
			preparedStatement.setInt(3, currencyId);
			preparedStatement.setString(4, CommonConstants.STATUS_AS_ACTIVE);
			preparedStatement.setString(5, CommonConstants.STATUS_AS_OPEN);	
			preparedStatement.setString(6, CommonConstants.STATUS_AS_PARTIALLY_PAID);
			preparedStatement.setString(7, CommonConstants.STATUS_AS_OVERDUE);	
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				InvoiceDetailsVo invoiceDetailsVo = new InvoiceDetailsVo();
				invoiceDetailsVo.setId(rs.getInt(1));
				invoiceDetailsVo.setValue(rs.getInt(1));
				String invoiceNo = rs.getString(2) != null ? rs.getString(2).replace("~", "/") : null;
				logger.info("invoiceNo is::" + invoiceNo);
				invoiceDetailsVo.setInvoiceNo(invoiceNo);
				invoiceDetailsVo.setName(invoiceNo);
				invoiceDetailsVo.setAmount(rs.getString(3));
				invoiceList.add(invoiceDetailsVo);
			}
		} catch (Exception e) {
			logger.info("Error in getInvoiceDetailsByCustomerId", e);
			throw new ApplicationException(e.getMessage());
		}finally{
			closeResources(rs, preparedStatement, con);
		}
		return invoiceList;
	}

	public List<InvoiceDetailsVo> getAllInvoicesOfCustomerCurrency(int orgId,int customerId, int currencyId)
			throws ApplicationException {
		logger.info("To get the getInvoiceDetailsByCustomerId");
		List<InvoiceDetailsVo> invoiceList = new ArrayList<InvoiceDetailsVo>();
		Connection con = null;
		PreparedStatement preparedStatement =null;
		ResultSet rs =null;
		try {
			con = getAccountsReceivableConnection();
			preparedStatement = con.prepareStatement(ArInvoiceConstants.GET_ALL_INVOICES_FOR_CUSTOMER_CURRENCY);
			preparedStatement.setInt(1, orgId);
			preparedStatement.setInt(2, customerId);
			preparedStatement.setInt(3, currencyId);
			preparedStatement.setString(4, CommonConstants.STATUS_AS_DRAFT);
			preparedStatement.setString(5, CommonConstants.STATUS_AS_VOID);	
				
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				InvoiceDetailsVo invoiceDetailsVo = new InvoiceDetailsVo();
				invoiceDetailsVo.setId(rs.getInt(1));
				invoiceDetailsVo.setValue(rs.getInt(1));
				String invoiceNo = rs.getString(2) != null ? rs.getString(2).replace("~", "/") : null;
				logger.info("invoiceNo is::" + invoiceNo);
				invoiceDetailsVo.setInvoiceNo(invoiceNo);
				invoiceDetailsVo.setName(invoiceNo);
				invoiceDetailsVo.setAmount(rs.getString(3));
				invoiceList.add(invoiceDetailsVo);
			}
		} catch (Exception e) {
			logger.info("Error in getInvoiceDetailsByCustomerId", e);
			throw new ApplicationException(e.getMessage());
		}finally{
			closeResources(rs, preparedStatement, con);
		}
		return invoiceList;
	}

	public WorkflowInvoiceVo getWorkflowRequiredDataForInvoiceById(Integer invoiceId) throws ApplicationException {
		logger.info("To  getWorkflowRequiredDataForInvoiceById for:::" + invoiceId);
		WorkflowInvoiceVo invoiceVo = null;
		Connection connection = null;
		PreparedStatement preparedStatement=null;
		ResultSet rs=null;
		if (invoiceId != null) {
			try {
				connection = getAccountsReceivableConnection();
				preparedStatement=connection.prepareStatement(ArInvoiceConstants.GET_WORKFLOW_DATA_FOR_INVOICE);
				preparedStatement.setInt(1, invoiceId);
				rs=preparedStatement.executeQuery();
				while (rs.next()) {
					invoiceVo = new WorkflowInvoiceVo();
					invoiceVo.setId(rs.getInt(1));
					invoiceVo.setOrganizationId(rs.getInt(2));
					invoiceVo.setInvoiceValue(rs.getString(3));
					invoiceVo.setCurrentRuleId(rs.getInt(4));
					invoiceVo.setStatus(rs.getString(5));
					invoiceVo.setPendingApprovalStatus(rs.getString(6));
					invoiceVo.setInvoiceNumber(rs.getString(7) != null ? rs.getString(7).replace("~", "/") : null);
					invoiceVo.setDueBalance(rs.getString(8)!=null?rs.getBigDecimal(8):BigDecimal.ZERO);
					int currencyId=rs.getInt(9);
					if(currencyId>0) {
					CurrencyVo currencyVo=currencyDao.getCurrency(currencyId);
					invoiceVo.setCurrencySymbol(currencyVo.getSymbol()!=null?currencyVo.getSymbol():"");
					}
				}
				logger.info("Succecssfully fetched invoice in ARInvoiceDao + " + invoiceVo);
			} catch (Exception e) {
				logger.info("Error in getWorkflowRequiredDataForInvoiceById:: ", e);
				throw new ApplicationException(e);
			}finally {
				closeResources(rs, preparedStatement, connection);
			}
		}
		return invoiceVo;

	}

	public boolean updateCurrentWorkflowRule(int invoiceId,int currentRuleId,String status,String pendingApprovalStatus) throws ApplicationException {
		logger.info("Entry into updateCurrentWorkflowRule :invoiceId:"+invoiceId+",currentRuleId"+currentRuleId+",status:"+status+",pendingApprovalStatus+"+pendingApprovalStatus);
		Connection con = null;
		PreparedStatement preparedStatement =null;
		try {
			con = getAccountsReceivableConnection();
			preparedStatement = con.prepareStatement(ArInvoiceConstants.UPDATE_CURRENT_RULE);
			preparedStatement.setInt(1, currentRuleId);
			preparedStatement.setString(2, status);
			preparedStatement.setString(3,pendingApprovalStatus);
			preparedStatement.setInt(4, invoiceId);
			preparedStatement.executeUpdate();
		} catch (Exception e) {
			logger.info("Error in updateCurrentWorkflowRule ", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(null, preparedStatement, con);			
		}
		return false;

	}


	public List<ArInvoiceListVo> getDashboardInvoiceList(int orgId) throws ApplicationException {
		logger.info("To get the getDashboardInvoiceList :::" + orgId);
		Connection connection = null;
		List<ArInvoiceListVo> listVos = new ArrayList<ArInvoiceListVo>();
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			connection = getAccountsReceivableConnection();
			preparedStatement = connection.prepareStatement(ArInvoiceConstants.GET_DASHBOARD_INVOICE_LIST);
			preparedStatement.setInt(1, orgId);
			preparedStatement.setString(2, CommonConstants.STATUS_AS_UNPAID);
			preparedStatement.setString(3, CommonConstants.STATUS_AS_PARTIALLY_PAID);
			preparedStatement.setString(4, CommonConstants.STATUS_AS_OVERDUE);
			rs = preparedStatement.executeQuery();
			
			while (rs.next()) {
				ArInvoiceListVo listVo = new ArInvoiceListVo();
				int invoiceId = rs.getInt(1);
				// BasicInvoiceDetailsVo
				// invoiceDetailsVo=getInvoiceDueBalanceAndStatus(invoiceId);
				listVo.setInvoiceId(invoiceId);
				listVo.setCustomerName(rs.getString(2));
				String invoiceNo = rs.getString(3) != null ? rs.getString(3).replace("~", "/") : null;
				logger.info("invoiceNo is::" + invoiceNo);
				listVo.setInvoiceNumber(invoiceNo);
				listVo.setPoNumber(rs.getString(4));
				Date invoiceDate = rs.getDate(5);
				String invoiceConvertedDate = DateConverter.getInstance().convertDateToGivenFormat(invoiceDate,
							"yyyy-MM-dd");
				listVo.setInvoiceDate(invoiceConvertedDate);
				Date dueDate = rs.getDate(6);
				String duedate = DateConverter.getInstance().convertDateToGivenFormat(dueDate, "yyyy-MM-dd");
				listVo.setDueDate(duedate);
				String status = rs.getString(7);
				String displayStatus = null;
				logger.info("Invoice Id:" + invoiceId + ",Status:" + status);
				if (status != null) {
					switch (status) {
					case CommonConstants.STATUS_AS_UNPAID:
						displayStatus = CommonConstants.DISPLAY_STATUS_AS_UNPAID;
						break;
					case CommonConstants.STATUS_AS_OVERDUE:
						displayStatus = CommonConstants.DISPLAY_STATUS_AS_OVERDUE;
						break;
					case CommonConstants.STATUS_AS_PARTIALLY_PAID:
						displayStatus = CommonConstants.DISPLAY_STATUS_AS_PARTIALLY_PAID;
						break;

					}
				}
				listVo.setStatus(displayStatus);
				int digits = rs.getInt(12);
				BigDecimal bdTotal = new BigDecimal(rs.getDouble(8) > 0 ? rs.getDouble(8) : 0).setScale(digits,
						RoundingMode.HALF_UP);
				listVo.setTotal(bdTotal.doubleValue());
				BigDecimal bdBalDue = new BigDecimal(rs.getDouble(9) > 0 ? rs.getDouble(9) : 0).setScale(digits,
						RoundingMode.HALF_UP);
				listVo.setBalanceDue(bdBalDue.doubleValue());

				listVo.setCurrencySymbol(rs.getString(10));
				listVo.setCurrencyId(rs.getInt(11));
				listVo.setCustomerId(rs.getInt(13));
				listVo.setPendingApprovalStatus(rs.getString(14));
				listVos.add(listVo);
			}
			logger.info("listVos size::" + listVos.size());
			logger.info("Retrieved Pos ");

		} catch (Exception e) {
			logger.info("Error in getAllFilteredPos:: ", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, connection);
		}
		return listVos;

	}
	
	public Map<String , String > getStatusPercent(int orgId) throws ApplicationException{
		Map<String , String > statusMap = new HashMap<String, String>();
		Connection con = null;
		PreparedStatement preparedStatement =null;
		ResultSet rs =null;
		try{
			con = getAccountsPayable();
			preparedStatement = con.prepareStatement(ArInvoiceConstants.GET_STATUS_AND_ITS_PERCENT);
			preparedStatement.setInt(1, orgId);
			preparedStatement.setInt(2, orgId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				String status = rs.getString(1);
				String displayStatus = null;
				switch (status) {
				case CommonConstants.STATUS_AS_ACTIVE:
					displayStatus = CommonConstants.DISPLAY_STATUS_AS_ACTIVE;
					break;
				case CommonConstants.STATUS_AS_VOID:
					displayStatus = CommonConstants.DISPLAY_STATUS_AS_VOID;
					break;
				case CommonConstants.STATUS_AS_DRAFT:
					displayStatus = CommonConstants.DISPLAY_STATUS_AS_DRAFT;
					break;
				case CommonConstants.STATUS_AS_INACTIVE:
					displayStatus = CommonConstants.DISPLAY_STATUS_AS_INACTIVE;
					break;
				case CommonConstants.STATUS_AS_ACCEPT:
					displayStatus = CommonConstants.DISPLAY_STATUS_AS_ACCEPT;
					break;
				case CommonConstants.STATUS_AS_DECLINE:
					displayStatus = CommonConstants.DISPLAY_STATUS_AS_DECLINE;
					break;
				case CommonConstants.STATUS_AS_EXPIRED:
					displayStatus = CommonConstants.DISPLAY_STATUS_AS_EXPIRED;
					break;
				case CommonConstants.STATUS_AS_OPEN:
					displayStatus = CommonConstants.DISPLAY_STATUS_AS_OPEN;
					break;
				case CommonConstants.STATUS_AS_PAID:
					displayStatus = CommonConstants.DISPLAY_STATUS_AS_PAID;
					break;
				case CommonConstants.STATUS_AS_UNPAID:
					displayStatus = CommonConstants.DISPLAY_STATUS_AS_UNPAID;
					break;
				case CommonConstants.STATUS_AS_OVERDUE:
					displayStatus = CommonConstants.DISPLAY_STATUS_AS_OVERDUE;
					break;
				case CommonConstants.STATUS_AS_PARTIALLY_PAID:
					displayStatus = CommonConstants.DISPLAY_STATUS_AS_PARTIALLY_PAID;
					break;

				}
				
				statusMap.put(displayStatus, rs.getString(2)) ;
			}
		} catch (Exception e) {
			logger.info("Error in getStatusPercent  ", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}

		return statusMap;
	}

}