package com.blackstrawai.ap;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.DateConverter;
import com.blackstrawai.ap.billsinvoice.BillsInvoiceDashboardVo;
import com.blackstrawai.ap.billsinvoice.CommonTransactionVo;
import com.blackstrawai.ap.billsinvoice.InvoiceFilterVo;
import com.blackstrawai.ap.billsinvoice.InvoiceGeneralInfoVo;
import com.blackstrawai.ap.billsinvoice.InvoiceListVo;
import com.blackstrawai.ap.billsinvoice.InvoiceProductVo;
import com.blackstrawai.ap.billsinvoice.InvoiceTaxDetailsVo;
import com.blackstrawai.ap.billsinvoice.InvoiceTaxDistributionVo;
import com.blackstrawai.ap.billsinvoice.InvoiceTrasactionVo;
import com.blackstrawai.ap.billsinvoice.InvoiceVo;
import com.blackstrawai.ap.billsinvoice.QuickInvoiceVo;
import com.blackstrawai.ap.creditnote.ApplyFundVo;
import com.blackstrawai.ap.dropdowns.PaymentNonCoreBillDetailsDropDownVo;
import com.blackstrawai.ap.dropdowns.VendorCreditInvoiceVo;
import com.blackstrawai.ap.payment.noncore.BillsPayableItemVo;
import com.blackstrawai.ap.payment.noncore.BillsPayableVo;
import com.blackstrawai.ap.payment.noncore.PaymentNonCoreBaseVo;
import com.blackstrawai.ap.payment.noncore.PaymentNonCoreVo;
import com.blackstrawai.ar.ReceiptConstants;
import com.blackstrawai.attachments.AttachmentsConstants;
import com.blackstrawai.attachments.AttachmentsDao;
import com.blackstrawai.attachments.AttachmentsVo;
import com.blackstrawai.attachments.UploadFileVo;
import com.blackstrawai.common.BaseDao;
import com.blackstrawai.common.CommonConstants;
import com.blackstrawai.externalintegration.banking.yesbank.YesBankConstants;
import com.blackstrawai.inventorymgmt.ProductInventoryDao;
import com.blackstrawai.journals.GeneralLedgerVo;
import com.blackstrawai.journals.JournalEntriesConstants;
import com.blackstrawai.onboarding.OrganizationDao;
import com.blackstrawai.onboarding.organization.BasicLocationDetailsVo;
import com.blackstrawai.settings.CurrencyDao;
import com.blackstrawai.settings.CurrencyVo;
import com.blackstrawai.vendorsettings.SettingsModuleVo;
import com.blackstrawai.vendorsettings.VamVendorSettingsVo;
import com.blackstrawai.vendorsettings.VendorAccessMgmtDao;
import com.blackstrawai.workflow.WorkflowInvoiceVo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysql.jdbc.Statement;

@Repository
public class BillsInvoiceDao extends BaseDao {
	private Logger logger = Logger.getLogger(BillsInvoiceDao.class);

	@Autowired
	private AttachmentsDao attachmentsDao;

	@Autowired
	private OrganizationDao organizationDao;

	@Autowired
	private VendorAccessMgmtDao vendorAccessMgmtDao;

	@Autowired
	private ProductInventoryDao inventoryDao;

	@Autowired
	private PaymentNonCoreDao paymentDao;

	@Autowired
	private CurrencyDao currencyDao;
	
	@Autowired
	private VendorCreditDao vendorCreditDao;

	// Create New Invoice
	public InvoiceVo createInvoice(InvoiceVo invoiceVo) throws SQLException, ApplicationException {
		logger.info("To create a new Invoice in BillsInvoiceDao:::" + invoiceVo);
		Connection connection = null;
		if (invoiceVo != null) {
			try {
				connection = getAccountsPayable();
				connection.setAutoCommit(false);
				createInvoiceGeneralInfo(invoiceVo, connection);
				if (invoiceVo.getId() != null) {
					if (invoiceVo.getTransactionDetails().getProducts() != null) {
						for (InvoiceProductVo product : invoiceVo.getTransactionDetails().getProducts()) {
							Integer productId = createProduct(product, connection, invoiceVo.getId());
							createTaxDetailsForProduct(productId, product, connection,
									invoiceVo.getIsInvoiceWithBills());
							inventoryDao.addAPInvoiceProductToInventoryMgmt(productId ,product, invoiceVo);

						}
					}
					createTransactionDetails(invoiceVo.getTransactionDetails(), connection, invoiceVo.getId());
				}
				if (invoiceVo.getIsInvoiceWithBills()) {
					attachmentsDao.createAttachments(invoiceVo.getOrganizationId(), invoiceVo.getUserId(),
							invoiceVo.getAttachments(), AttachmentsConstants.MODULE_TYPE_INVOICES_BILL,
							invoiceVo.getId(), invoiceVo.getRoleName());
				} else {
					attachmentsDao.createAttachments(invoiceVo.getOrganizationId(), invoiceVo.getUserId(),
							invoiceVo.getAttachments(), AttachmentsConstants.MODULE_TYPE_INVOICES_WITHOUT_BILL,
							invoiceVo.getId(), invoiceVo.getRoleName());
				}
				connection.commit();
				logger.info("Succecssfully created invoice in BillsInvoiceDao");
			} catch (ApplicationException e) {
				logger.info("Error in createInvoice:: ", e);
				connection.rollback();
				logger.info("Before throw in dao :: " + e.getMessage());
				throw e;
			} finally {
				closeResources(null,null, connection);
			}
		}
		return invoiceVo;
	}

	public InvoiceVo getInvoiceById(Integer invoiceId) throws ApplicationException {
		logger.info("To get Invoice in BillsInvoiceDao for:::" + invoiceId);
		InvoiceVo invoiceVo = null;
		if (invoiceId != null) {
			Connection con = null;
			try  {
				con = getAccountsPayable();
				invoiceVo = new InvoiceVo();
				invoiceVo.setId(invoiceId);
				getInvoiceGeneralInfoById(invoiceVo, con);
				InvoiceTrasactionVo trasactionVo = getInvoiceTransactionDetailsByInvoiceId(invoiceId, con);
				List<InvoiceProductVo> products = getProductListForInvoices(invoiceId, con,
						invoiceVo.getIsInvoiceWithBills());
				if (trasactionVo != null)
					trasactionVo.setProducts(products);
				invoiceVo.setTransactionDetails(trasactionVo);
				List<UploadFileVo> uploadFileVos = new ArrayList<UploadFileVo>();
				String moduleType = invoiceVo.getIsInvoiceWithBills() ? AttachmentsConstants.MODULE_TYPE_INVOICES_BILL
						: AttachmentsConstants.MODULE_TYPE_INVOICES_WITHOUT_BILL;
				for (AttachmentsVo attachments : attachmentsDao.getAttachmentDetails(invoiceId, moduleType)) {
					UploadFileVo uploadFileVo = new UploadFileVo();
					uploadFileVo.setId(attachments.getId());
					uploadFileVo.setName(attachments.getFileName());
					uploadFileVo.setSize(attachments.getSize());
					uploadFileVo.setIsAkshar(attachments.getIsAkshar());
					uploadFileVos.add(uploadFileVo);
				}
				invoiceVo.setAttachments(uploadFileVos);
				logger.info("Succecssfully fetched invoice in BillsInvoiceDao + " + invoiceVo);
			} catch (Exception e) {
				logger.info("Error in getInvoiceById:: ", e);
				throw new ApplicationException(e);
			}finally{
				closeResources(null,null, con);
			}
		}
		return invoiceVo;

	}

