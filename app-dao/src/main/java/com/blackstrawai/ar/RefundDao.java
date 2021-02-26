package com.blackstrawai.ar;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.DateConverter;
import com.blackstrawai.ap.dropdowns.BasicCurrencyVo;
import com.blackstrawai.ar.creditnotes.BasicCreditNotesVo;
import com.blackstrawai.ar.dropdowns.BasicInvoiceDetailsVo;
import com.blackstrawai.ar.receipt.BasicReceiptVo;
import com.blackstrawai.ar.refund.RefundVo;
import com.blackstrawai.attachments.AttachmentsConstants;
import com.blackstrawai.attachments.AttachmentsDao;
import com.blackstrawai.attachments.AttachmentsVo;
import com.blackstrawai.attachments.UploadFileVo;
import com.blackstrawai.common.BaseDao;
import com.blackstrawai.common.CommonConstants;
import com.blackstrawai.common.FinanceCommonDao;
import com.blackstrawai.onboarding.OrganizationDao;
import com.blackstrawai.settings.CurrencyDao;
import com.blackstrawai.settings.CurrencyVo;

@Repository
public class RefundDao extends BaseDao {

	private Logger logger = Logger.getLogger(RefundDao.class);

	@Autowired
	private OrganizationDao organizationDao;	


	@Autowired
	private FinanceCommonDao financeCommonDao;

	@Autowired
	private AttachmentsDao attachmentsDao;

	@Autowired
	private CurrencyDao currencyDao;

