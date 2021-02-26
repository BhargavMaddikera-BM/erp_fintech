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
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.DateConverter;
import com.blackstrawai.ap.creditnote.VendorCreditNoteDetailsVo;
import com.blackstrawai.ap.dropdowns.BasicCurrencyVo;
import com.blackstrawai.ar.applycredits.CreditNotesDetailsVo;
import com.blackstrawai.ar.applycredits.ReceiptsDetailsVo;
import com.blackstrawai.ar.dropdowns.BasicInvoiceDetailsVo;
import com.blackstrawai.ar.dropdowns.ReceiptBulkDropdownVo;
import com.blackstrawai.ar.receipt.ReceiptBulkDetailsVo;
import com.blackstrawai.ar.receipt.ReceiptCommonVo;
import com.blackstrawai.ar.receipt.ReceiptSettingsVo;
import com.blackstrawai.ar.receipt.ReceiptVo;
import com.blackstrawai.ar.receipt.VendorRefundReceiptDetailsVo;
import com.blackstrawai.attachments.AttachmentsConstants;
import com.blackstrawai.attachments.AttachmentsDao;
import com.blackstrawai.attachments.AttachmentsVo;
import com.blackstrawai.attachments.UploadFileVo;
import com.blackstrawai.common.BaseDao;
import com.blackstrawai.common.CommonConstants;
import com.blackstrawai.common.FinanceCommonDao;
import com.blackstrawai.journals.GeneralLedgerVo;
import com.blackstrawai.onboarding.OrganizationDao;
import com.blackstrawai.settings.CommonVo;
import com.blackstrawai.settings.CurrencyDao;
import com.blackstrawai.settings.CurrencyVo;
import com.fasterxml.jackson.databind.ObjectMapper;

@Repository
public class ReceiptDao extends BaseDao {

	private Logger logger = Logger.getLogger(ReceiptDao.class);

	@Autowired
	private OrganizationDao organizationDao;

	@Autowired
	private FinanceCommonDao financeCommonDao;

	@Autowired
	private AttachmentsDao attachmentsDao;

	@Autowired
	private CurrencyDao currencyDao;

	/*@Autowired
	private CreditNotesDao creditNotesDao;*/

