package com.blackstrawai.ar;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.ar.receipt.ReceiptVo;
import com.blackstrawai.common.BaseDao;
import com.blackstrawai.common.CommonConstants;

@Repository
public class ArBalanceUpdateDao extends BaseDao {

	private Logger logger = Logger.getLogger(ArBalanceUpdateDao.class);

	public boolean updateInvoiceDueBalance(Integer invoiceId) throws ApplicationException {
		logger.info("Entry into updateInvoiceDueBalance:");
		boolean result = false;
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			if (invoiceId > 0) {
				BigDecimal total = new BigDecimal("0");
				String status = null;
				LocalDate dueDate = null;
				LocalDate invoiceDate = null;
				String pendingApprovalStatus = null;
				conn = getAccountsReceivableConnection();
				preparedStatement = conn.prepareStatement(ArInvoiceConstants.GET_BASIC_INVOICE_BY_ID);
				preparedStatement.setInt(1, invoiceId);
				rs = preparedStatement.executeQuery();
				while (rs.next()) {
					if (rs.getString(1) != null) {
						total = new BigDecimal(rs.getString(1));
						total = total.setScale(2, BigDecimal.ROUND_HALF_EVEN);
					}
					status = rs.getString(2);
					dueDate = rs.getDate(3) != null ? rs.getDate(3).toLocalDate() : null;
					invoiceDate = rs.getDate(4) != null ? rs.getDate(4).toLocalDate() : null;
					rs.getInt(5);
					pendingApprovalStatus = rs.getString(6);
				}

				if (invoiceDate != null && total != null && status != null && dueDate != null
						&& !status.equalsIgnoreCase(CommonConstants.STATUS_AS_VOID)
						&& !status.equalsIgnoreCase(CommonConstants.STATUS_AS_INACTIVE)) {
					LocalDate presentDate = LocalDate.now();
					logger.info("******Dates: presentDate: " + presentDate + ",invoiceDate:" + invoiceDate + ",dueDate:"
							+ dueDate);
					BigDecimal totalActiveReceipts = getTotalAmountOfInvoiceFromActiveReceipts(invoiceId);
					BigDecimal totalAmtCreditNote = getTotalAmountOfInvoiceFromCreditNote(invoiceId);
					BigDecimal totalAmtRefund = getTotalAmountOfInvoiceFromRefund(invoiceId,
							RefundConstants.REFUND_TYPE_RECEIPT);
					logger.info("***********updateInvoiceDueBalance ,invoiceId: " + invoiceId + ",Invoice total:"
							+ total + "totalActiveReceipts:" + totalActiveReceipts.doubleValue()
							+ ",totalAmtCreditNote:" + totalAmtCreditNote.doubleValue() + ",totalAmtRefund"
							+ totalAmtRefund.doubleValue());
					BigDecimal balanceDue = total.subtract(totalActiveReceipts).subtract(totalAmtCreditNote)
							.add(totalAmtRefund);
					logger.info("inside updateInvoiceDueBalance ,balanceDue:" + balanceDue.doubleValue() + ",invoiceId:"
							+ invoiceId + ",total:" + total.doubleValue());
					if (balanceDue.doubleValue() >= 0 && total.doubleValue() >= balanceDue.doubleValue()) {// Should not and not greater than total	 be -value
						// amount
						
						if (balanceDue.compareTo(BigDecimal.ZERO) == 0) {
							status = CommonConstants.STATUS_AS_PAID;
						} else if ((presentDate.compareTo(invoiceDate) == 0) && balanceDue.compareTo(total) == 0) {
							if (pendingApprovalStatus == null) {
								status = CommonConstants.STATUS_AS_OPEN;
							}
						} else if (presentDate.isAfter(invoiceDate)
								&& (presentDate.isBefore(dueDate) || presentDate.compareTo(dueDate) == 0)
								&& balanceDue.equals(total)) {
							status = CommonConstants.STATUS_AS_UNPAID;
						} else if ((presentDate.isBefore(dueDate) || presentDate.compareTo(dueDate) == 0)
								&& balanceDue.compareTo(BigDecimal.ZERO) == 1 && balanceDue.compareTo(total) == -1) {
							status = CommonConstants.STATUS_AS_PARTIALLY_PAID;
						} else if (presentDate.isAfter(dueDate) && balanceDue.compareTo(BigDecimal.ZERO) == 1) {
							status = CommonConstants.STATUS_AS_OVERDUE;
						} else if (presentDate.isBefore(invoiceDate) && balanceDue.compareTo(total) == 0) {
							if (pendingApprovalStatus == null) {
								status = CommonConstants.STATUS_AS_OPEN;
							}
						}

						closeResources(rs, preparedStatement, conn);
						conn = getAccountsReceivableConnection();
						preparedStatement = conn.prepareStatement(ArInvoiceConstants.UPDATE_INVOICE_DUE_BALANCE);
						preparedStatement.setDouble(1, balanceDue.doubleValue());
						preparedStatement.setDate(2, new Date(System.currentTimeMillis()));
						preparedStatement.setString(3, status);
						preparedStatement.setInt(4, invoiceId);
						preparedStatement.executeUpdate();
						int rowAffected = preparedStatement.executeUpdate();
						if (rowAffected == 1) {
							result = true;
							logger.info("Successfully updated invoice balance!");
						}
						logger.info("After updateInvoiceDueBalance ,invoiceId:" + invoiceId + ",balanceDue:"
								+ balanceDue.doubleValue() + ",status:" + status);

					}else {
						logger.info("NOt able to update Invoice due balance,INvoice Balance due is greater than total");
					}
				}

				// Updating all creditnotes due balance for Invoice
				List<Integer> creditNotes = getAllActiveCreditNotesForInvoice(invoiceId);
				for (Integer creditNoteId : creditNotes) {
					updateCreditNotesDueBalance(creditNoteId);
				}
				// Updating all Receipts due balance for Invoice
				List<Integer> receipts = getAllActiveReceiptsForInvoice(invoiceId);
				for (Integer receiptId : receipts) {
					updateReceiptDueBalance(receiptId);
				}
			}
		} catch (Exception e) {
			logger.info("Error in updateInvoiceDueBalance:: ", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(null, preparedStatement, conn);
		}
		return result;
	}

