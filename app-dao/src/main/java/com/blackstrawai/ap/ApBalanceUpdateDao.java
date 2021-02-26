package com.blackstrawai.ap;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.ar.ReceiptConstants;
import com.blackstrawai.common.BaseDao;
import com.blackstrawai.common.CommonConstants;
import com.blackstrawai.payroll.PayRunConstants;

@Repository
public class ApBalanceUpdateDao extends BaseDao {

	private Logger logger = Logger.getLogger(ApBalanceUpdateDao.class);

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
				conn = getAccountsPayable();
				preparedStatement = conn.prepareStatement(BillsInvoiceConstants.GET_BASIC_INVOICE_BY_ID);
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
					pendingApprovalStatus = rs.getString(6);
				}

				if (invoiceDate != null && total != null && status != null && dueDate != null
						&& !status.equalsIgnoreCase(CommonConstants.STATUS_AS_VOID)
						&& !status.equalsIgnoreCase(CommonConstants.STATUS_AS_INACTIVE)) {
					LocalDate presentDate = LocalDate.now();
					logger.info("******Dates: presentDate: " + presentDate + ",invoiceDate:" + invoiceDate + ",dueDate:"
							+ dueDate);
					BigDecimal totalActiveBillPayments = getTotalAmountOfInvoiceFromActiveBillPayments(invoiceId);
					BigDecimal totalAmtRefund = getTotalAmountOfInvoiceFromVendorRefund(invoiceId);
					logger.info("***********updateInvoiceDueBalance ,invoiceId: " + invoiceId + ",Invoice total:"
							+ total + ",totalActiveBillPayments:" + totalActiveBillPayments.doubleValue()
							+ ",totalAmtRefund" + totalAmtRefund.doubleValue());
					BigDecimal balanceDue = total.subtract(totalActiveBillPayments).add(totalAmtRefund);
					logger.info("inside updateInvoiceDueBalance ,balanceDue:" + balanceDue.doubleValue() + ",invoiceId:"+ invoiceId + ",total:" + total.doubleValue());
					if (balanceDue.doubleValue() >= 0 && total.doubleValue() >= balanceDue.doubleValue()) {// Should not and not greater than total be -value amount

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
						conn = getAccountsPayable();
						conn.setAutoCommit(false);
						preparedStatement = conn.prepareStatement(BillsInvoiceConstants.UPDATE_INVOICE_DUE_BALANCE);
						preparedStatement.setDouble(1, balanceDue.doubleValue());
						preparedStatement.setDate(2, new Date(System.currentTimeMillis()));
						preparedStatement.setInt(3, invoiceId);
						preparedStatement.executeUpdate();
						int rowAffected = preparedStatement.executeUpdate();
						if (rowAffected == 1) {
							result = true;
							logger.info("Successfully updated invoice balance!");
						}
						preparedStatement = conn.prepareStatement(BillsInvoiceConstants.UPDATE_INVOICE_STATUS);
						preparedStatement.setString(1, status);
						preparedStatement.setDate(2, new Date(System.currentTimeMillis()));
						preparedStatement.setInt(3, invoiceId);
						rowAffected = preparedStatement.executeUpdate();
						if (rowAffected == 1) {
							result = true;
							logger.info("Successfully updated invoice Status!");
						}
						conn.commit();
						logger.info("After updateInvoiceDueBalance ,invoiceId:" + invoiceId + ",balanceDue:"
								+ balanceDue.doubleValue() + ",status:" + status);
					} else {
						logger.info("NOt able to update Invoice due balance,INvoice Balance due is greater than total");
					}
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

	public BigDecimal getTotalAmountOfInvoiceFromActiveBillPayments(int invoiceId) throws ApplicationException {
		logger.info("Entry into in getTotalAmountOfInvoiceFromActiveBillPayments");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		BigDecimal total = new BigDecimal("0");
		try {
			// Get Total Amount Of Invoice From all Bill Payments
			con = getAccountsPayable();
			preparedStatement = con.prepareStatement(PaymentNonCoreConstants.GET_TOTAL_INVOICE_AMOUNT_FROM_PAYMENTS);
			preparedStatement.setInt(1, invoiceId);
			preparedStatement.setString(2, CommonConstants.STATUS_AS_ACTIVE);
			preparedStatement.setString(3, PaymentNonCoreConstants.BILL_PAYMENTS_TYPE_BILL);
			rs = preparedStatement.executeQuery();
			logger.info(preparedStatement.toString());
			while (rs.next()) {
				if (rs.getString(1) != null) {
					total = rs.getBigDecimal(1);
					total = total.setScale(2, BigDecimal.ROUND_HALF_EVEN);
				}
			}

			logger.info("getTotalAmountOfInvoiceFromActiveBillPayments : Fetched total amount:" + total
					+ ",For  invoice id:" + invoiceId);
		} catch (Exception e) {
			logger.error("Error in getTotalAmountOfInvoiceFromActiveBillPayments", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return total;

	}
	
	public BigDecimal getTotalAdjustmentsForVendorAdvance(int vendorAdvanceId) throws ApplicationException {
		logger.info("Entry into in getTotalAdjustmentsForVendorAdvance");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		BigDecimal total = new BigDecimal("0");
		try {
			// Get Total Amount Of VednorAdv Id From all Bill Payments
			con = getAccountsPayable();
			preparedStatement = con.prepareStatement(PaymentNonCoreConstants.GET_ADJUSTMENTS_FOR_VENDOR_ADVANCE_BY_ID);
			preparedStatement.setInt(1, vendorAdvanceId);
			preparedStatement.setString(2, PaymentNonCoreConstants.BILL_PAYMENTS_TYPE_ADVANCE);
			preparedStatement.setString(3, CommonConstants.STATUS_AS_ACTIVE);
			preparedStatement.setString(4, CommonConstants.STATUS_AS_ACTIVE);
			rs = preparedStatement.executeQuery();
			logger.info(preparedStatement.toString());
			while (rs.next()) {
				if (rs.getString(1) != null) {
					total = rs.getBigDecimal(1);
					total = total.setScale(2, BigDecimal.ROUND_HALF_EVEN);
				}
			}

			logger.info("getTotalAdjustmentsForVendorAdvance : Fetched total amount:" + total
					+ ",For  vendorAdvanceId id:" + vendorAdvanceId);
		} catch (Exception e) {
			logger.error("Error in getTotalAdjustmentsForVendorAdvance", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return total;

	}

	public BigDecimal getTotalAmountOfInvoiceFromVendorRefund(int invoiceId) throws ApplicationException {
		logger.info("Entry into in getTotalAmountOfInvoiceFromVendorRefund");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		BigDecimal total = new BigDecimal("0");
		try {
			// Get Total Amount Of Invoice From all active Receipts
			con = getAccountsReceivableConnection();
			preparedStatement = con.prepareStatement(ReceiptConstants.GET_TOTAL_FOR_INVOICE_FROM_REFUND);
			preparedStatement.setString(1, CommonConstants.STATUS_AS_ACTIVE);
			preparedStatement.setInt(2, invoiceId);
			preparedStatement.setString(3, CommonConstants.STATUS_AS_ACTIVE);

			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				if (rs.getString(1) != null) {
					total = rs.getBigDecimal(1).setScale(2, BigDecimal.ROUND_HALF_EVEN);
				}
			}
			logger.info("getTotalAmountOfInvoiceFromVendorRefund :Fetched total amount:" + total + ",For  invoice id:"
					+ invoiceId);
		} catch (Exception e) {
			logger.error("Error in getTotalAmountOfInvoiceFromVendorRefund", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return total;

	}

	public boolean updateVendorAdvanceBalance(Integer vendorAdvanceId) throws ApplicationException {
		logger.info("Entry into updateVendorAdvanceBalance:");
		boolean result = false;
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			if (vendorAdvanceId !=null) {
				BigDecimal total = new BigDecimal("0");
				Integer paymentId=null;
				String status=null;
				conn = getAccountsPayable();
				preparedStatement = conn.prepareStatement(PaymentNonCoreConstants.GET_BASIC_VENDOR_ADVANCE_BY_ID);
				preparedStatement.setInt(1, vendorAdvanceId);
				rs = preparedStatement.executeQuery();
				
				while (rs.next()) {
					paymentId=rs.getInt(1);
					status=rs.getString(2);
					if (rs.getString(3) != null) {
						total = rs.getBigDecimal(3);
						total = total.setScale(2, BigDecimal.ROUND_HALF_EVEN);
						
					}
				}
				if ( status!=null && !status.equalsIgnoreCase(CommonConstants.STATUS_AS_DRAFT) &&  !status.equalsIgnoreCase(CommonConstants.STATUS_AS_VOID)) {	
				BigDecimal totalAdjustments = getTotalAdjustmentsForVendorAdvance(vendorAdvanceId);
					BigDecimal balanceDue = total.subtract(totalAdjustments);
					logger.info("inside updateVendorAdvanceBalance ,balanceDue:" + balanceDue.doubleValue() + ",vendorAdvanceId:"+ vendorAdvanceId + ",total:" + total.doubleValue());
					if (balanceDue.doubleValue() >= 0 && total.doubleValue() >= balanceDue.doubleValue()) {// Should not and not greater than total be -value amount

						closeResources(rs, preparedStatement, null);
						preparedStatement = conn.prepareStatement(PaymentNonCoreConstants.UPDATE_AVAILABLE_AMOUNT_VENDOR_ADVANCE);
						preparedStatement.setBigDecimal(1, balanceDue);
						preparedStatement.setInt(2, vendorAdvanceId);
						preparedStatement.executeUpdate();
						int rowAffected = preparedStatement.executeUpdate();
						if (rowAffected == 1) {
							result = true;
							logger.info("Successfully updated Vendor Advance Balance!");
						}
						logger.info("After updateVendorAdvanceBalance ,vendorAdvanceId:" + vendorAdvanceId + ",balanceDue:"	+ balanceDue.doubleValue() );
						if (balanceDue.compareTo(BigDecimal.ZERO) == 0) {
							status = CommonConstants.STATUS_AS_ADJUSTED;
						} else if ( balanceDue.compareTo(BigDecimal.ZERO) == 1 && balanceDue.compareTo(total) == -1) {
							status = CommonConstants.STATUS_AS_PARTIALLY_ADJUSTED;
						}
						if(paymentId!=null) {
						closeResources(rs, preparedStatement, null);
						preparedStatement = conn.prepareStatement(PaymentNonCoreConstants.UPDATE_PAYMENT_STATUS);
						preparedStatement.setString(1, status);
						preparedStatement.setInt(2, paymentId);
						preparedStatement.executeUpdate();
						}

					} else {
						logger.info("Not able to update Vendor Advance Balance,INvoice Balance due is greater than total");
					}
			}
					
				}

		} catch (Exception e) {
			logger.info("Error in updateVendorAdvanceBalance:: ", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(null, preparedStatement, conn);
		}
		return result;
	}
	
	public boolean updatePayRunBalance(Integer payRunId) throws ApplicationException {
		logger.info("Entry into updatePayRunBalance:");
		boolean result = false;
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			if (payRunId != null) {
				int currencyId = 0;
				String status="";
				conn = getPayrollConnection();
				conn.setAutoCommit(false);
				preparedStatement = conn.prepareStatement(PayRunConstants.GET_BASIC_PAYRUN);
				preparedStatement.setInt(1, payRunId);
				rs = preparedStatement.executeQuery();
				while (rs.next()) {
					currencyId = rs.getInt(1);
					status=rs.getString(2);
				}
				if (currencyId > 0) {
					closeResources(rs, preparedStatement, null);
					preparedStatement = conn.prepareStatement(PayRunConstants.SELECT_ALL_EMPLOYEES_BY_PAYRUN_ID);
					preparedStatement.setInt(1, payRunId);
					rs = preparedStatement.executeQuery();
					BigDecimal payrunTotal = new BigDecimal("0");
					BigDecimal payrunAdjustments = new BigDecimal("0");
					BigDecimal payrunDue = new BigDecimal("0");
					
					while (rs.next()) {
						int payRunEmpId = rs.getInt(1);
						int empId = rs.getInt(2);
						BigDecimal total = new BigDecimal("0");
						BigDecimal due = new BigDecimal("0");
						if (rs.getString(3) != null) {
							total = rs.getBigDecimal(3);
							total = total.setScale(2, BigDecimal.ROUND_HALF_EVEN);
						}
						payrunTotal=payrunTotal.add(total);
						BigDecimal adjustments = getTotalAdjustmentsForEmployeePayrunCurrency(payRunId, empId,
								currencyId);
						payrunAdjustments=payrunAdjustments.add(adjustments);
						due = total.subtract(adjustments);
						logger.info("inside updatePayRunBalance ,balanceDue:" + due.doubleValue() + ",empId:" + empId
								+ ",total:" + total.doubleValue());
						if (due.doubleValue() >= 0 && total.doubleValue() >= due.doubleValue()) {// Should not and not total be -value amount

						//	closeResources(null, preparedStatement, null);
							preparedStatement = conn.prepareStatement(PayRunConstants.UPDATE_PAYRUN_EMP_DUE_BALANCE);
							preparedStatement.setBigDecimal(1, due);
							preparedStatement.setInt(2, payRunEmpId);
							preparedStatement.executeUpdate();
							int rowAffected = preparedStatement.executeUpdate();
							if (rowAffected == 1) {
								result = true;
								logger.info("Successfully updated PayRunBalance !");
							}
							logger.info("After updatePayRunBalance ,empId:" + empId + ",balanceDue:" + due.doubleValue());
						} else {
							logger.info("Not able to update PayRunBalance, Balance due is greater than total");
						}

					}
					
					payrunDue=payrunTotal.subtract(payrunAdjustments);
					if(payrunDue.doubleValue()>= 0 && payrunTotal.doubleValue() >= payrunDue.doubleValue()) {
						if (payrunDue.compareTo(BigDecimal.ZERO) == 0) {
							status = CommonConstants.STATUS_AS_PAID;
						} else if ( payrunDue.compareTo(BigDecimal.ZERO) == 1 && payrunDue.compareTo(payrunTotal) == -1) {
							status = CommonConstants.STATUS_AS_PARTIALLY_PAID;
						}
						preparedStatement = conn.prepareStatement(PayRunConstants.UPDATE_PAYRUN_STATUS);
						preparedStatement.setString(1, status);
						preparedStatement.setInt(2, payRunId);
						preparedStatement.executeUpdate();
					}
					logger.info("Successfully updated PayRun status !"+status);
				}
				conn.commit();
			}
		} catch (Exception e) {
			logger.info("Error in updatePayRunBalance:: ", e);
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			throw new ApplicationException(e);
		} finally {
			closeResources(null, preparedStatement, conn);
		}
		return result;
	}
	
	public BigDecimal getTotalAdjustmentsForEmployeePayrunCurrency(int payRunId,int empId,int currencyId) throws ApplicationException {
		logger.info("Entry into in getTotalAdjustmentsForEmployeePayrunCurrency");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		BigDecimal total = new BigDecimal("0");
		try {
			// Get Total Amount Of VednorAdv Id From all Bill Payments
			con = getAccountsPayable();
			preparedStatement = con.prepareStatement(PayRunConstants.GET_ADJ_PAYRUN_FOR_EMP_CURRENCY);
			preparedStatement.setInt(1, payRunId);
			preparedStatement.setInt(2, empId);
			preparedStatement.setInt(3, currencyId);
			preparedStatement.setString(4, CommonConstants.STATUS_AS_ACTIVE);
			preparedStatement.setString(5, CommonConstants.STATUS_AS_ACTIVE);
			rs = preparedStatement.executeQuery();
			logger.info(preparedStatement.toString());
			while (rs.next()) {
				if (rs.getString(1) != null) {
					total = rs.getBigDecimal(1);
					total = total.setScale(2, BigDecimal.ROUND_HALF_EVEN);
				}
			}

			logger.info("getTotalAdjustmentsForEmployeePayrunCurrency : Fetched total amount:" + total
					+ ",For  payRunId:" + payRunId+" payRunId:" +empId+","+currencyId);
		} catch (Exception e) {
			logger.error("Error in getTotalAdjustmentsForEmployeePayrunCurrency", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return total;

	}

	public void updateCreditNoteBalance(Integer receiptId) {
		
		
	}

}
