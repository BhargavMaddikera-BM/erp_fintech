package com.blackstrawai.ap;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.ap.payment.noncore.PaymentInvoiceDetailsVo;
import com.blackstrawai.ap.payment.noncore.PaymentNonCoreBaseVo;
import com.blackstrawai.ap.payment.noncore.PaymentNonCoreVo;
import com.blackstrawai.ar.ArInvoiceConstants;
import com.blackstrawai.ar.ArInvoiceDao;
import com.blackstrawai.ar.ReceiptConstants;
import com.blackstrawai.ar.RefundConstants;
import com.blackstrawai.ar.applycredits.InvoiceDetailsVo;
import com.blackstrawai.ar.invoice.ArInvoiceGeneralInformationVo;
import com.blackstrawai.ar.invoice.ArInvoiceVo;
import com.blackstrawai.common.BaseDao;
import com.blackstrawai.common.CommonConstants;

@Repository
public class NonCoreCustomerRefundDao extends BaseDao {

	@Autowired
	private PaymentNonCoreDao paymentDao;
	@Autowired
	private ArInvoiceDao arInvoiceDao;

	private Logger logger = Logger.getLogger(NonCoreCustomerRefundDao.class);

	public List<InvoiceDetailsVo> getInvoiceDetailsByCustomerCurrencyId(int orgId, int customerId, int currencyId,
			int paymentId) throws ApplicationException {
		logger.info("To get the getInvoiceDetailsByCustomerId");
		List<InvoiceDetailsVo> invoiceList = new ArrayList<InvoiceDetailsVo>();
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement preparedStatement = null;
		try {
			con = getAccountsReceivableConnection();
			StringBuilder sqlQuery = new StringBuilder(ArInvoiceConstants.GET_INVOICE_DETAILS_FOR_CUSTOMER_CURRENCY);
			String invoiceIds = "";
			Map<Integer,InvoiceDetailsVo> visitedInvoices = new HashMap<Integer,InvoiceDetailsVo>();
			if (paymentId > 0) {
				PaymentNonCoreVo paymentVo = new PaymentNonCoreVo();
				paymentVo.setId(paymentId);
				Connection conAP = getAccountsPayable();
				paymentVo = paymentDao.fetchCustomerRefunds(paymentVo, conAP);
				closeResources(null, null, conAP);
				for (PaymentNonCoreBaseVo payment : paymentVo.getPayments()) {
					if (payment.getInvoiceId() > 0 && !visitedInvoices.containsKey(payment.getInvoiceId())) {
						ArInvoiceVo invoiceVo = new ArInvoiceVo();
						invoiceVo.setInvoiceId(payment.getInvoiceId());
						invoiceIds += payment.getInvoiceId() + ",";
						arInvoiceDao.getInvoiceGeneralInfoById(invoiceVo, con);
						InvoiceDetailsVo invoiceDetailsVo = new InvoiceDetailsVo();
						invoiceDetailsVo.setId(payment.getInvoiceId());
						invoiceDetailsVo.setValue(invoiceDetailsVo.getId());
						if (invoiceVo.getGeneralInformation() != null) {
							ArInvoiceGeneralInformationVo genInfo = invoiceVo.getGeneralInformation();
							String invoiceNo = genInfo.getInvoiceNoPrefix() + "/" + genInfo.getInvoiceNumber()
							+ "/" + genInfo.getInvoiceNoSuffix();
							invoiceDetailsVo.setName(invoiceNo);
							invoiceDetailsVo.setInvoiceNo(invoiceNo);
							invoiceDetailsVo.setAmount(Double.toString(genInfo.getBalanceDue()));
						}
						invoiceList.add(invoiceDetailsVo);
						visitedInvoices.put(payment.getInvoiceId(), invoiceDetailsVo);
					}
				}
				if (invoiceList != null && invoiceList.size() > 0 && invoiceIds.length() > 1) {
					sqlQuery.append(" and id NOT IN (");
					sqlQuery.append(invoiceIds);
					sqlQuery.setLength(sqlQuery.length() - 1);// Removing last ,
					sqlQuery.append(")");
				}
			}
			preparedStatement = con.prepareStatement(sqlQuery.toString());
			preparedStatement.setInt(1, orgId);
			preparedStatement.setInt(2, customerId);
			preparedStatement.setInt(3, currencyId);
			preparedStatement.setString(4, CommonConstants.STATUS_AS_PARTIALLY_PAID);
			preparedStatement.setString(5, CommonConstants.STATUS_AS_PAID);
			preparedStatement.setString(6, CommonConstants.STATUS_AS_OVERDUE);
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
			logger.info("Error in getInvoiceDetailsByCustomerCurrencyId", e);
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return invoiceList;
	}

	public List<PaymentInvoiceDetailsVo> getReceiptByInvoiceId(int organizationId, int customerId, int currencyId,Integer invoiceId, String invoiceNo, int paymentId)
			throws ApplicationException {
		logger.info("Entry into getReceiptByInvoiceId method");
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		List<PaymentInvoiceDetailsVo> receipts = new ArrayList<PaymentInvoiceDetailsVo>();
		if (invoiceId != null) {
			try {
				connection = getAccountsReceivableConnection();
				StringBuilder sqlQuery = new StringBuilder(ReceiptConstants.GET_RECEIPT_BY_INVOICE_ID); 
				String invoiceIds = "";
				if (paymentId > 0) {
					PaymentNonCoreVo paymentVo = new PaymentNonCoreVo();
					paymentVo.setId(paymentId);
					Connection conAP = getAccountsPayable();
					List<PaymentNonCoreBaseVo> refunds = paymentDao.fetchCustomerRefundsByPaymentIdAndReference(paymentId,RefundConstants.REFUND_TYPE_RECEIPT, conAP);
					closeResources(null, null, conAP);
					
					for (PaymentNonCoreBaseVo payment : refunds) {
						if (payment.getReferenceType() != null
								&& payment.getReferenceType().equalsIgnoreCase(RefundConstants.REFUND_TYPE_RECEIPT)) {
							invoiceIds += payment.getReferenceId() + ",";
							PaymentInvoiceDetailsVo paymentDetailsVo = getMinimalReciptDetailsByReference(
									organizationId, customerId, currencyId, payment,ReceiptConstants.AR_RECEIPT_TYPE_INVOICE);
							paymentDetailsVo.setInvoiceId(invoiceId);
							paymentDetailsVo.setInvoiceNumber(invoiceNo);
							receipts.add(paymentDetailsVo);
						}

					}
					
					if (receipts != null && receipts.size() > 0 && invoiceIds.length() > 1) {
						sqlQuery.append(" and rec.id NOT IN (");
						sqlQuery.append(invoiceIds);
						sqlQuery.setLength(sqlQuery.length() - 1);// Removing last ,
						sqlQuery.append(")");
					}
				}
				logger.info(sqlQuery.toString());
				preparedStatement = connection.prepareStatement(sqlQuery.toString());
				preparedStatement.setInt(1, invoiceId);
				preparedStatement.setString(2, ReceiptConstants.AR_RECEIPT_TYPE_INVOICE);
				preparedStatement.setString(3, CommonConstants.STATUS_AS_DELETE);
				preparedStatement.setString(4, CommonConstants.STATUS_AS_ACTIVE);
				preparedStatement.setString(5, CommonConstants.STATUS_AS_OPEN);
				preparedStatement.setString(6, CommonConstants.STATUS_AS_PARTIALLY_ADJUSTED);
				rs = preparedStatement.executeQuery();
				while (rs.next()) {
					PaymentInvoiceDetailsVo receipt = new PaymentInvoiceDetailsVo();
					receipt.setId(rs.getInt(1));
					receipt.setInvoiceId(invoiceId);
					receipt.setInvoiceNumber(invoiceNo);
					receipt.setName(rs.getString(2));
					receipt.setValue(rs.getInt(1));
					receipt.setAmount(rs.getString(3));
					receipts.add(receipt);
				}

			} catch (Exception e) {
				logger.info("Error in getReceiptByInvoiceId:: ", e);
				throw new ApplicationException(e);
			} finally {
				closeResources(rs, preparedStatement, connection);
			}
		}
		return receipts;

	}



	public List<PaymentInvoiceDetailsVo> getCreditNoteByInvoiceId(Integer invoiceId, String invoiceNo, int paymentId)
			throws ApplicationException {

		logger.info("Entry into getCreditNoteByInvoiceId method");
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		List<PaymentInvoiceDetailsVo> creditNotes = new ArrayList<PaymentInvoiceDetailsVo>();
		if (invoiceId != null) {
			try {
				connection = getAccountsReceivableConnection();
				StringBuilder sqlQuery = new StringBuilder(ArInvoiceConstants.GET_CREDIT_NOTE_BY_INVOICE_ID); 
				String invoiceIds = "";
				if (paymentId > 0) {
					PaymentNonCoreVo paymentVo = new PaymentNonCoreVo();
					paymentVo.setId(paymentId);
					Connection conAP = getAccountsPayable();
					paymentVo = paymentDao.fetchCustomerRefunds(paymentVo, conAP);
					closeResources(null, null, conAP);
					for (PaymentNonCoreBaseVo payment : paymentVo.getPayments()) {
						if (payment.getReferenceType() != null && payment.getReferenceType().equalsIgnoreCase(RefundConstants.REFUND_TYPE_CREDIT_NOTES)
								&& payment.getInvoiceId() == invoiceId) {
							invoiceIds += payment.getReferenceId() + ",";
							PaymentInvoiceDetailsVo invoiceDetailsVo = getCreditNoteByReferenceId(payment.getReferenceId(), invoiceId, invoiceNo, connection);

							creditNotes.add(invoiceDetailsVo);
						}
					}
					if (creditNotes != null && creditNotes.size() > 0 && invoiceIds.length() > 1) {
						sqlQuery.append(" and id NOT IN (");
						sqlQuery.append(invoiceIds);
						sqlQuery.setLength(sqlQuery.length() - 1);// Removing last ,
						sqlQuery.append(")");
					}
				}
				logger.info(sqlQuery.toString());
				preparedStatement = connection.prepareStatement(sqlQuery.toString());
				preparedStatement.setInt(1, invoiceId);
				preparedStatement.setString(2, CommonConstants.STATUS_AS_ACTIVE);
				preparedStatement.setString(3, CommonConstants.STATUS_AS_OPEN);
				preparedStatement.setString(4, CommonConstants.STATUS_AS_PARTIALLY_ADJUSTED);
				rs = preparedStatement.executeQuery();
				while (rs.next()) {
					PaymentInvoiceDetailsVo creditNote = new PaymentInvoiceDetailsVo();
					creditNote.setId(rs.getInt(1));
					creditNote.setInvoiceId(invoiceId);
					creditNote.setInvoiceNumber(invoiceNo);
					creditNote.setName(rs.getString(2));
					creditNote.setValue(rs.getInt(1));
					creditNote.setAmount(rs.getString(3));
					creditNotes.add(creditNote);
				}

			} catch (Exception e) {
				logger.info("Error in getWorkflowRequiredDataForInvoiceById:: ", e);
				throw new ApplicationException(e);
			} finally {
				closeResources(rs, preparedStatement, connection);
			}
		}
		return creditNotes;

	}
	
	public PaymentInvoiceDetailsVo getCreditNoteByReferenceId(Integer referenceId, Integer invoiceId, String invoiceNo, Connection connection)
			throws ApplicationException {

		logger.info("Entry into getCreditNoteByReferenceId method");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		PaymentInvoiceDetailsVo creditNote = new PaymentInvoiceDetailsVo();
		if (invoiceId != null) {
			try {
				preparedStatement = connection.prepareStatement(ArInvoiceConstants.GET_CREDIT_NOTE_BY_REFERENCE_ID);
				preparedStatement.setInt(1, invoiceId);
				preparedStatement.setInt(2, referenceId);
				rs = preparedStatement.executeQuery();
				while (rs.next()) {					
					creditNote.setId(rs.getInt(1));
					creditNote.setInvoiceId(invoiceId);
					creditNote.setInvoiceNumber(invoiceNo);
					creditNote.setName(rs.getString(2));
					creditNote.setValue(rs.getInt(1));
					creditNote.setAmount(rs.getString(3));					
				}
			} catch (Exception e) {
				logger.info("Error in getCreditNoteByReferenceId:: ", e);
				throw new ApplicationException(e);
			} finally {
				closeResources(rs, preparedStatement, null);
			}
		}
		return creditNote;

	}

	public List<PaymentInvoiceDetailsVo> getOnAccountOrAdvancePaymentReceipts(int organizationId, int customerId, int currencyId, int paymentId,String referenceType)
			throws ApplicationException {

		logger.info("Entry into getOnAccountOrAdvancePaymentReceipts method");
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		List<PaymentInvoiceDetailsVo> onAccounts = new ArrayList<PaymentInvoiceDetailsVo>();

		try {
			connection = getAccountsReceivableConnection();
			StringBuilder sqlQuery = new StringBuilder(ReceiptConstants.GET_ON_ACCOUNT_ADVANCE_RECEIPTS); 
			String receiptIds = "";
			String subType=referenceType!=null && referenceType.equalsIgnoreCase(ReceiptConstants.AR_RECEIPT_TYPE_ON_ACCOUNT)
					? ReceiptConstants.AR_RECEIPT_TYPE_CREATE_NEW_ON_ACCOUNT
					: ReceiptConstants.AR_RECEIPT_TYPE_CREATE_NEW_ADVANCE;
			if (paymentId > 0) {
				Connection conAP = getAccountsPayable();
				List<PaymentNonCoreBaseVo> refunds = paymentDao.fetchCustomerRefundsByPaymentIdAndReference(paymentId,referenceType, conAP);
				closeResources(null, null, conAP);
				
				for (PaymentNonCoreBaseVo payment : refunds) {
					if (payment.getReferenceType() != null
							&& (payment.getReferenceType().equalsIgnoreCase(ReceiptConstants.AR_RECEIPT_TYPE_ON_ACCOUNT)
								|| payment.getReferenceType().equalsIgnoreCase(ReceiptConstants.AR_RECEIPT_TYPE_ADVANCE_PAYMENT))) {
						receiptIds += payment.getReferenceId() + ",";
						PaymentInvoiceDetailsVo paymentDetailsVo = getMinimalReciptDetailsByReference(
								organizationId, customerId, currencyId, payment,subType);

						onAccounts.add(paymentDetailsVo);
					}

				}
				if (onAccounts != null && onAccounts.size() > 0 && receiptIds.length() > 1) {
					sqlQuery.append(" and rbd.receipt_id NOT IN (");
					sqlQuery.append(receiptIds);
					sqlQuery.setLength(sqlQuery.length() - 1);// Removing last ,
					sqlQuery.append(")");
				}
			}
			preparedStatement = connection.prepareStatement(sqlQuery.toString());
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
				PaymentInvoiceDetailsVo onAccount = new PaymentInvoiceDetailsVo();
				onAccount.setId(rs.getInt(1));
				onAccount.setValue(rs.getInt(1));
				onAccount.setInvoiceNumber(rs.getString(2));
				onAccount.setName(rs.getString(2));
				onAccount.setAmount(rs.getString(3));
				onAccounts.add(onAccount);
			}

		} catch (Exception e) {
			logger.info("Error in getOnAccountOrAdvancePaymentReceipts:: ", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, connection);
		}

		return onAccounts;

	}
	