	public boolean updateCreditNotesDueBalance(Integer creditNotesId) throws ApplicationException {
		logger.info("Entry into updateCreditNotesDueBalance:");
		boolean result = false;
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			if (creditNotesId != null) {
				BigDecimal total = new BigDecimal("0");
				BigDecimal invoiceDueBalance = new BigDecimal("0");
				String status = null;
				String pendingApprovalStatus = null;
				int originalInvoiceId = 0;

				conn = getAccountsReceivableConnection();
				preparedStatement = conn.prepareStatement(CreditNoteConstants.GET_BASIC_CREDIT_NOTE_BY_ID);
				preparedStatement.setInt(1, creditNotesId);
				rs = preparedStatement.executeQuery();
				while (rs.next()) {
					total = rs.getBigDecimal(1);
					status = rs.getString(2);
					originalInvoiceId = rs.getInt(3);
					pendingApprovalStatus = rs.getString(4);
					rs.getInt(5);
					invoiceDueBalance = rs.getBigDecimal(6);
				}
				logger.info("inside updateCreditNotesDueBalance update,invoiceDueBalance:" + invoiceDueBalance
						+ ",creditNotesId:" + creditNotesId + ",total:" + total);
				closeResources(rs, preparedStatement, conn);
				if (originalInvoiceId > 0 && total != null && status != null
						&& !status.equalsIgnoreCase(CommonConstants.STATUS_AS_DRAFT)
						&& !status.equalsIgnoreCase(CommonConstants.STATUS_AS_VOID)
						&& !status.equalsIgnoreCase(CommonConstants.STATUS_AS_INACTIVE)) {
					total = total.setScale(2, RoundingMode.HALF_UP);
					invoiceDueBalance = invoiceDueBalance != null ? invoiceDueBalance.setScale(2, RoundingMode.HALF_UP)
							: new BigDecimal(0);
					if (invoiceDueBalance.doubleValue() > 0) {
						// Exclude this credi note
						invoiceDueBalance = invoiceDueBalance.add(total);
					}
					BigDecimal totalFromAF = getTotalAmountFromApplicationFundsForType(creditNotesId, "Credit Notes");
					BigDecimal totalFromRefund = getTotalAmountOfInvoiceFromRefund(originalInvoiceId,
							RefundConstants.REFUND_TYPE_CREDIT_NOTES);
					BigDecimal totalFromReceipt = getTotalReceiptAmountForModule(creditNotesId,	"Credit Notes","Credit Note");
					logger.info("***********updateCreditNotesDueBalance ,creditNotesId: " + creditNotesId
							+ ",CreditNote total:" + total + ",Invoice Due Balance:" + invoiceDueBalance
							+ "totalFromAF:" + totalFromAF.doubleValue() + ",totalFromRefund"
							+ totalFromRefund.doubleValue() + ",totalFromReceipt:" + totalFromReceipt.doubleValue());
					BigDecimal balanceDue = total.subtract(totalFromAF).subtract(totalFromRefund)
							.subtract(totalFromReceipt);
					logger.info("inside updateCreditNotesDueBalance update,balanceDue:" + balanceDue + ",creditNotesId:"
							+ creditNotesId + ",total:" + total);
					if (balanceDue.doubleValue() >= 0 && total.doubleValue() >= balanceDue.doubleValue()) {// Should not
						// be -value
						// and not
						// greater
						// than
						// total
						// amount
						conn = getAccountsReceivableConnection();
						preparedStatement = conn.prepareStatement(CreditNoteConstants.UPDATE_CN_DUE_BALANCE);
						preparedStatement.setBigDecimal(1, balanceDue);
						logger.info("after updateCreditNotesDueBalance update,  balanceDue:" + balanceDue
								+ ",creditNotesId:" + creditNotesId + ",total:" + total);
						if (balanceDue.doubleValue() == 0) {
							status = CommonConstants.STATUS_AS_ADJUSTED;
						} else if (balanceDue.doubleValue() > 0 && balanceDue.doubleValue() < total.doubleValue()) {
							status = CommonConstants.STATUS_AS_PARTIALLY_ADJUSTED;
						} else if (balanceDue.doubleValue() > 0 && total.doubleValue() > 0 && balanceDue != null
								&& (balanceDue.doubleValue() == total.doubleValue())) {
							if (pendingApprovalStatus == null) {
								status = CommonConstants.STATUS_AS_OPEN;
							}
						}

						preparedStatement.setDate(2, new Date(System.currentTimeMillis()));
						preparedStatement.setString(3, status);
						preparedStatement.setInt(4, creditNotesId);
						preparedStatement.executeUpdate();
						int rowAffected = preparedStatement.executeUpdate();
						if (rowAffected == 1) {
							result = true;
							logger.info("Successfully updated creditNotes balance");
						}
					}
				}
			}
		} catch (Exception e) {
			logger.info("Error in updateCreditNotesDueBalance:: ", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(null, preparedStatement, conn);
		}
		return result;
	}