	// Create New Invoice
	public InvoiceVo updateInvoice(InvoiceVo invoiceVo) throws ApplicationException, SQLException {
		logger.info("To update a new Invoice in BillsInvoiceDao:::" + invoiceVo);
		Connection connection = null;
		if (invoiceVo != null && invoiceVo.getId() != null) {
			try {
				connection = getAccountsPayable();
				connection.setAutoCommit(false);
				updateInvoiceGeneralInfo(invoiceVo, connection);
				if (invoiceVo.getTransactionDetails().getProducts() != null) {
					for (InvoiceProductVo product : invoiceVo.getTransactionDetails().getProducts()) {
						if (product.getStatus() != null) {
							switch (product.getStatus().toUpperCase()) {
							case CommonConstants.STATUS_AS_NEW:
								Integer productId = createProduct(product, connection, invoiceVo.getId());
								createTaxDetailsForProduct(productId, product, connection,
										invoiceVo.getIsInvoiceWithBills());
								inventoryDao.addAPInvoiceProductToInventoryMgmt(productId , product, invoiceVo);

								break;
							case CommonConstants.STATUS_AS_ACTIVE:
								boolean updateStatus = updateProduct(product, connection, invoiceVo.getId());
								if (updateStatus)
									updateTaxDetailsForProduct(product.getId(), product, connection,
											invoiceVo.getIsInvoiceWithBills());
								inventoryDao.updateAPInvoiceProductToInventoryMgmt(product, invoiceVo);

								break;
							case CommonConstants.STATUS_AS_DELETE:
								changeStatusForInvoiceTables(product.getId(), CommonConstants.STATUS_AS_DELETE,
										connection, BillsInvoiceConstants.MODIFY_INVOICE_PRODUCT_STATUS_FOR_PRODUCT_ID);
								deleteTaxDetails(product.getId(), connection, invoiceVo.getIsInvoiceWithBills());
								inventoryDao.deleteAPInvoiceProductToInventoryMgmt(product, invoiceVo);

								break;
							}
						}
					}
					updateTransactionDetails(invoiceVo.getTransactionDetails(), connection, invoiceVo.getId());
				}

				if (invoiceVo.getAttachments() != null && invoiceVo.getAttachments().size() > 0) {
					if (invoiceVo.getIsInvoiceWithBills()) {
						attachmentsDao.createAttachments(invoiceVo.getOrganizationId(), invoiceVo.getUserId(),
								invoiceVo.getAttachments(), AttachmentsConstants.MODULE_TYPE_INVOICES_BILL,
								invoiceVo.getId(), invoiceVo.getRoleName());
					} else {
						attachmentsDao.createAttachments(invoiceVo.getOrganizationId(), invoiceVo.getUserId(),
								invoiceVo.getAttachments(), AttachmentsConstants.MODULE_TYPE_INVOICES_WITHOUT_BILL,
								invoiceVo.getId(), invoiceVo.getRoleName());
					}
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
				closeResources(null, null, connection);
			}
		}
		return invoiceVo;
	}


	public WorkflowInvoiceVo getWorkflowRequiredDataForInvoiceById(Integer invoiceId) throws ApplicationException {
		logger.info("To  getWorkflowRequiredDataForInvoiceById for:::" + invoiceId);
		WorkflowInvoiceVo invoiceVo = null;
		Connection connection = null;
		PreparedStatement preparedStatement=null;
		ResultSet rs=null;
		if (invoiceId != null) {
			try {
				connection = getAccountsPayable();
				preparedStatement=connection.prepareStatement(BillsInvoiceConstants.GET_WORKFLOW_DATA_FOR_INVOICE);
				preparedStatement.setInt(1, invoiceId);
				rs=preparedStatement.executeQuery();
				while (rs.next()) {
					invoiceVo = new WorkflowInvoiceVo();
					invoiceVo.setId(rs.getInt(1));
					invoiceVo.setOrganizationId(rs.getInt(2));
					int locationId=rs.getInt(3);
					String gstn=rs.getString(4);
					boolean isRegistered=rs.getBoolean(5);
					BasicLocationDetailsVo locationVo=organizationDao.getLocationById(rs.getInt(2), locationId, isRegistered, gstn);
					invoiceVo.setLocationId(locationId);
					invoiceVo.setRegistered(isRegistered);
					invoiceVo.setGstNumber(gstn);
					invoiceVo.setLocation(locationVo!=null ?locationVo.getName():"");
					invoiceVo.setVendorName(rs.getString(6));
					if(rs.getString(7)!=null && rs.getString(7).length()>0) {
						invoiceVo.setGstEnabled(true);
					}	
					invoiceVo.setInvoiceValue(rs.getString(8));
					invoiceVo.setVendorId(rs.getLong(9));
					invoiceVo.setCurrentRuleId(rs.getInt(10));
					invoiceVo.setStatus(rs.getString(11));
					invoiceVo.setPendingApprovalStatus(rs.getString(12));
					invoiceVo.setInvoiceNumber(rs.getString(13));
					invoiceVo.setInvoiceWithBills(rs.getBoolean(14));
					int currencyId=rs.getInt(15);
					if(currencyId>0) {
						CurrencyVo currencyVo=currencyDao.getCurrency(currencyId);
						invoiceVo.setCurrencySymbol(currencyVo.getSymbol()!=null?currencyVo.getSymbol():"");
					}
				}
				logger.info("Succecssfully fetched invoice in BillsInvoiceDao + " + invoiceVo);
			} catch (Exception e) {
				logger.info("Error in getWorkflowRequiredDataForInvoiceById:: ", e);
				throw new ApplicationException(e);
			}finally {
				closeResources(rs, preparedStatement, connection);
			}
		}
		return invoiceVo;

	}
	private InvoiceVo createInvoiceGeneralInfo(InvoiceVo invoiceVo, Connection connection) throws ApplicationException {
		logger.info("To insert into table invoice_general_information ");
		if (invoiceVo != null && invoiceVo.getGeneralInfo() != null && invoiceVo.getOrganizationId() != null) {
			if (invoiceVo.getIsInvoiceWithBills() != null && invoiceVo.getIsInvoiceWithBills()&& invoiceVo.getGeneralInfo() == null) {
				throw new ApplicationException("Invoice number is mandatory");
			}
			PreparedStatement preparedStatement =null;
			ResultSet rs =null;
			try {
				preparedStatement = connection.prepareStatement(BillsInvoiceConstants.INSERT_INTO_INVOICE_GENERAL_INFO, Statement.RETURN_GENERATED_KEYS);
				InvoiceGeneralInfoVo invoiceGenInfo=invoiceVo.getGeneralInfo();
				StringBuilder invoiceNumberBuilder = new StringBuilder();
				invoiceNumberBuilder
				.append(invoiceGenInfo.getInvoiceNoPrefix() != null && !invoiceGenInfo.getInvoiceNoPrefix().isEmpty() ? invoiceGenInfo.getInvoiceNoPrefix() : " ")
				.append("/").append(invoiceGenInfo.getInvoiceNo()).append("/")
				.append(invoiceGenInfo.getInvoiceNoSuffix() != null && !invoiceGenInfo.getInvoiceNoSuffix().isEmpty()?  invoiceGenInfo.getInvoiceNoSuffix()
						: " ");
				String invoiceNumber = invoiceNumberBuilder.toString();
				logger.info("Invoice No :: "+ invoiceNumber);
				boolean isInvoiceExist = checkInvoiceNoExistForAnOrganization(invoiceVo.getOrganizationId(),invoiceGenInfo.getVendorId(), connection,
						invoiceNumber);
				if (isInvoiceExist) {
					throw new ApplicationException("Invoice number already exist for the organization");
				}
				String dateFormat = getDefaultDateOfOrg(invoiceVo.getOrganizationId());
				preparedStatement.setInt(1,
						invoiceGenInfo.getVendorId() != null ? invoiceGenInfo.getVendorId()
								: 0);
				if (invoiceGenInfo.getInvoiceDate() != null
						&& !"".equals(invoiceGenInfo.getInvoiceDate())) {
					Date invDate = DateConverter.getInstance()
							.convertStringToDate(invoiceGenInfo.getInvoiceDate(), dateFormat);
					if (invDate != null) {
						String date = DateConverter.getInstance().convertDateToGivenFormat(invDate, "yyyy-MM-dd");
						logger.info("converted dueDate::" + date);
						preparedStatement.setString(2, date);
					} else {
						logger.info("converted dueDate set as null");
						preparedStatement.setString(2, null);
					}
				} else {
					preparedStatement.setString(2, invoiceGenInfo.getInvoiceDate());
				}
				preparedStatement.setInt(3,
						invoiceGenInfo.getLocationId() != null ? invoiceGenInfo.getLocationId()
								: 0);
				preparedStatement.setString(4,
						invoiceGenInfo.getGstNumberId() != null
						? invoiceGenInfo.getGstNumberId()
								: null);
				preparedStatement.setString(5,
						invoiceNumber != null ? invoiceNumber
								: null);
				preparedStatement.setString(6,
						invoiceGenInfo.getPoReferenceNo() != null
						? invoiceGenInfo.getPoReferenceNo()
								: null);
				preparedStatement.setInt(7,
						invoiceGenInfo.getPaymentTermsId() != null
						? invoiceGenInfo.getPaymentTermsId()
								: 0);

				if (invoiceGenInfo.getDueDate() != null
						&& !"".equals(invoiceGenInfo.getDueDate())) {
					Date dueDate = DateConverter.getInstance()
							.convertStringToDate(invoiceGenInfo.getDueDate(), dateFormat);
					if (dueDate != null) {
						String date = DateConverter.getInstance().convertDateToGivenFormat(dueDate, "yyyy-MM-dd");
						logger.info("convertedDate::" + date);
						preparedStatement.setString(8, date);
					} else {
						logger.info("convertedDate set as null");
						preparedStatement.setString(8, null);
					}
				} else {
					preparedStatement.setString(8, invoiceGenInfo.getDueDate());
				}
				preparedStatement.setString(9,
						invoiceGenInfo.getNotes() != null ? invoiceGenInfo.getNotes() : null);
				preparedStatement.setString(10,
						invoiceGenInfo.getTermsAndConditions() != null
						? invoiceGenInfo.getTermsAndConditions()
								: null);
				preparedStatement.setString(11,
						invoiceVo.getStatus() != null ? invoiceVo.getStatus() : CommonConstants.STATUS_AS_ACTIVE);
				preparedStatement.setBoolean(12,
						invoiceVo.getIsInvoiceWithBills() != null ? invoiceVo.getIsInvoiceWithBills() : false);
				preparedStatement.setDate(13, new Date(System.currentTimeMillis()));
				preparedStatement.setInt(14, Integer.valueOf(invoiceVo.getUserId()));
				preparedStatement.setInt(15, invoiceVo.getOrganizationId());
				preparedStatement.setBoolean(16, invoiceVo.getIsSuperAdmin());
				preparedStatement.setString(17, invoiceVo.getRoleName());
				preparedStatement.setBoolean(18, invoiceGenInfo.getIsRegistered());
				preparedStatement.setString(19, invoiceGenInfo.getAksharData());
				ObjectMapper mapper = new ObjectMapper();
				String glData = invoiceVo.getGeneralLedgerData() != null
						? mapper.writeValueAsString(invoiceVo.getGeneralLedgerData())
								: null;
						logger.info("glData>> " + glData);
						preparedStatement.setString(20, glData);
						preparedStatement.setString(21, invoiceGenInfo.getVendorGstNo());
						int rowAffected = preparedStatement.executeUpdate();
						if (rowAffected == 1) {
							rs = preparedStatement.getGeneratedKeys();
							while (rs.next()) {
								invoiceVo.setId(rs.getInt(1));
							}
						}
						logger.info("Successfully inserted into table invoice_general_information ");
			} catch (Exception e) {
				logger.info("Error in inserting to table invoice_general_information ", e);
				throw new ApplicationException(e.getMessage());
			}finally {
				closeResources(rs, preparedStatement, null);
			}
		}
		return invoiceVo;

	}

	private Integer createProduct(InvoiceProductVo product, Connection connection, Integer id)
			throws ApplicationException {
		logger.info("To insert into table invoice_product " + product);
		Integer generatedId = null;
		if (product != null && id != null) {
			PreparedStatement preparedStatement =null;
			ResultSet rs=null;
			try  {
				preparedStatement = connection.prepareStatement(BillsInvoiceConstants.INSERT_INTO_INVOICE_PRODUCT, Statement.RETURN_GENERATED_KEYS);
				preparedStatement.setInt(1, product.getProductId() != null ? product.getProductId() : 0);
				preparedStatement.setInt(2, product.getProductAccountId() != null ? product.getProductAccountId() : 0);
				preparedStatement.setString(3, product.getQuantity() != null ? product.getQuantity() : null);
				preparedStatement.setInt(4, product.getMeasureId() != null ? product.getMeasureId() : 0);
				preparedStatement.setDouble(5, product.getUnitPrice() != null ? product.getUnitPrice() : 0.00);
				preparedStatement.setInt(6, product.getTaxRateId() != null ? product.getTaxRateId() : 0);
				preparedStatement.setInt(7, product.getCustomerId() != null ? product.getCustomerId() : 0);
				preparedStatement.setBoolean(8, product.getIsBillable() != null ? product.getIsBillable() : false);
				preparedStatement.setString(9,
						product.getInputTaxCredit() != null ? product.getInputTaxCredit() : null);
				preparedStatement.setDouble(10, product.getAmount() != null ? product.getAmount() : 0.00);
				preparedStatement.setString(11, CommonConstants.STATUS_AS_ACTIVE);
				preparedStatement.setDate(12, new Date(System.currentTimeMillis()));
				preparedStatement.setInt(13, id);
				preparedStatement.setString(14, product.getProductAccountName());
				preparedStatement.setString(15, product.getProductAccountLevel());
				preparedStatement.setString(16, product.getDescription());
				int rowAffected = preparedStatement.executeUpdate();
				if (rowAffected == 1) {
					rs = preparedStatement.getGeneratedKeys();
					while (rs.next()) {
						generatedId = rs.getInt(1);
					}
				}
				logger.info("Successfully inserted into table invoice_product with Id ::" + generatedId);
			} catch (Exception e) {
				logger.info("Error in inserting to table invoice_product ", e);
				throw new ApplicationException(e);
			}finally {
				closeResources(rs, preparedStatement, null);
			}
		}
		return generatedId;
	}

	// WHen tax rate is not applicable the tax rate id would be 0. So validating tax
	// rate is not 0
	private void createTaxDetailsForProduct(Integer productId, InvoiceProductVo product, Connection connection,
			Boolean isInvoiceWithBills) throws ApplicationException {
		logger.info("To insert into table accounts_payable_tax_details " + product);
		if (productId != null && product != null && product.getTaxRateId() != null && product.getTaxRateId() != 0
				&& product.getTaxDetails() != null && product.getTaxDetails().getTaxDistribution() != null) {
			PreparedStatement preparedStatement=null;
			for (InvoiceTaxDistributionVo distributionVo : product.getTaxDetails().getTaxDistribution()) {
				try  {
					preparedStatement = connection.prepareStatement(BillsInvoiceConstants.INSERT_INTO_TAX_DETAILS);
					String moduleType = (isInvoiceWithBills ? BillsInvoiceConstants.MODULE_TYPE_INVOICES_BILL
							: BillsInvoiceConstants.MODULE_TYPE_INVOICES_WITHOUT_BILL);
					preparedStatement.setString(1, moduleType);
					preparedStatement.setInt(2, productId);
					preparedStatement.setString(3, product.getTaxDetails().getGroupName().replace("%", ""));
					preparedStatement.setString(4, distributionVo.getTaxName());
					preparedStatement.setString(5, distributionVo.getTaxRate());
					preparedStatement.setDouble(6, distributionVo.getTaxAmount());
					preparedStatement.setDate(7, new Date(System.currentTimeMillis()));
					preparedStatement.executeUpdate();
					logger.info("Successfully inserted into table accounts_payable_tax_details");

				} catch (Exception e) {
					logger.info("Error in inserting to table accounts_payable_tax_details ", e);
					throw new ApplicationException(e);
				}finally {
					closeResources(null, preparedStatement, null);
				}
			}
		}
	}

	private void createTransactionDetails(InvoiceTrasactionVo transactionDetails, Connection connection,
			Integer invoiceId) throws ApplicationException {
		logger.info("To insert into table invoice_transaction_details " + transactionDetails);
		if (invoiceId != null && transactionDetails != null) {
			PreparedStatement preparedStatement = null;
			try {
				preparedStatement = connection.prepareStatement(BillsInvoiceConstants.INSERT_INTO_INVOICE_TRANSACTION_DETAILS); 
				preparedStatement.setInt(1,
						transactionDetails.getSourceOfSupplyId() != null ? transactionDetails.getSourceOfSupplyId()
								: 0);
				preparedStatement.setInt(2,
						transactionDetails.getDestinationOfSupplyId() != null
						? transactionDetails.getDestinationOfSupplyId()
								: 0);
				preparedStatement.setInt(3,
						transactionDetails.getTaxApplicationMethodId() != null
						? transactionDetails.getTaxApplicationMethodId()
								: 0);
				preparedStatement.setInt(4,
						transactionDetails.getCurrencyid() != null ? transactionDetails.getCurrencyid() : 0);
				preparedStatement.setDouble(5,
						transactionDetails.getExchangeRate() != null ? transactionDetails.getExchangeRate() : 0.00);
				preparedStatement.setDouble(6,
						transactionDetails.getSubTotal() != null ? transactionDetails.getSubTotal() : 0.00);
				preparedStatement.setDouble(7,
						transactionDetails.getDiscountValue() != null ? transactionDetails.getDiscountValue() : 0.00);
				preparedStatement.setInt(8,
						transactionDetails.getDiscountTypeId() != null ? transactionDetails.getDiscountTypeId() : 0);
				preparedStatement.setDouble(9,
						transactionDetails.getDiscountAmount() != null ? transactionDetails.getDiscountAmount() : 0.00);
				preparedStatement.setInt(10,
						transactionDetails.getDiscountAccountId() != null ? transactionDetails.getDiscountAccountId()
								: 0);
				preparedStatement.setBoolean(11, transactionDetails.getIsApplyAfterTax());
				preparedStatement.setInt(12, transactionDetails.getTdsId() != null ? transactionDetails.getTdsId() : 0);
				preparedStatement.setDouble(13,
						transactionDetails.getTdsValue() != null ? transactionDetails.getTdsValue() : 0);
				preparedStatement.setDouble(14,
						transactionDetails.getAdjustment() != null ? transactionDetails.getAdjustment() : 0.00);
				preparedStatement.setInt(15,
						transactionDetails.getAdjustmentAccountId() != null
						? transactionDetails.getAdjustmentAccountId()
								: 0);
				preparedStatement.setDouble(16,
						transactionDetails.getTotal() != null ? transactionDetails.getTotal() : 0.00);
				preparedStatement.setInt(17, invoiceId);
				preparedStatement.setDate(18, new Date(System.currentTimeMillis()));
				preparedStatement.setString(19, transactionDetails.getDiscountAccountName());
				preparedStatement.setString(20, transactionDetails.getDiscountAccountLevel());
				preparedStatement.setString(21, transactionDetails.getAdjustmentAccountName());
				preparedStatement.setString(22, transactionDetails.getAdjustmentAccountLevel());
				preparedStatement.setDouble(23,
						transactionDetails.getTotal() != null ? transactionDetails.getTotal() : 0.00);
				preparedStatement.executeUpdate();
				logger.info("Successfully inserted into table invoice_transaction_details");

			} catch (Exception e) {
				logger.info("Error in inserting to table invoice_transaction_details ", e);
				throw new ApplicationException(e);
			}finally {
				closeResources(null, preparedStatement, null);
			}
		}
	}

	// To delete invoice table entries
	public void deleteInvoiceEntries(Integer id, Boolean isInvoiceWithBills, String userId, String roleName)
			throws ApplicationException, SQLException {
		logger.info("To deleteInvoiceEntries:: ");
		Connection connection = null;
		if (id != null) {
			try {
				connection = getAccountsPayable();
				// To remove from invoice info table
				changeStatusForInvoiceTables(id, CommonConstants.STATUS_AS_DELETE, connection,
						BillsInvoiceConstants.MODIFY_INVOICE_GENERAL_INFO_STATUS);
				// To remove from invoice Product table
				changeStatusForInvoiceTables(id, CommonConstants.STATUS_AS_DELETE, connection,
						BillsInvoiceConstants.MODIFY_INVOICE_PRODUCT_STATUS_FOR_INVOICE_ID);
				// To remove from Attachments table
				String moduleType = (isInvoiceWithBills ? BillsInvoiceConstants.MODULE_TYPE_INVOICES_BILL
						: BillsInvoiceConstants.MODULE_TYPE_INVOICES_WITHOUT_BILL);
				attachmentsDao.changeStatusForAttachments(id, CommonConstants.STATUS_AS_DELETE, moduleType, userId,
						roleName);
				logger.info("Deleted successfully in all tables ");
			} catch (Exception e) {
				logger.info("Error in deleteInvoiceEntries:: ", e);
				throw new ApplicationException(e);
			} finally {
				closeResources(null, null, connection);
			}
		}

	}

	// To get all the invoices with bill for given org Id
	public List<InvoiceListVo> getAllInvoicesWithBillsForOrg(Integer orgId, Boolean isInvoiceWithBills)
			throws ApplicationException {
		logger.info("To getAllInvoicesWithBillsForOrg :: " + orgId + "isInvoiceWithBills::" + isInvoiceWithBills);
		List<InvoiceListVo> listVos = null;
		if (orgId != null && isInvoiceWithBills != null) {
			String dateFormat = getDefaultDateOfOrg(orgId);
			Connection con = null;
			PreparedStatement preparedStatement =null;
			ResultSet rs =null;
			try {
				con = getAccountsPayable();
				preparedStatement = con.prepareStatement(BillsInvoiceConstants.GET_ALL_INVOICES);
				preparedStatement.setInt(1, orgId);
				preparedStatement.setBoolean(2, isInvoiceWithBills);
				listVos = new ArrayList<InvoiceListVo>();
				rs = preparedStatement.executeQuery();
				while (rs.next()) {
					InvoiceListVo listVo = new InvoiceListVo();
					listVo.setInvoiceId(rs.getInt(1));
					listVo.setVendorDisplayName(rs.getString(2));
					String[] invoiceNum = rs.getString(3) != null ? rs.getString(3).split("/") : null;
					logger.info("invoiceNo is::" + invoiceNum +"rs.getString(3)::"+rs.getString(3));

					String invoiceNo = null;
					if(invoiceNum!=null) {
						if (invoiceNum.length > 1) {
							invoiceNo = 
									(invoiceNum[0]!=null && invoiceNum[0].trim().length()>0?invoiceNum[0] +"/"  :"")+ (invoiceNum[1]) + 
									(invoiceNum[2]!=null && invoiceNum[2].trim().length()>0?  "/"+ invoiceNum[2]:"");
						}else {
							invoiceNo = rs.getString(3);
						}
					}
					logger.info("invoiceNo is::" + invoiceNo);


					listVo.setInvoiceNo(invoiceNo);

					listVo.setPoReferenceNo(rs.getString(4));
					Date invoiceDate = rs.getDate(5);
					if (invoiceDate != null && dateFormat != null) {
						String invDate = DateConverter.getInstance().convertDateToGivenFormat(invoiceDate,
								dateFormat);
						listVo.setInvoiceDate(invDate);
					}
					Date dueDate = rs.getDate(6);
					if (dueDate != null && dateFormat != null) {
						String duedate = DateConverter.getInstance().convertDateToGivenFormat(dueDate, dateFormat);
						listVo.setDueDate(duedate);
					}
					logger.info("AllInvoicesWithBillsForOrg Status1:"+rs.getString(7));
					listVo.setStatus(rs.getString(7));
					logger.info("AllInvoicesWithBillsForOrg Status2:"+listVo.getStatus());
					listVo.setBalanceDue(rs.getDouble(8));
					listVos.add(listVo);
				}
			} catch (Exception e) {
				logger.info("Error in getAllInvoicesWithBillsForOrg ", e);
				throw new ApplicationException(e);
			}finally{
				closeResources(rs, preparedStatement, con);
			}
		}
		return listVos;

	}

	// To get the invoice form invoice_general_information table using id
	public void getInvoiceGeneralInfoById(InvoiceVo invoiceVo, Connection con) throws ApplicationException {
		logger.info("To get  in getInvoiceGeneralInfoById " + invoiceVo);
		if (invoiceVo != null) {
			PreparedStatement preparedStatement =null;
			ResultSet rs = null;
			try {
				preparedStatement = con.prepareStatement(BillsInvoiceConstants.GET_INVOICE_BY_ID); 
				preparedStatement.setInt(1, invoiceVo.getId());

				rs = preparedStatement.executeQuery();
				while (rs.next()) {
					InvoiceGeneralInfoVo generalInfoVo = new InvoiceGeneralInfoVo();
					generalInfoVo.setVendorId(rs.getInt(1));
					// Date invDate = rs.getDate(2);
					generalInfoVo.setLocationId(rs.getInt(3));
					generalInfoVo.setGstNumberId(rs.getString(4) != null ? rs.getString(4) : "NA");
					String[] invoiceNum = rs.getString(5).split("/");
					logger.info("1::"+invoiceNum[0]+"2::"+invoiceNum[1]);

					if (invoiceNum.length > 1) {
						generalInfoVo.setInvoiceNoPrefix(invoiceNum[0]!=null && invoiceNum[0].trim().length()>0?invoiceNum[0]:"");
						generalInfoVo.setInvoiceNo(invoiceNum[1]);
						generalInfoVo.setInvoiceNoSuffix(invoiceNum[2]!=null && invoiceNum[2].trim().length()>0?invoiceNum[2]:"");
					}
					generalInfoVo.setPoReferenceNo(rs.getString(6));
					generalInfoVo.setPaymentTermsId(rs.getInt(7));
					// Date dueDate = rs.getDate(8);
					generalInfoVo.setNotes(rs.getString(9));
					generalInfoVo.setTermsAndConditions(rs.getString(10));
					invoiceVo.setStatus(rs.getString(11));
					invoiceVo.setIsInvoiceWithBills(rs.getBoolean(12));
					invoiceVo.setUserId(String.valueOf(rs.getInt(13)));
					invoiceVo.setOrganizationId(rs.getInt(14));
					invoiceVo.setIsSuperAdmin(rs.getBoolean(15));
					generalInfoVo.setInvoiceDate(rs.getString(2));
					generalInfoVo.setDueDate(rs.getString(8));
					generalInfoVo.setCreate_ts(rs.getDate(16));
					generalInfoVo.setIsRegistered(rs.getBoolean(17));
					generalInfoVo.setIsQuick(rs.getBoolean(18));
					String updatedUser = rs.getString(19);
					boolean isVendorEditable = true;
					if (CommonConstants.ROLE_SUPER_ADMIN.equals(updatedUser)) {
						isVendorEditable = false;
					}
					if (CommonConstants.STATUS_AS_ACTIVE.equals(invoiceVo.getStatus())
							|| CommonConstants.STATUS_AS_INACTIVE.equals(invoiceVo.getStatus())
							|| CommonConstants.STATUS_AS_OPEN.equals(invoiceVo.getStatus())) {
						isVendorEditable = false;
					}
					invoiceVo.setIsVendorEditable(isVendorEditable);
					invoiceVo.setCreatedBy(rs.getString(20));
					generalInfoVo.setAksharData(rs.getString(21));
					String json = rs.getString(22);
					if(json!=null) {
						ObjectMapper mapper = new ObjectMapper();
						GeneralLedgerVo gldata = mapper.readValue(json, GeneralLedgerVo.class);
						logger.info("Json map "+gldata);
						invoiceVo.setGeneralLedgerData(gldata);
					}
					generalInfoVo.setVendorGstNo(rs.getString(23));
					invoiceVo.setGeneralInfo(generalInfoVo);
				}

				logger.info("Successfully fetched  getInvoiceGeneralInfoById " + invoiceVo);
			} catch (Exception e) {
				logger.info("Error in getInvoiceGeneralInfoById ", e);
				throw new ApplicationException(e);
			}finally{
				closeResources(rs, preparedStatement, null);
			}
		}
	}

	// To get the invoice form invoice_transaction_details table using Invoice id
	private InvoiceTrasactionVo getInvoiceTransactionDetailsByInvoiceId(Integer invoiceId, Connection con)
			throws ApplicationException {
		logger.info("To get  in getInvoiceTransactionDetailsByInvoiceId ");
		InvoiceTrasactionVo invoiceTrasactionVo = null;
		if (invoiceId != null) {
			PreparedStatement preparedStatement =null;
			ResultSet rs = null;
			try {
				preparedStatement = con.prepareStatement(BillsInvoiceConstants.GET_INVOICE_TRANSACTION_DETAILS);
				preparedStatement.setInt(1, invoiceId);
				rs = preparedStatement.executeQuery();
				while (rs.next()) {
					invoiceTrasactionVo = new InvoiceTrasactionVo();
					invoiceTrasactionVo.setSourceOfSupplyId(rs.getInt(1));
					invoiceTrasactionVo.setDestinationOfSupplyId(rs.getInt(2));
					invoiceTrasactionVo.setTaxApplicationMethodId(rs.getInt(3));
					invoiceTrasactionVo.setCurrencyid(rs.getInt(4));
					invoiceTrasactionVo.setExchangeRate(rs.getDouble(5));
					invoiceTrasactionVo.setSubTotal(rs.getDouble(6));
					invoiceTrasactionVo.setDiscountValue(rs.getDouble(7));
					invoiceTrasactionVo.setDiscountTypeId(rs.getInt(8));
					invoiceTrasactionVo.setDiscountAmount(rs.getDouble(9));
					invoiceTrasactionVo.setDiscountAccountId(rs.getInt(10));
					invoiceTrasactionVo.setIsApplyAfterTax(rs.getBoolean(11));
					invoiceTrasactionVo.setTdsId(rs.getInt(12));
					invoiceTrasactionVo.setTdsValue(rs.getDouble(13));
					invoiceTrasactionVo.setAdjustment(rs.getDouble(14));
					invoiceTrasactionVo.setAdjustmentAccountId(rs.getInt(15));
					invoiceTrasactionVo.setTotal(rs.getDouble(16));
					invoiceTrasactionVo.setDiscountAccountName(rs.getString(17));
					invoiceTrasactionVo.setDiscountAccountLevel(rs.getString(18));
					invoiceTrasactionVo.setAdjustmentAccountName(rs.getString(19));
					invoiceTrasactionVo.setAdjustmentAccountLevel(rs.getString(20));
				}
				logger.info("Successfully fetched  getInvoiceTransactionDetailsByInvoiceId ");
			} catch (Exception e) {
				logger.info("Error in getInvoiceTransactionDetailsByInvoiceId ", e);
				throw new ApplicationException(e);
			}finally{
				closeResources(rs, preparedStatement, null);
			}
		}
		return invoiceTrasactionVo;
	}

	private InvoiceTaxDetailsVo getTaxDetails(Integer invoiceId, Integer productId, Connection con,
			Boolean isInvoiceWithBills) throws ApplicationException {
		logger.info("To get  in getTaxDetails for prodId:" + productId);
		InvoiceTaxDetailsVo taxDetailsVo = null;
		PreparedStatement preparedStatement =null;
		ResultSet rs = null;
		try {
			preparedStatement = con.prepareStatement(BillsInvoiceConstants.GET_TAX_DETAILS); 
			preparedStatement.setInt(1, productId);
			String moduleType = (isInvoiceWithBills ? BillsInvoiceConstants.MODULE_TYPE_INVOICES_BILL
					: BillsInvoiceConstants.MODULE_TYPE_INVOICES_WITHOUT_BILL);
			logger.info("Module Type is :" + moduleType);
			preparedStatement.setString(2, moduleType);
			taxDetailsVo = new InvoiceTaxDetailsVo();
			List<InvoiceTaxDistributionVo> distributionVos = new ArrayList<InvoiceTaxDistributionVo>();
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				InvoiceTaxDistributionVo distributionVo = new InvoiceTaxDistributionVo();
				taxDetailsVo.setId(rs.getInt(1));
				taxDetailsVo.setComponentName(rs.getString(2));
				taxDetailsVo.setComponentItemId(rs.getInt(3));
				taxDetailsVo.setGroupName(rs.getString(4));
				distributionVo.setTaxName(rs.getString(5));
				distributionVo.setTaxRate(rs.getString(6));
				distributionVo.setTaxAmount(rs.getDouble(7));
				distributionVos.add(distributionVo);
			}
			taxDetailsVo.setTaxDistribution(distributionVos);
			logger.info("Successfully fetched  getTaxDetails " + taxDetailsVo);
		} catch (Exception e) {
			logger.info("Error in getTaxDetails ", e);
			throw new ApplicationException(e);
		}finally{
			closeResources(rs, preparedStatement, null);
		}
		return taxDetailsVo;
	}