	public RefundVo createRefund(RefundVo refundVo) throws ApplicationException {
		logger.info("Entry into method: createRefund");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = getAccountsReceivableConnection();
			String sql = RefundConstants.INSERT_INTO_REFUND_ORGANIZATION;
			preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			String refundReference =""; 
			if(refundVo.getRefundReference()!=null) {
				String refundReferenceNo = refundVo.getRefundReferenceNo();
				if(refundReferenceNo!=null) {
					String refFormated="~"+refundReferenceNo+"~"; 
					refundReference = refundVo.getRefundReference().replaceAll(refundReferenceNo,refFormated);
				}
			}			
			boolean isRefundExist=checkRefundExist(refundReference,refundVo.getOrganizationId(),con);
			if(isRefundExist){
				throw new Exception("Refund Reference Exist for the Organization");
			}
			//(date_of_refund,customer_id,invoice_reference_id,payment_mode_id,refund_reference,amount,ledger_id,ledger_name,organization_id,user_id,isSuperAdmin)values(?,?,?,?,?,?,?,?,?,?,?,?)
			preparedStatement.setString(1, DateConverter.getInstance().correctDatePickerDateToString(refundVo.getDateOfRefund()));
			preparedStatement.setInt(2, refundVo.getCustomerId());
			preparedStatement.setInt(3, refundVo.getInvoiceReferenceId());
			preparedStatement.setInt(4, refundVo.getPaymentModeId());
			preparedStatement.setString(5, refundReference);
			preparedStatement.setString(6, refundVo.getAmount());
			preparedStatement.setInt(7, refundVo.getLedgerId());
			preparedStatement.setString(8, refundVo.getLedgerName());			
			preparedStatement.setInt(9, refundVo.getLocationId());
			preparedStatement.setBoolean(10, refundVo.getIsRegistered());
			preparedStatement.setString(11, refundVo.getGstNumber());
			preparedStatement.setInt(12, refundVo.getCurrencyId());
			preparedStatement.setString(13, refundVo.getCurrencySymbol());			
			preparedStatement.setInt(14, refundVo.getOrganizationId());
			preparedStatement.setString(15, refundVo.getUserId());
			preparedStatement.setBoolean(16, refundVo.getIsSuperAdmin());
			preparedStatement.setInt(17, refundVo.getPaymentAccountId());
			preparedStatement.setString(18, refundVo.getRoleName());			
			preparedStatement.setInt(19, refundVo.getCreditNoteId());
			preparedStatement.setInt(20,refundVo.getReceiptId());
			preparedStatement.setInt(21,refundVo.getRefundTypeId());
			preparedStatement.setString(22,refundVo.getExchangeRate());
			int rowAffected = preparedStatement.executeUpdate();
			if (rowAffected == 1) {
				rs = preparedStatement.getGeneratedKeys();
				if (rs.next()) {
					refundVo.setId(rs.getInt(1));

				}
			}
			if(refundVo.getAttachments()!=null && refundVo.getAttachments().size()>0) {
				attachmentsDao.createAttachments(refundVo.getOrganizationId(),refundVo.getUserId(),refundVo.getAttachments(), AttachmentsConstants.MODULE_TYPE_ACCOUNTS_RECEIVABLES_REFUND, refundVo.getId(),refundVo.getRoleName());
			}


		} catch (Exception e) {
			logger.error("Error while creating Refund",e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return refundVo;
	}

	public RefundVo updateRefund(RefundVo refundVo) throws ApplicationException {
		logger.info("Entry into method: updateRefund");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		/*		RefundVo refunVoExisting=getRefundById(refundVo.getId());
		 */		try {
			 con = getAccountsReceivableConnection();
			 String sql = RefundConstants.UPDATE_REFUND_ORGANIZATION;
			 preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			 preparedStatement.setString(1, DateConverter.getInstance().correctDatePickerDateToString(refundVo.getDateOfRefund()));
			 preparedStatement.setInt(2, refundVo.getCustomerId());
			 preparedStatement.setInt(3, refundVo.getInvoiceReferenceId());
			 preparedStatement.setInt(4, refundVo.getPaymentModeId());
			 String refundReference =""; 
			 if(refundVo.getRefundReference()!=null) {
				 String refundReferenceNo = refundVo.getRefundReferenceNo();
				 if(refundReferenceNo!=null) {
					 refundReference = refundVo.getRefundReference().replaceAll(refundReferenceNo, "~"+refundReferenceNo+"~");
				 }
			 }
			 preparedStatement.setString(5, refundReference);
			 preparedStatement.setString(6, refundVo.getAmount());
			 preparedStatement.setInt(7, refundVo.getLedgerId());
			 preparedStatement.setString(8, refundVo.getLedgerName());
			 preparedStatement.setInt(9, refundVo.getLocationId());
			 preparedStatement.setBoolean(10, refundVo.getIsRegistered());
			 preparedStatement.setString(11, refundVo.getGstNumber());
			 preparedStatement.setInt(12, refundVo.getCurrencyId());
			 preparedStatement.setString(13, refundVo.getCurrencySymbol());				
			 preparedStatement.setInt(14, refundVo.getOrganizationId());
			 preparedStatement.setString(15, refundVo.getUserId());
			 preparedStatement.setBoolean(16, refundVo.getIsSuperAdmin());
			 preparedStatement.setTimestamp(17, new Timestamp(System.currentTimeMillis()));
			 preparedStatement.setInt(18, refundVo.getPaymentAccountId());
			 preparedStatement.setString(19, refundVo.getRoleName());
			 preparedStatement.setInt(20,refundVo.getCreditNoteId());
			 preparedStatement.setInt(21,refundVo.getReceiptId());
			 preparedStatement.setInt(22,refundVo.getRefundTypeId());
			 preparedStatement.setString(23,refundVo.getExchangeRate());
			 preparedStatement.setInt(24, refundVo.getId());

			 int rowAffected = preparedStatement.executeUpdate();
			 if (rowAffected == 1) {				
				 rs = preparedStatement.getGeneratedKeys();

			 }
			 if(refundVo.getAttachments()!=null && refundVo.getAttachments().size()>0) {
				 attachmentsDao.createAttachments(refundVo.getOrganizationId(),refundVo.getUserId(),refundVo.getAttachments(), AttachmentsConstants.MODULE_TYPE_ACCOUNTS_RECEIVABLES_REFUND, refundVo.getId(),refundVo.getRoleName());
			 }

			 if(refundVo.getAttachmentsToRemove()!=null && refundVo.getAttachmentsToRemove().size()>0) {
				 for(Integer attachmentId : refundVo.getAttachmentsToRemove()) {
					 attachmentsDao.changeStatusForSingleAttachment(attachmentId, CommonConstants.STATUS_AS_DELETE,refundVo.getUserId(),refundVo.getRoleName());  
				 }
			 }


		 } catch (Exception e) {
			 logger.error("Error while updating Refund",e);
			 throw new ApplicationException(e);
		 } finally {
			 closeResources(rs, preparedStatement, con);
		 }		return refundVo;
	}

	public RefundVo deleteRefund(int id,String status,String userId,String roleName) throws ApplicationException {
		logger.info("Entry into method: deleteRefund");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;

		RefundVo refundVo = new RefundVo();
		try {			

			con = getAccountsReceivableConnection();
			String sql = RefundConstants.DELETE_REFUND_ORGANIZATION;
			preparedStatement = con.prepareStatement(sql);
			preparedStatement.setString(1, status);
			preparedStatement.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
			preparedStatement.setString(3, userId);
			preparedStatement.setString(4, roleName);
			preparedStatement.setInt(5, id);
			refundVo.setId(id);
			preparedStatement.executeUpdate();


		} catch (Exception e) {
			logger.error("Error while deleting Refund",e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return refundVo;
	}

	public List<RefundVo> getAllRefundsOfAnOrganization(int organizationId,String userId,String roleName) throws ApplicationException {
		logger.info("Entry into method: getAllRefundsOfAnOrganization");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		List<RefundVo> listRefunds = new ArrayList<RefundVo>();
		try {

			con = getAccountsReceivableConnection();
			String query ="";
			if(roleName.equals("Super Admin")){
				query= RefundConstants.GET_REFUND_BY_ORGANIZATION;
			}else{
				query = RefundConstants.GET_REFUND_BY_ORGANIZATION_USER_ROLE;
			}

			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, organizationId);
			if(!(roleName.equals("Super Admin"))){
				preparedStatement.setString(2, userId);
				preparedStatement.setString(3, roleName);
			}		
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				RefundVo refundVo = new RefundVo();
				refundVo.setId(rs.getInt(1));
				Connection con1=getUserMgmConnection();
				String dateFormat=organizationDao.getDefaultDateForOrganization(organizationId, con1);
				closeResources(null, null, con1);
				refundVo.setDateOfRefund(rs.getDate(2)!=null?DateConverter.getInstance().convertDateToGivenFormat(rs.getDate(2), dateFormat):null);
				refundVo.setCustomerId(rs.getInt(3));
				refundVo.setCustomerName(rs.getString(4));
				refundVo.setInvoiceReferenceId(rs.getInt(5));
				if(rs.getString(6)!=null) {					
					String[] refNum =rs.getString(6).split("~");
					if(refNum.length>2) {
						refundVo.setInvoiceReference(refNum[0]+"/"+refNum[1]+"/"+refNum[2]);
					}
				}


				refundVo.setPaymentModeId(rs.getInt(7));
				refundVo.setPaymentMode(rs.getString(8));
				if(rs.getString(9)!=null) {

					String[] refNum =rs.getString(9).split("~");
					if(refNum.length>2) {

						refundVo.setRefundReference(refNum[0]+"/"+refNum[1]+"/"+refNum[2]);
						refundVo.setRefundReferenceNo(refNum[1]);
					}
				}
				refundVo.setAmount( rs.getString(13)+""+rs.getString(10));
				refundVo.setLedgerId(rs.getInt(11));
				refundVo.setLedgerName(rs.getString(12));
				refundVo.setOrganizationId(rs.getInt(14));
				refundVo.setUserId(rs.getString(15));
				refundVo.setIsSuperAdmin(rs.getBoolean(16));
				refundVo.setCreateTs(rs.getTimestamp(17));
				refundVo.setUpdateTs(rs.getTimestamp(18));							
				refundVo.setStatus(rs.getString(19));
				refundVo.setRoleName(rs.getString(20));
				listRefunds.add(refundVo);
			}
		} catch (Exception e) {
			logger.error("Error while retrieving All Refunds information",e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return listRefunds;
	}



	public RefundVo getRefundById(int id) throws ApplicationException {
		logger.info("Entry into method: getRefundById");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		RefundVo refundVo = new RefundVo();
		try {

			con = getAccountsReceivableConnection();
			String query = RefundConstants.GET_REFUND_BY_ID_ORGANIZATION;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, id);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {

				refundVo.setId(rs.getInt(1));
				refundVo.setDateOfRefund(rs.getString(2));
				refundVo.setCustomerId(rs.getInt(3));
				refundVo.setCustomerName(rs.getString(4));
				refundVo.setInvoiceReferenceId(rs.getInt(5));
				refundVo.setInvoiceReference(rs.getString(6));
				refundVo.setPaymentModeId(rs.getInt(7));
				refundVo.setPaymentMode(rs.getString(8));
				if(rs.getString(9)!=null) {

					String[] refNum =rs.getString(9).split("~");
					if(refNum.length>2) {

						refundVo.setRefundReference(refNum[0]+"/"+refNum[1]+"/"+refNum[2]);
						refundVo.setRefundReferenceNo(refNum[1]);
					}
				}

				refundVo.setAmount( rs.getString(10));
				refundVo.setLedgerId(rs.getInt(11));
				refundVo.setLedgerName(rs.getString(12));
				refundVo.setLocationId(rs.getInt(13));
				refundVo.setIsRegistered(rs.getBoolean(14));
				refundVo.setGstNumber(rs.getString(15));
				refundVo.setCurrencyId(rs.getInt(16));
				refundVo.setCurrencySymbol(rs.getString(17));
				refundVo.setOrganizationId(rs.getInt(18));
				refundVo.setUserId(rs.getString(19));
				refundVo.setIsSuperAdmin(rs.getBoolean(20));
				refundVo.setCreateTs(rs.getTimestamp(21));
				refundVo.setUpdateTs(rs.getTimestamp(22));							
				refundVo.setStatus(rs.getString(23));
				refundVo.setPaymentAccountId(rs.getInt(24));
				refundVo.setRoleName(rs.getString(25));
				refundVo.setCreditNoteId(rs.getInt(26));
				refundVo.setReceiptId(rs.getInt(27));
				refundVo.setRefundTypeId(rs.getInt(28));
				refundVo.setExchangeRate(rs.getString(29));
				String invoiceType=financeCommonDao.getInvoiceType(rs.getInt(5));
				if(invoiceType!=null) {
					if(invoiceType.equalsIgnoreCase("Local")) {
						refundVo.setLocal(true);	
					}else {
						refundVo.setLocal(false);
					}
				}
			}
			List<UploadFileVo> uploadFileVos = new ArrayList<UploadFileVo>();
			for(AttachmentsVo attachments : attachmentsDao.getAttachmentDetails(refundVo.getId(), AttachmentsConstants.MODULE_TYPE_ACCOUNTS_RECEIVABLES_REFUND))
			{
				UploadFileVo uploadFileVo = new UploadFileVo();
				uploadFileVo.setId(attachments.getId());
				uploadFileVo.setName(attachments.getFileName());
				uploadFileVo.setSize(attachments.getSize());
				uploadFileVos.add(uploadFileVo);
			}
			refundVo.setAttachments(uploadFileVos);
		} catch (Exception e) {
			logger.error("Error while retrieving Refund information for id:"+id,e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return refundVo;
	}

	private boolean checkRefundExist(String refundReference,int organizationId,Connection conn) throws ApplicationException {
		logger.info("Entry into method: checkTaxRateExist");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			String query = RefundConstants.CHECK_REFUND_ORGANIZATION;
			preparedStatement = conn.prepareStatement(query);
			preparedStatement.setInt(1, organizationId);
			preparedStatement.setString(2, refundReference);
			rs = preparedStatement.executeQuery();
			if (rs.next()) {
				return true;
			}
		} catch (Exception e) {
			logger.error("Error in checkRefundExist",e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, null);
		}
		return false;
	}


	public int getInvoiceForRefund(int refundId) throws ApplicationException {
		logger.info("To get the getInvoiceForRefund");
		int invoiceId = 0;
		Connection connection = null;
		PreparedStatement preparedStatement =null;
		ResultSet rs =null;		
		try  {
			connection = getAccountsReceivableConnection();
			preparedStatement = connection.prepareStatement(RefundConstants.GET_INVOICE_FOR_REFUND);
			preparedStatement.setInt(1, refundId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				invoiceId = rs.getInt(1);
			}
		} catch (Exception e) {
			logger.info("Error in getInvoiceForRefund", e);
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, connection);
		}
		return invoiceId;
	}

	public List<BasicInvoiceDetailsVo> getDropdownInvoicesForRefund(int organizationId, int customerId,int refundId)
			throws ApplicationException, SQLException {
		logger.info("Entry into method : getInvoicesForARRefund");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		List<BasicInvoiceDetailsVo> invoicesList = new ArrayList<BasicInvoiceDetailsVo>();
		try {
			StringBuilder sqlQuery = new StringBuilder(RefundConstants.GET_ALL_REFUND_ELIGIBLE_INVOICES_FOR_ORG);
			if (refundId > 0) {// IF It s from Get by ID
				BasicInvoiceDetailsVo refundInvoice = getAllInvoicesForRefund(organizationId,refundId);
				if(refundInvoice!=null && refundInvoice.getId()>0) {
					sqlQuery.append(" and id NOT IN ("+refundInvoice.getId()+")");//Exclude refund invoices
					invoicesList.add(refundInvoice);
				}
			}			
			con = getAccountsReceivableConnection();
			preparedStatement = con.prepareStatement(sqlQuery.toString());
			logger.info(sqlQuery.toString());
			preparedStatement.setInt(1, organizationId);
			preparedStatement.setInt(2, customerId);
			preparedStatement.setString(3, CommonConstants.STATUS_AS_PAID);
			preparedStatement.setString(4, CommonConstants.STATUS_AS_PARTIALLY_PAID);
			rs = preparedStatement.executeQuery();

			while (rs.next()) {
				BasicInvoiceDetailsVo arVo = new BasicInvoiceDetailsVo();
				arVo.setId(rs.getInt(1));
				arVo.setName(rs.getString(2) != null ? rs.getString(2).replace("~", "/") : null);
				arVo.setValue(rs.getInt(1) + "");
				arVo.setCreditNotesList(getCreditNotesForInvoice(arVo.getId()));
				arVo.setReceiptsList(getReceiptsForInvoice(arVo.getId()));
				arVo.setCurrencyId(rs.getInt(5));
				Connection con1=getUserMgmConnection();
				BasicCurrencyVo basicCurrencyVo = currencyDao.getBasicCurrencyForOrganization(organizationId,
						con1);
				closeResources(null, null, con1);
				arVo.setOrganizationBasecurrencyId(basicCurrencyVo != null ? basicCurrencyVo.getId() : 0);
				boolean isLocal=false;
				if(rs.getString(6)!=null && rs.getString(6).equalsIgnoreCase("Local")) {
					isLocal=true;
				}
				arVo.setLocal(isLocal);
				invoicesList.add(arVo);
			}


			logger.info("Invoices List size:" + invoicesList.size());
		} catch (Exception e) {
			logger.info("Error in getInvoicesForARRefund:: ", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);

		}

		return invoicesList;
	}



	public List<BasicCreditNotesVo> getCreditNotesForInvoice(int invoiceId) throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		List<BasicCreditNotesVo> listCreditNotes = new ArrayList<BasicCreditNotesVo>();
		try {
			connection = getAccountsReceivableConnection();
			preparedStatement = connection.prepareStatement(RefundConstants.GET_CREDITNOTES_FOR_INVOICE);
			preparedStatement.setInt(1, invoiceId);

			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				BasicCreditNotesVo basicCreditNotesVo = new BasicCreditNotesVo();
				basicCreditNotesVo.setId(rs.getInt(1));
				basicCreditNotesVo.setCreditNoteNumber(rs.getString(2));

				CurrencyVo currencyVo = currencyDao.getCurrency(rs.getInt(4));
				if ( currencyVo != null) {

					BigDecimal bd = new BigDecimal(rs.getDouble(3))
							.setScale(currencyVo.getNoOfDecimalsForAmount(), RoundingMode.HALF_UP);
					basicCreditNotesVo.setBalanceDue(bd.doubleValue()+"");	
				}

				listCreditNotes.add(basicCreditNotesVo);
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, connection);
		}
		return listCreditNotes;
	}