	public boolean updateReceiptDueBalance(Integer receiptId) throws ApplicationException {
		logger.info("Entry into updateReceiptDueBalance:");
		boolean result = false;
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			if (receiptId != null) {
				BigDecimal total = new BigDecimal("0");
				ReceiptVo receipt = getMinimalReceiptDetailsById(receiptId);
				String status = receipt.getStatus();
				conn = getAccountsReceivableConnection();
				BigDecimal advanceAmount = getAdvOrOnAccountAmountForReceipt(receiptId,	ReceiptConstants.AR_RECEIPT_TYPE_CREATE_NEW_ADVANCE, conn);
				BigDecimal onAccountAmount = getAdvOrOnAccountAmountForReceipt(receiptId,ReceiptConstants.AR_RECEIPT_TYPE_CREATE_NEW_ON_ACCOUNT, conn);
				
				BigDecimal advanceRefundAmount =getTotalAmountOfInvoiceFromRefundForReceipt(ReceiptConstants.AR_RECEIPT_ADVANCE_PAYMENT_REFERENCE,receiptId);
				BigDecimal onAccountRefundAmount =getTotalAmountOfInvoiceFromRefundForReceipt(ReceiptConstants.AR_RECEIPT_ON_ACCOUNT_REFERENCE,receiptId);
				logger.info("updateReceiptDueBalance ,advanceAmount:" + advanceAmount + ",receiptId:" + receiptId+ ",onAccountAmount:" + onAccountAmount);
				closeResources(rs, preparedStatement, conn);
				
				BigDecimal onAccountAmoutAppliedOtherRec =getTotalReceiptAmountForModule(receiptId, ReceiptConstants.AR_RECEIPT_TYPE_ON_ACCOUNT,ReceiptConstants.AR_RECEIPT_TYPE_ADVANCE_PAYMENT);
				BigDecimal advanceAmoutAppliedOtherRec =getTotalReceiptAmountForModule(receiptId, ReceiptConstants.AR_RECEIPT_TYPE_ADVANCE_PAYMENT,ReceiptConstants.AR_RECEIPT_TYPE_ON_ACCOUNT);

				BigDecimal appliedAmountFromAfForAdvance=getTotalAmountFromApplicationFundsForType(receiptId, ReceiptConstants.AR_RECEIPT_TYPE_CREATE_NEW_ADVANCE);
				BigDecimal appliedAmountFromAfForOnAccount=getTotalAmountFromApplicationFundsForType(receiptId, ReceiptConstants.AR_RECEIPT_TYPE_CREATE_NEW_ON_ACCOUNT);
				
				total = advanceAmount.add(onAccountAmount);
				
				BigDecimal appliedFundAmount = onAccountAmoutAppliedOtherRec.add(advanceAmoutAppliedOtherRec)
						.add(onAccountAmoutAppliedOtherRec).add(advanceAmoutAppliedOtherRec).add(advanceRefundAmount).add(onAccountRefundAmount);
				
				logger.info("****** updateReceiptDueBalance update , receiptId:" + receiptId+ ", receiptIdappliedFundAmount:" + appliedFundAmount + ",total:" + total);
				if (total.doubleValue() >= appliedFundAmount.doubleValue()) {// Should not be -value and not greater
					// than total amount
					
					BigDecimal balanceDue = total.subtract(appliedFundAmount);
					logger.info("Before updateReceiptDueBalance update,appliedFundAmount:" + appliedFundAmount
							+ ",receiptId:" + receiptId + ",total:" + total + ",status" + status + ",balanceDue"
							+ balanceDue);
					if (balanceDue.doubleValue() >= 0 && total.doubleValue() >= balanceDue.doubleValue()) {// Should not be -value and not greater than total

						if (balanceDue.doubleValue() == 0) {
							if (total.doubleValue() > 0) {
								status = CommonConstants.STATUS_AS_ADJUSTED;
							} else {
								status = CommonConstants.STATUS_AS_ACTIVE;
							}
						} else if (balanceDue.doubleValue() > 0 && balanceDue.doubleValue() < total.doubleValue()) {
							status = CommonConstants.STATUS_AS_PARTIALLY_ADJUSTED;
						} else if (balanceDue.doubleValue() > 0 && total.doubleValue() > 0 && balanceDue != null
								&& (balanceDue.doubleValue()) == (total.doubleValue())) {
							status = CommonConstants.STATUS_AS_ACTIVE;
						}
						advanceAmount=advanceAmount.subtract((advanceAmoutAppliedOtherRec.add(advanceRefundAmount).add(appliedAmountFromAfForAdvance)));
						onAccountAmount=onAccountAmount.subtract((onAccountAmoutAppliedOtherRec.add(onAccountRefundAmount).add(appliedAmountFromAfForOnAccount)));
						conn = getAccountsReceivableConnection();
						preparedStatement = conn.prepareStatement(ReceiptConstants.UPDATE_RECEIPT_DUE_BALANCE);
						preparedStatement.setBigDecimal(1, advanceAmount);
						preparedStatement.setBigDecimal(2, onAccountAmount);
						preparedStatement.setBigDecimal(3, appliedFundAmount);
						preparedStatement.setString(4, status);
						preparedStatement.setDate(5, new Date(System.currentTimeMillis()));
						preparedStatement.setInt(6, receiptId);
						preparedStatement.executeUpdate();
						int rowAffected = preparedStatement.executeUpdate();
						if (rowAffected == 1) {
							result = true;
							logger.info("Successfully updated ReceiptDueBalance");
						}
						logger.info("after updateReceiptDueBalance update, appliedFundAmount:" + appliedFundAmount+ ",receiptId:" + receiptId + ",total:" + total + ",status" + status + ",balanceDue"
								+ balanceDue);

					}
				}
				updateInvoicesRefundBalanceForReceipt(receiptId);//Update invoice refund balances
			}
		} catch (Exception e) {
			logger.info("Error in updateReceiptDueBalance:: ", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(null, preparedStatement, conn);
		}
		return result;
	}