	public ReceiptVo createReceipt(ReceiptVo receiptVo) throws ApplicationException, SQLException {
		logger.info("Entry into method: createReceipt " + receiptVo);
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection connection = null;
		try {
			connection = getAccountsReceivableConnection();
			connection.setAutoCommit(false);
			String receiptNo = (receiptVo.getReceiptNoPrefix() != null ? receiptVo.getReceiptNoPrefix() : "") + "/"
					+ receiptVo.getReceiptNo() + "/"
					+ (receiptVo.getReceiptNoSuffix() != null ? receiptVo.getReceiptNoSuffix() : "");

			logger.info(receiptNo);
			receiptVo.setReceiptNo(receiptNo);
			boolean isReceiptExist = checkReceiptExist(receiptVo, connection);
			if (isReceiptExist) {
				throw new Exception("Receipt Exist for the Organization");
			}

			String sql = ReceiptConstants.INSERT_INTO_RECEIPT_ORGANIZATION;
			preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setString(1, receiptVo.getReceiptType());
			preparedStatement.setString(2, receiptNo);
			preparedStatement.setString(3,
					receiptVo.getReceiptDate() != null
					? DateConverter.getInstance().correctDatePickerDateToString(receiptVo.getReceiptDate())
							: null);
			preparedStatement.setInt(4, receiptVo.getCustomerId() != null ? receiptVo.getCustomerId() : 0);
			preparedStatement.setInt(5, receiptVo.getVendorId() != null ? receiptVo.getVendorId() : 0);
			preparedStatement.setInt(6, receiptVo.getContactId() != null ? receiptVo.getContactId() : 0);
			preparedStatement.setString(7, receiptVo.getContactType());
			preparedStatement.setString(8, receiptVo.getBankId());
			preparedStatement.setString(9, receiptVo.getBankType());
			preparedStatement.setInt(10, receiptVo.getCurrencyId());
			preparedStatement.setString(11, receiptVo.getAmount());
			preparedStatement.setString(12, receiptVo.getTotal());
			preparedStatement.setInt(13, receiptVo.getOrganizationId());
			preparedStatement.setBoolean(14, receiptVo.getIsBulk());
			List<ReceiptSettingsVo> settingsData = receiptVo.getCustomTableList();
			ObjectMapper mapper = new ObjectMapper();
			String newJsonData = mapper.writeValueAsString(settingsData);
			logger.info("settingsData:" + newJsonData);
			preparedStatement.setString(15, newJsonData);
			preparedStatement.setString(16, receiptVo.getNotes());
			preparedStatement.setString(17, receiptVo.getUserId());
			preparedStatement.setString(18, receiptVo.getRoleName());
			preparedStatement.setString(19, receiptVo.getExchangeRate());
			preparedStatement.setBoolean(20, receiptVo.getAddNotes());
			preparedStatement.setString(21, receiptVo.getStatus());
			preparedStatement.setString(22, receiptVo.getAdjustedAmount());
			preparedStatement.setString(23, receiptVo.getDifferenceAmount());
			preparedStatement.setBoolean(24, receiptVo.getRecordBillDetails());
			String glData =receiptVo.getGeneralLedgerData()!=null ? mapper.writeValueAsString(receiptVo.getGeneralLedgerData()) : null;
			logger.info("glData>> "+glData);
			preparedStatement.setString(25, glData);

			int rowAffected = preparedStatement.executeUpdate();
			if (rowAffected == 1) {
				rs = preparedStatement.getGeneratedKeys();

				if (rs.next()) {
					receiptVo.setId(rs.getInt(1));
				}
			}
			// Setting Child objects,
			if (receiptVo.getReceiptBulkDetails() != null && receiptVo.getReceiptBulkDetails().size() > 0) {
				for (ReceiptBulkDetailsVo invoiceCategoryReceiptVo : receiptVo.getReceiptBulkDetails()) {
					invoiceCategoryReceiptVo.setReceiptId(receiptVo.getId());
					invoiceCategoryReceiptVo.setStatus(CommonConstants.STATUS_AS_ACTIVE);
					createReceiptBulkDetails(invoiceCategoryReceiptVo,receiptVo, connection);
				}
			}

			if (receiptVo.getVendorReceiptDetails() != null && receiptVo.getVendorReceiptDetails().size() > 0) {
				for (VendorRefundReceiptDetailsVo vendorRefund : receiptVo.getVendorReceiptDetails()) {
					if (receiptVo.getId() != null && vendorRefund.getInvoiceId() != null) {
						createVendorRefundInvoiceDetails(receiptVo.getId(), vendorRefund, connection);
					}
				}
			}
			
			if(receiptVo.getVendorCreditNoteDetailsVos() != null && !CollectionUtils.isEmpty(receiptVo.getVendorCreditNoteDetailsVos())) {
				createVendorCreditNoteDetails(receiptVo.getId(), receiptVo.getVendorCreditNoteDetailsVos(), connection);
			}
			
			if (receiptVo.getId() > 0 && receiptVo.getAttachments() != null && receiptVo.getAttachments().size() > 0) {
				attachmentsDao.createAttachments(receiptVo.getOrganizationId(), receiptVo.getUserId(),
						receiptVo.getAttachments(), AttachmentsConstants.MODULE_TYPE_ACCOUNTS_RECEIVABLES_RECEIPTS,
						receiptVo.getId(), receiptVo.getRoleName());

			}
			connection.commit();

		} catch (Exception e) {
			logger.error("Error during createReceipt ", e);
			connection.rollback();
			logger.info("Before throw in dao :: " + e.getMessage());
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, connection);
		}
		return receiptVo;
	}

	private void createVendorCreditNoteDetails(Integer receiptId,List<VendorCreditNoteDetailsVo> vendorCreditNoteDetailsVos,
			Connection connection) throws ApplicationException {
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = connection.prepareStatement(ReceiptConstants.INSERT_INTO_VENDOR_CREDIT_NOTE);
			for(VendorCreditNoteDetailsVo vendorCreditNoteDetailsVo : vendorCreditNoteDetailsVos) {
				preparedStatement.setInt(1, vendorCreditNoteDetailsVo.getCreditNoteId());
				preparedStatement.setString(2, vendorCreditNoteDetailsVo.getCreditNoteRefNo());
				preparedStatement.setDouble(3, vendorCreditNoteDetailsVo.getCreditAmount());
				preparedStatement.setDouble(4, vendorCreditNoteDetailsVo.getRefundAmount());
				preparedStatement.setString(5, CommonConstants.STATUS_AS_ACTIVE);
				preparedStatement.setInt(6, receiptId);
				preparedStatement.addBatch();
			}
			preparedStatement.executeBatch();
			
		}catch (Exception e) {
			logger.error("Error during createVendorCreditNoteDetails ", e);
			throw new ApplicationException(e.getMessage());
		}finally{
			closeResources(null, preparedStatement, null);
		}
		
	}

	private void createVendorRefundInvoiceDetails(Integer receiptId, VendorRefundReceiptDetailsVo vendorRefunds,
			Connection con) throws ApplicationException {
		logger.info("To insert into vendor refunds invoice details table ");
		PreparedStatement preparedStatement =null;
		try  {
			preparedStatement = con.prepareStatement(ReceiptConstants.INSERT_INTO_VENDOR_REFUND_INVOICES);
			preparedStatement.setInt(1, vendorRefunds.getInvoiceId());
			preparedStatement.setString(2, vendorRefunds.getInvoiceRefNo());
			preparedStatement.setDouble(3, vendorRefunds.getBillAmount());
			preparedStatement.setDouble(4,
					vendorRefunds.getRefundAmount() != null ? vendorRefunds.getRefundAmount() : 0.00);
			preparedStatement.setString(5, CommonConstants.STATUS_AS_ACTIVE);
			preparedStatement.setInt(6, receiptId);
			preparedStatement.setTimestamp(7, new Timestamp(System.currentTimeMillis()));
			preparedStatement.executeUpdate();
			logger.info("Successfully inserted into vendor refunds invoice details table ");

		} catch (Exception e) {
			logger.error("Error during createVendorRefundInvoiceDetails ", e);
			throw new ApplicationException(e.getMessage());
		}finally{
			closeResources(null, preparedStatement, null);
		}
	}


	private void deleteVendorRefundInvoiceDetails(Integer receiptId, Integer vendorRefundsId, Connection con)
			throws ApplicationException {
		logger.info("To delete into vendor refunds invoice details table ");
		PreparedStatement preparedStatement =null;
		try {
			preparedStatement = con.prepareStatement(ReceiptConstants.DELETE_VENDOR_REFUND_INVOICES);
			preparedStatement.setString(1, CommonConstants.STATUS_AS_DELETE);
			preparedStatement.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
			preparedStatement.setInt(3, vendorRefundsId);
			preparedStatement.executeUpdate();
			logger.info("Successfully delete into vendor refunds invoice details table ");

		} catch (Exception e) {
			logger.error("Error during deleteVendorRefundInvoiceDetails ", e);
			throw new ApplicationException(e.getMessage());
		}finally{
			closeResources(null, preparedStatement, null);
		}

	}

	private List<VendorRefundReceiptDetailsVo> getVendorRefundInvoiceDetails(Integer receiptId, Connection con)
			throws ApplicationException {
		List<VendorRefundReceiptDetailsVo> vendorList = null;
		logger.info("To get into vendor refunds invoice details table ");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			preparedStatement = con.prepareStatement(ReceiptConstants.GET_VENDOR_REFUND_INVOICES);
			preparedStatement.setInt(1, receiptId);
			vendorList = new ArrayList<VendorRefundReceiptDetailsVo>();
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				VendorRefundReceiptDetailsVo vo = new VendorRefundReceiptDetailsVo();
				vo.setId(rs.getInt(1));
				vo.setInvoiceId(rs.getInt(2));
				vo.setInvoiceRefNo(rs.getString(3));
				vo.setBillAmount(rs.getDouble(4));
				vo.setRefundAmount(rs.getDouble(5));
				vo.setStatus(rs.getString(6));
				vendorList.add(vo);
			}

			logger.info("Successfully retrieved into vendor refunds invoice details table " + vendorList.size());

		} catch (Exception e) {
			logger.error("Error during getVendorRefundInvoiceDetails ", e);
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, null);
		}
		return vendorList;

	}

	private ReceiptBulkDetailsVo createReceiptBulkDetails(ReceiptBulkDetailsVo receiptBulkDetailsVo,ReceiptVo receiptVo, Connection con)
			throws ApplicationException {
		logger.info("Entry into method: createReceiptBulkDetails");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			String sql = ReceiptConstants.INSERT_INTO_RECEIPT_BULK_DETAILS;
			preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setInt(1, receiptBulkDetailsVo.getTypeId());
			preparedStatement.setInt(2, receiptBulkDetailsVo.getReferenceId());
			preparedStatement.setString(3, receiptBulkDetailsVo.getReferenceType());
			preparedStatement.setString(4, receiptBulkDetailsVo.getAmount());
			preparedStatement.setInt(5, receiptBulkDetailsVo.getReceiptId());

			String data = "{\"data\":";
			data += "{\n" + "\"bankCharges\":\"" + receiptBulkDetailsVo.getBankCharges() + "\"," + "\"tdsDeducted\":\""
					+ receiptBulkDetailsVo.getTdsDeducted() + "\"," + "\"others3\":\""
					+ receiptBulkDetailsVo.getOthers3() + "\"," + "\"others1\":\"" + receiptBulkDetailsVo.getOthers1()
					+ "\"," + "\"others2\":\"" + receiptBulkDetailsVo.getOthers2() + "\"" + "}" + "}";

			logger.info(data);
			preparedStatement.setString(6, data);
			preparedStatement.setString(7, CommonConstants.STATUS_AS_ACTIVE);
			String dueAmount=receiptBulkDetailsVo.getInvoiceDueAmount();
			if(receiptVo!=null &&  !receiptVo.getIsBulk()) {
				dueAmount=receiptVo.getAmount();
			}
			preparedStatement.setString(8, dueAmount);
			preparedStatement.setString(9, dueAmount);
			int rowAffected = preparedStatement.executeUpdate();
			if (rowAffected == 1) {
				rs = preparedStatement.getGeneratedKeys();
				if (rs.next()) {
					receiptBulkDetailsVo.setId(rs.getInt(1));
					logger.info("Receipt receiptBulkDetailsVo:" + receiptBulkDetailsVo);

				}
			}
		} catch (Exception e) {
			logger.error("Error in createReceiptBulkDetails", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, null);
		}
		return receiptBulkDetailsVo;
	}

	public ReceiptVo updateReceipt(ReceiptVo receiptVo) throws ApplicationException, SQLException {
		logger.info("Entry into method: updateReceipt");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection connection = getAccountsReceivableConnection();
		connection.setAutoCommit(false);
		try {
			if(receiptVo!=null && receiptVo.getId()!=null) {
			String receiptNo = (receiptVo.getReceiptNoPrefix() != null ? receiptVo.getReceiptNoPrefix() : "") + "/"
					+ receiptVo.getReceiptNo() + "/"
					+ (receiptVo.getReceiptNoSuffix() != null ? receiptVo.getReceiptNoSuffix() : "");
			receiptVo.setReceiptNo(receiptNo);
			boolean isReceiptExist = checkReceiptExist(receiptVo, connection);
			if (isReceiptExist) {
				throw new Exception("Receipt Exist for the Organization");
			}
			if (receiptVo!=null && receiptVo.getItemsToRemove() != null) {
				for (Integer id : receiptVo.getItemsToRemove()) {// IF deleted
					if (receiptVo != null && receiptVo.getReceiptType() != null && receiptVo.getReceiptType()
							.equalsIgnoreCase(ReceiptConstants.AR_RECEIPT_TYPE_VENDOR_REFUNDS)) {
						deleteVendorRefundInvoiceDetails(receiptVo.getId(), id, connection);
					} else if (receiptVo != null && receiptVo.getReceiptType() != null
							&& receiptVo.getReceiptType()
							.equalsIgnoreCase(ReceiptConstants.AR_RECEIPT_TYPE_CUSTOMER_PAYMENTS)) {
						deleteReceiptBulkDetails(id, connection);
					}
				}
			}

			String sql = ReceiptConstants.UPDATE_RECEIPT_ORGANIZATION;
			preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setString(1, receiptVo.getReceiptType());
			preparedStatement.setString(2, receiptNo);
			String date = receiptVo.getReceiptDate();
			DateConverter dc = DateConverter.getInstance();
			Connection userMgmtConnection = getUserMgmConnection();
			String dateFormat = organizationDao.getDefaultDateForOrganization(receiptVo.getOrganizationId(),
					userMgmtConnection);
			closeResources(null, null, userMgmtConnection);
			if (date != null && date.length() > 0 && date.contains("T") && date.contains("Z")) {
				date = dc.correctDatePickerDateToString(date);
			} else if (date != null && date.length() > 0) {
				date = dc.convertDateToGivenFormat(dc.convertStringToDate(date, dateFormat), "yyyy-MM-dd");
				;
			}
			preparedStatement.setString(3, date);
			preparedStatement.setInt(4, receiptVo.getCustomerId() != null ? receiptVo.getCustomerId() : 0);
			preparedStatement.setInt(5, receiptVo.getVendorId() != null ? receiptVo.getVendorId() : 0);
			preparedStatement.setInt(6, receiptVo.getContactId() != null ? receiptVo.getContactId() : 0);
			preparedStatement.setString(7, receiptVo.getContactType());
			preparedStatement.setString(8, receiptVo.getBankId());
			preparedStatement.setString(9, receiptVo.getBankType());
			preparedStatement.setInt(10, receiptVo.getCurrencyId());
			preparedStatement.setString(11, receiptVo.getAmount());
			preparedStatement.setString(12, receiptVo.getTotal());
			preparedStatement.setInt(13, receiptVo.getOrganizationId());
			preparedStatement.setBoolean(14, receiptVo.getIsBulk());
			List<ReceiptSettingsVo> settingsData = receiptVo.getCustomTableList();
			ObjectMapper mapper = new ObjectMapper();
			String newJsonData = mapper.writeValueAsString(settingsData);
			logger.info("settingsData:" + newJsonData);
			preparedStatement.setString(15, newJsonData);
			preparedStatement.setString(16, receiptVo.getNotes());
			preparedStatement.setString(17, receiptVo.getUserId());
			preparedStatement.setString(18, receiptVo.getRoleName());
			preparedStatement.setString(19, receiptVo.getExchangeRate());
			preparedStatement.setBoolean(20, receiptVo.getAddNotes());
			preparedStatement.setString(21, receiptVo.getStatus());
			preparedStatement.setString(22, receiptVo.getAdjustedAmount());
			preparedStatement.setString(23, receiptVo.getDifferenceAmount());
			preparedStatement.setBoolean(24, receiptVo.getRecordBillDetails());
			String glData =receiptVo.getGeneralLedgerData()!=null ? mapper.writeValueAsString(receiptVo.getGeneralLedgerData()) : null;
			logger.info("glData>> "+glData);
			preparedStatement.setString(25, glData);
			preparedStatement.setInt(26, receiptVo.getId());

			int rowAffected = preparedStatement.executeUpdate();
			if (rowAffected == 1) {
				rs = preparedStatement.getGeneratedKeys();

				if (rs.next()) {
					receiptVo.setId(rs.getInt(1));
				}

			}

			if (receiptVo.getAttachments() != null && receiptVo.getAttachments().size() > 0) {
				attachmentsDao.createAttachments(receiptVo.getOrganizationId(), receiptVo.getUserId(),
						receiptVo.getAttachments(), AttachmentsConstants.MODULE_TYPE_ACCOUNTS_RECEIVABLES_RECEIPTS,
						receiptVo.getId(), receiptVo.getRoleName());
			}
			
			//Remove child objects if there are any(Considering the Receipt type/Customer/Vendor/Currency changes in edit mode)
			deleteReceiptDetailsByReceiptId(receiptVo.getId(), connection);
			// Setting Child objects
			if (receiptVo.getReceiptBulkDetails() != null && receiptVo.getReceiptBulkDetails().size() > 0) {
				
				for (ReceiptBulkDetailsVo receiptBulkDetailsVo : receiptVo.getReceiptBulkDetails()) {
					receiptBulkDetailsVo.setReceiptId(receiptVo.getId());
						receiptBulkDetailsVo.setStatus(CommonConstants.STATUS_AS_ACTIVE);
						createReceiptBulkDetails(receiptBulkDetailsVo,receiptVo, connection);

				}
			}
			
			if (receiptVo.getVendorReceiptDetails() != null && receiptVo.getVendorReceiptDetails().size() > 0) {
				for (VendorRefundReceiptDetailsVo vo : receiptVo.getVendorReceiptDetails()) {
						vo.setStatus(CommonConstants.STATUS_AS_ACTIVE);
						createVendorRefundInvoiceDetails(receiptVo.getId(), vo, connection);
				}
			}

			if (receiptVo.getAttachmentsToRemove() != null && receiptVo.getAttachmentsToRemove().size() > 0) {
				for (Integer attachmentId : receiptVo.getAttachmentsToRemove()) {
					attachmentsDao.changeStatusForSingleAttachment(attachmentId, CommonConstants.STATUS_AS_DELETE,
							receiptVo.getUserId(), receiptVo.getRoleName());
				}
			}
			connection.commit();
}
		} catch (Exception e) {
			logger.error("Error in updateReceipt", e);
			connection.rollback();
			logger.info("Before throw in dao :: " + e.getMessage());
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, connection);
		}
		return receiptVo;
	}


	private boolean deleteReceiptDetailsByReceiptId(int receiptId, Connection con) throws ApplicationException {
		logger.info("Entry into method: deleteReceiptBulkDetailsByReceiptId");
		PreparedStatement preparedStatement = null;
		boolean result=false;
		try {
			preparedStatement = con.prepareStatement(ReceiptConstants.DELETE_RECEIPT_BULK_DETAILS_BY_RECEIPT_ID);
			preparedStatement.setInt(1, receiptId);
			preparedStatement.executeUpdate();
			closeResources(null, preparedStatement, null);
			preparedStatement = con.prepareStatement(ReceiptConstants.DELETE_VENDOR_REFUND_BY_RECEIPT_ID);
			preparedStatement.setInt(1, receiptId);
			preparedStatement.executeUpdate();

			result=true;
		} catch (Exception e) {
			logger.error("Error in deleteReceiptDetailsByReceiptId", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(null, preparedStatement, null);
		}
		return result;
	}

	private ReceiptBulkDetailsVo deleteReceiptBulkDetails(int id, Connection con) throws ApplicationException {
		logger.info("Entry into method: deleteReceiptBulkDetails");
		PreparedStatement preparedStatement = null;

		ReceiptBulkDetailsVo receiptBulkDetailsVo = new ReceiptBulkDetailsVo();
		try {
			String sql = ReceiptConstants.DELETE_RECEIPT_BULK_DETAILS_BY_ID;
			preparedStatement = con.prepareStatement(sql);
			preparedStatement.setString(1, CommonConstants.STATUS_AS_DELETE);
			preparedStatement.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
			preparedStatement.setInt(3, id);
			receiptBulkDetailsVo.setId(id);
			preparedStatement.executeUpdate();

		} catch (Exception e) {
			logger.error("Error in deleteReceiptBulkDetails", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(null, preparedStatement, null);
		}
		return receiptBulkDetailsVo;
	}

	public List<ReceiptVo> getAllReceiptsOfAnOrganizationForUserAndRole(int organizationId, String userId,
			String roleName) throws ApplicationException {
		logger.info("Entry into method: getAllReceiptsOfAnOrganization");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		List<ReceiptVo> listReceipts = new ArrayList<ReceiptVo>();
		try {
			con = getAccountsReceivableConnection();

			String query = "";
			if (roleName.equals("Super Admin")) {
				query = ReceiptConstants.GET_RECEIPTS_FOR_ORGNIZATION;
			} else {
				query = ReceiptConstants.GET_RECEIPTS_FOR_ORGNIZATION_USER_ROLE;
			}
			logger.info(query);
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, organizationId);
			if (!(roleName.equals("Super Admin"))) {
				preparedStatement.setString(2, userId);
				preparedStatement.setString(3, roleName);
			}
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				ReceiptVo receiptVo = new ReceiptVo();
				receiptVo.setId(rs.getInt(1));
				String receiptType=rs.getString(2);
				receiptVo.setReceiptType(receiptType);
				receiptVo.setCustomerName(rs.getString(3) != null ? rs.getString(3) : "NA");
				receiptVo.setVendorName(rs.getString(4) != null ? rs.getString(4) : "NA");
				Connection con1 = getUserMgmConnection();
				String dateFormat = organizationDao.getDefaultDateForOrganization(organizationId, con1);
				closeResources(null, null, con1);
				receiptVo.setReceiptDate(rs.getDate(5) != null
						? DateConverter.getInstance().convertDateToGivenFormat(rs.getDate(5), dateFormat)
								: null);
				receiptVo.setBankType(rs.getString(6));
				String amtReceived = rs.getString(7);
				receiptVo.setAmount(amtReceived);
				CurrencyVo currencyVo = currencyDao.getCurrency(rs.getInt(8));
				if (amtReceived != null && amtReceived.length() > 0 && currencyVo != null) {// Set symbol
					receiptVo.setAmount(
							currencyVo.getSymbol() != null ? currencyVo.getSymbol() + " " + amtReceived : amtReceived);
				}
				String status = rs.getString(9);
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
				case CommonConstants.STATUS_AS_ADJUSTED:
					displayStatus = CommonConstants.DISPLAY_STATUS_AS_ADJUSTED;
					break;
				case CommonConstants.STATUS_AS_PARTIALLY_ADJUSTED:
					displayStatus = CommonConstants.DISPLAY_STATUS_AS_PARTIALLY_ADJUSTED;
					break;
				case CommonConstants.STATUS_AS_OPEN:
					displayStatus = CommonConstants.DISPLAY_STATUS_AS_OPEN;
					break;
				}
				receiptVo.setStatus(displayStatus);
				String receiptNo = rs.getString(10);
				Integer contactId=rs.getInt(11);
				String contactType = rs.getString(12);
				if(receiptType!=null) {
					if(receiptType.equalsIgnoreCase(ReceiptConstants.RECEIPT_TYPE_OTHER_RECEIPTS)) {
						receiptVo.setContactName(getContactName(contactId, contactType,con));
					}else if(receiptType.equalsIgnoreCase(ReceiptConstants.RECEIPT_TYPE_CUSTOMER_PAYMENTS)) {
						receiptVo.setContactName(receiptVo.getCustomerName());
						// setting Invoices 
						List<BasicInvoiceDetailsVo> invoices=getAllInvoicesForReceipt(receiptVo.getId(),0,0);
						//include advances
						invoices.addAll(getAllAdvancesForReceipt(receiptVo.getId()));
						receiptVo.setInvoices(invoices);

					}else if(receiptType.equalsIgnoreCase(ReceiptConstants.RECEIPT_TYPE_VENDOR_REFUNDS)) {
						receiptVo.setContactName(receiptVo.getVendorName()	);
						// setting Invoices
						List<BasicInvoiceDetailsVo> invoices = new ArrayList<BasicInvoiceDetailsVo>();
						for (VendorRefundReceiptDetailsVo invoice : getVendorRefundInvoiceDetails(receiptVo.getId(), con)) {
							BasicInvoiceDetailsVo inv = new BasicInvoiceDetailsVo();
							inv.setId(invoice.getId());
							inv.setName(invoice.getInvoiceRefNo());
							inv.setInvoiceNumber(invoice.getInvoiceRefNo());
							invoices.add(inv);
						}
						receiptVo.setInvoices(invoices);
					}
				}
				logger.info(receiptNo);
				if (receiptNo != null) {
					String[] recNum = receiptNo.split("/");
					logger.info("recNum.length::" + recNum.length);
					if (recNum.length > 2) {
						receiptVo.setReceiptNoPrefix(recNum[0]);
						receiptVo.setReceiptNo(recNum[1]);
						receiptVo.setReceiptNoSuffix(recNum[2]);

					}
				}

				listReceipts.add(receiptVo);

			}



		} catch (Exception e) {
			logger.error("Error during getAllReceiptsOfAnOrganization", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return listReceipts;
	}

	private String getContactName(Integer contactId, String contactType, Connection con) throws ApplicationException {
		logger.info("Entry into getContact name: "+contactType+","+contactId);
		String contactName = "-";
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			if(contactId!=null && contactType!=null) {
				String query = null;
				if (contactType.equalsIgnoreCase(ReceiptConstants.RECEIPT_CONTACT_TYPE_CUSTOMER)) {
					query=ReceiptConstants.GET_CUSTOMER_NAME_BY_ID;
				} else if (contactType.equalsIgnoreCase(ReceiptConstants.RECEIPT_CONTACT_TYPE_VENDOR)) {
					query=ReceiptConstants.GET_VENDOR_NAME_BY_ID;
				} else if (contactType.equalsIgnoreCase(ReceiptConstants.RECEIPT_CONTACT_TYPE_STATUTORY_BODY)) {
					query=ReceiptConstants.GET_STATUTORY_BODY_NAME_BY_ID;
				} else if (contactType.equalsIgnoreCase(ReceiptConstants.RECEIPT_CONTACT_TYPE_EMPLOYEE)) {
					query=ReceiptConstants.GET_EMPLOYEE_NAME_BY_ID;
				}
				preparedStatement = con.prepareStatement(query);
				preparedStatement.setInt(1, contactId);
				rs = preparedStatement.executeQuery();
				if(query!=null) {
					logger.info("Query:"+query.toString());
				}
				while (rs.next()) {
					contactName=rs.getString(1);
				}

				logger.info("Sucessfully Feteched Contact:" + contactName);
			}
		} catch (Exception e) {
			logger.error("Error during getContactName", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, null);
		}
		return contactName;

	}

	public ReceiptVo getReceiptById(int receiptId) throws ApplicationException {
		logger.info("Entry into method: getReceiptById");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		ReceiptVo receiptVo = new ReceiptVo();
		try {
			con = getAccountsReceivableConnection();
			String query = ReceiptConstants.GET_RECEIPT_BY_ID;
			logger.info(query);
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, receiptId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {

				receiptVo.setId(rs.getInt(1));
				receiptVo.setReceiptType(rs.getString(2));
				String receiptNo = rs.getString(3);
				logger.info(receiptNo);
				if (receiptNo != null) {
					String[] recNum = receiptNo.split("/");
					logger.info("recNum.length::" + recNum.length);
					if (recNum.length > 2) {
						receiptVo.setReceiptNoPrefix(recNum[0]);
						receiptVo.setReceiptNo(recNum[1]);
						receiptVo.setReceiptNoSuffix(recNum[2]);

					}
				}
				Date receiptDate = rs.getDate(4);
				receiptVo.setReceiptDate(receiptDate != null
						? DateConverter.getInstance().convertDateToGivenFormat(receiptDate, "yyyy-MM-dd")
								: null);
				receiptVo.setCustomerId(rs.getInt(5));
				receiptVo.setVendorId(rs.getInt(6));
				receiptVo.setContactId(rs.getInt(7));
				receiptVo.setContactType(rs.getString(8));
				receiptVo.setBankId(rs.getString(9));
				receiptVo.setBankType(rs.getString(10));
				receiptVo.setCurrencyId(rs.getInt(11));
				receiptVo.setAmount(rs.getString(12));
				receiptVo.setTotal(rs.getString(13));
				receiptVo.setOrganizationId(rs.getInt(14));
				receiptVo.setIsBulk(rs.getBoolean(15));
				String settingsData = rs.getString(16);
				List<ReceiptSettingsVo> settings = new ArrayList<ReceiptSettingsVo>();
				if (settingsData != null && settingsData.length() > 4) {
					JSONParser parser = new JSONParser();
					JSONArray columnsArray = (JSONArray) parser.parse(settingsData);

					for (Object obj : columnsArray) {
						JSONObject columnsObj = (JSONObject) obj;

						ReceiptSettingsVo receiptSettingsVo = new ReceiptSettingsVo();
						receiptSettingsVo
						.setcName(columnsObj.get("cName") != null ? columnsObj.get("cName").toString() : null);
						receiptSettingsVo
						.setcType(columnsObj.get("cType") != null ? columnsObj.get("cType").toString() : null);
						receiptSettingsVo.
						setColumnShow(columnsObj.get("columnShow") != null ? (boolean) columnsObj.get("columnShow") : false);

						settings.add(receiptSettingsVo);
					}
					receiptVo.setCustomTableList(settings);
				}
				receiptVo.setNotes(rs.getString(17));
				receiptVo.setUserId(rs.getString(18));
				receiptVo.setRoleName(rs.getString(19));
				receiptVo.setStatus(rs.getString(20));
				receiptVo.setExchangeRate(rs.getString(21));
				receiptVo.setAddNotes(rs.getBoolean(22));
				receiptVo.setAdjustedAmount(rs.getString(23));
				receiptVo.setDifferenceAmount(rs.getString(24));
				receiptVo.setRecordBillDetails(rs.getBoolean(25));
				ObjectMapper mapper = new ObjectMapper();
				String json = rs.getString(26);
				if(json!=null) {
					GeneralLedgerVo gldata = mapper.readValue(json, GeneralLedgerVo.class);
					logger.info("Json map "+gldata);
					receiptVo.setGeneralLedgerData(gldata);
				}
				receiptVo.setCreateTs(rs.getTimestamp(27));
			}
			logger.info(receiptVo.getId());
			// setting child objects
			receiptVo.setReceiptBulkDetails(getReceiptBulkDetails(receiptVo.getId()));

			List<UploadFileVo> uploadFileVos = new ArrayList<UploadFileVo>();
			for (AttachmentsVo attachments : attachmentsDao.getAttachmentDetails(receiptVo.getId(),
					AttachmentsConstants.MODULE_TYPE_ACCOUNTS_RECEIVABLES_RECEIPTS)) {
				UploadFileVo uploadFileVo = new UploadFileVo();
				uploadFileVo.setId(attachments.getId());
				uploadFileVo.setName(attachments.getFileName());
				uploadFileVo.setSize(attachments.getSize());
				uploadFileVos.add(uploadFileVo);
			}
			if (ReceiptConstants.AR_RECEIPT_TYPE_VENDOR_REFUNDS.equals(receiptVo.getReceiptType())
					&& receiptVo.getRecordBillDetails()) {
				logger.info("to get vendor details ");
				receiptVo.setVendorReceiptDetails(getVendorRefundInvoiceDetails(receiptVo.getId(), con));
			}
			receiptVo.setAttachments(uploadFileVos);
			logger.info("Sucessfully Feteched Receipt:" + receiptVo);
		} catch (Exception e) {
			logger.error("Error during getReceiptById", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return receiptVo;
	}

	private List<ReceiptBulkDetailsVo> getReceiptBulkDetails(int receiptId) throws ApplicationException {
		logger.info("Entry into method: getReceiptBulkDetails");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		List<ReceiptBulkDetailsVo> listReceiptBulkDetails = new ArrayList<ReceiptBulkDetailsVo>();
		try {
			con = getAccountsReceivableConnection();
			String query = ReceiptConstants.GET_BULK_DETAILS_FOR_RECEIPT;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, receiptId);
			rs = preparedStatement.executeQuery();
			List<CommonVo> types = financeCommonDao.getReceiptBulkDetailTypes();
			Map<Integer, String> typesMap = new HashMap<Integer, String>();
			for (CommonVo type : types) {
				typesMap.put(type.getId(), type.getName());
			}
			while (rs.next()) {
				ReceiptBulkDetailsVo receiptBulkDetailsVo = new ReceiptBulkDetailsVo();
				receiptBulkDetailsVo.setId(rs.getInt(1));
				receiptBulkDetailsVo.setTypeId(rs.getInt(2));
				int referenceId = rs.getInt(3);
				String referenceType = rs.getString(4);
				String type = typesMap.get(rs.getInt(2));
				if (type != null && type.equalsIgnoreCase(ReceiptConstants.AR_RECEIPT_TYPE_INVOICE)
						&& referenceType != null) {
					if (referenceType.equalsIgnoreCase(ReceiptConstants.AR_RECEIPT_TYPE_CREATE_NEW_ADVANCE)) {
						referenceId = ReceiptConstants.AR_RECEIPT_ADVANCE_PAYMENT_REFERENCE;
					} else if (referenceType.equalsIgnoreCase(ReceiptConstants.AR_RECEIPT_TYPE_CREATE_NEW_ON_ACCOUNT)) {
						referenceId = ReceiptConstants.AR_RECEIPT_ON_ACCOUNT_REFERENCE;
					}
				}
				receiptBulkDetailsVo.setParentType(type);
				receiptBulkDetailsVo.setReferenceId(referenceId);
				receiptBulkDetailsVo.setReferenceType(referenceType);
				receiptBulkDetailsVo.setAmount(rs.getString(5));
				receiptBulkDetailsVo.setReceiptId(rs.getInt(6));
				// Processing JSON Data
				String data = rs.getString(7);
				logger.info("Data:" + data);
				if (data != null) {
					JSONParser parser = new JSONParser();
					JSONObject dataJson = (JSONObject) parser.parse(data);
					JSONObject columnsObj = (JSONObject) dataJson.get("data");

					receiptBulkDetailsVo.setBankCharges(
							columnsObj.get("bankCharges") != null ? columnsObj.get("bankCharges").toString() : null);
					receiptBulkDetailsVo.setTdsDeducted(
							columnsObj.get("tdsDeducted") != null ? columnsObj.get("tdsDeducted").toString() : null);
					receiptBulkDetailsVo.setOthers3(
							columnsObj.get("others3") != null ? columnsObj.get("others3").toString() : null);
					receiptBulkDetailsVo.setOthers1(
							columnsObj.get("others1") != null ? columnsObj.get("others1").toString() : null);
					receiptBulkDetailsVo.setOthers2(
							columnsObj.get("others2") != null ? columnsObj.get("others2").toString() : null);

				}
				receiptBulkDetailsVo.setInvoiceDueAmount(rs.getString(8));
				receiptBulkDetailsVo.setStatus(rs.getString(9));
				listReceiptBulkDetails.add(receiptBulkDetailsVo);
			}
			logger.info("Successfully fetched getReceiptBulkDetails:" + listReceiptBulkDetails);
		} catch (Exception e) {
			logger.error("Error during getReceiptBulkDetails", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return listReceiptBulkDetails;
	}
	public List<BasicInvoiceDetailsVo> getAllAdvancesForReceipt(int receiptId) throws ApplicationException {
		logger.info("Entry into method: getAllAdvancesForReceipt");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		List<BasicInvoiceDetailsVo> listInvoices = new ArrayList<BasicInvoiceDetailsVo>();
		try {
			con = getAccountsReceivableConnection();

			preparedStatement = con.prepareStatement(ReceiptConstants.GET_ADVANCES_FOR_RECEIPT);
			preparedStatement.setInt(1, receiptId);
			preparedStatement.setString(2, ReceiptConstants.AR_RECEIPT_TYPE_CREATE_NEW_ADVANCE);
			preparedStatement.setString(3, ReceiptConstants.AR_RECEIPT_TYPE_CREATE_NEW_ON_ACCOUNT);
			preparedStatement.setString(4, CommonConstants.STATUS_AS_ACTIVE);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				if (rs.getString(1) != null) {
					BasicInvoiceDetailsVo receiptInvoiceVo = new BasicInvoiceDetailsVo();
					receiptInvoiceVo.setCustomerId(rs.getInt(1));
					receiptInvoiceVo.setCurrencyId(rs.getInt(2));
					receiptInvoiceVo.setReceiptRefundableAmount(rs.getString(3));
					String type=rs.getString(4);
					if(type!=null) {
						if(type.equalsIgnoreCase(ReceiptConstants.AR_RECEIPT_TYPE_CREATE_NEW_ADVANCE)) {
							type=ReceiptConstants.AR_RECEIPT_TYPE_ADVANCE_PAYMENT;
							receiptInvoiceVo.setId(ReceiptConstants.AR_RECEIPT_ADVANCE_PAYMENT_REFERENCE);
						}else if(type.equalsIgnoreCase(ReceiptConstants.AR_RECEIPT_TYPE_CREATE_NEW_ON_ACCOUNT)) {
							type=ReceiptConstants.AR_RECEIPT_TYPE_ON_ACCOUNT;;
							receiptInvoiceVo.setId(ReceiptConstants.AR_RECEIPT_ON_ACCOUNT_REFERENCE);
						}
					}
					receiptInvoiceVo.setName(type);
					receiptInvoiceVo.setInvoiceNumber(type);
					listInvoices.add(receiptInvoiceVo);
				}
			}

		} catch (Exception e) {
			logger.error("Error during getAllAdvancesForReceipt", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return listInvoices;
	}

	public List<BasicInvoiceDetailsVo> getAllCreditNotesForReceipt(int receiptId,int customerId,int currencyId) throws ApplicationException {
		logger.info("Entry into method: getAllCreditNotesForReceipt");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		List<BasicInvoiceDetailsVo> listInvoices = new ArrayList<BasicInvoiceDetailsVo>();
		try {
			con = getAccountsReceivableConnection();

			StringBuffer query = new StringBuffer( ReceiptConstants.GET_INVOICES_FOR_RECEIPT);
			if(customerId>0 && currencyId>0) {
				query.append(" and receipt_id IN(SELECT id from receipt WHERE id=rbd.receipt_id and customer_id="+customerId+" and currency_id="+currencyId+")");
			}
			
			preparedStatement = con.prepareStatement(query.toString());
			preparedStatement.setInt(1, receiptId);
			preparedStatement.setString(2, ReceiptConstants.AR_RECEIPT_TYPE_INVOICE);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				if (rs.getString(1) != null) {
					BasicInvoiceDetailsVo receiptInvoiceVo = new BasicInvoiceDetailsVo();
					receiptInvoiceVo.setId(rs.getInt(1));
					receiptInvoiceVo.setName(rs.getString(2) != null ? rs.getString(2).replace("~", "/") : "");
					receiptInvoiceVo.setInvoiceNumber(rs.getString(2) != null ? rs.getString(2).replace("~", "/") : "");
					receiptInvoiceVo.setInvoiceAmount(rs.getString(6));

					double dueAmount = rs.getDouble(4) + rs.getDouble(6);// excluding invoice amount for receipt from
					// due balance
					String balanceDueStr = dueAmount + "";
					CurrencyVo currencyVo = currencyDao.getCurrency(rs.getInt(5));
					if (balanceDueStr != null && balanceDueStr.length() > 0 && currencyVo != null) {

						BigDecimal bd = new BigDecimal(Double.parseDouble(balanceDueStr))
								.setScale(currencyVo.getNoOfDecimalsForAmount(), RoundingMode.HALF_UP);
						receiptInvoiceVo.setDueAmount(bd.doubleValue() + "");
					}

					receiptInvoiceVo.setCurrencyId(rs.getInt(5));
					receiptInvoiceVo.setCustomerId(rs.getInt(7));
					listInvoices.add(receiptInvoiceVo);
				}
			}

		} catch (Exception e) {
			logger.error("Error during getAllCreditNotesForReceipt", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return listInvoices;
	}
	
	public List<BasicInvoiceDetailsVo> getAllInvoicesForReceipt(int receiptId,int customerId,int currencyId) throws ApplicationException {
		logger.info("Entry into method: getAllInvoicesForReceipt");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		List<BasicInvoiceDetailsVo> listInvoices = new ArrayList<BasicInvoiceDetailsVo>();
		try {
			con = getAccountsReceivableConnection();

			StringBuffer query = new StringBuffer( ReceiptConstants.GET_INVOICES_FOR_RECEIPT);
			if(customerId>0 && currencyId>0) {
				query.append(" and receipt_id IN(SELECT id from receipt WHERE id=rbd.receipt_id and customer_id="+customerId+" and currency_id="+currencyId+")");
			}
			
			preparedStatement = con.prepareStatement(query.toString());
			preparedStatement.setInt(1, receiptId);
			preparedStatement.setString(2, ReceiptConstants.AR_RECEIPT_TYPE_INVOICE);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				if (rs.getString(1) != null) {
					BasicInvoiceDetailsVo receiptInvoiceVo = new BasicInvoiceDetailsVo();
					receiptInvoiceVo.setId(rs.getInt(1));
					receiptInvoiceVo.setName(rs.getString(2) != null ? rs.getString(2).replace("~", "/") : "");
					receiptInvoiceVo.setInvoiceNumber(rs.getString(2) != null ? rs.getString(2).replace("~", "/") : "");
					receiptInvoiceVo.setInvoiceAmount(rs.getString(6));
					
					double dueAmount = rs.getDouble(4) + rs.getDouble(6);// excluding invoice amount for receipt from
					// due balance
					String balanceDueStr = dueAmount + "";
					CurrencyVo currencyVo = currencyDao.getCurrency(rs.getInt(5));
					if (balanceDueStr != null && balanceDueStr.length() > 0 && currencyVo != null) {

						BigDecimal bd = new BigDecimal(Double.parseDouble(balanceDueStr))
								.setScale(currencyVo.getNoOfDecimalsForAmount(), RoundingMode.HALF_UP);
						receiptInvoiceVo.setDueAmount(bd.doubleValue() + "");
					}

					receiptInvoiceVo.setCurrencyId(rs.getInt(5));
					receiptInvoiceVo.setCustomerId(rs.getInt(7));
					receiptInvoiceVo.setReceiptRefundableAmount(rs.getString(8));
					listInvoices.add(receiptInvoiceVo);
				}
			}

		} catch (Exception e) {
			logger.error("Error during getAllInvoicesForReceipt", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return listInvoices;
	}

	public String getTotalCreditNoteAmountForReceipt(int receiptId) throws ApplicationException {
		logger.info("Entry into method: getTotalCreditNoteAmountForReceipt");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		String amount = null;
		try {
			con = getAccountsReceivableConnection();

			String query = ReceiptConstants.GET_TOTAL_OF_CN_FOR_RECEIPT;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, receiptId);
			preparedStatement.setString(2, "Credit Notes");
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				amount = rs.getString(1);
			}

		} catch (Exception e) {
			logger.error("Error during getTotalCreditNoteAmountForReceipt", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return amount;
	}

	private boolean checkReceiptExist(ReceiptVo receiptvo, Connection conn) throws ApplicationException {
		logger.info("Entry into method: checkReceiptExist");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;

		try {

			if (receiptvo != null) {
				String query = ReceiptConstants.CHECK_RECEIPT_FOR_ORGANIZATION;
				preparedStatement = conn.prepareStatement(query);
				preparedStatement.setInt(1, receiptvo.getOrganizationId());
				preparedStatement.setString(2, receiptvo.getReceiptNo());
				rs = preparedStatement.executeQuery();
				logger.info("preparedStmnt::" + preparedStatement.toString());
				if (rs.next()) {

					if (receiptvo.getId() == null) {// create scenario
						return true;
					} else {
						if (receiptvo.getId() != rs.getInt(1)) {// If it is not same record
							return true;
						}

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

	// When Invoice status changed to PAID it will not list in initial dropdown
	// because PAID Status is not eligible for dropdown
	public List<BasicInvoiceDetailsVo> getReceiptDropdownInvoices(int organizationId, int currencyId, int customerId,
			int receiptId) throws ApplicationException {
		logger.info("Entry into method: getReceiptDropdownInvoices");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;

		List<BasicInvoiceDetailsVo> invoicesList = new ArrayList<BasicInvoiceDetailsVo>();

		try {
			int receiptTypeId = financeCommonDao
					.getReceiptBulkDetailTypeByName(ReceiptConstants.AR_RECEIPT_TYPE_INVOICE);
			StringBuilder sqlQuery = new StringBuilder(ArInvoiceConstants.GET_ALL_RECEIPT_ELIGIBLE_INVOICES_FOR_ORG);
			if (receiptId > 0) {// IF It s from Get by ID
				List<BasicInvoiceDetailsVo> receiptInvoices = getAllInvoicesForReceipt(receiptId,customerId,currencyId);
				String invoiceIds = "";
				for (BasicInvoiceDetailsVo receiptInvoiceVo : receiptInvoices) {
					BasicInvoiceDetailsVo basicInvoiceVo = new BasicInvoiceDetailsVo();
					int invoiceId = receiptInvoiceVo.getId();
					basicInvoiceVo.setId(receiptInvoiceVo.getId());
					basicInvoiceVo.setName(receiptInvoiceVo.getInvoiceNumber());
					basicInvoiceVo.setInvoiceNumber(receiptInvoiceVo.getInvoiceNumber());
					basicInvoiceVo.setValue(receiptInvoiceVo.getId() + "");
					invoiceIds += invoiceId + ",";
					basicInvoiceVo.setType(ReceiptConstants.AR_RECEIPT_TYPE_INVOICE);
					basicInvoiceVo.setParentId(receiptTypeId);
					basicInvoiceVo.setParentName(ReceiptConstants.AR_RECEIPT_TYPE_INVOICE);
					basicInvoiceVo.setDueAmount(receiptInvoiceVo.getDueAmount());
					invoicesList.add(basicInvoiceVo);
				}
				// Exclude current receipt invoices
				if (receiptInvoices != null && receiptInvoices.size() > 0 && invoiceIds.length() > 1) {
					sqlQuery.append(" and id NOT IN (");
					sqlQuery.append(invoiceIds);
					sqlQuery.setLength(sqlQuery.length() - 1);// Removing last ,
					sqlQuery.append(")");
				}

			}

			// Get All Eligible INvoices
			con = getAccountsReceivableConnection();
			logger.info("Query:" + sqlQuery.toString());
			preparedStatement = con.prepareStatement(sqlQuery.toString());
			preparedStatement.setInt(1, organizationId);
			preparedStatement.setInt(2, customerId);
			preparedStatement.setInt(3, currencyId);
			// TODO:Confirm on status ACTV vs OPEN
			preparedStatement.setString(4, CommonConstants.STATUS_AS_ACTIVE);
			preparedStatement.setString(5, CommonConstants.STATUS_AS_OPEN);
			preparedStatement.setString(6, CommonConstants.STATUS_AS_OVERDUE);
			preparedStatement.setString(7, CommonConstants.STATUS_AS_UNPAID);
			preparedStatement.setString(8, CommonConstants.STATUS_AS_PARTIALLY_PAID);
			rs = preparedStatement.executeQuery();

			while (rs.next()) {
				BasicInvoiceDetailsVo arVo = new BasicInvoiceDetailsVo();
				arVo.setId(rs.getInt(1));
				arVo.setName(rs.getString(2) != null ? rs.getString(2).replace("~", "/") : null);
				arVo.setParentName(ReceiptConstants.AR_RECEIPT_TYPE_INVOICE);
				arVo.setParentId(receiptTypeId);
				arVo.setInvoiceNumber(rs.getString(2) != null ? rs.getString(2).replace("~", "/") : null);
				arVo.setValue(rs.getInt(1) + "");
				arVo.setDueAmount(rs.getString(3));
				CurrencyVo currencyVo = currencyDao.getCurrency(rs.getInt(4));
				if (currencyVo != null && rs.getDouble(3) > 0) {
					BigDecimal bd = new BigDecimal(rs.getDouble(3)).setScale(currencyVo.getNoOfDecimalsForAmount(),
							RoundingMode.HALF_UP);
					arVo.setDueAmount(bd.doubleValue() + "");
				}
				Connection con1 = getUserMgmConnection();
				BasicCurrencyVo basicCurrencyVo = currencyDao.getBasicCurrencyForOrganization(organizationId, con1);
				closeResources(null, null, con1);
				arVo.setOrganizationBasecurrencyId(basicCurrencyVo != null ? basicCurrencyVo.getId() : 0);
				arVo.setType(ReceiptConstants.AR_RECEIPT_TYPE_INVOICE);
				arVo.setInvoiceAmount(rs.getString(6));
				invoicesList.add(arVo);
			}

			logger.info("All invoices:" + invoicesList);

			// Add On Account and Advance payment option in list
			BasicInvoiceDetailsVo arVoOnAccount = new BasicInvoiceDetailsVo();
			arVoOnAccount.setId(ReceiptConstants.AR_RECEIPT_ON_ACCOUNT_REFERENCE);
			arVoOnAccount.setName(ReceiptConstants.AR_RECEIPT_TYPE_CREATE_NEW_ON_ACCOUNT);
			arVoOnAccount.setType(arVoOnAccount.getName());
			arVoOnAccount.setParentName(ReceiptConstants.AR_RECEIPT_TYPE_INVOICE);
			arVoOnAccount.setValue(ReceiptConstants.AR_RECEIPT_ON_ACCOUNT_REFERENCE+"");
			arVoOnAccount.setParentId(receiptTypeId);
			invoicesList.add(arVoOnAccount);
			BasicInvoiceDetailsVo arVoAdvPayment = new BasicInvoiceDetailsVo();
			arVoAdvPayment.setId(ReceiptConstants.AR_RECEIPT_ADVANCE_PAYMENT_REFERENCE);
			arVoAdvPayment.setName(ReceiptConstants.AR_RECEIPT_TYPE_CREATE_NEW_ADVANCE);
			arVoAdvPayment.setValue(ReceiptConstants.AR_RECEIPT_ADVANCE_PAYMENT_REFERENCE+"");
			arVoAdvPayment.setType(arVoAdvPayment.getName());
			arVoAdvPayment.setParentId(receiptTypeId);
			arVoAdvPayment.setParentName(ReceiptConstants.AR_RECEIPT_TYPE_INVOICE);
			invoicesList.add(arVoAdvPayment);

		} catch (Exception e) {
			logger.error("Error in getReceiptDropdownInvoices", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return invoicesList;

	}

	public List<ReceiptBulkDropdownVo> getBulkReceiptDataForCustomer(int organizationId, int currencyId, int customerId,
			int receiptId) throws ApplicationException {
		logger.info("Entry into getBulkReceiptsData :" + organizationId + ",currencyId " + currencyId + ",customerId "
				+ customerId + ",receiptId " + receiptId);
		List<ReceiptBulkDropdownVo> list = new ArrayList<ReceiptBulkDropdownVo>();
		try {
			List<CommonVo> types = financeCommonDao.getReceiptBulkDetailTypes();
			CurrencyVo currencyVo = currencyDao.getCurrency(currencyId);
			if (types != null && !types.isEmpty()) {

				for (CommonVo type : types) {
					logger.info("Type:" + type);
					ReceiptBulkDropdownVo receiptBulkDropdownVo = new ReceiptBulkDropdownVo();
					receiptBulkDropdownVo.setId(type.getId());
					receiptBulkDropdownVo.setName(type.getName());
					receiptBulkDropdownVo.setValue(type.getId());
					List<ReceiptCommonVo> listInvoices = new ArrayList<ReceiptCommonVo>();
					switch (type.getName()) {
					case ReceiptConstants.AR_RECEIPT_TYPE_INVOICE:
						List<BasicInvoiceDetailsVo> invoices = getReceiptDropdownInvoices(organizationId, currencyId,
								customerId, receiptId);
						invoices.forEach(invoice -> {
							ReceiptCommonVo vo = new ReceiptCommonVo();
							vo.setId(invoice.getId());
							vo.setParentType(type.getName());
							vo.setValue(invoice.getId());
							vo.setName(invoice.getName());
							vo.setType(invoice.getType());
							String dueAmount = invoice.getDueAmount();
							if (currencyVo != null && dueAmount != null && Double.parseDouble(dueAmount) > 0) {
								BigDecimal bd = new BigDecimal(Double.parseDouble(dueAmount))
										.setScale(currencyVo.getNoOfDecimalsForAmount(), RoundingMode.HALF_UP);
								dueAmount = bd.doubleValue() + "";
							}
							
							vo.setDueAmount(dueAmount);
							listInvoices.add(vo);
						});
						break;
					case ReceiptConstants.AR_RECEIPT_TYPE_ADVANCE_PAYMENT:
						listInvoices.addAll(getOnAccountOrAdvancePaymentReceipts(organizationId, currencyId, customerId,ReceiptConstants.AR_RECEIPT_TYPE_ADVANCE_PAYMENT,receiptId));
						break;
					case ReceiptConstants.AR_RECEIPT_TYPE_ON_ACCOUNT:
						listInvoices.addAll(getOnAccountOrAdvancePaymentReceipts(organizationId, currencyId, customerId,ReceiptConstants.AR_RECEIPT_TYPE_ON_ACCOUNT,receiptId));
						break;
					case ReceiptConstants.AR_RECEIPT_TYPE_CREDIT_NOTE:
						
						List<CreditNotesDetailsVo> creditNotes =getCreditNotesForCustomerCurrency(organizationId, customerId, currencyId,receiptId);
						logger.info("Creditnotes:" + creditNotes);
						creditNotes.forEach(creditNote -> {
							ReceiptCommonVo vo = new ReceiptCommonVo();
							vo.setId(creditNote.getId());
							vo.setValue(creditNote.getId());
							vo.setType(creditNote.getType());
							vo.setParentType(type.getName());
							vo.setName(creditNote.getName());
							String dueAmount = creditNote.getAvailableAmount();
							if (currencyVo != null && dueAmount != null && Double.parseDouble(dueAmount) > 0) {
								BigDecimal bd = new BigDecimal(Double.parseDouble(dueAmount))
										.setScale(currencyVo.getNoOfDecimalsForAmount(), RoundingMode.HALF_UP);
								dueAmount = bd.doubleValue() + "";
							}
							vo.setDueAmount(dueAmount);
							listInvoices.add(vo);
						});
						break;
					}
					if (listInvoices != null && !listInvoices.isEmpty()) {
						receiptBulkDropdownVo.setList(listInvoices);
						list.add(receiptBulkDropdownVo);
					}

				}
			}
		} catch (Exception e) {
			logger.info("Error in getBulkReceiptsData", e);
			throw new ApplicationException(e.getMessage());
		}
		return list;
	}

	private List<ReceiptCommonVo> getOnAccountOrAdvancePaymentReceipts(int organizationId, int currencyId,
			int customerId,String type, int receiptId) throws ApplicationException {
		logger.info("Entry into getOnAccountOrAdvancePaymentReceipts");
		List<ReceiptCommonVo> receiptsList = new ArrayList<ReceiptCommonVo>();
		PreparedStatement preparedStatement = null;
		Connection con = null;
		ResultSet rs = null;
		String subType=type != null && type.equalsIgnoreCase(ReceiptConstants.AR_RECEIPT_TYPE_ADVANCE_PAYMENT)? ReceiptConstants.AR_RECEIPT_TYPE_CREATE_NEW_ADVANCE	: ReceiptConstants.AR_RECEIPT_TYPE_CREATE_NEW_ON_ACCOUNT;
		try {
			con = getAccountsReceivableConnection();
			StringBuilder sqlQuery = new StringBuilder(ReceiptConstants.GET_ONACCOUNT_OR_ADV_RECEIPTS_BY_CUSTOMER_CURRENCY);
			if(receiptId>0) {
				String receiptIds="";
				preparedStatement = con.prepareStatement(ReceiptConstants.GET_ONACC_OR_ADV_PAYMENTS_FOR_RECEIPT);
				preparedStatement.setString(1,subType);
				preparedStatement.setInt(2, receiptId);
				preparedStatement.setString(3,type);
				preparedStatement.setInt(4, customerId);
				preparedStatement.setInt(5, currencyId);
				rs = preparedStatement.executeQuery();
				logger.info("Query:" + preparedStatement.toString());
				while (rs.next()) {
					ReceiptCommonVo receiptsDetailsVo = new ReceiptCommonVo();
					receiptIds=rs.getInt(1)+",";
					receiptsDetailsVo.setId(rs.getInt(1));
					receiptsDetailsVo.setValue(rs.getInt(1));
					receiptsDetailsVo.setName(rs.getString(2));
					receiptsDetailsVo.setType(rs.getString(3));
					receiptsDetailsVo.setParentType(type);
					BigDecimal due=new BigDecimal(0);
						if( rs.getBigDecimal(4)!=null && rs.getBigDecimal(5)!=null) {
							due=rs.getBigDecimal(4).add(rs.getBigDecimal(5));
							due.setScale(2, BigDecimal.ROUND_HALF_EVEN);
						}	

					receiptsDetailsVo.setDueAmount(due.toString());
					
					receiptsList.add(receiptsDetailsVo);
				}
				closeResources(rs, preparedStatement, null);
				// Exclude current receipt invoices
				if (receiptsList != null && receiptsList.size() > 0 && receiptIds.length() > 1) {
					sqlQuery.append(" and rec.id NOT IN (");
					sqlQuery.append(receiptIds);
					sqlQuery.setLength(sqlQuery.length() - 1);// Removing last ,
					sqlQuery.append(")");
				}
			}
			

			preparedStatement = con.prepareStatement(sqlQuery.toString());
			preparedStatement.setInt(1, organizationId);
			preparedStatement.setInt(2, customerId);
			preparedStatement.setString(3, ReceiptConstants.AR_RECEIPT_TYPE_CUSTOMER_PAYMENTS);
			preparedStatement.setInt(4, currencyId);
			preparedStatement.setString(5, CommonConstants.STATUS_AS_ACTIVE);
			preparedStatement.setString(6, CommonConstants.STATUS_AS_OPEN);
			preparedStatement.setString(7, CommonConstants.STATUS_AS_PARTIALLY_ADJUSTED);
			preparedStatement.setString(8,subType);
			preparedStatement.setString(9, CommonConstants.STATUS_AS_ACTIVE);
			
			rs = preparedStatement.executeQuery();
			logger.info("Query:" + preparedStatement.toString());
			while (rs.next()) {
				ReceiptCommonVo receiptsDetailsVo = new ReceiptCommonVo();
				receiptsDetailsVo.setId(rs.getInt(1));
				receiptsDetailsVo.setValue(rs.getInt(1));
				receiptsDetailsVo.setName(rs.getString(2));
				
				BigDecimal due=new BigDecimal(0);
				if(rs.getBigDecimal(3)!=null ) {
					due=rs.getBigDecimal(3);
					due.setScale(2, BigDecimal.ROUND_HALF_EVEN);
				}
				receiptsDetailsVo.setDueAmount(due.toString());
				receiptsDetailsVo.setParentType(type);
				if(rs.getString(4)!=null ) {
					receiptsDetailsVo.setType(
							rs.getString(4).equalsIgnoreCase(ReceiptConstants.AR_RECEIPT_TYPE_CREATE_NEW_ADVANCE)
									? ReceiptConstants.AR_RECEIPT_TYPE_ADVANCE_PAYMENT
									: ReceiptConstants.AR_RECEIPT_TYPE_ON_ACCOUNT);
					}
				receiptsList.add(receiptsDetailsVo);
			}

			logger.info("Scussfully Feteched Receipts:" + receiptsList);
		} catch (Exception e) {
			logger.info("Error in getOnAccountOrAdvancePaymentReceipts", e);
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return receiptsList;

	}

	public List<ReceiptsDetailsVo> getReceiptsByCustomerId(int organizationId, int customerId, int currencyId)
			throws ApplicationException {
		logger.info("To get the getReceiptsByCustomerId");
		List<ReceiptsDetailsVo> receiptsList = new ArrayList<ReceiptsDetailsVo>();
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			con = getAccountsReceivableConnection();
			preparedStatement = con.prepareStatement(ReceiptConstants.GET_RECEIPTS_BY_CUSTOMER_ID);
			preparedStatement.setInt(1, organizationId);
			preparedStatement.setInt(2, customerId);
			preparedStatement.setString(3, ReceiptConstants.AR_RECEIPT_TYPE_CUSTOMER_PAYMENTS);
			preparedStatement.setInt(4, currencyId);
			preparedStatement.setString(5, CommonConstants.STATUS_AS_DELETE);
			preparedStatement.setString(6, CommonConstants.STATUS_AS_DRAFT);
			preparedStatement.setString(7, CommonConstants.STATUS_AS_VOID);
			preparedStatement.setString(8, CommonConstants.STATUS_AS_ACTIVE);
			preparedStatement.setString(9, ReceiptConstants.AR_RECEIPT_TYPE_CREATE_NEW_ADVANCE);
			preparedStatement.setString(10, ReceiptConstants.AR_RECEIPT_TYPE_CREATE_NEW_ON_ACCOUNT);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				ReceiptsDetailsVo receiptsDetailsVo = new ReceiptsDetailsVo();
				receiptsDetailsVo.setId(rs.getInt(1));
				receiptsDetailsVo.setValue(rs.getInt(1));
				receiptsDetailsVo.setReferenceNo(rs.getString(2));
				receiptsDetailsVo.setName(rs.getString(2));
				receiptsDetailsVo.setOriginalAmount(rs.getString(3));
				receiptsDetailsVo.setAvailableAmount(rs.getDouble(4) + "");
				receiptsDetailsVo.setType(rs.getString(5));
				receiptsList.add(receiptsDetailsVo);
			}
		} catch (Exception e) {
			logger.info("Error in getReceiptsByCustomerId", e);
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return receiptsList;
	}

	public String getAdvanceOnaccountDueAmountForCustomerCurrency(int organizationId, int customerId, int currencyId,
			Connection con) throws ApplicationException {
		logger.info("To get the getAdvanceOnaccountDueAmountForCustomerCurrency");
		String availableAmount = null;
		ResultSet rs = null;
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = con
					.prepareStatement(ReceiptConstants.GET_TOTAL_ADVANCE_ON_ACCOUNT_AMOUNT_FOR_CUSTOMER_CURRENCY);
			preparedStatement.setInt(1, organizationId);
			preparedStatement.setInt(2, customerId);
			preparedStatement.setString(3, ReceiptConstants.AR_RECEIPT_TYPE_CUSTOMER_PAYMENTS);
			preparedStatement.setInt(4, currencyId);
			preparedStatement.setString(5, CommonConstants.STATUS_AS_DRAFT);
			preparedStatement.setString(6, CommonConstants.STATUS_AS_VOID);
			preparedStatement.setString(7, ReceiptConstants.AR_RECEIPT_TYPE_CREATE_NEW_ADVANCE);
			preparedStatement.setString(8, ReceiptConstants.AR_RECEIPT_TYPE_CREATE_NEW_ADVANCE);
			preparedStatement.setString(9, CommonConstants.STATUS_AS_ACTIVE);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				availableAmount = rs.getString(1);
			}
		} catch (Exception e) {
			logger.info("Error in getAdvanceOnaccountDueAmountForCustomerCurrency", e);
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, null);
		}
		return availableAmount;
	}

	public String getAdvanceOnaccountDueAmountForCustomer(int organizationId, int customerId, Connection con)
			throws ApplicationException {
		logger.info("To get the getAdvanceOnaccountDueAmountForCustomer");
		String availableAmount = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			preparedStatement = con.prepareStatement(ReceiptConstants.GET_TOTAL_ADVANCE_ON_ACCOUNT_AMOUNT_FOR_CUSTOMER);
			preparedStatement.setInt(1, organizationId);
			preparedStatement.setInt(2, customerId);
			preparedStatement.setString(3, ReceiptConstants.AR_RECEIPT_TYPE_CUSTOMER_PAYMENTS);
			preparedStatement.setString(4, CommonConstants.STATUS_AS_DRAFT);
			preparedStatement.setString(5, CommonConstants.STATUS_AS_VOID);
			preparedStatement.setString(6, ReceiptConstants.AR_RECEIPT_TYPE_CREATE_NEW_ADVANCE);
			preparedStatement.setString(7, ReceiptConstants.AR_RECEIPT_TYPE_CREATE_NEW_ADVANCE);
			preparedStatement.setString(8, CommonConstants.STATUS_AS_ACTIVE);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				availableAmount = rs.getString(1);
			}

		} catch (Exception e) {
			logger.info("Error in getAdvanceOnaccountDueAmountForCustomer", e);
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, null);
		}

		return availableAmount;
	}
 
	public List<CreditNotesDetailsVo> getCreditNotesForCustomerCurrency(int organizationId,int customerId,int currencyId,int receiptId) throws ApplicationException {
		logger.info("Entry into method: getCreditNotesForCustomerCurrency");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		List<CreditNotesDetailsVo> listCreditDetails = new ArrayList<CreditNotesDetailsVo>();
		try {
			con = getAccountsReceivableConnection();
			StringBuffer mainQuery = new StringBuffer( CreditNoteConstants.GET_CREDIT_DETAILS_FOR_CUSTOMER);
			
			if(receiptId>0) {
			StringBuffer query = new StringBuffer( ReceiptConstants.GET_CREDITNOTES_FOR_RECEIPT);
			preparedStatement = con.prepareStatement(query.toString());
			preparedStatement.setInt(1, receiptId);
			preparedStatement.setString(2, "Credit Notes");
			preparedStatement.setInt(3, customerId);
			preparedStatement.setInt(4, currencyId);
			rs = preparedStatement.executeQuery();
			String creditNoteIds="";
			while (rs.next()) {
				CreditNotesDetailsVo creditNotesDetailsVo = new CreditNotesDetailsVo();
				creditNotesDetailsVo.setId(rs.getInt(1));
				creditNotesDetailsVo.setValue(rs.getInt(1));
				creditNotesDetailsVo.setReferenceNo(rs.getString(2));
				creditNotesDetailsVo.setName(rs.getString(2));
				creditNotesDetailsVo.setOriginalAmount(rs.getString(3));
				CurrencyVo currencyVo = currencyDao.getCurrency(currencyId);
				if (currencyVo != null) {
					BigDecimal balanceDue = rs.getString(4)!=null?new BigDecimal(rs.getDouble(4)):BigDecimal.ZERO;
					BigDecimal receiptCreditNoteAmount = rs.getString(5)!=null?new BigDecimal(rs.getDouble(5)):BigDecimal.ZERO;
					balanceDue=balanceDue.add(receiptCreditNoteAmount);
					balanceDue.setScale(currencyVo.getNoOfDecimalsForAmount(), RoundingMode.HALF_UP);
					creditNotesDetailsVo.setAvailableAmount(balanceDue.doubleValue() + "");
				}
				creditNotesDetailsVo.setType("Credit Notes");
				creditNoteIds += rs.getInt(1) + ",";
				listCreditDetails.add(creditNotesDetailsVo);
			}
			
			// Exclude current receipt invoices
			if (listCreditDetails != null && listCreditDetails.size() > 0 && creditNoteIds.length() > 1) {
				mainQuery.append(" and id NOT IN (");
				mainQuery.append(creditNoteIds);
				mainQuery.setLength(mainQuery.length() - 1);// Removing last ,
				mainQuery.append(")");
			}

			closeResources(rs, preparedStatement, null);
			}
			preparedStatement = con.prepareStatement(mainQuery.toString());
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
			logger.error("Error during getCreditNotesForCustomerCurrency", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return listCreditDetails;
	}
	
	public String getReceiptNumber( int receiptId)
			throws ApplicationException {
		logger.info("To get the getReceiptNumber");
		 Connection con = null;
 		String receiptNo = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			con = getAccountsReceivableConnection();
			preparedStatement = con.prepareStatement(ReceiptConstants.GET_RECEIPT_NO_BY_ID);
			preparedStatement.setInt(1, receiptId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				receiptNo = rs.getString(1);
			}

		} catch (Exception e) {
			logger.info("Error in getReceiptNumber", e);
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, con);
		}

		return receiptNo;
	}

	
}