	private List<InvoiceProductVo> getProductListForInvoices(Integer invoiceId, Connection con,
			Boolean isInvoiceWithBills) throws ApplicationException {
		logger.info("To get  in getProductListForInvoices ");
		List<InvoiceProductVo> productVos = new ArrayList<InvoiceProductVo>();
		PreparedStatement preparedStatement =null;
		ResultSet rs = null;
		try  {
			preparedStatement = con.prepareStatement(BillsInvoiceConstants.GET_INVOICE_PRODUCT_INFO);
			preparedStatement.setInt(1, invoiceId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				InvoiceProductVo productVo = new InvoiceProductVo();
				productVo.setId(rs.getInt(1));
				productVo.setProductId(rs.getInt(2));
				productVo.setProductDisplayname(rs.getString(3));
				productVo.setHsn(rs.getString(4));
				productVo.setProductAccountId(rs.getInt(5));
				productVo.setQuantity(rs.getString(6));
				productVo.setMeasureId(rs.getInt(7));
				productVo.setQuantityName(rs.getString(6) + " " + rs.getString(8));
				productVo.setUnitPrice(rs.getDouble(9));
				productVo.setTaxRateId(rs.getInt(10));
				productVo.setTaxDisplayValue(rs.getString(11) != null ? rs.getString(11) : "");
				productVo.setCustomerId(rs.getInt(12));
				productVo.setCustomerDisplayname(rs.getString(13));
				productVo.setIsBillable(rs.getBoolean(14));
				productVo.setInputTaxCredit(rs.getString(15));
				productVo.setAmount(rs.getDouble(16));
				productVo.setStatus(rs.getString(17));
				productVo.setProductAccountName(rs.getString(18));
				productVo.setProductAccountLevel(rs.getString(19));
				productVo.setDescription(rs.getString(20));
				productVo.setType(rs.getString(21));
				productVo.setTaxDetails(getTaxDetails(invoiceId, rs.getInt(1), con, isInvoiceWithBills));
				logger.info(productVo);
				productVos.add(productVo);
			}
			logger.info("Successfully fetched  getProductListForInvoices " + productVos);
		} catch (Exception e) {
			logger.info("Error in getProductListForInvoices ", e);
			throw new ApplicationException(e);
		}finally{
			closeResources(rs, preparedStatement, null);
		}
		return productVos;

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

	private void changeStatusForInvoiceTables(Integer id, String status, Connection con, String query)
			throws ApplicationException {
		logger.info("To Change the status with query :: " + query);
		if (query != null) {
			PreparedStatement preparedStatement =null;
			try {
				preparedStatement = con.prepareStatement(query);
				preparedStatement.setString(1, status);
				preparedStatement.setDate(2, new Date(System.currentTimeMillis()));
				preparedStatement.setInt(3, id);
				preparedStatement.executeUpdate();
				logger.info("Status chaneged successfully ");
			} catch (Exception e) {
				logger.info("Error in changeStatusForInvoiceTables ", e);
				throw new ApplicationException(e);
			}finally{
				closeResources(null, preparedStatement, null);
			}
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
			con = getAccountsPayable();
			String sql = BillsInvoiceConstants.ACTIVATE_DEACTIVATE_INVOICE;
			preparedStatement = con.prepareStatement(sql);
			preparedStatement.setString(1, status);
			preparedStatement.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
			preparedStatement.setString(3, userId);
			preparedStatement.setString(4, roleName);
			preparedStatement.setInt(5, id);
			preparedStatement.executeUpdate();
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}

	}

	// Update general info for invoice
	private InvoiceVo updateInvoiceGeneralInfo(InvoiceVo invoiceVo, Connection connection) throws ApplicationException {
		logger.info("To update into table invoice_general_information ");
		if (invoiceVo != null && invoiceVo.getId() != null && invoiceVo.getGeneralInfo() != null
				&& invoiceVo.getUserId() != null) {
			if (invoiceVo.getIsInvoiceWithBills() && invoiceVo.getGeneralInfo().getInvoiceNo() == null) {
				throw new ApplicationException("Invoice number is mandatory");
			}
			PreparedStatement preparedStatement = null;
			InvoiceGeneralInfoVo invoiceGenInfo=invoiceVo.getGeneralInfo();


			String invoiceNumber = null;
			String existinginvoiceNo = getInvoiceOrderNo(invoiceVo.getId(), invoiceVo.getOrganizationId(),
					connection);
			String invoiceNumToUpdate = new StringBuilder()
					.append(invoiceGenInfo.getInvoiceNoPrefix() != null && !invoiceGenInfo.getInvoiceNoPrefix().isEmpty()
					? invoiceGenInfo.getInvoiceNoPrefix()	: " ")
					.append("/").append(invoiceGenInfo.getInvoiceNo()).append("/")
					.append(invoiceGenInfo.getInvoiceNoSuffix() != null && !invoiceGenInfo.getInvoiceNoSuffix().isEmpty()
					? invoiceGenInfo.getInvoiceNoSuffix() : " ")
					.toString();
			logger.info("invoiceNumber::" + invoiceNumToUpdate);
			logger.info("existingPurchaseOrderNo::" + existinginvoiceNo);
			if (existinginvoiceNo.trim().equalsIgnoreCase(invoiceNumToUpdate.trim())) {
				invoiceNumber = invoiceNumToUpdate;
			} else {
				boolean isInvoiceExist = checkInvoiceNoExistForAnOrganization(invoiceVo.getOrganizationId(),invoiceGenInfo.getVendorId(),
						connection, invoiceNumToUpdate);
				if (isInvoiceExist) {
					throw new ApplicationException("Invoice number already exist for the organization");
				} else {
					logger.info("To add the new invoice in the Update purchse order api ");
					invoiceNumber = invoiceNumToUpdate;
				}
			}
			logger.info("The invoice number to be inserted is :: " + invoiceNumber);


			try {
				preparedStatement = connection.prepareStatement(BillsInvoiceConstants.MODIFY_INVOICE_GENERAL_INFO, Statement.RETURN_GENERATED_KEYS); 

				String dateFormat = getDefaultDateOfOrg(invoiceVo.getOrganizationId());
				preparedStatement.setInt(1,
						invoiceGenInfo.getVendorId() != null ? invoiceGenInfo.getVendorId()
								: 0);
				if (invoiceGenInfo.getInvoiceDate() != null) {
					Date invDate = DateConverter.getInstance()
							.convertStringToDate(invoiceGenInfo.getInvoiceDate(), dateFormat);
					if (invDate != null) {
						String date = DateConverter.getInstance().convertDateToGivenFormat(invDate, "yyyy-MM-dd");
						logger.info("converted dueDate::" + date);
						preparedStatement.setString(2, date);
					} else {
						logger.info("converted dueDate set as null");
						preparedStatement.setString(2, null);
					}
				} else {
					preparedStatement.setString(2, invoiceGenInfo.getInvoiceDate());
				}

				preparedStatement.setInt(3,
						invoiceGenInfo.getLocationId() != null ? invoiceGenInfo.getLocationId()
								: 0);
				preparedStatement.setString(4,
						invoiceGenInfo.getGstNumberId() != null
						? invoiceGenInfo.getGstNumberId()
								: null);
				preparedStatement.setString(5,
						invoiceNumber != null ? invoiceNumber
								: null);
				preparedStatement.setString(6,
						invoiceGenInfo.getPoReferenceNo() != null
						? invoiceGenInfo.getPoReferenceNo()
								: null);
				preparedStatement.setInt(7,
						invoiceGenInfo.getPaymentTermsId() != null
						? invoiceGenInfo.getPaymentTermsId()
								: 0);

				if (invoiceGenInfo.getDueDate() != null) {
					Date dueDate = DateConverter.getInstance()
							.convertStringToDate(invoiceGenInfo.getDueDate(), dateFormat);
					if (dueDate != null) {
						String date = DateConverter.getInstance().convertDateToGivenFormat(dueDate, "yyyy-MM-dd");
						logger.info("convertedDate::" + date);
						preparedStatement.setString(8, date);
					} else {
						logger.info("convertedDate set as null");
						preparedStatement.setString(8, null);
					}
				} else {
					preparedStatement.setString(8, invoiceGenInfo.getDueDate());
				}
				preparedStatement.setString(9, invoiceGenInfo.getNotes());
				preparedStatement.setString(10, invoiceGenInfo.getTermsAndConditions());
				preparedStatement.setString(11,
						invoiceVo.getStatus() != null ? invoiceVo.getStatus() : CommonConstants.STATUS_AS_ACTIVE);
				preparedStatement.setBoolean(12, invoiceVo.getIsInvoiceWithBills());
				preparedStatement.setDate(13, new Date(System.currentTimeMillis()));
				preparedStatement.setInt(14, Integer.valueOf(invoiceVo.getUserId()));
				preparedStatement.setInt(15, invoiceVo.getOrganizationId());
				preparedStatement.setBoolean(16, invoiceVo.getIsSuperAdmin());
				preparedStatement.setString(17, invoiceVo.getRoleName());
				preparedStatement.setBoolean(18, invoiceGenInfo.getIsRegistered());
				preparedStatement.setString(19, invoiceGenInfo.getAksharData());
				ObjectMapper mapper = new ObjectMapper();
				String glData = invoiceVo.getGeneralLedgerData() != null
						? mapper.writeValueAsString(invoiceVo.getGeneralLedgerData())
								: null;
						logger.info("glData>> " + glData);
						preparedStatement.setString(20, glData);
						preparedStatement.setString(21, invoiceGenInfo.getVendorGstNo());
						preparedStatement.setInt(22, invoiceVo.getId());
						preparedStatement.executeUpdate();
						logger.info("Successfully updated into table invoice_general_information ");
			} catch (Exception e) {
				logger.info("Error in updating  to table invoice_general_information ", e);
				throw new ApplicationException(e.getMessage());
			}finally{
				closeResources(null, preparedStatement, null);
			}
		}
		return invoiceVo;

	}

	private boolean updateProduct(InvoiceProductVo product, Connection connection, Integer invoiceId)
			throws ApplicationException {
		logger.info("To update into table invoice_product " + product);
		boolean isUpdatedSuccessfully = false;
		if (product != null && invoiceId != null) {
			PreparedStatement preparedStatement =null;
			try {
				preparedStatement = connection.prepareStatement(BillsInvoiceConstants.MODIFY_INVOICE_PRODUCT);
				preparedStatement.setInt(1, product.getProductId() != null ? product.getProductId() : 0);
				preparedStatement.setInt(2, product.getProductAccountId() != null ? product.getProductAccountId() : 0);
				preparedStatement.setString(3, product.getQuantity() != null ? product.getQuantity() : null);
				preparedStatement.setInt(4, product.getMeasureId() != null ? product.getMeasureId() : 0);
				preparedStatement.setDouble(5, product.getUnitPrice() != null ? product.getUnitPrice() : 0.00);
				preparedStatement.setInt(6, product.getTaxRateId() != null ? product.getTaxRateId() : 0);
				preparedStatement.setInt(7, product.getCustomerId() != null ? product.getCustomerId() : 0);
				preparedStatement.setBoolean(8,  product.getIsBillable() != null ? product.getIsBillable() : false);
				preparedStatement.setString(9, product.getInputTaxCredit() != null ? product.getInputTaxCredit() : null);
				preparedStatement.setDouble(10,  product.getAmount() != null ? product.getAmount() : 0.00);
				preparedStatement.setString(11,
						product.getStatus() != null ? product.getStatus() : CommonConstants.STATUS_AS_ACTIVE);
				preparedStatement.setDate(12, new Date(System.currentTimeMillis()));
				preparedStatement.setString(13, product.getProductAccountName());
				preparedStatement.setString(14, product.getProductAccountLevel());
				preparedStatement.setString(15, product.getDescription());
				preparedStatement.setInt(16, product.getId());
				preparedStatement.setInt(17, invoiceId);
				preparedStatement.executeUpdate();
				isUpdatedSuccessfully = true;
				logger.info("Successfully updated into table invoice_product with Id ");
			} catch (Exception e) {
				isUpdatedSuccessfully = false;
				logger.info("Error in updating to table invoice_product ", e);
				throw new ApplicationException(e.getMessage());
			}finally{
				closeResources(null, preparedStatement, null);
			}
		}
		return isUpdatedSuccessfully;
	}

	private void updateTaxDetailsForProduct(Integer productId, InvoiceProductVo product, Connection connection,
			Boolean isInvoiceWithBills) throws ApplicationException {
		logger.info("To update into table accounts_payable_tax_details " + product);
		/*
		 * To delete the existing entries and then adding the updated entries.. This is
		 * because during Update we do tax recalculation the Primary id would be missed
		 * out and so following this way of updating
		 */
		deleteTaxDetails(productId, connection, isInvoiceWithBills);
		createTaxDetailsForProduct(productId, product, connection, isInvoiceWithBills);

	}

	private void deleteTaxDetails(Integer productId, Connection connection, Boolean isInvoiceWithBills)
			throws ApplicationException {
		logger.info("To delete into table accounts_payable_tax_details ");
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = connection.prepareStatement(BillsInvoiceConstants.MODIFY_TAX_DETAILS_STATUS);
			preparedStatement.setDate(1, new Date(System.currentTimeMillis()));
			preparedStatement.setString(2, CommonConstants.STATUS_AS_DELETE);
			String moduleType = (isInvoiceWithBills ? BillsInvoiceConstants.MODULE_TYPE_INVOICES_BILL
					: BillsInvoiceConstants.MODULE_TYPE_INVOICES_WITHOUT_BILL);
			preparedStatement.setString(3, moduleType);
			preparedStatement.setInt(4, productId);
			preparedStatement.execute();
		} catch (Exception e) {
			logger.info("Error in deleteTaxDetails to table accounts_payable_tax_details ", e);
			throw new ApplicationException(e);
		}finally{
			closeResources(null, preparedStatement, null);
		}
	}