	public List<BasicReceiptVo> getReceiptsForInvoice(int invoiceId) throws ApplicationException {
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		List<BasicReceiptVo> receipts = new ArrayList<BasicReceiptVo>();
		try {
			con = getAccountsReceivableConnection();
			preparedStatement = con.prepareStatement(RefundConstants.GET_RECEIPTS_FOR_INVOICE);
			preparedStatement.setInt(1, invoiceId);

			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				BasicReceiptVo receiptVo = new BasicReceiptVo();
				receiptVo.setId(rs.getInt(1));
				receiptVo.setName(rs.getString(2));
				receiptVo.setInvoiceAmount(getInvoiceAmountFromReceiptForInvoice(invoiceId,rs.getInt(3)));
				receipts.add(receiptVo);
			}
			// Add On Account and Advance payment option in list
			BasicReceiptVo arVoOnAccount = new BasicReceiptVo();
			arVoOnAccount.setId(ReceiptConstants.AR_RECEIPT_ON_ACCOUNT_REFERENCE);
			arVoOnAccount.setName(ReceiptConstants.AR_RECEIPT_TYPE_CREATE_NEW_ON_ACCOUNT);	
			arVoOnAccount.setCurrencyEditable(true);
			receipts.add(arVoOnAccount);
			BasicReceiptVo arVoAdvPayment = new BasicReceiptVo();
			arVoAdvPayment.setId(ReceiptConstants.AR_RECEIPT_ADVANCE_PAYMENT_REFERENCE);
			arVoAdvPayment.setName(ReceiptConstants.AR_RECEIPT_TYPE_CREATE_NEW_ADVANCE);
			arVoAdvPayment.setCurrencyEditable(true);
			receipts.add(arVoAdvPayment);
		} catch (Exception e) {
			logger.error("Error during getAllReceiptsOfAnOrganization", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}

		return receipts;
	}

