package com.blackstrawai.ar;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
import com.blackstrawai.ar.applycredits.CreditNotesDetailsVo;
import com.blackstrawai.ar.creditnotes.CreditNotesVo;
import com.blackstrawai.ar.invoice.ArInvoiceFilterVo;
import com.blackstrawai.ar.invoice.ArInvoiceGeneralInformationVo;
import com.blackstrawai.ar.invoice.ArInvoiceProductVo;
import com.blackstrawai.ar.invoice.ArInvoiceTaxDetailsVo;
import com.blackstrawai.ar.invoice.ArInvoiceTaxDistributionVo;
import com.blackstrawai.ar.invoice.ArInvoiceVo;
import com.blackstrawai.attachments.AttachmentsConstants;
import com.blackstrawai.attachments.AttachmentsDao;
import com.blackstrawai.attachments.AttachmentsVo;
import com.blackstrawai.attachments.UploadFileVo;
import com.blackstrawai.common.BaseDao;
import com.blackstrawai.common.CommonConstants;
import com.blackstrawai.common.FinanceCommonDao;
import com.blackstrawai.inventorymgmt.ProductInventoryDao;
import com.blackstrawai.journals.GeneralLedgerVo;
import com.blackstrawai.onboarding.OrganizationDao;
import com.blackstrawai.settings.CommonVo;
import com.blackstrawai.settings.CurrencyDao;
import com.blackstrawai.settings.CurrencyVo;
import com.blackstrawai.workflow.WorkflowInvoiceVo;
import com.fasterxml.jackson.databind.ObjectMapper;

@Repository
public class CreditNotesDao extends BaseDao {

	private Logger logger = Logger.getLogger(CreditNotesDao.class);

	@Autowired
	private ArInvoiceDao arInvoiceDao;

	@Autowired
	private OrganizationDao organizationDao;

	@Autowired
	private FinanceCommonDao financeCommonDao;

	@Autowired
	private AttachmentsDao attachmentsDao;

	@Autowired
	private CurrencyDao currencyDao;
	
	
	@Autowired
	private ProductInventoryDao productInventoryDao;

	public CreditNotesVo createCreditNotes(CreditNotesVo creditNotesVo) throws ApplicationException, SQLException {
		logger.info("Entry into method: createCreditNotes");
		Connection connection = null;
		if (creditNotesVo != null) {
			try {
				connection = getAccountsReceivableConnection();
				connection.setAutoCommit(false);
				createCreditnotesInfo(creditNotesVo, connection);
				String invoiceNo = arInvoiceDao.getInvoiceOrderNo(creditNotesVo.getOriginalInvoiceId()!=null ? Integer.valueOf(creditNotesVo.getOriginalInvoiceId()) : 0 , creditNotesVo.getOrganizationId(), connection);
				if(invoiceNo!=null ) {
					creditNotesVo.setOriginalInvoiceNumber(invoiceNo.replace("~", "/"));
				}
				if (creditNotesVo.getId() > 0 && creditNotesVo.getProducts() != null
						&& creditNotesVo.getProducts().size() > 0) {
					for (ArInvoiceProductVo product : creditNotesVo.getProducts()) {
						Integer productId = createProduct(product, connection, creditNotesVo.getId());
						createTaxDetailsForProduct(productId, product, connection);
						productInventoryDao.addCreditNoteProductToInventoryMgmt(product, creditNotesVo);
					}
				}
				if (creditNotesVo.getId() > 0 && creditNotesVo.getAttachments() != null
						&& creditNotesVo.getAttachments().size() > 0) {
					attachmentsDao.createAttachments(creditNotesVo.getOrganizationId(), creditNotesVo.getUserId(),
							creditNotesVo.getAttachments(),
							AttachmentsConstants.MODULE_TYPE_ACCOUNTS_RECEIVABLES_CREDIT_NOTE, creditNotesVo.getId(),
							creditNotesVo.getRoleName());

				}
				connection.commit();
				logger.info("Succecssfully created CreditNotes in CreditNotesDao");
			} catch (ApplicationException | SQLException e) {
				logger.info("Error in createCreditNotes:: ", e);
				connection.rollback();
				logger.info("Before throw in dao :: " + e.getMessage());
				throw new ApplicationException(e.getMessage());
			} finally {
				closeResources(null, null, connection);
			}
		}
		return creditNotesVo;
	}