	private void updateTransactionDetails(InvoiceTrasactionVo transactionDetails, Connection connection,
			Integer invoiceId) throws ApplicationException {
		logger.info("To update into table invoice_transaction_details " + transactionDetails);
		if (invoiceId != null && transactionDetails != null) {
			// Validation if the Total matches the Quick Invoice Total Specified by vendor
			Double totalAmount = getTotalAmountOfQuickInvoiceByInvoiceId(invoiceId, connection);
			if (totalAmount != null && !totalAmount.equals(0.00) && transactionDetails.getTotal() != null
					&& !transactionDetails.getTotal().equals(0.00)) {
				if (!totalAmount.equals(transactionDetails.getTotal())) {
					throw new ApplicationException("Invoice Total does not match with the Vendor Submitted Value.");
				}
			}
			PreparedStatement preparedStatement = null;
			try {
				preparedStatement = connection.prepareStatement(BillsInvoiceConstants.MODIFY_INVOICE_TRANSACTION_DETAILS);
				preparedStatement.setInt(1,
						transactionDetails.getSourceOfSupplyId() != null ? transactionDetails.getSourceOfSupplyId()
								: 0);
				preparedStatement.setInt(2,
						transactionDetails.getDestinationOfSupplyId() != null
						? transactionDetails.getDestinationOfSupplyId()
								: 0);
				preparedStatement.setInt(3,
						transactionDetails.getTaxApplicationMethodId() != null
						? transactionDetails.getTaxApplicationMethodId()
								: 0);
				preparedStatement.setInt(4,
						transactionDetails.getCurrencyid() != null ? transactionDetails.getCurrencyid() : 0);
				preparedStatement.setDouble(5,
						transactionDetails.getExchangeRate() != null ? transactionDetails.getExchangeRate() : 0);
				preparedStatement.setDouble(6,
						transactionDetails.getSubTotal() != null ? transactionDetails.getSubTotal() : 0.00);
				preparedStatement.setDouble(7,
						transactionDetails.getDiscountValue() != null ? transactionDetails.getDiscountValue() : 0.00);
				preparedStatement.setInt(8,
						transactionDetails.getDiscountTypeId() != null ? transactionDetails.getDiscountTypeId() : 0);
				preparedStatement.setDouble(9,
						transactionDetails.getDiscountAmount() != null ? transactionDetails.getDiscountAmount() : 0.00);
				preparedStatement.setInt(10,
						transactionDetails.getDiscountAccountId() != null ? transactionDetails.getDiscountAccountId()
								: 0);
				preparedStatement.setBoolean(11, transactionDetails.getIsApplyAfterTax());
				preparedStatement.setInt(12, transactionDetails.getTdsId() != null ? transactionDetails.getTdsId() : 0);
				preparedStatement.setDouble(13,
						transactionDetails.getTdsValue() != null ? transactionDetails.getTdsValue() : 0.00);
				preparedStatement.setDouble(14,
						transactionDetails.getAdjustment() != null ? transactionDetails.getAdjustment() : 0.00);
				preparedStatement.setInt(15,
						transactionDetails.getAdjustmentAccountId() != null
						? transactionDetails.getAdjustmentAccountId()
								: 0);
				preparedStatement.setDouble(16,
						transactionDetails.getTotal() != null ? transactionDetails.getTotal() : 0.00);
				preparedStatement.setDate(17, new Date(System.currentTimeMillis()));
				preparedStatement.setString(18, transactionDetails.getDiscountAccountName());
				preparedStatement.setString(19, transactionDetails.getDiscountAccountLevel());
				preparedStatement.setString(20, transactionDetails.getAdjustmentAccountName());
				preparedStatement.setString(21, transactionDetails.getAdjustmentAccountLevel());
				preparedStatement.setInt(22, invoiceId);
				preparedStatement.executeUpdate();
				logger.info("Successfully updated into table invoice_transaction_details");

			} catch (Exception e) {
				logger.info("Error in updating to table invoice_transaction_details ", e);
				throw new ApplicationException(e);
			}finally{
				closeResources(null, preparedStatement, null);
			}
		}

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<InvoiceListVo> getInvoiceFilteredList(InvoiceFilterVo filterVo) throws ApplicationException {
		logger.info("To get the Filteredlist with filter values :::" + filterVo);
		Connection connection = null;
		List<InvoiceListVo> listVos = new ArrayList<InvoiceListVo>();
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			// String dateFormat = getDefaultDateOfOrg(filterVo.getOrgId());
			StringBuilder filterQuery = new StringBuilder(BillsInvoiceConstants.GET_ALL_INVOICES);
			List paramsList = new ArrayList<>();
			paramsList.add(filterVo.getOrgId());
			paramsList.add(filterVo.getIsInvoiceWithBills());
			/*if(!CommonConstants.ROLE_SUPER_ADMIN.equals(filterVo.getRoleName())) {
				filterQuery.append(" and igi.user_id = ? and igi.role_name in(?) ");
				paramsList.add(filterVo.getUserId());
				paramsList.add(filterVo.getRoleName());
			}*/
			if (filterVo.getStatus() != null) {
				filterQuery.append(" and igi.status = ? ");
				paramsList.add(filterVo.getStatus());
			}
			if (filterVo.getFromAmount() != null) {
				filterQuery.append(" and itx.total >= ?");
				paramsList.add(filterVo.getFromAmount());
			}
			if (filterVo.getToAmount() != null) {
				filterQuery.append(" and itx.total <= ?");
				paramsList.add(filterVo.getToAmount());
			}
			if (filterVo.getVendorId() != null) {
				filterQuery.append(" and igi.vendor_id = ?");
				paramsList.add(filterVo.getVendorId());
			}
			
			if(filterVo.getDueAmountGreaterZero() != null && filterVo.getDueAmountGreaterZero()) {
				filterQuery.append(" and itx.balance_due > ?");
				paramsList.add(0.0);
			}
			
			if(filterVo.getCurrencyId() != null) {
				filterQuery.append(" and itx.currency_organization_id = ?");
				paramsList.add(filterVo.getCurrencyId());
			}

			if (filterVo.getStartDate() != null) {
				if (filterVo.getEndDate() == null) {
					java.sql.Date date = new java.sql.Date(System.currentTimeMillis());
					filterVo.setEndDate(DateConverter.getInstance().getDatePickerDateFormat(date));
					logger.info("End date is null and so setting the sysdate::" + filterVo.getEndDate());
				}

				if (filterVo.getStartDate() != null && filterVo.getEndDate() != null) {
					filterQuery.append(" and Cast(igi.invoice_date as datetime)  between ? and ? ");
					String fromdate = DateConverter.getInstance()
							.correctDatePickerDateToString(filterVo.getStartDate());
					logger.info("convertedFromDate::" + fromdate);
					paramsList.add(fromdate);
					String todate = DateConverter.getInstance().correctDatePickerDateToString(filterVo.getEndDate());
					logger.info("convertedEndDate::" + todate);
					paramsList.add(todate);
				}
			}
			SettingsModuleVo allInvoiceModuleVo = null;
			SettingsModuleVo vendorCreatedModuleVo = null;
			Boolean viewInvoices = true;
			if (filterVo.getRoleName() != null
					&& CommonConstants.ROLE_VENDOR.equalsIgnoreCase(filterVo.getRoleName())) {
				if (filterVo.getVendorId() != null && !filterVo.getVendorId().equals(0)) {
					VamVendorSettingsVo vendorAccessVo = vendorAccessMgmtDao
							.getVendorSettingsById(filterVo.getVendorId());
					if (vendorAccessVo != null && vendorAccessVo.getSettingsData() != null) {
						logger.info("Settings data of vendor::" + vendorAccessVo.getSettingsData());
						if (vendorAccessVo.getSettingsData().getModules() != null
								&& vendorAccessVo.getSettingsData().getModules().size() > 0) {
							for (SettingsModuleVo module : vendorAccessVo.getSettingsData().getModules()) {
								if (module.getBaseId().equals(5)) {
									allInvoiceModuleVo = module;
								}
								if (module.getBaseId().equals(4)) {
									vendorCreatedModuleVo = module;
								}
							}
							if (allInvoiceModuleVo != null && vendorCreatedModuleVo != null
									&& !allInvoiceModuleVo.getIsActive() && vendorCreatedModuleVo.getIsActive()) {
								filterQuery.append(" and igi.role_name= ?");
								paramsList.add(CommonConstants.ROLE_VENDOR);
							}
							if (allInvoiceModuleVo != null && allInvoiceModuleVo.getIsActive()) {
								filterQuery.append(" and igi.role_name= ?");
								paramsList.add(CommonConstants.ROLE_VENDOR);
								listVos = getExceptVendorCreatedInvoices(filterVo.getOrgId(),
										filterVo.getIsInvoiceWithBills(), filterVo.getVendorId());
							}
							if (allInvoiceModuleVo != null && vendorCreatedModuleVo != null
									&& !allInvoiceModuleVo.getIsActive() && !vendorCreatedModuleVo.getIsActive()) {
								viewInvoices = false;
							}
						}
					}
				}
			}
			filterQuery.append("  order by igi.id desc");
			logger.info(filterQuery.toString());
			logger.info(paramsList);
			if (viewInvoices) {
				connection = getAccountsPayable();
				preparedStatement = connection.prepareStatement(filterQuery.toString());
				int counter = 1;
				for (int i = 0; i < paramsList.size(); i++) {
					logger.info(counter);
					preparedStatement.setObject(counter, paramsList.get(i));
					counter++;
				}
				rs = preparedStatement.executeQuery();
				while (rs.next()) {
					InvoiceListVo listVo = new InvoiceListVo();
					Integer invoiceId=rs.getInt(1);
					listVo.setInvoiceId(invoiceId);
					listVo.setVendorDisplayName(rs.getString(2));
					String[] invoiceNum = rs.getString(3) != null ? rs.getString(3).split("/") : null;
					logger.info("invoiceNo is::" + invoiceNum +"rs.getString(3)::"+rs.getString(3));

					String invoiceNo = null;
					if(invoiceNum!=null) {
						if (invoiceNum.length > 1) {
							invoiceNo = 
									(invoiceNum[0]!=null && invoiceNum[0].trim().length()>0?invoiceNum[0] +"/"  :"")+ (invoiceNum[1]) + 
									(invoiceNum[2]!=null && invoiceNum[2].trim().length()>0?  "/"+ invoiceNum[2]:"");
						}else {
							invoiceNo = rs.getString(3);
						}
					}
					logger.info("invoiceNo is::" + invoiceNo);


					listVo.setInvoiceNo(invoiceNo);
					listVo.setPoReferenceNo(rs.getString(4));
					/*
					 * Date invoiceDate = rs.getDate(5); if(invoiceDate!=null && dateFormat!=null) {
					 * String invDate =
					 * DateConverter.getInstance().convertDateToGivenFormat(invoiceDate,
					 * dateFormat); listVo.setInvoiceDate(invDate); } Date dueDate = rs.getDate(6);
					 * if(dueDate!=null && dateFormat!=null) { String duedate =
					 * DateConverter.getInstance().convertDateToGivenFormat(dueDate, dateFormat);
					 * listVo.setDueDate(duedate); }
					 */
					listVo.setInvoiceDate(rs.getString(5));
					listVo.setDueDate(rs.getString(6));
					String status = rs.getString(7);
					String displayStatus = null;
					switch (status) {
					case CommonConstants.STATUS_AS_ACTIVE:
						displayStatus = CommonConstants.DISPLAY_STATUS_AS_ACTIVE;
						break;
					case CommonConstants.STATUS_AS_DRAFT:
						displayStatus = CommonConstants.DISPLAY_STATUS_AS_DRAFT;
						break;
					case CommonConstants.STATUS_AS_INACTIVE:
						displayStatus = CommonConstants.DISPLAY_STATUS_AS_INACTIVE;
						break;
					case CommonConstants.STATUS_AS_OPEN:
						displayStatus = CommonConstants.DISPLAY_STATUS_AS_OPEN;
						break;
					case CommonConstants.STATUS_AS_OVERDUE:
						displayStatus = CommonConstants.DISPLAY_STATUS_AS_OVERDUE;
						break;
					case CommonConstants.STATUS_AS_PAID:
						displayStatus = CommonConstants.DISPLAY_STATUS_AS_PAID;
						break;
					case CommonConstants.STATUS_AS_UNPAID:
						displayStatus = CommonConstants.DISPLAY_STATUS_AS_UNPAID;
						break;	
					case CommonConstants.STATUS_AS_VOID:
						displayStatus = CommonConstants.DISPLAY_STATUS_AS_VOID;
						break;
					case CommonConstants.STATUS_AS_PARTIALLY_PAID:
						displayStatus = CommonConstants.DISPLAY_STATUS_AS_PARTIALLY_PAID;
						break;
					}
					listVo.setStatus(displayStatus);

					Double total = rs.getDouble(8);
					listVo.setAmount(total != null && !total.equals(0.00) ? total : rs.getDouble(11));
					listVo.setOrgName(rs.getString(9));
					listVo.setBalanceDue(rs.getDouble(10));
					String updatedUser = rs.getString(12);
					boolean isVendorEditable = true;
					if (CommonConstants.ROLE_SUPER_ADMIN.equals(updatedUser)) {
						isVendorEditable = false;
					}
					if (CommonConstants.DISPLAY_STATUS_AS_ACTIVE.equals(listVo.getStatus())
							|| CommonConstants.DISPLAY_STATUS_AS_INACTIVE.equals(listVo.getStatus())
							|| CommonConstants.DISPLAY_STATUS_AS_OPEN.equals(listVo.getStatus())) {
						isVendorEditable = false;
					}
					listVo.setIsVendorEditable(isVendorEditable);
					listVo.setIsQuick(rs.getBoolean(13));
					listVo.setRoleName(rs.getString(14));
					listVo.setPendingApprovalStatus(rs.getString(15));
					listVo.setVendorId(rs.getInt(16));
					Integer currencyId=rs.getInt(17);
					if(currencyId!=null) {
						listVo.setCurrencyId(currencyId);
						CurrencyVo currencyVo=currencyDao.getCurrency(currencyId);
						listVo.setCurrencySymbol(currencyVo!=null && currencyVo.getSymbol()!=null?currencyVo.getSymbol():"");	
					}
					List<CommonTransactionVo> trasactions=getInvoiceTransactions(invoiceId);
					listVo.setTrasactions(trasactions);
					listVo.setIsVoidable(trasactions!=null &&trasactions.isEmpty());
					listVo.setIsPaymentInitiated(checkYesBankPaymentInitiated(filterVo.getOrgId(), rs.getInt(16), YesBankConstants.KEYCONTACT_VENDOR, invoiceId, YesBankConstants.MODULE_INVOICE_PAYMENT));
					listVos.add(listVo);
					
				}

			}
			logger.info("listVos size::" + listVos.size());
			logger.info("Retrieved Invoices ");

		} catch (Exception e) {
			logger.info("Error in getAllFilteredInvoices:: ", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, connection);
		}
		return listVos;

	}