	public boolean updateInvoicesRefundBalanceForReceipt(Integer receiptId) throws ApplicationException {
		logger.info("Entry into updateReceiptDueBalance:");
		boolean result = false;
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			if (receiptId != null) {
				BigDecimal total = new BigDecimal("0");
				conn = getAccountsReceivableConnection();
				preparedStatement = conn.prepareStatement(ReceiptConstants.GET_ALL_INVOICES_FOR_RECEIPT);
				preparedStatement.setInt(1, receiptId);
				preparedStatement.setString(2, ReceiptConstants.AR_RECEIPT_TYPE_INVOICE);
				preparedStatement.setString(3, ReceiptConstants.AR_RECEIPT_TYPE_CREATE_NEW_ADVANCE);
				preparedStatement.setString(4, ReceiptConstants.AR_RECEIPT_TYPE_CREATE_NEW_ON_ACCOUNT);
				rs = preparedStatement.executeQuery();
				while (rs.next()) {
					int receiptDetailId=rs.getInt(1);
					int invoiceId=rs.getInt(2);
					total = rs.getBigDecimal(3);
					total = total.setScale(2, BigDecimal.ROUND_HALF_EVEN);
					BigDecimal refundAmount=getTotalAmountOfInvoiceFromRefundForReceipt(invoiceId,receiptId);
					
					BigDecimal maxRefundableAmount=total.subtract(refundAmount);
					String referenceType=rs.getString(4);
					if(referenceType!=null) {
					//If it is on account/advance payment get onacc/adv amount an susbsttract from the balance
					
					if(referenceType.equalsIgnoreCase(ReceiptConstants.AR_RECEIPT_TYPE_CREATE_NEW_ADVANCE)) {
						
						BigDecimal advanceAmoutAppliedOtherRec =getTotalReceiptAmountForModule(receiptId, ReceiptConstants.AR_RECEIPT_TYPE_ADVANCE_PAYMENT,
								ReceiptConstants.AR_RECEIPT_TYPE_ADVANCE_PAYMENT);
						BigDecimal appliedAmountFromAfForAdvance=getTotalAmountFromApplicationFundsForType(receiptId, ReceiptConstants.AR_RECEIPT_TYPE_CREATE_NEW_ADVANCE);
						maxRefundableAmount =maxRefundableAmount.subtract((advanceAmoutAppliedOtherRec.add(appliedAmountFromAfForAdvance)));	
					}else if(referenceType.equalsIgnoreCase(ReceiptConstants.AR_RECEIPT_TYPE_CREATE_NEW_ON_ACCOUNT)) {
						BigDecimal onAccountAmoutAppliedOtherRec =getTotalReceiptAmountForModule(receiptId, ReceiptConstants.AR_RECEIPT_TYPE_ON_ACCOUNT,
								ReceiptConstants.AR_RECEIPT_TYPE_ON_ACCOUNT);
						BigDecimal appliedAmountFromAfForOnAccount=getTotalAmountFromApplicationFundsForType(receiptId, ReceiptConstants.AR_RECEIPT_TYPE_CREATE_NEW_ON_ACCOUNT);
						maxRefundableAmount =maxRefundableAmount.subtract((onAccountAmoutAppliedOtherRec.add(appliedAmountFromAfForOnAccount)));	
					}
					logger.info("Max Refundable Amount:"+maxRefundableAmount+" ,For receipt:"+receiptId+",type:"+referenceType);
					}
					preparedStatement = conn.prepareStatement(ReceiptConstants.UPDATE_RECEIPT_INVOICE_REFUND_AMOUNT);
					preparedStatement.setBigDecimal(1, maxRefundableAmount);
					preparedStatement.setDate(2, new Date(System.currentTimeMillis()));
					preparedStatement.setInt(3, receiptDetailId);
					int rowAffected = preparedStatement.executeUpdate();
					if (rowAffected == 1) {
						result = true;
						logger.info("Successfully updated RefundDueBalance");
					}
				}
			}
		} catch (Exception e) {
			logger.info("Error in updateReceiptDueBalance:: ", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, conn);
		}
		return result;
	}

	
	public BigDecimal getTotalAmountOfInvoiceFromActiveReceipts(int invoiceId) throws ApplicationException {
		logger.info("Entry into in getTotalAmountOfInvoiceFromActiveReceipts");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		BigDecimal total = new BigDecimal("0");
		try {
			// Get Total Amount Of Invoice From all active Receipts
			con = getAccountsReceivableConnection();
			preparedStatement = con.prepareStatement(ReceiptConstants.GET_TOTAL_FOR_INVOICE_FROM_RECEIPTS);
			preparedStatement.setInt(1, invoiceId);
			preparedStatement.setString(2, CommonConstants.STATUS_AS_ACTIVE);
			preparedStatement.setString(3, CommonConstants.STATUS_AS_OPEN);
			preparedStatement.setString(4, CommonConstants.STATUS_AS_ADJUSTED);
			preparedStatement.setString(5, CommonConstants.STATUS_AS_PARTIALLY_ADJUSTED);
			preparedStatement.setString(6, ReceiptConstants.AR_RECEIPT_TYPE_INVOICE);
			rs = preparedStatement.executeQuery();
			logger.info(preparedStatement.toString());
			while (rs.next()) {
				if (rs.getString(1) != null) {
					total = rs.getBigDecimal(1);
					total = total.setScale(2, BigDecimal.ROUND_HALF_EVEN);
				}
			}

			logger.info("getTotalAmountOfInvoiceFromActiveReceipts : Fetched total amount:" + total
					+ ",For  invoice id:" + invoiceId);
		} catch (Exception e) {
			logger.error("Error in getTotalAmountOfInvoiceFromActiveReceipts", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return total;

	}

	public List<Integer> getAllActiveReceiptsForInvoice(int invoiceId) throws ApplicationException {
		logger.info("Entry into in getAllActiveReceiptsForInvoice");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		List<Integer> receipts = new ArrayList<Integer>();
		try {
			// Get Total Amount Of Invoice From all active Receipts
			con = getAccountsReceivableConnection();
			preparedStatement = con.prepareStatement(ReceiptConstants.GET_ALL_ACTIVE_RECEIPTS_FOR_ORG_INVOICE);
			preparedStatement.setInt(1, invoiceId);
			preparedStatement.setString(2, CommonConstants.STATUS_AS_ACTIVE);
			preparedStatement.setString(3, CommonConstants.STATUS_AS_OPEN);
			preparedStatement.setString(4, CommonConstants.STATUS_AS_ADJUSTED);
			preparedStatement.setString(5, CommonConstants.STATUS_AS_PARTIALLY_ADJUSTED);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				receipts.add(rs.getInt(1));
			}
			logger.info("Fetched Receipts :" + receipts + ",For  invoiceId:" + invoiceId);
		} catch (Exception e) {
			logger.error("Error in getAllActiveReceiptsForInvoice", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return receipts;

	}

	public BigDecimal getTotalAmountOfInvoiceFromCreditNote(int invoiceId) throws ApplicationException {
		logger.info("Entry into in getTotalAmountOfInvoiceFromCreditNote");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		BigDecimal total = new BigDecimal("0");
		try {
			// Get Total Amount Of Invoice From all active Receipts
			con = getAccountsReceivableConnection();
			preparedStatement = con.prepareStatement(CreditNoteConstants.GET_TOTAL_FOR_INVOICE_FROM_CN);
			preparedStatement.setInt(1, invoiceId);
			preparedStatement.setString(2, CommonConstants.STATUS_AS_ACTIVE);
			preparedStatement.setString(3, CommonConstants.STATUS_AS_OPEN);
			preparedStatement.setString(4, CommonConstants.STATUS_AS_ADJUSTED);
			preparedStatement.setString(5, CommonConstants.STATUS_AS_PARTIALLY_ADJUSTED);
			

			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				if (rs.getString(1) != null) {
					total = new BigDecimal(rs.getString(1));
					total = total.setScale(2, BigDecimal.ROUND_HALF_EVEN);
				}
			}
			logger.info("getTotalAmountOfInvoiceFromCreditNote :Fetched total amount:" + total + ",For  invoice id:"
					+ invoiceId);
		} catch (Exception e) {
			logger.error("Error in getTotalAmountOfInvoiceFromCreditNote", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return total;

	}

	private List<Integer> getAllActiveCreditNotesForInvoice(int invoiceId) throws ApplicationException {
		logger.info("Entry into in getAllActiveCreditNotesForInvoice");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		List<Integer> creditNoteslist = new ArrayList<Integer>();
		try {
			// Get Total Amount Of Invoice From all active Receipts
			con = getAccountsReceivableConnection();
			preparedStatement = con.prepareStatement(CreditNoteConstants.GET_ALL_ACT_CN_FOR_INVOICE);
			preparedStatement.setInt(1, invoiceId);
			preparedStatement.setString(2, CommonConstants.STATUS_AS_ACTIVE);
			preparedStatement.setString(3, CommonConstants.STATUS_AS_OPEN);
			preparedStatement.setString(4, CommonConstants.STATUS_AS_ADJUSTED);
			preparedStatement.setString(5, CommonConstants.STATUS_AS_PARTIALLY_ADJUSTED);

			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				creditNoteslist.add(rs.getInt(1));
			}
			logger.info("Fetched total creditnotes :" + creditNoteslist + ",For  invoice id:" + invoiceId);
		} catch (Exception e) {
			logger.error("Error in getAllActiveCreditNotesForInvoice", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return creditNoteslist;

	}

	public Double getTotalAmountOfCreditNote(int creditNoteId) throws ApplicationException {
		logger.info("Entry into in getTotalAmountOfCreditNote");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		Double totalInvoiceValue = 0.0;
		try {
			// Get Total Amount Of Invoice From all active Receipts
			con = getAccountsReceivableConnection();
			preparedStatement = con.prepareStatement(CreditNoteConstants.GET_TOTAL_AMOINT_FOR_CREDITNOTE);
			preparedStatement.setInt(1, creditNoteId);

			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				totalInvoiceValue = rs.getDouble(1);
			}
			logger.info("Fetched total amount:" + totalInvoiceValue + ",For  creditNote Id :" + creditNoteId);
		} catch (Exception e) {
			logger.error("Error in getTotalAmountOfCreditNote", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return totalInvoiceValue;

	}

	public BigDecimal getTotalAmountOfInvoiceFromRefund(int invoiceId, String refundType) throws ApplicationException {
		logger.info("Entry into in getTotalAmountOfInvoiceFromRefund");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		BigDecimal total = new BigDecimal("0");
		try {
			// Get Total Amount Of Invoice From all active Receipts
			con = getAccountsPayable();
			preparedStatement = con.prepareStatement(RefundConstants.GET_TOTAL_FOR_INVOICE_FROM_REFUND);
			preparedStatement.setInt(1, invoiceId);
			preparedStatement.setString(2, CommonConstants.STATUS_AS_ACTIVE);
			preparedStatement.setString(3, refundType);
			preparedStatement.setString(4, CommonConstants.STATUS_AS_ACTIVE);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				if (rs.getString(1) != null) {
					total = rs.getBigDecimal(1).setScale(2, BigDecimal.ROUND_HALF_EVEN);
				}
			}
			logger.info("getTotalAmountOfInvoiceFromRefund :Fetched total amount:" + total + ",For  invoice id:"
					+ invoiceId + ",Type:" + refundType);
		} catch (Exception e) {
			logger.error("Error in getTotalAmountOfInvoiceFromRefund", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return total;

	}
	
	public BigDecimal getTotalAmountOfInvoiceFromRefundForReceipt(int invoiceId, int receipId) throws ApplicationException {
		logger.info("Entry into in getTotalAmountOfInvoiceFromRefundForReceipt");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		BigDecimal total = new BigDecimal("0");
		try {
			// Get Total Amount Of Invoice From all active Receipts
			con = getAccountsPayable();
			preparedStatement = con.prepareStatement(RefundConstants.GET_TOTAL_FOR_INVOICE_FROM_REFUND_FOR_RECEIPT);
			preparedStatement.setInt(1, invoiceId);
			preparedStatement.setInt(2, receipId);
			preparedStatement.setString(3, CommonConstants.STATUS_AS_ACTIVE);
			preparedStatement.setString(4, RefundConstants.REFUND_TYPE_RECEIPT);
			preparedStatement.setString(5, ReceiptConstants.AR_RECEIPT_TYPE_ON_ACCOUNT);
			preparedStatement.setString(6, ReceiptConstants.AR_RECEIPT_TYPE_ADVANCE_PAYMENT);
			preparedStatement.setString(7, CommonConstants.STATUS_AS_ACTIVE);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				if (rs.getString(1) != null) {
					total = rs.getBigDecimal(1).setScale(2, BigDecimal.ROUND_HALF_EVEN);
				}
			}
			logger.info("getTotalAmountOfInvoiceFromRefundForReceipt :Fetched total amount:" + total + ",For  invoice id:"
					+ invoiceId + ",receipId:" + receipId);
		} catch (Exception e) {
			logger.error("Error in getTotalAmountOfInvoiceFromRefundForReceipt", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return total;

	}

	public BigDecimal getTotalAmountFromApplicationFundsForType(int referenceId, String type)
			throws ApplicationException {
		logger.info("Entry into in getTotalAmountFromApplicationFundsForType");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		BigDecimal totalInvoiceValue = new BigDecimal("0");
		try {
			// Get Total Amount Of Invoice From all active AF
			con = getAccountsReceivableConnection();
			preparedStatement = con.prepareStatement(RefundConstants.GET_TOTAL_FOR_CREDIT_NOTE_FROM_AF);
			preparedStatement.setInt(1, referenceId);// Either Receipt Id/Credit noteId
			preparedStatement.setString(2, type);
			preparedStatement.setString(3, CommonConstants.STATUS_AS_ACTIVE);

			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				if (rs.getString(1) != null) {
					totalInvoiceValue = rs.getBigDecimal(1).setScale(2, BigDecimal.ROUND_HALF_EVEN);
				}
			}
			logger.info("getTotalAmountFromApplicationFundsForType : Fetched amount:" + totalInvoiceValue
					+ ",For  referenceId id:" + referenceId);
		} catch (Exception e) {
			logger.error("Error in getTotalAmountFromApplicationFundsForType", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return totalInvoiceValue;

	}

	public BigDecimal getAdvOrOnAccountAmountForReceipt(int receiptId, String type, Connection con)
			throws ApplicationException {
		logger.info("To get the getAdvOrOnAccountAmountForReceipt");
		BigDecimal totalAmount = new BigDecimal("0");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			con = getAccountsReceivableConnection();
			preparedStatement = con.prepareStatement(ReceiptConstants.GET_ON_ACCOUNT_ADV_PAYMENT_AMOUNT_FROM_RECEIPT);
			preparedStatement.setInt(1, receiptId);
			preparedStatement.setString(2, type);
			preparedStatement.setString(3, ReceiptConstants.AR_RECEIPT_TYPE_INVOICE);
			preparedStatement.setString(4, CommonConstants.STATUS_AS_DRAFT);
			preparedStatement.setString(5, CommonConstants.STATUS_AS_VOID);
			preparedStatement.setString(6, CommonConstants.STATUS_AS_DELETE);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				if (rs.getString(1) != null) {
					totalAmount = rs.getBigDecimal(1).setScale(2, BigDecimal.ROUND_HALF_EVEN);
				}

			}

		} catch (Exception e) {
			logger.info("Error in getAdvOnAccountAmountForReceipt", e);
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, null);
		}
		return totalAmount;
	}

	public ReceiptVo getMinimalReceiptDetailsById(int receiptId) throws ApplicationException {
		logger.info("Entry into method: getMinimalReceiptDetailsById");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		ReceiptVo receiptVo = new ReceiptVo();
		try {
			con = getAccountsReceivableConnection();
			String query = ReceiptConstants.GET_MINIMAL_RECEIPT_BY_ID;
			logger.info(query);
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, receiptId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				receiptVo.setId(rs.getInt(1));
				receiptVo.setReceiptType(rs.getString(2));
				receiptVo.setReceiptNo(rs.getString(3));
				receiptVo.setStatus(rs.getString(4));
				logger.info("getMinimalReceiptDetailsById: Sucessfully Feteched Receipt:" + receiptVo);
			}
		} catch (Exception e) {
			logger.error("Error during getMinimalReceiptDetailsById", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return receiptVo;

	}

	private BigDecimal getTotalReceiptAmountForModule(int id, String referenceType, String parentReferenceType)
			throws ApplicationException {
		logger.info("Entry into method: getTotalReceiptAmountForModule");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		BigDecimal total = new BigDecimal("0");
		try {
			con = getAccountsReceivableConnection();

			String query = ReceiptConstants.GET_TOTAL_AMOUNT_FOR_MODULE;
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, id);
			preparedStatement.setString(2, referenceType);
			preparedStatement.setString(3, parentReferenceType);
			preparedStatement.setString(4, CommonConstants.STATUS_AS_DRAFT);
			preparedStatement.setString(5, CommonConstants.STATUS_AS_VOID);
			preparedStatement.setString(6, CommonConstants.STATUS_AS_DELETE);
			;
			rs = preparedStatement.executeQuery();
			logger.info(preparedStatement.toString());
			while (rs.next()) {
				if (rs.getString(1) != null) {
					total = rs.getBigDecimal(1).setScale(2, BigDecimal.ROUND_HALF_EVEN);
					;
				}
			}
			logger.info("getTotalReceiptAmountForModule : Fetched Amount :" + total + " ,for ID:" + id + ",Type :"
					+ referenceType);
		} catch (Exception e) {
			logger.error("Error during getTotalCreditNoteAmountForReceipt", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return total;
	}

}