	public PaymentInvoiceDetailsVo getMinimalReciptDetailsByReference(int organizationId, int customerId, int currencyId, PaymentNonCoreBaseVo payment,String referenceType)
			throws ApplicationException {

		logger.info("Entry into getMinimalReciptDetailsByReference method");
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		PaymentInvoiceDetailsVo onAccount = new PaymentInvoiceDetailsVo();

		try {
			connection = getAccountsReceivableConnection();
			preparedStatement = connection.prepareStatement(ReceiptConstants.GET_MINIMAL_RECEIPT_DETAILS_BY_REFERENCE);
			preparedStatement.setInt(1, organizationId);
			preparedStatement.setInt(2, customerId);
			preparedStatement.setString(3, ReceiptConstants.AR_RECEIPT_TYPE_CUSTOMER_PAYMENTS);
			preparedStatement.setInt(4, currencyId);
			preparedStatement.setString(5, CommonConstants.STATUS_AS_ACTIVE);
			preparedStatement.setString(6, referenceType);
			preparedStatement.setInt(7, payment.getReferenceId());
			preparedStatement.setInt(8, payment.getInvoiceId());
			
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				onAccount.setId(rs.getInt(1));
				onAccount.setInvoiceNumber(rs.getString(2));
				onAccount.setName(rs.getString(2));
				onAccount.setValue(rs.getInt(1));
				logger.info(rs.getInt(1)+":Invoice "+payment.getInvoiceAmount()+" : "+rs.getString(3));
				BigDecimal due=new BigDecimal(0);
				if( payment.getInvoiceAmount()!=null && rs.getBigDecimal(3)!=null) {
					due=new BigDecimal(payment.getInvoiceAmount()).add(rs.getBigDecimal(3));
					due.setScale(2, BigDecimal.ROUND_HALF_EVEN);
				}
				
				onAccount.setAmount(due+"");
			}

		} catch (Exception e) {
			logger.info("Error in getMinimalReciptDetailsByReference:: ", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, connection);
		}

		return onAccount;

	}

	

	



}