	public Boolean checkYesBankPaymentInitiated(int orgId,int keyContactId,String keyContactType,int moduleId,String moduleType)throws ApplicationException
	{
		Connection con = null;
		PreparedStatement preparedStatement = null;
		Boolean isPaymentInitiated =false;
		ResultSet rs = null;
		try {
			con = getBankingConnection();
			preparedStatement = con.prepareStatement(YesBankConstants.CHECK_PAYMENT_INITIATED);
			preparedStatement.setInt(1, orgId);
			preparedStatement.setInt(2, keyContactId);
			preparedStatement.setString(3, keyContactType);
			preparedStatement.setString(4, moduleType);
			preparedStatement.setInt(5,moduleId);
			rs=preparedStatement.executeQuery();
			while (rs.next()) {
				
				isPaymentInitiated=true;
			}
		} catch (Exception e) {
			logger.info("::: Exception in  checkPaymentInitiated Details ::::" + e);
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return isPaymentInitiated;
	}
	private List<InvoiceListVo> getExceptVendorCreatedInvoices(Integer orgId, Boolean isBillsInvoice, Integer vendorId)
			throws ApplicationException {
		StringBuilder query = new StringBuilder(BillsInvoiceConstants.GET_ALL_INVOICES);
		query.append("  and igi.vendor_id = ?  and igi.role_name not in ( ? ) and igi.status not in(?) ");
		List<InvoiceListVo> listVos = new ArrayList<InvoiceListVo>();
		Connection con =null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			con = getAccountsPayable();
			preparedStatement = con.prepareStatement(query.toString());
			preparedStatement.setInt(1, orgId);
			preparedStatement.setBoolean(2, isBillsInvoice);
			preparedStatement.setInt(3, vendorId);
			preparedStatement.setString(4, CommonConstants.ROLE_VENDOR);
			preparedStatement.setString(5, CommonConstants.STATUS_AS_DRAFT);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				InvoiceListVo listVo = new InvoiceListVo();
				listVo.setInvoiceId(rs.getInt(1));
				listVo.setVendorDisplayName(rs.getString(2));
				String[] invoiceNum = rs.getString(3) != null ? rs.getString(3).split("/") : null;
				logger.info("invoiceNo is::" + invoiceNum +"rs.getString(3)::"+rs.getString(3));

				String invoiceNo = null;
				if(invoiceNum!=null) {
					if (invoiceNum.length > 1) {
						invoiceNo = 
								(invoiceNum[0]!=null && invoiceNum[0].trim().length()>0?invoiceNum[0] +"/"  :"")+ (invoiceNum[1]) + 
								(invoiceNum[2]!=null && invoiceNum[2].trim().length()>0?  "/"+ invoiceNum[2]:"");
					}else {
						invoiceNo = rs.getString(3);
					}
				}
				logger.info("invoiceNo is::" + invoiceNo);


				listVo.setInvoiceNo(invoiceNo);

				listVo.setPoReferenceNo(rs.getString(4));

				listVo.setInvoiceDate(rs.getString(5));
				listVo.setDueDate(rs.getString(6));
				String status = rs.getString(7);
				String displayStatus = null;
				switch (status) {
				case CommonConstants.STATUS_AS_ACTIVE:
					displayStatus = CommonConstants.DISPLAY_STATUS_AS_ACTIVE;
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
				}
				listVo.setStatus(displayStatus);
				Double total = rs.getDouble(8);
				listVo.setAmount(total != null && !total.equals(0.00) ? total : rs.getDouble(11));
				listVo.setOrgName(rs.getString(9));
				listVo.setBalanceDue(rs.getDouble(10));
				String updatedUser = rs.getString(12);
				boolean isVendorEditable = true;
				if (CommonConstants.ROLE_SUPER_ADMIN.equals(updatedUser)) {
					isVendorEditable = false;
				}
				if (CommonConstants.DISPLAY_STATUS_AS_ACTIVE.equals(listVo.getStatus())
						|| CommonConstants.DISPLAY_STATUS_AS_INACTIVE.equals(listVo.getStatus())
						|| CommonConstants.DISPLAY_STATUS_AS_OPEN.equals(listVo.getStatus())) {
					isVendorEditable = false;
				}
				listVo.setIsVendorEditable(isVendorEditable);
				listVo.setIsQuick(rs.getBoolean(13));
				listVo.setRoleName(rs.getString(14));
				listVos.add(listVo);

			}
			logger.info("except vendor created invoice size ::" + listVos.size());
		} catch (Exception e) {
			logger.info("Error in getExceptVendorCreatedInvoices:: ", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return listVos;
	}

	public Map<String, Integer> getMaxAmountForOrg(Integer orgId, Boolean isInvoiceWIthBills)
			throws ApplicationException {
		logger.info("To getMinMaxValuesOfOrganisation");
		Connection connection = null;
		PreparedStatement preparedStatement =null;
		Map<String, Integer> maxAmountMap = null;
		ResultSet rs = null;
		if (orgId != null) {
			try {
				connection = getAccountsPayable();
				preparedStatement = connection.prepareStatement(BillsInvoiceConstants.GET_MAX_AMOUNT_FOR_ORG);
				preparedStatement.setInt(1, orgId);
				preparedStatement.setBoolean(2, isInvoiceWIthBills);
				rs = preparedStatement.executeQuery();
				while (rs.next()) {
					maxAmountMap = new HashMap<String, Integer>();
					Integer maxAmt = rs.getInt(1);
					if (maxAmt != null) {
						maxAmountMap.put("maxAmount", maxAmt);
					}
				}

			} catch (ApplicationException | SQLException e) {
				logger.info(e);
				throw new ApplicationException(e);
			} finally {
				closeResources(rs, preparedStatement, connection);
			}

		}
		return maxAmountMap;

	}

	// TO check of Invoice already Exist For Organization
	private boolean checkInvoiceNoExistForAnOrganization(Integer orgId,Integer vendorId, Connection con, String invoiceNo)
			throws ApplicationException {
		Boolean isInvoiceNoExist = false;
		PreparedStatement preparedStatement =null;
		ResultSet rs = null;
		try {
			preparedStatement = con.prepareStatement(BillsInvoiceConstants.CHECK_INVOICE_EXIST_FOR_ORG);
			preparedStatement.setInt(1, orgId);
			preparedStatement.setInt(2, vendorId);
			preparedStatement.setString(3, invoiceNo);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				isInvoiceNoExist = true;
			}
		} catch (Exception e) {
			logger.info("Error in checkInvoiceNoExistForAnOrganization  ", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, null);
		}
		return isInvoiceNoExist;
	}

	public String getInvoiceOrderNo(Integer id, Integer orgId, Connection con) throws ApplicationException {
		String invoiceNo = null;
		PreparedStatement preparedStatement =null;
		ResultSet rs =null;
		try{
			preparedStatement = con.prepareStatement(BillsInvoiceConstants.GET_INVOICE_FOR_INVOICE_ID);
			preparedStatement.setInt(1, orgId);
			preparedStatement.setInt(2, id);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				invoiceNo = rs.getString(1);
			}
		} catch (Exception e) {
			logger.info("Error in getInvoiceOrderNo  ", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, null);
		}