	private double getInvoiceAmountFromReceiptForInvoice(int invoiceId,int currencyId) throws ApplicationException {
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		double invoiceAmount=0.0;
		try {
			con = getAccountsReceivableConnection();
			preparedStatement = con.prepareStatement(RefundConstants.GET_INVOICE_AMOUT_FROM_RECEIPTS_FOR_INVOICE);
			preparedStatement.setInt(1, invoiceId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				invoiceAmount=rs.getDouble(1);				
			}
			CurrencyVo currencyVo = currencyDao.getCurrency(currencyId);
			if ( currencyVo != null) {

				BigDecimal bd = new BigDecimal(invoiceAmount)
						.setScale(currencyVo.getNoOfDecimalsForAmount(), RoundingMode.HALF_UP);
				invoiceAmount=bd.doubleValue();	
			}
		} catch (Exception e) {
			logger.error("Error during getAllReceiptsOfAnOrganization", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}

		return invoiceAmount;
	}

	public BasicInvoiceDetailsVo getAllInvoicesForRefund(int organizationId,int refundId) throws ApplicationException {
		logger.info("Entry into method: getAllInvoicesForRefund");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		BasicInvoiceDetailsVo basicInvoiceDetailsVo = new BasicInvoiceDetailsVo();
		try {
			con = getAccountsReceivableConnection();
			String query = RefundConstants.GET_INVOICES_FOR_REFUND;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, refundId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				if (rs.getString(1) != null) {

					basicInvoiceDetailsVo.setId(rs.getInt(1));
					basicInvoiceDetailsVo.setName(rs.getString(2) != null ? rs.getString(2).replace("~", "/") : null);
					basicInvoiceDetailsVo.setValue(rs.getInt(1) + "");
					basicInvoiceDetailsVo.setCurrencyId(rs.getInt(3));
					Connection con1=getUserMgmConnection();
					BasicCurrencyVo basicCurrencyVo = currencyDao.getBasicCurrencyForOrganization(organizationId,
							con1);
					closeResources(null,null, con1);
					basicInvoiceDetailsVo.setOrganizationBasecurrencyId(basicCurrencyVo != null ? basicCurrencyVo.getId() : 0);
					boolean isLocal=false;
					if(rs.getString(4)!=null && rs.getString(4).equalsIgnoreCase("Local")) {
						isLocal=true;
					}
					basicInvoiceDetailsVo.setLocal(isLocal);
					Double refundAmount=rs.getDouble(5);
					String refundType=rs.getString(6);
					basicInvoiceDetailsVo.setReceiptsList(getReceiptsForInvoice(basicInvoiceDetailsVo.getId()));
					basicInvoiceDetailsVo.setCreditNotesList(getCreditNotesForInvoice(basicInvoiceDetailsVo.getId()));


					//To reset the balance due with exclusion of current refund amount
					if(refundType!=null) {
						if(refundType.equalsIgnoreCase(RefundConstants.REFUND_TYPE_RECEIPT)) {

							//TODO:WHether refund can be raised multiple times against same invoice of receipt?
							//							int receiptId=rs.getInt(8);
							//							List<BasicReceiptVo> receiptsList=basicInvoiceDetailsVo.getReceiptsList();
							//							if(receiptsList!=null && receiptsList.size()>0) {
							//							receiptsList.stream().forEach(receipt -> {
							//								if(receipt.getId()==receiptId) {
							//									
							//									receipt.setInvoiceAmount(receipt.getInvoiceAmount()+refundAmount);
							//								}
							//							});
							//							basicInvoiceDetailsVo.setReceiptsList(receiptsList);
							//							}
						}else if(refundType.equalsIgnoreCase(RefundConstants.REFUND_TYPE_CREDIT_NOTES)) {
							int creditNoteId=rs.getInt(7);
							List<BasicCreditNotesVo> creditNoteList=basicInvoiceDetailsVo.getCreditNotesList();		
							if(creditNoteList!=null && creditNoteList.size()>0) {
								creditNoteList.stream().forEach(creditNote -> {
									if(creditNote.getId()==creditNoteId) {

										double balanceDue=refundAmount;
										if(creditNote.getBalanceDue()!=null) {
											balanceDue+=Double.parseDouble(creditNote.getBalanceDue());
										}
										creditNote.setBalanceDue(balanceDue+"");
									}
								});

								basicInvoiceDetailsVo.setCreditNotesList(creditNoteList);
							}
						}

					}




				}
			}

		} catch (Exception e) {
			logger.error("Error during getAllInvoicesForReceipt", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return basicInvoiceDetailsVo;
	}
}