	private CreditNotesVo createCreditnotesInfo(CreditNotesVo creditNotesVo, Connection connection)
			throws ApplicationException {
		logger.info("Entry innto createCreditnotesGeneralInfo ");
		if (creditNotesVo != null && creditNotesVo.getOrganizationId() > 0) {
			if (creditNotesVo.getCreditNoteNumber() == null) {
				throw new ApplicationException("Credit note number is mandatory");
			}
		}
		PreparedStatement preparedStatement = null;
		ResultSet rs =null;
		try  {
			preparedStatement = connection.prepareStatement(CreditNoteConstants.INSERT_INTO_CREDIT_NOTES_ORGANIZATION, Statement.RETURN_GENERATED_KEYS);
			StringBuilder creditNoteNumberBuilder = new StringBuilder();
			creditNoteNumberBuilder.append(
					creditNotesVo.getCreditNoteNumberPrefix() != null ? creditNotesVo.getCreditNoteNumberPrefix() : " ")
			.append("/").append(creditNotesVo.getCreditNoteNumber()).append("/")
			.append(creditNotesVo.getCreditNoteNumberSuffix() != null
			? creditNotesVo.getCreditNoteNumberSuffix()
					: " ");
			String creditNoteNumber = creditNoteNumberBuilder.toString();
			logger.info("creditNoteNumber" + creditNoteNumber);
			boolean isCredtnoteExist = checkCreditNotesExist(creditNoteNumber, creditNotesVo, connection);
			if (isCredtnoteExist) {
				throw new ApplicationException("Credit note number already exist for the organization");
			}

			preparedStatement.setInt(1, creditNotesVo.getCustomerId());
			preparedStatement.setString(2, creditNotesVo.getOriginalInvoiceId());
			int creditNoteTypeId=creditNotesVo.getCreditNoteTypeId();
			preparedStatement.setInt(3, creditNotesVo.getCreditNoteTypeId());
			preparedStatement.setString(4, creditNoteNumber);
			preparedStatement.setString(5, creditNotesVo.getCreditNoteDate() != null
					? DateConverter.getInstance().correctDatePickerDateToString(creditNotesVo.getCreditNoteDate())
							: null);
			preparedStatement.setString(6, creditNotesVo.getReason());
			preparedStatement.setString(7, creditNotesVo.getTnc());
			preparedStatement.setString(8, creditNotesVo.getSubTotal());
			preparedStatement.setInt(9, creditNotesVo.getTdsId());
			preparedStatement.setString(10, creditNotesVo.getTdsValue());
			preparedStatement.setInt(11, creditNotesVo.getDiscLedgerId());
			preparedStatement.setString(12, creditNotesVo.getDiscLedgerName());
			preparedStatement.setString(13, creditNotesVo.getDiscountValue());
			preparedStatement.setString(14, creditNotesVo.getDiscountAccountLevel());
			preparedStatement.setString(15, creditNotesVo.getAdjValue());
			preparedStatement.setInt(16, creditNotesVo.getAdjLedgerId());
			preparedStatement.setString(17, creditNotesVo.getAdjLedgerName());
			preparedStatement.setString(18, creditNotesVo.getAdjAccountLevel());
			preparedStatement.setString(19, creditNotesVo.getTotal());
			preparedStatement.setString(20, creditNotesVo.getStatus());
			preparedStatement.setTimestamp(21, new Timestamp(System.currentTimeMillis()));
			preparedStatement.setString(22, creditNotesVo.getUserId());
			preparedStatement.setInt(23, creditNotesVo.getOrganizationId());
			preparedStatement.setBoolean(24, creditNotesVo.getIsSuperAdmin());
			preparedStatement.setString(25, creditNotesVo.getRoleName());
			preparedStatement.setString(26, creditNotesVo.getTotal());
			preparedStatement.setString(27, creditNotesVo.getNote());
			preparedStatement.setInt(28, creditNotesVo.getReasonId());
			String exchangeRate=creditNotesVo.getExchangeRate();
			String crediNoteType=creditNoteTypeId>0?financeCommonDao.getCreditnoteTypeById(creditNoteTypeId).getName():"";
			if(crediNoteType!=null && crediNoteType.equalsIgnoreCase("Local")) {
				exchangeRate="1";//If it is local currency it should be 1
			}
			preparedStatement.setString(29, exchangeRate);
			ObjectMapper mapper = new ObjectMapper();
			String newJsonData =creditNotesVo.getGeneralLedgerData()!=null ? mapper.writeValueAsString(creditNotesVo.getGeneralLedgerData()) : null;
			logger.info("newJsonData"+newJsonData);
			preparedStatement.setString(30, newJsonData);
			int rowAffected = preparedStatement.executeUpdate();
			if (rowAffected == 1) {
				rs = preparedStatement.getGeneratedKeys();
				while (rs.next()) {
					creditNotesVo.setId(rs.getInt(1));
				}
			}

		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, null);
		}
		return creditNotesVo;
	}

	public CreditNotesVo updateCreditNotes(CreditNotesVo creditNotesVo) throws ApplicationException {
		logger.info("Entry into method: updateCreditNotes");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection connection = null;

		try {
			if (creditNotesVo != null && creditNotesVo.getId() > 0) {
				connection = getAccountsReceivableConnection();
				connection.setAutoCommit(false);
				for (Integer id : creditNotesVo.getItemsToRemove()) {// IF deleted
					logger.info("Deleting Credit note Item:" + id);
					deleteCreditNoteItemById(id, connection);
					productInventoryDao.deleteCrediNotesProductToInventoryMgmt(id, creditNotesVo);
				}
				updateCreditNotesInfo(creditNotesVo, connection);
				String invoiceNo = arInvoiceDao.getInvoiceOrderNo(creditNotesVo.getOriginalInvoiceId()!=null ? Integer.valueOf(creditNotesVo.getOriginalInvoiceId()) : 0 , creditNotesVo.getOrganizationId(), connection);
				creditNotesVo.setOriginalInvoiceNumber(invoiceNo.replace("~", "/"));
				if (creditNotesVo.getProducts() != null) {
					for (ArInvoiceProductVo product : creditNotesVo.getProducts()) {
						if (product.getStatus() != null) {
							switch (product.getStatus().toUpperCase()) {
							case CommonConstants.STATUS_AS_NEW:// If Newly Added
								Integer productId = createProduct(product, connection, creditNotesVo.getId());
								createTaxDetailsForProduct(productId, product, connection);
								productInventoryDao.addCreditNoteProductToInventoryMgmt(product, creditNotesVo);
								break;
							case CommonConstants.STATUS_AS_ACTIVE:// If it is existing
								boolean updateStatus = updateProduct(product, connection, creditNotesVo.getId());
								if (updateStatus)
									updateTaxDetailsForProduct(product.getId(),ArInvoiceConstants.MODULE_TYPE_AR_CREDITNOTE, product, connection);
									productInventoryDao.updateCreditNoteProductToInventoryMgmt(product, creditNotesVo);
								break;
							case CommonConstants.STATUS_AS_DELETE:
								changeStatusForCreditNoteItems(product.getId(), CommonConstants.STATUS_AS_DELETE,
										connection, CreditNoteConstants.MODIFY_CN_PRODUCT_STATUS_FOR_PRODUCT_ID);
								deleteTaxDetails(product.getId(), ArInvoiceConstants.MODULE_TYPE_AR_CREDITNOTE,
										connection);
								productInventoryDao.deleteCrediNotesProductToInventoryMgmt(product.getId(), creditNotesVo);
								break;
							}

						}
					}
				}
				if (creditNotesVo.getAttachments() != null && creditNotesVo.getAttachments().size() > 0) {
					attachmentsDao.createAttachments(creditNotesVo.getOrganizationId(), creditNotesVo.getUserId(),
							creditNotesVo.getAttachments(),
							AttachmentsConstants.MODULE_TYPE_ACCOUNTS_RECEIVABLES_CREDIT_NOTE, creditNotesVo.getId(),
							creditNotesVo.getRoleName());
				}

				if (creditNotesVo.getAttachmentsToRemove() != null
						&& creditNotesVo.getAttachmentsToRemove().size() > 0) {
					for (Integer attachmentId : creditNotesVo.getAttachmentsToRemove()) {
						attachmentsDao.changeStatusForSingleAttachment(attachmentId, CommonConstants.STATUS_AS_DELETE,
								creditNotesVo.getUserId(), creditNotesVo.getRoleName());
					}
				}

				connection.commit();
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, connection);
		}
		return creditNotesVo;
	}

	private void changeStatusForCreditNoteItems(Integer id, String status, Connection connection, String query)
			throws ApplicationException {
		logger.info("To Change the status with query :: " + query);
		if (query != null) {
			PreparedStatement preparedStatement =null;
			try{
				preparedStatement = connection.prepareStatement(query);
				preparedStatement.setString(1, status);
				preparedStatement.setDate(2, new Date(System.currentTimeMillis()));
				preparedStatement.setInt(3, id);
				preparedStatement.executeUpdate();
				logger.info("Status chaneged successfully ");
			} catch (Exception e) {
				logger.info("Error in changeStatusForInvoiceTables ", e);
				throw new ApplicationException(e);
			} finally {
				closeResources(null, preparedStatement, null);
			}
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


	private void deleteTaxDetails(Integer productId, String module, Connection connection) throws ApplicationException {
		logger.info("To delete into table ar_tax_details ");
		PreparedStatement preparedStatement =null;
		try  {
			preparedStatement = connection.prepareStatement(ArInvoiceConstants.MODIFY_TAX_DETAILS_STATUS);
			preparedStatement.setDate(1, new Date(System.currentTimeMillis()));
			preparedStatement.setString(2, CommonConstants.STATUS_AS_DELETE);
			preparedStatement.setString(3, module);
			preparedStatement.setInt(4, productId);
			preparedStatement.execute();
		} catch (Exception e) {
			logger.info("Error in deleteTaxDetails to table ar_tax_details ", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(null, preparedStatement, null);
		}
	}

	private void updateCreditNotesInfo(CreditNotesVo creditNotesVo, Connection connection) throws ApplicationException {
		logger.info("Entry into updateCreditNotesInfo ");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			if (creditNotesVo != null && creditNotesVo.getId() > 0) {
				preparedStatement = connection.prepareStatement(CreditNoteConstants.UPDATE_CREDIT_NOTES_ORGANIZATION,
						Statement.RETURN_GENERATED_KEYS);
				StringBuilder creditNoteNumberBuilder = new StringBuilder();
				creditNoteNumberBuilder
				.append(creditNotesVo.getCreditNoteNumberPrefix() != null
				? creditNotesVo.getCreditNoteNumberPrefix()
						: " ")
				.append("/").append(creditNotesVo.getCreditNoteNumber()).append("/")
				.append(creditNotesVo.getCreditNoteNumberSuffix() != null
				? creditNotesVo.getCreditNoteNumberSuffix()
						: " ");
				String creditNoteNumber = creditNoteNumberBuilder.toString();
				logger.info("creditNoteNumber" + creditNoteNumber);
				boolean isCredtnoteExist = checkCreditNotesExist(creditNoteNumber, creditNotesVo, connection);
				if (isCredtnoteExist) {
					throw new ApplicationException("Credit note number already exist for the organization");
				}

				preparedStatement.setInt(1, creditNotesVo.getCustomerId());
				preparedStatement.setString(2, creditNotesVo.getOriginalInvoiceId());
				int creditNoteTypeId=creditNotesVo.getCreditNoteTypeId();
				preparedStatement.setInt(3, creditNoteTypeId);
				preparedStatement.setString(4, creditNoteNumber);
				preparedStatement.setString(5, creditNotesVo.getCreditNoteDate() != null
						? DateConverter.getInstance().correctDatePickerDateToString(creditNotesVo.getCreditNoteDate())
								: null);
				preparedStatement.setString(6, creditNotesVo.getReason());
				preparedStatement.setString(7, creditNotesVo.getTnc());
				preparedStatement.setString(8, creditNotesVo.getSubTotal());
				preparedStatement.setInt(9, creditNotesVo.getTdsId());
				preparedStatement.setString(10, creditNotesVo.getTdsValue());
				preparedStatement.setInt(11, creditNotesVo.getDiscLedgerId());
				preparedStatement.setString(12, creditNotesVo.getDiscLedgerName());
				preparedStatement.setString(13, creditNotesVo.getDiscountValue());
				preparedStatement.setString(14, creditNotesVo.getDiscountAccountLevel());
				preparedStatement.setString(15, creditNotesVo.getAdjValue());
				preparedStatement.setInt(16, creditNotesVo.getAdjLedgerId());
				preparedStatement.setString(17, creditNotesVo.getAdjLedgerName());
				preparedStatement.setString(18, creditNotesVo.getAdjAccountLevel());
				preparedStatement.setString(19, creditNotesVo.getTotal());
				preparedStatement.setString(20, creditNotesVo.getStatus());
				preparedStatement.setTimestamp(21, new Timestamp(System.currentTimeMillis()));
				preparedStatement.setString(22, creditNotesVo.getUserId());
				preparedStatement.setInt(23, creditNotesVo.getOrganizationId());
				preparedStatement.setBoolean(24, creditNotesVo.getIsSuperAdmin());
				preparedStatement.setString(25, creditNotesVo.getRoleName());
				preparedStatement.setString(26, creditNotesVo.getTotal());
				preparedStatement.setString(27, creditNotesVo.getNote());
				preparedStatement.setInt(28, creditNotesVo.getReasonId());
				String exchangeRate=creditNotesVo.getExchangeRate();
				String crediNoteType=creditNoteTypeId>0?financeCommonDao.getCreditnoteTypeById(creditNoteTypeId).getName():"";
				if(crediNoteType!=null && crediNoteType.equalsIgnoreCase("Local")) {
					exchangeRate="1";//If it is local currency it should be 1
				}
				preparedStatement.setString(29, exchangeRate);
				ObjectMapper mapper = new ObjectMapper();
				String newJsonData =creditNotesVo.getGeneralLedgerData()!=null ? mapper.writeValueAsString(creditNotesVo.getGeneralLedgerData()) : null;
				logger.info("newJsonData"+newJsonData);
				preparedStatement.setString(30, newJsonData);
				preparedStatement.setInt(31, creditNotesVo.getId());
				preparedStatement.executeUpdate();
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, null);
		}

	}

	private boolean updateProduct(ArInvoiceProductVo product, Connection connection, Integer creditNoteId)
			throws ApplicationException {
		logger.info("Entry To updateProduct " + product);
		boolean result = false;
		if (product != null && creditNoteId != null) {
			PreparedStatement preparedStatement =null;
			try  {
				preparedStatement = connection.prepareStatement(CreditNoteConstants.UPDATE_CREDIT_NOTES_ITEMS, Statement.RETURN_GENERATED_KEYS);
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
				preparedStatement.setInt(15, creditNoteId);
				preparedStatement.setDouble(16, product.getNetAmount());
				preparedStatement.setDouble(17,product.getBaseUnitPrice()!=null ? product.getBaseUnitPrice() : 0.00);
				preparedStatement.setString(18, product.getDescription());
				preparedStatement.setString(19, product.getBaseQuantity());
				preparedStatement.setInt(20, product.getTempId()!=null ? product.getTempId() : 0);
				preparedStatement.setInt(21, product.getId());
				preparedStatement.executeUpdate();
				result = true;
				logger.info("Successfully Update credit note item with Id ::" + product.getId());
			} catch (Exception e) {

				logger.info("Error in inserting to table ar_invoice_items ", e);
				throw new ApplicationException(e);
			} finally {
				closeResources(null, preparedStatement, null);
			}
		}
		return result;
	}

	public CreditNotesVo deleteCreditNoteItemById(int id, Connection connection) throws ApplicationException {
		logger.info("Entry into method: deleteCreditNoteItemById");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;

		CreditNotesVo creditNotesVo = new CreditNotesVo();
		try {
			String sql = CreditNoteConstants.DELETE_CREDIT_NOTE_ITEM_BY_ID;
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, id);
			creditNotesVo.setId(id);
			preparedStatement.executeUpdate();

		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, null);
		}
		return creditNotesVo;
	}

	public CreditNotesVo deleteCreditNotes(int creditNoteId, String status, String userId, String roleName)
			throws ApplicationException {
		logger.info("Entry into method: deleteCreditNote");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection connection = null;

		CreditNotesVo creditNotesVo = new CreditNotesVo();
		try {
			connection = getAccountsReceivableConnection();
			String sql = CreditNoteConstants.CHANGE_CREDIT_NOTE_STATUS_ORGANIZATION;
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, status);
			preparedStatement.setDate(2, new Date(System.currentTimeMillis()));
			preparedStatement.setString(3, userId);
			preparedStatement.setString(4, roleName);
			preparedStatement.setInt(5, creditNoteId);
			preparedStatement.executeUpdate();
			creditNotesVo.setId(creditNoteId);
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, connection);
		}
		return creditNotesVo;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<CreditNotesVo> getAllCreditNotesByFilter(ArInvoiceFilterVo filterVo, String userId, String roleName)
			throws ApplicationException {
		logger.info("Entry into method: getAllCreditNotessOfAnOrganization");
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		List<CreditNotesVo> listCreditNotes = new ArrayList<CreditNotesVo>();
		CommonVo vo = financeCommonDao.getRefundTypeByName(RefundConstants.REFUND_TYPE_CREDIT_NOTES);
		int refundTypeId = 0;
		if (vo != null) {
			refundTypeId = vo.getId();
		}
		try {
			connection = getUserMgmConnection();
			String dateFormat = organizationDao.getDefaultDateForOrganization(filterVo.getOrgId(), connection);
			closeResources(null, null, connection);
			StringBuilder filterQuery = null;
			if (roleName.equals("Super Admin")) {
				filterQuery = new StringBuilder(CreditNoteConstants.GET_ALL_CREDIT_NOTES_FOR_ORGANIZATION);
			} else {
				filterQuery = new StringBuilder(CreditNoteConstants.GET_ALL_CREDIT_NOTES_FOR_ORGANIZATION_USER_ROLE);
			}
			List paramsList = new ArrayList<>();
			paramsList.add(filterVo.getOrgId());
			if (!(roleName.equals("Super Admin"))) {
				paramsList.add(userId);
				paramsList.add(roleName);
			}
			paramsList.add(CommonConstants.STATUS_AS_DELETE);
			if (filterVo.getStatus() != null) {
				filterQuery.append("and cn.status = ? ");
				paramsList.add(filterVo.getStatus());
			}
			if (filterVo.getFromAmount() != null) {
				filterQuery.append(" and cn.total >= ?");
				paramsList.add(filterVo.getFromAmount());
			}
			if (filterVo.getToAmount() != null) {
				filterQuery.append(" and cn.total <= ?");
				paramsList.add(filterVo.getToAmount());
			}
			if (filterVo.getCustomerId() != null) {
				filterQuery.append(" and cn.customer_id = ?");
				paramsList.add(filterVo.getCustomerId());
			}
			if (filterVo.getStartDate() != null) {
				if (filterVo.getEndDate() == null) {
					java.sql.Date date = new java.sql.Date(System.currentTimeMillis());
					filterVo.setEndDate(DateConverter.getInstance().getDatePickerDateFormat(date));
					logger.info("End date is null and so setting the sysdate::" + filterVo.getEndDate());
				}

				if (filterVo.getStartDate() != null && filterVo.getEndDate() != null) {
					filterQuery.append(" and cn.credit_note_date between ? and ? ");
					String fromdate = DateConverter.getInstance()
							.correctDatePickerDateToString(filterVo.getStartDate());
					logger.info("convertedFromDate::" + fromdate);
					paramsList.add(fromdate);
					String todate = DateConverter.getInstance().correctDatePickerDateToString(filterVo.getEndDate());
					logger.info("convertedEndDate::" + todate);
					paramsList.add(todate);
				}
			}
			filterQuery.append("  order by cn.id desc");
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
				CreditNotesVo creditNotesVo = new CreditNotesVo();
				creditNotesVo.setId(rs.getInt(1));
				creditNotesVo.setCustomerId(rs.getInt(2));
				creditNotesVo.setCustomerName(rs.getString(3));
				creditNotesVo
				.setOriginalInvoiceNumber(rs.getString(4) != null ? rs.getString(4).replace("~", "/") : null);
				creditNotesVo.setCreditNoteTypeId(rs.getInt(5));
				creditNotesVo.setCreditNoteNumber(rs.getString(6));
				creditNotesVo.setCreditNoteDate(rs.getDate(7) != null
						? DateConverter.getInstance().convertDateToGivenFormat(rs.getDate(7), dateFormat)
								: null);
				creditNotesVo.setReason(rs.getString(8));
				creditNotesVo.setTnc(rs.getString(9));
				creditNotesVo.setSubTotal(rs.getString(10));
				creditNotesVo.setTdsId(rs.getInt(11));
				creditNotesVo.setTdsValue(rs.getString(12));
				creditNotesVo.setDiscLedgerId(rs.getInt(13));
				creditNotesVo.setDiscLedgerName(rs.getString(14));
				creditNotesVo.setDiscountValue(rs.getString(15));
				creditNotesVo.setDiscountAccountLevel(rs.getString(16));
				creditNotesVo.setAdjValue(rs.getString(17));
				creditNotesVo.setAdjLedgerId(rs.getInt(18));
				creditNotesVo.setAdjLedgerName(rs.getString(19));
				creditNotesVo.setAdjAccountLevel(rs.getString(20));
				Integer invoiceId = rs.getInt(30);
				creditNotesVo.setOriginalInvoiceId(invoiceId + "");
				String currencySymbol = "";
				int digits = 0;
				if (invoiceId != null && invoiceId > 0) {
					CurrencyVo currencyVo = arInvoiceDao.getInvoiceCurrencyDetails(invoiceId);
					if( currencyVo != null ) {
						currencySymbol = currencyVo.getSymbol() != null
								? currencyVo.getSymbol() + " "
										: "";
						digits = currencyVo.getNoOfDecimalsForAmount();
						creditNotesVo.setCurrencyId(currencyVo.getId());
					}
				}

				Double balanceDue = rs.getDouble(22);
				Double total = rs.getDouble(21);
				if (total != null && balanceDue != null) {
					BigDecimal bd = new BigDecimal(balanceDue).setScale(digits, RoundingMode.HALF_UP);
					creditNotesVo.setBalanceDue(currencySymbol + bd.doubleValue());
					creditNotesVo.setAmount(bd.doubleValue()+"");
					BigDecimal totalValue = new BigDecimal(total).setScale(digits, RoundingMode.HALF_UP);
					creditNotesVo.setTotal(currencySymbol + totalValue.doubleValue());
				}
				// creditNotesVo.setTotal(currencySymbol+rs.getString(21));
				// creditNotesVo.setBalanceDue(currencySymbol+rs.getString(22));
				String status = rs.getString(23);
				String displayStatus = null;
				boolean isEditable = false;
				switch (status) {
				case CommonConstants.STATUS_AS_ACTIVE:
					isEditable = true;
					displayStatus = CommonConstants.DISPLAY_STATUS_AS_ACTIVE;
					break;
				case CommonConstants.STATUS_AS_DRAFT:
					isEditable = true;
					displayStatus = CommonConstants.DISPLAY_STATUS_AS_DRAFT;
					break;
				case CommonConstants.STATUS_AS_INACTIVE:
					displayStatus = CommonConstants.DISPLAY_STATUS_AS_INACTIVE;
					break;
				case CommonConstants.STATUS_AS_PENDING_FOR_APPROVAL:
					displayStatus = CommonConstants.DISPLAY_STATUS_AS_PENDING_FOR_APPROVAL;
					break;
				case CommonConstants.STATUS_AS_UN_ADJUSTED:
					displayStatus = CommonConstants.DISPLAY_STATUS_AS_UN_ADJUSTED;
					break;
				case CommonConstants.STATUS_AS_ADJUSTED:
					displayStatus = CommonConstants.DISPLAY_STATUS_AS_ADJUSTED;
					break;
				case CommonConstants.STATUS_AS_PARTIALLY_ADJUSTED:
					displayStatus = CommonConstants.DISPLAY_STATUS_AS_PARTIALLY_ADJUSTED;
					break;
				case CommonConstants.STATUS_AS_OPEN:
					isEditable = true;
					displayStatus = CommonConstants.DISPLAY_STATUS_AS_OPEN;
					break;
				case CommonConstants.STATUS_AS_VOID:
					displayStatus = CommonConstants.DISPLAY_STATUS_AS_VOID;
					break;

				}
				creditNotesVo.setStatus(displayStatus);
				creditNotesVo.setEditable(isEditable);
				creditNotesVo.setCreateTs(rs.getTimestamp(24));
				creditNotesVo.setUserId(rs.getString(25));
				creditNotesVo.setOrganizationId(rs.getInt(26));
				creditNotesVo.setIsSuperAdmin(rs.getBoolean(27));
				creditNotesVo.setRoleName(rs.getString(28));
				creditNotesVo.setNote(rs.getString(29));
				creditNotesVo.setRefundTypeId(refundTypeId);
				creditNotesVo.setPendingApprovalStatus(rs.getString(31));
				listCreditNotes.add(creditNotesVo);
			}
		} catch (Exception e) {
			logger.error("Error in get all Credit notes ", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, connection);
		}
		return listCreditNotes;
	}

	private boolean checkCreditNotesExist(String creditNoteNumber, CreditNotesVo creditNotesVo, Connection conn)
			throws ApplicationException {
		logger.info("Entry into method: checkCreditNotesExist");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {

			String query = CreditNoteConstants.CHECK_CREDIT_NOTES_EXISTS_FOR_ORGANIZATION;
			preparedStatement = conn.prepareStatement(query);
			preparedStatement.setInt(1, creditNotesVo.getOrganizationId());
			preparedStatement.setString(2, creditNoteNumber);
			rs = preparedStatement.executeQuery();
			if (rs.next()) {
				if (creditNotesVo.getId() == 0) {// create scenario
					return true;
				} else {
					if (creditNotesVo.getId() != rs.getInt(1)) {// If it is not same record
						return true;
					}
				}
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, null);
		}
		return false;
	}

	public CreditNotesVo getCreditNotesById(int creditNoteId) throws ApplicationException {
		logger.info("Entry into method: getCreditNotesById");
		CreditNotesVo creditNotesVo = null;
		if (creditNoteId > 0) {
			Connection con =null;
			try  {
				con = getAccountsReceivableConnection();
				creditNotesVo = new CreditNotesVo();
				creditNotesVo.setId(creditNoteId);
				getCreditNotesInfoById(creditNotesVo, con);
				List<ArInvoiceProductVo> products = getProductListForCreditNotes(creditNoteId, con);
				creditNotesVo.setProducts(products);
				List<UploadFileVo> uploadFileVos = new ArrayList<UploadFileVo>();
				for (AttachmentsVo attachments : attachmentsDao.getAttachmentDetails(creditNotesVo.getId(),
						AttachmentsConstants.MODULE_TYPE_ACCOUNTS_RECEIVABLES_CREDIT_NOTE)) {
					UploadFileVo uploadFileVo = new UploadFileVo();
					uploadFileVo.setId(attachments.getId());
					uploadFileVo.setName(attachments.getFileName());
					uploadFileVo.setSize(attachments.getSize());
					uploadFileVos.add(uploadFileVo);
				}
				creditNotesVo.setAttachments(uploadFileVos);
				logger.info("Succecssfully fetched Credit notes Details + " + creditNotesVo);
			} catch (Exception e) {
				logger.info("Error in getInvoiceById:: ", e);
				throw new ApplicationException(e);
			} finally {
				closeResources(null, null, con);
			}
		}
		return creditNotesVo;
	}

	private void getCreditNotesInfoById(CreditNotesVo creditNotesVo, Connection con) throws ApplicationException {
		PreparedStatement preparedStatement = null;
		ResultSet rs =null;
		try  {
			preparedStatement = con.prepareStatement(CreditNoteConstants.GET_CREDIT_NOTES_BY_ID);
			preparedStatement.setInt(1, creditNotesVo.getId());
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				// creditNotesVo = new CreditNotesVo();
				creditNotesVo.setId(rs.getInt(1));
				creditNotesVo.setCustomerId(rs.getInt(2));
				creditNotesVo.setOriginalInvoiceId(rs.getString(3));
				creditNotesVo.setCreditNoteTypeId(rs.getInt(4));
				if (rs.getString(5) != null) {
					String[] creditNoteNum = rs.getString(5).split("/");
					if (creditNoteNum.length > 1) {
						creditNotesVo.setCreditNoteNumberPrefix(creditNoteNum[0]!=null && creditNoteNum[0].trim().length()>0?creditNoteNum[0]:"");
						creditNotesVo.setCreditNoteNumber(creditNoteNum[1]);
						creditNotesVo.setCreditNoteNumberSuffix("");
						if(creditNoteNum.length > 2) {
						creditNotesVo.setCreditNoteNumberSuffix(creditNoteNum[2]!=null && creditNoteNum[2].trim().length()>0?creditNoteNum[2]:"");
						}
					}
				}
				int organizationId = rs.getInt(25);
				/*
				 * Connection userMgmtConnection=getUserMgmConnection(); String dateFormat =
				 * organizationDao.getDefaultDateForOrganization(organizationId,
				 * userMgmtConnection); closeResources(null,null,userMgmtConnection);
				 */
				creditNotesVo.setCreditNoteDate(rs.getDate(6) != null
						? DateConverter.getInstance().getDatePickerDateFormat(rs.getDate(6))
								: null);
				creditNotesVo.setReason(rs.getString(7));
				creditNotesVo.setTnc(rs.getString(8));
				creditNotesVo.setSubTotal(rs.getString(9));
				creditNotesVo.setTdsId(rs.getInt(10));
				creditNotesVo.setTdsValue(rs.getString(11));
				creditNotesVo.setDiscLedgerId(rs.getInt(12));
				creditNotesVo.setDiscLedgerName(rs.getString(13));
				creditNotesVo.setDiscountValue(rs.getString(14));
				creditNotesVo.setDiscountAccountLevel(rs.getString(15));
				creditNotesVo.setAdjValue(rs.getString(16));
				creditNotesVo.setAdjLedgerId(rs.getInt(17));
				creditNotesVo.setAdjLedgerName(rs.getString(18));
				creditNotesVo.setAdjAccountLevel(rs.getString(19));
				creditNotesVo.setTotal(rs.getString(20));
				creditNotesVo.setBalanceDue(rs.getString(21));
				creditNotesVo.setStatus(rs.getString(22));
				creditNotesVo.setCreateTs(rs.getTimestamp(23));
				creditNotesVo.setUserId(rs.getString(24));
				creditNotesVo.setOrganizationId(organizationId);
				creditNotesVo.setIsSuperAdmin(rs.getBoolean(26));
				creditNotesVo.setRoleName(rs.getString(27));
				creditNotesVo.setNote(rs.getString(28));
				creditNotesVo.setReasonId(rs.getInt(29));
				creditNotesVo.setExchangeRate(rs.getString(30));
				ObjectMapper mapper = new ObjectMapper();
				String json = rs.getString(31);
				if(json!=null) {
					GeneralLedgerVo gldata = mapper.readValue(json, GeneralLedgerVo.class);
					logger.info("Json map "+gldata);
					creditNotesVo.setGeneralLedgerData(gldata);
				}
			}
			logger.info("General Info:" + creditNotesVo);
		} catch (Exception e) {
			logger.error("Error in getInvoiceById:: ", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, null);
		}

	}

	private List<ArInvoiceProductVo> getProductListForCreditNotes(Integer creditNoteId, Connection con)
			throws ApplicationException {
		logger.info("To get  in getProductListForCreditNotes ");
		List<ArInvoiceProductVo> productVos = new ArrayList<ArInvoiceProductVo>();
		PreparedStatement preparedStatement =null;
		ResultSet rs =null;
		try {
			preparedStatement = con.prepareStatement(CreditNoteConstants.GET_ITEMS_FOR_CREDIT_NOTE_ID);
			preparedStatement.setInt(1, creditNoteId);
			preparedStatement.setString(2, CommonConstants.STATUS_AS_DELETE);
			rs = preparedStatement.executeQuery();
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
				productVo.setBaseQuantity(rs.getString(21));
				productVo.setTempId(rs.getInt(22));
				productVo.setTaxDetails(getTaxDetails(rs.getInt(1), con));
				logger.info(productVo);
				productVos.add(productVo);
			}
			logger.info("Successfully fetched  getProductListForCreditNotes " + productVos);
		} catch (Exception e) {
			logger.info("Error in getProductListForCreditNotes ", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, null);
		}
		return productVos;
	}

	private ArInvoiceTaxDetailsVo getTaxDetails(Integer productId, Connection con) throws ApplicationException {
		logger.info("To get  in getTaxDetails for prodId:" + productId);
		ArInvoiceTaxDetailsVo taxDetailsVo = null;
		PreparedStatement preparedStatement =null;
		ResultSet rs =null;
		try {
			preparedStatement = con.prepareStatement(ArInvoiceConstants.GET_TAX_DETAILS_BY_ITEM_ID);
			preparedStatement.setInt(1, productId);
			preparedStatement.setString(2, ArInvoiceConstants.MODULE_TYPE_AR_CREDITNOTE);
			taxDetailsVo = new ArInvoiceTaxDetailsVo();
			List<ArInvoiceTaxDistributionVo> distributionVos = new ArrayList<ArInvoiceTaxDistributionVo>();
			rs = preparedStatement.executeQuery();
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
			taxDetailsVo.setTaxDistribution(distributionVos);
			logger.info("Successfully fetched  getTaxDetails " + taxDetailsVo);
		} catch (Exception e) {
			logger.info("Error in getTaxDetails ", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, null);
		}
		return taxDetailsVo;
	}

	private Integer createProduct(ArInvoiceProductVo product, Connection connection, Integer creditNoteId)
			throws ApplicationException {
		logger.info("To insert into table ar_invoice_items " + product);
		Integer generatedId = null;
		if (product != null && creditNoteId != null) {
			PreparedStatement preparedStatement =null;
			ResultSet rs = null;
			try {
				preparedStatement = connection.prepareStatement(CreditNoteConstants.INSERT_INTO_CREDIT_NOTES_ITEMS, Statement.RETURN_GENERATED_KEYS); 
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
				preparedStatement.setInt(15, creditNoteId);
				preparedStatement.setDouble(16, product.getNetAmount());
				preparedStatement.setDouble(17, product.getBaseUnitPrice()!=null ? product.getBaseUnitPrice() : 0.00);
				preparedStatement.setString(18, product.getDescription());
				preparedStatement.setString(19, product.getBaseQuantity());
				preparedStatement.setInt(20, product.getTempId()!=null ? product.getTempId() : 0);
				int rowAffected = preparedStatement.executeUpdate();
				if (rowAffected == 1) {
					rs = preparedStatement.getGeneratedKeys();
					while (rs.next()) {
						generatedId = rs.getInt(1);
					}
				}
				logger.info("Successfully inserted into table ar_invoice_items with Id ::" + generatedId);
			} catch (Exception e) {
				logger.info("Error in inserting to table ar_invoice_items ", e);
				throw new ApplicationException(e);
			} finally {
				closeResources(rs, preparedStatement, null);
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
			PreparedStatement preparedStatement = null;
			for (ArInvoiceTaxDistributionVo distributionVo : product.getTaxDetails().getTaxDistribution()) {
				try  {
					preparedStatement = connection.prepareStatement(ArInvoiceConstants.INSERT_INTO_TAX_DETAILS);
					preparedStatement.setString(1, ArInvoiceConstants.MODULE_TYPE_AR_CREDITNOTE);
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
				} finally {
					closeResources(null, preparedStatement, null);
				}
			}
		}

	}

	public Map<String, Integer> getMaxAmountForOrg(Integer organizationId) throws ApplicationException {
		logger.info("To getMinMaxValuesOfOrganisation");
		Connection connection = null;
		Map<String, Integer> maxAmountMap = null;
		PreparedStatement preparedStatement =null;
		ResultSet rs=null;
		if (organizationId != null) {
			try {
				connection = getAccountsReceivableConnection();
				preparedStatement = connection.prepareStatement(CreditNoteConstants.GET_MAX_AMOUNT_FOR_ORG);
				preparedStatement.setInt(1, organizationId);
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

	public String getcreditNotesAmountForCustomerCurrency(int orgId,int customerId,int currencyId, Connection con) throws ApplicationException {
		logger.info("To get the getcreditNotesAmountForCustomerCurrency");
		String totalAmount = null;
		PreparedStatement preparedStatement =null;
		ResultSet rs =null;
		try {
			preparedStatement = con.prepareStatement(CreditNoteConstants.GET_TOTAL_CREDIT_NOTES_AMOUNT_FOR_CUSTOMER_CURRENCY);
			preparedStatement.setInt(1, orgId);
			preparedStatement.setInt(2, customerId);
			preparedStatement.setInt(3, currencyId);
			preparedStatement.setString(4, CommonConstants.STATUS_AS_ACTIVE);
			preparedStatement.setString(5, CommonConstants.STATUS_AS_OPEN);			
			preparedStatement.setString(6, CommonConstants.STATUS_AS_PARTIALLY_ADJUSTED);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				CurrencyVo currencyVo = currencyDao.getCurrency(currencyId);
				if (currencyVo != null) {

					BigDecimal bd = new BigDecimal(rs.getDouble(1))
							.setScale(currencyVo.getNoOfDecimalsForAmount(), RoundingMode.HALF_UP);
					totalAmount=bd.doubleValue() + "";
				}
			}
		} catch (Exception e) {
			logger.info("Error in getcreditNotesAmountForCustomerCurrency", e);
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, null);
		}
		return totalAmount;
	}


	public String getcreditNotesDueAmountForCustomer(int orgId,int customerId, Connection con) throws ApplicationException {
		logger.info("To get the getcreditNotesAmountForCustomer");
		String totalAmount = null;
		ResultSet rs =null;
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = con.prepareStatement(CreditNoteConstants.GET_TOTAL_CREDIT_NOTES_AMOUNT_FOR_CUSTOMER);
			preparedStatement.setInt(1, orgId);
			preparedStatement.setInt(2, customerId);
			preparedStatement.setString(3, CommonConstants.STATUS_AS_ACTIVE);
			preparedStatement.setString(4, CommonConstants.STATUS_AS_OPEN);			
			preparedStatement.setString(5, CommonConstants.STATUS_AS_PARTIALLY_ADJUSTED);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {

				totalAmount=rs.getString(1);
			}
		} catch (Exception e) {
			logger.info("Error in getcreditNotesAmountForCustomer", e);
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, null);
		}
		return totalAmount;
	}

	public int getInvoiceForCreditNote(int creditNoteId) throws ApplicationException {
		logger.info("To get the getInvoiceForCreditNote");
		int invoiceId = 0;
		Connection connection = null;		
		ResultSet rs =null;
		PreparedStatement preparedStatement=null;
		try {
			connection = getAccountsReceivableConnection();
			preparedStatement = connection.prepareStatement(CreditNoteConstants.GET_INVOICE_FOR_CREDIT_NOTES);
			preparedStatement.setInt(1, creditNoteId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				invoiceId = rs.getInt(1);
			}
		} catch (Exception e) {
			logger.info("Error in getInvoiceForCreditNote", e);
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, connection);
		}
		return invoiceId;
	}

	public List<CreditNotesDetailsVo> getCreditNotesDetailsByCustomerId(int organizationId,int customerId,int currencyId, Connection con)
			throws ApplicationException {
		logger.info("To get the getCreditNotesDetailsByCustomerId");
		List<CreditNotesDetailsVo> listCreditDetails = new ArrayList<CreditNotesDetailsVo>();
		PreparedStatement preparedStatement = null;
		ResultSet rs =null;
		try  {
			preparedStatement = con.prepareStatement(CreditNoteConstants.GET_CREDIT_DETAILS_FOR_CUSTOMER);
			preparedStatement.setInt(1, organizationId);
			preparedStatement.setInt(2, customerId);
			preparedStatement.setInt(3, currencyId);
			preparedStatement.setString(4, CommonConstants.STATUS_AS_ACTIVE);
			preparedStatement.setString(5, CommonConstants.STATUS_AS_OPEN);			
			preparedStatement.setString(6, CommonConstants.STATUS_AS_PARTIALLY_ADJUSTED);
			rs = preparedStatement.executeQuery();
			logger.info("Query"+preparedStatement.toString());
			while (rs.next()) {
				//id,credit_note_number,total,balance_due
				CreditNotesDetailsVo creditNotesDetailsVo = new CreditNotesDetailsVo();
				creditNotesDetailsVo.setId(rs.getInt(1));
				creditNotesDetailsVo.setValue(rs.getInt(1));
				creditNotesDetailsVo.setReferenceNo(rs.getString(2));
				creditNotesDetailsVo.setName(rs.getString(2));
				creditNotesDetailsVo.setOriginalAmount(rs.getString(3));
				CurrencyVo currencyVo = currencyDao.getCurrency(currencyId);
				if (currencyVo != null) {
					BigDecimal bd = new BigDecimal(rs.getDouble(4))
							.setScale(currencyVo.getNoOfDecimalsForAmount(), RoundingMode.HALF_UP);
					creditNotesDetailsVo.setAvailableAmount(bd.doubleValue() + "");
				}


				creditNotesDetailsVo.setType("Credit Notes");
				listCreditDetails.add(creditNotesDetailsVo);
			}
		} catch (Exception e) {
			logger.info("Error in getCreditNotesDetailsByCustomerId", e);
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, null);
		}
		return listCreditDetails;
	}

	public WorkflowInvoiceVo getWorkflowRequiredDataForCreditNotebyId(Integer invoiceId) throws ApplicationException {
		logger.info("To  getWorkflowRequiredDataForCreditNotebyId for:::" + invoiceId);
		WorkflowInvoiceVo invoiceVo = null;
		Connection connection = null;
		PreparedStatement preparedStatement=null;
		ResultSet rs=null;
		if (invoiceId != null) {
			try {
				connection = getAccountsReceivableConnection();
				preparedStatement=connection.prepareStatement(CreditNoteConstants.GET_WORKFLOW_DATA_FOR_CN);
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
					invoiceVo.setOriginalInvoiceId(rs.getInt(8));
					int currencyId=rs.getInt(9);
					if(currencyId>0) {
					CurrencyVo currencyVo=currencyDao.getCurrency(currencyId);
					invoiceVo.setCurrencySymbol(currencyVo.getSymbol()!=null?currencyVo.getSymbol():"");
					}
				}
				logger.info("getWorkflowRequiredDataForCreditNotebyId:Succecssfully fetched invoice in ARInvoiceDao + " + invoiceVo);
			} catch (Exception e) {
				logger.info("Error in getWorkflowRequiredDataForCreditNotebyId:: ", e);
				throw new ApplicationException(e);
			}finally {
				closeResources(rs, preparedStatement, connection);
			}
		}
		return invoiceVo;

	}

	public boolean updateCurrentWorkflowRule(int creditNoteId,int currentRuleId,String status,String pendingApprovalStatus) throws ApplicationException {
		logger.info("Entry into updateCurrentWorkflowRule :creditNoteId:"+creditNoteId+",currentRuleId"+currentRuleId+",status:"+status+",pendingApprovalStatus+"+pendingApprovalStatus);
		Connection con = null;
		PreparedStatement preparedStatement =null;
		try {
			con = getAccountsReceivableConnection();
			preparedStatement = con.prepareStatement(CreditNoteConstants.UPDATE_CURRENT_RULE);
			preparedStatement.setInt(1, currentRuleId);
			preparedStatement.setString(2, status);
			preparedStatement.setString(3,pendingApprovalStatus);
			preparedStatement.setInt(4, creditNoteId);
			preparedStatement.executeUpdate();
		} catch (Exception e) {
			logger.info("Error in updateCurrentWorkflowRule ", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(null, preparedStatement, con);			
		}
		return false;

	}

	
	
	public String getCreditNoteNumber( int creditNoteId)
			throws ApplicationException {
		logger.info("To get the getCreditNoteNumber");
		 Connection con = null;
 		String creditNoteNo = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			con = getAccountsReceivableConnection();
			preparedStatement = con.prepareStatement(CreditNoteConstants.GET_CREDITNOTE_NUMBER);
			preparedStatement.setInt(1, creditNoteId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				creditNoteNo = rs.getString(1);
			}

		} catch (Exception e) {
			logger.info("Error in getCreditNoteNumber", e);
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, con);
		}

		return creditNoteNo;
	}

	public List<ArInvoiceVo> getInvoicesForCreditNotes(int organizationId, int customerId,int creditNoteId)
			throws ApplicationException, SQLException {
		logger.info("Entry into method: getInvoicesForCreditNotes");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		List<ArInvoiceVo> invoicesList = new ArrayList<ArInvoiceVo>();
		try {
			con = getAccountsReceivableConnection();
			StringBuilder sqlQuery = new StringBuilder(ArInvoiceConstants.GET_ALL_CREDITNOTE_ELIGIBLE_INVOICES_FOR_ORG);
			WorkflowInvoiceVo basicCreditNoteVo=null;
			if(creditNoteId>0) {
			basicCreditNoteVo=getWorkflowRequiredDataForCreditNotebyId(creditNoteId);
			}
			preparedStatement = con.prepareStatement(sqlQuery.toString());
			preparedStatement.setInt(1, organizationId);
			preparedStatement.setInt(2, customerId);
			preparedStatement.setString(3, CommonConstants.STATUS_AS_ACTIVE);
			preparedStatement.setString(4, CommonConstants.STATUS_AS_OPEN);
			preparedStatement.setString(5, CommonConstants.STATUS_AS_OVERDUE);
			preparedStatement.setString(6, CommonConstants.STATUS_AS_UNPAID);
			preparedStatement.setString(7, CommonConstants.STATUS_AS_PARTIALLY_PAID);
			preparedStatement.setString(8, CommonConstants.STATUS_AS_PAID);
			rs = preparedStatement.executeQuery();

			while (rs.next()) {

				Integer invoiceId = rs.getInt(1);
				if (invoiceId != null && invoiceId.intValue() > 0) {
					ArInvoiceVo arInvoiceVo = arInvoiceDao.getInvoiceById(invoiceId);
					if (arInvoiceVo != null) {
						ArInvoiceGeneralInformationVo arInvoiceGeneralInformationVo = arInvoiceVo
								.getGeneralInformation();
						
						if (arInvoiceGeneralInformationVo != null) {
							CurrencyVo currency=currencyDao.getCurrency(arInvoiceGeneralInformationVo.getCurrencyId());
							if(basicCreditNoteVo!=null && currency!=null && basicCreditNoteVo.getOriginalInvoiceId().equals(invoiceId)) {

								BigDecimal orginalDue=arInvoiceGeneralInformationVo.getBalanceDue()!=null?new BigDecimal(arInvoiceGeneralInformationVo.getBalanceDue()):BigDecimal.ZERO;
								BigDecimal cnTotal=basicCreditNoteVo.getInvoiceValue()!=null?new BigDecimal(basicCreditNoteVo.getInvoiceValue()):BigDecimal.ZERO;
								BigDecimal due=orginalDue.add(cnTotal).setScale(currency.getNoOfDecimalsForAmount(), RoundingMode.HALF_UP);
								arInvoiceGeneralInformationVo.setBalanceDue(due.doubleValue());
							}
							// Setting Supply Service Name
							CommonVo supplyService = financeCommonDao
									.getSupplyServicesById(arInvoiceGeneralInformationVo.getSupplyServiceId());
							if (supplyService != null) {
								arInvoiceGeneralInformationVo.setSupplyServiceName(supplyService.getName());
							}
							// Set Currency
							arInvoiceGeneralInformationVo.setCurrency(currency);

							invoicesList.add(arInvoiceVo);
						}
					}

				}
			}
		} catch (Exception e) {
			logger.info("Error in getInvoicesForCreditNotes:: ", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);

		}

		return invoicesList;
	}

	
}