		return invoiceNo;
	}

	// TO check of Invoice already Exist For Organization
	public Double getTotalAmountOfQuickInvoiceByInvoiceId(Integer invoiceId, Connection con)
			throws ApplicationException {
		Double totalAmount = null;
		PreparedStatement preparedStatement =null;
		ResultSet rs =null;
		try {
			preparedStatement = con.prepareStatement(BillsInvoiceConstants.GET_QUICKINVOICE_TXN_DETAILS_FOR_INVOICE_ID);
			preparedStatement.setInt(1, invoiceId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				totalAmount = rs.getDouble(1);
			}
		} catch (Exception e) {
			logger.info("Error in getTotalAmountOfQuickInvoiceByInvoiceId  ", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, null);
		}
		return totalAmount;
	}
	
	public void getTotalAmountOfQuickInvoiceByInvoiceId(List<ApplyFundVo> applyFundVos, int vendorCreditId)
			throws ApplicationException, SQLException {	
		Connection con = null;
		con = getAccountsPayable();
		con.setAutoCommit(false);
		PreparedStatement preparedStatement = null;
		
		ResultSet rs = null;
		Map<Integer, Double> invoiceIdAndDueBalance = new HashMap<>();
		try {

			preparedStatement = con
					.prepareStatement(String.format(BillsInvoiceConstants.GET_INVOICE_TXN_DETAILS_FOR_INVOICE_IDS,
							applyFundVos.stream().map(ApplyFundVo::getInvoiceId).map(Object::toString)
									.collect(Collectors.joining(", "))));
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				invoiceIdAndDueBalance.put(rs.getInt(1), rs.getDouble(2));
			}
		} catch (Exception e) {
			logger.info("Error in getTotalAmountOfQuickInvoiceByInvoiceId  ", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, null);
		}

		try (PreparedStatement updateInvoiceDueBalance = con
				.prepareStatement(BillsInvoiceConstants.UPDATE_INVOICE_DUE_BALANCE_FOR_INVOICE_IDS)) {
			if (invoiceIdAndDueBalance.size() == applyFundVos.size()) {
				for (ApplyFundVo applyFundVo : applyFundVos) {
					Double amount = invoiceIdAndDueBalance.get(applyFundVo.getInvoiceId())
							- applyFundVo.getAppliedAmount();
					if (amount > 0.0) {
						updateInvoiceDueBalance.setDouble(1, amount);
						updateInvoiceDueBalance.setInt(2, applyFundVo.getInvoiceId());
						updateInvoiceDueBalance.executeUpdate();
					} else {
						throw new ApplicationException("Error while updating invoice dueBalance");
					}
				}
//				updateInvoiceDueBalance.executeBatch();				
			}
		}
		double totalAppliedAmount = applyFundVos.stream().mapToDouble(ApplyFundVo::getAppliedAmount).sum();
		vendorCreditDao.updateVendorAdvanceCreditById(vendorCreditId, con, totalAppliedAmount);
		con.commit();
		con.close();
		
	}

	public QuickInvoiceVo createQuickInvoice(QuickInvoiceVo invoiceVo) throws Exception {
		logger.info("To create a new Invoice in createQuickInvoice:::" + invoiceVo);
		Connection connection = null;
		if (invoiceVo != null) {
			try {
				connection = getAccountsPayable();
				connection.setAutoCommit(false);
				insertIntoGeneralInfoForQuickInvoice(invoiceVo, connection);
				if (invoiceVo.getId() != null) {
					createTransactionDetailsForQuicInvoice(invoiceVo, connection);
					if (invoiceVo.getAttachments() != null) {
						attachmentsDao.createAttachments(invoiceVo.getOrganizationId(), invoiceVo.getUserId(),
								invoiceVo.getAttachments(), AttachmentsConstants.MODULE_TYPE_INVOICES_BILL,
								invoiceVo.getId(), invoiceVo.getRoleName());
					}
				}
				connection.commit();
				logger.info("Succecssfully created QuickInvoice in BillsInvoiceDao");
			} catch (Exception e) {
				logger.info("Error in createInvoice:: ", e);
				connection.rollback();
				logger.info("Before throw in dao :: " + e.getMessage());
				throw new ApplicationException(e.getMessage());
			} finally {
				closeResources(null,null, connection);
			}
		}
		return invoiceVo;
	}

	private QuickInvoiceVo insertIntoGeneralInfoForQuickInvoice(QuickInvoiceVo invoiceVo, Connection connection)
			throws ApplicationException {
		logger.info("To insert into table invoice_general_information ");
		if (invoiceVo != null && invoiceVo.getOrganizationId() != null) {
			if (invoiceVo.getIsInvoiceWithBills() != null && invoiceVo.getIsInvoiceWithBills()
					&& invoiceVo.getInvoiceNumber() == null) {
				throw new ApplicationException("Invoice number is mandatory");
			}
			PreparedStatement preparedStatement =null;
			ResultSet rs =null;
			try {
				preparedStatement = connection.prepareStatement(	BillsInvoiceConstants.INSERT_INTO_GENERAL_INFO_FOR_QUICKINVOICE, Statement.RETURN_GENERATED_KEYS); 
				boolean isInvoiceExist = checkInvoiceNoExistForAnOrganization(invoiceVo.getOrganizationId(),invoiceVo.getVendorId(), connection,
						invoiceVo.getInvoiceNumber());
				if (isInvoiceExist) {
					throw new ApplicationException("Invoice number already exist for the organization");
				}
				String dateFormat = getDefaultDateOfOrg(invoiceVo.getOrganizationId());
				preparedStatement.setInt(1, invoiceVo.getVendorId() != null ? invoiceVo.getVendorId() : 0);
				preparedStatement.setString(2,
						invoiceVo.getInvoiceNumber() != null ? invoiceVo.getInvoiceNumber() : null);

				if (invoiceVo.getInvoiceDate() != null && !"".equals(invoiceVo.getInvoiceDate())) {
					Date invDate = DateConverter.getInstance().convertStringToDate(invoiceVo.getInvoiceDate(),
							dateFormat);
					if (invDate != null) {
						String date = DateConverter.getInstance().convertDateToGivenFormat(invDate, "yyyy-MM-dd");
						logger.info("converted dueDate::" + date);
						preparedStatement.setString(3, date);
					} else {
						logger.info("converted dueDate set as null");
						preparedStatement.setString(3, null);
					}
				} else {
					preparedStatement.setString(3, invoiceVo.getInvoiceDate());
				}

				if (invoiceVo.getDueDate() != null && !"".equals(invoiceVo.getDueDate())) {
					Date dueDate = DateConverter.getInstance().convertStringToDate(invoiceVo.getDueDate(), dateFormat);
					if (dueDate != null) {
						String date = DateConverter.getInstance().convertDateToGivenFormat(dueDate, "yyyy-MM-dd");
						logger.info("convertedDate::" + date);
						preparedStatement.setString(4, date);
					} else {
						logger.info("convertedDate set as null");
						preparedStatement.setString(4, null);
					}
				} else {
					preparedStatement.setString(4, invoiceVo.getDueDate());
				}
				preparedStatement.setBoolean(5,
						invoiceVo.getIsInvoiceWithBills() != null ? invoiceVo.getIsInvoiceWithBills() : true);
				preparedStatement.setString(6, invoiceVo.getRemarks() != null ? invoiceVo.getRemarks() : null);
				preparedStatement.setString(7, CommonConstants.STATUS_AS_DRAFT);
				preparedStatement.setDate(8, new Date(System.currentTimeMillis()));
				preparedStatement.setInt(9, Integer.valueOf(invoiceVo.getUserId()));
				preparedStatement.setInt(10, invoiceVo.getOrganizationId());
				preparedStatement.setBoolean(11, invoiceVo.getIsSuperAdmin());
				preparedStatement.setString(12, invoiceVo.getRoleName());
				preparedStatement.setBoolean(13, true);
				preparedStatement.setString(14, invoiceVo.getAksharData());
				int rowAffected = preparedStatement.executeUpdate();
				if (rowAffected == 1) {
					rs = preparedStatement.getGeneratedKeys();
					while (rs.next()) {
						invoiceVo.setId(rs.getInt(1));
					}
				}
				logger.info("Successfully inserted into table invoice_general_information ");
			} catch (Exception e) {
				logger.info("Error in inserting to table invoice_general_information ", e);
				throw new ApplicationException(e.getMessage());
			} finally {
				closeResources(rs,preparedStatement, null);
			}
		}
		return invoiceVo;
	}

	private void createTransactionDetailsForQuicInvoice(QuickInvoiceVo invoiceVo, Connection connection)
			throws ApplicationException {
		logger.info("To insert into table invoice_transaction_details " + invoiceVo);
		if (invoiceVo.getId() != null) {
			PreparedStatement preparedStatement =null;
			try {
				preparedStatement = connection.prepareStatement(BillsInvoiceConstants.INSERT_INTO_TRANSACTION_FOR_QUICKINVOICE);
				preparedStatement.setInt(1, invoiceVo.getCurrencyId() != null ? invoiceVo.getCurrencyId() : 0);
				preparedStatement.setDouble(2, invoiceVo.getBaseAmount() != null ? invoiceVo.getBaseAmount() : 0.00);
				preparedStatement.setDouble(3, invoiceVo.getGstAmount() != null ? invoiceVo.getGstAmount() : 0.00);
				preparedStatement.setDouble(4, invoiceVo.getTotalAmount() != null ? invoiceVo.getTotalAmount() : 0.00);
				preparedStatement.setBoolean(5, invoiceVo.getIsRcmApplicable());
				preparedStatement.setInt(6, invoiceVo.getId());
				preparedStatement.setDate(7, new Date(System.currentTimeMillis()));
				preparedStatement.executeUpdate();
				logger.info("Successfully inserted into table invoice_transaction_details");
			} catch (Exception e) {
				logger.info("Error in inserting to table invoice_transaction_details ", e);
				throw new ApplicationException(e);
			} finally {
				closeResources(null,preparedStatement, null);
			}
		}
	}

	public QuickInvoiceVo getQuickInvoiceById(Integer invoiceId) throws ApplicationException {
		logger.info("To get Quick Invoice in BillsInvoiceDao for:::" + invoiceId);
		QuickInvoiceVo invoiceVo = null;
		if (invoiceId != null) {
			Connection con = null;
			try {
				con = getAccountsPayable();
				invoiceVo = new QuickInvoiceVo();
				invoiceVo.setId(invoiceId);
				getQuickInvoiceGeneralInfoById(invoiceVo, con);
				getQuickInvoiceTransactionDetailsByInvoiceId(invoiceVo, con);
				List<UploadFileVo> uploadFileVos = new ArrayList<UploadFileVo>();
				String moduleType = AttachmentsConstants.MODULE_TYPE_INVOICES_BILL;
				for (AttachmentsVo attachments : attachmentsDao.getAttachmentDetails(invoiceId, moduleType)) {
					UploadFileVo uploadFileVo = new UploadFileVo();
					uploadFileVo.setId(attachments.getId());
					uploadFileVo.setName(attachments.getFileName());
					uploadFileVo.setSize(attachments.getSize());
					uploadFileVo.setIsAkshar(attachments.getIsAkshar());
					uploadFileVos.add(uploadFileVo);
				}
				invoiceVo.setAttachments(uploadFileVos);
				logger.info("Succecssfully fetched quick invoice in BillsInvoiceDao + " + invoiceVo);
			} catch (Exception e) {
				logger.info("Error in getQuickInvoiceById:: ", e);
				throw new ApplicationException(e);
			} finally {
				closeResources(null,null, con);
			}
		}
		return invoiceVo;

	}

	private void getQuickInvoiceGeneralInfoById(QuickInvoiceVo invoiceVo, Connection con) throws ApplicationException {
		logger.info("To get  in getQuickInvoiceTransactionDetailsByInvoiceId " + invoiceVo);
		if (invoiceVo != null) {
			PreparedStatement preparedStatement = null;
			ResultSet rs = null;
			try {
				preparedStatement = con.prepareStatement(BillsInvoiceConstants.GET_GENERAL_INFO_FOR_QUICKINVOICE);
				preparedStatement.setInt(1, invoiceVo.getId());
				rs = preparedStatement.executeQuery();
				while (rs.next()) {
					invoiceVo.setVendorId(rs.getInt(1));
					// Date invDate = rs.getDate(2);
					invoiceVo.setInvoiceNumber(rs.getString(2));
					invoiceVo.setInvoiceDate(rs.getString(3));
					invoiceVo.setDueDate(rs.getString(4));
					invoiceVo.setIsInvoiceWithBills(rs.getBoolean(5));
					invoiceVo.setRemarks(rs.getString(6));
					invoiceVo.setUserId(String.valueOf(rs.getInt(8)));
					invoiceVo.setOrganizationId(rs.getInt(9));
					invoiceVo.setIsSuperAdmin(rs.getBoolean(10));
					invoiceVo.setRoleName(rs.getString(11));
					invoiceVo.setIsQuick(rs.getBoolean(12));
					String updatedUser = rs.getString(13);
					String status = rs.getString(14);
					boolean isVendorEditable = true;
					if (CommonConstants.ROLE_SUPER_ADMIN.equals(updatedUser)) {
						isVendorEditable = false;
					}
					if (CommonConstants.STATUS_AS_ACTIVE.equals(status)
							|| CommonConstants.STATUS_AS_INACTIVE.equals(status)
							|| CommonConstants.STATUS_AS_OPEN.equals(status)) {
						isVendorEditable = false;
					}
					invoiceVo.setIsVendorEditable(isVendorEditable);
					invoiceVo.setAksharData(rs.getString(15));						
				}
				logger.info("Successfully fetched  getInvoiceGeneralInfoById " + invoiceVo);
			} catch (Exception e) {
				logger.info("Error in getInvoiceGeneralInfoById ", e);
				throw new ApplicationException(e);
			} finally {
				closeResources(rs,preparedStatement, null);
			}
		}
	}

	private void getQuickInvoiceTransactionDetailsByInvoiceId(QuickInvoiceVo invoiceVo, Connection con)
			throws ApplicationException {
		logger.info("To get  in getQuickInvoiceGeneralInfoById ");
		if (invoiceVo != null && invoiceVo.getId() != null) {
			PreparedStatement preparedStatement = null;
			ResultSet rs = null;
			try {
				preparedStatement = con.prepareStatement(BillsInvoiceConstants.GET_TRANSACTION_FOR_QUICKINVOICE); 
				preparedStatement.setInt(1, invoiceVo.getId());
				rs = preparedStatement.executeQuery();
				while (rs.next()) {
					invoiceVo.setCurrencyId(rs.getInt(1));
					invoiceVo.setBaseAmount(rs.getDouble(2));
					invoiceVo.setGstAmount(rs.getDouble(3));
					invoiceVo.setTotalAmount(rs.getDouble(4));
					invoiceVo.setIsRcmApplicable(rs.getBoolean(5));
				}
				logger.info("Successfully fetched  getInvoiceTransactionDetailsByInvoiceId ");
			} catch (Exception e) {
				logger.info("Error in getInvoiceTransactionDetailsByInvoiceId ", e);
				throw new ApplicationException(e);
			} finally {
				closeResources(rs,preparedStatement, null);
			}
		}

	}

	public QuickInvoiceVo updateQuickInvoice(QuickInvoiceVo invoiceVo) throws ApplicationException, SQLException {
		logger.info("To updateQuickInvoice in BillsInvoiceDao:::" + invoiceVo);
		Connection connection = null;
		if (invoiceVo != null && invoiceVo.getId() != null) {
			try {
				connection = getAccountsPayable();
				connection.setAutoCommit(false);
				updateQuickInvoiceGeneralInfo(invoiceVo, connection);
				updateQuickTransactionDetails(invoiceVo, connection);

				if (invoiceVo.getAttachments() != null && invoiceVo.getAttachments().size() > 0) {
					if (invoiceVo.getIsInvoiceWithBills()) {
						attachmentsDao.createAttachments(invoiceVo.getOrganizationId(), invoiceVo.getUserId(),
								invoiceVo.getAttachments(), AttachmentsConstants.MODULE_TYPE_INVOICES_BILL,
								invoiceVo.getId(), invoiceVo.getRoleName());
					}
				}
				if (invoiceVo.getAttachmentsToRemove() != null && invoiceVo.getAttachmentsToRemove().size() > 0) {
					for (Integer attachmentId : invoiceVo.getAttachmentsToRemove()) {
						attachmentsDao.changeStatusForSingleAttachment(attachmentId, CommonConstants.STATUS_AS_DELETE,
								invoiceVo.getUserId(), invoiceVo.getRoleName());
					}
				}
				connection.commit();
				logger.info("Succecssfully updated quick invoice in BillsInvoiceDao");
			} catch (Exception e) {
				logger.info("Error in updateQuickInvoice:: ", e);
				connection.rollback();
				throw new ApplicationException(e.getMessage());
			} finally {
				closeResources(null, null, connection);			
			}
		}
		return invoiceVo;
	}

	private void updateQuickInvoiceGeneralInfo(QuickInvoiceVo invoiceVo, Connection connection)
			throws ApplicationException {
		logger.info("To update into table invoice_general_information ");
		if (invoiceVo != null && invoiceVo.getId() != null) {
			if (invoiceVo.getIsInvoiceWithBills() && invoiceVo.getInvoiceNumber() == null) {
				throw new ApplicationException("Invoice number is mandatory");
			}
			PreparedStatement preparedStatement =null;
			try  {
				preparedStatement = connection.prepareStatement(BillsInvoiceConstants.UPDATE_GENERAL_INFO_FOR_QUICKINVOICE);
				String invoiceNo = getInvoiceOrderNo(invoiceVo.getId(), invoiceVo.getOrganizationId(), connection);
				if (invoiceNo != null && invoiceVo.getInvoiceNumber() != null
						&& !invoiceVo.getInvoiceNumber().equals(invoiceNo)) {
					logger.info("Existing invoice and to update invoice number differs ");
					boolean isInvoiceExist = checkInvoiceNoExistForAnOrganization(invoiceVo.getOrganizationId(),invoiceVo.getVendorId(),
							connection, invoiceVo.getInvoiceNumber());
					if (isInvoiceExist) {
						throw new ApplicationException("Invoice number already exist for the organization");
					}
				}
				String dateFormat = getDefaultDateOfOrg(invoiceVo.getOrganizationId());
				preparedStatement.setInt(1, invoiceVo.getVendorId() != null ? invoiceVo.getVendorId() : 0);
				preparedStatement.setString(2,
						invoiceVo.getInvoiceNumber() != null ? invoiceVo.getInvoiceNumber() : null);

				if (invoiceVo.getInvoiceDate() != null && !"".equals(invoiceVo.getInvoiceDate())) {
					Date invDate = DateConverter.getInstance().convertStringToDate(invoiceVo.getInvoiceDate(),
							dateFormat);
					if (invDate != null) {
						String date = DateConverter.getInstance().convertDateToGivenFormat(invDate, "yyyy-MM-dd");
						logger.info("converted dueDate::" + date);
						preparedStatement.setString(3, date);
					} else {
						logger.info("converted dueDate set as null");
						preparedStatement.setString(3, null);
					}
				} else {
					preparedStatement.setString(3, invoiceVo.getInvoiceDate());
				}

				if (invoiceVo.getDueDate() != null && !"".equals(invoiceVo.getDueDate())) {
					Date dueDate = DateConverter.getInstance().convertStringToDate(invoiceVo.getDueDate(), dateFormat);
					if (dueDate != null) {
						String date = DateConverter.getInstance().convertDateToGivenFormat(dueDate, "yyyy-MM-dd");
						logger.info("convertedDate::" + date);
						preparedStatement.setString(4, date);
					} else {
						logger.info("convertedDate set as null");
						preparedStatement.setString(4, null);
					}
				} else {
					preparedStatement.setString(4, invoiceVo.getDueDate());
				}
				preparedStatement.setBoolean(5,
						invoiceVo.getIsInvoiceWithBills() != null ? invoiceVo.getIsInvoiceWithBills() : true);
				preparedStatement.setString(6, invoiceVo.getRemarks() != null ? invoiceVo.getRemarks() : null);
				preparedStatement.setString(7, CommonConstants.STATUS_AS_DRAFT);
				preparedStatement.setDate(8, new Date(System.currentTimeMillis()));
				preparedStatement.setInt(9, Integer.valueOf(invoiceVo.getUserId()));
				preparedStatement.setInt(10, invoiceVo.getOrganizationId());
				preparedStatement.setBoolean(11, invoiceVo.getIsSuperAdmin());
				preparedStatement.setString(12, invoiceVo.getRoleName());
				preparedStatement.setString(13, invoiceVo.getAksharData());
				preparedStatement.setInt(14, invoiceVo.getId());
				preparedStatement.executeUpdate();
				logger.info("Successfully updated into table invoice_general_information ");
			} catch (Exception e) {
				logger.info("Error in updating  to table invoice_general_information ", e);
				throw new ApplicationException(e.getMessage());
			} finally {
				closeResources(null, preparedStatement, null);			
			}
		}
	}

	private void updateQuickTransactionDetails(QuickInvoiceVo invoiceVo, Connection connection)
			throws ApplicationException {
		logger.info("To update into table invoice_transaction_details " + invoiceVo);
		if (invoiceVo.getId() != null) {
			PreparedStatement preparedStatement = null;
			try {
				preparedStatement = connection.prepareStatement(BillsInvoiceConstants.UPDATE_TRANSACTION_FOR_QUICKINVOICE);
				preparedStatement.setInt(1, invoiceVo.getCurrencyId() != null ? invoiceVo.getCurrencyId() : 0);
				preparedStatement.setDouble(2, invoiceVo.getBaseAmount() != null ? invoiceVo.getBaseAmount() : 0.00);
				preparedStatement.setDouble(3, invoiceVo.getGstAmount() != null ? invoiceVo.getGstAmount() : 0.00);
				preparedStatement.setDouble(4, invoiceVo.getTotalAmount() != null ? invoiceVo.getTotalAmount() : 0.00);
				preparedStatement.setBoolean(5, invoiceVo.getIsRcmApplicable());
				preparedStatement.setDate(6, new Date(System.currentTimeMillis()));
				preparedStatement.setInt(7, invoiceVo.getId());
				preparedStatement.executeUpdate();
				logger.info("Successfully updated into table invoice_transaction_details");
			} catch (Exception e) {
				logger.info("Error in updating to table invoice_transaction_details ", e);
				throw new ApplicationException(e);
			} finally {
				closeResources(null, preparedStatement, null);			
			}
		}
	}

	public List<Integer> getInvoiceIdsForVendor(Integer vendorId,Integer currencyId) throws ApplicationException{
		Connection connection =null;
		PreparedStatement preparedStatement =null;
		ResultSet rs =null;
		List<Integer> invoiceIds=new ArrayList<Integer>();
		try  {
			if (vendorId != null) {
				connection = getAccountsPayable();
				preparedStatement = connection.prepareStatement(BillsInvoiceConstants.GET_ALL_INVOICES_FOR_VENDOR);
				preparedStatement.setInt(1, vendorId);
				preparedStatement.setString(2, CommonConstants.STATUS_AS_DRAFT);
				preparedStatement.setString(3, CommonConstants.STATUS_AS_VOID);
				preparedStatement.setString(4, CommonConstants.STATUS_AS_INACTIVE);
				preparedStatement.setString(5, CommonConstants.STATUS_AS_DELETE);
				preparedStatement.setInt(6, currencyId);

				rs = preparedStatement.executeQuery();
				while (rs.next()) {
					invoiceIds.add(rs.getInt(1));
				}
			}	
		} catch (Exception e) {
			logger.info("Error in getInvoiceIdsForVendor ", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, connection);			
		}
		return invoiceIds;
	}

	public List<PaymentNonCoreBillDetailsDropDownVo> getBillReferenceNumberByOrgVendor(Integer orgId, Integer vendorId, int currencyId, int paymentId) throws ApplicationException {

		List<PaymentNonCoreBillDetailsDropDownVo> listVos = new ArrayList<PaymentNonCoreBillDetailsDropDownVo>();
		if (orgId != null && vendorId != null) {
			String dateFormat = getDefaultDateOfOrg(orgId);
			Connection con =null;
			PreparedStatement preparedStatement =null;
			ResultSet rs =null;
			try  {
				con = getAccountsPayable();
				StringBuilder sqlQuery = new StringBuilder(BillsInvoiceConstants.GET_ALL_INVOICES_ORG_VENDOR);
				String invoiceIds = "";
				if (paymentId > 0) {
					PaymentNonCoreVo paymentVo = new PaymentNonCoreVo();
					paymentVo.setId(paymentId);
					paymentVo = paymentDao.fetchBillDetails(paymentVo, con);
					for (PaymentNonCoreBaseVo payment : paymentVo.getPayments()) {
						if (payment.getType() != null && payment.getType().equalsIgnoreCase("bill")) {
							//							InvoiceVo invoiceVo = new InvoiceVo();
							//							invoiceVo.setId(Integer.parseInt(payment.getBillRef()));
							invoiceIds += payment.getBillRef() + ",";
							//							getInvoiceGeneralInfoById(invoiceVo, con);
							//							PaymentNonCoreBillDetailsDropDownVo listVo = new PaymentNonCoreBillDetailsDropDownVo();
							//							listVo.setId(Integer.parseInt(payment.getBillRef()));
							//							listVo.setValue(Integer.parseInt(payment.getBillRef()));
							//							if (invoiceVo.getGeneralInfo() != null) {
							//								listVo.setName(invoiceVo.getGeneralInfo().getInvoiceNo());
							//							}
							//							listVo.setBalanceDue(getBalanceDueByInvoiceId(listVo.getId()));
							PaymentNonCoreBillDetailsDropDownVo listVo = getBalanceDueByInvoiceId(Integer.parseInt(payment.getBillRef()), orgId, currencyId, vendorId, dateFormat);
							listVos.add(listVo);

						}
					}
					if (listVos != null && listVos.size() > 0 && invoiceIds.length() > 1) {
						sqlQuery.append(" and igi.id NOT IN (");
						sqlQuery.append(invoiceIds);
						sqlQuery.setLength(sqlQuery.length() - 1);// Removing last ,
						sqlQuery.append(")");
					}
				}
				preparedStatement = con.prepareStatement(sqlQuery.toString());
				preparedStatement.setInt(1, orgId);
				preparedStatement.setInt(2, vendorId);
				preparedStatement.setInt(3, currencyId);
				preparedStatement.setString(4, CommonConstants.STATUS_AS_OPEN);
				preparedStatement.setString(5, CommonConstants.STATUS_AS_PARTIALLY_PAID);
				preparedStatement.setString(6, CommonConstants.STATUS_AS_ACTIVE);
				preparedStatement.setString(7, CommonConstants.DISPLAY_STATUS_AS_ACTIVE);
				preparedStatement.setString(8, CommonConstants.STATUS_AS_OVERDUE);
				preparedStatement.setString(9, CommonConstants.STATUS_AS_UNPAID);

				rs = preparedStatement.executeQuery();
				while (rs.next()) {
					PaymentNonCoreBillDetailsDropDownVo listVo = new PaymentNonCoreBillDetailsDropDownVo();
					listVo.setId(rs.getInt(1));
					listVo.setValue(rs.getInt(1));
					listVo.setVendorDisplayName(rs.getString(2));
					String[] invoiceNum = rs.getString(3) != null ? rs.getString(3).split("/") : null;
					logger.info("invoiceNo is::" + invoiceNum +"rs.getString(3)::"+rs.getString(3));

					String invoiceNo = null;
					if(invoiceNum!=null) {
						if (invoiceNum.length > 1) {
							invoiceNo = 
									(invoiceNum[0]!=null && invoiceNum[0].trim().length()>0?invoiceNum[0] +"/"  :"")+ (invoiceNum[1]) + 
									(invoiceNum[2]!=null && invoiceNum[2].trim().length()>0?  "/"+ invoiceNum[2]:"");
						}else {
							invoiceNo = rs.getString(3);
						}
					}
					logger.info("invoiceNo is::" + invoiceNo);


					listVo.setInvoiceNo(invoiceNo);

					listVo.setName(invoiceNo);
					listVo.setPoReferenceNo(rs.getString(4));
					Date invoiceDate = rs.getDate(5);
					if (invoiceDate != null && dateFormat != null) {
						String invDate = DateConverter.getInstance().convertDateToGivenFormat(invoiceDate,
								dateFormat);
						listVo.setInvoiceDate(invDate);
					}
					Date dueDate = rs.getDate(6);
					if (dueDate != null && dateFormat != null) {
						String duedate = DateConverter.getInstance().convertDateToGivenFormat(dueDate, dateFormat);
						listVo.setDueDate(duedate);
					}
					listVo.setStatus(rs.getString(7));
					listVo.setBalanceDue(rs.getDouble(10));
					listVos.add(listVo);
				}

			} catch (Exception e) {
				logger.info("Error in getAllInvoicesWithBillsForOrg ", e);
				throw new ApplicationException(e);
			} finally {
				closeResources(rs, preparedStatement, con);			
			}
		}
		return listVos;

	}


	public PaymentNonCoreBillDetailsDropDownVo getBalanceDueByInvoiceId(Integer id, int orgId, int currencyId, int vendorId, String dateFormat) throws ApplicationException {
		PaymentNonCoreBillDetailsDropDownVo billDetail = null;
		Connection con =null;
		PreparedStatement preparedStatement =null;
		ResultSet rs =null;
		try  {
			con = getAccountsPayable();

			preparedStatement = con.prepareStatement(BillsInvoiceConstants.GET_BALANCE_DUE_BY_INVOICE_ID);
			preparedStatement.setInt(1, orgId);
			preparedStatement.setInt(2, vendorId);
			preparedStatement.setInt(3, currencyId);
			preparedStatement.setString(4, CommonConstants.STATUS_AS_OPEN);
			preparedStatement.setString(5, CommonConstants.STATUS_AS_PARTIALLY_PAID);
			preparedStatement.setString(6, CommonConstants.STATUS_AS_ACTIVE);
			preparedStatement.setString(7, CommonConstants.DISPLAY_STATUS_AS_ACTIVE);
			preparedStatement.setString(8, CommonConstants.STATUS_AS_OVERDUE);
			preparedStatement.setString(9, CommonConstants.STATUS_AS_UNPAID);
			preparedStatement.setString(10, CommonConstants.STATUS_AS_PAID);
			preparedStatement.setInt(11, id);

			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				billDetail = new PaymentNonCoreBillDetailsDropDownVo();
				billDetail.setId(id);
				billDetail.setValue(id);
				billDetail.setBalanceDue(rs.getDouble(1));
				billDetail.setName(rs.getString(2));
				Date dueDate = rs.getDate(3);
				if (dueDate != null && dateFormat != null) {
					String duedate = DateConverter.getInstance().convertDateToGivenFormat(dueDate, dateFormat);
					billDetail.setDueDate(duedate);
				}
			}

		} catch (Exception e) {
			logger.info("Error in getAllInvoicesWithBillsForOrg ", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);			
		}

		return billDetail;


	}

	public List<InvoiceListVo> getBillReferenceNumberByOrgVendorAndStatus(Integer orgId, Integer id, Integer currencyId) throws ApplicationException {

		List<InvoiceListVo> listVos = null;
		if (orgId != null && id != null) {
			String dateFormat = getDefaultDateOfOrg(orgId);
			Connection con =null;
			PreparedStatement preparedStatement =null;
			ResultSet rs =null;
			try {
				con = getAccountsPayable();
				preparedStatement = con.prepareStatement(BillsInvoiceConstants.GET_INVOICES_BY_ORG_VENDOR_STATUS);
				preparedStatement.setInt(1, orgId);
				preparedStatement.setInt(2, id);
				preparedStatement.setString(3, CommonConstants.STATUS_AS_DRAFT);
				preparedStatement.setString(4, CommonConstants.STATUS_AS_DELETE);
				preparedStatement.setString(5, CommonConstants.STATUS_AS_INACTIVE);
				preparedStatement.setString(6, CommonConstants.STATUS_AS_VOID);
				preparedStatement.setInt(7, currencyId);

				listVos = new ArrayList<InvoiceListVo>();
				rs = preparedStatement.executeQuery();
				while (rs.next()) {
					InvoiceListVo listVo = new InvoiceListVo();
					listVo.setInvoiceId(rs.getInt(1));
					listVo.setValue(rs.getInt(1));
					listVo.setVendorDisplayName(rs.getString(2));
					String[] invoiceNum = rs.getString(3) != null ? rs.getString(3).split("/") : null;
					logger.info("invoiceNo is::" + invoiceNum +"rs.getString(3)::"+rs.getString(3));

					String invoiceNo = null;
					if(invoiceNum!=null) {
						if (invoiceNum.length > 1) {
							invoiceNo = 
									(invoiceNum[0]!=null && invoiceNum[0].trim().length()>0?invoiceNum[0] +"/"  :"")+ (invoiceNum[1]) + 
									(invoiceNum[2]!=null && invoiceNum[2].trim().length()>0?  "/"+ invoiceNum[2]:"");
						}else {
							invoiceNo = rs.getString(3);
						}
					}
					logger.info("invoiceNo is::" + invoiceNo);


					listVo.setInvoiceNo(invoiceNo);

					listVo.setName(invoiceNo);
					listVo.setPoReferenceNo(rs.getString(4));
					Date invoiceDate = rs.getDate(5);
					if (invoiceDate != null && dateFormat != null) {
						String invDate = DateConverter.getInstance().convertDateToGivenFormat(invoiceDate,
								dateFormat);
						listVo.setInvoiceDate(invDate);
					}
					Date dueDate = rs.getDate(6);
					if (dueDate != null && dateFormat != null) {
						String duedate = DateConverter.getInstance().convertDateToGivenFormat(dueDate, dateFormat);
						listVo.setDueDate(duedate);
					}
					listVo.setStatus(rs.getString(7));
					listVo.setAmount(rs.getDouble(8));
					listVo.setOrgName(rs.getString(9));
					double dueAmount=rs.getDouble(10);
					logger.info("Invoice:"+rs.getString(3)+"Total due: "+rs.getDouble(8)+"Bal due: "+dueAmount);
					CurrencyVo currencyVo=currencyDao.getCurrency(currencyId);
					if (rs.getDouble(8)>0 && dueAmount>=0 &currencyVo!=null) {
						double balanceDue=rs.getDouble(8)-dueAmount;//Total -invoice due balance
						logger.info("Invoice:"+rs.getString(3)+"Bal due: "+balanceDue);
						if(balanceDue==0) {
							balanceDue=rs.getDouble(8);
						}
						BigDecimal bd = new BigDecimal(balanceDue).setScale( currencyVo.getNoOfDecimalsForAmount(), RoundingMode.HALF_UP);
						listVo.setBalanceDue(bd.doubleValue());
					}
					//Consider only partially paid/paid
					if(dueAmount>=0 && listVo.getAmount()!=null && dueAmount<listVo.getAmount().doubleValue()) {
						listVos.add(listVo);
					}
				}
			} catch (Exception e) {
				logger.error("Error in getAllInvoicesWithBillsForOrg ", e);
				throw new ApplicationException(e);
			} finally {
				closeResources(rs, preparedStatement, con);			
			}
		}
		return listVos;

	}

	public boolean updateCurrentWorkflowRule(int invoiceId,int currentRuleId,String status,String pendingApprovalStatus) throws ApplicationException {
		logger.info("Entry into updateCurrentWorkflowRule :invoiceId:"+invoiceId+",currentRuleId"+currentRuleId+",status:"+status+",pendingApprovalStatus+"+pendingApprovalStatus);
		Connection con = null;
		PreparedStatement preparedStatement =null;
		try {
			con = getAccountsPayable();
			preparedStatement = con.prepareStatement(BillsInvoiceConstants.UPDATE_CURRENT_RULE);
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

	public boolean checkCurrentRuleExists(int invoiceId) throws ApplicationException {
		Connection con =null;
		PreparedStatement preparedStatement =null;
		ResultSet rs =null;
		try {
			con = getAccountsPayable();
			preparedStatement = con.prepareStatement(BillsInvoiceConstants.GET_CURRENT_RULE);
			preparedStatement.setInt(1, invoiceId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				int currentRule=rs.getInt(1);
				if(currentRule>0) {
					return true;
				}
			}
		} catch (Exception e) {
			logger.info("Error in getAllInvoicesWithBillsForOrg ", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);			
		}
		return false;
	}

	private List<CommonTransactionVo> getInvoiceTransactions(int invoiceId) throws ApplicationException {
		logger.info("Entry into in getInvoiceTransactions");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		boolean paymentsExists=false;
		boolean refundExists=false;
		boolean isVoidable=true;
		List<CommonTransactionVo> trasactions=new ArrayList<CommonTransactionVo>();
		try {
			// Get all Bill Payments of invoice
			con = getAccountsPayable();
			preparedStatement = con.prepareStatement(PaymentNonCoreConstants.GET_PAYMENTS_OF_INVOICE);
			preparedStatement.setInt(1, invoiceId);
			preparedStatement.setString(2, CommonConstants.STATUS_AS_ACTIVE);
			preparedStatement.setString(3, PaymentNonCoreConstants.BILL_PAYMENTS_TYPE_BILL);
			rs = preparedStatement.executeQuery();
			logger.info(preparedStatement.toString());
			while (rs.next()) {
				CommonTransactionVo trasaction=new CommonTransactionVo();
				trasaction.setId(rs.getInt(1));
				trasaction.setReference(rs.getString(2));
				trasaction.setModule(JournalEntriesConstants.SUB_MODULE_PAYMENTS);
				trasaction.setValue(rs.getString(4)!=null?rs.getString(3)+" "+rs.getString(4):"-");
				trasactions.add(trasaction);
				paymentsExists=true;
			}
			closeResources(rs, preparedStatement, con);
			con = getAccountsReceivableConnection();
			preparedStatement = con.prepareStatement(ReceiptConstants.GET_ALL_REFUNDS_OF_INVOICE);
			preparedStatement.setInt(1, invoiceId);
			preparedStatement.setString(2, CommonConstants.STATUS_AS_DRAFT);
			preparedStatement.setString(3, CommonConstants.STATUS_AS_VOID);
			preparedStatement.setString(4, CommonConstants.STATUS_AS_ACTIVE);

			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				CommonTransactionVo trasaction=new CommonTransactionVo();
				trasaction.setId(rs.getInt(1));
				trasaction.setReference(rs.getString(2));
				trasaction.setModule(JournalEntriesConstants.SUB_MODULE_RECEIPTS);
				trasaction.setValue(rs.getString(4)!=null?rs.getString(3)+" "+rs.getString(4):"-");
				trasactions.add(trasaction);
				refundExists=true;
			}
			if(refundExists || paymentsExists) {
				isVoidable=false;
			}

			logger.info("isInvoiceVoidable : paymentsExists?:" +paymentsExists+",refundExists?:"+ refundExists
					+",isVoidable?:"+ isVoidable+ ",For  invoice id:" + invoiceId);
		} catch (Exception e) {
			logger.error("Error in isInvoiceVoidable", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return trasactions;

	}

	public Map<String , String > getStatusPercent(int orgId) throws ApplicationException{
		Map<String , String > statusMap = new HashMap<String, String>();
		Connection con = null;
		PreparedStatement preparedStatement =null;
		ResultSet rs =null;
		try{
			con = getAccountsPayable();
			preparedStatement = con.prepareStatement(BillsInvoiceConstants.GET_STATUS_AND_ITS_PERCENT);
			preparedStatement.setInt(1, orgId);
			preparedStatement.setInt(2, orgId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				String status = rs.getString(1) ;
				String displayStatus = null;
				switch (status) {
				case CommonConstants.STATUS_AS_ACTIVE:
					displayStatus = CommonConstants.DISPLAY_STATUS_AS_ACTIVE;
					break;
				case CommonConstants.STATUS_AS_DRAFT:
					displayStatus = CommonConstants.DISPLAY_STATUS_AS_DRAFT;
					break;
				case CommonConstants.STATUS_AS_INACTIVE:
					displayStatus = CommonConstants.DISPLAY_STATUS_AS_INACTIVE;
					break;
				case CommonConstants.STATUS_AS_OPEN:
					displayStatus = CommonConstants.DISPLAY_STATUS_AS_OPEN;
					break;
				case CommonConstants.STATUS_AS_OVERDUE:
					displayStatus = CommonConstants.DISPLAY_STATUS_AS_OVERDUE;
					break;
				case CommonConstants.STATUS_AS_PAID:
					displayStatus = CommonConstants.DISPLAY_STATUS_AS_PAID;
					break;
				case CommonConstants.STATUS_AS_UNPAID:
					displayStatus = CommonConstants.DISPLAY_STATUS_AS_UNPAID;
					break;	
				case CommonConstants.STATUS_AS_VOID:
					displayStatus = CommonConstants.DISPLAY_STATUS_AS_VOID;
					break;
				case CommonConstants.STATUS_AS_PARTIALLY_PAID:
					displayStatus = CommonConstants.DISPLAY_STATUS_AS_PARTIALLY_PAID;
					break;
				}

				statusMap.put( displayStatus, rs.getString(2)) ;
			}
		} catch (Exception e) {
			logger.info("Error in getStatusPercent  ", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}

		return statusMap;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<InvoiceListVo> getDueInvoicesList(int organizationId,
			String userId, String roleName) throws ApplicationException {
		logger.info("To get the getDueInvoicesList with filter values :::" );
		Connection connection = null;
		List<InvoiceListVo> listVos = new ArrayList<InvoiceListVo>();
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			// String dateFormat = getDefaultDateOfOrg(filterVo.getOrgId());
			StringBuilder filterQuery = new StringBuilder(BillsInvoiceConstants.GET_ALL_DUE_INVOICES);
			List paramsList = new ArrayList<>();
			paramsList.add(organizationId);
			paramsList.add(true);
			if(!CommonConstants.ROLE_SUPER_ADMIN.equals(roleName)) {
				filterQuery.append(" and igi.user_id = ? and igi.role_name in(?) ");
				paramsList.add(userId);
				paramsList.add(roleName);
			}

			filterQuery.append(" and igi.status IN (?,?,?,?,?) ORDER BY igi.due_date asc ");
			paramsList.add(CommonConstants.STATUS_AS_ACTIVE);
			paramsList.add(CommonConstants.STATUS_AS_OPEN);
			paramsList.add(CommonConstants.STATUS_AS_OVERDUE);
			paramsList.add(CommonConstants.STATUS_AS_UNPAID);
			paramsList.add(CommonConstants.STATUS_AS_PARTIALLY_PAID);

			Boolean viewInvoices = true;
			logger.info(filterQuery.toString());
			logger.info(paramsList);
			if (viewInvoices) {
				connection = getAccountsPayable();
				preparedStatement = connection.prepareStatement(filterQuery.toString());
				int counter = 1;
				for (int i = 0; i < paramsList.size(); i++) {
					logger.info(counter);
					preparedStatement.setObject(counter, paramsList.get(i));
					counter++;
				}
				rs = preparedStatement.executeQuery();
				while (rs.next()) {
					InvoiceListVo listVo = new InvoiceListVo();
					Integer invoiceId=rs.getInt(1);
					listVo.setInvoiceId(invoiceId);
					listVo.setVendorDisplayName(rs.getString(2));
					String[] invoiceNum = rs.getString(3) != null ? rs.getString(3).split("/") : null;
					logger.info("invoiceNo is::" + invoiceNum +"rs.getString(3)::"+rs.getString(3));

					String invoiceNo = null;
					if(invoiceNum!=null) {
						if (invoiceNum.length > 1) {
							invoiceNo = 
									(invoiceNum[0]!=null && invoiceNum[0].trim().length()>0?invoiceNum[0] +"/"  :"")+ (invoiceNum[1]) + 
									(invoiceNum[2]!=null && invoiceNum[2].trim().length()>0?  "/"+ invoiceNum[2]:"");
						}else {
							invoiceNo = rs.getString(3);
						}
					}
					logger.info("invoiceNo is::" + invoiceNo);


					listVo.setInvoiceNo(invoiceNo);

					listVo.setPoReferenceNo(rs.getString(4));
					listVo.setInvoiceDate(rs.getString(5));
					listVo.setDueDate(rs.getString(6));
					String status = rs.getString(7);
					String displayStatus = null;
					switch (status) {
					case CommonConstants.STATUS_AS_ACTIVE:
						displayStatus = CommonConstants.DISPLAY_STATUS_AS_ACTIVE;
						break;
					case CommonConstants.STATUS_AS_DRAFT:
						displayStatus = CommonConstants.DISPLAY_STATUS_AS_DRAFT;
						break;
					case CommonConstants.STATUS_AS_INACTIVE:
						displayStatus = CommonConstants.DISPLAY_STATUS_AS_INACTIVE;
						break;
					case CommonConstants.STATUS_AS_OPEN:
						displayStatus = CommonConstants.DISPLAY_STATUS_AS_OPEN;
						break;
					case CommonConstants.STATUS_AS_OVERDUE:
						displayStatus = CommonConstants.DISPLAY_STATUS_AS_OVERDUE;
						break;
					case CommonConstants.STATUS_AS_PAID:
						displayStatus = CommonConstants.DISPLAY_STATUS_AS_PAID;
						break;
					case CommonConstants.STATUS_AS_UNPAID:
						displayStatus = CommonConstants.DISPLAY_STATUS_AS_UNPAID;
						break;	
					case CommonConstants.STATUS_AS_VOID:
						displayStatus = CommonConstants.DISPLAY_STATUS_AS_VOID;
						break;
					case CommonConstants.STATUS_AS_PARTIALLY_PAID:
						displayStatus = CommonConstants.DISPLAY_STATUS_AS_PARTIALLY_PAID;
						break;
					}
					listVo.setStatus(displayStatus);

					Double total = rs.getDouble(8);
					listVo.setAmount(total != null && !total.equals(0.00) ? total : rs.getDouble(11));
					listVo.setOrgName(rs.getString(9));
					listVo.setBalanceDue(rs.getDouble(10));
					String updatedUser = rs.getString(12);
					boolean isVendorEditable = true;
					if (CommonConstants.ROLE_SUPER_ADMIN.equals(updatedUser)) {
						isVendorEditable = false;
					}
					if (CommonConstants.DISPLAY_STATUS_AS_ACTIVE.equals(listVo.getStatus())
							|| CommonConstants.DISPLAY_STATUS_AS_INACTIVE.equals(listVo.getStatus())
							|| CommonConstants.DISPLAY_STATUS_AS_OPEN.equals(listVo.getStatus())) {
						isVendorEditable = false;
					}
					listVo.setIsVendorEditable(isVendorEditable);
					listVo.setIsQuick(rs.getBoolean(13));
					listVo.setRoleName(rs.getString(14));
					listVo.setPendingApprovalStatus(rs.getString(15));
					listVo.setVendorId(rs.getInt(16));
					Integer currencyId=rs.getInt(17);
					if(currencyId!=null) {
						listVo.setCurrencyId(currencyId);
						CurrencyVo currencyVo=currencyDao.getCurrency(currencyId);
						listVo.setCurrencySymbol(currencyVo!=null && currencyVo.getSymbol()!=null?currencyVo.getSymbol():"");	
					}
					List<CommonTransactionVo> trasactions=getInvoiceTransactions(invoiceId);
					listVo.setTrasactions(trasactions);
					listVo.setIsVoidable(trasactions!=null &&trasactions.isEmpty());
					listVos.add(listVo);
					listVos=listVos.stream().sorted().collect(Collectors.toList());;
					logger.info(listVos);

				}

			}
			logger.info("listVos size::" + listVos.size());
			logger.info("Retrieved Invoices ");

		} catch (Exception e) {
			logger.info("Error in getAllFilteredInvoices:: ", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, connection);
		}
		return listVos;

	}


	public List<VendorCreditInvoiceVo> getInvoiceDataForVendorCredit(int orgId)
			throws ApplicationException {
		logger.info("To get the getInvoiceDataForVendorCredit");
		Connection connection = null;
		List<VendorCreditInvoiceVo> listVos = new ArrayList<>();
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;

		try {
			connection = getAccountsPayable();
			preparedStatement = connection.prepareStatement(BillsInvoiceConstants.GET_ALL_INVOICES_FOR_VENDOR_CREDIT);
			preparedStatement.setInt(1, orgId);
			preparedStatement.setString(2, CommonConstants.STATUS_AS_ACTIVE);
			preparedStatement.setString(3, CommonConstants.STATUS_AS_OPEN);
			preparedStatement.setString(4, CommonConstants.STATUS_AS_OVERDUE);
			preparedStatement.setString(5, CommonConstants.STATUS_AS_UNPAID);
			preparedStatement.setString(6, CommonConstants.STATUS_AS_PARTIALLY_PAID);

			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				VendorCreditInvoiceVo creditInvoiceVo = new VendorCreditInvoiceVo();
				creditInvoiceVo.setId(rs.getInt(1));
				creditInvoiceVo.setName(rs.getString(2));
				creditInvoiceVo.setOriginalInvoiceDate(rs.getString(3));
				creditInvoiceVo.setValue(rs.getInt(1));

				listVos.add(creditInvoiceVo);
			}

		} catch (Exception e) {
			logger.error("Error in getInvoiceDataForVendorCredit", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, connection);
		}

		return listVos;

	}
	
	public List<BillsInvoiceDashboardVo> getRecentInvoicesOfAnOrganizationForUserAndRole(int organizationId,
			String userId, String roleName) throws ApplicationException {


		logger.info("Entry into method: getRecentInvoicesOfAnOrganizationForUserAndRole");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		List<BillsInvoiceDashboardVo> totalList = new ArrayList<BillsInvoiceDashboardVo>();
		try {
			con = getAccountsPayable();
			StringBuilder filterQuery = new StringBuilder(BillsInvoiceConstants.GET_DASH_BOARD_INVOICES);

			if(!CommonConstants.ROLE_SUPER_ADMIN.equals(roleName)) {
				filterQuery.append(" and igi.user_id = ? and igi.role_name in(?) ");
			}
			filterQuery.append(" ORDER BY igi.due_date ASC limit 10");
			preparedStatement = con.prepareStatement(filterQuery.toString());

			preparedStatement.setInt(1, organizationId);
			preparedStatement.setString(2, CommonConstants.STATUS_AS_ACTIVE);
			preparedStatement.setString(3, CommonConstants.STATUS_AS_OPEN);
			preparedStatement.setString(4, CommonConstants.STATUS_AS_OVERDUE);
			preparedStatement.setString(5, CommonConstants.STATUS_AS_PARTIALLY_PAID);
			if (!(roleName.equals(CommonConstants.ROLE_SUPER_ADMIN))) {
				preparedStatement.setString(6, userId);
				preparedStatement.setString(7, roleName);
			}
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				BillsInvoiceDashboardVo listVo = new BillsInvoiceDashboardVo();
				listVo.setId(rs.getInt(1));
				Connection con1 = getUserMgmConnection();
				String dateFormat = organizationDao.getDefaultDateForOrganization(organizationId, con1);
				closeResources(null, null, con1);

				String[] invoiceNum = rs.getString(2) != null ? rs.getString(2).split("/") : null;
				logger.info("invoiceNo is::" + invoiceNum +"rs.getString(3)::"+rs.getString(2));

				String invoiceNo = null;
				if(invoiceNum!=null) {
					if (invoiceNum.length > 1) {
						invoiceNo = 
								(invoiceNum[0]!=null && invoiceNum[0].trim().length()>0?invoiceNum[0] +"/"  :"")+ (invoiceNum[1]) + 
								(invoiceNum[2]!=null && invoiceNum[2].trim().length()>0?  "/"+ invoiceNum[2]:"");
					}else {
						invoiceNo = rs.getString(3);
					}
				}
				logger.info("invoiceNo is::" + invoiceNo);


				listVo.setReferenceNumber(invoiceNo);


				if (dateFormat != null && dateFormat.length() > 0) {
					listVo.setDate(rs.getDate(3) != null
							? DateConverter.getInstance().convertDateToGivenFormat(rs.getDate(3), dateFormat)
									: null);
				}


				String displayStatus="";
				if(rs.getString(4)!=null) {
					switch (rs.getString(4)) {
					case CommonConstants.STATUS_AS_ACTIVE:
						displayStatus = CommonConstants.DISPLAY_STATUS_AS_OPEN;
						break;
					case CommonConstants.STATUS_AS_OPEN:
						displayStatus = CommonConstants.DISPLAY_STATUS_AS_OPEN;
						break;
					case CommonConstants.STATUS_AS_OVERDUE:
						displayStatus = CommonConstants.DISPLAY_STATUS_AS_OVERDUE;
						break;
					case CommonConstants.STATUS_AS_PARTIALLY_PAID:
						displayStatus = CommonConstants.DISPLAY_STATUS_AS_PARTIALLY_PAID;
						break;
					}
					listVo.setPaymentType(displayStatus);
				}
				String amountPaid = rs.getString(5);
				listVo.setAmount(amountPaid);
				int currencyId=rs.getInt(6);
				if(currencyId>0) {
					CurrencyVo currencyVo = currencyDao.getCurrency(currencyId);
					if (amountPaid != null && amountPaid.length() > 0 && currencyVo != null) {// Set symbol
						listVo.setAmount(
								currencyVo.getSymbol() != null ? currencyVo.getSymbol() + " " + amountPaid : amountPaid);
					}
				}
				totalList.add(listVo);
				totalList=totalList.stream().sorted().collect(Collectors.toList());;

			}

		} catch (Exception e) {
			logger.error("Error during getRecentInvoicesOfAnOrganizationForUserAndRole", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}

		return totalList;


	}

	public BillsPayableVo getBillsPayable(int organizationId, String userId, String roleName) throws ApplicationException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		BillsPayableVo billsPayable = new BillsPayableVo();
		List<BillsPayableItemVo> bills = new ArrayList<>();
		Double totalAmount = 0.0;
		Double overdueAmount = 0.0;
		String symbol = null;
		int count = 0;
		Connection con =null;
		try {
			con = getAccountsPayable();
			if (!roleName.equalsIgnoreCase(CommonConstants.ROLE_SUPER_ADMIN)) {
				ps = con.prepareStatement(BillsInvoiceConstants.GET_BILLS_PAYABLE_USER_ROLE);
				ps.setInt(1, organizationId);
				ps.setString(2, CommonConstants.STATUS_AS_PAID);
				ps.setString(3, CommonConstants.STATUS_AS_DRAFT);
				ps.setString(4, CommonConstants.STATUS_AS_VOID);
				ps.setString(5, CommonConstants.STATUS_AS_DELETE);
				ps.setString(6, userId);
				ps.setString(7, roleName);
			} else {
				ps = con.prepareStatement(BillsInvoiceConstants.GET_BILLS_PAYABLE);
				ps.setInt(1, organizationId);
				ps.setString(2, CommonConstants.STATUS_AS_PAID);
				ps.setString(3, CommonConstants.STATUS_AS_DRAFT);
				ps.setString(4, CommonConstants.STATUS_AS_VOID);
				ps.setString(5, CommonConstants.STATUS_AS_DELETE);
			}
			rs = ps.executeQuery();
			while (rs.next()) {
				BillsPayableItemVo bill = new BillsPayableItemVo();
				bill.setId(rs.getInt(1));
				bill.setBillNo(rs.getString(2));
				bill.setVendor(rs.getString(3));
				bill.setDueDate(rs.getString(4));
				String amount = rs.getString(5);
				bill.setBalanceDue(amount);
				String status = rs.getString(6);
				switch(status) {
				case CommonConstants.STATUS_AS_ACTIVE:
					status = CommonConstants.DISPLAY_STATUS_AS_OPEN;
					break;
				case CommonConstants.STATUS_AS_OVERDUE:
					status = CommonConstants.DISPLAY_STATUS_AS_OVERDUE;
					break;
				case CommonConstants.STATUS_AS_PARTIALLY_PAID:
					status = CommonConstants.DISPLAY_STATUS_AS_PARTIALLY_PAID;
					break;
				case CommonConstants.STATUS_AS_OPEN:
					status = CommonConstants.DISPLAY_STATUS_AS_OPEN;
					break;
				}
				bill.setStatus(status);
				symbol = rs.getString(7);
				bill.setCurrencySymbol(symbol);
				if (symbol != null && amount != null) {
					bill.setAmount(symbol + " " + amount);
					totalAmount += Double.parseDouble(amount);
					if (status.equalsIgnoreCase(CommonConstants.DISPLAY_STATUS_AS_OVERDUE)) {
						overdueAmount += Double.parseDouble(amount);
					}
				}
				bill.setInvoiceDate(rs.getString(8));
				bill.setVendorId(rs.getInt(9));
				bill.setCurrencyId(rs.getInt(10));
				bill.setIsPaymentInitiated(checkYesBankPaymentInitiated(organizationId, rs.getInt(9), YesBankConstants.KEYCONTACT_VENDOR, rs.getInt(1), YesBankConstants.MODULE_INVOICE_PAYMENT));

				if (count < 10)
					bills.add(bill);	
				count++;
			}
			billsPayable.setBills(bills);
			DecimalFormat df = new DecimalFormat("#.##");
			df.setRoundingMode(RoundingMode.CEILING);
			billsPayable.setTotalAmount(symbol + " " + String.valueOf(df.format(totalAmount)));
			billsPayable.setOverdueAmount(symbol + " " + String.valueOf(df.format(overdueAmount)));
		} catch (Exception e) {
			throw new ApplicationException(e);

		} finally {
			closeResources(rs, ps, con);
		}
		return billsPayable;
	}
	
	public Boolean checkInvoiceNoExist(Integer organizationId, Integer vendorId, String invoiceNo) throws ApplicationException {
		PreparedStatement ps = null;
		Boolean invoiceExist = false;
		ResultSet rs = null;
		Connection con =null;
		try {
			con = getAccountsPayable();
			invoiceExist = checkInvoiceNoExistForAnOrganization(organizationId,vendorId, con,invoiceNo);
			
		} catch (Exception e) {
			throw new ApplicationException(e);

		} finally {
			closeResources(rs, ps, con);
		}
		return invoiceExist;
	}

}
